package es.altia.agora.business.gestionInformes.particular.impl;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.gestionInformes.particular.CodigoEtiqueta;
import es.altia.agora.business.gestionInformes.particular.InformeParticularFacade;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.interfaces.user.web.gestionInformes.InformeParticularAction;
import es.altia.agora.technical.Fecha;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;


import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class InformeAgenciaAntidroga implements InformeParticularFacade {

    protected static Log m_Log = LogFactory.getLog(InformeAgenciaAntidroga.class.getName());
    protected static Config m_Conf = ConfigServiceHelper.getConfig("common");

    private String[] paramsBD;

    private static final ArrayList<CodigoEtiqueta> meses;
    private static final String codProcedimiento = "CAID";

    static {
        meses = new ArrayList<CodigoEtiqueta>();
        meses.add(new CodigoEtiqueta("01", "ENERO"));
        meses.add(new CodigoEtiqueta("02", "FEBRERO"));
        meses.add(new CodigoEtiqueta("03", "MARZO"));
        meses.add(new CodigoEtiqueta("04", "ABRIL"));
        meses.add(new CodigoEtiqueta("05", "MAYO"));
        meses.add(new CodigoEtiqueta("06", "JUNIO"));
        meses.add(new CodigoEtiqueta("07", "JULIO"));
        meses.add(new CodigoEtiqueta("08", "AGOSTO"));
        meses.add(new CodigoEtiqueta("09", "SEPTIEMBRE"));
        meses.add(new CodigoEtiqueta("10", "OCTUBRE"));
        meses.add(new CodigoEtiqueta("11", "NOVIEMBRE"));
        meses.add(new CodigoEtiqueta("12", "DICIEMBRE"));
    }

    public ArrayList<CodigoEtiqueta> getCodigosEtiquetasSelect(String idCampo, UsuarioValueObject usuario) throws TechnicalException {

        if (idCampo.equals("mesInforme")) {
            return meses;
        } else if (idCampo.equals("usuarioInforme")) {
            return recuperaUsuariosProcCaid(usuario.getOrgCod());
        } else if (idCampo.equals("anhoInforme")) {
            return recuperaAnhosProcCaid();
        } else {
            return new ArrayList<CodigoEtiqueta>();
        }
    }

    public GeneralValueObject recuperarDatosRequest(HttpServletRequest request) {

        GeneralValueObject datosRequest = new GeneralValueObject();
        datosRequest.setAtributo("mesInforme", request.getParameter("mesInforme"));
        datosRequest.setAtributo("usuarioInforme", request.getParameter("usuarioInforme"));
        datosRequest.setAtributo("anhoInforme", request.getParameter("anhoInforme"));

        return datosRequest;
    }

    public void setParamsBD(String[] params) {
        this.paramsBD = params;
    }

    private ArrayList<CodigoEtiqueta> recuperaUsuariosProcCaid(int codOrganizacion) throws TechnicalException {

        String sqlQuery = "SELECT DISTINCT USU_COD, USU_NOM " +
                "FROM E_TRA " +
                "JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON (TRA_UIN = UOU_UOR) " +
                "JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_USU ON (UOU_USU = USU_COD) " +
                "WHERE TRA_PRO = ? AND UOU_ORG = ?";

        m_Log.debug("recuperaUsuariosProcCaid=" + sqlQuery);
        m_Log.debug ("Parametros=" + codProcedimiento + "," + codOrganizacion);
        AdaptadorSQLBD abd = new AdaptadorSQLBD(paramsBD);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlQuery);
            ps.setString(1, codProcedimiento);
            ps.setInt(2, codOrganizacion);

            rs = ps.executeQuery();

            ArrayList<CodigoEtiqueta> codsEtiquetas = new ArrayList<CodigoEtiqueta>();
            while (rs.next()) {
                String codigo = rs.getString(1);
                String etiqueta = rs.getString(2);
                codsEtiquetas.add(new CodigoEtiqueta(codigo, etiqueta));
            }
            codsEtiquetas.add(new CodigoEtiqueta("TODOS", "TODOS"));

            return codsEtiquetas;

        } catch (BDException bde) {
            throw new TechnicalException(bde.getMensaje(), bde);
        } catch (SQLException sqle) {
            throw new TechnicalException(sqle.getMessage(), sqle);
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            devolverConexion(abd, con);

        }

    }

    private ArrayList<CodigoEtiqueta> recuperaAnhosProcCaid() throws TechnicalException {

        ResourceBundle propsAgenciaAntiDroga = ResourceBundle.getBundle("es.altia.agora.business.gestionInformes.particular.impl.InformeAgenciaAntidroga");
        String codProcedimiento = propsAgenciaAntiDroga.getString("AgenciaAntidroga/codigoProcedimiento");
        String codsTramitesCitas = propsAgenciaAntiDroga.getString("AgenciaAntidroga/tramitesCitas");

        String sqlQuery = "SELECT DISTINCT TO_CHAR(TFET_VALOR, 'YYYY'), TO_CHAR(TFET_VALOR, 'YYYY') " +
                "FROM E_TCA JOIN E_TFET ON (TCA_COD = TFET_COD) " +
                "WHERE TCA_PRO = ? " +
                "AND TCA_TRA IN (SELECT TRA_COD FROM E_TRA WHERE TRA_PRO = ? AND TRA_COU IN (" + codsTramitesCitas + ")) " +
                "AND TCA_TDA = 3 " +
                "UNION " +
                "SELECT DISTINCT TO_CHAR(CRO_FEI, 'YYYY'), TO_CHAR(CRO_FEI, 'YYYY') " +
                "FROM E_CRO " +
                "WHERE CRO_PRO = ?";
        
        m_Log.debug("recuperaAnhosProcCaid="+sqlQuery);
        m_Log.debug("Parametro=" + codProcedimiento);

        AdaptadorSQLBD abd = new AdaptadorSQLBD(paramsBD);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlQuery);
            ps.setString(1, codProcedimiento);
            ps.setString(2, codProcedimiento);
            ps.setString(3, codProcedimiento);

            rs = ps.executeQuery();

            ArrayList<CodigoEtiqueta> codsEtiquetas = new ArrayList<CodigoEtiqueta>();
            while (rs.next()) {
                String codigo = rs.getString(1);
                String etiqueta = rs.getString(2);
                codsEtiquetas.add(new CodigoEtiqueta(codigo, etiqueta));
            }

            return codsEtiquetas;

        } catch (BDException bde) {
            throw new TechnicalException(bde.getMensaje(), bde);
        } catch (SQLException sqle) {
            throw new TechnicalException(sqle.getMessage(), sqle);
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            devolverConexion(abd, con);

        }

    }

    private void devolverConexion(AdaptadorSQLBD bd, Connection con) throws TechnicalException {
        try {
            if (con != null) bd.devolverConexion(con);
        } catch (BDException bde) {
            throw new TechnicalException(bde.getMensaje());
        }
    }

    private void closeStatement(Statement stmt) throws TechnicalException {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException sqle) {
            throw new TechnicalException(sqle.getMessage(), sqle);
        }
    }

    private void closeResultSet(ResultSet rs) throws TechnicalException {
        try {
            if (rs != null) rs.close();
        } catch (SQLException sqle) {
            throw new TechnicalException(sqle.getMessage(), sqle);
        }
    }

    public Collection recuperaDatosInforme(GeneralValueObject datosEntrada, UsuarioValueObject usuario) throws TechnicalException {

        ResourceBundle propsAgenciaAntiDroga = ResourceBundle.getBundle("es.altia.agora.business.gestionInformes.particular.impl.InformeAgenciaAntidroga");
        String codProcedimiento = propsAgenciaAntiDroga.getString("AgenciaAntidroga/codigoProcedimiento");
        String codsTramitesCitas = propsAgenciaAntiDroga.getString("AgenciaAntidroga/tramitesCitas");
        String codCampoFechaCita = propsAgenciaAntiDroga.getString("AgenciaAntidroga/codFechaCita");
        String codCampoTipoIntervecion = propsAgenciaAntiDroga.getString("AgenciaAntidroga/codTipoIntervencion");
        String codCampoAcudeCita = propsAgenciaAntiDroga.getString("AgenciaAntidroga/codAcudeCita");
        String codCampoCitaPrevia = propsAgenciaAntiDroga.getString("AgenciaAntidroga/codCitaPrevia");
        String codCampoRAD = propsAgenciaAntiDroga.getString("AgenciaAntidroga/codRAD");
        int codOrganizacion = (Integer)datosEntrada.getAtributo("codOrganizacion");
        
        String mesInforme = (String) datosEntrada.getAtributo("mesInforme");
        String anhoInforme = (String) datosEntrada.getAtributo("anhoInforme");
        String usuarioInforme = (String)datosEntrada.getAtributo("usuarioInforme");

        String primerDiaMes = "01/" + mesInforme + "/" + anhoInforme;
        int intMesInforme = Integer.parseInt(mesInforme);
        int intAnhoInforme = Integer.parseInt(anhoInforme);
        String ultimoDiaMes = Fecha.numDiasMes(intMesInforme, intAnhoInforme) + "/" + mesInforme + "/" + anhoInforme;

        AdaptadorSQLBD abd = new AdaptadorSQLBD(paramsBD);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlQuery = "SELECT A_USU.USU_NOM NOMBREUSUARIO, TXT_VALOR CODIGORAD, TFET_VALOR FECHACITA, " +
                "TIPOINT.TDET_VALOR CODINTERVENCION, ACUDECITA.TDET_VALOR ACUDECITA, CITAPREVIA.TDET_VALOR CITAPREVIA " +
                "FROM (SELECT DISTINCT USU_COD, USU_NOM FROM E_TRA JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON (TRA_UIN = UOU_UOR) JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_USU ON (UOU_USU = USU_COD) WHERE TRA_PRO = ? AND UOU_ORG = ?";
        if (usuarioInforme != null && !"TODOS".equals(usuarioInforme)) {
            sqlQuery += " AND USU_COD = ? ";
        }
        sqlQuery += ") A_USU " +
                "JOIN E_EXP ON (EXP_PRO = ? AND EXP_MUN = ? AND EXP_FEI <= " + abd.convertir("?", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + " AND (EXP_FEF >= " + abd.convertir("?", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + " OR EXP_FEF IS NULL)) " +
                "JOIN E_PCA ON (EXP_PRO = PCA_PRO AND PCA_COD = ?) " +
                "LEFT JOIN E_TXT ON (PCA_COD = TXT_COD AND EXP_NUM = TXT_NUM) " +
                "LEFT JOIN E_CRO ON (EXP_NUM = CRO_NUM AND CRO_TRA IN (SELECT TRA_COD FROM E_TRA WHERE TRA_PRO = ? AND TRA_COU IN (" + codsTramitesCitas + ")) AND CRO_USF = USU_COD) " +
                "LEFT JOIN E_TFET ON (CRO_NUM = TFET_NUM AND CRO_TRA = TFET_TRA AND CRO_OCU = TFET_OCU AND TFET_COD LIKE ?) AND TO_CHAR(TFET_VALOR, 'MM') = ? AND TO_CHAR(TFET_VALOR, 'YYYY') = ? " +
                "LEFT JOIN E_TDET TIPOINT ON (CRO_NUM = TIPOINT.TDET_NUM AND CRO_TRA = TIPOINT.TDET_TRA AND CRO_OCU = TIPOINT.TDET_OCU AND (TIPOINT.TDET_COD = ?)) " +
                "LEFT JOIN E_TDET ACUDECITA ON (CRO_NUM = ACUDECITA.TDET_NUM AND CRO_TRA = ACUDECITA.TDET_TRA AND CRO_OCU = ACUDECITA.TDET_OCU AND (ACUDECITA.TDET_COD = ?)) " +
                "LEFT JOIN E_TDET CITAPREVIA ON (CRO_NUM = CITAPREVIA.TDET_NUM AND CRO_TRA = CITAPREVIA.TDET_TRA AND CRO_OCU = CITAPREVIA.TDET_OCU AND (CITAPREVIA.TDET_COD = ?)) " +
                "WHERE TXT_VALOR IS NOT NULL " +
                "ORDER BY NOMBREUSUARIO, CODIGORAD";

        m_Log.debug(sqlQuery);

        try {
            con = abd.getConnection();

            ps = con.prepareStatement(sqlQuery);
            m_Log.debug("####### - CONSULTA PARA OBTENER LOS DATOS DEL INFORME PARA LA AGENCIA ANTIODROGA");
            m_Log.debug(codProcedimiento);
            m_Log.debug(codOrganizacion);
            if (usuarioInforme != null && !"TODOS".equals(usuarioInforme)) m_Log.debug(Integer.parseInt(usuarioInforme));
            m_Log.debug(codProcedimiento);
            m_Log.debug(codOrganizacion);
            m_Log.debug(ultimoDiaMes);
            m_Log.debug(primerDiaMes);
            m_Log.debug(codCampoRAD);
            m_Log.debug(codProcedimiento);
            m_Log.debug(codCampoFechaCita + "%");
            m_Log.debug(mesInforme);
            m_Log.debug(anhoInforme);
            m_Log.debug(codCampoTipoIntervecion);
            m_Log.debug(codCampoAcudeCita);
            m_Log.debug(codCampoCitaPrevia);

            int i = 1;
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, codOrganizacion);
            if (usuarioInforme != null && !"TODOS".equals(usuarioInforme)) ps.setInt(i++, Integer.parseInt(usuarioInforme));
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, codOrganizacion);
            ps.setString(i++, ultimoDiaMes);
            ps.setString(i++, primerDiaMes);
            ps.setString(i++, codCampoRAD);
            ps.setString(i++, codProcedimiento);
            ps.setString(i++, codCampoFechaCita + "%");
            ps.setString(i++, mesInforme);
            ps.setString(i++, anhoInforme);
            ps.setString(i++, codCampoTipoIntervecion);
            ps.setString(i++, codCampoAcudeCita);
            ps.setString(i, codCampoCitaPrevia);

            rs = ps.executeQuery();

            Collection<DatoAgenciaAntidroga> listaDatos = new ArrayList<DatoAgenciaAntidroga>();
            while (rs.next()) {
                DatoAgenciaAntidroga dato = new DatoAgenciaAntidroga();
                i = 1;
                dato.setNombreUsuario(rs.getString(i++));
                dato.setCodigoRAD(rs.getString(i++));
                Calendar fechaCita = Calendar.getInstance();
                Date dateCita = rs.getTimestamp(i++);
                if (dateCita != null) fechaCita.setTime(dateCita);
                else fechaCita = null;
                dato.setFechaCita(fechaCita);
                if (fechaCita != null) {
                    String strTipoIntervencion = rs.getString(i++);
                    dato.setTipoIntervencion(Integer.parseInt(strTipoIntervencion));
                    String strAcudeCita = rs.getString(i++);
                    dato.setAcudeCita(strAcudeCita.equals("S"));
                    String strCitaPrevia = rs.getString(i);
                    dato.setCitaPrevia(strCitaPrevia.equals("S"));
                }

                listaDatos.add(dato);
                m_Log.debug(dato);
            }



            return listaDatos;

        } catch (BDException bde) {
            throw new TechnicalException(bde.getMensaje(), bde);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new TechnicalException(sqle.getMessage(), sqle);
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            devolverConexion(abd, con);

        }
    }

    public String generarInforme(GeneralValueObject datosEntrada, Collection datosInforme,
    		UsuarioValueObject usuVO, String url, String servlet) throws TechnicalException {

        String strMesInforme = (String) datosEntrada.getAtributo("mesInforme");
        int mesInforme = Integer.parseInt(strMesInforme);

        String strAñoInforme = (String) datosEntrada.getAtributo("anhoInforme");
        int añoInforme = Integer.parseInt(strAñoInforme);

        int numDiasMes = Fecha.numDiasMes(mesInforme, añoInforme);

        int codOrganizacion = (Integer)datosEntrada.getAtributo("codOrganizacion");

        // Formateamos los datos del informe.
        HashMap<String, Vector<Vector<String>>> informePorUsuario = new HashMap<String, Vector<Vector<String>>>();

        for (Object objDato : datosInforme) {
            DatoAgenciaAntidroga dato = (DatoAgenciaAntidroga) objDato;
            Vector<Vector<String>> filasInforme = informePorUsuario.get(dato.getNombreUsuario());
            if (filasInforme == null) {
                filasInforme = new Vector<Vector<String>>();
                informePorUsuario.put(dato.getNombreUsuario(), filasInforme);
            }

            boolean newFile = true;
            for (Vector<String> filaInforme : filasInforme) {
                if (filaInforme.get(0).equals(dato.getCodigoRAD())) {
                    newFile = false;
                    Calendar fechaCita = dato.getFechaCita();
                    if (fechaCita != null) {
                        int numDia = fechaCita.get(Calendar.DAY_OF_MONTH);
                        String codInt = String.valueOf(dato.getTipoIntervencion());
                        if (dato.isCitaPrevia()) codInt += "1";
                        if (!dato.isAcudeCita()) codInt = "0";
                        filaInforme.setElementAt(codInt, numDia);
                    }

                    break;
                }
            }

            if (newFile && codigoRadValidoPorFecha(dato.getCodigoRAD(), strMesInforme, strAñoInforme, paramsBD)) {
                Vector<String> filaInforme = new Vector<String>(numDiasMes + 2);
                filaInforme.setSize(numDiasMes + 2);
                Calendar fechaCita = dato.getFechaCita();
                if (fechaCita != null) {
                    int numDia = fechaCita.get(Calendar.DAY_OF_MONTH);
                    String codInt = String.valueOf(dato.getTipoIntervencion());
                    if (dato.isCitaPrevia()) codInt += "1";
                    if (!dato.isAcudeCita()) codInt = "0";
                    filaInforme.setElementAt(codInt, numDia);
                }
                filaInforme.setElementAt(dato.getCodigoRAD(), 0);

                filasInforme.add(filaInforme);
            }
        }

        // Formateamos las columnas del informe.
        Vector<String> columnasInforme = new Vector<String>(numDiasMes + 2);
        columnasInforme.setSize(numDiasMes + 2);
        for (int i = 0; i < numDiasMes + 2; i++) {
            String tituloColumna;
            if (i == 0) tituloColumna = "código RAD";
            else if (i == numDiasMes + 1) tituloColumna = "Total";
            else tituloColumna = String.valueOf(i);

            columnasInforme.setElementAt(tituloColumna, i);            
        }

        // Proceso la información y genero el xls
        HSSFWorkbook libroExcel = new HSSFWorkbook();
        for (String key: informePorUsuario.keySet()) {
            Vector<Vector<String>> filasInforme = informePorUsuario.get(key);

            m_Log.debug("El nombre de la hoja será: " + key);
            HSSFSheet hoja = libroExcel.createSheet(key);

            for (short indCol = 0; indCol < columnasInforme.size(); indCol++) {
                short anchoCol;
                if (indCol == 0) anchoCol = 7000;
                else if (indCol == columnasInforme.size()-1) anchoCol = 3000;
                else anchoCol = 700;

                hoja.setColumnWidth(indCol, anchoCol);
            }

            // Creamos el estilo de la cabecera del libro
            HSSFCellStyle estiloCeldaCabecera = libroExcel.createCellStyle();
            estiloCeldaCabecera.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
            estiloCeldaCabecera.setBottomBorderColor((short) 8);
            estiloCeldaCabecera.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
            estiloCeldaCabecera.setLeftBorderColor((short) 8);
            estiloCeldaCabecera.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
            estiloCeldaCabecera.setRightBorderColor((short) 8);
            estiloCeldaCabecera.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
            estiloCeldaCabecera.setTopBorderColor((short) 8);

            // Creamos el estilo de la primera celda de cada fila
            HSSFCellStyle estiloPrimeraCelda = libroExcel.createCellStyle();
            estiloPrimeraCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloPrimeraCelda.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
            estiloPrimeraCelda.setBottomBorderColor((short) 8);
            estiloPrimeraCelda.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
            estiloPrimeraCelda.setLeftBorderColor((short) 8);
            estiloPrimeraCelda.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
            estiloPrimeraCelda.setRightBorderColor((short) 8);
            estiloPrimeraCelda.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
            estiloPrimeraCelda.setTopBorderColor((short) 8);

            // Creamos el estilo de una celda cualquiera
            HSSFCellStyle estiloCelda = libroExcel.createCellStyle();
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setBottomBorderColor((short) 8);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setLeftBorderColor((short) 8);
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setRightBorderColor((short) 8);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setTopBorderColor((short) 8);

            HSSFCellStyle estiloUltimaCelda = libroExcel.createCellStyle();
            estiloUltimaCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            estiloUltimaCelda.setBottomBorderColor((short) 8);
            estiloUltimaCelda.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
            estiloUltimaCelda.setLeftBorderColor((short) 8);
            estiloUltimaCelda.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
            estiloUltimaCelda.setRightBorderColor((short) 8);
            estiloUltimaCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
            estiloUltimaCelda.setTopBorderColor((short) 8);

            // Creamos el encabezado
            HSSFFont fuenteEncabezado1 = libroExcel.createFont();
            fuenteEncabezado1.setFontHeightInPoints((short)10);
            fuenteEncabezado1.setFontName(HSSFFont.FONT_ARIAL);
            fuenteEncabezado1.setUnderline(HSSFFont.U_SINGLE);

            HSSFCellStyle estiloEncabezado1 = libroExcel.createCellStyle();
            estiloEncabezado1.setFont(fuenteEncabezado1);

            HSSFRow filaLeyenda = hoja.createRow((short)0);
            HSSFCell celdaLeyenda = filaLeyenda.createCell((short)6);

            String tituloCabeceraHoja;
            try {
                tituloCabeceraHoja = getCabeceraInforme(key, codOrganizacion);
                if (tituloCabeceraHoja == null) tituloCabeceraHoja = "ACTIVIDADES ÁREA ";
            } catch (TechnicalException te) {
                tituloCabeceraHoja = "ACTIVIDADES ÁREA ";
            }
            celdaLeyenda.setCellValue(tituloCabeceraHoja);
            celdaLeyenda.setCellStyle(estiloEncabezado1);

            short k;
            for (k = 19; k < columnasInforme.size() + 2; k++) {
                celdaLeyenda = filaLeyenda.createCell(k);
                if (k == 19) {
                    HSSFFont fuenteEncabezado2 = libroExcel.createFont();
                    fuenteEncabezado2.setFontHeightInPoints((short) 10);
                    fuenteEncabezado2.setFontName(HSSFFont.FONT_ARIAL);
                    fuenteEncabezado2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                    HSSFCellStyle estiloEncabezado2 = libroExcel.createCellStyle();
                    estiloEncabezado2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloEncabezado2.setLeftBorderColor((short) 8);
                    estiloEncabezado2.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    estiloEncabezado2.setTopBorderColor((short) 8);
                    estiloEncabezado2.setFont(fuenteEncabezado2);
                    celdaLeyenda.setCellValue("Códigos");
                    celdaLeyenda.setCellStyle(estiloEncabezado2);

                } else if (k == 27) {
                    HSSFFont fuenteEncabezado2 = libroExcel.createFont();
                    fuenteEncabezado2.setFontHeightInPoints((short) 8);
                    fuenteEncabezado2.setFontName(HSSFFont.FONT_ARIAL);

                    HSSFCellStyle estiloEncabezado2 = libroExcel.createCellStyle();
                    estiloEncabezado2.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    estiloEncabezado2.setTopBorderColor((short) 8);
                    estiloEncabezado2.setFont(fuenteEncabezado2);
                    celdaLeyenda.setCellValue("2 Intervención Individual");
                    celdaLeyenda.setCellStyle(estiloEncabezado2);
                } else if (k == 32) {
                    HSSFFont fuenteEncabezado2 = libroExcel.createFont();
                    fuenteEncabezado2.setFontHeightInPoints((short) 8);
                    fuenteEncabezado2.setFontName(HSSFFont.FONT_ARIAL);

                    HSSFCellStyle estiloEncabezado2 = libroExcel.createCellStyle();
                    estiloEncabezado2.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    estiloEncabezado2.setTopBorderColor((short) 8);
                    estiloEncabezado2.setFont(fuenteEncabezado2);
                    celdaLeyenda.setCellValue("5 Coordinación");
                    celdaLeyenda.setCellStyle(estiloEncabezado2);
                } else {
                    HSSFCellStyle estiloEncabezado2 = libroExcel.createCellStyle();
                    estiloEncabezado2.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    estiloEncabezado2.setTopBorderColor((short) 8);
                    celdaLeyenda.setCellStyle(estiloEncabezado2);
                }
            }

            // Fila 2 del encabezado.
            filaLeyenda = hoja.createRow((short)1);

            fuenteEncabezado1 = libroExcel.createFont();
            fuenteEncabezado1.setFontHeightInPoints((short)8);
            fuenteEncabezado1.setFontName(HSSFFont.FONT_ARIAL);

            estiloEncabezado1 = libroExcel.createCellStyle();
            estiloEncabezado1.setFont(fuenteEncabezado1);

            for (k = 0; k < columnasInforme.size() + 2; k++) {
                celdaLeyenda = filaLeyenda.createCell(k);
                if (k == 19) {
                    HSSFCellStyle estiloEncabezado2 = libroExcel.createCellStyle();
                    estiloEncabezado2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloEncabezado2.setLeftBorderColor((short)8);
                    estiloEncabezado2.setFont(fuenteEncabezado1);
                    celdaLeyenda.setCellValue("0 No Acude");
                    celdaLeyenda.setCellStyle(estiloEncabezado2);
                } else if (k == 27) {
                    celdaLeyenda.setCellValue("3 Intervención Familiar");
                    celdaLeyenda.setCellStyle(estiloEncabezado1);
                } else if (k == 32) {
                    celdaLeyenda.setCellValue("6 Otros");
                    celdaLeyenda.setCellStyle(estiloEncabezado1);
                } else if (k == 6) {
                    HSSFFont fuenteEncabezado2 = libroExcel.createFont();
                    fuenteEncabezado2.setFontHeightInPoints((short)10);
                    fuenteEncabezado2.setFontName(HSSFFont.FONT_ARIAL);

                    HSSFCellStyle estiloEncabezado2 = libroExcel.createCellStyle();
                    estiloEncabezado2.setFont(fuenteEncabezado2);

                    celdaLeyenda.setCellValue(key.toUpperCase());
                    celdaLeyenda.setCellStyle(estiloEncabezado2);
                }
            }

            // Fila 3 del encabezado.
            filaLeyenda = hoja.createRow((short)2);

            fuenteEncabezado1 = libroExcel.createFont();
            fuenteEncabezado1.setFontHeightInPoints((short)8);
            fuenteEncabezado1.setFontName(HSSFFont.FONT_ARIAL);

            estiloEncabezado1 = libroExcel.createCellStyle();
            estiloEncabezado1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            estiloEncabezado1.setBottomBorderColor((short) 8);
            estiloEncabezado1.setFont(fuenteEncabezado1);

            for (k = 0; k < columnasInforme.size() + 2; k++) {
                celdaLeyenda = filaLeyenda.createCell(k);
                if (k == 19) {
                    HSSFCellStyle estiloEncabezado2 = libroExcel.createCellStyle();
                    estiloEncabezado2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloEncabezado2.setLeftBorderColor((short)8);
                    estiloEncabezado2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    estiloEncabezado2.setBottomBorderColor((short) 8);
                    estiloEncabezado2.setFont(fuenteEncabezado1);
                    celdaLeyenda.setCellValue("1 Primera Entrevista");
                    celdaLeyenda.setCellStyle(estiloEncabezado2);
                } else if (k == 27) {
                    celdaLeyenda.setCellValue("4 Intervención Grupal");
                    celdaLeyenda.setCellStyle(estiloEncabezado1);
                } else if (k == 32) {
                    celdaLeyenda.setCellValue("7 Intervención Múltiple");
                    celdaLeyenda.setCellStyle(estiloEncabezado1);
                } else {
                    celdaLeyenda.setCellStyle(estiloEncabezado1);
                }
            }

            // Fila 4 del encabezado.
            filaLeyenda = hoja.createRow((short)3);
            fuenteEncabezado1 = libroExcel.createFont();
            fuenteEncabezado1.setFontHeightInPoints((short)6);
            fuenteEncabezado1.setFontName(HSSFFont.FONT_ARIAL);
            fuenteEncabezado1.setItalic(true);

            estiloEncabezado1 = libroExcel.createCellStyle();
            estiloEncabezado1.setFont(fuenteEncabezado1);

            celdaLeyenda = filaLeyenda.createCell((short)12);
            celdaLeyenda.setCellStyle(estiloEncabezado1);
            celdaLeyenda.setCellValue("Si es una cita no programada: Poner un nº1 a la derecha del código correspondiente a la actividad realizada con el paciente");

            // Fila 5 del encabezado
            filaLeyenda = hoja.createRow((short)4);
            filaLeyenda.setHeightInPoints(6);

            hoja.createFreezePane(columnasInforme.size() + 2, 5);

            // Proceso la cabecera del informe.
            HSSFRow fila = hoja.createRow((short)5);
            short i = 0;
            for (String tituloColumna: columnasInforme) {
                HSSFCell celda = fila.createCell(i++);
                celda.setCellValue(tituloColumna);
                celda.setCellStyle(estiloCeldaCabecera);
            }

            i = 6;
            for (Vector<String> filaInforme: filasInforme) {
                HSSFRow filaDatos = hoja.createRow(i++);
                short j = 0;
                for (String valorCelda: filaInforme) {
                    HSSFCell celda = filaDatos.createCell(j++);
                    celda.setCellValue(valorCelda);
                    if (j-1 == 0) celda.setCellStyle(estiloPrimeraCelda);
                    else if (j == filaInforme.size()) celda.setCellStyle(estiloUltimaCelda);
                    else celda.setCellStyle(estiloCelda);
                }
            }
        }


        String rutaArchivoSalida;
        try {
            File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
            File informe = File.createTempFile("informeAgenciaAntidroga_", ".xls", directorioTemp);

            FileOutputStream archivoSalida = new FileOutputStream(informe);
            libroExcel.write(archivoSalida);
            archivoSalida.close();

            rutaArchivoSalida = informe.getName();

        } catch (IOException ioe) {
            throw new TechnicalException(ioe.getMessage(), ioe);
        }

        return rutaArchivoSalida;

    }

    private String getCabeceraInforme(String nombreUsuario, int codOrganizacion) throws TechnicalException {

        ResourceBundle propsAgenciaAntiDroga = ResourceBundle.getBundle("es.altia.agora.business.gestionInformes.particular.impl.InformeAgenciaAntidroga");
        String codsTramitesCitas = propsAgenciaAntiDroga.getString("AgenciaAntidroga/tramitesCitas");

        String sqlQuery = "SELECT UOR_COD_VIS " +
                "FROM A_UOR " +
                "JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON (UOU_ORG = ? AND UOU_UOR = UOR_COD) " +
                "JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_USU ON (UOU_USU = USU_COD AND USU_NOM = ?) " +
                "WHERE UOR_COD IN (SELECT TRA_UTR FROM E_TRA WHERE TRA_PRO = 'CAID' AND TRA_COU IN (" + codsTramitesCitas + "))";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(paramsBD);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String codUorUsuario = null;
        try {

            con = abd.getConnection();
            ps = con.prepareStatement(sqlQuery);

            int i = 1;
            ps.setInt(i++, codOrganizacion);
            ps.setString(i, nombreUsuario);

            rs = ps.executeQuery();
            if (rs.next()) {
                codUorUsuario = rs.getString(1);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            codUorUsuario = null;

        } catch (BDException bde) {
            bde.printStackTrace();
            codUorUsuario = null;

        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            devolverConexion(abd, con);
        }

        String cabecera = null;
        if (codUorUsuario != null) {
            try {
                cabecera = propsAgenciaAntiDroga.getString("AgenciaAntidroga/" + codUorUsuario + "/cabecera");
            } catch (MissingResourceException mre) {
                mre.printStackTrace();
                cabecera = null;
            }
        }

        return cabecera;

    }

    private boolean codigoRadValidoPorFecha(String codigoRAD, String mesInforme, String anhoInforme, String[] params) throws TechnicalException {

        ResourceBundle propsAgenciaAntiDroga = ResourceBundle.getBundle("es.altia.agora.business.gestionInformes.particular.impl.InformeAgenciaAntidroga");
        String codCampoRAD = propsAgenciaAntiDroga.getString("AgenciaAntidroga/codRAD");

        String sqlQuery = "SELECT TO_CHAR(FECHA, 'MM/YYYY'), TIPO FROM (" +
                "SELECT EXP_FEI AS FECHA, 'INICIO' AS TIPO " +
                "FROM E_EXP " +
                "JOIN E_PCA ON (EXP_PRO = PCA_PRO AND PCA_COD = ?) " +
                "JOIN E_TXT ON (PCA_COD = TXT_COD AND EXP_NUM = TXT_NUM) " +
                "WHERE TXT_VALOR = ? " +
                "UNION " +
                "SELECT TFET_VALOR AS FECHA, 'DERIVACION' AS TIPO " +
                "FROM E_EXP " +
                "JOIN E_PCA ON (EXP_PRO = PCA_PRO) " +
                "JOIN E_TXT ON (PCA_COD = TXT_COD AND EXP_NUM = TXT_NUM AND TXT_VALOR = ?  AND PCA_COD = ?) " +
                "JOIN E_TFET ON (TFET_COD = 'FECHADERIVACION' AND TFET_NUM = EXP_NUM AND TFET_MUN = EXP_MUN) " +
                "UNION " +
                "SELECT TFET_VALOR AS FECHA, 'VUELTA' AS TIPO " +
                "FROM E_EXP " +
                "JOIN E_PCA ON (EXP_PRO = PCA_PRO) " +
                "JOIN E_TXT ON (PCA_COD = TXT_COD AND EXP_NUM = TXT_NUM AND TXT_VALOR = ?  AND PCA_COD = ?) " +
                "JOIN E_TFET ON (TFET_COD = 'FECHAVUELTA' AND TFET_NUM = EXP_NUM AND TFET_MUN = EXP_MUN) " +
                "UNION " +
                "SELECT EXP_FEF AS FECHA, 'FIN' AS TIPO " +
                "FROM E_EXP " +
                "JOIN E_PCA ON (EXP_PRO = PCA_PRO AND PCA_COD = ?) " +
                "JOIN E_TXT ON (PCA_COD = TXT_COD AND EXP_NUM = TXT_NUM) " +
                "WHERE TXT_VALOR = ?) " +
                "ORDER BY FECHA";

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Vector<String> periodosValidos = new Vector<String>();

        try {
            con = abd.getConnection();
            ps = con.prepareStatement(sqlQuery);

            int i = 1;
            ps.setString(i++, codCampoRAD);
            ps.setString(i++, codigoRAD);
            ps.setString(i++, codigoRAD);
            ps.setString(i++, codCampoRAD);
            ps.setString(i++, codigoRAD);
            ps.setString(i++, codCampoRAD);
            ps.setString(i++, codCampoRAD);
            ps.setString(i, codigoRAD);

            rs = ps.executeQuery();

            boolean inicioPeriodo = true;
            while (rs.next()) {
                i = 1;
                String fecha = rs.getString(i++);
                String tipo = rs.getString(i);

                if (inicioPeriodo) {
                    if (tipo.equals("VUELTA") || tipo.equals("INICIO")) {
                        periodosValidos.add(fecha);
                        inicioPeriodo = false;
                    }
                } else {
                    if (tipo.equals("DERIVACION")) {
                        periodosValidos.add(fecha);
                        inicioPeriodo = true;
                    } else if (tipo.equals("FIN")) {
                        periodosValidos.add(tipo);
                        inicioPeriodo = true;
                    }
                }
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (BDException bde) {
            bde.printStackTrace();
        } finally {
            closeStatement(ps);
            closeResultSet(rs);
            devolverConexion(abd, con);
        }



        boolean periodoValido = false;

        try {
            for (int j = 0; j < periodosValidos.size(); j = j + 2) {
                String fecha = "15/" + mesInforme + "/" + anhoInforme;
                String fechaInicio = "01/" + periodosValidos.get(j);
                if (periodosValidos.get(j + 1).equals("FIN")) {
                    DateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy");
                    Date dateFechaInicio = formateadorFecha.parse(fechaInicio);
                    Date dateFecha = formateadorFecha.parse(fecha);
                    if (dateFecha.after(dateFechaInicio)) {
                        periodoValido = true;
                        break;
                    }
                } else {
                    String fechaFin = Fecha.numDiasMes(Integer.parseInt(mesInforme), Integer.parseInt(anhoInforme)) + "/" + periodosValidos.get(j + 1);
                    DateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy");
                    Date dateFechaInicio = formateadorFecha.parse(fechaInicio);
                    Date dateFechaFin = formateadorFecha.parse(fechaFin);
                    Date dateFecha = formateadorFecha.parse(fecha);
                    if (dateFecha.after(dateFechaInicio) && dateFecha.before(dateFechaFin)) {
                        periodoValido = true;
                        break;
                    }
                }
            }
        } catch(ParseException pe){
            throw new TechnicalException(pe.getMessage());
        }

        return periodoValido;
    }

}
