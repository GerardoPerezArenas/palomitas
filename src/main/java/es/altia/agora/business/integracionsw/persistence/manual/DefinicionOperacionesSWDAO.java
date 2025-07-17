package es.altia.agora.business.integracionsw.persistence.manual;

import es.altia.agora.business.integracionsw.AvanzarRetrocederSWVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.altia.agora.business.integracionsw.DefinicionArraySWVO;
import es.altia.agora.business.integracionsw.DefinicionOperacionVO;
import es.altia.agora.business.integracionsw.DefinicionParametroEntradaVO;
import es.altia.agora.business.integracionsw.DefinicionParametroSalidaVO;
import es.altia.agora.business.integracionsw.InfoConfTramSWVO;
import es.altia.agora.business.integracionsw.ParametroConfigurableVO;
import es.altia.agora.business.integracionsw.TipoServicioWebVO;
import es.altia.agora.business.integracionsw.exception.FalloPublicacionException;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.jdbc.GeneralOperations;
import es.altia.util.exceptions.InternalErrorException;
import java.sql.Statement;
import java.util.List;

public class DefinicionOperacionesSWDAO {

    protected static Log m_Log = LogFactory.getLog(DefinicionOperacionesSWDAO.class.getName());

    private static DefinicionOperacionesSWDAO instance = null;

    protected DefinicionOperacionesSWDAO() {}

    public static DefinicionOperacionesSWDAO getInstance() {
        if (instance == null) {
            synchronized (DefinicionOperacionesSWDAO.class) {
                if (instance == null) instance = new DefinicionOperacionesSWDAO();
            }
        }
        return instance;
    }

    public Collection<DefinicionOperacionVO> getOpsDefinidasBySW(int codigoSW, String[] params)
    throws InternalErrorException, TechnicalException {

        String sqlBuscaOperaciones = "SELECT OPS_DEF_COD, OPS_SW_NOM, OPS_DEF_TIT, OPS_DEF_PUB, OPS_DEF_EST " +
                "FROM SW_OPS_DEF WHERE OPS_SW_COD = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlBuscaOperaciones);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR UNA LISTA DE OPERACIONES DEFINIDAS PARA " +
                    "EL SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaOperaciones);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Servicio Web: " + codigoSW);

            ps.setInt(1, codigoSW);

            rs = ps.executeQuery();

            Collection<DefinicionOperacionVO> listaOps = new ArrayList<DefinicionOperacionVO>();
            while (rs.next()) {
                int i = 1;
                int codigoOpDefinida = rs.getInt(i++);
                String nombreOpWSDL = rs.getString(i++);
                String nombreOpDefinida = rs.getString(i++);
                boolean publicada = rs.getBoolean(i++);
                boolean estructuraDef = rs.getBoolean(i);
                listaOps.add(new DefinicionOperacionVO(codigoOpDefinida, nombreOpDefinida, publicada, codigoSW,
                        nombreOpWSDL, estructuraDef));
            }

            return listaOps;

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA RECUPERAR LAS OPERACIONES DEFINIDAS PARA UN SERVICIO WEB", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA RECUPERAR LAS OPERACIONES DEFINIDAS" +
                    " PARA UN SERVICIO WEB", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR LAS OPERACIONES DEFINIDAS PARA UN SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR LAS OPERACIONES DEFINIDAS PARA" +
                    " UN SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }

    }

    public boolean existeOperacionByTitulo(String tituloOp, int codigoSW, String[] params)
            throws InternalErrorException, TechnicalException {

        String sqlExisteOperacion = "SELECT OPS_DEF_COD FROM SW_OPS_DEF WHERE OPS_SW_COD = ? AND OPS_DEF_TIT = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlExisteOperacion);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA BUSCAR SI EXISTE UNA OPERACION DEFINIDA PARA " +
                    "EL SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlExisteOperacion);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Servicio Web: " + codigoSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Titulo de la Operacion Definida: " + tituloOp);

            int i = 1;
            ps.setInt(i++, codigoSW);
            ps.setString(i, tituloOp);

            rs = ps.executeQuery();

            return rs.next();

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA BUSCAR SI EXISTE UNA OPERACION DEFINIDA PARA UN SERVICIO WEB", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA BUSCAR SI EXISTE UNA OPERACION DEFINIDA" +
                    " PARA UN SERVICIO WEB", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA BUSCAR SI EXISTE UNA OPERACION DEFINIDA PARA UN SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA BUSCAR SI EXISTE UNA OPERACION DEFINIDA PARA" +
                    " UN SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }

    }

    public int creaEstructuraArray(int codigoSW, String nombreOpSW, String tituloOpSW, String[] params)
            throws TechnicalException, InternalErrorException {

        String sqlNuevoCodigoDef = "SELECT MAX(OPS_DEF_COD) FROM SW_OPS_DEF";

        String sqlInsertaDefOp = "INSERT INTO SW_OPS_DEF (OPS_DEF_COD, OPS_SW_NOM, OPS_SW_COD, OPS_DEF_TIT, " +
                "OPS_DEF_PUB, OPS_DEF_EST) VALUES (?, ?, ?, ?, ?, ?)";

        String sqlBuscaParamsEntOp = "SELECT PRM_ORD, PRM_NOM, PRM_CLASS, PRM_TIP_COD FROM SW_PARAMS " +
                "WHERE PRM_SW_OP = ? AND PRM_SW_COD = ? AND PRM_IN_OUT = 1";

        String sqlBuscaParamsSalOp = "SELECT PRM_NOM, PRM_CLASS, PRM_TIP_COD FROM SW_PARAMS " +
                "WHERE PRM_SW_OP = ? AND PRM_SW_COD = ? AND PRM_IN_OUT = 0";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            abd.inicioTransaccion(con);

            // Obtener el nuevo codigo de operacion definida.
            ps = con.prepareStatement(sqlNuevoCodigoDef);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER EL NUEVO CODIGO DE OPERACION DEFINIDA");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlNuevoCodigoDef);

            rs = ps.executeQuery();

            int nuevoCodigoOp = 0;
            if (rs.next()) {
                nuevoCodigoOp = rs.getInt(1);
            }
            nuevoCodigoOp++;

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

            // Insertar la definicion general de la operacion.
            ps = con.prepareStatement(sqlInsertaDefOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR LA DEFINICION GENERAL DE UNA OPERACION " +
                    "DEFINIDA PARA EL SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertaDefOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Operacion Definida: " + nuevoCodigoOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Nombre de la Operacion: " + nombreOpSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo de Servicio Web: " + codigoSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Titulo de la Operacion: " + tituloOpSW);

            int i = 1;
            ps.setInt(i++, nuevoCodigoOp);
            ps.setString(i++, nombreOpSW);
            ps.setInt(i++, codigoSW);
            ps.setString(i++, tituloOpSW);
            ps.setBoolean(i++, false);
            ps.setBoolean(i, false);

            int insertedRows = ps.executeUpdate();
            if (insertedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR " +
                        "LA DEFINICION GENERAL DE UNA OPERACION DEFINIDA PARA EL SERVICIO WEB");

            GeneralOperations.closeStatement(ps);

            // Buscamos las descripciones de los parametros de entrada y todos sus posibles campos.
            ps = con.prepareStatement(sqlBuscaParamsEntOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA BUSCAR LOS PARAMETROS DE ENTRADA DE UNA OPERACION " +
                    "DEFINIDA PARA EL SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaParamsEntOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Servicio Web: " + codigoSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Nombre de la Operacion: " + nombreOpSW);

            i = 1;
            ps.setString(i++, nombreOpSW);
            ps.setInt(i, codigoSW);

            rs = ps.executeQuery();

            Collection<GeneralValueObject> listaParams = new ArrayList<GeneralValueObject>();
            while (rs.next()) {
                i = 1;
                GeneralValueObject gVO = new GeneralValueObject();
                DefinicionArraySWVO definicion = new DefinicionArraySWVO();
                definicion.setCodigoParam(rs.getInt(i++));
                definicion.setCodigoOpDef(nuevoCodigoOp);
                definicion.setDescParamArray(rs.getString(i++));
                definicion.setEntradaSalida(1);
                definicion.setNumRepeticiones(-1);
                gVO.setAtributo("definicion", definicion);
                gVO.setAtributo("tipo_param", rs.getString(i++));
                gVO.setAtributo("codigo_tipo_param", rs.getString(i));
                listaParams.add(gVO);
            }

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

            // Obtenemos una descripcion detallada de todos los posibles campos de entrada del Servicio Web.
            ArrayList<DefinicionArraySWVO> listaDescParams = new ArrayList<DefinicionArraySWVO>();
            for (GeneralValueObject param : listaParams) {
                listaDescParams.addAll(extenderEstructuraArray(param, con));
            }

            // Buscaremos las descripciones de los parametros de salida y sus posibles campos.
            ps = con.prepareStatement(sqlBuscaParamsSalOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA BUSCAR LOS PARAMETROS DE SALIDA DE UNA OPERACION " +
                    "DEFINIDA PARA EL SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaParamsSalOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Servicio Web: " + codigoSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Nombre de la Operacion: " + nombreOpSW);

            i = 1;
            ps.setString(i++, nombreOpSW);
            ps.setInt(i, codigoSW);

            rs = ps.executeQuery();

            Collection<GeneralValueObject> listaParamsOut = new ArrayList<GeneralValueObject>();
            while (rs.next()) {
                i = 1;
                GeneralValueObject gVO = new GeneralValueObject();
                DefinicionArraySWVO definicion = new DefinicionArraySWVO();
                definicion.setCodigoParam(0);
                definicion.setCodigoOpDef(nuevoCodigoOp);
                definicion.setDescParamArray(rs.getString(i++));
                definicion.setEntradaSalida(0);
                definicion.setNumRepeticiones(-1);
                gVO.setAtributo("definicion", definicion);
                gVO.setAtributo("tipo_param", rs.getString(i++));
                gVO.setAtributo("codigo_tipo_param", rs.getString(i));
                listaParamsOut.add(gVO);
            }

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

            // Obtenemos una descripcion detallada de todos los posibles campos de entrada del Servicio Web.
            for (GeneralValueObject paramGVO: listaParamsOut) {
                listaDescParams.addAll(extenderEstructuraArray(paramGVO, con));
            }

            // Grabamos dichos campos en la base de datos.
            for (DefinicionArraySWVO defParam: listaDescParams) {
                grabaDefParamArray(defParam, con);
            }

            commitTransaction(abd, con);

            return nuevoCodigoOp;

        } catch (BDException bde) {
            rollBackTransaction(abd,con);
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA CREAR LA ESTRUCTURA DE DATOS DE UNA OPERACION DEFINIDA " +
                    "PARA UN SERVICIO WEB", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA CREAR LA ESTRUCTURA DE DATOS DE UNA " +
                    "OPERACION DEFINIDA PARA UN SERVICIO WEB", bde);
        } catch (SQLException sqle) {
            rollBackTransaction(abd,con);
            m_Log.error("ERROR EN LA CONSULTA PARA CREAR LA ESTRUCTURA DE DATOS DE UNA OPERACION DEFINIDA PARA UN " +
                    "SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA CREAR LA ESTRUCTURA DE DATOS DE UNA OPERACION " +
                    "DEFINIDA PARA UN SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public void creaEstructuraDatos(int codigoOpDef, String[] params) throws TechnicalException, InternalErrorException {

        String sqlGetDatosOp = "SELECT OPS_SW_NOM, OPS_SW_COD FROM SW_OPS_DEF WHERE OPS_DEF_COD = ?";

        String sqlBuscaParamsEntOp = "SELECT PRM_ORD, PRM_NOM, PRM_CLASS, PRM_TIP_COD FROM SW_PARAMS " +
                "WHERE PRM_SW_OP = ? AND PRM_SW_COD = ? AND PRM_IN_OUT = 1";

        String sqlBuscaParamsSalOp = "SELECT PRM_NOM, PRM_CLASS, PRM_TIP_COD FROM SW_PARAMS " +
                "WHERE PRM_SW_OP = ? AND PRM_SW_COD = ? AND PRM_IN_OUT = 0";

        String sqlUpdateExisteEst = "UPDATE SW_OPS_DEF SET OPS_DEF_EST = ? WHERE OPS_DEF_COD = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            abd.inicioTransaccion(con);

            // Buscamos los datos que nos hacen falta del servicio web.
            ps = con.prepareStatement(sqlGetDatosOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA BUSCAR LOS DATOS GENERALES DE LA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaParamsEntOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Operacion: " + codigoOpDef);

            ps.setInt(1, codigoOpDef);

            rs = ps.executeQuery();
            int codigoSW;
            String nombreOpSW;
            if (rs.next()) {
                nombreOpSW = rs.getString(1);
                codigoSW = rs.getInt(2);
            } else {
                throw new InternalErrorException(new Exception("ERROR: NO EXISTE UNA OPERACION DEFINIDA CON EL " +
                    "CODIGO " + codigoOpDef));
            }

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

            // Buscamos las descripciones de los parametros de entrada y todos sus posibles campos.
            ps = con.prepareStatement(sqlBuscaParamsEntOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA BUSCAR LOS PARAMETROS DE ENTRADA DE UNA OPERACION " +
                    "DEFINIDA PARA EL SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaParamsEntOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Servicio Web: " + codigoSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Nombre de la Operacion: " + nombreOpSW);

            int i = 1;
            ps.setString(i++, nombreOpSW);
            ps.setInt(i, codigoSW);

            rs = ps.executeQuery();

            Collection listaParams = new ArrayList();
            while (rs.next()) {
                i = 1;
                GeneralValueObject gVO = new GeneralValueObject();
                DefinicionParametroEntradaVO definicion = new DefinicionParametroEntradaVO();
                definicion.setCodigoParam(rs.getInt(i++));
                definicion.setCodigoOpDef(codigoOpDef);
                definicion.setCodigoSW(codigoSW);
                definicion.setNombreOpSW(nombreOpSW);
                definicion.setDescParam(rs.getString(i++));
                definicion.setEsObligatorio(false);
                definicion.setTipoParametro(1);
                gVO.setAtributo("parametro", definicion.getDescParam());
                gVO.setAtributo("definicion", definicion);
                gVO.setAtributo("tipo_param", rs.getString(i++));
                gVO.setAtributo("codigo_tipo_param", rs.getString(i));
                listaParams.add(gVO);
            }

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

            // Obtenemos una descripcion detallada de todos los posibles campos de entrada del Servicio Web.
            ArrayList listaDescParams = new ArrayList();
            Iterator itParams = listaParams.iterator();
            while (itParams.hasNext()) {
                GeneralValueObject paramGVO = (GeneralValueObject)itParams.next();
                listaDescParams.addAll(extenderEstructuraDatos(paramGVO, con));
            }

            // Grabamos dichos campos en la base de datos.
            Iterator itDefsParam = listaDescParams.iterator();
            while (itDefsParam.hasNext()) {
                DefinicionParametroEntradaVO defParam = (DefinicionParametroEntradaVO)itDefsParam.next();
                grabaDefParamIn(defParam, con);
            }

            // Buscaremos las descripciones de los parametros de salida y sus posibles campos.
            ps = con.prepareStatement(sqlBuscaParamsSalOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA BUSCAR LOS PARAMETROS DE SALIDA DE UNA OPERACION " +
                    "DEFINIDA PARA EL SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaParamsSalOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Servicio Web: " + codigoSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Nombre de la Operacion: " + nombreOpSW);

            i = 1;
            ps.setString(i++, nombreOpSW);
            ps.setInt(i, codigoSW);

            rs = ps.executeQuery();

            Collection listaParamsOut = new ArrayList();
            while (rs.next()) {
                i = 1;
                GeneralValueObject gVO = new GeneralValueObject();
                DefinicionParametroEntradaVO definicion = new DefinicionParametroEntradaVO();
                definicion.setCodigoParam(0);
                definicion.setCodigoOpDef(codigoOpDef);
                definicion.setCodigoSW(codigoSW);
                definicion.setNombreOpSW(nombreOpSW);
                definicion.setDescParam(rs.getString(i++));
                definicion.setEsObligatorio(false);
                definicion.setTipoParametro(1);
                gVO.setAtributo("parametro", definicion.getDescParam());
                gVO.setAtributo("definicion", definicion);
                gVO.setAtributo("tipo_param", rs.getString(i++));
                gVO.setAtributo("codigo_tipo_param", rs.getString(i));
                listaParamsOut.add(gVO);
            }

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

            // Obtenemos una descripcion detallada de todos los posibles campos de entrada del Servicio Web.
            ArrayList listaDescParamsOut = new ArrayList();
            Iterator itParamsOut = listaParamsOut.iterator();
            while (itParamsOut.hasNext()) {
                GeneralValueObject paramGVO = (GeneralValueObject)itParamsOut.next();
                listaDescParamsOut.addAll(extenderEstructuraDatos(paramGVO, con));
            }

            // Grabamos dichos campos en la base de datos.
            Iterator itDefsParamOut = listaDescParamsOut.iterator();
            while (itDefsParamOut.hasNext()) {
                DefinicionParametroEntradaVO defParam = (DefinicionParametroEntradaVO)itDefsParamOut.next();
                DefinicionParametroSalidaVO defParamOut = new DefinicionParametroSalidaVO();
                defParamOut.setCodOpDef(defParam.getCodigoOpDef());
                defParamOut.setCodigoSW(defParam.getCodigoSW());
                defParamOut.setNombreOpSW(defParam.getNombreOpSW());
                defParamOut.setDescParam(defParam.getDescParam());
                defParamOut.setTituloParam(defParam.getTituloParam());

                grabaDefParamOut(defParamOut, con);
            }

            // Actualizamos el estado de la estructura de entrada.
            ps = con.prepareStatement(sqlUpdateExisteEst);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ACTUALIZAR EL ESTADO DE LA ESTRUCTURA DE DATOS " +
                    "EN LA TABLA DE DEFINICION DE OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaParamsSalOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de la Operacion: " + codigoOpDef);

            ps.setBoolean(1, true);
            ps.setInt(2, codigoOpDef);

            int updatedRows = ps.executeUpdate();
            if (updatedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA ACTUALIZAR EL ESTADO DE " +
                        "LA ESTRUCTURA DE DATOS DE UNA OPERACION DEL SERVICIO WEB");

            commitTransaction(abd, con);

        } catch (BDException bde) {
            rollBackTransaction(abd,con);
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA CREAR LA ESTRUCTURA DE DATOS DE UNA OPERACION DEFINIDA " +
                    "PARA UN SERVICIO WEB", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA CREAR LA ESTRUCTURA DE DATOS DE UNA " +
                    "OPERACION DEFINIDA PARA UN SERVICIO WEB", bde);
        } catch (SQLException sqle) {
            rollBackTransaction(abd,con);
            m_Log.error("ERROR EN LA CONSULTA PARA CREAR LA ESTRUCTURA DE DATOS DE UNA OPERACION DEFINIDA PARA UN " +
                    "SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA CREAR LA ESTRUCTURA DE DATOS DE UNA OPERACION " +
                    "DEFINIDA PARA UN SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public int insertParam(long cfo,ParametroConfigurableVO param, Connection con) 
    		throws InternalErrorException, TechnicalException, SQLException {
    	String sqlInsertParam = "INSERT INTO CONF_TRA_SW(DEF_CFO_CONF, COD_ORD_CONF, " +
    			"DEF_PRM_CONF, TIP_DAT_CONF, COD_DAT_CONF, VAL_DAT_CONF) VALUES (?,?,?,?,?,?)";    	    	        
        
        PreparedStatement ps = null;
        ResultSet rs = null;                			

		ps = con.prepareStatement(sqlInsertParam);
		if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR EL PARAM");
		if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertParam);
		if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Clave SW: " + cfo);
        if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de Orden: " + param.getOrdenParam());            
        if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Nombre Parametro: " + param.getNombreDefinicion());
        if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Tipo de Dato: " + param.getTipoValorPaso());
        if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 5: Codigo del Campo del Expediente: " + param.getCodCampoExp());
        if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 6: Valor Constante: " + param.getValorConstante());
        
        
		int i=1;
		ps.setLong(i++, cfo);
        ps.setInt(i++,param.getOrdenParam() );            
        ps.setString(i++, param.getNombreDefinicion());
        ps.setInt(i++, param.getTipoValorPaso());
        ps.setString(i++,param.getCodCampoExp());
        ps.setString(i++, param.getValorConstante());
        

        int insertedRows = ps.executeUpdate();
        if (insertedRows != 1)
            throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR EL PARAMETRO");

        GeneralOperations.closeResultSet(rs);
        GeneralOperations.closeStatement(ps);                               	
    	
        return insertedRows;
    }
    
    public void removeParamsSW(long cfo,Connection con) throws InternalErrorException, TechnicalException{
    	String sqlDelParam = "DELETE CONF_TRA_SW WHERE DEF_CFO_CONF = ?";

       
       
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
           

            ps = con.prepareStatement(sqlDelParam);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ELIMINAR LOS PARAMETROS DE UN SW");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlDelParam);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo SW: " + cfo);            
            
            int i = 1;
			ps.setLong(i++, cfo);            
            
            ps.executeUpdate();
            
	   
	    } catch (SQLException sqle) {
	        m_Log.error("ERROR EN LA CONSULTA PARA ELIMINAR LOS PARAMETROS DE UN SW");
	        throw new TechnicalException("ERROR EN LA CONSULTA PARA ELIMINAR LOS PARAMETROS DE UN SW", sqle);
	    } finally {
	        GeneralOperations.closeResultSet(rs);
	        GeneralOperations.closeStatement(ps);
	       
	    }
        
    }

    private Collection<DefinicionArraySWVO> extenderEstructuraArray(GeneralValueObject gVO, Connection con)
            throws InternalErrorException, TechnicalException {

        String strTipoParam = (String)gVO.getAtributo("tipo_param");
        int tipoParam = Integer.parseInt(strTipoParam);
        String strCodTipoParam = (String)gVO.getAtributo("codigo_tipo_param");
        int codTipoParam = Integer.parseInt(strCodTipoParam);

        Collection<DefinicionArraySWVO> listaDescs = new ArrayList<DefinicionArraySWVO>();

        if (tipoParam == TipoServicioWebVO.TIPO_ARRAY) {
            DefinicionArraySWVO definicion = (DefinicionArraySWVO)gVO.getAtributo("definicion");
            definicion.setDescParamArray(definicion.getDescParamArray() + ".(ARRAY)");
            listaDescs.add(definicion);
            GeneralValueObject tipoContArray = obtenerTipoContenidoArray(tipoParam, codTipoParam, con);
            tipoContArray.setAtributo("definicion", definicion.copy());
            listaDescs.addAll(extenderEstructuraArray(tipoContArray, con));
        }

        if (tipoParam == TipoServicioWebVO.TIPO_COMPLEJO) {
            DefinicionArraySWVO definicion = (DefinicionArraySWVO)gVO.getAtributo("definicion");
            Collection camposDefs = obtenerCamposTipoCompuestoArray(definicion, codTipoParam, con);
            Iterator itCamposDefs = camposDefs.iterator();
            for (Object camposDef : camposDefs) {
                GeneralValueObject campoDefGVO = (GeneralValueObject) camposDef;
                listaDescs.addAll(extenderEstructuraArray(campoDefGVO, con));
            }

        }
        return listaDescs;
    }

    private Collection extenderEstructuraDatos(GeneralValueObject gVO, Connection con)
            throws InternalErrorException, TechnicalException {

        String strTipoParam = (String)gVO.getAtributo("tipo_param");
        int tipoParam = Integer.parseInt(strTipoParam);
        String strCodTipoParam = (String)gVO.getAtributo("codigo_tipo_param");
        int codTipoParam = Integer.parseInt(strCodTipoParam);

        Collection listaDescs = new ArrayList();

        if (tipoParam == TipoServicioWebVO.TIPO_BASE) {
            listaDescs.add(gVO.getAtributo("definicion"));
        }

        if (tipoParam == TipoServicioWebVO.TIPO_ARRAY) {
            DefinicionParametroEntradaVO definicion = (DefinicionParametroEntradaVO)gVO.getAtributo("definicion");
            String descParamBase = (String)gVO.getAtributo("parametro");
            int numInstancias = obtenerNumInstanciasArray(descParamBase + ".(ARRAY)", definicion.getCodigoOpDef(), con);
            GeneralValueObject tipoContArray = obtenerTipoContenidoArray(tipoParam, codTipoParam, con);
            tipoContArray.setAtributo("parametro", descParamBase + ".(ARRAY)");
            for (int i = 1; i <= numInstancias; i++) {
                DefinicionParametroEntradaVO copiaDef = definicion.copy();
                copiaDef.setDescParam(definicion.getDescParam() + "[" + createStrIndex(i, numInstancias) + "]");
                tipoContArray.setAtributo("definicion", copiaDef);
                listaDescs.addAll(extenderEstructuraDatos(tipoContArray, con));
            }                                    
        }

        if (tipoParam == TipoServicioWebVO.TIPO_COMPLEJO) {
            DefinicionParametroEntradaVO definicion = (DefinicionParametroEntradaVO)gVO.getAtributo("definicion");
            Collection camposDefs = obtenerCamposTipoCompuesto(definicion, codTipoParam, con);
            Iterator itCamposDefs = camposDefs.iterator();
            while (itCamposDefs.hasNext()) {
                GeneralValueObject campoDefGVO = (GeneralValueObject)itCamposDefs.next();
                DefinicionParametroEntradaVO defCampo = (DefinicionParametroEntradaVO)campoDefGVO.getAtributo("definicion");
                campoDefGVO.setAtributo("parametro", defCampo.getDescParam());
                listaDescs.addAll(extenderEstructuraDatos(campoDefGVO, con));
            }

        }
        return listaDescs;
    }

    private int obtenerNumInstanciasArray(String descParam, int codigoOpDef, Connection con) throws TechnicalException, InternalErrorException {

        String sqlGetNumInstancias = "SELECT DEF_ARR_VAL FROM SW_ARRAY_DEF WHERE OPS_DEF_COD = ? AND DEF_ARR_DES = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sqlGetNumInstancias);
            if (m_Log.isDebugEnabled())
                m_Log.debug("CONSULTA PARA OBTENER EL NUMERO DE INSTANCIAS DE UN ARRAY");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetNumInstancias);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Operacion: " + codigoOpDef);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Descripcion Array: " + descParam);

            ps.setInt(1, codigoOpDef);
            ps.setString(2, descParam);

            rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);
            else throw new InternalErrorException(new Exception("ERROR AL NO ENCONTRAR LA DEFINICION DEL ARRAY"));

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA BUSCAR EL NUMERO DE INSTANCIAS DEL TIPO CONTENIDO POR UN TIPO ARRAY", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA BUSCAR EL NUMERO DE INSTANCIAS DEL TIPO " +
                    "CONTENIDO POR UN TIPO ARRAY", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
        }
    }

    private GeneralValueObject obtenerTipoContenidoArray(int tipoParam, int codTipoParam, Connection con) throws TechnicalException, InternalErrorException {

        String sqlBuscaCodigoArray = "SELECT TIP_CONT_CLASS, TIP_CONT_TIPO FROM SW_TIPO " +
                "WHERE TIP_COD = ? AND TIP_CLASE = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sqlBuscaCodigoArray);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA BUSCAR EL CODIGO DEL TIPO CONTENIDO POR UN TIPO ARRAY");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaCodigoArray);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo del Tipo: " + codTipoParam);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Clase del Tipo: " + tipoParam);

            ps.setInt(1, codTipoParam);
            ps.setInt(2, tipoParam);

            rs = ps.executeQuery();

            GeneralValueObject datosTipoCont = new GeneralValueObject();
            if (rs.next()) {
                String claseTipoCont = rs.getString(1);
                String codTipoCont = rs.getString(2);
                datosTipoCont.setAtributo("tipo_param", claseTipoCont);
                datosTipoCont.setAtributo("codigo_tipo_param", codTipoCont);
            }

            return datosTipoCont;
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA BUSCAR EL CODIGO DEL TIPO CONTENIDO POR UN TIPO ARRAY", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA BUSCAR EL CODIGO DEL TIPO CONTENIDO" +
                    " POR UN TIPO ARRAY", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
        }
    }

    private Collection obtenerCamposTipoCompuesto(DefinicionParametroEntradaVO param, int codTipoParam, Connection con)
            throws TechnicalException, InternalErrorException {

        String sqlBuscaCamposCompuesto = "SELECT CAMPO_NOM, CAMPO_TIPO, CAMPO_CLASE FROM SW_CAMPO_TIPO " +
                "WHERE CAMPO_TIP_COD = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sqlBuscaCamposCompuesto);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA BUSCAR LOS CAMPOS DE UN TIPO COMPUESTO");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaCamposCompuesto);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo del Tipo: " + codTipoParam);

            ps.setInt(1, codTipoParam);

            rs = ps.executeQuery();
            Collection campos = new ArrayList();
            while (rs.next()) {
                int i = 1;
                GeneralValueObject camposGVO = new GeneralValueObject();
                DefinicionParametroEntradaVO copyParam = param.copy();
                copyParam.setDescParam(copyParam.getDescParam() + "." + rs.getString(i++));
                camposGVO.setAtributo("definicion", copyParam);
                camposGVO.setAtributo("codigo_tipo_param", rs.getString(i++));
                camposGVO.setAtributo("tipo_param", rs.getString(i));
                campos.add(camposGVO);
            }

            return campos;

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA BUSCAR LOS CAMPOS DE UN TIPO COMPUESTO", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA BUSCAR LOS CAMPOS DE UN TIPO COMPUESTO", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
        }
    }

    private Collection obtenerCamposTipoCompuestoArray(DefinicionArraySWVO param, int codTipoParam, Connection con)
            throws TechnicalException, InternalErrorException {

        String sqlBuscaCamposCompuesto = "SELECT CAMPO_NOM, CAMPO_TIPO, CAMPO_CLASE FROM SW_CAMPO_TIPO " +
                "WHERE CAMPO_TIP_COD = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sqlBuscaCamposCompuesto);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA BUSCAR LOS CAMPOS DE UN TIPO COMPUESTO");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaCamposCompuesto);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo del Tipo: " + codTipoParam);

            ps.setInt(1, codTipoParam);

            rs = ps.executeQuery();
            Collection campos = new ArrayList();
            while (rs.next()) {
                int i = 1;
                GeneralValueObject camposGVO = new GeneralValueObject();
                DefinicionArraySWVO copyParam = param.copy();
                copyParam.setDescParamArray(copyParam.getDescParamArray() + "." + rs.getString(i++));
                camposGVO.setAtributo("definicion", copyParam);
                camposGVO.setAtributo("codigo_tipo_param", rs.getString(i++));
                camposGVO.setAtributo("tipo_param", rs.getString(i));
                campos.add(camposGVO);
            }

            return campos;

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA BUSCAR LOS CAMPOS DE UN TIPO COMPUESTO", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA BUSCAR LOS CAMPOS DE UN TIPO COMPUESTO", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
        }
    }

    private void grabaDefParamIn(DefinicionParametroEntradaVO defParamIn, Connection con) throws TechnicalException, InternalErrorException {

        String sqlInsertDefParamIn = "INSERT INTO SW_PARAMS_IN_DEF(ORD_PRM_SW, OPS_DEF_COD, PRM_DEF_NOM, PRM_DEF_TIT, " +
                "PRM_DEF_OBL, PRM_DEF_TIP, PRM_DEF_VAL) VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sqlInsertDefParamIn);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR LA DEFINICION DE UN PARAMETRO DE ENTRADA");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertDefParamIn);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Orden del Parametro: " + defParamIn.getCodigoParam());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de Op. Definida: " + defParamIn.getCodigoOpDef());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Descripcion del Parametro: " + defParamIn.getDescParam());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Titulo del Parametro: " + defParamIn.getTituloParam());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 5: Es Obligatorio?: " + defParamIn.isEsObligatorio());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 6: Codigo Tipo Parametro: " + defParamIn.getTipoParametro());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 7: Valor Por Defecto: " + defParamIn.getValorDefecto());

            int i = 1;
            ps.setInt(i++, defParamIn.getCodigoParam());
            ps.setInt(i++, defParamIn.getCodigoOpDef());
            ps.setString(i++, defParamIn.getDescParam());
            ps.setString(i++, defParamIn.getTituloParam());
            ps.setBoolean(i++, defParamIn.isEsObligatorio());
            ps.setInt(i++, defParamIn.getTipoParametro());
            ps.setString(i, defParamIn.getValorDefecto());

            int insertedRows = ps.executeUpdate();
            if (insertedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR " +
                        "LA DEFINICION DE LOS PARAMETROS DE UNA OPERACION DEFINIDA PARA EL SERVICIO WEB");

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA INSERTAR LA DEFINICION DE LOS PARAMETROS DE UNA OPERACION " +
                    "DEFINIDA PARA EL SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA INSERTAR LA DEFINICION DE LOS PARAMETROS " +
                    "DE UNA OPERACION DEFINIDA PARA EL SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeStatement(ps);
        }
    }

    private void grabaDefParamArray(DefinicionArraySWVO defArray, Connection con) throws TechnicalException, InternalErrorException {

        String sqlInsertDefParamIn = "INSERT INTO SW_ARRAY_DEF(ORD_PRM_SW, OPS_DEF_COD, DEF_ARR_DES, DEF_ARR_IO, " +
                "DEF_ARR_VAL) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sqlInsertDefParamIn);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR LA DEFINICION DE UN ARRAY");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertDefParamIn);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Orden del Parametro: " + defArray.getCodigoParam());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de Op. Definida: " + defArray.getCodigoOpDef());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Descripcion del Array: " + defArray.getDescParamArray());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Tipo E/S: " + defArray.getEntradaSalida());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 5: Numero Repeticiones: " + defArray.getNumRepeticiones());

            int i = 1;
            ps.setInt(i++, defArray.getCodigoParam());
            ps.setInt(i++, defArray.getCodigoOpDef());
            ps.setString(i++, defArray.getDescParamArray());
            ps.setInt(i++, defArray.getEntradaSalida());
            ps.setInt(i, defArray.getNumRepeticiones());

            int insertedRows = ps.executeUpdate();
            if (insertedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR " +
                        "LA DEFINICION DE UN ARRAY DE UNA OPERACION DEFINIDA PARA EL SERVICIO WEB");

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA INSERTAR LA DEFINICION DE LOS PARAMETROS DE UN ARRAY " +
                    "DEFINIDA PARA EL SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA INSERTAR LA DEFINICION DE LOS PARAMETROS " +
                    "DE UN ARRAY DEFINIDA PARA EL SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeStatement(ps);
        }
    }

    private void grabaDefParamOut(DefinicionParametroSalidaVO defParamOut, Connection con)
            throws TechnicalException, InternalErrorException {

        String sqlInsertDefParamIn = "INSERT INTO SW_PARAMS_OUT_DEF(OPS_DEF_COD, PRM_DEF_NOM, PRM_DEF_TIT, " +
                "PRM_TIP_DAT, PRM_VAL_OK) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sqlInsertDefParamIn);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR LA DEFINICION DE UN PARAMETRO DE SALIDA");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertDefParamIn);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Op. Definida: " + defParamOut.getCodOpDef());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Descripcion del Parametro: " + defParamOut.getDescParam());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Titulo del Parametro: " + defParamOut.getTituloParam());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Tipo de Dato: " + defParamOut.getTipoDato());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 5: Valor Correcto: " + defParamOut.getValorCorrecto());

            int i = 1;
            ps.setInt(i++, defParamOut.getCodOpDef());
            ps.setString(i++, defParamOut.getDescParam());
            ps.setString(i++, defParamOut.getTituloParam());
            ps.setInt(i++, defParamOut.getTipoDato());
            ps.setString(i, defParamOut.getValorCorrecto());

            int insertedRows = ps.executeUpdate();
            if (insertedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR " +
                        "LA DEFINICION DE LOS PARAMETROS DE UNA OPERACION DEFINIDA PARA EL SERVICIO WEB");

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA INSERTAR LA DEFINICION DE LOS PARAMETROS DE UNA OPERACION " +
                    "DEFINIDA PARA EL SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA INSERTAR LA DEFINICION DE LOS PARAMETROS " +
                    "DE UNA OPERACION DEFINIDA PARA EL SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeStatement(ps);
        }
    }


    public GeneralValueObject getInfoGeneralOperacion(int codOpDef, String[] params) throws TechnicalException, InternalErrorException {

        String sqlBuscaInfoOp = "SELECT SW_WSDL, SW_COD, SW_TIT, OPS_DEF_TIT, OPS_SW_NOM FROM SW_INFO, SW_OPS_DEF " +
                "WHERE OPS_SW_COD = SW_COD AND OPS_DEF_COD = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlBuscaInfoOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LA INFORMACION GENERAL DE UNA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaInfoOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Op. Definida: " + codOpDef);

            ps.setInt(1, codOpDef);

            rs = ps.executeQuery();

            GeneralValueObject datosOpGVO = new GeneralValueObject();
            if (rs.next()) {
                int i = 1;
                datosOpGVO.setAtributo("wsdlSW", rs.getString(i++));
                datosOpGVO.setAtributo("codigoSW", rs.getString(i++));
                datosOpGVO.setAtributo("tituloSW", rs.getString(i++));
                datosOpGVO.setAtributo("tituloOp", rs.getString(i++));
                datosOpGVO.setAtributo("nombreOpSW", rs.getString(i));
            }

            return datosOpGVO;
        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA OBTENER LA INFORMACION GENERAL DE UNA OPERACION", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA OBTENER LA INFORMACION GENERAL" +
                    " DE UNA OPERACION", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LA INFORMACION GENERAL DE UNA OPERACION", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LA INFORMACION GENERAL" +
                    " DE UNA OPERACION", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public Collection<DefinicionParametroEntradaVO> getParamsDefEntrada(int codOpDef, String[] params)
    throws TechnicalException, InternalErrorException {

        String sqlGetListaParamsEntrada = "SELECT b.ORD_PRM_SW, b.PRM_DEF_NOM, b.PRM_DEF_TIT, b.PRM_DEF_OBL, " +
                "b.PRM_DEF_TIP, b.PRM_DEF_VAL, a.OPS_SW_NOM, a.OPS_SW_COD FROM SW_OPS_DEF a, SW_PARAMS_IN_DEF b " +
                "WHERE a.OPS_DEF_COD = b.OPS_DEF_COD AND b.OPS_DEF_COD = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetListaParamsEntrada);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LA LISTA DE PARAMAETROS DEFINIDOS " +
                    "PARA UNA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetListaParamsEntrada);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Op. Definida: " + codOpDef);

            ps.setInt(1, codOpDef);

            rs = ps.executeQuery();

            Collection<DefinicionParametroEntradaVO> listaParams = new ArrayList<DefinicionParametroEntradaVO>();
            while (rs.next()) {
                int i = 1;
                DefinicionParametroEntradaVO paramEntrada = new DefinicionParametroEntradaVO();
                paramEntrada.setCodigoOpDef(codOpDef);
                paramEntrada.setCodigoParam(rs.getInt(i++));
                paramEntrada.setDescParam(rs.getString(i++));
                paramEntrada.setTituloParam(rs.getString(i++));
                paramEntrada.setEsObligatorio(rs.getBoolean(i++));
                paramEntrada.setTipoParametro(rs.getInt(i++));
                paramEntrada.setValorDefecto(rs.getString(i++));
                paramEntrada.setNombreOpSW(rs.getString(i++));
                paramEntrada.setCodigoSW(rs.getInt(i));
                listaParams.add(paramEntrada);
            }

            return listaParams;

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA OBTENER LA LISTA DE PARAMETROS DE UNA OPERACION", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA OBTENER LA LISTA DE PARAMETROS " +
                    "DE UNA OPERACION", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE PARAMETROS DE UNA OPERACION", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE PARAMETROS" +
                    " DE UNA OPERACION", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public Collection<DefinicionParametroSalidaVO> getParamsDefSalida(int codOpDef, String[] params)
    throws TechnicalException, InternalErrorException {

        String sqlGetListaParamsEntrada = "SELECT b.PRM_DEF_NOM, b.PRM_DEF_TIT, b.PRM_TIP_DAT, b.PRM_VAL_OK, a.OPS_SW_NOM, a.OPS_SW_COD " +
                "FROM SW_OPS_DEF a, SW_PARAMS_OUT_DEF b WHERE a.OPS_DEF_COD = b.OPS_DEF_COD AND b.OPS_DEF_COD = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetListaParamsEntrada);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LA LISTA DE PARAMAETROS DEFINIDOS " +
                    "PARA UNA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetListaParamsEntrada);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Op. Definida: " + codOpDef);

            ps.setInt(1, codOpDef);

            rs = ps.executeQuery();

            Collection<DefinicionParametroSalidaVO> listaParams = new ArrayList<DefinicionParametroSalidaVO>();
            while (rs.next()) {
                int i = 1;
                DefinicionParametroSalidaVO paramSalida = new DefinicionParametroSalidaVO();
                paramSalida.setCodOpDef(codOpDef);
                paramSalida.setDescParam(rs.getString(i++));
                paramSalida.setTituloParam(rs.getString(i++));
                paramSalida.setTipoDato(rs.getInt(i++));
                paramSalida.setValorCorrecto(rs.getString(i++));
                paramSalida.setNombreOpSW(rs.getString(i++));
                paramSalida.setCodigoSW(rs.getInt(i));
                listaParams.add(paramSalida);
            }

            return listaParams;

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA OBTENER LA LISTA DE PARAMETROS DE UNA OPERACION", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA OBTENER LA LISTA DE PARAMETROS " +
                    "DE UNA OPERACION", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE PARAMETROS DE UNA OPERACION", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE PARAMETROS" +
                    " DE UNA OPERACION", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public void isOperacionDefinida(int codOpDef, String[] params) throws InternalErrorException, TechnicalException, FalloPublicacionException {

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;

        try {
            con = abd.getConnection();

            if (areArraysUndefined(codOpDef, params)) throw new FalloPublicacionException("NO SE HA PODIDO PUBLICAR " +
                    "LA OPERACION: FALTA POR DEFINIR LA ESTRUCTURA DE LOS ARRAYS");
            if (!isDataStructureDefined(codOpDef, con)) throw new FalloPublicacionException("NO SE HA PODIDO PUBLICAR " +
                    "LA OPERACION: NO SE HA DEFINIDO LA ESTRUCTURA DE DATOS PARA LA OPERACION");
            if (!allConstantsHaveValue(codOpDef, con)) throw new FalloPublicacionException("NO SE HA PODIDO PUBLICAR " +
                    "LA OPERACION: NO TODOS LOS VALORES DEFINIDOS COMO CONSTANTES TIENEN UN VALOR ASOCIADO");
            if (!allInParamsTitulo(codOpDef, con)) throw new FalloPublicacionException("NO SE HA PODIDO PUBLICAR " +
                    "LA OPERACION: NO TODOS LOS PARAMETROS DE ENTRADA TIENEN UN TITULO ASOCIADO");
            if (!allOutParamsTitulo(codOpDef, con)) throw new FalloPublicacionException("NO SE HA PODIDO PUBLICAR " +
                    "LA OPERACION: NO TODOS LOS PARAMETROS DE SALIDA TIENEN UN TITULO ASOCIADO");

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA COMPROBAR SI SE PUEDE PUBLICAR UNA OPERACION", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA COMPROBAR SI SE PUEDE PUBLICAR " +
                    "UNA OPERACION", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA COMPROBAR SI SE PUEDE PUBLICAR UNA OPERACION", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA COMPROBAR SI SE PUEDE PUBLICAR UNA OPERACION", sqle);
        } finally {
            GeneralOperations.closeConnection(con);
        }
    }

    public void cambiaEstadoPublicacion(int codOpDef, boolean publicar, String[] params) throws TechnicalException, InternalErrorException {

        String sqlUpdateOpDef = "UPDATE SW_OPS_DEF SET OPS_DEF_PUB = ? WHERE OPS_DEF_COD = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlUpdateOpDef);
            if (m_Log.isDebugEnabled())
                m_Log.debug("CONSULTA PARA ACTUALIZAR EL ESTADO DE PUBLICACION DE UNA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlUpdateOpDef);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Publicar?: " + publicar);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo de Op. Definida: " + codOpDef);

            ps.setBoolean(1, publicar);
            ps.setInt(2, codOpDef);

            int updatedRows = ps.executeUpdate();

            if (updatedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA ACTUALIZAR EL ESTADO" +
                        " DE PUBLICACION DE UNA OPERACION");

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA ACTUALIZAR EL ESTADO DE PUBLICACION DE UNA OPERACION", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA ACTUALIZAR EL ESTADO DE PUBLICACION " +
                    "DE UNA OPERACION", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA ACTUALIZAR EL ESTADO DE PUBLICACION DE UNA OPERACION", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA ACTUALIZAR EL ESTADO DE PUBLICACION" +
                    " DE UNA OPERACION", sqle);
        } finally {
            GeneralOperations.closeStatement(ps);
            GeneralOperations.closeConnection(con);
        }
    }

    private boolean allConstantsHaveValue(int codOpDef, Connection con) throws SQLException, InternalErrorException {

        String sqlBuscaNoValor = "SELECT OPS_DEF_COD FROM SW_PARAMS_IN_DEF WHERE OPS_DEF_COD = ? AND " +
                "PRM_DEF_TIP = 2 AND (PRM_DEF_VAL IS NULL OR PRM_DEF_VAL = '') ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sqlBuscaNoValor);
            if (m_Log.isDebugEnabled())
                m_Log.debug("CONSULTA PARA COMPROBAR SI TODAS LAS CONSTANTES DE ENTRADA TIENEN UN VALOR POR DEFECTO");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaNoValor);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Op. Definida: " + codOpDef);

            ps.setInt(1, codOpDef);

            rs = ps.executeQuery();

            return !rs.next();

        } catch (SQLException e) {
            throw e;
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
        }
    }

    private boolean allInParamsTitulo(int codOpDef, Connection con) throws SQLException, InternalErrorException {

        String sqlBuscaNoTitulo = "SELECT OPS_DEF_COD FROM SW_PARAMS_IN_DEF WHERE OPS_DEF_COD = ? AND " +
                "(PRM_DEF_TIP = 1 OR PRM_DEF_TIP = 3) AND (PRM_DEF_TIT IS NULL OR PRM_DEF_TIT = '')";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sqlBuscaNoTitulo);
            if (m_Log.isDebugEnabled())
                m_Log.debug("CONSULTA PARA COMPROBAR SI TODOS LOS PARAMETROS DE ENTRADA TIENEN UN TITULO ASOCIADO");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaNoTitulo);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Op. Definida: " + codOpDef);

            ps.setInt(1, codOpDef);

            rs = ps.executeQuery();

            return !rs.next();

        } catch (SQLException e) {
            throw e;
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
        }
    }

    private boolean allOutParamsTitulo(int codOpDef, Connection con) throws SQLException, InternalErrorException {

        String sqlBuscaNoTitulo = "SELECT OPS_DEF_COD FROM SW_PARAMS_OUT_DEF WHERE OPS_DEF_COD = ? AND " +
                "(PRM_DEF_TIT IS NULL OR PRM_DEF_TIT = '')";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sqlBuscaNoTitulo);
            if (m_Log.isDebugEnabled())
                m_Log.debug("CONSULTA PARA COMPROBAR SI TODOS LOS PARAMETROS DE SALIDA TIENEN UN TITULO ASOCIADO");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaNoTitulo);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Op. Definida: " + codOpDef);

            ps.setInt(1, codOpDef);

            rs = ps.executeQuery();

            return !rs.next();

        } catch (SQLException e) {
            throw e;
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
        }
    }

    public void updateDefParamIn(DefinicionParametroEntradaVO paramIn, String[] params)
            throws InternalErrorException, TechnicalException {

        String sqlUpdateDefParam = "UPDATE SW_PARAMS_IN_DEF SET PRM_DEF_TIT = ?, PRM_DEF_OBL = ?, PRM_DEF_TIP = ?, " +
                "PRM_DEF_VAL = ? WHERE ORD_PRM_SW = ? AND OPS_DEF_COD = ? AND PRM_DEF_NOM = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlUpdateDefParam);
            if (m_Log.isDebugEnabled())
                m_Log.debug("CONSULTA PARA ACTUALIZAR LA DEFINICION DE UN PARAMETRO DE ENTRADA DE UNA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlUpdateDefParam);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Titulo Parametro: " + paramIn.getTituloParam());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Es Obligatorio?: " + paramIn.isEsObligatorio());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Tipo Parametro: " + paramIn.getTipoParametro());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Valor por Defecto: " + paramIn.getValorDefecto());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 5: Codigo Parametro: " + paramIn.getCodigoParam());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 6: Codigo Op. Definida: " + paramIn.getCodigoOpDef());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 7: Nombre Parametro: " + paramIn.getDescParam());

            int i = 1;
            ps.setString(i++, paramIn.getTituloParam());
            ps.setBoolean(i++, paramIn.isEsObligatorio());
            ps.setInt(i++, paramIn.getTipoParametro());
            ps.setString(i++, paramIn.getValorDefecto());
            ps.setInt(i++, paramIn.getCodigoParam());
            ps.setInt(i++, paramIn.getCodigoOpDef());
            ps.setString(i, paramIn.getDescParam());
            
            int updatedRows = ps.executeUpdate();
            if (updatedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA ACTUALIZAR LA DEFINICION " +
                        "DE UN PARAMETRO DE ENTRADA DE UNA OPERACION");

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA ACTUALIZAR LA DEFINICION DE UN PARAMETRO DE " +
                    "ENTRADA DE UNA OPERACION", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA ACTUALIZAR LA DEFINICION DE UN " +
                    "PARAMETRO DE ENTRADA DE UNA OPERACION", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA ACTUALIZAR LA DEFINICION DE UN PARAMETRO DE ENTRADA " +
                    "DE UNA OPERACION", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA ACTUALIZAR LA DEFINICION DE UN PARAMETRO DE" +
                    " ENTRADA DE UNA OPERACION", sqle);
        } finally {
            GeneralOperations.closeStatement(ps);
            GeneralOperations.closeConnection(con);
        }
    }

    public void updateDefParamOut(DefinicionParametroSalidaVO paramOut, String[] params)
            throws InternalErrorException, TechnicalException {

        String sqlUpdateDefParam = "UPDATE SW_PARAMS_OUT_DEF SET PRM_DEF_TIT = ?, PRM_TIP_DAT = ?, PRM_VAL_OK = ? " +
                "WHERE OPS_DEF_COD = ? AND PRM_DEF_NOM = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlUpdateDefParam);
            if (m_Log.isDebugEnabled())
                m_Log.debug("CONSULTA PARA ACTUALIZAR LA DEFINICION DE UN PARAMETRO DE SALIDA DE UNA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlUpdateDefParam);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Titulo Parametro: " + paramOut.getTituloParam());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Tipo de Dato: " + paramOut.getTipoDato());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Valor Correcto: " + paramOut.getValorCorrecto());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Codigo Op. Definida: " + paramOut.getCodOpDef());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 5: Nombre Parametro: " + paramOut.getDescParam());

            int i = 1;
            ps.setString(i++, paramOut.getTituloParam());
            ps.setInt(i++, paramOut.getTipoDato());
            ps.setString(i++, paramOut.getValorCorrecto());
            ps.setInt(i++, paramOut.getCodOpDef());
            ps.setString(i, paramOut.getDescParam());

            int updatedRows = ps.executeUpdate();
            if (updatedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA ACTUALIZAR LA DEFINICION " +
                        "DE UN PARAMETRO DE SALIDA DE UNA OPERACION");

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA ACTUALIZAR LA DEFINICION DE UN PARAMETRO DE " +
                    "SALIDA DE UNA OPERACION", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA ACTUALIZAR LA DEFINICION DE UN " +
                    "PARAMETRO DE SALIDA DE UNA OPERACION", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA ACTUALIZAR LA DEFINICION DE UN PARAMETRO DE SALIDA " +
                    "DE UNA OPERACION", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA ACTUALIZAR LA DEFINICION DE UN PARAMETRO DE" +
                    " ENTRADA DE UNA OPERACION", sqle);
        } finally {
            GeneralOperations.closeStatement(ps);
            GeneralOperations.closeConnection(con);
        }
    }

    public boolean areArraysUndefined(int codOpDef, String[] params) throws TechnicalException, InternalErrorException {

        String sqlArraysUndefined = "SELECT * FROM SW_ARRAY_DEF WHERE OPS_DEF_COD = ? AND DEF_ARR_VAL = -1";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlArraysUndefined);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA COMPROBAR SI HAY ARRAY SIN DEFINIR PARA LA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlArraysUndefined);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Op. Definida: " + codOpDef);

            ps.setInt(1, codOpDef);

            rs = ps.executeQuery();

            return rs.next();

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA COMPROBAR SI HAY ARRAY SIN DEFINIR PARA LA OPERACION", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA COMPROBAR SI HAY ARRAY SIN DEFINIR" +
                    " PARA LA OPERACION", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA COMPROBAR SI HAY ARRAY SIN DEFINIR PARA LA OPERACION", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA PARA COMPROBAR SI HAY ARRAY SIN DEFINIR" +
                    " PARA LA OPERACION", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    private boolean isDataStructureDefined(int codOpDef, Connection con) throws TechnicalException, InternalErrorException {

        String sqlStructureDefined = "SELECT * FROM SW_OPS_DEF WHERE OPS_DEF_COD = ? AND OPS_DEF_EST = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sqlStructureDefined);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA COMPROBAR SI LA ESTRUCTURA DE DATOS PARA LA " +
                    "OPERACION ESTA DEFINIDA");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlStructureDefined);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Op. Definida: " + codOpDef);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Estructura Definida: " + true);

            int i = 1;
            ps.setInt(i++, codOpDef);
            ps.setBoolean(i, true);

            rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA COMPROBAR SI LA ESTRUCTURA DE DATOS PARA LA OPERACION ESTA " +
                    "DEFINIDA", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA COMPROBAR SI LA ESTRUCTURA DE DATOS PARA LA " +
                    "OPERACION ESTA DEFINIDA", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
        }
    }

    public Collection getArraysByOpCod(int codOpDef, String[] params) throws TechnicalException, InternalErrorException {

        String sqlGetArrays = "SELECT ORD_PRM_SW, DEF_ARR_DES, DEF_ARR_IO, DEF_ARR_VAL FROM SW_ARRAY_DEF " +
                "WHERE OPS_DEF_COD = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetArrays);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR LOS ARRAYS DEFINIDOS PARA UNA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetArrays);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Op. Definida: " + codOpDef);

            ps.setInt(1, codOpDef);

            rs = ps.executeQuery();

            Collection listaArrays = new ArrayList();
            while (rs.next()) {
                int i = 1;
                DefinicionArraySWVO defArray = new DefinicionArraySWVO();
                defArray.setCodigoOpDef(codOpDef);
                defArray.setCodigoParam(rs.getInt(i++));
                defArray.setDescParamArray(rs.getString(i++));
                defArray.setEntradaSalida(rs.getInt(i++));
                defArray.setNumRepeticiones(rs.getInt(i));
                listaArrays.add(defArray);
            }

            return listaArrays;

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA RECUPERAR LOS ARRAYS DEFINIDOS PARA UNA OPERACION", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA RECUPERAR LOS ARRAYS DEFINIDOS " +
                    "PARA UNA OPERACION", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR LOS ARRAYS DEFINIDOS PARA UNA OPERACION", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR LOS ARRAYS DEFINIDOS " +
                    "PARA UNA OPERACION", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public void updateNumRepsArray(DefinicionArraySWVO defArray, String[] params) throws TechnicalException, InternalErrorException {

        String sqlUpdateArray = "UPDATE SW_ARRAY_DEF SET DEF_ARR_VAL = ? WHERE DEF_ARR_DES = ? AND OPS_DEF_COD = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlUpdateArray);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR LOS ARRAYS DEFINIDOS PARA UNA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlUpdateArray);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Num Instancias: " + defArray.getNumRepeticiones());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Descripcion Array: " + defArray.getDescParamArray());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo Operacion: " + defArray.getCodigoOpDef());

            int i = 1;
            ps.setInt(i++, defArray.getNumRepeticiones());
            ps.setString(i++, defArray.getDescParamArray());
            ps.setInt(i, defArray.getCodigoOpDef());

            int updatedRows = ps.executeUpdate();
            if (updatedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA ACTUALIZAR LOS ARRAYS " +
                        "DEFINIDOS PARA UNA OPERACION");

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA ACTUALIZAR LOS ARRAYS DEFINIDOS PARA UNA OPERACION", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA ACTUALIZAR LOS ARRAYS DEFINIDOS " +
                    "PARA UNA OPERACION", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA ACTUALIZAR LOS ARRAYS DEFINIDOS PARA UNA OPERACION", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA ACTUALIZAR LOS ARRAYS DEFINIDOS " +
                    "PARA UNA OPERACION", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }
    
    public boolean existeEstructuraDatos(int codigoOpDef, String[] params)
            throws TechnicalException, InternalErrorException {

        String sqlExisteEstructura = "SELECT OPS_DEF_EST FROM SW_OPS_DEF WHERE OPS_DEF_COD = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlExisteEstructura);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR LOS ARRAYS DEFINIDOS PARA UNA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlExisteEstructura);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Operacion: " + codigoOpDef);

            ps.setInt(1, codigoOpDef);

            rs = ps.executeQuery();

            if (rs.next()) return rs.getBoolean(1);
            else throw new InternalErrorException(new Exception("ERROR: NO EXISTE UNA OPERACION DEFINIDA CON EL " +
                    "CODIGO " + codigoOpDef));

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA COMPROBAR SI UNA OPERACION TIENE DEFINIDA SU ESTRUCTURA " +
                    "DE PARAMETROS", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA COMPROBAR SI UNA OPERACION TIENE " +
                    "DEFINIDA SU ESTRUCTURA DE PARAMETROS", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA COMPROBAR SI UNA OPERACION TIENE DEFINIDA SU ESTRUCTURA DE " +
                    "PARAMETROS", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA COMPROBAR SI UNA OPERACION TIENE DEFINIDA SU " +
                    "ESTRUCTURA DE PARAMETROS", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public Vector getAllPublishedOperations(String[] params) throws TechnicalException, InternalErrorException {

        String sqlBuscaOpsDefinidas = "SELECT OPS_DEF_COD, OPS_DEF_TIT, SW_TIT FROM SW_OPS_DEF A, SW_INFO B " +
                "WHERE A.OPS_SW_COD = B.SW_COD AND A.OPS_DEF_PUB = ? AND B.SW_PUB = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlBuscaOpsDefinidas);
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaOpsDefinidas);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Esta Publicada Operacion: " + true);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Esta Publicado Servicio Web: " + true);

            int i = 1;
            ps.setBoolean(i++, true);
            ps.setBoolean(i, true);

            rs = ps.executeQuery();

            Vector opsPublished = new Vector();
            while (rs.next()) {
                i = 1;
                GeneralValueObject infoOpPublished = new GeneralValueObject();
                infoOpPublished.setAtributo("codigoOp", rs.getString(i++));
                infoOpPublished.setAtributo("tituloOp", rs.getString(i++));
                infoOpPublished.setAtributo("tituloSW", rs.getString(i));
                opsPublished.add(infoOpPublished);
            }

            return opsPublished;

        } catch (BDException bde) {
             m_Log.error("ERROR AL OBTENER LA CONEXION PARA RECUPERAR TODAS LAS OPERACIONES DEFINIDAS Y PUBLICADAS",
                     bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA RECUPERAR TODAS LAS OPERACIONES " +
                    "DEFINIDAS Y PUBLICADAS", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR TODAS LAS OPERACIONES DEFINIDAS Y PUBLICADAS", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR TODAS LAS OPERACIONES DEFINIDAS Y " +
                    "PUBLICADAS", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public Vector getParamsEntradaTramitacion(int codOpDef, String[] params)
            throws TechnicalException, InternalErrorException {

        String sqlBuscaOpsDefinidas = "SELECT ORD_PRM_SW, PRM_DEF_NOM, PRM_DEF_TIT FROM SW_PARAMS_IN_DEF " +
                "WHERE OPS_DEF_COD = ? AND PRM_DEF_TIP <> 2";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlBuscaOpsDefinidas);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LOS PARAMETROS DE ENTRADA QUE SE PUEDEN " +
                    "MODIFICAR EN LA DEFINICION DE TRAMITES");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaOpsDefinidas);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Operacion: " + codOpDef);

            ps.setInt(1, codOpDef);

            rs = ps.executeQuery();

            Vector paramsEntrada = new Vector();
            while (rs.next()) {
                int i = 1;
                ParametroConfigurableVO param = new ParametroConfigurableVO();
                param.setCodigoOp(codOpDef);
                param.setOrdenParam(rs.getInt(i++));
                param.setNombreDefinicion(rs.getString(i++));
                param.setTituloParam(rs.getString(i));
                param.setTipoValorPaso(1);
                param.setCodCampoExp("");
                param.setValorConstante("");
                paramsEntrada.add(param);
            }

            return paramsEntrada;

        } catch (BDException bde) {
             m_Log.error("ERROR AL OBTENER LA CONEXION PARA OBTENER LOS PARAMETROS DE ENTRADA QUE SE PUEDEN " +
                     "MODIFICAR EN LA DEFINICION DE TRAMITES", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA OBTENER LOS PARAMETROS DE ENTRADA QUE " +
                    "SE PUEDEN MODIFICAR EN LA DEFINICION DE TRAMITES", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LOS PARAMETROS DE ENTRADA QUE SE PUEDEN MODIFICAR EN LA " +
                    "DEFINICION DE TRAMITES", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LOS PARAMETROS DE ENTRADA QUE SE PUEDEN " +
                    "MODIFICAR EN LA DEFINICION DE TRAMITES", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }
    public Vector getParamsEntradaTramitacion(int codOpDef, Connection con)
    	throws TechnicalException, InternalErrorException {

    	String sqlBuscaOpsDefinidas = "SELECT ORD_PRM_SW, PRM_DEF_NOM, PRM_DEF_TIT FROM SW_PARAMS_IN_DEF " +
        "WHERE OPS_DEF_COD = ? AND PRM_DEF_TIP <> 2";

    	

    	PreparedStatement ps = null;
    	ResultSet rs = null;

    	try {
    

    		ps = con.prepareStatement(sqlBuscaOpsDefinidas);
    		if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LOS PARAMETROS DE ENTRADA QUE SE PUEDEN " +
    			"MODIFICAR EN LA DEFINICION DE TRAMITES");
    		if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaOpsDefinidas);
    		if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Operacion: " + codOpDef);

    		ps.setInt(1, codOpDef);

    		rs = ps.executeQuery();

    		Vector paramsEntrada = new Vector();
    		while (rs.next()) {
	        int i = 1;
	        ParametroConfigurableVO param = new ParametroConfigurableVO();
	        param.setCodigoOp(codOpDef);
	        param.setOrdenParam(rs.getInt(i++));
	        param.setNombreDefinicion(rs.getString(i++));
	        param.setTituloParam(rs.getString(i));
	        param.setTipoValorPaso(1);
//	        param.setCodCampoExp("");
//	        param.setValorConstante("");
	        paramsEntrada.add(param);
	    }
	
	    return paramsEntrada;
	

	    
		} catch (SQLException sqle) {
		    m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LOS PARAMETROS DE ENTRADA QUE SE PUEDEN MODIFICAR EN LA " +
		            "DEFINICION DE TRAMITES", sqle);
		    throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LOS PARAMETROS DE ENTRADA QUE SE PUEDEN " +
		            "MODIFICAR EN LA DEFINICION DE TRAMITES", sqle);
		} finally {
		    GeneralOperations.closeResultSet(rs);
		    GeneralOperations.closeStatement(ps);
		   
		}
	}    

    public Vector getParamsSalidaTramitacion(int codOpDef, String[] params)
            throws TechnicalException, InternalErrorException {

        String sqlBuscaOpsDefinidas = "SELECT PRM_DEF_NOM, PRM_DEF_TIT FROM SW_PARAMS_OUT_DEF WHERE OPS_DEF_COD = ? " +
                "AND PRM_TIP_DAT = 0";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlBuscaOpsDefinidas);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LOS PARAMETROS DE SALIDA QUE SE PUEDEN " +
                    "MODIFICAR EN LA DEFINICION DE TRAMITES");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaOpsDefinidas);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Operacion: " + codOpDef);

            ps.setInt(1, codOpDef);

            rs = ps.executeQuery();

            Vector paramsSalida = new Vector();
            while (rs.next()) {
                int i = 1;
                ParametroConfigurableVO param = new ParametroConfigurableVO();
                param.setCodigoOp(codOpDef);
                param.setOrdenParam(0);
                param.setNombreDefinicion(rs.getString(i++));
                param.setTituloParam(rs.getString(i));
                param.setTipoValorPaso(1);
                param.setCodCampoExp("");
                param.setValorConstante("");
                paramsSalida.add(param);
            }

            return paramsSalida;

        } catch (BDException bde) {
             m_Log.error("ERROR AL OBTENER LA CONEXION PARA OBTENER LOS PARAMETROS DE SALIDA QUE SE PUEDEN " +
                     "MODIFICAR EN LA DEFINICION DE TRAMITES", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA OBTENER LOS PARAMETROS DE SALIDA QUE " +
                    "SE PUEDEN MODIFICAR EN LA DEFINICION DE TRAMITES", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LOS PARAMETROS DE SALIDA QUE SE PUEDEN MODIFICAR EN LA " +
                    "DEFINICION DE TRAMITES", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LOS PARAMETROS DE SALIDA QUE SE PUEDEN " +
                    "MODIFICAR EN LA DEFINICION DE TRAMITES", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }
    public Vector getParamsSalidaTramitacion(int codOpDef, Connection con)
    	throws TechnicalException, InternalErrorException {

		String sqlBuscaOpsDefinidas = "SELECT PRM_DEF_NOM, PRM_DEF_TIT FROM SW_PARAMS_OUT_DEF WHERE OPS_DEF_COD = ? " +
		        "AND PRM_TIP_DAT = 0";

		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
		    
		
		    ps = con.prepareStatement(sqlBuscaOpsDefinidas);
		    if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LOS PARAMETROS DE SALIDA QUE SE PUEDEN " +
		            "MODIFICAR EN LA DEFINICION DE TRAMITES");
		    if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaOpsDefinidas);
		    if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Operacion: " + codOpDef);
		
		    ps.setInt(1, codOpDef);
		
		    rs = ps.executeQuery();
		
		    Vector paramsSalida = new Vector();
		    while (rs.next()) {
		        int i = 1;
		        ParametroConfigurableVO param = new ParametroConfigurableVO();
		        param.setCodigoOp(codOpDef);
		        param.setOrdenParam(0);
		        param.setNombreDefinicion(rs.getString(i++));
		        param.setTituloParam(rs.getString(i));
		        param.setTipoValorPaso(1);
		        param.setCodCampoExp("");
		        param.setValorConstante("");
		        paramsSalida.add(param);
		    }
		
		    return paramsSalida;
		
		
		} catch (SQLException sqle) {
		    m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LOS PARAMETROS DE SALIDA QUE SE PUEDEN MODIFICAR EN LA " +
		            "DEFINICION DE TRAMITES", sqle);
		    throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LOS PARAMETROS DE SALIDA QUE SE PUEDEN " +
		            "MODIFICAR EN LA DEFINICION DE TRAMITES", sqle);
		} finally {
		    GeneralOperations.closeResultSet(rs);
		    GeneralOperations.closeStatement(ps);
		    
		}
    }
    public void grabaParametro(ParametroConfigurableVO param, String[] params) 
    		throws TechnicalException, InternalErrorException {
    	String sqlUpdateParam = "UPDATE CONF_TRA_SW SET TIP_DAT_CONF = ?, COD_DAT_CONF = ?, VAL_DAT_CONF = ? " +
    			"WHERE DEF_CFO_CONF = ? AND COD_ORD_CONF = ? AND DEF_PRM_CONF = ?"; 
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlUpdateParam);
            if (m_Log.isDebugEnabled())
                m_Log.debug("CONSULTA PARA ACTUALIZAR UN PARAMETRO");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlUpdateParam);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Tipo de Dato: " + param.getTipoValorPaso());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Codigo del Campo del Exp: " + param.getCodCampoExp());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Valor Constante: " + param.getValorConstante());                       
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Clave SW: " + param.getCfo());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 5: Orden del parametro: " + param.getOrdenParam());
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 6: Nombre Definicion: " + param.getNombreDefinicion());
            
            

            int i = 1;
            ps.setInt(i++, param.getTipoValorPaso());
            ps.setString(i++, param.getCodCampoExp());
            ps.setString(i++, param.getValorConstante());
            ps.setLong(i++, param.getCfo());
            ps.setInt(i++, param.getOrdenParam());
            ps.setString(i++, param.getNombreDefinicion());

            
            int updatedRows = ps.executeUpdate();
            if (updatedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA ACTUALIZAR UN PARAMETRO");

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA ACTUALIZAR UN PARAMETRO", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA ACTUALIZAR UN PARAMETRO", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA ACTUALIZAR UN PARAMETRO", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA ACTUALIZAR UN PARAMETRO", sqle);
        } finally {
            GeneralOperations.closeStatement(ps);
            GeneralOperations.closeConnection(con);
        }
    }
    
    public int getCodOp(long cfo, String[] params) 
		throws TechnicalException, InternalErrorException {
    	int codOp = -1;
    	
    	String sqlGetCodOp = "SELECT DEF_TRA_OP FROM DEF_TRA_SW WHERE DEF_TRA_CFO = ?";
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetCodOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER EL CODIGO DE OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetCodOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo SW: " + cfo);

            ps.setLong(1, cfo);

            rs = ps.executeQuery();    
            if (rs.next()) codOp = rs.getInt(1);
            
        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA OBTENER EL CODIGO DE OPERACION", bde);
           throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA OBTENER EL CODIGO DE OPERACION", bde);
       } catch (SQLException sqle) {
           m_Log.error("ERROR EN LA CONSULTA PARA OBTENER EL CODIGO DE OPERACION", sqle);
           throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER EL CODIGO DE OPERACION", sqle);
       } finally {
           GeneralOperations.closeResultSet(rs);
           GeneralOperations.closeStatement(ps);
           devolverConexion(abd, con);
       }            
    	return codOp;
    }
    public String getTituloOp(int codOp, String[] params) 
    	throws TechnicalException, InternalErrorException {
    	
    	String titOp = "";
    	String sqlGetTitOp = "SELECT OPS_DEF_TIT FROM SW_OPS_DEF WHERE OPS_DEF_COD = ?";
    	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
    	Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try {
    		con = abd.getConnection();

    		ps = con.prepareStatement(sqlGetTitOp);
    		if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER EL TITULO DE OPERACION");
    		if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetTitOp);
    		if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Operacion: " + codOp);

    		ps.setLong(1, codOp);

    		rs = ps.executeQuery();    
    		if (rs.next()) titOp = rs.getString(1);
        
    	} catch (BDException bde) {
    		m_Log.error("ERROR AL OBTENER LA CONEXION PARA OBTENER EL TITULO DE OPERACION", bde);
    		throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA OBTENER EL TITULO DE OPERACION", bde);
    	} catch (SQLException sqle) {
    		m_Log.error("ERROR EN LA CONSULTA PARA OBTENER EL TITULO DE OPERACION", sqle);
    		throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER EL TITULO DE OPERACION", sqle);
    	} finally {
    		GeneralOperations.closeResultSet(rs);
    		GeneralOperations.closeStatement(ps);
    		devolverConexion(abd, con);
    	}            
    	return titOp;
    }
    
    public InfoConfTramSWVO getConfVO(long cfo, String[] params) 
		throws TechnicalException, InternalErrorException {
    	int ob = -1;
    	InfoConfTramSWVO info = new InfoConfTramSWVO();
		String sqlGetEsOb = "SELECT DEF_TRA_OBL, DEF_TRA_MUN, DEF_TRA_PRO, DEF_COD_TRA FROM DEF_TRA_SW WHERE DEF_TRA_CFO = ?";
    	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
    	Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try {
        	con = abd.getConnection();

        	ps = con.prepareStatement(sqlGetEsOb);
        	if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER INFORMACION DE CONFIGURACION");
        	if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetEsOb);
        	if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo SW: " + cfo);

        	ps.setLong(1, cfo);
        	
        	int i=1;
        	rs = ps.executeQuery();
        	
        	if (rs.next()) {
        		ob = rs.getInt(i++);
        		if (ob==1) {
        			info.setObligatorio(true);
        		}
        		else {
        			info.setObligatorio(false);
        		}
        		info.setCodMunicipio(rs.getInt(i++));
        		info.setTxtCodigo(rs.getString(i++));
        		info.setCodigoTramite(rs.getInt(i++));;
        		
        	}
        
    	} catch (BDException bde) {
    		m_Log.error("ERROR AL OBTENER LA CONEXION PARA OBTENER INFORMACION DE CONFIGURACION", bde);
        	throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA OBTENER INFORMACION DE CONFIGURACION", bde);
    	} catch (SQLException sqle) {
       		m_Log.error("ERROR EN LA CONSULTA PARA OBTENER INFORMACION DE CONFIGURACION", sqle);
       		throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER INFORMACION DE CONFIGURACION", sqle);
   		} finally {
       		GeneralOperations.closeResultSet(rs);
       		GeneralOperations.closeStatement(ps);
       		devolverConexion(abd, con);
   		}            
		return info;
    }    
    
    public Vector getParamsEntrada (long cfo,int codOp, String[] params) throws TechnicalException, InternalErrorException{
    	Vector<ParametroConfigurableVO> listaEntrada = new Vector<ParametroConfigurableVO>();
    	String sqlGetParams = "SELECT COD_ORD_CONF, DEF_PRM_CONF, TIP_DAT_CONF, COD_DAT_CONF, VAL_DAT_CONF, PRM_DEF_OBL, PRM_DEF_TIT" +
    			" FROM CONF_TRA_SW JOIN DEF_TRA_SW ON (DEF_CFO_CONF = DEF_TRA_CFO)" +
                " JOIN SW_OPS_DEF ON (DEF_TRA_OP = SW_OPS_DEF.OPS_DEF_COD)" +
                " JOIN SW_PARAMS_IN_DEF ON (SW_OPS_DEF.OPS_DEF_COD = SW_PARAMS_IN_DEF.OPS_DEF_COD AND DEF_PRM_CONF = PRM_DEF_NOM)" +
                " WHERE DEF_CFO_CONF = ? AND COD_ORD_CONF<>0 ORDER BY DEF_PRM_CONF";

    	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
    	Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try {
        	con = abd.getConnection();           
            
        	ps = con.prepareStatement(sqlGetParams);
        	if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LOS DATOS DE LOS PARAMETROS DE ENTRADA");
        	if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetParams);
        	if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo SW: " + cfo);

        	ps.setLong(1, cfo);

        	rs = ps.executeQuery();
        	while(rs.next()){
        		int i = 1;
        		ParametroConfigurableVO param = new ParametroConfigurableVO();
        		int orden = rs.getInt(i++);
        		String nombreDefinicion = rs.getString(i++);
        		int tipoValor = rs.getInt(i++);
        		String codCampoExp = rs.getString(i++);        		        	
        		String valorConstante = rs.getString(i++);
        		if (codCampoExp == null) codCampoExp = "";
        		if (valorConstante == null) valorConstante = "";
        		param.setCfo(cfo);
        		param.setOrdenParam(orden);
        		param.setNombreDefinicion(nombreDefinicion);
        		param.setTipoValorPaso(tipoValor);
        		param.setCodCampoExp(codCampoExp);
        		param.setValorConstante(valorConstante);
        		param.setObligatorio(rs.getInt(i++));
                param.setTituloParam(rs.getString(i));
        		param.setCodigoOp(codOp);
        		listaEntrada.addElement(param);
        	}
        	
    	} catch (BDException bde) {
    		m_Log.error("ERROR AL OBTENER LA CONEXION PARA OBTENER LOS DATOS DE LOS PARAMETROS DE ENTRADA", bde);
        	throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA OBTENER LOS DATOS DE LOS PARAMETROS DE ENTRADA", bde);
    	} catch (SQLException sqle) {
       		m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LOS DATOS DE LOS PARAMETROS DE ENTRADA", sqle);
       		throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LOS DATOS DE LOS PARAMETROS DE ENTRADA", sqle);
   		} finally {
       		GeneralOperations.closeResultSet(rs);
       		GeneralOperations.closeStatement(ps);
       		devolverConexion(abd, con);
   		}            
		return listaEntrada;
            	
    }
    
    public Vector getParamsSalida (long cfo,int codOp, String[] params) throws TechnicalException, InternalErrorException{
    	Vector<ParametroConfigurableVO> listaSalida = new Vector<ParametroConfigurableVO>();

        String sqlGetParams = "SELECT COD_ORD_CONF, DEF_PRM_CONF, TIP_DAT_CONF, COD_DAT_CONF, VAL_DAT_CONF, PRM_DEF_TIT" +
    			" FROM CONF_TRA_SW JOIN DEF_TRA_SW ON (DEF_CFO_CONF = DEF_TRA_CFO)" +
                " JOIN SW_OPS_DEF ON (DEF_TRA_OP = SW_OPS_DEF.OPS_DEF_COD)" +
                " JOIN SW_PARAMS_OUT_DEF ON (SW_OPS_DEF.OPS_DEF_COD = SW_PARAMS_OUT_DEF.OPS_DEF_COD AND DEF_PRM_CONF = PRM_DEF_NOM)" +
                " WHERE DEF_CFO_CONF = ? AND COD_ORD_CONF=0 ORDER BY DEF_PRM_CONF";

    	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
    	Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try {
        	con = abd.getConnection();           
        	ps = con.prepareStatement(sqlGetParams);
        	if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LOS DATOS DE LOS PARAMETROS DE SALIDA");
        	if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetParams);
        	if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo SW: " + cfo);

        	ps.setLong(1, cfo);

        	rs = ps.executeQuery();
        	while(rs.next()){
        		int i = 1;
        		ParametroConfigurableVO param = new ParametroConfigurableVO();
        		int orden = rs.getInt(i++);
        		String nombreDefinicion = rs.getString(i++);
        		int tipoValor = rs.getInt(i++);
        		String codCampoExp = rs.getString(i++);        		        	
        		String valorConstante = rs.getString(i++);
        		param.setCfo(cfo);
        		param.setOrdenParam(orden);
        		param.setNombreDefinicion(nombreDefinicion);
        		param.setTipoValorPaso(tipoValor);
        		param.setCodCampoExp(codCampoExp);
        		param.setValorConstante(valorConstante);
        		param.setTituloParam(rs.getString(i));
        		param.setCodigoOp(codOp);
        		listaSalida.addElement(param);
        	}
        	
    	} catch (BDException bde) {
    		m_Log.error("ERROR AL OBTENER LA CONEXION PARA OBTENER LOS DATOS DE LOS PARAMETROS DE SALIDA", bde);
        	throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA OBTENER LOS DATOS DE LOS PARAMETROS DE SALIDA", bde);
    	} catch (SQLException sqle) {
       		m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LOS DATOS DE LOS PARAMETROS DE SALIDA", sqle);
       		throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LOS DATOS DE LOS PARAMETROS DE SALIDA", sqle);
   		} finally {
       		GeneralOperations.closeResultSet(rs);
       		GeneralOperations.closeStatement(ps);
       		devolverConexion(abd, con);
   		}            
		return listaSalida;
            	
    }    
    
    public List<ParametroConfigurableVO> getParametrosOpByCfo(long cfo, Connection con) throws TechnicalException, InternalErrorException {

        List<ParametroConfigurableVO> listaParams = new ArrayList<ParametroConfigurableVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql;

        try {
            sql = "SELECT COD_ORD_CONF,DEF_PRM_CONF,TIP_DAT_CONF,COD_DAT_CONF,VAL_DAT_CONF "
                    + "FROM CONF_TRA_SW WHERE DEF_CFO_CONF = ?";
            
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LA LISTA DE PARAMAETROS DE ENTRADA PARA UNA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Cfo de Op. Definida: " + cfo);

            ps = con.prepareStatement(sql);
            ps.setLong(1, cfo);

            rs = ps.executeQuery();

            while (rs.next()) {
                int i = 1;
                ParametroConfigurableVO paramEntrada = new ParametroConfigurableVO();
                paramEntrada.setCfo(cfo);
                paramEntrada.setOrdenParam(rs.getInt("COD_ORD_CONF"));
                paramEntrada.setNombreDefinicion(rs.getString("DEF_PRM_CONF"));
                paramEntrada.setTipoValorPaso(rs.getInt("TIP_DAT_CONF"));
                paramEntrada.setCodCampoExp(rs.getString("COD_DAT_CONF"));
                paramEntrada.setValorConstante(rs.getString("VAL_DAT_CONF"));
                listaParams.add(paramEntrada);
            }

            return listaParams;

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE PARAMETROS DE UNA OPERACION", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE PARAMETROS" +
                    " DE UNA OPERACION", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
        }
    }
    
    public boolean setOperacionSWConParam(AvanzarRetrocederSWVO operacion, int codMunicipio, String codProcedimiento, int codTramite, Connection con) throws TechnicalException {
        m_Log.debug("setOperacionSWConParam BEGIN");
        long cfoOp = -1;
        int insertaSW = -1;
        int insertaParams = -1;

        try {
            // Obtenemos el máximo cfo de DEF_TRA_SW
            cfoOp = DefinicionSWTramitacionDAO.getInstance().getMaxCodSW(con);
            int codIntegracion = operacion.getCodIntegracion();
            int codOperacion = operacion.getCodOperacion();
            int orden = operacion.getNumeroOrden();
            String tipo = operacion.getTipoOperacionFinal();
            String nombreOp = operacion.getNombreOperacionFinal();
            String nombreMod = operacion.getNombreModuloFinal();
            int tipoRet = operacion.getTipoRetroceso();
            int oblig = operacion.getObligatorio();

            insertaSW = DefinicionSWTramitacionDAO.getInstance().insertarSW(++cfoOp, codMunicipio, codProcedimiento, codTramite, codIntegracion, codOperacion, orden, tipo, nombreOp, nombreMod, tipoRet, oblig, con);
            if (insertaSW > 0) {
                insertaParams++; // Ponemos la variable de inserción de parámetros a 0
                List<ParametroConfigurableVO> paramsOp = operacion.getListaParametros();
                for (ParametroConfigurableVO paramOp : paramsOp) {
                    insertaParams += insertParam(cfoOp, paramOp, con);
                }
            }
        } catch (TechnicalException tex) {
            m_Log.error("ERROR EN LA SENTENCIA PARA INSERTAR LA LISTA DE OPERACIONES DEL TRÁMITE");
            throw tex;
        } catch (InternalErrorException ieex) {
            m_Log.error("ERROR EN LA SENTENCIA PARA INSERTAR LA LISTA DE PARAMETROS DE UNA OPERACION");
            throw new TechnicalException(ieex.getMessage());
        } catch (SQLException sex) {
            m_Log.error("ERROR EN LA SENTENCIA PARA INSERTAR LA LISTA DE PARAMETROS DE UNA OPERACION");
            throw new TechnicalException(sex.getMessage());
        }
        m_Log.debug("setOperacionSWConParam END");
        return (insertaParams >= 0 && insertaParams == operacion.getListaParametros().size());
    }
    
    public void setObligatoria(long cfo,int ob, String[] params) throws TechnicalException, InternalErrorException{
    	String sqlSetOb = "UPDATE DEF_TRA_SW SET DEF_TRA_OBL = ? WHERE DEF_TRA_CFO = ?";
    	AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
    	Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try {
        	con = abd.getConnection();           
                		
            
        	ps = con.prepareStatement(sqlSetOb);
        	if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ACTUALIZAR EL CAMPO OBLIGATORIO DE UN SW");
        	if (m_Log.isDebugEnabled()) m_Log.debug(sqlSetOb);
        	if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Obligatoria: " + ob);
        	if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Clave SW: " +  cfo);        	
        	int i =1;
        	ps.setInt(i++, ob);
        	ps.setLong(i++, cfo);

        	int updatedRows = ps.executeUpdate();
        	if (updatedRows !=1 ) {
        		throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA ACTUALIZAR EL CAMPO OBLIGATORIO DE UN SW");
        	}
        	
    	} catch (BDException bde) {
    		m_Log.error("ERROR AL OBTENER LA CONEXION PARA ACTUALIZAR EL CAMPO OBLIGATORIO DE UN SW", bde);
        	throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA ACTUALIZAR EL CAMPO OBLIGATORIO DE UN SW", bde);
    	} catch (SQLException sqle) {
       		m_Log.error("ERROR EN LA CONSULTA PARA ACTUALIZAR EL CAMPO OBLIGATORIO DE UN SW", sqle);
       		throw new TechnicalException("ERROR EN LA CONSULTA PARA ACTUALIZAR EL CAMPO OBLIGATORIO DE UN SW", sqle);
   		} finally {
       		GeneralOperations.closeResultSet(rs);
       		GeneralOperations.closeStatement(ps);
       		devolverConexion(abd, con);
   		}         	
    }
    
    public void creaEstructura(long cfo,Vector paramsIn,Vector paramsOut, Connection con) 
    	throws TechnicalException, InternalErrorException {
        

    	
		try {
			
			for(Iterator it = paramsIn.iterator(); it.hasNext();) {
				ParametroConfigurableVO param = (ParametroConfigurableVO) it.next();
				insertParam(cfo, param, con);
			}
			for(Iterator it = paramsOut.iterator(); it.hasNext();) {
				ParametroConfigurableVO param = (ParametroConfigurableVO) it.next();
				insertParam(cfo, param, con);
			}

		
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		
			e.printStackTrace();
		}
		
    }
    public void eliminarDefinicionOp(int codigoDefOp, Connection con) throws SQLException, InternalErrorException {

        String sqlDeleteArrayDef = "DELETE FROM SW_ARRAY_DEF WHERE OPS_DEF_COD = ?";
        String sqlDeleteParOut = "DELETE FROM SW_PARAMS_OUT_DEF WHERE OPS_DEF_COD = ?";
        String sqlDeleteParIn = "DELETE FROM SW_PARAMS_IN_DEF WHERE OPS_DEF_COD = ?";
        String sqlDeleteOpsDef = "DELETE FROM SW_OPS_DEF WHERE OPS_DEF_COD = ?";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sqlDeleteArrayDef);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ELIMINAR LA DEFINICION DE LOS ARRAYS DE UNA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlDeleteArrayDef);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Operacion: " + codigoDefOp);

            ps.setInt(1, codigoDefOp);
            ps.executeUpdate();

            ps.close();

            ps = con.prepareStatement(sqlDeleteParOut);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ELIMINAR LA DEFINICION DE LOS PARAMETROS DE SALIDA DE UNA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlDeleteParOut);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Operacion: " + codigoDefOp);

            ps.setInt(1, codigoDefOp);
            ps.executeUpdate();

            ps.close();

            ps = con.prepareStatement(sqlDeleteParIn);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ELIMINAR LA DEFINICION DE LOS PARAMETROS DE ENTRADA DE UNA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlDeleteParIn);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Operacion: " + codigoDefOp);

            ps.setInt(1, codigoDefOp);
            ps.executeUpdate();

            ps.close();

            ps = con.prepareStatement(sqlDeleteOpsDef);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ELIMINAR LA DEFINICION DE UNA OPERACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlDeleteOpsDef);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Operacion: " + codigoDefOp);

            ps.setInt(1, codigoDefOp);
            int deletedRows = ps.executeUpdate();
            if (deletedRows != 1) throw new SQLException("ERROR EN EL RESULTADO DEVUELTO POR LA CONSULTA PARA ELIMINAR LA DEFINICION DE UNA OPERACION DEL SERVICIO WEB");

        } finally {
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

    private String createStrIndex(int index, int numInstancias) {
        String strNumInstancias = Integer.toString(numInstancias);
        int lengNumInstancias = strNumInstancias.length();
        String strIndex = Integer.toString(index);
        int lengIndex = strIndex.length();
        while (lengIndex < lengNumInstancias) {
            strIndex = "0" + strIndex;
            lengIndex++;
        }
        return strIndex;
    }



    public ArrayList<String> getModulos(String codProcedimiento,Connection con){
        ArrayList<String> modulos = new ArrayList<String>();
        Statement st = null;
        ResultSet rs = null;
        
        try{
            String sql = "SELECT DISTINCT(DEF_TRA_NOMBRE_MODULO) AS MODULO FROM DEF_TRA_SW " +
                          "WHERE DEF_TRA_PRO='" + codProcedimiento + "' AND DEF_TRA_TIPO_ORIGEN_OPERACION='MODULO'";
            m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                modulos.add(rs.getString("MODULO"));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return modulos;
    }


}
