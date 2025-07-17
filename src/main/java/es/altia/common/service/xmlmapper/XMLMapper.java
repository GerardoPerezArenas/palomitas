package es.altia.common.service.xmlmapper;

import es.altia.common.service.config.*;
import es.altia.common.service.*;
import es.altia.common.exception.*;

import org.exolab.castor.xml.*;
import org.exolab.castor.mapping.*;
import org.xml.sax.*;

import java.io.*;
import java.net.*;

/**
 * Wrapper class around the Castor <code>Mapping</code> class.
 */
public class XMLMapper
{
    /**
     * File name of the Config resource for Exceptions and logs
     */
    private final static String CONFIG_RESOURCE_NAME = "es.altia.common.service.xmlmapper.xmlmapperconfig";

    /**
     * Config resource used for log and exception messages
     */
    private Config config;

    /**
     * The Castor <code>Mapping</code> class used to represent a mapping.
     */
    private Mapping mapping;

    /**
     * The XML source file used by the <code>unmarshal()</code> method.
     */
    private InputSource xmlSource;

	/**
	 * The XML source resource name
	 */
	private String xmlSourceResourceName;

    /**
     *
     */
    public XMLMapper(String theXMLSourceResourceName, String theMappingURLResourceName)
    {
        // Read configuration file for exception and log messages
        config = ConfigServiceHelper.getConfig(CONFIG_RESOURCE_NAME);

        // Create a Mapping object
        mapping = new Mapping();

        // Get the mapping file as an URL
        URL mappingURL = ServiceManager.getResource(theMappingURLResourceName, this.getClass());


        // Get the XML source file as an InputSource
        xmlSource = new InputSource(
            ServiceManager.getResourceAsStream(theXMLSourceResourceName,
				this.getClass()));

        try {

            mapping.loadMapping(mappingURL);

        } catch (MappingException e) {

            throw new CriticalException(this.getClass().getName(),
                config.getMessage("Error.XMLMapper.MappingException",
                    new Object[] { mappingURL.toString() }), e);

        } catch (IOException e) {

           throw new CriticalException(this.getClass().getName(),
            config.getMessage("Error.XMLMapper.IOException",
                new Object[] { mappingURL.toString() }), e);

        }
	
		xmlSourceResourceName = theXMLSourceResourceName;
    }

    /**
     * Transform a XML file into a java object according to the set mapping file.
     *
     * @return the corresponding java object
     * @throws TechnicalException when there is an error mapping the XML file to
     *         the java objects
     */
    public Object unmarshal() throws TechnicalException
    {
        Object returnObject;

        try {

            Unmarshaller unmarshaller = new Unmarshaller(mapping);
            returnObject = unmarshaller.unmarshal(xmlSource);

        } catch (Exception e) {

            throw new TechnicalException(
                config.getMessage("Error.XMLMapper.UnmarshalException",
                    new Object[] { xmlSourceResourceName }), e);

        }

        return returnObject;

    }

}
