/*______________________________BOF_________________________________*/
package es.altia.util.jdbc.logdecorator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @version $\Date$ $\Revision$
 */
public class LoggingDataSource implements DataSource {
    /*_______Constants______________________________________________*/

    /*_______Attributes_____________________________________________*/
    private DataSource pDecorated = null;
    private static Log _log =
            LogFactory.getLog(LoggingDataSource.class.getName());


    /*_______Operations_____________________________________________*/
    public LoggingDataSource(DataSource decorated) {
        pDecorated = decorated;
    }

    public int getLoginTimeout() throws SQLException {
        return pDecorated.getLoginTimeout();
    }

    public void setLoginTimeout(int i) throws SQLException {
        pDecorated.setLoginTimeout(i);
    }

    public PrintWriter getLogWriter() throws SQLException {
        return pDecorated.getLogWriter();
    }

    public void setLogWriter(PrintWriter printWriter) throws SQLException {
        pDecorated.setLogWriter(printWriter);
    }

    public Connection getConnection() throws SQLException {
        if (_log.isDebugEnabled()) _log.debug("LoggingDataSource: getConnection()");
        return new LoggingConnection(pDecorated.getConnection());
    }

    public Connection getConnection(String s, String s1) throws SQLException {
        if (_log.isDebugEnabled()) _log.debug("LoggingDataSource: getConnection("+s+","+s1+")");
        return new LoggingConnection(pDecorated.getConnection(s,s1));
    }

    public DataSource getDecorated() {
        return pDecorated;
    }
    
     public boolean isWrapperFor(Class<?> iface) throws SQLException{
            	throw new UnsupportedOperationException("Not implemented");
        }
     
     
       public <T> T unwrap(Class<T> iface) throws SQLException{
            	throw new UnsupportedOperationException("Not implemented");
        }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}//class
/*______________________________EOF_________________________________*/
