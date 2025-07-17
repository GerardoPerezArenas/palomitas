package es.altia.common.service.cache;



/**
 * Cache manager
 *
 * @see Cache
 * @see CreatorInterface
 */
public interface CacheManager
{
    /**
     * If the object is cached, then it fetch it from the cache otherwise it
     * create a new one and add it in the cache memory.
     *
     * @param  theKey           the pk used to stored the object in the cache
     * @return                  the object requested.
     * @throws CreateException  if it can't create a new object
     */
    public Object fetch(Object theKey)
            throws CreateException;


    /**
     * Clear the cache
     */
     public void clear();


    /**
     * Enable or disable the cache
     */
    public void setCacheEnable(boolean isCacheEnable);


    /**
     * Return true if the cache is enable and false otherwise.
     */
    public boolean isCacheEnabled();
}
