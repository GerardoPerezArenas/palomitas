package es.altia.util.jdbc.sqlbuilder;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;

/**
 * Clase de conveniencia que encapsula los resultados de un ResultSet.
 */
public class QueryResult implements Result {

	/** Resultados de la consulta. Cada fila es un mapa. */
    private List<Map<String, Object>> resultado = new ArrayList<Map<String, Object>>();
	/** Posición actual en el recorrido de las filas de los resultados. */
    private int posicion = -1;

    public QueryResult() { }
    
    /**
     * Crea un QueryResult a partir de un ResultSet
     * 
     * @param rs
     * @throws SQLException
     */
    public QueryResult(ResultSet rs) throws SQLException {
        this.setValues(rs);
    }
    
    /**
     * Recupera, en la fila actual, el valor de la columna de nombre key.
     *
     * @param <T>
     * @param key
     * @param clase
     * @return
     */
    public <T> T get(String key, Class<T> clase) {
        return clase.cast(resultado.get(posicion).get(key));
    }

    /**
     * Recupera, en la fila actual, el valor de la columna de nombre key.
     * Recupera, en la fila actual, el valor de la columna de nombre key.
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return resultado.get(posicion).get(key);
    }

    /**
     * Recupera, en la fila actual, el valor de la columna de nombre key y lo castea a String.
     *
     * @param key
     * @return
     */
    public String getString(String key) {
		// Se utiliza StringBuilder porque es capaz de parsear la mayoría de tipos a String
        return new StringBuilder().append(get(key)).toString();
    }

    /**
     * Recupera, en la fila actual, el valor de la columna de nombre key y lo parsea a Integer.
     *
     * @param key
     * @return
     */
    public Integer getInteger(String key) {
		return SqlParser.parsearAInt(get(key));
    }

	/**
	 * Recupera, en la fila actual, el valor de la columna de nombre key y lo parsea a Boolean.
	 *
	 * @param key
	 * @return
	 */
	public Boolean getBoolean(String key) {
		return SqlParser.parsearABoolean(get(key));
	}

	/**
	 * Recupera, en la fila actual, el valor de la columna de nombre key como byte[] a partir de un {@link Blob}.
	 * 
	 * @param key
	 * @return
	 * @throws SQLException 
	 */
	
	public byte[] getBlobBytes(String key) throws SQLException {
		Blob blob = get(key, Blob.class);
		return (blob != null) ? blob.getBytes(1, (int) blob.length()) : null;
	}
	
	public byte[] getBlobBytesBase64(String key) throws SQLException {
		return Base64.encodeBase64(getBlobBytes(key));
	}
	
	public String getClobString(String key) throws SQLException {
		Clob clob = get(key, Clob.class);
		int tamano = Long.valueOf(clob.length()).intValue();
		return clob.getSubString(1, tamano);
	}
	
    /**
     * Devuelve un boolean que representa si hay más filas y mueve el cursor a
     * la siguiente posición en ese caso. En caso contrario devuelve false.
     *
     * @return 
     */
    public boolean next() {
        if (posicion >= resultado.size() - 1) {
            return false;
        }
        // Hay siguiente
        posicion++;
        return true;
    }

//	/**
//	 * Devuelve la fila actual en forma de mapa.
//	 *
//	 * @return
//	 */
//	public Map<String, Object> getRow() {
//		return resultado.get(posicion);
//	}

	/**
	 * Devuelve la fila actual en forma de {@link RowResult}.
	 *
	 * @return
	 */
	public RowResult getRow() {
		return new RowResult(resultado.get(posicion));
	}

    /**
     * Introduce los valores de un ResultSet en el QueryResult.
     *
     * @param rs ResultSet cuyos valores serán encapsulados en el QueryResult.
     * @throws SQLException 
     */
    void setValues(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData;
        // Se iteran las tuplas
        while (rs.next()) {
            metaData = rs.getMetaData();
            // Se iteran las columnas
            Map<String, Object> tupla = inicializarFila();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                tupla.put(metaData.getColumnName(i), rs.getObject(i));
            }
            resultado.add(tupla);
        }
    }

    /**
     * De este modo se aísla la implementación interna de los Maps.
     * La implementación interna de cada fila es un LinkedHashMap para mantener el orden original.
     */
    private Map<String, Object> inicializarFila() {
        return new LinkedHashMap<String, Object>();
    }

}
