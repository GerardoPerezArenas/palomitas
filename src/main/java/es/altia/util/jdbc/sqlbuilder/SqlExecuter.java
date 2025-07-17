package es.altia.util.jdbc.sqlbuilder;

import es.altia.util.jdbc.JdbcOperations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Clase de conveniencia para facilitar la ejecución de consultas SQL.
 */
public class SqlExecuter extends SqlBuilder {

	// valores nunca es null
	List<Object> valores = crearLista();

	public SqlExecuter() {
		super();
	}

	public SqlExecuter(SqlBuilder sqlBuilder) {
		super(sqlBuilder.toString());
	}

	/**
	 * Añade los valores dados para sustituir por los elementos parametrizados (?) de la consulta.
	 *
	 * @param valores
	 * @return
	 */
	public SqlExecuter addValues(ArrayList<? extends Object> valores) {
		this.valores.addAll(valores);
		return this;
	}

	/**
	 * Añade los valores dados para sustituir por los elementos parametrizados (?) de la consulta.
	 *
	 * @param valores
	 * @return
	 */
	public SqlExecuter addValues(Object... valores) {
		for (Object valor : valores) {
			this.valores.add(valor);
		}
		return this;
	}

	/**
	 * Introduce los valores dados para sustituirlos por los elementos parametrizados (?) de la consulta.
	 *
	 * @param valores
	 * @return
	 */
	public SqlExecuter setValues(List<?> valores) {
		this.valores = crearLista(valores);
		return this;
	}

	/**
	 * Introduce los valores dados para sustituirlos por los elementos parametrizados (?) de la consulta.
	 *
	 * @param valores
	 * @return
	 */
	public SqlExecuter setValues(Object... valores) {
		this.valores = crearLista(valores);
		return this;
	}

	/**
	 * Elimina los valores introducidos en el SqlExecuter.
	 */
	public void clearValues() {
		valores = crearLista();
	}

	/**
	 * Devuelve la consulta incluyendo los valores parametrizados.
	 *
	 * @return
	 */
	@Override
	public String toString() {
		String consulta = super.toString();
		StringBuilder sb = new StringBuilder();
		int i = 0;

		for (String parte : consulta.split(" ")) {
			if (parte.equals("?")) {
				sb.append(getValorOComodin(i, valores));
				i++;
			} else {
				sb.append(parte);
			}
			sb.append(" ");
		}
		return sb.toString();
	}

	/**
	 * Ejecuta la consulta SQL y devuelve un objeto QueryResult que contiene los resultados de la misma. Para sentencias
	 * de tipo INSERT, UPDATE, DELETE o que no devuelven ningún resultado como sentencias DDL ver
	 * {@link SqlExecuter#executeUpdate(java.sql.Connection)}.
	 *
	 * @param conexion
	 * @return
	 * @throws SQLException
	 */
	public QueryResult executeQuery(Connection conexion) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conexion.prepareStatement(getSqlParametrizado());
			JdbcOperations.setValues(ps, 1, valores);
			rs = ps.executeQuery();
			return new QueryResult(rs);
		} catch (SQLException ex) {
			throw (ex);
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * Ejecuta la consulta SQL y devuelve un int como resultado. Para sentencias de tipo INSERT, UPDATE, DELETE o que no
	 * devuelven ningún resultado como sentencias DDL.
	 *
	 * @param conexion
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate(Connection conexion) throws SQLException {
		PreparedStatement ps = null;

		try {
			ps = conexion.prepareStatement(getSqlParametrizado());
			JdbcOperations.setValues(ps, 1, valores);
			return ps.executeUpdate();
		} catch (SQLException ex) {
			throw (ex);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	/**
	 * Crea un log de la consulta SQL, la ejecuta y devuelve un int como resultado. Para sentencias de tipo INSERT,
	 * UPDATE, DELETE o que no devuelven ningún resultado como sentencias DDL.
	 *
	 * @param conexion
	 * @param log
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate(Connection conexion, Logger log) throws SQLException {
		this.logSqlDebug(log);
		return this.executeUpdate(conexion);
	}

	/**
	 * Crea una consulta de insercion de la forma "INSERT INTO tabla (pk, campo1, campo2, ... , campoN) VALUES
	 * (nombreSecuencia.NEXTVAL, ?1, ?2, ... , ?N) ", donde cada entrada del mapa tendra, en orden, la clave en la parte
	 * de INSERT INTO y el valor en el lugar correspondiente en la parte de VALUES. Los elementos pk y
	 * nombreSecuencia.NEXTVAL solo se usaran en caso de proveer cadenas no vacias para las variables pk y
	 * nombreSecuencia. Los valores se reinician para acomodar los valores del mapa como unicos valores.
	 *
	 * @param tabla
	 * @param nombreSecuencia
	 * @param pk
	 * @param mapaParametros
	 * @return
	 */
	private SqlExecuter insert(String tabla, String nombreSecuencia, String pk, Map<String, Object> mapaParametros) {
		// numCampos es el numero de valores en el mapa, incrementado en uno mas si hay parametros para nextVal (nombreSecuencia y pk)
		boolean usarNextVal = (StringUtils.isNotEmpty(nombreSecuencia) && StringUtils.isNotEmpty(pk) &&
				gestor == GestorBaseDatos.ORACLE);
		int numCampos = mapaParametros.size() + (usarNextVal ? 1 : 0);
		String[] valoresInsercion = new String[numCampos];
		String[] campos = new String[numCampos];

		int i = 0;
		if (usarNextVal) {
			campos[i] = pk;
			valoresInsercion[i] = SqlBuilder.nextVal(nombreSecuencia);
			i++;
		}
		
		this.clearValues();
		for (Map.Entry<String, Object> entry : mapaParametros.entrySet()) {
			Object valor = entry.getValue();
			if (valor instanceof SqlBuilder) {
				// Se prepara subconsulta para la insercion en caso de que sea un SqlBuilder
				valoresInsercion[i] = SqlBuilder.prepararSubconsulta((SqlBuilder) valor);
			} else {
				// Si no, se introduce un comodin " ? " en el lugar apropiado
				valoresInsercion[i] = SqlBuilder.PARAMETRO;
				this.addValues(valor);
			}
			campos[i] = entry.getKey();
			i++;
		}
		this.insertInto(tabla, campos);
		this.values(valoresInsercion);
		return this;
	}

	/**
	 * Crea una consulta de insercion de la forma "INSERT INTO tabla (campo1, campo2, ... , campoN) VALUES (?1, ?2, ...
	 * , ?N) ", donde cada entrada del mapa tendra, en orden, la clave en la parte de INSERT INTO y el valor en el lugar
	 * correspondiente en la parte de VALUES.
	 *
	 * @param tabla
	 * @param mapaParametros
	 * @return
	 */
	public SqlExecuter insertWithValues(String tabla, Map<String, Object> mapaParametros) {
		return this.insert(tabla, null, null, mapaParametros);
	}

	/**
	 * Crea una consulta de insercion de la forma "INSERT INTO tabla (pk, campo1, campo2, ... , campoN) VALUES
	 * (nombreSecuencia.NEXTVAL, ?1, ?2, ... , ?N) ", donde cada entrada del mapa tendra, en orden, la clave en la parte
	 * de INSERT INTO y el valor en el lugar correspondiente en la parte de VALUES.
	 *
	 * @param tabla
	 * @param nombreSecuencia
	 * @param pk
	 * @param mapaParametros
	 * @return
	 */
	public SqlExecuter insertNextValWithValues(String tabla, String pk, String nombreSecuencia, Map<String, Object> mapaParametros) {
		return this.insert(tabla, nombreSecuencia, pk, mapaParametros);
	}

	/**
	 * Recupera el valor de la lista en el indice dado o un comodín "?" si no existe.
	 *
	 * @param indice
	 * @param lista
	 * @return
	 */
	private static Object getValorOComodin(int indice, List<Object> lista) {
		if (lista.size() > indice) {
			Object obj = lista.get(indice);
			if (obj instanceof String) {
				obj = String.format("'%s'", obj);
			}
			return obj;
		}
		return "?";
	}

	/**
	 * Devuelve la consulta SQL sin sustituir los elementos parametrizados.
	 *
	 * @return
	 */
	private String getSqlParametrizado() {
		return super.toString();
	}

	private List<Object> crearLista() {
		return new ArrayList<Object>();
	}

	private List<Object> crearLista(List<?> objetos) {
		return new ArrayList<Object>(objetos);
	}

	private List<Object> crearLista(Object[] objetos) {
		return new ArrayList<Object>(Arrays.asList(objetos));
	}

}
