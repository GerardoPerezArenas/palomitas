package es.altia.util.struts;

import es.altia.util.exceptions.InternalErrorException;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A base clase for all preprocessing filters. Usually, concrete filters only
 * need to provide an implementation of <code>doProcess</code>. 
 */
public abstract class PreProcessingFilter {
    protected static final Log _log =
            LogFactory.getLog(PreProcessingFilter.class.getName());

    PreProcessingFilter nextFilter;

	public PreProcessingFilter(PreProcessingFilter nextFilter) {
		this.nextFilter = nextFilter;
	}//constructor
	
	/**
	 * Calls upon <code>doProcess</code>, and if necessary, continues to call 
	 * <code>process</code> on the next filter
	 */
	public ActionForward process(Action action, ActionMapping mapping,
		ActionForm form, HttpServletRequest request, 
		HttpServletResponse response) throws IOException, ServletException {
		
		final ActionForward actionForward;
		
		/* Process this filter. */
		try {
			actionForward = doProcess(action, mapping, form, request, response);
		} catch (InternalErrorException e) {
			return doOnInternalErrorException(action, mapping, form, request, 
				response, e);
		}//try-catch
		
		/* Process next filter in the chain. */
		if ((actionForward == null) && (nextFilter != null)) {
			return nextFilter.process(action, mapping, form, request, response);
		} else {
			return actionForward;
		}//if		
	}//process



	/**
     * Does the processing of this filter.
	 *
	 * @return <code>null</code> if the next filter must be processed; an
	 *         <code>ActionForward</code> otherwise
	 */
	protected abstract ActionForward doProcess(Action action, 
		ActionMapping mapping, ActionForm form, HttpServletRequest request, 
		HttpServletResponse response) throws IOException, ServletException,
			InternalErrorException;


	/**
	 * Treats an <code>InternalErrorException</code> thrown by
	 * <code>doProcess</code>. In particular, it works as follows:
	 *
	 * <ul>
	 * <li>Logs the <code>InternalErrorException</code>.</li>
	 * <li>Returns the <code>ActionForward</code> returned by
	 * <code>mapping.findForward("InternalError")</code>.</li>
	 * </ul>
	 *
	 */
	protected ActionForward doOnInternalErrorException(Action action,
		ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response, 
		InternalErrorException internalErrorException)
		throws IOException, ServletException {

		/* 
		 * Log error, even with debug level <= 0, because it is a severe
		 * error. 
		 */
		final ServletContext servletContext =
			action.getServlet().getServletConfig().getServletContext();
		servletContext.log(internalErrorException.getMessage(), 
			internalErrorException);

		/* Redirect to input page. */
		return mapping.findForward("InternalError");		
	}//doOnInternalErrorException

}//class
