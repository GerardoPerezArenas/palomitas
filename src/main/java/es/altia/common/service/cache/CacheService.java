package es.altia.common.service.cache;

import es.altia.common.service.*;
import es.altia.common.service.config.*;
import es.altia.common.exception.CriticalException;

import java.util.Hashtable;


/**
 * A Service that returns instances of <code>CacheManager</code> classes. An instance
 * of <code>CacheManager</code> is hashtable with key and object cached value
 *
 * @version 1.0
 */
public class CacheService extends Service
{
    // --------------------------------------------------------------
    // Members
    // --------------------------------------------------------------

    // Constants
    private final static String CACHESERVICE_PREFIXKEY = "CacheService.";
    private final static String CREATOR_SUFFIXKEY = ".creator";

    // Class variables
    private static Config messages = ConfigServiceHelper.getConfig("es.altia.common.service.cache.messages");

    // Instance variables
    private Hashtable caches;
    private String configPropertiesName;


    // --------------------------------------------------------------
    // Constructor
    // --------------------------------------------------------------

    /**
     * @param theServiceName the service's name
     * @param thePropertyFileName the common configuration file name
     */
    public CacheService(String theServiceName, String thePropertyFileName)
    {
        super(theServiceName, thePropertyFileName);
        caches = new Hashtable();
        configPropertiesName = thePropertyFileName;
    }


    // --------------------------------------------------------------
    // Methods
    // --------------------------------------------------------------

    /**
     * @param theLogicalName    the logical name found in the common.properties
     *                          file.
     * @return                  the CacheManager instance associated with the
     *                          specified logical name
     */
    public CacheManager getCache(String theLogicalName)
    {
        CacheManager cache = (CacheManager)caches.get(theLogicalName);
        if (cache == null) {

            String className = getServiceProperty(CACHESERVICE_PREFIXKEY + theLogicalName + CREATOR_SUFFIXKEY);
            Class creatorClass = null;
            try {
                creatorClass = Class.forName(className);
            } catch (ClassNotFoundException ex) {
                throw new CriticalException(this.getClass().getName(),
                                            messages.getMessage("Error.CacheService.CreatorClassNotFound", new Object[]{className}),
                                            ex);
            }

            synchronized (creatorClass) {
                cache = (CacheManager)caches.get(theLogicalName);
                if (cache == null) {

                    try {
                        CreatorInterface creator = (CreatorInterface)creatorClass.newInstance();
                        cache = new BaseCacheManager(creator);
                        caches.put(theLogicalName, cache);
                    } catch (ClassCastException ex) {
                        throw new CriticalException(this.getClass().getName(),
                                                    messages.getMessage("Error.CacheService.NotACreatorInterface", new Object[]{className}),
                                                    ex);
                    } catch (IllegalAccessException ex) {
                        throw new CriticalException(this.getClass().getName(),
                                                    messages.getMessage("Error.CacheService.IllegalAccessException", new Object[]{className}),
                                                    ex);
                    } catch (InstantiationException ex) {
                        throw new CriticalException(this.getClass().getName(),
                                                    messages.getMessage("Error.CacheService.InstantiationException", new Object[]{className}),
                                                    ex);
                    }

                }
            }
        }

        return cache;
    }


    /**
     * Called by the <code>ServiceManager</code> during Service initialization
     */
    public void init()
    {
        setInitialized(true);
    }


    /**
     * Called by the <code>ServiceManager</code> during Service shutdown
     */
    public void shutdown()
    {
    }

    /**
     * Helper method to get a string property from the Service configuration
     * file.
     */
    private String getServiceProperty(String thePropertyName)
    {
        return ConfigServiceHelper.getConfig(configPropertiesName).getString(thePropertyName);
    }
}
