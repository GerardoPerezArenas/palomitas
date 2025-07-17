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
import es.altia.util.jdbc.*;
import es.altia.util.persistance.searchcriterias.*;



/**
  *
  *
  **/
public interface SQLDAOCreateIntArrayPropertyCommandAdapter {

	/**
	  * SQL attribute names for the INSERT command (included PK attributes,ordered)
	  **/
	public String[] cGetSQLAttributeNames();

	/**
	  * SQL table name for the concrete VO
	  **/
	public String cGetSQLTableName();

	/**
	  * Binds the PreparedStatement with PrimaryKey elements
	  *
	  * @param 	pk		concrete impl you must use for binding
	  *		preparedStatement	the JDBC statement to be bound
	  *		i			actual PreparedStatement positional field counter
	  * @return positional counter incremented properly
     **/
	public int cpkBind(PrimaryKey pk, PreparedStatement preparedStatement, int i)
		throws SQLException;


}//class
/*______________________________EOF_________________________________*/
