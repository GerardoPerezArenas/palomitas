// NOMBRE DEL PAQUETE
package es.altia.agora.business.sge.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.PendientesPorSolicitanteValueObject;
import es.altia.common.exception.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.*;

import java.sql.*;

import java.util.Vector;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DefinicionTramitesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class PendientesPorSolicitanteDAO {

    private static PendientesPorSolicitanteDAO instance = null;
    protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log =
            LogFactory.getLog(PendientesPorSolicitanteDAO.class.getName());

    protected static String exp_mun;
    protected static String exp_pro;
    protected static String exp_eje;
    protected static String exp_num;
    protected static String exp_fei;
    protected static String exp_fef;
    protected static String exp_ter;
    protected static String exp_tnv;
    protected static String exp_est;

    protected static String ter_nom;
    protected static String ter_ap1;
    protected static String ter_pa1;
    protected static String ter_ap2;
    protected static String ter_pa2;
    protected static String ter_idn;

    protected static String exr_mun;
    protected static String exr_pro;
    protected static String exr_eje;
    protected static String exr_num;
    protected static String exr_der;
    protected static String exr_uor;
    protected static String exr_ejr;
    protected static String exr_nur;
    protected static String exr_tip;
    protected static String exr_ori;
    protected static String exr_top;

    protected PendientesPorSolicitanteDAO() {
        super();
        // Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");

        exp_mun = m_ConfigTechnical.getString("SQL.E_EXP.codMunicipio");
        exp_pro = m_ConfigTechnical.getString("SQL.E_EXP.codProcedimiento");
        exp_eje = m_ConfigTechnical.getString("SQL.E_EXP.ano");
        exp_num = m_ConfigTechnical.getString("SQL.E_EXP.numero");
        exp_fei = m_ConfigTechnical.getString("SQL.E_EXP.fechaInicio");
        exp_fef = m_ConfigTechnical.getString("SQL.E_EXP.fechaFin");
        exp_ter = m_ConfigTechnical.getString("SQL.E_EXP.tercero");
        exp_tnv = m_ConfigTechnical.getString("SQL.E_EXP.version");
        exp_est = m_ConfigTechnical.getString("SQL.E_EXP.estado");

        ter_nom = m_ConfigTechnical.getString("SQL.T_TER.nombre");
        ter_ap1 = m_ConfigTechnical.getString("SQL.T_TER.apellido1");
        ter_pa1 = m_ConfigTechnical.getString("SQL.T_TER.partApellido1");
        ter_ap2 = m_ConfigTechnical.getString("SQL.T_TER.apellido2");
        ter_pa2 = m_ConfigTechnical.getString("SQL.T_TER.partApellido2");
        ter_idn = m_ConfigTechnical.getString("SQL.T_TER.identificador");

        exr_mun = m_ConfigTechnical.getString("SQL.E_EXR.codMunicipio");
        exr_pro = m_ConfigTechnical.getString("SQL.E_EXR.codProcedimiento");
        exr_eje = m_ConfigTechnical.getString("SQL.E_EXR.ejercicio");
        exr_num = m_ConfigTechnical.getString("SQL.E_EXR.numero");
        exr_der = m_ConfigTechnical.getString("SQL.E_EXR.departamentoAnotacion");
        exr_uor = m_ConfigTechnical.getString("SQL.E_EXR.unidadAnotacion");
        exr_ejr = m_ConfigTechnical.getString("SQL.E_EXR.ejercicioAnotacion");
        exr_nur = m_ConfigTechnical.getString("SQL.E_EXR.numeroRegistro");
        exr_tip = m_ConfigTechnical.getString("SQL.E_EXR.tipoRegistro");
        exr_ori = m_ConfigTechnical.getString("SQL.E_EXR.origen");
        exr_top = m_ConfigTechnical.getString("SQL.E_EXR.tipoOperacion");

    }

    public static PendientesPorSolicitanteDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread)
            // Las invocaciones de este metodo
            synchronized(PendientesPorSolicitanteDAO.class) {
                if (instance == null) {
                    instance = new PendientesPorSolicitanteDAO();
                }
            }
        }
        return instance;
    }

    public Vector consultarP(PendientesPorSolicitanteValueObject pendSolVO,String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("consultar");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector list = new Vector();
        String identificadorTerc = pendSolVO.getIdentificadorTerc();
        String version = pendSolVO.getVersion();

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            from = exp_mun + ","+ exp_pro + "," + exp_eje + "," + exp_num + "," +
                    oad.convertir(exp_fei, oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS " + exp_fei +"," +
                    oad.convertir(exp_fef, oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS " +
                    exp_fef + "," + exp_ter + "," + exp_tnv + "," + exp_est + "," +
                    exr_ejr + "," + exr_nur;
            where = exp_ter + "=" + identificadorTerc + " AND " + exp_tnv + "=" + version;

            String join[] = new String[8];
            join[0] = "E_EXP";
            join[1] = "INNER";
            join[2] = "t_ter";
            join[3] = "e_exp." + exp_ter + "=t_ter." + ter_idn;
            join[4] = "LEFT";
            join[5] = "e_exr";
            join[6] = "e_exr." + exr_mun + "=" + exp_mun + " AND e_exr." + exr_pro + "=" + exp_pro +
                    " AND e_exr." + exr_eje + "=" + exp_eje + " AND e_exr." + exr_num + "=" +
                    exp_num;
            join[7] = "false";

            sql = oad.join(from,where,join);

            String parametros[] = {"2","2"};
            sql += oad.orderUnion(parametros);

            if(m_Log.isDebugEnabled()) m_Log.debug("PendientesPorSolicitanteDAO, consultar: Sentencia SQL:" + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                PendientesPorSolicitanteValueObject pSVO = new PendientesPorSolicitanteValueObject();
                String departamento = rs.getString(exp_mun);
                pSVO.setCodDepartamento(departamento);
                String procedimiento = rs.getString(exp_pro);
                pSVO.setCodProcedimiento(procedimiento);
                String ano = rs.getString(exp_eje);
                pSVO.setAno(ano);
                String numero = rs.getString(exp_num);
                pSVO.setNumero(numero);
                String fechaInicio = rs.getString(exp_fei);
                pSVO.setFechaInicio(fechaInicio);
                String fechaFin = rs.getString(exp_fef);
                pSVO.setFechaFin(fechaFin);
                identificadorTerc = rs.getString(exp_ter);
                pSVO.setIdentificadorTerc(identificadorTerc);
                version = rs.getString(exp_tnv);
                pSVO.setVersion(version);
                String estado = rs.getString(exp_est);
                pSVO.setEstado(estado);
                String ejercicioAnotacion = rs.getString(exr_ejr);
                String numeroAnotacion = rs.getString(exr_nur);
                String entrada = ejercicioAnotacion + "/" + numeroAnotacion;
                pSVO.setEntrada(entrada);
                if(fechaFin ==null) {
                    list.addElement(pSVO);
                }
            }
            m_Log.debug("PendientesPorSolicitanteDAO, consultar: Lista de consulta cargada");
            if(m_Log.isDebugEnabled()) m_Log.debug("PendientesPorSolicitanteDAO, consultar: Tamaño lista:" + list.size());
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.PendientesPorSolicitanteDAO.consultar"), e);
        }finally {
            try{
                if (st!=null) st.close();
                if (rs!=null) rs.close();
                if(con != null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("consultar");
        return list;
    }

    public Vector consultarF(PendientesPorSolicitanteValueObject pendSolVO,String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("consultar");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector list = new Vector();
        String identificadorTerc = pendSolVO.getIdentificadorTerc();
        String version = pendSolVO.getVersion();

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            from = exp_mun + ","+ exp_pro + "," + exp_eje + "," + exp_num + "," +
                    oad.convertir(exp_fei, oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS " + exp_fei +"," +
                    oad.convertir(exp_fef, oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS " +
                    exp_fef + "," + exp_ter + "," + exp_tnv + "," + exp_est + "," +
                    exr_ejr + "," + exr_nur;
            where = exp_ter + "=" + identificadorTerc + " AND " + exp_tnv + "=" + version;

            String join[] = new String[8];
            join[0] = "E_EXP";
            join[1] = "INNER";
            join[2] = "t_ter";
            join[3] = "e_exp." + exp_ter + "=t_ter." + ter_idn;
            join[4] = "LEFT";
            join[5] = "e_exr";
            join[6] = "e_exr." + exr_mun + "=" + exp_mun + " AND e_exr." + exr_pro + "=" + exp_pro +
                    " AND e_exr." + exr_eje + "=" + exp_eje + " AND e_exr." + exr_num + "=" +
                    exp_num;
            join[7] = "false";

            sql = oad.join(from,where,join);

            String parametros[] = {"2","2"};
            sql += oad.orderUnion(parametros);

            if(m_Log.isDebugEnabled()) m_Log.debug("PendientesPorSolicitanteDAO, consultar: Sentencia SQL:" + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                PendientesPorSolicitanteValueObject pSVO = new PendientesPorSolicitanteValueObject();
                String departamento = rs.getString(exp_mun);
                pSVO.setCodDepartamento(departamento);
                String procedimiento = rs.getString(exp_pro);
                pSVO.setCodProcedimiento(procedimiento);
                String ano = rs.getString(exp_eje);
                pSVO.setAno(ano);
                String numero = rs.getString(exp_num);
                pSVO.setNumero(numero);
                String fechaInicio = rs.getString(exp_fei);
                pSVO.setFechaInicio(fechaInicio);
                String fechaFin = rs.getString(exp_fef);
                pSVO.setFechaFin(fechaFin);
                identificadorTerc = rs.getString(exp_ter);
                pSVO.setIdentificadorTerc(identificadorTerc);
                version = rs.getString(exp_tnv);
                pSVO.setVersion(version);
                String estado = rs.getString(exp_est);
                pSVO.setEstado(estado);
                String ejercicioAnotacion = rs.getString(exr_ejr);
                String numeroAnotacion = rs.getString(exr_nur);
                String entrada = ejercicioAnotacion + "/" + numeroAnotacion;
                pSVO.setEntrada(entrada);
                if(fechaFin !=null) {
                    list.addElement(pSVO);
                }
            }
            m_Log.debug("PendientesPorSolicitanteDAO, consultar: Lista de consulta cargada");
            if(m_Log.isDebugEnabled()) m_Log.debug("PendientesPorSolicitanteDAO, consultar: Tamaño lista:" + list.size());
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.PendientesPorSolicitanteDAO.consultar"), e);
        }finally {
            try{
                if (st!=null) st.close();
                if (rs!=null) rs.close();
                if(con != null) con.close();
            } catch(SQLException sqle) {
                sqle.printStackTrace();
                m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("consultar");
        return list;
    }
}