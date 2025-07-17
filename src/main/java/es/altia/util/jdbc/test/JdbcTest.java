/*______________________________BOF_________________________________*/
package es.altia.util.jdbc.test;

import es.altia.util.commons.DebugOperations;
import es.altia.util.jdbc.DataSourceLocator;
import es.altia.util.jdbc.SimpleDataSource;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;

/**
 * @version $\Date$ $\Revision$
 */
public class JdbcTest extends TestCase {
    /*_______Attributes_____________________________________________*/
    private static final String SHORT_CLASS_NAME = DebugOperations.getShortNameForClass(JdbcTest.class);
    protected static Log _log = LogFactory.getLog(JdbcTest.class.getName());


    /*_______Operations_____________________________________________*/
    public JdbcTest(String name) {
        super(name);
    }//Constructor for the test

    public static void main(String args[]) {
        junit.textui.TestRunner.run(JdbcTest.suite());
    }//Main method for running this test

    protected void setUp() {
        if (_log.isDebugEnabled()) _log.debug(SHORT_CLASS_NAME + ".setUp()");
    }//Method for initializing the tests

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new JdbcTest("** " + SHORT_CLASS_NAME + " **\n"));
        return suite;
    }//Suite of tests


    protected void runTest() {
        if (_log.isDebugEnabled()) _log.debug(SHORT_CLASS_NAME + ".runTest() BEGIN\n");
        testDatabaseName();
        if (_log.isDebugEnabled()) _log.debug(SHORT_CLASS_NAME + ".runTest() END\n");
    }//runTest

    public void testDatabaseName() {
        if (_log.isDebugEnabled()) _log.debug(SHORT_CLASS_NAME + ".testDatabaseName() BEGIN");
        try {
            Connection conn = new SimpleDataSource().getConnection();
            if (_log.isInfoEnabled()) _log.info(SHORT_CLASS_NAME + ".testDatabaseName() DBNAME="+conn.getMetaData().getDatabaseProductName()+" "+conn.getMetaData().getDatabaseProductVersion());
        } catch (Exception e) {
            _log.fatal(DebugOperations.getDetailedMessage(e));
            fail(SHORT_CLASS_NAME + ".testDatabaseName() Exception:" + e);
        }//try-catch
        if (_log.isDebugEnabled()) _log.debug(SHORT_CLASS_NAME + ".testDatabaseName() END");
    }//testDatabaseName

}//class

/*______________________________EOF_________________________________*/