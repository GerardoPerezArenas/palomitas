package es.altia.common.service.jndi;

import es.altia.common.service.config.*;
import es.altia.common.service.*;

import java.util.Enumeration;
import java.util.Hashtable;


/**
 * JNDI service acting as a wrapper around the LDAP directory.
 *
 * @version 1.0
 */
public class JNDIService extends Service
{

    // --------------------------------------------------------------
    // Members
    // --------------------------------------------------------------

    // Constants
    private final static String JNDI_PREFIXKEY                      = "JNDIService.";
    private final static String INITIAL_CONTEXT_FACTORY_SUFFIXKEY   = ".InitContextFactory";
    private final static String PROVIDER_URL_SUFFIXKEY              = ".providerURL";
    private final static String SECURITY_AUTHENTICATION_SUFFIXKEY   = ".SecurityAuthentication";
    private final static String SECURITY_PRINCIPAL_SUFFIXKEY        = ".SecurityPrincipal";
    private final static String SECURITY_CREDENTIALS_SUFFIXKEY      = ".SecurityCredentials";

    /** List of JNDIWrapper instances indexed on the logical name */
    private Hashtable jndiWrappers;

    /**
     * The common properties file name.
     */
    private String configPropertiesName;


    // --------------------------------------------------------------
    // Constructor
    // --------------------------------------------------------------

    /**
     * @param theServiceName the service's name
     * @param thePropertyFileName the common configuration file name
     */
    public JNDIService(String theServiceName, String thePropertyFileName)
    {
        super(theServiceName, thePropertyFileName);
        jndiWrappers = new Hashtable();
        configPropertiesName = thePropertyFileName;
    }


    // --------------------------------------------------------------
    // Methods
    // --------------------------------------------------------------

    /**
     * Called by the <code>ServiceManager</code> during Service initialization
     */
    public void init()
    {
        setInitialized(true);
    }


    /**
     * Called by the <code>ServiceManager</code> during Service shutdown
     */
    public void shutdown()
    {
        Enumeration enum1 = jndiWrappers.elements();
        while (enum1.hasMoreElements()) {
            JNDIWrapper jndi = (JNDIWrapper)enum1.nextElement();
            jndi.disconnect();
        }
    }


    /**
     * @param theLogicalName    the logical name found in the common.properties
     *                          file.
     * @return                  the JNDIWrapper instance associated with the
     *                          specified logical name
     */
    public JNDIWrapper getJNDI(String theLogicalName)
    {
        JNDIWrapper jndi = (JNDIWrapper)jndiWrappers.get(theLogicalName);

        if (jndi == null) {

            synchronized (jndiWrappers) {

                jndi = (JNDIWrapper)jndiWrappers.get(theLogicalName);
                if (jndi == null) {
                    // Getting parameters
                    String prefixKey = JNDI_PREFIXKEY + theLogicalName;

                    // Initial context factory class
                    String initContextFactory = getServiceProperty(prefixKey + INITIAL_CONTEXT_FACTORY_SUFFIXKEY);

                    // URL to the LDAP server
                    String providerURL = getServiceProperty(prefixKey + PROVIDER_URL_SUFFIXKEY);

                    // Authentication method
                    String securityAuthentication = getServiceProperty(prefixKey + SECURITY_AUTHENTICATION_SUFFIXKEY);

                    // Login name to the ldap server
                    String securityPrincipal = getServiceProperty(prefixKey + SECURITY_PRINCIPAL_SUFFIXKEY);

                    // Password associated with the login name
                    String securityCredentials = getServiceProperty(prefixKey + SECURITY_CREDENTIALS_SUFFIXKEY);

                    jndi = new BaseJNDIWrapper(initContextFactory, providerURL, securityAuthentication, securityPrincipal, securityCredentials);
                    jndiWrappers.put(theLogicalName, jndi);
                }
            }
        }
        return jndi;
    }


    /**
     * Helper method to get a string property from the Service configuration
     * file.
     */
    private String getServiceProperty(String thePropertyName) {
        return ConfigServiceHelper.getConfig(configPropertiesName).getString(thePropertyName);
    }
}
