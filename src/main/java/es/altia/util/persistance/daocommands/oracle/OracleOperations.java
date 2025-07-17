/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.oracle;

import es.altia.util.io.IOOperations;
import es.altia.util.jdbc.unwrapper.JdbcUnwrapperFactory;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.sql.BLOB;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class OracleOperations {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/
    private OracleOperations() {}

    public static final BLOB newOracleBlob(byte[] blob, OracleConnection conn) throws SQLException {
        BLOB result = null;
        try {
            result = BLOB.createTemporary(conn, false, BLOB.DURATION_SESSION);
            result.open(BLOB.MODE_READWRITE);
            OutputStream tempBlobOStream = result.getBinaryOutputStream();
            tempBlobOStream.write(blob);
            tempBlobOStream.flush();
            tempBlobOStream.close();
            result.close();
        } catch (IOException e) {
            throw new SQLException(e.getMessage());
        } catch (SQLException e) {
            throw e;
        }//try-catch
        return result;
    }//newOracleBlob

    public static final byte[] toByteArray(BLOB blob) throws SQLException {
        if (blob==null) return null;
        if (blob.length() > 0) {
            try {
                blob.open(BLOB.MODE_READONLY);
                InputStream blobIStream = blob.getBinaryStream();
                byte[] result = new byte[0];
                result = IOOperations.toByteArray(blobIStream);
                blobIStream.close();
                blob.close();
                return result;
            } catch (IOException e) {
                throw new SQLException(e.getMessage());
            }//try-catch
        } else {
            return new byte[0];
        }//if
    }//toByteArray

    public static final void freeOracleBlob(BLOB blob) throws SQLException {
        if (blob != null) {
                // If the BLOB is open, close it
                if (blob.isOpen()) blob.close();
                // Free the memory used by this BLOB
                blob.freeTemporary();
                blob = null;
        }//if
    }//freeOracleBlob

    public static final void freeOracleBlobSilently(BLOB blob) {
        try {
            freeOracleBlob(blob);
        } catch (SQLException e) {
            /* ignore */
        }//try-catch
    }//freeOracleBlob

    public static final OraclePreparedStatement toOraclePreparedStatement(PreparedStatement ps) {
        if (ps==null) return null;
        return (OraclePreparedStatement) JdbcUnwrapperFactory.getInstance().newUnwrapper().unwrap(ps);
    }//toOraclePreparedStatement

    public static OracleConnection toOracleConnection(Connection obj) {
        if (obj==null) return null;
        return (OracleConnection) JdbcUnwrapperFactory.getInstance().newUnwrapper().unwrap(obj);
    }//toOracleConnection

//    public static final OraclePreparedStatement toOraclePreparedStatement(PreparedStatement ps) {
//        if (ps==null) return null;
//        if (ps instanceof LoggingPreparedStatement) {
//            LoggingPreparedStatement lps = (LoggingPreparedStatement) ps;
//            return toOraclePreparedStatement(lps.getDecoratedPreparedStatement());
//        }//if
//        return (OraclePreparedStatement) ps;
//    }//toOraclePreparedStatement
//
//    public static OracleConnection toOracleConnection(Connection obj) {
//        if (obj==null) return null;
//        if (obj instanceof LoggingConnection) {
//            LoggingConnection lobj = (LoggingConnection) obj;
//            return toOracleConnection(lobj.getDecoratedConnection());
//        }//if
//        return (OracleConnection) obj;
//    }
//

}//class
/*______________________________EOF_________________________________*/
