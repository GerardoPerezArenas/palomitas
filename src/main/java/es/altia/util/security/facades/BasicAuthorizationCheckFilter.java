/*______________________________BOF_________________________________*/
package es.altia.util.security.facades;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.facades.BusinessRequest;

import java.sql.Connection;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class BasicAuthorizationCheckFilter extends AuthorizationCheckFilter {
    /*_______Operations_____________________________________________*/
    public boolean checkAuthorization(String userId, BusinessRequest request, Connection connection)
            throws InternalErrorException {
        boolean result = super.checkAuthorization(userId,request,connection);
        /* Check user exists */
        /* Check user is active */

        /* Check is authorized to execute the concrete BusinessRequest class */

        /* Put here other checks you want to be done before other checks */
        return result;
    }//checkAuthorization

}//class
/*______________________________EOF_________________________________*/
