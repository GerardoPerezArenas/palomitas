package es.altia.common.service.cache;



/**
 * Cache manager
 *
 * @see Cache
 * @see CreatorInterface
 */
public class BaseCacheManager implements CacheManager
{
    // --------------------------------------------------------------
    // Members
    // --------------------------------------------------------------
    private CreatorInterface    creator = null;
    private Cache               cache = null;
    private boolean             isCacheEnable;


    // --------------------------------------------------------------
    // Constructor
    // --------------------------------------------------------------

    /**
     * Create a cache manager. By default, the cache is enable.
     *
     * @param  theCreator   object factory used when the requested object is not
     *                      on the cache
     */
    public BaseCacheManager(CreatorInterface theCreator)
    {
        creator = theCreator;
        isCacheEnable = true;
        cache = new BaseCache();
    }



    // --------------------------------------------------------------
    // Methods
    // --------------------------------------------------------------

    /**
     * If the object is cached, then it fetch it from the cache otherwise it
     * create a new one and add it in the cache memory.
     *
     * @param  theKey           the pk used to stored the object in the cache
     * @return                  the object requested.
     * @throws CreateException  if it can't create a new object
     */
    public Object fetch(Object theKey)
            throws CreateException
    {
        if (isCacheEnable) {

            Object value = cache.fetch(theKey);
            if (value == null) {

                synchronized (cache) {
                    value = cache.fetch(theKey);
                    value = creator.create(theKey);
                    cache.add(theKey, value);
                }
            }
            return value;

        } else {

            Object value = creator.create(theKey);
            return value;
        }
    }


    /**
     * Clear the cache
     */
    public void clear()
    {
        cache.clear();
    }


    /**
     * Enable or disable the cache
     */
    public void setCacheEnable(boolean isCacheEnable)
    {
        this.isCacheEnable = isCacheEnable;
    }


    /**
     * Return true if the cache is enable and false otherwise.
     */
    public boolean isCacheEnabled()
    {
        return isCacheEnable;
    }
}
