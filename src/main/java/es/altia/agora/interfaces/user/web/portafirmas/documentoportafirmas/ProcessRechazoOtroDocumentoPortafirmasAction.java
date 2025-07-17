package es.altia.agora.interfaces.user.web.portafirmas.documentoportafirmas;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.portafirmas.documentofirma.vo.*;
import es.altia.agora.business.portafirmas.persistence.manual.EDocExtPortafirmasManager;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.struts.DefaultAction;
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


/**
 *
 * @author oscar.rodriguez
 */
public class ProcessRechazoOtroDocumentoPortafirmasAction extends DefaultAction{

      /*_______Constants______________________________________________*/
    private static final String CLSNAME = "ProcessRechazoDocumentoPortafirmasAction";
    private static final String MESSAGE_SUCCESS = "Portafirmas.FirmaDocumentoPortafirmasForm.DocumentoRechazado";
    private static final String MESSAGE_SUCCESS_FAIL = "Portafirmas.FirmaDocumentoPortafirmasForm.DocumentoNoRechazado";

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
        int idUsuarioAutenticado  = SessionManager.getAuthenticatedUser(request).getIdUsuario();
        int idUsuarioResponsable = concreteForm.getUsuarioFirmante();
                
        UsuarioValueObject usuarioVO = (UsuarioValueObject)request.getSession().getAttribute("usuario");
        boolean documentoRechazado = false;
        if(!"".equals(concreteForm.getObservaciones())){
            DocumentoOtroFirmaVO doc = new DocumentoOtroFirmaVO();
            doc.setCodigoDocumento(concreteForm.getIdNumeroDocumento());            
            doc.setCodigoUsuarioFirma(Integer.toString(idUsuarioAutenticado));
            doc.setFechaFirma(Calendar.getInstance());
            doc.setObservaciones(concreteForm.getObservaciones());

            documentoRechazado = EDocExtPortafirmasManager.getInstance().rechazarDocumento(doc, usuarioVO.getParamsCon());
        }

        String mensaje = null;
        if(documentoRechazado){
            mensaje = MESSAGE_SUCCESS;
        }else
            mensaje = MESSAGE_SUCCESS_FAIL;
      
        /* Do search */
        PrepareSearchDocumentoPortafirmasAction searchAction =  new PrepareSearchDocumentoPortafirmasAction();
        searchAction.execute(mapping,form,request,response);

        
        

        

       
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
}
