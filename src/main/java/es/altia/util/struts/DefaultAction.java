package es.altia.util.struts;

import es.altia.util.commons.DebugOperations;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.struts.exceptions.InvalidParameterException;
import es.altia.util.struts.exceptions.MissingParameterException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A base <code>Action</code> to facilitate the implementation of
 * web applications using Struts. It provides a default implementation
 * of <code>perform</code> that calls on the abstract method 
 * <code>doPerform</code>. 
 *
 */
public abstract class DefaultAction extends Action {
    /*_______Attributes_____________________________________________*/
    protected static Log _log =
            LogFactory.getLog(DefaultAction.class.getName());

    /*_______Operations_____________________________________________*/
    /**
	 *
	 * A default implementation of <code>execute</code> that calls on the 
	 * abstract method <code>doPerform</code>. Currently its responsabilities 
	 * are as follows:
	 *
	 * <ul>
	 *
	 * <li>It catches <code>InternalErrorException</code> (thrown by 
	 * <code>doPerform</code>) and calls on the protected method
	 * <code>doOnInternalErrorException</code>. 
	 *
	 * </ul>
	 */
	public ActionForward execute(ActionMapping mapping,
		ActionForm form, HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {
        ActionForward result;
		if (_log.isDebugEnabled()) _log.debug("DefaultAction: BEGIN ----------------------------");
		try {
			if (isCancelled(request)) {
                if (_log.isDebugEnabled()) _log.debug("DefaultAction: END (CANCELLED) ------------------");
	    		return (mapping.findForward(getCancelMapping()));
			}//if
		    result = doPerform(mapping, form, request, response);
            if (_log.isDebugEnabled()) _log.debug("DefaultAction: END (OK) ------------------------");
		} catch (ModelException e) {
            result= doOnModelException(mapping, form, request, response, e);
            if (_log.isDebugEnabled()) _log.debug("DefaultAction: END (MODELEXCEPTION) ------------");
		} catch (InternalErrorException e) {
			result= doOnInternalErrorException(mapping, form, request, response, e);
            if (_log.isDebugEnabled()) _log.debug("DefaultAction: END (INTERNALERROR) ------------");
		}//try-catch
        return result;
	}//perform


    protected abstract ActionForward doPerform(ActionMapping mapping,
		ActionForm form, HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException, InternalErrorException, 
		ModelException;


	/**
	 * Treats an <code>InternalErrorException</code> thrown by
	 * <code>doPerform</code>. In particular, it works as follows:
	 *
	 * <ul>
	 * <li>Logs the <code>InternalErrorException</code>.</li>
	 * <li>Returns the <code>ActionForward</code> returned by
	 * <code>mapping.findForward("InternalError")</code>.</li>
	 * </ul>
	 *
	 */
	protected ActionForward doOnInternalErrorException(
		ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response, 
		InternalErrorException internalErrorException)
		throws IOException, ServletException {

		/* 
		 * Log error, even with debug level <= 0, because it is a severe
		 * error. 
		 */
		final ServletContext servletContext = servlet.getServletConfig().getServletContext();
		servletContext.log(internalErrorException.getMessage(), internalErrorException);

        /* Save error message */
        final ActionErrors messages = new ActionErrors();
        messages.add(ActionErrors.GLOBAL_ERROR,new ActionError(internalErrorException.getMessage()));
        messages.add(ActionErrors.GLOBAL_ERROR,new ActionError(DebugOperations.getDetailedMessage(internalErrorException)));
        saveErrors( request, messages );


		/* Redirect to error page. */
		return mapping.findForward(getInternalErrorMapping());
	}//doOnInternalErrorException


	protected ActionForward doOnModelException(
		ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response, 
		ModelException modelException)
		throws IOException, ServletException {

		/* Log error */
		final ServletContext servletContext = servlet.getServletConfig().getServletContext();
		servletContext.log(modelException.getMessage(), modelException);

		/* Go to error page */
		final ActionErrors errors = new ActionErrors();
			
		errors.add(ActionErrors.GLOBAL_ERROR,
			new ActionError(modelException.getMessage()));

		saveErrors(request, errors);				
		/* Redirect to error page. */
		return mapping.findForward(getModelErrorMapping());
	}//doOnModelException




	protected String getMandatoryStringParameter(HttpServletRequest request,
		String paramName) 
		throws MissingParameterException {

		final String result=request.getParameter(paramName);
		if (result==null)
			throw new MissingParameterException(this.getClass(),paramName);
		return result;
	}//getMandatoryStringParameter

	protected Integer getMandatoryIntegerParameter(HttpServletRequest request,
		String paramName) 
		throws MissingParameterException, InvalidParameterException {
		
		final String resultAsString=request.getParameter(paramName);
		if (resultAsString==null)
			throw new MissingParameterException(this.getClass(),paramName);
		else {
			try {
				return new Integer(resultAsString);
			} catch (NumberFormatException e) {
				throw new InvalidParameterException(this.getClass(),paramName);
			}//try-catch
		}//if
	}//getMandatoryIntegerParameter

	protected Integer getOptionalIntegerParameter(HttpServletRequest request,
		String paramName) 
		throws InvalidParameterException {
		
		Integer result=null;
		final String resultAsString=request.getParameter(paramName);
		if (resultAsString!=null) {
			try {
				result=new Integer(resultAsString);
			} catch (NumberFormatException e) {
				throw new InvalidParameterException(this.getClass(),paramName);
			}//try-catch
		}//if
		return result;
	}//getMandatoryIntegerParameter


	protected Long getMandatoryLongParameter(HttpServletRequest request,
		String paramName) 
		throws MissingParameterException, InvalidParameterException {
		
		final String resultAsString=request.getParameter(paramName);
		if (resultAsString==null)
			throw new MissingParameterException(this.getClass(),paramName);
		else {
			try {
				return new Long(resultAsString);
			} catch (NumberFormatException e) {
				throw new InvalidParameterException(this.getClass(),paramName);
			}//try-catch
		}//if
	}//getMandatoryLongParameter


	protected char getMandatoryCharParameter(HttpServletRequest request,
		String paramName) 
		throws MissingParameterException, InvalidParameterException {

		final String resultAsString=request.getParameter(paramName);
		if (resultAsString==null)
			throw new MissingParameterException(this.getClass(),paramName);
		else if (resultAsString.length()!=1)
			throw new InvalidParameterException(this.getClass(),paramName);
		else 
			return resultAsString.charAt(0);
	}//getMandatoryStringParameter

    protected void saveSingleMessage(HttpServletRequest request, ActionMessage msg) {
        final ActionMessages messages = new ActionMessages();
        messages.add( ActionMessages.GLOBAL_MESSAGE, msg );
        saveMessages( request, messages );
    }//saveSingleMessage

    protected void saveSingleError(HttpServletRequest request, ActionError msg) {
        final ActionErrors errors = new ActionErrors();
        errors.add( ActionErrors.GLOBAL_ERROR, msg );
        saveErrors( request, errors );
    }//saveSingleError


    protected String getMainPageMapping() {
        return "MainPage";
    }

    protected String getInternalErrorMapping() {
        return "InternalError";
    }

    protected String getModelErrorMapping() {
        return "ModelError";
    }

    protected String getCancelMapping() {
        return getMainPageMapping();
    }

}//class
