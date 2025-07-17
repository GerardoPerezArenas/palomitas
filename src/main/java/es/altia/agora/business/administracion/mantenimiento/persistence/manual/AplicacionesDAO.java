// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.ParametrosBDVO;
import java.util.Vector;
import java.sql.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.*;
import es.altia.util.conexion.*;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.util.cache.CacheDatosFactoria;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: AplicacionesesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class AplicacionesDAO  {
  private static AplicacionesDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(AplicacionesDAO.class.getName());

  protected AplicacionesDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static AplicacionesDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (AplicacionesDAO.class) {
        if (instance == null) {
          instance = new AplicacionesDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarAplicacion(String codigo, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    String sql2 = "";
    try{
      m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      m_Log.debug("A por la conexion");
     
      stmt = conexion.createStatement();

      sql = "DELETE FROM A_AAU WHERE " +
      campos.getString("SQL.A_AAU.aplicacion")+"="+codigo;
      stmt.executeUpdate(sql);
      stmt.close();
      stmt = conexion.createStatement();
      sql = "DELETE FROM A_AID WHERE " +
      campos.getString("SQL.A_AID.aplicacion")+"="+codigo;
      stmt.executeUpdate(sql);
      stmt.close();

      stmt = conexion.createStatement();
      sql = "DELETE FROM A_EEA WHERE " +
      campos.getString("SQL.A_EEA.aplicacion")+"="+codigo;
      stmt.executeUpdate(sql);
      stmt.close();
      SortedMap <ArrayList<String>,ParametrosBDVO> parametrosBD = (SortedMap <ArrayList<String>,ParametrosBDVO>) CacheDatosFactoria.getImplParametrosBD().getDatos();
      for(Map.Entry<ArrayList<String>,ParametrosBDVO> entry : parametrosBD.entrySet()) {
          ParametrosBDVO organizacion = entry.getValue();
          if (codigo.equals(organizacion.getCodAplicacion())){
              String strOrg = String.valueOf(organizacion.getCodOrganizacion());
              String strEnt = String.valueOf(organizacion.getCodEntidad());
              String strApl = String.valueOf(organizacion.getCodAplicacion());
              
              CacheDatosFactoria.getImplParametrosBD().eliminarDatoClaveUnica(strOrg,strEnt,strApl);
           }
       }

      
      stmt = conexion.createStatement();
      sql = "DELETE FROM A_MOR WHERE " +
      campos.getString("SQL.A_MOR.aplicacion")+"="+codigo;
      stmt.executeUpdate(sql);
      stmt.close();
      stmt = conexion.createStatement();
      sql = "DELETE FROM A_MNU WHERE " +
      campos.getString("SQL.A_MNU.aplicacion")+"="+codigo;
      stmt.executeUpdate(sql);
      stmt.close();
      stmt = conexion.createStatement();
      sql = "DELETE FROM A_PID WHERE " +
      campos.getString("SQL.A_PID.aplicacion")+"="+codigo;
      stmt.executeUpdate(sql);
      stmt.close();
      stmt = conexion.createStatement();
      sql = "DELETE FROM A_RPG WHERE " +
      campos.getString("SQL.A_RPG.aplicacion")+"="+codigo;
      stmt.executeUpdate(sql);
      stmt.close();
      stmt = conexion.createStatement();
      sql = "DELETE FROM A_RPU WHERE " +
      campos.getString("SQL.A_RPU.aplicacion")+"="+codigo;
      stmt.executeUpdate(sql);
      stmt.close();
      stmt = conexion.createStatement();
      sql = "DELETE FROM A_TEX WHERE " +
      campos.getString("SQL.A_TEX.codApli")+"="+codigo;
      stmt.executeUpdate(sql);
      stmt.close();
      stmt = conexion.createStatement();
      sql = "DELETE FROM A_UAE WHERE " +
      campos.getString("SQL.A_UAE.aplicacion")+"="+codigo;
      stmt.executeUpdate(sql);
      stmt.close();
      stmt = conexion.createStatement();
      sql = "DELETE FROM A_UGO WHERE " +
      campos.getString("SQL.A_UGO.aplicacion")+"="+codigo;
      stmt.executeUpdate(sql);
      stmt.close();
      stmt = conexion.createStatement();
      sql = "DELETE FROM A_PRO WHERE " +
      campos.getString("SQL.A_PRO.aplicacion")+"="+codigo;
      stmt.executeUpdate(sql);
      stmt.close();
      stmt = conexion.createStatement();
      sql = "DELETE FROM A_AAE WHERE " +
      campos.getString("SQL.A_AAE.aplicacion")+"="+codigo;
      stmt.executeUpdate(sql);
      stmt.close();
      stmt = conexion.createStatement();
      
      sql ="DELETE FROM " + GlobalNames.ESQUEMA_GENERICO + "A_AID WHERE AID_APL=" + codigo;

      stmt.executeUpdate(sql);
      
      stmt = conexion.createStatement();      
      
      sql = "DELETE FROM A_APL WHERE " +
            campos.getString("SQL.A_APL.codigo")+"="+codigo;
      stmt.executeUpdate(sql);
      

      stmt.close();

      if(m_Log.isDebugEnabled()){
    	  m_Log.debug(sql);
    
      }
     
      m_Log.debug("********************Cierro la bbdd ******");
    }catch (Exception e){
        e.printStackTrace();
   	 	rollBackTransaction(abd,conexion,e);
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
        try{
        
            abd.finTransaccion(conexion);
            abd.devolverConexion(conexion);
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
    Vector lista = new Vector();
    lista = getListaAplicaciones(params);
    return lista;
  }

  public Vector getListaAplicaciones(String[] params){
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      // Creamos la select con los parametros adecuados.
      sql = "SELECT * FROM " + GlobalNames.ESQUEMA_GENERICO + "A_APL ";
      String[] orden = {campos.getString("SQL.A_APL.codigo"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.A_APL.codigo")));
        gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.A_APL.nombre")));
        gVO.setAtributo("ejecutable",rs.getString(campos.getString("SQL.A_APL.url")));
        gVO.setAtributo("icono",rs.getString(campos.getString("SQL.A_APL.icono")));
        gVO.setAtributo("seguridad",rs.getString(campos.getString("SQL.A_APL.seguridad")));
        gVO.setAtributo("accesoDefecto",rs.getString(campos.getString("SQL.A_APL.accesoDefecto")));
        gVO.setAtributo("diccionario",rs.getString(campos.getString("SQL.A_APL.diccionario")));
        gVO.setAtributo("informes",rs.getString(campos.getString("SQL.A_APL.informes")));
        gVO.setAtributo("version",rs.getString(campos.getString("SQL.A_APL.version")));
        gVO.setAtributo("conEntidades",rs.getString(campos.getString("SQL.A_APL.conEntidades")));
        gVO.setAtributo("conEjercicios",rs.getString(campos.getString("SQL.A_APL.conEjercicios")));
        //Añadir un campo vector que almacene los títulos
        gVO.setAtributo("titulos", getTitulosAplicacion(rs.getString(campos.getString("SQL.A_APL.codigo")),conexion));
        resultado.add(gVO);
      }
      rs.close();
      stmt.close();
    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
        try{
            abd.devolverConexion(conexion);
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
    return resultado;
  }
  
  private Vector getTitulosAplicacion(String codApl, Connection conexion) {
	  Vector lista = new Vector();

	  PreparedStatement ps = null;
	  ResultSet rs = null;
	  
	  try {
		String sql ="SELECT * FROM " + GlobalNames.ESQUEMA_GENERICO + "A_AID," + GlobalNames.ESQUEMA_GENERICO +
		"A_IDI WHERE AID_IDI=IDI_COD AND AID_APL=" + codApl;
		ps = conexion.prepareStatement(sql);
		rs = ps.executeQuery();
		m_Log.debug("getTitulosAplicacion == " + sql);
		
		while (rs.next()){
			GeneralValueObject gVO = new GeneralValueObject();
			gVO.setAtributo("titulo", rs.getString("AID_TEX"));
			gVO.setAtributo("codIdioma", String.valueOf(rs.getInt("AID_IDI")));
			gVO.setAtributo("desIdioma", rs.getString("IDI_NOM"));
			lista.add(gVO);
		}
		return lista;
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
    
	  return lista;
}
  
  
public Vector getListaAplicaciones(GeneralValueObject g,String[] params){
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    String codOrganizacion = (String) g.getAtributo("codOrganizacion");
    String codEntidad = (String) g.getAtributo("codEntidad");
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      // Creamos la select con los parametros adecuados.
      sql = "SELECT " + campos.getString("SQL.A_APL.codigo") + "," + 
            campos.getString("SQL.A_APL.nombre") + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_AAE, " +
            GlobalNames.ESQUEMA_GENERICO + "A_APL WHERE " +
            campos.getString("SQL.A_AAE.organizacion") + "=" + codOrganizacion + " AND " +  
            campos.getString("SQL.A_AAE.entidad") + "=" + codEntidad + " AND " +
            GlobalNames.ESQUEMA_GENERICO + "a_aae." + campos.getString("SQL.A_AAE.aplicacion") + "=" +
            GlobalNames.ESQUEMA_GENERICO + "a_apl." + campos.getString("SQL.A_APL.codigo");
      String[] orden = {campos.getString("SQL.A_APL.codigo"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.A_APL.codigo")));
        gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.A_APL.nombre")));
        resultado.add(gVO);
      }
      rs.close();
      stmt.close();
    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
        try{
            abd.devolverConexion(conexion);
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
    return resultado;
  }


  public Vector modificarAplicacion(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      sql = "UPDATE A_APL SET " +
        campos.getString("SQL.A_APL.codigo")+"="+gVO.getAtributo("codigo")+","+ 
        campos.getString("SQL.A_APL.nombre")+"='"+gVO.getAtributo("descripcion")+"',"+ 
        campos.getString("SQL.A_APL.url")+"='"+gVO.getAtributo("ejecutable")+"',"+
        campos.getString("SQL.A_APL.icono")+"='"+gVO.getAtributo("icono")+"',"+
        campos.getString("SQL.A_APL.seguridad")+"="+gVO.getAtributo("seguridad")+","+ 
        campos.getString("SQL.A_APL.accesoDefecto")+"="+gVO.getAtributo("accesoDefecto")+","+ 
        campos.getString("SQL.A_APL.diccionario")+"='"+gVO.getAtributo("diccionario")+"',"+ 
        campos.getString("SQL.A_APL.informes")+"='"+gVO.getAtributo("informes")+"',"+ 
        campos.getString("SQL.A_APL.version")+"='"+gVO.getAtributo("version")+"',"+ 
        campos.getString("SQL.A_APL.conEntidades")+"="+gVO.getAtributo("conEntidades")+","+ 
        campos.getString("SQL.A_APL.conEjercicios")+"="+gVO.getAtributo("conEjercicios")+ 
        " WHERE " +
        campos.getString("SQL.A_APL.codigo")+"="+gVO.getAtributo("codigoAntiguo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      int res = stmt.executeUpdate(sql);
      stmt.close();
      
      //titulos de la aplicación
      res = modificarTitulosAplicacion(gVO, conexion);
      //m_Log.debug("las filas afectadas en el update son : " + res);
    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
        try{
            abd.devolverConexion(conexion);
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
    Vector lista = new Vector();
    lista = getListaAplicaciones(params);
    return lista;
  }
  
  
  private int modificarTitulosAplicacion(GeneralValueObject gVO, Connection conexion) {
	  int resultado = 0;
	  PreparedStatement ps = null;
	  String codigoApl = (String) gVO.getAtributo("codigo");
	  Vector titulos = (Vector) gVO.getAtributo("titulos");
	  Vector element = new Vector();
	  
	  try {
		  
		String sql ="DELETE FROM " + GlobalNames.ESQUEMA_GENERICO + "A_AID WHERE AID_APL=" + codigoApl ;
		ps = conexion.prepareStatement(sql);
		ps.executeUpdate();
		m_Log.debug("modificarTitulosAplicacion eliminacion== " + sql);
		
		for(int i=0;i<titulos.size();i++){
			element = (Vector) titulos.elementAt(i);
			sql ="INSERT INTO A_AID (AID_APL, AID_IDI, AID_TEX) VALUES (" + codigoApl + "," +
					element.elementAt(0) + ",'" + element.elementAt(1) + "')";
			m_Log.debug("modificarTitulosAplicacion insercion == " + sql);
			ps = conexion.prepareStatement(sql);
			resultado = ps.executeUpdate();
		}

		
	} catch (SQLException e) {
		resultado = -1;
		e.printStackTrace();
	}
    return resultado;

}
  

  public Vector altaAplicacion(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      sql = "INSERT INTO A_APL("+
        campos.getString("SQL.A_APL.codigo")+","+
        campos.getString("SQL.A_APL.nombre")+","+
        campos.getString("SQL.A_APL.url")+","+
        campos.getString("SQL.A_APL.icono")+","+
        campos.getString("SQL.A_APL.seguridad")+","+
        campos.getString("SQL.A_APL.accesoDefecto")+","+
        campos.getString("SQL.A_APL.diccionario")+","+
        campos.getString("SQL.A_APL.informes")+","+
        campos.getString("SQL.A_APL.version")+","+
        campos.getString("SQL.A_APL.conEntidades")+","+
        campos.getString("SQL.A_APL.conEjercicios")+
        ") VALUES (" + 
        gVO.getAtributo("codigo")+",'"+ 
        gVO.getAtributo("descripcion") + "','"+
        gVO.getAtributo("ejecutable")+"','"+
        gVO.getAtributo("icono")+"',"+
        gVO.getAtributo("seguridad")+","+ 
        gVO.getAtributo("accesoDefecto")+",'"+ 
        gVO.getAtributo("diccionario")+"','"+ 
        gVO.getAtributo("informes")+"','"+
        gVO.getAtributo("version")+"',"+ 
        gVO.getAtributo("conEntidades")+","+ 
        gVO.getAtributo("conEjercicios")+")";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
      
      //titulos de la aplicacion
      altaTitulosAplicacion(gVO, conexion);
      
      //m_Log.debug("las filas afectadas en el insert son : " + res);
    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
        try{
            abd.devolverConexion(conexion);
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
    Vector lista = new Vector();
    lista = getListaAplicaciones(params);
    return lista;
   }
  
  
  private int altaTitulosAplicacion (GeneralValueObject gVO, Connection conexion) {
	  int resultado = 0;
	  PreparedStatement ps = null;
	  String codigoApl = (String) gVO.getAtributo("codigo");
	  Vector titulos = (Vector) gVO.getAtributo("titulos");
	  Vector element = new Vector();
	  String sql="";
	  
	  try {
		
		for(int i=0;i<titulos.size();i++){
			element = (Vector) titulos.elementAt(i);
			sql ="INSERT INTO A_AID (AID_APL, AID_IDI, AID_TEX) VALUES (" + codigoApl + "," +
					element.elementAt(0) + ",'" + element.elementAt(1) + "')";
			m_Log.debug("modificarTitulosAplicacion insercion == " + sql);
			ps = conexion.prepareStatement(sql);
			resultado = ps.executeUpdate();
		}
		
	} catch (SQLException e) {
		resultado = -1;
		e.printStackTrace();
	}
    return resultado;

}
  
  
  private void rollBackTransaction(AdaptadorSQLBD bd,Connection con,Exception e){
    try {
      bd.rollBack(con);
    }catch (Exception e1) {
      e1.printStackTrace();
    }finally {
      e.printStackTrace();
      m_Log.error(e.getMessage());
    }
  }
  
}