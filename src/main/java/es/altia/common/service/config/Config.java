package es.altia.common.service.config;

import java.util.*;
import java.net.*;

import es.altia.common.service.*;

/**
 * Interface for configuration implementation classes that are returned
 * by <code>ConfigService.getConfig()</code> methods.
 *
 * @version 1.0
 */
public interface Config extends Reconfigurable
{
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
    public boolean getBoolean(String theKey);

    /**
     * Read a property and transform it into an integer.
     *
     * @param theKey the key to look for
     * @return the integer value for the requested property
     *
     * @throws CriticalException if the property is not found or the property
     *                           is not an integer
     */
    public int getInt(String theKey);

    /**
     * Read a property as a string.
     *
     * @param theKey the key to look for
     * @return the String object for the requested property
     *
     * @throws CriticalException if the property is not found
     */
    public String getString(String theKey);

    /**
     * Read a property and transform it into a Collection. The structure
     * of the property must be [el1;el2;el3;...]
     * @param theKey the key to look for
     * @return the Collection for the requested property
     *
     * @throws CriticalException if the property is not found
     */
    public Collection getCollection(String theKey);

    /**
     * Read a property and transform it into an URL.
     *
     * @param theKey the key to look for
     * @return the URL object for the requested property
     *
     * @throws CriticalException if the property is not found or the property
     *                           is not an URL
     */
    public URL getURL(String theKey);

    /**
     * @return all properties as a <code>Properties</code> object
     */
    public Properties getProperties();

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
    public String getMessage(String theKey, Object[] theObjects);

}
