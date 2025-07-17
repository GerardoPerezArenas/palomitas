package es.altia.flexia.sir.persistence;

import es.altia.flexia.sir.exception.GestionSirException;
import es.altia.flexia.sir.model.Anexo;
import es.altia.flexia.sir.model.Asiento;
import es.altia.flexia.sir.model.Domicilio;
import es.altia.flexia.sir.model.Interesado;
import es.altia.flexia.sir.exception.CodigoMensajeSir;
import es.altia.flexia.sir.model.EstadoAsiento;
import es.altia.flexia.sir.model.Intercambio;
import es.altia.util.jdbc.sqlbuilder.QueryResult;
import es.altia.util.jdbc.sqlbuilder.RowResult;
import es.altia.util.jdbc.sqlbuilder.SqlBuilder;
import es.altia.util.jdbc.sqlbuilder.SqlExecuter;
import java.sql.Clob;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 * Reune metodos que interactuan con la DB y estan directamente relacionados con la gestion de los procesos que soportan
 * el SIR en Flexia.
 */
public class GestionSirDAO {

	private static volatile GestionSirDAO instance = null;
	private final Logger LOGGER = Logger.getLogger(GestionSirDAO.class);

	/**
	 * Factory method para el <code>Singleton</code>.
	 *
	 * @return La unica instancia de GestionSirDAO
	 */
	public static GestionSirDAO getInstance() {
		GestionSirDAO dao = GestionSirDAO.instance;
		//Si no hay una instancia de esta clase tenemos que crear una.
		if (dao == null) {
			// Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
			synchronized (GestionSirDAO.class) {
				dao = GestionSirDAO.instance;
				if (dao == null) {
					GestionSirDAO.instance = dao = new GestionSirDAO();
				}
			}
		}
		return dao;
	}

	private GestionSirDAO() {

	}

	/**
	 * Recupera de la base de datos los asientos cuyo estado pertenezca a uno del filtroEstados.
	 *
	 * @param filtroEstados Lista de {@link EstadoAsiento} de los {@link Intercambio intercambios} que seran recuperados.
	 * @param conexion Conexion a la base de datos
	 * @return
	 * @throws SQLException
	 */
	public List<Intercambio> buscarIntercambiosByEstadoAsiento(List<EstadoAsiento> filtroEstados, Connection conexion) throws SQLException {

		SqlBuilder sql = new SqlBuilder().select().from("INTERCAMBIO_SIR")
				.innerEquiJoin("ASIENTO_SIR", "INTERCAMBIO_SIR.COD_ASIENTO", "ASIENTO_SIR.COD_ASIENTO")
				.whereInParametrizado("ESTADO", filtroEstados)
				.orderBy(SqlBuilder.desc("ASIENTO_SIR.FECHA_ENTRADA"));
		QueryResult resultado;

		SqlExecuter consulta = new SqlExecuter(sql);
		for (EstadoAsiento estado : filtroEstados) {
			consulta.addValues(estado.getValor());
		}
		consulta.logSqlDebug(LOGGER);

		resultado = consulta.executeQuery(conexion);
		List<Intercambio> intercambios = new ArrayList<Intercambio>();
		while (resultado.next()) {
			RowResult row = resultado.getRow();
			Intercambio intercambio = new Intercambio(row);
			Asiento asiento = new Asiento(row);
			asiento.setAnexos(buscarAnexosByCodAsiento(asiento.getCodAsiento(), conexion));
			asiento.setInteresados(buscarInteresadosByCodAsiento(asiento.getCodAsiento(), conexion));
			intercambio.setAsiento(asiento);
			intercambios.add(intercambio);
		}
		return intercambios;
	}

	/**
	 * Recupera de la base de datos {@link Intercambio} relacionado con el codIntercambio recibido como por parametro.
	 *
	 * @param conexion Conexion a la base de datos.
	 * @param codIntercambio Codigo del asiento a recuperar
	 * @return El {@link Intercambio} completo.
	 * @throws SQLException
	 * @throws GestionSirException
	 */
	public Intercambio buscarIntercambio(String codIntercambio, Connection conexion) throws SQLException, GestionSirException {
		SqlBuilder sql = new SqlBuilder()
				.select().from("INTERCAMBIO_SIR")
				.innerEquiJoin("ASIENTO_SIR", "INTERCAMBIO_SIR.COD_ASIENTO", "ASIENTO_SIR.COD_ASIENTO")
				.whereEqualsParametrizado("COD_INTERCAMBIO");
		QueryResult resultado;
		SqlExecuter consulta = new SqlExecuter(sql);
		consulta.setValues(codIntercambio).logSqlDebug(LOGGER);

		resultado = consulta.executeQuery(conexion);
		if (resultado.next()) {
			RowResult row = resultado.getRow();
			Intercambio intercambio = new Intercambio(row);
			Asiento asiento = new Asiento(row);
			asiento.setAnexos(buscarAnexosByCodAsiento(asiento.getCodAsiento(), conexion));
			asiento.setInteresados(buscarInteresadosByCodAsiento(asiento.getCodAsiento(), conexion));
			intercambio.setAsiento(asiento);
			return intercambio;
		} else {
			throw new GestionSirException(CodigoMensajeSir.ASIENTO_NO_ENCONTRADO.getCodigo(),
					"No ha habido coincidencias para la búsqueda de registro", codIntercambio);
		}
	}

	/**
	 * Recupera los {@link Anexo anexos} relacionados con el codAsiento recibido como parametro.
	 *
	 * @param codAsiento Codigo del {@link Asiento} al que pertenecen los {@link Anexo anexos}.
	 * @param conexion Conexion a la base de datos.
	 * @return Los {@link Anexo anexos} relacionados con el codAsiento.
	 * @throws SQLException
	 */
	public List<Anexo> buscarAnexosByCodAsiento(int codAsiento, Connection conexion) throws SQLException {
		SqlBuilder sql = new SqlBuilder().select().from("ANEXO_SIR").whereEqualsParametrizado("COD_ASIENTO");
		QueryResult resultado;
		SqlExecuter consulta = new SqlExecuter(sql);
		consulta.setValues(codAsiento).logSqlDebug(LOGGER);

		resultado = consulta.executeQuery(conexion);
		List<Anexo> anexos = new ArrayList<Anexo>();
		while (resultado.next()) {
			anexos.add(new Anexo(resultado.getRow()));
		}
		return anexos;
	}

	/**
	 * Recupera los {@link Interesado interesados} relacionados con el codAsiento recibido como parametro.
	 *
	 * @param codAsiento Codigo del {@link Asiento} al que pertenecen los {@link Interesado interesados}.
	 * @param conexion Conexion a la base de datos.
	 * @return Los {@link Interesado interesados} relacionados con el codAsiento, incluyendo los
	 * {@link Domicilio domicilios}.
	 * @throws SQLException
	 */
	public List<Interesado> buscarInteresadosByCodAsiento(int codAsiento, Connection conexion) throws SQLException {
		SqlBuilder sql = new SqlBuilder().select().from("INTERESADO_SIR")
				.innerEquiJoin("DOMICILIO_SIR", "INTERESADO_SIR.COD_DOMICILIO", "DOMICILIO_SIR.COD_DOMICILIO")
				.whereEqualsParametrizado("COD_ASIENTO");
		QueryResult resultado;
		SqlExecuter consulta = new SqlExecuter(sql);
		consulta.setValues(codAsiento).logSqlDebug(LOGGER);

		resultado = consulta.executeQuery(conexion);
		List<Interesado> interesados = new ArrayList<Interesado>();
		while (resultado.next()) {
			interesados.add(new Interesado(resultado.getRow()));
		}
		return interesados;
	}

	/**
	 * Metodo para rechazar un asiento. Se actualiza un asiento con la informacion que indica que a sigo rechazado
	 *
	 * @param sqlExecuter Query para actualizar los datos del {@link Intercambio}.
	 * @param conexion Conexion a la base de datos.
	 * @throws SQLException
	 * @throws GestionSirException
	 */
	public void actualizarIntercambio(SqlExecuter sqlExecuter, Connection conexion) throws SQLException, GestionSirException {
		sqlExecuter.logSqlDebug(LOGGER);
		int resultado = sqlExecuter.executeUpdate(conexion);
		LOGGER.debug(String.format("%s filas actualizadas", resultado));
	}

	/**
	 * Elimina los {@link Domicilio domicilios} de un {@link Asiento}. Es necesario que los domicilios esten 
	 * relacionados con los {@link Interesado interesados} a los que pertenecen.
	 * 
	 * @param codAsiento Codigo del {@link Asiento} cuyos {@link Domicilio domicilios} seran eliminados.
	 * @param conexion Conexion a la base de datos.
	 * @throws java.sql.SQLException
	 */
	public void borrarDomiciliosByAsiento(int codAsiento, Connection conexion) throws SQLException{
		SqlBuilder subSql = new SqlBuilder().select("COD_DOMICILIO").from("INTERESADO_SIR")
				.whereEqualsParametrizado("COD_ASIENTO");
		SqlBuilder sql = new SqlBuilder().delete().from("DOMICILIO_SIR")
				.whereIn("COD_DOMICILIO", subSql);
		SqlExecuter consulta = new SqlExecuter(sql);
		consulta.setValues(codAsiento).logSqlDebug(LOGGER);
		consulta.executeUpdate(conexion);
	}
	
	/**
	 * Elimina los {@link Interesado interesados} de un {@link Asiento}.
	 *
	 * @param codAsiento Codigo del {@link Asiento} cuyos {@link Interesado interesados} seran eliminados.
	 * @param conexion Conexion a la base de datos.
	 * @throws java.sql.SQLException
	 */
	public void borrarInteresadosByAsiento(int codAsiento, Connection conexion) throws SQLException {
		borrarByAsiento("INTERESADO_SIR", codAsiento, conexion);
	}

	/**
	 * Elimina los {@link Anexo anexos} de un {@link Asiento}.
	 *
	 * @param codAsiento Codigo del {@link Asiento} cuyos {@link Anexo anexos} seran eliminados.
	 * @param conexion Conexion a la base de datos.
	 * @throws java.sql.SQLException
	 */
	public void borrarAnexosByAsiento(int codAsiento, Connection conexion) throws SQLException {
		borrarByAsiento("ANEXO_SIR", codAsiento, conexion);
	}
	
	/**
	 * Elimina el {@link Asiento}.
	 *
	 * @param codAsiento Codigo del {@link Asiento} que sera eliminado.
	 * @param conexion Conexion a la base de datos.
	 * @throws java.sql.SQLException
	 */
	public void borrarAsiento(int codAsiento, Connection conexion) throws SQLException {
		borrarByAsiento("ASIENTO_SIR", codAsiento, conexion);
	}
	
	/**
	 * Elimina las filas de la tabla dada que cumplan COD_ASIENTO = codAsiento.
	 * 
	 * @param tabla Tabla cuyas filas seran eliminadas.
	 * @param codAsiento Codigo del {@link Asiento} cuyos datos seran eliminados.
	 * @param conexion Conexion a la base de datos.
	 * @throws SQLException 
	 */
	private void borrarByAsiento(String tabla, int codAsiento, Connection conexion) throws SQLException {
		SqlBuilder sql = new SqlBuilder().delete().from(tabla).whereEqualsParametrizado("COD_ASIENTO");
		SqlExecuter consulta = new SqlExecuter(sql);
		consulta.setValues(codAsiento).logSqlDebug(LOGGER);
		consulta.executeUpdate(conexion);
	}

	/**
	 * Metodo que ejecuta la query de insertado de Justificante
	 *
	 * @param conexion Conexion a base de datos
	 * @param sqlExecuter SQL lista para ejecutarse
	 * @throws SQLException
	 */
	public void insertarJustificante(Connection conexion, SqlExecuter sqlExecuter) throws SQLException {

		PreparedStatement pst = null;

		sqlExecuter.logSqlDebug(LOGGER);
		try {
			sqlExecuter.executeUpdate(conexion);
		} catch (SQLException e) {
			throw (e);
		} finally {
			if (pst != null) {
				pst.close();
			}
		}
	}

	/**
	 * Recupera el contenido de un fichero de intercambio de la base de datos.
	 *
	 * @param codIntercambio Codigo del {@link Intercambio} cuyo fichero XML se va a recuperar.
	 * @param conexion Conexion a la base de datos
	 * @return
	 * @throws SQLException
	 */
	public String cargarFicheroIntercambio(String codIntercambio, Connection conexion) throws SQLException {
		SqlBuilder sql = new SqlBuilder().select("FICHERO_INTERCAMBIO").from("INTERCAMBIO_SIR")
				.whereEqualsParametrizado("COD_INTERCAMBIO");
		SqlExecuter consulta = new SqlExecuter(sql).setValues(codIntercambio);
		consulta.logSqlDebug(LOGGER);
		QueryResult resultado = consulta.executeQuery(conexion);
		resultado.next();
		return resultado.getClobString("FICHERO_INTERCAMBIO");
	}

}
