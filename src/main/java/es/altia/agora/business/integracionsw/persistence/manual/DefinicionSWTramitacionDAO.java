package es.altia.agora.business.integracionsw.persistence.manual;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Iterator;
import java.util.Vector;

import es.altia.agora.business.integracionsw.AvanzarRetrocederSWVO;
import es.altia.agora.business.integracionsw.InfoConfTramSWVO;
import es.altia.agora.business.integracionsw.ParametroConfigurableVO;
import es.altia.agora.business.integracionsw.persistence.DefinicionOperacionesSWManager;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.GeneralOperations;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.agora.business.gestionInformes.persistence.CampoValueObject;
import es.altia.agora.business.sge.persistence.manual.TramitacionExpedientesDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.manual.ModuloIntegracionExternoDAO;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefinicionSWTramitacionDAO {

    protected static Log m_Log = LogFactory.getLog(DefinicionSWTramitacionDAO.class.getName());

    private static DefinicionSWTramitacionDAO instance = null;

    protected DefinicionSWTramitacionDAO() {
    }

    public static DefinicionSWTramitacionDAO getInstance() {
        if (instance == null) {
            synchronized (DefinicionSWTramitacionDAO.class) {
                if (instance == null) instance = new DefinicionSWTramitacionDAO();
            }
        }
        return instance;
    }

    public boolean existeOperacionAsociada(int codigoOp, String[] params) throws TechnicalException, InternalErrorException {

        String sqlExisteOp = "SELECT * FROM DEF_TRA_SW WHERE DEF_TRA_OP = ? AND DEF_TRA_TIPO_ORIGEN_OPERACION='WS'";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlExisteOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA COMPROBAR SI UNA OPERACION ESTA ASOCIADA A " +
                    "ALGUN TRAMITE");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlExisteOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Operacion: " + codigoOp);

            ps.setInt(1, codigoOp);

            rs = ps.executeQuery();

            return rs.next();

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA COMPROBAR SI UNA OPERACION ESTA ASOCIADA A" +
                    " ALGUN TRAMITE");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA COMPROBAR SI UNA OPERACION ESTA " +
                    "ASOCIADA A ALGUN TRAMITE", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA COMPROBAR SI UNA OPERACION ESTA ASOCIADA A ALGUN TRAMITE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA COMPROBAR SI UNA OPERACION ESTA ASOCIADA A " +
                    "ALGUN TRAMITE", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }





    public InfoConfTramSWVO getInfoOperacionModulo(long cfo, Connection con)
            throws TechnicalException, InternalErrorException {

        String sqlGetCodigoAvz = "SELECT DEF_TRA_OP, DEF_TRA_OBL, DEF_TRA_NOMBRE_OPERACION,DEF_TRA_TIPO_ORIGEN_OPERACION, DEF_TRA_NOMBRE_MODULO " +
                                 "FROM DEF_TRA_SW WHERE DEF_TRA_CFO = " + cfo + " AND DEF_TRA_TIPO_ORIGEN_OPERACION='MODULO'";

        m_Log.debug(sqlGetCodigoAvz);
        Statement st = null;
        ResultSet rs = null;

        try {
            st = con.createStatement();
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER EL CODIGO DE LA OPERACION DEL SERVICIO" +
                    " WEB ASIGNADA AL TRAMITE");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetCodigoAvz);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo sw: " + cfo);
 
                        rs = st.executeQuery(sqlGetCodigoAvz);

            InfoConfTramSWVO infoServicioWeb = null;
            if (rs.next()) {
                infoServicioWeb = new InfoConfTramSWVO();
                infoServicioWeb.setCodOpSW(rs.getInt("DEF_TRA_OP"));
                infoServicioWeb.setObligatorio(rs.getBoolean("DEF_TRA_OBL"));
                infoServicioWeb.setTituloOperacion(rs.getString("DEF_TRA_NOMBRE_OPERACION"));                
                infoServicioWeb.setNombreModulo(rs.getString("DEF_TRA_NOMBRE_MODULO"));
            }

            return infoServicioWeb;

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER EL CODIGO DE LA OPERACION DEL SERVICIO WEB ASIGNADA AL" +
                    " TRAMITE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER EL CODIGO DE LA OPERACION DEL SERVICIO" +
                    " WEB ASIGNADA AL TRAMITE", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(st);
        }
    }




    public InfoConfTramSWVO[] getInfoSWTramite(AvanzarRetrocederSWVO avRet, String[] params)
            throws TechnicalException, InternalErrorException {

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        InfoConfTramSWVO[] codigosArray = new InfoConfTramSWVO[3];

        try {
            con = abd.getConnection();            
            codigosArray[0] = getInfoSW(avRet.getCfoAvanzar(), con);
            codigosArray[1] = getInfoSW(avRet.getCfoRetroceder(), con);
            codigosArray[2] = getInfoSW(avRet.getCfoIniciar(),con);

            if(codigosArray[0]!=null){
                // Es una operación de un servicio web
                codigosArray[0].setOperacionModulo(false);
            }else{
                // Se comprueba si es una operación de un módulo de integración
                codigosArray[0] = getInfoOperacionModulo(avRet.getCfoAvanzar(), con);
                if(codigosArray[0]!=null) codigosArray[0].setOperacionModulo(true);
            }

            if(codigosArray[1]!=null){
                // Es una operación de un servicio web
                codigosArray[1].setOperacionModulo(false);
            }else{
                // Se comprueba si es una operación de un módulo de integración
                codigosArray[1] = getInfoOperacionModulo(avRet.getCfoRetroceder(), con);
                if(codigosArray[1]!=null) codigosArray[1].setOperacionModulo(true);
            }

            if(codigosArray[2]!=null){
                // Es una operación de un servicio web
                codigosArray[2].setOperacionModulo(false);
            }else{
                // Se comprueba si es una operación de un módulo de integración
                codigosArray[2] = getInfoOperacionModulo(avRet.getCfoIniciar(), con);
                if(codigosArray[2]!=null) codigosArray[2].setOperacionModulo(true);
            }

            
        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR LOS CODIGOS DE LAS OPERACIONES DEL" +
                    " SERVICIO WEB ASOCIADAS A UN TRAMITE");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR LOS CODIGOS DE LAS" +
                    " OPERACIONES DEL SERVICIO WEB ASOCIADAS A UN TRAMITE", bde);
        } finally {
            devolverConexion(abd, con);
        }
        DefinicionOperacionesSWManager defOpSWManager = DefinicionOperacionesSWManager.getInstance();
        if (codigosArray[0] != null && !codigosArray[0].isOperacionModulo()) {
            codigosArray[0].setListaParamsEntrada(defOpSWManager.getParamsEntrada(avRet.getCfoAvanzar(), avRet.getCodAvanzar(), params));
            codigosArray[0].setListaParamsSalida(defOpSWManager.getParamsSalida(avRet.getCfoAvanzar(), avRet.getCodAvanzar(), params));
        }
        if (codigosArray[1] != null && !codigosArray[1].isOperacionModulo()) {
            codigosArray[1].setListaParamsEntrada(defOpSWManager.getParamsEntrada(avRet.getCfoRetroceder(), avRet.getCodRetroceder(), params));
            codigosArray[1].setListaParamsSalida(defOpSWManager.getParamsSalida(avRet.getCfoRetroceder(), avRet.getCodRetroceder(), params));
        }
        if (codigosArray[2] != null && !codigosArray[2].isOperacionModulo()) {
            codigosArray[2].setListaParamsEntrada(defOpSWManager.getParamsEntrada(avRet.getCfoIniciar(), avRet.getCodIniciar(), params));
            codigosArray[2].setListaParamsSalida(defOpSWManager.getParamsSalida(avRet.getCfoIniciar(), avRet.getCodIniciar(), params));
        }


        return codigosArray;
    }

//    public void creaActualizaOpsTramite(DefinicionTramitesValueObject defTramVO, String[] params)
//            throws TechnicalException, InternalErrorException {
//
//        int codMunicipio = Integer.parseInt(defTramVO.getCodMunicipio());
//        String codProcedimiento = defTramVO.getTxtCodigo();
//        int codTramite = Integer.parseInt(defTramVO.getCodigoTramite());
//        InfoConfTramSWVO newInfoSWAvanzar = defTramVO.getInfoSWAvanzar();
//        InfoConfTramSWVO newInfoSWRetroceder = defTramVO.getInfoSWRetroceder();
//
//        m_Log.debug(newInfoSWAvanzar);
//        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
//        Connection con = null;
//
//        try {
//            con = abd.getConnection();
//            abd.inicioTransaccion(con);
//
//            InfoConfTramSWVO oldInfoSWAvanzar = getInfoSW(codMunicipio, codProcedimiento, codTramite, true, con);
//            if (newInfoSWAvanzar != null || oldInfoSWAvanzar != null) {
//                if (newInfoSWAvanzar == null) {
//                    eliminarOpsTramite(codMunicipio, codProcedimiento, codTramite, true, con);
//                    eliminarListaParams(codMunicipio, codProcedimiento, codTramite, true, con);
//                } else if (oldInfoSWAvanzar == null) {
//                    crearOpsTramite(codMunicipio, codProcedimiento, codTramite, true, newInfoSWAvanzar, con);
//                    crearListaParams(codMunicipio, codProcedimiento, codTramite, true, newInfoSWAvanzar, con);
//                } else {
//                    updateOpsTramite(codMunicipio, codProcedimiento, codTramite, true, newInfoSWAvanzar, con);
//                    eliminarListaParams(codMunicipio, codProcedimiento, codTramite, true, con);
//                    crearListaParams(codMunicipio, codProcedimiento, codTramite, true, newInfoSWAvanzar, con);
//                }
//            }
//
//            InfoConfTramSWVO oldInfoSWRetroceder = getInfoSW(codMunicipio, codProcedimiento, codTramite, false, con);
//            if (newInfoSWRetroceder != null || oldInfoSWRetroceder != null) {
//                if (newInfoSWRetroceder == null) {
//                    eliminarOpsTramite(codMunicipio, codProcedimiento, codTramite, false, con);
//                    eliminarListaParams(codMunicipio, codProcedimiento, codTramite, false, con);
//                } else if (oldInfoSWRetroceder == null) {
//                    crearOpsTramite(codMunicipio, codProcedimiento, codTramite, false, newInfoSWRetroceder, con);
//                    crearListaParams(codMunicipio, codProcedimiento, codTramite, false, newInfoSWRetroceder, con);
//                } else {
//                    updateOpsTramite(codMunicipio, codProcedimiento, codTramite, false, newInfoSWRetroceder, con);
//                    eliminarListaParams(codMunicipio, codProcedimiento, codTramite, false, con);
//                    crearListaParams(codMunicipio, codProcedimiento, codTramite, false, newInfoSWRetroceder, con);
//                }
//            }
//
//        } catch (BDException bde) {
//            rollBackTransaction(abd, con);
//            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA ACTUALIZAR O ASOCIAR OPERACIONES A TRAMITES");
//            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA ACTUALIZAR O ASOCIAR OPERACIONES" +
//                    " A TRAMITES", bde);
//        } finally {
//            commitTransaction(abd, con);
//            devolverConexion(abd, con);
//        }
//    }

    private void eliminarOpsTramite(int codMunicipio, String codProcedimiento, int codTramite, boolean avanzar, Connection con)
            throws TechnicalException, InternalErrorException {

        String sqlEliminarTraOp = "DELETE DEF_TRA_SW WHERE DEF_TRA_MUN = ? AND DEF_TRA_PRO = ? AND DEF_COD_TRA = ? " +
                "AND DEF_TRA_AVZ = ?";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sqlEliminarTraOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ELIMINAR LA ASOCIACION ENTRE UN OP DE SERVICIO" +
                    " WEB Y UN TRAMITE");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlEliminarTraOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de Procedimiento: " + codProcedimiento);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo de Tramite: " + codTramite);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Avanzar o Retroceder?: " + avanzar);

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, codTramite);
            ps.setBoolean(i, avanzar);

            int insertedRows = ps.executeUpdate();
            if (insertedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA ELIMINAR LA ASOCIACION" +
                        " ENTRE UN OP DE SERVICIO WEB Y UN TRAMITE");

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA ELIMINAR LA ASOCIACION ENTRE UN OP DE SERVICIO WEB Y UN TRAMITE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA ELIMINAR LA ASOCIACION ENTRE UN OP DE SERVICIO" +
                    " WEB Y UN TRAMITE", sqle);
        } finally {
            GeneralOperations.closeStatement(ps);
        }
    }

    private void crearOpsTramite(int codMunicipio, String codProcedimiento, int codTramite, boolean avanzar,
                                 InfoConfTramSWVO infoSW, Connection con)
            throws TechnicalException, InternalErrorException {

        String sqlInsertTraOp = "INSERT INTO DEF_TRA_SW(DEF_TRA_MUN, DEF_TRA_PRO, DEF_COD_TRA, DEF_TRA_AVZ, " +
                "DEF_TRA_OBL, DEF_TRA_OP) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sqlInsertTraOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR LA ASOCIACION ENTRE UN OP DE SERVICIO" +
                    " WEB Y UN TRAMITE");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertTraOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de Procedimiento: " + codProcedimiento);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo de Tramite: " + codTramite);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Avanzar o Retroceder?: " + avanzar);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Obligatorio u opcional?: " + infoSW.isObligatorio());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 6: Codigo de Operacion: " + infoSW.getCodOpSW());

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, codTramite);
            ps.setBoolean(i++, avanzar);
            ps.setBoolean(i++, infoSW.isObligatorio());
            ps.setInt(i, infoSW.getCodOpSW());

            int insertedRows = ps.executeUpdate();
            if (insertedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR LA ASOCIACION" +
                        " ENTRE UN OP DE SERVICIO WEB Y UN TRAMITE");

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA INSERTAR LA ASOCIACION ENTRE UN OP DE SERVICIO WEB Y UN TRAMITE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA INSERTAR LA ASOCIACION ENTRE UN OP DE SERVICIO" +
                    " WEB Y UN TRAMITE", sqle);
        } finally {
            GeneralOperations.closeStatement(ps);
        }
    }

    private void updateOpsTramite(int codMunicipio, String codProcedimiento, int codTramite, boolean avanzar,
                                  InfoConfTramSWVO infoServicioWeb, Connection con)
            throws TechnicalException, InternalErrorException {

        String sqlInsertTraOp = "UPDATE DEF_TRA_SW SET DEF_TRA_OP = ?, DEF_TRA_OBL = ? WHERE DEF_TRA_MUN = ? AND DEF_TRA_PRO = ? AND " +
                "DEF_COD_TRA = ? AND  DEF_TRA_AVZ = ?";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sqlInsertTraOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ACTUALIZAR LA ASOCIACION ENTRE UN OP DE SERVICIO" +
                    " WEB Y UN TRAMITE");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertTraOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Operacion: " + infoServicioWeb.getCodOpSW());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Obligatorio?: " + infoServicioWeb.isObligatorio());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo de Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Codigo de Procedimiento: " + codProcedimiento);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 5: Codigo de Tramite: " + codTramite);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 6: Avanzar o Retroceder?: " + avanzar);


            int i = 1;
            ps.setInt(i++, infoServicioWeb.getCodOpSW());
            ps.setBoolean(i++, infoServicioWeb.isObligatorio());
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, codTramite);
            ps.setBoolean(i, avanzar);


            int insertedRows = ps.executeUpdate();
            if (insertedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA ACTUALIZAR LA ASOCIACION" +
                        " ENTRE UN OP DE SERVICIO WEB Y UN TRAMITE");

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA ACTUALIZAR LA ASOCIACION ENTRE UN OP DE SERVICIO WEB Y UN TRAMITE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA ACTUALIZAR LA ASOCIACION ENTRE UN OP DE SERVICIO" +
                    " WEB Y UN TRAMITE", sqle);
        } finally {
            GeneralOperations.closeStatement(ps);
        }
    }

    public InfoConfTramSWVO getInfoSW(long cfo, Connection con)
            throws TechnicalException, InternalErrorException {
        /*
        String sqlGetCodigoAvz = "SELECT DEF_TRA_OP, DEF_TRA_OBL, OPS_DEF_TIT FROM DEF_TRA_SW, SW_OPS_DEF " +
                "WHERE DEF_TRA_CFO = ? "; */
        String sqlGetCodigoAvz = "SELECT DEF_TRA_OP, DEF_TRA_OBL, OPS_DEF_TIT FROM DEF_TRA_SW, SW_OPS_DEF " +
                "WHERE DEF_TRA_CFO = ? AND DEF_TRA_TIPO_ORIGEN_OPERACION='WS'";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sqlGetCodigoAvz);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER EL CODIGO DE LA OPERACION DEL SERVICIO" +
                    " WEB ASIGNADA AL TRAMITE");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetCodigoAvz);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo sw: " + cfo);

            int i = 1;
            ps.setLong(i++, cfo);
         
            rs = ps.executeQuery();
            InfoConfTramSWVO infoServicioWeb = null;
            if (rs.next()) {
                infoServicioWeb = new InfoConfTramSWVO();
                infoServicioWeb.setCodOpSW(rs.getInt(1));
                infoServicioWeb.setObligatorio(rs.getBoolean(2));
                infoServicioWeb.setTituloOperacion(rs.getString(3));
            }

            return infoServicioWeb;

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER EL CODIGO DE LA OPERACION DEL SERVICIO WEB ASIGNADA AL" +
                    " TRAMITE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER EL CODIGO DE LA OPERACION DEL SERVICIO" +
                    " WEB ASIGNADA AL TRAMITE", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
        }
    }

    public int getCodigoListaParams(int codMunicipio, String codProcedimiento, int codTramite, boolean avanzar, String[] params) throws TechnicalException, InternalErrorException {

        String sqlGetCodigoListaParams = "SELECT DISTINCT COD_OP_CONF FROM CONF_TRA_SW WHERE COD_MUN_CONF = ? " +
                "AND COD_PRO_CONF = ? AND COD_TRA_CONF = ? AND COD_AVZ_CONF = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetCodigoListaParams);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER EL CODIGO DE LA LISTA DE PARAMETROS " +
                    "DEFINIDA PARA UN TRAMITE");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetCodigoListaParams);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de Procedimiento: " + codProcedimiento);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo de Tramite: " + codTramite);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Avanzar o Retroceder?: " + avanzar);

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, codTramite);
            ps.setBoolean(i, avanzar);

            rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);
            else return -1;

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER EL CODIGO DE LA LISTA DE PARAMETROS" +
                    " DEFINIDA PARA UN TRAMITE");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER EL CODIGO DE LA LISTA" +
                    " DE PARAMETROS DEFINIDA PARA UN TRAMITE", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER EL CODIGO DE LA LISTA DE PARAMETROS DEFINIDA PARA UN" +
                    " TRAMITE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER EL CODIGO DE LA LISTA DE PARAMETROS" +
                    " DEFINIDA PARA UN TRAMITE", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }

    }

    public void eliminarListaParams(int codMunicipio, String codProcedimiento, int codTramite, boolean avanzar,
                                    Connection con) throws TechnicalException, InternalErrorException {

        String sqlDeleteListaParams = "DELETE CONF_TRA_SW WHERE COD_MUN_CONF = ? AND COD_PRO_CONF = ? AND " +
                "COD_TRA_CONF = ? AND COD_AVZ_CONF = ?";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sqlDeleteListaParams);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ELIMINAR LA LISTA DE PARAMETROS DE UN TRAMITE");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlDeleteListaParams);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de Procedimiento: " + codProcedimiento);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo de Tramite: " + codTramite);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Avanzar o Retroceder?: " + avanzar);

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, codTramite);
            ps.setBoolean(i, avanzar);

            ps.executeUpdate();

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA ELIMINAR LA LISTA DE PARAMETROS DE UN TRAMITE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA ELIMINAR LA LISTA DE PARAMETROS DE UN TRAMITE", sqle);
        } finally {
            GeneralOperations.closeStatement(ps);
        }

    }

    public void crearListaParams(int codMunicipio, String codProcedimiento, int codTramite, boolean avanzar,
                                 InfoConfTramSWVO infoServicioWeb, Connection con)
            throws TechnicalException, InternalErrorException {

        String sqlInsertParametro = "INSERT INTO CONF_TRA_SW(COD_MUN_CONF, COD_PRO_CONF, COD_TRA_CONF, " +
                "COD_AVZ_CONF, COD_OP_CONF, COD_ORD_CONF, DEF_PRM_CONF, TIP_DAT_CONF, COD_DAT_CONF, VAL_DAT_CONF) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = null;

        try {
            for (Iterator itParamsIn = infoServicioWeb.getListaParamsEntrada().iterator(); itParamsIn.hasNext(); ) {
                ParametroConfigurableVO paramIn = (ParametroConfigurableVO) itParamsIn.next();


                ps = con.prepareStatement(sqlInsertParametro);
                if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR UN PARAMETRO DE ENTRADA PARA UN TRAMITE");
                if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertParametro);
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Municipio: " + codMunicipio);
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de Procedimiento: " + codProcedimiento);
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo de Tramite: " + codTramite);
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Avanzar o Retroceder?: " + avanzar);
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 5: Codigo de Operacion: " + paramIn.getCodigoOp());
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 6: Numero de Orden: " + paramIn.getOrdenParam());
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 7: Descripcion de Param: " + paramIn.getNombreDefinicion());
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 8: Tipo de Dato: " + paramIn.getTipoValorPaso());
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 9: Codigo Dato Procedimiento: " + paramIn.getCodCampoExp());
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 10: Valor Constante: " + paramIn.getValorConstante());

                int i = 1;
                ps.setInt(i++, codMunicipio);
                ps.setString(i++, codProcedimiento);
                ps.setInt(i++, codTramite);
                ps.setBoolean(i++, avanzar);
                ps.setInt(i++, paramIn.getCodigoOp());
                ps.setInt(i++, paramIn.getOrdenParam());
                ps.setString(i++, paramIn.getNombreDefinicion());
                ps.setInt(i++, paramIn.getTipoValorPaso());
                ps.setString(i++, paramIn.getCodCampoExp());
                ps.setString(i, paramIn.getValorConstante());

                int insertedRows = ps.executeUpdate();
                if (insertedRows != 1)
                    throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR UN PARAMETRO DE " +
                            "ENTRADA EN LA ASOCIACION CON UN TRAMITE");

                GeneralOperations.closeStatement(ps);

            }

            for (Iterator itParamsOut = infoServicioWeb.getListaParamsSalida().iterator(); itParamsOut.hasNext(); ) {
                ParametroConfigurableVO paramOut = (ParametroConfigurableVO) itParamsOut.next();

                ps = con.prepareStatement(sqlInsertParametro);
                if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR UN PARAMETRO DE SALIDA PARA UN TRAMITE");
                if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertParametro);
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Municipio: " + codMunicipio);
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de Procedimiento: " + codProcedimiento);
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo de Tramite: " + codTramite);
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Avanzar o Retroceder?: " + avanzar);
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 5: Codigo de Operacion: " + paramOut.getCodigoOp());
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 6: Numero de Orden: " + 0);
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 7: Descripcion de Param: " + paramOut.getNombreDefinicion());
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 8: Tipo de Dato: " + 1);
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 9: Codigo Dato Procedimiento: " + paramOut.getCodCampoExp());
                if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 10: Valor Constante: Ninguno");

                int i = 1;
                ps.setInt(i++, codMunicipio);
                ps.setString(i++, codProcedimiento);
                ps.setInt(i++, codTramite);
                ps.setBoolean(i++, avanzar);
                ps.setInt(i++, paramOut.getCodigoOp());
                ps.setInt(i++, 0);
                ps.setString(i++, paramOut.getNombreDefinicion());
                ps.setInt(i++, 1);
                ps.setString(i++, paramOut.getCodCampoExp());
                ps.setString(i, null);

                int insertedRows = ps.executeUpdate();
                if (insertedRows != 1)
                    throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR UN PARAMETRO DE " +
                        "ENTRADA EN LA ASOCIACION CON UN TRAMITE");

                GeneralOperations.closeStatement(ps);

            }

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA INSERTAR LA LISTA DE PARAMETROS DE UN TRAMITE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA INSERTAR LA LISTA DE PARAMETROS DE UN TRAMITE", sqle);
        } finally {
            GeneralOperations.closeStatement(ps);
        }
    }

    public Vector getParamsEntradaTramite(int codMunicipio, String codProcedimiento, int codTramite,
                                              boolean avanzar, String[] params)
            throws TechnicalException, InternalErrorException {

        String sqlGetParamsIn = "SELECT A.DEF_CFO_CONF, A.COD_OP_CONF, A.COD_ORD_CONF, A.DEF_PRM_CONF, B.PRM_DEF_TIT, A.TIP_DAT_CONF, " +
                "A.COD_DAT_CONF, A.VAL_DAT_CONF FROM CONF_TRA_SW A, SW_PARAMS_IN_DEF B " +
                "WHERE A.COD_ORD_CONF = B.ORD_PRM_SW AND A.COD_OP_CONF = B.OPS_DEF_COD AND " +
                "A.DEF_PRM_CONF = B.PRM_DEF_NOM AND A.COD_MUN_CONF = ? AND A.COD_PRO_CONF = ? AND " +
                "A.COD_TRA_CONF = ? AND A.COD_AVZ_CONF = ? AND A.COD_ORD_CONF <> 0";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetParamsIn);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LA LISTA DE PARAMETROS DE ENTRADA ASOCIADOS A UN TRAMITE");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetParamsIn);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de Procedimiento: " + codProcedimiento);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo de Tramite: " + codTramite);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Avanzar o Retroceder?: " + avanzar);

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, codTramite);
            ps.setBoolean(i, avanzar);

            rs = ps.executeQuery();

            Vector listaParamsIn = new Vector();
            while (rs.next()) {
                i = 1;
                long cfo = rs.getLong(i++);
                int codigoOp = rs.getInt(i++);
                int codigoOrden = rs.getInt(i++);
                String defParam = rs.getString(i++);
                String tituloParam = rs.getString(i++);
                int tipoParam = rs.getInt(i++);
                String codigoDato = rs.getString(i++);
                String valorDato = rs.getString(i);
                listaParamsIn.add(new ParametroConfigurableVO(cfo, codigoOp, codigoOrden, defParam, tituloParam, tipoParam,
                        codigoDato, valorDato));
            }

            return listaParamsIn;

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER LA LISTA DE PARAMETROS DE ENTRADA ASOCIADOS A UN TRAMITE");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER LA LISTA DE PARAMETROS " +
                    "DE ENTRADA ASOCIADOS A UN TRAMITE", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE PARAMETROS DE ENTRADA ASOCIADOS A UN TRAMITE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE PARAMETROS DE ENTRADA " +
                    "ASOCIADOS A UN TRAMITE", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public Vector getParamsSalidaTramite(int codMunicipio, String codProcedimiento, int codTramite,
                                              boolean avanzar, String[] params)
            throws TechnicalException, InternalErrorException {

        String sqlGetParamsOut = "SELECT A.DEF_CFO_CONF, A.COD_OP_CONF, A.COD_ORD_CONF, A.DEF_PRM_CONF, B.PRM_DEF_TIT, A.TIP_DAT_CONF, " +
                "A.COD_DAT_CONF, A.VAL_DAT_CONF FROM CONF_TRA_SW A, SW_PARAMS_OUT_DEF B " +
                "WHERE A.COD_OP_CONF = B.OPS_DEF_COD AND A.DEF_PRM_CONF = B.PRM_DEF_NOM AND A.COD_MUN_CONF = ? AND " +
                "A.COD_PRO_CONF = ? AND A.COD_TRA_CONF = ? AND A.COD_AVZ_CONF = ? AND A.COD_ORD_CONF = 0";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetParamsOut);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LA LISTA DE PARAMETROS DE ENTRADA ASOCIADOS A UN TRAMITE");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetParamsOut);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de Procedimiento: " + codProcedimiento);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo de Tramite: " + codTramite);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Avanzar o Retroceder?: " + avanzar);

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, codTramite);
            ps.setBoolean(i, avanzar);

            rs = ps.executeQuery();

            Vector listaParamsOut = new Vector();
            while (rs.next()) {
                i = 1;
                long cfo = rs.getLong(i++);
                int codigoOp = rs.getInt(i++);
                int codigoOrden = rs.getInt(i++);
                String defParam = rs.getString(i++);
                String tituloParam = rs.getString(i++);
                int tipoParam = rs.getInt(i++);
                String codigoDato = rs.getString(i++);
                String valorDato = rs.getString(i);
                listaParamsOut.add(new ParametroConfigurableVO(cfo,codigoOp, codigoOrden, defParam, tituloParam, tipoParam,
                        codigoDato, valorDato));
            }

            return listaParamsOut;

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER LA LISTA DE PARAMETROS DE ENTRADA ASOCIADOS A UN TRAMITE");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER LA LISTA DE PARAMETROS " +
                    "DE ENTRADA ASOCIADOS A UN TRAMITE", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE PARAMETROS DE ENTRADA ASOCIADOS A UN TRAMITE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE PARAMETROS DE ENTRADA " +
                    "ASOCIADOS A UN TRAMITE", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public boolean esOperacionObligatoria(int codMunicipio, String codProcedimiento, int codTramite, boolean avanzar,
                                         int codigoOp, String[] params)
            throws TechnicalException, InternalErrorException {

        String sqlEsObligatoria = "SELECT DEF_TRA_OBL FROM DEF_TRA_SW WHERE DEF_TRA_MUN = ? AND DEF_TRA_PRO = ? AND " +
                "DEF_COD_TRA = ? AND DEF_TRA_AVZ = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlEsObligatoria);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER SI LA DEFINICION DE ESTA OPERACION ES " +
                    "OBLIGATORIA O NO");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlEsObligatoria);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de Procedimiento: " + codProcedimiento);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo de Tramite: " + codTramite);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Avanzar o Retroceder?: " + avanzar);

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, codTramite);
            ps.setBoolean(i, avanzar);

            rs = ps.executeQuery();

            return rs.next() && rs.getBoolean(1);

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER SI LA DEFINICION DE ESTA OPERACION ES " +
                    "OBLIGATORIA O NO");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER SI LA DEFINICION DE " +
                    "ESTA OPERACION ES OBLIGATORIA O NO", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER SI LA DEFINICION DE ESTA OPERACION ES OBLIGATORIA O NO");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER SI LA DEFINICION DE ESTA OPERACION ES " +
                    "OBLIGATORIA O NO", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public int getMaxOrd(int codMunicipio, String codProcedimiento, int codTramite, String[] params) 
    									throws TechnicalException, InternalErrorException {
    	int ord = -1;
        
    	String sqlMaxOrd = "SELECT MAX(DEF_TRA_ORD) FROM DEF_TRA_SW WHERE DEF_TRA_MUN = ? AND DEF_TRA_PRO = ? AND " +
        "DEF_COD_TRA = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;  
        
        try {
			con = abd.getConnection();

			ps = con.prepareStatement(sqlMaxOrd);
			if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LA EL ORDEN MAXIMO");
			if (m_Log.isDebugEnabled()) m_Log.debug(sqlMaxOrd);
			if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Municipio: " + codMunicipio);
			if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de Procedimiento: " + codProcedimiento);
			if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo de Tramite: " + codTramite);
			

			int i = 1;
			ps.setInt(i++, codMunicipio);
			ps.setString(i++, codProcedimiento);
			ps.setInt(i++, codTramite);
			
			i=1;
			rs = ps.executeQuery();
			while (rs.next()) {
				ord = rs.getInt(i++);				
			}
			m_Log.debug("EL ORDEN MAXIMO ES " + ord);
			return(ord);
			
		} catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER EL ORDEN MAXIMO");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER EL ORDEN MAXIMO", bde);
		} catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER EL ORDEN MAXIMO");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER EL ORDEN MAXIMO", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
        
    	
    }

    public long getMaxCodSW(Connection con) throws TechnicalException, InternalErrorException {
    	
    	int cod = -1;
        
    	String sqlMaxCod = "SELECT MAX(DEF_TRA_CFO) FROM DEF_TRA_SW" ;

        
        PreparedStatement ps = null;
        ResultSet rs = null;    
        try {
			

			ps = con.prepareStatement(sqlMaxCod);
			if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LA CLAVE FORANEA MAXIMA DEL SW");
			if (m_Log.isDebugEnabled()) m_Log.debug(sqlMaxCod);			
			
			int i=1;
			rs = ps.executeQuery();
			while (rs.next()) {
				cod = rs.getInt(i++);				
			}
			m_Log.debug("LA CLAVE MAXIMA ES " + cod);
			return(cod);
			

		} catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LA CLAVE MAXIMA");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LA CLAVE MAXIMA", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            
        }        
    }


      

    /**
     *
     * @param codMunicipio: Código del municipio u organización
     * @param codProcedimiento: Código del procedimiento
     * @param codTramite: Código del trámite
     * @param codOpAv
     * @param codOpRet
     * @param ord: Orden
     * @param tipoOrigenAvanzar: Tipo del origen de la operación de avanzar (WS o
     * @param tipoOrigenRetroceder
     * @param descOpAvanzar
     * @param descOpRetroceder
     * @param nombreModuloAvanzar: Nombre del módulo de integración que se corresponde con la operación de avanzar
     * @param nombreModuloRetroceder: Nombre del módulo de integración que se corresponde con la operación de retroceder
     * @param params
     * @throws es.altia.common.exception.TechnicalException
     * @throws es.altia.util.exceptions.InternalErrorException
     */
    public void insertWS (int codMunicipio, String codProcedimiento, int codTramite, 
			int codOpAv, int codOpRet, int codOpIniciar,int ord,String tipoOrigenAvanzar,String tipoOrigenRetroceder,String tipoOrigenIniciar,String tituloOperacionAvanzar,String tituloOperacionRetroceder,String tituloOperacionIniciar,String nombreModuloAvanzar,String nombreModuloRetroceder,String nombreModuloIniciar,int codTipoRetroceso,String[] params) throws TechnicalException, InternalErrorException{
    	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
    	Connection con = null;
    	
		try {
			con = abd.getConnection();    	
			abd.inicioTransaccion(con);
            long cfoIni = getMaxCodSW(con)+1;
            long cfoAv  = cfoIni + 1;
			long cfoRet = cfoAv + 1 ;
            /*
			long cfoAv = getMaxCodSW(con)+1;
			long cfoRet = cfoAv+1 ;
            */
			
			DefinicionOperacionesSWDAO defDAO = DefinicionOperacionesSWDAO.getInstance();
            
            if (codOpIniciar!=-1) { // Operación configurada al inicio del trámite
				insertarSW(cfoIni,codMunicipio,codProcedimiento,codTramite,2,codOpIniciar,ord,tipoOrigenIniciar,tituloOperacionIniciar,nombreModuloIniciar,codTipoRetroceso,con);
				if(tipoOrigenIniciar!=null && "WS".equals(tipoOrigenIniciar)){
                    Vector paramsInIniciar = defDAO.getParamsEntradaTramitacion(codOpIniciar, con);
                    Vector paramsOutIniciar = defDAO.getParamsSalidaTramitacion(codOpIniciar, con);
                    defDAO.creaEstructura(cfoIni,paramsInIniciar,paramsOutIniciar,con);
                }
			}

			if (codOpAv!=-1) { // Operación configurada al avanzar el trámite
				insertarSW(cfoAv,codMunicipio,codProcedimiento,codTramite,1,codOpAv,ord,tipoOrigenAvanzar,tituloOperacionAvanzar,nombreModuloAvanzar,codTipoRetroceso,con);
				if(tipoOrigenAvanzar!=null && "WS".equals(tipoOrigenAvanzar)){
                    Vector paramsInAv = defDAO.getParamsEntradaTramitacion(codOpAv, con);
                    Vector paramsOutAv = defDAO.getParamsSalidaTramitacion(codOpAv, con);
                    defDAO.creaEstructura(cfoAv,paramsInAv,paramsOutAv,con);
                }
			}
            
			if (codOpRet!=-1) {	 // Operación configurada al retroceder el trámite
				insertarSW(cfoRet,codMunicipio,codProcedimiento,codTramite,0,codOpRet,ord,tipoOrigenRetroceder,tituloOperacionRetroceder,nombreModuloRetroceder,codTipoRetroceso,con);
				if(tipoOrigenRetroceder!=null && "WS".equals(tipoOrigenRetroceder)){
                    Vector paramsInRet = defDAO.getParamsEntradaTramitacion(codOpRet, con);
                    Vector paramsOutRet = defDAO.getParamsSalidaTramitacion(codOpRet, con);
                    defDAO.creaEstructura(cfoRet,paramsInRet,paramsOutRet,con);
                }
			}
			
		} catch (Exception e) {
			rollBackTransaction(abd,con);
			e.printStackTrace();
			throw new TechnicalException("ERROR AL INSERTAR EL SW",e);					
		} 
		commitTransaction(abd,con);
		devolverConexion(abd,con);						
    }

   
    public int insertarSW(long codSW, int codMunicipio, String codProcedimiento, int codTramite,
            int avanzar, int codOp, int ord, String tipoOrigenOperacion, String nombreOperacion, String nombreModulo, int codTipoRetroceso, int obligatorio, Connection con) throws TechnicalException, InternalErrorException {

        String sqlInsertWS = "INSERT INTO DEF_TRA_SW(DEF_TRA_CFO,DEF_TRA_MUN, DEF_TRA_PRO, "
                + "DEF_COD_TRA, DEF_TRA_AVZ, DEF_TRA_OBL, DEF_TRA_OP, DEF_TRA_ORD, DEF_TRA_TIPO_ORIGEN_OPERACION,DEF_TRA_NOMBRE_OPERACION,DEF_TRA_NOMBRE_MODULO,TIPO_OP_RETROCESO) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = null;
        ResultSet rs = null;
        int insertedRows = -1;
        
        try {

            ps = con.prepareStatement(sqlInsertWS);
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("CONSULTA PARA INSERTAR EL SW");
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sqlInsertWS);
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Parametro 1: Clave SW: " + codSW);
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Parametro 2: Codigo de Municipio: " + codMunicipio);
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Parametro 3: Codigo de Procedimiento: " + codProcedimiento);
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Parametro 4: Codigo de Tramite: " + codTramite);
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Parametro 5: Avanzar o Retroceder?: " + avanzar);
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Parametro 6: Obligatorio u opcional?: " + 0);
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Parametro 7: Codigo de Operacion: " + codOp);
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Parametro 8: Orden de Ejecucion: " + ord);
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Parametro 9: Tipo origen de la operación: " + tipoOrigenOperacion);
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Parametro 10: Nombre de la operación: " + nombreOperacion);
            }

            int i = 1;
            ps.setLong(i++, codSW);
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, codTramite);
            ps.setInt(i++, avanzar);
            ps.setInt(i++, obligatorio);
            ps.setInt(i++, codOp);
            ps.setInt(i++, ord);
            ps.setString(i++, tipoOrigenOperacion);
            if (nombreOperacion != null && !"".equals(nombreOperacion)) {
                ps.setString(i++, nombreOperacion);
            } else {
                ps.setNull(i++, java.sql.Types.VARCHAR);
            }

            if (nombreModulo != null && !"".equals(nombreModulo)) {
                ps.setString(i++, nombreModulo);
            } else {
                ps.setNull(i++, java.sql.Types.VARCHAR);
            }

            ps.setInt(i++, codTipoRetroceso);

            insertedRows = ps.executeUpdate();
            if (insertedRows != 1) {
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR EL SW");
            }

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA INSERTAR EL SW");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA INSERTAR EL SW", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

        }
        
        return insertedRows;
    }
    
    private void insertarSW(long codSW, int codMunicipio, String codProcedimiento, int codTramite,
            int avanzar, int codOp, int ord, String tipoOrigenOperacion, String nombreOperacion, String nombreModulo, int codTipoRetroceso, Connection con) throws TechnicalException, InternalErrorException {

        insertarSW(codSW, codMunicipio, codProcedimiento, codTramite, avanzar, codOp, ord, tipoOrigenOperacion, nombreOperacion, nombreModulo, codTipoRetroceso, 0, con);
    }
    
    
    private void removeWS(int codMunicipio, String codProcedimiento, int codTramite, int orden, 
			Connection con) throws TechnicalException, InternalErrorException{
    	
        String sqlRemoveWS = "DELETE DEF_TRA_SW WHERE DEF_TRA_MUN = ? AND DEF_TRA_PRO = ? AND DEF_COD_TRA = ? " +
        "AND DEF_TRA_ORD = ?";   
        
        
        PreparedStatement ps = null;
        ResultSet rs = null;    
        try {
			

			ps = con.prepareStatement(sqlRemoveWS);
			if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ELIMINAR EL SW");
			if (m_Log.isDebugEnabled()) m_Log.debug(sqlRemoveWS);			
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de Procedimiento: " + codProcedimiento);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo de Tramite: " + codTramite);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Orden de Ejecucion: " + orden);
            
			int i=1;
		
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, codTramite);
            ps.setInt(i++, orden);            
            
            ps.executeUpdate();
            


		} catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA ELIMINAR EL SW");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA ELIMINAR EL SW", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
           
        }                                                                                     
    }
    private Vector<Long> getCfos (int codMunicipio, String codProcedimiento, int codTramite, int orden, Connection con)
    throws TechnicalException, InternalErrorException{

    	String sqlCfos = "SELECT DEF_TRA_CFO FROM DEF_TRA_SW WHERE DEF_TRA_MUN = ? AND " +
    			"DEF_TRA_PRO = ? AND DEF_COD_TRA = ? AND DEF_TRA_ORD = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;      	
        try {

			ps = con.prepareStatement(sqlCfos);
			if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LAS CFOS DE LOS SW A ELIMINAR");
			if (m_Log.isDebugEnabled()) m_Log.debug(sqlCfos);
			if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Municipio: " + codMunicipio);
			if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de Procedimiento: " + codProcedimiento);
			if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo de Tramite: " + codTramite);
			if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo de Orden: " + orden);

			int i = 1;
			ps.setInt(i++, codMunicipio);
			ps.setString(i++, codProcedimiento);
			ps.setInt(i++, codTramite);
			ps.setInt(i, orden);
			
			Vector<Long> lista = new Vector<Long>();
			rs = ps.executeQuery();			

			while (rs.next()) {				
				i = 1;			
				long cod = rs.getLong(i);
				lista.addElement(cod);
			}
				
			return lista;

		} catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LAS CFOS DE LOS SW A ELIMINAR");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LAS CFOS DE LOS SW A ELIMINAR", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
        }    	
    }


    /**
     *  
     * @param codMunicipio
     * @param codProcedimiento
     * @param codTramite
     * @param ejecutarTareasInicio
     * @param numExpediente
     * @param ocurrenciaTramite
     * @param params
     * @return
     * @throws es.altia.common.exception.TechnicalException
     * @throws es.altia.util.exceptions.InternalErrorException
     */
     public Hashtable<Integer,Hashtable<String,GeneralValueObject>> getListaConfSW (int codMunicipio, String codProcedimiento, int codTramite,
		 String[] params) throws TechnicalException, InternalErrorException{
    	//DEVUELVE LA LISTA DE PARES AV/RET DEL TRAMITE CORRESPONDIENTE TODOS SEGUIDOS
    	//ORDENADOS POR EL ORDEN DE EJECUCION Y PRIMERO EL DE RETROCEDER Y DESPUES EL DE AVANZAR
    	//SE ENCARGA EL MANAGER DE CONSTRUIR LA LISTA DE PARES PROPIAMENTE DICHA

        Hashtable<Integer,Hashtable<String,GeneralValueObject>> salida = new Hashtable<Integer,Hashtable<String,GeneralValueObject>>();

        String sqlListaConfSW = "SELECT DEF_TRA_OP,DEF_TRA_ORD,DEF_TRA_AVZ, DEF_TRA_OBL, DEF_TRA_CFO,DEF_TRA_NOMBRE_OPERACION,DEF_TRA_TIPO_ORIGEN_OPERACION,DEF_TRA_NOMBRE_MODULO,TIPO_OP_RETROCESO FROM DEF_TRA_SW WHERE " +
    			"DEF_TRA_MUN = " + codMunicipio + " AND DEF_TRA_PRO ='" + codProcedimiento + "' AND DEF_COD_TRA =" + codTramite + " ORDER BY DEF_TRA_ORD,DEF_TRA_AVZ";
        
        if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LA LISTA DE SW AV/RET DEL TRAMITE");
	    if (m_Log.isDebugEnabled()) m_Log.debug(sqlListaConfSW);
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = abd.getConnection();

            st  = con.createStatement();
            rs  = st.executeQuery(sqlListaConfSW);
            int avz = -1;
            long cfo = -1;
            int i=1;
            int ordenNuevo = 0;            
			
            while (rs.next()) {
                i = 1;
                int codOp = rs.getInt("DEF_TRA_OP");				
                int obligatorio = rs.getInt("DEF_TRA_OBL");				
                avz = rs.getInt("DEF_TRA_AVZ");
                cfo = rs.getLong("DEF_TRA_CFO");
                ordenNuevo = rs.getInt("DEF_TRA_ORD");
                String tipoOrigen = rs.getString("DEF_TRA_TIPO_ORIGEN_OPERACION");
                String nombreModulo = rs.getString("DEF_TRA_NOMBRE_MODULO");
                String tipoOperacionRetroceso = rs.getString("TIPO_OP_RETROCESO");
                
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("COD_OP",codOp);
                gVO.setAtributo("CFO", cfo);
                gVO.setAtributo("NOMBRE_OPERACION",rs.getString("DEF_TRA_NOMBRE_OPERACION"));
                gVO.setAtributo("TIPO_ORIGEN",tipoOrigen);
                gVO.setAtributo("NOMBRE_MODULO",nombreModulo);
                gVO.setAtributo("TIPO_OPERACION_RETROCESO",tipoOperacionRetroceso);
                gVO.setAtributo("NUMERO_ORDEN",ordenNuevo);
                gVO.setAtributo("OBLIGATORIO",obligatorio);

                if(!salida.containsKey(ordenNuevo)){
                    Hashtable<String,GeneralValueObject> aux = new Hashtable<String, GeneralValueObject>();
                    if(avz==0){
                        aux.put("RETROCEDER",gVO);
                    }else
                    if(avz==1){
                       aux.put("AVANZAR", gVO);
                    }else
                    if(avz==2){
                       aux.put("INICIAR",gVO);
                    }
                    salida.put(ordenNuevo,aux);
                }else{
                    Hashtable<String,GeneralValueObject> elemento = salida.get(ordenNuevo);
                    if(avz==0){
                        elemento.put("RETROCEDER",gVO);
                    }else
                    if(avz==1){
                       elemento.put("AVANZAR", gVO);
                    }else
                    if(avz==2){
                       elemento.put("INICIAR",gVO);
                    }
                    
                    salida.put(ordenNuevo,elemento);
                }// else
                
            }// while

		} catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER LA LISTA DE SW AV/RET DEL TRAMITE");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER LA LISTA DE SW AV/RET DEL TRAMITE", bde);
		} catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE SW AV/RET DEL TRAMITE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE SW AV/RET DEL TRAMITE", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(st);
            devolverConexion(abd, con);
        }

        return salida;
    }
     
    

      /**
     *
     * @param codMunicipio: Código del municipio/organización
     * @param codProcedimiento: Código del procedimiento
     * @param codTramite: Código del trámite     
     * @param defTraAvz: 0=Retroceso,1=Avanzar,2=Iniciar
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param params: Parámetros de conexión a la base de datos
     * @return Hashtable<Integer,Hashtable<String,GeneralValueObject>>
     * @throws es.altia.common.exception.TechnicalException
     * @throws es.altia.util.exceptions.InternalErrorException
     */
      public Hashtable<Integer,Hashtable<String,GeneralValueObject>> getListaConfSW (int codMunicipio, String codProcedimiento, int codTramite,
		 int defTraAvz,int ocurrenciaTramite,String numExpediente,int ejercicio,boolean cerrado,String[] params) throws TechnicalException, InternalErrorException{
    	//DEVUELVE LA LISTA DE PARES AV/RET DEL TRAMITE CORRESPONDIENTE TODOS SEGUIDOS
    	//ORDENADOS POR EL ORDEN DE EJECUCION Y PRIMERO EL DE RETROCEDER Y DESPUES EL DE AVANZAR
    	//SE ENCARGA EL MANAGER DE CONSTRUIR LA LISTA DE PARES PROPIAMENTE DICHA

        Hashtable<Integer,Hashtable<String,GeneralValueObject>> salida = new Hashtable<Integer,Hashtable<String,GeneralValueObject>>();

        String sqlListaConfSW = "SELECT DEF_TRA_OP,DEF_TRA_ORD,DEF_TRA_AVZ, DEF_TRA_CFO,TIPO_OP_RETROCESO FROM DEF_TRA_SW WHERE " +
    			"DEF_TRA_MUN = " + codMunicipio + " AND DEF_TRA_PRO ='" + codProcedimiento + "' AND DEF_COD_TRA =" + codTramite  +
                        " AND DEF_TRA_AVZ=" + defTraAvz + " ORDER BY DEF_TRA_ORD,DEF_TRA_AVZ";

        if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LA LISTA DE SW AV/RET DEL TRAMITE");
	    if (m_Log.isDebugEnabled()) m_Log.debug(sqlListaConfSW);
        
        
    	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            
            con = abd.getConnection();            
            st  = con.createStatement();
            
            int avz = -1;
            long cfo = -1;
            int tipoOperacionRetroceso = -1;
            int i=1;
            int ordenNuevo = 0;

            rs  = st.executeQuery(sqlListaConfSW);
            
	    while (rs.next()) {
                
                i = 1;
                int codOp = rs.getInt("DEF_TRA_OP");
                avz = rs.getInt("DEF_TRA_AVZ");
                cfo = rs.getLong("DEF_TRA_CFO");
                ordenNuevo = rs.getInt("DEF_TRA_ORD");
                tipoOperacionRetroceso = rs.getInt("TIPO_OP_RETROCESO");

                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("COD_OP",codOp);
                gVO.setAtributo("CFO", cfo);
                
                
                if(!salida.containsKey(ordenNuevo)){
                    Hashtable<String,GeneralValueObject> aux = new Hashtable<String, GeneralValueObject>();
                    if(avz==0){
                        /** prueba **/
                        boolean insertar = false;
                        if(tipoOperacionRetroceso==1 && cerrado)                        
                            // Si la ocurrencia del trámite está cerrada y la operación se ejecuta al retroceder un trámite cerrado
                            insertar = true;
                        
                        if(tipoOperacionRetroceso==2 && !cerrado)                        
                            // Si la ocurrencia del trámite está pendiente/abierta y la operación se ejecuta al retroceder un trámite abierto
                            insertar = true;
                            
                        if(insertar){
                            aux.put("RETROCEDER",gVO);
                            salida.put(ordenNuevo,aux);
                        }
                        /** prueba **/                            
                        // original    
                        //aux.put("RETROCEDER",gVO); 
                        
                    }else
                    if(avz==1){
                       aux.put("AVANZAR", gVO);
                       salida.put(ordenNuevo,aux);
                    }else
                    if(avz==2){
                       aux.put("INICIAR",gVO);
                       salida.put(ordenNuevo,aux);
                    }
                    // ORIGINAL
                    //salida.put(ordenNuevo,aux);
                }else{
                    Hashtable<String,GeneralValueObject> elemento = salida.get(ordenNuevo);
                    if(avz==0){
                        /** prueba **/
                        boolean insertar = false;
                        if(tipoOperacionRetroceso==1 && cerrado)                        
                            // Si la ocurrencia del trámite está cerrada y la operación se ejecuta al retroceder un trámite cerrado
                            insertar = true;
                        
                        if(tipoOperacionRetroceso==2 && !cerrado)                        
                            // Si la ocurrencia del trámite está pendiente/abierta y la operación se ejecuta al retroceder un trámite abierto
                            insertar = true;
                            
                        if(insertar){
                            elemento.put("RETROCEDER",gVO);
                            salida.put(ordenNuevo,elemento);
                        }
                        /** prueba **/
                        
                        //elemento.put("RETROCEDER",gVO);
                    }else
                    if(avz==1){
                       elemento.put("AVANZAR", gVO);
                       salida.put(ordenNuevo,elemento);
                    }else
                    if(avz==2){
                       elemento.put("INICIAR",gVO);
                       salida.put(ordenNuevo,elemento);
                    }

                    // ORIGINAL
                    //salida.put(ordenNuevo,elemento);
                }// else

            }// while

		} catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER LA LISTA DE SW AV/RET DEL TRAMITE");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER LA LISTA DE SW AV/RET DEL TRAMITE", bde);
		} catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE SW AV/RET DEL TRAMITE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE SW AV/RET DEL TRAMITE", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(st);
            devolverConexion(abd, con);
        }

        return salida;
    }




    /**
     * Recupera la lista de operaciones sean de WS o de módulos externos que han fallado y están pendientes de ser ejecutadas
     * @param codMunicipio: Cod. de municipio
     * @param codProcedimiento: Cod. del procedimiento
     * @param codTramite: Cod. del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param numExpediente: Nº de expediente
     * @param params: Parámetros de conexión a la BD
     * @return Hashtable<Integer,Hashtable<String,GeneralValueObject>>
     * @throws es.altia.common.exception.TechnicalException
     * @throws es.altia.util.exceptions.InternalErrorException
     */
    public Hashtable<Integer,Hashtable<String,GeneralValueObject>> getListaConfSWPendientes (int codMunicipio, String codProcedimiento, int codTramite,int ocurrenciaTramite,String numExpediente,String[] params) throws TechnicalException, InternalErrorException{
    	//DEVUELVE LA LISTA DE PARES AV/RET DEL TRAMITE CORRESPONDIENTE TODOS SEGUIDOS
    	//ORDENADOS POR EL ORDEN DE EJECUCION Y PRIMERO EL DE RETROCEDER Y DESPUES EL DE AVANZAR
    	//SE ENCARGA EL MANAGER DE CONSTRUIR LA LISTA DE PARES PROPIAMENTE DICHA

        Hashtable<Integer,Hashtable<String,GeneralValueObject>> salida = new Hashtable<Integer,Hashtable<String,GeneralValueObject>>();
        
        String sqlListaConfSW = "SELECT DEF_TRA_OP,DEF_TRA_ORD,DEF_TRA_AVZ, DEF_TRA_CFO,DEF_TRA_TIPO_ORIGEN_OPERACION,ID FROM DEF_TRA_SW,TAREAS_PENDIENTES_INICIO WHERE " +
                            "DEF_TRA_MUN =" + codMunicipio + " AND DEF_TRA_PRO = '" + codProcedimiento + "' AND DEF_COD_TRA = " + codTramite + " " + 
                            "AND DEF_TRA_CFO = TAREAS_PENDIENTES_INICIO.COD_OPERACION AND DEF_TRA_AVZ=2 " +
                            "AND TAREAS_PENDIENTES_INICIO.NUM_EXPEDIENTE='" + numExpediente + "' " +
                            "AND TAREAS_PENDIENTES_INICIO.COD_MUNICIPIO=" + codMunicipio + " AND TAREAS_PENDIENTES_INICIO.COD_TRAMITE=" + codTramite + " " +
                            "AND TAREAS_PENDIENTES_INICIO.OCU_TRAMITE=" + ocurrenciaTramite + " ORDER BY DEF_TRA_ORD,DEF_TRA_AVZ";

        if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LA LISTA DE SW INI/AV/RET DEL TRAMITE");
	    if (m_Log.isDebugEnabled()) m_Log.debug(sqlListaConfSW);
        
    	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
			con = abd.getConnection();

            st  = con.createStatement();
            rs  = st.executeQuery(sqlListaConfSW);
            int avz = -1;
            long cfo = -1;
            int i=1;
            int ordenNuevo = 0;
			while (rs.next()) {
				i = 1;
                int idTarea       = rs.getInt("ID");
				int codOp         = rs.getInt("DEF_TRA_OP");
				avz               = rs.getInt("DEF_TRA_AVZ");
				cfo               = rs.getLong("DEF_TRA_CFO");
                ordenNuevo        = rs.getInt("DEF_TRA_ORD");
                String tipoOrigen = rs.getString("DEF_TRA_TIPO_ORIGEN_OPERACION");

                m_Log.debug("cfo: " + cfo + ",codOp: " + codOp + ",avz: " + avz + ",idTarea: " + idTarea +  ",tipoOrigen: " + tipoOrigen);

                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("COD_OP",codOp);
                gVO.setAtributo("CFO", cfo);
                gVO.setAtributo("ID_TAREA_PENDIENTE",idTarea);
                gVO.setAtributo("TIPO_OPERACION",tipoOrigen);

                if(!salida.containsKey(ordenNuevo)){
                    Hashtable<String,GeneralValueObject> aux = new Hashtable<String, GeneralValueObject>();
                    if(avz==0){
                        aux.put("RETROCEDER",gVO);
                    }else
                    if(avz==1){
                       aux.put("AVANZAR", gVO);
                    }else
                    if(avz==2){
                       aux.put("INICIAR",gVO);
                    }
                    salida.put(ordenNuevo,aux);
                }else{
                    Hashtable<String,GeneralValueObject> elemento = salida.get(ordenNuevo);
                    if(avz==0){
                        elemento.put("RETROCEDER",gVO);
                    }else
                    if(avz==1){
                       elemento.put("AVANZAR", gVO);
                    }else
                    if(avz==2){
                       elemento.put("INICIAR",gVO);
                    }

                    salida.put(ordenNuevo,elemento);
                }// else

            }// while

		} catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER LA LISTA DE SW AV/RET DEL TRAMITE");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER LA LISTA DE SW AV/RET DEL TRAMITE", bde);
		} catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE SW AV/RET DEL TRAMITE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE SW AV/RET DEL TRAMITE", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(st);
            devolverConexion(abd, con);
        }

        return salida;
    }


    
    private void reSort(int codMunicipio, String codProcedimiento, int codTramite,
    		int orden, Connection con) throws TechnicalException, InternalErrorException {
    	
    	String sqlResort = "UPDATE DEF_TRA_SW SET DEF_TRA_ORD = DEF_TRA_ORD - 1 WHERE DEF_TRA_ORD > ? " +
    			"AND DEF_TRA_MUN = ? AND DEF_TRA_PRO = ? AND DEF_COD_TRA = ?";
    	
    	
       
        PreparedStatement ps = null;
        ResultSet rs = null;      	
        try {
			

			ps = con.prepareStatement(sqlResort);
			if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER EL NUEVO ORDEN DE EJECUCION");
			if (m_Log.isDebugEnabled()) m_Log.debug(sqlResort);
			if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Orden del eliminado: " + orden);
			if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de Municipio: " + codMunicipio);
			if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo de Procedimiento: " + codProcedimiento);
			if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Codigo de Tramite: " + codTramite);
			

			int i = 1;
			ps.setInt(i++, orden);
			ps.setInt(i++, codMunicipio);
			ps.setString(i++, codProcedimiento);
			ps.setInt(i, codTramite);
								
			ps.executeUpdate();
			
		
		} catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER EL NUEVO ORDEN DE EJECUCION");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER EL NUEVO ORDEN DE EJECUCION", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
           
        }    	
    }
    
    public void removeWSyOrdena(int codMunicipio, String codProcedimiento, int codTramite,
    		int orden, String [] params) throws TechnicalException, InternalErrorException {
    	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
    	Connection con = null;
    	
		try {
			con = abd.getConnection();    	
			abd.inicioTransaccion(con);

            boolean borrado = ModuloIntegracionExternoDAO.getInstance().borrarTareasPendientesAsociadasTramite(codMunicipio,codTramite,orden,codProcedimiento,con);
            if(borrado) {
                Vector<Long> cfos = getCfos(codMunicipio, codProcedimiento, codTramite, orden, con);
                removeWS(codMunicipio, codProcedimiento, codTramite, orden, con);
                for (Long cfo : cfos) {
                    DefinicionOperacionesSWDAO.getInstance().removeParamsSW(cfo, con);
                }

                // Si se han borrado las tareas pendientes de inicio que haya para operaciones del trámite de un determinado orden
                reSort(codMunicipio, codProcedimiento, codTramite,
                        orden,con);                
            }else
                rollBackTransaction(abd,con);

		} catch (Exception e) {
			rollBackTransaction(abd,con);
			e.printStackTrace();
			throw new TechnicalException("ERROR AL ELIMINAR EL SW",e);					
		} 
		commitTransaction(abd,con);
		devolverConexion(abd,con);
    }

    
    public void updateWS(long cfoAv,long cfoRet,long cfoIni, int codMunicipio, String codProcedimiento, int codTramite,
    		int orden, int codOpAv,int codOpRet,int codOpIni, String tipoOperacionAvanzar,String tipoOperacionRetroceder,String tipoOperacionIniciar, String tituloOperacionAvanzar,String tituloOperacionRetroceder,String tituloOperacionIniciar,String nombreModuloAvanzar,String nombreModuloRetroceder, String nombreModuloIniciar, int codTipoRetrocesoSW, String[] params)
    		throws TechnicalException, InternalErrorException {
    	
    	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
    	Connection con = null;
    	boolean noHayAvanzar = false;
    	boolean noHayRetroceder = false;
        boolean noHayIniciar = false;
    	
		try {
			con = abd.getConnection();    	
			abd.inicioTransaccion(con);
			DefinicionOperacionesSWDAO defDAO = DefinicionOperacionesSWDAO.getInstance();
			
			if (cfoAv==-1){
				if (codOpAv!=-1){					
					long newCfoAv = getMaxCodSW(con)+1;

                   
					insertarSW(newCfoAv,codMunicipio,codProcedimiento,codTramite,1,codOpAv,orden,tipoOperacionAvanzar,tituloOperacionAvanzar,nombreModuloAvanzar,-1,con);
					if(tipoOperacionAvanzar!=null && "WS".equals(tipoOperacionAvanzar)){
                        Vector paramsInAv = defDAO.getParamsEntradaTramitacion(codOpAv, con);
                        Vector paramsOutAv = defDAO.getParamsSalidaTramitacion(codOpAv, con);
                        defDAO.creaEstructura(newCfoAv,paramsInAv,paramsOutAv,con);
                    }
				}
				else noHayAvanzar = true;
			}
			else {
				removeByCfo(cfoAv,con);
				DefinicionOperacionesSWDAO.getInstance().removeParamsSW(cfoAv,con);
	    		
				if (codOpAv!=-1) {
					insertarSW(cfoAv,codMunicipio,codProcedimiento,codTramite,1,codOpAv,orden,tipoOperacionAvanzar,tituloOperacionAvanzar,nombreModuloAvanzar,-1,con);
					if(tipoOperacionAvanzar!=null && "WS".equals(tipoOperacionAvanzar)){
                        Vector paramsInAv = defDAO.getParamsEntradaTramitacion(codOpAv, con);
                        Vector paramsOutAv = defDAO.getParamsSalidaTramitacion(codOpAv, con);
                        defDAO.creaEstructura(cfoAv,paramsInAv,paramsOutAv,con);
                    }
				}
				else noHayAvanzar = true;
				
			}
            
			if (cfoRet==-1){
				if (codOpRet!=-1){					
					long newCfoRet = getMaxCodSW(con)+1;
					insertarSW(newCfoRet,codMunicipio,codProcedimiento,codTramite,0,codOpRet,orden,tipoOperacionRetroceder,tituloOperacionRetroceder,nombreModuloRetroceder,codTipoRetrocesoSW,con);
					if(tipoOperacionRetroceder!=null && "WS".equals(tipoOperacionRetroceder)){
                        Vector paramsInRet = defDAO.getParamsEntradaTramitacion(codOpRet, con);
                        Vector paramsOutRet = defDAO.getParamsSalidaTramitacion(codOpRet, con);
                        defDAO.creaEstructura(newCfoRet,paramsInRet,paramsOutRet,con);
                    }
				}
				else noHayRetroceder = true;
			}
			else {
				removeByCfo(cfoRet,con);
				DefinicionOperacionesSWDAO.getInstance().removeParamsSW(cfoRet,con);
	    		
				if (codOpRet!=-1) {
					insertarSW(cfoRet,codMunicipio,codProcedimiento,codTramite,0,codOpRet,orden,tipoOperacionRetroceder,tituloOperacionRetroceder,nombreModuloRetroceder,codTipoRetrocesoSW,con);
					if(tipoOperacionRetroceder!=null && "WS".equals(tipoOperacionRetroceder)){
                        Vector paramsInRet = defDAO.getParamsEntradaTramitacion(codOpRet, con);
                        Vector paramsOutRet = defDAO.getParamsSalidaTramitacion(codOpRet, con);
                        defDAO.creaEstructura(cfoRet,paramsInRet,paramsOutRet,con);
                    }
				}
				else noHayRetroceder = true;
			}    		


            if (cfoIni==-1){
				if (codOpIni!=-1){
					long newCfoIni = getMaxCodSW(con)+1;
					insertarSW(newCfoIni,codMunicipio,codProcedimiento,codTramite,2,codOpIni,orden,tipoOperacionIniciar,tituloOperacionIniciar,nombreModuloIniciar,-1,con);
					if(tipoOperacionRetroceder!=null && "WS".equals(tipoOperacionRetroceder)){
                        Vector paramsInRet = defDAO.getParamsEntradaTramitacion(codOpIni, con);
                        Vector paramsOutRet = defDAO.getParamsSalidaTramitacion(codOpIni, con);
                        defDAO.creaEstructura(newCfoIni,paramsInRet,paramsOutRet,con);
                    }
				}
				else noHayIniciar = true;
			}
			else {
				removeByCfo(cfoIni,con);
				DefinicionOperacionesSWDAO.getInstance().removeParamsSW(cfoIni,con);

				if (codOpIni!=-1) {
					insertarSW(cfoIni,codMunicipio,codProcedimiento,codTramite,2,codOpIni,orden,tipoOperacionIniciar,tituloOperacionIniciar,nombreModuloIniciar,-1,con);
					if(tipoOperacionIniciar!=null && "WS".equals(tipoOperacionIniciar)){
                        Vector paramsInRet = defDAO.getParamsEntradaTramitacion(codOpIni, con);
                        Vector paramsOutRet = defDAO.getParamsSalidaTramitacion(codOpIni, con);
                        defDAO.creaEstructura(cfoIni,paramsInRet,paramsOutRet,con);
                    }
				}
				else noHayIniciar = true;
			}


    		
    		//if (noHayAvanzar&&noHayRetroceder) {
            if (noHayAvanzar&&noHayRetroceder&&noHayIniciar) {
    			reSort(codMunicipio,codProcedimiento,codTramite,orden,con);
    		}
    		


		} catch (Exception e) {
			rollBackTransaction(abd,con);
			e.printStackTrace();
			throw new TechnicalException("ERROR AL MODIFICAR EL SW",e);					
		} 
		commitTransaction(abd,con);
		devolverConexion(abd,con);
    }       

    public boolean haySW(int codMunicipio, String codProcedimiento, int codTramite,int orden, 
    		Connection con) throws TechnicalException, InternalErrorException{
    	String sqlHaySW = "SELECT * FROM DEF_TRA_SW WHERE DEF_TRA_MUN = ? AND DEF_TRA_PRO = ? AND DEF_COD_TRA = ? AND" +
    			"DEF_TRA_ORD = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;    
        try {
		

			ps = con.prepareStatement(sqlHaySW);
			if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER SI HAY UN SW CON ESAS CARACTERISTICAS");
			if (m_Log.isDebugEnabled()) m_Log.debug(sqlHaySW);			
						
			rs = ps.executeQuery();			
			return(rs.next());
			

		} catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER SI HAY UN SW CON ESAS CARACTERISTICAS");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER SI HAY UN SW CON ESAS CARACTERISTICAS", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
         
        }        
    }    	
    
    public String getTitulo(int codOp,String[] params) throws TechnicalException, InternalErrorException{
    	
    	String sqlgetTitulo = "SELECT OPS_DEF_TIT FROM SW_OPS_DEF WHERE OPS_DEF_COD = ?";

    	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;    
        try {
        	con = abd.getConnection();

			ps = con.prepareStatement(sqlgetTitulo);
			if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER EL TITULO DE UNA OPERACION");
			if (m_Log.isDebugEnabled()) m_Log.debug(sqlgetTitulo);			
			
			ps.setInt(1, codOp);
			
			rs = ps.executeQuery();
			if (rs.next()) return(rs.getString(1));
			else return "";
			
		} catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER EL TITULO DE UNA OPERACION");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER EL TITULO DE UNA OPERACION", bde);
		} catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER EL TITULO DE UNA OPERACION");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER EL TITULO DE UNA OPERACION", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }
    
    private void removeByCfo(long cfo, Connection con) throws TechnicalException, InternalErrorException{
    	
        String sqlRemoveWS = "DELETE DEF_TRA_SW WHERE DEF_TRA_CFO = ?";   
        
        
        PreparedStatement ps = null;
        ResultSet rs = null;    
        try {
			

			ps = con.prepareStatement(sqlRemoveWS);
			if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ELIMINAR EL SW");
			if (m_Log.isDebugEnabled()) m_Log.debug(sqlRemoveWS);			
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Cfo: " + cfo);

            
			int i=1;		
            ps.setLong(i, cfo);            
            int rowsDeleted = ps.executeUpdate();
            m_Log.debug("**** Nº de filas borradas " + rowsDeleted);
            
		} catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA ELIMINAR EL SW");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA ELIMINAR EL SW", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
           
        }                                                                                     
    }

    public Vector<CampoValueObject> getListaCampos(int codMunicipio, String codProcedimiento, boolean mostrarTramites, int codTramite,
                                                   String[] params)
    throws TechnicalException, InternalErrorException {

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;

        Config common = ConfigServiceHelper.getConfig("common");
        String codAplicacion = "4";

        try {
            conexion = abd.getConnection();

            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT CI.NOMEAS AS CODIGO,CI.NOME AS NOME, 'V_CRO' AS TABLA")
                    .append(" FROM CAMPOINFORME CI, CAMPOSELECCIONINFORME CSI, ESTRUCTURAINFORME ESI, A_DOC ")
                    .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" (A_DOC.DOC_CEI = 1 OR A_DOC.DOC_CEI = 2) AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                    .append(" A_DOC.DOC_CEI = CSI.COD_ESTRUCTURA AND ")
                    .append(" CSI.COD_CAMPOINFORME = CI.CODIGO AND CI.CAMPO NOT LIKE 'V_INT%'")
                    .append(" UNION SELECT PCA_COD AS CODIGO,PCA_ROT AS NOME, TDA_TAB AS TABLA ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_PCA, E_TDA ")
                    .append(" WHERE E_PCA.PCA_TDA = E_TDA.TDA_COD AND ")
                    .append(" PCA_MUN = ").append(codMunicipio).append(" AND ")
                    .append(" PCA_PRO = '").append(codProcedimiento).append("' AND ")
                    .append(" PCA_ACTIVO = 'SI' AND")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 1 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                    .append(" E_PCA.PCA_PLT<>").append(common.getString("E_PLT.CodigoPlantillaFichero"))
                    .append(" UNION SELECT ")
                    .append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'T_'", abd.convertir("TCA_TRA", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null), "'_'", "TCA_COD"}))
                    .append(" AS CODIGO, TCA_ROT AS NOME,")
                    .append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"TDA_TAB", "'T'"})).append(" AS TABLA ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_TCA, E_TDA ")
                    .append(" WHERE E_TCA.TCA_TDA = E_TDA.TDA_COD AND ")
                    .append(" TCA_MUN = ").append(codMunicipio).append(" AND ")
                    .append(" TCA_PRO = '").append(codProcedimiento).append("' AND ");
            if (!mostrarTramites) sql.append(" TCA_TRA = ").append(codTramite).append(" AND");
            sql.append(" TCA_ACTIVO = 'SI' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 1 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                    .append(" E_TCA.TCA_PLT<>").append(common.getString("E_PLT.CodigoPlantillaFichero"))
                    .append(" UNION SELECT ROL_DES AS CODIGO,").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'NombreInteresado'", "ROL_DES"}))
                    .append(" AS NOME, 'INT' AS TABLA ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(codMunicipio).append(" AND ")
                    .append(" ROL_PRO = '").append(codProcedimiento).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT ").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'Dom'", "ROL_DES"})).append(" AS CODIGO,").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'DomicilioInteresado'", "ROL_DES"})).append("AS NOME, 'INT' AS TABLA ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(codMunicipio).append(" AND ")
                    .append(" ROL_PRO = '").append(codProcedimiento).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT ").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'Rol'", "ROL_DES"})).append(" AS CODIGO,").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'RolInteresado'", "ROL_DES"})).append(" AS NOME, 'INT' AS TABLA ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(codMunicipio).append(" AND ")
                    .append(" ROL_PRO = '").append(codProcedimiento).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT ").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'Doc'", "ROL_DES"})).append(" AS CODIGO,").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'DocumInteresado'", "ROL_DES"})).append(" AS NOME, 'INT' AS TABLA ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(codMunicipio).append(" AND ")
                    .append(" ROL_PRO = '").append(codProcedimiento).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT ").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'Pob'", "ROL_DES"})).append(" AS CODIGO,").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'PoblacionInteresado'", "ROL_DES"})).append(" AS NOME, 'INT' AS TABLA ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(codMunicipio).append(" AND ")
                    .append(" ROL_PRO = '").append(codProcedimiento).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                            /* Kr */
                    .append(" UNION SELECT ").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'Tlfno'", "ROL_DES"})).append(" AS CODIGO,").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'TelefonoInteresado'", "ROL_DES"})).append(" AS NOME, 'INT' AS TABLA ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(codMunicipio).append(" AND ")
                    .append(" ROL_PRO = '").append(codProcedimiento).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    //Añadir el codigo externo del tercero
                    .append(" UNION SELECT ").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'codExterno'", "ROL_DES"})).append(" AS CODIGO,").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"'codExternoInteresado'", "ROL_DES"})).append(" AS NOME, 'INT' AS TABLA ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(codMunicipio).append(" AND ")
                    .append(" ROL_PRO = '").append(codProcedimiento).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" ORDER BY 2");

            ps = conexion.prepareStatement(sql.toString());
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = ps.executeQuery();

            Vector<CampoValueObject> listaCampos = new Vector<CampoValueObject>();
            while (rs.next()) {
                CampoValueObject campoVO = new CampoValueObject();
                campoVO.setOrigen(rs.getString("CODIGO"));
                campoVO.setTitulo(rs.getString("NOME"));
                campoVO.setTituloOriginal(rs.getString("NOME"));
                campoVO.setTabla(rs.getString("TABLA"));

                listaCampos.add(campoVO);
            }

            ps.close();
            rs.close();

            for (CampoValueObject campoVO : listaCampos) {

                if (campoVO.getTabla().equals("E_TDE")) {
                    String sql2 = " SELECT PCA_DESPLEGABLE FROM E_PCA WHERE PCA_COD='" + campoVO.getTitulo() +
                            "' AND PCA_PRO='" + codProcedimiento + "' AND PCA_MUN=" + codMunicipio;
                    if (m_Log.isDebugEnabled()) m_Log.debug(sql2);
                    ps2 = conexion.prepareStatement(sql2);
                    rs2 = ps2.executeQuery();

                    if (rs2.next()) campoVO.setValor2Criterio(rs2.getString("PCA_DESPLEGABLE"));

                    rs2.close();
                    ps2.close();

                } else if (campoVO.getTabla().equals("E_TDET")) {
                    String sql2 = " SELECT TCA_DESPLEGABLE FROM E_TCA WHERE TCA_COD='" + campoVO.getTitulo() +
                            "' AND TCA_PRO='" + codProcedimiento + "' AND TCA_MUN=" + codMunicipio;
                    if (m_Log.isDebugEnabled()) m_Log.debug(sql2);
                    ps2 = conexion.prepareStatement(sql2);
                    rs2 = ps2.executeQuery();

                    if (rs2.next()) campoVO.setValor2Criterio(rs2.getString("TCA_DESPLEGABLE"));

                    rs2.close();
                    ps2.close();
                }

            }

            return listaCampos;

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER EL TITULO DE UNA OPERACION");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA OBTENER EL TITULO DE UNA OPERACION", bde);
		} catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER EL TITULO DE UNA OPERACION");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER EL TITULO DE UNA OPERACION", sqle);
        } finally {

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            GeneralOperations.closeResultSet(rs2);
            GeneralOperations.closeStatement(ps2);

            devolverConexion(abd, conexion);
        }
    }


    private void devolverConexion(AdaptadorSQLBD abd, Connection con) throws InternalErrorException {
        try {
            if (con != null) abd.devolverConexion(con);
        } catch (BDException bde) {
            m_Log.error("ERROR AL DEVOLVER LA CONEXION A LA BASE DE DATOS", bde);
            throw new InternalErrorException(bde);
        }

    }

    private void commitTransaction(AdaptadorSQLBD abd, Connection con) throws InternalErrorException {
        try {
            if (con != null) abd.finTransaccion(con);
        } catch (BDException bde) {
            m_Log.error("ERROR AL FINALIZAR LA TRANSACCION EN LA CONEXION A LA BASE DE DATOS", bde);
            throw new InternalErrorException(bde);
        }
    }

    private void rollBackTransaction(AdaptadorSQLBD abd, Connection con) throws InternalErrorException {
        try {
            if (con != null) abd.rollBack(con);
        } catch (BDException bde) {
            m_Log.error("ERROR AL HACER ROLLBACK DE  LA TRANSACCION EN LA CONEXION A LA BASE DE DATOS", bde);
            throw new InternalErrorException(bde);
        }
    }



     /**
     * Recupera el título o nombre de la operación de un servicio web
     * @param codMunicipio: Código del municipio u organización
     * @param codTramite: Código del trámite
     * @param codOperacion: Código de la operación
     * @param con: Conexión a la BBDD
     * @return String con el nombre
     */
    public String getTituloOperacionWS(int codMunicipio,int codTramite,long codOperacion,Connection con){
        String nombre = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "SELECT OPS_SW_NOM " +
                         "FROM DEF_TRA_SW,SW_OPS_DEF " +
                         "WHERE DEF_TRA_CFO=" + codOperacion + " AND DEF_TRA_MUN=" + codMunicipio + " AND DEF_COD_TRA=" + codTramite  + " " +
                         "AND DEF_TRA_TIPO_ORIGEN_OPERACION='WS' " +
                         "AND DEF_TRA_OP=OPS_DEF_COD ";
            m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                nombre = rs.getString("OPS_SW_NOM");
            }

        }catch(SQLException e){
            m_Log.error("Error al ejecutar getTituloOperacionWS: " + e.getMessage());
            nombre = null;
        }
        finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return nombre;
    }
}
