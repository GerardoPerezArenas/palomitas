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
public class TransactionalBRProcessingFilter extends BusinessRequestProcessingFilter {
	/*_______Attributes_____________________________________________*/
    private static final Log _log =
            LogFactory.getLog(TransactionalBRProcessingFilter.class.getName());

	/*_______Operations_____________________________________________*/
	public TransactionalBRProcessingFilter(BusinessRequestProcessingFilter next) {
		super(next);
	}//constructor
	
	protected void doProcess(BusinessRequest request)
		throws ModelException, InternalErrorException {
		try {
			final TransactionalRequest req = (TransactionalRequest) request;
			processTransactionalRequest(req);
		} catch (ClassCastException e) {
			throw new InternalErrorException(e);
		}//try-catch
	}//doProcess

	protected boolean doIHaveToProcess(BusinessRequest request) {
		return (request instanceof TransactionalRequest);
	}//doIHaveToProcess
	

	private void processTransactionalRequest(TransactionalRequest request) 
		throws ModelException, InternalErrorException {
		Connection connection = null;
		boolean commited = false;
		
		if (_log.isDebugEnabled()) _log.debug("TransactionalBRProcessingFilter.processTransactionalRequest("+DebugOperations.getShortNameForClass(request.getClass())+") BEGIN");
		try {
			// BEFORE EXECUTE
			connection = request.txGetConnection();
			if (connection==null) {
                if (_log.isDebugEnabled()) _log.debug("TransactionalBRProcessingFilter.processTransactionalRequest() getting new connection");
				final String dsKey = request.txGetDataSourceKey();
				connection = JdbcOperations.getFullTransactionalConnection(dsKey);
				request.txSetConnection(connection);
			}//if
			
			// EXECUTE
			processNext(request);
		
			// AFTER EXECUTE
			if (request.txIsTopLevel()) {
				connection.commit();
				commited = true;
			}//if
		} catch(SQLException e) {
			throw new InternalErrorException(e);
		} finally {
			try {
				if ( (connection != null) && (request.txIsTopLevel()) ) {
					if (!commited) {
						connection.rollback();
					}//if
					connection.close();
				}//if
			} catch (SQLException e) {
				throw new InternalErrorException(e);
			}//try-catch
		}//try-catch		
		if (_log.isDebugEnabled()) _log.debug("TransactionalBRProcessingFilter.processTransactionalRequest() END");
	}//processTransactionalRequest

}//class
/*______________________________EOF_________________________________*/
