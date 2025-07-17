/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.stdsql;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.daocommands.SQLDAODeleteByCriteriaCommand;
import es.altia.util.persistance.daocommands.SQLDAOPKCommandAdapter;
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
  * @author
  * @version      $\Date$ $\Revision$
  *
  **/
public class StdSQLDAODeleteByCriteriaCmd implements SQLDAODeleteByCriteriaCommand {
    /*_______Atributes______________________________________________*/
    private static final Log _log =
            LogFactory.getLog(StdSQLDAODeleteByCriteriaCmd.class.getName());
    
    private String sqlQuery = null;

    /*_______Operations_____________________________________________*/
	public StdSQLDAODeleteByCriteriaCmd( SQLDAOPKCommandAdapter concreteAdapter) {
		final StringBuffer buff = new StringBuffer("DELETE FROM ");
		buff.append(concreteAdapter.pkGetSQLMainTableName());
		this.sqlQuery = buff.toString();
	}//constructor
	
	public int delete(Connection connection, SearchCriteria criteria)
		throws InternalErrorException {
		PreparedStatement preparedStatement = null;

		try {
			if (_log.isInfoEnabled()) _log.info("StdSQLDAODeleteCmd: BEGIN delete(_criteria_)");

			/* Create "preparedStatement". */			
			final StringBuffer queryString = new StringBuffer(sqlQuery);
			if (criteria!=null) {
				queryString.append(" WHERE ( ");
				queryString.append(criteria.getSQLStringToPrepare());
				queryString.append(" ) ");
			}//if
			//if (_log.isDebugEnabled()) _log.debug("StdSQLDAOSearchCmd:       QUERY=" + queryString.toString());
			preparedStatement = connection.prepareStatement(queryString.toString());

			/* Fill "preparedStatement". */
			if (criteria!=null) criteria.bind(preparedStatement, 1);
			
			/* Execute query. */
            final int result = preparedStatement.executeUpdate();
            if (_log.isInfoEnabled()) _log.info("StdSQLDAODeleteCmd: END delete()");
			return result;
		} catch (SQLException e) {
			throw new InternalErrorException(e);
		} finally {
			JdbcOperations.closeStatement(preparedStatement);
		}//try-catch
	}//delete

}//class
/*______________________________EOF_________________________________*/
