/*______________________________BOF_________________________________*/
package es.altia.util.jdbc.logdecorator;

import es.altia.util.jdbc.unwrapper.JdbcUnwrapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @version $\Date$ $\Revision$
 */
public class LogUnwrapper implements JdbcUnwrapper {
    /*_______Operations_____________________________________________*/
    public Connection unwrap(Connection conn) {
        if (conn instanceof LoggingConnection)
            return ((LoggingConnection)conn).getDecoratedConnection();
        else
            return conn;
    }

    public DataSource unwrap(DataSource ds) {
        if (ds instanceof LoggingDataSource)
            return ((LoggingDataSource)ds).getDecorated();
        else
            return ds;
    }

    public Statement unwrap(Statement st) {
        if (st instanceof LoggingStatement)
            return ((LoggingStatement)st).getDecorated();
        else
            return st;
    }

    public PreparedStatement unwrap(PreparedStatement ps) {
        if (ps instanceof LoggingPreparedStatement)
            return ((LoggingPreparedStatement)ps).getDecoratedPreparedStatement();
        else
            return ps;
    }

    public ResultSet unwrap(ResultSet rs) {
        return rs;
    }

}//class
/*______________________________EOF_________________________________*/
