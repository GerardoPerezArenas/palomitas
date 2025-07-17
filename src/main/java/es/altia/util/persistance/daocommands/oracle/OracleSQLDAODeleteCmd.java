/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.oracle;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.daocommands.SQLDAODeleteCommand;
import es.altia.util.persistance.daocommands.SQLDAOPKCommandAdapter;
import es.altia.util.persistance.daocommands.stdsql.SQLDAOCommandsHelper;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.exceptions.IntegrityViolationAttemptedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;



/**
  * @author
  * @version      $\Date$ $\Revision$
  *
  **/
public class OracleSQLDAODeleteCmd implements SQLDAODeleteCommand {
    private static final String CLSNAME= "OracleSQLDAODeleteCmd";
    private static final int[] INTEGRITY_VIOLATION_ERROR_CODES = {1,1400,1722,2291};

    /*_______Atributes______________________________________________*/
    private static final Log _log =
            LogFactory.getLog(OracleSQLDAODeleteCmd.class.getName());
    
    private SQLDAOPKCommandAdapter pkAdapter = null;
	private String sqlQuery = null;

    /*_______Operations_____________________________________________*/
	public OracleSQLDAODeleteCmd( SQLDAOPKCommandAdapter concreteAdapter) {
		final String[] attribs = concreteAdapter.pkGetSQLPKAttributeNames();
		final StringBuffer buff = new StringBuffer("DELETE FROM ");
		buff.append(concreteAdapter.pkGetSQLMainTableName());
		buff.append(" WHERE ( ");
		buff.append(SQLDAOCommandsHelper.composeWhereCriteriaList(attribs));
		buff.append(" )");
		this.pkAdapter = concreteAdapter;
		this.sqlQuery = buff.toString();
	}//constructor
	
	
	public void delete(Connection connection, PrimaryKey primaryKey)
            throws InstanceNotFoundException, InternalErrorException, IntegrityViolationAttemptedException {
		PreparedStatement preparedStatement = null;

		try {
			if (_log.isInfoEnabled()) _log.info(CLSNAME+".delete("+primaryKey+") BEGIN");

			/* Create "preparedStatement". */
			//if (_log.isDebugEnabled()) _log.debug(CLSNAME+".delete() QUERY= " + sqlQuery);
			preparedStatement = connection.prepareStatement( sqlQuery );

			/* Fill "preparedStatement". */
			pkAdapter.pkBind(primaryKey, preparedStatement, 1);

			/* Execute query. */
			final int deletedRows = preparedStatement.executeUpdate();

			if (deletedRows == 0) {
				if (_log.isWarnEnabled()) _log.warn(CLSNAME+".delete() InstanceNotFoundException removing object with pk="+primaryKey);
				throw new InstanceNotFoundException(PersistentObject.class.getName(),primaryKey.toString());
			}//if
            if (_log.isInfoEnabled()) _log.info(CLSNAME+".delete() END");
		} catch (SQLException e) {
            if (Arrays.binarySearch(INTEGRITY_VIOLATION_ERROR_CODES,e.getErrorCode()) >= 0 )
                throw new IntegrityViolationAttemptedException(PersistentObject.class.getName(),primaryKey.toString());
            else
			    throw new InternalErrorException(e);
		} finally {
			JdbcOperations.closeStatement(preparedStatement);
		}//try-catch
	}//delete

    public void delete(Connection connection, PrimaryKey[] primaryKeys)
            throws InstanceNotFoundException, InternalErrorException, IntegrityViolationAttemptedException {
        PreparedStatement preparedStatement = null;
        PrimaryKey primaryKey = null;
        try {
            if (_log.isInfoEnabled()) _log.info(CLSNAME+".delete([]) BEGIN");

            /* Create "preparedStatement". */
            //if (_log.isDebugEnabled()) _log.debug(CLSNAME+".delete([]) QUERY= " + sqlQuery);
            preparedStatement = connection.prepareStatement( sqlQuery );

            for (int i = 0; i < primaryKeys.length; i++) {
                primaryKey = primaryKeys[i];
                /* Fill "preparedStatement". */
                pkAdapter.pkBind(primaryKey, preparedStatement, 1);

                /* Execute query. */
                final int deletedRows = preparedStatement.executeUpdate();

                if (deletedRows == 0) {
                    if (_log.isWarnEnabled()) _log.warn(CLSNAME+".delete() InstanceNotFoundException removing object with pk="+primaryKey);
                    throw new InstanceNotFoundException(PersistentObject.class.getName(),primaryKey.toString());
                }//if
            }//for

            if (_log.isInfoEnabled()) _log.info(CLSNAME+".delete() END");
        } catch (SQLException e) {
            if (Arrays.binarySearch(INTEGRITY_VIOLATION_ERROR_CODES,e.getErrorCode()) >= 0 )
                throw new IntegrityViolationAttemptedException(PersistentObject.class.getName(),((primaryKey!=null)?primaryKey.toString():"null"));
            else
			    throw new InternalErrorException(e);
        } finally {
            JdbcOperations.closeStatement(preparedStatement);
        }//try-catch
    }//delete
}//class
/*______________________________EOF_________________________________*/
