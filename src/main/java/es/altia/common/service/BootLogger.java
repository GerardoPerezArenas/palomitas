package es.altia.common.service;

/**
 * The BootLogger is designed to be used internally by the service manager and
 * other service when the <code>LogService</code> service may not have been
 * started.
 *
 * @version 1.0
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BootLogger
{
    //Para informacion de logs.
    protected static Log m_Log = LogFactory.getLog(BootLogger.class.getName());

    /**
     * if the System property <code>servicemanager.debug</code> is defined and
     * set to true then more verbose messages will be printed to stdout.
     */
    private static boolean debug = Boolean.getBoolean("servicemanager.debug");

    public BootLogger() {
    }
    /**
     *
     * @param theMessage the message to log
     */
    public static void logDebug(String theMessage)
    {
        if (debug) {
            m_Log.info(theMessage);
	}
    }

    /**
     *
     * @param theMessage the message to log
     */
    public static void log(String theMessage)
    {
        m_Log.info(theMessage);
    }
}
