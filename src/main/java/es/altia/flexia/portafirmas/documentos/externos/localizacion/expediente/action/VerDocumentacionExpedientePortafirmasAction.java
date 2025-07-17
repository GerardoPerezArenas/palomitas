package es.altia.flexia.portafirmas.documentos.externos.localizacion.expediente.action;

import es.altia.agora.business.administracion.mantenimiento.persistence.OrganizacionesManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.OrganizacionesDAO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.editor.mantenimiento.persistence.manual.DocumentosAplicacionDAO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.persistence.DatosSuplementariosManager;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.sge.persistence.DocumentosExpedienteManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.business.documentospresentados.persistence.ExpedienteDocPresentadoManager;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.documentos.DocumentOperations;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.exception.gestionmensaje.GestionMensajeErrorDokusi;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
  * Action usado para descargar los documentos  asociados a un expediente, desde el portafirmas.
  * Este action hereda de DispatchAction, para que puedar ser llamado en función del parámetro opcion, el método correspondiente.
  * 
  * Habrá un método para cada tipo de documento.
  * @author oscar
  */
public class VerDocumentacionExpedientePortafirmasAction extends DispatchAction{
    
    Logger log = Logger.getLogger(VerDocumentacionExpedientePortafirmasAction.class);
    private final String GUION ="-";
    private final String DOC ="DOC";

    
    
     /**
     * Descarga y visualiza un determinado documento de inicio de un determinado expediente
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return ActionForward que siempre estará a null
     * @throws Exception 
     */
    public ActionForward verDocumentoInicio(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
          
         String numExpediente = request.getParameter("numExpediente");
         String codigo = request.getParameter("codigo");
         
         AlmacenDocumentoTramitacionException exceptionDokusiGestionMensajeError = null;
         
         HttpSession session = request.getSession();
         UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
         String[] params = usuario.getParamsCon();
         
         String codMunicipio       = null;
         String ejercicio          = null;
         String numeroExpediente   = null;
         String codDocumento       = null;
         String codProcedimiento   = null;
         
         if(codigo!=null && codigo.length()>0 && numExpediente!=null && numExpediente.length()>0){
             
             String[] parametros = codigo.split("-");
             if(parametros!=null && parametros.length==4){
                codDocumento = parametros[0];
                codMunicipio = parametros[1];
                ejercicio = parametros[2];
                numeroExpediente = parametros[3];                
                codProcedimiento = numeroExpediente.split("/")[1];
                
                Hashtable<String,Object> datos = new Hashtable<String,Object>();
                datos.put("codDocumento",codDocumento);
                datos.put("codMunicipio",codMunicipio);
                datos.put("ejercicio",ejercicio);
                datos.put("numeroExpediente",numeroExpediente);
                datos.put("params",params);
                datos.put("codProcedimiento",codProcedimiento);
                
                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,codProcedimiento);
                Documento doc = null;
                int tipoDocumento = -1;
                if(!almacen.isPluginGestor())
                      tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                 else{
                   ResourceBundle bundle = ResourceBundle.getBundle("documentos");            
                   String carpetaRaiz  = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                   
                   
                   String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);        
                   datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);
                   datos.put("origen",almacen.getNombreServicio());

                   TraductorAplicacionBean traductor = new TraductorAplicacionBean();
                   traductor.setApl_cod(4);
                   traductor.setIdi_cod(usuario.getIdioma());

                   /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN LOS DOCUMENTOS EN EL GESTOR DOCUMENTAL **/
                   ArrayList<String> listaCarpetas = new ArrayList<String>();
                   listaCarpetas.add(carpetaRaiz);
                   listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + usuario.getOrg());
                   listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                   listaCarpetas.add(numeroExpediente.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));
                   listaCarpetas.add(traductor.getDescripcion("carpetaDocumentosPresentados"));
                   datos.put("listaCarpetas",listaCarpetas);
                   tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;

                }
                
                
                byte[] fichero = null;
                String nombreFichero = null;
                String tipoContenido  = null;

                 try{

                       doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                       if(almacen.isPluginGestor()){

                           // Se recupera la información del documento de la base de datos para extraer la extensión del fichero y componer el nombre del fichero
                           // para poder obtener su contenido del gestor
                           doc = ExpedienteDocPresentadoManager.getInstance().getDocumentoPresentado(doc);
                           log.debug(" ********* nombredocumento: " + doc.getNombreDocumento());
                           log.debug(" ********* extensiondocumento: " + doc.getExtension());
                           DocumentoGestor docGestor = (DocumentoGestor)doc;
                           docGestor.setNombreFicheroCompleto(codDocumento + ConstantesDatos.GUION + doc.getNombreDocumento() + ConstantesDatos.DOT + doc.getExtension());
                           doc = almacen.getDocumentoPresentado(docGestor);
                       }else
                           doc = almacen.getDocumentoPresentado(doc);

                       fichero             = doc.getFichero();
                       nombreFichero = doc.getNombreDocumento() + "." + doc.getExtension();
                       tipoContenido  = doc.getTipoMimeContenido();

                       log.debug(" >>>>>>>>>>>>>>>>>>>>>>  fichero longitud: " + fichero.length);
                       log.debug(" >>>>>>>>>>>>>>>>>>>>>>  fichero nombreFichero: " + nombreFichero);
                       log.debug(" >>>>>>>>>>>>>>>>>>>>>>  fichero tipoContenido: " + tipoContenido);

                 }catch(AlmacenDocumentoTramitacionException e){
					 e.printStackTrace();
                     log.error("Error al recuperar documento : ", e);
                     log.error("Error al recuperar documento : Codigo Error recibido flexia(Regitrado en BBDD ERRDIGIT) y mensaje :" + e.getCodigo() + " - " + e.getMessage());
                     exceptionDokusiGestionMensajeError = e;
                     fichero             = null;
                     nombreFichero = null;
                     tipoContenido   = null;
                 }


                 
                if(fichero != null)
                {
                      BufferedOutputStream bos = null;
                      try{

                          response.setContentType(tipoContenido);
                          response.setHeader("Content-Disposition", "inline; filename=" + nombreFichero);
                          response.setHeader("Content-Transfer-Encoding", "binary");

                          ServletOutputStream out = response.getOutputStream();
                          response.setContentLength(fichero.length);
                          bos = new BufferedOutputStream(out);
                          bos.write(fichero, 0, fichero.length);
                          bos.flush();
                          bos.close();


                      }catch(Exception e){
                          if(log.isDebugEnabled()) log.debug("Excepcion en catch de VerDocumentoPresentadoServlet.defaultAction()");
                          throw e;
                      }finally {
                          if(bos != null) bos.close();
                      }
                } else {
                    log.warn("FICHERO NULO EN VerDocumentoPresentadoServlet.defaultAction");
                    try {
                        log.error(this.getClass() + " - Preparamos el js para mostrar mensaje error Dokusi en Servelet");
                        //ResourceBundle configDocumentosDokusi = ResourceBundle.getBundle("MELANBIDE_DOKUSI");
                        UsuarioValueObject usu = (UsuarioValueObject) session.getAttribute("usuario");
                        Integer idioma = 1;
                        if (usu != null) {
                            idioma = usu.getIdioma();
                        }
                        GestionMensajeErrorDokusi gestionMensajeErrorDokusi = new GestionMensajeErrorDokusi();
                        String textoMensaje = gestionMensajeErrorDokusi.preparaTextoHtmlContenidoMensaje(exceptionDokusiGestionMensajeError, idioma);
                        PrintWriter out = response.getWriter();
                        out.println(textoMensaje);
                    } catch (Exception e) {
                        log.error(this.getClass() + " - Error al mostrar mensaje error Dokusi en Servelet - " + e.getMessage());
                    }
                }
                        
             }
         }
        return null;
    }
    
    
    
   /**
     * Descarga y visualiza un determinado campo suplementario de tipo fichero de un determinado expediente
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return ActionForward que siempre estará a null
     * @throws Exception 
     */    
     public ActionForward verFicheroCampoSuplementarioExpediente(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        
        String numExpediente = request.getParameter("numExpediente");
        String codigo = request.getParameter("codigo");
        byte[] fichero = null;
        
        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
	String[] params = usuario.getParamsCon();
        
        if(codigo!=null && codigo.length()>0 && numExpediente!=null && numExpediente.length()>0){
            
            String[] parametros = numExpediente.split("/");            
            String codOrganizacion = Integer.toString(usuario.getOrgCod());
            String ejercicio    = parametros[0];
            String codProcedimiento = parametros[1];
                        
            Hashtable<String,Object> datos = new Hashtable<String,Object>();
            datos.put("numeroDocumento",codigo);              
            datos.put("codTipoDato",codigo);
            datos.put("codMunicipio",codOrganizacion);
            datos.put("ejercicio",ejercicio);
            datos.put("numeroExpediente",numExpediente);              
            datos.put("params",params);

            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrganizacion).getImplClassPluginProcedimiento(codOrganizacion,codProcedimiento);            
            Documento doc = null;
            String nombreFichero = null;
            String tipoContenido = null;
            int tipoDocumento = -1;
            if(!almacen.isPluginGestor())
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
            else{
                   
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                
                //  Si se trata de un plugin de un gestor documental, se pasa la información
                // extra necesaria                                    
                ResourceBundle config = ResourceBundle.getBundle("documentos");          
                String carpetaRaiz  = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

                GeneralValueObject gVO = DatosSuplementariosManager.getInstance().getInfoCampoSuplementarioFicheroExpediente(Integer.parseInt(codOrganizacion),codigo,numExpediente,false,params);              
                String descripcionOrganizacion = (String)gVO.getAtributo("descOrganizacion");
                String descProcedimiento = (String)gVO.getAtributo("descProcedimiento");
                nombreFichero = (String)gVO.getAtributo("nombreFichero");
                tipoContenido = (String)gVO.getAtributo("tipoMime");

                datos.put("tipoMime",tipoContenido);                    
                datos.put("nombreDocumento", codigo + "_" + nombreFichero);
                datos.put("nombreFicheroCompleto", codigo + "_" + nombreFichero);

                ArrayList<String> listaCarpetas = new ArrayList<String>();
                listaCarpetas.add(carpetaRaiz);
                listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + descripcionOrganizacion);
                listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + descProcedimiento);
                listaCarpetas.add(numExpediente.replaceAll("/","-"));
                datos.put("listaCarpetas",listaCarpetas);
            }
              
            
            try{
                doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                doc.setExpHistorico(false);
		doc = almacen.getDocumentoDatosSuplementarios(doc);

		fichero = doc.getFichero();
		nombreFichero = doc.getNombreDocumento() ;
		tipoContenido  = doc.getTipoMimeContenido();

		if (log.isDebugEnabled()) {
                    log.debug("Logitud del fichero: " + fichero.length);
		    log.debug("Nombre del fichero: " + nombreFichero);
		    log.debug("Tipo contenido fichero: " + tipoContenido);
                }
                
                
                if(fichero != null){
                    BufferedOutputStream bos = null;
                    try{                        

                        response.setContentType(tipoContenido);
                        response.setHeader("Content-Disposition", "inline; filename=" + nombreFichero);
                        response.setHeader("Content-Transfer-Encoding", "binary");            

                        ServletOutputStream out = response.getOutputStream();
                        response.setContentLength(fichero.length);
                        bos = new BufferedOutputStream(out);
                        bos.write(fichero, 0, fichero.length);
                        bos.flush();
                        bos.close();


                    }catch(Exception e){
                        if(log.isDebugEnabled()) log.debug("Excepcion en catch de VerDocumentoServlet.defaultAction()");
                        throw e;
                    }finally {
                        if(bos != null) bos.close();
                    } 
                }
                

            }catch(AlmacenDocumentoTramitacionException e){
                    e.printStackTrace();
                    fichero       = null;
                    nombreFichero = null;
                    tipoContenido = null;
                    log.error("Error al recuperar documento : Codigo Error recibido flexia(Regitrado en BBDD ERRDIGIT) y mensaje :" + e.getCodigo() + " - " + e.getMessage());
                    try {
                        log.error(this.getClass() + " - Preparamos el js para mostrar mensaje error Dokusi en Servelet");
                        //ResourceBundle configDocumentosDokusi = ResourceBundle.getBundle("MELANBIDE_DOKUSI");
                        UsuarioValueObject usu = (UsuarioValueObject) session.getAttribute("usuario");
                        Integer idioma = 1;
                        if (usu != null) {
                            idioma = usu.getIdioma();
                        }
                        GestionMensajeErrorDokusi gestionMensajeErrorDokusi = new GestionMensajeErrorDokusi();
                        String textoMensaje = gestionMensajeErrorDokusi.preparaTextoHtmlContenidoMensaje(e, idioma);
                        PrintWriter out = response.getWriter();
                        out.println(textoMensaje);
                    } catch (Exception ex) {
                        log.error(this.getClass() + " - Error al mostrar mensaje error Dokusi en Servelet - " + e.getMessage());
                    }
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }

        return null;
    }
    
    
    
            
   /**
     * Descarga y visualiza un determinado documento externo (Otros documentos) asociado a un determinado expediente
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return ActionForward que siempre estará a null
     * @throws Exception 
     */
    public ActionForward verDocumentoExternoExpediente(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();
        try{
            String numExpediente = request.getParameter("numExpediente");
            String codigo = request.getParameter("codigo");
            String extension = request.getParameter("extension");            
            String nombreDocumento = request.getParameter("nombreDocumento");
            
            AlmacenDocumentoTramitacionException exceptionDokusiGestionMensajeError = null;
                        
            if(numExpediente!=null && !"".equals(numExpediente) && codigo!=null && !"".equals(codigo) && extension!=null && !"".equals(extension)
                    && nombreDocumento!=null && !"".equals(nombreDocumento)){
                
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");


                String codOrganizacion = Integer.toString(usuario.getOrgCod());
                String[] params = usuario.getParamsCon();
                String[] datosExp = numExpediente.split("/");
                String ejercicio = datosExp[0];
                
                Hashtable<String,Object> datos = new Hashtable<String,Object>();
                datos.put("codMunicipio",codOrganizacion);
                datos.put("ejercicio",ejercicio);
                datos.put("numeroExpediente",numExpediente);              
                datos.put("numeroDocumento",codigo);
                datos.put("params",params);
                datos.put("nombreDocumento",nombreDocumento);
                datos.put("extension",extension);                
                datos.put("tipoMime","application/octet-stream");

                byte[] fichero = null;
                String nombreFichero = null;
                String tipoContenido  = null;
                String codProcedimiento = numExpediente.split("[/]")[1];
                                
                FichaExpedienteForm fichaForm = (FichaExpedienteForm)session.getAttribute("FichaExpedienteForm");
                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrganizacion).getImplClassPluginProcedimiento(codOrganizacion,codProcedimiento);            
                Documento doc = null;
                int tipoDocumento = -1;
                if(!almacen.isPluginGestor())
                     tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
                else{                      
                      String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);
                      ResourceBundle bundle = ResourceBundle.getBundle("documentos");                 
                      String carpetaRaiz  = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                      
                      datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);
                      TraductorAplicacionBean traductor = new TraductorAplicacionBean();
                      traductor.setApl_cod(4);
                      traductor.setIdi_cod(usuario.getIdioma());

                      //SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN LOS DOCUMENTOS EN EL GESTOR DOCUMENTAL 
                      ArrayList<String> listaCarpetas = new ArrayList<String>();
                      listaCarpetas.add(carpetaRaiz);
                      listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + usuario.getOrg());
                      listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                      listaCarpetas.add(numExpediente.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));                    
                      datos.put("listaCarpetas",listaCarpetas);

                      tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                }// else

                try{
                      doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                      doc.setExpHistorico(fichaForm.isExpHistorico());
                      doc = almacen.getDocumentoExterno(doc);
                      fichero             = doc.getFichero();
                      nombreFichero = doc.getNombreDocumento() + "." + doc.getExtension();
                      tipoContenido  = doc.getTipoMimeContenido();

                }catch(AlmacenDocumentoTramitacionException e){
					e.printStackTrace();
                    log.error("Error al recuperar documento : ", e);
                    log.error("Error al recuperar documento : Codigo Error recibido flexia(Regitrado en BBDD ERRDIGIT) y mensaje :" + e.getCodigo() + " - " + e.getMessage());
                    exceptionDokusiGestionMensajeError = e;
                    fichero             = null;
                    nombreFichero = null;
                    tipoContenido   = null;
                    log.error(this.getClass().getName() + " ERROR AL RECUPERAR EL CONTENIDO DE UN DOCUMENTO DE TRAMITACIÓN: " + e.getMessage());
                }
                
                
               
                if(fichero != null){
                    BufferedOutputStream bos = null;
                    try{                        

                        response.setContentType(tipoContenido);
                        response.setHeader("Content-Disposition", "inline; filename=" + nombreFichero);
                        response.setHeader("Content-Transfer-Encoding", "binary");            

                        ServletOutputStream out = response.getOutputStream();
                        response.setContentLength(fichero.length);
                        bos = new BufferedOutputStream(out);
                        bos.write(fichero, 0, fichero.length);
                        bos.flush();
                        bos.close();


                    }catch(Exception e){
                        log.error(this.getClass().getName() + " -- ERROR al mostrar el documento externo del expediente: " + e.getMessage());
                        throw e;
                    }finally {
                        if(bos != null) bos.close();
                    } 
                }
            }else
                log.error(this.getClass().getName() + " ERROR: Se está intentando descargar un documento de inicio de expediente pero se desconoce cual es el número del expediente y/o código del documento");
                try {
                    log.error(this.getClass() + " - Preparamos el js para mostrar mensaje error Dokusi en Servelet");
                    //ResourceBundle configDocumentosDokusi = ResourceBundle.getBundle("MELANBIDE_DOKUSI");
                    UsuarioValueObject usu = (UsuarioValueObject) session.getAttribute("usuario");
                    Integer idioma = 1;
                    if (usu != null) {
                        idioma = usu.getIdioma();
                    }
                    GestionMensajeErrorDokusi gestionMensajeErrorDokusi = new GestionMensajeErrorDokusi();
                    String textoMensaje = gestionMensajeErrorDokusi.preparaTextoHtmlContenidoMensaje(exceptionDokusiGestionMensajeError, idioma);
                    PrintWriter out = response.getWriter();
                    out.println(textoMensaje);
                } catch (Exception e) {
                    log.error(this.getClass() + " - Error al mostrar mensaje error Dokusi en Servelet - " + e.getMessage());
                }
        }catch(Exception e){            
            log.error(this.getClass().getName() + " ERROR: " + e.getMessage());
            e.printStackTrace();
        }        

        return null;
    }  
    
       
              
   /**
     * Descarga y visualiza un determinado documento de tramitación, asociado a una ocurrencia de trámite de un determinado
     * expediente
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return ActionForward que siempre estará a null
     * @throws Exception 
     */
    public ActionForward verDocumentoTramitacion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();
        try{
            String numExpediente = request.getParameter("numExpediente");
            String codigo = request.getParameter("codigo");            
            String nombreDocumento = request.getParameter("nombreDocumento");            
            
                 
            if(numExpediente!=null && !"".equals(numExpediente) && codigo!=null && !"".equals(codigo) 
                    && nombreDocumento!=null && !"".equals(nombreDocumento)){
                
                
                // Se recupera el usuario de la sesión
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");

                String[] params = usuario.getParamsCon();
                byte[] fichero = null;                
                String extension = null;
                String codTramite = null;
                String ocurrenciaTramite = null;
                String tipoMime = "";
                ResourceBundle CONFIG_TECHNICAL = ResourceBundle.getBundle("common");
                
                String[] DATOS_DOCUMENTO = codigo.split(GUION);
                if(DATOS_DOCUMENTO!=null && DATOS_DOCUMENTO.length==6 && DATOS_DOCUMENTO[0].equals(DOC)){
                
                    // Código del trámite
                    codTramite        = DATOS_DOCUMENTO[3];
                    // Ocurrencia del trámite
                    ocurrenciaTramite = DATOS_DOCUMENTO[4];
                    // Código identificativo del documento
                    codigo            = DATOS_DOCUMENTO[5];
                    
                    String codMunicipio = Integer.toString(usuario.getOrgCod());
                    Hashtable<String,Object> datos = new Hashtable<String,Object>();
                    datos.put("codMunicipio",codMunicipio);
                    datos.put("numeroExpediente",numExpediente);
                    datos.put("codTramite",codTramite);
                    datos.put("ocurrenciaTramite",ocurrenciaTramite);
                    datos.put("numeroDocumento",codigo);
                    datos.put("perteneceRelacion","false");
                    datos.put("params",params);
                    
                    String[] DATOS_EXP = numExpediente.split("/");
                    String ejercicio  = DATOS_EXP[0];
                    String codProcedimiento = DATOS_EXP[1];
                    
                    AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,codProcedimiento);            
                    Documento doc = null;
                    int tipoDocumento = -1;
                    
                    String editorTexto = "";
              
                    try{
                        editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTexto(Integer.parseInt(codMunicipio),
                        numExpediente,Integer.parseInt(codTramite),Integer.parseInt(ocurrenciaTramite),
                        Integer.parseInt(codigo),false,params);
                    }catch (NumberFormatException e)
                    {
                        log.error("No se puede obtener el editor de texto definido debido a una excepcion");
                    }
                    
                    tipoMime = DocumentOperations.determinarTipoMimePlantilla(
                            editorTexto, nombreDocumento);
                    extension = MimeTypes.guessExtensionFromMimeType(tipoMime);
                    
                    if(!almacen.isPluginGestor())
                        tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                    else{
                       
                        // Se obtiene el nombre del documento a mostrar porque se necesita para el caso de que se venga de firmar el documento
                        GeneralValueObject gVO = new GeneralValueObject();
                        gVO.setAtributo("codMunicipio",codMunicipio);
                        gVO.setAtributo("codProcedimiento",codProcedimiento);
                        gVO.setAtributo("ejercicio",ejercicio);
                        gVO.setAtributo("codTramite",codTramite);
                        gVO.setAtributo("ocurrenciaTramite",ocurrenciaTramite);
                        gVO.setAtributo("numeroExpediente",numExpediente);
                        gVO.setAtributo("numeroDocumento",codigo);
                        
                        String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(codMunicipio,codProcedimiento,codTramite, params);
                        String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);
                        String descripcionOrganizacion = OrganizacionesManager.getInstance().getDescripcionOrganizacion(codMunicipio, params);
                                               
                        datos.put("nombreDocumento",DocumentosExpedienteManager.getInstance().getNombreDocumentoGestor(gVO, params));
                        datos.put("codProcedimiento",codProcedimiento);
                        datos.put("nombreDocumento",nombreDocumento);
                        datos.put("extension",extension);              
                        datos.put("tipoMime",tipoMime);
                        datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);
                        datos.put("codigoVisibleTramite",codigoVisibleTramite);

                        /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
                        ResourceBundle bundle = ResourceBundle.getBundle("documentos");                   
                        String carpetaRaiz = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                        
                        ArrayList<String> listaCarpetas = new ArrayList<String>();
                        listaCarpetas.add(carpetaRaiz);
                        listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + descripcionOrganizacion);
                        listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                        listaCarpetas.add(((String)gVO.getAtributo("numeroExpediente")).replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));

                        datos.put("listaCarpetas",listaCarpetas);
                        /*** FIN  ***/

                        tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                    }

                    doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos,tipoDocumento);
                    fichero = almacen.getDocumento(doc);
                
                
                    /**********************************/                
                    if(fichero != null){
                        BufferedOutputStream bos = null;
                        try{                        

                            response.setContentType(tipoMime);
                            response.setHeader("Content-Disposition", "inline; filename=" + nombreDocumento + "." + extension);
                            response.setHeader("Content-Transfer-Encoding", "binary");            

                            ServletOutputStream out = response.getOutputStream();
                            response.setContentLength(fichero.length);
                            bos = new BufferedOutputStream(out);
                            bos.write(fichero, 0, fichero.length);
                            bos.flush();
                            bos.close();


                        }catch(Exception e){
                            log.error(this.getClass().getName() + " -- ERROR al mostrar el documento externo del expediente: " + e.getMessage());
                            throw e;
                        }finally {
                            if(bos != null) bos.close();
                        }                         
                    }
                }            
            }else
                log.error(this.getClass().getName() + " ERROR: Se está intentando descargar un documento de inicio de expediente pero se desconoce cual es el número del expediente y/o código del documento");
        }catch (AlmacenDocumentoTramitacionException e1){
            log.error("Error al recuperar documento : ", e1);
            log.error("Error al recuperar documento : Codigo Error recibido flexia(Regitrado en BBDD ERRDIGIT) y mensaje :" + e1.getCodigo() + " - " + e1.getMessage());
            try {
                log.error(this.getClass() + " - Preparamos el js para mostrar mensaje error Dokusi en Servelet");
                //ResourceBundle configDocumentosDokusi = ResourceBundle.getBundle("MELANBIDE_DOKUSI");
                UsuarioValueObject usu = (UsuarioValueObject) session.getAttribute("usuario");
                Integer idioma = 1;
                if (usu != null) {
                    idioma = usu.getIdioma();
                }
                GestionMensajeErrorDokusi gestionMensajeErrorDokusi = new GestionMensajeErrorDokusi();
                String textoMensaje = gestionMensajeErrorDokusi.preparaTextoHtmlContenidoMensaje(e1, idioma);
                PrintWriter out = response.getWriter();
                out.println(textoMensaje);
            } catch (Exception e) {
                log.error(this.getClass() + " - Error al mostrar mensaje error Dokusi en Servelet - " + e.getMessage());
            }
        }catch(Exception e){            
            log.error(this.getClass().getName() + " ERROR: " + e.getMessage());
            e.printStackTrace();
        }        

        return null;
    }       
    
    /**
     * Descarga y visualiza un determinado documento asociado a una determinada anotación de registro
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return ActionForward que siempre estará a null
     * @throws Exception 
     */
    public ActionForward verDocumentoAnotacionRegistro(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        String numExpediente = request.getParameter("numExpediente");
        String codigo = request.getParameter("codigo");
        
        
        String ejercicio = null;
        String numeroAnotacion = null;
        String tipoEntrada = null;
        String nombreDocumento = null;
        String codUor = null;
        String codDepartamento = null;
        String tipoMime = null;
        
        byte[] fichero = null;
        String tipoContenido = null;
        String nombreFichero = null;
          
        HttpSession session = request.getSession();
        
        if(codigo!=null && codigo.length()>0 && numExpediente!=null && numExpediente.length()>0){
                
            String[] parametros = codigo.split("-");            
            
            if(parametros!=null && parametros.length==7){
              
              ejercicio = parametros[0];
              numeroAnotacion = parametros[1];
              tipoEntrada = parametros[2];
              nombreDocumento = parametros[3];
              codDepartamento = parametros[4];
              codUor = parametros[5];              
              tipoMime = parametros[6];
              
              /*******/
              UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
              String[] params = usuario.getParamsCon();
              
              AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassRegistro(Integer.toString(usuario.getOrgCod()));
              Hashtable<String,Object> datos = new Hashtable<String,Object>();

              int codOrganizacion = usuario.getOrgCod();
              datos.put("identDepart",new Integer(codDepartamento));
              datos.put("unidadOrgan",new Integer(codUor));
              datos.put("anoReg",new Integer(ejercicio));
              datos.put("numReg",new Long(numeroAnotacion));
              datos.put("tipoReg",tipoEntrada);
              datos.put("nombreDocumento",nombreDocumento);            
              datos.put("documentoRegistro",new Boolean(true));
              datos.put("params",params);

              int tipoDocumento = -1;
              if(!almacen.isPluginGestor())
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
              else{
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                
                datos.put("codMunicipio",Integer.toString(codOrganizacion));
                datos.put("tipoMime",tipoMime);
                datos.put("extension", MimeTypes.guessExtensionFromMimeType(tipoMime));

                if(almacen.isPluginGestor()){
                    //  Si se trata de un plugin de un gestor documental, se pasa la información
                    // extra necesaria                                    
                    ResourceBundle config = ResourceBundle.getBundle("documentos");           
                    String carpetaRaiz = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                    
                    Connection con = null;
                    String descripcionOrganizacion = null;
                    String descripcionUnidadRegistro = null;
                    AdaptadorSQLBD adapt = null;
                    try{
                        adapt = new AdaptadorSQLBD(params);
                        con = adapt.getConnection();

                        descripcionOrganizacion   = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(codOrganizacion, con);
                        UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],codUor);
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
                    listaCarpetas.add(codUor + ConstantesDatos.GUION + descripcionUnidadRegistro);

                    if(tipoEntrada.equalsIgnoreCase("E"))
                        listaCarpetas.add(ConstantesDatos.DESCRIPCION_ENTRADAS_REGISTRO);
                    else
                    if(tipoEntrada.equalsIgnoreCase("S"))
                        listaCarpetas.add(ConstantesDatos.DESCRIPCION_SALIDAS_REGISTRO);    

                    listaCarpetas.add(ejercicio + ConstantesDatos.GUION + numeroAnotacion);
                    datos.put("listaCarpetas",listaCarpetas);
                 }                       
               }

                try{
                    Documento documento = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                    documento = almacen.getDocumentoRegistro(documento);

                    if(documento!=null && documento.getFichero()!=null && documento.getFichero().length>0){

                        fichero = documento.getFichero();
                        tipoContenido = documento.getTipoMimeContenido();
                        nombreFichero = documento.getNombreDocumento() + "." + MimeTypes.guessExtensionFromMimeType(tipoContenido);

                        
                        
                        if(fichero != null){
                            BufferedOutputStream bos = null;
                            try{                        

                                response.setContentType(tipoContenido);
                                response.setHeader("Content-Disposition", "inline; filename=" + nombreFichero);
                                response.setHeader("Content-Transfer-Encoding", "binary");            

                                ServletOutputStream out = response.getOutputStream();
                                response.setContentLength(fichero.length);
                                bos = new BufferedOutputStream(out);
                                bos.write(fichero, 0, fichero.length);
                                bos.flush();
                                bos.close();


                            }catch(Exception e){
                                if(log.isDebugEnabled()) log.debug("Excepcion en catch de VerDocumentoServlet.defaultAction()");
                                throw e;
                            }finally {
                                if(bos != null) bos.close();
                            } 
                        }
                    }

                }catch(AlmacenDocumentoTramitacionException e){
					e.printStackTrace();
                    log.error("Erorr al verDocumentoAnotacionRegistro : ", e);
                    log.error("Error al recuperar documento : Codigo Error recibido flexia(Regitrado en BBDD ERRDIGIT) y mensaje :" + e.getCodigo() + " - " + e.getMessage());
                    try {
                        log.error(this.getClass() + " - Preparamos el js para mostrar mensaje error Dokusi en Servelet");
                        //ResourceBundle configDocumentosDokusi = ResourceBundle.getBundle("MELANBIDE_DOKUSI");
                        UsuarioValueObject usu = (UsuarioValueObject) session.getAttribute("usuario");
                        Integer idioma = 1;
                        if (usu != null) {
                            idioma = usu.getIdioma();
                        }
                        GestionMensajeErrorDokusi gestionMensajeErrorDokusi = new GestionMensajeErrorDokusi();
                        String textoMensaje = gestionMensajeErrorDokusi.preparaTextoHtmlContenidoMensaje(e, idioma);
                        PrintWriter out = response.getWriter();
                        out.println(textoMensaje);
                    } catch (Exception ex) {
                        log.error(this.getClass() + " - Error al mostrar mensaje error Dokusi en Servelet - " + e.getMessage());
                    }
                }
                       
           }   
         }

        return null;
    }
    
    
    
    
    
    
    /**
     * Descarga y visualiza un determinado campo suplementario de tipo fichero de un determinado expediente y de un determinado trámite
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return ActionForward que siempre estará a null
     * @throws Exception 
     */
    public ActionForward verCampoFicheroTramite(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();
        try{
            String numExpediente = request.getParameter("numExpediente");
            String codigo = request.getParameter("codigo");
            
            byte[] fichero = null;
            String nombreFichero = null;
            String tipoContenido = null;
            
            if(numExpediente!=null && !"".equals(numExpediente) && codigo!=null && !"".equals(codigo)){
               
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");


                /*******************/  
                String codMunicipio = Integer.toString(usuario.getOrgCod());
                String numero = null;
                String codTramite = null;
                String ocurrenciaTramite = null;
                String codigoCampo = null;
                String ejercicio = null;
                String[] datosExp = codigo.split("-");
          
                if(datosExp!=null && datosExp.length==6){

                  codMunicipio = datosExp[1];
                  numero = datosExp[2];
                  codTramite = datosExp[3];
                  ocurrenciaTramite = datosExp[4];
                  codigoCampo = datosExp[5];

                  String[] datosExp2 = numero.split("/");
                  ejercicio = datosExp2[0];
                  String codProcedimiento = (numero.split("/"))[1];
                  
                  String[] params = usuario.getParamsCon();

                  Hashtable<String,Object> datos = new Hashtable<String,Object>();                                                  
                  datos.put("codTipoDato",codigoCampo);             
                  datos.put("codMunicipio",codMunicipio);
                  datos.put("ejercicio",ejercicio);
                  datos.put("numeroExpediente",numero);              
                  datos.put("params",params);
                  datos.put("codTramite",codTramite);
                  datos.put("ocurrenciaTramite",ocurrenciaTramite);

                  AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),codProcedimiento);
                    
                  Documento doc = null;
                  int tipoDocumento = -1;
                  if(!almacen.isPluginGestor())
                       tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
                  else{
                       //PENDIENTE IMPLEMENTACION - GESTOR ALFRESCO
                       tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;

                       //  Si se trata de un plugin de un gestor documental, se pasa la información
                       // extra necesaria                                    
                       ResourceBundle config = ResourceBundle.getBundle("documentos");               
                       String carpetaRaiz    = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

                       
                       GeneralValueObject gVO = DatosSuplementariosManager.getInstance().getInfoCampoSuplementarioFicheroTramite(Integer.parseInt(codMunicipio),
                               Integer.parseInt(codTramite),Integer.parseInt(ocurrenciaTramite),codigoCampo,numero,false,params);
                       String descripcionOrganizacion = (String)gVO.getAtributo("descOrganizacion");
                       String descProcedimiento = (String)gVO.getAtributo("descProcedimiento");
                       nombreFichero = (String)gVO.getAtributo("nombreFichero");
                       tipoContenido = (String)gVO.getAtributo("tipoMime");

                       datos.put("tipoMime",tipoContenido);                    
                       datos.put("nombreDocumento", codigoCampo + "_" + nombreFichero);
                       datos.put("nombreFicheroCompleto", codigoCampo + "_" + codTramite + "_" + ocurrenciaTramite + "_" +  nombreFichero);
                       datos.put("nombreDocumento", codigoCampo + "_" + codTramite + "_" + ocurrenciaTramite + "_" +  nombreFichero);


                       ArrayList<String> listaCarpetas = new ArrayList<String>();
                       listaCarpetas.add(carpetaRaiz);
                       listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + descripcionOrganizacion);
                       listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + descProcedimiento);
                       listaCarpetas.add(numero.replaceAll("/","-"));
                       datos.put("listaCarpetas",listaCarpetas);

                  }
                  
                    doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                    doc.setExpHistorico(false);
                    doc = almacen.getDocumentoDatosSuplementariosTramite(doc);

                    fichero = doc.getFichero();
                    nombreFichero = doc.getNombreDocumento() ;
                    tipoContenido  = doc.getTipoMimeContenido();

                    if (log.isDebugEnabled()) {
                        log.debug("Logitud del fichero: " + fichero.length);
                        log.debug("Nombre del fichero: " + nombreFichero);
                        log.debug("Tipo contenido fichero: " + tipoContenido);
                    }

                   
                   if(fichero != null){
                        BufferedOutputStream bos = null;
                        try{                        
                            response.setContentType(tipoContenido);
                            response.setHeader("Content-Disposition", "inline; filename=" + nombreFichero);
                            response.setHeader("Content-Transfer-Encoding", "binary");            

                            ServletOutputStream out = response.getOutputStream();
                            response.setContentLength(fichero.length);
                            bos = new BufferedOutputStream(out);
                            bos.write(fichero, 0, fichero.length);
                            bos.flush();
                            bos.close();


                        }catch(Exception e){
                            if(log.isDebugEnabled()) log.debug("Excepcion en catch de VerDocumentoServlet.defaultAction()");
                            throw e;
                        }finally {
                            if(bos != null) bos.close();
                        } 
                    }
                    
                    
                    
                 

                }
                
                
                /*******************/
             
            }else
                log.error(this.getClass().getName() + " ERROR: Se está intentando descargar un campo suplementario de tipo fichero a nivel de trámite, pero se desconoce cual es el número del expediente y/o código del documento");
            
        }catch(AlmacenDocumentoTramitacionException e){
            //log.error(this.getClass().getName() + " ERROR: " + e.getMessage());
            log.error(this.getClass().getName() + " ERROR AL RECUPERAR EL CAMPO SUPLEMENTARIO DE TIPO FICHERO A NIVEL DE TRÁMITE: " + e.getMessage());
            e.printStackTrace();    
            log.error("Error al recuperar documento : Codigo Error recibido flexia(Regitrado en BBDD ERRDIGIT) y mensaje :" + e.getCodigo() + " - " + e.getMessage());
            try {
                log.error(this.getClass() + " - Preparamos el js para mostrar mensaje error Dokusi en Servelet");
                //ResourceBundle configDocumentosDokusi = ResourceBundle.getBundle("MELANBIDE_DOKUSI");
                UsuarioValueObject usu = (UsuarioValueObject) session.getAttribute("usuario");
                Integer idioma = 1;
                if (usu != null) {
                    idioma = usu.getIdioma();
                }
                GestionMensajeErrorDokusi gestionMensajeErrorDokusi = new GestionMensajeErrorDokusi();
                String textoMensaje = gestionMensajeErrorDokusi.preparaTextoHtmlContenidoMensaje(e, idioma);
                PrintWriter out = response.getWriter();
                out.println(textoMensaje);
            } catch (Exception ex) {
                log.error(this.getClass() + " - Error al mostrar mensaje error Dokusi en Servelet - " + e.getMessage());
            }
        }catch(Exception e){            
            //log.error(this.getClass().getName() + " ERROR: " + e.getMessage());
            log.error(this.getClass().getName() + " ERROR AL RECUPERAR EL CAMPO SUPLEMENTARIO DE TIPO FICHERO A NIVEL DE TRÁMITE: " + e.getMessage());
            e.printStackTrace();
        }  

        return null;
    }
    
    
    
    
    
}