/*______________________________BOF_________________________________*/
package es.altia.agora.interfaces.user.web.portafirmas.documentoportafirmas;

import es.altia.agora.business.editor.mantenimiento.persistence.manual.DocumentosAplicacionDAO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.portafirmas.delegacionfirmafacade.DelegacionFirmaFacadeDelegate;
import es.altia.agora.business.portafirmas.documentofirmafacade.DocumentoFirmaFacadeDelegate;
import es.altia.agora.business.portafirmas.documentofirmafacade.DocumentoRelacionFirmaFacadeDelegate;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoCustomVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoOtroFirmaVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoPortafirmasVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoProcedimientoFirmaVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoRelacionPortafirmasVO;
import es.altia.agora.business.portafirmas.persistence.manual.DocsProcedimientoPortafirmasManager;
import es.altia.agora.business.portafirmas.persistence.manual.EDocExtPortafirmasManager;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.flexia.notificacion.persistence.AdjuntoNotificacionManager;
import es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO;
import es.altia.flexia.portafirmasexternocliente.factoria.PluginPortafirmasExternoClienteFactoria;
import es.altia.util.commons.BasicTypesOperations;
import es.altia.util.commons.BasicValidations;
import es.altia.util.commons.MimeTypes;
import es.altia.util.documentos.DocumentOperations;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.facades.BusinessFacadeDelegateFactory;
import es.altia.util.persistance.daocommands.OrderCriteria;
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import es.altia.util.struts.DefaultSearchAction;
import es.altia.util.struts.DefaultSearchActionForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.commons.lang.StringUtils;

/**
 * @version $\Date$ $\Revision$
 */
public class PrepareSearchDocumentoPortafirmasAction extends DefaultSearchAction {
    /*_______Constants______________________________________________*/
    private static final String CLSNAME = "PrepareSearchDocumentoPortafirmasAction";
    private static final String MAPPING_SUCCESS = "SearchDocumentoPortafirmasForm";
    private static final Log _log =
            LogFactory.getLog(PrepareSearchDocumentoPortafirmasAction.class.getName());

    private String pDataSourceKey = null;

    /*_______Operations_____________________________________________*/
    protected void putCommonSearchParameters(Map parameters, DefaultSearchActionForm form) {
        /* Cast form */
        final SearchDocumentoPortafirmasActionForm concreteForm = (SearchDocumentoPortafirmasActionForm) form;

        if ( (concreteForm.getChkEstado()) && (!BasicValidations.isEmpty(concreteForm.getEstadoFirma())) ) {
            parameters.put(SearchDocumentoPortafirmasActionForm.CHK_ESTADO, concreteForm.getChkEstado());
            parameters.put(SearchDocumentoPortafirmasActionForm.ESTADO_FIRMA,concreteForm.getEstadoFirma());
        }//if
    }//putCommonSearchParameters

    protected String getDefaultMappingKey() {
        return MAPPING_SUCCESS;
    }

    protected String getPrintPreviewMappingKey() {
        return MAPPING_SUCCESS;
    }

    protected String getPopUpMappingKey() {
        return MAPPING_SUCCESS;
    }

    protected List doSearch(ActionMapping mapping, DefaultSearchActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doSearch()  BEGIN");

        /* Cast form */
        final SearchDocumentoPortafirmasActionForm concreteForm = (SearchDocumentoPortafirmasActionForm) form;
        if (_log.isDebugEnabled()) _log.debug(CLSNAME+".doSearch()  ConcreteForm="+concreteForm);

        /* Recuperar parámetros de conexión */
        UsuarioValueObject usuario = SessionManager.getAuthenticatedUser(request);
        String[] params = usuario.getParamsCon();
        if (_log.isDebugEnabled()) _log.debug(CLSNAME+".doPerform() PARAMS = {"+BasicTypesOperations.toString(params,",")+"}");
        this.pDataSourceKey = params[params.length-1];

        /* Get facades */
        final DocumentoFirmaFacadeDelegate facade =
                (DocumentoFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DocumentoFirmaFacadeDelegate.class);
        facade.setDsKey(this.pDataSourceKey);
        final DocumentoRelacionFirmaFacadeDelegate relFacade =
                (DocumentoRelacionFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DocumentoRelacionFirmaFacadeDelegate.class);
        relFacade.setDsKey(this.pDataSourceKey);

        /* Get Page by Page parameters */
        int startIndex = -1;
        int count = -1;
        if (form.getDoPageByPage()) {
            startIndex = form.getStartIndex();
            if (startIndex < 0) startIndex = 0;
            count = form.getCount();
            if (count < 1) count = 1;
            form.setCount(count);
        }//if

        /* Get ordercriteria from sort parameters */
        final OrderCriteria oc = getOrderCriteria(form,facade);
        final SearchCriteria sc = getSearchCriteria(form, request, facade);

        List<DocumentoCustomVO> result = new ArrayList<DocumentoCustomVO>();
        
        if(_log.isDebugEnabled()) _log.debug("Recuperamos la propiedad que indica si existe un portafirmas externo para no hacer determinadas busquedas");
        Boolean portafirmaExterno = 
                PluginPortafirmasExternoClienteFactoria.getExistePortafirmasExterno(String.valueOf(usuario.getOrgCod()));
        
        if(!portafirmaExterno){
            /* Do search */
            final List expResult = facade.findDocumentoPortafirmas(sc, oc,startIndex,count);
            for (Object objDoc: expResult) {
                DocumentoPortafirmasVO docu = (DocumentoPortafirmasVO)objDoc;
                DocumentoCustomVO docCustom = new DocumentoCustomVO();
                docCustom.setEstadoFirma(docu.getEstadoFirma());
                docCustom.setFechaEnvioFirma(docu.getFechaEnvioFirma());
                docCustom.setFechaFirma(docu.getFechaFirma());
                docCustom.setFirma(docu.getFirma());
                docCustom.setIdEjercicio(docu.getIdEjercicio());
                docCustom.setIdMunicipio(docu.getIdMunicipio());
                docCustom.setIdNumeroDocumento(docu.getIdNumeroDocumento());
                docCustom.setIdNumeroExpediente(docu.getIdNumeroExpediente());
                docCustom.setIdOcurrenciaTramite(docu.getIdOcurrenciaTramite());
                docCustom.setIdProcedimiento(docu.getIdProcedimiento());
                docCustom.setIdTramite(docu.getIdTramite());
                docCustom.setIdUsuarioDelegadoFirmante(docu.getIdUsuarioDelegadoFirmante());
                docCustom.setNombreDocumento(docu.getNombreDocumento());
                docCustom.setNombreProcedimiento(docu.getNombreProcedimiento());
                docCustom.setNombreTramite(docu.getNombreTramite());
                docCustom.setObservaciones(docu.getObservaciones());
                docCustom.setTipoDocumento(DocumentoCustomVO.DOCUMENTO_EXPEDIENTE);
                docCustom.setUsuarioFirmante(docu.getUsuarioFirmante());
                docCustom.setFinalizaRechazo(docu.getFinalizaRechazo());
                
                String editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTexto(docu.getIdMunicipio(),
                            docu.getIdNumeroExpediente(),docu.getIdTramite(),
                            docu.getIdOcurrenciaTramite(),docu.getIdNumeroDocumento(),false,params);
                docCustom.setEditorTexto(editorTexto);
                docCustom.setTipoMime(DocumentOperations.determinarTipoMimePlantilla(editorTexto, docu.getNombreDocumento()));
                
                result.add(docCustom);
            }//for (Object objDoc: expResult) 
        }//if(!portafirmaExterno){

        // Ahora buscaremos aquellos documentos enviados a firmar que pertenecen a relaciones.
        final List relResult = relFacade.findDocumentoPortafirmas(sc, oc, startIndex, count);
        for (Object objDoc: relResult) {
            DocumentoRelacionPortafirmasVO docu = (DocumentoRelacionPortafirmasVO)objDoc;
            DocumentoCustomVO docCustom = new DocumentoCustomVO();
            docCustom.setEstadoFirma(docu.getEstadoFirma());
            docCustom.setFechaEnvioFirma(docu.getFechaEnvioFirma());
            docCustom.setFechaFirma(docu.getFechaFirma());
            docCustom.setFirma(docu.getFirma());
            docCustom.setIdEjercicio(docu.getIdEjercicio());
            docCustom.setIdMunicipio(docu.getIdMunicipio());
            docCustom.setIdNumeroDocumento(docu.getIdNumeroDocumento());
            docCustom.setIdNumeroExpediente(docu.getIdNumeroExpediente());
            docCustom.setIdOcurrenciaTramite(docu.getIdOcurrenciaTramite());
            docCustom.setIdProcedimiento(docu.getIdProcedimiento());
            docCustom.setIdTramite(docu.getIdTramite());
            docCustom.setIdUsuarioDelegadoFirmante(docu.getIdUsuarioDelegadoFirmante());
            docCustom.setNombreDocumento(docu.getNombreDocumento());
            docCustom.setNombreProcedimiento(docu.getNombreProcedimiento());
            docCustom.setNombreTramite(docu.getNombreTramite());
            docCustom.setObservaciones(docu.getObservaciones());
            docCustom.setTipoDocumento(DocumentoCustomVO.DOCUMENTO_RELACION);
            docCustom.setUsuarioFirmante(docu.getUsuarioFirmante());
            docCustom.setFinalizaRechazo(docu.getFinalizaRechazo());
             
            String editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTexto(docu.getIdMunicipio(),
                            docu.getIdNumeroExpediente(),docu.getIdTramite(),
                            docu.getIdOcurrenciaTramite(),docu.getIdNumeroDocumento(),true,params);
            docCustom.setEditorTexto(editorTexto);
            docCustom.setTipoMime(DocumentOperations.determinarTipoMimePlantilla(editorTexto, docu.getNombreDocumento()));
            
            result.add(docCustom);
        }


        /** SE COMPRUEBA SI AL USUARIO ACTUAL LE HAN DENEGADO ALGUNA FIRMA **/
        Hashtable<String,String> delegados = EDocExtPortafirmasManager.getInstance().getUsuarioDelegado(usuario.getIdUsuario(), params);
        String nifUsuarioActual        = null;
        String nifUsuarioDelegado   = null;


        if(delegados!=null && delegados.size()>0){
            if("SI".equals(delegados.get("DELEGACION_FIRMA")) && delegados.get("NIF_USUARIO_DELEGADO")!=null && !"-1".equals(delegados.get("NIF_USUARIO_DELEGADO"))
                    && delegados.get("NIF_USUARIO_QUE_DELEGA")!=null && !"-1".equals(delegados.get("NIF_USUARIO_QUE_DELEGA"))){
                nifUsuarioDelegado = delegados.get("NIF_USUARIO_DELEGADO");
                nifUsuarioActual      = delegados.get("NIF_USUARIO_QUE_DELEGA");
            }else
            if("NO".equals(delegados.get("DELEGACION_FIRMA")) && delegados.get("NIF_USUARIO_ACTUAL")!=null && !"-1".equals(delegados.get("NIF_USUARIO_ACTUAL"))){
                nifUsuarioActual                   = delegados.get("NIF_USUARIO_ACTUAL");
            }
        }
        
        if((nifUsuarioDelegado)==null && (nifUsuarioActual==null))
        {
            List<DocumentoCustomVO> resultVacio = new ArrayList<DocumentoCustomVO>();
            _log.warn("PrepareSearchDocumentoPortafirmasAction--> EL nif del delegado y el del usuario no existe. Resultado vacio");
            return (resultVacio);
        }

        String estadoFirma = "";
        if(concreteForm.getEstadoFirma()!=null)
            estadoFirma = concreteForm.getEstadoFirma();  // Se viene de la página de firma o de rechazo de documento
        else
            estadoFirma = concreteForm.getCodEstadoFirma(); // Se viene del formulario de búsqueda

        final List expExternosResult = EDocExtPortafirmasManager.getInstance().getDocumentoExternosPortafirmas(nifUsuarioDelegado,nifUsuarioActual,estadoFirma,params);
        
        for (Object objDoc: expExternosResult) {
            DocumentoOtroFirmaVO docu = (DocumentoOtroFirmaVO)objDoc;
            DocumentoCustomVO docCustom = new DocumentoCustomVO();


            docCustom.setEstadoFirma(docu.getEstadoFirma());
            docCustom.setFechaEnvioFirma(docu.getFechaEnvioFirma());
            docCustom.setFechaFirma(docu.getFechaFirma());            
            docCustom.setIdEjercicio(docu.getEjercicio());
            if(docu.getCodigoUsuarioAlta()!=null)
                docCustom.setIdUsuarioDelegadoFirmante(Integer.parseInt(docu.getCodigoUsuarioAlta()));
            docCustom.setNombreDocumento(docu.getNombreDocumento());
            if(docu.getCodigoUsuarioFirma()!=null)
                docCustom.setUsuarioFirmante(Integer.parseInt(docu.getCodigoUsuarioFirma()));
            docCustom.setIdNumeroDocumento(docu.getCodigoDocumento());
            docCustom.setTipoDocumento(DocumentoCustomVO.DOCUMENTO_EXTERNO);
            docCustom.setFinalizaRechazo(docu.getFinalizaRechazo());
            String tipoMime = docu.getTipoMime();
            if (StringUtils.isEmpty(tipoMime)) {
                tipoMime = MimeTypes.guessMimeType("", docu.getNombreDocumento());
            }
            docCustom.setTipoMime(tipoMime);

            result.add(docCustom);
        }
        
        final List docsProcedimiento = DocsProcedimientoPortafirmasManager.getInstance().
                getDocumentosProcedimientoPortafirmas(nifUsuarioDelegado, nifUsuarioActual, estadoFirma, usuario.getOrgCod(),params);
        for (Object objDoc: docsProcedimiento) {
            DocumentoProcedimientoFirmaVO docu = (DocumentoProcedimientoFirmaVO) objDoc;
            DocumentoCustomVO docCustom = new DocumentoCustomVO();
            
            docCustom.setEstadoFirma(docu.getEstadoFirma().trim());
            docCustom.setFechaEnvioFirma(docu.getFechaEnvioFirma());
            docCustom.setFechaFirma(docu.getFechaFirma());
            docCustom.setIdEjercicio(Integer.valueOf(docu.getIdNumeroExpediente().substring(0,4)));
            if (docu.getFirma()!=null){
                docCustom.setFirma(docu.getFirma());
                docCustom.setFirmaBase64(new String (docCustom.getFirma()));
            } else {
                docCustom.setFirma(null);
                docCustom.setFirmaBase64(null);
            }
            docCustom.setNombreDocumento(docu.getNombreDocumento());
            docCustom.setIdNumeroDocumento(docu.getCodigoDocumento());
            docCustom.setIdPresentado(docu.getIdPresentado());
            docCustom.setTipoDocumento(DocumentoCustomVO.DOCUMENTO_PROCEDIMIENTO);
            docCustom.setIdNumeroExpediente(docu.getIdNumeroExpediente());
            docCustom.setIdProcedimiento(docu.getIdProcedimiento());
            docCustom.setIdNumFirma(docu.getIdNumFirma());
            docCustom.setNombreProcedimiento(docu.getNombreProcedimiento());
            docCustom.setObservaciones(docu.getObservaciones());
            docCustom.setFinalizaRechazo(docu.getFinalizaRechazo());
            String tipoMime = docu.getTipoMime();
            if (StringUtils.isEmpty(tipoMime)) {
                tipoMime = MimeTypes.guessMimeType("", docu.getNombreDocumento());
            }
            docCustom.setTipoMime(tipoMime);

            result.add(docCustom);
        }

        
        /******************** ADJUNTOS EXTERNOS DE UNA NOTIFICACIÓN ELECTRÓNICA ***********************
         * 
         *  EL PORTAFIRMAS ESTÁ PREPARADO PARA PODER FIRMAR DOCUMENTOS EXTERNOS QUE ESTÁN ASOCIADOS A UNA
         * NOTIFICACIÓN, PERO POR EL MOMENTO NO SE TENDRÁ EN CUENTA ESTA FUNCIONALIDAD Y SE COMENTA ESTE CODIGO,
         * POR SI EN UN FUTURO SE ACABA UTILIZANDO, SIMPLEMENTE SE DESCOMENTARIA
         * # Óscar Rodríguez
         * #76673
        ArrayList<AdjuntoNotificacionVO> adjuntos = AdjuntoNotificacionManager.getInstance().getListaAdjuntosExternosNotificacionPortafirmas(usuario.getIdUsuario(), estadoFirma, usuario.getParamsCon());
        for(int i=0;adjuntos!=null && i<adjuntos.size();i++){
            AdjuntoNotificacionVO adjunto = adjuntos.get(i);
            
            DocumentoCustomVO docCustom = new DocumentoCustomVO();
            docCustom.setEstadoFirma(adjunto.getEstadoFirma());
            docCustom.setFechaEnvioFirma(adjunto.getFechaAlta());
            docCustom.setIdNumeroExpediente(adjunto.getNumeroExpediente());
            docCustom.setIdOcurrenciaTramite(adjunto.getOcurrenciaTramite());
            docCustom.setIdTramite(adjunto.getCodigoTramite());
            docCustom.setNombreDocumento(adjunto.getNombre());        
            docCustom.setTipoDocumento(DocumentoCustomVO.DOCUMENTO_EXTERNO_NOTIFICACION);
            docCustom.setIdEjercicio(Integer.valueOf(adjunto.getNumeroExpediente().substring(0,4)));            
            docCustom.setNombreProcedimiento(adjunto.getNombreProcedimiento());
            docCustom.setEstadoFirma(adjunto.getEstadoFirma());
            docCustom.setIdNumeroDocumento(adjunto.getIdDocExterno());
            result.add(docCustom);
        }                
        ******************** ADJUNTOS EXTERNOS DE UNA NOTIFICACIÓN ELECTRÓNICA ***********************/
        
        
        
        
        
        int totalCount = result.size();
        form.setTotalCount(result.size());

        /* check upper limit of startIndex */
        if ( (form.getDoPageByPage()) && (totalCount>0) && (startIndex >= totalCount) ) {
            startIndex = 0;
            form.setStartIndex(startIndex);
        }//if

        /* Ahora vamos a comprobar si existe un indice preseleccionado en caso de vuelta atras */
        String selectedIndex = SessionManager.getSelectedIndex(request);
        if (selectedIndex != null) {
            SessionManager.removeSelectedIndex(request);
            request.setAttribute("selectedIndex", selectedIndex);
        }
        _log.debug("INDICE SELECCIONADO AL VOLVER DE UN CANCELAR ES: " + selectedIndex);

        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doSearch()  END");
        return result;
    }//doSearch

    private final SearchCriteria getSearchCriteria(DefaultSearchActionForm form, HttpServletRequest request, DocumentoFirmaFacadeDelegate facade) throws InternalErrorException {
        SearchCriteria result = null;
        // Obtener id usuario autenticado
        int idUsuario = SessionManager.getAuthenticatedUser(request).getIdUsuario();
        // Obtener conjunto de ids de usuarios cuyos documentos puedo firmar ahora
        DelegacionFirmaFacadeDelegate delegacionFirmaFacade = (DelegacionFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DelegacionFirmaFacadeDelegate.class);
        delegacionFirmaFacade.setDsKey(this.pDataSourceKey);
        Integer[] idsUsuarios = delegacionFirmaFacade.findCierreUsuariosPortafirmas(idUsuario);
        // Fijar al menos este criterio de búsqueda
        result = facade.scDocumentoPortafirmasByUsuarios(idsUsuarios);

        final SearchDocumentoPortafirmasActionForm concreteForm = (SearchDocumentoPortafirmasActionForm) form;
        if ( (concreteForm.getChkEstado()) && (!BasicValidations.isEmpty(concreteForm.getEstadoFirma())) ) {
            result = result.and(facade.scDocumentoFirmaByEstadoFirma(concreteForm.getEstadoFirma()));
        }//if
        return result;
    }//getSearchCriteria

    private final OrderCriteria getOrderCriteria(DefaultSearchActionForm form, DocumentoFirmaFacadeDelegate facade) throws InternalErrorException {
        final int sort = form.getSort();
        final String sortField = form.getSortField();
        if ( (sort > 0) && (sortField!=null) ) {
            if (BasicTypesOperations.equals(sortField,"estadoFirma"))
                return facade.ocDocumentoFirmaByEstadoFirma( (sort==DefaultSearchActionForm.SORT_ASC) );
            else if (BasicTypesOperations.equals(sortField,"numeroExpediente"))
                return facade.ocDocumentoFirmaByNumeroExpediente( (sort==DefaultSearchActionForm.SORT_ASC) );
            else if (BasicTypesOperations.equals(sortField,"nombreDocumento"))
                return facade.ocDocumentoPortafirmasByNombreDocumento( (sort==DefaultSearchActionForm.SORT_ASC) );
            else if (BasicTypesOperations.equals(sortField,"fechaEnvio"))
                return facade.ocDocumentoFirmaByFechaEnvio( (sort==DefaultSearchActionForm.SORT_ASC) );
            else
                return null;
        } else {
            return null;
        }//if
    }//getOrderCriteria
}//class
/*______________________________EOF_________________________________*/