package es.altia.common.service.config;

import java.io.*;
import java.util.*;

/**
 * The MapResourceBundle class extends the standard ResourceBundle to provide
 * a way to reload the properties file.
 *
 * Instead of using the parent mechanism, all key/value pairs are flattened
 * into one {@link java.util.Hashtable Hashtable}, with values added later via
 * {@link #addResources addResources} overriding the previous
 * setting for an identical key. <p>
 *
 * With {@link #fetchProperties fetchProperties} resources can be added from a
 * property file that is bundled with a jar archive, something that doesn't
 * always work with {@link java.util.ResourceBundle#getBundle(String)
 * ResourceBundle.getBundle}, especially in Applets.
 *
 * @version 1.0
 */
public class MapResourceBundle extends ResourceBundle
{
    /**
     * The properties.
     */
    private Hashtable map = new Hashtable();

    /**
     * Create a new MapResourceBundle.
     */
    public MapResourceBundle()
    {
    }

    /**
     * Create a new MapResourceBundle from a ResourceBundle, copying its
     * key/value pairs.
     *
     * @param theRb the resources to start with
     */
    public MapResourceBundle(ResourceBundle theRb)
    {
        addResources(theRb);
    }

    /**
     * Add a ResourceBundle's resources to the MapResourceBundle, overriding
     * existing resources with identical keys.
     *
     * @param theBundle the ResourceBundle to add
     */
    public void addResources(ResourceBundle theBundle)
    {
        Enumeration e = theBundle.getKeys();
        while(e.hasMoreElements()) {
            String key = (String)e.nextElement();
            map.put(key, theBundle.getObject(key));
        }
    }

    /**
     * Add a some Properties to the MapResourceBundle, overriding existing
     * resources with identical keys.
     *
     * @param theProperties the Properties to add
     */
    public void addResources(Properties theProperties)
    {
        Enumeration e = theProperties.keys();
        while(e.hasMoreElements()) {
            String key = (String)e.nextElement();
            map.put(key, theProperties.get(key));
        }
    }

    /**
     * This is a replacement for {@link
     * java.util.ResourceBundle#getBundle(String,Locale)
     * ResourceBundle.getBundle} that can handle resources from jar files.
     * This version searches for <code>.properties</code> files only. The
     * properties are then added to the MapResourceBundle.
     *
     * @param theBasename the basename for resource file
     * @param theLocale   the locale to use
     * @param theResolver A class from the same jar file as the resource
     *
     * @throws MissingResourceException If the resource cannot be found.
     */
    public void fetchProperties(String theBasename, Locale theLocale, Class theResolver) throws MissingResourceException
    {
        boolean found = false;

        // Transform the '.' into '/'
        String canonicalName = theBasename.replace('.', '/');

        // Prefix the name with a '/' if there isn't already one
        if (!canonicalName.startsWith("/")) {
            canonicalName = '/' + canonicalName;
        }

        // first the base file
        InputStream stream =
            theResolver.getResourceAsStream(canonicalName + ".properties");

        if (stream != null) {
            try {
                addResources(new PropertyResourceBundle(stream));
                found = true;
            } catch (IOException e) {
                // Do not do anything. It just means the file does not
                // exist.
            }
        }

        // now add the language
        stream = theResolver.getResourceAsStream(canonicalName + "_" +
            theLocale.getLanguage() + ".properties");

        if (stream != null) {
            try {
                addResources (new PropertyResourceBundle(stream));
                found = true;
            } catch (IOException ex) {
                // Do not do anything. It just means the file does not
                // exist.
            }
        }

        // and now the full locale (no variants)
        stream = theResolver.getResourceAsStream(canonicalName + "_" +
            theLocale.getLanguage() + "_" + theLocale.getCountry() + ".properties");

        if (stream != null) {
            try {
                addResources (new PropertyResourceBundle(stream));
                found = true;
            } catch (IOException ex) {
                // Do not do anything. It just means the file does not
                // exist.
            }
        }

        if (found == false) {
            throw new MissingResourceException(
                ConfigError.getMessage("MapResourceBundle.MissingResource",
                    new Object[] { canonicalName, theLocale }),
                canonicalName, theLocale.toString());
        }

    }

    /**
     * This is a replacement for {@link
     * java.util.ResourceBundle#getBundle(String) ResourceBundle.getBundle}
     * that can handle resources from jar files.  This version searches for
     * <code>.properties</code> files only. The properties are then added to
     * the MapResourceBundle.
     *
     * @param theBasename the basename for resource file
     * @param theResolver A class from the same jar file as the resource
     *
     * @throws MissingResourceException If the resource cannot be found.
     */
    public void fetchProperties(String theBasename, Class theResolver) throws MissingResourceException
    {
        fetchProperties(theBasename, Locale.getDefault(), theResolver);
    }

    /**
     * Return an enumeration of the keys.
     *
     * @return The enumeration.
     */
    public Enumeration getKeys()
    {
        return map.keys();
    }

    /**
     * Get an object from the MapResourceBundle.
     *
     * @param theKey the key to look for
     * @return the object associated with the key
     *
     * @throws MissingResourceException If the key is not found.
     */
    public Object handleGetObject(String theKey) throws MissingResourceException
    {
        if (map.containsKey(theKey)) {
            return map.get(theKey);
        }

        throw new MissingResourceException (
            ConfigError.getMessage("MapResourceBundle.MissingProperty", new Object[] { theKey }),
            MapResourceBundle.class.getName(), theKey);
    }

}
