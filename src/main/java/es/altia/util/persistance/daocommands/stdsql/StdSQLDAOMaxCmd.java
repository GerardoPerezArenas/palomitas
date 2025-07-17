/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.stdsql;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.daocommands.SQLDAOMaxCommand;
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class StdSQLDAOMaxCmd implements SQLDAOMaxCommand {
    /*_______Atributes______________________________________________*/
    private static final Log _log =
            LogFactory.getLog(StdSQLDAOMaxCmd.class.getName());

    private String sqlQuery = null;

    /*_______Operations_____________________________________________*/
    public StdSQLDAOMaxCmd( String attrib, String tableName) {
        final StringBuffer buff = new StringBuffer("SELECT MAX(");
        buff.append(attrib);
        buff.append(") FROM ");
        buff.append(tableName);
        this.sqlQuery = buff.toString();
    }//constructor

    public Long max(Connection connection, SearchCriteria criteria) throws InternalErrorException {
        Statement stmnt = null;
        ResultSet resultSet = null;

        try {
            if (_log.isInfoEnabled()) _log.info("StdSQLDAOMaxCmd: BEGIN max()");
            /* Create "stmnt". */
            final StringBuffer queryString = new StringBuffer(sqlQuery);
            if (criteria!=null) {
                queryString.append(" WHERE ( ");
                queryString.append(criteria.getSQLStringToExecuteDirectly());
                queryString.append(" ) ");
            }//if
            //if (_log.isDebugEnabled()) _log.debug("StdSQLDAOMaxCmd:       QUERY=" + queryString.toString());

            /* Execute query. */
            stmnt = connection.createStatement();
            resultSet = stmnt.executeQuery(queryString.toString());

            /* Get Result */
            final Long result;
            if (resultSet.next())
                result = new Long(resultSet.getLong(1));
            else
                result = null;

            /* Return the result. */
            if (_log.isInfoEnabled()) _log.info("StdSQLDAOMaxCmd: END max() returning "+result);
            return result;
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        } finally {
            JdbcOperations.closeResultSet(resultSet);
            JdbcOperations.closeStatement(stmnt);
        }//try-catch
    }//max

}//class
/*______________________________EOF_________________________________*/
