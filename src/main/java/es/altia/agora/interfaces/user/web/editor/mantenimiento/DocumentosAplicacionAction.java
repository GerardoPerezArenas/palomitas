package es.altia.agora.interfaces.user.web.editor.mantenimiento;

// PAQUETES IMPORTADOS
import java.io.*;
import com.google.gson.Gson;
import es.altia.agora.business.administracion.mantenimiento.persistence.IdiomasManager;	
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.editor.mantenimiento.persistence.DocumentosAplicacionManager;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.EstructuraEtiquetaModuloIntegracionVO;
import es.altia.agora.interfaces.user.web.sge.FichaExpedienteDocumentoForm;
import es.altia.util.ajax.respuesta.RespuestaAjaxUtils;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.util.struts.StrutsFileValidation;
import org.apache.struts.upload.FormFile;


import java.util.ArrayList;


public class DocumentosAplicacionAction extends ActionSession{

   protected static Log log =
           LogFactory.getLog(DocumentosAplicacionAction.class.getName());

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
         form = new DocumentosAplicacionForm();
         if ("request".equals(mapping.getScope())){
           request.setAttribute(mapping.getAttribute(), form);
         }else{
           session.setAttribute(mapping.getAttribute(), form);
         }
     }
     DocumentosAplicacionForm myForm = (DocumentosAplicacionForm)form;
     String opcion = myForm.getOpcion();

     UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
     String[] params = usuario.getParamsCon();
     int codOrganizacion=usuario.getOrgCod();
     String codigoProcedimiento= myForm.getCodProcedimiento();
     String numeroExpediente=myForm.getNumeroExpediente();
     if (m_Log.isInfoEnabled()) m_Log.debug("Codigo organizacion:"+codOrganizacion);  
     if (m_Log.isInfoEnabled()) m_Log.debug("Codigo procedimiento:"+ myForm.getCodProcedimiento());
      if (m_Log.isInfoEnabled()) m_Log.debug("Codigo expediente:"+ myForm.getNumeroExpediente());



     if ("cargarAplicaciones".equalsIgnoreCase(opcion)){
       //Definimos una cadena con el CODIGO de la Aplicacion Documentos
       //para que cargue todas las Aplicaciones que haya en la tabla
       //menos la Documentos que tiene el código 6.
       String codigoAplicacionDocumentos="6";
       Vector lista         = DocumentosAplicacionManager.getInstance().loadAplicaciones(params,codigoAplicacionDocumentos);
       Vector listaProc     = DocumentosAplicacionManager.getInstance().loadProcedimientos(params);
       Vector listaTramites = DocumentosAplicacionManager.getInstance().loadTramites(params);
       Vector listaPlantillaActiva = DocumentosAplicacionManager.getInstance().loadDespPlantillaActiva(params,"BOOL");

       myForm.limpiar();
       myForm.setListaAplicaciones(lista);
       myForm.setListaProcedimientos(listaProc);
       myForm.setListaTramites(listaTramites);
       myForm.setListaActivos(listaPlantillaActiva);
     }
     else if("cargarDocumentos".equals(opcion))
     {
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codigoAplicacion",myForm.getCodAplicacion());
        gVO.setAtributo("codProcedimiento",myForm.getCodProcedimiento());
        gVO.setAtributo("codTramite",myForm.getCodTramite());
        gVO.setAtributo("codActivo", myForm.getCodActivo());

        if(myForm.getCodTramiteSeleccionado()!=null){
            gVO.setAtributo("codTramite", myForm.getCodTramiteSeleccionado());
        }
        Vector lista = DocumentosAplicacionManager.getInstance().loadDocumentos(gVO,params);
        myForm.setListaDocumentos(lista);
        myForm.setOpcion(null);
        
         String peticionAjax = request.getParameter("ajax");	
        if(peticionAjax!=null && peticionAjax.equalsIgnoreCase("si")){	
            RespuestaAjaxUtils.retornarJSON(new Gson().toJson(lista), response);	
            return null;	
        }
     }else if("cargarDocumentosDesdeDefinicion".equals(opcion))
     {
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codigoAplicacion",myForm.getCodAplicacion());
        gVO.setAtributo("codProcedimiento",myForm.getCodProcedimiento());
        gVO.setAtributo("codTramite",myForm.getCodTramite());

       
        
        if(gVO.getAtributo("codigoAplicacion").equals("4") && (gVO.getAtributo("codProcedimiento").equals("")||gVO.getAtributo("codTramite").equals("")))
		{
			opcion = "noHacerNada";
	    }
	    else
	    {
        	Vector lista = DocumentosAplicacionManager.getInstance().loadDocumentosDesdeDefinicion(gVO,params);
        	myForm.setListaDocumentos(lista);
        	opcion = "cargarDocumentos";
		}
        myForm.setOpcion("");
     }
     else if("eliminarDocumento".equals(opcion))
     {
        int resultadoEliminacion;  
       DocumentosAplicacionForm documentosAplicacionForm = (DocumentosAplicacionForm)form;
        GeneralValueObject gVO = new GeneralValueObject();
        int cM = usuario.getOrgCod();
        String codMunicipio = Integer.toString(cM);
        gVO.setAtributo("codMunicipio",codMunicipio);
        gVO.setAtributo("codProcedimiento", request.getParameter("codProcedimiento"));
        gVO.setAtributo("codTramite",request.getParameter("codTramite"));
        gVO.setAtributo("codDocumento",myForm.getCodDocumento());
        gVO.setAtributo("codigoAplicacion", request.getParameter("codAplicacion"));
        gVO.setAtributo("codigoPlantilla",myForm.getCodDocumento());
        gVO.setAtributo("docActivo", myForm.getDocActivo());
        
         myForm.setCodAplicacion((String)request.getParameter("codAplicacion"));   
         myForm.setCodProcedimiento((String)request.getParameter("codProcedimiento"));
        if(myForm.getCodTramiteSeleccionado()!=null && !"".equals(myForm.getCodTramiteSeleccionado())){
            gVO.setAtributo("codTramite", myForm.getCodTramiteSeleccionado());
        }
         myForm.setCodTramite((String)gVO.getAtributo("codTramite"));
         if (m_Log.isDebugEnabled()){
             m_Log.debug("DOC ACTIVO = " + documentosAplicacionForm.getDocActivo());
             m_Log.debug("DOC ACTIVO = " + myForm.getDocActivo());
         } 
     
        if(("".equals(gVO.getAtributo("codProcedimiento")))&&("".equals(gVO.getAtributo("codTramite")))){
             resultadoEliminacion = DocumentosAplicacionManager.getInstance().eliminaDocumentoAplicacion(gVO,params);
        }else{
        
            resultadoEliminacion = DocumentosAplicacionManager.getInstance().eliminaDocumento(gVO,params);
        }
       
       // myForm.setResultadoEliminarDocumentos(Integer.toString(resultadoEliminacion));
        myForm.setListaDocumentos(new Vector());
        myForm.setOpcion(null);
      
         }else if("visibleExterior".equals(opcion)){	
         m_Log.debug("Entra en visibleExterior");	
         int resultadosCambio;	
         DocumentosAplicacionForm documentosAplicacionForm= (DocumentosAplicacionForm)form;	
         GeneralValueObject gVO=new GeneralValueObject();	
         	
         gVO.setAtributo("codProcedimiento", request.getParameter("codProcedimiento"));	
         gVO.setAtributo("codTramite", request.getParameter("codTramite"));	
         gVO.setAtributo("codDocumento", myForm.getCodDocumento());	
         gVO.setAtributo("codAplicacion", request.getParameter("codAplicacion"));	
         gVO.setAtributo("codigoPlantilla", myForm.getCodDocumento());	
         gVO.setAtributo("visibleExt", myForm.getVisibleExt());	
         	
         myForm.setCodAplicacion((String)request.getParameter("codAplicacion"));	
         myForm.setCodProcedimiento((String)request.getParameter("codProcedimiento"));	
         m_Log.debug("tramite seleccionado"+myForm.getCodTramiteSeleccionado());	
         if((myForm.getCodTramiteSeleccionado()!=null && !"".equals(myForm.getCodTramiteSeleccionado())) || (myForm.getCodTramite()!=null && !"".equals(myForm.getCodTramite()))){	
             gVO.setAtributo("codTramite", myForm.getCodTramiteSeleccionado());	
             m_Log.debug("Visible Exterior ="+myForm.getVisibleExt());	
             resultadosCambio=DocumentosAplicacionManager.getInstance().documentoVisibleExterior(gVO, params);	
         }	
         myForm.setListaDocumentos(new Vector());	
         myForm.setOpcion(null);
        
        
     } else if ("verDocumento".equals(opcion)) {
         GeneralValueObject gVO = new GeneralValueObject();
         int cM = usuario.getOrgCod();
         String codTramite = "";
         if(usuario.getAppCod()==4) codTramite= myForm.getCodTramite();
         else if (usuario.getAppCod()==6) codTramite=myForm.getCodTramiteSeleccionado();
         String codMunicipio = Integer.toString(cM);
         gVO.setAtributo("codMunicipio", codMunicipio);
         gVO.setAtributo("codigoAplicacion", myForm.getCodAplicacion());
         gVO.setAtributo("codProcedimiento", myForm.getCodProcedimiento());
         gVO.setAtributo("codTramite", codTramite);
         gVO.setAtributo("codDocumento", myForm.getCodDocumento());
         gVO.setAtributo("interesado", myForm.getInteresado());
         gVO.setAtributo("relacion", myForm.getRelacion());
         gVO.setAtributo("docActivo", myForm.getDocActivo());
         Vector listaIdiomas = IdiomasManager.getInstance().getListaIdiomas(params);	
         gVO.setAtributo("listaIdiomas", listaIdiomas);

         /** prueba **/
         TraductorAplicacionBean traductor = new TraductorAplicacionBean();
         traductor.setApl_cod(4);
         traductor.setIdi_cod(usuario.getIdioma());         
         gVO.setAtributo("sufijoEtiquetaFechaAlternativa",traductor.getDescripcion("msgFechaDocAlternativa"));
         /** prueba **/
         
         
         
         //Recuperamos los campos de los modulos externos por los que se puede filtrar
          ArrayList<ModuloIntegracionExterno> modulos= new ArrayList<ModuloIntegracionExterno>();
          ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetasModulo=new ArrayList<EstructuraEtiquetaModuloIntegracionVO>();
          modulos=
                  ModuloIntegracionExternoFactoria.getInstance().getImplClassModuloConEtiquetas(codOrganizacion,
                  codigoProcedimiento, params,(String)gVO.getAtributo("interesado"),
                  (String)gVO.getAtributo("relacion"));
        
          //Imprimimos para debug los modulos..
            for (int i = 0; i < modulos.size(); i++) {
                ModuloIntegracionExterno modulo=modulos.get(i); 
                 etiquetasModulo.addAll(modulo.getEtiquetas());
               if (m_Log.isInfoEnabled()) m_Log.debug("Nombre Modulo:"+modulo.getNombreModulo());  
               if (m_Log.isInfoEnabled()) m_Log.debug("Descripcion Modulo:"+modulo.getDescripcionModulo());

            }
          
          if (m_Log.isInfoEnabled()) m_Log.debug("Lista de las etiquetas:"+etiquetasModulo.toString());
         
          
          
          
         m_Log.debug("DocumentosAplicacionAction VER DOCUMENTO");
         m_Log.debug("Interesado ::" + gVO.getAtributo("interesado"));
         m_Log.debug("Relacion ::" + gVO.getAtributo("relacion"));
         try {
             Vector etiquetas = DocumentosAplicacionManager.getInstance().loadEtiquetas(gVO, params);
            //Tenemos que, a partir de nuestro ArrayList<EstructurEtiquetasModuloIntegracionVO> devolver
            //un vector de GeneralValueObject
             Vector<GeneralValueObject> etiquetasModuloVector= new Vector<GeneralValueObject>();
             for(int i=0; i<etiquetasModulo.size(); i++){
                 EstructuraEtiquetaModuloIntegracionVO etiqueta=etiquetasModulo.get(i);
                 GeneralValueObject etiquetaGvo= new GeneralValueObject();
                 etiquetaGvo.setAtributo("codigo", etiqueta.getCodigoEtiqueta());
                 etiquetaGvo.setAtributo("nombre", etiqueta.getNombreEtiqueta());
                 etiquetaGvo.setAtributo("tipo", etiqueta.getTipoEtiqueta());
                 etiquetaGvo.setAtributo("columna",etiqueta.getNombreColumna());
                 etiquetaGvo.setAtributo("sql", etiqueta.getSqlS());
                 etiquetaGvo.setAtributo("whereS",etiqueta.getWhereS());
                 etiquetaGvo.setAtributo("and1",etiqueta.getAnd1());
                 etiquetaGvo.setAtributo("and2",etiqueta.getAnd2());
                 etiquetaGvo.setAtributo("and3",etiqueta.getAnd3());
                 etiquetaGvo.setAtributo("campoAnd1",etiqueta.getCampoAnd1());
                 etiquetaGvo.setAtributo("campoAnd12",etiqueta.getCampoAnd2());
                 etiquetaGvo.setAtributo("campoWhere",etiqueta.getCampoWhere());
            
                 etiquetasModuloVector.add(etiquetaGvo);
             }
             
             
            etiquetas.addAll(etiquetasModuloVector);
             myForm.setListaEtiquetas(etiquetas);
             myForm.setOpcion(null);
             
            
              for (int i=0;i<etiquetas.size();i++){
                 m_Log.debug ("i="+i+"-->" + 
                         (String)(((GeneralValueObject)etiquetas.elementAt(i)).getAtributo("codigo"))
                         +" "+(String)((GeneralValueObject)etiquetas.elementAt(i)).getAtributo("nombre"));
             }
             
             
             
         } catch (TechnicalException te) {
             te.printStackTrace();
             opcion = "errorDocumento";
             request.setAttribute("errorDocumento", "ErrorMessages.Editor.LoadingTagsError");
         }
     }
     
      else if("descargarDocumento".equals(opcion)){
         GeneralValueObject gVO=new GeneralValueObject();
         int cM = usuario.getOrgCod();
         String codTramite = "";
         if(usuario.getAppCod()==4) codTramite= myForm.getCodTramite();
         else if (usuario.getAppCod()==6) codTramite=myForm.getCodTramiteSeleccionado();
         String codMunicipio = Integer.toString(cM);
         gVO.setAtributo("codMunicipio", codMunicipio);
         gVO.setAtributo("codigoAplicacion", myForm.getCodAplicacion());
         gVO.setAtributo("codProcedimiento", myForm.getCodProcedimiento());
         gVO.setAtributo("codTramite", codTramite);
         gVO.setAtributo("codDocumento", myForm.getCodDocumento());
         gVO.setAtributo("interesado", myForm.getInteresado());
         gVO.setAtributo("relacion", myForm.getRelacion());
         gVO.setAtributo("docActivo", myForm.getDocActivo());
         m_Log.debug("DocumentosAplicacion: Descargar Plantilla");
         GeneralValueObject file=DocumentosAplicacionManager.getInstance().loadDocumento(gVO, params);
         if (file != null) {
             try{  
                    String nombrePlantilla=myForm.getNombreDocumento().replace(" ","_");
                    String extension=".odt";
                    byte[] fichero = (byte[]) file.getAtributo("fichero");
                    response.setHeader("Content-Transfer-Encoding", "binary");
                    log.debug("nombre Documento --->"+myForm.getNombreDocumento());
                    response.setHeader("Content-Disposition", "inline; filename=" +nombrePlantilla+extension);
                    response.setContentType("application/vnd.oasis.opendocument.text");
                    response.setContentLength(fichero.length);
                    DataOutputStream out = new DataOutputStream(response.getOutputStream());
                    out.write(fichero, 0, fichero.length);
                    out.flush();
                    out.close();
                    log.debug(fichero.length);
      
            }catch(Exception e){
                 m_Log.error("Excepción capturada: " + e.toString());  
            }
      }else{
          log.warn("FICHERO NULO");
      }
         
     }
     
     else if ("verDocumentoOOffice".equals(opcion)){
     GeneralValueObject gVO = new GeneralValueObject();
         int cM = usuario.getOrgCod();
         String codTramite = "";
         if(usuario.getAppCod()==4) codTramite= myForm.getCodTramite();
         else if (usuario.getAppCod()==6) codTramite=myForm.getCodTramiteSeleccionado();
         String codMunicipio = Integer.toString(cM);
         gVO.setAtributo("codMunicipio", codMunicipio);
         gVO.setAtributo("codigoAplicacion", myForm.getCodAplicacion());
         gVO.setAtributo("codProcedimiento", myForm.getCodProcedimiento());
         gVO.setAtributo("codTramite",codTramite);
         gVO.setAtributo("codDocumento", myForm.getCodDocumento());
         gVO.setAtributo("interesado", myForm.getInteresado());
         gVO.setAtributo("relacion", myForm.getRelacion());
         gVO.setAtributo("docActivo", myForm.getDocActivo());
         m_Log.debug("DocumentosAplicacionAction VER DOCUMENTO");
         m_Log.debug("Interesado ::" + gVO.getAtributo("interesado"));
         m_Log.debug("Relacion ::" + gVO.getAtributo("relacion"));
         
         Vector listaIdiomas = IdiomasManager.getInstance().getListaIdiomas(params);	
         gVO.setAtributo("listaIdiomas", listaIdiomas);
         
          //Recuperamos los campos de los modulos externos por los que se puede filtrar
          ArrayList<ModuloIntegracionExterno> modulos= new ArrayList<ModuloIntegracionExterno>();
          ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetasModulo=new ArrayList<EstructuraEtiquetaModuloIntegracionVO>();
          modulos=
                  ModuloIntegracionExternoFactoria.getInstance().getImplClassModuloConEtiquetas(codOrganizacion,
                  codigoProcedimiento, params,(String)gVO.getAtributo("interesado"),
                  (String)gVO.getAtributo("relacion"));
        
          //Imprimimos para debug los modulos..
            for (int i = 0; i < modulos.size(); i++) {
                ModuloIntegracionExterno modulo=modulos.get(i); 
                 etiquetasModulo.addAll(modulo.getEtiquetas());
               if (m_Log.isInfoEnabled()) m_Log.debug("Nombre Modulo:"+modulo.getNombreModulo());  
               if (m_Log.isInfoEnabled()) m_Log.debug("Descripcion Modulo:"+modulo.getDescripcionModulo());

            }
          
          if (m_Log.isInfoEnabled()) m_Log.debug("Lista de las etiquetas:"+etiquetasModulo.toString());
         
          
         
         try {
             Vector etiquetas = DocumentosAplicacionManager.getInstance().loadEtiquetas(gVO, params);
             
             //Tenemos que, a partir de nuestro ArrayList<EstructurEtiquetasModuloIntegracionVO> devolver
            //un vector de GeneralValueObject
             Vector<GeneralValueObject> etiquetasModuloVector= new Vector<GeneralValueObject>();
             for(int i=0; i<etiquetasModulo.size(); i++){
                 EstructuraEtiquetaModuloIntegracionVO etiqueta=etiquetasModulo.get(i);
                 GeneralValueObject etiquetaGvo= new GeneralValueObject();
                 etiquetaGvo.setAtributo("codigo", etiqueta.getCodigoEtiqueta());
                 etiquetaGvo.setAtributo("nombre", etiqueta.getNombreEtiqueta());
                 etiquetaGvo.setAtributo("tipo", etiqueta.getTipoEtiqueta());
                 etiquetaGvo.setAtributo("columna",etiqueta.getNombreColumna());
                 etiquetaGvo.setAtributo("sql", etiqueta.getSqlS());
                 etiquetaGvo.setAtributo("whereS",etiqueta.getWhereS());
                 etiquetaGvo.setAtributo("and1",etiqueta.getAnd1());
                 etiquetaGvo.setAtributo("and2",etiqueta.getAnd2());
                 etiquetaGvo.setAtributo("and3",etiqueta.getAnd3());
                 etiquetaGvo.setAtributo("campoAnd1",etiqueta.getCampoAnd1());
                 etiquetaGvo.setAtributo("campoAnd12",etiqueta.getCampoAnd2());
                 etiquetaGvo.setAtributo("campoWhere",etiqueta.getCampoWhere());
            
                 etiquetasModuloVector.add(etiquetaGvo);
             }
             
             
            etiquetas.addAll(etiquetasModuloVector);
            myForm.setListaEtiquetas(etiquetas);
            myForm.setOpcion(null);
             
         } catch (TechnicalException te) {
             te.printStackTrace();
             opcion = "errorDocumento";
             request.setAttribute("errorDocumento", "ErrorMessages.Editor.LoadingTagsError");
         }
         
     }
     else if ("verEtiquetasDocumento".equals(opcion)){ // #275367
         m_Log.debug("DocumentosAplicacionAction::verEtiquetasDocumento");
         GeneralValueObject gVO = new GeneralValueObject();
         int codError = 0; // Ejecución OK
         
         int cM = usuario.getOrgCod();
         String codMunicipio = Integer.toString(cM);
         String paraInteresado = request.getParameter("interesado");
         String paraRelacion = request.getParameter("relacion");
         m_Log.debug("Interesado ::" + paraInteresado);
         m_Log.debug("Relacion ::" + paraRelacion);
         String codApp = request.getParameter("codAplicacion");
         if(codApp == null || codApp.equals(""))codApp = String.valueOf(usuario.getAppCod());
         gVO.setAtributo("codMunicipio", codMunicipio);
         gVO.setAtributo("codigoAplicacion", codApp);
         gVO.setAtributo("codProcedimiento", request.getParameter("codProcedimiento"));
         gVO.setAtributo("codTramite",request.getParameter("codTramite"));
         gVO.setAtributo("codDocumento", request.getParameter("codDocumento"));
         gVO.setAtributo("interesado", request.getParameter("interesado"));
         gVO.setAtributo("relacion", request.getParameter("relacion"));
         gVO.setAtributo("docActivo", request.getParameter("docActivo"));
         
         
          //Recuperamos los campos de los modulos externos por los que se puede filtrar
          ArrayList<ModuloIntegracionExterno> modulos= new ArrayList<ModuloIntegracionExterno>();
          ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetasModulo=new ArrayList<EstructuraEtiquetaModuloIntegracionVO>();
          modulos = ModuloIntegracionExternoFactoria.getInstance().getImplClassModuloConEtiquetas(codOrganizacion,
                  codigoProcedimiento, params,paraInteresado,paraRelacion);
        
          //Imprimimos para debug los modulos..
            for (int i = 0; i < modulos.size(); i++) {
                ModuloIntegracionExterno modulo=modulos.get(i); 
                 etiquetasModulo.addAll(modulo.getEtiquetas());
               if (m_Log.isInfoEnabled()) m_Log.debug("Nombre Modulo:"+modulo.getNombreModulo());  
               if (m_Log.isInfoEnabled()) m_Log.debug("Descripcion Modulo:"+modulo.getDescripcionModulo());
            }
          
          if (m_Log.isInfoEnabled()) m_Log.debug("Lista de las etiquetas:"+etiquetasModulo.toString());        
          
         
         Vector etiquetas = null;
         try {
             etiquetas = DocumentosAplicacionManager.getInstance().loadEtiquetasPlantillaODT(gVO, params);
             
             //Tenemos que, a partir de nuestro ArrayList<EstructurEtiquetasModuloIntegracionVO> devolver
            //un vector de GeneralValueObject
             Vector<GeneralValueObject> etiquetasModuloVector= new Vector<GeneralValueObject>();
             for(int i=0; i<etiquetasModulo.size(); i++){
                 EstructuraEtiquetaModuloIntegracionVO etiqueta=etiquetasModulo.get(i);
                 GeneralValueObject etiquetaGvo= new GeneralValueObject();
                 etiquetaGvo.setAtributo("codigo", etiqueta.getCodigoEtiqueta());
                 etiquetaGvo.setAtributo("nombre", etiqueta.getNombreEtiqueta());
                 etiquetaGvo.setAtributo("tipo", etiqueta.getTipoEtiqueta());
                 etiquetaGvo.setAtributo("columna",etiqueta.getNombreColumna());
                 etiquetaGvo.setAtributo("sql", etiqueta.getSqlS());
                 etiquetaGvo.setAtributo("whereS",etiqueta.getWhereS());
                 etiquetaGvo.setAtributo("and1",etiqueta.getAnd1());
                 etiquetaGvo.setAtributo("and2",etiqueta.getAnd2());
                 etiquetaGvo.setAtributo("and3",etiqueta.getAnd3());
                 etiquetaGvo.setAtributo("campoAnd1",etiqueta.getCampoAnd1());
                 etiquetaGvo.setAtributo("campoAnd12",etiqueta.getCampoAnd2());
                 etiquetaGvo.setAtributo("campoWhere",etiqueta.getCampoWhere());
            
                 etiquetasModuloVector.add(etiquetaGvo);
             }
             
             
            etiquetas.addAll(etiquetasModuloVector);
             
         } catch (TechnicalException te) {
             te.printStackTrace();
             codError = 2;
         }
         
        if(etiquetas==null || etiquetas.isEmpty())
            codError = 1; // No se han obtenido etiquetas
        
         //Enviamos la respuesta a la petición ajax en formato json
         GeneralValueObject resultado = new GeneralValueObject();
         resultado.setAtributo("paraInteresado", paraInteresado);
         resultado.setAtributo("error", codError);
         resultado.setAtributo("etiquetas", etiquetas);
         RespuestaAjaxUtils.retornarJSON(new Gson().toJson(resultado), response);
         
     } 
     else if("grabarDocumento".equals(opcion))
     {
        GeneralValueObject gVO = new GeneralValueObject();
        int cM = usuario.getOrgCod();
        String codMunicipio = Integer.toString(cM);
        gVO.setAtributo("codMunicipio",codMunicipio);
        gVO.setAtributo("codigoAplicacion",myForm.getCodAplicacion());
        gVO.setAtributo("codProcedimiento",myForm.getCodProcedimiento());
        gVO.setAtributo("codTramite",myForm.getCodTramite());
        gVO.setAtributo("codDocumento",myForm.getCodDocumento());
        gVO.setAtributo("ficheroWord",myForm.getFicheroWord().getFileData());
        gVO.setAtributo("nombreDocumento",myForm.getNombreDocumento());
        gVO.setAtributo("interesado",myForm.getInteresado());
        gVO.setAtributo("relacion",myForm.getRelacion());
        gVO.setAtributo("docActivo", myForm.getDocActivo());
        gVO.setAtributo("editorTexto", myForm.getEditorTexto());
         m_Log.debug("DocumentosAplicacionAction GRABAR DOCUMENTO");
         m_Log.debug("Interesado ::"+gVO.getAtributo("interesado"));
         m_Log.debug("Relacion ::"+gVO.getAtributo("relacion"));
        int resultado = DocumentosAplicacionManager.getInstance().grabarDocumento(gVO,params);
        myForm.setListaDocumentos(new Vector());
        myForm.setResultadoEliminarDocumentos(null);
        myForm.setOpcion(null);
     } else if ("documentoNuevo".equals(opcion)) {

         m_Log.debug("Nuevo Documento");
         myForm.setModificando(false);
         myForm.setCodDocumento("");
         GeneralValueObject gVO = new GeneralValueObject();
         m_Log.debug("request  codProcedimiento " + request.getParameter("codProcedimiento"));
         m_Log.debug("opcion: " + myForm.getOpcion());

    }else if("documentoModificar".equals(opcion)){	
            GeneralValueObject gVO = new GeneralValueObject(); 	
            myForm.setModificando(true);	
            m_Log.debug("request  codProcedimiento "+request.getParameter("codProcedimiento"));	
            m_Log.debug("Modificar Plantilla Documentos");	
            	
     }else if ("documentoAlta".equals(opcion)) {	
                 m_Log.debug(" =========================> Alta de un documento");
         DocumentosAplicacionForm documentosAplicacionForm = (DocumentosAplicacionForm) form;
         //FichaExpedienteDocumentoForm fichaForm = (FichaExpedienteDocumentoForm) form;
         FormFile fichero = null;
         if (documentosAplicacionForm != null && documentosAplicacionForm.getFicheroWord() != null) {
             if (!"".equals(documentosAplicacionForm.getFicheroWord().getFileName())) {
                 fichero = documentosAplicacionForm.getFicheroWord();
             }
         }
                boolean isExtensionCorrect = false;
                boolean isSizeCorrect = false;
                boolean datosModificados = fichero != null;
                String navegador = request.getParameter("navegador");
                m_Log.debug(" =========================> Alta de un documento otro navegador web utlizado: " + navegador);

                // Si se ha sube el documento por POST
                if (fichero != null && fichero.getFileSize() > 0) {
                    // Si se ha sube el documento por POST
                    // Comprobación del tamaño del fichero y de extensión correcta
                    isExtensionCorrect = StrutsFileValidation.isExtensionValid(fichero, StrutsFileValidation.APP_EXPEDIENTES);
                    isSizeCorrect = StrutsFileValidation.isSizeValid(fichero, StrutsFileValidation.APP_EXPEDIENTES);


                    // Comprobacion de la extension del fichero
                    if (!isExtensionCorrect) {
                        m_Log.debug("La extensión del fichero no es válida");
                        request.setAttribute(ConstantesDatos.EXTENSION_FILE_INCORRECT, "si");
                        String ext = (String) StrutsFileValidation.getLimite(StrutsFileValidation.EXTENSION_DOC_PREFFIX, StrutsFileValidation.SUFFIX_EXPEDIENTE);
                        request.setAttribute(ConstantesDatos.EXTENSION_PERMITED, ext);
                        request.setAttribute(ConstantesDatos.DESCRIPCION_BYTES, ConstantesDatos.DESCRIPCION_BYTES);
                        opcion = "documentoNuevo"; // Volvemos al formulario de entrada
                    } else {
                        request.setAttribute(ConstantesDatos.EXTENSION_FILE_INCORRECT, "no");
                    }

                    // Comprobacion del tamaño del fichero
                    if (!isSizeCorrect) {
                        request.setAttribute(ConstantesDatos.TAM_MAX_FILE_EXCEED, "si");
                        Integer tam = Math.round((Integer)StrutsFileValidation.getLimite(StrutsFileValidation.TAM_MAX_DOC_PREFFIX,StrutsFileValidation.APP_EXPEDIENTES)/ConstantesDatos.DIVISOR_BYTES);
                        //int num = Math.round(StrutsFileValidation.TAM_MAX_FILE/ConstantesDatos.DIVISOR_BYTES);
                        request.setAttribute(ConstantesDatos.TAM_MAX_FILE_BYTE, tam);
                        request.setAttribute(ConstantesDatos.DESCRIPCION_BYTES, ConstantesDatos.DESCRIPCION_BYTES);
                        opcion = "documentoNuevo"; // Volvemos al formulario de entrada
                    } else {
                        request.setAttribute(ConstantesDatos.TAM_MAX_FILE_EXCEED, "no");
                    }


                    if(isExtensionCorrect && isSizeCorrect){
                        
                        GeneralValueObject gVO = new GeneralValueObject();
                        int cM = usuario.getOrgCod();
                        String codMunicipio = Integer.toString(cM);
                        gVO.setAtributo("codMunicipio",codMunicipio);
                         m_Log.debug("COD_PROCEDIMIENTO " +request.getParameter("codProcedimiento"));
                         m_Log.debug("codAplicacion " +request.getParameter("codAplicacion"));
                        gVO.setAtributo("codigoAplicacion",(String)request.getParameter("codAplicacion"));
                        if("1".equals((String)request.getParameter("codAplicacion"))){
                            gVO.setAtributo("codProcedimiento","");
                            gVO.setAtributo("codTramite","");
                            gVO.setAtributo("interesado","");
                            gVO.setAtributo("relacion","");
                        }else
                        {
                            gVO.setAtributo("codProcedimiento",(String)request.getParameter("codProcedimiento"));
                            gVO.setAtributo("codTramite",(String)request.getParameter("codTramite"));
                            gVO.setAtributo("interesado",myForm.getInteresado());
                            gVO.setAtributo("relacion",myForm.getRelacion());
                        }
                        
                        gVO.setAtributo("codDocumento",myForm.getCodDocumento());
                        gVO.setAtributo("ficheroWord",myForm.getFicheroWord().getFileData());
                        gVO.setAtributo("nombreDocumento",myForm.getNombreDocumento());
                        
                        gVO.setAtributo("docActivo", "SI");
                        gVO.setAtributo("editorTexto",(ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE).toUpperCase());
                         m_Log.debug("DocumentosAplicacionAction GRABAR DOCUMENTO");
                         m_Log.debug("Interesado ::"+gVO.getAtributo("interesado"));
                         m_Log.debug("Relacion ::"+gVO.getAtributo("relacion"));
                        int resultado = DocumentosAplicacionManager.getInstance().grabarDocumento(gVO,params);
                        
                    }// if todo correcto
                }

            } 
     else if("descargarDocumento".equals(opcion)){
         GeneralValueObject gVO=new GeneralValueObject();
         int cM = usuario.getOrgCod();
         String codTramite = "";
         if(usuario.getAppCod()==4) codTramite= myForm.getCodTramite();
         else if (usuario.getAppCod()==6) codTramite=myForm.getCodTramiteSeleccionado();
         String codMunicipio = Integer.toString(cM);
         gVO.setAtributo("codMunicipio", codMunicipio);
         gVO.setAtributo("codigoAplicacion", myForm.getCodAplicacion());
         gVO.setAtributo("codProcedimiento", myForm.getCodProcedimiento());
         gVO.setAtributo("codTramite", codTramite);
         gVO.setAtributo("codDocumento", myForm.getCodDocumento());
         gVO.setAtributo("interesado", myForm.getInteresado());
         gVO.setAtributo("relacion", myForm.getRelacion());
         gVO.setAtributo("docActivo", myForm.getDocActivo());
         m_Log.debug("DocumentosAplicacion: Descargar Plantilla");
         GeneralValueObject file=DocumentosAplicacionManager.getInstance().loadDocumento(gVO, params);
         if (file != null) {
             try{  
                    String nombrePlantilla=myForm.getNombreDocumento().replace(" ","_");
                    String extension=".odt";
                    byte[] fichero = (byte[]) file.getAtributo("fichero");
	
                    response.setHeader("Content-Transfer-Encoding", "binary");
                    log.debug("nombre Documento --->"+myForm.getNombreDocumento());
                    response.setHeader("Content-Disposition", "inline; filename=" +nombrePlantilla+extension);
                    response.setContentType("application/vnd.oasis.opendocument.text");
                    response.setContentLength(fichero.length);
                    DataOutputStream out = new DataOutputStream(response.getOutputStream());
                    out.write(fichero, 0, fichero.length);
                    out.flush();
                    out.close();
                    log.debug(fichero.length);
            }catch(Exception e){
                 m_Log.error("Excepcion capturada: " + e.toString());  
            }
      }else{
          log.warn("FICHERO NULO");
      }
         
     }

     else if("salir".equals(opcion))
     {
       if ((session.getAttribute(mapping.getAttribute()) != null))
         session.removeAttribute(mapping.getAttribute());
     }
     else
     {
       opcion = mapping.getInput();
     }
     return (mapping.findForward(opcion));
   }
}