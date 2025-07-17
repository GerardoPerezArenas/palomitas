/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.stdsql;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.daocommands.SQLDAOGetNextIdentifierAdapter;
import es.altia.util.persistance.daocommands.SQLDAOGetNextIdentifierCommand;
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
public class StdSQLDAOGetNextIdentifierCmd implements SQLDAOGetNextIdentifierCommand {

    private SQLDAOGetNextIdentifierAdapter pAdapter;
    private static Log _log =
            LogFactory.getLog(StdSQLDAOGetNextIdentifierCmd.class.getName());
    
    private String sqlRetrieveQuery = null;
    private String sqlUpdateQuery = null;
    private String sqlCreateQuery = null;
    private int blockSize = 1;

    private long[] pCachedIdentifiers = new long[0];
    private int pIndex = 1;


    public StdSQLDAOGetNextIdentifierCmd(SQLDAOGetNextIdentifierAdapter adapter) {
        pAdapter = adapter;

        /* Check and get blockSize */
        blockSize = pAdapter.niGetBlockSize();
        blockSize = ((blockSize>0)?(blockSize):(1));

        /* Common where clause */
        StringBuffer buff = new StringBuffer(" WHERE ( ");
        buff.append(SQLDAOCommandsHelper.composeWhereCriteriaList(pAdapter.niGetSQLPKAttributeNames()));
        buff.append(" )");
        final String whereClause = buff.toString();

        /* RetrieveQuery */
        buff = new StringBuffer("SELECT ");
        buff.append(pAdapter.niGetSQLSequenceAttributeName());
        buff.append(" , ");
        buff.append(SQLDAOCommandsHelper.composeCommasList(pAdapter.niGetSQLPKAttributeNames()));
        buff.append(" FROM ");
        buff.append(pAdapter.niGetSQLTableName());
        buff.append(whereClause);
        this.sqlRetrieveQuery = buff.toString();

        /* UpdateQuery */
        buff = new StringBuffer("UPDATE ");
		buff.append(pAdapter.niGetSQLTableName());
		buff.append(" SET ");
    	buff.append(pAdapter.niGetSQLSequenceAttributeName());
		buff.append("= ");
        buff.append(pAdapter.niGetSQLSequenceAttributeName());
        buff.append(" + ");
        buff.append(blockSize);
        buff.append(whereClause);
        this.sqlUpdateQuery = buff.toString();

        /* CreateQuery */
        buff = new StringBuffer("INSERT INTO  ");
		buff.append(pAdapter.niGetSQLTableName());
        buff.append(" ( ");
        buff.append(SQLDAOCommandsHelper.composeCommasList(pAdapter.niGetSQLPKAttributeNames()));
        buff.append(" , ");
        buff.append(pAdapter.niGetSQLSequenceAttributeName());
        buff.append(" ) VALUES ( ");
        buff.append(SQLDAOCommandsHelper.composePlaceholderCommasList(pAdapter.niGetSQLPKAttributeNames().length));
        buff.append(" , ");
        buff.append(pAdapter.niGetStartValue()+blockSize);
        buff.append(" )");
        this.sqlCreateQuery = buff.toString();
    }//constructor

    private long[] nextLongIds(Connection connection, PrimaryKey primaryKey)
            throws InternalErrorException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int i;
        boolean wasNotFound = false;

        if (_log.isInfoEnabled()) _log.info("StdSQLDAOGetNextIdentifierCmd: BEGIN nextLongIds("+primaryKey+")");
        final long[] result = new long[blockSize];

        /* Retrieve actual value */
        try {
            preparedStatement = connection.prepareStatement( sqlRetrieveQuery );
            pAdapter.niPkBind(primaryKey, preparedStatement, 1);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                wasNotFound = true;
            } else {
                result[0]=resultSet.getLong(1);
                for (i=1;i<blockSize;i++) {
                    result[i]=result[0]+i;
                }//for
            }//if
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        } finally {
            JdbcOperations.closeResultSet(resultSet);
            JdbcOperations.closeStatement(preparedStatement);
        }//try-catch

        if (wasNotFound) {
            /* Create sequence */
            try {
                //if (_log.isDebugEnabled())  _log.debug("StdSQLDAOGetNextIdentifierCmd:  Create Query..."+sqlCreateQuery);
                preparedStatement = connection.prepareStatement( sqlCreateQuery );
                pAdapter.niPkBind(primaryKey, preparedStatement, 1);
                final int createdRows = preparedStatement.executeUpdate();

                if (createdRows == 0) {
                    //if (_log.isInfoEnabled()) _log.info("StdSQLDAOGetNextIdentifierCmd:  Unknown error when inserting PersistentObject");
                    _log.error("StdSQLDAOGetNextIdentifierCmd:  Unknown error when inserting PersistentObject");
                    throw new SQLException("Can not add row to table" +	" '"+pAdapter.niGetSQLTableName()+"'");
                }//if
                if (createdRows > 1) {
                    //if (_log.isInfoEnabled()) _log.info("StdSQLDAOGetNextIdentifierCmd: DuplicateInstanceException creating PersistentObject");
                    _log.error("StdSQLDAOGetNextIdentifierCmd: DuplicateInstanceException creating PersistentObject");
                    throw new SQLException("Duplicate row for pk = '" + primaryKey.toString() + "' in table '"+pAdapter.niGetSQLTableName()+"'");
                }//if
                result[0]=pAdapter.niGetStartValue();
                for (i=1;i<blockSize;i++) {
                    result[i]=result[0]+i;
                }//for

            } catch (SQLException e) {
                throw new InternalErrorException(e);
            } finally {
                JdbcOperations.closeStatement(preparedStatement);
            }//try-catch
        } else {
            /* Update sequence */
            try {
                //if (_log.isDebugEnabled()) _log.debug("StdSQLDAOGetNextIdentifierCmd:  Update Query..."+sqlUpdateQuery);
                preparedStatement = connection.prepareStatement( sqlUpdateQuery );
                pAdapter.niPkBind(primaryKey, preparedStatement, 1);
                final int updatedRows = preparedStatement.executeUpdate();

                if (updatedRows == 0) {
                    //if (_log.isInfoEnabled()) _log.info("StdSQLDAOGetNextIdentifierCmd: InstanceNotFoundException updating PersistentObject");
                    _log.error("StdSQLDAOGetNextIdentifierCmd: InstanceNotFoundException updating PersistentObject");
                    throw new InternalErrorException(new InstanceNotFoundException(PersistentObject.class.getName(), primaryKey.toString()));
                }//if
                if (updatedRows > 1) {
                    //if (_log.isInfoEnabled()) _log.info("StdSQLDAOGetNextIdentifierCmd: DuplicateInstanceException updating PersistentObject");
                    _log.error("StdSQLDAOGetNextIdentifierCmd: DuplicateInstanceException updating PersistentObject");
                    throw new SQLException("Duplicate row for pk = '" + primaryKey.toString() + "' in table '"+pAdapter.niGetSQLTableName()+"'");
                }//if

            } catch (SQLException e) {
                throw new InternalErrorException(e);
            } finally {
                JdbcOperations.closeStatement(preparedStatement);
            }//try-catch
        }//if

        if (_log.isInfoEnabled()) _log.info("StdSQLDAOGetNextIdentifierCmd: END nextLongIds()");
        return result;
    }//nextLongIds




    public long nextLongIdentifier(Connection conn, PrimaryKey pk)
     throws InternalErrorException {
        if (_log.isInfoEnabled()) _log.info("StdSQLDAOGetNextIdentifierCmd: BEGIN nextLongIdentifier()");
        synchronized(this) {
            if (pIndex >= pCachedIdentifiers.length) {
                pCachedIdentifiers = nextLongIds(conn,pk);
                pIndex = 0;
            }//if
            if (_log.isInfoEnabled()) _log.info("StdSQLDAOGetNextIdentifierCmd: END nextLongIdentifier() returning "+pCachedIdentifiers[pIndex]);
            return pCachedIdentifiers[pIndex++];
        }//synchronized
    }//nextLongIdentifier



}//class

/*______________________________EOF_________________________________*/