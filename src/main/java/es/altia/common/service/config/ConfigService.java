package es.altia.common.service.config;

import java.util.*;

import es.altia.common.service.*;

/**
 * A Service that returns instances of <code>Config</code> classes. An instance
 * of <code>Config</code> is a unique (properties file name, locale) couple. The
 * <code>ConfigService</code> class caches <code>Config</code> objects for performance
 * and provides a <code>refreshAll()</code> method to refresh all <code>Config</code>
 * instances.
 *
 * @version 1.0
 */
public class ConfigService extends Service implements Reconfigurable {

    /**
     * Cache of <code>Config</code> objects.
     */
    private Hashtable configs = new Hashtable();

    /**
     * @param theServiceName the service's name
     * @param thePropertyFileName the common configuration file name
     */
    public ConfigService(String theServiceName, String thePropertyFileName)
    {
        super(theServiceName, thePropertyFileName);
    }

    /**
     * @param theConfigName the name of the properties file (without trailing ".properties").
     *                      The name can contain "." or "/" in it. Example :
     *                      "es.altia.common.service.config.myproperties" or
     *                      "com/irf/common/service/config/myproperties"
     *
     * @return the <code>Config</code> object that corresponds to the specified
     *         properties file for the default language
     */
    public Config getConfig(String theConfigName)
    {
        return getConfig(theConfigName, Locale.getDefault());
    }

    /**
     * @param theConfigName the name of the properties file (without trailing ".properties").
     *                      The name can contain "." or "/" in it. Example :
     *                      "es.altia.common.service.config.myproperties" or
     *                      "com/irf/common/service/config/myproperties"
     * @param theLocale     the locale to use when looking up the properties file
     *
     * @return the <code>Config</code> object that corresponds to the specified
     *         properties file for the specified locale
     */
    public Config getConfig(String theConfigName, Locale theLocale)
    {
	ConfigKey key = new ConfigKey(theConfigName, theLocale);

	if (!configs.containsKey(key)) {

            synchronized(ConfigService.class) {

                if (!configs.containsKey(key)) {

                    configs.put(key, new BaseConfig(theConfigName, theLocale));

                }
            }
        }

	return (Config)configs.get(key);
    }

    /**
     * Refresh (i.e. reload) all <code>Config</code> instances.
     */
    public synchronized void reconfigure() {

        Enumeration enum1 = configs.elements();
        while (enum1.hasMoreElements()) {
            Config config = (Config)enum1.nextElement();
            if (config instanceof BaseConfig) {
                ((BaseConfig)config).reconfigure();
            }
        }

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

}
