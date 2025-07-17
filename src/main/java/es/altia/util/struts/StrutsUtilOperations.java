/*______________________________BOF_________________________________*/
package es.altia.util.struts;

import es.altia.util.struts.exceptions.InvalidParameterException;
import es.altia.util.struts.exceptions.MissingParameterException;
import es.altia.util.validator.FormattedLabel;
import es.altia.util.commons.BasicValidations;
import es.altia.util.commons.BasicTypesOperations;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class StrutsUtilOperations {
    /*_______Operations_____________________________________________*/
    private StrutsUtilOperations() {}

    public static final ActionMessage toActionMessage(final FormattedLabel lbl) {
        ActionMessage result = null;
        if (lbl!=null) {
            if (lbl.getParameters()!=null)
                result = new ActionMessage(lbl.getLabel(),lbl.getParameters());
            else
                result = new ActionMessage(lbl.getLabel());
        }//if
        return result;
    }//toActionMessage

    public static final ActionError toActionError(final FormattedLabel lbl) {
        ActionError result = null;
        if (lbl!=null) {
            if (lbl.getParameters()!=null)
                result = new ActionError(lbl.getLabel(),lbl.getParameters());
            else
                result = new ActionError(lbl.getLabel());
        }//if
        return result;
    }//toActionError

    public static final ActionMessages toGlobalActionMessages(final List formattedLabels) {
        ActionMessages result= null;
        ActionMessage msg;
        if (formattedLabels!=null) {
            result = new ActionMessages();
            for (Iterator iterator = formattedLabels.iterator(); iterator.hasNext();) {
                final FormattedLabel label = (FormattedLabel) iterator.next();
                msg = toActionMessage(label);
                if (msg!=null) result.add(ActionMessages.GLOBAL_MESSAGE,msg);
            }//for
        }//if
        return result;
    }//toGlobalActionMessages

    public static final ActionErrors toGlobalActionErrors(final List formattedLabels) {
        ActionErrors result= null;
        ActionError msg;
        if (formattedLabels!=null) {
            result = new ActionErrors();
            for (Iterator iterator = formattedLabels.iterator(); iterator.hasNext();) {
                final FormattedLabel label = (FormattedLabel) iterator.next();
                msg = toActionError(label);
                if (msg!=null) result.add(ActionErrors.GLOBAL_ERROR,msg);
            }//for
        }//if
        return result;
    }//toGlobalActionErrors

    public static final Locale getLocale(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);

        if (session == null)
            return Locale.getDefault();
        final Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
        if (locale == null)
            return Locale.getDefault();
        else
            return locale;
    }//getLocale

    public static final ActionMessages getErrors(final HttpServletRequest request) {
        return (ActionMessages)request.getAttribute(Globals.ERROR_KEY);
    }//getErrors

    public static final ActionMessages getMessages(final HttpServletRequest request) {
        return (ActionMessages)request.getAttribute(Globals.MESSAGE_KEY);
    }//getMessages

    public static final MessageResources getMessageResources(final HttpServletRequest request, final ServletContext app) {
        final ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request, app);
        return (MessageResources)app.getAttribute(Globals.MESSAGES_KEY + moduleConfig.getPrefix());
    }//getMessageResources

/*
    public static final MessageResources getMessageResources(HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        if (session!=null) {
            final ServletContext servletCtx = session.getServletContext();
            if (servletCtx!=null)
                return StrutsUtilOperations.getMessageResources(request,servletCtx);
            else
                return null;
        } else {
            return null;
        }//if
    }//getMessageResources

    public static final String getSimpleMessage(final HttpServletRequest request, final String key) {
        String result= null;
        final MessageResources resources = getMessageResources(request);
        if (resources!=null)
            result = resources.getMessage(key);
        return result;
    }//getSimpleMessage

*/

    public static char getCharParameter(final HttpServletRequest request, final String paramName, final boolean mandatory, final char defaultValue)
        throws MissingParameterException, InvalidParameterException {
        char result=defaultValue;

        final String resultAsString=request.getParameter(paramName);
        if (resultAsString==null) {
            if (mandatory) throw new MissingParameterException(StrutsUtilOperations.class,paramName);
        } else if (resultAsString.length()!=1) {
            throw new InvalidParameterException(StrutsUtilOperations.class,paramName);
        } else {
            result=resultAsString.charAt(0);
        }//if
        return result;
    }//getCharParameter

    public static Integer getIntegerParameter(final HttpServletRequest request, final String paramName) {
        final String resultAsString=request.getParameter(paramName);
        if (BasicValidations.isEmpty(resultAsString)) return null;
        return BasicTypesOperations.toInteger(resultAsString);
    }//getIntegerParameter


    public static final void removeActionFormAttributeFromScope(ActionMapping mapping, HttpServletRequest request) {
        String attribName = mapping.getAttribute();
        String scope = mapping.getScope();
        if ("request".equalsIgnoreCase(scope)) {
           request.removeAttribute(attribName);
        } else if ("session".equalsIgnoreCase(scope)) {
            HttpSession session = request.getSession(false);
            if (session!=null) session.removeAttribute(attribName);
        }//if
    }//removeActionFormAttributeFromScope


   /**
     * Devuelve el protocolo usado por la request usando
     * ServletRequest.getRequestURL(), quedandonos con la parte
     * anterior a los 2 puntos : , para poder usarlo en la construcción de 
     * una URL.
     * @author Juan Jato
     * @param request HttpServletRequest.
     */
    public static final String getProtocol(HttpServletRequest request) {
    	String p = request.getRequestURL().toString();
    	p = p.substring(0, p.indexOf(":")).toLowerCase();
    	// Valor por defecto
    	if ((!p.equals("http")) && (!p.equals("https"))) {
    		p = "http";
    	}
    	return p;
    }

}//class
/*______________________________EOF_________________________________*/
