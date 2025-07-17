/*______________________________BOF_________________________________*/
package es.altia.util.security.facades;

import es.altia.util.collections.CollectionsFactory;
import es.altia.util.commons.DebugOperations;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.facades.BusinessRequest;
import es.altia.util.facades.BusinessRequestProcessingFilter;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.GlobalNames;
import es.altia.util.security.exceptions.NotAllowedException;
import es.altia.util.security.exceptions.NotAuthenticatedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



/**
 * @author
 * @version $\Date$ $\Revision$
 *
 **/
public class AuthorizedBRProcessingFilter extends BusinessRequestProcessingFilter {
	/*_______Attributes_____________________________________________*/
    private static final Log _log =
            LogFactory.getLog(AuthorizedBRProcessingFilter.class.getName());

    private List commonAuthorizationCheckFilters = Collections.synchronizedList(CollectionsFactory.getInstance().newLinkedList());
    private Map brAuthorizationCheckFilters = Collections.synchronizedMap(CollectionsFactory.getInstance().newHashMap());

	/*_______Operations_____________________________________________*/
	public AuthorizedBRProcessingFilter(BusinessRequestProcessingFilter next) {
		super(next);
        commonAuthorizationCheckFilters.add(new BasicAuthorizationCheckFilter());
	}//constructor

    /**
     * Adds an authorization filter to be checked for all BusinessRequests before
     * it's executed
     */
    public void addCommonAuthorizationCheckFilter(AuthorizationCheckFilter newFilter) {
        commonAuthorizationCheckFilters.add(newFilter);
    }//addCommonAuthorizationCheckFilter

    /**
     * Adds an authorization filter to be checked for all instances of a concrete
     * subclass of BusinessRequest before it's executed
     */
    public void addBusinessRequestAuthorizationCheckFilter(Class cls, AuthorizationCheckFilter newFilter) {
        final Object obj = brAuthorizationCheckFilters.get(cls);
        if (obj!=null) {
            if (obj instanceof List) {
                final List brFilters = (List) obj;
                brFilters.add(newFilter);
            }//if
        } else {
            final List lst = Collections.synchronizedList(CollectionsFactory.getInstance().newLinkedList());
            lst.add(newFilter);
            brAuthorizationCheckFilters.put(cls,lst);
        }//if
    }//addBusinessRequestAuthorizationCheckFilter

	protected void doProcess(BusinessRequest request)
		throws ModelException, InternalErrorException {
		try {
			final AuthorizedRequest req = (AuthorizedRequest) request;
			processAuthorizedRequest(req);
		} catch (ClassCastException e) {
			throw new InternalErrorException(e);
		}//try-catch
	}//doProcess

	protected boolean doIHaveToProcess(BusinessRequest request) {
        return (request instanceof AuthorizedRequest);
	}//doIHaveToProcess
	
	private void processAuthorizedRequest(AuthorizedRequest request)
		throws ModelException, InternalErrorException {
		if (_log.isDebugEnabled()) _log.debug("AuthorizedBRProcessingFilter: BEGIN processAuthorizedRequest()");
        final String userId = request.authGetUserId();
        if (userId==null) {
            if (_log.isWarnEnabled()) _log.warn("AuthorizedBRProcessingFilter: processAuthorizedRequest() userId == null, NOT AUTHENTICATED!");
            throw new NotAuthenticatedException();
        } else {
            boolean allOk=true;

            Connection conn = null;
            try {
                /* Get connection */
                conn = JdbcOperations.getNonTxReadOnlyConnection(GlobalNames.CFGDATASOURCE);

                /* Common checks */
                for (Iterator iterator = commonAuthorizationCheckFilters.iterator(); ( (allOk) && (iterator.hasNext()) );) {
                    final AuthorizationCheckFilter authorizationCheckFilter = (AuthorizationCheckFilter) iterator.next();
                    allOk = allOk && authorizationCheckFilter.checkAuthorization(userId,request,conn);
                }//for

                if (allOk) {
                    /* Concrete BR checks */
                    final List brChecks = (List) brAuthorizationCheckFilters.get(request.getClass());
                    if (brChecks!=null) {
                        for (Iterator iterator = brChecks.iterator(); ( (allOk) && (iterator.hasNext()) );) {
                            final AuthorizationCheckFilter authorizationCheckFilter = (AuthorizationCheckFilter) iterator.next();
                            allOk = allOk && authorizationCheckFilter.checkAuthorization(userId,request,conn);
                        }//for
                    }//if
                }//if

            } catch (InternalErrorException e) {
                if (_log.isErrorEnabled()) _log.error("AuthorizationCheckFilter: checkAuthorization() Internal Error... "+DebugOperations.getDetailedMessage(e));
                throw e;
            } finally {
                JdbcOperations.closeConnection(conn);
            }//try-catch

            /* Final decision */
            if (allOk)
                processNext(request);
            else
                throw new NotAllowedException();
        }//if
		if (_log.isDebugEnabled()) _log.debug("AuthorizedBRProcessingFilter: END processAuthorizedRequest()");
	}//processAuthorizedRequest

}//class
/*______________________________EOF_________________________________*/
