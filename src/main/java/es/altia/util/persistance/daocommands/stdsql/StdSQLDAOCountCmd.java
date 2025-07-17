/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.stdsql;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.daocommands.SQLDAOCountCommand;
import es.altia.util.persistance.daocommands.SQLDAOPKCommandAdapter;
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

/**
  * @author
  * @version      $\Date$ $\Revision$
  *
  **/
public class StdSQLDAOCountCmd implements SQLDAOCountCommand {
    /*_______Atributes______________________________________________*/
    private static final Log _log =
            LogFactory.getLog(StdSQLDAOCountCmd.class.getName());

    private String sqlQuery = null;

    /*_______Operations_____________________________________________*/
	public StdSQLDAOCountCmd( SQLDAOPKCommandAdapter concreteAdapter) {
		final StringBuffer buff = new StringBuffer("SELECT COUNT (*) FROM ");
		buff.append(concreteAdapter.pkGetSQLMainTableName());
        this.sqlQuery = buff.toString();
	}//constructor
	
	public long count(Connection connection, SearchCriteria criteria)
		throws InternalErrorException {
		Statement statement = null;
		ResultSet resultSet = null;
		final long count;

		try {
			if (_log.isDebugEnabled()) _log.debug("StdSQLDAOCountCmd.count() BEGIN");
			/* Create "preparedStatement". */
			final StringBuffer queryString = new StringBuffer(sqlQuery);
			if (criteria!=null) {
				queryString.append(" WHERE ( ");
				queryString.append(criteria.getSQLStringToExecuteDirectly());
				queryString.append(" ) ");
			}//if
			//if (_log.isDebugEnabled()) _log.debug("StdSQLDAOCountCmd:       QUERY=" + queryString.toString());
            statement = connection.createStatement();

			/* Execute query. */
			resultSet = statement.executeQuery(queryString.toString());
			
			/* Get Result */
			resultSet.next();
			count = resultSet.getLong(1);
			
			/* Return the count. */
            if (_log.isDebugEnabled()) _log.debug("StdSQLDAOCountCmd.count() END returning "+count);
			return count;
		} catch (SQLException e) {
			throw new InternalErrorException(e);
		} finally {
			JdbcOperations.closeResultSet(resultSet);
			JdbcOperations.closeStatement(statement);
		}//try-catch
	}//count

    /**
     * Count operation given multiple criterias
     *
     * @param connection JDBC connection
     * @param criterias The criterias used to count entities in database. Must be not empty array.
     * They all must be not null and have all the same structure!!!
     * @return an array with the same length of input and counts inside. Null if null input criterias.
     * @throws InternalErrorException
     */
    public long[] count(Connection connection, SearchCriteria[] criterias)
        throws InternalErrorException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            if (_log.isDebugEnabled()) _log.debug("StdSQLDAOCountCmd.count([]) BEGIN");
            if ( (criterias!=null) && (criterias.length>0) ) {
                final long[] count= new long[criterias.length];

                /* Create "preparedStatement". */
                final StringBuffer queryString = new StringBuffer(sqlQuery);
                queryString.append(" WHERE ( ");
                queryString.append(criterias[0].getSQLStringToPrepare());
                queryString.append(" ) ");

                //if (_log.isDebugEnabled()) _log.debug("StdSQLDAOCountCmd:       QUERY=" + queryString.toString());
                statement = connection.prepareStatement(queryString.toString());

                for (int i = 0; i < criterias.length; i++) {
                    /* Bind statement */
                    criterias[i].bind(statement,1);

                    /* Execute query. */
                    resultSet = statement.executeQuery();

                    /* Get Result */
                    resultSet.next();
                    count[i] = resultSet.getLong(1);

                    JdbcOperations.closeResultSet(resultSet); resultSet = null;
                }//for

                /* Return the count. */
                if (_log.isDebugEnabled()) _log.debug("StdSQLDAOCountCmd.count() END");
                return count;
            } else {
                if (_log.isWarnEnabled()) _log.warn("StdSQLDAOCountCmd.count() returning null 'cos null input criterias");
                return null;
            }//if

        } catch (SQLException e) {
            throw new InternalErrorException(e);
        } finally {
            JdbcOperations.closeResultSet(resultSet);
            JdbcOperations.closeStatement(statement);
        }//try-catch
    }//count

}//class
/*______________________________EOF_________________________________*/
