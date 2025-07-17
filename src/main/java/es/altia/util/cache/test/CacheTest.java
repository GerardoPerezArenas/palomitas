/*______________________________BOF_________________________________*/
package es.altia.util.cache.test;

import es.altia.util.commons.DebugOperations;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.cache.CachedRetrievalMechanism;
import es.altia.util.cache.CacheFactory;
import es.altia.util.cache.RetrievalMechanism;
import es.altia.util.cache.KeyObject;
import es.altia.util.persistance.impl.StringPrimaryKey;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @version $\Date$ $\Revision$
 */
public class CacheTest extends TestCase {
    /*_______Attributes_____________________________________________*/
    private static final String SHORT_CLASS_NAME = DebugOperations.getShortNameForClass(CacheTest.class);
    protected static Log _log = LogFactory.getLog(CacheTest.class.getName());
    protected CachedRetrievalMechanism cachedrm;
    protected KeyObject key1,key2,key3,key4,key5,key6;


    /*_______Operations_____________________________________________*/
    public CacheTest(String name) {
        super(name);
    }//Constructor for the test

    public static void main(String args[]) {
        junit.textui.TestRunner.run(CacheTest.suite());
    }//Main method for running this test

    protected void setUp() {
        if (_log.isDebugEnabled()) _log.debug(SHORT_CLASS_NAME + ".setUp() BEGIN");
        key1 = new StringPrimaryKey("1");
        key2 = new StringPrimaryKey("2");
        key3 = new StringPrimaryKey("3");
        key4 = new StringPrimaryKey("4");
        key5 = new StringPrimaryKey("5");
        key6 = new StringPrimaryKey("6");
        if (_log.isDebugEnabled()) _log.debug(SHORT_CLASS_NAME + ".setUp() END");
    }//Method for initializing the tests

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new CacheTest("** " + SHORT_CLASS_NAME + " **\n"));
        return suite;
    }//Suite of tests


    protected void runTest() {
        if (_log.isDebugEnabled()) _log.debug(SHORT_CLASS_NAME + ".runTest() BEGIN\n");
        testXXX();
        if (_log.isDebugEnabled()) _log.debug(SHORT_CLASS_NAME + ".runTest() END\n");
    }//runTest

    public void testXXX() {
        if (_log.isDebugEnabled()) _log.debug(SHORT_CLASS_NAME + ".testXXX() BEGIN");
        try {
            cachedrm = CacheFactory.getInstance().newReadCache(new DummyRetrievalMechanism(),5);
            /* Test empty cache */
            assertTrue(!cachedrm.isCached(key1));
            cachedrm.flush();
            assertTrue(!cachedrm.isCached(key1));
            cachedrm.reset();
            assertTrue(!cachedrm.isCached(key1));

            /* Test cache fill */
            assertTrue(cachedrm.retrieve(key1)==key1);
            assertTrue(cachedrm.isCached(key1));
            assertTrue(cachedrm.retrieve(key2)==key2);
            assertTrue(cachedrm.isCached(key2));
            assertTrue(cachedrm.retrieve(key3)==key3);
            assertTrue(cachedrm.isCached(key3));
            assertTrue(cachedrm.retrieve(key4)==key4);
            assertTrue(cachedrm.isCached(key4));
            assertTrue(cachedrm.retrieve(key5)==key5);
            assertTrue(cachedrm.isCached(key5));
            assertTrue(cachedrm.retrieve(key6)==key6);

            /* Test cache flush */
            cachedrm.flush();
            assertTrue(!cachedrm.isCached(key1));
            assertTrue(cachedrm.retrieve(key1)==key1);
            assertTrue(cachedrm.isCached(key1));

            /* Test cache reset */
            cachedrm.reset();
            assertTrue(!cachedrm.isCached(key1));

            /* Test cache replacement */
            assertTrue(cachedrm.retrieve(key1)==key1);
            assertTrue(cachedrm.isCached(key1));
            assertTrue(cachedrm.retrieve(key2)==key2);
            assertTrue(cachedrm.isCached(key2));
            assertTrue(cachedrm.retrieve(key3)==key3);
            assertTrue(cachedrm.isCached(key3));
            assertTrue(cachedrm.retrieve(key4)==key4);
            assertTrue(cachedrm.isCached(key4));
            assertTrue(cachedrm.retrieve(key5)==key5);
            assertTrue(cachedrm.isCached(key5));
            assertTrue(cachedrm.retrieve(key6)==key6);
            assertTrue(cachedrm.isCached(key6));
            assertTrue(! ( cachedrm.isCached(key1) &&
                           cachedrm.isCached(key2) &&
                           cachedrm.isCached(key3) &&
                           cachedrm.isCached(key4) &&
                           cachedrm.isCached(key5) ));

            cachedrm.destroy();
        } catch (InternalErrorException e) {
            _log.fatal(DebugOperations.getDetailedMessage(e));
            fail(SHORT_CLASS_NAME + ".testXXX() Exception:" + e);
        }//try-catch
        if (_log.isDebugEnabled()) _log.debug(SHORT_CLASS_NAME + ".testXXX() END");
    }//testXXX

    private class DummyRetrievalMechanism implements RetrievalMechanism {
        /*_______Operations_____________________________________________*/
        public Object retrieve(KeyObject key) throws InternalErrorException {
            final Object result = key;
            if (_log.isDebugEnabled()) _log.debug("DummyRetrievalMechanism.retrieve("+key+")");
            return result;
        }//retrieve
    }//inner class

}//class
/*______________________________EOF_________________________________*/