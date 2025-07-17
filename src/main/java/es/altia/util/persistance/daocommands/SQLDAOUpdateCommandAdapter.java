/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Calendar;
import java.util.List;

import es.altia.util.exceptions.*;
import es.altia.util.persistance.*;
import es.altia.util.persistance.exceptions.*;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.daocommands.SQLDAOPKCommandAdapter;
import es.altia.util.jdbc.*;
import es.altia.util.persistance.searchcriterias.*;



/**
  * @author
  *
  *
  **/
public interface SQLDAOUpdateCommandAdapter extends SQLDAOPKCommandAdapter{

	/**
	  * SQL attribute names to be updated (non PrimaryKey)
	  **/
	public String[] upGetSQLUpdateAttributeNames();
	
	/**
	  * Binds the PreparedStatement with all PersistentObject properties to be updated
	  *
	  * @param 	po			concrete impl you must use for binding
	  *		preparedStatement	the JDBC statement to be bound
	  *		i			actual PreparedStatement positional field counter
	  * @return positional counter incremented properly
	  **/
	public int upBindUpdateFieldsToStatement(PersistentObject po, PreparedStatement preparedStatement, int i)
		throws SQLException;

    /**
     * Getter for attribute for Version Number pattern implementation
     *
     * @return the attribute name for this pattern, null if not used
     */
    public String getVersionNumberAttributeName();

    
}//class
/*______________________________EOF_________________________________*/
