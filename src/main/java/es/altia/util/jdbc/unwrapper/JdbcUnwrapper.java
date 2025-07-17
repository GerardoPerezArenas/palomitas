/*______________________________BOF_________________________________*/
package es.altia.util.jdbc.unwrapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * @version $\Date$ $\Revision$
 */
public interface JdbcUnwrapper {
    /*_______Operations_____________________________________________*/
    public Connection unwrap(Connection conn);
    public DataSource unwrap(DataSource ds);
    public Statement unwrap(Statement st);
    public PreparedStatement unwrap(PreparedStatement ps);
    public ResultSet unwrap(ResultSet rs);
}//class
/*______________________________EOF_________________________________*/
