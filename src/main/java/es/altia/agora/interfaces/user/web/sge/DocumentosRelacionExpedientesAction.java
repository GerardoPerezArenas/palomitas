package es.altia.agora.interfaces.user.web.sge;

// PAQUETES IMPORTADOS
import es.altia.agora.business.editor.mantenimiento.persistence.manual.DocumentosAplicacionDAO;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.sge.persistence.DocumentosRelacionExpedientesManager;
import es.altia.agora.business.sge.persistence.OperacionesExpedienteManager;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.webservice.tramitacion.servicios.WSException;
import es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.business.SWFirmaDocManager;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.commons.MimeTypes;
import es.altia.util.documentos.DocumentOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.StringTokenizer;


public class DocumentosRelacionExpedientesAction extends ActionSession{

   protected static Log log =
           LogFactory.getLog(DocumentosRelacionExpedientesAction.class.getName());

     protected static Config conf = ConfigServiceHelper.getConfig("techserver");
     
   public ActionForward performSession(ActionMapping mapping,ActionForm form,
      HttpServletRequest request,HttpServletResponse response)
      throws IOException, ServletException
   {
     log.debug("perform");
     ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
     ActionErrors errors = new ActionErrors();
     // Validaremos los parametros del request especificados
     HttpSession session = request.getSession();
     TramitacionExpedientesForm tramExpForm = (TramitacionExpedientesForm)session.getAttribute("TramitacionExpedientesForm");
     
     if (form == null){
         form = new DocumentosExpedienteForm();
         if ("request".equals(mapping.getScope())){
           request.setAttribute(mapping.getAttribute(), form);
         }else{
           session.setAttribute(mapping.getAttribute(), form);
         }
     }
     DocumentosRelacionExpedientesForm myForm = (DocumentosRelacionExpedientesForm)form;
     String opcion = myForm.getOpcion();
     
     
     HashMap mapa=tramExpForm.getMapa();
     if(mapa!=null){
     log.debug("Tamanho del mapa: "+ mapa.size());
     
     }
     
          
     UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
     String[] params = usuario.getParamsCon();

     if ("altaDocumento".equalsIgnoreCase(opcion)){
      log.debug("ALTA DOCUMENTO:    DocumentosRelacionExpedientes");
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codAplicacion",Integer.toString(usuario.getAppCod()));
      gVO.setAtributo("codMunicipio",Integer.toString(usuario.getOrgCod()));
      gVO.setAtributo("codProcedimiento",myForm.getCodProcedimiento());
      gVO.setAtributo("ejercicio",myForm.getEjercicio());
      gVO.setAtributo("numeroRelacion",myForm.getNumeroRelacion());
      gVO.setAtributo("numeroExpediente",myForm.getNumeroExpediente());
      gVO.setAtributo("mapa", tramExpForm.getMapa());
      gVO.setAtributo("codTramite",myForm.getCodTramite());
      gVO.setAtributo("ocurrenciaTramite",myForm.getOcurrenciaTramite());
      /** prueba ***/
      gVO.setAtributo("codPlantilla",myForm.getCodPlantilla());
      /** prueba ***/
      String lCodInteresados = myForm.getListaCodInteresados();
      String lVersInteresados = myForm.getListaVersInteresados();
      Vector listaCodInteresados = listaInteresadosSeleccionados(lCodInteresados);
      Vector listaVersInteresados = listaInteresadosSeleccionados(lVersInteresados);
      gVO.setAtributo("listaCodInteresados",listaCodInteresados);
      gVO.setAtributo("listaVersInteresados",listaVersInteresados);
      gVO.setAtributo("idioma", conf.getString("idiomaDefecto"));
      gVO.setAtributo("opcionGrabar",myForm.getOpcionGrabar());
      log.debug("OPCION GRABAR +++++++++++++++++++++++++++++++++++++++++  " + gVO.getAtributo("opcionGrabar"));
      String xml;
      log.debug("INTERESADOS ++++++  " + lCodInteresados);

      gVO.setAtributo("tipoPlantilla",myForm.getTipoPlantilla());
      log.debug("TIPO PLANTILLA +++++++++++++++++++++++++++++++++++++++++  " + gVO.getAtributo("tipoPlantilla"));

      if (!gVO.getAtributo("tipoPlantilla").equals("UNICO")) {
          if(lCodInteresados == null || lCodInteresados.equals("")) {
            //xml = DocumentosRelacionExpedientesManager.getInstance().consultaXMLRelacion(gVO,params);
              xml = DocumentosRelacionExpedientesManager.getInstance().consultaXMLRelacionDocumentoNormal(gVO,params);
          } else {
            xml = DocumentosRelacionExpedientesManager.getInstance().consultaXML2Relacion(gVO,params);
          }
      } else {
          xml = DocumentosRelacionExpedientesManager.getInstance().consultaXML(gVO,params);
      }
      myForm.setOpcion(null);
      myForm.setNumeroDocumento(null);
      myForm.setDatosXML(xml);
      log.debug("CORRECTO +++++++++++++++++++++++++++++++++++++++++  OK");
      
     }else if ("altaDocumentoOOffice".equalsIgnoreCase(opcion)){
         
         log.debug("ALTA DOCUMENTO open office:    DocumentosRelacionExpedientes");
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codAplicacion",Integer.toString(usuario.getAppCod()));
      gVO.setAtributo("codMunicipio",Integer.toString(usuario.getOrgCod()));
      gVO.setAtributo("codProcedimiento",myForm.getCodProcedimiento());
      gVO.setAtributo("ejercicio",myForm.getEjercicio());
      gVO.setAtributo("numeroRelacion",myForm.getNumeroRelacion());
      gVO.setAtributo("numeroExpediente",myForm.getNumeroExpediente());
      gVO.setAtributo("codTramite",myForm.getCodTramite());
      gVO.setAtributo("ocurrenciaTramite",myForm.getOcurrenciaTramite());
      gVO.setAtributo("mapa", tramExpForm.getMapa());
      String lCodInteresados = myForm.getListaCodInteresados();
      String lVersInteresados = myForm.getListaVersInteresados();
      Vector listaCodInteresados = listaInteresadosSeleccionados(lCodInteresados);
      Vector listaVersInteresados = listaInteresadosSeleccionados(lVersInteresados);
      gVO.setAtributo("listaCodInteresados",listaCodInteresados);
      gVO.setAtributo("listaVersInteresados",listaVersInteresados);
      gVO.setAtributo("idioma", conf.getString("idiomaDefecto"));
      gVO.setAtributo("opcionGrabar",myForm.getOpcionGrabar());
      log.debug("OPCION GRABAR +++++++++++++++++++++++++++++++++++++++++  " + gVO.getAtributo("opcionGrabar"));
      String xml;
      log.debug("INTERESADOS ++++++  " + lCodInteresados);

      gVO.setAtributo("tipoPlantilla",myForm.getTipoPlantilla());
      log.debug("TIPO PLANTILLA +++++++++++++++++++++++++++++++++++++++++  " + gVO.getAtributo("tipoPlantilla"));

      if (!gVO.getAtributo("tipoPlantilla").equals("UNICO")) {
          if(lCodInteresados == null || lCodInteresados.equals("")) {
            xml = DocumentosRelacionExpedientesManager.getInstance().consultaXMLRelacion(gVO,params);
          } else {
            xml = DocumentosRelacionExpedientesManager.getInstance().consultaXML2Relacion(gVO,params);
          }
      } else {
          xml = DocumentosRelacionExpedientesManager.getInstance().consultaXML(gVO,params);
      }
      myForm.setOpcion(null);
      myForm.setNumeroDocumento(null);
      myForm.setDatosXML(xml);
      log.debug("CORRECTO +++++++++++++++++++++++++++++++++++++++++  OK");
      
      
      
     } else if("grabarDocumento".equalsIgnoreCase(opcion)){
         
        String codMunicipio = Integer.toString(usuario.getOrgCod()); 
         
        Hashtable<String,Object> datos = new Hashtable<String,Object>();
        datos.put("codMunicipio",codMunicipio);
        datos.put("codProcedimiento",myForm.getCodProcedimiento());
        datos.put("ejercicio",myForm.getEjercicio());
        datos.put("numeroRelacion",myForm.getNumeroRelacion());
        datos.put("codTramite",myForm.getCodTramite());
        datos.put("ocurrenciaTramite",myForm.getOcurrenciaTramite());
        datos.put("codDocumento",myForm.getCodDocumento());
        datos.put("codUsuario",myForm.getCodUsuario());
        datos.put("nombreDocumento",myForm.getNombreDocumento());
        datos.put("numeroDocumento",myForm.getNumeroDocumento());
        datos.put("perteneceRelacion","true");
        datos.put("params",params);
        datos.put("fichero",myForm.getFicheroWord().getFileData());
        datos.put("opcionGrabar",myForm.getOpcionGrabar());
        
        //AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance().getImplClass(Integer.toString(usuario.getOrgCod()));
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,myForm.getCodProcedimiento());
        Documento doc = null;
        int tipoDocumento = -1;
        if(!almacen.isPluginGestor())
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        else{
            String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(Integer.toString(usuario.getOrgCod()), myForm.getCodProcedimiento(), myForm.getCodTramite(), params);
            String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(myForm.getCodProcedimiento(), params);
            // Se recupera la lista de los expedientes que forman parte de la relación
            ArrayList<String> listadoExpedientes = TramitacionManager.getInstance().getExpedientesRelacion(Integer.toString(usuario.getOrgCod()), myForm.getCodProcedimiento(), myForm.getNumeroRelacion(), myForm.getEjercicio(), params);

            //datos.put("nombreOrganizacion",usuario.getOrg());
            //datos.put("nombreProcedimiento",nombreProcedimiento);
            datos.put("codigoVisibleTramite",codigoVisibleTramite);
            datos.put("listaExpedientesRelacion",listadoExpedientes);
            datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);
            
             String editorTexto="";
            if("".equals(myForm.getNumeroDocumento())||("undefined").equals(myForm.getNumeroDocumento())||("UNDEFINED").equals(myForm.getNumeroDocumento())||(myForm.getNumeroDocumento()==null))
            {
                editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTextoAlta(usuario.getOrgCod(),
                    myForm.getNumeroExpediente(),Integer.parseInt(myForm.getCodTramite()),Integer.parseInt(myForm.getCodDocumento()),params);
                 }else
            {
                editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTexto(usuario.getOrgCod(),
                    myForm.getNumeroExpediente(),Integer.parseInt(myForm.getCodTramite()),
                    Integer.parseInt(myForm.getOcurrenciaTramite()),Integer.parseInt(myForm.getNumeroDocumento()),true,params);
            }
            
            String tipoMime = DocumentOperations.determinarTipoMimePlantilla(
                    editorTexto, myForm.getNombreDocumento());
            datos.put("extension", MimeTypes.guessExtensionFromMimeType(tipoMime));
            datos.put("tipoMime", tipoMime);

            /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
            ResourceBundle bundleDocumentos = ResourceBundle.getBundle("documentos");           
            String carpetaRaiz  = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + usuario.getOrgCod() + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ); 


            String descripcionOrganizacion = usuario.getOrg();
            ArrayList<String> listaCarpetas = new ArrayList<String>();
            listaCarpetas.add(carpetaRaiz);
            listaCarpetas.add(usuario.getOrgCod() + ConstantesDatos.GUION + descripcionOrganizacion);
            listaCarpetas.add(myForm.getCodProcedimiento() + ConstantesDatos.GUION + nombreProcedimiento);
            listaCarpetas.add(ConstantesDatos.PREFIJO_DOCUMENTO_RELACION + myForm.getNumeroRelacion().replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));
            datos.put("listaCarpetas",listaCarpetas);
            /** FIN **/
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
        }

        doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
        /****/        
        boolean documentoGrabado = false;
        try{
            documentoGrabado = almacen.setDocumento(doc);
            OperacionesExpedienteManager.getInstance().registrarAltaDocumentoTramite(doc,usuario.getNombreUsu(),params);
        }catch(AlmacenDocumentoTramitacionException e){
            e.printStackTrace();
            m_Log.error(e.getMessage());
        }

        myForm.setResultado((documentoGrabado)?"ok":"No se ha podido grabar el Documento.");
        myForm.setOpcion(null);

     } else if("modificarDocumento".equalsIgnoreCase(opcion) || "modificarDocumentoOOffice".equalsIgnoreCase(opcion)
             || "verDocumentoOOffice".equalsIgnoreCase(opcion)){
     	myForm.setOpcion(null);

         // Opcion de Visualizar Documentos asociados a asientos del registro.
         // Solo si la integracion con firmadoc esta activada.
     } else if(opcion.equalsIgnoreCase("visualizarDocAsiento")) {
         visualizarDocumento(request, response, params);
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

    /* Metodo encargado de extraer el contenido del documento del servicio web y visualizarlo. */
    private void visualizarDocumento(HttpServletRequest request, HttpServletResponse response,
                                     String[] params)
    throws WSException {

        byte[] documentoExtraido;

        try {

            // Obtenemos el contenido del documento.
            String codigoDocumento = request.getParameter("codigoDoc");
            SWFirmaDocManager manager = SWFirmaDocManager.getInstance(params);
            documentoExtraido = manager.obtenerDocumento(codigoDocumento);

            // Visualizamos el documento.
            response.setContentType("text/html");
            response.setContentLength(documentoExtraido.length);
            ServletOutputStream out = response.getOutputStream();
            BufferedOutputStream bufferOut = new BufferedOutputStream(out);
            bufferOut.write(documentoExtraido, 0, documentoExtraido.length);

            m_Log.debug("ESCRIBE LA SALIDA");

            bufferOut.close();
            out.close();

        } catch (WSException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw new WSException("WebService Exception:" + e.getMessage());
        }
    }
}