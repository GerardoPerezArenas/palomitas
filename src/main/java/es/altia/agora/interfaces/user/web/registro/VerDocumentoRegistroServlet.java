package es.altia.agora.interfaces.user.web.registro;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.OrganizacionesDAO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Vector;
import es.altia.util.cache.CacheDatosFactoria;

import es.altia.common.service.config.*;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.registro.justificante.util.FileOperations;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.exception.gestionmensaje.GestionMensajeErrorDokusi;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;


public class VerDocumentoRegistroServlet extends HttpServlet
{
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");

    protected static Log log =
            LogFactory.getLog(VerDocumentoRegistroServlet.class.getName());

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }
  
    
  
   public void defaultAction(HttpServletRequest req,HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession(false);
        UsuarioValueObject usuario  = (UsuarioValueObject) session.getAttribute("usuario");            
		AlmacenDocumentoTramitacionException exceptionDokusiGestionMensajeError=null; 
        String indice   = req.getParameter("codigo");
        String nombre   = req.getParameter("nombre");
        boolean modoAttachment = false;
        
        if (StringUtils.equalsIgnoreCase(req.getParameter("attachment"), "true")) {
            modoAttachment = true;
            
        }
      
        if(indice!=null && !"".equals(indice)){
            MantAnotacionRegistroForm mantARForm = (MantAnotacionRegistroForm)session.getAttribute("MantAnotacionRegistroForm");
            Vector docs = mantARForm.getListaDocsAsignados();
        
            RegistroValueObject doc = (RegistroValueObject)docs.elementAt(Integer.parseInt(indice));
            byte[] fichero = null;
            String tipo = null;
            String nombreFichero = null;
            
            if(doc!=null){
                if(doc.getEstadoDocumentoRegistro()==ConstantesDatos.ESTADO_DOCUMENTO_NUEVO && doc.getRutaDocumentoRegistroDisco()==null && doc.getDoc()!=null){
                    String pathFile="";
                    // Se mueve el fichero  a una determinada ruta en disco para permitir la visualizaci?n                     
                    byte[] contenido = doc.getDoc();
                    String nombreDoc = doc.getNombreDoc();

                    boolean continuar = false;
                    String path = null;
                    try{

                        ResourceBundle config  = ResourceBundle.getBundle("documentos");

                        path = config.getString("RUTA_DISCO_DOCUMENTOS");

                        continuar = true;

                    }catch(Exception e){

                        log.error("Error al recuperar la propiedad RUTA_DISCO_DOCUMENTOS en documentos.properties: " + e.getMessage());

                        e.printStackTrace();

                    }

                    if(continuar){

                        try{
                            
                            String pathDir = path + File.separator + ConstantesDatos.SUBCARPETA_DOCUMENTOS_REGISTRO;                            
                            
                            if(!FileOperations.createDirectory(pathDir)){                              

                                pathFile=null;

                            }else{                            

                                // Se crea el subdirectorio dentro del anterior 

                                String pathDirSessionId = pathDir + File.separator+session.getId();

                                if(!FileOperations.createDirectory(pathDirSessionId)){

                                  pathFile=null;  

                                }else{
                                
                                    pathFile = pathDirSessionId + File.separator + nombreDoc;

                                    FileOperations.writeFile(pathFile, contenido);

         
                                }

                            }

                        }catch(Exception e){

                            log.error("Error al copiar contenido del fichero adjuntado al directorio que los almacena temporalmente: " + e.getMessage());

                            e.printStackTrace();

                            

                        }

                    }

                    doc.setRutaDocumentoRegistroDisco(pathFile);

                }

               
                if(doc.getEstadoDocumentoRegistro()== ConstantesDatos.ESTADO_DOCUMENTO_NUEVO && doc.getRutaDocumentoRegistroDisco()!=null){
                    // No se ha confirmado todavía la grabación del documento en el repositorio de documentación, 
                    // que bien puede ser la base de datos o algún gestor documental, por tanto, para poder 
                    // visualizar su contenido habrá que leer el fichero del disco
                    
                    // Se recupera la ruta del fichero en el disco del servidor
                    String pathFile = doc.getRutaDocumentoRegistroDisco();
                    
                    try{
                        File f = new File(pathFile);
                        fichero = FileOperations.readFile(f);
                        
                        tipo = doc.getTipoDoc();
                        nombreFichero = doc.getNombreDoc() + "." + MimeTypes.guessExtensionFromMimeType(tipo);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    
                    
                }else
                if(doc.getEstadoDocumentoRegistro()==ConstantesDatos.ESTADO_DOCUMENTO_GRABADO){
                     
                    // Si el documento ya ha sido grabado con anterioridad, entonces habrá que invocar al 
                    // plugin correspondiente para descargar su contenido
                    
                    String[] params = usuario.getParamsCon();                   
                    AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassRegistro(Integer.toString(usuario.getOrgCod()));
                    int codOrganizacion = usuario.getOrgCod();
                    int codUnidadRegistro = doc.getUnidadOrgan();                            
                    
                    Hashtable<String,Object> datos = new Hashtable<String,Object>();
                    datos.put("identDepart",new Integer(doc.getDptoUsuarioQRegistra()));
                    datos.put("unidadOrgan",new Integer(doc.getUnidadOrgan()));
                    datos.put("anoReg",new Integer(doc.getAnoReg()));
                    datos.put("numReg",new Long(doc.getNumReg()));
                    datos.put("tipoReg",doc.getTipoReg());
                    datos.put("nombreDocumento",doc.getNombreDoc());            
                    datos.put("documentoRegistro",new Boolean(true));
                    datos.put("params",params);

                    int tipoDocumento = -1;
                    if(!almacen.isPluginGestor())
                        tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                    else{
                       tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                        datos.put("codMunicipio",Integer.toString(codOrganizacion));
                        datos.put("tipoMime",doc.getTipoDoc());
                        datos.put("extension", MimeTypes.guessExtensionFromMimeType(doc.getTipoDoc()));

                        if(almacen.isPluginGestor()){
                            //  Si se trata de un plugin de un gestor documental, se pasa la información
                            // extra necesaria                                    
                            ResourceBundle config = ResourceBundle.getBundle("documentos");
                            /**
                            String tipoPlugin     = config.getString(ConstantesDatos.PROPIEDAD_PLUGIN_ALMACENAMIENTO);
                            String nombreGestor   = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + tipoPlugin + ConstantesDatos.SUFIJO_PLUGIN_NOMBRE_GESTOR);
                            String carpetaRaiz    = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + nombreGestor + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                            */
                            String carpetaRaiz  = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ); 
                            
                            Connection con = null;
                            String descripcionOrganizacion = null;
                            String descripcionUnidadRegistro = null;
                            AdaptadorSQLBD adapt = null;
                            try{
                                adapt = new AdaptadorSQLBD(params);
                                con = adapt.getConnection();
                                
                                descripcionOrganizacion   = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(codOrganizacion, con);                            
                                //descripcionUnidadRegistro = UORsDAO.getInstance().getDescripcionUOR(codOrganizacion,con);
                               UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],String.valueOf(codUnidadRegistro));	
                                if (uorDTO!=null)	
                                    descripcionUnidadRegistro = uorDTO.getUor_nom();
                                
                                
                            }catch(BDException e){
                                log.error("Error al recuperar una conexión a la BBDD: " + e.getMessage());
                            }finally{
                                try{
                                    adapt.devolverConexion(con);
                                }catch(BDException e){
                                    log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
                                }
                            }
                            
                            ArrayList<String> listaCarpetas = new ArrayList<String>();
                            listaCarpetas.add(carpetaRaiz);
                            listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + descripcionOrganizacion);
                            listaCarpetas.add(codUnidadRegistro + ConstantesDatos.GUION + descripcionUnidadRegistro);

                            if(doc.getTipoReg().equalsIgnoreCase("E"))
                                listaCarpetas.add(ConstantesDatos.DESCRIPCION_ENTRADAS_REGISTRO);
                            else
                            if(doc.getTipoReg().equalsIgnoreCase("S"))
                                listaCarpetas.add(ConstantesDatos.DESCRIPCION_SALIDAS_REGISTRO);    

                            listaCarpetas.add(doc.getAnoReg() + ConstantesDatos.GUION + doc.getNumReg());
                            datos.put("listaCarpetas",listaCarpetas);
                        }                       
                    }

                    try{
                        Documento documento = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                        documento = almacen.getDocumentoRegistro(documento);

                        if(documento!=null && documento.getFichero()!=null && documento.getFichero().length>0){

                            fichero = documento.getFichero();
                            tipo = documento.getTipoMimeContenido();
                            nombreFichero = documento.getNombreDocumento() + "." + MimeTypes.guessExtensionFromMimeType(tipo);
                           
                        }

                    }catch(AlmacenDocumentoTramitacionException e){
						e.printStackTrace();
                        log.error("Error al recuperar documento : ", e);
                        log.error("Error al recuperar documento : Codigo Error recibido flexia(Regitrado en BBDD ERRDIGIT) y mensaje :" + e.getCodigo() + " - " + e.getMessage());
                        exceptionDokusiGestionMensajeError=e;
                    }
                
               }
                
               
                // Se devuelve el contenido del fichero a través del navegador
                if(fichero != null && tipo!=null && nombreFichero!=null){
                    BufferedOutputStream bos = null;
                    try{
                        res.setContentType(tipo);
                        if (modoAttachment) {
                            res.setHeader("Content-Disposition", "attachment; filename=" + nombreFichero); 
                        } else {
                        res.setHeader("Content-Disposition", "inline; filename=" + nombreFichero); 
                        }
                        ServletOutputStream out = res.getOutputStream();
                        res.setContentLength(fichero.length);
                        bos = new BufferedOutputStream(out);
                        bos.write(fichero, 0, fichero.length);
                        bos.flush();
                       
                    }catch(Exception e){
                        if(log.isDebugEnabled()) log.debug("Excepcion en catch de VerDocumentoRegistroServlet.defaultAction()");
                        throw e;
                    }finally {
                        if(bos != null) bos.close();
                    }
               } else {
                   log.error("FICHERO NULO EN VerDocumentoRegistroServlet.defaultAction");
                    try {
                        log.error(this.getClass()+" - Preparamos el js para mostrar mensaje error Dokusi en Servelet");
                        //ResourceBundle configDocumentosDokusi = ResourceBundle.getBundle("MELANBIDE_DOKUSI");
                        UsuarioValueObject usu = (UsuarioValueObject) session.getAttribute("usuario");
                        Integer idioma = 1;
                        if (usu != null) {
                            idioma = usu.getIdioma();
                        }
                        GestionMensajeErrorDokusi gestionMensajeErrorDokusi = new GestionMensajeErrorDokusi();
                        String textoMensaje = gestionMensajeErrorDokusi.preparaTextoHtmlContenidoMensaje(exceptionDokusiGestionMensajeError,idioma);
                        PrintWriter out = res.getWriter();
                        out.println(textoMensaje);
                    } catch (Exception e) {
                        log.error(this.getClass()+" - Error al mostrar mensaje error Dokusi en Servelet - " + e.getMessage());
                    }
               }     
            }
        }     
 
  }
  
  

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
      try     {
          if (log.isInfoEnabled()) log.info("VerDocumentoRegistroServlet.doGet()");
          defaultAction(req, res);
    } catch (Exception e) {
        if (log.isErrorEnabled()) log.error("ERROR doGet:" + e);
        e.printStackTrace();
    }
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
    try {
        defaultAction(req, res);
    } catch (Exception e) {
        if (log.isErrorEnabled()) log.error("ERROR doGet:" + e);
        e.printStackTrace();
    }
  }
  
  /**
   * Devuelve la extension de fichero correspondiente al tipo MIME pasado.
   */
  private String getExtension(String tipoMIME) {
      if (tipoMIME.equals("application/msword")) return ".doc";
      if (tipoMIME.equals("application/pdf"))    return ".pdf";
      if (tipoMIME.equals("image/jpeg")) return ".jpg";
      if (tipoMIME.equals("image/pjpeg")) return ".jpg";
      if (tipoMIME.equals("image/gif")) return ".gif";       
      if (tipoMIME.equals("image/png")) return ".png";
      if (tipoMIME.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) return ".docx";                             
      if (tipoMIME.equals("text/plain")) return ".txt";
      return "";        
  }
}
