/*______________________________BOF_________________________________*/
package es.altia.util.persistance.test;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;

import es.altia.util.jdbc.*;
import es.altia.util.persistance.GlobalNames;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.persistance.daocommands.SQLDAOCommandFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author
 */
public class DAOCommandsTest extends TestCase {

   protected static Log m_Log =
            LogFactory.getLog(DAOCommandsTest.class.getName());
    /*_______Attributes_____________________________________________*/

    /*_______Operations_____________________________________________*/
    public DAOCommandsTest(String name) {
        super(name);
    }//Constructor for the test

    public static void main(String args[]) {
        junit.textui.TestRunner.run(DAOCommandsTest.suite());
    }//Main method for running this test

    protected void setUp() {
        DataSourceLocator.addDataSource(GlobalNames.CFGDATASOURCE,new SimpleDataSource());
    }//Method for initializing the tests

    public static Test suite() {
        TestSuite suite= new TestSuite();
        suite.addTest(
            new DAOCommandsTest("******** DAOCommandsTest **********")
            );
        return suite;
    }//Suite of tests


                    protected void runTest() {
                        SQLDAOCommandFactory cmdFactory= getFactoryTest(SQLDAOCommandFactory.DB_STANDARDSQL);
                    }//runTest


    public SQLDAOCommandFactory getFactoryTest(String dbKey) {
        m_Log.debug("####### getFactoryTest BEGINS HERE #########");
        SQLDAOCommandFactory result  = null;
        try {
            result = SQLDAOCommandFactory.getInstance(dbKey);
            assertTrue(result!=null);
        } catch(InternalErrorException e){
            m_Log.error("InternalErrorExcepton!");
            e.printStackTrace();
            assertTrue(false);
        }//try-catch
        m_Log.debug("####### getFactoryTest ENDS HERE   #########");
        return result;
    }//getFactoryTest

}//class
/*______________________________EOF_________________________________*/