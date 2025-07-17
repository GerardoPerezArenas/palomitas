package es.altia.common.service.jdbc;

import java.util.Vector;
import java.sql.*;
import javax.sql.*;
import es.altia.common.exception.*;
import es.altia.technical.*;

public class XAJDBCWrapper extends GenericWrapper {
    /** Retrieve one of the data source from the cache to improve the performance. */
    DataSource getDataSource() throws TechnicalException {
        m_Log.debug("getDataSource()");
        isTransactional = true;
        if (m_DataSourceXA == null) {
            synchronized(this) {
                if (m_DataSourceXA == null) {
                    PortableContext pc = PortableContext.getInstance();
                    m_DataSourceXA = (DataSource)pc.lookup(m_ConfigTech.getString("CON.jndiXA"), DataSource.class);
                }
            }
        }
        m_Log.debug("getDataSource");
        return m_DataSourceXA;
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
            m_Prep.executeUpdate();
        } catch (SQLException e) {
            String message = e.getMessage();
            if (message.indexOf("ORA-08177") != -1) {
                m_Log.debug("ORA-08177 up : request re-executed");
                int countdown = 5;
                boolean requete_executee = false;
                while ((countdown != 0) && (requete_executee == false)) {
                    try {
                        countdown--;
                        m_Prep.executeUpdate();
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
            m_Prep.executeUpdate();
        } catch (SQLException e) {
            String message = e.getMessage();
            m_Log.debug(message);
            if (message.indexOf("ORA-08177") != -1) {
                m_Log.debug("ORA-08177 up: request re-executed");
                int countdown = 5;
                boolean requete_executee = false;
                while ((countdown != 0) && (requete_executee == false)) {
                    try {
                        countdown--;
                        m_Prep.executeUpdate();
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
                    //m_Log.debug("Vamos a tratar un objeto de clase " + className);
                    if (params.elementAt(i)instanceof java.lang.String)
                        m_Prep.setString(i + 1, (String)params.elementAt(i));
                    else if (params.elementAt(i)instanceof java.lang.Integer)
                        m_Prep.setInt(i + 1, ((Integer)params.elementAt(i)).intValue());
                    else if (params.elementAt(i)instanceof java.lang.Long)
                        m_Prep.setLong(i + 1, ((Long)params.elementAt(i)).longValue());
                    else if (params.elementAt(i)instanceof java.lang.Double)
                        m_Prep.setDouble(i + 1, ((Double)params.elementAt(i)).doubleValue());
                    else if (params.elementAt(i)instanceof java.util.Date)
                        m_Prep.setTimestamp(i + 1, new java.sql.Timestamp(((java.util.Date) params.elementAt(i)).getTime()));
                    else if (params.elementAt(i)instanceof java.sql.Date)
                        m_Prep.setDate(i + 1, (java.sql.Date) params.elementAt(i));
                    else if (params.elementAt(i)instanceof java.sql.Time)
                        m_Prep.setTime(i + 1, (java.sql.Time) params.elementAt(i));
                    else if (params.elementAt(i)instanceof java.sql.Timestamp)
                        m_Prep.setTimestamp(i + 1, (java.sql.Timestamp) params.elementAt(i));
                    else {
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
            //            m_Rset = m_Prep.executeQuery();
            m_Prep.executeUpdate();
            //        } catch (SQLException e) {
        } catch (Exception e) {
            m_Log.error("Excepcion en el Wrapper :" + e.getMessage());
            e.printStackTrace();
            String message = e.getMessage();
            m_Log.error(message);
            if (message.indexOf("ORA-08177") != -1) {
                m_Log.debug("ORA-08177 up: request re-executed");
                int countdown = 5;
                boolean requete_executee = false;
                while ((countdown != 0) && (requete_executee == false)) {
                    try {
                        countdown--;
                        //m_Rset = m_Prep.executeQuery();
                        m_Prep.executeUpdate();
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
        }
        m_Log.debug("execute");
    }
}
