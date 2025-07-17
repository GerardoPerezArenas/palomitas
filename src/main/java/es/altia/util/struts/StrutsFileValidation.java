/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.util.struts;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.commons.MimeTypes;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.struts.upload.FormFile;

/**
 * Operaciones de validación de ficheros subidos al servidor
 * 
 * @author Óscar Rodríguez Brea
 */
public class StrutsFileValidation {

     // Fichero de configuración
     private static Config m_ConfigTechnical  = ConfigServiceHelper.getConfig("techserver");
     private static Config m_ConfigRegistro   = ConfigServiceHelper.getConfig("Registro");
     private static Config m_ConfigExpediente = ConfigServiceHelper.getConfig("Expediente");
     private static Config m_ConfigTercero    = ConfigServiceHelper.getConfig("Terceros");
     
     private static Log log = LogFactory.getLog(StrutsFileValidation.class.getName());          
     private static final String COMMA = ",";   
     private static final String MIMETYPES_UPLOAD_PARAM     = "mimetype.upload.correct";
     private static final String FILESIZE_UPLOAD_PARAM      = "filesize.upload.correct";
     private static final String EXTENSION_UPLOAD_CORRECT   = "extension.upload.correct";
     private static final String DOT   = ".";
     //public static int TAM_MAX_FILE = 0;
     //public static String EXTENSION_FILE = null;
     //public static String MYME_TYPES = null;     
     public static String APP_REGISTRO                   = "REGISTRO";
     public static String APP_EXPEDIENTES                = "EXPEDIENTE";
     public static String APP_TERCERO                    = "TERCERO";
     
     public static final String SUFFIX_REGISTRO         = "REGISTRO";
     public static final String SUFFIX_EXPEDIENTE       = "EXPEDIENTE";
     public static final String SUFFIX_TERCERO          = "TERCERO";
     public static final String TAM_MAX_DOC_PREFFIX     = "TAM_MAX_DOC_";
     public static final String EXTENSION_DOC_PREFFIX   = "EXTENSION_DOC_";
     public static final String TIPOS_MIME_PREFFIX      = "TIPOS_MIME_";
     public static final String TAM_MAX_DOCS_PREFFIX     = "TAM_MAX_DOCS_";          
     // Tabla hash que contiene los límites en cuanto a subida de ficheros, tanto para 
     // el Registro como para Expedientes
     protected static Hashtable<String,Object> limites = new Hashtable<String,Object>();
     public static long SIZE_FILE_VALIDATED      = 0;
     
     static{        
         // Guardando parámetros configuración Registro de entrada
         limites.put(TAM_MAX_DOC_PREFFIX + SUFFIX_REGISTRO ,Integer.parseInt(m_ConfigRegistro.getString(FILESIZE_UPLOAD_PARAM)));
         limites.put(EXTENSION_DOC_PREFFIX + SUFFIX_REGISTRO ,m_ConfigRegistro.getString(EXTENSION_UPLOAD_CORRECT));
         limites.put(TIPOS_MIME_PREFFIX + SUFFIX_REGISTRO,m_ConfigRegistro.getString(MIMETYPES_UPLOAD_PARAM));

         
         // Guardando parámetros configuración Gestión de expedientes
         limites.put(TAM_MAX_DOC_PREFFIX + SUFFIX_EXPEDIENTE,Integer.parseInt(m_ConfigExpediente.getString(FILESIZE_UPLOAD_PARAM)));
         limites.put(EXTENSION_DOC_PREFFIX + SUFFIX_EXPEDIENTE,m_ConfigExpediente.getString(EXTENSION_UPLOAD_CORRECT));
         limites.put(TIPOS_MIME_PREFFIX + SUFFIX_EXPEDIENTE,m_ConfigExpediente.getString(MIMETYPES_UPLOAD_PARAM));


         limites.put(TAM_MAX_DOCS_PREFFIX + SUFFIX_REGISTRO,m_ConfigRegistro.getString("filesize.total.upload.correct"));
         limites.put(TAM_MAX_DOCS_PREFFIX + SUFFIX_EXPEDIENTE,m_ConfigExpediente.getString("filesize.total.upload.correct"));

         /*
         // Guardando parámetros de configuración para Gestión de terceros
         limites.put(TAM_MAX_DOC_PREFFIX + SUFFIX_TERCERO ,Integer.parseInt(m_ConfigTercero.getString(FILESIZE_UPLOAD_PARAM)));
         limites.put(EXTENSION_DOC_PREFFIX + SUFFIX_TERCERO ,m_ConfigTercero.getString(EXTENSION_UPLOAD_CORRECT));
         limites.put(TIPOS_MIME_PREFFIX + SUFFIX_TERCERO,m_ConfigTercero.getString(MIMETYPES_UPLOAD_PARAM));
         limites.put(TAM_MAX_DOCS_PREFFIX + SUFFIX_TERCERO,m_ConfigTercero.getString("filesize.total.upload.correct"));
         */
     }  
     
     
     /**
      * Devuelve el límite correspondiente a un determinado prefijo y sufijo, ejemplo,
      * TAM_MAX_DOC_PREFFIX y APP_REGISTRO devuelve el tamaño de cada fichero individual para 
      * la aplicación de registro de expedientes
      * @param preffix: Prefijo
      * @param suffix: Sufijo
      * @return El objeto correspondiente o null si no existe
      */
     public static Object getLimite(String preffix,String suffix)
     {
         return limites.get(preffix + suffix);
     }
          
    /**
     * Comprueba que el tipo mime de un fichero subido al servidor sea de uno
     * de los tipos mime válidos definidos en el fichero de configuración
     * @param file: Formfile
     * @param app: Aplicación de la que se extrae
     * @return boolean
     */
    public static boolean isExtensionValid(FormFile file,String app)
    {   
        String[] types   = ((String)limites.get(TIPOS_MIME_PREFFIX + app)).split(COMMA);                
        //MYME_TYPES.split(COMMA);
        boolean exito = false;
            
        log.debug("isExtensionValid FormFile,app: " + file + "<>" + app);
        log.debug("isExtensionValid - FormFile: " + file);
        if(file!=null && file.getFileSize()>0)
        {            
            ArrayList<String> lista = new ArrayList(Arrays.asList(types));
            log.debug("Tipos mime válidos: " + lista);
            String filetype = file.getContentType();
            log.debug("Tipo mime file upload: " + filetype);
            // Si es octet-stream tratamos de deducir el mimetype a partir de 
            // la extension
            if (MimeTypes.BINARY[0].equals(filetype)) {
                filetype = MimeTypes.guessMimeType(filetype, file.getFileName());
            }
            if(lista.contains(filetype)){
               exito = true; 
            }    
            //Añadimos estas dos comprobaciones ya que el contains de la lista anterior devuelve false aunque en la lista estén los mimetypes siguientes
            if(filetype.equals("text/plain")) return true;
            if(filetype.equals("text/xml")) return true;
        }                
        return exito;
    }
         
    
     /**
     * Comprueba que el tipo mime de un fichero subido al servidor sea de uno
     * de los tipos mime válidos definidos en el fichero de configuración
     * @param extension: Extensión del fichero
     * @param app: Aplicación para la que se busca la extensión, p ej: StrutsFileValidation.APP_REGISTRO
     * o StrutsFileValidation.APP_EXPEDIENTE
     * @return boolean
     */
    public static boolean isExtensionValid(String extension,String app)
    {     
        //String[] ext   = EXTENSION_FILE.split(COMMA);
        String[] ext   = ((String)limites.get(EXTENSION_DOC_PREFFIX + app)).split(COMMA); 
        boolean exito = false;                
                
        log.debug("isExtensionValid extension,app: " + extension + "<>" + app);
        log.debug("isExtensionValid - File - extension: " + extension);        
        ArrayList<String> lista = new ArrayList(Arrays.asList(ext));
        log.debug("Tipos extension válidos: " + lista);                        
        for(int i=0;i<lista.size();i++){
            log.debug("ext- ArrayList: " + lista.get(i));
        }
        
        if(lista.contains(extension)){
            log.debug("isExtensionValid - la extensión es válida");
            exito = true; 
        }else log.debug("isExtensionValid - la extensión no es válida");
        
        return exito;
    }   
    
    
    /**
     * Comprueba que el tipo mime de un fichero subido al servidor sea de uno
     * de los tipos mime válidos definidos en el fichero de configuración     
     * @param extension: Extensión del fichero
     * @param app: Aplicación para la que se busca la extensión, p ej: StrutsFileValidation.APP_REGISTRO
     * o StrutsFileValidation.APP_EXPEDIENTE
     * @return boolean
     */
    public static boolean isExtensionValid(File file,String app)
    {     
        //String[] ext   = EXTENSION_FILE.split(COMMA);
        String[] ext   = ((String)limites.get(EXTENSION_DOC_PREFFIX + app)).split(COMMA); 
        boolean exito = false;                
                
        log.debug("isExtensionValid File,app: " + file + "<>" + app);
        log.debug("isExtensionValid - File - name: " + file.getName());             
        int punto        = file.getName().lastIndexOf('.');
        String extension = file.getName().substring(punto + 1);
        log.debug("isExtensionValid - File - extension: " + extension);                     
        ArrayList<String> lista = new ArrayList(Arrays.asList(ext));        
        if(lista.contains(extension)){
            log.debug("isExtensionValid - la extensión es válida");
            exito = true; 
        }
        else log.debug("isExtensionValid - la extensión no es válida");
        
        return exito;
    }   
        
     /**
     * Comprueba si el tamaño de un fichero no excede el límite establecido en el fichero
     * de configuración techserver.properties
     * @param file: Formfile
     * @param app: Aplicación para la que se busca la extensión, p ej: StrutsFileValidation.APP_REGISTRO
     * o StrutsFileValidation.APP_EXPEDIENTE
     * @return boolean
     */
    public static boolean isSizeValid(FormFile file,String app)
    {
        boolean exito = true;      
        log.debug("isSizeValid FormFile,app: " + file + "<>" + app);
        int TAM_MAX_FILE = (Integer)limites.get(TAM_MAX_DOC_PREFFIX + app);
        log.debug("isExtensionValid - fileSize max: " + TAM_MAX_FILE);
        if(file!=null && file.getFileSize()>0)
        {               
            log.debug("isSizeValid - size form : " + file.getFileSize());                     
            if(file.getFileSize()>TAM_MAX_FILE){                
                exito = false;
            }
            else SIZE_FILE_VALIDATED = file.getFileSize();
        } 
        return exito;
    }
    
   /**
     * Comprueba si el tamaño de un determinado fichero no excede el límite establecido en el fichero
     * de configuración techserver.properties
     * @param file: File
     * @param app: Aplicación para la que se busca la extensión, p ej: StrutsFileValidation.APP_REGISTRO
     * o StrutsFileValidation.APP_EXPEDIENTE
     * @return boolean
     */
    public static boolean isSizeValid(File file,String app)
    {
        boolean exito = true;        
    
        log.debug("isSizeValid File,app: " + file + "<>" + app);
        int TAM_MAX_FILE = (Integer)limites.get(TAM_MAX_DOC_PREFFIX + app);
        log.debug("isSizeValid - Longitud maxima ficheros permitida: " + TAM_MAX_FILE);
        log.debug("isSizeValid: path fichero: " + file.getPath());
        
        if(file!=null && file.isFile() && file.length()>TAM_MAX_FILE){            
            exito = false;
        } 
        else SIZE_FILE_VALIDATED = file.length();
        
        
        return exito;
    }            
}