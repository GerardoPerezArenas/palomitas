/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.sqlserver;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.daocommands.SQLDAOBlobCommandAdapter;
import es.altia.util.persistance.daocommands.SQLDAOBlobRetrieveCommand;
import es.altia.util.persistance.daocommands.stdsql.SQLDAOCommandsHelper;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
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
public class SQLServerRawSQLDAOBlobRetrieveCmd implements SQLDAOBlobRetrieveCommand {
    private static final Log _log =
            LogFactory.getLog(SQLServerRawSQLDAOBlobRetrieveCmd.class.getName());
    private static final String CLSNAME = "SQLServerRawSQLDAOBlobRetrieveCmd";

	private SQLDAOBlobCommandAdapter retrieveAdapter = null;
	private String sqlQuery = null;


	SQLServerRawSQLDAOBlobRetrieveCmd( SQLDAOBlobCommandAdapter concreteAdapter ) {
		this.retrieveAdapter = concreteAdapter;
		
		final StringBuffer buff = new StringBuffer("SELECT ");
        buff.append(concreteAdapter.blGetSQLBlobAttributeName());
        buff.append(", ");
		buff.append(SQLDAOCommandsHelper.composeCommasList(concreteAdapter.pkGetSQLPKAttributeNames()));
		buff.append(" FROM ");
		buff.append(concreteAdapter.pkGetSQLMainTableName());
		buff.append(" WHERE ( ");
		buff.append(SQLDAOCommandsHelper.composeWhereCriteriaList(concreteAdapter.pkGetSQLPKAttributeNames()));
		buff.append(" )");
		this.sqlQuery = buff.toString();
	}//constructor
	
	
	public byte[] retrieve(Connection connection, PrimaryKey primaryKey)
		throws InstanceNotFoundException, InternalErrorException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			if (_log.isInfoEnabled()) _log.info(CLSNAME+" BEGIN retrieve("+primaryKey+")");

			/* Create "preparedStatement". */
			//if (_log.isDebugEnabled()) _log.debug(CLSNAME+" QUERY= " + sqlQuery);
			preparedStatement = connection.prepareStatement( sqlQuery );

			/* Fill "preparedStatement". */
			retrieveAdapter.pkBind(primaryKey, preparedStatement, 1);

			/* Execute query. */
			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				if (_log.isErrorEnabled()) _log.error(CLSNAME+" InstanceNotFoundException retrieving PersistentObject");
				throw new InstanceNotFoundException(PersistentObject.class.getName(),primaryKey.toString());
			}//if


			/* Return the value object. */
            final byte[] result = resultSet.getBytes(1);
            if (_log.isInfoEnabled()) _log.info(CLSNAME+" END retrieve() returning "+result.length+" bytes.");
            return result;
		} catch (SQLException e) {
			throw new InternalErrorException(e);
		} finally {
			JdbcOperations.closeResultSet(resultSet);
			JdbcOperations.closeStatement(preparedStatement);
		}//try-catch
	}//retrieve


}//class
/*______________________________EOF_________________________________*/
