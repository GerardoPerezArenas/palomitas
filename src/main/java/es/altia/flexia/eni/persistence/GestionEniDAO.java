/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.eni.persistence;

import es.altia.util.jdbc.sqlbuilder.QueryResult;
import es.altia.util.jdbc.sqlbuilder.SqlBuilder;
import es.altia.util.jdbc.sqlbuilder.SqlExecuter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Kevin
 */
public class GestionEniDAO {

	private static GestionEniDAO instance = null;
	private final Logger log = Logger.getLogger(GestionEniDAO.class);
	
	/**
	 * *
	 * Instancia del DAO
	 *
	 * @return
	 */
	public static GestionEniDAO getInstance() {
		if (instance == null) {
			synchronized (GestionEniDAO.class) {
				if (instance == null) {
					instance = new GestionEniDAO();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Metodo que ejecuta las queries para recuperar los documentos en base de
	 * datos.
	 *
	 * @param con Conexion a base de datos
	 * @param sqlBuilder SQL lista para ejecutarse
	 * @return
	 */
	public QueryResult executeQuery(Connection con, SqlBuilder sqlBuilder) {
		QueryResult result = null;
		try {
			result = new SqlExecuter(sqlBuilder).executeQuery(con);
		} catch (SQLException ex) {
			log.error("Error al recuperar el Tercero: " + ex);
		}
		return result;
	}
	
	/**
	 * Metodo que ejecuta las queries para insertar los documentos en base de
	 * datos.
	 *
	 * @param conexion Conexion a base de datos
	 * @param sqlExecuter SQL lista para ejecutarse
	 * @throws SQLException
	 */
	public void insertarQuery(Connection conexion, SqlExecuter sqlExecuter) throws SQLException {
		PreparedStatement pst = null;

		sqlExecuter.logSqlDebug(log);
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
	 * Recupera los interesados de version más actual y cuyo documento de identificacion sea uno de los que esta en el
	 * parametro de entrada.
	 * 
	 * @param docsInteresados
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	public QueryResult getInteresadosByListaDocumentos(List<String> docsInteresados, Connection con) throws SQLException {
		SqlBuilder subconsulta = new SqlBuilder()
				.select(SqlBuilder.max("TER_NVE"))
				.from(SqlBuilder.tablaConAlias("T_TER", "ter_b"))
				.whereEquals("ter_a.TER_DOC", "ter_b.TER_DOC");
		
		SqlBuilder sql = new SqlBuilder()
				.select()
				.from(SqlBuilder.tablaConAlias("T_TER", "ter_a"))
				.whereInParametrizado("TER_DOC", docsInteresados)
				.andEquals("TER_NVE", subconsulta);
		
		SqlExecuter consulta = new SqlExecuter(sql);
		consulta.setValues(docsInteresados).logSqlDebug(log);
		QueryResult queryResult = consulta.executeQuery(con);
		
		return queryResult;
	}
}
