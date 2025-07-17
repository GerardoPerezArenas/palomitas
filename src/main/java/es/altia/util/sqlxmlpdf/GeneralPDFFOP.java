package es.altia.util.sqlxmlpdf;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.TransformacionAtributoSelect;
import es.altia.util.conexion.AdaptadorSQLBD;
import oracle.xml.parser.v2.DOMParser;
import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XSLProcessor;
import oracle.xml.parser.v2.XSLStylesheet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.Driver;
import org.xml.sax.InputSource;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * <p>Título: @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera & Manuel Vera Silvestre
 * @version 1.0
 */

public class GeneralPDFFOP {

    private String BASE_DIR = "";
    private String APL_PATH_REAL = "";
    private String DATA_DIR = "";
    private String DATA_OUTPUT_DIR = "";
    private String DATA_OUTPUT_PDF = "";
    private String USU_DIR = "";
    private String S_ALEATORIO;
    private String pdf;
    private File cuerpo;
    private File documento;
    private File cabecera = null;
    private File pie = null;
    private String[] conexion;
    private boolean flag = false;
    private String body;
    private static Log m_Log;

    public GeneralPDFFOP(GeneralValueObject parametrosPdfVO){
        m_Log = LogFactory.getLog(this.getClass());
        BASE_DIR = (String)parametrosPdfVO.getAtributo("baseDir");//el directorio base
        APL_PATH_REAL = (String)parametrosPdfVO.getAtributo("aplPathReal");//el directorio base
        USU_DIR = (String)parametrosPdfVO.getAtributo("usuDir");//el directorio del usuario
        pdf = (String)parametrosPdfVO.getAtributo("pdfFile");//el nombre del pdf
        if((String)parametrosPdfVO.getAtributo("cabecera") != null){
            cabecera = new File((String)parametrosPdfVO.getAtributo("cabecera"));//la cabecera de las páginas
        }
        if((String)parametrosPdfVO.getAtributo("pie") != null){
            pie = new File((String)parametrosPdfVO.getAtributo("pie"));//la cabecera de las páginas
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

        S_ALEATORIO = "_" + Double.toString(Math.random()).substring(2);

        if((String)parametrosPdfVO.getAtributo("cuerpo") != null){
            body = (String)parametrosPdfVO.getAtributo("cuerpo");
            flag = true;
        } else {
            cuerpo = new File(DATA_OUTPUT_DIR + File.separator + pdf + "_cuerpo_" + S_ALEATORIO + ".xml");
        }
        documento = new File(DATA_OUTPUT_DIR + File.separator + pdf + "_documento_" + S_ALEATORIO + ".xml");
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
            if (m_Log.isDebugEnabled()) m_Log.debug("UBICACION PDFS GENERADOS: " + DATA_OUTPUT_PDF);

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
    public GeneralPDFFOP(String[] params, GeneralValueObject parametrosPdfVO){
        this(parametrosPdfVO);
        conexion = params;//parámetros de conexión a la BD
    }

    public String getPdf(){
        //m_Log.debug("Entro en el getPdf");
        String r = DATA_OUTPUT_PDF + File.separator + pdf + S_ALEATORIO + ".pdf";
        construyeDocumento();//preparo el documento
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputSource input = new InputSource();
            input.setEncoding("ISO-8859-1");
            input.setCharacterStream(new FileReader(documento));
            Driver driver = new Driver();
            driver.setRenderer(Driver.RENDER_PDF);
            driver.setInputSource(input);
            driver.setOutputStream(out);
            driver.run();
            out.close();
            byte[] content = out.toByteArray();
            FileOutputStream fOS = new FileOutputStream(r);//se escribe el fichero pdf
            if (m_Log.isDebugEnabled()) m_Log.debug("El directorio donde se guarda el pdf es : " + r);
            fOS.write(content);
            fOS.close();
        }
        catch(Exception e){
            e.printStackTrace();
            m_Log.error("Error en el método principal de GeneralPDF");
        }
        documento.delete();//borro el fichero auxiliar
        //m_Log.debug("Salgo del getPdf");
        //m_Log.debug("lo que devuelve el getPdf es : " + USU_DIR + "/" + pdf + S_ALEATORIO);
        return (pdf + S_ALEATORIO);
    }

    /**
     * Función pública encargada de realizar el pdf
     * @param ficheros Vector de objetos File con las diferentes partes del
     * cuerpo del archivo pdf
     * @return Un String indicando la ruta del pdf
     */
    public String getPdf(Vector ficheros){
        //m_Log.debug("Entro en el getPdf con ficheros");
        concatena(ficheros,cuerpo);//hago el cuerpo
        //m_Log.debug("Salgo de getPdf con ficheros");
        return getPdf();
    }

    /**
     * Función encargada de construir una tabla en formato xml aplicándole la
     * hoja xsl por defecto
     * @param sql La sentencia sql a partir de la cual se generará la tabla
     * @return Un File con el descriptor del fichero de la tabla
     */
    public File construyeTabla(String sql){
        return construyeTabla(sql,"defecto");
    }

    /**
     * Función encargada de construir una tabla en formato xml aplicando la hoja
     * xsl especificada por el usuario
     * @param sql La sentencia sql a partir de la cual se generará la tabla
     * @param xsl La hoja xsl a aplicar
     * @return Un File con el descriptor del fichero de la tabla
     */
    public File construyeTabla(String sql, String xsl){
        //m_Log.debug("Entro en el construyeTabla");
        File f = null;
        String S_LINEA  = " ";
        String S_COLUMN_NAME  = " ";
        String S_COLUMN_VALUE  = " ";
        String Snodatos  = " ";
        int IC = 0;
        int IC_MAX = 0;
        int IContador = 0;
        // CONEXION A LA BD
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(conexion);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();
            String S_ALEATORIO_TABLA = "_" +
                    Double.toString(Math.random()).substring(2);
            String S_XML_FILENAME = "sqlxml_" + pdf + S_ALEATORIO +
                    S_ALEATORIO_TABLA;
            String S_XML_FILEPATH = DATA_OUTPUT_DIR + File.separator + S_XML_FILENAME +
                    ".xml";
            FileWriter fw = new FileWriter(S_XML_FILEPATH);
            BufferedWriter bw= new BufferedWriter(fw);
            PrintWriter salida = new PrintWriter(new PrintWriter(bw));
            S_LINEA = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
            salida.println(S_LINEA);
            S_LINEA="<ROWSET>";
            salida.println(S_LINEA);
            rs = state.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            IC_MAX=meta.getColumnCount();
            if (rs.next()){//Hay datos
                do{
                    IContador++;
                    S_LINEA="<ROW ID=\""+ IContador + "\">";
                    salida.println(S_LINEA);
                    for (IC=1;IC < IC_MAX + 1 ;IC++){
                        S_COLUMN_NAME = meta.getColumnLabel(IC);
                        String aux = "";
                        if("ANOTACION".equals(S_COLUMN_NAME) && ( "diligencias".equals(xsl) || "diligenciasG".equals(xsl)) ) {
                            aux = bd.js_escape(rs.getString(IC));
                            aux = TransformacionAtributoSelect.replace(aux,"%0D%0A","[br/]");
                        } else {
                            aux = rs.getString(IC);
                        }
                        if(aux == null) {
                            aux = "";
                        } else {
                            aux = TransformacionAtributoSelect.escapeValorParaXML(aux);
                        }

                        S_COLUMN_VALUE= aux;
                        S_LINEA="<"+ S_COLUMN_NAME + ">" + S_COLUMN_VALUE  +
                                "</"+S_COLUMN_NAME + ">";
                        salida.println(S_LINEA);
                    }
                    S_LINEA="</ROW>";
                    salida.println(S_LINEA);
                } while (rs.next());
            }else{
                Snodatos="Non hay datos";
            }
            S_LINEA="</ROWSET>";
            salida.println(S_LINEA);
            salida.close();
            f = procesaXSL(S_XML_FILENAME, xsl);
        }catch(Exception e){
            rollBackTransaction(bd,con,e);
        }finally {
            commitTransaction(bd,con);
        }
        //m_Log.debug("Salgo del construyeTabla");
        return f;
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
        String S_ALEATORIO_TABLA = "_" +	Double.toString(Math.random()).substring(2);
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
        String S_ALEATORIO_TABLA = "_" +	Double.toString(Math.random()).substring(2);
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
        //m_Log.debug("Entro en el concatena");
        try{
            FileWriter fw = new FileWriter(union);
            BufferedWriter bw= new BufferedWriter(fw);
            PrintWriter salida = new PrintWriter(new PrintWriter(bw));
            AdaptadorSQLBD oad = new AdaptadorSQLBD(conexion);
            while(!ficheros.isEmpty()){
                File fich = (File)ficheros.remove(0);
                RandomAccessFile aux = new RandomAccessFile(fich, "r");
                String linea;
                while(aux.getFilePointer() != aux.length()){
                    linea = oad.js_unescape(convertirDeUTF8(aux.readLine()));
                    if(linea.length() > 5){
                        if(!("<?xml".equals(linea.substring(0,5))))
                            salida.println(linea);
                    }
                    else
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
        //m_Log.debug("Salgo de concatena");
    }

    /**
     * Función auxiliar encargada de construir el documento xml final
     */
    private void construyeDocumento(){
        //m_Log.debug("Entro en el construyeDocumento");
        RandomAccessFile entrada = null;
        RandomAccessFile cab = null;
        RandomAccessFile foot = null;
        try{
            if(!flag)
                entrada = new RandomAccessFile(cuerpo, "r");
            FileWriter fw = new FileWriter(documento);
            BufferedWriter bw= new BufferedWriter(fw);
            PrintWriter salida = new PrintWriter(new PrintWriter(bw));
            salida.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            salida.println("");
            salida.println("<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">");
            salida.println("<fo:layout-master-set>");
            salida.println("<fo:simple-page-master page-width=\"297mm\" page-height=\"210mm\" master-name=\"first\" padding-bottom=\"2mm\" background-color=\"white\">");
            salida.println("<fo:region-before extent=\"5cm\"/>");
            salida.println("<fo:region-body margin-top=\"1cm\" margin-bottom=\"0cm\"/>");
            salida.println("<fo:region-after extent=\"0cm\"/>");
            salida.println("</fo:simple-page-master>");
            salida.println("</fo:layout-master-set>");
            salida.println("<fo:page-sequence master-reference=\"first\">");
            salida.println("<fo:static-content flow-name=\"xsl-region-before\">");
            salida.println("<fo:block text-align=\"center\" padding-top=\"1cm\">");
            if (cabecera!=null) {
                cab = new RandomAccessFile(cabecera, "r");
                while(cab.getFilePointer() != cab.length())
                    salida.println(cab.readLine());
            }
            salida.println("</fo:block>");
            salida.println("</fo:static-content>");
            salida.println("<fo:static-content flow-name=\"xsl-region-after\">");
            salida.println("<fo:block text-align=\"center\" padding-top=\"1cm\">");
            if (pie!=null) {
                foot = new RandomAccessFile(pie, "r");
                while(foot.getFilePointer() != foot.length())
                    salida.println(foot.readLine());
            }
            salida.println("</fo:block>");
            salida.println("</fo:static-content>");
            salida.println("<fo:flow flow-name=\"xsl-region-body\">");
            salida.println("<fo:block text-align=\"center\" padding-top=\"2.5cm\">");
            if(flag)
                salida.println(body);
            else{
                while(entrada.getFilePointer() != entrada.length())
                    salida.println(entrada.readLine());
            }
            salida.println("</fo:block>");
            salida.println("</fo:flow>");
            salida.println("</fo:page-sequence>");
            salida.println("</fo:root>");

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
        //m_Log.debug("Salgo del construyeDocumento");
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
            processor.setOutputEncoding("ISO-8859-1");
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
        catch (UnsupportedEncodingException e){
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
            String F_XMLPDF = DATA_OUTPUT_DIR + File.separator + "xmlpdf_" + S_ALEATORIO + ".xml";
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
}