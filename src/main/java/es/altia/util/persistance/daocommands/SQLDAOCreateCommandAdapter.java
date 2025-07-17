/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import es.altia.util.exceptions.*;
import es.altia.util.persistance.*;
import es.altia.util.persistance.exceptions.*;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.daocommands.stdsql.StdSQLDAOExistsCmd;
import es.altia.util.jdbc.*;
import es.altia.util.persistance.searchcriterias.*;



/**
  *
  *
  **/
public interface SQLDAOCreateCommandAdapter {

	/**
	  * SQL attribute names for the INSERT command (included PK attributes,ordered)
	  **/
	public String[] crGetSQLAttributeNames();

	/**
	  * SQL main table name for the concrete VO
	  **/
	public String pkGetSQLMainTableName();

	/**
	  * Binds the PreparedStatement with PrimaryKey elements
	  *
	  * @param 	pk		concrete impl you must use for binding
	  *		preparedStatement	the JDBC statement to be bound
	  *		i			actual PreparedStatement positional field counter
	  * @return positional counter incremented properly
	  **/
	public int pkBind(PrimaryKey pk, PreparedStatement preparedStatement, int i) 
		throws SQLException;

	/**
	  * Binds the PreparedStatement with all PersistentObject properties but PrimaryKey ones 
	  *
	  * @param 	vo			concrete impl you must use for binding
	  *		preparedStatement	the JDBC statement to be bound
	  *		i			actual PreparedStatement positional field counter
	  * @return positional counter incremented properly
	  **/
	public int crBindAllNonPK(PersistentObject vo, PreparedStatement preparedStatement, int i) 
		throws SQLException;

	/**
	  * Compose the complete PersistentObject with a given one and generated PrimaryKey
	  * if it was generated
	  *
	  * @param 	connection		the JDBC connection (maybe you need it to get generated key)
	  *		preparedStatement	the JDBC statement (maybe you need it to get generated key)
	  *		givenVO			the VO with its properties
	  *		generatedKey		the PRE-generated key (may be null if it is POST-generated)
	  * @return the complete PersistenObject (with generated PrimaryKey if it was generated)
	  **/
	public PersistentObject crReturnNewVO(Connection connection, PreparedStatement preparedStatement, PersistentObject givenVO, PrimaryKey generatedKey)
		throws SQLException;

	/**
	  * Shall I generate a PrimaryKey for this object?
	  **/
	public boolean crMustGeneratePK(PrimaryKey primaryKey);

	/**
	  * Generate a PrimaryKey for this PersistentObject
	  **/
	public PrimaryKey crPreGenerateKey(Connection connection, PersistentObject vo) 
		throws SQLException;

	/**
	  * Get Exists DAO Command
	  **/
	public SQLDAOExistsCommand crGetExistsCmd();

    /**
     * Getter for attribute for Version Number pattern implementation
     *
     * @return the attribute name for this pattern, null if not used
     */
    public String getVersionNumberAttributeName();


    public static String VERSION_NUMBER_DEFAULT_ATTRIBUTE_NAME = "pVersion";

}//class
/*______________________________EOF_________________________________*/
