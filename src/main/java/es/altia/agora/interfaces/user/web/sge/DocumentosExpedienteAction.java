package es.altia.agora.interfaces.user.web.sge;

// PAQUETES IMPORTADOS
import com.google.gson.Gson;
import es.altia.agora.business.documentos.DocumentoManager;
import es.altia.agora.business.editor.mantenimiento.persistence.DocumentosAplicacionManager;
import es.altia.agora.business.editor.mantenimiento.persistence.manual.DocumentosAplicacionDAO;
import es.altia.agora.business.administracion.mantenimiento.persistence.IdiomasManager;
import es.altia.agora.business.documentos.DocumentosLanbideManager;
import es.altia.agora.business.documentos.helper.CodigoSeguroVerificacionHelper;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.DocumentoTramiteVO;
import es.altia.agora.business.sge.MetadatosDocumentoVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.firma.FirmaFlujoManager;
import es.altia.agora.business.sge.firma.vo.FirmaUsuarioVO;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.business.util.GeneralValueObject;
import java.util.ArrayList;
import es.altia.agora.business.sge.persistence.DocumentosExpedienteManager;
import es.altia.agora.business.sge.persistence.OperacionesExpedienteManager;
import es.altia.agora.business.sge.persistence.TramitacionExpedientesManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.util.DocumentoTramitacionHelper;

import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoFirma;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoFirmaBBDD;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoFirmaGestor;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.interfaces.user.web.util.ResultadoAjax;
import es.altia.agora.technical.ConstantesAjax;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.DocumentConversionException;
import es.altia.common.exception.FormatNotSupportedException;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.commons.DateOperations;
import es.altia.util.commons.JODConverterHelper;
import es.altia.util.commons.MimeTypes;
import es.altia.util.commons.WebOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.documentos.DocumentOperations;
import es.altia.util.exceptions.JODReportsException;
import es.altia.util.struts.StrutsFileValidation;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.StringTokenizer;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import freemarker.core.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.struts.upload.FormFile;


public class DocumentosExpedienteAction extends ActionSession{

   protected static Log log =
           LogFactory.getLog(DocumentosExpedienteAction.class.getName());

   protected static Config conf = ConfigServiceHelper.getConfig("techserver");
   protected static Config m_Documentos = ConfigServiceHelper.getConfig("documentos");
   protected static Config registroConf = ConfigServiceHelper.getConfig("Registro");

   public ActionForward performSession(ActionMapping mapping,ActionForm form,
      HttpServletRequest request,HttpServletResponse response)
      throws IOException, ServletException
   {
     log.debug("perform");
     ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
     ActionErrors errors = new ActionErrors();
     // Validaremos los parametros del request especificados
     HttpSession session = request.getSession();
     if (form == null){
         form = new DocumentosExpedienteForm();
         if ("request".equals(mapping.getScope())){
           request.setAttribute(mapping.getAttribute(), form);
         }else{
           session.setAttribute(mapping.getAttribute(), form);
         }
     }
     DocumentosExpedienteForm myForm = (DocumentosExpedienteForm)form;
     String opcion = myForm.getOpcion();
     log.debug("Que nos viene en el form:"+myForm.toString());
     log.debug("Que opcion nos viene del form:"+opcion);
     String numeroExpediente = request.getParameter("numeroExpediente");
     String ocuTramite=myForm.getOcurrenciaTramite();
     String codTramite=myForm.getCodTramite();
     log.debug("Que numeroExpediente nos viene del form:"+numeroExpediente);
     log.debug("Que ocurrencia de tramite nos viene del form:"+ocuTramite);
     log.debug("Que tramite viene del form:"+codTramite);
          
     UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
     String[] params = usuario.getParamsCon();

     if ("altaDocumento".equalsIgnoreCase(opcion)){
     	GeneralValueObject gVO = new GeneralValueObject();
     	gVO.setAtributo("codAplicacion",Integer.toString(usuario.getAppCod()));
     	gVO.setAtributo("codMunicipio",Integer.toString(usuario.getOrgCod()));
      gVO.setAtributo("codProcedimiento",myForm.getCodProcedimiento());
      gVO.setAtributo("ejercicio",myForm.getEjercicio());
      gVO.setAtributo("numeroExpediente",myForm.getNumeroExpediente());
      gVO.setAtributo("codTramite",myForm.getCodTramite());
      gVO.setAtributo("ocurrenciaTramite",myForm.getOcurrenciaTramite());
      gVO.setAtributo("codPlantilla",myForm.getCodPlantilla());
      String lCodInteresados = myForm.getListaCodInteresados();
      String lVersInteresados = myForm.getListaVersInteresados();
      Vector listaCodInteresados = listaInteresadosSeleccionados(lCodInteresados);
      Vector listaVersInteresados = listaInteresadosSeleccionados(lVersInteresados);


      gVO.setAtributo("listaCodInteresados",listaCodInteresados);
      gVO.setAtributo("listaVersInteresados",listaVersInteresados);
      gVO.setAtributo("idioma", conf.getString("idiomaDefecto"));
      Vector listaIdiomas = IdiomasManager.getInstance().getListaIdiomas(params);	
      gVO.setAtributo("listaIdiomas", listaIdiomas);
      String xml;


      if(lCodInteresados == null || lCodInteresados.equals("")) {
        xml = DocumentosExpedienteManager.getInstance().consultaXML(gVO,params);
      } else {
        xml = DocumentosExpedienteManager.getInstance().consultaXML2(gVO,params);
      }
      myForm.setOpcion(null);
      myForm.setNumeroDocumento(null);
      myForm.setDatosXML(xml);
      
      
     } else if ("altaDocumentoOOffice".equalsIgnoreCase(opcion)){
      
      GeneralValueObject gVO = new GeneralValueObject();
     	gVO.setAtributo("codAplicacion",Integer.toString(usuario.getAppCod()));
     	gVO.setAtributo("codMunicipio",Integer.toString(usuario.getOrgCod()));
      gVO.setAtributo("codProcedimiento",myForm.getCodProcedimiento());
      gVO.setAtributo("ejercicio",myForm.getEjercicio());
      gVO.setAtributo("numeroExpediente",myForm.getNumeroExpediente());
      gVO.setAtributo("codTramite",myForm.getCodTramite());
      gVO.setAtributo("ocurrenciaTramite",myForm.getOcurrenciaTramite());
      gVO.setAtributo("codPlantilla",myForm.getCodPlantilla());
      String lCodInteresados = myForm.getListaCodInteresados();
      String lVersInteresados = myForm.getListaVersInteresados();
      Vector listaCodInteresados = listaInteresadosSeleccionados(lCodInteresados);
      Vector listaVersInteresados = listaInteresadosSeleccionados(lVersInteresados);
      gVO.setAtributo("listaCodInteresados",listaCodInteresados);
      gVO.setAtributo("listaVersInteresados",listaVersInteresados);
      gVO.setAtributo("idioma", conf.getString("idiomaDefecto"));
      Vector listaIdiomas = IdiomasManager.getInstance().getListaIdiomas(params);	
      gVO.setAtributo("listaIdiomas", listaIdiomas);
      String xml;
      if(lCodInteresados == null || lCodInteresados.equals("")) {
        xml = DocumentosExpedienteManager.getInstance().consultaXML(gVO,params);
      } else {
        xml = DocumentosExpedienteManager.getInstance().consultaXML2(gVO,params);
      }
      myForm.setOpcion(null);
      myForm.setNumeroDocumento(null);
      myForm.setDatosXML(xml);
      
     } else if ("altaDocumentoAdjunto".equalsIgnoreCase(opcion)){
      
      GeneralValueObject gVO = new GeneralValueObject();
     	gVO.setAtributo("codAplicacion",Integer.toString(usuario.getAppCod()));
     	gVO.setAtributo("codMunicipio",Integer.toString(usuario.getOrgCod()));
      gVO.setAtributo("codProcedimiento",myForm.getCodProcedimiento());
      gVO.setAtributo("ejercicio",myForm.getEjercicio());
      gVO.setAtributo("numeroExpediente",myForm.getNumeroExpediente());
      gVO.setAtributo("codTramite",myForm.getCodTramite());
      gVO.setAtributo("ocurrenciaTramite",myForm.getOcurrenciaTramite());
      gVO.setAtributo("codPlantilla",myForm.getCodPlantilla());
      String lCodInteresados = myForm.getListaCodInteresados();
      String lVersInteresados = myForm.getListaVersInteresados();
      Vector listaCodInteresados = listaInteresadosSeleccionados(lCodInteresados);
      Vector listaVersInteresados = listaInteresadosSeleccionados(lVersInteresados);
      gVO.setAtributo("listaCodInteresados",listaCodInteresados);
      gVO.setAtributo("listaVersInteresados",listaVersInteresados);
      gVO.setAtributo("idioma", conf.getString("idiomaDefecto"));
      String xml="";
       Map campos=new HashMap();
      if(lCodInteresados == null || lCodInteresados.equals("")) {
        //xml = DocumentosExpedienteManager.getInstance().consultaXML(gVO,params);
        campos=DocumentosExpedienteManager.getInstance().consultaHashEtiquetasValor(gVO,params);
      } else {
        xml = DocumentosExpedienteManager.getInstance().consultaXML2(gVO,params);
        campos=DocumentosExpedienteManager.getInstance().consultaHashEtiquetasValor2(gVO,params);
      }
      
      /*campos.put("ANOACTUAL", "2025");
      Map campos2=new HashMap();
      Map campos3=new HashMap();
      List<Map> ejemploLista = new ArrayList<Map>();
       campos2.put("INTERESADO","JUAN");
       ejemploLista.add(campos2);     
      campos3.put("INTERESADO","PEDRO");      
      ejemploLista.add(campos3);
      campos.put("INFOINTERESADO", ejemploLista);*/
      
         myForm.setOpcion(null);
         myForm.setNumeroDocumento(null);
         myForm.setDatosXML(xml);
         myForm.setCampos(campos);

         DocumentosAplicacionManager docAplMan = DocumentosAplicacionManager.getInstance();

         GeneralValueObject gVO1 = new GeneralValueObject();
         gVO1.setAtributo("codDocumento", myForm.getCodPlantilla());
         try {
             gVO1 = docAplMan.loadDocumento(gVO1, params);
             String nombreDocumento = (String) gVO1.getAtributo("nombre");
             String extension = m_Documentos.getString("EXTENSION_DOCUMENTOS_TRAMITACION");
             m_Log.debug("extension "+ extension);
             byte[] fichero = null;
             if (gVO1 != null) {
                 String PATH_DIR_DOC = null;
                 try {
                     
                    PATH_DIR_DOC = DocumentOperations.crearDocumentoDesdePlantilla(gVO, nombreDocumento, extension, (byte[]) gVO1.getAtributo("fichero"), campos, session.getId(), false);
                    if(PATH_DIR_DOC != null){    
                        fichero = FileUtils.readFileToByteArray(new File(PATH_DIR_DOC.toString()));

                        Hashtable<String, Object> datos = new Hashtable<String, Object>();
                        datos.put("codMunicipio", Integer.toString(usuario.getOrgCod()));
                        datos.put("codProcedimiento", myForm.getCodProcedimiento());
                        datos.put("ejercicio", myForm.getEjercicio());
                        datos.put("numeroExpediente", myForm.getNumeroExpediente());
                        datos.put("codTramite", myForm.getCodTramite());
                        datos.put("ocurrenciaTramite", myForm.getOcurrenciaTramite());
                        datos.put("codDocumento", myForm.getCodDocumento());
                        datos.put("codUsuario", myForm.getCodUsuario());
                        datos.put("nombreDocumento", myForm.getNombreDocumento() + "." + extension);

                        datos.put("perteneceRelacion", "false");
                        datos.put("params", params);
                        datos.put("fichero", fichero);

                        log.debug(myForm.getCodProcedimiento());
                        log.debug(myForm.getEjercicio());
                        log.debug(myForm.getNumeroExpediente());
                        log.debug(myForm.getCodTramite());
                        log.debug(myForm.getOcurrenciaTramite());
                        log.debug(myForm.getCodDocumento());
                        log.debug(myForm.getCodUsuario());
                        log.debug(myForm.getNombreDocumento());
                        log.debug(myForm.getNumeroDocumento());

                        String codProcedimiento = myForm.getCodProcedimiento();
                        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()), codProcedimiento);
                        Documento doc = null;
                        int tipoDocumento = -1;
                        if (!almacen.isPluginGestor()) {
                            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                        } else {
                            String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(Integer.toString(usuario.getOrgCod()), myForm.getCodProcedimiento(), myForm.getCodTramite(), params);
                            String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(myForm.getCodProcedimiento(), params);
                            datos.put("codigoVisibleTramite", codigoVisibleTramite);
                            datos.put("codificacion", ConstantesDatos.CODIFICACION_UTF_8);

                            String editorTexto = "";
                            
                            //Se graba un documento en el alta, no sabemos su codigo
                            if ("".equals(myForm.getNumeroDocumento()) || ("undefined").equals(myForm.getNumeroDocumento()) || ("UNDEFINED").equals(myForm.getNumeroDocumento()) || (myForm.getNumeroDocumento() == null)) {

                                editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTextoAlta(usuario.getOrgCod(),
                                        myForm.getNumeroExpediente(), Integer.parseInt(myForm.getCodTramite()), Integer.parseInt(myForm.getCodDocumento()), params);

                            } else {
                                editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTexto(usuario.getOrgCod(),
                                        myForm.getNumeroExpediente(), Integer.parseInt(myForm.getCodTramite()),
                                        Integer.parseInt(myForm.getOcurrenciaTramite()), Integer.parseInt(myForm.getNumeroDocumento()), false, params);

                            }
                            
                            String tipoMime = DocumentOperations.determinarTipoMimePlantilla(
                                    editorTexto, nombreDocumento);
                            datos.put("extension", MimeTypes.guessExtensionFromMimeType(tipoMime));
                            datos.put("tipoMime", tipoMime);

                            /**
                             * SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS
                             * QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL *
                             */
                            ResourceBundle bundleDocumentos = ResourceBundle.getBundle("documentos");
                            /**
                             * String tipoPlugin =
                             * bundleDocumentos.getString(ConstantesDatos.PROPIEDAD_PLUGIN_ALMACENAMIENTO);
                             * String nombreGestor =
                             * bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO
                             * + usuario.getOrgCod() + ConstantesDatos.BARRA +
                             * tipoPlugin +
                             * ConstantesDatos.SUFIJO_PLUGIN_NOMBRE_GESTOR); String
                             * carpetaRaiz =
                             * bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO
                             * + usuario.getOrgCod() + ConstantesDatos.BARRA +
                             * nombreGestor +
                             * ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                             */
                            String carpetaRaiz = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + usuario.getOrgCod() + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

                            String descripcionOrganizacion = usuario.getOrg();
                            ArrayList<String> listaCarpetas = new ArrayList<String>();
                            listaCarpetas.add(carpetaRaiz);
                            listaCarpetas.add(usuario.getOrgCod() + ConstantesDatos.GUION + descripcionOrganizacion);
                            listaCarpetas.add(myForm.getCodProcedimiento() + ConstantesDatos.GUION + nombreProcedimiento);
                            listaCarpetas.add(myForm.getNumeroExpediente().replaceAll(ConstantesDatos.BARRA, ConstantesDatos.GUION));

                            datos.put("listaCarpetas", listaCarpetas);
                            /**
                             * * FIN  **
                             */

                            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                        }

                        doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);

                        try {
                            boolean guardado = almacen.setDocumento(doc);
                            if (guardado) {
                                request.getSession().setAttribute("resGrabarDocumento", "resGrabarDocumentoOK");
                            } else {
                                request.getSession().setAttribute("resGrabarDocumento", "resGrabarDocumentoKO");
                            }

                        } catch (Exception e) {
                            request.getSession().setAttribute("resGrabarDocumento", "resGrabarDocumentoKO");
                        }

                        myForm.setOpcion(null);
                    }
                 }catch (ParseException pe){
                     pe.printStackTrace();
                     myForm.setResultado("fail");
                     opcion = "grabarDocumento";

                 } catch (JODReportsException jre) {
                     jre.printStackTrace();
                     myForm.setResultado("fail");
                     opcion = "grabarDocumento";

                 } catch (IOException io) {
                     io.printStackTrace();
                     myForm.setResultado("fail");
                     opcion = "grabarDocumento";

                 } catch (NullPointerException npe) {
                     npe.printStackTrace();
                     myForm.setResultado("fail");
                     opcion = "grabarDocumento";
                 } catch (Exception e) {
                     e.printStackTrace();
                     myForm.setResultado("fail");
                     opcion = "grabarDocumento";

                 }

             } else {
                 log.error("NO SE HA PODIDO RECUPERAR EL CONTENIDO DEL DOCUMENTO");
                 myForm.setResultado("fail");
                 opcion = "grabarDocumento";
                 throw new ServletException("NO SE HA PODIDO RECUPERAR EL CONTENIDO DEL DOCUMENTO");
             }
         } catch (Exception exc) {
             log.error("NO SE HA PODIDO RECUPERAR LA PLANTILLA DE BBDD");

             myForm.setResultado("fail");
             opcion = "grabarDocumento";
             throw new ServletException("NO SE HA PODIDO RECUPERAR la plantilla de BBDD");
         }


      
    }else if ("anexarDocumentoAdjunto".equalsIgnoreCase(opcion)){
          myForm.setNombreDocumento("");
         
          opcion = "documentoNuevoAnexo";
      
      
      
     } else if("grabarDocumento".equalsIgnoreCase(opcion)){
        Hashtable<String,Object> datos = new Hashtable<String,Object>();
        datos.put("codMunicipio",Integer.toString(usuario.getOrgCod()));
        datos.put("codProcedimiento",myForm.getCodProcedimiento());
        datos.put("ejercicio",myForm.getEjercicio());
        datos.put("numeroExpediente",myForm.getNumeroExpediente());
        datos.put("codTramite",myForm.getCodTramite());
        datos.put("ocurrenciaTramite",myForm.getOcurrenciaTramite());
        datos.put("codDocumento",myForm.getCodDocumento());
        datos.put("codUsuario",myForm.getCodUsuario());
        datos.put("nombreDocumento",myForm.getNombreDocumento());
        datos.put("numeroDocumento",myForm.getNumeroDocumento());
        datos.put("perteneceRelacion","false");
        datos.put("params",params);
        datos.put("fichero",myForm.getFicheroWord().getFileData());
        
        log.debug(myForm.getCodProcedimiento());
        log.debug(myForm.getEjercicio());
        log.debug(myForm.getNumeroExpediente());
        log.debug(myForm.getCodTramite());
        log.debug(myForm.getOcurrenciaTramite());
        log.debug(myForm.getCodDocumento());
        log.debug(myForm.getCodUsuario());
        log.debug(myForm.getNombreDocumento());
        log.debug(myForm.getNumeroDocumento());
      
        
        String codProcedimiento = myForm.getCodProcedimiento();        
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),codProcedimiento);
        Documento doc = null;
        int tipoDocumento = -1;
        if(!almacen.isPluginGestor())
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        else{
             String editorTexto="";
             //Se graba un documento en el alta, no sabemos su codigo
            if("".equals(myForm.getNumeroDocumento())||("undefined").equals(myForm.getNumeroDocumento())||("UNDEFINED").equals(myForm.getNumeroDocumento())||(myForm.getNumeroDocumento()==null))
            {
                
                  editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTextoAlta(usuario.getOrgCod(),
                    myForm.getNumeroExpediente(),Integer.parseInt(myForm.getCodTramite()),Integer.parseInt(myForm.getCodDocumento()),params);
                
            }else
            {
                 editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTexto(usuario.getOrgCod(),
                    myForm.getNumeroExpediente(),Integer.parseInt(myForm.getCodTramite()),
                    Integer.parseInt(myForm.getOcurrenciaTramite()),Integer.parseInt(myForm.getNumeroDocumento()),false,params);
               
            
            }

            String tipoMime = DocumentOperations.determinarTipoMimePlantilla(
                    editorTexto, myForm.getNombreDocumento());
            datos.put("extension", MimeTypes.guessExtensionFromMimeType(tipoMime));
            datos.put("tipoMime", tipoMime);
            
            tipoDocumento = this.obtenerListaCarpetas(Integer.toString(usuario.getOrgCod()), myForm.getCodProcedimiento(), Integer.valueOf(myForm.getCodTramite()), 
                    params, datos, almacen, usuario, myForm.getNumeroExpediente());
            
        }

        datos.put("origen", almacen.getNombreServicio());
        doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
       
      
        try{
            boolean guardado = almacen.setDocumento(doc);
            if (guardado) {
                OperacionesExpedienteManager.getInstance().registrarAltaDocumentoTramite(doc,usuario.getNombreUsu(),params);
                request.getSession().setAttribute("resGrabarDocumento", "resGrabarDocumentoOK");
            } else 
                request.getSession().setAttribute("resGrabarDocumento", "resGrabarDocumentoKO");
            
        }catch(Exception e){
             request.getSession().setAttribute("resGrabarDocumento", "resGrabarDocumentoKO");
        }
       
		myForm.setOpcion(null);

     } else if("modificarDocumento".equalsIgnoreCase(opcion) || "modificarDocumentoOOffice".equalsIgnoreCase(opcion)
             || "verDocumentoOOffice".equalsIgnoreCase(opcion)){
     	myForm.setOpcion(null);

        
        }else if("modificarDocumentoODT".equalsIgnoreCase(opcion)){
         String nombreDocumento=request.getParameter("nombreDocumento");
         String numeroDocumento=request.getParameter("numeroDocumento");
         String ocurrencia = request.getParameter("ocurrencia");
         String codDocumento = request.getParameter("codDocumento");
         myForm.setCodDocumento(codDocumento);
         myForm.setNombreDocumento(nombreDocumento);
         myForm.setNumeroDocumento(numeroDocumento);
         myForm.setOcurrenciaTramite(ocurrencia);
 
         opcion="modificarDocumentoODT";
     }else if("modificarODT".equalsIgnoreCase(opcion)){
         m_Log.debug("Modificacion Documento ODT");
         FormFile fichero=null;
         if(myForm != null && myForm.getFicheroWord() !=null){
             if(!"".equals(myForm.getFicheroWord().getFileName())){
                 fichero=myForm.getFicheroWord();
             }
         }
         boolean isExtensionCorrect = false;
         boolean isSizeCorrect = false;
         if(fichero !=null && fichero.getFileSize()>0){
             //Si se sube por POST el documento
             //Comprobación del tamaño del fichero y de la extensión correcta
             isExtensionCorrect = StrutsFileValidation.isExtensionValid(fichero, StrutsFileValidation.APP_EXPEDIENTES);
             isSizeCorrect = StrutsFileValidation.isSizeValid(fichero, StrutsFileValidation.APP_EXPEDIENTES);
             if(!isExtensionCorrect){
                 m_Log.debug("La extensión del fichero no es válida");
                 request.setAttribute(ConstantesDatos.EXTENSION_FILE_INCORRECT, "si");
                 String ext=(String) StrutsFileValidation.getLimite(StrutsFileValidation.EXTENSION_DOC_PREFFIX, StrutsFileValidation.SUFFIX_EXPEDIENTE);
                 request.setAttribute(ConstantesDatos.EXTENSION_PERMITED,ext);
                 request.setAttribute(ConstantesDatos.DESCRIPCION_BYTES, ConstantesDatos.DESCRIPCION_BYTES);
                 opcion="modificarDocumentoODT"; //volvemos al formulario de entrada
                 
             }else{
                 request.setAttribute(ConstantesDatos.EXTENSION_FILE_INCORRECT,"no");
             }
             //Comprobacion del tamaño del fichero
             if(!isSizeCorrect){
                 request.setAttribute(ConstantesDatos.TAM_MAX_FILE_EXCEED,"si");
                 Integer tam= 
                         Math.round((Integer)StrutsFileValidation.getLimite(StrutsFileValidation.TAM_MAX_DOC_PREFFIX,StrutsFileValidation.APP_EXPEDIENTES)/ConstantesDatos.DIVISOR_BYTES);
                 request.setAttribute(ConstantesDatos.TAM_MAX_FILE_BYTE,tam);
                 request.setAttribute(ConstantesDatos.DESCRIPCION_BYTES, ConstantesDatos.DESCRIPCION_BYTES);
                 opcion="modificarDocumentoODT"; //volvemos al formulario de entrada
                 
             }else{
                 request.setAttribute(ConstantesDatos.TAM_MAX_FILE_EXCEED, "no");
             }
             if(isExtensionCorrect && isSizeCorrect){
                
                  Hashtable<String,Object> datos = new Hashtable<String,Object>();
                    datos.put("codMunicipio",myForm.getCodMunicipio());
                    datos.put("codProcedimiento",myForm.getCodProcedimiento());
                    datos.put("ejercicio",myForm.getEjercicio());
                    datos.put("numeroExpediente",myForm.getNumeroExpediente());
                    datos.put("codTramite",myForm.getCodTramite());
                    datos.put("ocurrenciaTramite",myForm.getOcurrenciaTramite());
                    datos.put("codUsuario",String.valueOf(usuario.getIdUsuario()));        
                    datos.put("numeroDocumento",myForm.getNumeroDocumento());
                    datos.put("perteneceRelacion","false");
                    datos.put("params",params);
                    datos.put("nombreDocumento",myForm.getNombreDocumento());
                    datos.put("fichero",myForm.getFicheroWord().getFileData());
                    
                    AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),myForm.getCodProcedimiento());
                    Documento doc = null;
                    int tipoDocumento = -1;
                      String filetype = fichero.getContentType();
                        log.debug("Tipo mime file upload: " + filetype);
                        // Si es octet-stream tratamos de deducir el mimetype a partir de 
                        // la extension
                        if (MimeTypes.BINARY[0].equals(filetype)) {
                            filetype = MimeTypes.guessMimeType(filetype, fichero.getFileName());
                        }
                    if(!almacen.isPluginGestor()){
                        m_Log.debug("El documento no se almacena en el plugin del gestor");
                        tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                    }else{
                        m_Log.debug("El documento se almacena en el plugin del gestor");
                        String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(Integer.toString(usuario.getOrgCod()),myForm.getCodProcedimiento(),myForm.getCodTramite(),params);
                        String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(myForm.getCodProcedimiento(), params);            
                        datos.put("codigoVisibleTramite",codigoVisibleTramite);
                        datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);
                        // el documento modificado solo puede ser .doc o .docx
                        if (!filetype.equals(ConstantesDatos.TIPO_MIME_DOCUMENTO_OFFICE)) {
                             m_Log.debug("Documento doc");
                            datos.put("extension", ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_WORD);
                            datos.put("tipoMime", ConstantesDatos.TIPO_MIME_DOCUMENTO_WORD);
                        } else {
                            m_Log.debug("Documento docx");
                            datos.put("extension", ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OFFICE);
                            datos.put("tipoMime", ConstantesDatos.TIPO_MIME_DOCUMENTO_OFFICE);
                        }
                        
           
                        /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
                        ResourceBundle bundleDocumentos = ResourceBundle.getBundle("documentos");            
                        String carpetaRaiz = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + usuario.getOrgCod() + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
            
                        String descripcionOrganizacion = usuario.getOrg();
                        ArrayList<String> listaCarpetas = new ArrayList<String>();
                        listaCarpetas.add(carpetaRaiz);
                        listaCarpetas.add(usuario.getOrgCod() + ConstantesDatos.GUION + descripcionOrganizacion);
                        listaCarpetas.add(myForm.getCodProcedimiento()+ ConstantesDatos.GUION + nombreProcedimiento);
                        listaCarpetas.add(myForm.getNumeroExpediente().replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));

                        datos.put("listaCarpetas",listaCarpetas);
                        /*** FIN  ***/
                        tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                        }
                        doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);try{
                        boolean guardado = almacen.setDocumento(doc);
            
            
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
            }


     }else if("actualizarFechaInforme".equalsIgnoreCase(opcion)){

     	GeneralValueObject gVO = new GeneralValueObject();
     	gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
     	gVO.setAtributo("codProcedimiento",request.getParameter("codProcedimiento"));        
        gVO.setAtributo("ejercicio",request.getParameter("ejercicio"));
        gVO.setAtributo("numeroExpediente",request.getParameter("numero"));
        gVO.setAtributo("codTramite",request.getParameter("codTramite"));
        gVO.setAtributo("ocurrenciaTramite",request.getParameter("ocurrencia"));
        gVO.setAtributo("codDocumento",request.getParameter("codDocumento"));
        gVO.setAtributo("fechaInforme",request.getParameter("fechaInforme"));
        		

        boolean ok = false;
         try {
            DocumentosExpedienteManager.getInstance().actualizarFechaInforme(gVO, params);
            ok = true;
         } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
         }
		
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        if(ok) out.println("ok=si");  else out.println("ok=no");
     } else if("crearCSV".equalsIgnoreCase(opcion)){
         DocumentoTramiteVO campo = null;
         ByteArrayInputStream inputStream = null;
         ByteArrayOutputStream outputStream = null;
        
         ResultadoAjax<HashMap<String,Object>> respuesta = new ResultadoAjax<HashMap<String,Object>>();
         HashMap<String,Object> resp = new HashMap<String, Object>();

         DocumentoFirma duplicado = null;
         respuesta.setStatus(ConstantesAjax.STATUS_AJAX_OK);
         respuesta.setDescStatus(ConstantesAjax.DESC_STATUS_AJAX_OK);

         try {
             
            //Se obtienen los tipos de procedimientos del fichero properties Portafirmas.properties
            ResourceBundle expPortafirmas = ResourceBundle.getBundle("Portafirmas");
            String propiedad = usuario.getOrgCod()+"/Portafirmas";
            String portafirmas = expPortafirmas.getString(propiedad);
 
             // Obtenemos los parametros que necesitan los metodos para crear el CSV
             Map<String, Object> parametros = obtenerParametrosCrearCSV(request, session, portafirmas);
             Integer codigoDocumentoAnterior = (Integer) parametros.get("codDocumento");
             
             // Se comprueba si ya existe el metadato CSV
             m_Log.debug("Se comprueba si ya existe el metadato CSV");
             if (!existeDocumentoMetadatoCSV(parametros)) {
                 m_Log.debug("No existe el metadato CSV");
                 
                 // Se obtiene el fichero del documento
                 m_Log.debug("Se obtiene el fichero del documento");
                 
                //Se indica si al convertir el documento se desea que sea PDF o PDFA y ese valor se almacena en el fichero documentos.properties
                ResourceBundle configDocuemntos = ResourceBundle.getBundle("documentos");
                propiedad = usuario.getOrgCod()+"/DOCUMENTOS_EXPEDIENTE/FORMATO_PDFA";
                String pdfA = configDocuemntos.getString(propiedad);
                 
                byte[] ficheroDoc = obtieneFicheroPdfCSV(parametros, pdfA);

                // Antes de continuar con el proceso y convertir el documento a pdf, hacemos una copia de sus datos
                m_Log.debug("Antes de continuar con el proceso y convertir el documento a pdf, hacemos una copia de sus datos");
                
                Integer codDocOriginal = null;
                
                duplicado = duplicarDocumentoAntesEnviarFirmar(usuario.getOrgCod(),parametros, codDocOriginal);
                
                m_Log.info("codDocOriginal: " + codDocOriginal);
                m_Log.debug("Se ha realizado el duplicado de Documento");
                 
                String codigoCSV ="";
                boolean generarCSV = CodigoSeguroVerificacionHelper.incrustarCSVenJustificante(usuario.getOrgCod(), registroConf);

                 if (ficheroDoc != null && ficheroDoc.length > 0) {
                     m_Log.debug("El fichero no se encuentra vacio");
                     outputStream = new ByteArrayOutputStream();
                     inputStream = new ByteArrayInputStream(ficheroDoc);
                    
                    if (generarCSV ) {
                        // Incrustar el CSV en el pdf
                        String cabeceraCSV = ""; // TODO implementar cabecera
                        codigoCSV = DocumentoManager.getInstance().incrustarCSVenPDF(inputStream, outputStream, cabeceraCSV, usuario.getNombreUsu(), String.valueOf(usuario.getOrgCod()));
                    } else {
                        IOUtils.copy(inputStream,outputStream);
                    }
                     
                     // Almacenar el documento
                     campo = almacenarDocumentoCSV(parametros, outputStream, codigoCSV, portafirmas, duplicado);
                     
                     if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
                        m_Log.debug("duplicado.getCodDocumento() antes del cambio: " + duplicado.getCodDocumento());
                        m_Log.debug("duplicado.getNombreDocumento() antes del cambio: " + duplicado.getNombreDocumento());
                        m_Log.debug("campo.getCodigo() antes del cambio: " + campo.getCodigo());
                        m_Log.debug("campo.getNombre() antes del cambio: " + campo.getNombre());
                      
                        String nombreAux = duplicado.getNombreDocumento();
                        duplicado.setNombreDocumento(campo.getNombre());
                        campo.setNombre(nombreAux);
                        duplicado.setCodDocumento(Integer.valueOf(campo.getCodigo()));
                        campo.setCodigo(String.valueOf(codigoDocumentoAnterior));
                        duplicado.setEstadoFirma("V");
                        
                        //Si se trata el portafirmas de tipo lanbide se obtiene el id y nombre del usuario que se guardó en el flujo de firmas (E_CRD_FIR_FIRMANTE)
                        //De esta forma se guardara el usuario que se selecciona en la lista de usuarios disponibles
                        String[] paramsFirmante = (String[]) parametros.get("paramsBBDD");
                        Integer codMunicipioFirmante = (Integer) parametros.get("codMunicipio");
                        String codProcedimientoFirmante = (String) parametros.get("codProcedimiento");
                        Integer ejercicioFirmante = (Integer) parametros.get("ejercicio");
                        String numExpedienteFirmante = (String) parametros.get("numExpediente");
                        Integer codTramiteFirmante = (Integer) parametros.get("codTramite");
                        Integer ocurrenciaTramiteFirmante = (Integer) parametros.get("ocurrenciaTramite");
                        Integer numeroDocumentoFirmante = codigoDocumentoAnterior;

                        FirmaFlujoManager firmaFlujoManager = FirmaFlujoManager.getInstance();
                        FirmaUsuarioVO usuarioFirmante = firmaFlujoManager.getUsuarioFirmanteByDocumentoTramite(
                                    codMunicipioFirmante, codProcedimientoFirmante, ejercicioFirmante, numExpedienteFirmante,
                                    codTramiteFirmante, ocurrenciaTramiteFirmante, numeroDocumentoFirmante, portafirmas,
                                    paramsFirmante);

                        if (usuarioFirmante != null && usuarioFirmante.getIdUsuario() != null) {
                            campo.setNombreUsuarioFirmante(usuarioFirmante.getNombre());
                        }
                        
                        m_Log.debug("duplicado.getCodDocumento() despues del cambio: " + duplicado.getCodDocumento());
                        m_Log.debug("duplicado.getNombreDocumento() despues del cambio: " + duplicado.getNombreDocumento());
                        m_Log.debug("campo.getCodigo() despues del cambio: " + campo.getCodigo());
                        m_Log.debug("campo.getNombre() despues del cambio: " + campo.getNombre());
                        m_Log.debug("campo.geNombreUsuarioFirmante() despues del cambio: " + campo.getNombreUsuarioFirmante());
                    }
                     
                 } else {
                     m_Log.debug("El fichero no existe o se encuentra vacio");
                     respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_NO_EXISTE_FICHERO);
                     respuesta.setDescStatus(ConstantesAjax.DESC_STATUS_AJAX_ERROR_NO_EXISTE_FICHERO);
                }
             } else {
                 m_Log.debug("Existe el metadato CSV");
                 respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_YA_EXISTE_CSV);
                 respuesta.setDescStatus(ConstantesAjax.DESC_STATUS_AJAX_ERROR_YA_EXISTE_CSV);
                 
                 /* ***** FUNCION JAVASCRIPT crearCSVDocumentoTramite *****
                    Cuando se crea el CSV al enviar la firma al Portafirmas
                    no se considera un error que ya exista CSV. En este caso
                    se necesita el codigo del documento para seguir el
                    flujo de ejecucion.
                  */
                 campo = new DocumentoTramiteVO();
                 campo.setCodigo(request.getParameter("codDocumento"));
             }
         } catch (FormatNotSupportedException fnse) {
             m_Log.error("Ha ocurrido un error al intentar generar el CSV", fnse);
             respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_FORMATO_FICHERO_NO_SOPORTADO);
             respuesta.setDescStatus(ConstantesAjax.DESC_STATUS_AJAX_ERROR_FORMATO_FICHERO_NO_SOPORTADO);
         } catch (Exception e) {
           
                 m_Log.error("Ha ocurrido un error al intentar generar el CSV", e);
                 respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_INTERNO);
                 respuesta.setDescStatus(ConstantesAjax.DESC_STATUS_AJAX_ERROR_INTERNO);  
         } finally {
             IOUtils.closeQuietly(inputStream);
             IOUtils.closeQuietly(outputStream);
             
             if(respuesta.getStatus()== ConstantesAjax.STATUS_AJAX_ERROR_FORMATO_FICHERO_NO_SOPORTADO || respuesta.getStatus()== ConstantesAjax.STATUS_AJAX_ERROR_INTERNO
                     || respuesta.getStatus() == ConstantesAjax.STATUS_AJAX_ERROR_NO_EXISTE_FICHERO){
                   //si falla se elimina el duplicado
                  try {    
                    eliminarDuplicadoDocumento(usuario.getOrgCod(), duplicado, params);
                    
                  } catch (AlmacenDocumentoTramitacionException ex) {
                    m_Log.error("Ha ocurrido un error al tratar de eliminar el documento duplicado", ex);
           
                  }
             }
         }

         resp.put("docGen", campo);
         resp.put("docDup", duplicado);
         respuesta.setResultado(resp);
         WebOperations.retornarJSON(new Gson().toJson(respuesta), response);
         return null;
     } else {
       opcion = mapping.getInput();
     }
     return (mapping.findForward(opcion));
   }
   
   private Vector listaInteresadosSeleccionados(String listSelecc) {
    Vector lista = new Vector();
    StringTokenizer codigos;

    if (listSelecc != null) {
      codigos = new StringTokenizer(listSelecc,"§¥",false);

      while (codigos.hasMoreTokens()) {
        String cod = codigos.nextToken();
        lista.addElement(cod);
      }

    }
    return lista;
  }

    private boolean existeDocumentoMetadatoCSV(Map<String, Object> parametros) throws TechnicalException {
        boolean existe = false;

        Integer codDocumento = (Integer) parametros.get("codDocumento");
        String codProcedimiento = (String) parametros.get("codProcedimiento");
        Integer codMunicipio = (Integer) parametros.get("codMunicipio");
        Integer ejercicio = (Integer) parametros.get("ejercicio");
        String numExpediente = (String) parametros.get("numExpediente");
        Integer codTramite = (Integer) parametros.get("codTramite");
        Integer ocurrenciaTramite = (Integer) parametros.get("ocurrenciaTramite");
        UsuarioValueObject usuario = (UsuarioValueObject) parametros.get("usuario");

        existe = DocumentoManager.getInstance().existeMetadatoCSV(usuario.getParamsCon(), ConstantesDatos.SUBCONSULTA_E_CRD_ID_METADATO_PK,
                codMunicipio,
                codProcedimiento,
                ejercicio,
                numExpediente,
                codTramite,
                ocurrenciaTramite,
                codDocumento);

        if (m_Log.isDebugEnabled()) {
            m_Log.debug(String.format("Existe metadato?: %b", existe));
        }

        return existe;
    }
    
    private byte[] obtieneFicheroPdfCSV(Map<String, Object> parametros, String pdfA)
            throws TechnicalException, FormatNotSupportedException, Exception {
        byte[] documentoConvertido = null;

        m_Log.debug("obtieneFicheroPdfCSV INI");
       
        byte[] ficheroDoc = null;
        
        // Descargar fichero
        ficheroDoc = this.obtenerDocumento(parametros);
        m_Log.debug("Se ha realizado la descarga del fichero");
        if (ficheroDoc == null || ficheroDoc.length == 0) {
            m_Log.error("El fichero se encuentra vacio");
            throw new TechnicalException("El fichero esta vacio");
        }

        m_Log.debug("Se procede a realizar la conversion a PDF");
        try {
            String tipoMimeOriginal = (String) parametros.get("tipoMimeOriginal");
            if (!MimeTypes.PDF[0].equals(tipoMimeOriginal)) {
                // Convertir fichero a PDF
                String rutaTemporalCSV = getRutaTemporalFicherosCSV(parametros);
                documentoConvertido = JODConverterHelper.convertirDocumentoAPdf(rutaTemporalCSV, ficheroDoc, tipoMimeOriginal, pdfA);
            } else {
                documentoConvertido = ficheroDoc;
            }
        } catch (DocumentConversionException dce) {
            throw new TechnicalException("Error al intentar convertir el fichero a pdf", dce);
        }  

        parametros.put("ficheroDoc", ficheroDoc);
        
        m_Log.debug("obtieneFicheroPdfCSV FIN");
        return documentoConvertido;
    }
    
    private byte[] descargarFichero(Map<String, Object> parametros) throws TechnicalException {
        byte[] fichero = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        m_Log.debug("descargarFichero");
        
        try {
            Integer codMunicipio = (Integer) parametros.get("codMunicipio");
            String numExpediente = (String) parametros.get("numExpediente");
            Integer codTramite = (Integer) parametros.get("codTramite");
            Integer ocurrenciaTramite = (Integer) parametros.get("ocurrenciaTramite");
            Integer codDocumento = (Integer) parametros.get("codDocumento");
            Boolean isExpHistorico = (Boolean) parametros.get("isExpHistorico");
            String contextPath = (String) parametros.get("contextPath");
            String sessionId = (String) parametros.get("sessionId");
            
            Config configCommon = ConfigServiceHelper.getConfig("common");
            Config configDoc = ConfigServiceHelper.getConfig("documentos");
            String url = String.format("%s%s%s",
                    configCommon.getString("hostVirtual"),
                    contextPath,
                    configDoc.getString("CSV/URI/SERVLET_VER_DOCUMENTO"));

            String codigo = String.format("%s-%d-%s-%d-%d-%d",
                ConstantesDatos.VER_DOCUMENTO_SERVLET_PREFIJO_CODIGO_DOCUMENTO_TRAMITE,
                codMunicipio,
                numExpediente,
                codTramite,
                ocurrenciaTramite,
                codDocumento);
            
            url += ";jsessionid=" + sessionId + 
                    "?codigo=" + codigo + "&opcion=7" + "&expHistorico=" + isExpHistorico + "&embedded=true";
            
            m_Log.info("url = " + url);
                    
            // Descarga del fichero
            WebOperations.descargarUrlGet(url, sessionId, out);
            
            fichero = out.toByteArray();
        } catch (MalformedURLException mue) {
            throw new TechnicalException("Error de tipo MalformedURLException al intentar descargar el fichero del documento", mue);
        } catch (IOException ioe) {
            throw new TechnicalException("Error de tipo IOException al intentar descargar el fichero del documento", ioe);
        } catch (Exception exc) {
            throw new TechnicalException("Error de tipo Exception al intentar descargar el fichero del documento", exc);
        } finally {
            IOUtils.closeQuietly(out);
        }

        return fichero;
    }
    
    private String getRutaTemporalFicherosCSV(Map<String, Object> parametros) throws TechnicalException {
        StringBuilder path = new StringBuilder();

        m_Log.debug("getRutaTemporalFicherosCSV");
        
        try {
            ResourceBundle config = ResourceBundle.getBundle("documentos");
            String rutaBase = config.getString("RUTA_DISCO_DOCUMENTOS");
            String sessionId = (String) parametros.get("sessionId");
            String numExpediente = (String) parametros.get("numExpediente");
            numExpediente = numExpediente.replaceAll("/", "-");

            path.append(rutaBase)
                .append(File.separator)
                .append(ConstantesDatos.SUBCARPETA_DOCUMENTOS_EXPEDIENTE)
                .append(File.separator)
                .append(sessionId)
                .append(File.separator)
                .append(numExpediente)
                .append(File.separator)
                .append(ConstantesDatos.SUBCARPETA_DOCUMENTOS_CSV);

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("Ruta temporal de ficheros CSV: %s", path.toString()));
            }
        } catch (Exception e) {
            throw new TechnicalException("Error al obtener la ruta temporal para almacenar los CSV ", e);
        }

        return path.toString();
    }
    
    private DocumentoTramiteVO almacenarDocumentoCSV(Map<String, Object> parametros,
            ByteArrayOutputStream outputStream, String codigoCSV, String portafirmas, DocumentoFirma documento)
            throws TechnicalException {

        DocumentoTramiteVO documentoTramite = null;

        m_Log.debug("almacenarDocumentoCSV");
        Map<String, Object> parametrosDocumentoDuplciado = null;
        
        if (portafirmas != null && "LAN".equals(portafirmas)) {
            parametrosDocumentoDuplciado =  parametros;
            parametrosDocumentoDuplciado.remove("codDocumento");
            parametrosDocumentoDuplciado.put("codDocumento", documento.getCodDocumento());
            TramitacionExpedientesValueObject tramitesVO = (TramitacionExpedientesValueObject) parametros.get("tramitesVO");
            tramitesVO.setCodDocumento(String.valueOf(documento.getCodDocumento()));
            parametrosDocumentoDuplciado.remove("tramitesVO");
            parametrosDocumentoDuplciado.put("tramitesVO", tramitesVO);
        }

        if (portafirmas != null && "LAN".equals(portafirmas)) {
            documentoTramite = grabarDocumentoCSV(parametrosDocumentoDuplciado, outputStream, codigoCSV, documentoTramite, portafirmas);
        } else {
            documentoTramite = grabarDocumentoCSV(parametros, outputStream, codigoCSV, documentoTramite, portafirmas);
        }

        return documentoTramite;
    }

    private DocumentoTramiteVO grabarDocumentoCSV(Map<String, Object> parametros, ByteArrayOutputStream outputStream, String codigoCSV, DocumentoTramiteVO documentoTramite, String portafirmas) throws TechnicalException {
        try {
            TramitacionExpedientesValueObject tramitesVO = (TramitacionExpedientesValueObject) parametros.get("tramitesVO");
            String editorTexto = (String) parametros.get("editorTexto");
            String[] params = (String[]) parametros.get("paramsBBDD");
            Integer codMunicipio = (Integer) parametros.get("codMunicipio");
            String codProcedimiento = (String) parametros.get("codProcedimiento");
            Integer ejercicio = (Integer) parametros.get("ejercicio");
            String numeroExpediente = (String) parametros.get("numExpediente");
            Integer codTramite = (Integer) parametros.get("codTramite");
            Integer ocurrenciaTramite = (Integer) parametros.get("ocurrenciaTramite");
            Integer codDocumento = (Integer) parametros.get("codDocumento");
            byte[] fichero = outputStream.toByteArray();

            // Nombre del fichero. Modificar la extension y cambiarla por PDF
            String nombreAntiguo = (String) parametros.get("nombreDocumento");
            m_Log.debug("nombreAntiguo vale: " + nombreAntiguo);
            m_Log.debug("editorTexto vale: " + editorTexto);
            String nombreNuevo = null;
            if (ConstantesDatos.OOFFICE.equals(editorTexto)) {
                nombreNuevo = String.format("%s.%s",
                        StringUtils.removeEndIgnoreCase(nombreAntiguo, ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE_CON_PUNTO),
                        MimeTypes.FILEEXTENSION_PDF);
            } else if (ConstantesDatos.WORD.equals(editorTexto)) {
                nombreNuevo = String.format("%s.%s",
                        StringUtils.removeEndIgnoreCase(nombreAntiguo, ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_WORD_CON_PUNTO),
                        MimeTypes.FILEEXTENSION_PDF);
            } else {
                nombreNuevo = String.format("%s.%s",
                        FilenameUtils.removeExtension(nombreAntiguo),
                        MimeTypes.FILEEXTENSION_PDF);
            }
            
            m_Log.debug("nombreNuevo vale: " + nombreNuevo);

            // Metadatos del documento
            MetadatosDocumentoVO metadatosNuevo = new MetadatosDocumentoVO();
            metadatosNuevo.setCsv(codigoCSV);
            metadatosNuevo.setCsvAplicacion(ConstantesDatos.APLICACION_ORIGEN_DOCUMENTO_FLEXIA);
            parametros.put("extension", MimeTypes.FILEEXTENSION_PDF);
            metadatosNuevo.setCsvUri(generarUriCsvDescargaFichero(parametros));

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("nombreAntiguo: %s", nombreAntiguo));
                m_Log.debug(String.format("nombreNuevo: %s", nombreNuevo));
                m_Log.debug(String.format("metadatosNuevo: %s", metadatosNuevo));
            }

            parametros.put("nombreDocumentoNuevo", nombreNuevo);
            parametros.put("fichero", fichero);
            parametros.put("metadatosNuevo", metadatosNuevo);
            
            // Grabar el documento en el almacen
            //TODO comentar
            grabarDocumentoCSV(parametros, portafirmas);
            
            // Obtenemos la fecha de modificacion del documento y actualizamos los
            // datos de la lista de documentos de tramite del formulario
            Vector<TramitacionExpedientesValueObject> listaDocumentos = tramitesVO.getListaDocumentos();
            Calendar resultFecha = TramitacionExpedientesManager.getInstance().getDocumentoFechaModificacion(
                    codDocumento, ejercicio,
                    codMunicipio, numeroExpediente, codTramite,
                    ocurrenciaTramite, codProcedimiento, params);
            
            String fechaModificacion = DateOperations.toString(resultFecha, DateOperations.LATIN_DATE_FORMAT);
            
            for (TramitacionExpedientesValueObject doc : listaDocumentos) {
                if (codDocumento.equals(NumberUtils.createInteger(doc.getCodDocumento()))) {
                    doc.setDescDocumento(nombreNuevo);
                    doc.setFechaModificacion(fechaModificacion);
                    //doc.setEditorTexto(null);
                    //doc.setCodPlantilla(null);
                    //doc.setTipoPlantilla(null);
                    break;
                }
            }
            
            documentoTramite = new DocumentoTramiteVO();
            documentoTramite.setCodigo(String.valueOf(codDocumento));
            documentoTramite.setNombre(nombreNuevo);
            documentoTramite.setFechaModificacion(fechaModificacion);
        } catch (Exception e) {
            throw new TechnicalException("Error al intentar almacenar el fichero", e);
        }
        return documentoTramite;
    }
    
    private void grabarDocumentoCSV(Map<String, Object> parametros, String portafirmas) throws TechnicalException {
        log.debug("grabarDocumentoCSV");

//        try {
            UsuarioValueObject usuario = (UsuarioValueObject) parametros.get("usuario");
            String[] params = (String[]) parametros.get("paramsBBDD");
            Integer codMunicipio = (Integer) parametros.get("codMunicipio");
            String codProcedimiento = (String) parametros.get("codProcedimiento");
            Integer ejercicio = (Integer) parametros.get("ejercicio");
            String numeroExpediente = (String) parametros.get("numExpediente");
            Integer codTramite = (Integer) parametros.get("codTramite");
            Integer ocurrenciaTramite = (Integer) parametros.get("ocurrenciaTramite");
            Integer codDocumento = (Integer) parametros.get("codDocumento");
            Integer codUsuario = usuario.getIdUsuario();
            String codOrganizacion = Integer.toString(usuario.getOrgCod());
            String nombreDocumento = (String) parametros.get("nombreDocumentoNuevo");
            MetadatosDocumentoVO metadatos = (MetadatosDocumentoVO) parametros.get("metadatosNuevo");
            Boolean insertarMetadatoEnBBDD = Boolean.FALSE;
            byte[] fichero = (byte[]) parametros.get("fichero");

            if (m_Log.isDebugEnabled()) {
                log.debug(String.format("codMunicipio: %d", codMunicipio));
                log.debug(String.format("codProcedimiento: %s", codProcedimiento));
                log.debug(String.format("ejercicio: %d", ejercicio));
                log.debug(String.format("numeroExpediente: %s", numeroExpediente));
                log.debug(String.format("codTramite: %d", codTramite));
                log.debug(String.format("ocurrenciaTramite: %d", ocurrenciaTramite));
                log.debug(String.format("codDocumento: %d", codDocumento));
                log.debug(String.format("codUsuario: %d", codUsuario));
                log.debug(String.format("nombreDocumento: %s", nombreDocumento));
                log.debug(String.format("numeroDocumento: %d", codDocumento));
                log.debug(String.format("metadatos: %s", metadatos));
                log.debug(String.format("tamanho fichero: %d", fichero.length));
            }

            Hashtable<String, Object> datos = new Hashtable<String, Object>();
            datos.put("codMunicipio", Integer.toString(codMunicipio));
            datos.put("codProcedimiento", codProcedimiento);
            datos.put("ejercicio", Integer.toString(ejercicio));
            datos.put("numeroExpediente", numeroExpediente);
            datos.put("codTramite", Integer.toString(codTramite));
            datos.put("ocurrenciaTramite", Integer.toString(ocurrenciaTramite));
            datos.put("codDocumento", Integer.toString(codDocumento));
            datos.put("codUsuario", Integer.toString(codUsuario));
            datos.put("nombreDocumento", nombreDocumento);
            datos.put("numeroDocumento", Integer.toString(codDocumento));
            datos.put("perteneceRelacion", "false");
            datos.put("params", params);
            datos.put("fichero", fichero);
            
            if (metadatos != null) {
                // CSV
                if (StringUtils.isNotEmpty(metadatos.getCsv())) {
                    datos.put("metadatoDocumentoCsv", metadatos.getCsv());
                    datos.put("metadatoDocumentoCsvAplicacion", metadatos.getCsvAplicacion());
                    datos.put("metadatoDocumentoCsvUri", metadatos.getCsvUri());
                    insertarMetadatoEnBBDD = Boolean.TRUE;
                }
            }
            datos.put("insertarMetadatosEnBBDD", insertarMetadatoEnBBDD);

            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrganizacion).getImplClassPluginProcedimiento(codOrganizacion, codProcedimiento);
            Documento doc = null;
            int tipoDocumento = -1;
            if (!almacen.isPluginGestor()) {
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
            } else { 
                
                String editorTexto = "";
                editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTexto(usuario.getOrgCod(),
                        numeroExpediente, codTramite,
                        ocurrenciaTramite, codDocumento, false, params);
                obtenerExtensionYTipoMime(editorTexto, nombreDocumento, datos);

                tipoDocumento = obtenerListaCarpetas(codOrganizacion, codProcedimiento, codTramite, params, datos, almacen, usuario, numeroExpediente);
            }

            doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
            
            log.debug(String.format("doc.getNombreDocumento(): %s", doc.getNombreDocumento()));
            
            //Si el Portafirmas es de tipo Lanbide no es necesario guardar los datos ya que Lanbide crearía 
            //un nuevo registro y los OID de los documentos no serian los correctos 
            try {
                if (!almacen.setDocumento(doc)) {
                    throw new TechnicalException("El documento no ha sido guardado");
                }
            } catch (AlmacenDocumentoTramitacionException adte) {
                throw new TechnicalException("Error al intentar grabar el documento", adte);
            }
    }

    private int obtenerListaCarpetas(String codOrganizacion, String codProcedimiento, Integer codTramite, String[] params, Hashtable<String, Object> datos, 
            AlmacenDocumento almacen, UsuarioValueObject usuario, String numeroExpediente) {
        int tipoDocumento;
        String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(codOrganizacion, codProcedimiento, String.valueOf(codTramite), params);
        String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);
        datos.put("codigoVisibleTramite", codigoVisibleTramite);
        datos.put("codificacion", ConstantesDatos.CODIFICACION_UTF_8);
        /**
         * SE INDICA POR ORDEN CUALES SER\C1N LAS CARPETAS EN LAS QUE SE
         * ALOJAR\C1N EL DOCUMENTO EN EL GESTOR DOCUMENTAL *
         */
        ResourceBundle bundleDocumentos = ResourceBundle.getBundle("documentos");
        String carpetaRaiz = bundleDocumentos.getString(String.format("%s%s%s%s%s",
                ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO,
                codOrganizacion,
                ConstantesDatos.BARRA,
                almacen.getNombreServicio(),
                ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ));
        String descripcionOrganizacion = usuario.getOrg();
        ArrayList<String> listaCarpetas = new ArrayList<String>();
        listaCarpetas.add(carpetaRaiz);
        listaCarpetas.add(String.format("%s%s%s", codOrganizacion, ConstantesDatos.GUION, descripcionOrganizacion));
        listaCarpetas.add(String.format("%s%s%s", codProcedimiento, ConstantesDatos.GUION, nombreProcedimiento));
        listaCarpetas.add(numeroExpediente.replaceAll(ConstantesDatos.BARRA, ConstantesDatos.GUION));
        datos.put("listaCarpetas", listaCarpetas);
        tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
        return tipoDocumento;
    }

    private void obtenerExtensionYTipoMime(String editorTexto, String nombreDocumento, Hashtable<String, Object> datos) {
        String extension = null;
        String tipoMime = null;
        if (ConstantesDatos.OOFFICE.equals(editorTexto)) {
            extension = ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE;
            tipoMime = ConstantesDatos.TIPO_MIME_DOCUMENTO_OPENOFFICE;
        } else if (ConstantesDatos.WORD.equals(editorTexto)) {
            extension = ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_WORD;
            tipoMime = ConstantesDatos.TIPO_MIME_DOC_TRAMITES;
        } else {
            extension = FilenameUtils.getExtension(nombreDocumento);
            tipoMime = MimeTypes.guessMimeTypeFromExtension(extension);
        }
        datos.put("extension", extension);
        datos.put("tipoMime", tipoMime);
    }
    
    private Map<String, Object> obtenerParametrosCrearCSV(HttpServletRequest request, HttpSession session, String portafirmas) throws Exception{
        Map<String, Object> parametros = new HashMap<String, Object>();

        FichaExpedienteForm fichaExpForm = (FichaExpedienteForm) session.getAttribute("FichaExpedienteForm");
        TramitacionExpedientesForm tramExpForm = (TramitacionExpedientesForm) session.getAttribute("TramitacionExpedientesForm");
        TramitacionExpedientesValueObject tramExpVO = tramExpForm.getTramitacionExpedientes();
        
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();
        String sessionId = request.getSession().getId();
        String contextPath = request.getContextPath();
        Integer codDocumento = NumberUtils.createInteger(request.getParameter("codDocumento"));
        Integer ejercicio = NumberUtils.createInteger(tramExpVO.getEjercicio());
        Integer codMunicipio = NumberUtils.createInteger(tramExpVO.getCodMunicipio());
        String numExpediente = tramExpVO.getNumeroExpediente();
        Integer codTramite = NumberUtils.createInteger(tramExpVO.getCodTramite());
        Integer ocurrenciaTramite = NumberUtils.createInteger(tramExpVO.getOcurrenciaTramite());
        String codProcedimiento = tramExpVO.getCodProcedimiento();
        Boolean isExpHistorico = fichaExpForm.isExpHistorico();
        String nombreDocumento = request.getParameter("nombreDocumento");
        String tipoMimeOriginal = null;
        String extension = null;

        String editorTexto = request.getParameter("editorTexto");
        
        tipoMimeOriginal = DocumentOperations.determinarTipoMimePlantilla(
                editorTexto, nombreDocumento);
        extension = MimeTypes.guessExtensionFromMimeType(tipoMimeOriginal);

        // Asignamos los valores al mapa
        parametros.put("usuario", usuario);
        parametros.put("paramsBBDD", params);
        parametros.put("sessionId", sessionId);
        parametros.put("contextPath", contextPath);
        parametros.put("tramitesVO", tramExpVO);
        parametros.put("codDocumento", codDocumento);
        parametros.put("ejercicio", ejercicio);
        parametros.put("codMunicipio", codMunicipio);
        parametros.put("numExpediente", numExpediente);
        parametros.put("codTramite", codTramite);
        parametros.put("ocurrenciaTramite", ocurrenciaTramite);
        parametros.put("codProcedimiento", codProcedimiento);
        parametros.put("isExpHistorico", isExpHistorico);
        parametros.put("editorTexto", editorTexto);
        parametros.put("nombreDocumento", nombreDocumento);
        parametros.put("extension", extension);
        parametros.put("tipoMimeOriginal", tipoMimeOriginal);

        m_Log.debug("Parametros obtenidos del nombre del documento en el metodo obtenerParametrosCrearCSV");
        m_Log.debug("nombreDocumento = " + request.getParameter("nombreDocumento"));
        m_Log.debug("tipoMimeOriginal = " + tipoMimeOriginal);
        m_Log.debug("extension = " + extension);
        
        
        if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
            String OIDDocumento = "";
            m_Log.debug("Tiene seleccionada la propiedad Portafirmas a Lanbide y se devolvera el OID del documento a enviar");
            AdaptadorSQLBD adapt = new AdaptadorSQLBD((String[]) parametros.get("paramsBBDD"));
        
            try {
                //Se obtiene el OID del documento de la tabla MELANBIDE_DOKUSI_RELDOC_TRAMIT
                OIDDocumento = DocumentosLanbideManager.getInstance().obtenerOIDDocumento(codMunicipio, ejercicio,
                        codProcedimiento, numExpediente, codTramite, ocurrenciaTramite,
                        codDocumento.toString(), adapt);
            } catch (Exception ex) {
                 m_Log.error("Se produce un error a la hora de obtener el OID del documento. " + ex.getMessage());
                 throw new Exception ("Se produce un error a la hora de obtener el OID del documento. " + ex.getMessage());
            }

            m_Log.debug("OID Documento vale: " + OIDDocumento);
            parametros.put("OIDDocumento", OIDDocumento.trim());
            
            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(
                    Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),codProcedimiento);
            
            parametros.put("nombreServicioAlmacen", almacen.getNombreServicio());
            
            
        }

        return parametros;
    }

    private String generarUriCsvDescargaFichero(Map<String, Object> parametros) {
        DocumentoManager documentoManager = DocumentoManager.getInstance();
        Map<String, Object> paramUrlCsv = new HashMap<String, Object>();

        Integer codMunicipio = (Integer) parametros.get("codMunicipio");
        String numExpediente = (String) parametros.get("numExpediente");
        Integer codTramite = (Integer) parametros.get("codTramite");
        Integer ocurrenciaTramite = (Integer) parametros.get("ocurrenciaTramite");
        Integer codDocumento = (Integer) parametros.get("codDocumento");
        
        //<DOC>-<codMunicipio>-<numExpediente>-<codTramite>-<ocurrenciaTramite>-<numeroDocumento>
        String codigo = String.format("%s-%d-%s-%d-%d-%d",
                ConstantesDatos.VER_DOCUMENTO_SERVLET_PREFIJO_CODIGO_DOCUMENTO_TRAMITE,
                codMunicipio,
                numExpediente,
                codTramite,
                ocurrenciaTramite,
                codDocumento);
        
        paramUrlCsv.put("codigo", codigo);
        paramUrlCsv.put("opcion", "7");
        paramUrlCsv.put("expHistorico", parametros.get("isExpHistorico"));

        return documentoManager.crearURLCodigoSeguroVerificacion(paramUrlCsv);
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
                   m_Log.error("Eror al obtener la propiedad que indica la ruta en la que se almacenan los documentos en disco: " + e.getMessage());
                   
                 
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
      
      private DocumentoFirma duplicarDocumentoAntesEnviarFirmar(int usuOrg, Map<String, Object> parametros, Integer codDocOriginal) throws AlmacenDocumentoTramitacionException {
        m_Log.info("DocumentosExpedienteAction.duplicarDocumentoAntesEnviarFirmar()");
        DocumentoFirma docFirma = null;
        int tipoDocumento = -1;
        String[] params = (String[]) parametros.get("paramsBBDD");
        String codProcedimiento = (String) parametros.get("codProcedimiento");

        // Comprobamos cual es el plugin de almacen de documentos para el procedimiento
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuOrg)).getImplClassPluginProcedimiento(Integer.toString(usuOrg), codProcedimiento);
        // Instanciamos el documento con una clase u otra en funcion de si el plugin de almacenamiento es de gestor documental o es bbdd
        if (!almacen.isPluginGestor()) {
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        } else {
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
        }
        
        m_Log.info("nombreServicioAlmacen: " + almacen.getNombreServicio());
        parametros.put("nombreServicioAlmacen", almacen.getNombreServicio());
        
        // Construimos el mapa con los datos del documento para obtener el objeto Documento de la implementacion correcta (BBDD o Gestor)
        m_Log.info("Construimos el mapa con los datos del documento para obtener el objeto Documento de la implementacion correcta (BBDD o Gestor)");
        Hashtable<String, Object> datos = DocumentoTramitacionHelper.construirMapaDatosDocumentoTramitacion((HashMap) parametros, tipoDocumento);
        
        m_Log.info("obtener el documento Firma");
        docFirma = DocumentoTramitacionFactoria.getInstance().getDocumentoFirma(datos, tipoDocumento);
        
        codDocOriginal = docFirma.getCodDocumento();
        
        // Llamamos a la funcion que duplica el documento segun el plugin de almacen documental
        m_Log.info("Llamamos a la funcion que duplica el documento segun el plugin de almacen documental");
        almacen.setDocumentoDuplicado(docFirma, params);

        return docFirma;
    }
      
      private void eliminarDuplicadoDocumento(int usuOrg, DocumentoFirma duplicado,  String[] params) throws AlmacenDocumentoTramitacionException{
          m_Log.info("DocumentosExpedienteAction.eliminarDuplicadoDocumento()");
          DocumentoFirma doc = null;
          
        if (duplicado != null) {
          String codProcedimiento = duplicado.getCodProcedimiento();
          
          Integer codMunicipio = duplicado.getCodMunicipio();
          Integer ejercicio = duplicado.getEjercicio();
          String numeroExpediente = duplicado.getNumExpediente();
          Integer codTramite = duplicado.getCodTramite();
          Integer ocurrenciaTramite = duplicado.getOcurrenciaTramite();
          Integer codDocumento = duplicado.getCodDocumento();
          //Comprobamos cual es el plugin de almacen de documentos para el procedimiento
          AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuOrg)).getImplClassPluginProcedimiento(Integer.toString(usuOrg), codProcedimiento);
          
          // Instanciamos el documento con una clase u otra en funcion de si el plugin de almacenamiento es de gestor documental o es bbdd
            if (!almacen.isPluginGestor()) {
                doc = new DocumentoFirmaBBDD();
            } else {
                doc = new DocumentoFirmaGestor();
            }
            doc.setCodMunicipio(codMunicipio);
            doc.setCodProcedimiento(codProcedimiento);
            doc.setEjercicio(ejercicio);
            doc.setNumExpediente(numeroExpediente);
            doc.setCodTramite(codTramite);
            doc.setOcurrenciaTramite(ocurrenciaTramite);
            doc.setCodDocumento(codDocumento);
            
            // Llamamos a la funcion que duplica el documento segun el plugin de almacen documental
            almacen.eliminarDocumentoDuplicado(doc, params);
      }
    }
      
     
    /**
     * Metodo que devuelve un mapa con los datos necesarios 
     * para obtener un documento
     * @param parametros
     * @return 
     */
    private Hashtable<String, Object> datosParaObtenerDocumento (Map<String,Object> parametros) {
        Hashtable<String, Object> datos = new Hashtable<String, Object>();
        
        UsuarioValueObject usuario = (UsuarioValueObject) parametros.get("usuario");
        String[] params = (String[]) parametros.get("paramsBBDD");
        Integer codMunicipio = (Integer) parametros.get("codMunicipio");
        String codProcedimiento = (String) parametros.get("codProcedimiento");
        Integer ejercicio = (Integer) parametros.get("ejercicio");
        String numeroExpediente = (String) parametros.get("numExpediente");
        Integer codTramite = (Integer) parametros.get("codTramite");
        Integer ocurrenciaTramite = (Integer) parametros.get("ocurrenciaTramite");
        Integer codDocumento = (Integer) parametros.get("codDocumento");
        Integer codUsuario = usuario.getIdUsuario();
        
        datos.put("codMunicipio", Integer.toString(codMunicipio));
        datos.put("codProcedimiento", codProcedimiento);
        datos.put("ejercicio", Integer.toString(ejercicio));
        datos.put("numeroExpediente", numeroExpediente);
        datos.put("codTramite", Integer.toString(codTramite));
        datos.put("ocurrenciaTramite", Integer.toString(ocurrenciaTramite));
        datos.put("codDocumento", Integer.toString(codDocumento));
        datos.put("codUsuario", Integer.toString(codUsuario));
        datos.put("numeroDocumento", Integer.toString(codDocumento));
        datos.put("perteneceRelacion", "false");
        datos.put("params", params);
        
        return datos;
    }
      
      
    /**
     * Metodo utilizado para descargar un documento
     * @param parametros
     * @return
     * @throws TechnicalException 
     */
    private byte[] obtenerDocumento (Map<String,Object> parametros) throws TechnicalException {
         
        byte[] fichero = null;
        String nombreFichero = null;
        String tipoContenido = null;
        
        UsuarioValueObject usuario = (UsuarioValueObject) parametros.get("usuario");
        String codOrganizacion = Integer.toString(usuario.getOrgCod());
        String[] params = (String[]) parametros.get("paramsBBDD");
        Integer codMunicipio = (Integer) parametros.get("codMunicipio");
        String codProcedimiento = (String) parametros.get("codProcedimiento");
        Integer ejercicio = (Integer) parametros.get("ejercicio");
        String numero = (String) parametros.get("numExpediente");
        Integer codTramite = (Integer) parametros.get("codTramite");
        Integer ocurrenciaTramite = (Integer) parametros.get("ocurrenciaTramite");
        Integer numeroDocumento = (Integer) parametros.get("codDocumento");
        String nombreDocumento = (String) parametros.get("nombreDocumento");
        Boolean isExpHistorico = (Boolean) parametros.get("isExpHistorico");
        
        
        Hashtable<String,Object> datos = datosParaObtenerDocumento(parametros);
        datos.put("nombreDocumento", nombreDocumento);
        
        log.info("params : " + params);

        String editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTexto(codMunicipio,
            numero,codTramite,ocurrenciaTramite, numeroDocumento,false,params);
        log.info("editorTexto : " + editorTexto);

        // Para las viejas plantillas comprobamos si tiene CSV, en cuyo caso se
        // trata como un documento que tiene extension en el nombre de fichero.
        if (ConstantesDatos.OOFFICE.equals(editorTexto) || ConstantesDatos.WORD.equals(editorTexto)) {
            // Se verifica si tiene CSV, si tiene CSV es que no es un documento de tipo WORD o OOFFICE
            // Por lo que el fichero tiene extension
            if (DocumentoManager.getInstance().existeMetadatoCSV(
                    params, ConstantesDatos.SUBCONSULTA_E_CRD_ID_METADATO_PK,
                    codMunicipio,
                    codProcedimiento,
                    ejercicio,
                    numero,
                    codTramite,
                    ocurrenciaTramite,
                    numeroDocumento)) {
                log.info("editorTexto valdra vacio");
                editorTexto = "";
            }
        }

        obtenerExtensionYTipoMime(editorTexto, nombreDocumento, datos);      

        log.info("Se obtiene el almacen de documentos para descargar el documento");
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),codProcedimiento);
        Documento doc = null;

        int tipoDocumento = -1;
        if(!almacen.isPluginGestor()){
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        } else{
            tipoDocumento = obtenerListaCarpetas(codOrganizacion, codProcedimiento, codTramite, params, datos, almacen, usuario, numero);
        }
            
        try{
            log.info("Se obtiene el documento");
            doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
            doc.setExpHistorico(isExpHistorico);
            fichero = almacen.getDocumento(doc);

            String nombreFicheroExtension = FilenameUtils.getExtension(doc.getNombreDocumento());
            if (StringUtils.isEmpty(nombreFicheroExtension) || !nombreFicheroExtension.equalsIgnoreCase(doc.getExtension())) {
            nombreFichero = doc.getNombreDocumento() + ConstantesDatos.DOT + doc.getExtension();
            } else {
                nombreFichero = doc.getNombreDocumento();
            }

            tipoContenido  = doc.getTipoMimeContenido();

            if (log.isDebugEnabled()) {
                log.info("Logitud del fichero: " + fichero.length);
                log.info("Nombre del fichero: " + nombreFichero);
                log.info("Tipo contenido fichero: " + tipoContenido);
            }

        }catch(AlmacenDocumentoTramitacionException e){
                            e.printStackTrace();
            log.error("Error al recuperar documento : ", e);
            log.error("Error al recuperar documento : Codigo Error recibido flexia(Regitrado en BBDD ERRDIGIT) y mensaje :" + e.getCodigo() + " - " + e.getMessage());
            fichero  = null;
            nombreFichero = null;
            tipoContenido = null;
            throw new TechnicalException (e.getMessage());
        } 
        return fichero;
     }
}
