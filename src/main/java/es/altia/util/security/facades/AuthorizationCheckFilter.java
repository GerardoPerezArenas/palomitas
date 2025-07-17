/*______________________________BOF_________________________________*/
package es.altia.util.security.facades;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.facades.BusinessRequest;

import java.sql.Connection;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public abstract class AuthorizationCheckFilter {
    /*_______Operations_____________________________________________*/
    public boolean checkAuthorization(String userId, BusinessRequest request, Connection connection)
            throws InternalErrorException {
        boolean result = ( (userId!=null) && (request!=null) );
        /* Put here very checks you want to be done before every (AND EACH) other check */
        return result;
    }//checkAuthorization
}//class
/*______________________________EOF_________________________________*/
