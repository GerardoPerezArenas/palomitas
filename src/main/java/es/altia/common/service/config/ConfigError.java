package es.altia.common.service.config;

import java.util.*;
import java.text.*;

/**
 * Externalization of messages to log and exceptions messages for the
 * configuration classes themselves. Indeed, they cannot use a standard
 * properties file because there has been an error in the configuration
 * classes, meaning the reading of some properties file failed. It's the
 * chicken and the egg problem.
 *
 *@version 1.0
 */
public class ConfigError extends ListResourceBundle {

    /**
     * The externalized strings.
     */
    static final Object[][] contents = {

        {"MapResourceBundle.MissingResource", "Cannot find resource for [{0}] locale [{1}]"},
        {"MapResourceBundle.MissingProperty", "Cannot find property [{0}]"},

        {"BaseConfig.MissingProperty", "Cannot find property [{0}]"},
        {"BaseConfig.BadInteger", "The value [{0}] for property [{1}] is not a valid integer"},
        {"BaseConfig.BadCollection", "The value [{0}] for property [{1}] is not a valid collection"},
        {"BaseConfig.BadURL", "The value [{0}] for property [{1}] is not a valid URL"}

    };

    /**
     * @return the externalized strings.
     */
    public Object[][] getContents()
    {
        return contents;
    }

    /**
     * Helper method to easily get an externalized string.
     *
     * @param theKey the message key
     * @return the message belonging to <code>theKey</code>
     */
    public static String getMessage(String theKey)
    {
        return getBundle(ConfigError.class.getName()).getString(theKey);
    }

    /**
     * Helper method to easily get an externalized string and format it at
     * the same time using <code>MessageFormat</code> formats.
     *
     * @param theKey the message key
     * @param theObjects the data to use to format the returned message
     * @return the formatted message belonging to <code>key</code>
     */
    public static String getMessage(String theKey, Object[] theObjects)
    {
        return MessageFormat.format(getMessage(theKey), theObjects);
    }

}
