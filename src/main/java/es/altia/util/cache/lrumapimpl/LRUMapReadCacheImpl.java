/*______________________________BOF_________________________________*/
package es.altia.util.cache.lrumapimpl;

import es.altia.util.cache.CachedRetrievalMechanism;
import es.altia.util.cache.KeyObject;
import es.altia.util.cache.RetrievalMechanism;
import es.altia.util.exceptions.InternalErrorException;
import org.apache.commons.collections.LRUMap;

import java.util.Map;

/**
 * @version $\Date$ $\Revision$
 */
public class LRUMapReadCacheImpl implements CachedRetrievalMechanism {
    /*_______Constants______________________________________________*/
    public static final int LRU = 1;
    private static final int MAX_ENTRIES_UPPER_THRESHOLD = 20;

    /*_______Atributes______________________________________________*/
    //private static Log _log = LogFactory.getLog(SimpleHashMapReadCacheImpl.class);
    private RetrievalMechanism pOrigin=null;
    private Map cm = null;
    private int pMaxEntries = -1;

    /*_______Operations_____________________________________________*/
    public LRUMapReadCacheImpl() {
    }//constructor

    public LRUMapReadCacheImpl(RetrievalMechanism from, int maxEntries) {
        init(from,maxEntries);
    }//constructor

    public void init(RetrievalMechanism from, int maxEntries) {
        pOrigin = from;
        if (maxEntries<0)
            pMaxEntries = Integer.MAX_VALUE;
        else
            pMaxEntries = maxEntries;
        initCache( (pMaxEntries<=MAX_ENTRIES_UPPER_THRESHOLD)?(pMaxEntries):(MAX_ENTRIES_UPPER_THRESHOLD) );
    }//init

    private synchronized void initCache(int maxEntries) {
        if (cm==null) {
            cm = new LRUMap(maxEntries+1);
        }//if
    }//initCache

    public boolean isCached(KeyObject key) {
        return (cm.get(key) != null);
    }//isCached

    public void flush() {
        cm.clear();
    }//flush

    public void reset() {
        cm.clear();
        cm = null;
        initCache( (pMaxEntries<=MAX_ENTRIES_UPPER_THRESHOLD)?(pMaxEntries):(MAX_ENTRIES_UPPER_THRESHOLD) );
    }//reset

    public void destroy() {
        cm.clear();
        cm = null;
    }//destroy

    public Object retrieve(KeyObject key)
        throws InternalErrorException {
        final Object result;
        final Object entry = cm.get(key);
        if (entry!=null) {
            /* Object found in cache */
            result = entry;
        } else {
            /* Object not cached... retrieve from its origin */
            final Object obj = pOrigin.retrieve(key);
            cm.put(key,obj);
            result = obj;
        }//if
        return result;
    }//retrieve

}//class
/*______________________________EOF_________________________________*/
