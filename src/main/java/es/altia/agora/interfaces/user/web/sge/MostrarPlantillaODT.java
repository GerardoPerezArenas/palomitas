package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.administracion.mantenimiento.persistence.OrganizacionesManager;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.sge.persistence.DocumentosExpedienteManager;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.text.SimpleDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.editor.mantenimiento.persistence.DocumentosAplicacionManager;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.interfaces.user.web.editor.mantenimiento.DocumentosAplicacionForm;
import static es.altia.agora.interfaces.user.web.sge.DocumentosExpedienteAction.conf;
import static es.altia.agora.interfaces.user.web.sge.VerDocumentoServlet.log;
import es.altia.common.exception.TechnicalException;
import es.altia.util.documentos.DocumentOperations;

import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateException;
import net.sf.jooreports.templates.DocumentTemplateFactory;
import org.apache.commons.io.FileUtils;

public class MostrarPlantillaODT extends HttpServlet {
  public static Log m_log;
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");
    protected static Config m_Documentos = ConfigServiceHelper.getConfig("documentos");


  //Process the HTTP Get request
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String uri = request.getRequestURI();      
      m_log = LogFactory.getLog(this.getClass().getName());
      m_log.debug ("doGet mostrar documento="+uri);
      BufferedOutputStream bos= null;
      DocumentosAplicacionForm daForm=null;
      DocumentosExpedienteForm daForm2=null;
      Documento doc = null;
      byte[] file;
      
      HttpSession session = request.getSession();
      if (session == null) {
          log.error("SESION NULA EN VerDocumentoServlet.defaultAction");
          return;
      }
      log.debug("ID de la sesion : " + session.getId());        
      try{
          
         UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
         String[] params = usuario.getParamsCon();
     
        
        daForm =(DocumentosAplicacionForm)session.getAttribute("EditorDocumentosAplicacionForm");
        daForm2 =(DocumentosExpedienteForm)session.getAttribute("DocumentosExpedienteForm"); 
        
        
          GeneralValueObject gVO = new GeneralValueObject();
          gVO.setAtributo("codAplicacion", Integer.toString(usuario.getAppCod()));
          gVO.setAtributo("codMunicipio", Integer.toString(usuario.getOrgCod()));
          gVO.setAtributo("codProcedimiento", daForm2.getCodProcedimiento());
          gVO.setAtributo("ejercicio", daForm2.getEjercicio());
          gVO.setAtributo("numeroExpediente", daForm2.getNumeroExpediente());
          gVO.setAtributo("codTramite", daForm2.getCodTramite());
          gVO.setAtributo("ocurrenciaTramite", daForm2.getOcurrenciaTramite());
          gVO.setAtributo("codPlantilla", daForm2.getCodPlantilla());
          String lCodInteresados = daForm2.getListaCodInteresados();
          String lVersInteresados = daForm2.getListaVersInteresados();

          
          DocumentosAplicacionManager docAplMan = DocumentosAplicacionManager.getInstance();
          
          String codigoPlantilla = request.getParameter("codigoPlantilla");
          
           Map campos=new HashMap();

           campos=daForm2.getCampos();
           
           System.out.println("CAMPO CAMPO CAMPOS--> "+campos);
           
           GeneralValueObject gVO1 = new GeneralValueObject();
           gVO1.setAtributo("codDocumento",daForm2.getCodPlantilla());
           
           
          gVO1 = docAplMan.loadDocumento(gVO1, params);
          String nombreDocumento = (String) gVO1.getAtributo("nombre");
          String extension = m_Documentos.getString("EXTENSION_DOCUMENTOS_TRAMITACION");
          String ext = null;
          String mimeType = null;
          if(extension.equalsIgnoreCase(ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_WORD)){
              ext = ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_WORD_CON_PUNTO;
              mimeType = ConstantesDatos.TIPO_MIME_DOCUMENTO_WORD;
          }else if(extension.equalsIgnoreCase(ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OFFICE)){
              ext = ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OFFICE_CON_PUNTO;
              mimeType = ConstantesDatos.TIPO_MIME_DOCUMENTO_OFFICE;
          }else{
              ext = ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE_CON_PUNTO;
              mimeType = ConstantesDatos.TIPO_MIME_DOCUMENTO_OPENOFFICE;
          }

         byte[] fichero = null;
          if (gVO1 != null) {
              String PATH_DIR_DOC = null;
              try {
                  PATH_DIR_DOC = DocumentOperations.crearDocumentoDesdePlantilla(gVO, nombreDocumento, extension, (byte[]) gVO1.getAtributo("fichero"), campos, session.getId(), false);
                  if(PATH_DIR_DOC != null){
                   fichero = FileUtils.readFileToByteArray(new File(PATH_DIR_DOC.toString()));

                   if (fichero != null) {

                        // Se devuelve el contenido del fichero a traves del navegador
                        //TODO definir la extension de otro modo
                        response.setContentType(mimeType);
                        response.setHeader("Content-Disposition", "attachment; filename=\"" + nombreDocumento+ext+"\"");
                        response.setContentLength(fichero.length);
                        bos = new BufferedOutputStream(response.getOutputStream());
                        bos.write(fichero, 0, fichero.length);
                        bos.flush();
                        } else {
                        throw new TechnicalException("Error al obtener el fichero de cotejo");
                        }  
                  }
              } catch (Exception e) {
                  log.error("No se puede acceder a la ruta de descarga del documento");
              }
                  

                     
                   

          } else {
              log.error("NO SE HA PODIDO RECUPERAR EL CONTENIDO DEL DOCUMENTO");
              throw new ServletException("NO SE HA PODIDO RECUPERAR EL CONTENIDO DEL DOCUMENTO");
          }

        
      } catch (Exception e) {
          //e.printStackTrace();
           m_log.error("Error al mostrarDocumentoPlantillaGen:  "+e.getMessage());
          throw new ServletException("NO SE HA PODIDO RECUPERAR EL CONTENIDO DEL DOCUMENTO", e);
      } finally{
            if (bos != null) {
                bos.close();
            }
    }
  }
  
  
    private String rutaFicheroPlantillas (GeneralValueObject gVO, HttpSession session,String nombreDocumento)
   {
   
        String pathFicheroFinal = null;
        boolean directorio = false;
                String PATH_DIR = null;
                try{
                    ResourceBundle config = ResourceBundle.getBundle("documentos");
                    PATH_DIR = config.getString("RUTA_DISCO_DOCUMENTOS");
                    directorio = true;
                }catch(Exception e){
                   log.error("Eror al obtener la propiedad que indica la ruta en la que se almacenan los documentos en disco: " + e.getMessage());
                   
                 
                }
                
                if(directorio){
                
                    if(PATH_DIR!=null){
                        String subPathDir = PATH_DIR + File.separator + ConstantesDatos.SUBCARPETA_DOCUMENTOS_EXPEDIENTE;
                        boolean subpathdir = false;
                        
                        try{
                            File dir = new File(subPathDir);
                            if(!dir.exists()){
                                dir.mkdir();
                            }
                            subpathdir = true;
                        }catch(Exception e){
                          
                        }
                        
                        
                        if(subpathdir){                           
                            try{
                                // Se crea una carpeta con el sessionid
                                String sessionId = subPathDir + File.separator +  session.getId();
                                File sessionDir = new File(sessionId);
                                if(!sessionDir.exists()){
                                    sessionDir.mkdir();
                                }
                                
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
                                
                                
                                
                            }catch(Exception e){
                              
                            }                            
                        }
                   }
                }
                return pathFicheroFinal;
                
   }
      

  //Process the HTTP Post request
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request,response);
  }
}