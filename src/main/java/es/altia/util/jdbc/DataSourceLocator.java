package es.altia.util.jdbc;

import es.altia.util.collections.CollectionsFactory;
import es.altia.util.configuration.ConfigurationParametersManager;
import es.altia.util.configuration.MissingConfigurationParameterException;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.logdecorator.LoggingDataSource;
import es.altia.technical.PortableContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;

/**
 * Caches references to <code>DataSources</code>. It allows to register
 * explicitally a <code>DataSource</code> under a name, and get a reference to
 * it by its name (using JNDI, if not already registered or cached).
 */
public class DataSourceLocator {
    private static final String DBKEY_PARAMETER = "CON.gestor";

	public static final String JNDI_PREFIX = "java:comp/env/";

	private static Map dataSources = Collections.synchronizedMap(CollectionsFactory.getInstance().newHashMap(3));

    private static final Log _log =
            LogFactory.getLog(DataSourceLocator.class.getName());

    private DataSourceLocator() {}
	
	/**
     * Allows to register explicitally a <code>DataSource</code> with a
	 * given name. This method should only be used when 
	 * <code>DataSources</code> are not accessible through JNDI (for example 
	 * when using an application server providing only servlets and JSP).
	 */
	public static final void addDataSource(String name, DataSource dataSource) {

		dataSources.put(name, new LoggingDataSource(dataSource));
        //dataSources.put(name, dataSource);
		if (_log.isDebugEnabled()) _log.debug("DataSourceLocator.addDataSource("+name+")");
	}//addDataSource
	
	/**
     * Gets a reference to the <code>DataSource</code> with the given 
	 * <code>name</code>. If the reference has been explicitally registered 
	 * (with <code>addDataSource(String, DataSource)</code>) or is cached, it 
	 * is returned immediatelly. Otherwise, JNDI is used to get a reference to
	 * it (caching it for the next time) using the JNDI name:
	 * <code>JNDI_PREFIX +  name</code>. This allows an easy transition from
	 * an application server not providing <code>DataSources</code> registered
	 * under JNDI to one providing them.
     */
	public static final DataSource getDataSource(String name)
		throws InternalErrorException{        

		if (_log.isDebugEnabled()) _log.debug("DataSourceLocator.getDataSource("+name+") BEGIN");
		DataSource dataSource = (DataSource) dataSources.get(name);
		
		if (dataSource == null) {
			// if (_log.isDebugEnabled()) _log.debug("DataSourceLocator.getDataSource("+name+") Not into map!");
            try {
				/*
				final InitialContext initialContext = new InitialContext();
            	dataSource = (DataSource) initialContext.lookup(JNDI_PREFIX + name);
                */
                PortableContext pc = PortableContext.getInstance();
		        if (_log.isDebugEnabled()) _log.debug("He cogido el jndi: " + name);
		        // dataSource = (DataSource)pc.lookup("java:comp/env/"+jndi, DataSource.class);
		        dataSource = (DataSource)pc.lookup(name, DataSource.class);

                addDataSource(name, dataSource);
			} catch (Exception e) {
                if (_log.isErrorEnabled()) _log.error("DataSourceLocator.getDataSource("+name+") Not found!");
				throw new InternalErrorException(e);
			}//try-catch
		}//if
		if (_log.isDebugEnabled()) _log.debug("DataSourceLocator: getDataSource("+name+") END");
		return dataSource;
	}//getDataSource

    /**
     * Get the DBKey for an given DSKey
     * @param dsKey The DataSource key
     * @return the dbKey
     * @throws InternalErrorException if some fatal error happens or the dsKey isn't configured properly
     * @see es.altia.util.persistance.daocommands.SQLDAOCommandFactory for dbKeys
     */
    public static final String getDBKeyForDataSource(String dsKey)
        throws InternalErrorException {
        final String result;
        try {
            result = ConfigurationParametersManager.getParameter(DBKEY_PARAMETER);
        } catch (MissingConfigurationParameterException e) {
            e.printStackTrace();
            throw new InternalErrorException(e);
        }//try-catch
        return result;
    }//getDBKeyForDataSource

}//class
