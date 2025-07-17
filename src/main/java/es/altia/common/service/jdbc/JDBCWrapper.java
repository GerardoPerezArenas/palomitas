package es.altia.common.service.jdbc;

import javax.sql.*;
import es.altia.common.exception.*;
import es.altia.technical.*;

public class JDBCWrapper extends GenericWrapper{

private String jndi = null;
private static final String JNDI_PREFIX = "java:comp/env/";

public void setJndi(String jndi){
   this.jndi = jndi;
}

/**
 * Retrieve one of the data source from the cache to improve
 * the performance.
 *
 */
DataSource getDataSource() throws TechnicalException {

	m_Log.debug("getDataSource()");

    isTransactional=false;
	 synchronized (this) {
	    PortableContext pc = PortableContext.getInstance();
	    if(jndi == null){//si no hemos establecido el jndi usamos el general
		 jndi = m_ConfigTech.getString("CON.jndi");
		 m_Log.debug("he cogido el jndi por defecto");
	    }
	    else if (m_Log.isDebugEnabled()) m_Log.debug("He cogido el jndi: " + jndi);
	    m_DataSource = (DataSource)pc.lookup(jndi, DataSource.class);
	}
	m_Log.debug("getDataSource");
	return m_DataSource;
}


}
