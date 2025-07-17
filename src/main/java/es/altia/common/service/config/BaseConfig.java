package es.altia.common.service.config;

import java.util.*;
import java.text.*;
import java.net.*;
import es.altia.common.exception.*;


/**
 * Wrapper class around the <code>MapResourceBundle</code> class. It
 * standardizes the API seen by the user and provides properties getters.
 * Instances of this class are returned by the
 * <code>ConfigManager.getConfig()</code> methods. The <code>BaseConfig</code>
 * properties can be refreshed (i.e. reread from disk) by calling the
 * <code>refresh()</code> method.
 *
 * @version 1.0
 */
public class BaseConfig implements Config {

    /**
     * The properties.
     */
    protected MapResourceBundle data;

    /**
     * The properties file name.
     */
    protected String configName;

    /**
     * Read the properties file whose name is passed as parameter, using default
     * language.
     *
     * @param theConfigName the name of the properties file (without trailing ".properties").
     *                      The name can contain "." or "/" in it. Example :
     *                      "es.altia.common.service.config.myproperties" or
     *                      "com/irf/common/service/config/myproperties"
     */
    public BaseConfig(String theConfigName)
    {
        this(theConfigName, Locale.getDefault());
    }

    /**
     * Read the properties file whose name is passed as parameter, using
     * specified language.
     *
     * @param theConfigName the name of the properties file (without trailing ".properties").
     *                      The name can contain "." or "/" in it. Example :
     *                      "es.altia.common.service.config.myproperties" or
     *                      "com/irf/common/service/config/myproperties"
     *
     * @param theLocale     the locale to use when looking up the properties file
     */
    public BaseConfig(String theConfigName, Locale theLocale)
    {
        configName = theConfigName;
        data = new MapResourceBundle(
            PropertyResourceBundle.getBundle(theConfigName, theLocale));
    }

    /**
     * Read a property and transform it into a boolean. If the property value is
     * <code>"true"</code>, it returns <code>true</code>. It returns
     * <code>false</code> otherwise.
     *
     * @param theKey the key to look for
     * @return the boolean value for the requested property
     *
     * @throws CriticalException if the property is not found
     */
    public boolean getBoolean(String theKey)
    {
	boolean returnValue;

	String value = getString(theKey);

	// The Boolean class constructor never raises any exception. It just
        // look for "true" and return true if found. Returns false otherwise.
	returnValue = new Boolean(value).booleanValue();

	return returnValue;
    }

    /**
     * Read a property and transform it into an integer.
     *
     * @param theKey the key to look for
     * @return the integer value for the requested property
     *
     * @throws CriticalException if the property is not found or the property
     *                           is not an integer
     */
    public int getInt(String theKey)
    {
	int returnValue;

	String value = getString(theKey);

	try {

            returnValue = new Integer(value).intValue();

	} catch (NumberFormatException e) {

            throw new CriticalException(
                this.getClass().getName(),
                ConfigError.getMessage("BaseConfig.BadInteger",
                    new Object[] { theKey, value }));
	}

	return returnValue;
    }

    /**
     * Read a property as a string.
     *
     * @param theKey the key to look for
     * @return the String object for the requested property
     *
     * @throws CriticalException if the property is not found
     */
    public String getString(String theKey)
    {
	String key;

        try {

            key = (String)data.getString(theKey);

        } catch (MissingResourceException e) {

            throw new CriticalException(
                this.getClass().getName(),
                ConfigError.getMessage("BaseConfig.MissingProperty",
                    new Object[] { theKey }), e);
	}

	return key;
    }

    /**
     * Read a property and transform it into a Collection. The structure
     * of the property must be [el1;el2;el3;...]
     * @param theKey the key to look for
     * @return the Collection for the requested property
     *
     * @throws CriticalException if the property is not found
     */
    public Collection getCollection(String theKey)
    {
        Collection returnValue;

        String value = getString(theKey);
        try {
            if ((value.charAt(0)!='[') || (value.charAt(value.length()-1)!=']')) {
                throw new CriticalException(
                    this.getClass().getName(),
                    ConfigError.getMessage("BaseConfig.BadCollection",
                        new Object[] { theKey, value }));
            }
            StringTokenizer st = new StringTokenizer(value.substring(1,value.length()-1), ";");
            returnValue = new ArrayList();
            while (st.hasMoreElements()) {
                returnValue.add(st.nextElement());
            }
        } catch (Exception e) {
            throw new CriticalException(
                this.getClass().getName(),
                ConfigError.getMessage("BaseConfig.BadCollection",
                    new Object[] { theKey, value }));
        }
        return returnValue;
    }

    /**
     * Read a property and transform it into an URL.
     *
     * @param theKey the key to look for
     * @return the URL object for the requested property
     *
     * @throws CriticalException if the property is not found or the property
     *                           is not an URL
     */
    public URL getURL(String theKey)
    {
	URL url;
	String value;

	value = getString(theKey);

	try {

            url = new URL(value);

	} catch (MalformedURLException e) {

            throw new CriticalException(
                this.getClass().getName(),
                ConfigError.getMessage("BaseConfig.BadURL",
                    new Object[] { theKey, value }));

	}

	return url;
    }

    /**
     * Reload the properties from the properties file.
     */
    public synchronized void reconfigure()
    {
        data.fetchProperties(configName, this.getClass());
    }

    /**
     * @return all properties as a <code>Properties</code> object
     */
    public Properties getProperties()
    {
        Properties props = new Properties();
        Enumeration enum1 = data.getKeys();
        while (enum1.hasMoreElements()) {
            String key = (String)enum1.nextElement();
            props.put(key, data.getString(key));
        }

        return props;
    }

    /**
     * Read a property key and format it using the <code>MessageFormat</code>
     * syntax.
     *
     * @param theKey     the message key
     * @param theObjects the data to use to format the returned message
     * @return the formatted message belonging to <code>key</code>
     *
     * @throws CriticalException if the property is not found
     */
    public String getMessage(String theKey, Object[] theObjects)
    {
        return MessageFormat.format(getString(theKey), theObjects);
    }

}


