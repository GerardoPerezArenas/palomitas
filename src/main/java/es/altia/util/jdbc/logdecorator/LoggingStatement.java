/*______________________________BOF_________________________________*/
package es.altia.util.jdbc.logdecorator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * @version $\Date$ $\Revision$
 */
public class LoggingStatement implements Statement {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    protected Statement pDecorated = null;
    private Connection pConnection = null;
    private final Log _log =
            LogFactory.getLog(LoggingStatement.class.getName());

    /*_______Operations_____________________________________________*/
    public LoggingStatement(Connection conn, Statement decorated) {
        pConnection = conn;
        pDecorated = decorated;
    }

    public int getFetchDirection() throws SQLException {
        return pDecorated.getFetchDirection();
    }

    public int getFetchSize() throws SQLException {
        return pDecorated.getFetchSize();
    }

    public int getMaxFieldSize() throws SQLException {
        return pDecorated.getMaxFieldSize();
    }

    public int getMaxRows() throws SQLException {
        return pDecorated.getMaxRows();
    }

    public int getQueryTimeout() throws SQLException {
        return pDecorated.getQueryTimeout();
    }

    public int getResultSetConcurrency() throws SQLException {
        return pDecorated.getResultSetConcurrency();
    }

    public int getResultSetHoldability() throws SQLException {
        return pDecorated.getResultSetHoldability();
    }

    public int getResultSetType() throws SQLException {
        return pDecorated.getResultSetType();
    }

    public int getUpdateCount() throws SQLException {
        return pDecorated.getUpdateCount();
    }

    public void cancel() throws SQLException {
        pDecorated.cancel();
    }

    public void clearBatch() throws SQLException {
        pDecorated.clearBatch();
    }

    public void clearWarnings() throws SQLException {
        pDecorated.clearWarnings();
    }

    public void close() throws SQLException {
        pDecorated.close();
    }

    public boolean getMoreResults() throws SQLException {
        return pDecorated.getMoreResults();
    }

    public int[] executeBatch() throws SQLException {
        return pDecorated.executeBatch();
    }

    public void setFetchDirection(int i) throws SQLException {
        pDecorated.setFetchDirection(i);
    }

    public void setFetchSize(int i) throws SQLException {
        pDecorated.setFetchSize(i);
    }

    public void setMaxFieldSize(int i) throws SQLException {
        pDecorated.setMaxFieldSize(i);
    }

    public void setMaxRows(int i) throws SQLException {
        pDecorated.setMaxRows(i);
    }

    public void setQueryTimeout(int i) throws SQLException {
        pDecorated.setQueryTimeout(i);
    }

    public boolean getMoreResults(int i) throws SQLException {
        return pDecorated.getMoreResults(i);
    }

    public void setEscapeProcessing(boolean b) throws SQLException {
        pDecorated.setEscapeProcessing(b);
    }

    public int executeUpdate(String s) throws SQLException {
        if (_log.isDebugEnabled()) _log.debug("LoggingStatement: executeUpdate("+s+")");
        return pDecorated.executeUpdate(s);
    }

    public void addBatch(String s) throws SQLException {
        pDecorated.addBatch(s);
    }

    public void setCursorName(String s) throws SQLException {
        pDecorated.setCursorName(s);
    }

    public boolean execute(String s) throws SQLException {
        if (_log.isDebugEnabled()) _log.debug("LoggingStatement: execute("+s+")");
        return pDecorated.execute(s);
    }

    public int executeUpdate(String s, int i) throws SQLException {
        if (_log.isDebugEnabled()) _log.debug("LoggingStatement: executeUpdate("+s+", "+i+")");
        return pDecorated.executeUpdate(s,i);
    }

    public boolean execute(String s, int i) throws SQLException {
        if (_log.isDebugEnabled()) _log.debug("LoggingStatement: execute("+s+", "+i+")");
        return pDecorated.execute(s,i);
    }

    public int executeUpdate(String s, int[] ints) throws SQLException {
        return pDecorated.executeUpdate(s,ints);
    }

    public boolean execute(String s, int[] ints) throws SQLException {
        return pDecorated.execute(s,ints);
    }

    public Connection getConnection() throws SQLException {
        return pConnection;
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        return pDecorated.getGeneratedKeys();
    }

    public ResultSet getResultSet() throws SQLException {
        return pDecorated.getResultSet();
    }

    public SQLWarning getWarnings() throws SQLException {
        return pDecorated.getWarnings();
    }

    public int executeUpdate(String s, String[] strings) throws SQLException {
        return pDecorated.executeUpdate(s,strings);
    }

    public boolean execute(String s, String[] strings) throws SQLException {
        return pDecorated.execute(s,strings);
    }

    public ResultSet executeQuery(String s) throws SQLException {
        if (_log.isDebugEnabled()) _log.debug("LoggingStatement: executeQuery("+s+")");
        return pDecorated.executeQuery(s);
    }

    public Statement getDecorated() {
        return pDecorated;
    }
    
    
    public boolean isPoolable() throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public void setPoolable(boolean poolable) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public boolean isClosed() throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
        
    public boolean isWrapperFor(java.lang.Class<?> iface) throws java.sql.SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public <T> T unwrap(java.lang.Class<T> iface) throws java.sql.SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }

    public void closeOnCompletion() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isCloseOnCompletion() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}//class
/*______________________________EOF_________________________________*/
