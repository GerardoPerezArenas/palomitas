package es.altia.agora.business.util.persistence.manual;

import java.util.Vector;
import es.altia.util.conexion.*;
import java.sql.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Manuel Vera Silvestre
 * @version 1.0
 */

public class ValidarDocumentoDAO {

   private static ValidarDocumentoDAO instance = null;
   private Config m_ConfigTechnical;
   private String codigo;
   private String grupo1;
   private String tipo1;
   private String grupo2;
   private String tipo2;
   private String grupo3;
   private String tipo3;
   private String grupo4;
   private String tipo4;
   private String grupo5;
   private String tipo5;
   private String valAso;
   private String nombre;
   //Para informacion de logs.
   protected static Log m_Log =
            LogFactory.getLog(ValidarDocumentoDAO.class.getName());


   private ValidarDocumentoDAO(){
	m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
	codigo = m_ConfigTechnical.getString("SQL.T_TID.codigo");
	grupo1 = m_ConfigTechnical.getString("SQL.T_TID.grupo1");
	tipo1 = m_ConfigTechnical.getString("SQL.T_TID.tipo1");
	grupo2 = m_ConfigTechnical.getString("SQL.T_TID.grupo2");
	tipo2 = m_ConfigTechnical.getString("SQL.T_TID.tipo2");
	grupo3 = m_ConfigTechnical.getString("SQL.T_TID.grupo3");
	tipo3 = m_ConfigTechnical.getString("SQL.T_TID.tipo3");
	grupo4 = m_ConfigTechnical.getString("SQL.T_TID.grupo4");
	tipo4 = m_ConfigTechnical.getString("SQL.T_TID.tipo4");
	grupo5 = m_ConfigTechnical.getString("SQL.T_TID.grupo5");
	tipo5 = m_ConfigTechnical.getString("SQL.T_TID.tipo5");
	valAso = m_ConfigTechnical.getString("SQL.T_TID.valAso");
	nombre = m_ConfigTechnical.getString("SQL.T_TID.nombre");
   }

   /**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de MenuManager
    */
   public static ValidarDocumentoDAO getInstance() {
	//Si no hay una instancia de esta clase tenemos que crear una.
	if (instance == null) {
	   // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
	   synchronized(ValidarDocumentoDAO.class) {
		if (instance == null)
		   instance = new ValidarDocumentoDAO();
	   }
	}
	return instance;
   }

   public Vector load(String params[]){
	Vector v = new Vector();
	AdaptadorSQLBD aod = null;
	Connection conexion = null;
	try{
	   aod = new AdaptadorSQLBD(params);
	   conexion = aod.getConnection();
	   Statement stmt = conexion.createStatement();
	   String[] pNulo = {grupo2,"0"};
	   String sql = "Select " + codigo + "," + grupo1 + "," + tipo1 + "," +
			    aod.funcionSistema(aod.FUNCIONSISTEMA_NVL,pNulo) + "," +
			    tipo2 + ",";
	   pNulo[0] = grupo3;
	   sql += aod.funcionSistema(aod.FUNCIONSISTEMA_NVL,pNulo) + "," + tipo3 +
		    ",";
	   pNulo[0] = grupo4;
	   sql += aod.funcionSistema(aod.FUNCIONSISTEMA_NVL,pNulo) + "," + tipo4 +
		    ",";
	   pNulo[0] = grupo5;
	   sql += aod.funcionSistema(aod.FUNCIONSISTEMA_NVL,pNulo) + "," + tipo5 +
		    ",";
	   pNulo[0] = valAso;
	   pNulo[1] = "''";
	   sql += aod.funcionSistema(aod.FUNCIONSISTEMA_NVL,pNulo)+" from T_TID where " + 
	          nombre + "='N.I.F.' OR " + nombre + "='SIN DOCUMENTO' " + " OR " + nombre + 
	          "='SEN DOCUMENTO' OR " + nombre + "='C.I.F.'";	          
	   if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + sql);
	   ResultSet rs = stmt.executeQuery(sql);
	   while(rs.next()){
		String[] aux = new String[12];
		for(int i=0;i<12;i++) {
		   aux[i] = rs.getString(i+1);//Resultset empieza en 1 no en 0
		}
		v.add(aux);
	   }
	}
	catch(BDException bde){
	   m_Log.error("Error del OAD en método load");
	   bde.printStackTrace();
	}
	catch(SQLException sqle){
	   m_Log.error("Error de SQL en método load");
	   sqle.printStackTrace();
	}
	catch(Exception e){
	   m_Log.error("Error inesperado en método load");
	   e.printStackTrace();
	}
	finally{
	   if(conexion != null){
		try{
		   aod.devolverConexion(conexion);
		}
		catch(BDException bde){
		   m_Log.error("Error al cerrar la conexion");
		   bde.printStackTrace();
		}
	   }
	}
	return v;
   }
}