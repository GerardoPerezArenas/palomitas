package es.altia.common.service.config;

import java.util.*;

import es.altia.common.service.*;

/**
 * Helper class to easily get a <code>Config</code> object. Normally you
 * would call <code>((ConfigService)ServiceManager.getInstance().
 * getService("config")).getConfig("myconfig")</code>. Using this helper class
 * you simply need to call <code>ConfigServiceHelper.getConfig("myconfig")</code>.
 *
 * @version 1.0
 */
public class ConfigServiceHelper
{
     /**
     * @param theConfigName the name of the properties file (without trailing ".properties").
     *                      The name can contain "." or "/" in it. Example :
     *                      "es.altia.common.service.config.myproperties" or
     *                      "com/irf/common/service/config/myproperties"
     *        idioma= euskera o castellano
     * @return the <code>Config</code> object that corresponds to the specified
     *         properties file for the default language
     * Si es euskera, devuelve el xxx_eu.properties
     */
    public static Config getConfig(String theConfigName, String idioma)
    {
        String sufijo = "";
        if ("euskera".equals(idioma) )
        {   sufijo="_eu";
			theConfigName=theConfigName+sufijo;
        }

        return ((ConfigService)ServiceManager.getInstance().getService("config")).getConfig(theConfigName);
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
    public static Config getConfig(String theConfigName)
    {
        return ((ConfigService)ServiceManager.getInstance().getService("config")).getConfig(theConfigName);
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
    public static Config getConfig(String theConfigName, Locale theLocale)
    {
        return ((ConfigService)ServiceManager.getInstance().getService("config")).getConfig(theConfigName, theLocale);
    }

}
