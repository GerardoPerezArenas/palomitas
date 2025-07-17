package es.altia.util.jdbc;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import es.altia.util.configuration.ConfigurationParametersManager;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;





/**
 * A non-pooled implementation of a <code>DataSource</code>.
 * <p>
 * <b>WARNING:</b> The only method implemented of <code>DataSource</code> is
 * <code>getConnection()</code>. Rest of methods throw the unchecked exception 
 * <code>UnsupportedOperationException</code>.
 * <p>
 * Required configuration properties:
 * <ul>
 * <li><code>SimpleDataSource/driverClassName</code>: it must specify the full
 * class name of the JBDC driver.</li>
 * <li><code>SimpleDataSource/url</code>: the database URL (without user and
 * password).</li>
 * <li><code>SimpleDataSource/user</code>: the database user.</li>
 * <li><code>SimpleDataSource/password</code>: the user's password.</li>
 * </ul>
 */
public class SimpleDataSource implements DataSource {

	private static final String DRIVER_CLASS_NAME_PARAMETER = 
		"CON.driver";
	private static final String URL_PARAMETER = 
		"CON.url";
	private static final String USER_PARAMETER = 
		"CON.usuario";
	private static final String PASSWORD_PARAMETER = 
		"CON.password";
		
	private static String url;
	private static String user;
	private static String password;
	private static Log log =
            LogFactory.getLog(SimpleDataSource.class.getName());

    static {

		try {
			/* Read configuration parameters. */
			String driverClassName = 
				ConfigurationParametersManager.getParameter(DRIVER_CLASS_NAME_PARAMETER);
			url = ConfigurationParametersManager.getParameter(URL_PARAMETER);
			user = ConfigurationParametersManager.getParameter(USER_PARAMETER);
			password = ConfigurationParametersManager.getParameter(PASSWORD_PARAMETER);
			/* Load driver. */	
			Class.forName(driverClassName);		
            if (log.isDebugEnabled())
                log.debug("SimpleDataSource: -- URL=" + url + " -- USR=" + user + " -- PWD=" + password + " --");
		} catch (Exception e) {
            if (log.isFatalEnabled())
                log.fatal("SimpleDataSource: Cannot read datasource configuration");
			e.printStackTrace();
		} finally {
			log = null;
		}//try-catch
	}//static
	
	public Connection getConnection() throws SQLException {
        if (log.isDebugEnabled())
           log.debug("SimpleDataSource: getConnection()");
		return DriverManager.getConnection(url, user, password);
	}//getConnection
		
	public Connection getConnection(String anUsername, String aPassword)
		throws SQLException {		
        if (log.isDebugEnabled())
           log.debug("SimpleDataSource: getConnection("+anUsername+","+aPassword+")");
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
