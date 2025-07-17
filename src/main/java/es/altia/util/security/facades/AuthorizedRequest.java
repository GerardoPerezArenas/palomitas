/*______________________________BOF_________________________________*/
package es.altia.util.security.facades;

import es.altia.util.facades.BusinessRequest;

/**
 * @author
 * @version $\Date$ $\Revision$
 *
 **/
public interface AuthorizedRequest extends BusinessRequest {
	/*_______Operations_____________________________________________*/
	public String authGetUserId();
}//interface
/*______________________________EOF_________________________________*/
