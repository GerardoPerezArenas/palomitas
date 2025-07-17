package es.altia.common.service.xmlmapper;

import es.altia.common.service.*;
import es.altia.common.service.config.*;

/**
 * Service used to map XML file with java object.
 */
public class XMLMapperService extends Service
{
    /**
     * File name of the Config resource for Exceptions and logs
     */
    private final static String CONFIG_RESOURCE_NAME = "es.altia.common.service.xmlmapper.xmlmapperconfig";

    /**
     * Prefix of the properties in common.properties that are related to the XMLMapper Service
     */
    private final static String XMLMAPPER_SERVICE_PREFIX = "XMLMapperService";

    /**
     * Suffix of the property in common.properties that specifies a mapping file
     */
    private final static String MAPPING_FILE_SUFFIX = "MappingFile";

    /**
     * Suffix of the property in common.properties that specifies a source XML file
     */
    private final static String XML_FILE_SUFFIX = "XMLFile";

    /**
     * Config Resource
     */
    private Config config;

    /**
     * The common properties file name.
     */
    private String configPropertiesName;

    /**
     * @param theServiceName the service's name
     * @param thePropertyFileName the common configuration file name
     */
    public XMLMapperService(String theServiceName, String thePropertyFileName)
    {
        super(theServiceName, thePropertyFileName);
        configPropertiesName = thePropertyFileName;
    }

    /**
     * Initialization of the service. Loads the configuration resource file.
     */
    public void init()
    {
        config = ConfigServiceHelper.getConfig(CONFIG_RESOURCE_NAME);
    }

    /**
     * Shutdown of the service.
     */
    public void shutdown()
    {
    }

    /**
     * Returns the XMLMapper object that is configured to use a mapping file
     * whose logical name is <code>theLogicalName</code>. This logical name is
     * defined in the common.properties file.
     *
     * @param theLogicalName the logical name of the mapping file
     * @return the XMLMapper object
     */
    public XMLMapper getXMLMapper(String theLogicalName)
    {
        return new XMLMapper(
            getServiceProperty(
                XMLMAPPER_SERVICE_PREFIX + "." + theLogicalName + "." + XML_FILE_SUFFIX),
            getServiceProperty(
                XMLMAPPER_SERVICE_PREFIX + "." + theLogicalName + "." + MAPPING_FILE_SUFFIX));
    }

    /**
     * Helper method to get a string property from the Service configuration
     * file.
     */
    private String getServiceProperty(String thePropertyName) {
        return ConfigServiceHelper.getConfig(configPropertiesName).getString(thePropertyName);
    }

}
