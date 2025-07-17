package es.altia.util.jdbc.sqlbuilder;

import java.sql.Blob;
import java.sql.SQLException;

/**
 * Interfaz que ofrece métodos para recuperar valores a través de claves. Encapsula resultados obtenidos de consultas
 * SQL.
 */
public interface Result {

	/**
	 * Recupera el valor asociado al nombre key.
	 *
	 * @param <T>
	 * @param key
	 * @param clase
	 * @return
	 */
	<T> T get(String key, Class<T> clase);

	/**
	 * Recupera el valor asociado al nombre key.
	 *
	 * @param key
	 * @return
	 */
	Object get(String key);

	/**
	 * Recupera el valor asociado al nombre key en forma de String.
	 *
	 * @param key
	 * @return
	 */
	String getString(String key);

	/**
	 * Recupera el valor asociado al nombre key en forma de Integer.
	 *
	 * @param key
	 * @return
	 */
	Integer getInteger(String key);

	/**
	 * Recupera el valor asociado al nombre key en forma de Boolean.
	 *
	 * @param key
	 * @return
	 */
	Boolean getBoolean(String key);

	/**
	 * Recupera el valor asociado al nombre key como byte[] a partir de un {@link Blob}.
	 *
	 * @param key
	 * @return
	 * @throws java.sql.SQLException
	 */
	byte[] getBlobBytes(String key) throws SQLException;
	
	/**
	 * Recupera el valor asociado al nombre key como byte[] en base 64 a partir de un {@link Blob}.
	 *
	 * @param key
	 * @return
	 * @throws java.sql.SQLException
	 */
	byte[] getBlobBytesBase64(String key) throws SQLException;

}
