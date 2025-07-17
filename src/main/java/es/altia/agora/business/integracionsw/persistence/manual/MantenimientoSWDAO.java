package es.altia.agora.business.integracionsw.persistence.manual;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.jdbc.GeneralOperations;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.common.exception.TechnicalException;
import es.altia.agora.business.integracionsw.*;
import es.altia.agora.business.integracionsw.util.TraductorTipoBasico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MantenimientoSWDAO {

    protected static Log m_Log = LogFactory.getLog(MantenimientoSWDAO.class.getName());

    private static MantenimientoSWDAO instance = null;

    protected MantenimientoSWDAO() {
    }

    public static MantenimientoSWDAO getInstance() {
        if (instance == null) {
            synchronized (MantenimientoSWDAO.class) {
                if (instance == null) instance = new MantenimientoSWDAO();
            }
        }
        return instance;
    }

    public boolean existeServicioWebPorTitulo(String tituloSW, String[] params) throws TechnicalException, InternalErrorException {

        String sqlQuery = "SELECT SW_COD FROM SW_INFO WHERE SW_TIT = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();
            ps = con.prepareStatement(sqlQuery);
            if (m_Log.isDebugEnabled())
                m_Log.debug("CONSULTA PARA COMPROBAR SI EXISTE UN SERVICIO WEB CON UN DETERMINADO TITULO");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlQuery);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Titulo Servicio Web:" + tituloSW);

            ps.setString(1, tituloSW);

            rs = ps.executeQuery();

            return rs.next();

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA COMPROBAR SI EXISTE UN SERVICIO WEB CON UN DETERMINADO TITULO", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA COMPROBAR SI EXISTE UN SERVICIO " +
                    "WEB CON UN DETERMINADO TITULO", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA COMPROBAR SI EXISTE UN SERVICIO WEB CON UN DETERMINADO TITULO", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA COMPROBAR SI EXISTE UN SERVICIO " +
                    "WEB CON UN DETERMINADO TITULO", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public InfoServicioWebVO altaServicioWeb(InfoServicioWebVO infoSWVO, String[] params)
            throws TechnicalException, InternalErrorException {

        String sqlSelectNuevoCodSW = "SELECT MAX(SW_COD) FROM SW_INFO";

        String sqlInsertInfoSW = "INSERT INTO SW_INFO(SW_COD, SW_TIT, SW_URL, SW_WSDL, SW_PUB) VALUES (?, ?, ?, ?, ?)";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Obtenemos la conexion y comenzamos la transaccion.
            con = abd.getConnection();
            abd.inicioTransaccion(con);

            // Obtenemos el nuevo codigo de Servicio Web.
            int nuevoCodigo = 0;
            ps = con.prepareStatement(sqlSelectNuevoCodSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA EL NUEVO CODIGO DE UN SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertInfoSW);

            rs = ps.executeQuery();
            if (rs.next()) nuevoCodigo = rs.getInt(1);
            infoSWVO.setCodigoSW(nuevoCodigo + 1);

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

            // Insertamos la informacion general del Servicio Web.
            ps = con.prepareStatement(sqlInsertInfoSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR LA INFORMACION DE UN SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertInfoSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Codigo Servicio Web:" + infoSWVO.getCodigoSW());
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 2: Titulo Servicio Web:" + infoSWVO.getTituloSW());
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 3: URL Servicio Web:" + infoSWVO.getUrlAccesoSW());
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 4: WSDL Servicio Web:" + infoSWVO.getWsdlSW());
            if (m_Log.isDebugEnabled())
                m_Log.debug("PARAMETRO 5: Publicar Servicio Web?:" + infoSWVO.isEstaPublicado());

            int i = 1;
            ps.setInt(i++, infoSWVO.getCodigoSW());
            ps.setString(i++, infoSWVO.getTituloSW());
            ps.setString(i++, infoSWVO.getUrlAccesoSW());
            ps.setString(i++, infoSWVO.getWsdlSW());
            ps.setBoolean(i, infoSWVO.isEstaPublicado());

            int insertedRows = ps.executeUpdate();

            if (insertedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR " +
                        "LA INFORMACION GENERAL DE UN SERVICIO WEB");

            GeneralOperations.closeStatement(ps);

            // Insertamos la informacion de las operaciones asociadas
            for (Object o : infoSWVO.getOperacionesSW()) {
                OperacionServicioWebVO opSW = (OperacionServicioWebVO) o;

                altaOperacionSW(opSW, infoSWVO.getCodigoSW(), con);

            }

            commitTransaction(abd, con);
            return infoSWVO;

        } catch (BDException bde) {
            rollBackTransaction(abd, con);
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA INSERTAR LA INFORMACION DE UN SERVICIO WEB", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA INSERTAR LA INFORMACION DE UN" +
                    " SERVICIO WEB", bde);
        } catch (SQLException sqle) {
            rollBackTransaction(abd, con);
            m_Log.error("ERROR EN LA CONSULTA PARA INSERTAR LA INFORMACION DE UN SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA INSERTAR LA INFORMACION DE UN SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public void cambiarEstadoPublicacionSW(int codigoSW, boolean nuevoEstado, String[] params)
            throws TechnicalException, InternalErrorException {

        String sqlUpdateEstado = "UPDATE SW_INFO SET SW_PUB = ? WHERE SW_COD = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlUpdateEstado);
            if (m_Log.isDebugEnabled())
                m_Log.debug("CONSULTA PARA MODIFICAR EL ESTADO DE PUBLICACION DE UN SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlUpdateEstado);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Esta Publicado?:" + nuevoEstado);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 2: Codigo Servicio Web:" + codigoSW);

            int i = 1;
            ps.setBoolean(i++, nuevoEstado);
            ps.setInt(i, codigoSW);

            int updatedRows = ps.executeUpdate();

            if (updatedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA MODIFICAR " +
                        "EL ESTADO DE PUBLICACION DE UN SERVICIO WEB");

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA MODIFICAR EL ESTADO DE PUBLICACION DE UN SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA MODIFICAR EL ESTADO DE PUBLICACION DE UN " +
                    "SERVICIO WEB", sqle);
        } catch (BDException bde) {
            m_Log.error("ERROR EN LA CONSULTA PARA MODIFICAR EL ESTADO DE PUBLICACION DE UN SERVICIO WEB", bde);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA MODIFICAR EL ESTADO DE PUBLICACION DE UN " +
                    "SERVICIO WEB", bde);
        } finally {
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }

    }

    public Collection getListaServiciosWeb(String[] params) throws TechnicalException, InternalErrorException {

        String sqlQuery = "SELECT SW_COD, SW_TIT, SW_URL, SW_WSDL, SW_PUB FROM SW_INFO";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlQuery);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA EL LISTADO DE SERVICIOS WEB EN LA APLICACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlQuery);

            rs = ps.executeQuery();

            Collection listaServicios = new ArrayList();
            while (rs.next()) {
                int i = 1;
                int codigoSW = rs.getInt(i++);
                String tituloSW = rs.getString(i++);
                String urlSW = rs.getString(i++);
                String wsdlSW = rs.getString(i++);
                boolean publicado = rs.getBoolean(i);

                InfoServicioWebVO infoSW = new InfoServicioWebVO(codigoSW, tituloSW, urlSW, wsdlSW, publicado);
                listaServicios.add(infoSW);

            }
            if (m_Log.isDebugEnabled()) m_Log.debug("DEVOLVEMOS LA LISTA CON LOS SERVICIOS WEB RECUPERADOS. " +
                    "RECUPERADOS " + listaServicios.size() + " SERVICIOS WEB");
            return listaServicios;

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR LA LISTA DE SERVICIOS WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR LA LISTA DE SERVICIOS WEB", sqle); //To change body of catch statement use File | Settings | File Templates.
        } catch (BDException bde) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR LA LISTA DE SERVICIOS WEB", bde);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR LA LISTA DE SERVICIOS WEB", bde);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    private void altaOperacionSW(OperacionServicioWebVO opSWVO, int codigoSW, Connection con)
            throws TechnicalException, InternalErrorException {

        String sqlInsertOpSW = "INSERT INTO SW_OPS(OPS_SW_COD, OPS_SW_NOM, OPS_SW_NS, OPS_SW_SAU, OPS_SW_STY) " +
                "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = null;
        try {

            ps = con.prepareStatement(sqlInsertOpSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR UNA OPERACION DE UN SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertOpSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Codigo Servicio Web:" + codigoSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 2: Nombre Operacion:" + opSWVO.getNombreOperacion());
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 3: Namespace Operacion:" + opSWVO.getNamespace());
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 4: Soap Action Uri:" + opSWVO.getSoapActionUri());
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 5: Style Operation:" + opSWVO.getOpStyle());

            int i = 1;
            ps.setInt(i++, codigoSW);
            ps.setString(i++, opSWVO.getNombreOperacion());
            ps.setString(i++, opSWVO.getNamespace());
            ps.setString(i++, opSWVO.getSoapActionUri());
            ps.setString(i, opSWVO.getOpStyle());

            int insertedRows = ps.executeUpdate();

            if (insertedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR " +
                        "UNA OPERACION DE UN SERVICIO WEB");

            GeneralOperations.closeStatement(ps);

            ParametroSWVO paramSalida = opSWVO.getSalida();
            altaParamSalidaSW(paramSalida, codigoSW, opSWVO.getNombreOperacion(), con);
            int orden = 0;
            Vector listaParamsIn = opSWVO.getParamsEntrada();
            while (orden < listaParamsIn.size()) {
                ParametroSWVO paramIn = (ParametroSWVO) listaParamsIn.elementAt(orden);
                altaParamEntradaSW(paramIn, codigoSW, opSWVO.getNombreOperacion(), orden + 1, con);
                orden++;
            }

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA INSERTAR UNA OPERACION DE UN SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA INSERTAR UNA OPERACION DE UN SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeStatement(ps);
        }
    }

    private void altaParamSalidaSW(ParametroSWVO paramSalida, int codigoSW, String nombreOp, Connection con)
            throws TechnicalException, InternalErrorException {

        String sqlInsertOutParam = "INSERT INTO SW_PARAMS(PRM_SW_COD, PRM_SW_OP, PRM_ORD, PRM_NOM, PRM_IN_OUT, " +
                "PRM_CLASS, PRM_TIP_COD) VALUES (?, ?, 0, ?, 0, ?, ?)";

        // Primero hay que comprobar de que clase de parametro se trata y ver si ya ha sido guardado en BBDD.
        TipoServicioWebVO tipo = paramSalida.getTipoParametro();
        int claseTipo = -1;
        int codigoTipo = -1;
        if (tipo.esTipoBase()) {
            TipoBasicoVO tipoBasico = (TipoBasicoVO) tipo;
            claseTipo = TipoServicioWebVO.TIPO_BASE;
            codigoTipo = TraductorTipoBasico.getCodigo(tipoBasico.getNombreTipo());
        } else if (tipo.esTipoArray()) {
            TipoArrayVO tipoArray = (TipoArrayVO) tipo;
            claseTipo = TipoServicioWebVO.TIPO_ARRAY;
            codigoTipo = creaExisteTipoArray(tipoArray, codigoSW, con);
        } else if (tipo.esTipoComplejo()) {
            TipoCompuestoVO tipoCompuesto = (TipoCompuestoVO) tipo;
            claseTipo = TipoServicioWebVO.TIPO_COMPLEJO;
            codigoTipo = creaExisteTipoCompuesto(tipoCompuesto, codigoSW, con);
        }
        String nombreParametro = paramSalida.getNombreParametro();

        if (claseTipo == -1 || codigoTipo == -1) throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CLASE" +
                " Y EL CODIGO DEL TIPO PARA GUARDARLOS");

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sqlInsertOutParam);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR EL PARAMETRO DE SALIDA DE UNA OPERACION" +
                    " DEL SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertOutParam);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Codigo Servicio Web:" + codigoSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 2: Nombre Operacion:" + nombreOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 3: Nombre Parametro:" + nombreParametro);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 4: Codigo Clase del Tipo:" + claseTipo);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 5: Codigo del Tipo:" + codigoTipo);

            int i = 1;
            ps.setInt(i++, codigoSW);
            ps.setString(i++, nombreOp);
            ps.setString(i++, nombreParametro);
            ps.setInt(i++, claseTipo);
            ps.setInt(i, codigoTipo);

            int insertedRows = ps.executeUpdate();
            if (insertedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR " +
                        "EL PARAMETRO DE SALIDA DE UNA OPERACION DEL SERVICIO WEB");

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA INSERTAR EL PARAMETRO DE SALIDA DE UNA OPERACION DEL " +
                    "SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA INSERTAR EL PARAMETRO DE SALIDA DE UNA OPERACION " +
                    "DEL SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeStatement(ps);
        }
    }

    private void altaParamEntradaSW(ParametroSWVO paramIn, int codigoSW, String nombreOp, int orden, Connection con)
            throws InternalErrorException, TechnicalException {

        String sqlInsertInParam = "INSERT INTO SW_PARAMS(PRM_SW_COD, PRM_SW_OP, PRM_ORD, PRM_NOM, PRM_IN_OUT, " +
                "PRM_CLASS, PRM_TIP_COD) VALUES (?, ?, ?, ?, 1, ?, ?)";

        // Primero hay que comprobar de que clase de parametro se trata y ver si ya ha sido guardado en BBDD.
        TipoServicioWebVO tipo = paramIn.getTipoParametro();
        int claseTipo = -1;
        int codigoTipo = -1;
        if (tipo.esTipoBase()) {
            TipoBasicoVO tipoBasico = (TipoBasicoVO) tipo;
            claseTipo = TipoServicioWebVO.TIPO_BASE;
            codigoTipo = TraductorTipoBasico.getCodigo(tipoBasico.getNombreTipo());
        } else if (tipo.esTipoArray()) {
            TipoArrayVO tipoArray = (TipoArrayVO) tipo;
            claseTipo = TipoServicioWebVO.TIPO_ARRAY;
            codigoTipo = creaExisteTipoArray(tipoArray, codigoSW, con);
        } else if (tipo.esTipoComplejo()) {
            TipoCompuestoVO tipoCompuesto = (TipoCompuestoVO) tipo;
            claseTipo = TipoServicioWebVO.TIPO_COMPLEJO;
            codigoTipo = creaExisteTipoCompuesto(tipoCompuesto, codigoSW, con);
        }

        if (claseTipo == -1 || codigoTipo == -1) throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CLASE" +
                " Y EL CODIGO DEL TIPO PARA GUARDARLOS");

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sqlInsertInParam);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR UN PARAMETRO DE ENTRADA DE UNA OPERACION" +
                    " DEL SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertInParam);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Codigo Servicio Web:" + codigoSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 2: Nombre Operacion:" + nombreOp);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 3: Orden del Parametro:" + orden);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 4: Nombre Parametro:" + paramIn.getNombreParametro());
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 5: Codigo Clase del Tipo:" + claseTipo);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 6: Codigo del Tipo:" + codigoTipo);

            int i = 1;
            ps.setInt(i++, codigoSW);
            ps.setString(i++, nombreOp);
            ps.setInt(i++, orden);
            ps.setString(i++, paramIn.getNombreParametro());
            ps.setInt(i++, claseTipo);
            ps.setInt(i, codigoTipo);

            int insertedRows = ps.executeUpdate();
            if (insertedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR " +
                        "UN PARAMETRO DE ENTRADA DE UNA OPERACION DEL SERVICIO WEB");

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA INSERTAR UN PARAMETRO DE ENTRADA DE UNA OPERACION DEL " +
                    "SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA INSERTAR UN PARAMETRO DE ENTRADA DE UNA OPERACION " +
                    "DEL SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeStatement(ps);
        }
    }

    private int creaExisteTipoArray(TipoArrayVO tipoArray, int codigoSW, Connection con)
            throws TechnicalException, InternalErrorException {

        String sqlExisteTipo = "SELECT TIP_COD FROM SW_TIPO WHERE TIP_CLASE = ? AND TIP_NOM = ? AND TIP_SW_COD = ? AND " +
                "TIP_CONT_CLASS = ? AND TIP_CONT_TIPO = ?";

        String sqlNuevoCodigoTipo = "SELECT MAX(TIP_COD) FROM SW_TIPO WHERE TIP_CLASE = ?";

        String sqlInsertaTipo = "INSERT INTO SW_TIPO" +
                "(TIP_COD, TIP_CLASE, TIP_NOM, TIP_SW_COD, TIP_NS, TIP_CONT_CLASS, TIP_CONT_TIPO) " +
                "VALUES (?, ?, ?, ?, NULL, ?, ?)";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Primero necesitamos saber el valor del codigo del tipo contenido en el array.
            TipoServicioWebVO tipo = tipoArray.getTipoContenido();
            int claseTipoContenido = -1;
            int codigoTipoContenido = -1;
            if (tipo.esTipoBase()) {
                TipoBasicoVO tipoBasicoContenido = (TipoBasicoVO) tipo;
                claseTipoContenido = TipoServicioWebVO.TIPO_BASE;
                codigoTipoContenido = TraductorTipoBasico.getCodigo(tipoBasicoContenido.getNombreTipo());
            } else if (tipo.esTipoArray()) {
                TipoArrayVO tipoArrayContenido = (TipoArrayVO) tipo;
                claseTipoContenido = TipoServicioWebVO.TIPO_ARRAY;
                codigoTipoContenido = creaExisteTipoArray(tipoArrayContenido, codigoSW, con);
            } else if (tipo.esTipoComplejo()) {
                TipoCompuestoVO tipoCompuesto = (TipoCompuestoVO) tipo;
                claseTipoContenido = TipoServicioWebVO.TIPO_COMPLEJO;
                codigoTipoContenido = creaExisteTipoCompuesto(tipoCompuesto, codigoSW, con);
            }

            if (claseTipoContenido == -1 || codigoTipoContenido == -1) throw new TechnicalException(
                    "ERROR AL INTENTAR OBTENER LA CLASE Y EL CODIGO DEL TIPO PARA GUARDARLOS");

            ps = con.prepareStatement(sqlExisteTipo);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA VER SI YA EXISTE CIERTO TIPO ARRAY DE UN " +
                    "SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlExisteTipo);
            if (m_Log.isDebugEnabled())
                m_Log.debug("PARAMETRO 1: Codigo Clase del Tipo:" + TipoServicioWebVO.TIPO_ARRAY);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 2: Nombre del Tipo:" + tipoArray.getNombreTipo());
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 3: Codigo del Servicio Web:" + codigoSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 4: Clase del Tipo Contenido:" + claseTipoContenido);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 5: Codigo del Tipo Contenido:" + codigoTipoContenido);

            int i = 1;
            ps.setInt(i++, TipoServicioWebVO.TIPO_ARRAY);
            ps.setString(i++, tipoArray.getNombreTipo());
            ps.setInt(i++, codigoSW);
            ps.setInt(i++, claseTipoContenido);
            ps.setInt(i, codigoTipoContenido);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

            // El tipo no existe, por lo que vamos a obtener un nuevo codigo para guardarlo.
            ps = con.prepareStatement(sqlNuevoCodigoTipo);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER UN NUEVO CODIGO PARA UN TIPO ARRAY DE UN " +
                    "SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlNuevoCodigoTipo);
            if (m_Log.isDebugEnabled())
                m_Log.debug("PARAMETRO 1: Codigo Clase del Tipo:" + TipoServicioWebVO.TIPO_ARRAY);

            ps.setInt(1, TipoServicioWebVO.TIPO_ARRAY);

            rs = ps.executeQuery();

            // Obtenemos el nuevo codigo.
            int nuevoCodigo = 0;
            if (rs.next()) nuevoCodigo = rs.getInt(1);
            nuevoCodigo++;

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

            // Ahora insertamos en la BBDD el nuevo tipo de clase Array.
            ps = con.prepareStatement(sqlInsertaTipo);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR UN TIPO ARRAY DE UN " +
                    "SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertaTipo);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Codigo del Tipo:" + nuevoCodigo);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 2: Clase del Tipo:" + TipoServicioWebVO.TIPO_ARRAY);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 3: Nombre del Tipo:" + tipoArray.getNombreTipo());
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 4: Codigo del Servicio Web:" + codigoSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 5: Clase del Tipo Contenido:" + claseTipoContenido);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 6: Codigo del Tipo Contenido:" + codigoTipoContenido);

            i = 1;
            ps.setInt(i++, nuevoCodigo);
            ps.setInt(i++, TipoServicioWebVO.TIPO_ARRAY);
            ps.setString(i++, tipoArray.getNombreTipo());
            ps.setInt(i++, codigoSW);
            ps.setInt(i++, claseTipoContenido);
            ps.setInt(i, codigoTipoContenido);

            int insertedRows = ps.executeUpdate();

            if (insertedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR " +
                        "UN TIPO DE CLASE ARRAY");

            return nuevoCodigo;

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA VER SI YA EXISTE CIERTO TIPO ARRAY DE UN " +
                    "SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA VER SI YA EXISTE CIERTO TIPO ARRAY DE UN SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
        }
    }

    private int creaExisteTipoCompuesto(TipoCompuestoVO tipoCompuesto, int codigoSW, Connection con)
            throws TechnicalException, InternalErrorException {

        String sqlBuscaTipoCompuesto = "SELECT TIP_COD FROM SW_TIPO WHERE TIP_CLASE = ? AND TIP_NOM = ? AND " +
                "TIP_SW_COD = ? AND TIP_NS = ?";

        String sqlNuevoCodigoTipo = "SELECT MAX(TIP_COD) FROM SW_TIPO WHERE TIP_CLASE = ?";

        String sqlInsertaTipo = "INSERT INTO SW_TIPO" +
                "(TIP_COD, TIP_CLASE, TIP_NOM, TIP_SW_COD, TIP_NS, TIP_CONT_CLASS, TIP_CONT_TIPO) " +
                "VALUES (?, ?, ?, ?, ?, NULL, NULL)";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = con.prepareStatement(sqlBuscaTipoCompuesto);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA COMPROBAR SI EXISTE UN TIPO COMPUESTO DE UN " +
                    "SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaTipoCompuesto);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Clase del Tipo:" + TipoServicioWebVO.TIPO_COMPLEJO);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 2: Nombre del Tipo:" + tipoCompuesto.getNombreTipo());
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 3: Codigo del Servicio Web:" + codigoSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 4: Namespace del Tipo:" + tipoCompuesto.getNamespace());

            int i = 1;
            ps.setInt(i++, TipoServicioWebVO.TIPO_COMPLEJO);
            ps.setString(i++, tipoCompuesto.getNombreTipo());
            ps.setInt(i++, codigoSW);
            ps.setString(i, tipoCompuesto.getNamespace());

            rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

            // El tipo no existe, así que insertamos su informacion general en la bbdd.
            // Primero obtenemos el nuevo codigo de Tipo.
            // El tipo no existe, por lo que vamos a obtener un nuevo codigo para guardarlo.
            ps = con.prepareStatement(sqlNuevoCodigoTipo);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER UN NUEVO CODIGO PARA UN TIPO COMPLEJO " +
                    "DE UN SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlNuevoCodigoTipo);
            if (m_Log.isDebugEnabled())
                m_Log.debug("PARAMETRO 1: Codigo Clase del Tipo:" + TipoServicioWebVO.TIPO_COMPLEJO);

            ps.setInt(1, TipoServicioWebVO.TIPO_COMPLEJO);

            rs = ps.executeQuery();
            // Obtenemos el nuevo codigo.
            int nuevoCodigo = 0;
            if (rs.next()) nuevoCodigo = rs.getInt(1);
            nuevoCodigo++;

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

            // Ahora insertamos en la BBDD el nuevo tipo de clase Complejo.
            ps = con.prepareStatement(sqlInsertaTipo);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR UN TIPO COMPLEJO DE UN " +
                    "SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertaTipo);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Codigo del Tipo:" + nuevoCodigo);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 2: Clase del Tipo:" + TipoServicioWebVO.TIPO_COMPLEJO);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 3: Nombre del Tipo:" + tipoCompuesto.getNombreTipo());
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 4: Codigo del Servicio Web:" + codigoSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 5: Namespace del Servicio Web:" +
                    tipoCompuesto.getNamespace());

            i = 1;
            ps.setInt(i++, nuevoCodigo);
            ps.setInt(i++, TipoServicioWebVO.TIPO_COMPLEJO);
            ps.setString(i++, tipoCompuesto.getNombreTipo());
            ps.setInt(i++, codigoSW);
            ps.setString(i, tipoCompuesto.getNamespace());

            int insertedRows = ps.executeUpdate();

            if (insertedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR " +
                        "UN TIPO DE CLASE COMPLEJO");

            GeneralOperations.closeStatement(ps);

            // Ahora insertamos los campos que componen el Tipo Complejo.
            int indexCampo = 1;
            for (CampoTipoCompuestoVO tipoCampo : tipoCompuesto.getFields()) {
                String clave = tipoCampo.getIdCampo();
                TipoServicioWebVO tipo = tipoCampo.getTipoCampo();
                grabarCampoTipoCompuesto(tipo, nuevoCodigo, clave, indexCampo, codigoSW, con);
                indexCampo++;
            }

            return nuevoCodigo;

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA VER SI YA EXISTE CIERTO TIPO COMPUESTO DE UN SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA VER SI YA EXISTE CIERTO TIPO COMPLEJO DE UN " +
                    "SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
        }

    }

    private void grabarCampoTipoCompuesto(TipoServicioWebVO tipo, int codTipoCompuesto, String nombreCampo,
                                          int indexCampo, int codigoSW, Connection con)
    throws TechnicalException, InternalErrorException {

        String sqlInsertCampo = "INSERT INTO SW_CAMPO_TIPO(CAMPO_TIP_COD, CAMPO_NOM, CAMPO_ORD, CAMPO_CLASE, CAMPO_TIPO)" +
                " VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = null;

        try {
            int claseTipoCampo = -1;
            int codigoTipoCampo = -1;
            if (tipo.esTipoBase()) {
                TipoBasicoVO tipoBasicoContenido = (TipoBasicoVO) tipo;
                claseTipoCampo = TipoServicioWebVO.TIPO_BASE;
                codigoTipoCampo = TraductorTipoBasico.getCodigo(tipoBasicoContenido.getNombreTipo());
            } else if (tipo.esTipoArray()) {
                TipoArrayVO tipoArrayContenido = (TipoArrayVO) tipo;
                claseTipoCampo = TipoServicioWebVO.TIPO_ARRAY;
                codigoTipoCampo = creaExisteTipoArray(tipoArrayContenido, codigoSW, con);
            } else if (tipo.esTipoComplejo()) {
                TipoCompuestoVO tipoCompuesto = (TipoCompuestoVO) tipo;
                claseTipoCampo = TipoServicioWebVO.TIPO_COMPLEJO;
                codigoTipoCampo = creaExisteTipoCompuesto(tipoCompuesto, codigoSW, con);
            }

            if (claseTipoCampo == -1 || codigoTipoCampo == -1) throw new TechnicalException("ERROR AL INTENTAR" +
                    " OBTENER LA CLASE Y EL CODIGO DEL TIPO PARA GUARDARLOS");

            ps = con.prepareStatement(sqlInsertCampo);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR UN TIPO COMPLEJO DE UN " +
                    "SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertCampo);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Codigo del Tipo Compuesto:" + codTipoCompuesto);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 2: Nombre del Campo:" + nombreCampo);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 3: Orden del Campo:" + indexCampo);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 4: Clase del Tipo del Campo:" + claseTipoCampo);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 5: Codigo del Tipo del Campo:" + codigoTipoCampo);

            int i = 1;
            ps.setInt(i++, codTipoCompuesto);
            ps.setString(i++, nombreCampo);
            ps.setInt(i++, indexCampo);
            ps.setInt(i++, claseTipoCampo);
            ps.setInt(i, codigoTipoCampo);

            int insertedRows = ps.executeUpdate();

            if (insertedRows != 1)
                throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA INSERTAR " +
                        "UN CAMPO DE UN TIPO CLASE COMPLEJO");

        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA CREAR EL CAMPO DE UN TIPO COMPUESTO DE UN SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA CREAR EL CAMPO DE UN TIPO COMPUESTO DE UN " +
                    "SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeStatement(ps);
        }
    }

    public InfoServicioWebVO getInfoGeneralSWByCodigo(int codigoSW, String[] params) throws InternalErrorException, TechnicalException {

        String sqlBuscaInfoSW = "SELECT SW_TIT, SW_URL, SW_WSDL, SW_PUB FROM SW_INFO WHERE SW_COD = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlBuscaInfoSW);

            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA BUSCAR LA INFORMACION GENERAL DE UN " +
                    "SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaInfoSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Codigo del Servicio Web:" + codigoSW);

            ps.setInt(1, codigoSW);

            rs = ps.executeQuery();

            if (rs.next()) {
                int i = 1;
                String tituloSW = rs.getString(i++);
                String urlSW = rs.getString(i++);
                String wsdlSW = rs.getString(i++);
                boolean isPublicado = rs.getBoolean(i);

                return new InfoServicioWebVO(codigoSW, tituloSW, urlSW, wsdlSW, isPublicado);
            } else
                throw new InternalErrorException(new Exception("NO SE HA ENCONTRADO NINGUN SERVICIO WEB CON ESE CODIGO"));

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA BUSCAR LA INFORMACION GENERAL DE UN SERVICIO WEB", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA BUSCAR LA INFORMACION GENERAL " +
                    "DE UN SERVICIO WEB", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA BUSCAR LA INFORMACION GENERAL DE UN SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA BUSCAR LA INFORMACION GENERAL " +
                    "DE UN SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }

    }

    public Collection getOperacionesByServicioWeb(int codigoSW, String[] params) throws TechnicalException, InternalErrorException {

        String sqlBuscaOperaciones = "SELECT OPS_SW_NOM FROM SW_OPS WHERE OPS_SW_COD = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlBuscaOperaciones);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA BUSCAR LAS OPERACIONES DE UN SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscaOperaciones);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Codigo del Servicio Web:" + codigoSW);

            ps.setInt(1, codigoSW);

            rs = ps.executeQuery();

            ArrayList nombreOperaciones = new ArrayList();
            while (rs.next()) {
                nombreOperaciones.add(rs.getString(1));
            }

            return nombreOperaciones;

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA BUSCAR LAS OPERACIONES DE UN SERVICIO WEB", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA BUSCAR LA INFORMACION GENERAL " +
                    "DE UN SERVICIO WEB", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA BUSCAR LAS OPERACIONES DE UN SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA BUSCAR LA INFORMACION GENERAL " +
                    "DE UN SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public boolean puedePublicarseServicioWeb(int codigoSW, String[] params) throws TechnicalException, InternalErrorException {

        String sqlPuedePublicarse = "SELECT * FROM SW_OPS_DEF WHERE OPS_SW_COD = ? AND OPS_DEF_PUB = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlPuedePublicarse);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA COMPROBAR SI PUEDE PUBLICARSE UN SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlPuedePublicarse);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Codigo del Servicio Web:" + codigoSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 2: Estado Publicacion: true");

            int i = 1;
            ps.setInt(i++, codigoSW);
            ps.setBoolean(i, true);

            rs = ps.executeQuery();

            return rs.next();

        } catch (BDException bde) {
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA COMPROBAR SI PUEDE PUBLICARSE UN SERVICIO WEB", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA COMPROBAR SI PUEDE PUBLICARSE UN SERVICIO WEB", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA COMPROBAR SI PUEDE PUBLICARSE UN SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LA CONSULTA PARA COMPROBAR SI PUEDE PUBLICARSE UN SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public void eliminarServicioWeb(int codigoSW, Collection ops, String[] params) throws TechnicalException, InternalErrorException {

        String sqlDeleteParams = "DELETE FROM SW_PARAMS WHERE PRM_SW_COD = ?";
        String sqlDeleteTipos = "DELETE FROM SW_TIPO WHERE TIP_SW_COD = ?";
        String sqlDeleteOps = "DELETE FROM SW_OPS WHERE OPS_SW_COD = ?";
        String sqlDeleteSWInfo = "DELETE FROM SW_INFO WHERE SW_COD = ?";
        
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = abd.getConnection();
            abd.inicioTransaccion(con);

            // Comenzamos eliminando las definiciones de las operaciones creadas.
            for (Iterator itOps = ops.iterator(); itOps.hasNext(); ) {
                DefinicionOperacionVO defOp = (DefinicionOperacionVO)itOps.next();
                DefinicionOperacionesSWDAO.getInstance().eliminarDefinicionOp(defOp.getCodigoDefinicionOp(), con);
            }

            // Ahora eliminaremos la info del SW propiamente dicho.
            // Primero recuperamos los tipos compuestos asociados al servicio web.
            Vector codsTipos = getCodigosTiposComplejos(codigoSW, con);
            // Eliminamos los campos asociados a dichos tipos compuestos.
            for (Iterator itCods = codsTipos.iterator(); itCods.hasNext(); ) {
                int codigo = (Integer)itCods.next();
                eliminaCamposTipoCompuesto(codigo, con);
            }

            // Eliminamos la informacion de los parametros de las Operaciones del Servicio Web.
            ps = con.prepareStatement(sqlDeleteParams);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ELIMINAR LOS PARAMETROS DE LAS OPERACIONES DE UN SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlDeleteParams);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Codigo del Servicio Web: " + codigoSW);

            ps.setInt(1, codigoSW);
            ps.executeUpdate();
            ps.close();

            // Eliminamos la informacion de los tipos asociados a las operaciones del Servicio Web
            ps = con.prepareStatement(sqlDeleteTipos);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ELIMINAR LOS TIPOS DE LAS OPERACIONES DE UN SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlDeleteTipos);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Codigo del Servicio Web: " + codigoSW);

            ps.setInt(1, codigoSW);
            ps.executeUpdate();
            ps.close();

            // Eliminamos la informacion de las operaciones asociadas al Servicio Web.
            ps = con.prepareStatement(sqlDeleteOps);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ELIMINAR LAS OPERACIONES DE UN SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlDeleteOps);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Codigo del Servicio Web: " + codigoSW);

            ps.setInt(1, codigoSW);
            ps.executeUpdate();
            ps.close();

            // Eliminamos la informacion del Servicio Web.
            ps = con.prepareStatement(sqlDeleteSWInfo);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ELIMINAR LA INFORMACION DE UN SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlDeleteSWInfo);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Codigo del Servicio Web: " + codigoSW);

            ps.setInt(1, codigoSW);
            int deletedRows = ps.executeUpdate();
            if (deletedRows != 1) throw new SQLException("ERROR EN LOS DATOS DEVUELTOS POR LA CONSULTA PARA ELIMINAR LA INFORMACION DE UN SERVICIO WEB");
            ps.close();

            commitTransaction(abd, con);

        } catch (BDException bde) {
            rollBackTransaction(abd, con);
            m_Log.error("ERROR AL OBTENER LA CONEXION PARA ELIMINAR LA INFORMACION DE UN SERVICIO WEB", bde);
            throw new TechnicalException("ERROR AL OBTENER LA CONEXION PARA ELIMINAR LA INFORMACION DE UN SERVICIO WEB", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LAS CONSULTAS PARA ELIMINAR LA INFORMACION DE UN SERVICIO WEB", sqle);
            throw new TechnicalException("ERROR EN LAS CONSULTAS PARA ELIMINAR LA INFORMACION DE UN SERVICIO WEB", sqle);
        } finally {
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }


    }

    private void eliminaCamposTipoCompuesto(int codigoTipo, Connection con) throws SQLException, InternalErrorException {

        String sqlDeleteCampos = "DELETE FROM SW_CAMPO_TIPO WHERE CAMPO_TIP_COD = ?";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sqlDeleteCampos);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ELIMINAR LOS CAMPOS DE UN TIPO COMPLEJO");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlDeleteCampos);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Codigo del Tipo Complejo: " + codigoTipo);

            ps.setInt(1, codigoTipo);
            ps.executeUpdate();

        } finally {
            GeneralOperations.closeStatement(ps);
        }
    }

    private Vector getCodigosTiposComplejos(int codigoSW, Connection con) throws SQLException, InternalErrorException {

        String sqlGetCodsComplejos = "SELECT TIP_COD FROM SW_TIPO WHERE TIP_CLASE = ? AND TIP_SW_COD = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sqlGetCodsComplejos);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR LOS CODIGOS DE LOS TIPOS COMPLEJOS ASOCIADOS A UN SERVICIO WEB");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetCodsComplejos);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1: Codigo de Tipo Complejo: " + TipoCompuestoVO.TIPO_COMPLEJO);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 2: Codigo de Servicio Web: " + codigoSW);

            ps.setInt(1, TipoCompuestoVO.TIPO_COMPLEJO);
            ps.setInt(2, codigoSW);

            rs = ps.executeQuery();

            Vector codigos = new Vector();
            while (rs.next()) {
                codigos.add(rs.getInt(1));
            }

            return codigos;

        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
        }
    }

    // METODOS PARA GESTIONAR LAS CONEXIONES CON LA BASE DE DATOS.
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

}
