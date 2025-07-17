package es.altia.util.sqlxmlpdf;

import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.agora.business.util.GeneralValueObject;
import java.io.*;
import org.faceless.report.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import oracle.xml.parser.v2.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.*;

import es.altia.agora.business.util.TransformacionAtributoSelect;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import com.lowagie.text.pdf.*;
import es.altia.agora.business.registro.AnotacionRegistroVO;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import org.apache.commons.lang.math.NumberUtils;


/**
 * <p>Título: @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera & Manuel Vera Silvestre
 * @version 1.0
 */

public class GeneralPDF {

   private String BASE_DIR = "";
   private String APL_PATH_REAL = "";
   private String DATA_DIR = "";
   private String DATA_OUTPUT_DIR = "";
   private String DATA_OUTPUT_PDF = "";
   private String DATA_OUTPUT_HTML = "";
   private String DATA_OUTPUT_CSV = "";
   private String USU_DIR = "";
   private String S_ALEATORIO;
   private String pdf;
   private String estilo = null;//para la hoja de estilo
   private String style = null;//para los estilos q vienen del editor
   private File cuerpo;
   private File documento;
   private File cabecera = null;
   private File pie = null;
   private String[] conexion;
   private String sql;
   private String pagina;
   private boolean flag = false;
   private String body;
   private String tamCabecera = null;
   private String tamPie = null;
   private int numFilas = 0;
   private static Log m_Log;
  //protected static Config confReg = ConfigServiceHelper.getConfig("Registro");
   private String opcionint;
   String nombreLargo=" ";

   private Config commonConfig = ConfigServiceHelper.getConfig("common");

   public GeneralPDF(GeneralValueObject parametrosPdfVO){
        m_Log = LogFactory.getLog(this.getClass());

        BASE_DIR = (String)parametrosPdfVO.getAtributo("baseDir");//el directorio base
        APL_PATH_REAL = (String)parametrosPdfVO.getAtributo("aplPathReal");//el directorio base
        USU_DIR = (String)parametrosPdfVO.getAtributo("usuDir");//el directorio del usuario
        if((String)parametrosPdfVO.getAtributo("estilo") != null)
           estilo = (String)parametrosPdfVO.getAtributo("estilo");//la hoja de estilos
        if((String)parametrosPdfVO.getAtributo("style") != null)
           style = (String)parametrosPdfVO.getAtributo("style");//la hoja de estilos
        pdf = (String)parametrosPdfVO.getAtributo("pdfFile");//el nombre del pdf
        pagina = (String)parametrosPdfVO.getAtributo("pagina");//numeración de página
        if((String)parametrosPdfVO.getAtributo("cabecera") != null){
           cabecera = new File((String)parametrosPdfVO.getAtributo("cabecera"));//la cabecera de las páginas
           tamCabecera = (String)parametrosPdfVO.getAtributo("tamCabecera");//tamaño de la cabecera
        }
        if((String)parametrosPdfVO.getAtributo("pie") != null){
           pie = new File((String)parametrosPdfVO.getAtributo("pie"));//la cabecera de las páginas
           tamPie = (String)parametrosPdfVO.getAtributo("tamPie");//tamaño de la cabecera
        }

        creaDirectorios();

        String sPathUsuDir = APL_PATH_REAL + File.separator + USU_DIR + File.separator + "pdf" + File.separator + "xsl";
        File dir_usu_dir = new File(sPathUsuDir);
        if(USU_DIR == null || "".equals(USU_DIR.trim()) || !dir_usu_dir.exists() || !dir_usu_dir.isDirectory()){
            DATA_DIR =  APL_PATH_REAL + File.separator + "pdf" + File.separator + "xsl";             // El directorio donde estan las hojas xsl para generar los pdf
        } else {
            DATA_DIR =  sPathUsuDir;             // El directorio donde estan las hojas xsl para generar los pdf
        }
        if (m_Log.isDebugEnabled()) m_Log.debug("DIRECTORIO DE LOS FICHEROS XSL: " + DATA_DIR);

        S_ALEATORIO = "_" + Double.toString(java.lang.Math.random()).substring(2);

        if((String)parametrosPdfVO.getAtributo("cuerpo") != null){
           body = (String)parametrosPdfVO.getAtributo("cuerpo");
           flag = true;
        } else {
           cuerpo = new File(DATA_OUTPUT_DIR + File.separator + pdf + "_cuerpo_" + S_ALEATORIO + ".xml");
        }
        documento = new File(DATA_OUTPUT_DIR + File.separator + pdf + "_documento_" + S_ALEATORIO + ".xml");

        //OPCION IMPRESION INTERESADO
        if((null==parametrosPdfVO.getAtributo("opcionint"))||("".equals(parametrosPdfVO.getAtributo("opcionint"))))
        {
            parametrosPdfVO.setAtributo("opcionint", "op3");
        }
        if (parametrosPdfVO.getAtributo("opcionint").equals("op1")) nombreLargo="CORTO";
        else if(parametrosPdfVO.getAtributo("opcionint").equals("op2")) nombreLargo="MEDIO";
             else if (parametrosPdfVO.getAtributo("opcionint").equals("op3")) nombreLargo="LARGO";
   }

    /**
     * Funcion que permite fijar el pie de pagina de un documento pdf
     */
    public void setPie(String pieFile) {
        pie = new File(pieFile);
    }

   private void creaDirectorios(){

    try{
        DATA_OUTPUT_DIR = BASE_DIR + "/" + "tmp";                // El directorio temporal para los ficheros temporales necesarios para generar el pdf
        StringTokenizer st = new StringTokenizer(DATA_OUTPUT_DIR, "/");
        String tempRuta = "/";
        while(st.hasMoreTokens()){
            tempRuta += st.nextToken();
            File dir_temp = new File( tempRuta);
            if(!dir_temp.exists() || !dir_temp.isDirectory()){
                if (m_Log.isDebugEnabled()) m_Log.debug("NO EXISTE EL DIRECTORIO: " + tempRuta);
                if(!dir_temp.mkdir()) {
                    m_Log.error("NO SE HA PODIDO CREAR EL DIRECTORIO: " + tempRuta);
                }else{
                    if (m_Log.isDebugEnabled()) m_Log.debug("SE HA CREADO EL DIRECTORIO: " + tempRuta);
                }

            }
            tempRuta += "/";
        }
       if (m_Log.isDebugEnabled()) m_Log.debug("UBICACION TEMPORALES GENERADOS PARA OBTENER PDF: " + DATA_OUTPUT_DIR);

        DATA_OUTPUT_PDF = BASE_DIR + "/" + USU_DIR + "/pdf";         // El directorio donde se guardaran los pdfs generados
        st = new StringTokenizer(DATA_OUTPUT_PDF, "/");
        tempRuta = "/";
        while (st.hasMoreTokens()) {
            tempRuta += st.nextToken();
            File dir_temp = new File(tempRuta);
            if (!dir_temp.exists() || !dir_temp.isDirectory()) {
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("NO EXISTE EL DIRECTORIO: " + tempRuta);
                }
                if (!dir_temp.mkdir()) {
                    m_Log.error("NO SE HA PODIDO CREAR EL DIRECTORIO: " + tempRuta);
                } else {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("SE HA CREADO EL DIRECTORIO: " + tempRuta);
                    }
                }
            }
            tempRuta += "/";
        }
        if (m_Log.isDebugEnabled()) m_Log.debug("UBICACION PDFS GENERADOS: " + DATA_OUTPUT_PDF);

        // El directorio donde se guardaran los htmls generados
        DATA_OUTPUT_HTML = BASE_DIR + "/" + USU_DIR + "/html";
        st = new StringTokenizer(DATA_OUTPUT_HTML, "/");
        tempRuta = "/";
        while (st.hasMoreTokens()) {
            tempRuta += st.nextToken();
            File dir_temp = new File(tempRuta);
            if (!dir_temp.exists() || !dir_temp.isDirectory()) {
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("NO EXISTE EL DIRECTORIO: " + tempRuta);
                }
                if (!dir_temp.mkdir()) {
                    m_Log.error("NO SE HA PODIDO CREAR EL DIRECTORIO: " + tempRuta);
                } else {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("SE HA CREADO EL DIRECTORIO: " + tempRuta);
                    }
                }
            }
            tempRuta += "/";
        }
        if (m_Log.isDebugEnabled()) m_Log.debug("UBICACION HTMLS GENERADOS: " + DATA_OUTPUT_HTML);

        // El directorio donde se guardaran los csv generados
        DATA_OUTPUT_CSV = BASE_DIR + "/" + USU_DIR + "/csv";
        st = new StringTokenizer(DATA_OUTPUT_CSV, "/");
        tempRuta = "/";
        while (st.hasMoreTokens()) {
            tempRuta += st.nextToken();
            File dir_temp = new File(tempRuta);
            if (!dir_temp.exists() || !dir_temp.isDirectory()) {
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("NO EXISTE EL DIRECTORIO: " + tempRuta);
                }
                if (!dir_temp.mkdir()) {
                    m_Log.error("NO SE HA PODIDO CREAR EL DIRECTORIO: " + tempRuta);
                } else {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("SE HA CREADO EL DIRECTORIO: " + tempRuta);
                    }
                }
            }
            tempRuta += "/";
        }
        if (m_Log.isDebugEnabled()) m_Log.debug("UBICACION HTMLS GENERADOS: " + DATA_OUTPUT_CSV);

    }catch(Exception e){
      m_Log.error("******** EXCEPCION CATPURADA EN:" + this.getClass().getName() + " **********");
      e.printStackTrace();
    }
   }
   /**
    * Constructor de la clase
    * @param params Los parámetros de la conexión a BD
    * @param parametrosPdfVO GeneralValueObject que debe contener los atributos
    * baseDir con la dirección del directorio pdf, usuDir con la dirección del
    * directorio del usuario, estilo con la ruta relativa a la hoja de estilos
    * que usará el pdf y pdfFile con el nombre del fichero pdf a generar. Todas
    * las rutas son relativas al path del servidor
    */
   public GeneralPDF(String[] params, GeneralValueObject parametrosPdfVO){
	this(parametrosPdfVO);
	conexion = params;//parámetros de conexión a la BD
   }

    public String getPdf() {
        //m_Log.debug("Entro en el getPdf");
        //String r = DATA_OUTPUT_PDF + File.separator + "pdf" + File.separator + pdf + S_ALEATORIO + ".pdf";
        String r = DATA_OUTPUT_PDF + File.separator + pdf + S_ALEATORIO + ".pdf";
        construyeDocumento();//preparo el documento
        try {
            ReportParser.setLicenseKey("GB102A89CB0F748");
            ReportParser parser = ReportParser.getInstance();
            m_Log.debug("Parseando el xml");
            org.faceless.pdf2.PDF pdf = parser.parse(documento);//se hace la transformación
            FileOutputStream fOS = new FileOutputStream(r);//se escribe el fichero pdf
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("El directorio donde se guarda el pdf es : " + r);
            }
            pdf.render(fOS);
            fOS.close();
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error en el método principal de GeneralPDF");
        }
        documento.delete();//borro el fichero auxiliar

        return (pdf + S_ALEATORIO);
    }


    /*
     * Creamos el fichero CSV que se mostrará al usuario a partir del xml generado anteriormente
     */
    private String getCSV() {
        String r = DATA_OUTPUT_CSV + File.separator + pdf + S_ALEATORIO + ".csv";

        try {
            // copiamos el contenido del fichero xml al nuevo fichero csv
            FileOutputStream fOS = new FileOutputStream(r);
            InputStream in = new FileInputStream(cuerpo);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                fOS.write(buf, 0, len);
            }
            in.close();
            fOS.close();

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("El directorio donde se guarda el csv es : " + r);
            }

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error en el método principal de GeneralPDF");
        }

        // borramos los ficheros auxiliares
        documento.delete();
        cuerpo.delete();

        return (pdf + S_ALEATORIO); //devolvemos el nombre del fichero csv generado
    }

    /*
     * Creamos el fichero CSV CEPAP que se mostrará al usuario a partir del xml generado anteriormente
     */
    private String getCSVCEPAP(String nombreFichero) {

        
        String r = DATA_OUTPUT_CSV + File.separator + nombreFichero + ".csv";
        m_Log.debug("getCSVCEPAP::Ruta del fichero->" + r);
        try {
            // copiamos el contenido del fichero xml al nuevo fichero csv
            FileOutputStream fOS = new FileOutputStream(r);
            InputStream in = new FileInputStream(cuerpo);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                fOS.write(buf, 0, len);
            }
            in.close();
            fOS.close();

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("El directorio donde se guarda el csv es : " + r);
            }

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error en el método principal de GeneralPDF");
        }

        // borramos los ficheros auxiliares
        documento.delete();
        cuerpo.delete();

        return (nombreFichero); //devolvemos el nombre del fichero csv generado
    }



    /**
     * Función pública encargada de realizar el pdf
     * @param ficheros Vector de objetos File con las diferentes partes del
     * cuerpo del archivo pdf
     * @return Un String indicando la ruta del pdf
     */
    public String getPdf(Vector ficheros) {
        //m_Log.debug("Entro en el getPdf con ficheros");
        concatena(ficheros, cuerpo);//hago el cuerpo
        //m_Log.debug("Salgo de getPdf con ficheros");
        return getPdf();
    }

    /**
     * Función pública encargada de realizar el csv
     * @param ficheros Vector de objetos File con las diferentes partes del
     * cuerpo del archivo csv
     * @return Un String indicando la ruta del csv
     */
    public String getCSV(Vector ficheros) {
        concatena(ficheros, cuerpo);//hago el cuerpo
        return getCSV();
    }
/**
     * Función pública encargada de realizar el csv (CEPAP)
     * @param ficheros Vector de objetos File con las diferentes partes del
     * cuerpo del archivo csv
     * @return Un String indicando la ruta del csv (CEPAP)
     */
    public String getCSVCEPAP(Vector ficheros, String nombreFichero) {
        concatena(ficheros, cuerpo);//hago el cuerpo
        return getCSVCEPAP(nombreFichero);
    }


    public String getPdfConPortada(String pdfPortada, Vector ficheros) {
        String pdfResto = getPdf(ficheros);
        return anhadePortada(pdfPortada, pdfResto);
    }

    public int getNumeroPaginas(String pdfFile) {
        int n = 0;
        try {
            PdfReader reader = new PdfReader(DATA_OUTPUT_PDF + File.separator + pdfFile + ".pdf");
            reader.consolidateNamedDestinations();
            n = reader.getNumberOfPages();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n;
    }

   /**
    * Función encargada de construir una tabla en formato xml aplicándole la
    * hoja xsl por defecto
    * @param sql La sentencia sql a partir de la cual se generará la tabla
    * @return Un File con el descriptor del fichero de la tabla
    */
   public File construyeTabla(String sql) throws TechnicalException {
	return construyeTabla(sql,"defecto");
   }

   /**
    * Función encargada de construir una tabla en formato xml aplicando la hoja
    * xsl especificada por el usuario
    * @param sql La sentencia sql a partir de la cual se generará la tabla
    * @param xsl La hoja xsl a aplicar
    * @return Un File con el descriptor del fichero de la tabla
    */
    public File construyeTabla(String sql, String xsl) throws TechnicalException {
        return construyeTabla(sql, xsl, null);
   }

    /**
     * Función encargada de construir una tabla en formato xml aplicando la hoja
     * xsl especificada por el usuario
     *
     * @param sql La sentencia sql a partir de la cual se generará la tabla
     * @param xsl La hoja xsl a aplicar
     * @param listaAnotacionesImpresas La lista de numero de anotaciones obtenidas a imprimir
     * @return Un File con el descriptor del fichero de la tabla
     */
    public File construyeTabla(String sql, String xsl, List<AnotacionRegistroVO> listaAnotacionesImpresas) throws TechnicalException {

        m_Log.debug("Entro en el construyeTabla   " + xsl);
        File f = null;
        String S_LINEA = " ";
        String S_COLUMN_NAME = " ";
        String S_COLUMN_VALUE = " ";
        String Snodatos = " ";
        int IC = 0;
        int IC_MAX = 0;
        int IContador = 0;
        AnotacionRegistroVO anotacionImpresa = null;
        boolean anadirAnotacionImpresa = false;
        
        // CONEXION A LA BD
        AdaptadorSQLBD bd = new AdaptadorSQLBD(conexion);
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement state = null;

        try {
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            m_Log.debug("CONSULTA " + sql);
            state = con.prepareStatement(sql);
            String S_ALEATORIO_TABLA = "_" + Double.toString(java.lang.Math.random()).substring(2);
            String S_XML_FILENAME = "sqlxml_" + pdf + S_ALEATORIO + S_ALEATORIO_TABLA;
            String S_XML_FILEPATH = DATA_OUTPUT_DIR + File.separator + S_XML_FILENAME + ".xml";
            FileWriter fw = new FileWriter(S_XML_FILEPATH);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter salida = new PrintWriter(new PrintWriter(bw));
            S_LINEA = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
            salida.println(S_LINEA);
            S_LINEA = "<ROWSET>";
            salida.println(S_LINEA);
            rs = state.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            IC_MAX = meta.getColumnCount();
            if (rs.next()) {//Hay datos
                boolean col = true;
                boolean impr_anotacion = false;
                String fecha = null;
                String anotacion = null;
                String LINEA_S = "";
                String tmp = null;
                String tmp_f = null;
                do {
                    anotacionImpresa = new AnotacionRegistroVO();
                    anadirAnotacionImpresa = false;
                    
                    IContador++;
                    //m_Log.debug("------------- IContador"+IContador);
                    //S_LINEA = "<ROW ID=\"" + IContador + "\">";
                    //salida.println(S_LINEA);
                    LINEA_S = "<ROW ID=\"" + IContador + "\">";

                    for (IC = 1; IC < IC_MAX + 1; IC++) {

                        S_COLUMN_NAME = meta.getColumnLabel(IC);
                        //m_Log.debug("------------- S_COLUMN_NAME"+S_COLUMN_NAME);
                        String aux = "";
                        if ("ANOTACION".equals(S_COLUMN_NAME)) {
                            aux = TransformacionAtributoSelect.replace(AdaptadorSQLBD.js_escape(rs.getString(IC)), "%0D%0A", "[br/]");
                            //anotacion = aux;

                        } else {
                            aux = rs.getString(IC);
                            // Referencias de las anotaciones para la auditoria
                            if (listaAnotacionesImpresas != null) {
                                if ("NUM".equals(S_COLUMN_NAME)) {
                                    anotacionImpresa.setNumero(NumberUtils.createInteger(aux));
                                    anadirAnotacionImpresa = true;
                                } else if ("EJERCICIO".equals(S_COLUMN_NAME)) {
                                    anotacionImpresa.setEjercicio(NumberUtils.createInteger(aux));
                                    anadirAnotacionImpresa = true;
                                } else if ("TIPO".equals(S_COLUMN_NAME)) {
                                    anotacionImpresa.setTipo(aux);
                                    anadirAnotacionImpresa = true;
                                }
                            }
                        }
                        if (aux == null) {
                            aux = "";
                        } else {
                            aux = TransformacionAtributoSelect.escapeValorParaXML(aux);
                        }

                        S_COLUMN_VALUE = aux;
			 //m_Log.debug(" S_COLUMN_VALUE  "+S_COLUMN_VALUE);
                        //m_Log.debug(" ************  "+nombreLargo);
                        //m_Log.debug(" ************  "+S_COLUMN_NAME);

			 if ((("CALLE1".equals(S_COLUMN_NAME))||("CALLE2".equals(S_COLUMN_NAME))||("CALLE3".equals(S_COLUMN_NAME))||
                         ("CALLE4".equals(S_COLUMN_NAME)))&& ((nombreLargo.equals("CORTO"))||(nombreLargo.equals("MEDIO"))))  {
                            S_LINEA = "<" + S_COLUMN_NAME + ">" + " " + "</" + S_COLUMN_NAME + ">";
                            //m_Log.debug("----------------------------- S_LINEA  "+S_LINEA);
                        } else {
                            S_LINEA = "<" + S_COLUMN_NAME + ">" + S_COLUMN_VALUE + "</" + S_COLUMN_NAME + ">";
                        }
                        // salida.println(S_LINEA);*/
                        LINEA_S += S_LINEA;
                        if (S_COLUMN_NAME.equals("FECHA")) {

                            if (fecha == null) {
                                fecha = S_COLUMN_VALUE;
                                impr_anotacion = true;
                                if ((tmp == null)) {
                                    tmp = anotacion;
                                    tmp_f = fecha;
                                    impr_anotacion = false;
                                }
                                fecha = S_COLUMN_VALUE;
                            } else if (!fecha.trim().equals(S_COLUMN_VALUE.trim())) {
                                impr_anotacion = true;
                                if ((tmp == null)) {
                                    tmp = anotacion;
                                    tmp_f = fecha;
                                    impr_anotacion = false;
                                }
                                fecha = S_COLUMN_VALUE;
                                col = !col;
                            }
                        }
                    }

                    if (col) {
                        S_LINEA = "<COLOR> 1 </COLOR>";
                    } else {
                        S_LINEA = "<COLOR> 0 </COLOR>";
                    }
                    //salida.println(S_LINEA);
                    LINEA_S += S_LINEA;

                    S_LINEA = "</ROW>";
                    //  salida.println(S_LINEA);
                    LINEA_S += S_LINEA;

                    if ((tmp != null) && (!tmp.equals("")) && impr_anotacion) {
                        S_LINEA = "<DILIGENCIA> <ANOTACION>" + TransformacionAtributoSelect.escapeValorParaXML(tmp) + "</ANOTACION><FECHA>" + tmp_f + "</FECHA></DILIGENCIA>";
                        tmp = null;
                        impr_anotacion = false;
                        salida.println(S_LINEA);
                    }
                    salida.println(LINEA_S);

                    // Referencias de las anotaciones para la auditoria
                    if (anadirAnotacionImpresa) {
                        listaAnotacionesImpresas.add(anotacionImpresa);
                    }
                } while (rs.next());
                m_Log.debug("TOTAL FILAS RECUPERADAS : " + IContador);
                if (IContador > numFilas) {
                    numFilas = IContador;
                }
                if ((tmp != null) && (!tmp.equals(""))) {
                    S_LINEA = "<DILIGENCIA> <ANOTACION>" + TransformacionAtributoSelect.escapeValorParaXML(tmp) + "</ANOTACION><FECHA>" + tmp_f + "</FECHA></DILIGENCIA>";
                    tmp = null;
                    impr_anotacion = false;
                    salida.println(S_LINEA);
                }
            } else { // No hay datos en el ResultSet
                return null;
            }
            S_LINEA = "</ROWSET>";
            salida.println(S_LINEA);
            salida.flush();
            salida.close();
            f = procesaXSL(S_XML_FILENAME, xsl);

            SigpGeneralOperations.commit(bd, con);

        } catch (Exception e) {
            e.printStackTrace();
            SigpGeneralOperations.rollBack(bd, con);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(state);
            SigpGeneralOperations.devolverConexion(bd, con);
        }
        m_Log.debug("Salgo del construyeTabla");
        return f;
    }
    
    
    /**
    * Función encargada de construir una tabla en formato xml aplicando la hoja
    * xsl especificada por el usuario
    * @param sql La sentencia sql a partir de la cual se generará la tabla
    * @param xsl La hoja xsl a aplicar
    * @return Un File con el descriptor del fichero de la tabla
    */
    public File construyeTablaListadoEntradasOficina(String sql, String xsl,ArrayList<GeneralValueObject> anotaciones) throws TechnicalException {
        return construyeTablaListadoEntradasOficina(sql, xsl, anotaciones, null);
    }
    
    /**
    * Función encargada de construir una tabla en formato xml aplicando la hoja
    * xsl especificada por el usuario
    * @param sql La sentencia sql a partir de la cual se generará la tabla
    * @param xsl La hoja xsl a aplicar
    * @param anotaciones
    * @param listaNumAnotacionImpresos
    * @return Un File con el descriptor del fichero de la tabla
    * @throws es.altia.common.exception.TechnicalException
    */
    public File construyeTablaListadoEntradasOficina(String sql, String xsl,ArrayList<GeneralValueObject> anotaciones, List<AnotacionRegistroVO> listaNumAnotacionImpresos) throws TechnicalException {

	m_Log.debug("Entro en el construyeTablaListadoEntradasOficina   "+xsl);
	File f = null;
	String S_LINEA  = " ";
	String S_COLUMN_NAME  = " ";
	String S_COLUMN_VALUE  = " ";
	String Snodatos  = " ";
	int IC = 0;
	int IC_MAX = 0;
	int IContador = 0;


	// CONEXION A LA BD
        AdaptadorSQLBD bd = new AdaptadorSQLBD(conexion);
	Connection con=null;
	ResultSet rs=null;
        PreparedStatement state = null;

	try{
	   con = bd.getConnection();
	   bd.inicioTransaccion(con);
           m_Log.debug("CONSULTA "+sql);
            state = con.prepareStatement(sql);
            String S_ALEATORIO_TABLA = "_" + Double.toString(java.lang.Math.random()).substring(2);
            String S_XML_FILENAME = "sqlxml_" + pdf + S_ALEATORIO + S_ALEATORIO_TABLA;
            String S_XML_FILEPATH = DATA_OUTPUT_DIR + File.separator + S_XML_FILENAME + ".xml";
	   FileWriter fw = new FileWriter(S_XML_FILEPATH);
	   BufferedWriter bw= new BufferedWriter(fw);
	   PrintWriter salida = new PrintWriter(new PrintWriter(bw));
           boolean devolverContenido=false;
	   S_LINEA = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
	   salida.println(S_LINEA);
	   S_LINEA="<ROWSET>";
	   salida.println(S_LINEA);
            rs = state.executeQuery();
       ResultSetMetaData meta = rs.getMetaData();
	   IC_MAX=meta.getColumnCount();
	   if (rs.next()){//Hay datos
                boolean col = true;
                boolean impr_anotacion = false;
                String fecha = null;
                String anotacion = null;
                String LINEA_S="";
                String tmp = null;
                String tmp_f = null;
                

                
		do{

                   boolean continuar=false;

                    String numero=rs.getString("NUM");
                    String ejercicio =rs.getString("EJERCICIO");
                    String tipo=rs.getString("TIPO");

                    for(int i=0;i<anotaciones.size();i++){
                      GeneralValueObject anot = anotaciones.get(i);
                      anot.getAtributo("numero");
                      anot.getAtributo("ejercicio");
                      anot.getAtributo("tipo");

                      if((numero.equals(anot.getAtributo("numero")))&&(ejercicio.equals(anot.getAtributo("ejercicio")))&&(tipo.equals(anot.getAtributo("tipo"))))
                      {
                          continuar=true;
                          devolverContenido=true;

                      }
                    }
                   if(continuar){
                       // Referencia de las anotaciones para la auditoria
                       if (listaNumAnotacionImpresos != null) {
                           AnotacionRegistroVO anotacionImpresa = new AnotacionRegistroVO();
                           anotacionImpresa.setNumero(NumberUtils.createInteger(numero));
                           anotacionImpresa.setEjercicio(NumberUtils.createInteger(ejercicio));
                           anotacionImpresa.setTipo(tipo);
                           listaNumAnotacionImpresos.add(anotacionImpresa);
                       }
                       
		   IContador++;
		   //m_Log.debug("------------- IContador"+IContador);
                    //S_LINEA = "<ROW ID=\"" + IContador + "\">";
                    //salida.println(S_LINEA);
                    LINEA_S ="<ROW ID=\"" + IContador + "\">";

		   for (IC=1;IC < IC_MAX + 1 ;IC++){

			S_COLUMN_NAME = meta.getColumnLabel(IC);
			 //m_Log.debug("------------- S_COLUMN_NAME"+S_COLUMN_NAME);
			String aux = "";
                        if ("ANOTACION".equals(S_COLUMN_NAME))  {
                            aux = TransformacionAtributoSelect.replace(AdaptadorSQLBD.js_escape(rs.getString(IC)), "%0D%0A", "[br/]");
                            //anotacion = aux;

			} else{
				aux = rs.getString(IC);

			}
			if(aux == null) {
			   aux = "";
			} else {
				aux = TransformacionAtributoSelect.escapeValorParaXML(aux);
			}

			S_COLUMN_VALUE= aux;
			 //m_Log.debug(" S_COLUMN_VALUE  "+S_COLUMN_VALUE);
			 //m_Log.debug(" ************  "+nombreLargo);
			 //m_Log.debug(" ************  "+S_COLUMN_NAME);

			 if ((("CALLE1".equals(S_COLUMN_NAME))||("CALLE2".equals(S_COLUMN_NAME))||("CALLE3".equals(S_COLUMN_NAME))||
                         ("CALLE4".equals(S_COLUMN_NAME)))&& ((nombreLargo.equals("CORTO"))||(nombreLargo.equals("MEDIO"))))  {
                        S_LINEA = "<" + S_COLUMN_NAME + ">" +" "+ "</" + S_COLUMN_NAME + ">";
                        //m_Log.debug("----------------------------- S_LINEA  "+S_LINEA);
			 }else{
				 S_LINEA = "<" + S_COLUMN_NAME + ">" + S_COLUMN_VALUE + "</" + S_COLUMN_NAME + ">";
			 }
                        // salida.println(S_LINEA);*/
                        LINEA_S += S_LINEA;
                        if (S_COLUMN_NAME.equals("FECHA")){

                           if (fecha == null){
                               fecha = S_COLUMN_VALUE;
                                impr_anotacion= true;
                                if ((tmp==null)){
                                    tmp = anotacion;
                                    tmp_f = fecha;
                                    impr_anotacion= false;
                                }
                                fecha = S_COLUMN_VALUE;
                           }else  if (!fecha.trim().equals(S_COLUMN_VALUE.trim())){
                                impr_anotacion= true;
                                if ((tmp==null)){
                                    tmp = anotacion;
                                    tmp_f = fecha;
                                    impr_anotacion= false;
                                }
                                fecha = S_COLUMN_VALUE;
                                col = !col;
                           }
                        }
		   }

                    if (col) {
                        S_LINEA = "<COLOR> 1 </COLOR>";
                    } else {
                        S_LINEA = "<COLOR> 0 </COLOR>";
                    }
                    //salida.println(S_LINEA);
                    LINEA_S += S_LINEA;

		   S_LINEA="</ROW>";
                    //  salida.println(S_LINEA);
                    LINEA_S += S_LINEA;

                    if ((tmp != null) && (!tmp.equals("")) && impr_anotacion){
                        S_LINEA = "<DILIGENCIA> <ANOTACION>"+TransformacionAtributoSelect.escapeValorParaXML(tmp)+"</ANOTACION><FECHA>"+tmp_f+"</FECHA></DILIGENCIA>";
                        tmp = null;
                        impr_anotacion = false;
		   salida.println(S_LINEA);
                    }
                    salida.println(LINEA_S);

                   }//if cotinuar
		} while (rs.next());
               
		m_Log.debug("TOTAL FILAS RECUPERADAS : " + IContador);
                if (IContador > numFilas) {
                    numFilas = IContador;
                }
     if ((tmp != null) && (!tmp.equals(""))){
              S_LINEA = "<DILIGENCIA> <ANOTACION>"+TransformacionAtributoSelect.escapeValorParaXML(tmp)+"</ANOTACION><FECHA>"+tmp_f+"</FECHA></DILIGENCIA>";
              tmp = null;
              impr_anotacion = false;
              salida.println(S_LINEA);
        }
	   }else{ // No hay datos en el ResultSet
		   return null;
	   }
	   S_LINEA="</ROWSET>";
	   salida.println(S_LINEA);
            salida.flush();
	   salida.close();
	   if(devolverContenido)f = procesaXSL(S_XML_FILENAME, xsl);
           else f=null;

            SigpGeneralOperations.commit(bd, con);

	}catch(Exception e){
            e.printStackTrace();
            SigpGeneralOperations.rollBack(bd, con);
	}finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(state);
            SigpGeneralOperations.devolverConexion(bd, con);
	}
	m_Log.debug("Salgo del construyeTabla");
	return f;
   }

   /**
    * Función que devuelve el número máximo de filas recuperadas en alguna consulta. Puede
    * ser llamada despues de construyeTabla para saber cuantas filas se han obtenido.
    * @return un int con el numero máximo de filas observado
    */
   public int getNumFilas()
   {
    return numFilas;
   }

    /**
     * Función encargada de construir un Fichero con el xsl pasado y sin
     * ningun dato
     * @param xsl la hoja xsl a aplicar
     * @return un File con el descriptor del fichero sin datos
     */
    public File construyeTablaVacia(String xsl)
    {
       File f = null;
	   String S_LINEA  = " ";
	   String S_ALEATORIO_TABLA = "_" +	Double.toString(java.lang.Math.random()).substring(2);
	   String S_XML_FILENAME = "sqlxml_" + pdf + S_ALEATORIO + S_ALEATORIO_TABLA;
	   String S_XML_FILEPATH = DATA_OUTPUT_DIR + File.separator + S_XML_FILENAME + ".xml";
        try{
                FileWriter fw = new FileWriter(S_XML_FILEPATH);
                BufferedWriter bw= new BufferedWriter(fw);
	            PrintWriter salida = new PrintWriter(new PrintWriter(bw));
	            S_LINEA = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
	            salida.println(S_LINEA);
	            S_LINEA="<ROWSET>";
                salida.println(S_LINEA);
                S_LINEA="</ROWSET>";
                salida.println(S_LINEA);
                salida.close();
          }catch(Exception e){
            e.printStackTrace();
        }
       f = procesaXSL(S_XML_FILENAME, xsl);
	   return f;
    }

      /** 26-12-2005
     * Función encargada de construir un Fichero con el xsl pasado y con
     * los datos pasados
     * @param xsl la hoja xsl a aplicar
     * @param gVO la relación de los datos y sus nombre
     * @return un File con el descriptor del fichero sin datos
     */
    public File construyeTablaVacia2(String xsl, Hashtable gVO)
    {
       File f = null;
	   String S_LINEA  = " ";
	   String S_ALEATORIO_TABLA = "_" +	Double.toString(java.lang.Math.random()).substring(2);
	   String S_XML_FILENAME = "sqlxml_" + pdf + S_ALEATORIO + S_ALEATORIO_TABLA;
	   String S_XML_FILEPATH = DATA_OUTPUT_DIR + File.separator + S_XML_FILENAME + ".xml";
        try{
                FileWriter fw = new FileWriter(S_XML_FILEPATH);
                BufferedWriter bw= new BufferedWriter(fw);
	            PrintWriter salida = new PrintWriter(new PrintWriter(bw));
	            S_LINEA = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
	            salida.println(S_LINEA);
	            S_LINEA="<ROWSET>";
                salida.println(S_LINEA);
                Enumeration  claves = gVO.keys();
                while (claves.hasMoreElements()){
                    String dato   = (String) claves.nextElement();
                    String valor  = (String) gVO.get(dato);
                    salida.println("<" + dato + ">"+valor+"</" + dato + ">");
                }
                S_LINEA="</ROWSET>";
                salida.println(S_LINEA);
                salida.close();
          }catch(Exception e){
            e.printStackTrace();
        }
       f = procesaXSL(S_XML_FILENAME, xsl);
	   return f;
    }
   /**
    * Función auxiliar encargada de concatenar los diferente ficheros que
    * componen el cuerpo del documento
    * @param ficheros Vector de File con los ficheros a concatenar
    */
   public void concatena(Vector ficheros, File union){
	m_Log.debug("Entro en el concatena");
	try{
	   FileWriter fw = new FileWriter(union);
	   BufferedWriter bw= new BufferedWriter(fw);
	   PrintWriter salida = new PrintWriter(new PrintWriter(bw));
	   AdaptadorSQLBD oad = new AdaptadorSQLBD(conexion);
	   while(!ficheros.isEmpty()){

        if (ficheros.get(0)==null){
            ficheros.remove(0);
            continue;
        }

        File fich = (File)ficheros.remove(0);

        RandomAccessFile aux = new RandomAccessFile(fich, "r");
		String linea;
		while(aux.getFilePointer() != aux.length()){
		   linea = oad.js_unescape(convertirDeUTF8(aux.readLine())).trim();;
		   if(!linea.isEmpty() && (linea.length() < 5 || !"<?xml".equals(linea.substring(0,5))))
			   salida.println(linea);
		   		
		}
		aux.close();
		fich.delete();
	   }
	   salida.close();
	   bw.close();
	   fw.close();
	}
	catch(IOException ioe){
	   ioe.printStackTrace();
	   m_Log.error("Error al concatenar los ficheros en GeneralPDF");
	}
	m_Log.debug("Salgo de concatena");
   }

   /**
    * Función auxiliar encargada de construir el documento xml final
    */
   private void construyeDocumento(){
	m_Log.debug("Entro en el construyeDocumento");
	RandomAccessFile entrada = null;
	RandomAccessFile cab = null;
	RandomAccessFile foot = null;
	try{
	   if(!flag)
		entrada = new RandomAccessFile(cuerpo, "r");
	   FileWriter fw = new FileWriter(documento);
	   BufferedWriter bw= new BufferedWriter(fw);
	   PrintWriter salida = new PrintWriter(new PrintWriter(bw));
	   String skel = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
	   salida.println(skel);
	   skel = "<pdf>";
	   salida.println(skel);
	   skel = "<head>";
	   salida.println(skel);
	   if(estilo != null){
               String sigpBaseDir = commonConfig.getString("SIGP.webapp_base_dir");
                skel = "<meta name=\"base\" value=\"file:" + sigpBaseDir + "temp\"/>";
                salida.println(skel);
	   	skel = "<link type=\"stylesheet\" src=\"" + estilo + "\"></link>";
		salida.println(skel);
	   }

	   if(cabecera != null || pie != null){
		skel = "<macrolist>";
		salida.println(skel);
		if(cabecera != null){
		   skel = "<macro id=\"header\">";
		   salida.println(skel);
		   cab = new RandomAccessFile(cabecera, "r");
		   while(cab.getFilePointer() != cab.length()){
			String linea = convertirDeUTF8(cab.readLine()).trim();
			
			  if(!linea.isEmpty() && (linea.length() < 5 || !"<?xml".equals(linea.substring(0,5))))
				salida.println(linea);
						
		   }
		   skel = "</macro>";
		   salida.println(skel);
		}
		if(pie != null){
		   skel = "<macro id=\"footer\">";
		   salida.println(skel);
		   foot = new RandomAccessFile(pie, "r");
		   while(foot.getFilePointer() != foot.length()){
			String linea = convertirDeUTF8(foot.readLine());
			if(linea.length() > 5){
			   if(!("<?xml".equals(linea.substring(0,5))))
				salida.println(linea);
			}
			else
			   salida.println(linea);
		   }
		   skel = "</macro>";
		   salida.println(skel);
		}
		skel = "</macrolist>";
		salida.println(skel);
	   }
	   skel = "</head>";
	   salida.println(skel);
	   skel = "<body ";
	   if(pagina != null)
		skel += "pagenumber=\"" + pagina + "\"";
	   skel += " class=\"page\"";
	   if(style != null)
		skel += " style=\"" + style + "\"";
	   if(cabecera != null){
		skel += " header=\"header\" header-height=\"";
		if(tamCabecera != null)
		   skel += tamCabecera;
		else
		   skel += "40mm";
		skel += "\"";
	   }
	   if(pie != null){
		skel += " footer=\"footer\" footer-height=\"";
		if(tamPie != null)
		   skel += tamPie;
		else
		   skel += "24mm";
		skel += "\"";
	   }
	   skel += ">";
	   salida.println(skel);
	   if(flag)
		salida.println(body);
	   else {
                while (entrada.getFilePointer() != entrada.length()) {
                    String linea = entrada.readLine().trim();
                    if (!linea.isEmpty() && (linea.length() < 5 || !"<?xml".equals(linea.substring(0, 5)))) {
                        salida.println(linea);
                    }
                }
            }
	   skel = "</body>";
	   salida.println(skel);
	   skel = "</pdf>";
	   salida.println(skel);
	   salida.close();
	   bw.close();
	   fw.close();

	}
	catch(IOException ioe){
	   ioe.printStackTrace();
	   m_Log.error("Error en el método construyeDocumento");
	}
	finally{
	   if(!flag){
		cerrarFichero(entrada);
		cuerpo.delete();
	   }
	   if(cabecera != null){
		cerrarFichero(cab);
		cabecera.delete();
	   }
	   if(pie != null){
		cerrarFichero(foot);
		pie.delete();
	   }
	}
	m_Log.debug("Salgo del construyeDocumento");
   }

   /**
    * Función encargada de realizar el proceso de la hoja xsl
    * @param xmlFile El xml a tratar
    * @param xslFile La hoja xsl a aplicar
    * @return El fichero procesado
    */
   private File procesaXSL(String xmlFile,String xslFile){
	//m_Log.debug("Entro en el procesaXSL");
	File f = null;
	//carga variables
	String P_XML= xmlFile;
	String P_XSL= xslFile;
	String P_PDF= pdf + S_ALEATORIO;
	DOMParser dparser;
	XMLDocument xml, xsldoc, out;
	URL xslURL;
	URL xmlURL;
	try{
	   // Must pass in the names of the XSL and XML files
	   // Parse xsl and xml documents
	   dparser = new DOMParser();
	   dparser.setPreserveWhitespace(true);
	   // parser input XSL file
	   xslURL = this.createURL(DATA_DIR + File.separator + P_XSL + ".xsl");
	   //xslURL = new URL(P_XSL+ ".xsl");
	   dparser.parse(xslURL);

	   xsldoc = dparser.getDocument();
	   // parser input XML file
	   xmlURL = this.createURL(DATA_OUTPUT_DIR + File.separator + P_XML + ".xml");
	   //xmlURL = new URL(P_XML + ".xml");
	   dparser.parse(xmlURL);
	   File datos = new File(xmlURL.getFile());
	   xml = dparser.getDocument();
	   // instantiate a stylesheet
	   XSLProcessor processor = new XSLProcessor();
	   processor.setBaseURL(xslURL);
	   XSLStylesheet xsl = processor.newXSLStylesheet(xsldoc);
	   // display any warnings that may occur
	   processor.showWarnings(true);
	   processor.setErrorStream(System.err);
	   // Process XSL
	   // processor.processXSL(xsl, xml, System.out);
	   // miguel
	   String F_XMLPDF = DATA_OUTPUT_DIR + File.separator + "xmlpdf_" + P_XML + ".xml";
	   f = new File(F_XMLPDF);
	   FileOutputStream fOS = new FileOutputStream(f);
	   processor.processXSL(xsl, xml,fOS );
	   fOS.close();
	   datos.delete();
	}
	catch(Exception e){
	   e.printStackTrace();
	   m_Log.error("Error al escribir la tabla");
	}
	//m_Log.debug("Salgo del procesaXSL");
	return f;
   }

   private void rollBackTransaction(AdaptadorSQLBD bd, Connection con,
						Exception e){
	try {
	   bd.rollBack(con);
	}catch(Exception e1) {
	   e1.printStackTrace();
	}finally {
	   e.printStackTrace();
	}
   }

   private void commitTransaction(AdaptadorSQLBD bd,Connection con){
	try{
	   bd.finTransaccion(con);
	   bd.devolverConexion(con);
	}catch(Exception ex) {
	   ex.printStackTrace();
	   m_Log.error("SQLException: " + ex.getMessage()) ;
	}
   }

  static URL createURL(String fileName){
		URL url = null;
		try{
	  	url = new URL(fileName);
		}catch (MalformedURLException ex){
	   	File f = new File(fileName);
	   	try{
				String path = f.getAbsolutePath();
				// This is a bunch of weird code that is required to
				// make a valid URL on the Windows platform, due
				// to inconsistencies in what getAbsolutePath returns.
				String fs = System.getProperty("file.separator");
				if (fs.length() == 1){
		   		char sep = fs.charAt(0);
		   		if (sep != '/')
						path = path.replace(sep, '/');
		   		if (path.charAt(0) != '/')
						path = '/' + path;
				}
				path = "file://" + path;
				url = new URL(path);
	   	}catch (MalformedURLException e){
			m_Log.error("NO SE PUEDE CREAR LA URL PARA: " + fileName);
			System.exit(0);
	   	}
		}
		return url;
  }

   private String convertirDeUTF8(String s) {
	String out = "";
	try{
	   out = new String(s.getBytes("ISO-8859-1"), "UTF-8");
	}
	catch (java.io.UnsupportedEncodingException e){
	   e.printStackTrace();
	   m_Log.error("Error en el método auxiliar convertirDeUTF8");
	}
	//m_Log.debug("cadena: " + out);
	return out;
   }

   /**
    * Función auxiliar encargada de cerrar un RandomAccessFile
    * @param raf el fichero a cerrar
    */
   private void cerrarFichero(RandomAccessFile raf){
	try{
	   raf.close();
	}
	catch(IOException ioe){
	   ioe.printStackTrace();
	   m_Log.error("Error al cerrar el fichero");
	}
   }

/**
 * Función encargada de transformar un texto xml a partir de una hoja xsl.
 *
 * @param textoXml Texto xml a transformar
 * @param nombreXsl Nombre de la hoja xsl que determina la transformación
 * @return Un File con el descriptor del fichero de la tabla
 */
	public File transformaXML (String textoXml, String nombreXsl) {
		File f = null; // Resultado de la transformación.
		DOMParser dparser;	// Realiza el parseo.
		StringReader srXml; // Para que el parser pueda leer el texto xml.
		XMLDocument xsldoc; // Plantilla xsl parseada.
		XMLDocument xmldoc; // Texto xml parseado.
		URL xslURL;			// Ruta del archivo xsl.

		try {
			// Creamos el objeto parseador.
			dparser = new DOMParser();
			dparser.setPreserveWhitespace(true);

			// Creamos la ruta del fichero xsl y lo parseamos.
			xslURL = this.createURL(DATA_DIR + File.separator + nombreXsl + ".xsl");
                        m_Log.info("ruta fichero " + xslURL);
			dparser.parse(xslURL);
			xsldoc = dparser.getDocument();

			// Creamos el "reader" del texto xml y lo parseamos.
                        textoXml=textoXml.replaceAll("&", "&amp;");                        
			srXml = new StringReader (textoXml);                       
			dparser.parse(srXml); 
			xmldoc = dparser.getDocument();

			// Instanciamos los objetos para el procesado xsl.
			XSLProcessor processor = new XSLProcessor();
			processor.setBaseURL(xslURL);
			XSLStylesheet xsl = processor.newXSLStylesheet(xsldoc);
			// display any warnings that may occur
			processor.showWarnings(true);
			processor.setErrorStream(System.err);
			// Procesamos el texto xml a partir de la plantilla xsl.
			String F_XMLPDF = DATA_OUTPUT_DIR + File.separator + "xmlpdf_" + S_ALEATORIO + ".xml";
                        m_Log.info("ruta fichero " + F_XMLPDF);
			f = new File(F_XMLPDF);
			FileOutputStream fos = new FileOutputStream(f);
			processor.processXSL(xsl, xmldoc, fos);
			fos.close();
		} catch(Exception e){
			m_Log.error("Error al realizar la transformación de xml con xsl");
			e.printStackTrace();
		}

		return f;
	}

    /**
     * Concatena los pdfs especificados por los paths contenidos en pathsPdfs
     * @param pdfPortada, pdfResto
     * @return String
     */
    public String anhadePortada(String pdfPortada, String pdfResto)
    {
        try {
            Vector pathsPdfs = new Vector();
            pathsPdfs.add(pdfPortada);
            pathsPdfs.add(pdfResto);
            String r = DATA_OUTPUT_PDF + File.separator + pdf + "portada" + S_ALEATORIO + ".pdf";
            int pageOffset = 0;
            ArrayList master = new ArrayList();
            int f = 0;
            com.lowagie.text.Document document = null;
            PdfCopy  writer = null;
            while (f < pathsPdfs.size()) {
                // we create a reader for a certain document
                PdfReader reader = new PdfReader(DATA_OUTPUT_PDF + File.separator + (String)pathsPdfs.get(f) + ".pdf");
                reader.consolidateNamedDestinations();
                // we retrieve the total number of pages
                int n = reader.getNumberOfPages();
                List bookmarks = SimpleBookmark.getBookmark(reader);
                if (bookmarks != null) {
                    if (pageOffset != 0)
                        SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
                    master.addAll(bookmarks);
                }
                pageOffset += n;

                if (f == 0) {
                    // step 1: creation of a document-object
                    document = new com.lowagie.text.Document(reader.getPageSizeWithRotation(1));
                    // step 2: we create a writer that listens to the document
                    writer = new PdfCopy(document, new FileOutputStream(r));
                    // step 3: we open the document
                    document.open();
                }
                // step 4: we add content
                PdfImportedPage page;
                for (int i = 1; i < n+1; i++) {
                    page = writer.getImportedPage(reader, i);
                    writer.addPage(page);
                }
                PRAcroForm form = reader.getAcroForm();
                if (form != null)
                    writer.copyAcroForm(reader);
                f++;
            }
            if (master.size() > 0)
                writer.setOutlines(master);
            // step 5: we close the document
            document.close();
            //Eliminamos los documentos concatenados
            for (int i=0;i<pathsPdfs.size();i++) {
                File file = new File(DATA_OUTPUT_PDF + (String)pathsPdfs.get(i));
                file.delete();
                //m_Log.debug("Fichero " + (String)pathsPdfs.get(i) + " borrado: " + file.delete());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return pdf + "portada" + S_ALEATORIO;
    }


    /**
     * Función encargada de transformar un texto xml en una pagina html mediante una hoja xsl.
     *
     * @param textoXml Texto xml a transformar
     * @param nombreXsl Nombre de la hoja xsl que determina la transformación
     * @return Un String con el nombre del fichero resultado de la operacion
     */
    	public String transformaXMLenHTML (String textoXml, String nombreXsl) {
    		File f = null; // Resultado de la transformación.
    		DOMParser dparser;	// Realiza el parseo.
    		StringReader srXml; // Para que el parser pueda leer el texto xml.
    		XMLDocument xsldoc; // Plantilla xsl parseada.
    		XMLDocument xmldoc; // Texto xml parseado.
    		URL xslURL;			// Ruta del archivo xsl.

    		try {
    			// Creamos el objeto parseador.
    			dparser = new DOMParser();
    			dparser.setPreserveWhitespace(true);

    			// Creamos la ruta del fichero xsl y lo parseamos.
    			xslURL = createURL(DATA_DIR + File.separator + nombreXsl + ".xsl");
    			dparser.parse(xslURL);
    			xsldoc = dparser.getDocument();

    			// Creamos el "reader" del texto xml y lo parseamos.
    			srXml = new StringReader (textoXml);
    			dparser.parse(srXml);
    			xmldoc = dparser.getDocument();

    			// Instanciamos los objetos para el procesado xsl.
    			XSLProcessor processor = new XSLProcessor();
    			processor.setBaseURL(xslURL);
    			XSLStylesheet xsl = processor.newXSLStylesheet(xsldoc);
    			// display any warnings that may occur
    			processor.showWarnings(true);
    			processor.setErrorStream(System.err);
    			// Procesamos el texto xml a partir de la plantilla xsl.
    			// Nota: 'pdf' hace las veces de 'nombreFichero' en esta clase
    			String F_XMLPDF = DATA_OUTPUT_HTML + File.separator + pdf + S_ALEATORIO + ".html";
    			f = new File(F_XMLPDF);
    			FileOutputStream fos = new FileOutputStream(f);
    			processor.processXSL(xsl, xmldoc, fos);
    			fos.close();
    		} catch(Exception e){
    			m_Log.error("Error al realizar la transformación de xml con xsl");
    			e.printStackTrace();
    		}

    		return pdf + S_ALEATORIO;
    	}

}