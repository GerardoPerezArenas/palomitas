package es.altia.common.service.log;

import org.apache.log4j.PropertyConfigurator;
import es.altia.common.service.config.*;
import es.altia.common.service.*;

import java.util.Hashtable;

/**
 * Logging service acting as a wrapper around the IBM Log4j logging class.
 * (<code>www.log4j.org</code>).
 *
 * @version 1.0
 */
public class LogService extends Service implements Reconfigurable {

    /**
     * List of <code>Log</code> instances (i.e. <code>Category</code>
     * objects for Log4j) indexed on the category's name.
     */
    private Hashtable logCategories;

    /**
     * The common properties file name.
     */
    private String configPropertiesName;

    /**
     * @param theServiceName the service's name
     * @param thePropertyFileName the common configuration file name
     */
    public LogService(String theServiceName, String thePropertyFileName)
    {
        super(theServiceName, thePropertyFileName);
        logCategories = new Hashtable();
        configPropertiesName = thePropertyFileName;        
    }

    /**
     * Called by the <code>ServiceManager</code> during Service initialization
     */
    public void init()
    {
        // Initiliaze Log4j
        reconfigure();

        setInitialized(true);
    }

    /**
     * Called by the <code>ServiceManager</code> during Service shutdown
     */
    public void shutdown()
    {
    }

    /**
     * @param theCategoryName the category's name. Usually, it is the full
     *        name of the class being logged, including the package name
     * @return the <code>Log</code> instance associated with the specified
     *         category name
     */
    public Log getLog(String theCategoryName)
    {
        Log log = (Log)logCategories.get(theCategoryName);

        if (log == null) {

            synchronized (logCategories) {
                log = (Log)logCategories.get(theCategoryName);
                if (log == null) {

                    //log = new BaseLog(theCategoryName);
                    log = new CommonsLoggingLog(theCategoryName);
                    logCategories.put(theCategoryName, log);
                }
            }

        }
        return log;
    }



    /**
     * Reconfigure the Log4j system by rereading the properties file
     */
    public synchronized void reconfigure()
    {
        // Read the Log properties file using the Config Service
        Config config = ConfigServiceHelper.getConfig(configPropertiesName);

        PropertyConfigurator.configure(config.getProperties());
    }

}
