package es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.business;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.webservice.tramitacion.servicios.WSException;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.axis.encoding.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SWFirmaDocRelacionManager {

    private static Log m_Log = LogFactory.getLog(SWFirmaDocRelacionManager.class.getName());
    private String[] params;
    private static SWFirmaDocRelacionManager instance = null;


    protected SWFirmaDocRelacionManager(String[] params) {
            this.params = params;
    }

    public static SWFirmaDocRelacionManager getInstance(String[] params) {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(SWFirmaDocRelacionManager.class) {
                if (instance == null)
                    instance = new SWFirmaDocRelacionManager(params);
            }
        }
        return instance;
    }


    // Grabar un documento, almacenando su contenido en el Servicio Web de Firmadoc.
    public int grabarDocumentoRelacion(GeneralValueObject gvo) throws WSException, TechnicalException {

        AdaptadorSQLBD adaptadorBD = new AdaptadorSQLBD(params);
        Connection connection = null;
        int resultado;

        String numeroDocumento = (String)gvo.getAtributo("numeroDocumento");
        try {
            // Creamos la conexion e iniciamos la transaccion.
            connection = adaptadorBD.getConnection();
            adaptadorBD.inicioTransaccion(connection);

            // Comprobamos si existe o no numero de documento.
            // Si existe, se trata de una modificacion, sino de un alta.
            if ((numeroDocumento != null) && (!numeroDocumento.equals(""))) {
                resultado = modificarDocumentoRelacion(connection, gvo);
            } else {
                resultado = insertarNuevoDocumentoRelacion(connection, gvo);
            }

            commitTransaction(adaptadorBD, connection);

        } catch (WSException wse){
            rollBackTransaction(adaptadorBD, connection, wse);
            wse.printStackTrace();
            throw wse;
        } catch (Exception e){
            rollBackTransaction(adaptadorBD, connection, e);
            e.printStackTrace();
            throw new TechnicalException(e.getMessage());
        }
        return resultado;
    }

    private int modificarDocumentoRelacion(Connection connection, GeneralValueObject gvo)
    throws WSException {

        PreparedStatement ps;
        ResultSet rs;
        int resultado;
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        try {

            // Primer paso: Recupero el codigo de documento y el codigo de expediente de firmadoc.
            String sqlQuery = "SELECT CRD_EXP_FD, CRD_DOC_FD " +
                    "FROM G_CRD "+
                    "WHERE CRD_PRO = ? " +
                    "AND CRD_EJE = ? " +
                    "AND CRD_NUM = ? " +
                    "AND CRD_TRA = ? " +
                    "AND CRD_OCU = ? " +
                    "AND CRD_NUD = ? " +
                    "AND CRD_EXP_FD IS NOT NULL " +
                    "AND CRD_DOC_FD IS NOT NULL";
            ps = connection.prepareStatement(sqlQuery);
            int i = 1;
            ps.setString(i++, (String)gvo.getAtributo("codProcedimiento"));
            ps.setString(i++, (String)gvo.getAtributo("ejercicio"));
            ps.setString(i++, (String)gvo.getAtributo("numeroRelacion"));
            ps.setString(i++, (String)gvo.getAtributo("codTramite"));
            ps.setString(i++, (String)gvo.getAtributo("ocurrenciaTramite"));
            ps.setString(i, (String)gvo.getAtributo("numeroDocumento"));
            rs = ps.executeQuery();
            String codExpFirmaDoc;
            String codDocFirmaDoc;
            if (rs.next()) {
                i = 1;
                codExpFirmaDoc = rs.getString(i++);
                codDocFirmaDoc = rs.getString(i);
            } else throw new Exception("ERROR de consistencia en la BBDD de Agora");
            if ((codExpFirmaDoc == null) || (codDocFirmaDoc == null))
                throw new Exception("Datos Incorrectos en la BB.DD de Agora");
            rs.close();
            ps.close();

            // Segundo Paso: Insertar el documento nuevo en firmaDoc.
            String docWordBase64 = Base64.encode((byte[])gvo.getAtributo("ficheroWord"));
            String nombreDocumento = (String)gvo.getAtributo("nombreDocumento");
            String nuevoCodDocFirmaDoc = SWFirmaDocService.getInstance().almacenarFichero(
                    docWordBase64, nombreDocumento, "", null);

            // Tercer Paso: Modifico la BB.DD. de Agora.
            sqlQuery = "UPDATE G_CRD "+
                    "SET CRD_FMO = " + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", CRD_USM = ?, CRD_DES = ?, CRD_DOC_FD = ? " +
                    "WHERE CRD_PRO = ? " +
                    "AND CRD_EJE = ? " +
                    "AND CRD_NUM = ? " +
                    "AND CRD_TRA = ? " +
                    "AND CRD_OCU = ? " +
                    "AND CRD_NUD = ? " +
                    "AND CRD_EXP_FD IS NOT NULL";
            ps = connection.prepareStatement(sqlQuery);
            i = 1;
            ps.setString(i++, (String)gvo.getAtributo("codUsuario"));
            ps.setString(i++, nombreDocumento);
            ps.setString(i++, nuevoCodDocFirmaDoc);
            ps.setString(i++, (String)gvo.getAtributo("codProcedimiento"));
            ps.setString(i++, (String)gvo.getAtributo("ejercicio"));
            ps.setString(i++, (String)gvo.getAtributo("numeroRelacion"));
            ps.setString(i++, (String)gvo.getAtributo("codTramite"));
            ps.setString(i++, (String)gvo.getAtributo("ocurrenciaTramite"));
            ps.setString(i, (String)gvo.getAtributo("numeroDocumento"));
            resultado = ps.executeUpdate();
            if (resultado < 1) {
                throw new Exception("ERROR al actualizar los datos en BB.DD. de Agora");
            }

            // Cuarto Paso: Elimino el documento antiguo de firmadoc.
            SWFirmaDocService.getInstance().borrarDocumento(codDocFirmaDoc);

            // Quinto paso: Asocio el nuevo documento al codigo de expediente de firmadoc.
            SWFirmaDocService.getInstance().asociarDocumentoAExpediente(nuevoCodDocFirmaDoc, codExpFirmaDoc);

        } catch (SQLException e) {
            throw new WSException("ERROR en la BB.DD. de Agora", e);
        } catch (WSException e) {
            throw e;
        } catch (Exception e) {
            throw new WSException("ERROR en la BB.DD. de Agora", e);
        }
        return resultado;
    }

    private int insertarNuevoDocumentoRelacion(Connection connection, GeneralValueObject gvo)
        throws WSException, TechnicalException {

            PreparedStatement ps;
            ResultSet rs;
            int resultado;
            AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
            try {
                m_Log.debug("##### - Comprobar si es el primer documento asociado al expediente - #####");
                // Primer paso: Comprobar si es el primer documento asociado a este expediente.
                String sqlQuery = "SELECT CRD_EXP_FD FROM G_CRD " +
                        "WHERE CRD_MUN = ? " +
                        "AND CRD_PRO = ? " +
                        "AND CRD_EJE = ? " +
                        "AND CRD_NUM = ? " +
                        "AND CRD_DOC_FD IS NOT NULL " +
                        "AND CRD_EXP_FD IS NOT NULL";
                ps = connection.prepareStatement(sqlQuery);
                int i = 1;
                ps.setString(i++, (String)gvo.getAtributo("codMunicipio"));
                ps.setString(i++, (String)gvo.getAtributo("codProcedimiento"));
                ps.setString(i++, (String)gvo.getAtributo("ejercicio"));
                ps.setString(i, (String)gvo.getAtributo("numeroRelacion"));
                rs = ps.executeQuery();
                String codExpFirmaDoc = null;
                if (rs.next()) {
                    i = 1;
                    codExpFirmaDoc = rs.getString(i);
                }
                rs.close();
                ps.close();

                m_Log.debug("##### - Obtener descripcion del procedimiento y asunto del expediente - #####");
                // Segundo paso: Obtener descripcion del procedimiento y asunto del expediente.
                sqlQuery = "SELECT REL_ASU, PML_VALOR " +
                        "FROM G_REL JOIN E_PML ON (PML_COD = REL_PRO AND PML_MUN = REL_MUN) " +
                        "WHERE REL_PRO = ? " +
                        "AND REL_EJE = ? " +
                        "AND REL_NUM = ? " +
                        "AND REL_MUN = ? ";
                ps = connection.prepareStatement(sqlQuery);
                i = 1;
                ps.setString(i++, (String)gvo.getAtributo("codProcedimiento"));
                ps.setString(i++, (String)gvo.getAtributo("ejercicio"));
                ps.setString(i++, (String)gvo.getAtributo("numeroRelacion"));
                ps.setString(i, (String)gvo.getAtributo("codMunicipio"));
                rs = ps.executeQuery();
                String asuntoExpediente = "";
                String descProcedimiento = "";
                if (rs.next()) {
                    i = 1;
                    asuntoExpediente = rs.getString(i++);
                    descProcedimiento = rs.getString(i);
                }
                rs.close();
                ps.close();

//            // Tercer paso: Almacenamos en el Servicio Web el documento.
//            byte[] documentoWordByteArray = (byte[])gvo.getAtributo("ficheroWord");
//            String documentoWordBase64 = Base64.encode(documentoWordByteArray, 0, documentoWordByteArray.length);
//            String nombreDocumento = (String) gvo.getAtributo("nombreDocumento");
//            String codDocFirmaDoc =
//                    SWFirmaDocService.getInstance().almacenarFichero(
//                            documentoWordBase64, nombreDocumento, "", null);
//
//            // Cuarto Paso: Si no existe expediente, crear uno mediante el SW de firmadoc.
//            if (codExpFirmaDoc == null) {
//                codExpFirmaDoc = SWFirmaDocService.getInstance().crearExpediente(
//                        asuntoExpediente,
//                        (String)gvo.getAtributo("numeroExpediente"),
//                        (String)gvo.getAtributo("ejercicio"),
//                        (String)gvo.getAtributo("codProcedimiento"),
//                        descProcedimiento);
//            }

                m_Log.debug("##### - Guardar documento y crear expediente en el Servicio Web - #####");
                String codDocFirmaDoc;
                if (codExpFirmaDoc == null) {
                    // Es el primer documento asociado a un expediente.
                    byte[] documentoWordByteArray = (byte[])gvo.getAtributo("ficheroWord");
                    String documentoWordBase64 =
                            Base64.encode(documentoWordByteArray, 0, documentoWordByteArray.length);
                    gvo.setAtributo("base64", documentoWordBase64);
                    gvo.setAtributo("asuntoExpediente", asuntoExpediente);
                    gvo.setAtributo("descProcedimiento", descProcedimiento);
                    gvo.setAtributo("tipoDocumento", "");

                    GeneralValueObject resultados = SWFirmaDocService.getInstance().gestionCompleta(gvo);
                    codDocFirmaDoc = (String)resultados.getAtributo("codDocFirmaDoc");
                    codExpFirmaDoc = (String)resultados.getAtributo("codExpFirmaDoc");
                } else {
                    // Hay algun otro documento asociado a este expediente.
                    // Tercer paso: Almacenamos en el Servicio Web el documento.
                    byte[] documentoWordByteArray = (byte[])gvo.getAtributo("ficheroWord");
                    String documentoWordBase64 =
                            Base64.encode(documentoWordByteArray, 0, documentoWordByteArray.length);
                    String nombreDocumento = (String) gvo.getAtributo("nombreDocumento");
                    codDocFirmaDoc = SWFirmaDocService.getInstance().almacenarFichero(
                                    documentoWordBase64, nombreDocumento, "", null);

                    SWFirmaDocService.getInstance().asociarDocumentoAExpediente(codDocFirmaDoc, codExpFirmaDoc);
                }


                // Quinto Paso: Obtener el numero de documento para almacenarlo en la BBDD de agora.
                String numeroDocumento = obtenerNuevoNumeroDocumentoRelacion(connection, gvo);

                m_Log.debug("##### - Guardar informacion de documento y expediente en SGE - #####");
                // Sexto Paso: Almacenar la informacion relevante en la BBDD de agora.
                String sql = "INSERT INTO G_CRD(CRD_MUN, CRD_PRO, CRD_EJE, CRD_NUM, CRD_TRA, CRD_OCU, CRD_NUD, CRD_FAL, " +
                        "CRD_FMO, CRD_USC, CRD_USM, CRD_FIL, CRD_DES, CRD_DOT, CRD_FIR_EST, CRD_EXP_FD, CRD_DOC_FD, CRD_FIR_FD, " +
                        "CRD_EXP)" +
                        " VALUES (?, ?, ?, ?, ?, ?, ?, " + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", " + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", ?, ?, NULL, ?, ?, NULL, ?, ?, NULL, ?)";
                ps = connection.prepareStatement(sql);
                i = 1;
                ps.setString(i++, (String)gvo.getAtributo("codMunicipio"));
                ps.setString(i++, (String)gvo.getAtributo("codProcedimiento"));
                ps.setString(i++, (String)gvo.getAtributo("ejercicio"));
                ps.setString(i++, (String)gvo.getAtributo("numeroRelacion"));
                ps.setString(i++, (String)gvo.getAtributo("codTramite"));
                ps.setString(i++, (String)gvo.getAtributo("ocurrenciaTramite"));
                ps.setString(i++, numeroDocumento);
                ps.setString(i++, (String)gvo.getAtributo("codUsuario"));
                ps.setString(i++, (String)gvo.getAtributo("codUsuario"));
                ps.setString(i++, (String)gvo.getAtributo("nombreDocumento"));
                ps.setString(i++, (String)gvo.getAtributo("codDocumento"));
                ps.setString(i++, codExpFirmaDoc);
                ps.setString(i++, codDocFirmaDoc);
                ps.setString(i, (String)gvo.getAtributo("opcionGrabar"));

                resultado = ps.executeUpdate();
                ps.close();


            } catch (SQLException e) {
                e.printStackTrace();
                throw new TechnicalException("ERROR en el Servicio Web al insertar en Agora", e);
            }

            return resultado;
        }

    private String obtenerNuevoNumeroDocumentoRelacion(Connection connection, GeneralValueObject gvo) {

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        ResultSet rs = null;
        PreparedStatement stmt = null;
        String nuevoNumero = null;

        try {

            // Creamos la select con los parametros adecuados.
            String sql = "SELECT ";
            sql += abd.funcionSistema(
                    AdaptadorSQLBD.FUNCIONSISTEMA_NVL,
                    new String[]{abd.funcionMatematica(
                            AdaptadorSQLBD.FUNCIONMATEMATICA_MAX,
                            new String[]{"CRD_NUD"}) + "+1", "1"});
            sql += " FROM E_CRD " +
                    "WHERE CRD_PRO = ? " +
                    "AND CRD_EJE = ? " +
                    "AND CRD_NUM = ? " +
                    "AND CRD_TRA = ? " +
                    "AND CRD_OCU = ?";

            // Rellenar la query con los datos de entrada.
            stmt = connection.prepareStatement(sql);
            int i = 1;
            stmt.setString(i++, (String)gvo.getAtributo("codProcedimiento"));
            stmt.setString(i++, (String)gvo.getAtributo("ejercicio"));
            stmt.setString(i++, (String)gvo.getAtributo("numeroRelacion"));
            stmt.setString(i++, (String)gvo.getAtributo("codTramite"));
            stmt.setString(i, (String)gvo.getAtributo("ocurrenciaTramite"));

            // Ejecutar la query
            rs = stmt.executeQuery();
            if (rs.next()) {
                nuevoNumero = rs.getString(1);
            }

        } catch (Exception sqle) {
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Error de SQL en obtenMaximo: " + sqle.toString());
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch(Exception bde) {
                bde.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName());
            }
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("NUEVO NUMERO:::" + nuevoNumero);
        return nuevoNumero;
    }

    private void commitTransaction(AdaptadorSQLBD bd, Connection con) {
        try {
            bd.finTransaccion(con);
            bd.devolverConexion(con);
        } catch (Exception ex) {
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("SQLException haciendo commitTransaction en: " + getClass().getName());
        }
    }

    private void rollBackTransaction(AdaptadorSQLBD bd, Connection con, Exception e){
        try {
            bd.rollBack(con);
            bd.devolverConexion(con);
        } catch (Exception e1) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("SQLException haciendo rollBackTransaction en: " + getClass().getName());
        }
    }

    public void getListaDocumentosRelacionCronologia(TramitacionExpedientesValueObject tEVO)
            throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaDocumentosRelacionCronologia");

        Connection con = null;
        PreparedStatement ps;
        ResultSet rs;
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Vector listaDocumentos = new Vector();

        try{
            // Obtenemos la conexion.
            con = oad.getConnection();
            // Creamos la consulta SQL.
            String from = "CRD_NUD, CRD_DES, TO_CHAR(CRD_FAL, 'DD/MM/YYYY') AS CRD_FAL, " +
                    "TO_CHAR(CRD_FMO, 'DD/MM/YYYY') AS CRD_FMO, " +
                    "USU.USU_NOM AS USUARIOCREACION, USU1.USU_NOM AS USUARIOMODIFICACION, PLT_INT, CRD_FIR_EST";

            String where = "CRD_MUN = " + tEVO.getCodMunicipio() + " AND CRD_PRO ='" + tEVO.getCodProcedimiento() + "' " +
                    "AND CRD_TRA = " + tEVO.getCodTramite() + " AND CRD_NUM ='" + tEVO.getNumeroRelacion() + "' " +
                    "AND CRD_OCU = " + tEVO.getOcurrenciaTramite() + " AND CRD_EXP_FD IS NOT NULL " +
                    "AND CRD_DOC_FD IS NOT NULL ";
            String[] join1 = new String[14];
            join1[0] = "G_CRD";
            join1[1] = "INNER";
            join1[2] = GlobalNames.ESQUEMA_GENERICO + "A_USU USU";
            join1[3] = "G_CRD.CRD_USC = USU.USU_COD";
            join1[4] = "LEFT";
            join1[5] = GlobalNames.ESQUEMA_GENERICO + "A_USU USU1";
            join1[6] = "G_CRD.CRD_USM = USU1.USU_COD";
            join1[7] = "INNER";
            join1[8] = "E_DOT";
            join1[9] = "G_CRD.CRD_PRO = E_DOT.DOT_PRO AND G_CRD.CRD_TRA = E_DOT.DOT_TRA AND " +
                    "G_CRD.CRD_DOT = E_DOT.DOT_COD";
            join1[10] = "INNER";
            join1[11] = "A_PLT";
            join1[12] = "E_DOT.DOT_PLT = A_PLT.PLT_COD";
            join1[13] = "false";

            String sql = oad.join(from, where, join1);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = con.prepareStatement(sql);

            // Ejecutamos la consulta
            rs = ps.executeQuery();
            String entrar = "no";
            while ( rs.next() ) {
                entrar = "si";
                int i = 1;
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                String codDocumento = rs.getString(i++);
                tramExpVO.setCodDocumento(codDocumento);
                String descripcion = rs.getString(i++);
                tramExpVO.setDescDocumento(descripcion);
                String fechaCreacion = rs.getString(i++);
                tramExpVO.setFechaCreacion(fechaCreacion);
                String fechaModificacion = rs.getString(i++);
                if(fechaModificacion != null) {
                    if(!fechaModificacion.equals("")) {
                        tramExpVO.setFechaModificacion(fechaModificacion);
                    } else tramExpVO.setFechaModificacion("");
                } else tramExpVO.setFechaModificacion("");
                String usuarioCreacion = rs.getString(i++);
                String usuarioModificacion = rs.getString(i++);
                if(usuarioModificacion == null || usuarioModificacion.equals("")) {
                    tramExpVO.setUsuario(usuarioCreacion);
                } else tramExpVO.setUsuario(usuarioModificacion);
                String interesado = rs.getString(i++);
                tramExpVO.setInteresado(interesado);
                String estadoFirma = rs.getString(i);
                tramExpVO.setEstadoFirma(estadoFirma);
                listaDocumentos.addElement(tramExpVO);
                tEVO.setListaDocumentos(listaDocumentos);
            }
            if(entrar.equals("no")) {
                tEVO.setListaDocumentos(listaDocumentos);
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException("Error. TramitacionExpedientesDAO.getListaDocumentosRelacionCronologia", e);
        } finally {
            try {
                oad.devolverConexion(con);
            } catch (BDException be) {
                be.printStackTrace();
            }
        }

        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaDocumentosRelacionCronologia");
    }

    public byte[] loadDocumento(GeneralValueObject gvo) throws WSException {

        // Primer paso, obtener el codigo de documento en firmadoc.
        m_Log.debug("SWFirmaDocManager.loadDocumento: Obtenemos el numero de documento en SGE");
        String codDocFirmaDoc = obtenerCodDocFirmaDoc(gvo);

        // Segundo paso, extraer el fichero proveniente de firmadoc.
        String docExtraidoBase64 = SWFirmaDocService.getInstance().extraerFichero(codDocFirmaDoc);
        return Base64.decode(docExtraidoBase64);

    }

    private String obtenerCodDocFirmaDoc(GeneralValueObject gvo) {

        String codDocFirmaDoc = null;
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            conexion = abd.getConnection();

            String sql = "SELECT CRD_DOC_FD " +
                    "FROM G_CRD " +
                    "WHERE CRD_MUN = ? " +
                    "AND CRD_NUM = ? " +
                    "AND CRD_TRA = ? " +
                    "AND CRD_OCU = ? " +
                    "AND CRD_NUD = ? " +
                    "AND CRD_DOC_FD IS NOT NULL " +
                    "AND CRD_EXP_FD IS NOT NULL";
            ps = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            int i = 1;
            ps.setString(i++, (String)gvo.getAtributo("codMunicipio"));
            ps.setString(i++, AdaptadorSQLBD.js_unescape((String)gvo.getAtributo("numeroRelacion")));
            ps.setString(i++, (String)gvo.getAtributo("codTramite"));
            ps.setString(i++, (String)gvo.getAtributo("ocurrenciaTramite"));
            ps.setString(i, (String)gvo.getAtributo("numeroDocumento"));

            rs = ps.executeQuery();
            if (rs.next()) {
                i = 1;
                codDocFirmaDoc = rs.getString(i);
                m_Log.debug("CODIGO DE DCOUMENTO RECUPERADO:" + codDocFirmaDoc);
            }

        } catch (Exception sqle) {
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Error de SQL en loadDocumento: " + sqle.toString());
        } finally {
            try {
                rs.close();
                ps.close();
                abd.devolverConexion(conexion);
            } catch(Exception bde) {
                bde.getMessage();
                if(m_Log.isErrorEnabled()) m_Log.error("No se pudo devolver la conexion: " + bde.toString());
            }
        }
        return codDocFirmaDoc;

    }

    public int eliminarDocumentoRelacionCRD(TramitacionExpedientesValueObject tEVO) throws WSException {

        int resBorrado = 0;

        // Primer Paso: Obtener el codigo del documento en firmadoc.
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codMunicipio", tEVO.getCodMunicipio());
        gVO.setAtributo("numeroRelacion", tEVO.getNumeroRelacion());
        gVO.setAtributo("codTramite", tEVO.getCodTramite());
        gVO.setAtributo("ocurrenciaTramite", tEVO.getOcurrenciaTramite());
        gVO.setAtributo("numeroDocumento", tEVO.getCodDocumento());
        String codDocFirmaDoc = obtenerCodDocFirmaDoc(gVO);

        // Segundo paso: Iniciar la transaccion.
        AdaptadorSQLBD adaptador = new AdaptadorSQLBD(params);
        Connection connection = null;
        PreparedStatement ps;

        try {
            connection = adaptador.getConnection();
            adaptador.inicioTransaccion(connection);

            // Tercer paso: Eliminar el documento de la BBDD de Agora.
            String sql = "DELETE FROM G_CRD " +
                    "WHERE CRD_MUN = ? " +
                    "AND CRD_PRO = ? " +
                    "AND CRD_EJE = ? " +
                    "AND CRD_NUM = ? " +
                    "AND CRD_TRA = ? " +
                    "AND CRD_OCU = ? " +
                    "AND CRD_NUD = ?";
            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, tEVO.getCodMunicipio());
            ps.setString(i++, tEVO.getCodProcedimiento());
            ps.setString(i++, tEVO.getEjercicio());
            ps.setString(i++, tEVO.getNumeroRelacion());
            ps.setString(i++, tEVO.getCodTramite());
            ps.setString(i++, tEVO.getOcurrenciaTramite());
            ps.setString(i, tEVO.getCodDocumento());
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            resBorrado = ps.executeUpdate();
            if (resBorrado < 0) throw new WSException("Error.SWFirmaDocManager.eliminardocumentoRelacionCRD:");

            // Cuarto Paso: Eliminar el documento en firmadoc.
            SWFirmaDocService.getInstance().borrarDocumento(codDocFirmaDoc);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WSException("Error.SWFirmaDocManager.eliminardocumentoRelacionCRD:" + e.getMessage());
        } catch (BDException bde) {
            bde.printStackTrace();
            throw new WSException("Error.SWFirmaDocManager.eliminardocumentoRelacionCRD:" + bde.getMessage());
        } catch (WSException wse) {
            wse.printStackTrace();
            resBorrado = -1;
        } finally{
            try{
                if (resBorrado >= 0) adaptador.finTransaccion(connection);
                else adaptador.rollBack(connection);
                adaptador.devolverConexion(connection);

            } catch(BDException bde) {
                bde.printStackTrace();
                m_Log.error("Error.SWFirmaDocManager.eliminardocumentoRelacionCRD:"+ bde.getMensaje());
                resBorrado = -1;
            }
        }

        if (resBorrado < 0) {
            throw new WSException("Error.TramitacionExpedientesDAO.grabarTamite.Fin transaccion y devolver conexion");
        }

        return resBorrado;
    }

}
