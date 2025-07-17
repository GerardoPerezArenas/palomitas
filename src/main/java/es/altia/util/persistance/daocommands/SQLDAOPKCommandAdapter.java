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
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.jdbc.*;
import es.altia.util.persistance.searchcriterias.*;



/**
  *
  *
  **/
public interface SQLDAOPKCommandAdapter {

	/**
	  * SQL main table name for the concrete VO
	  **/
	public String pkGetSQLMainTableName();

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

}//class
/*______________________________EOF_________________________________*/
