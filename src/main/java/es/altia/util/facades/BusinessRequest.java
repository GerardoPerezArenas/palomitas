/*______________________________BOF_________________________________*/
package es.altia.util.facades;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;

/**
 * @version $\Date$ $\Revision$
 */
public interface BusinessRequest {
	public void execute() throws ModelException, InternalErrorException;
}//class
/*______________________________EOF_________________________________*/
