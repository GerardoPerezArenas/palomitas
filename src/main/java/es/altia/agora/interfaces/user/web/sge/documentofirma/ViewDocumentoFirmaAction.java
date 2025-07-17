/*______________________________BOF_________________________________*/
package es.altia.agora.interfaces.user.web.sge.documentofirma;

import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.portafirmas.documentofirma.vo.*;
import es.altia.agora.business.portafirmas.documentofirmafacade.DocumentoFirmaFacadeDelegate;
import es.altia.agora.business.portafirmas.documentofirmafacade.DocumentoRelacionFirmaFacadeDelegate;
import es.altia.agora.business.portafirmas.persistence.manual.DocsProcedimientoPortafirmasManager;
import es.altia.agora.business.portafirmas.persistence.manual.EDocExtPortafirmasManager;
import es.altia.agora.business.sge.persistence.FichaExpedienteManager;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm;
import es.altia.flexia.notificacion.persistence.AdjuntoNotificacionManager;
import es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO;
import es.altia.util.commons.BasicTypesOperations;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.facades.BusinessFacadeDelegateFactory;
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import es.altia.util.struts.DefaultAction;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * @version $\Date$ $\Revision$
 */
public class ViewDocumentoFirmaAction extends DefaultAction {
    /*_______Constants______________________________________________*/

    public static final String CLSNAME = "ViewDocumentoFirmaAction";
    public static final String MAPPING_SUCCESS = "DocumentoFirmaForm";
    private Logger log = Logger.getLogger(ViewDocumentoFirmaAction.class);
            

    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/
    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        /*Cast form*/
        DocumentoFirmaActionForm concreteForm = (DocumentoFirmaActionForm) form;

        /* Pre-fill static form fields */
        int idUsuario = SessionManager.getAuthenticatedUser(request).getIdUsuario();
        concreteForm.setUsuario(idUsuario);

        /* If the request is to allow the user to correct errors in the form, "concreteForm" must not be modified. */
        if (request.getAttribute(Globals.ERROR_KEY) == null) {
            if (_log.isDebugEnabled()) {
                _log.debug(CLSNAME + ".doPerform() There are no previous errors... prefilling form");
            }

            /* Recuperar parámetros de conexión */
            UsuarioValueObject usuario = SessionManager.getAuthenticatedUser(request);
            String[] params = usuario.getParamsCon();
            if (_log.isDebugEnabled()) {
                _log.debug(CLSNAME + ".doPerform() PARAMS = {" + BasicTypesOperations.toString(params, ",") + "}");
            }

            /* Recuperar documento firma */
            List vos = null;
            int tipoDocumento = concreteForm.getTipoDocumento();
            if (tipoDocumento == DocumentoCustomVO.DOCUMENTO_EXPEDIENTE) {

                DocumentoFirmaFacadeDelegate facade = (DocumentoFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DocumentoFirmaFacadeDelegate.class);
                facade.setDsKey(params[params.length - 1]);
                DocumentoFirmaPK pk = new DocumentoFirmaPK(concreteForm.getIdMunicipio(), concreteForm.getIdProcedimiento(),
                        concreteForm.getIdEjercicio(), concreteForm.getIdNumeroExpediente(),
                        concreteForm.getIdTramite(), concreteForm.getIdOcurrenciaTramite(),
                        concreteForm.getIdNumeroDocumento(), -1);
                
                SearchCriteria searchCriteria = facade.scDocumentoFirmaByDocumento(pk);
                /*
                 * 0 --> Expediente activo
                 * 1 --> Expediente historico
                 * 2 --> Expediente no existe
                 */
                boolean estaHistorico = false;
                int historico = FichaExpedienteManager.getInstance().estaExpedienteHistorico(concreteForm.getIdEjercicio(),concreteForm.getIdNumeroExpediente(), params);
                
                
                switch(historico){
                    case 0: estaHistorico = false;
                            break;
                    case 1: estaHistorico = true;
                            break;
                    default: estaHistorico = false;
                            break;                        
                }// switch                
                searchCriteria.setExpedienteHistorico(estaHistorico);
                vos = facade.findDocumentoFirma(searchCriteria,facade.ocDocumentoFirmaByEstadoFirma(true), -1, -1);
                
            } else if (tipoDocumento == DocumentoCustomVO.DOCUMENTO_RELACION) {
                DocumentoRelacionFirmaFacadeDelegate facade = (DocumentoRelacionFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DocumentoRelacionFirmaFacadeDelegate.class);
                facade.setDsKey(params[params.length - 1]);
                DocumentoRelacionFirmaPK pk = new DocumentoRelacionFirmaPK(concreteForm.getIdMunicipio(), concreteForm.getIdProcedimiento(),
                        concreteForm.getIdEjercicio(), concreteForm.getIdNumeroExpediente(),
                        concreteForm.getIdTramite(), concreteForm.getIdOcurrenciaTramite(),
                        concreteForm.getIdNumeroDocumento(), -1);
                vos = facade.findDocumentoFirma(facade.scDocumentoFirmaByDocumento(pk), facade.ocDocumentoFirmaByEstadoFirma(true), -1, -1);
            } else if (tipoDocumento == DocumentoCustomVO.DOCUMENTO_EXTERNO){
                log.debug ("nº de documento externo "+concreteForm.getIdNumeroDocumento());
                vos = new ArrayList<DocumentoOtroFirmaVO>();
                vos.add(EDocExtPortafirmasManager.getInstance().getDocumento
                        (String.valueOf(concreteForm.getIdNumeroDocumento()), params));
                
            } else if (tipoDocumento == DocumentoCustomVO.DOCUMENTO_PROCEDIMIENTO){
                log.debug ("nº de documento procedimeinto"+request.getParameter("codigoFirmaDocProcedimiento"));
                vos = DocsProcedimientoPortafirmasManager.getInstance().getDocumentoFirma(request.getParameter("codigoFirmaDocProcedimiento"), params);
                
            }else if (tipoDocumento == DocumentoCustomVO.DOCUMENTO_EXTERNO_NOTIFICACION){
                log.debug ("nº de documento externo notificacion: "+request.getParameter("codigoFirmaDocProcedimiento"));
                
                int codDocumento = Integer.parseInt(request.getParameter("idNumeroDocumento"));
                vos = new ArrayList<AdjuntoNotificacionVO>();
                vos.add(AdjuntoNotificacionManager.getInstance().getDocumentoExternoNotificacion(codDocumento,params));
                
            }


            if ((vos != null) && (vos.size() > 0)) {

                int usuarioFirmante = -1;
                Integer usuarioDelegadoFirmante = null;
                Object objDoc = vos.get(0);
                if (objDoc instanceof DocumentoFirmaVO) {
                    DocumentoFirmaVO vo = (DocumentoFirmaVO) objDoc;                    
                    concreteForm.setDocumentoFirmaVO(vo,request);
                    usuarioFirmante = vo.getUsuarioFirmante();
                    usuarioDelegadoFirmante = vo.getIdUsuarioDelegadoFirmante();
                } else if (objDoc instanceof DocumentoRelacionFirmaVO) {
                    DocumentoRelacionFirmaVO vo = (DocumentoRelacionFirmaVO)objDoc;
                concreteForm.setDocumentoFirmaVO(vo,request);
                    usuarioFirmante = vo.getUsuarioFirmante();
                    usuarioDelegadoFirmante = vo.getIdUsuarioDelegadoFirmante();
                } else if (objDoc instanceof DocumentoProcedimientoFirmaVO) {                    
                    DocumentoProcedimientoFirmaVO vo = (DocumentoProcedimientoFirmaVO) objDoc;
                    concreteForm.setDocumentoFirmaVO(vo, request);
                    concreteForm.setIdNumFirma(vo.getIdNumFirma());
                    if(vo.getCodigoUsuarioFirma()!=null && !"".equals(vo.getCodigoUsuarioFirma())){
                        usuarioFirmante = Integer.valueOf(vo.getCodigoUsuarioFirma());                    
                        usuarioDelegadoFirmante = Integer.valueOf(vo.getCodigoUsuarioFirma());
                    }
                } else if (objDoc instanceof DocumentoOtroFirmaVO) {
                    DocumentoOtroFirmaVO vo = (DocumentoOtroFirmaVO) objDoc;
                    concreteForm.setDocumentoFirmaVO(vo, request);      
                    concreteForm.setIdNumFirma(vo.getCodigoDocumento());
                    usuarioFirmante = Integer.valueOf(vo.getCodigoUsuarioFirma());
                    usuarioDelegadoFirmante = Integer.valueOf(vo.getCodigoUsuarioFirma());
                } else if(objDoc instanceof AdjuntoNotificacionVO){
                    AdjuntoNotificacionVO adjunto = (AdjuntoNotificacionVO)objDoc;
                    concreteForm.setDocumentoFirmaVO(adjunto,request);
                    usuarioFirmante = adjunto.getCodUsuarioFirmaOtro();
                    usuarioDelegadoFirmante = adjunto.getCodUsuarioFirmaOtro();
                    
                }
                
                concreteForm.setTipoDocumento(tipoDocumento);

                final UsuarioManager mgr = UsuarioManager.getInstance();

                /* Recuperar nombre usuario quien debía firmar el documento */
                if (usuarioFirmante >= 0) {
                    try {
                        UsuarioEscritorioValueObject usuarioEscritorioVO = mgr.buscaUsuario(usuarioFirmante);
                        final String nombreUsuarioFirmante = usuarioEscritorioVO.getNombreUsu();
                        concreteForm.setNombreUsuarioFirmante(nombreUsuarioFirmante);
                    } catch (Exception e) {
                        concreteForm.setNombreUsuarioFirmante("ERROR");
                    }//try-catch
                }//if

                /* Recuperar nombre usuario quien firmó el documento */
                if (usuarioDelegadoFirmante != null) {
                    usuarioFirmante = usuarioDelegadoFirmante;
                    if (usuarioFirmante >= 0) {
                        try {
                            UsuarioEscritorioValueObject usuarioEscritorioVO = mgr.buscaUsuario(usuarioFirmante);
                            final String nombreUsuarioFirmante = usuarioEscritorioVO.getNombreUsu();
                            concreteForm.setNombreUsuarioDelegadoFirmante(nombreUsuarioFirmante);
                        } catch (Exception e) {
                            concreteForm.setNombreUsuarioDelegadoFirmante("ERROR");
                        }//try-catch
                    }//if
                }//if
            } else {
                if (_log.isWarnEnabled()) {
                    _log.warn(CLSNAME + ".doPerform() No objects found!");
                }
            }//if

            if (_log.isDebugEnabled()) {
                _log.debug(CLSNAME + ".doPerform() ConcreteForm =" + concreteForm);
            }
        } else {
            if (_log.isDebugEnabled()) {
                _log.debug(CLSNAME + ".doPerform() There are errors... do not touch form");
            }
        }//if

        /* Return ActionForward */
        return mapping.findForward(MAPPING_SUCCESS);
    }

    protected String getMainPageMapping() {
        return MAPPING_SUCCESS;
    }

    protected String getInternalErrorMapping() {
        return GlobalNames.INTERNALERROR_GLOBAL_FORWARD;
    }

    protected String getModelErrorMapping() {
        return GlobalNames.MODELERROR_GLOBAL_FORWARD;
    }
}//class
/*______________________________EOF_________________________________*/
