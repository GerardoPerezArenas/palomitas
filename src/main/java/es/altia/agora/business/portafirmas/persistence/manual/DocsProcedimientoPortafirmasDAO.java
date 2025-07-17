package es.altia.agora.business.portafirmas.persistence.manual;

import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoProcedimientoFirmaVO;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.TechnicalException;
import es.altia.util.commons.DateOperations;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.log4j.Logger;

/**
 *
 * @author Tiffany
 */
public class DocsProcedimientoPortafirmasDAO {

    private static DocsProcedimientoPortafirmasDAO instance = null;
    private Logger log = Logger.getLogger(DocsProcedimientoPortafirmasDAO.class);
    public static final String ESTADO_PENDIENTE_DOCUMENTO = "O";
    public static final String ESTADO_FIRMADO_DOCUMENTO = "F";
    public static final String ESTADO_RECHAZADO_DOCUMENTO = "R";
    public static final String ESTADO_SUBSANADO_DOCUMENTO = "S";
    
    public static DocsProcedimientoPortafirmasDAO getInstance() {
        if (instance == null) {
            instance = new DocsProcedimientoPortafirmasDAO();
        }

        return instance;
    }

    public ArrayList<DocumentoProcedimientoFirmaVO> getDocumentoExpedientePortafirmas(
            String nifUsuarioDelegado, String nifUsuarioActual, String estado, int organizacion,
            Connection con) throws TechnicalException {
        ResultSet rs = null;
        Statement st = null;
        String codUsuarioActual = "";
        String codUsuarioDelegado = "";
        String org = String.valueOf(organizacion);

        ArrayList<DocumentoProcedimientoFirmaVO> documentos = new ArrayList<DocumentoProcedimientoFirmaVO>();
        try {

            String sql = "SELECT USU_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE USU_NIF='"
                    + nifUsuarioActual + "'";

            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                codUsuarioActual = rs.getString("USU_COD");
            }


            if (nifUsuarioDelegado != null) {
                sql = "SELECT USU_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE USU_NIF='"
                        + nifUsuarioDelegado + "'";

                log.debug(sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);
                if (rs.next()) {
                    codUsuarioDelegado = rs.getString("USU_COD");
                }
            }

            //Necesito saber el código interno del cargo "TODOS"
            sql = "SELECT CAR_COD FROM A_CAR WHERE CAR_COD_VIS='TD'";
            st = con.prepareStatement(sql);
            rs = st.executeQuery(sql);
            String codigoCargoTodos = "";
            if (rs.next()) {
                codigoCargoTodos = rs.getString("CAR_COD");
            }

            //Tenemso que comprobar que existe el cargo todos para tratar la query segunexista o no.
            String sqlPendientes = "SELECT PRESENTADO_COD_DOC,ID_DOC_PRESENTADO,doc_firma_orden,pml_cod,PML_VALOR,PRESENTADO_NUM,DOC_FIRMA_FECHA,DOC_FECHA_ENVIO,ID_DOC_FIRMA,DOC_FIRMA_USUARIO,DOC_FIRMA_ESTADO,"
                    + "PRESENTADO_NOMBRE, FIRMA_FIN_REC, PRESENTADO_TIPO FROM E_DOCS_FIRMAS firmas LEFT JOIN E_DOCS_PRESENTADOS "
                    + "ON (PRESENTADO_COD       =ID_DOC_PRESENTADO) "
                    + "LEFT JOIN E_PML ON (PRESENTADO_PRO=PML_COD) "
                    +" LEFT JOIN E_DEF_FIRMA DF ON (FIRMA_PROC =PRESENTADO_PRO AND DOC_FIRMA_ORDEN = DF.FIRMA_ORDEN AND PRESENTADO_COD_DOC = DF.FIRMA_COD_DOC)"
                    + " WHERE ( ";
            if (nifUsuarioActual != null && nifUsuarioDelegado != null) {
                sqlPendientes += " doc_firma_usuario IN (" + codUsuarioDelegado + "," + codUsuarioActual + ")";
            } else if (nifUsuarioActual != null && nifUsuarioDelegado == null) {
                sqlPendientes += " doc_firma_usuario IN (" + codUsuarioActual + ")";
            }
            sqlPendientes += " OR (EXISTS "
                    + "(SELECT uor_cod FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "a_uou ON (usu_cod=uou_usu "
                    + "AND uou_org=" + org
                    + ") LEFT JOIN a_uor ON (uou_uor  =uor_cod) WHERE ";
            if (nifUsuarioActual != null && nifUsuarioDelegado != null) {
                sqlPendientes += " usu_cod IN (" + codUsuarioDelegado + "," + codUsuarioActual + ")";
            } else if (nifUsuarioActual != null && nifUsuarioDelegado == null) {
                sqlPendientes += " usu_cod IN (" + codUsuarioActual + ")";
            }
            sqlPendientes += "AND uor_cod  =firmas.doc_firma_uor) AND (doc_firma_cargo IS NULL ";
            if (codigoCargoTodos != null && !codigoCargoTodos.equals("")) {
                sqlPendientes += " OR doc_firma_cargo    ='" + codigoCargoTodos + "'";
            }
            sqlPendientes += ") and doc_firma_usuario is null ) "
                    + "OR (EXISTS (SELECT uor_cod, car_cod FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "a_uou "
                    + "ON (usu_cod=uou_usu AND uou_org=" + org + ") LEFT JOIN a_uor ON (uou_uor=uor_cod) LEFT JOIN a_car "
                    + "ON (car_cod  =uou_car) WHERE ";
            if (nifUsuarioActual != null && nifUsuarioDelegado != null) {
                sqlPendientes += " usu_cod IN (" + codUsuarioDelegado + "," + codUsuarioActual + ")";
            } else if (nifUsuarioActual != null && nifUsuarioDelegado == null) {
                sqlPendientes += " usu_cod IN (" + codUsuarioActual + ")";
            }
            sqlPendientes += "AND uor_cod  =firmas.doc_firma_uor AND ";
            sqlPendientes += "(car_cod  =firmas.doc_firma_cargo ";
            if (codigoCargoTodos != null && !codigoCargoTodos.equals("")) {
                sqlPendientes += " OR CAR_COD= " + codigoCargoTodos;
            }
            sqlPendientes += ")) AND (doc_firma_cargo IS NOT NULL ";
            if (codigoCargoTodos != null && !codigoCargoTodos.equals("")) {
                sqlPendientes += "AND doc_firma_cargo  <>'" + codigoCargoTodos + "'";
            }
            
            /** original
            sqlPendientes += ") and doc_firma_usuario is null ) ) AND doc_firma_estado  ='O' AND NOT EXISTS (SELECT * FROM e_docs_firmas aux WHERE "
                    + "firmas.id_doc_presentado=aux.id_doc_presentado AND firmas.doc_firma_orden    >aux.doc_firma_orden "
                    + "AND aux.doc_firma_estado='O' )";
            **/
            
            
            sqlPendientes += ") and doc_firma_usuario is null ) ) AND doc_firma_estado  ='O' AND NOT EXISTS (SELECT * FROM e_docs_firmas aux WHERE "
                    + "(firmas.id_doc_presentado=aux.id_doc_presentado AND firmas.doc_firma_orden>aux.doc_firma_orden  AND aux.doc_firma_estado='O') "
                    + "OR (firmas.id_doc_presentado=aux.id_doc_presentado AND firmas.doc_firma_orden>aux.doc_firma_orden  AND aux.doc_firma_estado='S'))";
                    
            
            

            
            /******************* SUBSANADOS ***************************/
            
            String sqlSubsanados = "SELECT PRESENTADO_COD_DOC,ID_DOC_PRESENTADO,doc_firma_orden,pml_cod,PML_VALOR,PRESENTADO_NUM,DOC_FIRMA_FECHA,DOC_FECHA_ENVIO,ID_DOC_FIRMA,DOC_FIRMA_USUARIO,DOC_FIRMA_ESTADO,"
                    + "PRESENTADO_NOMBRE, FIRMA_FIN_REC, PRESENTADO_TIPO FROM E_DOCS_FIRMAS firmas LEFT JOIN E_DOCS_PRESENTADOS "
                    + "ON (PRESENTADO_COD       =ID_DOC_PRESENTADO) "
                    + "LEFT JOIN E_PML ON (PRESENTADO_PRO=PML_COD) "
                    +" LEFT JOIN E_DEF_FIRMA DF ON (FIRMA_PROC =PRESENTADO_PRO AND DOC_FIRMA_ORDEN = DF.FIRMA_ORDEN AND PRESENTADO_COD_DOC = DF.FIRMA_COD_DOC)"
                    + " WHERE ( ";
            if (nifUsuarioActual != null && nifUsuarioDelegado != null) {
                sqlSubsanados += " doc_firma_usuario IN (" + codUsuarioDelegado + "," + codUsuarioActual + ")";
            } else if (nifUsuarioActual != null && nifUsuarioDelegado == null) {
                sqlSubsanados += " doc_firma_usuario IN (" + codUsuarioActual + ")";
            }
            sqlSubsanados += " OR (EXISTS "
                    + "(SELECT uor_cod FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "a_uou ON (usu_cod=uou_usu "
                    + "AND uou_org=" + org
                    + ") LEFT JOIN a_uor ON (uou_uor  =uor_cod) WHERE ";
            if (nifUsuarioActual != null && nifUsuarioDelegado != null) {
                sqlSubsanados += " usu_cod IN (" + codUsuarioDelegado + "," + codUsuarioActual + ")";
            } else if (nifUsuarioActual != null && nifUsuarioDelegado == null) {
                sqlSubsanados += " usu_cod IN (" + codUsuarioActual + ")";
            }
            sqlSubsanados += "AND uor_cod  =firmas.doc_firma_uor) AND (doc_firma_cargo IS NULL ";
            if (codigoCargoTodos != null && !codigoCargoTodos.equals("")) {
                sqlSubsanados += " OR doc_firma_cargo    ='" + codigoCargoTodos + "'";
            }
            sqlSubsanados += ") and doc_firma_usuario is null ) "
                    + "OR (EXISTS (SELECT uor_cod, car_cod FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "a_uou "
                    + "ON (usu_cod=uou_usu AND uou_org=" + org + ") LEFT JOIN a_uor ON (uou_uor=uor_cod) LEFT JOIN a_car "
                    + "ON (car_cod  =uou_car) WHERE ";
            if (nifUsuarioActual != null && nifUsuarioDelegado != null) {
                sqlSubsanados += " usu_cod IN (" + codUsuarioDelegado + "," + codUsuarioActual + ")";
            } else if (nifUsuarioActual != null && nifUsuarioDelegado == null) {
                sqlSubsanados += " usu_cod IN (" + codUsuarioActual + ")";
            }
            sqlSubsanados += "AND uor_cod  =firmas.doc_firma_uor AND ";
            sqlSubsanados += "(car_cod  =firmas.doc_firma_cargo ";
            if (codigoCargoTodos != null && !codigoCargoTodos.equals("")) {
                sqlSubsanados += " OR CAR_COD= " + codigoCargoTodos;
            }
            sqlSubsanados += ")) AND (doc_firma_cargo IS NOT NULL ";
            if (codigoCargoTodos != null && !codigoCargoTodos.equals("")) {
                sqlSubsanados += "AND doc_firma_cargo  <>'" + codigoCargoTodos + "'";
            }
            sqlSubsanados += ") and doc_firma_usuario is null ) ) AND doc_firma_estado  ='S' AND NOT EXISTS (SELECT * FROM e_docs_firmas aux WHERE "
                    + "firmas.id_doc_presentado=aux.id_doc_presentado AND firmas.doc_firma_orden    >aux.doc_firma_orden "
                    + "AND aux.doc_firma_estado='S' )";

            
            /********************* SUBSANADOS *************************/
            
            
            
            /** ORIGINAL
            String sqlFirmados = "SELECT PRESENTADO_COD_DOC,ID_DOC_PRESENTADO,doc_firma_orden,pml_cod,PML_VALOR,PRESENTADO_NUM,DOC_FIRMA_FECHA,DOC_FECHA_ENVIO,ID_DOC_FIRMA,"
                    + "DOC_FIRMA_USUARIO,DOC_FIRMA_ESTADO"
                    + ",PRESENTADO_NOMBRE FROM E_DOCS_FIRMAS firmas LEFT JOIN E_DOCS_PRESENTADOS ON"
                    + " (PRESENTADO_COD       =ID_DOC_PRESENTADO) "
                    + "LEFT JOIN E_PML ON (PML_COD=PRESENTADO_PRO) "
                    + "where doc_firma_estado='F' and ";
                    ***/
            String sqlFirmados = "SELECT PRESENTADO_COD_DOC,ID_DOC_PRESENTADO,doc_firma_orden,pml_cod,PML_VALOR,PRESENTADO_NUM,DOC_FIRMA_FECHA,DOC_FECHA_ENVIO,ID_DOC_FIRMA,"
                    + "DOC_FIRMA_USUARIO,DOC_FIRMA_ESTADO"
                    + ",PRESENTADO_NOMBRE,FIRMA_FIN_REC, PRESENTADO_TIPO FROM E_DOCS_FIRMAS firmas LEFT JOIN E_DOCS_PRESENTADOS ON"
                    + " (PRESENTADO_COD       =ID_DOC_PRESENTADO) "
                    + "LEFT JOIN E_PML ON (PML_COD=PRESENTADO_PRO) "
                    +" LEFT JOIN E_DEF_FIRMA DF ON (FIRMA_PROC =PRESENTADO_PRO AND DOC_FIRMA_ORDEN = DF.FIRMA_ORDEN AND PRESENTADO_COD_DOC = DF.FIRMA_COD_DOC)"                    
                    + "where doc_firma_estado='F' and ";
            
            if (nifUsuarioActual != null && nifUsuarioDelegado != null) {
                sqlFirmados += " doc_firma_usuario IN (" + codUsuarioDelegado + "," + codUsuarioActual + ")";
            } else if (nifUsuarioActual != null && nifUsuarioDelegado == null) {
                sqlFirmados += " doc_firma_usuario IN (" + codUsuarioActual + ")";
            }

            String sqlRechazados = "SELECT PRESENTADO_COD_DOC,ID_DOC_PRESENTADO,doc_firma_orden,pml_cod,PML_VALOR,PRESENTADO_NUM,DOC_FIRMA_FECHA,DOC_FECHA_ENVIO,ID_DOC_FIRMA,"
                    + "DOC_FIRMA_USUARIO,DOC_FIRMA_ESTADO,"
                    + "PRESENTADO_NOMBRE,FIRMA_FIN_REC,PRESENTADO_TIPO FROM E_DOCS_FIRMAS firmas LEFT JOIN E_DOCS_PRESENTADOS ON"
                    + " (PRESENTADO_COD       =ID_DOC_PRESENTADO) LEFT JOIN E_PML ON (PML_COD=PRESENTADO_PRO)"
                    + " LEFT JOIN E_DEF_FIRMA DF ON (FIRMA_PROC =PRESENTADO_PRO AND DOC_FIRMA_ORDEN = DF.FIRMA_ORDEN AND PRESENTADO_COD_DOC = DF.FIRMA_COD_DOC)"                    
                    + " where doc_firma_estado='R' and ";
            
            if (nifUsuarioActual != null && nifUsuarioDelegado != null) {
                sqlRechazados += " doc_firma_usuario IN ('" + codUsuarioDelegado + "','" + codUsuarioActual + "')";
            } else if (nifUsuarioActual != null && nifUsuarioDelegado == null) {
                sqlRechazados += " doc_firma_usuario IN ('" + codUsuarioActual + "')";
            }

            sql = "";
            if (estado == null || "".equals(estado)) {
                //sql += sqlPendientes + " UNION " + sqlRechazados + " UNION " + sqlFirmados;
                sql += sqlPendientes + " UNION " + sqlRechazados + " UNION " + sqlFirmados + " UNION " + sqlSubsanados;
            } else if (estado.equals("O")) {
                sql = sqlPendientes;
            } else if (estado.equals("F")) {
                sql = sqlFirmados;
            } else if (estado.equals("R")) {
                sql = sqlRechazados;
            } else if(estado.equals("S")){
                sql = sqlSubsanados;
            }
            
            sql=sql+ "ORDER BY DOC_FECHA_ENVIO DESC";

            log.debug("CONSULTA DE FIRMAS DEL USUSARIO SEGÚN FILTRO: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {

                java.sql.Timestamp fechaFirma = rs.getTimestamp("DOC_FIRMA_FECHA");
                java.sql.Timestamp fechaEnvio = rs.getTimestamp("DOC_FECHA_ENVIO");

                DocumentoProcedimientoFirmaVO doc = new DocumentoProcedimientoFirmaVO();
                doc.setIdPresentado(rs.getInt("ID_DOC_PRESENTADO"));
                doc.setCodigoDocumento(rs.getInt("PRESENTADO_COD_DOC"));
                doc.setIdNumFirma(rs.getInt("ID_DOC_FIRMA"));
                doc.setCodigoUsuarioFirma(rs.getString("DOC_FIRMA_USUARIO"));
                doc.setEstadoFirma(rs.getString("DOC_FIRMA_ESTADO"));
                doc.setFechaEnvioFirma(DateOperations.timestampToCalendar(fechaEnvio));
                doc.setFechaFirma(DateOperations.timestampToCalendar(fechaFirma));
                doc.setNombreDocumento(rs.getString("PRESENTADO_NOMBRE"));
                doc.setIdProcedimiento(rs.getString("pml_cod"));
                doc.setNombreProcedimiento(rs.getString("pml_valor"));
                doc.setIdNumeroExpediente(rs.getString("presentado_num"));
                doc.setOrden(rs.getInt("doc_firma_orden"));
                if (estado == null || "".equals(estado) || estado.equals("O")){
                    doc.setFinalizaRechazo(rs.getString("FIRMA_FIN_REC"));
                }
                doc = obtenerFirmaAnterior(doc, con);
                documentos.add(doc);
                doc.setTipoMime(rs.getString("PRESENTADO_TIPO"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("ERROR EN LA CONSULTA DE OBTENCIÓN DE FIRMAS PENDIENTES");
            throw new TechnicalException("ERROR EN LA CONSULTA DE OBTENCIÓN DE FIRMAS PENDIENTES", e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
        }

        return documentos;
    }

    public Hashtable<String, String> getUsuarioDelegado(int codigoUsuario, Connection con) throws TechnicalException {
        Hashtable<String, String> salida = null;
        Statement st = null;
        ResultSet rs = null;

        try {

            String SQL = "SELECT USU_COD,USU_DELEGADO FROM " + GlobalNames.ESQUEMA_GENERICO + "USU_FIR_DEL "
                    + "WHERE USU_DELEGADO=" + codigoUsuario;

            log.debug(SQL);
            st = con.createStatement();
            rs = st.executeQuery(SQL);

            String usuQueDelega = null;
            String usuDelegado = null;
            /** SE COMPRUEBA SI AL USUARIO ACTUAL SE LE HA DELEGADO LA FIRMA POR PARTE DE ALGÚN OTRO USUARIO */
            while (rs.next()) {

                usuQueDelega = rs.getString("USU_COD");
                usuDelegado = rs.getString("USU_DELEGADO");
                salida = new Hashtable<String, String>();
                salida.put("COD_USUARIO_QUE_DELEGA", usuQueDelega);
                salida.put("COD_USUARIO_DELEGADO", usuDelegado);
                salida.put("DELEGACION_FIRMA", "SI");
            }

            st.close();
            rs.close();

            // Se recupera el nif del usuario delegado si lo hubiera
            st = con.createStatement();
            SQL = "SELECT USU_NIF FROM " + GlobalNames.ESQUEMA_GENERICO + " A_USU WHERE USU_COD=" + usuDelegado;
            rs = st.executeQuery(SQL);
            while (rs.next()) {
                String nifUsuarioDelegado = rs.getString("USU_NIF");
                salida.put("NIF_USUARIO_DELEGADO", nifUsuarioDelegado);
            }

            st.close();
            rs.close();


            /****  Se obtiene el nif del usuario que ha delegado la firma en el usuario actual *****/
            if (usuQueDelega != null && salida != null) {
                // Si al usuario actual le han delegado la firma => Hay que recuperar el DNI este usuario.
                SQL = "SELECT USU_NIF FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE USU_COD=" + usuQueDelega;
                st = con.createStatement();
                rs = st.executeQuery(SQL);
                while (rs.next()) {
                    String nifUsuarioQueDelega = rs.getString("USU_NIF");
                    if (nifUsuarioQueDelega != null && nifUsuarioQueDelega.length() > 0) {
                        salida.put("NIF_USUARIO_QUE_DELEGA", nifUsuarioQueDelega);
                    }
                }
            } else {
                // Como no se ha delegado en el usuario actual la firma => Se recupera su nif
                SQL = "SELECT USU_NIF FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE USU_COD=" + codigoUsuario;
                st = con.createStatement();
                rs = st.executeQuery(SQL);
                while (rs.next()) {
                    String nifUsuarioActual = rs.getString("USU_NIF");
                    if (nifUsuarioActual != null && nifUsuarioActual.length() > 0) {
                        salida = new Hashtable<String, String>();
                        salida.put("DELEGACION_FIRMA", "NO");
                        salida.put("COD_USUARIO_QUE_DELEGA", "-1");
                        salida.put("COD_USUARIO_DELEGADO", "-1");
                        salida.put("NIF_USUARIO_DELEGADO", "-1");
                        salida.put("NIF_USUARIO_QUE_DELEGA", "-1");
                        salida.put("NIF_USUARIO_ACTUAL", nifUsuarioActual);
                    }
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
            log.error("ERROR EN LA CONSULTA DE OBTENCIÓN DE USUSARIO DELEGADO");
            throw new TechnicalException("ERROR EN LA CONSULTA DE OBTENCIÓN DE USUSARIO DELEGADO", e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
        }
        return salida;
    }

    public boolean rechazarDocumento(DocumentoProcedimientoFirmaVO doc, Connection con) throws TechnicalException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exito = false;
        try {

            String sql = "UPDATE E_DOCS_FIRMAS SET DOC_FIRMA_ESTADO=?,"
                    + "DOC_FIRMA_USUARIO=?,DOC_FIRMA_FECHA=?,DOC_FIRMA_OBSERVACIONES=? "
                    + "WHERE ID_DOC_FIRMA=?";

            log.debug("guardarFirma RECHAZADO SQL: " + sql);
            int i = 1;
            ps = con.prepareStatement(sql);

            ps.setString(i++, this.ESTADO_RECHAZADO_DOCUMENTO);
            ps.setInt(i++, Integer.parseInt(doc.getCodigoUsuarioFirma()));
            if (doc.getFechaFirma() != null) {
                ps.setTimestamp(i++, DateOperations.toTimestamp(doc.getFechaFirma()));
            } else {
                ps.setNull(i++, java.sql.Types.TIMESTAMP);
            }
            ps.setString(i++, doc.getObservaciones());
            ps.setInt(i++, doc.getIdNumFirma());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 1) {
                exito = true;
            }

            if (exito) {
                avanzarCircuito(doc, con);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("ERROR AL GUARDAR EL RECHAZO DE LA FIRMA");
            throw new TechnicalException("ERROR AL GUARDAR EL RECHAZO DE LA FIRMA", ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.closeResultSet(rs);
        }

        return exito;

    }

    public boolean guardarFirma(DocumentoProcedimientoFirmaVO doc, Connection con) throws TechnicalException {
        PreparedStatement ps = null;
        boolean exito = false;
        try {

            String sql = "UPDATE E_DOCS_FIRMAS SET DOC_FIRMA_ESTADO=?, DOC_FIRMA_FECHA=?, FIRMA=?, DOC_FIRMA_USUARIO=? "
                    + "WHERE ID_DOC_FIRMA=?";

            log.debug("guardarFirma SQL: " + sql);
            int i = 1;
            ps = con.prepareStatement(sql);

            ps.setString(i++, this.ESTADO_FIRMADO_DOCUMENTO);
            if (doc.getFechaFirma() != null) {
                ps.setTimestamp(i++, DateOperations.toTimestamp(doc.getFechaFirma()));
            } else {
                ps.setNull(i++, java.sql.Types.TIMESTAMP);
            }
            // Contenido del fichero
            if (doc.getFirma() != null && doc.getFirma().length > 0) {
                java.io.InputStream st = new java.io.ByteArrayInputStream(doc.getFirma());
                ps.setBinaryStream(i++, st, doc.getFirma().length);
            } else {
                ps.setNull(i++, java.sql.Types.LONGVARBINARY);
            }

            ps.setInt(i++, Integer.parseInt(doc.getCodigoUsuarioFirma()));
            ps.setInt(i++, doc.getIdNumFirma());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 1) {
                exito = true;
                avanzarCircuito(doc, con);
            }

            log.debug("Número de filas actualizadas: " + rowsUpdated);

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("ERROR AL GUARDAR LA FIRMA DEL DOCUMENTO");
            throw new TechnicalException("ERROR AL GUARDAR LA FIRMA DEL DOCUMENTO", e);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }

        return exito;
    }

    public boolean avanzarCircuito(DocumentoProcedimientoFirmaVO doc, Connection con) throws TechnicalException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exito = false;
        try {

            //Actualizamos la siguiente firma en caso de que exista. En caso de que no exista no se hace nada.
            String sql = "SELECT ID_DOC_FIRMA AS SIGUIENTEFIRMA FROM E_DOCS_FIRMAS WHERE ID_DOC_PRESENTADO=? AND DOC_FIRMA_ORDEN="
                    + " 1+ (SELECT DOC_FIRMA_ORDEN FROM E_DOCS_FIRMAS WHERE ID_DOC_FIRMA=?)";
            log.debug(sql);
            log.debug("doc presentado= " + doc.getIdPresentado());
            log.debug("codigo firma realizada= " + doc.getIdNumFirma());
            ps = con.prepareStatement(sql);

            int i = 1;
            ps.setInt(i++, doc.getIdPresentado());
            ps.setInt(i++, doc.getIdNumFirma());

            rs = ps.executeQuery();
            if (rs.next()) {

                //Si existe una firma posterior, lo único que debo hacer es actualizar la fecha de envío a firma y el estado.
                sql = "UPDATE E_DOCS_FIRMAS SET DOC_FECHA_ENVIO=?, DOC_FIRMA_ESTADO=? WHERE ID_DOC_FIRMA=?";
                log.debug(sql);
                log.debug("siguiente firma = " + rs.getInt("SIGUIENTEFIRMA"));
                ps = con.prepareStatement(sql);
                i = 1;
                ps.setTimestamp(i++, DateOperations.toTimestamp(doc.getFechaFirma()));
                ps.setString(i++, this.ESTADO_PENDIENTE_DOCUMENTO);
                ps.setInt(i++, rs.getInt("SIGUIENTEFIRMA"));
                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated == 1) {
                    exito = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("ERROR AL AVANZAR EL CIRCUITO DE FIRMAS");
            throw new TechnicalException("ERROR AL AVANZAR LE CIRCUITO DE FIRMAS", e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            return exito;
        }
    }

    public DocumentoProcedimientoFirmaVO getDocumentoExpediente(DocumentoProcedimientoFirmaVO doc, Connection con) throws TechnicalException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT PRESENTADO_TIPO,PRESENTADO_NOMBRE,PRESENTADO_EXTENSION,PRESENTADO_CONTENIDO"
                + " FROM E_DOCS_PRESENTADOS WHERE PRESENTADO_COD =" + doc.getIdPresentado();
        try {

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                byte[] contenido = null;
                // Se lee el contenido binario del documento
                java.io.InputStream stream = rs.getBinaryStream("PRESENTADO_CONTENIDO");
                java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                int c;
                if (stream != null) {
                    while ((c = stream.read()) != -1) {
                        ot.write(c);
                    }
                }
                ot.flush();
                contenido = ot.toByteArray();
                ot.close();

                doc.setContenido(contenido);
                doc.setTipoMime(rs.getString("PRESENTADO_TIPO"));
                doc.setNombreDocumento(rs.getString("PRESENTADO_NOMBRE"));
                doc.setExtension(rs.getString("PRESENTADO_EXTENSION"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("ERROR EN LA CONSULTA PARA CONSULTAR LAS FIRMAS DE DOCS DE EXPEDIENTE");
            throw new TechnicalException("ERROR EN LA CONSULTA DE DOCUEMNTO DE UN EXPEDIENTE", ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error("ERROR EN LA RECUPERACIÓN EL CONTENIDO DEL DOCUMENTO DE EXPEDIENTE");
            throw new TechnicalException("ERROR EN LA RECUPERACIÓN EL CONTENIDO DEL DOCUMENTO DE EXPEDIENTE", ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return doc;
    }

    public DocumentoProcedimientoFirmaVO obtenerFirmaAnterior(DocumentoProcedimientoFirmaVO docu, Connection con) throws TechnicalException {

        String sql = "SELECT FIRMA FROM E_DOCS_FIRMAS WHERE DOC_FIRMA_ORDEN=? AND ID_DOC_PRESENTADO=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        byte[] r = null;
        log.debug(sql);

        try {
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, docu.getOrden() - 1);
            ps.setInt(i++, docu.getIdPresentado());

            rs = ps.executeQuery();

            if (rs.next()) {
                log.debug("Encontrado documento con firma anterior=" + docu.getIdPresentado());
                java.io.InputStream st = rs.getBinaryStream("FIRMA");

                java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                int c;
                if (st != null) {
                    while ((c = st.read()) != -1) {
                        ot.write(c);
                    }
                }
                ot.flush();
                r = ot.toByteArray();
                ot.close();
                st.close();
                docu.setFirma(r);
            } else {
                docu.setFirma(null);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("ERROR AL RECUPERAR LA FIRMA ANTERIOR");
            throw new TechnicalException("ERROR AL RECUPERAR LA FIRMA ANTERIOR", ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error("ERROR AL RECUPERAR LA FIRMA ANTERIOR");
            throw new TechnicalException("ERROR AL RECUPERAR LA FIRMA ANTERIOR", ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.closeResultSet(rs);
            return docu;
        }

    }

    public DocumentoProcedimientoFirmaVO getFirmaFichero(DocumentoProcedimientoFirmaVO docu, Connection con) throws TechnicalException {

        String sql = "SELECT FIRMA FROM E_DOCS_FIRMAS WHERE ID_DOC_FIRMA=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        byte[] r = null;
        log.debug(sql);

        try {
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, docu.getIdNumFirma());
            rs = ps.executeQuery();

            if (rs.next()) {
                java.io.InputStream st = rs.getBinaryStream("FIRMA");

                java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                int c;
                if (st != null) {
                    while ((c = st.read()) != -1) {
                        ot.write(c);
                    }
                }
                ot.flush();
                r = ot.toByteArray();
                ot.close();
                st.close();
                docu.setFirma(r);
            } else {
                docu.setFirma(null);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("ERROR AL RECUPERAR LA FIRMA ANTERIOR");
            throw new TechnicalException("ERROR AL RECUPERAR LA FIRMA ANTERIOR", ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error("ERROR AL RECUPERAR LA FIRMA ANTERIOR");
            throw new TechnicalException("ERROR AL RECUPERAR LA FIRMA ANTERIOR", ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.closeResultSet(rs);
            return docu;
        }

    }

    public ArrayList<DocumentoProcedimientoFirmaVO> getDocumentoFirma(String codigoFirma, Connection con) throws TechnicalException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList<DocumentoProcedimientoFirmaVO> documentos = new ArrayList<DocumentoProcedimientoFirmaVO>();
        DocumentoProcedimientoFirmaVO doc = new DocumentoProcedimientoFirmaVO();

        try {
            String sql = "SELECT * FROM E_DOCS_FIRMAS WHERE ID_DOC_FIRMA=?";
            byte[] r = null;
            log.debug(sql);

            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, Integer.valueOf(codigoFirma));
            rs = ps.executeQuery();

            if (rs.next()) {
                                
                java.io.InputStream st = rs.getBinaryStream("FIRMA");
                if (rs.wasNull()) doc.setFirma(null);
                else {
                    log.debug("hay firma");
                    java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                    int c;
                    if (st != null) { 
                        while ((c = st.read()) != -1) {
                            ot.write(c);
                        }
                    }
                    ot.flush();
                    r = ot.toByteArray();
                    ot.close();

                    doc.setFirma(r);
                }
                doc.setObservaciones(rs.getString("DOC_FIRMA_OBSERVACIONES"));
                doc.setCodigoUsuarioFirma(rs.getString("DOC_FIRMA_USUARIO"));
                doc.setFechaFirma(DateOperations.toCalendar(rs.getTimestamp("DOC_FIRMA_FECHA")));
                doc.setEstadoFirma(rs.getString("DOC_FIRMA_ESTADO"));
                doc.setIdNumFirma(Integer.valueOf(rs.getString("ID_DOC_FIRMA")));
                
                
                
                documentos.add(doc);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("ERROR EN LA CONSULTA DE OBTENCIÓN DE FIRMAS PENDIENTES");
            throw new TechnicalException("ERROR EN LA CONSULTA DE OBTENCIÓN DE FIRMAS PENDIENTES", e);
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error("ERROR AL RECUPERAR LA FIRMA ANTERIOR");
            throw new TechnicalException("ERROR AL RECUPERAR LA FIRMA ANTERIOR", ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            return documentos;
        }
    }

    public boolean subsanarDocumento(DocumentoProcedimientoFirmaVO doc, Connection con) throws TechnicalException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exito = false;
        log.debug(DocsProcedimientoPortafirmasDAO.class.getName() + "--> subsanarDocumento");
        try {
            String sql = "UPDATE E_DOCS_FIRMAS SET DOC_FIRMA_ESTADO = ?, "
                    + "DOC_FIRMA_USUARIO = ?, DOC_FIRMA_FECHA = ?, DOC_FIRMA_OBSERVACIONES = ? "
                    + "WHERE ID_DOC_FIRMA = ?";

            log.debug("guardarFirma SUBSANADO SQL: " + sql);
            int i = 1;
            ps = con.prepareStatement(sql);

            ps.setString(i++, this.ESTADO_SUBSANADO_DOCUMENTO);
            ps.setInt(i++, Integer.parseInt(doc.getCodigoUsuarioFirma()));
            if (doc.getFechaFirma() != null) {
                ps.setTimestamp(i++, DateOperations.toTimestamp(doc.getFechaFirma()));
            } else {
                ps.setNull(i++, java.sql.Types.TIMESTAMP);
            }
            ps.setString(i++, doc.getObservaciones());
            ps.setInt(i++, doc.getIdNumFirma());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 1) {
                exito = true;
            }
            if (exito) {
                avanzarCircuito(doc, con);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("ERROR AL GUARDAR LA SUBSANACIÓN DE LA FIRMA");
            throw new TechnicalException("ERROR AL GUARDAR LA SUBSANACIÓN DE LA FIRMA", ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.closeResultSet(rs);
        }
        log.debug(DocsProcedimientoPortafirmasDAO.class.getName() + "<-- subsanarDocumento");
        return exito;
    }
    
    
    /**
     * Recupera el nombre de un doucmento de inicio de expediente
     * @param codDocumentoPresentado: Código del documento presentado
     * @param numExpediente: Número del expediente
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la BBDD
     * @return String con el nombre del documento
     * @throws TechnicalException si ocurre algún error
     */
    public String getNombreDocumentoExpediente(int codDocumentoPresentado,String numExpediente,String codProcedimiento, Connection con) throws TechnicalException {
        String nombre = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT PRESENTADO_NOMBRE"
                + " FROM E_DOCS_PRESENTADOS WHERE PRESENTADO_COD_DOC =" + codDocumentoPresentado;
        log.debug(sql);
        try {

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while(rs.next()) {
                nombre = rs.getString("PRESENTADO_NOMBRE");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("ERROR EN LA CONSULTA PARA RECUPERAR EL NOMBRE DE UN DOUCMENTO DE INICIO DE EXPEDIENTE: " + ex.getMessage());
            throw new TechnicalException("ERROR EN LA CONSULTA DE DOCUEMNTO DE UN EXPEDIENTE", ex);
        }finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return nombre;
    }
    

    /**
     * Permite almacenar los datos de la firma de un documento de inicio de expedinete, pero sin almacenar el binario
     * de la firma, porque esta irá a un gestor documental
     * @param doc: Objeto de tipo DocumentoProcedimientoFirmaVO
     * @param con: Conexión a la BBDD
     * @return
     * @throws TechnicalException 
     */
    public boolean guardarDatosFirmaSinBinario(DocumentoProcedimientoFirmaVO doc, Connection con) throws TechnicalException {
        PreparedStatement ps = null;
        boolean exito = false;
        try {

            String sql = "UPDATE E_DOCS_FIRMAS SET DOC_FIRMA_ESTADO=?, DOC_FIRMA_FECHA=?, DOC_FIRMA_USUARIO=? "
                    + "WHERE ID_DOC_FIRMA=?";

            log.debug("guardarFirma SQL: " + sql);
            int i = 1;
            ps = con.prepareStatement(sql);

            ps.setString(i++, this.ESTADO_FIRMADO_DOCUMENTO);
            if (doc.getFechaFirma() != null) {
                ps.setTimestamp(i++, DateOperations.toTimestamp(doc.getFechaFirma()));
            } else {
                ps.setNull(i++, java.sql.Types.TIMESTAMP);
            }
            
            /**
            // Contenido del fichero
            if (doc.getFirma() != null && doc.getFirma().length > 0) {
                java.io.InputStream st = new java.io.ByteArrayInputStream(doc.getFirma());
                ps.setBinaryStream(i++, st, doc.getFirma().length);
            } else {
                ps.setNull(i++, java.sql.Types.LONGVARBINARY);
            } **/

            ps.setInt(i++, Integer.parseInt(doc.getCodigoUsuarioFirma()));
            ps.setInt(i++, doc.getIdNumFirma());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 1) {
                exito = true;
                avanzarCircuito(doc, con);
            }

            log.debug("Número de filas actualizadas: " + rowsUpdated);

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("ERROR AL GUARDAR LA FIRMA DEL DOCUMENTO");
            throw new TechnicalException("ERROR AL GUARDAR LA FIRMA DEL DOCUMENTO", e);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }

        return exito;
    }
    
}
