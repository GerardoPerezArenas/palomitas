/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.stdsql;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.daocommands.SQLDAOExistsCommand;
import es.altia.util.persistance.daocommands.SQLDAOPKCommandAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



/**
  * @author
  * @version      $\Date$ $\Revision$
  *
  **/
public class StdSQLDAOExistsCmd implements SQLDAOExistsCommand {
    /*_______Atributes______________________________________________*/
    private static final Log _log =
            LogFactory.getLog(StdSQLDAOExistsCmd.class.getName());

    private SQLDAOPKCommandAdapter pkAdapter = null;
	private String sqlQuery = null;

    /*_______Operations_____________________________________________*/
	public StdSQLDAOExistsCmd( SQLDAOPKCommandAdapter concreteAdapter) {
		final String[] attribs = concreteAdapter.pkGetSQLPKAttributeNames();
		final StringBuffer buff = new StringBuffer("SELECT ");
		buff.append(SQLDAOCommandsHelper.composeCommasList(attribs));
		buff.append(" FROM ");
		buff.append(concreteAdapter.pkGetSQLMainTableName());
		buff.append(" WHERE ( ");
		buff.append(SQLDAOCommandsHelper.composeWhereCriteriaList(attribs));
		buff.append(" )");
		this.pkAdapter = concreteAdapter;
		this.sqlQuery = buff.toString();
	}//constructor
	
	
	public boolean exists(Connection connection, PrimaryKey primaryKey)
		throws InternalErrorException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			if (_log.isInfoEnabled()) _log.info("StdSQLDAOExistsCmd: BEGIN exists("+primaryKey+")");

			/* Create "preparedStatement". */
			//if (_log.isDebugEnabled()) _log.debug("StdSQLDAOExistsCmd: QUERY= " + sqlQuery);
			preparedStatement = connection.prepareStatement( sqlQuery );

			/* Fill "preparedStatement". */
			pkAdapter.pkBind(primaryKey, preparedStatement, 1);

			/* Execute query. */
			resultSet = preparedStatement.executeQuery();

			final boolean result = resultSet.next();
			if (_log.isInfoEnabled()) _log.info("StdSQLDAOExistsCmd: END exists() returning "+result);
			return result;
		} catch (SQLException e) {
			throw new InternalErrorException(e);
		} finally {
			JdbcOperations.closeResultSet(resultSet);
			JdbcOperations.closeStatement(preparedStatement);
		}//try-catch
	}//exists

    public boolean[] exists(Connection connection, PrimaryKey[] primaryKeys)
        throws InternalErrorException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;

        try {
            final boolean[] result = new boolean[primaryKeys.length];
            if (_log.isInfoEnabled()) _log.info("StdSQLDAOExistsCmd: BEGIN exists([])");

            /* Create "preparedStatement". */
            //if (_log.isDebugEnabled()) _log.debug("StdSQLDAOExistsCmd: QUERY= " + sqlQuery);
            preparedStatement = connection.prepareStatement( sqlQuery );

            for (int i = 0; i < primaryKeys.length; i++) {
                final PrimaryKey primaryKey = primaryKeys[i];
                /* Fill "preparedStatement". */
                pkAdapter.pkBind(primaryKey, preparedStatement, 1);

                /* Execute query. */
                resultSet = preparedStatement.executeQuery();

                /* Save result */
                result[i]=resultSet.next();
                JdbcOperations.closeResultSet(resultSet); resultSet = null;
            }//for

            if (_log.isInfoEnabled()) _log.info("StdSQLDAOExistsCmd: END exists()");
            return result;
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        } finally {
            JdbcOperations.closeResultSet(resultSet);
            JdbcOperations.closeStatement(preparedStatement);
        }//try-catch
    }//exists

}//class
/*______________________________EOF_________________________________*/
