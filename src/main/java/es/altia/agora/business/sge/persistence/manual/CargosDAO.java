package es.altia.agora.business.sge.persistence.manual;

import java.util.Vector;
import java.sql.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.*;
import es.altia.util.conexion.*;
import es.altia.agora.business.util.GeneralValueObject;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author sin atribuir
 * @version 1.0
 */

public class CargosDAO{
   //Para el fichero de configuracion tecnico.
   protected static Config conf;
   //Para informacion de logs.
   protected static Log m_Log =
            LogFactory.getLog(CargosDAO.class.getName());

   private static CargosDAO instance = null;

   protected CargosDAO() {
	super();
	//Queremos usar el fichero de configuracion techserver
	conf = ConfigServiceHelper.getConfig("techserver");
   }

   public static CargosDAO getInstance() {
	//si no hay ninguna instancia de esta clase tenemos que crear una.
	if (instance == null) {
	   // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
	   synchronized(CargosDAO.class){
		if (instance == null)
		   instance = new CargosDAO();
	   }
	}
	return instance;
   }

   public Vector eliminarCargo(String cod, String[] params){
	AdaptadorSQLBD abd = null;
	Connection conexion = null;
	Statement stmt = null;
	String sql = "";
	try{
	   //m_Log.debug("A por el OAD");
	   abd = new AdaptadorSQLBD(params);
	   //m_Log.debug("A por la conexion");
	   conexion = abd.getConnection();
	   abd.inicioTransaccion(conexion);
	   stmt = conexion.createStatement();
	   sql = "DELETE FROM E_CAR WHERE " + conf.getString("SQL.E_CAR.cod") +
		   "=" + cod ;
	   //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	   stmt.executeUpdate(sql);
	}
	catch (SQLException sqle){
	   m_Log.error("No se pudo borrar el cargo en eliminarCargo: " +
			 sqle.toString());
	   try{
		abd.rollBack(conexion);
	   }
	   catch(BDException bde){
		m_Log.error("error al ejecutar el rollback en eliminarCargo: " +
			    bde.toString());
	   }
	}
	catch (BDException bde){
	   m_Log.error("error al conectar en eliminarCargo: " + bde.toString());
	}
	finally{
	   if (conexion != null){
		try{
		   abd.finTransaccion(conexion);
		   stmt.close();
		   abd.devolverConexion(conexion);
		}
		catch(SQLException sqle){
		   m_Log.error("Error al cerrar el Statement: " + sqle.toString());
		}
		catch(BDException bde){
		   m_Log.error("Error al devolver la conexion: " + bde.toString());
		}
	   }
	}
	Vector lista = new Vector();
	lista = loadCargos(params);
	return lista;
   }

   public Vector loadCargos(String[] params){
	Vector r = new Vector();
	AdaptadorSQLBD abd = null;
	Connection conexion = null;
	try{
	   //m_Log.debug("A por el OAD");
	   abd = new AdaptadorSQLBD(params);
	   //m_Log.debug("A por la conexion");
	   conexion = abd.getConnection();
	   // Creamos la select con los parametros adecuados.
	   String sql = "SELECT " + conf.getString("SQL.E_CAR.cod") + "," +
	   conf.getString("SQL.E_CAR.desc") + "," +
	   conf.getString("SQL.E_CAR.cargo") + "," +
	   conf.getString("SQL.E_CAR.tratam") + "," +
	   conf.getString("SQL.E_CAR.sit") + " FROM E_CAR ";

	   String[] orden = {conf.getString("SQL.E_CAR.cod"),"1"};
	   sql += abd.orderUnion(orden);
	   if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + sql);
	   Statement stmt = conexion.createStatement();
	   ResultSet rs = stmt.executeQuery(sql);
	   while(rs.next()){
		m_Log.debug("Entro en el bucle");
		GeneralValueObject gVO = new GeneralValueObject();
		gVO.setAtributo("cod", rs.getString(1));
		gVO.setAtributo("desc", rs.getString(2));
		gVO.setAtributo("cargo", rs.getString(3));
		gVO.setAtributo("tratam", rs.getString(4));
		gVO.setAtributo("sit", rs.getString(5));
		r.add(gVO);
	   }
	   rs.close();
	   stmt.close();
	}
	catch (SQLException sqle){
	   m_Log.error("Error de SQL en loadCargos: " + sqle.toString());
	}
	catch (BDException bde){
	   m_Log.error("error del OAD en el metodo loadCargos: " +
				    bde.toString());
	}
	finally {
	   if (conexion != null){
		try{
		   abd.devolverConexion(conexion);
		}
		catch(BDException bde){
		   m_Log.error("No se pudo devolver la conexion: " + bde.toString());
		}
	   }
	}
	return r;
   }

   public Vector modificarCargo(GeneralValueObject gVO, String[] params){
	AdaptadorSQLBD abd = null;
	Connection conexion = null;
	Statement stmt = null;
	String sql = "";
	try{
	   //m_Log.debug("A por el OAD");
	   abd = new AdaptadorSQLBD(params);
	   //m_Log.debug("A por la conexion");
	   conexion = abd.getConnection();
	   conexion.setAutoCommit(false) ;
	   String cod = (String)gVO.getAtributo("cod");
	   String desc = (String)gVO.getAtributo("desc");
	   String cargo = (String)gVO.getAtributo("cargo");
	   String tratam = (String)gVO.getAtributo("tratam");
	   String sit = (String)gVO.getAtributo("sit");
	   sql = "UPDATE E_CAR SET " + conf.getString("SQL.E_CAR.cod") + "=" +
		   cod + "," + conf.getString("SQL.E_CAR.desc") + "='" + desc +
		   "'," + conf.getString("SQL.E_CAR.cargo") + "='" + cargo + "'," +
		   conf.getString("SQL.E_CAR.tratam") + "='" + tratam + "'," +
		   conf.getString("SQL.E_CAR.sit") + "='" + sit + "' WHERE " +
		   conf.getString("SQL.E_CAR.cod") + "=" + cod;
	   //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	   stmt = conexion.createStatement();
	   int res = stmt.executeUpdate(sql);
	   //m_Log.debug("las filas afectadas en el update son : " + res);
	}
	catch(SQLException sqle){
	   m_Log.error("Error al actualizar: " + sqle.toString());
	   try{
		abd.rollBack(conexion);
	   }
	   catch(BDException bde){
		m_Log.error("Imposible hacer el rollback: " + bde.toString());
	   }
	}
	catch (BDException bde) {
	   m_Log.error("Error del OAD: " + bde.toString());
	}
	finally {
	   if(conexion != null){
		try{
		   conexion.commit();
		   stmt.close();
		   conexion.close();
		}
		catch(SQLException sqle) {
		   m_Log.error("Error al cerrar la conexión: " + sqle.toString());
		}
	   }
	}
	Vector lista = new Vector();
	lista = loadCargos(params);
	return lista;
   }

   public Vector altaCargo(GeneralValueObject gVO, String[] params){
	AdaptadorSQLBD abd = null;
	Connection conexion = null;
	Statement stmt = null;
	String sql = "";
	try{
	   //m_Log.debug("A por el OAD");
	   abd = new AdaptadorSQLBD(params);
	   //m_Log.debug("A por la conexion");
	   conexion = abd.getConnection();
	   conexion.setAutoCommit(false) ;
	   stmt = conexion.createStatement();
	   sql = "INSERT INTO E_CAR VALUES(" + (String)gVO.getAtributo("cod") +
		   ", '" + (String)gVO.getAtributo("desc") + "','" +
		   (String)gVO.getAtributo("cargo") + "','" +
		   (String)gVO.getAtributo("tratam") + "','" +
		   (String)gVO.getAtributo("sit") + "')";
	   if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	   int res = stmt.executeUpdate(sql);
	   m_Log.debug("las filas afectadas en el insert son : " + res);
	}
	catch (SQLException sqle){
	   m_Log.error("Error al ejecutar el alta: " + sqle.toString());
	   try{
		abd.rollBack(conexion);
	   }
	   catch(BDException bde){
		m_Log.error("Imposible hacer el rollback: " + bde.toString());
	   }
	}
	catch (BDException bde) {
	   m_Log.error("error del OAD: " + bde.toString());
	}
	finally {
	   if(conexion != null){
		try{
		   conexion.commit();
		   stmt.close();
		   conexion.close();
		}
		catch(SQLException sqle){
		   m_Log.error("Error al cerrar la conexión: " + sqle.toString()) ;
		}
	   }
	}
	Vector lista = new Vector();
	lista = loadCargos(params);
	return lista;
   }

public GeneralValueObject getCargo(GeneralValueObject gVO, String[] params)
{ 
  /* Obtiene la información de un cargo dado su identificador en el atributo "cod" del GeneralValueObject */

	AdaptadorSQLBD abd = null;
	Connection conexion = null;
	try{

	   abd = new AdaptadorSQLBD(params);

	   conexion = abd.getConnection();
	   // Creamos la select con los parametros adecuados.
	   String sql = "SELECT " + conf.getString("SQL.E_CAR.cod") + "," +
	   conf.getString("SQL.E_CAR.desc") + "," +
	   conf.getString("SQL.E_CAR.cargo") + "," +
	   conf.getString("SQL.E_CAR.tratam") + "," +
	   conf.getString("SQL.E_CAR.sit") + " FROM E_CAR "
     + " WHERE " + conf.getString("SQL.E_CAR.cod") + " = " + (String) gVO.getAtributo("cod");
	   String[] orden = {conf.getString("SQL.E_CAR.cod"),"1"};
	   sql += abd.orderUnion(orden);
	   if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + sql);
	   Statement stmt = conexion.createStatement();
	   ResultSet rs = stmt.executeQuery(sql);
	   while(rs.next()){
      gVO.setAtributo("cod", rs.getString(1));
      gVO.setAtributo("desc", rs.getString(2));
      gVO.setAtributo("cargo", rs.getString(3));
      gVO.setAtributo("tratam", rs.getString(4));
      gVO.setAtributo("sit", rs.getString(5));
	   }
	   rs.close();
	   stmt.close();
	}
	catch (SQLException sqle){
	   m_Log.error("Error de SQL en getCargo: " + sqle.toString());
	}
	catch (BDException bde){
	   m_Log.error("error del OAD en el metodo getCargo: " +
				    bde.toString());
	}
	finally {
	   if (conexion != null){
		try{
		   abd.devolverConexion(conexion);
		}
		catch(BDException bde){
		   m_Log.error("No se pudo devolver la conexion: " + bde.toString());
		}
	   }
	}
	return gVO;
   }

}