/*______________________________BOF_________________________________*/
package es.altia.agora.business.portafirmas.delegacionfirma.test;

import es.altia.agora.business.portafirmas.delegacionfirma.vo.DelegacionFirmaPK;
import es.altia.agora.business.portafirmas.delegacionfirma.vo.DelegacionFirmaVO;
import es.altia.util.persistance.GlobalNames;
import es.altia.util.persistance.test.NewDAOTest;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Calendar;


/**
  * @version      $\Revision$ $\Date$
  *
  * This is a UNIT TEST CLASS. It uses Junit Framework.
  *
  **/
public class DelegacionFirmaTest extends NewDAOTest {

	/*_______Attributes_____________________________________________*/
	DelegacionFirmaVO vo00=null;
	DelegacionFirmaVO vo01=null;
	DelegacionFirmaVO vo02=null;
	DelegacionFirmaVO vo03=null;
	DelegacionFirmaVO vo04=null;
	DelegacionFirmaPK pk00=null;
	DelegacionFirmaPK pk01=null;
	DelegacionFirmaPK pk02=null;
    Calendar nextYear = Calendar.getInstance();

    protected static Log _log =
            LogFactory.getLog(DelegacionFirmaTest.class.getName());

    /*_______Operations_____________________________________________*/
	protected void doSetUp() {
        nextYear.add(Calendar.YEAR,1);

		pk00=new DelegacionFirmaPK(7);
		vo00=new DelegacionFirmaVO(pk00, 8, Calendar.getInstance(), nextYear);
		vo01=new DelegacionFirmaVO(pk00, 5, nextYear, Calendar.getInstance());

		assertTrue(pk00!=null);
		assertTrue(vo00!=null);
		assertTrue(vo01!=null);

		pk02=new DelegacionFirmaPK(8);
		vo02=new DelegacionFirmaVO(pk02, 5, nextYear, Calendar.getInstance());
		
		assertTrue( vo00.getPrimaryKey().equals(pk00) );		
		assertTrue( vo01.getPrimaryKey().equals(pk00) );
		assertTrue( ! vo02.getPrimaryKey().equals(pk00) );
	}//Method for initializing the tests

       	protected void runTest() {
		//_log.info("***********************************************************");
		_log.debug("*** BASE TESTS                                          ***");
		//_log.info("***********************************************************");
		dao_find_Test(pk00, false); 		// READ   -> Not Found
		dao_update_Test(vo01, false, false);	// UPDATE -> Not Found
		dao_delete_Test(pk00, false);		// DELETE -> Not Found
		long count = dao_countAll_Test();	// COUNT  -> x
		vo00 = (DelegacionFirmaVO) dao_create_Test(vo00);	// CREATE -> Created
		pk00 = (DelegacionFirmaPK) vo00.getPrimaryKey();
		vo01=new DelegacionFirmaVO(pk00, 5, nextYear, Calendar.getInstance());
		dao_find_Test(pk00, true); 		// READ   -> Found
		dao_update_Test(vo01, true, false);	// UPDATE -> Updated
		dao_find_Test(pk00, true); 		// READ   -> Found
		long count2 = dao_countAll_Test();	// COUNT  -> x+1
		assertTrue( ((count+1)==count2) );
		dao_delete_Test(pk00, true);		// DELETE -> Deleted
		dao_find_Test(pk00, false); 		// READ   -> Not Found
		long count3 = dao_countAll_Test();	// COUNT  -> x
		assertTrue( ((count)==count3) );
		//_log.info("***********************************************************");

		//_log.info("*");
		
		//_log.info("***********************************************************");
		_log.debug("*** STALE TEST                                          ***");
		//_log.info("***********************************************************");
		vo00 = (DelegacionFirmaVO) dao_create_Test(vo00);	// CREATE -> Created
		pk00 = (DelegacionFirmaPK) vo00.getPrimaryKey();
		vo03 = (DelegacionFirmaVO) dao_find_Test(pk00, true); 	// READ   -> Found
		vo04 = (DelegacionFirmaVO) dao_find_Test(pk00, true); 	// READ   -> Found
//		vo03.setName("Pepe03"); //!!!!! Check this !!!!!!!!!!
//		vo04.setName("Pepe04");
		_log.debug(vo03);
		_log.debug(vo04);
		dao_update_Test(vo04, true, false);		// UPDATE -> Updated
		dao_update_Test(vo03, true, true);		// UPDATE -> Stale		
		//_log.info("***********************************************************");
       	}//runTest



	/*---------------------------------------------*/
	/*---- Do not touch below if not necessary ----*/
	/*---------------------------------------------*/

	public DelegacionFirmaTest(String name) {
		super(name);
	}//Constructor for the test

	public static void main(String args[]) {
		junit.textui.TestRunner.run(DelegacionFirmaTest.suite());
	}//Main method for running this test

	public static Test suite() {
		TestSuite suite= new TestSuite();
		suite.addTest(new DelegacionFirmaTest("******************* DelegacionFirmaTest ***********************"));
		return suite;
	}//Suite of tests

    protected String getDSKey(){
        return GlobalNames.CFGDATASOURCE;
    }//getDSKey

    protected String getDBKey() {
        return GlobalNames.CFG_DB_SERVER;
    }

    protected Class getVOClass(){
    		return DelegacionFirmaVO.class;
    	}//getVOClass

}//End of the concrete test class
/*______________________________EOF_________________________________*/

