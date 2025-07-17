/*______________________________BOF_________________________________*/
package es.altia.util.persistance.facades.test;

import junit.framework.*;

import java.util.Iterator;
import java.util.Collection;
import java.util.Calendar;
import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import es.altia.util.exceptions.*;
import es.altia.util.persistance.exceptions.*;
import es.altia.util.jdbc.*;

import es.altia.util.facades.*;
import es.altia.util.jdbc.DataSourceLocator;
import es.altia.util.jdbc.SimpleDataSource;




/**
  * @author
  * @version      %I%, %G%
  *
  * This is a UNIT TEST CLASS. It uses Junit Framework.
  *
  * This class tests:
  *		- BusinessRequestManager
  *		- BusinessRequest
  *		- BusinessRequestProcessingFilter
  *		- TransactionalRequest
  *		- TransactionalBRProcessingFilter
  **/
public class TxYNonTxFacadeTest extends TestCase {

	/*_______Attributes_____________________________________________*/

	/*_______Operations_____________________________________________*/
	public TxYNonTxFacadeTest(String name) {
		super(name);
	}//Constructor for the test

	public static void main(String args[]) {
		junit.textui.TestRunner.run(TxYNonTxFacadeTest.suite());
	}//Main method for running this test

	protected void setUp() {
		DataSourceLocator.addDataSource("PLIS_MAIN_DATASOURCE",new SimpleDataSource());
		assertTrue(true);
	}//Method for initializing the tests

	public static Test suite() {
    	TestSuite suite= new TestSuite();
	    suite.addTest(new TxYNonTxFacadeTest("******** TxYNonTxFacadeTest **********"));
    	return suite;
	}//Suite of tests


	            	protected void runTest() {
						txRequestTest();
						nonTxRequestTest();
	            	}//runTest

	private void txRequestTest() {
		BusinessRequest br = new DummyTxRequest();		
		try {
			BusinessRequestManager.getInstance().handleRequest(br);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}//try-catch
	}//txRequestTest


	private void nonTxRequestTest() {
		BusinessRequest br = new DummyNonTxRequest();		
		try {
			BusinessRequestManager.getInstance().handleRequest(br);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}//try-catch
	}//nonTxRequestTest



}//End of the concrete test class
/*______________________________EOF_________________________________*/
