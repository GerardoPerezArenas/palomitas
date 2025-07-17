/*______________________________BOF_________________________________*/
package es.altia.util.jdbc.logdecorator;

import java.sql.Array;
import java.sql.Blob;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @version $\Date$ $\Revision$
 */
public class LoggingConnection implements Connection {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    private Connection pDecorated = null;
    private static final Log _log =
            LogFactory.getLog(LoggingConnection.class.getName());

    /*_______Operations_____________________________________________*/
    public LoggingConnection(Connection decorated) {
        pDecorated = decorated;
    }

    public Connection getDecoratedConnection() {
        return pDecorated;
    }

    public int getHoldability() throws SQLException {
        return pDecorated.getHoldability();
    }

    public int getTransactionIsolation() throws SQLException {
        return pDecorated.getTransactionIsolation();
    }

    public void clearWarnings() throws SQLException {
        pDecorated.clearWarnings();
    }

    public void close() throws SQLException {
        pDecorated.close();
    }

    public void commit() throws SQLException {
        if (_log.isDebugEnabled()) _log.debug("LoggingConnection: commit()");
        pDecorated.commit();
    }

    public void rollback() throws SQLException {
        if (_log.isDebugEnabled()) _log.debug("LoggingConnection: rollback()");
        pDecorated.rollback();
    }

    public boolean getAutoCommit() throws SQLException {
        return pDecorated.getAutoCommit();
    }

    public boolean isClosed() throws SQLException {
        return pDecorated.isClosed();
    }

    public boolean isReadOnly() throws SQLException {
        return pDecorated.isReadOnly();
    }

    public void setHoldability(int i) throws SQLException {
        pDecorated.setHoldability(i);
    }

    public void setTransactionIsolation(int i) throws SQLException {
        //final Log _log = LogFactory.getLog(this.getClass());
        //if (_log.isDebugEnabled()) _log.debug("LoggingConnection: setTransactionIsolation("+i+")");
        pDecorated.setTransactionIsolation(i);
    }

    public void setAutoCommit(boolean b) throws SQLException {
        //final Log _log = LogFactory.getLog(this.getClass());
        //if (_log.isDebugEnabled()) _log.debug("LoggingConnection: setAutoCommit("+b+")");
        pDecorated.setAutoCommit(b);
    }

    public void setReadOnly(boolean b) throws SQLException {
        //final Log _log = LogFactory.getLog(this.getClass());
        //if (_log.isDebugEnabled()) _log.debug("LoggingConnection: setReadOnly("+b+")");
        pDecorated.setReadOnly(b);
    }

    public String getCatalog() throws SQLException {
        return pDecorated.getCatalog();
    }

    public void setCatalog(String s) throws SQLException {
        pDecorated.setCatalog(s);
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return pDecorated.getMetaData();
    }

    public SQLWarning getWarnings() throws SQLException {
        return pDecorated.getWarnings();
    }

    public Savepoint setSavepoint() throws SQLException {
        return pDecorated.setSavepoint();
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        pDecorated.releaseSavepoint(savepoint);
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        pDecorated.rollback(savepoint);
    }

    public Statement createStatement() throws SQLException {
        return new LoggingStatement(this,pDecorated.createStatement());
    }

    public Statement createStatement(int i, int i1) throws SQLException {
        return new LoggingStatement(this,pDecorated.createStatement(i,i1));
    }

    public Statement createStatement(int i, int i1, int i2) throws SQLException {
        return new LoggingStatement(this,pDecorated.createStatement(i,i1,i2));
    }

    public Map getTypeMap() throws SQLException {
        return pDecorated.getTypeMap();
    }

    public void setTypeMap(Map map) throws SQLException {
        pDecorated.setTypeMap(map);
    }

    public String nativeSQL(String s) throws SQLException {
        return pDecorated.nativeSQL(s);
    }

    public CallableStatement prepareCall(String s) throws SQLException {
        return pDecorated.prepareCall(s);
    }

    public CallableStatement prepareCall(String s, int i, int i1) throws SQLException {
        return pDecorated.prepareCall(s,i,i1);
    }

    public CallableStatement prepareCall(String s, int i, int i1, int i2) throws SQLException {
        return pDecorated.prepareCall(s,i,i1,i2);
    }

    public PreparedStatement prepareStatement(String s) throws SQLException {
        if (_log.isDebugEnabled()) _log.debug("LoggingConnection: prepareStatement("+s+")");
        return new LoggingPreparedStatement(this,pDecorated.prepareStatement(s));
    }

    public PreparedStatement prepareStatement(String s, int i) throws SQLException {
        if (_log.isDebugEnabled()) _log.debug("LoggingConnection: prepareStatement("+s+", "+i+")");
        return new LoggingPreparedStatement(this,pDecorated.prepareStatement(s,i));
    }

    public PreparedStatement prepareStatement(String s, int i, int i1) throws SQLException {
        if (_log.isDebugEnabled()) _log.debug("LoggingConnection: prepareStatement("+s+", "+i+", "+i1+")");
        return new LoggingPreparedStatement(this,pDecorated.prepareStatement(s,i,i1));
    }

    public PreparedStatement prepareStatement(String s, int i, int i1, int i2) throws SQLException {
        if (_log.isDebugEnabled()) _log.debug("LoggingConnection: prepareStatement("+s+", "+i+", "+i1+", "+i2+")");
        return new LoggingPreparedStatement(this,pDecorated.prepareStatement(s,i,i1,i2));
    }

    public PreparedStatement prepareStatement(String s, int[] ints) throws SQLException {
        return new LoggingPreparedStatement(this,pDecorated.prepareStatement(s,ints));
    }

    public Savepoint setSavepoint(String s) throws SQLException {
        return pDecorated.setSavepoint(s);
    }

    public PreparedStatement prepareStatement(String s, String[] strings) throws SQLException {
        return new LoggingPreparedStatement(this,pDecorated.prepareStatement(s,strings));
    }
    
    
    public Struct createStruct(String typeName,Object[] attributes) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public Array createArrayOf(String typeName,Object[] elements) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public Properties getClientInfo() throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    
    public String getClientInfo(String name) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    

    public void setClientInfo(Properties properties) throws SQLClientInfoException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public void setClientInfo(String name, String value) throws SQLClientInfoException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    
    public boolean isValid(int timeout) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    
    public SQLXML createSQLXML() throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    
    public NClob createNClob() throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    
    public Blob createBlob() throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    

    public Clob createClob() throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public boolean isWrapperFor(java.lang.Class<?> iface) throws java.sql.SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    
    public <T> T unwrap(java.lang.Class<T> iface) throws java.sql.SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }

    public void setSchema(String schema) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getSchema() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void abort(Executor executor) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getNetworkTimeout() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}//class
/*______________________________EOF_________________________________*/
