/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.oracle.test;

import es.altia.util.commons.DebugOperations;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.io.IOOperations;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.jdbc.SimpleDataSource;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.daocommands.SQLDAOBlobCommandAdapter;
import es.altia.util.persistance.daocommands.SQLDAOBlobRetrieveCommand;
import es.altia.util.persistance.daocommands.SQLDAOBlobStoreCommand;
import es.altia.util.persistance.daocommands.SQLDAOCommandFactory;
import es.altia.util.persistance.impl.SingleLongPrimaryKey;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class OracleTest extends TestCase implements SQLDAOBlobCommandAdapter {
    /*_______Attributes_____________________________________________*/
    private static final String SHORT_CLASS_NAME = DebugOperations.getShortNameForClass(OracleTest.class);
    protected static Log _log = LogFactory.getLog(OracleTest.class.getName());


    /*_______Operations_____________________________________________*/
    public OracleTest(String name) {
        super(name);
    }//Constructor for the test

    public static void main(String args[]) {
        junit.textui.TestRunner.run(OracleTest.suite());
    }//Main method for running this test

    protected void setUp() {
        if (_log.isDebugEnabled()) _log.debug(SHORT_CLASS_NAME + ".setUp()");
    }//Method for initializing the tests

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new OracleTest("** " + SHORT_CLASS_NAME + " **\n"));
        return suite;
    }//Suite of tests


    protected void runTest() {
        if (_log.isDebugEnabled()) _log.debug(SHORT_CLASS_NAME + ".runTest() BEGIN\n");
        testOracleBlob();
        if (_log.isDebugEnabled()) _log.debug(SHORT_CLASS_NAME + ".runTest() END\n");
    }//runTest

    public void testOracleBlob() {
        if (_log.isDebugEnabled()) _log.debug(SHORT_CLASS_NAME + ".testOracleBlob() BEGIN");
        InputStream fis = null;
        Connection conn = null;
        try {
            DataSource dataSource = new SimpleDataSource();
            conn = dataSource.getConnection();
            SQLDAOCommandFactory daoCommandFactory = SQLDAOCommandFactory.getInstance(SQLDAOCommandFactory.DB_ORACLE);
            PrimaryKey pk = new SingleLongPrimaryKey(1);

            SQLDAOBlobStoreCommand storeCmd = daoCommandFactory.newBlobStoreCmd(this);
            fis = new FileInputStream("/tmp/prueba10.pdf");
            byte[] contents = IOOperations.toByteArray(fis);
            storeCmd.store(conn,pk,contents);

            SQLDAOBlobRetrieveCommand retrieveCmd = daoCommandFactory.newBlobRetrieveCmd(this);
            byte[] newContents = retrieveCmd.retrieve(conn,pk);

            assertEquals(contents.length,newContents.length);
        } catch (InternalErrorException e) {
            _log.fatal(DebugOperations.getDetailedMessage(e));
            fail(SHORT_CLASS_NAME + ".testOracleBlob() Exception:" + e);
        } catch (FileNotFoundException e) {
            _log.fatal(DebugOperations.getDetailedMessage(e));
            fail(SHORT_CLASS_NAME + ".testOracleBlob() Exception:" + e);
        } catch (IOException e) {
            _log.fatal(DebugOperations.getDetailedMessage(e));
            fail(SHORT_CLASS_NAME + ".testOracleBlob() Exception:" + e);
        } catch (SQLException e) {
            _log.fatal(DebugOperations.getDetailedMessage(e));
            fail(SHORT_CLASS_NAME + ".testOracleBlob() Exception:" + e);
        } catch (InstanceNotFoundException e) {
            _log.fatal(DebugOperations.getDetailedMessage(e));
            fail(SHORT_CLASS_NAME + ".testOracleBlob() Exception:" + e);
        } finally {
            IOOperations.closeInputStreamSilently(fis);
            JdbcOperations.closeConnectionSilently(conn);
        }//try-catch
        if (_log.isDebugEnabled()) _log.debug(SHORT_CLASS_NAME + ".testOracleBlob() END");
    }//testOracleBlob

//    public void testXXX() {
//        _log.debug(SHORT_CLASS_NAME + ".testXXX() BEGIN");
//        try {
//
//        } catch (InternalErrorException e) {
//            _log.fatal(DebugOperations.getDetailedMessage(e));
//            fail(SHORT_CLASS_NAME + ".testXXX() Exception:" + e);
//        }//try-catch
//        _log.debug(SHORT_CLASS_NAME + ".testXXX() END");
//    }//testXXX

    public String blGetSQLBlobAttributeName() {
        return "contents";
    }

    /**
     * SQL main table name for the concrete VO
     */
    public String pkGetSQLMainTableName() {
        return "tBlobTest";
    }

    /**
     * PrimaryKey SQL attribute names (ordered)
     */
    public String[] pkGetSQLPKAttributeNames() {
        String[] result = new String[1];
        result[0] = "pk";
        return result;
    }

    /**
     * Binds the PreparedStatement with PrimaryKey elements
     *
     * @param primaryKey concrete impl you must use for binding
     *                   preparedStatement	the JDBC statement to be bound
     *                   i			actual PreparedStatement positional field counter
     * @return positional counter incremented properly
     */
    public int pkBind(PrimaryKey primaryKey, PreparedStatement preparedStatement, int i) throws SQLException {
        int result = i;
        SingleLongPrimaryKey pk = (SingleLongPrimaryKey) primaryKey;
        preparedStatement.setLong(result++,pk.getId());
        return result;
    }
}//class

/*______________________________EOF_________________________________*/