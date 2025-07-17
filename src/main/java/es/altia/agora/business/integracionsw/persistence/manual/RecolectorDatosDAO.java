package es.altia.agora.business.integracionsw.persistence.manual;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.jdbc.GeneralOperations;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.common.exception.TechnicalException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;

public class RecolectorDatosDAO {

    protected static Log m_Log = LogFactory.getLog(RecolectorDatosDAO.class.getName());

    private static RecolectorDatosDAO instance = null;

    protected RecolectorDatosDAO() {
    }

    public static RecolectorDatosDAO getInstance() {
        if (instance == null) {
            synchronized (RecolectorDatosDAO.class) {
                if (instance == null) instance = new RecolectorDatosDAO();
            }
        }
        return instance;
    }

    public String getDatoVistaCRO(int ocurrencia, String numExpediente, int codTramite, String codigoCampo, String[] params)
            throws TechnicalException, InternalErrorException {

        String sqlNombreColumna = "SELECT CI.CAMPO AS CAMPO " +
                "FROM CAMPOINFORME CI, CAMPOSELECCIONINFORME CSI, ESTRUCTURAINFORME ESI, A_DOC " +
                "WHERE A_DOC.DOC_APL = 4 AND (A_DOC.DOC_CEI = 1 OR A_DOC.DOC_CEI = 2) " +
                "AND A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND A_DOC.DOC_CEI = CSI.COD_ESTRUCTURA " +
                "AND CSI.COD_CAMPOINFORME = CI.CODIGO AND CI.NOMEAS = ?";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlNombreColumna);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR EL NOMBRE DE LA COLUMNA A CONSULTAR EN" +
                    " LA TABLA V_CRO");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlNombreColumna);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo de Columna: " + codigoCampo);

            ps.setString(1, codigoCampo);

            rs = ps.executeQuery();

            String nombreColumna = "";
            if (rs.next()) nombreColumna = rs.getString(1).substring(6);

            if ("".equals(nombreColumna)) return "";

            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);

            String sqlGetDato = "SELECT " + nombreColumna + " FROM V_CRO WHERE CRO_OCU = ? AND CRO_NUM = ? AND TRA_COD = ?";

            ps = con.prepareStatement(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA V_CRO");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Ocurrencia Tramite: " + ocurrencia);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Numero Expediente: " + numExpediente);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo de Tramite: " + codTramite);

            int i = 1;
            ps.setInt(i++, ocurrencia);
            ps.setString(i++, numExpediente);
            ps.setInt(i, codTramite);

            rs = ps.executeQuery();
            if (rs.next()) return rs.getString(nombreColumna);
            else return "";

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA V_CRO");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA V_CRO", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA V_CRO");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA V_CRO", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public String getDatoVistaINT(int codMunicipio, String numExpediente, String codigoCampo, String[] params)
            throws TechnicalException, InternalErrorException {

        String sqlGetDato = "SELECT  int_rol, int_nom, int_domnn, int_doc, int_pob, int_tlf, int_codexterno " +
                "FROM v_int WHERE INT_MUN = ? AND int_num = ? ORDER BY 1";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA V_INT");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Numero Expediente: " + numExpediente);

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i, numExpediente);

            String valor = "";
            rs = ps.executeQuery();
            if (rs.next()) {
                String nomRol = rs.getString(1);
                if (nomRol.equals(codigoCampo)) valor = rs.getString(2);
                if (codigoCampo.equals("Dom" + nomRol)) valor = rs.getString(3);
                if (codigoCampo.equals("Doc" + nomRol)) valor = rs.getString(4);
                if (codigoCampo.equals("Pob" + nomRol)) valor = rs.getString(5);
                if (codigoCampo.equals("Tlfno" + nomRol)) valor = rs.getString(6);
                if (codigoCampo.equals("codExterno" + nomRol)) valor = rs.getString(7);
            }
            return valor;

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA V_CRO");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA V_INT", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA V_CRO");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA V_INT", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public String getDatoTablaTNU(int codMunicipio, String numExpediente, String codigoCampo, String[] params)
            throws TechnicalException, InternalErrorException {

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlGetDato = "SELECT " +abd.convertir("TNU_VALOR", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null) + " " +
                "AS TNU_VALOR FROM E_TNU WHERE TNU_MUN = ? AND TNU_NUM = ? AND TNU_COD = ?";

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TNU");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Numero Expediente: " + numExpediente);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo Campo: " + codigoCampo);

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, numExpediente);
            ps.setString(i, codigoCampo);

            String valor = "";
            rs = ps.executeQuery();
            if (rs.next()) {
                valor = rs.getString(1);
            }
            return valor;

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TNU");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TNU", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TNU");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TNU", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public String getDatoTablaTXT(int codMunicipio, String numExpediente, String codigoCampo, String[] params)
            throws TechnicalException, InternalErrorException {

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlGetDato = "SELECT " +abd.convertir("TXT_VALOR", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null) + " " +
                "AS TXT_VALOR FROM E_TXT WHERE TXT_MUN = ? AND TXT_NUM = ? AND TXT_COD = ?";

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TXT");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Numero Expediente: " + numExpediente);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo Campo: " + codigoCampo);

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, numExpediente);
            ps.setString(i, codigoCampo);

            String valor = "";
            rs = ps.executeQuery();
            if (rs.next()) {
                valor = rs.getString(1);
            }
            return valor;

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TXT");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TXT", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TXT");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TXT", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public String getDatoTablaTFE(int codMunicipio, String numExpediente, String codigoCampo, String[] params)
            throws TechnicalException, InternalErrorException {

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlGetDato = "SELECT " +abd.convertir("TFE_VALOR", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " " +
                "AS TFE_VALOR FROM E_TFE WHERE TFE_MUN = ? AND TFE_NUM = ? AND TFE_COD = ?";

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TFE");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Numero Expediente: " + numExpediente);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo Campo: " + codigoCampo);

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, numExpediente);
            ps.setString(i, codigoCampo);

            String valor = "";
            rs = ps.executeQuery();
            if (rs.next()) {
                valor = rs.getString(1);
            }
            return valor;

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TFE");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TFE", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TFE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TFE", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public String getDatoTablaTTL(int codMunicipio, String numExpediente, String codigoCampo, String[] params)
            throws TechnicalException, InternalErrorException {

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlGetDato = "SELECT TTL_VALOR FROM E_TTL WHERE TTL_MUN = ? AND TTL_NUM = ? AND TTL_COD = ?";

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TTL");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Numero Expediente: " + numExpediente);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo Campo: " + codigoCampo);

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, numExpediente);
            ps.setString(i, codigoCampo);

            String valor = "";
            rs = ps.executeQuery();
            if (rs.next()) {
                java.io.Reader cr = rs.getCharacterStream(1);
                if (cr != null) {
                    java.io.CharArrayWriter ot = new java.io.CharArrayWriter();
                    int c;
                    while ((c = cr.read()) != -1) {
                        ot.write(c);
                    }
                    ot.flush();
                    valor = ot.toString();
                    ot.close();
                    cr.close();
                }
            }
            return valor;

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TTL");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TTL", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TTL");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TTL", sqle);
        } catch (IOException e) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TTL");
            return "";
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public String getDatoTablaTDE(int codMunicipio, String numExpediente, String codigoCampo, String[] params)
            throws TechnicalException, InternalErrorException {

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlGetDato = "SELECT TDE_VALOR FROM E_TDE WHERE TDE_MUN = ? AND TDE_NUM = ? " +
                "AND TDE_COD = ?";

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TDE");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Numero Expediente: " + numExpediente);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo Campo: " + codigoCampo);

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, numExpediente);
            ps.setString(i, codigoCampo);

            String valor = "";
            rs = ps.executeQuery();
            if (rs.next()) {
                valor = rs.getString(1);
            }
            return valor;

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TDE");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TDE", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TDE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TDE", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public String getDatoTablaTNUT(int codMunicipio, String numExpediente, String codigoCampo, int ocurrencia,String[] params)
            throws TechnicalException, InternalErrorException {

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlGetDato = "SELECT " +abd.convertir("TNUT_VALOR", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null) + " " +
                "AS TNUT_VALOR FROM E_TNUT WHERE TNUT_MUN = ? AND TNUT_NUM = ? AND TNUT_COD = ? AND TNUT_TRA = ? AND TNUT_OCU= ? ";

        String[] datosCampo = codigoCampo.split("_");
        int codTramite = Integer.parseInt(datosCampo[1]);
        String codigo = datosCampo[2];

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TNUT");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Numero Expediente: " + numExpediente);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo Campo: " + codigo);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Codigo Tramite: " + codTramite);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 5: Codigo ocurrencia: " + ocurrencia);

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, numExpediente);
            ps.setString(i++, codigo);
            ps.setInt(i++, codTramite);
            ps.setInt(i, ocurrencia);

            String valor = "";
            rs = ps.executeQuery();
            if (rs.next()) {
                valor = rs.getString(1);
            }
            return valor;

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TNUT");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TNUT", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TNUT");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TNUT", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public String getDatoTablaTXTT(int codMunicipio, String numExpediente, String codigoCampo,int ocurrencia, String[] params)
            throws TechnicalException, InternalErrorException {

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlGetDato = "SELECT " +abd.convertir("TXTT_VALOR", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null) + " " +
                "AS TXTT_VALOR FROM E_TXTT WHERE TXTT_MUN = ? AND TXTT_NUM = ? AND TXTT_COD = ? AND TXTT_TRA = ? AND TXTT_OCU=? ";

        String[] datosCampo = codigoCampo.split("_");
        int codTramite = Integer.parseInt(datosCampo[1]);
        String codigo = datosCampo[2];

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TXTT");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Numero Expediente: " + numExpediente);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo Campo: " + codigo);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Codigo Tramite: " + codTramite);
             if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 5: Codigo ocurrencia: " + ocurrencia);

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, numExpediente);
            ps.setString(i++, codigo);
            ps.setInt(i++, codTramite);
            ps.setInt(i, ocurrencia);

            String valor = "";
            rs = ps.executeQuery();
            if (rs.next()) {
                valor = rs.getString(1);
            }
            return valor;

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TXTT");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TXTT", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TXTT");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TXTT", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public String getDatoTablaTFET(int codMunicipio, String numExpediente, String codigoCampo, int ocurrencia,String[] params)
            throws TechnicalException, InternalErrorException {

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlGetDato = "SELECT " +abd.convertir("TFET_VALOR", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " " +
                "AS TFET_VALOR FROM E_TFET WHERE TFET_MUN = ? AND TFET_NUM = ? AND TFET_COD = ? AND TFET_TRA = ? AND TFET_OCU= ? ";

        String[] datosCampo = codigoCampo.split("_");
        int codTramite = Integer.parseInt(datosCampo[1]);
        String codigo = datosCampo[2];

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TFET");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Numero Expediente: " + numExpediente);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo Campo: " + codigo);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Codigo Tramite: " + codTramite);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 5: Codigo ocurrencia: " + ocurrencia);

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, numExpediente);
            ps.setString(i++, codigo);
            ps.setInt(i++, codTramite);
            ps.setInt(i, ocurrencia);
            

            String valor = "";
            rs = ps.executeQuery();
            if (rs.next()) {
                valor = rs.getString(1);
            }
            return valor;

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TFET");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TFET", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TFET");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TFET", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public String getDatoTablaTTLT(int codMunicipio, String numExpediente, String codigoCampo,int ocurrencia, String[] params)
            throws TechnicalException, InternalErrorException {

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlGetDato = "SELECT TTLT_VALOR FROM E_TTLT WHERE TTLT_MUN = ? AND TTLT_NUM = ? AND TTLT_COD = ? " +
                "AND TTLT_TRA = ? AND TTLT_OCU= ? ";

        String[] datosCampo = codigoCampo.split("_");
        int codTramite = Integer.parseInt(datosCampo[1]);
        String codigo = datosCampo[2];

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TTLT");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Numero Expediente: " + numExpediente);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo Campo: " + codigo);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Codigo Tramite: " + codTramite);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 5: Codigo ocurrencia: " + ocurrencia);

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, numExpediente);
            ps.setString(i++, codigo);
            ps.setInt(i++, codTramite);
            ps.setInt(i, ocurrencia);

            String valor = "";
            rs = ps.executeQuery();
            if (rs.next()) {
                java.io.Reader cr = rs.getCharacterStream(1);
                if (cr != null) {
                    java.io.CharArrayWriter ot = new java.io.CharArrayWriter();
                    int c;
                    while ((c = cr.read()) != -1) {
                        ot.write(c);
                    }
                    ot.flush();
                    valor = ot.toString();
                    ot.close();
                    cr.close();
                }
            }
            return valor;

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TTLT");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TTLT", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TTLT");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TTLT", sqle);
        } catch (IOException e) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TTLT");
            return "";
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
        }
    }

    public String getDatoTablaTDET(int codMunicipio, String numExpediente, String codigoCampo,int ocurrencia, String[] params)
            throws TechnicalException, InternalErrorException {

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlGetDato = "SELECT TDET_VALOR FROM E_TDET WHERE TDET_MUN = ? AND TDET_NUM = ? " +
                "AND TDET_COD = ? AND TDET_TRA = ? AND TDET_OCU=?";



        String[] datosCampo = codigoCampo.split("_");
        int codTramite = Integer.parseInt(datosCampo[1]);
        String codigo = datosCampo[2];

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TDET");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlGetDato);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1: Codigo Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2: Numero Expediente: " + numExpediente);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3: Codigo Campo: " + codigo);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4: Codigo Tramite: " + codTramite);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 5: Codigo ocurrencia: " + ocurrencia);

            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, numExpediente);
            ps.setString(i++, codigo);
            ps.setInt(i++, codTramite);
            ps.setInt(i, ocurrencia);

            String valor = "";
            rs = ps.executeQuery();
            if (rs.next()) {
                valor = rs.getString(1);
            }
            return valor;

        } catch (BDException bde) {
            m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TDET");
            throw new TechnicalException("ERROR AL INTENTAR OBTENER LA CONEXION PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TDET", bde);
        } catch (SQLException sqle) {
            m_Log.error("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TDET");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA RECUPERAR EL DATO NECESARIO DE LA TABLA E_TDET", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(ps);
            devolverConexion(abd, con);
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


}
