/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.stdsql;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.daocommands.SQLDAOCreateCommand;
import es.altia.util.persistance.daocommands.SQLDAOCreateCommandAdapter;
import es.altia.util.persistance.exceptions.DuplicateInstanceException;
import es.altia.util.persistance.impl.BasePersistanceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;



/**
  * @author
  * @version      $\Date$ $\Revision$
  *
  *
  **/
public class StdSQLDAOCreateCmd implements SQLDAOCreateCommand {
    /*_______Atributes______________________________________________*/
    private static final Log _log =
            LogFactory.getLog(StdSQLDAOCreateCmd.class.getName());

    protected SQLDAOCreateCommandAdapter createAdapter = null;
	protected String sqlQuery = null;

    /*_______Operations_____________________________________________*/
    protected StdSQLDAOCreateCmd() {}

	public StdSQLDAOCreateCmd( SQLDAOCreateCommandAdapter concreteAdapter ) {
        this.createAdapter = concreteAdapter;

        final StringBuffer buff = new StringBuffer("INSERT INTO ");
        final String[] attribs = concreteAdapter.crGetSQLAttributeNames();
        buff.append(concreteAdapter.pkGetSQLMainTableName().split(" ")[0]);
        buff.append(" ( ");
        buff.append(SQLDAOCommandsHelper.composeCommasList(attribs));
        if (concreteAdapter.getVersionNumberAttributeName()!=null) {
            buff.append(", ");
            buff.append(concreteAdapter.getVersionNumberAttributeName());
        }//if
        buff.append(" ) VALUES ( ");
        buff.append(SQLDAOCommandsHelper.composePlaceholderCommasList(attribs.length));
        if (concreteAdapter.getVersionNumberAttributeName()!=null)
            buff.append(", 0 ");
        buff.append(" ) ");
        this.sqlQuery = buff.toString();
    }//constructor

    public PersistentObject create(Connection connection, PersistentObject theVO)
        throws DuplicateInstanceException, InternalErrorException {
        PrimaryKey generatedKey = null;

        if ( (connection == null) || (theVO == null) ) {
            if (_log.isErrorEnabled()) _log.error("StdSQLDAOCreateCmd: Null input parameters!");
            throw new InternalErrorException(new Exception("StdSQLDAOCreateCmd: Null input parameters!"));
        }//if

        if (_log.isInfoEnabled()) _log.info("StdSQLDAOCreateCmd: BEGIN create("+theVO.getPrimaryKey()+")");

        if (! createAdapter.crMustGeneratePK(theVO.getPrimaryKey()) ) {
            /* Check if the object already exists. */
            if (createAdapter.crGetExistsCmd().exists(connection, theVO.getPrimaryKey())) {
                if (_log.isDebugEnabled()) _log.debug("StdSQLDAOCreateCmd: DuplicateInstanceException before inserting PersistentObject");
                throw new DuplicateInstanceException(theVO.getClass().getName(), theVO.getPrimaryKey().toString());
            }//if
        }//if

        PreparedStatement preparedStatement = null;
        try {
            /* Create "preparedStatement". */
            //if (_log.isDebugEnabled()) _log.debug("StdSQLDAOCreateCmd: 	QUERY="+sqlQuery);
            preparedStatement = connection.prepareStatement( sqlQuery );

            /* Fill "preparedStatement". */
            int i = 1;
            if (! createAdapter.crMustGeneratePK(theVO.getPrimaryKey()) ) {
                i = createAdapter.pkBind(theVO.getPrimaryKey(), preparedStatement, i);
            } else {
                generatedKey = createAdapter.crPreGenerateKey(connection, theVO);
                if (_log.isDebugEnabled()) _log.debug("StdSQLDAOCreateCmd:     Pre-generated key="+generatedKey);
                i = createAdapter.pkBind(generatedKey, preparedStatement, i);
            }//if
            createAdapter.crBindAllNonPK(theVO, preparedStatement, i);

            /* Execute query. */
            final int insertedRows = preparedStatement.executeUpdate();

            if (insertedRows == 0) {
                if (_log.isErrorEnabled()) _log.error("StdSQLDAOCreateCmd: Unknown error when inserting PersistentObject");
                throw new SQLException("Can not add row to table" +	" '"+createAdapter.pkGetSQLMainTableName()+"'");
            }//if
            if (insertedRows > 1) {
                if (_log.isDebugEnabled()) _log.debug("StdSQLDAOCreateCmd: Duplicate row when inserting PersistentObject");
                throw new SQLException("Duplicate row for pk = '" +	theVO.getPrimaryKey() + "' in table '"+createAdapter.pkGetSQLMainTableName()+"'");
            }//if

            /* Return result */
            final PersistentObject newPO = createAdapter.crReturnNewVO(connection, preparedStatement, theVO, generatedKey);
            newPO.setPersistanceContext(new BasePersistanceContext(0,false,true));
            if (_log.isInfoEnabled()) _log.info("StdSQLDAOCreateCmd: END create()");
            return newPO;
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        } finally {
            JdbcOperations.closeStatement(preparedStatement);
        }//try-catch
    }//create

    public PersistentObject[] create(Connection connection, PersistentObject[] theVO) throws DuplicateInstanceException, InternalErrorException {
        if ( (connection == null) || (theVO == null) ) {
            if (_log.isErrorEnabled()) _log.error("StdSQLDAOCreateCmd: Null input parameters!");
            throw new InternalErrorException(new Exception("StdSQLDAOCreateCmd: Null input parameters!"));
        }//if

        if (_log.isInfoEnabled()) _log.info("StdSQLDAOCreateCmd: BEGIN create("+theVO.length+" VOs)");

        /* Create "preparedStatement". */
        if (_log.isDebugEnabled()) _log.debug("StdSQLDAOCreateCmd: 	QUERY="+sqlQuery);
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement( sqlQuery );
            /* Check if can be batched updates or not */
            boolean canBeBatched=connection.getMetaData().supportsBatchUpdates();
            for (int vosIndex = 0; ( (canBeBatched) && (vosIndex < theVO.length) ); vosIndex++) {
                if (createAdapter.crMustGeneratePK(theVO[vosIndex].getPrimaryKey()) )
                    canBeBatched = false;
            }//for
            final PersistentObject[] result;
            if (canBeBatched)
                result = createWithExecBatch(connection, preparedStatement, theVO);
            else
                result = createWithExecUpdate(connection, preparedStatement, theVO);
            if (_log.isInfoEnabled()) _log.info("StdSQLDAOCreateCmd: END create([])");
            return result;
        } catch (DuplicateInstanceException e) {
            throw e;
        } catch (InternalErrorException e) {
            throw e;
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        } finally {
            JdbcOperations.closeStatement(preparedStatement);
        }//try-catch
    }//create

    public PersistentObject[] createWithExecBatch(Connection connection, PreparedStatement preparedStatement, PersistentObject[] theVO) throws DuplicateInstanceException, InternalErrorException {
        final PersistentObject[] result = theVO;
        PrimaryKey generatedKey;

        for (int vosIndex = 0; vosIndex < theVO.length; vosIndex++) {
            if (! createAdapter.crMustGeneratePK(theVO[vosIndex].getPrimaryKey()) ) {
                /* Check if the object already exists. */
                if (createAdapter.crGetExistsCmd().exists(connection, theVO[vosIndex].getPrimaryKey())) {
                    if (_log.isDebugEnabled()) _log.debug("StdSQLDAOCreateCmd: DuplicateInstanceException before inserting PersistentObject");
                    throw new DuplicateInstanceException(theVO[vosIndex].getClass().getName(), theVO[vosIndex].getPrimaryKey().toString());
                }//if
            }//if

            try {
                /* Fill "preparedStatement". */
                int i = 1;
                if (! createAdapter.crMustGeneratePK(theVO[vosIndex].getPrimaryKey()) ) {
                    i = createAdapter.pkBind(theVO[vosIndex].getPrimaryKey(), preparedStatement, i);
                } else {
                    generatedKey = createAdapter.crPreGenerateKey(connection, theVO[vosIndex]);
                    i = createAdapter.pkBind(generatedKey, preparedStatement, i);
                }//if
                createAdapter.crBindAllNonPK(theVO[vosIndex], preparedStatement, i);

                /* Execute query. */
                preparedStatement.addBatch();

                /* Save results */
                result[vosIndex].setPersistanceContext(new BasePersistanceContext(0,false,true));
            } catch (SQLException e) {
                throw new InternalErrorException(e);
            }//try-catch
        }//for

        try {
            final int[] insertedRows = preparedStatement.executeBatch();
            if ( (insertedRows!=null) && (insertedRows.length==1) ) {
                if (insertedRows[0] < result.length) {
                    if (_log.isErrorEnabled()) _log.error("StdSQLDAOCreateCmd: Unknown error when inserting PersistentObject");
                    throw new SQLException("Can not add row to table '"+createAdapter.pkGetSQLMainTableName()+"'");
                }//if
                if (insertedRows[0] > result.length) {
                    if (_log.isDebugEnabled()) _log.debug("StdSQLDAOCreateCmd: Duplicate row when inserting PersistentObject");
                    throw new SQLException("Duplicate row in table '"+createAdapter.pkGetSQLMainTableName()+"'");
                }//if
            } else if ( (insertedRows!=null) && (insertedRows.length==result.length) ) {
                for (int kk = 0; kk < insertedRows.length; kk++) {
                    final int insertedRow = insertedRows[kk];
                    if (insertedRow == 0) {
                        if (_log.isErrorEnabled()) _log.error("StdSQLDAOCreateCmd: Unknown error when inserting PersistentObject");
                        throw new SQLException("Can not add row to table" +	" '"+createAdapter.pkGetSQLMainTableName()+"'");
                    }//if
                    if (insertedRow > 1) {
                        if (_log.isDebugEnabled()) _log.debug("StdSQLDAOCreateCmd: Duplicate row when inserting PersistentObject");
                        throw new SQLException("Duplicate row for pk = '" +	theVO[kk].getPrimaryKey() + "' in table '"+createAdapter.pkGetSQLMainTableName()+"'");
                    }//if
                }//for
            } else {
                if (_log.isErrorEnabled()) _log.error("StdSQLDAOCreateCmd: Unknown error when inserting PersistentObject");
                throw new SQLException("Can not add row to table '"+createAdapter.pkGetSQLMainTableName()+"'");
            }//if
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }//try-catch
        return result;
    }//create

    public PersistentObject[] createWithExecUpdate(Connection connection, PreparedStatement preparedStatement, PersistentObject[] theVO) throws DuplicateInstanceException, InternalErrorException {
        final PersistentObject[] result = new PersistentObject[theVO.length];
        PrimaryKey generatedKey = null;

        for (int vosIndex = 0; vosIndex < theVO.length; vosIndex++) {
            if (! createAdapter.crMustGeneratePK(theVO[vosIndex].getPrimaryKey()) ) {
                /* Check if the object already exists. */
                if (createAdapter.crGetExistsCmd().exists(connection, theVO[vosIndex].getPrimaryKey())) {
                    if (_log.isDebugEnabled()) _log.debug("StdSQLDAOCreateCmd: DuplicateInstanceException before inserting PersistentObject");
                    throw new DuplicateInstanceException(theVO[vosIndex].getClass().getName(), theVO[vosIndex].getPrimaryKey().toString());
                }//if
            }//if

            try {
                /* Fill "preparedStatement". */
                int i = 1;
                if (! createAdapter.crMustGeneratePK(theVO[vosIndex].getPrimaryKey()) ) {
                    i = createAdapter.pkBind(theVO[vosIndex].getPrimaryKey(), preparedStatement, i);
                } else {
                    generatedKey = createAdapter.crPreGenerateKey(connection, theVO[vosIndex]);
                    i = createAdapter.pkBind(generatedKey, preparedStatement, i);
                }//if
                createAdapter.crBindAllNonPK(theVO[vosIndex], preparedStatement, i);

                /* Execute query. */
                final int insertedRows = preparedStatement.executeUpdate();

                if (insertedRows == 0) {
                    if (_log.isErrorEnabled()) _log.error("StdSQLDAOCreateCmd: Unknown error when inserting PersistentObject");
                    throw new SQLException("Can not add row to table" +	" '"+createAdapter.pkGetSQLMainTableName()+"'");
                }//if
                if (insertedRows > 1) {
                    if (_log.isDebugEnabled()) _log.debug("StdSQLDAOCreateCmd: Duplicate row when inserting PersistentObject");
                    throw new SQLException("Duplicate row for pk = '" +	theVO[vosIndex].getPrimaryKey() + "' in table '"+createAdapter.pkGetSQLMainTableName()+"'");
                }//if

                /* Return result */
                final PersistentObject newPO = createAdapter.crReturnNewVO(connection, preparedStatement, theVO[vosIndex], generatedKey);
                newPO.setPersistanceContext(new BasePersistanceContext(0,false,true));
                result[vosIndex]=newPO;
            } catch (SQLException e) {
                throw new InternalErrorException(e);
            }//try-catch
        }//for
        return result;
    }//create

}//class
/*______________________________EOF_________________________________*/
