/*______________________________BOF_________________________________*/
package es.altia.util.struts.actions;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.commons.BasicValidations;
import es.altia.util.struts.StrutsUtilOperations;
import es.altia.util.struts.DefaultAction;
import es.altia.util.struts.DefaultActionForm;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public abstract class SimpleCrudProcessAction extends DefaultAction {
    /*_______Constants______________________________________________*/
    private static final String CLSNAME = "SimpleCrudProcessAction";
    public static final String DO_CREATE ="C";
    public static final String DO_DELETE ="D";
    public static final String DO_UPDATE ="U";

    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/

    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() BEGIN");
        /* Default jump */
        ActionForward result = mapping.findForward(this.getMainPageMapping());

        /* Cast form */
        DefaultActionForm concreteForm = (DefaultActionForm) form;

        String actionCode = concreteForm.getActionCode();
        if (!BasicValidations.isEmpty(actionCode)) {
            if (DO_CREATE.equals(actionCode)) {
                if (_log.isInfoEnabled()) _log.info(CLSNAME+".doCreate()");
                result = doCreate(mapping, concreteForm, request, response);
            } else if (DO_UPDATE.equals(actionCode)) {
                if (_log.isInfoEnabled()) _log.info(CLSNAME+".doUpdate()");
                result = doUpdate(mapping, concreteForm, request, response);
            } else if (DO_DELETE.equals(actionCode)) {
                if (_log.isInfoEnabled()) _log.info(CLSNAME+".doDelete()");
                result = doDelete(mapping, concreteForm, request, response);
            } else {
                if (_log.isWarnEnabled()) _log.warn(CLSNAME+".doPerform() ActionCode='"+actionCode+"'. What's this?");
                StrutsUtilOperations.removeActionFormAttributeFromScope(mapping,request);
            }//if
        }//if

        /* Return ActionForward. */
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() END jumping to '"+((result!=null)?result.getName():"NULL")+"'");
        return result;
    }//doPerform

    protected abstract ActionForward doCreate(ActionMapping mapping, DefaultActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, InternalErrorException, ModelException;

    protected abstract ActionForward doUpdate(ActionMapping mapping, DefaultActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, InternalErrorException, ModelException;

    protected abstract ActionForward doDelete(ActionMapping mapping, DefaultActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, InternalErrorException, ModelException;

    protected ActionForward saveSingleMessageAndForward(ActionMapping mapping, HttpServletRequest request, String messageKey, String forwardKey) {
        /* Save success messages */
        saveSingleMessage( request, new ActionMessage(messageKey) );

        /* Return ActionForward. */
        if (!BasicValidations.isEmpty(forwardKey))
            return mapping.findForward(forwardKey);
        else
            return null;
    }//saveSingleMessageAndForward

    protected ActionForward saveSingleErrorAndForward(ActionMapping mapping, HttpServletRequest request, String messageKey, String forwardKey) {
        /* Save messages */
        saveSingleError( request, new ActionError(messageKey) );

        /* Return ActionForward. */
        if (!BasicValidations.isEmpty(forwardKey))
            return mapping.findForward(forwardKey);
        else
            return null;
    }//saveSingleErrorAndForward

}//class
/*______________________________EOF_________________________________*/
