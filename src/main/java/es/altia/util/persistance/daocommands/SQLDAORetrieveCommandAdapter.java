/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.persistance.PrimaryKey;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;



/**
  *
  *
  **/
public interface SQLDAORetrieveCommandAdapter {

	/**
	  * Selected SQL attribute names (included PrimaryKey, ordered)
	  **/
	public String[] rtGetSQLSelectedAttributeNames();
	
	/**
	  * SQL table names for the concrete VO
	  **/
	public String[] rtGetSQLTableNames();
	
	/**
	  * PrimaryKey SQL attribute names (ordered)
	  **/
	public String[] pkGetSQLPKAttributeNames();

	/**
	  * Binds the PreparedStatement with PrimaryKey elements
	  *
	  * @param 	primaryKey		concrete impl you must use for binding
	  *		preparedStatement	the JDBC statement to be bound
	  *		i			actual PreparedStatement positional field counter
	  * @return positional counter incremented properly
	  **/
	public int pkBind(PrimaryKey primaryKey, PreparedStatement preparedStatement, int i)
		throws SQLException;

	/**
	  * uses two entries in resultMap for returning results: <ol>
	  * <li> COUNTER : Integer (ResultSet offset)</li>
	  * <li> PERSISTENT_OBJECT : PersistentObject (The result object) </li>
	  * </ol>
	  **/
	public void rtExtractVOFromResultSet(ResultSet resultSet, Map resultMap) 
		throws SQLException;

    /**
     * Getter for attribute for Version Number pattern implementation
     *
     * @return the attribute name for this pattern, null if not used
     */
    public String getVersionNumberAttributeName();

    
}//class
/*______________________________EOF_________________________________*/
