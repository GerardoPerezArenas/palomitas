package es.altia.common.service.cache;


import es.altia.common.service.ServiceManager;

/**
 * Helper class to easily get a <code>CacheManager</code> object. Normally you
 * would call <code>((CacheService)ServiceManager.getInstance().
 * getService("cache")).getCache(String)</code>. Using this helper class
 * you simply need to call <code>CacheServiceHelper.getCache(String)</code>.
 *
 * @version 1.0
 */
public class CacheServiceHelper
{
    /**
     * @param theLogicalName    the logical's name of the directory.
     * @return                  the <code>CacheManager</code> instance associated
     *                          with the specified logical name
     */
    public static CacheManager getCache(String theLogicalName)
    {
        return ((CacheService)ServiceManager.getInstance().getService("cache")).getCache(theLogicalName);
    }

}
