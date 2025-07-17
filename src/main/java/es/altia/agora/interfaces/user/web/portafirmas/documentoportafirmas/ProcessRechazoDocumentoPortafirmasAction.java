/*______________________________BOF_________________________________*/
package es.altia.agora.interfaces.user.web.portafirmas.documentoportafirmas;

import es.altia.agora.business.portafirmas.documentofirma.vo.*;
import es.altia.agora.business.portafirmas.documentofirmafacade.DocumentoFirmaFacadeDelegate;
import es.altia.agora.business.portafirmas.documentofirmafacade.DocumentoRelacionFirmaFacadeDelegate;
import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.facades.BusinessFacadeDelegateFactory;
import es.altia.util.struts.DefaultAction;
import es.altia.common.service.mail.MailHelper;
import es.altia.common.service.mail.exception.MailServiceNotActivedException;
import es.altia.common.service.mail.exception.MailException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;
import java.util.ResourceBundle;

/**
 * @version $\Date$ $\Revision$
 */
public class ProcessRechazoDocumentoPortafirmasAction extends DefaultAction {
    /*_______Constants______________________________________________*/
    private static final String CLSNAME = "ProcessRechazoDocumentoPortafirmasAction";
    private static final String MESSAGE_SUCCESS = "Portafirmas.FirmaDocumentoPortafirmasForm.DocumentoRechazado";
    private static final String MESSAGE_SUCCESS_MAIL_FAIL = "Portafirmas.FirmaDocumentoPortafirmasForm.DocumentoRechazado.FalloMail";
    private static final Log _log =
            LogFactory.getLog(ProcessRechazoDocumentoPortafirmasAction.class.getName());

    private String pDataSourceKey = null;

    /*_______Operations_____________________________________________*/
    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() BEGIN");

        /* Aceptamos el rechazo, por lo que eliminamos de la sesion el parametro de valor seleccionado en caso de cancelar */
        SessionManager.removeSelectedIndex(request);
        
        /* Cast form */
        FirmaDocumentoPortafirmasActionForm concreteForm = (FirmaDocumentoPortafirmasActionForm) form;
        if (_log.isDebugEnabled()) _log.debug(CLSNAME+".doPerform() ConcreteForm = "+concreteForm);

        /* Retrieve DataSource key */
        this.pDataSourceKey = SessionManager.getDataSourceKey(request);

        /* Save Signature */
        int idUsuarioAutenticado = SessionManager.getAuthenticatedUser(request).getIdUsuario();
        int idUsuarioResponsable = concreteForm.getUsuarioFirmante();
        int tipoDocumento = concreteForm.getTipoDocumento();

        _log.debug("EL TIPO DE DOCUMENTO ES : " + tipoDocumento);

        if (tipoDocumento == DocumentoCustomVO.DOCUMENTO_EXPEDIENTE) {
        final DocumentoFirmaVO vo = new DocumentoFirmaVO(
                new DocumentoFirmaPK(concreteForm.getIdMunicipio(),
                        concreteForm.getIdProcedimiento(), concreteForm.getIdEjercicio(),
                        concreteForm.getIdNumeroExpediente(), concreteForm.getIdTramite(),
                        concreteForm.getIdOcurrenciaTramite(), concreteForm.getIdNumeroDocumento(),
                        idUsuarioResponsable),
                DocumentoFirmaVO.ESTADO_FIRMA_RECHAZADO, null, Calendar.getInstance(),
                    concreteForm.getObservaciones(), idUsuarioAutenticado);
        DocumentoFirmaFacadeDelegate facade = (DocumentoFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DocumentoFirmaFacadeDelegate.class);
        facade.setDsKey(this.pDataSourceKey);
        facade.firmarDocumentoPortafirmas(vo);
        } else {
            final DocumentoRelacionFirmaVO vo = new DocumentoRelacionFirmaVO(
                    new DocumentoRelacionFirmaPK(concreteForm.getIdMunicipio(),
                            concreteForm.getIdProcedimiento(), concreteForm.getIdEjercicio(),
                            concreteForm.getIdNumeroExpediente(), concreteForm.getIdTramite(),
                            concreteForm.getIdOcurrenciaTramite(), concreteForm.getIdNumeroDocumento(),
                            idUsuarioResponsable),
                    DocumentoRelacionFirmaVO.ESTADO_FIRMA_RECHAZADO, null, Calendar.getInstance(),
                    concreteForm.getObservaciones(), idUsuarioAutenticado);
            DocumentoRelacionFirmaFacadeDelegate facade = (DocumentoRelacionFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DocumentoRelacionFirmaFacadeDelegate.class);
            facade.setDsKey(this.pDataSourceKey);
            facade.firmarDocumentoPortafirmas(vo);
        }

        /* Do search */
        PrepareSearchDocumentoPortafirmasAction searchAction =  new PrepareSearchDocumentoPortafirmasAction();
        searchAction.execute(mapping,form,request,response);

        /* ---------------------------Envio de mail de confirmacion de firma ------------------------*/
        String mensaje = MESSAGE_SUCCESS;
        try {
            _log.debug("Enviando mail");

            String numeroExpediente = "";
            if (tipoDocumento == DocumentoCustomVO.DOCUMENTO_RELACION) {
                ResourceBundle common = ResourceBundle.getBundle("common");
                numeroExpediente += common.getString("constante.relacion");
            }
            numeroExpediente += concreteForm.getIdNumeroExpediente();

            MailHelper mailHelper = new MailHelper();
            Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
            String email = m_ConfigTechnical.getString("mail.send.reject.to");
            String subject = m_ConfigTechnical.getString("mail.send.reject.subject");
            String content = m_ConfigTechnical.getString("mail.send.reject.content");
            String usuario = null;
            String documento = null;
            String tramite = null;
            /* Reemplazos de campos en el asunto y el contenido del mensaje*/
            subject = subject.replaceAll("@municipio@", (new Integer(concreteForm.getIdMunicipio())).toString());
            subject = subject.replaceAll("@procedimiento@", concreteForm.getIdProcedimiento());
            subject = subject.replaceAll("@ejercicio@", (new Integer(concreteForm.getIdEjercicio())).toString());
            subject = subject.replaceAll("@expediente@", numeroExpediente);
            if (subject.indexOf("@tramite@")!=-1) {
                DefinicionTramitesValueObject dtVO = new DefinicionTramitesValueObject();
                dtVO.setCodMunicipio(Integer.toString(concreteForm.getIdMunicipio()));
                dtVO.setTxtCodigo(concreteForm.getIdProcedimiento());
                dtVO.setNumeroTramite("");
                Vector tramites = DefinicionTramitesManager.getInstance().getListaTramites(dtVO,
                        SessionManager.getAuthenticatedUser(request).getParamsCon());
                for (int i=0;i<tramites.size();i++) {
                    ElementoListaValueObject elementoVO = (ElementoListaValueObject) tramites.get(i);
                    if (elementoVO.getIdentificador().equals(Integer.toString(concreteForm.getIdTramite()))) {
                        tramite = elementoVO.getDescripcion();
                        break;
                    }
                }
                subject = subject.replaceAll("@tramite@", tramite);
            }
            subject = subject.replaceAll("@ocurrencia@", (new Integer(concreteForm.getIdOcurrenciaTramite())).toString());
            if (subject.indexOf("@documento@")!=-1) {
                documento = concreteForm.getDescripcionDocumento();
                subject = subject.replaceAll("@documento@", documento);
            }
            if (subject.indexOf("@usuario@")!=-1) {
                usuario = SessionManager.getAuthenticatedUser(request).getNombreUsu();
                subject = subject.replaceAll("@usuario@", usuario);
            }
            content = content.replaceAll("@municipio@", (new Integer(concreteForm.getIdMunicipio())).toString());
            content = content.replaceAll("@procedimiento@", concreteForm.getIdProcedimiento());
            content = content.replaceAll("@ejercicio@", (new Integer(concreteForm.getIdEjercicio())).toString());
            content = content.replaceAll("@expediente@", numeroExpediente);
            if (content.indexOf("@tramite@")!=-1) {
                if (tramite==null) {
                    DefinicionTramitesValueObject dtVO = new DefinicionTramitesValueObject();
                    dtVO.setCodMunicipio(Integer.toString(concreteForm.getIdMunicipio()));
                    dtVO.setTxtCodigo(concreteForm.getIdProcedimiento());
                    dtVO.setNumeroTramite("");
                    Vector tramites = DefinicionTramitesManager.getInstance().getListaTramites(dtVO,
                            SessionManager.getAuthenticatedUser(request).getParamsCon());
                    for (int i=0;i<tramites.size();i++) {
                        ElementoListaValueObject elementoVO = (ElementoListaValueObject) tramites.get(i);
                        if (elementoVO.getIdentificador().equals(Integer.toString(concreteForm.getIdTramite()))) {
                            tramite = elementoVO.getDescripcion();
                            break;
                        }
                    }
                }
                content = content.replaceAll("@tramite@", tramite);
            }
            content = content.replaceAll("@ocurrencia@", (new Integer(concreteForm.getIdOcurrenciaTramite())).toString());
            if (content.indexOf("@documento@")!=-1) {
                if (documento == null) {
                    documento = concreteForm.getDescripcionDocumento();
                }
                content = content.replaceAll("@documento@", documento);
            }
            if (content.indexOf("@usuario@")!=-1) {
                if (usuario==null) {
                    usuario = SessionManager.getAuthenticatedUser(request).getNombreUsu();
                }
                content = content.replaceAll("@usuario@", usuario);
            }
            mailHelper.sendMail(email, subject, content);
            _log.debug("mail enviado");
        } catch(MailServiceNotActivedException e) {
            e.printStackTrace();
        } catch(MailException e) {
            mensaje = MESSAGE_SUCCESS_MAIL_FAIL;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalErrorException(e);
        }
        /*---------------------------------------------------------------------------------*/

        /* Save messages */
        saveSingleMessage(request,new ActionMessage(mensaje));

        /* Return ActionForward */
        final ActionForward result;
        if ( concreteForm.getDoPopUp() ) result = mapping.findForward( searchAction.getPopUpMappingKey() );
        else if ( concreteForm.getDoPrintPreview() ) result = mapping.findForward( searchAction.getPrintPreviewMappingKey() );
        else result = mapping.findForward( searchAction.getDefaultMappingKey() );
        if (_log.isDebugEnabled()) _log.debug(CLSNAME+ ".doPerform() END  Jumping to "+ result.getName() + "-----");
        return result;
    }//doPerform

    protected String getMainPageMapping() {
        return GlobalNames.MAINPAGE_GLOBAL_FORWARD;
    }

    protected String getInternalErrorMapping() {
        return GlobalNames.INTERNALERROR_GLOBAL_FORWARD;
    }

    protected String getModelErrorMapping() {
        return GlobalNames.MODELERROR_GLOBAL_FORWARD;
    }

}//class
/*______________________________EOF_________________________________*/