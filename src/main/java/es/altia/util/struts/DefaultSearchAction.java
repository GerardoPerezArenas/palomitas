package es.altia.util.struts;

import es.altia.util.collections.CollectionsFactory;
import es.altia.util.collections.CollectionsOperations;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class DefaultSearchAction extends DefaultAction {
    private static final int EXPECTED_PARAMETERS_NUMBER = 10;
    protected static Log _log =
            LogFactory.getLog(DefaultSearchAction.class.getName());

    protected abstract void putCommonSearchParameters(Map parameters, DefaultSearchActionForm concreteForm);
	protected abstract String getDefaultMappingKey();
	protected abstract String getPrintPreviewMappingKey();
	protected abstract String getPopUpMappingKey();
	protected abstract List doSearch(ActionMapping mapping, DefaultSearchActionForm form, 
					HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException, InternalErrorException, ModelException;

    protected void doInitSearchForm(ActionMapping mapping, DefaultSearchActionForm form,
                    HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException, InternalErrorException, ModelException {
    }//doInitSearchForm

	public ActionForward doPerform(ActionMapping mapping,
		ActionForm form, HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException, InternalErrorException,
		ModelException {

		List vos =  null;
		
		if (_log.isDebugEnabled()) _log.debug("DefaultSearchAction: BEGIN ----------------------------");
		/* Cast "form". */
		DefaultSearchActionForm concreteForm = (DefaultSearchActionForm) form;
		
		/*
		 * If the request is to allow the user to correct errors in the form,
		 * "concreteForm" must not be modified.
		 */ 
		if (request.getAttribute(Globals.ERROR_KEY) == null) {
            if (_log.isDebugEnabled()) _log.debug("DefaultSearchAction: about to call doInitSearchForm()");
            doInitSearchForm(mapping, concreteForm, request, response);
			/* Search only if necessary */
			if (concreteForm.getDoSearch()) {
                if (_log.isDebugEnabled()) _log.debug("DefaultSearchAction: about to call doSearch()");
				vos = doSearch(mapping, concreteForm, request, response);
			}//if
			if (vos == null) vos = CollectionsOperations.getEmptyInmutableList();
			
			/*Fill results*/
			concreteForm.setResults(vos);
			if (concreteForm.getDoPageByPage()) setPageByPageParameters(concreteForm, request);
			setPrintPreviewParameters(concreteForm, request);
		}//if
		

		/* Return ActionForward. */
		if ( concreteForm.getDoPopUp() ) {
			if (_log.isDebugEnabled()) _log.debug("DefaultSearchAction:  END  Jumping to "+ getPopUpMappingKey() + "-----");
			return mapping.findForward( getPopUpMappingKey() );
		} else if ( concreteForm.getDoPrintPreview() ) {
			if (_log.isDebugEnabled()) _log.debug("DefaultSearchAction:  END  Jumping to "+ getPrintPreviewMappingKey() + "-----");
			return mapping.findForward( getPrintPreviewMappingKey() );
		} else {
			if (_log.isDebugEnabled()) _log.debug("DefaultSearchAction:  END  Jumping to "+ getDefaultMappingKey() + "-----");
			return mapping.findForward( getDefaultMappingKey() );
		}//if
	}//doPerform



	protected void setPrintPreviewParameters(DefaultSearchActionForm concreteForm, HttpServletRequest request) {
		Map previewParameters = CollectionsFactory.getInstance().newHashMap(EXPECTED_PARAMETERS_NUMBER);
		putCommonParameters(previewParameters, concreteForm);
		previewParameters.put(DefaultSearchActionForm.PRINTPREVIEW,new Boolean(true));
		previewParameters.put(DefaultSearchActionForm.STARTINDEX, new Integer(concreteForm.getStartIndex()) );
		previewParameters.put(DefaultSearchActionForm.COUNT, new Integer(concreteForm.getCount()) );
		request.setAttribute("preview", previewParameters);
		_log.info("DefaultSearchAction:  filling print preview page parameters maps");
	}//setPrintPreviewParameters

	protected void setPageByPageParameters(DefaultSearchActionForm concreteForm, HttpServletRequest request) {
			if (concreteForm.getTotalCount() > 0) {
				if ( (concreteForm.getStartIndex()-concreteForm.getCount()) >= 0 ) {
					Map previousParameters = CollectionsFactory.getInstance().newHashMap(EXPECTED_PARAMETERS_NUMBER);
					putCommonParameters(previousParameters, concreteForm);
					putPreviousParameters(previousParameters, concreteForm);
					request.setAttribute("previous", previousParameters);
					_log.info("DefaultSearchAction:  filling previous page parameters maps");
					
					Map firstParameters = CollectionsFactory.getInstance().newHashMap(EXPECTED_PARAMETERS_NUMBER);
					putCommonParameters(firstParameters, concreteForm);
					putFirstParameters(firstParameters, concreteForm);
					request.setAttribute("first", firstParameters);
					_log.info("DefaultSearchAction:  filling first page parameters maps");
				}//if
				if ( (concreteForm.getStartIndex()+concreteForm.getCount()) < (concreteForm.getTotalCount()) ) {
					Map nextParameters = CollectionsFactory.getInstance().newHashMap(EXPECTED_PARAMETERS_NUMBER);
					putCommonParameters(nextParameters, concreteForm);
					putNextParameters(nextParameters, concreteForm);
					request.setAttribute("next", nextParameters);
					_log.info("DefaultSearchAction:  filling next page parameters maps");
					
					Map lastParameters = CollectionsFactory.getInstance().newHashMap(EXPECTED_PARAMETERS_NUMBER);
					putCommonParameters(lastParameters, concreteForm);
					putLastParameters(lastParameters, concreteForm);
					request.setAttribute("last", lastParameters);					
					_log.info("DefaultSearchAction:  filling last page parameters maps");
				}//if
			}//if
	}//setPageByPageParameters



	private void putCommonParameters(Map parameters, DefaultSearchActionForm concreteForm) {
		parameters.put(DefaultSearchActionForm.CANEDIT, new Boolean(concreteForm.getCanEdit()));
		parameters.put(DefaultSearchActionForm.POPUP, new Boolean(concreteForm.getDoPopUp()));
		parameters.put(DefaultSearchActionForm.SEARCH, new Boolean(concreteForm.getDoSearch()));
		putCommonSearchParameters(parameters, concreteForm);
	}//putCommonParameters






	private void putNextParameters(Map parameters, DefaultSearchActionForm concreteForm) {
		int newStartIndex = concreteForm.getStartIndex()+concreteForm.getCount();
		parameters.put(DefaultSearchActionForm.STARTINDEX, new Integer(newStartIndex) );
		parameters.put(DefaultSearchActionForm.COUNT, new Integer(concreteForm.getCount()) );
	}//putNextParameters

	private void putPreviousParameters(Map parameters, DefaultSearchActionForm concreteForm) {
		int newStartIndex = concreteForm.getStartIndex()-concreteForm.getCount();
		if (newStartIndex < 0) newStartIndex = 0;
		parameters.put(DefaultSearchActionForm.STARTINDEX, new Integer(newStartIndex) );
		parameters.put(DefaultSearchActionForm.COUNT, new Integer(concreteForm.getCount()) );
	}//putNextParameters

	private void putFirstParameters(Map parameters, DefaultSearchActionForm concreteForm) {
		parameters.put(DefaultSearchActionForm.STARTINDEX, new Integer(0) );
		parameters.put(DefaultSearchActionForm.COUNT, new Integer(concreteForm.getCount()) );
	}//putFirstParameters

	private void putLastParameters(Map parameters, DefaultSearchActionForm concreteForm) {
	
		long theReminder = 0;
		if (concreteForm.getCount() > 0)
			theReminder = ( concreteForm.getTotalCount() % concreteForm.getCount() );
		if (theReminder == 0) theReminder = concreteForm.getCount();
		long newStartIndex = concreteForm.getTotalCount() - theReminder;
		if (newStartIndex < 0) newStartIndex = 0;
		parameters.put(DefaultSearchActionForm.STARTINDEX, new Integer((int) newStartIndex) );
		parameters.put(DefaultSearchActionForm.COUNT, new Integer(concreteForm.getCount()) );
	}//putLastParameters



}//class
