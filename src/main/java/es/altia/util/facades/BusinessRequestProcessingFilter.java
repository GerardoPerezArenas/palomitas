/*______________________________BOF_________________________________*/
package es.altia.util.facades;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

/**
 * @version $\Date$ $\Revision$
 *
 **/
public abstract class BusinessRequestProcessingFilter {
	/*_______Attributes_____________________________________________*/
	BusinessRequestProcessingFilter _next = null;

	/*_______Operations_____________________________________________*/
	public BusinessRequestProcessingFilter(BusinessRequestProcessingFilter next) {
		_next = next;
	}//constructor
	
	public final BusinessRequestProcessingFilter getNext() {
		return _next;
	}//getNext
	
	public final void process(BusinessRequest request)
		throws ModelException, InternalErrorException {
//        final Log _log = LogFactory.getLog( this.getClass() );
//		if (_log.isDebugEnabled()) _log.debug(this.getClass().getName()+": BEGIN process() ");
		if ( doIHaveToProcess(request) )
			doProcess(request);
		else
			processNext(request);
//		if (_log.isDebugEnabled()) _log.debug(this.getClass().getName()+": END   process() ");
	}//process

	protected final void processNext(BusinessRequest request)
		throws ModelException, InternalErrorException {
		if (getNext()!=null)
			getNext().process(request);
		else
			request.execute();			
	}//processNext

	protected abstract boolean doIHaveToProcess(BusinessRequest request);

	protected abstract void doProcess(BusinessRequest request)
		throws ModelException, InternalErrorException;
}//class
/*______________________________EOF_________________________________*/
