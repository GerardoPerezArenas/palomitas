/*______________________________BOF_________________________________*/
package es.altia.util.jdbc.logdecorator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;

/**
 * @version $\Date$ $\Revision$
 */
public class LoggingPreparedStatement extends LoggingStatement implements PreparedStatement {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    StringBuffer pBoundParameters = new StringBuffer();
    private static final Log _log =
            LogFactory.getLog(LoggingPreparedStatement.class.getName());

    /*_______Operations_____________________________________________*/
    public LoggingPreparedStatement(Connection conn, PreparedStatement decorated) {
        super(conn, decorated);
    }

    public PreparedStatement getDecoratedPreparedStatement() {
        return (PreparedStatement) pDecorated;
    }

    public int executeUpdate() throws SQLException {

        if (_log.isDebugEnabled()) _log.debug("LoggingPreparedStatement: executeUpdate() "+pBoundParameters.toString());
        pBoundParameters = new StringBuffer();
        return getDecoratedPreparedStatement().executeUpdate();
    }

    public void addBatch() throws SQLException {
        if (_log.isDebugEnabled()) _log.debug("LoggingPreparedStatement: addBatch() "+pBoundParameters.toString());
        pBoundParameters = new StringBuffer();
        getDecoratedPreparedStatement().addBatch();
    }

    public void clearParameters() throws SQLException {
        getDecoratedPreparedStatement().clearParameters();
        pBoundParameters = new StringBuffer();
    }

    public boolean execute() throws SQLException {
        if (_log.isDebugEnabled()) _log.debug("LoggingPreparedStatement: execute() "+pBoundParameters.toString());
        pBoundParameters = new StringBuffer();
        return getDecoratedPreparedStatement().execute();
    }

    public void setByte(int i, byte b) throws SQLException {
        getDecoratedPreparedStatement().setByte(i,b);
        pBoundParameters.append("("+i+":bt;"+b+")");
    }

    public void setDouble(int i, double v) throws SQLException {
        getDecoratedPreparedStatement().setDouble(i,v);
        pBoundParameters.append("("+i+":db;"+v+")");
    }

    public void setFloat(int i, float v) throws SQLException {
        getDecoratedPreparedStatement().setFloat(i,v);
        pBoundParameters.append("("+i+":fl;"+v+")");
    }

    public void setInt(int i, int i1) throws SQLException {
        getDecoratedPreparedStatement().setInt(i,i1);
        pBoundParameters.append("("+i+":in;"+i1+")");
    }

    public void setNull(int i, int i1) throws SQLException {
        getDecoratedPreparedStatement().setNull(i,i1);
        pBoundParameters.append("("+i+":null;"+i1+")");
    }

    public void setLong(int i, long l) throws SQLException {
        getDecoratedPreparedStatement().setLong(i,l);
        pBoundParameters.append("("+i+":lg;"+l+")");
    }

    public void setShort(int i, short i1) throws SQLException {
        getDecoratedPreparedStatement().setShort(i,i1);
        pBoundParameters.append("("+i+":sh;"+i1+")");
    }

    public void setBoolean(int i, boolean b) throws SQLException {
        getDecoratedPreparedStatement().setBoolean(i,b);
        pBoundParameters.append("("+i+":bl;"+b+")");
    }

    public void setBytes(int i, byte[] bytes) throws SQLException {
        getDecoratedPreparedStatement().setBytes(i,bytes);
        if (bytes==null)
            pBoundParameters.append("("+i+":bytes;null)");
        else
            pBoundParameters.append("("+i+":bytes;not-shown;length="+bytes.length+")");
    }

    public void setAsciiStream(int i, InputStream inputStream, int i1) throws SQLException {
        getDecoratedPreparedStatement().setAsciiStream(i,inputStream,i1);
        pBoundParameters.append("("+i+":asciistream;not-shown)");
    }

    public void setBinaryStream(int i, InputStream inputStream, int i1) throws SQLException {
        getDecoratedPreparedStatement().setBinaryStream(i,inputStream,i1);
        pBoundParameters.append("("+i+":binstream;not-shown)");
    }

    public void setUnicodeStream(int i, InputStream inputStream, int i1) throws SQLException {
        throw new SQLException("LoggingPreparedStatement: setUnicodeStream() DEPRECATED. DO NOT USE!");
        //getDecoratedPreparedStatement().setUnicodeStream(i,inputStream,i1);
    }

    public void setCharacterStream(int i, Reader reader, int i1) throws SQLException {
        getDecoratedPreparedStatement().setCharacterStream(i,reader,i1);
        pBoundParameters.append("("+i+":charstream;not-shown)");
    }

    public void setObject(int i, Object o) throws SQLException {
        getDecoratedPreparedStatement().setObject(i,o);
        pBoundParameters.append("("+i+":object;not-shown)");
    }

    public void setObject(int i, Object o, int i1) throws SQLException {
        getDecoratedPreparedStatement().setObject(i,o,i1);
        pBoundParameters.append("("+i+":object;not-shown)");
    }

    public void setObject(int i, Object o, int i1, int i2) throws SQLException {
        getDecoratedPreparedStatement().setObject(i,o,i1,i2);
        pBoundParameters.append("("+i+":object;not-shown)");
    }

    public void setNull(int i, int i1, String s) throws SQLException {
        getDecoratedPreparedStatement().setNull(i,i1,s);
        pBoundParameters.append("("+i+":null;"+i1+", '"+s+"')");
    }

    public void setString(int i, String s) throws SQLException {
        getDecoratedPreparedStatement().setString(i,s);
        pBoundParameters.append("("+i+":st;'"+s+"')");
    }

    public void setBigDecimal(int i, BigDecimal bigDecimal) throws SQLException {
        getDecoratedPreparedStatement().setBigDecimal(i,bigDecimal);
        pBoundParameters.append("("+i+":big;not-shown)");
    }

    public void setURL(int i, URL url) throws SQLException {
        getDecoratedPreparedStatement().setURL(i,url);
        pBoundParameters.append("("+i+":url;not-shown)");
    }

    public void setArray(int i, Array array) throws SQLException {
        getDecoratedPreparedStatement().setArray(i,array);
        pBoundParameters.append("("+i+":array;not-shown)");
    }

    public void setBlob(int i, Blob blob) throws SQLException {
        getDecoratedPreparedStatement().setBlob(i,blob);
        pBoundParameters.append("("+i+":blob;not-shown)");
    }

    public void setClob(int i, Clob clob) throws SQLException {
        getDecoratedPreparedStatement().setClob(i,clob);
        pBoundParameters.append("("+i+":clob;not-shown)");
    }

    public void setDate(int i, Date date) throws SQLException {
        getDecoratedPreparedStatement().setDate(i,date);
        pBoundParameters.append("("+i+":dt;"+date+")");
    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        return getDecoratedPreparedStatement().getParameterMetaData();
    }

    public void setRef(int i, Ref ref) throws SQLException {
        getDecoratedPreparedStatement().setRef(i,ref);
        pBoundParameters.append("("+i+":rf;"+ref+")");
    }

    public ResultSet executeQuery() throws SQLException {
        if (_log.isDebugEnabled()) _log.debug("LoggingPreparedStatement: executeQuery() "+pBoundParameters.toString());
        pBoundParameters = new StringBuffer();
        return getDecoratedPreparedStatement().executeQuery();
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return getDecoratedPreparedStatement().getMetaData();
    }

    public void setTime(int i, Time time) throws SQLException {
        getDecoratedPreparedStatement().setTime(i,time);
        pBoundParameters.append("("+i+":tm;"+time+")");
    }

    public void setTimestamp(int i, Timestamp timestamp) throws SQLException {
        getDecoratedPreparedStatement().setTimestamp(i,timestamp);
        pBoundParameters.append("("+i+":ts;"+timestamp+")");
    }

    public void setDate(int i, Date date, Calendar calendar) throws SQLException {
        getDecoratedPreparedStatement().setDate(i,date,calendar);
        pBoundParameters.append("("+i+":dt;"+date+", "+calendar+")");
    }

    public void setTime(int i, Time time, Calendar calendar) throws SQLException {
        getDecoratedPreparedStatement().setTime(i,time,calendar);
        pBoundParameters.append("("+i+":tm;"+time+", "+calendar+")");
    }

    public void setTimestamp(int i, Timestamp timestamp, Calendar calendar) throws SQLException {
        getDecoratedPreparedStatement().setTimestamp(i, timestamp, calendar);
        pBoundParameters.append("("+i+":ts;"+timestamp+", "+calendar+")");
    }
    
    
    public void setNClob(int parameterIndex, NClob value) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }

    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
        
    public void setNClob(int parameterIndex, Reader reader) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }

    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public void setClob(int parameterIndex, Reader reader) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public void setCharacterStream(int parameterIndex, java.io.Reader reader) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public void setBinaryStream(int parameterIndex, java.io.InputStream x) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public void setAsciiStream(int parameterIndex, java.io.InputStream x) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    
    public void setCharacterStream(int parameterIndex,java.io.Reader reader,long length) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public void setBinaryStream(int parameterIndex, java.io.InputStream x,long length) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public void setAsciiStream(int parameterIndex, java.io.InputStream x, long length) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    
    public void setNString(int parameterIndex, String value) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public void setRowId(int parameterIndex, RowId x) throws SQLException{
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
