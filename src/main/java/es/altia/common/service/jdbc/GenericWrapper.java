package es.altia.common.service.jdbc;

import java.sql.*;
import java.util.*;
import java.text.*;
import javax.sql.*;
import es.altia.common.exception.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.*;

/** Encapsulation class to access to the data base via the API JDBC */
public abstract class GenericWrapper {
    boolean isTransactional;

    /** Configuration : technical data. */
    protected static Config m_ConfigTech;

    /** Configuration : applicative data. */
    protected static Config m_ConfigError;

    /** Data source JDBC 2.0 */
    DataSource m_DataSource;
    static DataSource m_DataSourceXA;

    /** Recordset which contains the data retrieved by the request */
    protected ResultSet m_Rset;

    /** Statement */
    //private PreparedStatement m_Prep;
    protected PreparedStatement m_Prep;

    /** Connection to the data base */
    private Connection m_Connection;

    /** If true then JDBCWrapper has already a connection to the open data base */
    private boolean m_IsConnected = false;

    /** Log management */
    static Log m_Log;

    /** Constructor of the object <code>JDBCWrapper</code>. */
    public GenericWrapper() {
	  // Retrieval of properties files
	  m_ConfigTech = ConfigServiceHelper.getConfig("techserver");
	  m_ConfigError = ConfigServiceHelper.getConfig("error");
	  // Creation of the log object
      m_Log = LogFactory.getLog(this.getClass().getName());
    }

    /**
     * This method allow to prepare the execution of a request closing eventually the PreparedStatement and/or the
     * RecordSet previously open
     * @exception TechnicalException
     */
    void beforeExecute() throws TechnicalException {
	  try {
		// Close the RecordSet
		if (m_Rset != null) {
		    m_Rset.close();
		    m_Rset = null;
		}
		closePreparedStatement(m_Prep);
	  } catch (Exception e) {
		String message = m_ConfigError.getString("Erreur.JDBCWrapper.beforeExecute");
		throw new TechnicalException(message, e);
	  }
    }

    /**
     * Close the connexion to the data base and les objects eventually used (ResultSet et PreparedStatement)
     * @exception TechnicalException	If there is any problem when the connection is closed
     */
    public void close() throws TechnicalException {
	  try {
		// Close ResultSet
		if (m_Rset != null) {
		    m_Rset.close();
		    m_Rset = null;
		}
		// Close PreparedStatement
		closePreparedStatement(m_Prep);
		// Close the connection.
		if (m_Connection != null) {
		    m_Connection.close();
		    m_Log.debug("Conexión cerrada en el Wrapper");
		    m_Connection = null;
		    m_Log.debug("Connection fermée.");
		} else {
		    m_Log.debug("Connection déjà fermée.");
		}
	  } catch (Exception e) {
		throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.FermetureConnectionBD"), e);
	  }
    }

    /**
     * Close PreparedStatement
     * @exception TechnicalException	If there is any problem when PreparedStatement is closed
     */
    private void closePreparedStatement(PreparedStatement prep) throws TechnicalException {
	  try {
		// Close preparedStatement.
		if (prep != null) {
		    prep.close();
		}
	  } catch (Exception e) {
		throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.closePreparedStatement"), e);
	  }
    }

    /**
     * Execute a request SQL whithout parameters. The result is stored into the class attribute m_Rset (Resultset).
     * @param sql SQL request to execute
     * @exception TechnicalException in the case of error while the request is being executed (f.e. SQL error).
     */
    public void execute(String sql) throws TechnicalException {
	  m_Log.debug("execute(...)");
	  try {
		beforeExecute();
		m_Prep = getConnection().prepareStatement(sql);
		if (m_Log.isDebugEnabled()) m_Log.debug("SQL request: [" + sql + "].");
		m_Rset = m_Prep.executeQuery();
	  } catch (SQLException e) {
		String message = e.getMessage();
		m_Log.debug(message);
		if (message.indexOf("ORA-08177") != -1) {
		    m_Log.debug("ORA-08177 up : request re-executed");
		    int countdown = 5;
		    boolean requete_executee = false;
		    while ((countdown != 0) && (requete_executee == false)) {
			  try {
				countdown--;
				m_Rset = m_Prep.executeQuery();
				requete_executee = true;
			  } catch (Exception error) {
				String messageb = error.getMessage();
				if (messageb.indexOf("ORA-08177") != -1) {
				    m_Log.debug("New try");
				    requete_executee = false;
				}
				else {
				    throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ExecutionSQL"), error);
				}
			  }
		    }
		    if (requete_executee == false) {
			  throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ExecutionSQL"), e);
		    }
		} else {
		    throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ExecutionSQL"), e);
		}
	  } catch (Exception e) {
		throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ExecutionSQL"), e);
	  }
	  m_Log.debug("execute");
    }

    /**
     * Execute a SQL request with parameters Nothing is returned. The result is stored into the class
     * attribute m_Rset (Resultset).
     * @param sql SQL request to execute
     * @param param1 string which represent the request parameter
     * @exception TechnicalException in the case of error when the request is being executed (SQL error, for example).
     */
    public void execute(String sql, String param1) throws TechnicalException {
	  m_Log.debug("execute(...)");
	  try {
		beforeExecute();
		m_Prep = getConnection().prepareStatement(sql);
		m_Prep.setString(1, param1);
		if (m_Log.isDebugEnabled()) m_Log.debug("SQL request: [" + sql + "], [P1 = " + param1 != null ? param1.trim() : "null" + "]).");
		m_Rset = m_Prep.executeQuery();
	  } catch (SQLException e) {
		String message = e.getMessage();
		if (message.indexOf("ORA-08177") != -1) {
		    m_Log.debug("ORA-08177 up: request re-executed");
		    int countdown = 5;
		    boolean requete_executee = false;
		    while ((countdown != 0) && (requete_executee == false)) {
			  try {
				countdown--;
				m_Rset = m_Prep.executeQuery();
				requete_executee = true;
			  } catch (Exception error) {
				String messageb = error.getMessage();
				if (messageb.indexOf("ORA-08177") != -1) {
				    m_Log.debug("New try");
				    requete_executee = false;
				}
				else {
				    throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ExecutionSQL"), error);
				}
			  }
		    }
		    if (requete_executee == false) {
			  throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ExecutionSQL"), e);
		    }
		} else {
		    throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ExecutionSQL"), e);
		}
	  } catch (Exception e) {
		throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ExecutionSQL"), e);
	  }
	  m_Log.debug("execute");
    }

    /**
     * Execute a SQL request with parameters Nothing is returned. The result is stored into the class
     * attribute m_Rset (Resultset).
     * @param sql SQL request to execute
     * @param params request parameters list (vector)
     * @exception TechnicalException in the case of error when the request is being executed (SQL error, for example).
     */
    public void execute(String sql, Vector params) throws TechnicalException {
	  try {
		beforeExecute();
		m_Prep = getConnection().prepareStatement(sql);
		for (int i = 0; i < params.size(); i++) {
		    if (params.elementAt(i) != null) {
			  String className = params.elementAt(i).getClass().getName();
			  if (className.equals("java.lang.String")) {
				m_Prep.setString(i + 1, (String)params.elementAt(i));
			  } else if (className.equals("java.lang.Integer")) {
				m_Prep.setInt(i + 1, ((Integer)params.elementAt(i)).intValue());
			  } else if (className.equals("java.lang.Long")) {
				m_Prep.setLong(i + 1, ((Long)params.elementAt(i)).longValue());
			  } else if (className.equals("java.lang.Double")) {
				m_Prep.setDouble(i + 1, ((Double)params.elementAt(i)).doubleValue());
			  } else if (className.equals("java.util.Date")) {
				m_Prep.setTimestamp(i + 1, new java.sql.Timestamp(((java.util.Date) params.elementAt(i)).getTime()));
			  } else if (className.equals("java.sql.Date")) {
				m_Prep.setDate(i + 1, (java.sql.Date) params.elementAt(i));
			  } else if (className.equals("java.sql.Time")) {
				m_Prep.setTime(i + 1, (java.sql.Time) params.elementAt(i));
			  } else if (className.equals("java.sql.Timestamp")) {
				m_Prep.setTimestamp(i + 1, (java.sql.Timestamp) params.elementAt(i));
			  } else {
				// The class of one of parameters is not dealt here
				throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ExecutionSQL.unknownParameterClass"));
			  }
		    } else
			  m_Prep.setObject(i + 1, null);
		}
		if (m_Log.isDebugEnabled()) {
		    StringBuffer sb = new StringBuffer("([Request= " + sql + "]");
		    for (int i = 0; i < params.size(); i++) {
			  sb.append(", [P");
			  sb.append(i);
			  sb.append("= ");
			  sb.append(params.elementAt(i) == null ? "null" : params.elementAt(i).toString().trim());
			  sb.append("]");
		    }
		    sb.append(").");
		    m_Log.debug("SQL request: " + sb);
		}
		m_Rset = m_Prep.executeQuery();
		// m_Prep.executeUpdate();
	  } catch (SQLException e) {
		m_Log.error(e);
		String message = e.getMessage();
		m_Log.error(message);
		if (message.indexOf("ORA-08177") != -1) {
		    m_Log.debug("ORA-08177 up: request re-executed");
		    int countdown = 5;
		    boolean requete_executee = false;
		    while ((countdown != 0) && (requete_executee == false)) {
			  try {
				countdown--;
				m_Rset = m_Prep.executeQuery();
				requete_executee = true;
			  } catch (Exception error) {
				String messageb = error.getMessage();
				if (messageb.indexOf("ORA-08177") != -1) {
				    m_Log.debug("New try");
				    requete_executee = false;
				}
				else {
				    throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ExecutionSQL"), error);
				}
			  }
		    }
		    if (requete_executee == false) {
			  throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ExecutionSQL"), e);
		    }
		} else {
		    throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ExecutionSQL"), e);
		}
	  } catch (Exception e) {
		m_Log.error("Estamos en el execute en JDBCWrapper. Exception");
		m_Log.error(e);
		throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ExecutionSQL"), e);
	  }
	  m_Log.debug("execute");
    }

    /**
     * Method executed before unload the object JDBCWrapper from the memory.
     * Close the connection to the data base if it is not done yet. The method finalize is not controled, it is not neccesary
     * to close the connection to the data base and to free this resource.
     * The method close must be called explicitly over the objects JDBCWrapper in the final clause.
     */
    protected void finalize() throws TechnicalException {
	  if (m_Connection != null) {
		m_Log.debug("finalize(): the connection have not been liberated");
	  }
	  close();
    }

    /**
     * Retrieve a connection to the data base
     * @return a connection to the data base
     */
    protected Connection getConnection() throws TechnicalException {
	  if (m_Connection == null) {
		try {
		    m_Log.debug("Retrieve connection from a data source.");
		    m_Connection = getDataSource().getConnection();
		    m_Log.debug("Adquirida conexión en el Wrapper");
		    m_Log.debug("Retrieve connection from a finished data source.");
		    m_IsConnected = true;
		} catch (SQLException e) {
		    throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ConnectionBD"), e);
		}
	  }
	  return m_Connection;
    }

    /** Retrieve one of the data source from the cache to improve the performance. */
    abstract DataSource getDataSource() throws TechnicalException;

    /**
     * Obtiene la fecha de un campo mediante el método getTimeStamp El tipo que devuelve es java.util.Date
     * El metodo que lo llame lo llame podrá hacer casting a java.sql.timestamp
     * @param champ 			field name
     * @return 				field value
     * @exception	TechnicalException 	If there is any error reading resultset, from the SQL request
     */
    public java.util.Date getDateTimeStamp(String champ) throws TechnicalException {
	  try {
		return m_Rset.getTimestamp(champ);
	  } catch (Exception e) {
		String message = MessageFormat.format(m_ConfigError.getString("Erreur.JDBCWrapper.LectureChamp"),
		    new Object[] { champ });
		throw new TechnicalException(message, e);
	  }
    }

    /**
     * Obtiene la fecha de un campo mediante el método getTimeStamp El tipo que devuelve es java.util.Date
     * El metodo que lo llame lo llame podrá hacer casting a java.sql.Date
     * @param champ 			field name
     * @return 				field value
     * @exception	TechnicalException 	If there is any error reading resultset, from the SQL request
     */
    public java.util.Date getDate(String champ) throws TechnicalException {
	  try {
		return m_Rset.getDate(champ);
	  } catch (Exception e) {
		String message = MessageFormat.format(m_ConfigError.getString("Erreur.JDBCWrapper.LectureChamp"),
		    new Object[] { champ });
		throw new TechnicalException(message, e);
	  }
    }

    /**
     * Retrieve the value of a field of type double, which comes from a SQL request
     * @param champ 			field name
     * @return 				field value
     * @exception	TechnicalException 	If there is any error reading resultset, from the SQL request
     */
    public double getDouble(String champ) throws TechnicalException {
	  try {
		return m_Rset.getDouble(champ);
	  } catch (Exception e) {
		String message = MessageFormat.format(m_ConfigError.getString("Erreur.JDBCWrapper.LectureChamp"),
		    new Object[] { champ });
		throw new TechnicalException(message, e);
	  }
    }

    /**
     * Retrieve the value of a field of type int, which comes from a SQL request
     * @param champ 			field name
     * @return 				field value
     * @exception	TechnicalException 	If there is any error reading resultset, from the SQL request
     */
    public int getInt(String champ) throws TechnicalException {
	  try {
		return m_Rset.getInt(champ);
	  } catch (Exception e) {
		String message = MessageFormat.format(m_ConfigError.getString("Erreur.JDBCWrapper.LectureChamp"),
		    new Object[] { champ });
		throw new TechnicalException(message, e);
	  }
    }

    /**
     * Retrieve the value of a field of type long, which comes from a SQL request
     * @param champ 			field name
     * @return 				field value
     * @exception	TechnicalException 	If there is any error reading resultset, from the SQL request
     */
    public long getLong(String champ) throws TechnicalException {
	  try {
		return m_Rset.getLong(champ);
	  } catch (Exception e) {
		String message = MessageFormat.format(m_ConfigError.getString("Erreur.JDBCWrapper.LectureChamp"),
		    new Object[] { champ });
		throw new TechnicalException(message, e);
	  }
    }

    /**
     * Retrieve the value of a field of type Object (for example Objet Oracle), which comes from a SQL request
     * @param champ 			field name
     * @return 				field value
     * @exception	TechnicalException 	If there is any error reading resultset, from the SQL request
     */
    public Object getObject(String champ) throws TechnicalException {
	  try {
		return m_Rset.getObject(champ);
	  } catch (Exception e) {
		String message = MessageFormat.format(m_ConfigError.getString("Erreur.JDBCWrapper.LectureChamp"),
		    new Object[] { champ });
		throw new TechnicalException(message, e);
	  }
    }

    /**
     * Retrieve the value of a field of type String, which comes from a SQL request
     * @param champ 			field name
     * @return 				field value
     * @exception	TechnicalException 	If there is any error reading resultset, from the SQL request
     */
    public String getString(String champ) throws TechnicalException {
	  try {
		return m_Rset.getString(champ);
	  } catch (Exception e) {
		String message = MessageFormat.format(m_ConfigError.getString("Erreur.JDBCWrapper.LectureChamp"),
		    new Object[] { champ });
		throw new TechnicalException(message, e);
	  }
    }

    /**
     * Retrieve the value of a field of type Date, which comes from a SQL request
     * @param champ 			field name
     * @return 				field value
     * @exception	PgnDBException 	If there is any error reading resultset, from the SQL request
     */
    public java.util.Date getTimestamp(String champ) throws TechnicalException {
	  try {
		return m_Rset.getTimestamp(champ);
	  } catch (Exception e) {
		String message = MessageFormat.format(m_ConfigError.getString("Erreur.JDBCWrapper.LectureChamp"),
		    new Object[] { champ });
		throw new TechnicalException(message, e);
	  }
    }

    /**
     * Go through resultset (result of a SQL request). Pass to the next element.
     * @return 		 		True if there is a following element, otherwise false
     * @exception	TechnicalException 	If there is any error reading resultset, from the SQL request
     */
    public boolean next() throws TechnicalException {
	  boolean booleanRetour = false;
	  try {
		booleanRetour = m_Rset.next();
	  } catch (SQLException e) {
		String message = e.getMessage();
		if (message.indexOf("ORA-08177") != -1) {
		    m_Log.debug("ORA-08177 up: request re-executed over JDBCWrapper.next()");
		    int countdown = 5;
		    boolean requete_executee = false;
		    while ((countdown != 0) && (requete_executee == false)) {
			  try {
				countdown--;
				booleanRetour = m_Rset.next();
				requete_executee = true;
			  } catch (Exception error) {
				String messageb = error.getMessage();
				if (messageb.indexOf("ORA-08177") != -1) {
				    m_Log.debug("New try");
				    requete_executee = false;
				}
				else {
				    throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ResultatSQL"), error);
				}
			  }
		    }
		    if (requete_executee == false) {
			  throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ResultatSQL"), e);
		    }
		} else {
		    throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ResultatSQL"), e);
		}
	  } catch (Exception e) {
		throw new TechnicalException(m_ConfigError.getString("Erreur.JDBCWrapper.ResultatSQL"), e);
	  }
	  return booleanRetour;
    }
}
