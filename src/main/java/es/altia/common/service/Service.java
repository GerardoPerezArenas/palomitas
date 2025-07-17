package es.altia.common.service;

import java.util.*;
import es.altia.common.exception.*;

/**
 * Base Service class that all services must extend.
 *
 * @version 1.0
 */
public abstract class Service
{
    /**
     * The service's name
     */
    private String serviceName;

    /**
     * Specify whether this service has already been initialized or not
     */
    private boolean initialized;

    /**
     * The properties file Resource bundle
     */
    private ResourceBundle configBundle;

    /**
     * Initialization of the Service
     */
    public abstract void init() throws TechnicalException;

    /**
     * Shutdown of the Service
     */
    public abstract void shutdown() throws TechnicalException;

    /**
     * Private constructor to force the subclasses to define a
     * one argument constructor.
     */
    private Service()
    {
    }

    /**
     * @param theServiceName the service's name
     * @param thePropertyFileName the common configuration file name
     */
    public Service(String theServiceName, String thePropertyFileName)
    {
        serviceName = theServiceName;
        setInitialized(false);

        // Read te common properties file
        configBundle = PropertyResourceBundle.getBundle(thePropertyFileName);
    }

    /**
     * @param isInitialized true if the service is initialized, false otherwise
     */
    protected void setInitialized(boolean isInitialized)
    {
        initialized = isInitialized;
    }

    /**
     * @return true if the service has already been initialized, false otherwise
     */
    public boolean isInitialized()
    {
        return initialized;
    }

    /**
     * @return the service's name
     */
    protected String getServiceName()
    {
        return serviceName;
    }

}
