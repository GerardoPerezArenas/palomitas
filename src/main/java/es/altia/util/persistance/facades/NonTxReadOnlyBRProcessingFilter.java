/*______________________________BOF_________________________________*/
package es.altia.util.persistance.facades;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.facades.BusinessRequest;
import es.altia.util.facades.BusinessRequestProcessingFilter;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.commons.DebugOperations;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * @author
 * @version $\Date$ $\Revision$
 *
 **/
public class NonTxReadOnlyBRProcessingFilter extends BusinessRequestProcessingFilter {
    /*_______Attributes_____________________________________________*/
    private static final Log _log =
            LogFactory.getLog(NonTxReadOnlyBRProcessingFilter.class.getName());

	/*_______Operations_____________________________________________*/
	public NonTxReadOnlyBRProcessingFilter(BusinessRequestProcessingFilter next) {
		super(next);
	}//constructor
	
	protected void doProcess(BusinessRequest request)
		throws ModelException, InternalErrorException {
		try {
			final NonTxReadOnlyRequest req = (NonTxReadOnlyRequest) request;
			processNonTxReadOnlyRequest(req);
		} catch (ClassCastException e) {
			throw new InternalErrorException(e);
		}//try-catch
	}//doProcess

	protected boolean doIHaveToProcess(BusinessRequest request) {
		return (request instanceof NonTxReadOnlyRequest);
	}//doIHaveToProcess
	

	private void processNonTxReadOnlyRequest(NonTxReadOnlyRequest request) 
		throws ModelException, InternalErrorException {
		Connection connection = null;
		
		if (_log.isDebugEnabled()) _log.debug("NonTxReadOnlyBRProcessingFilter.processNonTxReadOnlyRequest("+DebugOperations.getShortNameForClass(request.getClass())+") BEGIN");
		try {
			// BEFORE EXECUTE
			connection = request.txGetConnection();
			if (connection==null) {
                if (_log.isDebugEnabled()) _log.debug("NonTxReadOnlyBRProcessingFilter.processNonTxReadOnlyRequest() getting new connection");
				final String dsKey = request.txGetDataSourceKey();
                connection = JdbcOperations.getNonTxReadOnlyConnection(dsKey);
				request.txSetConnection(connection);
			}//if
			
			// EXECUTE
			processNext(request);
		
			// AFTER EXECUTE

		} catch(InternalErrorException e) {
			throw e;
		} finally {
			try {
				if ( (connection != null) && (request.txIsTopLevel()) ) {
					connection.close();
				}//if
			} catch (SQLException e) {
				throw new InternalErrorException(e);
			}//try-catch
		}//try-catch		
		if (_log.isDebugEnabled()) _log.debug("NonTxReadOnlyBRProcessingFilter.processNonTxReadOnlyRequest() END");
	}//processNonTxReadOnlyRequest

}//class
/*______________________________EOF_________________________________*/
