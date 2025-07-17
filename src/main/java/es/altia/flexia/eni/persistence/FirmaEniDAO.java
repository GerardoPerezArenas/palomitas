package es.altia.flexia.eni.persistence;

import es.altia.flexia.eni.exception.CodigoMensajeEni;
import es.altia.flexia.eni.exception.GestionEniException;
import es.altia.flexia.eni.model.FirmaEni;
import es.altia.util.jdbc.sqlbuilder.QueryResult;
import es.altia.util.jdbc.sqlbuilder.SqlBuilder;
import es.altia.util.jdbc.sqlbuilder.SqlExecuter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class FirmaEniDAO {

	private static FirmaEniDAO instance = null;
	private final Logger log = Logger.getLogger(FirmaEniDAO.class);

	public final static String ID_INS_INTERMEDIA = "ID_INSERCION";
	public final static String ID_FIRMA_INTERMEDIA = "ID_FIRMA_ENI";
	public final static String TABLA_INTERMEDIA = "INTERMEDIA_FIRMAS_ENI";
	/**
	 * *
	 * Instancia del DAO
	 *
	 * @return
	 */
	public static FirmaEniDAO getInstance() {
		if (instance == null) {
			synchronized (FirmaEniDAO.class) {
				if (instance == null) {
					instance = new FirmaEniDAO();
				}
			}
		}
		return instance;
	}

	/**
	 * Recupera las firmas. No se comprueba si hay mas de un resultado porque la base de datos debe tener la restriccion
	 * de unicidad para el id de la firma.
	 *
	 * @param idFirma Identificador de la {@link FirmaEni firma} a recuperar.
	 * @param con Conexion abierta con la base de datos.
	 * @return {@link FirmaEni Firma} recuperada de base de datos 
	 * @throws java.sql.SQLException
	 */
	public FirmaEni recuperarFirmaById(long idFirma, Connection con) throws SQLException {
		SqlBuilder sql = new SqlBuilder().select().from(FirmaEni.TABLA).whereEqualsParametrizado(FirmaEni.ID);
		SqlExecuter sqlExecuter = new SqlExecuter(sql);
		sqlExecuter.setValues(idFirma).logSqlDebug(log);
		QueryResult resultado = sqlExecuter.executeQuery(con);
		FirmaEni firma = null;
		if (resultado.next()) {
			firma = new FirmaEni(resultado.getRow());
		}
		return firma;
	}

	/**
	 * Guarda la firma en base de datos y la devuelve con el id generado.
	 *
	 * @param firma Firma a insertar en la base de datos.
	 * @param con Conexion abierta con la base de datos.
	 * @return {@link FirmaEni Firma} insertada con el identificador generado.
	 * @throws SQLException
	 */
	public FirmaEni insertar(FirmaEni firma, Connection con) throws SQLException {
		Map<String, Object> camposFirma = new HashMap<String, Object>();
		camposFirma.put(FirmaEni.CONTENIDO, firma.getContenido());
		camposFirma.put(FirmaEni.FORMATO_FIRMA_ENI, firma.getFormato().getValor());
		camposFirma.put(FirmaEni.TIPO_FIRMA_ENI, firma.getTipo().getValor());

		SqlExecuter sqlExecuter = new SqlExecuter();
		sqlExecuter.insertNextValWithValues(FirmaEni.TABLA, FirmaEni.ID, FirmaEni.SECUENCIA, camposFirma).logSqlDebug(log);
		int resultadoInsercion = sqlExecuter.executeUpdate(con);

		FirmaEni firmaInsertada = null;
		if (resultadoInsercion > 0) {
			// Si se ha conseguido insertar correctamente, se recupera el id de dicha firma
			SqlBuilder sql = new SqlBuilder().select(SqlBuilder.columnaConAlias(SqlBuilder.max(FirmaEni.ID), "MAX_ID"))
					.from(FirmaEni.TABLA);
			sql.logSqlDebug(log);
			QueryResult resultado = new SqlExecuter(sql).executeQuery(con);
			if (resultado.next()) {
				firmaInsertada = firma;
				firmaInsertada.setId(resultado.getInteger("MAX_ID"));
			} else {
				log.error("No se ha encontrado ninguna coincidencia");
				throw new SQLException(String.format("No se ha encontrado ninguna coincidencia para la query: %s", 
						sql.toString()));
			}
		}

		return firmaInsertada;
	}

	/**
	 * Inserta una fila en la tabla intermedia INTERMEDIA_FIRMAS_ENI y devuelve el ultimo codigo de insercion, 
	 * producido en este metodo en caso de que idInsercion venga como null o 0.
	 * 
	 * @param idInsercion Identificador de la insercion con el que se relaciona el idFirma. Si viene a null o 0 se 
	 * genera en este metodo.
	 * @param idFirma Identificador de la firma con la que estara relacionada el idInsercion. La firma debe haber sido
	 * insertada en la tabla correspondiente y el idFirma debe reflejar la pk de la misma.
	 * @param con Conexion abierta con la base de datos.
	 * @return idInsercion una vez generado o sin alterar en caso de que venga cubierto.
	 * @throws java.sql.SQLException 
	 */
	public int insertarFilaTablaIntermedia(Integer idInsercion, long idFirma, Connection con) throws SQLException {
		
		SqlBuilder sql = new SqlBuilder();
		SqlExecuter sqlExecuter;
		
		if (idInsercion == null || idInsercion == 0) {
			final String ALIAS = "MAX_INS";
			// Se recupera el ultimo identificador de insercion de la tabla
			sql.select(SqlBuilder.columnaConAlias(SqlBuilder.coalesce(SqlBuilder.max(ID_INS_INTERMEDIA), "0"), ALIAS))
					.from(TABLA_INTERMEDIA);
			sqlExecuter = new SqlExecuter(sql);
			sqlExecuter.logSqlDebug(log);
			QueryResult resultado = sqlExecuter.executeQuery(con);
			if (resultado.next()) {
				// idInsercion es el id nuevo que se va a insertar
				idInsercion = resultado.getInteger(ALIAS) + 1;
			} else {
				log.error("No se ha encontrado ninguna coincidencia");
				throw new SQLException(String.format("No se ha encontrado ninguna coincidencia para la query: %s",
						sql.toString()));
			}
		}
		
		// Crear fila. idInsercion siempre llega con un valor mayor que 0 a este punto
		sql.insertIntoParametrizado(TABLA_INTERMEDIA, ID_INS_INTERMEDIA, ID_FIRMA_INTERMEDIA);
		sqlExecuter = new SqlExecuter(sql);
		sqlExecuter.setValues(idInsercion, idFirma).logSqlDebug(log);
		if (sqlExecuter.executeUpdate(con) < 1) {
			String mensajeError = String.format("No se ha podido insertar la fila de valor [%s: %d, %s, %d] en %s", 
					ID_INS_INTERMEDIA, idInsercion, ID_FIRMA_INTERMEDIA, idFirma, TABLA_INTERMEDIA);
			log.error(mensajeError);
			throw new SQLException(mensajeError);
		}
		return idInsercion;
	}
	

}
