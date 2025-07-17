package es.altia.common.service.cache;

import java.lang.ref.SoftReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;


import java.util.Hashtable;



/**
 * Cache used to store Object
 *
 * @see CacheManager
 */
public class BaseCache implements Cache
{
    // --------------------------------------------------------------
    // Members
    // --------------------------------------------------------------
    private Hashtable cacheMemory;
    private ReferenceQueue refQueue;



    // --------------------------------------------------------------
    // Constructor
    // --------------------------------------------------------------
    public BaseCache()
    {
        cacheMemory = new Hashtable();
        refQueue = new ReferenceQueue();
    }



    // --------------------------------------------------------------
    // Methods
    // --------------------------------------------------------------

    /**
     * Fetch an object from the cache.
     *
     * @param  theKey   the pk used to stored the object in the cache
     * @return          the object requested. null if the object is not on the cache
     */
    public Object fetch(Object theKey)
    {
        processQueue();
        Object objRef = cacheMemory.get(theKey);
        if (objRef != null) {
            MySoftReference softRef = (MySoftReference)objRef;
            return softRef.get();
        }
        return null;
    }


    /**
     * Add a new object in the cache.
     *
     * @param  theKey       the pk used to store the object
     * @param  theValue     the object it-self
     */
    public void add(Object theKey, Object theValue)
    {
        processQueue();
        cacheMemory.put(theKey , new MySoftReference(theKey, theValue, refQueue));
    }


    /**
     * Clear the cache
     */
    public void clear()
    {
        processQueue();
        cacheMemory.clear();
    }


    /**
     * Remove from the cache every unreferenced object
     */
    private void processQueue()
    {
        Reference objRef = null;
        while ((objRef = refQueue.poll()) != null)
        {
            MySoftReference softRef = (MySoftReference)objRef;
            cacheMemory.remove(softRef.getKey());
        }
    }




    // --------------------------------------------------------------
    // Inner class
    // --------------------------------------------------------------
    class MySoftReference extends SoftReference
    {
        private Object key = null;

        public MySoftReference(Object theKey, Object theValue, ReferenceQueue theRefQueue)
        {
            super(theValue, theRefQueue);
            key = theKey;
        }

        public Object getKey()
        {
            return key;
        }
    }
}
