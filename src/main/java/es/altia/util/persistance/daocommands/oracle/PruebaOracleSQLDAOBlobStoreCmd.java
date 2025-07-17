/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.oracle;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.daocommands.SQLDAOBlobCommandAdapter;
import es.altia.util.persistance.daocommands.SQLDAOBlobStoreCommand;
import es.altia.util.persistance.daocommands.stdsql.StdSQLDAOExistsCmd;
import es.altia.util.persistance.daocommands.stdsql.SQLDAOCommandsHelper;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.OraclePreparedStatement;


/**
  * @author
  * @version      $\Date$ $\Revision$
  *
  **/
public class PruebaOracleSQLDAOBlobStoreCmd implements SQLDAOBlobStoreCommand {
    private static final Log _log =
            LogFactory.getLog(PruebaOracleSQLDAOBlobStoreCmd.class.getName());
    private static final String CLSNAME = "OracleSQLDAOBlobStoreCmd";

    /*_______Atributes______________________________________________*/
    private SQLDAOBlobCommandAdapter blobAdapter = null;
    private String insertQuery = null;
    private String updateQuery = null;
    private StdSQLDAOExistsCmd existsCmd = null;

    /*_______Operations_____________________________________________*/
    PruebaOracleSQLDAOBlobStoreCmd( SQLDAOBlobCommandAdapter concreteAdapter ) {
		this.blobAdapter = concreteAdapter;
		final String [] attribs = concreteAdapter.pkGetSQLPKAttributeNames();

		StringBuffer buff = new StringBuffer("INSERT INTO ");
		buff.append(concreteAdapter.pkGetSQLMainTableName());
		buff.append(" ( ");
		buff.append(SQLDAOCommandsHelper.composeCommasList(attribs));
        buff.append(", "+concreteAdapter.blGetSQLBlobAttributeName());
		buff.append(" ) VALUES ( ");
		buff.append(SQLDAOCommandsHelper.composePlaceholderCommasList(attribs.length+1));
		buff.append(" ) ");
		this.insertQuery = buff.toString();

        buff = new StringBuffer("UPDATE ");
        buff.append(concreteAdapter.pkGetSQLMainTableName());
        buff.append(" SET ");
        buff.append(concreteAdapter.blGetSQLBlobAttributeName());
	    buff.append(" = ? ");
        buff.append(" WHERE ( ");
        buff.append(SQLDAOCommandsHelper.composeWhereCriteriaList(attribs));
        buff.append(" ) ");
        this.updateQuery = buff.toString();

        existsCmd = new StdSQLDAOExistsCmd(concreteAdapter);
	}//constructor
	

	public void store(Connection connection, PrimaryKey pk, byte[] blob)
            throws InternalErrorException {
		if ( (connection == null) || (blob == null) ) {
			if (_log.isErrorEnabled()) _log.error(CLSNAME+" Null input parameters!");
			throw new InternalErrorException(new Exception(CLSNAME+" Null input parameters!"));
		}//if
		
		if (_log.isInfoEnabled()) _log.info(CLSNAME+" BEGIN store("+blob.length+" bytes)");

        if ( existsCmd.exists(connection, pk) ) {
            /*Update*/
            if (_log.isInfoEnabled()) _log.info(CLSNAME+" already exists... trying update");
            update(connection, pk, blob);
        } else {
            /*Insert*/
            if (_log.isInfoEnabled()) _log.info(CLSNAME+" already exists... trying update");
            insert(connection, pk, blob);
        }//if
        if (_log.isInfoEnabled()) _log.info(CLSNAME+" END store("+blob.length+" bytes)");
	}//blob

    private void update(Connection connection, PrimaryKey pk, byte[] blob)
            throws InternalErrorException {
        OraclePreparedStatement preparedStatement = null;
        try {
            /* Create "preparedStatement". */
            preparedStatement = OracleOperations.toOraclePreparedStatement(connection.prepareStatement( updateQuery ));

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setBLOB(i++,OracleOperations.newOracleBlob(blob,OracleOperations.toOracleConnection(connection)));
            blobAdapter.pkBind(pk, preparedStatement, i);

            /* Execute query. */
            final int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                //if (_log.isInfoEnabled()) _log.info(CLSNAME+" InstanceNotFoundException updating blob");
                _log.error(CLSNAME+" InstanceNotFoundException updating blob");
                throw new InstanceNotFoundException(PersistentObject.class.getName(), pk.toString());
            }//if

            if (updatedRows > 1) {
                //if (_log.isInfoEnabled()) _log.info(CLSNAME+" DuplicateInstanceException updating blob");
                _log.error(CLSNAME+" DuplicateInstanceException updating blob");
                throw new SQLException("Duplicate row for pk = '" + pk.toString() + "' in table '"+blobAdapter.pkGetSQLMainTableName()+"'");
            }//if
        } catch (Exception e) {
            throw new InternalErrorException(e);
        } finally {
            JdbcOperations.closeStatement(preparedStatement);
        }//try-catch
    }//update


    private void insert(Connection connection, PrimaryKey pk, byte[] blob)
            throws InternalErrorException {
        OraclePreparedStatement preparedStatement = null;
        try {
            /* Create "preparedStatement". */
            preparedStatement = OracleOperations.toOraclePreparedStatement(connection.prepareStatement( insertQuery ));

            /* Fill "preparedStatement". */
            int i = 1;
            i = blobAdapter.pkBind(pk, preparedStatement, i);
            preparedStatement.setBLOB(i,OracleOperations.newOracleBlob(blob,OracleOperations.toOracleConnection(connection)));

            /* Execute query. */
            final int insertedRows = preparedStatement.executeUpdate();

            if (insertedRows == 0) {
                //if (_log.isInfoEnabled()) _log.info(CLSNAME+" Error inserting blob");
                _log.error(CLSNAME+" Error inserting blob");
                throw new SQLException("Cannot add row with pk ='"+ pk.toString() + "' to table '"+blobAdapter.pkGetSQLMainTableName()+"'");
            }//if

            if (insertedRows > 1) {
                //if (_log.isInfoEnabled()) _log.info(CLSNAME+" Error inserting blob");
                _log.error(CLSNAME+" Error inserting blob");
                throw new SQLException("Duplicate row for pk = '" + pk.toString() + "' in table '"+blobAdapter.pkGetSQLMainTableName()+"'");
            }//if
        } catch (Exception e) {
            throw new InternalErrorException(e);
        } finally {
            JdbcOperations.closeStatement(preparedStatement);
        }//try-catch
    }//insert



}//class
/*______________________________EOF_________________________________*/
