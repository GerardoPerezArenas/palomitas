/*______________________________BOF_________________________________*/
package es.altia.util.cache;

import es.altia.util.cache.lrumapimpl.LRUMapReadCacheImpl;
import es.altia.util.commons.DebugOperations;
import es.altia.util.configuration.ConfigurationParametersManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @version $\Date$ $\Revision$
 */
public class CacheFactory {
    /*_______Constants______________________________________________*/
    private static final String FACTORY_IMPL_PARAMETER = "CacheFactory/impl";

    /*_______Atributes______________________________________________*/
    private static CacheFactory SINGLETON = null;
    final static Log _log =
            LogFactory.getLog(CacheFactory.class.getName());

    /*_______Operations_____________________________________________*/
    protected CacheFactory() {
    }

    public static CacheFactory getInstance() {
        if (SINGLETON == null) initSingleton();
        return SINGLETON;
    }//getInstance

    private synchronized static void initSingleton() {
        if (_log.isInfoEnabled()) _log.info("CacheFactory: initSingleton() BEGIN");
        if (SINGLETON == null) {
            try {
                final String implClassName = ConfigurationParametersManager.getParameter(FACTORY_IMPL_PARAMETER);
                SINGLETON = (CacheFactory) Class.forName(implClassName).newInstance();
            } catch (Exception e) {
                if (_log.isErrorEnabled()) _log.error("CacheFactory: initSingleton() InternalErrorException .... " + DebugOperations.getDetailedMessage(e));
            }//try-catch
            if (SINGLETON == null) SINGLETON = new CacheFactory();
        }//if
        if (_log.isInfoEnabled()) _log.info("CacheFactory: initSingleton() END");
    }//initSingleton



    
    public CachedRetrievalMechanism newReadCache(RetrievalMechanism from) {
        return new LRUMapReadCacheImpl(from, -1);
    }//newReadCache

    public CachedRetrievalMechanism newReadCache(RetrievalMechanism from, int maxEntriesNumber) {
        return new LRUMapReadCacheImpl(from, maxEntriesNumber);
    }//newReadCache

}//class
/*______________________________EOF_________________________________*/