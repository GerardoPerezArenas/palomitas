/*______________________________BOF_________________________________*/
package es.altia.util.jdbc;

import es.altia.util.commons.DateOperations;
import es.altia.util.commons.BasicTypesOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.persistance.daocommands.SQLFormatter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Calendar;
import java.util.List;


/**
 * @version $\Revision$ $\Date$
 */
public final class JdbcOperations {

    private static final Log _log =
            LogFactory.getLog(JdbcOperations.class.getName());

    public static final int VALOR_TRUE_EN_CAMPOS_BBDD = 1;
    public static final int VALOR_FALSE_EN_CAMPOS_BBDD = 0;
    
    private JdbcOperations(){}


    public static final Connection getNonTxReadOnlyConnection(String dsKey)
            throws InternalErrorException {
        Connection connection=null;
        final DataSource dataSource = DataSourceLocator.getDataSource(dsKey);
        try {
            connection = dataSource.getConnection();
            connection.setReadOnly(true);
        } catch(SQLException e) {
            _log.fatal("JdbcOperations: getNontTxReadOnlyConnection() error configuring connection");
            closeConnection(connection);
            throw new InternalErrorException(e);
        }//try-catch
        try {
            if (connection.getMetaData().supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED))
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        } catch (SQLException e) {
            _log.warn("JdbcOperations: getNontTxReadOnlyConnection() Not supported transactions isolation level");
        }//try-catch
        return connection;
    }//getNonTxReadOnlyConnection


    public static Connection getFullTransactionalConnection(String dsKey) throws InternalErrorException {
        
        final DataSource dataSource = DataSourceLocator.getDataSource(dsKey);
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            _log.debug("NIVEL DE TRANSACCION ACTUAL DE LA CONEXION: " + connection.getTransactionIsolation());
            //if (connection.getTransactionIsolation() == Connection.TRANSACTION_SERIALIZABLE) _log.debug("NO SE CAMBIA EL NIVEL DE LA TRANSACCION");
            //else connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);
            connection.setReadOnly(false);
            return connection;
        } catch(SQLException e) {
            _log.fatal("JdbcOperations: getFullTransactionalConnection() error configuring connection");
            closeConnection(connection);
            throw new InternalErrorException(e);
        }

    }


    /**
      * It closes a <code>ResultSet</code> if not <code>null</code>.
      */
    public static final void closeResultSet(ResultSet resultSet)
        throws InternalErrorException {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new InternalErrorException(e);
            }//try-catch
        }//if
    }//closeResultSet

    /**
      * It closes a <code>ResultSet</code> if not <code>null</code>.
      */
    public static final void closeResultSetSilently(ResultSet resultSet) {
        try {
            closeResultSet(resultSet);
        } catch (InternalErrorException e) {
            final SQLException sqle = (SQLException) e.getNestedException();
            if (_log.isWarnEnabled()) _log.warn("JdbcOperations: closeResultSetSilently() "+toString(sqle));
        }//try-catch
    }//closeResultSetSilently

    /**
      * It closes a <code>Statement</code> if not <code>null</code>.
      */
    public static final void closeStatement(Statement statement)
        throws InternalErrorException {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new InternalErrorException(e);
            }//try-catch
        }//if
    }//closeStatement

    /**
      * It closes a <code>Statement</code> if not <code>null</code>.
      */
    public static final void closeStatementSilently(Statement statement) {
        try {
            closeStatement(statement);
        } catch (InternalErrorException e) {
            final SQLException sqle = (SQLException) e.getNestedException();
            if (_log.isWarnEnabled()) _log.warn("JdbcOperations: closeStatementSilently() "+toString(sqle));
        }//try-catch
    }//closeStatementSilently

    /**
      * It closes a <code>Connection</code> if not <code>null</code>.
      */
    public static final void closeConnection(Connection connection)
        throws InternalErrorException {
        if (connection != null) {
            try {
                if (!connection.isClosed()) connection.close();
            } catch (SQLException e) {
                throw new InternalErrorException(e);
            }//try-catch
        }//if
    }//closeConnection

    /**
      * It closes a <code>Connection</code> if not <code>null</code>.
      */
    public static final void closeConnectionSilently(Connection connection) {
        try {
            closeConnection(connection);
        } catch (InternalErrorException e) {
            final SQLException sqle = (SQLException) e.getNestedException();
            if (_log.isWarnEnabled()) _log.warn("JdbcOperations: closeConnectionSilently() "+toString(sqle));
        }//try-catch
    }//closeConnection

    public static final int bindIntegerToStatement(Integer value, PreparedStatement preparedStatement, int i)
        throws SQLException {
        int result = i;

        if (value!=null)
            preparedStatement.setInt(result++, value.intValue());
        else
            preparedStatement.setNull(result++,java.sql.Types.INTEGER);

        return result;
    }//bindIntegerToStatement

    public static final int bindLongToStatement(Long value, PreparedStatement preparedStatement, int i)
        throws SQLException {
        int result = i;

        if (value!=null)
            preparedStatement.setLong(result++, value.longValue());
        else
            preparedStatement.setNull(result++,java.sql.Types.BIGINT);

        return result;
    }//bindLongToStatement

    public static final int bindCalendarToStatement(Calendar value, PreparedStatement preparedStatement, int i)
        throws SQLException {
        int result = i;

        if (value!=null) {
            final java.sql.Date dt = DateOperations.toSQLDate(value);
            preparedStatement.setDate(result++,dt);
        } else {
            preparedStatement.setNull(result++,java.sql.Types.DATE);
        }//if

        return result;
    }//bindCalendarToStatement

    public static final int bindCalendarAsTimestampToStatement(Calendar value, PreparedStatement preparedStatement, int i)
        throws SQLException {
        int result = i;

        if (value!=null) {
            final java.sql.Timestamp dt = DateOperations.toSQLTimestamp(value);
            preparedStatement.setTimestamp(result++,dt);
        } else {
            preparedStatement.setNull(result++,java.sql.Types.TIMESTAMP);
        }//if

        return result;
    }//bindCalendarToStatement

    public static final int bindStringToStatement(String value, int maxlength, PreparedStatement preparedStatement, int i)
        throws SQLException {
        int result = i;

        if (value!=null) {
                    if (value.length()>maxlength)
            preparedStatement.setString(result++, value.substring(0,maxlength));
                    else
            preparedStatement.setString(result++, value);
                } else {
            preparedStatement.setNull(result++,java.sql.Types.VARCHAR);
                }//if
        return result;
    }//bindStringToStatement

    public static final int bindTextToStatement(String value, PreparedStatement preparedStatement, int i)
        throws SQLException {
        int result = i;

        if (value!=null) {
            final byte[] valueAsBytes = value.getBytes();
            final InputStream in = new java.io.ByteArrayInputStream(valueAsBytes);
            preparedStatement.setAsciiStream(result++, in, valueAsBytes.length);
        } else {
            preparedStatement.setNull(result++,java.sql.Types.LONGVARCHAR);
        }//if
        return result;
    }//bindTextToStatement

    public static final int bindBinaryStreamToStatement(byte[] value, PreparedStatement preparedStatement, int i)
        throws SQLException {
        int result = i;

        if (value!=null) {
            final InputStream in = new java.io.ByteArrayInputStream(value);
            preparedStatement.setBinaryStream(result++, in, value.length);
        } else {
            preparedStatement.setNull(result++,java.sql.Types.BLOB);
        }//if
        return result;
    }//bindBinaryStreamToStatement

    public static final int bindObjectToStatement(Object value, int type, PreparedStatement preparedStatement, int i)
        throws SQLException {
        int result = i;
        switch (type) {
            case (SQLFormatter.TYPE_CALENDAR_DATE): {
                final Calendar c = (Calendar) value;
                result = bindCalendarToStatement(c,preparedStatement,i);
                break;
            }//case
            case (SQLFormatter.TYPE_CALENDAR_TIME): {
                throw new SQLException("JdbcOperations: bindToStatement(TYPE_CALENDAR_TIME). NOT YET IMPLEMENTED");
            }//case
            case (SQLFormatter.TYPE_CALENDAR_DATETIME): {
                throw new SQLException("JdbcOperations: bindToStatement(TYPE_CALENDAR_DATETIME). NOT YET IMPLEMENTED");
            }//case
            case (SQLFormatter.TYPE_CALENDAR_TIMESTAMP): {
                final Calendar c = (Calendar) value;
                result = bindCalendarAsTimestampToStatement(c,preparedStatement,i);
                break;
            }//case
            case (SQLFormatter.TYPE_STRING): {
                result = bindStringToStatement((String) value,Integer.MAX_VALUE,preparedStatement,i);
                break;
            }//case
            case (SQLFormatter.TYPE_TEXT): {
                result = bindTextToStatement((String) value,preparedStatement,i);
                break;
            }//case
            case (SQLFormatter.TYPE_INTEGER): {
                result = bindIntegerToStatement((Integer) value,preparedStatement,i);
                break;
            }//case
            case (SQLFormatter.TYPE_LONG): {
                result = bindLongToStatement((Long) value,preparedStatement,i);
                break;
            }//case
            default: {
                preparedStatement.setObject(result++,value);
            }//default
        }//switch
        return result;
    }//bindToStatement

    public static final String extractTextFromResultSet(ResultSet resultSet, int i)
        throws SQLException {
        String result = null;
        final InputStream in = resultSet.getAsciiStream(i);
        if (resultSet.wasNull())
            result = null;
        else if (in!=null) {
            int count = 0;
            try {
                final int size = in.available();
                final byte ary[] = new byte[size];
                byte buf = (byte) in.read();
                while(buf!=-1) {
                    ary[count] = buf;
                    count++;
                    buf = (byte) in.read();
                }//while
                result = new String(ary);
            } catch (IOException e) {
                throw new SQLException("JdbcOperations: extractTextFromResultSet(). Cannot read inputStream from DB. Message="+e.getMessage());
            }//try-catch
        }//if

        return result;
    }//extractTextFromResultSet

    public static final Integer extractIntegerFromResultSet(ResultSet resultSet, int i)
        throws SQLException {
        final int tmp = resultSet.getInt(i);
        if (resultSet.wasNull())
            return null;
        else
            return new Integer(tmp);
    }//extractIntegerFromResultSet

    public static final Long extractLongFromResultSet(ResultSet resultSet, int i)
        throws SQLException {
        final long tmp = resultSet.getLong(i);
        if (resultSet.wasNull())
            return null;
        else
            return new Long(tmp);
    }//extractLongFromResultSet

    public static final Calendar extractCalendarFromResultSet(ResultSet resultSet, int i)
        throws SQLException {
        Calendar result = null;
        final java.sql.Date dt = resultSet.getDate(i);
        if (dt!=null)
            result = DateOperations.toCalendar(dt);
        return result;
    }//extractCalendarFromResultSet

    public static final Calendar extractCalendarAsTimestampFromResultSet(ResultSet resultSet, int i)
        throws SQLException {
        Calendar result = null;
        final java.sql.Timestamp dt = resultSet.getTimestamp(i);
        if (dt!=null)
            result = DateOperations.toCalendar(dt);
        return result;
    }//extractCalendarFromResultSet

    public static final int bindCharToStatement(char c, PreparedStatement preparedStatement, int i) throws SQLException {
        int result = i;
        preparedStatement.setString(result++, String.valueOf(c));
        return result;
    }//bindCharToStatement

    public static final char extractCharFromResultSet(ResultSet resultSet, int i, char defaultValue)
        throws SQLException {
        final String tmp = resultSet.getString(i);
        if (tmp==null)
            return defaultValue;
        else
            return BasicTypesOperations.toChar(tmp);
    }//extractCharFromResultSet

    public static final String toString(SQLException e) {
        if (e!=null) {
            final StringBuffer buff = new StringBuffer("[SQLException:");
            buff.append(" ErrorCode=");
            buff.append(e.getErrorCode());
            buff.append(" SQLState=");
            buff.append(e.getSQLState());
            buff.append(" Message=");
            buff.append(e.getMessage());
            if (e.getNextException() != null) {
                buff.append(" NestedError=");
                buff.append(toString(e.getNextException()));
            }//if
            buff.append("]");
            return buff.toString();
        } else {
            return "null";
        }//if
    }//toString

    public static final String toString(SQLWarning e) {
        if (e!=null) {
            final StringBuffer buff = new StringBuffer("[SQLWarning:");
            buff.append(" WarningCode=");
            buff.append(e.getErrorCode());
            buff.append(" SQLState=");
            buff.append(e.getSQLState());
            buff.append(" Message=");
            buff.append(e.getMessage());
            if (e.getNextWarning() != null) {
                buff.append(" NestedWarning=");
                buff.append(toString(e.getNextWarning()));
            }//if
            buff.append("]");
            return buff.toString();
        } else {
            return "null";
        }//if
    }//toString

    /**
     * Metodo generico para la insercion de variables en un PreparedStatment.
     * Se insertaran en el orden en el que se pasen los valores y a partir
     * del indice especificado por indexStart. 
     * Si el indice es menor de 1 se empezara en 1.
     * 
     * @param stmt
     * @param indexStart
     * @param valores
     * @return valor del indice incrementado en el numero de elementos anadidos
     * @throws SQLException 
     */
   public static int setValues(PreparedStatement stmt, int indexStart, Object... valores) throws SQLException {
        int indiceStatement = indexStart;
        
        if (indiceStatement < 1) {
            indiceStatement = 1;
        }
        
        try {
            for (Object element : valores) {
                stmt.setObject(indiceStatement, element);
                indiceStatement++;
            }
        } catch (SQLException sqle) {
            _log.error(String.format("Error al hacer bind de las variables: posicion[%d], ", indiceStatement));
            throw sqle;
        }
        
        return indiceStatement;
    }

    /**
     * Metodo generico para la insercion de variables en un PreparedStatment.
     * Se insertaran en el orden en el que se pasen los valores y a partir
     * del indice especificado por indexStart. 
     * Si el indice es menor de 1 se empezara en 1.
     * 
     * @param stmt
     * @param indexStart
     * @param valores
     * @return valor del indice incrementado en el numero de elementos anadidos
     * @throws SQLException 
     */
    public static int setValues(PreparedStatement stmt, int indexStart, List<?> valores) throws SQLException {
        int indiceStatement = indexStart;
        
        if (indiceStatement < 1) {
            indiceStatement = 1;
        }
        
        try {
            for (Object element : valores) {
                stmt.setObject(indiceStatement, element);
                indiceStatement++;
            }
        } catch (SQLException sqle) {
            _log.error(String.format("Error al hacer bind de las variables: posicion[%d], ", indiceStatement));
            throw sqle;
        }
        
        return indiceStatement;
    }

    /**
     * Método que devuelve un Integer a partir de un ResultSet
     * 
     * @param resultSet
     * @param campo
     * @return 
     * @throws SQLException 
     */
    public static Integer getIntegerFromResultSet(ResultSet resultSet, String campo) throws SQLException {
        Integer valor = null;
        
        if (campo != null && !campo.isEmpty()) {
            valor = resultSet.getInt(campo);
            if (resultSet.wasNull()) {
                valor = null;
            }
        }

        return valor;
    }
    
    /**
     * Método que devuelve un Long a partir de un ResultSet
     * 
     * @param resultSet
     * @param campo
     * @return 
     * @throws SQLException 
     */
    public static Long getLongFromResultSet(ResultSet resultSet, String campo) throws SQLException {
        Long valor = null;
        
        if (campo != null && !campo.isEmpty()) {
            valor = resultSet.getLong(campo);
            if (resultSet.wasNull()) {
                valor = null;
            }
        }

        return valor;
    }

    /**
     * Método que devuelve un Boolean a partir de un valor Integer de un ResultSet
     * 
     * @param resultSet
     * @param campo
     * @return 
     * @throws SQLException 
     */
    public static Boolean getBooleanFromIntegerResultSet(ResultSet resultSet, String campo) throws SQLException {
        Boolean valor = null;
        
        Integer valorInt = getIntegerFromResultSet(resultSet, campo);
        
        if (valorInt != null) {
            if (VALOR_TRUE_EN_CAMPOS_BBDD == valorInt.intValue()) {
                valor = Boolean.TRUE;
            } else if (VALOR_FALSE_EN_CAMPOS_BBDD == valorInt.intValue()) {
                valor = Boolean.FALSE;
            }
        }

        return valor;
    }
    
    /**
     * Método que devuelve un Integer a partir de un valor Boolean para insercion en BBDD
     * 
     * @param valorBoolean
     * @return 
     */
    public static Integer convertBooleanToIntegerForDB(Boolean valorBoolean) {
        Integer valor = null;
        
        if (valorBoolean != null) {
            if (Boolean.TRUE.equals(valorBoolean)) {
                valor = VALOR_TRUE_EN_CAMPOS_BBDD;
            } else if (Boolean.FALSE.equals(valorBoolean)) {
                valor = VALOR_FALSE_EN_CAMPOS_BBDD;
            }
        }

        return valor;
    }
    
    /**
     * Anade a la consulta contenida en el parametro 'sql' la plabra WHERE o
     * AND.
     * Si whereInicializado = false -> " WHERE "
     * Si whereInicializado = true  -> " AND "
     *
     * @param sql
     * @param whereInicializado
     * @return
     */
    public static boolean anadirFiltroWhere(StringBuilder sql, boolean whereInicializado) {
        if (sql != null) {
            if (whereInicializado) {
                sql.append(" AND ");
            } else {
                sql.append(" WHERE ");
                whereInicializado = true;
            }
        }

        return whereInicializado;
    }

    /**
     * Adapta el campo para las consultas de tipo LIKE anadiendole el caracter '%'
     * segun el modo elegido:
     *    PREFIJO -> %valor
     *    SUFIJO  -> valor%
     *    AMBOS   -> %valor%
     *
     * @param campo
     * @param modo
     * @return
     */
    public static String convertirALike(String campo, JdbcOperations.LIKE_OPERATIONS modo) {
        String convertido = null;

        if (campo != null && modo != null) {
            switch (modo) {
                case PREFIJO:
                    convertido = String.format("%%%s", campo);
                    break;
                case SUFIJO:
                    convertido = String.format("%s%%", campo);
                    break;
                case AMBOS:
                    convertido = String.format("%%%s%%", campo);
                    break;
            }
        }

        return convertido;
    }

    /**
     * Construye el filtro con la fecha y hora segun el modo de comparacion deseado.
     * Permite truncar las horas, para poder realizar una comparacion por dias.
     * 
     * Los modos soportados actualmente son:
     *   campoBD = ?
     *   campoBD > ?
     *   campoBD >= ?
     *   campoBD < ?
     *   campoBD <= ?
     *   campoBD BETWEEN ? AND ?
     * 
     * @param operacionFecha
     * @param adapter
     * @param campoBD
     * @param truncar
     * @return
     */
    public static String construirFiltroFecha(AdaptadorSQLBD adapter,
            DateOperations.OPERACION_FECHA_BD operacionFecha,
            String campoBD,boolean truncar) {
        
        StringBuilder filtroFecha = new StringBuilder();
        
        filtroFecha.append(" ");
        if (truncar) {
            filtroFecha.append(adapter.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_TRUNCAR_HORA, new String[]{ campoBD }));
        } else {
            filtroFecha.append(campoBD);
        }

        switch (operacionFecha) {
            case NULO:
                filtroFecha.append(" IS NULL ");
                break;
                
            case NO_NULO:
                filtroFecha.append(" IS NOT NULL ");
                break;
                        
            case IGUAL:
                filtroFecha.append(" = ? ");
                break;

            case MAYOR:
                filtroFecha.append(" > ? ");
                break;

            case MAYOR_IGUAL:
                filtroFecha.append(" >= ? ");
                break;

            case MENOR:
                filtroFecha.append(" < ? ");
                break;

            case MENOR_IGUAL:
                filtroFecha.append(" <= ? ");
                break;

            case ENTRE:
                filtroFecha.append(" BETWEEN ? AND ? ");
                break;
        }
        
        return filtroFecha.toString();
    }

    /**
     * Tipo de filtrado para el LIKE
     */
    public static enum LIKE_OPERATIONS {
        PREFIJO,
        SUFIJO,
        AMBOS
    }
}//class
/*______________________________EOF_________________________________*/

