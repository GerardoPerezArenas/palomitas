package es.altia.agora.business.integracionsw.persistence.manual;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.integracionsw.*;
import es.altia.agora.business.integracionsw.util.TraductorTipoBasico;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.GeneralOperations;
import es.altia.util.persistance.GeneralValueObject;
import es.altia.common.exception.TechnicalException;

import java.util.Vector;
import java.util.Collection;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReconstruccionSWDAO {

    protected static Log m_Log = LogFactory.getLog(ReconstruccionSWDAO.class.getName());

    private static ReconstruccionSWDAO instance = null;

    protected ReconstruccionSWDAO() {
    }

    public static ReconstruccionSWDAO getInstance() {
        if (instance == null) {
            synchronized (ReconstruccionSWDAO.class) {
                if (instance == null) instance = new ReconstruccionSWDAO();
            }
        }
        return instance;
    }

    public OperacionServicioWebVO reconstruirOperacionVO(int codOpDef, String[] paramsBD)
            throws InternalErrorException, TechnicalException {

        String sqlGetInfoOperacion = "SELECT OPS.OPS_SW_NOM, OPS.OPS_SW_COD, OPS.OPS_SW_NS, OPS.OPS_SW_SAU, OPS.OPS_SW_STY " +
                "FROM SW_OPS OPS, SW_OPS_DEF OPS_DEF " +
                "WHERE OPS_DEF.OPS_SW_NOM = OPS.OPS_SW_NOM " +
                "AND OPS_DEF.OPS_SW_COD = OPS.OPS_SW_COD " +
                "AND OPS_DEF.OPS_DEF_COD = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(paramsBD);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetInfoOperacion);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LOS DATOS GENERALES DE UNA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetInfoOperacion);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de la Operacion Definida: " + codOpDef);

            ps.setInt(1, codOpDef);

            rs = ps.executeQuery();

            String nombreOp;
            int codigoSW;
            String namespaceOp;
            String soapActionUri;
            String opStyle;
            if (rs.next()) {
                int i = 1;
                nombreOp = rs.getString(i++);
                codigoSW = rs.getInt(i++);
                namespaceOp = rs.getString(i++);
                soapActionUri = rs.getString(i++);
                opStyle = rs.getString(i);
            } else throw new InternalErrorException(new Exception("ERROR AL NO ENCONTRARSE NINGUNA OPERACION PARA EL " +
                    "CODIGO DE OPERACION PASADO"));

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

            // Intentemos obtener los datos del parametro de salida.
            ParametroSWVO paramOut = reconstruyeParamOut(codigoSW, nombreOp, con);

            // Obtenemos los parametros de entrada.
            Vector paramIn = reconstruyeListaParamIn(codigoSW, nombreOp, con);

            return new OperacionServicioWebVO(nombreOp, paramOut, paramIn, namespaceOp, soapActionUri, opStyle);

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA RECONSTRUIR UNA OPERACION DEL SERVICIO WEB", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA RECONSTRUIR UNA OPERACION DEL SERVICIO WEB", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECONSTRUIR UNA OPERACION DEL SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECONSTRUIR UNA OPERACION DEL SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }


    }

    private Vector reconstruyeListaParamIn(int codigoSW, String nombreOp, Connection con)
            throws TechnicalException, InternalErrorException {

        String sqlGetInfoParamIn = "SELECT PRM_ORD, PRM_NOM, PRM_CLASS, PRM_TIP_COD FROM SW_PARAMS WHERE " +
                "PRM_IN_OUT = 1 AND PRM_SW_OP = ? AND PRM_SW_COD = ? ORDER BY PRM_ORD ASC";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sqlGetInfoParamIn);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER EL PARAMETRO DE ENTRADA DE UNA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetInfoParamIn);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Nombre de la Operacion: " + nombreOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo del Servicio Web: " + codigoSW);

            int i = 1;
            ps.setString(i++, nombreOp);
            ps.setInt(i, codigoSW);

            rs = ps.executeQuery();

            Vector<GeneralValueObject> listaInfoParam = new Vector<GeneralValueObject>();
            while (rs.next()) {
                i = 1;
                GeneralValueObject infoParam = new GeneralValueObject();
                infoParam.setAtributo("paramOrder", rs.getString(i++));
                infoParam.setAtributo("paramName", rs.getString(i++));
                infoParam.setAtributo("claseTipo", rs.getString(i++));
                infoParam.setAtributo("codigoTipo", rs.getString(i));
                listaInfoParam.add(infoParam);
            }

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

            Vector<ParametroSWVO> listaParamsIn = new Vector<ParametroSWVO>();
            for (i = 0; i < listaInfoParam.size(); i++) {
                GeneralValueObject infoParam = listaInfoParam.get(i);
                int claseTipo = Integer.parseInt((String)infoParam.getAtributo("claseTipo"));
                int codigoTipo = Integer.parseInt((String)infoParam.getAtributo("codigoTipo"));
                TipoServicioWebVO tipoParamIn = reconstruyeTipo(claseTipo, codigoTipo, con);
                ParametroSWVO paramIn = new ParametroSWVO(tipoParamIn, (String)infoParam.getAtributo("paramName"));
                listaParamsIn.add(paramIn);
            }

            return listaParamsIn;

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER EL PARAMETRO DE ENTRADA DE UNA OPERACION", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER EL PARAMETRO DE ENTRADA DE UNA " +
                    "OPERACION", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
        }
    }

    private ParametroSWVO reconstruyeParamOut(int codigoSW, String nombreOp, Connection con)
            throws TechnicalException, InternalErrorException {

        String sqlGetInfoParamOut = "SELECT PRM_CLASS, PRM_NOM, PRM_TIP_COD FROM SW_PARAMS WHERE PRM_ORD = 0 " +
                "AND PRM_IN_OUT = 0 AND PRM_SW_OP = ? AND PRM_SW_COD = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sqlGetInfoParamOut);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER EL PARAMETRO DE SALIDA DE UNA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetInfoParamOut);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Nombre de la Operacion: " + nombreOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo del Servicio Web: " + codigoSW);

            int i = 1;
            ps.setString(i++, nombreOp);
            ps.setInt(i, codigoSW);

            rs = ps.executeQuery();

            int codigoTipo;
            String nombreTipo;
            int claseTipo;
            if (rs.next()) {
                i = 1;
                claseTipo = rs.getInt(i++);
                nombreTipo = rs.getString(i++);
                codigoTipo = rs.getInt(i);
            } else throw new InternalErrorException(new Exception("ERROR AL NO ENCONTRARSE EN LA BASE DE DATOS EL " +
                    "PARAMETRO DE SALIDA QUE SE ESTA INTENTANDO BUSCAR"));

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

            TipoServicioWebVO tipoParamOut = reconstruyeTipo(claseTipo, codigoTipo, con);

            return new ParametroSWVO(tipoParamOut, nombreTipo);

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER EL PARAMETRO DE SALIDA DE UNA OPERACION", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER EL PARAMETRO DE SALIDA DE UNA " +
                    "OPERACION", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
        }
    }

    private TipoServicioWebVO reconstruyeTipo(int claseTipo, int codigoTipo, Connection con)
            throws InternalErrorException, TechnicalException {

        if (claseTipo == TipoServicioWebVO.TIPO_BASE)
            return reconstruyeTipoBasico(codigoTipo);
        else if (claseTipo == TipoServicioWebVO.TIPO_ARRAY)
            return reconstruyeTipoArray(claseTipo, codigoTipo, con);
        else if (claseTipo == TipoServicioWebVO.TIPO_COMPLEJO)
            return reconstruyeTipoComplejo(claseTipo, codigoTipo, con);
        else throw new InternalErrorException(new Exception("ERROR EL VALOR DE LA CLASE DEL TIPO NO SE CORRESPONDE " +
                "CON NINGUNO DE LOS QUE EXISTAN DEFINIDOS"));
    }

    private TipoBasicoVO reconstruyeTipoBasico(int codigoTipo)
            throws InternalErrorException, TechnicalException {
        
        return new TipoBasicoVO(TraductorTipoBasico.getClave(codigoTipo));
    }

    private TipoArrayVO reconstruyeTipoArray(int claseTipo, int codigoTipo, Connection con)
            throws InternalErrorException, TechnicalException {

        String sqlGetNombreTipo = "SELECT TIP_NOM, TIP_CONT_CLASS, TIP_CONT_TIPO FROM SW_TIPO " +
                "WHERE TIP_COD = ? AND TIP_CLASE = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sqlGetNombreTipo);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER EL NOMBRE DE UN TIPO ARRAY");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetNombreTipo);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo del Tipo: " + codigoTipo);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Clase del Tipo: " + claseTipo);

            int i = 1;
            ps.setInt(i++, codigoTipo);
            ps.setInt(i, claseTipo);

            rs = ps.executeQuery();

            String nombreTipo;
            int codigoTipoContenido;
            int claseTipoContenido;
            if (rs.next()) {
                i = 1;
                nombreTipo = rs.getString(i++);
                claseTipoContenido = rs.getInt(i++);
                codigoTipoContenido = rs.getInt(i);
            } else throw new InternalErrorException(new Exception("ERROR AL NO ENCONTRARSE EN LA BASE DE DATOS EL " +
                    "TIPO QUE SE ESTA INTENTANDO BUSCAR"));

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

            TipoServicioWebVO tipoContenido = reconstruyeTipo(claseTipoContenido, codigoTipoContenido, con);

            return new TipoArrayVO(nombreTipo, tipoContenido);

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA BUSCAR LOS DATOS REFERENTES A UN TIPO ARRAY DEL SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA BUSCAR LOS DATOS REFERENTES A UN TIPO ARRAY" +
                    " DEL SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
        }
    }

    private TipoCompuestoVO reconstruyeTipoComplejo(int claseTipo, int codigoTipo, Connection con)
            throws InternalErrorException, TechnicalException {

        String sqlGetNombreTipo = "SELECT TIP_NOM, TIP_NS FROM SW_TIPO WHERE TIP_COD = ? AND TIP_CLASE = ?";

        String sqlGetInfoCampos = "SELECT CAMPO_NOM, CAMPO_TIPO, CAMPO_CLASE FROM SW_CAMPO_TIPO " +
                "WHERE CAMPO_TIP_COD = ? ORDER BY CAMPO_ORD ASC";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sqlGetInfoCampos);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LOS CAMPOS DE UN TIPO COMPUESTO");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetInfoCampos);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo del Tipo: " + codigoTipo);

            ps.setInt(1, codigoTipo);

            rs = ps.executeQuery();

            Vector<String> listaNombres = new Vector<String>();
            Vector<String> listaCodigosTipos = new Vector<String>();
            Vector<String> listaClasesTipos = new Vector<String>();
            while (rs.next()) {
                int i = 1;
                listaNombres.add(rs.getString(i++));
                listaCodigosTipos.add(rs.getString(i++));
                listaClasesTipos.add(rs.getString(i));
            }

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

            Vector<CampoTipoCompuestoVO> campos = new Vector<CampoTipoCompuestoVO>();
            for (int i = 0; i < listaNombres.size(); i++) {
                String nombreCampo = listaNombres.get(i);
                int claseTipoCampo = Integer.parseInt(listaClasesTipos.get(i));
                int codigoTipoCampo = Integer.parseInt(listaCodigosTipos.get(i));
                TipoServicioWebVO tipoCampo = reconstruyeTipo(claseTipoCampo, codigoTipoCampo, con);
                campos.add(new CampoTipoCompuestoVO(nombreCampo, tipoCampo));
            }

            // Buscaremos ahora la inforamcion general del tipo (nombre y namespace).
            ps = con.prepareStatement(sqlGetNombreTipo);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LA INFORMACION GENERAL DE UN TIPO COMPUESTO");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetNombreTipo);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo del Tipo: " + codigoTipo);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Clase del Tipo: " + claseTipo);

            int i = 1;
            ps.setInt(i++, codigoTipo);
            ps.setInt(i, claseTipo);

            rs = ps.executeQuery();

            String nombreTipo;
            String namespaceTipo;
            if (rs.next()) {
                i = 1;
                nombreTipo = rs.getString(i++);
                namespaceTipo = rs.getString(i);
            } else throw new InternalErrorException(new Exception("ERROR AL NO ENCONTRARSE EN LA BASE DE DATOS EL " +
                    "TIPO QUE SE ESTA INTENTANDO BUSCAR"));

            return new TipoCompuestoVO(nombreTipo, campos, namespaceTipo);

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA BUSCAR LOS DATOS REFERENTES A UN TIPO COMPUESTO DEL SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA BUSCAR LOS DATOS REFERENTES A UN TIPO COMPUESTO" +
                    " DEL SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
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

    public Collection<TipoServicioWebVO> getTiposSerializables(int codServicioWeb, String[] paramsBD)
    throws InternalErrorException, TechnicalException {

        String sqlGetListaTipos = "SELECT TIP_COD, TIP_CLASE FROM SW_TIPO WHERE TIP_SW_COD = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(paramsBD);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetListaTipos);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER UN LISTADO DE REFERENCIAS A TIPOS SERIALIZABLES");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetListaTipos);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo del Servicio Web: " + codServicioWeb);

            ps.setInt(1, codServicioWeb);

            rs = ps.executeQuery();

            Vector<int[]> listaIdsTipos = new Vector<int[]>();
            while (rs.next()) {
                int codigoTipo = rs.getInt(1);
                int claseTipo = rs.getInt(2);

                int[] idsTipo = {codigoTipo, claseTipo};
                listaIdsTipos.add(idsTipo);
            }

            rs.close();
            ps.close();

            Collection<TipoServicioWebVO> listaTipos = new ArrayList<TipoServicioWebVO>();
            for (int[] idsTipo: listaIdsTipos) {
                TipoServicioWebVO tipoSerializable = reconstruyeTipo(idsTipo[1], idsTipo[0], con);
                listaTipos.add(tipoSerializable);
            }

            return listaTipos;


        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA RECUPERAR LA LISTA DE TIPOS SERIALIZABLES DEL SERVICIO WEB", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA RECUPERAR LA LISTA DE TIPOS SERIALIZABLES DEL SERVICIO WEB", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR LA LISTA DE TIPOS SERIALIZABLES DEL SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR LA LISTA DE TIPOS SERIALIZABLES DEL SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }


}
