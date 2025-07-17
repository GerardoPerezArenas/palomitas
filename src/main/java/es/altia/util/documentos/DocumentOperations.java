package es.altia.util.documentos;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.DocumentConversionException;
import es.altia.util.commons.JODReportsOperations;
import es.altia.util.exceptions.JODReportsException;
import java.io.File;
import java.util.Map;
import java.util.MissingResourceException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.commons.MimeTypes;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DocumentOperations {
    private static final Config config  = ConfigServiceHelper.getConfig("documentos");
    private static final Log log = LogFactory.getLog(DocumentOperations.class.getName());
    
    public static String rutaFicheroPlantillas (GeneralValueObject gVO, String idSession, String nombreDocumento) throws MissingResourceException, Exception{
        String pathFicheroFinal = null;
        boolean directorio = false;
        String PATH_DIR = null;
        try{
            PATH_DIR = config.getString("RUTA_DISCO_DOCUMENTOS");
            directorio = true;
        }catch(MissingResourceException mre){
            log.error("Error al obtener la propiedad que indica la ruta en la que se almacenan los documentos en disco: " + mre.getMessage());
           throw mre;
        }
                
        if(directorio){
            String codAplicacion = (String) gVO.getAtributo("codAplicacion");
            String subcarpeta = ConstantesDatos.SUBCARPETA_DOCUMENTOS_PLANTILLA;
            if(codAplicacion.equals("4")){
                subcarpeta = ConstantesDatos.SUBCARPETA_DOCUMENTOS_EXPEDIENTE;
            } else if(codAplicacion.equals("1")){
                subcarpeta = ConstantesDatos.SUBCARPETA_DOCUMENTOS_REGISTRO;
            }

            if(PATH_DIR!=null){
                String subPathDir = PATH_DIR + File.separator + subcarpeta;
                boolean subpathdir = false;

                try{
                    File dir = new File(subPathDir);
                    if(!dir.exists()){
                        dir.mkdir();
                    }
                    subpathdir = true;
                }catch(Exception e){
                    throw e;
                }


                if(subpathdir){                           
                    try{
                        // Se crea una carpeta con el sessionid
                        String sessionId = subPathDir + File.separator +  idSession;
                        File sessionDir = new File(sessionId);
                        if(!sessionDir.exists()){
                            sessionDir.mkdir();
                        }

                        if(codAplicacion.equals("4")){
                            String numExpediente = ((String)gVO.getAtributo("numeroExpediente")).replaceAll("/","-");
                            String dirExp = sessionId + File.separator + numExpediente;

                            File fDirExp = new File(dirExp);
                            if(!fDirExp.exists()){
                                fDirExp.mkdir();
                            }

                            String codTramite= (String)gVO.getAtributo("codTramite");
                            String ocurrenciaTramite=(String)gVO.getAtributo("ocurrenciaTramite");

                            if(codTramite==null || ocurrenciaTramite==null || "".equals(codTramite) || "".equals(ocurrenciaTramite) || "null".equals(codTramite) || "null".equals(ocurrenciaTramite))
                                pathFicheroFinal = dirExp + File.separator +  "_" + nombreDocumento;
                            else
                                pathFicheroFinal = dirExp + File.separator +  "_" + codTramite + "_" + ocurrenciaTramite + "_" + nombreDocumento;
                        } else if(codAplicacion.equals("1")){
                            String codOur = (String) gVO.getAtributo("codOur");
                            String codTip = (String) gVO.getAtributo("codTip");
                            String codDep = (String) gVO.getAtributo("codDep");
                            String depOurTipId = sessionId + File.separator + codDep + "_" + codOur + "_" + codTip;
                            
                            File dirPrevAnot = new File(depOurTipId);
                            if(!dirPrevAnot.exists()){
                                dirPrevAnot.mkdir();
                            }
                            
                            String ejercicio = (String) gVO.getAtributo("ejercicio");
                            String numero = (String) gVO.getAtributo("numero");
                            pathFicheroFinal = depOurTipId + File.separator + ejercicio + "_" + numero + "_" + nombreDocumento;
                            
                        }
                    }catch(Exception e){
                        throw e;
                    }                            
                }
           }
        }
        return pathFicheroFinal;
                
   }
    
    
    public static String crearDocumentoDesdePlantilla(GeneralValueObject gVO, String nombreDocumento, String extensionPlantilla, byte[] bytesFichero, Map datosPlantilla, String idSesion, boolean convertirPdf) throws JODReportsException, Exception  {
        log.debug("DocumentOperations.crearDocumentoDesdePlantilla");

        String PATH_DIR_PLANT = null;
        String PATH_DIR_DOC = null;
        String PATH_DIR_PDF = null;
        String PATH_DOC_DEVUELTO = null;

        try {
            PATH_DIR_PLANT = rutaFicheroPlantillas(gVO, idSesion, nombreDocumento + "_template." + extensionPlantilla);
            PATH_DIR_DOC = rutaFicheroPlantillas(gVO, idSesion, nombreDocumento + "_documento." + extensionPlantilla);

            FileUtils.writeByteArrayToFile(new File(PATH_DIR_PLANT), bytesFichero);

            JODReportsOperations jodReports = new JODReportsOperations(PATH_DIR_PLANT, PATH_DIR_DOC, datosPlantilla);
            if(!convertirPdf) {
                jodReports.createOdtDocument();
                PATH_DOC_DEVUELTO = jodReports.getPathDocument();
            } else {
                PATH_DIR_PDF = rutaFicheroPlantillas(gVO, idSesion, nombreDocumento + ".pdf");
                jodReports.setDocumentPdfPath(PATH_DIR_PDF);
                jodReports.createPdfDocument();
                PATH_DOC_DEVUELTO = jodReports.getDocumentPdfPath();
                
            }
        } catch (MissingResourceException mre) {
            log.error("No se puede acceder a la ruta de descarga del documento");
            throw new Exception(mre.getMessage());
        } catch (JODReportsException jre) {
            log.error("Ha ocurrido un error al generar el documento");
            throw jre;
        } catch (DocumentConversionException dce) {
            log.error("Ha ocurrido un error al generar el documento");
            throw new JODReportsException(dce.getMessage());
        } catch (Exception e) {
            log.error("Ha ocurrido un error al descargar la plantilla");
            throw e;
        } finally {
            //Borramos de disco el ficheros template .odt
            deleteFile(PATH_DIR_PLANT);
            if(convertirPdf) {
                //Borramos de disco el fichero documento .odt
                deleteFile(PATH_DIR_DOC);
            } 
            // combrobamos si la carpeta está vacia para eliminar las carpetas creadas en tal caso
            
        }
        return PATH_DOC_DEVUELTO;
   }
    
    public static void deleteFile(String path){
        File file = null;
        try{
            file = new File(path);
            if(file.isFile()){
                if(file.delete()){
                    log.debug("El fichero " + path + " se ha borrado correctamente.");
                }else{
                    log.error("El fichero " + path + " no se ha podibo borrar.");
                }
            } else throw new Exception("La ruta indicada no se corresponde con un fichero.");
    	}catch(Exception e){
            log.error("Error al intentar borrar el fichero " + path);
            e.printStackTrace();
    	}
    }
    
    /**
     * Determina el tipo mime a partir del tipo de editor de plantillas. Si no
     * Se encuentra con el editor de plantillas se intenta inferir del nombre.
     * 
     * @param editorPlantillas
     * @param nombreFichero
     * @return 
     */
    public static String determinarTipoMimePlantilla(String editorPlantillas, String nombreFichero) {
        String tipoMime = "";

        if (ConstantesDatos.OOFFICE.equalsIgnoreCase(editorPlantillas)) {
            tipoMime = ConstantesDatos.TIPO_MIME_DOCUMENTO_OPENOFFICE;
        } else if (ConstantesDatos.WORD.equalsIgnoreCase(editorPlantillas)) {
            tipoMime = ConstantesDatos.TIPO_MIME_DOC_TRAMITES;
        } else {
            tipoMime = MimeTypes.guessMimeTypeFromExtension(FilenameUtils.getExtension(nombreFichero));
        }
        
        return tipoMime;
    }
    
    /**
     * Determina la extension a partir del tipo de editor de plantillas
     * 
     * @param editorPlantillas
     * @return 
     */
    public static String determinarExtensionPlantilla(String editorPlantillas) {
        String extension = "";

        if (ConstantesDatos.OOFFICE.equalsIgnoreCase(editorPlantillas)) {
            extension = MimeTypes.FILEEXTENSION_ODT;
        } else if (ConstantesDatos.WORD.equalsIgnoreCase(editorPlantillas)) {
            extension = MimeTypes.FILEEXTENSION_DOC;
        }
        
        return extension;
    }
}
