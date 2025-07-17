package es.altia.common.service.xmlmapper;

import es.altia.common.service.*;

/**
 * Helper class to easily get a <code>XMLMapper</code> object. Normally you
 * would call <code>((XMLMapperService)ServiceManager.getInstance().
 * getService("xmlmapper")).getXMLMapper("mymap")</code>. Using this helper class
 * you simply need to call <code>XMLMapperServiceHelper.getXMLMapper("mymap")</code>.
 *
 * @version 1.0
 */
public class XMLMapperServiceHelper
{
    /**
     * Name of this service
     */
    private final static String MAPPER_SERVICE_NAME="xmlmapper";

    /**
     * Gets the XMLMapper associated with the specified logical name
     *
     * @param theLogicalName the logical name used to retrieve the XML Mapping file
     */
    public static XMLMapper getXMLMapper(String theLogicalName) {
        return ((XMLMapperService)ServiceManager.getInstance().getService(MAPPER_SERVICE_NAME)).getXMLMapper(theLogicalName);
    }

}
