/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.persistance.PrimaryKey;

import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 */
public interface SQLDAORetrieveIntArrayPropertyCommandAdapter {

    public String rtGetSQLPropertyAttributeName();
    public String[] rtGetSQLFilterAttributeNames();
    public String[] rtGetSQLAttributeNames();
    public String rtGetSQLTableName();
    public int rtFilterBind(PrimaryKey primaryKey, PreparedStatement preparedStatement, int i) 
        throws SQLException;
}//interface

/*______________________________EOF_________________________________*/