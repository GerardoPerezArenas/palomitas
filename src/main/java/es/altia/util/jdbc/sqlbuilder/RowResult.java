package es.altia.util.jdbc.sqlbuilder;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;

public class RowResult implements Result {

	/**
	 * Resultados de la consulta. Cada fila es un mapa.
	 */
	private Map<String, Object> resultado = inicializarFila();

	public RowResult(Map<String, Object> resultado) {
		this.resultado = resultado;
	}

	/**
	 * Recupera el valor asociado al nombre key.
	 *
	 * @param <T> Tipo de la clase a castear.
	 * @param key Clave a utilizar para recuperar el valor.
	 * @param clase {@link Class} a la que se castea el valor recuperado.
	 * @return El valor casteado si existe. Null si no existe.
	 */
	@Override
	public <T> T get(String key, Class<T> clase) {
		Object valor = resultado.get(key);
		return (valor != null) ? clase.cast(valor) : null;
	}

	/**
	 * Recupera el valor asociado al nombre key.
	 *
	 * @param key
	 * @return
	 */
	@Override
	public Object get(String key) {
		return resultado.get(key);
	}

	/**
	 * Recupera el valor asociado al nombre key en forma de String.
	 *
	 * @param key
	 * @return
	 */
	@Override
	public String getString(String key) {
		// Se utiliza StringBuilder porque es capaz de parsear la mayoría de tipos a String
		Object obtenido = get(key);
		return (obtenido != null) ? new StringBuilder().append(obtenido).toString() : null;
	}

	/**
	 * Recupera el valor asociado al nombre key en forma de Integer.
	 *
	 * @param key
	 * @return
	 */
	@Override
	public Integer getInteger(String key) {
		return SqlParser.parsearAInt(get(key));
	}

	/**
	 * Recupera el valor asociado al nombre key en forma de Boolean.
	 *
	 * @param key
	 * @return
	 */
	public Boolean getBoolean(String key) {
		return SqlParser.parsearABoolean(get(key));
	}

	/**
	 * Recupera el valor asociado al nombre key como byte[] a partir de un {@link Blob}.
	 *
	 * @param key
	 * @return
	 * @throws java.sql.SQLException
	 */
	@Override
	public byte[] getBlobBytes(String key) throws SQLException {
		Blob blob = get(key, Blob.class);
		return (blob != null) ? blob.getBytes(1, (int) blob.length()) : null;
	}
	
	/**
	 * Recupera el valor asociado al nombre key como byte[] en base 64 a partir de un {@link Blob}.
	 *
	 * @param key
	 * @return
	 * @throws java.sql.SQLException
	 */
	@Override
	public byte[] getBlobBytesBase64(String key) throws SQLException {
		return Base64.encodeBase64(getBlobBytes(key));
	}

	/**
	 * Devuelve la fila como una lista iterable de pares.
	 *
	 * @return
	 */
	public List<Map.Entry<String, Object>> asList() {
		List<Map.Entry<String, Object>> list = new ArrayList<Map.Entry<String, Object>>();
		list.addAll(resultado.entrySet());
		return list;
	}

	/**
	 * Devuelve la fila como una lista iterable de pares.
	 *
	 * @return
	 */
	public List<?> getValores() {
		List<Object> list = new ArrayList<Object>();
		list.addAll(resultado.values());
		return list;
	}
	
		/**
	 * Devuelve la fila como una lista iterable de pares.
	 *
	 * @return
	 */
	public List<?> getKeys() {
		List<Object> list = new ArrayList<Object>();
		list.addAll(resultado.keySet());
		return list;
	}
	

	/**
	 * De este modo se aisla la implementacion interna de los Maps. La implementacion interna de cada fila es un
	 * LinkedHashMap para mantener el orden original.
	 */
	private Map<String, Object> inicializarFila() {
		return new LinkedHashMap<String, Object>();
	}

}
