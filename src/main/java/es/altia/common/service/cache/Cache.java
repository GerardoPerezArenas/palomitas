package es.altia.common.service.cache;


/**
 * Cache used to store Object
 *
 * @see CacheManager
 */
public interface Cache
{
    /**
     * Fetch an object from the cache.
     *
     * @param  theKey   the pk used to stored the object in the cache
     * @return          the object requested. null if the object is not on the cache
     */
    public Object fetch(Object theKey);


    /**
     * Add a new object in the cache.
     *
     * @param  theKey       the pk used to store the object
     * @param  theValue     the object it-self
     */
    public void add(Object theKey, Object theValue);


    /**
     * Clear the cache
     */
    public void clear();
}
