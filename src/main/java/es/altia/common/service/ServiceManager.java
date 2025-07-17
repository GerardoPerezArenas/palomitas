package es.altia.common.service;

import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.reflect.*;

import es.altia.common.exception.*;
import es.altia.common.service.config.MapResourceBundle;

/**
 * Manages all Service classes. It provides support initialization,
 * shutdown and retrieval of services by name. It is a singleton and
 * caches a unique instance of each Service.
 *
 * @version 1.0
 */
public class ServiceManager
{
    /**
     * The default properties file name where the Common user configurations are
     * stored. The value can be overriden by calling the
     * <code>setPropertyFileName()</code> method.
     */
    private String configPropertiesName = "common";

    /**
     * Inner class that describes a service entry in the propeties file. This
     * is needed in order to keep the ordering of service's definition (a
     * <code>Hashtable</code> would not keep the order).
     */
    private class ServiceDefinition
    {
        public String serviceName;
        public String className;
    }

    /**
     * The unique instance of this class (singleton)
     */
    private static ServiceManager manager;

    /**
     * List of Services indexed by service name
     */
    private Hashtable services;

    /**
     * Definition of services (as defined in the properties file). It is
     * a list of <code>ServiceDefinition</code> objects.
     */
    private Vector serviceDefinitions;

    /**
     * Private constructor to enforce the singleton pattern
     */
    private ServiceManager()
    {
        services = new Hashtable();
        serviceDefinitions = new Vector();

        // Read service definitions
        readServiceDefinitions();
    }

    /**
     * Get the unique instance of the <code>ServiceManager</code> class.
     *
     * @return the unique singleton instance
     */
    public static ServiceManager getInstance()
    {
        if (manager == null) {
            synchronized (ServiceManager.class) {
                if (manager == null) {
                    manager = new ServiceManager();
                }
            }
        }

        return manager;
    }

    /**
     * Initialization of all services.
     *
     * @throws InitializationException if the service failed to initialize
     */
    public void init()
    {
        // Initialize all services
        Enumeration keys = serviceDefinitions.elements();
        while (keys.hasMoreElements()) {

            ServiceDefinition sd = (ServiceDefinition)keys.nextElement();

            // Initialize the service
            initializeService(sd.serviceName, sd.className);
        }
    }

    /**
     * Remove all initialized services
     */
    public void shutdown()
    {
        services = new Hashtable();
    }

    /**
     * @param theServiceName the service's name
     * @return the unique instance to the given service
     *
     * @throws InitializationException if the service is not found, meaning
     *         that it failed to initialize
     */
    public Service getService(String theServiceName)
    {
        // Look for service's instance in the cache
        Service service = (Service)services.get(theServiceName);

        // If the service is not found, try to lazy initialize it !
        // To do that, first initialize all services that this service depends
        // upon ...
        if (service == null) {

            boolean found = false;

            Enumeration enum1 = serviceDefinitions.elements();
            while (!found && enum1.hasMoreElements()) {

                ServiceDefinition sd = (ServiceDefinition)enum1.nextElement();

                // Is it our service ?
                if (sd.serviceName.equals(theServiceName)) {
                    found = true;
                }

                // If this service is not already initialized, initialize it !
                Service dependentService = (Service)services.get(sd.serviceName);
                if ((dependentService == null) || !dependentService.isInitialized()) {
                    initializeService(sd.serviceName, sd.className);
                }

            }

            // If the requested service has not been found, raise an exception
            if (!found) {
                throw new InitializationException(
                    ServiceManagerError.getMessage("ServiceManager.UnknownService",
                        new Object[] { theServiceName }));
            }

            // Now, try again to get the service !
            service = (Service)services.get(theServiceName);

            if (service == null) {

                throw new InitializationException(
                    ServiceManagerError.getMessage("ServiceManager.UnknownError",
                        new Object[] { theServiceName }));
            }

        }

        return service;
    }

    /**
     * Initialize a given Service.
     *
     * @param theServiceName the service's name
     * @param theClassName   the service's class name
     *
     * @throws InitializationException if the service failed to initialize
     */
    private void initializeService(String theServiceName, String theClassName)
    {
        try {
            Class serviceClass = Class.forName(theClassName);

            // Get constructor that takes 2 Strings and call it with the service's
            // name
            Constructor constructor = serviceClass.getConstructor(
                new Class[] { String.class, String.class });
            Service service = (Service)constructor.newInstance(
                new Object[] { theServiceName, configPropertiesName });

            // initialize the service if the service has not already been initialized
            if (!service.isInitialized()) {

                service.init();

                // Add the service instance to the cache
                services.put(theServiceName, service);
            } else {
                BootLogger.logDebug(
                    ServiceManagerError.getMessage("ServiceManager.ServiceAlreadyInitalized",
                        new Object[] { theServiceName }));
            }

	} catch (Exception e) {
            throw new InitializationException(
                ServiceManagerError.getMessage("ServiceManager.Initialization",
                    new Object[] { theServiceName }), e);
        }

    }

    /**
     * Read service definitions
     */
    private void readServiceDefinitions()
    {
        // Read the properties file containing Service definitions
        MapResourceBundle rb = new MapResourceBundle();
        rb.fetchProperties("es.altia.common.service.serviceconfig", this.getClass());

        // Read the services property
        String serviceNames = rb.getString("services");

        // Separate the service definitions (separated by ",")
        StringTokenizer st = new StringTokenizer(serviceNames, ",");
        while(st.hasMoreTokens()) {

            String serviceDef = st.nextToken();

            // Separate the service name from the service class name
            StringTokenizer st2 = new StringTokenizer(serviceDef, "=");
            String name = st2.nextToken().trim();
            String className = st2.nextToken().trim();

            // Add it to the list of services
            ServiceDefinition sd = new ServiceDefinition();
            sd.serviceName = name;
            sd.className = className;

            serviceDefinitions.add(sd);
        }
    }

    /**
     * @param theFullResourceName the full resource name to get. For example
     *                            es.altia.common.test.xml
     * @param theResolver         a class from the same class loader that is to
     *                            be used to get the resource.
     *
     * @return the resource as an URL.
     */
    public static URL getResource(String theFullResourceName, Class theResolver)
    {
        URL url = theResolver.getResource(makeCanonicalName(theFullResourceName));

        if (url == null) {

            throw new CriticalException(ServiceManager.class.getName(),
                ServiceManagerError.getMessage("ServiceManager.ResourceNotFound",
                    new Object[] { theFullResourceName }));

        }

        return url;
    }

    /**
     * @param theFullResourceName the full resource name to get. For example
     *                            es.altia.common.test.xml
     * @param theResolver         a class from the same class loader that is to
     *                            be used to get the resource.
     *
     * @return the resource as an input stream.
     */
    public static InputStream getResourceAsStream(String theFullResourceName, Class theResolver)
    {
        InputStream is = theResolver.getResourceAsStream(makeCanonicalName(theFullResourceName));

        if (is == null) {

            throw new CriticalException(ServiceManager.class.getName(),
                ServiceManagerError.getMessage("ServiceManager.ResourceNotFound",
                    new Object[] { theFullResourceName }));

        }

        return is;
    }

    /**
     * Creates a canonical name for a resource, i.e. replace the "." by "/" and
     * prepend a "/" if none is found. The last "." is ketp as it represents
     * the resource extension.
     *
     * @param theFullResourceName the full resource name to get. For example
     *                            es.altia.common.test.xml
     *
     * @return the canonical name fo the resource
     */
    private static String makeCanonicalName(String theFullResourceName)
    {
        // We must leave the last "." unchanged (it is the resource extension), so find it's
        // position
        int pos = theFullResourceName.lastIndexOf('.');

        // Transform the '.' into '/'
        String canonicalName = theFullResourceName.replace('.', '/');

        // Transform back the last '/' into a '.' (resource extension)
        if (pos != -1) {
            canonicalName = canonicalName.substring(0, pos) + "." +
                canonicalName.substring(pos + 1);
        }

        // Prefix the name with a '/' if there isn't already one
        if (!canonicalName.startsWith("/")) {
            canonicalName = '/' + canonicalName;
        }

        return canonicalName;
    }

    /**
     * Set the Common configuration properties file.
     *
     * @param theFileName the properties file name
     */
    public void setPropertyFileName(String theFileName)
    {
        configPropertiesName = theFileName;
    }

    /**
     * @return the name of the Common configuration file.
     */
    public String getPropertyFileName()
    {
        return configPropertiesName;
    }

}
