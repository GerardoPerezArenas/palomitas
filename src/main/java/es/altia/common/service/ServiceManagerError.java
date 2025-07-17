package es.altia.common.service;

import java.util.*;
import java.text.*;

/**
 * Externalization of messages to log and exceptions messages for the
 * <code>ServiceManager</code> class. Indeed, it cannot use the <code>LogService</code>
 * service because the error could have happened when initializing this service.
 * It's the chicken and the egg problem.
 *
 * @version 1.0
 */
public class ServiceManagerError extends ListResourceBundle {

    /**
     * The externalized strings.
     */
    static final Object[][] contents = {

        {"ServiceManager.Initialization", "Error initializing service [{0}]"},
        {"ServiceManager.UnknownService", "Unknown service [{0}]"},
        {"ServiceManager.UnknownError", "Unknown error while getting service instance for service [{0}]"},
        {"ServiceManager.ServiceAlreadyInitalized", "Service [{0}] is has already been initialized"},
        {"ServiceManager.ResourceNotFound", "Cannot find resource [{0}]"}

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
        return getBundle(ServiceManagerError.class.getName()).getString(theKey);
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
