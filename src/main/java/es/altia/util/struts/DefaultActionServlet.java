package es.altia.util.struts;

import java.util.Iterator;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;

import es.altia.util.jdbc.DataSourceLocator;
import es.altia.util.jdbc.DataSourceLocator;

/**
 * A base <code>ActionServlet</code> to facilitate the implementation of
 * web applications using Struts. Currently, it provides the following 
 * functionality:
 *
 * <ul>
 * <li>Redefines <code>initDataSources</code>.</li>
 * </ul>
 */
public class DefaultActionServlet extends ActionServlet {

	/**
	 * Apart from calling <code>super.initModuleDataSources()</code>, registers the 
	 * <code>DataSources</code> (connection poools) specified by
 	 * <code>struts-config.xml</code> in
 	 * {@link es.altia.util.jdbc.DataSourceLocator}. <b>WARNING</b>:
	 * each <code>DataSource</code> must be specified with the <code>key</code> 
 	 * attribute.
	 */
	protected void initModuleDataSources(ModuleConfig config) 
		throws ServletException {
		super.initModuleDataSources(config);
		registerDataSources();
	}//initModuleDataSources

	private void registerDataSources() throws ServletException {
	/*
	 * The implementation of this method uses the inherited protected attribute
	 * "dataSources".
     */
		Iterator dataSourceNameIterator = dataSources.keySet().iterator();
		
		while (dataSourceNameIterator.hasNext()) {
			/* Register current DataSource to DataSourceLocator. */
			String dataSourceName = (String) dataSourceNameIterator.next();
			DataSource dataSource = 
				(DataSource) dataSources.get(dataSourceName);
			DataSourceLocator.addDataSource(dataSourceName, dataSource);
			/* 
			 * Log that the current DataSource has been registered in 
			 * DataSourceLocator. 
			 */
			if (log.isDebugEnabled()) log.debug("DefaultActionServlet: DataSource '" + dataSourceName + 
				"' registered in DataSourceLocator!");
		}//while	
	}//registerDataSources	
}//class
