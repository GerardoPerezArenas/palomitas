package es.altia.common.service.config;

import java.util.*;

/**
 * Key to store <code>Config</code> objects in the <code>ConfigManager</code>
 * cache. A key is a unique (properties file name, locale) couple.
 *
 * @version 1.0
 */
public class ConfigKey {

    /**
     * The locale.
     */
    private Locale locale;

    /**
     * Properties file name.
     */
    private String configName;

    /**
     * @param theLocale     the locale
     * @param theConfigName the name of the properties file (without trailing ".properties").
     *                      The name can contain "." or "/" in it. Example :
     *                      "es.altia.common.service.config.myproperties" or
     *                      "com/irf/common/service/config/myproperties"
     */
    public ConfigKey(String theConfigName, Locale theLocale)
    {
        configName = theConfigName;
        locale = theLocale;
    }

    /**
     * Compare two <code>ConfigKey</code> objects.
     *
     * @param  theObject the object to compare
     * @return true when the 2 instances correspond to the same <code>Config</code>
     *         object
     */
    public boolean equals(Object theObject)
    {
        boolean result = false;

        if ((theObject != null) && (theObject.getClass().equals(this.getClass()))) {

            ConfigKey key = (ConfigKey) theObject;

            result = key.configName.equals(this.configName);
            result = result & (key.locale.equals(this.locale));

        }

        return result;
    }

    /**
     * @return the hashcode for a config key
     */
    public int hashCode()
    {
        return configName.length();
    }

}
