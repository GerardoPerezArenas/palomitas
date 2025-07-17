/*______________________________BOF_________________________________*/
package es.altia.util.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import es.altia.technical.PortableContext;

/**
 * @version $\Revision$ $\Date$
 */
public final class ConfigurationParametersManager {
    /*_______Constants______________________________________________*/
    private static final String JNDI_PREFIX = "java:comp/env/";
    private static final String CONFIGURATION_FILE = "techserver.properties";
    private static final String COMMON_FILE = "common.properties";

    /*_______Atributes______________________________________________*/
    private static boolean usesJNDI;
    private static Map parameters;
    private static Log log =
        LogFactory.getLog(ConfigurationParametersManager.class.getName());
    static {
        try {
            /* Read property file (if exists).*/
            final Class configurationParametersManagerClass = ConfigurationParametersManager.class;
            final ClassLoader classLoader = configurationParametersManagerClass.getClassLoader();
            final InputStream inputStream = classLoader.getResourceAsStream(CONFIGURATION_FILE);
            final Properties properties = new Properties();
            properties.load(inputStream);
            inputStream.close();
            /* We have been able to read the file. */
            usesJNDI = false;
            /*
             * We use a "HashMap" instead of a "HashTable" because HashMap's
             * methods are *not* synchronized (so they are faster), and the
             * parameters are only read.
             */
            parameters = new HashMap(properties);
            
            //Cargamos tambien el fichero commons.properties
            properties.clear();
            final InputStream inputStreamCommon = classLoader.getResourceAsStream(COMMON_FILE);
            properties.load(inputStreamCommon);
            if(parameters != null){
                parameters.putAll(properties);
            }else{
                parameters = new HashMap(properties);
            }//if(parameters != null)

            if (log.isDebugEnabled())
                log.debug("ConfigurationParametersManager: Using '"+CONFIGURATION_FILE+"' file for configuration.");
         } catch (Exception e) {
            /* We assume configuration with JNDI. */
            usesJNDI = true;
            /*
             * We use a synchronized map because it will be filled by using a
             * lazy strategy.
             */
            parameters = Collections.synchronizedMap(new HashMap());

            if (log.isDebugEnabled())
                log.debug("ConfigurationParametersManager: Using JNDI for configuration.");
        }//try-catch
    }//static


    /*_______Operations_____________________________________________*/
    private ConfigurationParametersManager() {}

    public static final String getParameter(String name)
            throws MissingConfigurationParameterException {        
        if (log.isDebugEnabled()) log.debug("ConfigurationParametersManager.getParameter("+name+")");
        String value = (String) parameters.get(name);
        if (value == null) {
            //if (log.isDebugEnabled()) log.debug("ConfigurationParametersManager.getParameter() Conf value not cached");
            if (usesJNDI) {
                //if (log.isDebugEnabled()) log.debug("ConfigurationParametersManager.getParameter() Trying JNDI lookup");
                try {
                    /*
                    final InitialContext initialContext = new InitialContext();
                    value = (String) initialContext.lookup(JNDI_PREFIX + name);
                    */
                    PortableContext pc = PortableContext.getInstance();
                    if (log.isDebugEnabled()) log.debug("He cogido el jndi: " + name);
                    //value = (String)pc.lookup(JNDI_PREFIX  + jndi, DataSource.class);
                    value = (String)pc.lookup(name, DataSource.class);

                    parameters.put(name, value);
                } catch (Exception e) {
                    if (log.isErrorEnabled()) log.error("ConfigurationParametersManager.getParameter() Not found parameter '"+name+"'");
                    throw new MissingConfigurationParameterException(name);
                }//try-catch                
            } else {
                throw new MissingConfigurationParameterException(name);
            }//if            
        }//if        
        return value;
    }//getParameter

}//class
/*______________________________EOF_________________________________*/