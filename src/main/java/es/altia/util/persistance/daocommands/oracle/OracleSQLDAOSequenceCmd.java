/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.oracle;

import es.altia.util.persistance.daocommands.SQLDAOSequenceCommand;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class OracleSQLDAOSequenceCmd implements SQLDAOSequenceCommand {
    /*_______Constants______________________________________________*/
    private static final Log _log =
            LogFactory.getLog(OracleSQLDAOSequenceCmd.class.getName());

    private static final String CLSNAME = "OracleSQLDAOSequenceCmd";

    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/
    public long nextValue(Connection connection, String sequenceName) throws InternalErrorException {
        final long result;
        final String query = new StringBuffer("SELECT ").append(sequenceName).append(".nextval FROM dual").toString();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            if (_log.isDebugEnabled()) _log.debug(CLSNAME+".nextValue() BEGIN");
            /* Create "Statement". */
            statement = connection.createStatement();

            /* Execute query. */
            resultSet = statement.executeQuery(query);

            /* Get Result */
            resultSet.next();
            result = resultSet.getLong(1);

            /* Return the count. */
            if (_log.isDebugEnabled()) _log.debug(CLSNAME+".nextValue() END returning "+result);
            return result;
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        } finally {
            JdbcOperations.closeResultSet(resultSet);
            JdbcOperations.closeStatement(statement);
        }//try-catch
    }//nextValue
}//class

/*______________________________EOF_________________________________*/
