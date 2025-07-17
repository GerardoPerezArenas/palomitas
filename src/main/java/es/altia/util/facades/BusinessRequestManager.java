/*______________________________BOF_________________________________*/
package es.altia.util.facades;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.persistance.facades.NonTxReadOnlyBRProcessingFilter;
import es.altia.util.persistance.facades.TransactionalBRProcessingFilter;

/**
 * @version $\Date$ $\Revision$
 */
public class BusinessRequestManager {
	/*_______Attributes_____________________________________________*/
	private static BusinessRequestManager INSTANCE = new BusinessRequestManager();
	private BusinessRequestProcessingFilter filterChain = null;
	
	/*_______Operations_____________________________________________*/
    private BusinessRequestManager() {
		init();
    }//constructor
	
	public static BusinessRequestManager getInstance() {
		return INSTANCE;
	}//getInstance
	
	public void handleRequest( BusinessRequest request )
		throws ModelException, InternalErrorException {
		if (request != null) {
			if (filterChain != null)
				filterChain.process(request);
			else
				request.execute();
		}//if
	}//handleRequest

	protected void init() {
		filterChain = /*new LoggingBRProcessingFilter(*/
                        new NonTxReadOnlyBRProcessingFilter(
                            new TransactionalBRProcessingFilter(null))/*)*/;
	}//init
	
}//class
/*______________________________EOF_________________________________*/
