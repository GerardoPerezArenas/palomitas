package es.altia.agora.interfaces.user.web.portafirmas.documentoportafirmas;

import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.struts.DefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author montserrat.rodriguez
 */
public class PrepareSubsanarDocumentoProcedimientoPortafirmasAction extends DefaultAction {
    private static final String CLSNAME = "PrepareSubsanarDocumentoPortafirmasAction";
    private static final String MAPPING_SUCCESS = "SubsanarDocumentoPortafirmasForm";
    private static final Log _log =
    LogFactory.getLog(PrepareSubsanarDocumentoProcedimientoPortafirmasAction.class.getName());

//    private String pDataSourceKey = null;

    /*_______Operations_____________________________________________*/
    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() BEGIN");

        /* Guardamos el indice del documento seleccionado para poder cancelar sabiendo que hemos cancelado */
        SessionManager.setSelectedIndex(request, request.getParameter("selectedIndex"));

        /* Return ActionForward */
        final ActionForward result = mapping.findForward(MAPPING_SUCCESS);
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() END"+form.toString());
        _log.debug(form.getClass().getCanonicalName());
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
