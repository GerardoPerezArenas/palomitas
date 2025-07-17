package es.altia.util.jdbc;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A non-pooled implementation of a <code>DataSource</code>.
 * <p>
 * <b>WARNING:</b> The only method implemented of <code>DataSource</code> is
 * <code>getConnection()</code>. Rest of methods throw the unchecked exception 
 * <code>UnsupportedOperationException</code>.
 * <p>
 * Required configuration properties:
 * <ul>
 * <li><code>CustomDataSource/driverClassName</code>: it must specify the full
 * class name of the JBDC driver.</li>
 * <li><code>CustomDataSource/url</code>: the database URL (without user and
 * password).</li>
 * <li><code>CustomDataSource/user</code>: the database user.</li>
 * <li><code>CustomDataSource/password</code>: the user's password.</li>
 * </ul>
 */
public class CustomDataSource implements DataSource {

	private String url;
	private String user;
	private String password;
    private static final Log log =
            LogFactory.getLog(CustomDataSource.class.getName());

    public CustomDataSource() {
		super();
	}//Constructor

	public CustomDataSource(String driverClassName, String url, String user, String passwd) {

        if (log.isDebugEnabled())
            log.debug("CustomDataSource(Custom constructor)");

		this.url=url;
		this.user=user;
		this.password=passwd;
        if (log.isDebugEnabled())
            log.debug("CustomDataSource: -- URL=" + url + " -- USR=" + user + " -- PWD=" + password + " --");
		try {
			Class.forName(driverClassName);
		} catch (Exception e) {
            if (log.isFatalEnabled())
                log.fatal("CustomDataSource: Cannot load driver");
			e.printStackTrace();
		}//try-catch
	}//constructor

	public Connection getConnection() throws SQLException {
        if (log.isDebugEnabled())
           log.debug("CustomDataSource: getConnection()");
		return DriverManager.getConnection(url, user, password);
	}//getConnection
		
	public Connection getConnection(String anUsername, String aPassword)
		throws SQLException {		
        if (log.isDebugEnabled())
           log.debug("CustomDataSource: getConnection("+anUsername+","+aPassword+")");
		return DriverManager.getConnection(url, anUsername, aPassword);
	}//getConnection




	/**
	  * NOT IMPLEMENTED
	  **/
	public PrintWriter getLogWriter() throws SQLException {	
		throw new UnsupportedOperationException("Not implemented");	
	}//getLogWriter

	/**
	  * NOT IMPLEMENTED
	  **/
	public void setLogWriter(PrintWriter out) throws SQLException {
		throw new UnsupportedOperationException("Not implemented");
	}//setLogWriter

	/**
	  * NOT IMPLEMENTED
	  **/
	public void setLoginTimeout(int seconds) throws SQLException {
		throw new UnsupportedOperationException("Not implemented");
	}//setLoginTimeout

	/**
	  * NOT IMPLEMENTED
	  **/
	public int getLoginTimeout() throws SQLException {
		throw new UnsupportedOperationException("Not implemented");
	}//getLoginTimeout
        
        
        
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
