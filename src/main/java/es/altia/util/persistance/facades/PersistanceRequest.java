/*______________________________BOF_________________________________*/
package es.altia.util.persistance.facades;


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

import es.altia.util.facades.BusinessRequest;
import es.altia.util.exceptions.*;





/**
  * @author
  * @version      %I%, %G%
  *
  *
  **/
public interface PersistanceRequest extends BusinessRequest {

	/*_______Operations_____________________________________________*/
	public String txGetDataSourceKey();
	public void txSetConnection(Connection connection);
	public Connection txGetConnection();
	public boolean txIsTopLevel();

}//interface
/*______________________________EOF_________________________________*/
