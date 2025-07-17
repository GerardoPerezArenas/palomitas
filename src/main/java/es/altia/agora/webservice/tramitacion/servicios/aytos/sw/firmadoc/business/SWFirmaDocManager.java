package es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.business;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.webservice.tramitacion.servicios.WSException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.axis.encoding.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Vector;


public class SWFirmaDocManager {

    private String[] params;
    protected static Config m_ConfigTechnical;
    private static Log m_Log = LogFactory.getLog(SWFirmaDocService.class.getName());

    protected static String crd_mun;
    protected static String crd_pro;
    protected static String crd_eje;
    protected static String crd_num;
    protected static String crd_tra;
    protected static String crd_ocu;
    protected static String crd_nud;
    protected static String crd_fal;
    protected static String crd_fmo;
    protected static String crd_usc;
    protected static String crd_usm;
    protected static String crd_fil;
    protected static String crd_des;
    protected static String crd_dot;
    protected static String crd_fir_est;
    protected static String crd_exp_fd;
    protected static String crd_doc_fd;
    protected static String crd_fir_fd;

    protected static String usu_cod;
    protected static String usu_nom;

    protected static String dot_mun;
    protected static String dot_pro;
    protected static String dot_tra;
    protected static String dot_cod;
    protected static String dot_plt;
    protected static String dot_frm;

    protected static String plt_cod;
    protected static String plt_int;

    protected static String exp_asu;
    protected static String exp_pro;
    protected static String exp_num;
    protected static String exp_mun;
    protected static String exp_eje;

    protected static String pml_valor;
    protected static String pml_cod;
    protected static String pml_mun;

    private static SWFirmaDocManager instance = null;

    // Constructor.
    protected SWFirmaDocManager(String[] params) {
        this.params = params;

        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");

        crd_mun = m_ConfigTechnical.getString("SQL.E_CRD.codMunicipio");
        crd_pro = m_ConfigTechnical.getString("SQL.E_CRD.codProcedimiento");
        crd_eje = m_ConfigTechnical.getString("SQL.E_CRD.ejercicio");
        crd_num = m_ConfigTechnical.getString("SQL.E_CRD.numeroExpediente");
        crd_tra = m_ConfigTechnical.getString("SQL.E_CRD.codTramite");
        crd_ocu = m_ConfigTechnical.getString("SQL.E_CRD.ocurrencia");
        crd_nud = m_ConfigTechnical.getString("SQL.E_CRD.numeroDocumento");
        crd_fal = m_ConfigTechnical.getString("SQL.E_CRD.fechaAlta");
        crd_fmo = m_ConfigTechnical.getString("SQL.E_CRD.fechaModificacion");
        crd_usc = m_ConfigTechnical.getString("SQL.E_CRD.codUsuarioCreac");
        crd_usm = m_ConfigTechnical.getString("SQL.E_CRD.codUsuarioModif");
        crd_fil = m_ConfigTechnical.getString("SQL.E_CRD.fichero");
        crd_des = m_ConfigTechnical.getString("SQL.E_CRD.descripcion");
        crd_dot = m_ConfigTechnical.getString("SQL.E_CRD.codDocumento");
        crd_fir_est = m_ConfigTechnical.getString("SQL.E_CRD.estadoFirma");
        crd_exp_fd = m_ConfigTechnical.getString("SQL.E_CRD.expedienteFirmaDoc");
        crd_doc_fd = m_ConfigTechnical.getString("SQL.E_CRD.documentoFirmaDoc");
        crd_fir_fd = m_ConfigTechnical.getString("SQL.E_CRD.firmaFirmaDoc");
        usu_cod = m_ConfigTechnical.getString("SQL.A_USU.codigo");
        usu_nom = m_ConfigTechnical.getString("SQL.A_USU.nombre");
        dot_mun = m_ConfigTechnical.getString("SQL.E_DOT.codMunicipio");
        dot_pro = m_ConfigTechnical.getString("SQL.E_DOT.codProcedimiento");
        dot_tra = m_ConfigTechnical.getString("SQL.E_DOT.codTramite");
        dot_cod = m_ConfigTechnical.getString("SQL.E_DOT.codDocumento");
        dot_plt = m_ConfigTechnical.getString("SQL.E_DOT.codPlantilla");
        dot_frm = m_ConfigTechnical.getString("SQL.E_DOT.firma");
        plt_cod = m_ConfigTechnical.getString("SQL.E_PLT.codPlantilla");
        plt_int = m_ConfigTechnical.getString("SQL.A_PLT.interesado");
        exp_mun = m_ConfigTechnical.getString("SQL.E_EXP.codMunicipio");
        exp_eje = m_ConfigTechnical.getString("SQL.E_EXP.ano");
        exp_num = m_ConfigTechnical.getString("SQL.E_EXP.numero");
        exp_pro = m_ConfigTechnical.getString("SQL.E_EXP.codProcedimiento");
        exp_asu = m_ConfigTechnical.getString("SQL.E_EXP.asunto");
        pml_cod = m_ConfigTechnical.getString("SQL.E_PML.codProcedimiento");
        pml_valor = m_ConfigTechnical.getString("SQL.E_PML.valor");
        pml_mun = m_ConfigTechnical.getString("SQL.E_PML.codMunicipio");

    }

    public static SWFirmaDocManager getInstance(String[] params) {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(SWFirmaDocManager.class) {
                if (instance == null)
                    instance = new SWFirmaDocManager(params);
            }
        }
        return instance;
    }


    // Obtener Documento.
    public byte[] obtenerDocumento(String codigoDocumento) throws WSException {

        String contenidoDoc = null;

        // Comprobamos que el dato de entrada no es nulo.
        if ((codigoDocumento != null) && (!codigoDocumento.equals(""))) {
            contenidoDoc = SWFirmaDocService.getInstance().extraerFichero(codigoDocumento);
        }

        return Base64.decode(contenidoDoc);

    }


    // Grabar un documento, almacenando su contenido en el Servicio Web de Firmadoc.
    public int grabarDocumento(GeneralValueObject gvo)
    throws WSException, TechnicalException {

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
                resultado = modificarDocumento(connection, gvo);
            } else {
                resultado = insertarNuevoDocumento(connection, gvo);
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



    public byte[] loadDocumento(GeneralValueObject gvo) throws WSException {

        // Primer paso, obtener el codigo de documento en firmadoc.
        m_Log.debug("SWFirmaDocManager.loadDocumento: Obtenemos el numero de documento en SGE");
        String codDocFirmaDoc = obtenerCodDocFirmaDoc(gvo);

        // Segundo paso, extraer el fichero proveniente de firmadoc.
        String docExtraidoBase64 = SWFirmaDocService.getInstance().extraerFichero(codDocFirmaDoc);
        return Base64.decode(docExtraidoBase64);

    }

    private int modificarDocumento(Connection connection, GeneralValueObject gvo)
    throws WSException {

        PreparedStatement ps;
        ResultSet rs;
        int resultado;
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        try {

            // Primer paso: Recupero el codigo de documento y el codigo de expediente de firmadoc.
            String sqlQuery = "SELECT " + crd_exp_fd + ", " + crd_doc_fd + " " +
                    "FROM E_CRD "+
                    "WHERE " + crd_pro + " = ? " +
                    "AND " + crd_eje + " = ? " +
                    "AND " + crd_num + " = ? " +
                    "AND " + crd_tra + " = ? " +
                    "AND " + crd_ocu + " = ? " +
                    "AND " + crd_nud + " = ? " +
                    "AND " + crd_exp_fd + " IS NOT NULL " +
                    "AND " + crd_doc_fd + " IS NOT NULL";
            ps = connection.prepareStatement(sqlQuery);
            int i = 1;
            ps.setString(i++, (String)gvo.getAtributo("codProcedimiento"));
            ps.setString(i++, (String)gvo.getAtributo("ejercicio"));
            ps.setString(i++, (String)gvo.getAtributo("numeroExpediente"));
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
            sqlQuery = "UPDATE E_CRD "+
                    "SET " + crd_fmo + " = " + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", " + crd_usm + " = ?, " + crd_des + " = ?, " + crd_doc_fd + " = ? " +
                    "WHERE " + crd_pro + " = ? " +
                    "AND " + crd_eje + " = ? " +
                    "AND " + crd_num + " = ? " +
                    "AND " + crd_tra + " = ? " +
                    "AND " + crd_ocu + " = ? " +
                    "AND " + crd_nud + " = ? " +
                    "AND " + crd_exp_fd + " IS NOT NULL";
            ps = connection.prepareStatement(sqlQuery);
            i = 1;
            ps.setString(i++, (String)gvo.getAtributo("codUsuario"));
            ps.setString(i++, nombreDocumento);
            ps.setString(i++, nuevoCodDocFirmaDoc);
            ps.setString(i++, (String)gvo.getAtributo("codProcedimiento"));
            ps.setString(i++, (String)gvo.getAtributo("ejercicio"));
            ps.setString(i++, (String)gvo.getAtributo("numeroExpediente"));
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



    private int insertarNuevoDocumento(Connection connection, GeneralValueObject gvo)
    throws WSException, TechnicalException {

        PreparedStatement ps;
        ResultSet rs;
        int resultado;
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        try {
            m_Log.debug("##### - Comprobar si es el primer documento asociado al expediente - #####");
            // Primer paso: Comprobar si es el primer documento asociado a este expediente.
            String sqlQuery = "SELECT " + crd_exp_fd +  " FROM E_CRD " +
                    "WHERE " + crd_mun + " = ? " +
                    "AND " + crd_pro + " = ? " +
                    "AND " + crd_eje + " = ? " +
                    "AND " + crd_num + " = ? " +
                    "AND " + crd_doc_fd + " IS NOT NULL " +
                    "AND " + crd_exp_fd + " IS NOT NULL";
            ps = connection.prepareStatement(sqlQuery);
            int i = 1;
            ps.setString(i++, (String)gvo.getAtributo("codMunicipio"));
            ps.setString(i++, (String)gvo.getAtributo("codProcedimiento"));
            ps.setString(i++, (String)gvo.getAtributo("ejercicio"));
            ps.setString(i, (String)gvo.getAtributo("numeroExpediente"));
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
            sqlQuery = "SELECT " + exp_asu + ", " + pml_valor + " " +
                    "FROM E_EXP JOIN E_PML ON (" + pml_cod + " = " + exp_pro + " AND " + pml_mun + " = " + exp_mun + ") " +
                    "WHERE " + exp_pro + " = ? " +
                    "AND " + exp_eje + " = ? " +
                    "AND " + exp_num + " = ? " +
                    "AND " + exp_mun + " = ? ";
            ps = connection.prepareStatement(sqlQuery);
            i = 1;
            ps.setString(i++, (String)gvo.getAtributo("codProcedimiento"));
            ps.setString(i++, (String)gvo.getAtributo("ejercicio"));
            ps.setString(i++, (String)gvo.getAtributo("numeroExpediente"));
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
            String numeroDocumento = obtenerNuevoNumeroDocumento(connection, gvo);

            m_Log.debug("##### - Guardar informacion de documento y expediente en SGE - #####");
            // Sexto Paso: Almacenar la informacion relevante en la BBDD de agora.
            /*
            String sql = "INSERT INTO E_CRD(" + crd_mun + ", " + crd_pro + ", " + crd_eje +", " + crd_num + ", " +
                    crd_tra + ", " + crd_ocu + ", " + crd_nud + ", " + crd_fal + ", " + crd_fmo + ", " + crd_usc + ", " +
                    crd_usm + ", " + crd_fil + ", " + crd_des + ", " + crd_dot + ", " + crd_fir_est + ", " +
                    crd_exp_fd + ", " + crd_doc_fd + ", " + crd_fir_fd + ")" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, " + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", " + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", ?, ?, NULL, ?, ?, NULL, ?, ?, NULL)";
             */
            String sql = "INSERT INTO E_CRD(" + crd_mun + ", " + crd_pro + ", " + crd_eje +", " + crd_num + ", " +
                    crd_tra + ", " + crd_ocu + ", " + crd_nud + ", " + crd_fal + ", " + crd_fmo + ", " + "CRD_FINF," + crd_usc + ", " +
                    crd_usm + ", " + crd_fil + ", " + crd_des + ", " + crd_dot + ", " + crd_fir_est + ", " +
                    crd_exp_fd + ", " + crd_doc_fd + ", " + crd_fir_fd + ")" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, " + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", " + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", ?, ?, NULL, ?, ?, NULL, ?, ?, NULL)";
            ps = connection.prepareStatement(sql);
            i = 1;
            ps.setString(i++, (String)gvo.getAtributo("codMunicipio"));
            ps.setString(i++, (String)gvo.getAtributo("codProcedimiento"));
            ps.setString(i++, (String)gvo.getAtributo("ejercicio"));
            ps.setString(i++, (String)gvo.getAtributo("numeroExpediente"));
            ps.setString(i++, (String)gvo.getAtributo("codTramite"));
            ps.setString(i++, (String)gvo.getAtributo("ocurrenciaTramite"));
            ps.setString(i++, numeroDocumento);
            ps.setString(i++, (String)gvo.getAtributo("codUsuario"));
            ps.setString(i++, (String)gvo.getAtributo("codUsuario"));
            ps.setString(i++, (String)gvo.getAtributo("nombreDocumento"));
            ps.setString(i++, (String)gvo.getAtributo("codDocumento"));
            ps.setString(i++, codExpFirmaDoc);
            ps.setString(i, codDocFirmaDoc);

            resultado = ps.executeUpdate();
            ps.close();


        } catch (SQLException e) {
            e.printStackTrace();
            throw new TechnicalException("ERROR en el Servicio Web al insertar en Agora", e);
        }

        return resultado;
    }

    private String obtenerNuevoNumeroDocumento(Connection connection, GeneralValueObject gvo) {

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
                            new String[]{crd_nud}) + "+1", "1"});
            sql += " FROM E_CRD " +
                    "WHERE " + crd_pro + " = ? " +
                    "AND " + crd_eje + " = ? " +
                    "AND " + crd_num + " = ? " +
                    "AND " + crd_tra + " = ? " +
                    "AND " + crd_ocu + " = ?";

            // Rellenar la query con los datos de entrada.
            stmt = connection.prepareStatement(sql);
            int i = 1;
            stmt.setString(i++, (String)gvo.getAtributo("codProcedimiento"));
            stmt.setString(i++, (String)gvo.getAtributo("ejercicio"));
            stmt.setString(i++, (String)gvo.getAtributo("numeroExpediente"));
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

    private String obtenerCodDocFirmaDoc(GeneralValueObject gvo) {

        String codDocFirmaDoc = null;
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            conexion = abd.getConnection();

            String sql;
                    
            if ("true".equals((String)gvo.getAtributo("expHistorico")))
                sql = "SELECT CRD_DOC_FD " +
                    "FROM HIST_E_CRD " +
                    "WHERE CRD_MUN = ? " +
                    "AND CRD_NUM = ? " +
                    "AND CRD_TRA = ? " +
                    "AND CRD_OCU = ? " +
                    "AND CRD_NUD = ? " +
                    "AND CRD_DOC_FD IS NOT NULL " +
                    "AND CRD_EXP_FD IS NOT NULL";
            else
                sql = "SELECT CRD_DOC_FD " +
                    "FROM E_CRD " +
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
            ps.setString(i++, AdaptadorSQLBD.js_unescape((String)gvo.getAtributo("numeroExpediente")));
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

    public void getListaDocumentosCronologia(TramitacionExpedientesValueObject tEVO)
            throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaDocumentosCronologia");

        Connection con = null;
        PreparedStatement ps;
        ResultSet rs;
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Vector listaDocumentos = new Vector();

        try{
            // Obtenemos la conexion.
            con = oad.getConnection();
            // Creamos la consulta SQL.
            String from = crd_nud + ", " + crd_des + ", TO_CHAR(" + crd_fal + ", 'DD/MM/YYYY') AS " + crd_fal + ", " +
                    "TO_CHAR(" + crd_fmo + ", 'DD/MM/YYYY') AS " + crd_fmo + ", " +
                    "TO_CHAR(CRD_FINF, 'DD/MM/YYYY') AS CRD_FINF, " +
                    "usu." + usu_nom + " AS usuarioCreacion, usu1." + usu_nom + " AS usuarioModificacion, " +
                    plt_int + ", " + crd_fir_fd;

            String where = crd_mun + " = " + tEVO.getCodMunicipio() + " AND " +
                    crd_pro + "='" + tEVO.getCodProcedimiento() + "' AND " +
                    crd_tra + "=" + tEVO.getCodTramite() + " AND " +
                    crd_num + "='" + tEVO.getNumeroExpediente() + "' AND " +
                    crd_ocu + " = " + tEVO.getOcurrenciaTramite() + " AND " +
                    crd_exp_fd + " IS NOT NULL AND " +
                    crd_doc_fd + " IS NOT NULL ";
            String[] join1 = new String[14];
            join1[0] = "E_CRD";
            join1[1] = "INNER";
            join1[2] = GlobalNames.ESQUEMA_GENERICO + "a_usu usu";
            join1[3] = "e_crd." + crd_usc + " = usu." + usu_cod;
            join1[4] = "LEFT";
            join1[5] = GlobalNames.ESQUEMA_GENERICO + "a_usu usu1";
            join1[6] = "e_crd." + crd_usm + " = usu1." + usu_cod;
            join1[7] = "INNER";
            join1[8] = "e_dot";
            join1[9] = "e_crd." + crd_pro + " = e_dot." + dot_pro + " AND " +
                    "e_crd." + crd_tra + " = e_dot." + dot_tra + " AND " +
                    "e_crd." + crd_dot + " = e_dot." + dot_cod;
            join1[10] = "INNER";
            join1[11] = "a_plt";
            join1[12] = "e_dot." + dot_plt + " = a_plt." + plt_cod;
            join1[13] = "false";

            String sql = oad.join(from, where, join1);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = con.prepareStatement(sql);

            // Ejecutamos la consulta
            rs = ps.executeQuery();
            String entrar = "no";
            while ( rs.next() ) {
                entrar = "si";
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                String codDocumento = rs.getString(crd_nud);
                tramExpVO.setCodDocumento(codDocumento);
                String descripcion = rs.getString(crd_des);
                tramExpVO.setDescDocumento(descripcion);
                String fechaCreacion = rs.getString(crd_fal);
                tramExpVO.setFechaCreacion(fechaCreacion);
                String fechaModificacion = rs.getString(crd_fmo);
                if(fechaModificacion != null) {
                    if(!fechaModificacion.equals("")) {
                        tramExpVO.setFechaModificacion(fechaModificacion);
                    } else tramExpVO.setFechaModificacion("");
                } else tramExpVO.setFechaModificacion("");

                String fechaInforme = rs.getString("CRD_FINF");
                if(fechaInforme!=null)
                    tramExpVO.setFechaInforme(fechaInforme);
                else
                    tramExpVO.setFechaInforme(" ");
                String usuarioCreacion = rs.getString("usuarioCreacion");
                String usuarioModificacion = rs.getString("usuarioModificacion");
                if(usuarioModificacion == null || usuarioModificacion.equals("")) {
                    tramExpVO.setUsuario(usuarioCreacion);
                } else tramExpVO.setUsuario(usuarioModificacion);
                String interesado = rs.getString(plt_int);
                tramExpVO.setInteresado(interesado);
                String estadoFirma = rs.getString(crd_fir_fd);
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
            throw new AnotacionRegistroException("Error. TramitacionExpedientesDAO.getListaDocumentosCronologia", e);
        } finally {
            try {
                oad.devolverConexion(con);
            } catch (BDException be) {
                be.printStackTrace();
            }
        }

        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaDocumentosCronologia");
    }

    public int eliminarDocumentoCRD(TramitacionExpedientesValueObject tEVO) throws WSException {

        int resBorrado = 0;

        // Primer Paso: Obtener el codigo del documento en firmadoc.
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codMunicipio", tEVO.getCodMunicipio());
        gVO.setAtributo("numeroExpediente", tEVO.getNumeroExpediente());
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
            String sql = "DELETE FROM E_CRD " +
                    "WHERE " + crd_mun + " = ? " +
                    "AND " + crd_pro + " = ? " +
                    "AND " + crd_eje + " = ? " +
                    "AND " + crd_num + " = ? " +
                    "AND " + crd_tra + " = ? " +
                    "AND " + crd_ocu + " = ? " +
                    "AND " + crd_nud + " = ?";
            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, tEVO.getCodMunicipio());
            ps.setString(i++, tEVO.getCodProcedimiento());
            ps.setString(i++, tEVO.getEjercicio());
            ps.setString(i++, tEVO.getNumeroExpediente());
            ps.setString(i++, tEVO.getCodTramite());
            ps.setString(i++, tEVO.getOcurrenciaTramite());
            ps.setString(i, tEVO.getCodDocumento());
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            resBorrado = ps.executeUpdate();
            if (resBorrado < 0) throw new WSException("Error.SWFirmaDocManager.eliminardocumentoCRD:");

            // Cuarto Paso: Eliminar el documento en firmadoc.
            SWFirmaDocService.getInstance().borrarDocumento(codDocFirmaDoc);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WSException("Error.SWFirmaDocManager.eliminardocumentoCRD:" + e.getMessage());
        } catch (BDException bde) {
            bde.printStackTrace();
            throw new WSException("Error.SWFirmaDocManager.eliminardocumentoCRD:" + bde.getMessage());
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
                m_Log.error("Error.SWFirmaDocManager.eliminardocumentoCRD:"+ bde.getMensaje());
                resBorrado = -1;
            }
        }

        if (resBorrado < 0) {
            throw new WSException("Error.TramitacionExpedientesDAO.grabarTamite.Fin transaccion y devolver conexion");
        }

        return resBorrado;
    }


}

