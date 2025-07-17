package es.altia.agora.business.gestionInformes.persistence.manual;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.agora.business.gestionInformes.*;
import es.altia.agora.business.gestionInformes.tareas.AsistenteEstado;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Vector;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author sin atribuir
 * @version 1.0
 */

public class SolicitudesInformesDAO {
   //Para el fichero de configuracion tecnico.
   protected static Config conf;
   //Para informacion de logs.
   protected static Log m_Log =
            LogFactory.getLog(SolicitudesInformesDAO.class.getName());


   private static SolicitudesInformesDAO instance = null;

   protected SolicitudesInformesDAO() {
	super();
	//Queremos usar el fichero de configuracion techserver
       conf = ConfigServiceHelper.getConfig("techserver");
          //Conexion
   }

   public static SolicitudesInformesDAO getInstance() {
	//si no hay ninguna instancia de esta clase tenemos que crear una.
	if (instance == null) {
	   // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
	   synchronized(SolicitudesInformesDAO.class){
		if (instance == null)
		   instance = new SolicitudesInformesDAO();
	   }
	}
	return instance;
   }

    public Vector getListaSolicitudes(UsuarioValueObject usuario, String[] params, String origen){
     Vector r = new Vector();
     AdaptadorSQLBD abd = null;
     Connection conexion = null;
     try{
        abd = new AdaptadorSQLBD(params);
        conexion = abd.getConnection();
        String sql = "SELECT " + conf.getString("SQL.INF_SOLICITUD.cod") + "," +
                                 conf.getString("SQL.INF_SOLICITUD.descripcion") + ", " +
                                 conf.getString("SQL.PLANT_INFORMES.proced") + ", " +
                                 conf.getString("SQL.INF_SOLICITUD.estado") + ", " +
                                 conf.getString("SQL.INF_ORIGEN.desc") + ", " +
                                 abd.convertir(conf.getString("SQL.INF_SOLICITUD.fechaGen") ,
                                         AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")+
                                
                     " FROM INF_SOLICITUD" +
                     " INNER JOIN PLANT_INFORMES ON (" + conf.getString("SQL.INF_SOLICITUD.plantilla") + "=" + conf.getString("SQL.PLANT_INFORMES.cod") + ")" +
                     " INNER JOIN INF_ORIGEN ON (" + conf.getString("SQL.PLANT_INFORMES.origen") + "=" + conf.getString("SQL.INF_ORIGEN.cod") + ")" +
                     " WHERE " + conf.getString("SQL.INF_SOLICITUD.plantilla") + " IN (SELECT COD_PLANTILLA FROM "+ GlobalNames.ESQUEMA_GENERICO +
                     " A_UOU UOR, PLANT_INF_UOR WHERE UOU_UOR=PLANT_INF_UOR_ID and PLANT_INF_UOR_PLANTILLA=COD_PLANTILLA AND UOR.UOU_USU = " +
                     usuario.getIdUsuario() + ")";
        if (origen != null && !origen.equals("")) sql += " AND " + conf.getString("SQL.INF_ORIGEN.cod") + " = " + origen;
        if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + sql);
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
         SolicitudesListaValueObject solVO = new SolicitudesListaValueObject();
         solVO.setCodigo(rs.getString(1));
         solVO.setTitulo(rs.getString(2));
         solVO.setCodProcedimiento(rs.getString(3)!=null?rs.getString(3):"");
         int estado = rs.getInt(4);
         switch (estado) {
            case AsistenteEstado.ESTADO_EN_PROCESO:  solVO.setEstado("En ejecución"); break;
            case AsistenteEstado.ESTADO_FINALIZADO:  solVO.setEstado("Finalizado"); break;
            case AsistenteEstado.ESTADO_ERROR:  solVO.setEstado("No generado"); break;
            case AsistenteEstado.ESTADO_FINALIZADO_SIN_DATOS:  solVO.setEstado("Sin datos"); break;
            default: solVO.setEstado("En espera");break;
         }
         solVO.setOrigen(rs.getString(5));
         solVO.setFecha(rs.getString(6));

         r.add(solVO);
        }
        rs.close();
        stmt.close();

     } catch (SQLException sqle){
        m_Log.error("Error de SQL en getListaSolicitudes: " + sqle.toString());
     }
     catch (BDException bde){
        m_Log.error("error del OAD en el metodo getListaSolicitudes: " +
                     bde.toString());
     }
     finally {
        if (conexion != null){
         try{
            abd.devolverConexion(conexion);
         }
         catch(BDException bde){
            m_Log.error("No se pudo devolver la conexion: " + bde.toString());
         }
        }
     }
     return r;
    }

    /**
     * Recupera los datos de una solicitud
     * @param params: Parámetros de conexión a la base de datos
     * @param codSolicitud: Código de la solicitud
     * @param idioma: Código del idioma actual del usuaario
     * @return SolicitudInformeValueObject con los datos de la solicitud
     * @throws java.lang.Exception si ocurre algún error
     */
    public SolicitudInformeValueObject obtenerSolicitud(String[] params, String codSolicitud) throws Exception{
    	SolicitudInformeValueObject solicitudInformeVO = new SolicitudInformeValueObject();
        CriteriosSolicitudValueObject criteriosSolicitudVO = new CriteriosSolicitudValueObject();
        Vector criterios = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt,stmt2;
        ResultSet rs,rs2;
        String sql,sql2;

        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            sql = "SELECT " + conf.getString("SQL.INF_SOLICITUD.usuario") + "," +
                conf.getString("SQL.INF_SOLICITUD.plantilla") + "," +
                conf.getString("SQL.INF_SOLICITUD.descripcion") + "," +
                abd.convertir(conf.getString("SQL.INF_SOLICITUD.fechaSol"),AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") + "," +
                abd.convertir(conf.getString("SQL.INF_SOLICITUD.fechaGen"),AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") + "," +
                conf.getString("SQL.INF_SOLICITUD.tiempo") + "," +
                conf.getString("SQL.INF_SOLICITUD.estado") + "," +
                conf.getString("SQL.INF_SOLICITUD.formato") + "," +
                conf.getString("SQL.INF_SOLICITUD.fichero") + "," +
                conf.getString("SQL.PLANT_INFORMES.proced") + "," +
                conf.getString("SQL.A_USU.nombre") + "," +
                conf.getString("SQL.INF_ORIGEN.desc") +
                " FROM INF_SOLICITUD " +
                " INNER JOIN PLANT_INFORMES ON (" + conf.getString("SQL.INF_SOLICITUD.plantilla") + "=" + conf.getString("SQL.PLANT_INFORMES.cod") + ")" +
                " INNER JOIN INF_ORIGEN ON (" + conf.getString("SQL.PLANT_INFORMES.origen") + "=" + conf.getString("SQL.INF_ORIGEN.cod") + ")" +
                " INNER JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_USU" + " ON (" + conf.getString("SQL.A_USU.codigo") + "=" + conf.getString("SQL.INF_SOLICITUD.usuario") + ")" +
                " WHERE " + conf.getString("SQL.INF_SOLICITUD.cod") + "=" + codSolicitud;
            if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                solicitudInformeVO.setCodSolicitud(codSolicitud);
                solicitudInformeVO.setCodUsuario(rs.getString(1));
                solicitudInformeVO.setCodPlantilla(rs.getString(2));
                solicitudInformeVO.setDescripcion(rs.getString(3));
                solicitudInformeVO.setFechaSolicitud(rs.getString(4));
                solicitudInformeVO.setFechaGeneracion(rs.getString(5));
                double tiempo = rs.getDouble(6);
                tiempo = tiempo / 1000;
                solicitudInformeVO.setTiempo(String.valueOf(tiempo)+ " seg");
                int estado = rs.getInt(7);
                switch (estado) {
                   case AsistenteEstado.ESTADO_EN_PROCESO:  solicitudInformeVO.setEstado("En ejecución"); break;
                   case AsistenteEstado.ESTADO_FINALIZADO:  solicitudInformeVO.setEstado("Finalizado"); break;
                   case AsistenteEstado.ESTADO_ERROR:  solicitudInformeVO.setEstado("No generado"); break;
                   case AsistenteEstado.ESTADO_FINALIZADO_SIN_DATOS:  solicitudInformeVO.setEstado("Sin datos"); break;
                   default: solicitudInformeVO.setEstado("En espera");break;
                }
                int formato = rs.getInt(8);
                switch (formato) {
                   case 1:  solicitudInformeVO.setFormato("HTML"); break;
                   case 2:  solicitudInformeVO.setFormato("EXCEL"); break;
                   case 3:  solicitudInformeVO.setFormato("CSV"); break;
                   default: solicitudInformeVO.setFormato("PDF");break;
                }
                solicitudInformeVO.setFichero(rs.getString(9));
                solicitudInformeVO.setProcedimiento(rs.getString(10));
                solicitudInformeVO.setUsuario(rs.getString(11));
                solicitudInformeVO.setOrigen(rs.getString(12));
                stmt2 = conexion.createStatement();
                sql2 = "SELECT " + conf.getString("SQL.INF_CRITERIOS.titulo") + "," +
                    conf.getString("SQL.INF_CRITERIOS.condicion") + "," +
                    conf.getString("SQL.INF_CRITERIOS.valor1") + "," +
                    conf.getString("SQL.INF_CRITERIOS.valor2") +
                    " FROM INF_CRITERIOS " +
                    " WHERE " + conf.getString("SQL.INF_CRITERIOS.solicitud") + "=" + codSolicitud;
                if(m_Log.isDebugEnabled()) m_Log.debug("sql2: " + sql2);
                rs2 = stmt2.executeQuery(sql2);
                String cad = "";
                while (rs2.next()) {
                    int condicion = rs2.getInt(2);
                    switch (condicion) {
                        case 0:  cad += " - " + rs2.getString(1) + " > " + rs2.getString(3) + "\n"  ; break;
                        case 1:  cad += " - " + rs2.getString(1) + " >= " + rs2.getString(3) + "\n"  ; break;
                        case 2:  cad += " - " + rs2.getString(1) + " < " + rs2.getString(3) + "\n"  ; break;
                        case 3:  cad += " - " + rs2.getString(1) + " <= " + rs2.getString(3) + "\n"  ; break;
                        case 4:  cad += " - " + rs2.getString(1) + " entre " + rs2.getString(3) + " Y " + rs2.getString(4) + "\n"  ; break;
                        case 5:  cad += " - " + rs2.getString(1) + " = " + rs2.getString(3) + "\n"  ; break;
                        case 6:  cad += " - " + rs2.getString(1) + " <> " + rs2.getString(3) + "\n"  ; break;
                        case 7:  cad += " - " + rs2.getString(1) + " LIKE " + rs2.getString(3) + "\n"  ; break;
                        case 8:  cad += " - " + rs2.getString(1) + " IS NULL" + "\n"  ; break;
                        case 9:  cad += " - " + rs2.getString(1) + " IS NOT NULL" + "\n"  ; break;                        
                        

                    }
                }
                solicitudInformeVO.setCadenaCriterios(cad);
                m_Log.debug("CADENA CRITERIOS : " + cad);
                rs2.close();
                stmt2.close();
            }
            rs.close();
            stmt.close();

        } catch (SQLException sqle) {
            m_Log.error("Error de SQL en getSolicitud: " + sqle.getMessage());
            throw new Exception(sqle);
        } catch (BDException bde) {
            m_Log.error("Error del OAD en el metodo getSolicitud: " + bde.getMessage());
            throw new Exception(bde);
        } finally {
            if (conexion != null){
                try{
                    abd.devolverConexion(conexion);
                } catch(BDException bde){
                    m_Log.error("No se pudo devolver la conexion: " + bde.getMessage());
                }
            }
        }
        return solicitudInformeVO;
    }

    public long grabarSolicitud (SolicitudInformeValueObject siVO, String[] params) throws Exception {
        long resul = -1;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String sql = "";
        ResultSet rs = null;
        Statement stmt = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            long solicitud=1;
            abd = new AdaptadorSQLBD(params);
            //conexion = abd.getConnection();
            sql="SELECT MAX(" + conf.getString("SQL.INF_SOLICITUD.cod") + ") FROM INF_SOLICITUD";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                solicitud = rs.getInt(1)+1;
            }
            rs.close();
            stmt.close();
            m_Log.debug("SOLICITUD "+solicitud);
            sql ="INSERT INTO INF_SOLICITUD ("+ conf.getString("SQL.INF_SOLICITUD.cod") +", " +
                                                conf.getString("SQL.INF_SOLICITUD.plantilla") +", " +
                                                conf.getString("SQL.INF_SOLICITUD.usuario") + ", " +
                                                conf.getString("SQL.INF_SOLICITUD.fechaSol") + ", " +
                                                conf.getString("SQL.INF_SOLICITUD.estado") + ", " +
                                                conf.getString("SQL.INF_SOLICITUD.tiempo") + ", " +
                                                conf.getString("SQL.INF_SOLICITUD.descripcion") + ", " +
                                                conf.getString("SQL.INF_SOLICITUD.formato") + ") VALUES (" +
                                                solicitud + "," +
                                                siVO.getCodPlantilla() + ", " +
                                                siVO.getCodUsuario() + ", " +
                                                abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", " +
                                                "0, " +
                                                "0,'" +
                                                siVO.getDescripcion() + "', " +
                                                siVO.getFormato() + ")";
            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();

            Vector listaCriterios = siVO.getListaCriterios();
            for (int i=0; i<listaCriterios.size(); i++) {
                CriteriosSolicitudValueObject criterio = (CriteriosSolicitudValueObject) listaCriterios.get(i);
                sql ="INSERT INTO INF_CRITERIOS (" + conf.getString("SQL.INF_CRITERIOS.solicitud") + "," +
                                                     conf.getString("SQL.INF_CRITERIOS.criterio") + "," +
                                                     conf.getString("SQL.INF_CRITERIOS.tipo") + "," +
                                                     conf.getString("SQL.INF_CRITERIOS.condicion") + "," +
                                                     conf.getString("SQL.INF_CRITERIOS.valor1") + "," +
                                                     conf.getString("SQL.INF_CRITERIOS.valor2") + "," +
                                                     conf.getString("SQL.INF_CRITERIOS.titulo") + "," +
                                                     conf.getString("SQL.INF_CRITERIOS.origen") + "," +
                                                     conf.getString("SQL.INF_CRITERIOS.tabla")+") VALUES (" +
                                                     solicitud +"," +
                                                     (i+1) +"," +
                                                     criterio.getCampo() +"," +
                                                     criterio.getCondicion() + ",'" +
                                                     criterio.getValor1() + "','" +
                                                     criterio.getValor2() + "','" +
                                                     criterio.getTitulo() + "','" +
                                                     criterio.getOrigen() + "','" +
                                                     criterio.getTabla() + "')";
                stmt = conexion.createStatement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
                stmt.close();
            }
            resul=solicitud;
            abd.finTransaccion(conexion);
            siVO.setCodSolicitud(String.valueOf(solicitud));
        } catch (SQLException sqle) {
            abd.rollBack(conexion);
            m_Log.error("Error de SQL en grabarSolicitud: " + sqle.toString());
            resul=-1;
            throw new Exception(sqle);
        } catch (BDException bde) {
            abd.rollBack(conexion);
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo grabarSolicitud: " + bde.toString());
            resul=-1;
            throw new Exception(bde);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde.toString());
                }
            }
        }
        return resul;
    }

    public void anotaInicioSolicitud(String[] params, String codSolicitud, int estado) throws Exception {
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String sql;
        Statement stmt;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            sql = "UPDATE INF_SOLICITUD SET " + conf.getString("SQL.INF_SOLICITUD.estado") + "=" + estado +
                  " WHERE " + conf.getString("SQL.INF_SOLICITUD.cod") + "=" + codSolicitud;

            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();

            abd.finTransaccion(conexion);

        } catch (SQLException sqle) {
            abd.rollBack(conexion);
            m_Log.error("Error de SQL en anotaInicioSolicitud: " + sqle.getMessage());
            throw new Exception(sqle);
        } catch (BDException bde) {
            abd.rollBack(conexion);
            if(m_Log.isErrorEnabled()) m_Log.error("Error del OAD en el metodo anotaInicioSolicitud: " + bde.getMessage());
            throw new Exception(bde);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde.getMessage());
                }
            }
        }
    }

    public void anotaFinSolicitud(String[] params, String codSolicitud, long tiempo, String fichero, int estado) throws Exception {
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String sql;
        Statement stmt;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            sql = "UPDATE INF_SOLICITUD SET " + conf.getString("SQL.INF_SOLICITUD.tiempo") + "=" + tiempo + "," +
                    conf.getString("SQL.INF_SOLICITUD.fichero") + "='" + fichero + "'," +
                    conf.getString("SQL.INF_SOLICITUD.fechaGen") + "=" + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + "," +
                    conf.getString("SQL.INF_SOLICITUD.estado") + "=" + estado +
                  " WHERE " + conf.getString("SQL.INF_SOLICITUD.cod") + "=" + codSolicitud;

            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();

            abd.finTransaccion(conexion);

        } catch (SQLException sqle) {
            abd.rollBack(conexion);
            m_Log.error("Error de SQL en anotaFinSolicitud: " + sqle.getMessage());
            throw new Exception(sqle);
        } catch (BDException bde) {
            abd.rollBack(conexion);
            if(m_Log.isErrorEnabled()) m_Log.error("Error del OAD en el metodo anotaFinSolicitud: " + bde.getMessage());
            throw new Exception(bde);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde.getMessage());
                }
            }
        }
    }

    public void eliminarSolicitud(String[] params, String codSolicitud) throws Exception {
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String sql;
        Statement stmt;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            sql = "DELETE FROM INF_CRITERIOS WHERE " + conf.getString("SQL.INF_CRITERIOS.solicitud") + "=" + codSolicitud;

            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();

            sql = "DELETE FROM INF_SOLICITUD WHERE " + conf.getString("SQL.INF_SOLICITUD.cod") + "=" + codSolicitud;

            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();

            abd.finTransaccion(conexion);

        } catch (SQLException sqle) {
            abd.rollBack(conexion);
            m_Log.error("Error de SQL en eliminarSolicitud: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new Exception(sqle);
        } catch (BDException bde) {
            abd.rollBack(conexion);
            if(m_Log.isErrorEnabled()) m_Log.error("Error del OAD en el metodo eliminarSolicitud: " + bde.getMessage());
            bde.printStackTrace();
            throw new Exception(bde);
        } catch (Exception e) {
            abd.rollBack(conexion);
            if(m_Log.isErrorEnabled()) m_Log.error("Error del OAD en el metodo eliminarSolicitud: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde.getMessage());
                }
            }
        }
    }

    /**
     * Convierte el criterio a una cadena legible
     * 
     * @param condicion
     * @param inicioLinea
     * @param finLinea
     * @param campo
     * @param valores
     * @return 
     */
    public String criterioSolicitudToString(int condicion, String inicioLinea, String finLinea, String campo, String... valores) {
        StringBuilder cadena = new StringBuilder();

        // Se anade el comienzo de la cadena
        if (condicion >= 0 && condicion <= 9) {
            cadena.append(inicioLinea).append(campo);
        }
        
        switch (condicion) {
            case 0:
                cadena.append(" > ").append(valores[0]);
                break;
            case 1:
                cadena.append(" >= ").append(valores[0]);
                break;
            case 2:
                cadena.append(" < ").append(valores[0]);
                break;
            case 3:
                cadena.append(" <= ").append(valores[0]);
                break;
            case 4:
                cadena.append(" entre ").append(valores[0]).append(" Y ").append(valores[1]);
                break;
            case 5:
                cadena.append(" = ").append(valores[0]);
                break;
            case 6:
                cadena.append(" <> ").append(valores[0]);
                break;
            case 7:
                cadena.append(" LIKE ").append(valores[0]);
                break;
            case 8:
                cadena.append(" IS NULL");
                break;
            case 9:
                cadena.append(" IS NOT NULL");
                break;
        }

        // Se anade el separador
        if (condicion >= 0 && condicion <= 9) {
            cadena.append(finLinea);
        }
        
        return cadena.toString();
    }
}
