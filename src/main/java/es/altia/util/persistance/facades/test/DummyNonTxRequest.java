/*______________________________BOF_________________________________*/
package es.altia.util.persistance.facades.test;


import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Locale;
import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.util.facades.BusinessRequest;
import es.altia.util.exceptions.*;

import es.altia.util.persistance.facades.*;




/**
  * @author
  * @version      %I%, %G%
  *
  *
  **/
public class DummyNonTxRequest implements NonTxReadOnlyRequest {
	/*_______Attributes_____________________________________________*/
	private static String DS_KEY="PLIS_MAIN_DATASOURCE";
	private Connection conn = null;
	

	/*_______Operations_____________________________________________*/
	public String txGetDataSourceKey() {
		return DS_KEY;
	}//txGetDataSourceKey


	public void txSetConnection(Connection connection) {
		conn = connection;
	}//txSetConnection

	public Connection txGetConnection() {
		return conn;
	}//txGetConnection

	public boolean txIsTopLevel() {
		return true;
	}//txIsTopLevel

	public void execute() 
		throws InternalErrorException {
        Log _log = LogFactory.getLog( this.getClass() );
		if (conn!=null) {
			if (_log.isDebugEnabled()) _log.debug(this.getClass().getName()+": NON TX REQUEST");			
		} else {
			throw new InternalErrorException(new Exception("Null Connection"));
		}//if
	}//execute

}//class
/*______________________________EOF_________________________________*/
