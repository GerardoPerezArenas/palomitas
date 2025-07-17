/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.stdsql;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.daocommands.SQLDAOCreateIntArrayPropertyCommand;
import es.altia.util.persistance.daocommands.SQLDAOCreateIntArrayPropertyCommandAdapter;
import es.altia.util.commons.BasicTypesOperations;
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
public class StdSQLDAOCreateIntArrayPropertyCmd implements SQLDAOCreateIntArrayPropertyCommand {
    /*_______Atributes______________________________________________*/
    private static final String CLSNAME = "StdSQLDAOCreateIntArrayPropertyCmd";
    private static final Log _log =
            LogFactory.getLog(StdSQLDAOCreateIntArrayPropertyCmd.class.getName());

    private SQLDAOCreateIntArrayPropertyCommandAdapter createAdapter = null;
	private String sqlQuery = null;

    /*_______Operations_____________________________________________*/
	public StdSQLDAOCreateIntArrayPropertyCmd( SQLDAOCreateIntArrayPropertyCommandAdapter concreteAdapter ) {
		this.createAdapter = concreteAdapter;

		final StringBuffer buff = new StringBuffer("INSERT INTO ");
		final String[] attribs = concreteAdapter.cGetSQLAttributeNames();
		buff.append(concreteAdapter.cGetSQLTableName());
		buff.append(" ( ");
		buff.append(SQLDAOCommandsHelper.composeCommasList(attribs));
		buff.append(" ) VALUES ( ");
		buff.append(SQLDAOCommandsHelper.composePlaceholderCommasList(attribs.length));
		buff.append(" ) ");
		this.sqlQuery = buff.toString();
	}//constructor

    public void create(Connection connection, PrimaryKey thePK, int[] intArray)
            throws InternalErrorException {
        if ( (connection == null) || (thePK == null) ) {
            if (_log.isErrorEnabled()) _log.error(CLSNAME+".create() Null input parameters!");
            throw new InternalErrorException(new Exception(CLSNAME+".create() Null input parameters!"));
        }//if

        if (_log.isInfoEnabled()) _log.info(CLSNAME+".create("+thePK+", {"+BasicTypesOperations.toString(intArray,",")+"}) BEGIN");

        /* Create "preparedStatement". */
        //if (_log.isDebugEnabled()) _log.debug("StdSQLDAOCreateIntArrayPropertyCmd: 	QUERY="+sqlQuery);
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement( sqlQuery );
            /* Check if can be batched updates or not */
            //final boolean canBeBatched=connection.getMetaData().supportsBatchUpdates();
            final boolean canBeBatched=false;// !!!!!!!! TEMPORALY DISABLED. CHECK BATCH CODE !!!!!!
            if (canBeBatched)
                createWithExecBatch(preparedStatement, thePK, intArray);
            else
                createWithExecUpdate(preparedStatement, thePK, intArray);
            if (_log.isInfoEnabled()) _log.info(CLSNAME+".create() END");
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        } finally {
            JdbcOperations.closeStatement(preparedStatement);
        }//try-catch
    }//create

    private void createWithExecBatch(PreparedStatement preparedStatement, PrimaryKey thePK, int[] intArray)
            throws SQLException {
        for (int i = 0; i < intArray.length; i++) {
            /* Fill "preparedStatement". */
            int j = 1;
            j = createAdapter.cpkBind(thePK, preparedStatement, j);
            preparedStatement.setInt(j, intArray[i]);

            /* Execute query. */
            preparedStatement.addBatch();
        }//for

        final int[] insertedRows = preparedStatement.executeBatch();
        if ( (insertedRows!=null) && (insertedRows.length==1) ) {
            if (insertedRows[0] < intArray.length) {
                if (_log.isErrorEnabled()) _log.error(CLSNAME+".createWithExecBatch() Unknown error when inserting PersistentObject");
                throw new SQLException("Can not add row to table '"+createAdapter.cGetSQLTableName()+"'");
            }//if
            if (insertedRows[0] > intArray.length) {
                if (_log.isDebugEnabled()) _log.debug(CLSNAME+".createWithExecBatch() Duplicate row when inserting PersistentObject");
                throw new SQLException("Duplicate row in table '"+createAdapter.cGetSQLTableName()+"'");
            }//if
        } else if ( (insertedRows!=null) && (insertedRows.length==intArray.length) ) {
            for (int kk = 0; kk < insertedRows.length; kk++) {
                final int insertedRow = insertedRows[kk];
                if (insertedRow == 0) {
                    if (_log.isErrorEnabled()) _log.error(CLSNAME+".createWithExecBatch() Unknown error when inserting PersistentObject");
                    throw new SQLException("Can not add row to table" +	" '"+createAdapter.cGetSQLTableName()+"'");
                }//if
                if (insertedRow > 1) {
                    if (_log.isDebugEnabled()) _log.debug(CLSNAME+".createWithExecBatch() Duplicate row when inserting PersistentObject");
                    throw new SQLException("Duplicate row for value = '" +	intArray[kk] + "' in table '"+createAdapter.cGetSQLTableName()+"'");
                }//if
            }//for
        } else {
            if (_log.isErrorEnabled()) _log.error(CLSNAME+".createWithExecBatch() Unknown error when inserting PersistentObject");
            throw new SQLException("Can not add row to table '"+createAdapter.cGetSQLTableName()+"'");
        }//if
    }//createWithExecBatch

    private void createWithExecUpdate(PreparedStatement preparedStatement, PrimaryKey thePK, int[] intArray)
            throws SQLException {
        for (int i = 0; i < intArray.length; i++) {
            /* Fill "preparedStatement". */
            int j = 1;
            j = createAdapter.cpkBind(thePK, preparedStatement, j);
            preparedStatement.setInt(j, intArray[i]);

            /* Execute query. */
            final int insertedRows = preparedStatement.executeUpdate();
         if (insertedRows == 0) {
             if (_log.isErrorEnabled()) _log.error(CLSNAME+".createWithExecUpdate() Unknown error when inserting PersistentObject");
             throw new SQLException("Can not add row to table" +	" '"+createAdapter.cGetSQLTableName()+"'");
         }//if

         if (insertedRows > 1) {
             if (_log.isDebugEnabled()) _log.debug(CLSNAME+".createWithExecUpdate() Duplicate row when inserting PersistentObject");
             throw new SQLException("Duplicate row for pk = '" +	thePK + "' in table '"+createAdapter.cGetSQLTableName()+"'");
         }//if
        }//for
    }//createWithExecUpdate

}//class
/*______________________________EOF_________________________________*/
