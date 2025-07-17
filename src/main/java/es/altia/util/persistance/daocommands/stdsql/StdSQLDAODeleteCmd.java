/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.stdsql;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.daocommands.SQLDAODeleteCommand;
import es.altia.util.persistance.daocommands.SQLDAOPKCommandAdapter;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
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
public class StdSQLDAODeleteCmd implements SQLDAODeleteCommand {
    /*_______Atributes______________________________________________*/
    private static final Log _log =
            LogFactory.getLog(StdSQLDAODeleteCmd.class.getName());

    private SQLDAOPKCommandAdapter pkAdapter = null;
	private String sqlQuery = null;

    /*_______Operations_____________________________________________*/
	public StdSQLDAODeleteCmd( SQLDAOPKCommandAdapter concreteAdapter) {
		final String[] attribs = concreteAdapter.pkGetSQLPKAttributeNames();
		final StringBuffer buff = new StringBuffer("DELETE FROM ");
		buff.append(concreteAdapter.pkGetSQLMainTableName().split(" ")[0]);
		buff.append(" WHERE ( ");
		buff.append(SQLDAOCommandsHelper.composeWhereCriteriaList(attribs));
		buff.append(" )");
		this.pkAdapter = concreteAdapter;
		this.sqlQuery = buff.toString();
	}//constructor
	
	
	public void delete(Connection connection, PrimaryKey primaryKey)
		throws InstanceNotFoundException, InternalErrorException {
		PreparedStatement preparedStatement = null;

		try {
			if (_log.isInfoEnabled()) _log.info("StdSQLDAODeleteCmd: BEGIN delete("+primaryKey+")");

			/* Create "preparedStatement". */
			//if (_log.isDebugEnabled()) _log.debug("StdSQLDAODeleteCmd: QUERY= " + sqlQuery);
			preparedStatement = connection.prepareStatement( sqlQuery );
			/* Fill "preparedStatement". */
			pkAdapter.pkBind(primaryKey, preparedStatement, 1);

			/* Execute query. */
			final int deletedRows = preparedStatement.executeUpdate();

			if (deletedRows == 0) {
				if (_log.isWarnEnabled()) _log.warn("StdSQLDAODeleteCmd: InstanceNotFoundException removing object with pk="+primaryKey);
				throw new InstanceNotFoundException(PersistentObject.class.getName(),primaryKey.toString());
			}//if
            if (_log.isInfoEnabled()) _log.info("StdSQLDAODeleteCmd: END delete()");
		} catch (SQLException e) {
			throw new InternalErrorException(e);
		} finally {
			JdbcOperations.closeStatement(preparedStatement);
		}//try-catch
	}//delete

    public void delete(Connection connection, PrimaryKey[] primaryKeys)
        throws InstanceNotFoundException, InternalErrorException {
        PreparedStatement preparedStatement = null;

        try {
            if (_log.isInfoEnabled()) _log.info("StdSQLDAODeleteCmd: BEGIN delete([])");

            /* Create "preparedStatement". */
            //if (_log.isDebugEnabled()) _log.debug("StdSQLDAODeleteCmd: QUERY= " + sqlQuery);
            preparedStatement = connection.prepareStatement( sqlQuery );

            for (int i = 0; i < primaryKeys.length; i++) {
                final PrimaryKey primaryKey = primaryKeys[i];
                /* Fill "preparedStatement". */
                pkAdapter.pkBind(primaryKey, preparedStatement, 1);

                /* Execute query. */
                final int deletedRows = preparedStatement.executeUpdate();

                if (deletedRows == 0) {
                    if (_log.isWarnEnabled()) _log.warn("StdSQLDAODeleteCmd: InstanceNotFoundException removing object with pk="+primaryKey);
                    throw new InstanceNotFoundException(PersistentObject.class.getName(),primaryKey.toString());
                }//if
            }//for

            if (_log.isInfoEnabled()) _log.info("StdSQLDAODeleteCmd: END delete()");
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        } finally {
            JdbcOperations.closeStatement(preparedStatement);
        }//try-catch
    }//delete
}//class
/*______________________________EOF_________________________________*/
