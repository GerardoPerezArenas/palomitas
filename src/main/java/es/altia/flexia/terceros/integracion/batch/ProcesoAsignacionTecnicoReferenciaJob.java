/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.terceros.integracion.batch;

import es.altia.agora.business.util.GlobalNames;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.StringUtils;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author alberto.pulpeiro
 */
public class ProcesoAsignacionTecnicoReferenciaJob implements Job {

    private final static Logger LOG = Logger.getLogger(ProcesoAsignacionTecnicoReferenciaJob.class);

    //Para el fichero	de configuracion tecnico.
    protected static Config m_ConfigTechnical;
    protected static String gestorD;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {

        LOG.info("***********************************  ProcesoAsignacionTecnicoReferenciaJob ==============> ");
        try {
            //Obtenemos el gestor de BBDD
            m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
            Config configLanbide = ConfigServiceHelper.getConfig("Lanbide");
            gestorD = m_ConfigTechnical.getString("CON.gestor");
            //String servidior_cluster_ejecucion = m_ConfigTechnical.getString("servidor_ejecucion_quartz");
            String server = configLanbide.getString("SERVIDOR");

            // Obtenemos los detalles de la tarea a ejecutar
            JobDetail jd = jec.getJobDetail();

            // Obtenemos los parámetros
            //Si estamos en un entorno weblogic con servidor en cluster:
            LOG.debug("propiedad weblogic = "+ System.getProperty("weblogic.Name"));
            LOG.debug("nombre del servidor en Lanbide.properties propiedad SERVIDOR :" + server);
            if (("".equals(server)) || (server.equals(System.getProperty("weblogic.Name")))) {
                String jndis = (String) jd.getJobDataMap().get("jndis_lanzamiento_proceso");

                StringTokenizer tokens = new StringTokenizer(jndis, ";");
                while (tokens.hasMoreTokens()) {
                    String params[] = {gestorD, "", "", "", "", "", tokens.nextToken()};
                    proceso(params);
                }
            }

        } catch (Exception e) {
            LOG.error("Error " + e.getMessage());
            throw new JobExecutionException("Error interno");
        }
        LOG.info("***********************************  ProcesoAsignacionTecnicoReferenciaJob FIN ==============> ");
    }

    private void proceso(String[] params) {
        LOG.info("ejecución del proceso job");
        updateTecnicosTerceros(params);
        LOG.info("finalización del proceso job");
    }
    
        /**
     * Proceso de actualización de los técnicos de referencia
     * @param params 
     */
    private void updateTecnicosTerceros(String[] params) {

        LOG.debug("updateTecnicosTerceros");
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        List<String> ListaConsultas = new ArrayList<String>();
        List<String> ListaNIFDuplicados = new ArrayList<String>();
        try {
            LOG.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            LOG.debug("A por la conexion");

            conexion = abd.getConnection();

            String sqlCheckDuplicados = " SELECT numero_documento_persona FROM " + GlobalNames.ESQUEMA_GENERICO + "S_VW_OR_TECNICOS_REFERENCIA GROUP BY numero_documento_persona HAVING COUNT(*)>1 ";

            Statement stmtCheckDuplicados = conexion.createStatement();
            ResultSet rsCheckDuplicados = stmtCheckDuplicados.executeQuery(sqlCheckDuplicados);
            LOG.debug("updateTecnicosTerceros sqlCheckDuplicados :" + sqlCheckDuplicados);
            while (rsCheckDuplicados.next()) {
                ListaNIFDuplicados.add(rsCheckDuplicados.getString("numero_documento_persona"));
            }

            stmtCheckDuplicados.close();
            rsCheckDuplicados.close();
            informaNIFDuplicados(ListaNIFDuplicados);

            String sql = "SELECT TTER.TER_COD,TTER.TER_DOC,tCamposTexto.VALOR,TL.NUMERO_DOCUMENTO_TECNICA"
                    + " FROM T_TER TTER"
                    + " LEFT JOIN T_CAMPOS_TEXTO tCamposTexto ON TTER.TER_COD = tCamposTexto.COD_TERCERO and tCamposTexto.cod_campo = 'TTECNICOREFERENCIA'"
                    + " LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "S_VW_OR_TECNICOS_REFERENCIA TL ON TTER.TER_DOC = TL.NUMERO_DOCUMENTO_PERSONA"
                    + " WHERE tCamposTexto.VALOR <> TL.NUMERO_DOCUMENTO_TECNICA  OR (tcampostexto.valor is null and tl.numero_documento_tecnica is not null) OR (tcampostexto.valor is not null and tl.numero_documento_tecnica is null)";
            if (LOG.isDebugEnabled()) {
                LOG.debug("updateTecnicosTerceros sql :" + sql);
            }

            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // recorremos los resultados y gestionamos lo que debemos hacer con ellos.     
            updateTecnicos(rs, ListaNIFDuplicados, conexion, ListaConsultas);

            // cerramos la conexión de datos
            rs.close();
            stmt.close();

            if (!ListaConsultas.isEmpty()) {
                // ponemos el autoCommint a false por si se produce algún error 
                //conexion.setAutoCommit(Boolean.FALSE);
                
                // se decide ejecutar las consultas en transacciones diferentes.
                for (String consulta : ListaConsultas) {
                ejecutaConsultarActualizacionTecnicosReferencia(consulta, conexion);
                }
         
                // hacemos un commit de las actualizaciones
                //conexion.commit();
            }

        } catch (SQLException sqle) {
            LOG.error("Error de SQL en updateTecnicosTerceros: " + sqle.getMessage());
        } catch (BDException bde) {
            LOG.error("error del OAD en el metodo updateTecnicosTerceros: "
                    + bde.getMessage());
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch (BDException bde) {
                    LOG.error("No se pudo devolver la conexion: " + bde.getMessage());
                }
            }
        }
    }

    private void updateTecnicos(ResultSet rs, List<String> ListaNIFDuplicados, Connection conexion, List<String> ListaConsultas) throws SQLException {
        while (rs.next()) {
            // si está duplicado no lo actualizamos pero si lo sacamos en logs.
            if (!ListaNIFDuplicados.contains(rs.getString("TER_DOC"))) {
                // recuperamos valores del tercero que ha cambiado su técnico de referencia.
                int codTercero = rs.getInt("TER_COD");
                String nifTecnicoNuevo = rs.getString("NUMERO_DOCUMENTO_TECNICA");
                
                Statement stmtCheckExisteCampoTexto = conexion.createStatement();
                String sqlCheckExisteCampoTexto = "SELECT COUNT(*) AS NUM FROM T_CAMPOS_TEXTO WHERE COD_CAMPO = 'TTECNICOREFERENCIA' AND COD_TERCERO = " + codTercero + "";
                LOG.debug("sqlCheckExisteCampoTexto :" + sqlCheckExisteCampoTexto);
                ResultSet rsCheckExisteCampoTexto = stmtCheckExisteCampoTexto.executeQuery(sqlCheckExisteCampoTexto);
                if (rsCheckExisteCampoTexto.next()) {
                    if (StringUtils.isNotNullOrEmptyOrNullString(nifTecnicoNuevo)) {
                        ListaConsultas.add(rsCheckExisteCampoTexto.getInt("NUM") > 0 ? consultaUpdate(nifTecnicoNuevo, codTercero) : consultaInsert(nifTecnicoNuevo, codTercero));
                    } else {
                        ListaConsultas.add(consultaDelete(codTercero));
                    }
                }
                rsCheckExisteCampoTexto.close();
                stmtCheckExisteCampoTexto.close();
            }
        }
    }

    /**
     * Construye una consulta de Update sobre la tabla que guarda el técnico de
     * referencia de un tercero
     *
     * @param nifTecnicoNuevo
     * @param codTercero
     * @return
     */
    private String consultaUpdate(String nifTecnicoNuevo, int codTercero) {
        return "UPDATE T_CAMPOS_TEXTO SET VALOR = '" + nifTecnicoNuevo + "' WHERE COD_CAMPO = 'TTECNICOREFERENCIA' AND COD_MUNICIPIO = 0 AND COD_TERCERO = " + codTercero + "";

    }

    /**
     * Construye una consulta de insert sobre la tabla que guarda el técnico de
     * referencia de un tercero
     *
     * @param nifTecnicoNuevo
     * @param codTercero
     * @return
     */
    private String consultaInsert(String nifTecnicoNuevo, int codTercero) {
        return "INSERT INTO T_CAMPOS_TEXTO (COD_CAMPO,COD_MUNICIPIO,COD_TERCERO,VALOR) VALUES ('TTECNICOREFERENCIA',0," + codTercero + ",'" + nifTecnicoNuevo + "')";

    }

    /**
     * Construye una consulta de delete sobre la tabla que guarda el técnico de
     * referencia de un tercero
     *
     * @param nifTecnicoNuevo
     * @param codTercero
     * @return
     */
    private String consultaDelete(int codTercero) {
        return "DELETE FROM T_CAMPOS_TEXTO WHERE COD_CAMPO = 'TTECNICOREFERENCIA' AND COD_MUNICIPIO = 0 AND COD_TERCERO = " + codTercero + "";
    }

    /**
     * Ejecuta los insert/update/delete para la actualización de los técnicos de
     * referencia de forma secuencial, en difrentes transacciones.
     *
     * @param ListaConsultas
     * @param con
     * @throws SQLException
     */
    private void ejecutaConsultarActualizacionTecnicosReferencia(String consulta, Connection con) {
        LOG.info("Ejecutando ejecutaConsultarActualizacionTecnicosReferencia");
            try {
                PreparedStatement ps = null;
                LOG.debug("Ejecutando la consulta : " + consulta);
                ps = con.prepareStatement(consulta);
                ps.executeUpdate(consulta);
                ps.close();
                LOG.debug("actualizacion correcta.");
            } catch (SQLException ex) {
                LOG.error("Error de SQL en updateTecnicosTerceros: " + ex.getMessage());
            }
        }

    /**
     * Informa mediante logs de aquellos nifs que están repetidos en la tabla
     * S_VW_OR_TECNICOS_REFERENCIA, en caso de estar duplicados, su tecnico de
     * referencia NO se actualiza
     *
     * @param ListaNIFDuplicados
     */
    private void informaNIFDuplicados(List<String> ListaNIFDuplicados) {
        LOG.info(" NIFS DUPLICADOS EN TABLA S_VW_OR_TECNICOS_REFERENCIA DURANTE LA EJECUCIÓN DEL PROCESO JOB ProcesoAsignacionTecnicoReferenciaJob");
        for (String nif : ListaNIFDuplicados) {
            LOG.info("Terceros con documento : " + nif);
        }

    }
    

}
