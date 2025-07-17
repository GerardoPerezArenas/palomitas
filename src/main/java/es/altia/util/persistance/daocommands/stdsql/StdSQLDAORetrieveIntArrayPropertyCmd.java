/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.stdsql;

import es.altia.util.collections.CollectionsFactory;
import es.altia.util.commons.BasicTypesOperations;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.daocommands.SQLDAORetrieveIntArrayPropertyCommand;
import es.altia.util.persistance.daocommands.SQLDAORetrieveIntArrayPropertyCommandAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * @author
 * @version $\Revision$ $\Date$
 */
public class StdSQLDAORetrieveIntArrayPropertyCmd implements SQLDAORetrieveIntArrayPropertyCommand {
    /*_______Atributes______________________________________________*/
    private static final Log _log =
        LogFactory.getLog(StdSQLDAORetrieveIntArrayPropertyCmd.class.getName());

    private SQLDAORetrieveIntArrayPropertyCommandAdapter adapter = null;
    private String sqlQuery = null;

    /*_______Operations_____________________________________________*/
    public StdSQLDAORetrieveIntArrayPropertyCmd( SQLDAORetrieveIntArrayPropertyCommandAdapter concreteAdapter ) {
        this.adapter = concreteAdapter;

        final String attribs= concreteAdapter.rtGetSQLPropertyAttributeName();
        final StringBuffer buff = new StringBuffer("SELECT ");
        buff.append(concreteAdapter.rtGetSQLPropertyAttributeName());
        buff.append(", ");
        buff.append(SQLDAOCommandsHelper.composeCommasList(concreteAdapter.rtGetSQLFilterAttributeNames()));
        buff.append(" FROM ");
        buff.append(concreteAdapter.rtGetSQLTableName());
        buff.append(" WHERE ( ");
        buff.append(SQLDAOCommandsHelper.composeWhereCriteriaList(concreteAdapter.rtGetSQLFilterAttributeNames()));
        buff.append(" ) ORDER BY ");
        buff.append(attribs);
        this.sqlQuery = buff.toString();
    }//constructor

    public int[] retrieveIntArray(Connection connection, PrimaryKey primaryKey)
        throws InternalErrorException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        final List resultList = CollectionsFactory.getInstance().newArrayList();
        int tmp;
        try {
            if (_log.isInfoEnabled()) _log.info("StdSQLDAORetriveIntArrayPropertyCmd: BEGIN retrieve()");
            /* Prepare statement */
            //if (_log.isDebugEnabled()) _log.debug("StdSQLDAORetriveIntArrayPropertyCmd:       QUERY=" + sqlQuery);
            preparedStatement = connection.prepareStatement(sqlQuery);

            /* Execute query */
            adapter.rtFilterBind(primaryKey, preparedStatement,1);
            resultSet = preparedStatement.executeQuery();

            /* Iterate results */
            while (resultSet.next()) {
                tmp = resultSet.getInt(1);
                resultList.add(new Integer(tmp));
            }//while

            /* Return the result. */
            if (_log.isInfoEnabled()) _log.info("StdSQLDAORetriveIntArrayPropertyCmd: END retrieve()");
            return BasicTypesOperations.toIntArrayFromIntegerCollection(resultList);
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        } finally {
            JdbcOperations.closeResultSet(resultSet);
            JdbcOperations.closeStatement(preparedStatement);
        }//try-catch
    }//retrieve

    public long[] retrieveLongArray(Connection connection, PrimaryKey primaryKey)
        throws InternalErrorException{        
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        final List resultList = CollectionsFactory.getInstance().newArrayList();
        long tmp;
        try {
            if (_log.isInfoEnabled()) _log.info("StdSQLDAORetriveIntArrayPropertyCmd: BEGIN retrieve()");
            /* Prepare statement */
            //if (_log.isDebugEnabled()) _log.debug("StdSQLDAORetriveIntArrayPropertyCmd:       QUERY=" + sqlQuery);
            preparedStatement = connection.prepareStatement(sqlQuery);

            /* Execute query */
            adapter.rtFilterBind(primaryKey, preparedStatement,1);
            resultSet = preparedStatement.executeQuery();

            /* Iterate results */
            while (resultSet.next()) {
                tmp = resultSet.getLong(1);
                resultList.add(new Long(tmp));
            }//while

            /* Return the result. */
            if (_log.isInfoEnabled()) _log.info("StdSQLDAORetriveIntArrayPropertyCmd: END retrieve()");
            return BasicTypesOperations.toLongArrayFromLongCollection(resultList);
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        } finally {
            JdbcOperations.closeResultSet(resultSet);
            JdbcOperations.closeStatement(preparedStatement);
        }//try-catch
    }//retrieve
}//class
/*______________________________EOF_________________________________*/