/*______________________________BOF_________________________________*/
package es.altia.util.struts;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.facades.BusinessFacadeDelegate;
import es.altia.util.facades.BusinessFacadeDelegateFactory;
import es.altia.util.persistance.daocommands.OrderCriteria;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author
 * @version $\Revision$ $\Date$
 */
public abstract class SimpleSearchAction extends DefaultSearchAction {
    /*_______Operations_____________________________________________*/
    protected final List doSearch(ActionMapping mapping, DefaultSearchActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws InternalErrorException {
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

        /* Get facade */
        final BusinessFacadeDelegate facade = BusinessFacadeDelegateFactory.getFacadeDelegate(doGetBusinessFacadeDelegateInterface(form));

        /* Get ordercriteria from sort parameters */
        final int sort = form.getSort();
        final String sortField = form.getSortField();
        final OrderCriteria oc;
        if ( (sort > 0) && (sortField!=null) ) {
            oc = doGetOrderCriteria(sortField, sort,facade);
        } else {
            oc = null;
        }//if

        /* Do count */
        final long totalCount = doGetTotalCount(form,facade);
        form.setTotalCount(totalCount);

        /* check upper limit of startIndex */
        if ( (form.getDoPageByPage()) && (startIndex >= totalCount) ) {
            startIndex = (int)totalCount-1;
            form.setStartIndex(startIndex);
        }//if

        /* Do search */
        return doGetResults(form, facade, oc, startIndex, count);
    }//doSearch

    protected abstract Class doGetBusinessFacadeDelegateInterface(DefaultSearchActionForm form);
    
    protected abstract long doGetTotalCount(DefaultSearchActionForm form, BusinessFacadeDelegate facade) throws InternalErrorException;
    
    protected abstract OrderCriteria doGetOrderCriteria(String sortField, int sort, BusinessFacadeDelegate facade) throws InternalErrorException;
    
    protected abstract List doGetResults(DefaultSearchActionForm form, BusinessFacadeDelegate facade,
                                OrderCriteria oc, int startIndex, int count) throws InternalErrorException;
}//class
/*______________________________EOF_________________________________*/