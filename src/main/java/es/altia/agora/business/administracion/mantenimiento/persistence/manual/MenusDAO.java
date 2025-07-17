// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.agora.business.util.GlobalNames;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: MenusDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: ALTIA CONSULTORES</p>
 */

public class MenusDAO  {
  private static MenusDAO instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(MenusDAO.class.getName());
  
  protected static String mnu_org;
  protected static String mnu_apl;
  protected static String mnu_cod;
  protected static String mnu_des;
  protected static String mnu_act;
    
  protected static String mor_org;
  protected static String mor_apl;
  protected static String mor_mnu;
  protected static String mor_ele;
  protected static String mor_pro;
  protected static String mor_des;
  protected static String mor_pad;
  protected static String mor_vis;
  
  protected static String pro_apl;
  protected static String pro_cod;
  protected static String pro_frm;
  protected static String pro_tpr;
  
  protected static String pid_apl;
  protected static String pid_pro;
  protected static String pid_idi;
  protected static String pid_des;
  
  protected static String eea_org;
  protected static String eea_bde;
  
  
  protected MenusDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");

    mnu_cod = m_ConfigTechnical.getString("SQL.A_MNU.codigo");
	mnu_org = m_ConfigTechnical.getString("SQL.A_MNU.organizacion");
	mnu_apl = m_ConfigTechnical.getString("SQL.A_MNU.aplicacion");
	mnu_des = m_ConfigTechnical.getString("SQL.A_MNU.descripcion");
	mnu_act = m_ConfigTechnical.getString("SQL.A_MNU.activo");
	
	mor_ele = m_ConfigTechnical.getString("SQL.A_MOR.elemento");
	mor_apl = m_ConfigTechnical.getString("SQL.A_MOR.aplicacion");
	mor_pro = m_ConfigTechnical.getString("SQL.A_MOR.proceso");
	mor_org = m_ConfigTechnical.getString("SQL.A_MOR.organizacion");
	mor_pad = m_ConfigTechnical.getString("SQL.A_MOR.padre");
	mor_mnu = m_ConfigTechnical.getString("SQL.A_MOR.menu");
	mor_des = m_ConfigTechnical.getString("SQL.A_MOR.descripcion");
	mor_vis = m_ConfigTechnical.getString("SQL.A_MOR.visible");
   		
   	pro_apl = m_ConfigTechnical.getString("SQL.A_PRO.aplicacion");
   	pro_cod = m_ConfigTechnical.getString("SQL.A_PRO.codigo");
   	pro_frm = m_ConfigTechnical.getString("SQL.A_PRO.formulario");
   	pro_tpr = m_ConfigTechnical.getString("SQL.A_PRO.tipoProceso");
   
   	pid_apl = m_ConfigTechnical.getString("SQL.A_PID.aplicacion");
   	pid_pro = m_ConfigTechnical.getString("SQL.A_PID.proceso");
   	pid_idi = m_ConfigTechnical.getString("SQL.A_PID.idioma");
   	pid_des = m_ConfigTechnical.getString("SQL.A_PID.descripcion");
   	
   	eea_org = m_ConfigTechnical.getString("SQL.A_EEA.organizacion");
   	eea_bde = m_ConfigTechnical.getString("SQL.A_EEA.jndi");
	
  }

  public static MenusDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (MenusDAO.class) {
        if (instance == null) {
          instance = new MenusDAO();
        }
      }
    }
    return instance;
  }
  public Vector getListaMenus(GeneralValueObject gVO,String[] params){
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    
    if  ( (gVO.getAtributo("codOrganizacion") != null) && (gVO.getAtributo("codAplicacion") != null) ) {
    	String aplicacion = (String) gVO.getAtributo("codAplicacion");	
    	String organizacion = (String) gVO.getAtributo("codOrganizacion");	
    	String menu ="";
    	if (!"".equals(aplicacion) && !"".equals(organizacion)) {		  
    		gVO.setAtributo("codMenu", null);
		    gVO.setAtributo("descMenu", null);		      	

		    try{		     
		      abd = new AdaptadorSQLBD(params);
		      conexion = abd.getConnection();
		      // Consulta del menu (suponemos que solo hay uno)
		      sql = "SELECT " + mnu_cod + "," + mnu_des + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_MNU WHERE " +
		      	    mnu_apl + "=" + aplicacion + " AND " + mnu_org + "=" + organizacion;
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		      stmt = conexion.createStatement();
		      rs = stmt.executeQuery(sql);      		      
		      		      
		      while(rs.next()){
		      	menu = rs.getString(mnu_cod);
		      	String desc = rs.getString(mnu_des);
		      	gVO.setAtributo("codMenu", menu);
		      	gVO.setAtributo("descMenu", desc);		      	
		      }
		      rs.close();
		      stmt.close();
		      if (menu != null) 
		      	if (!"".equals(menu)){
			      // Consultamos los puntos de menu.
			      sql = "SELECT " + mor_ele + "," + mor_pro + "," + mor_pad +","+ pro_frm + ","+ pid_idi+","+pid_des
			      	 + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_MOR, " + GlobalNames.ESQUEMA_GENERICO + "A_PRO, "
                     + GlobalNames.ESQUEMA_GENERICO + "A_PID "
			      	 + " WHERE " + GlobalNames.ESQUEMA_GENERICO + "a_mor." + mor_pro + "=" + GlobalNames.ESQUEMA_GENERICO
                     + "a_pro." + pro_cod + " AND " + GlobalNames.ESQUEMA_GENERICO + "a_mor." + mor_apl + "="
                     + GlobalNames.ESQUEMA_GENERICO + "a_pro." + pro_apl + " AND " + GlobalNames.ESQUEMA_GENERICO + "a_pro."
                     + pro_apl + "=" + GlobalNames.ESQUEMA_GENERICO + "a_pid." + pid_apl + " AND "
                     + GlobalNames.ESQUEMA_GENERICO + "a_pro." + pro_cod + "=" + GlobalNames.ESQUEMA_GENERICO + "a_pid."
                     + pid_pro + " AND " + mor_apl + "=" + aplicacion + " AND " + mor_org + "=" + organizacion
			      	 + " AND " + mor_mnu + "=" + menu + " ORDER BY " + mor_pad + "," + mor_ele +" ASC ";
			      
                  if(m_Log.isDebugEnabled()) m_Log.debug(sql);
			      stmt = conexion.createStatement();
			      rs = stmt.executeQuery(sql);      		      
			      GeneralValueObject eVO= new GeneralValueObject();
			      GeneralValueObject iVO= new GeneralValueObject();
			      Vector listaDescripciones = new Vector();		      		      
			      while(rs.next()){
			      	
			      	String codigo = rs.getString(mor_ele);
			      	if (codigo != null) 
			      		if (!codigo.equals(eVO.getAtributo("elemento"))) {
			      			eVO= new GeneralValueObject();
			      			eVO.setAtributo("elemento",codigo);
			      			eVO.setAtributo("padre",rs.getString(mor_pad));
			      			eVO.setAtributo("proceso",rs.getString(mor_pro));
			      			eVO.setAtributo("formulario",rs.getString(pro_frm));
			       			listaDescripciones = new Vector();
			      			eVO.setAtributo("descripcionesIdioma", listaDescripciones);
			      			iVO= new GeneralValueObject();
			      			iVO.setAtributo("codigoIdioma",rs.getString(pid_idi));
			      			iVO.setAtributo("descripcion",rs.getString(pid_des));
			      			listaDescripciones.addElement(iVO);
				   
			      			resultado.add(eVO);
			       		} else {
			      			iVO= new GeneralValueObject();
			      			iVO.setAtributo("codigoIdioma",rs.getString(pid_idi));		      		
			      			iVO.setAtributo("descripcion",rs.getString(pid_des));
			      			listaDescripciones.addElement(iVO);
			      		}        		    							      		
			      }
			      rs.close();
			      stmt.close();
			   }
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
		}
	}	
    return resultado;
  }
/*
  public Vector modificarMenu(GeneralValueObject gVO, String[] params){
  	if ( gVO.getAtributo("codAplicacion")!=null && gVO.getAtributo("codMenu")!=null) {
  		String apl = (String) gVO.getAtributo("codAplicacion");
  		String cod = (String) gVO.getAtributo("codMenu");
	
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
	      sql = "UPDATE A_PRO SET " + 
	        pro_frm +"="+(gVO.getAtributo("formulario")!=null?"'"+(String)gVO.getAtributo("formulario")+"'":" null ")
	        + " WHERE " + pro_apl+"="+apl+" AND " + pro_cod+"="+cod;
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	      stmt = conexion.createStatement();
	      int res = stmt.executeUpdate(sql);
	      stmt.close();
	      stmt = conexion.createStatement();
	      sql = "DELETE FROM A_PID WHERE " +
	        pid_apl+"="+apl+" AND " + pid_pro+"="+cod;
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	      stmt.executeUpdate(sql);
	      stmt.close();	      
		  // Insertar descripciones.
      	  Vector di = (Vector) gVO.getAtributo("idiomasDescripciones");
      	  if (di != null) 
      	  for (int i=0; i<di.size();i++) {
      		Vector datos = (Vector) di.elementAt(i);
		    stmt = conexion.createStatement();
		    sql = "INSERT INTO A_PID("+ pid_apl+","+pid_pro +","+pid_idi+","+pid_des+") VALUES ("
		      		+ apl + "," + cod +"," + (String) datos.elementAt(0) + ",'" + (String) datos.elementAt(1)+"')";		      		  	
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		    stmt = conexion.createStatement();
		    stmt.executeUpdate(sql);
		    stmt.close();      		
      	  } 
	      //m_Log.debug("las filas afectadas en el update son : " + res);
	    }catch (Exception e){
	      rollBackTransaction(abd,conexion,e);
	    }finally{
	      commitTransaction(abd,conexion);
	    }
	}
    Vector lista = new Vector();
    lista = getListaMenus(gVO,params);
    return lista;
  }

*/
  public Vector altaMenu(GeneralValueObject gVO, String[] params){
  	AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    if  ( (gVO.getAtributo("codAplicacion") != null) && (gVO.getAtributo("codOrganizacion") != null) 
    		&& (gVO.getAtributo("descMenu") != null)) {
	    String apl = (String) gVO.getAtributo("codAplicacion");  	
	  	String org = (String) gVO.getAtributo("codOrganizacion");  	    	
	  	String desc = (String) gVO.getAtributo("descMenu");  	    	
		
		if ( !"".equals(apl) && !"".equals(org) && !"".equals(desc) ) {
		    try{      
              String[] parametros = (String []) params.clone();
              parametros[6] = m_ConfigTechnical.getString("CON.jndi");
		      abd = new AdaptadorSQLBD(parametros);
		      conexion = abd.getConnection();
		      String codigo = "1";
		      // Buscar codigo menu.

              sql = "SELECT " + abd.funcionMatematica(AdaptadorSQLBD.FUNCIONMATEMATICA_MAX,new String[]{mnu_cod}) +
                    "+1 AS codigo " + "FROM A_MNU WHERE " +
                    mnu_org +"="+ org + " AND " + mnu_apl +"="+ apl;
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		      stmt = conexion.createStatement();
		      rs = stmt.executeQuery(sql);
		      if (rs.next()){
		      	codigo = rs.getString("codigo");
		      	if (codigo==null) codigo = "1";
		      } else codigo ="1"; 
		      rs.close();
		      stmt.close();      
		      
		      // Insertar.
		      sql = "INSERT INTO A_MNU("+ mnu_org+","+mnu_apl+","+mnu_cod +","+mnu_des
                    + "," + mnu_act + ") VALUES ("
		      		+ org + "," + apl + "," + codigo 
		      		+ ",'" + desc +"',1)";
		    		  	
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		      stmt = conexion.createStatement();
		      stmt.executeUpdate(sql);
		      stmt.close();
		      gVO.setAtributo("codMenu", codigo);
		      gVO.setAtributo("codMenu",desc);		      
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
		  }
	}   
    Vector lista = new Vector();
    lista = getListaMenus(gVO,params);
    return lista;
   }

public Vector modificarMenu(GeneralValueObject gVO, String[] params){
  	AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    if  ( (gVO.getAtributo("codAplicacion") != null) && (gVO.getAtributo("codOrganizacion") != null) 
    		&& (gVO.getAtributo("codMenu") != null) && (gVO.getAtributo("descMenu") != null)) {
	    String apl = (String) gVO.getAtributo("codAplicacion");  	
	  	String org = (String) gVO.getAtributo("codOrganizacion");  	    	
	  	String cod = (String) gVO.getAtributo("codMenu");  	    	
	  	String desc = (String) gVO.getAtributo("descMenu");  	    	
		
		if ( !"".equals(apl) && !"".equals(org) && !"".equals(cod) && !"".equals(desc) ) {
		    try{      
              String[] parametros = (String []) params.clone();
              parametros[6] = m_ConfigTechnical.getString("CON.jndi");
		      abd = new AdaptadorSQLBD(parametros);
		      conexion = abd.getConnection();

		      // Actualizar.
		      sql = " UPDATE A_MNU SET " + mnu_des+ "='" + desc +"' " +
                    " WHERE " + mnu_org + "=" + org + " AND " + mnu_apl + "=" + apl + " AND " + mnu_cod + "=" + cod;
		    		  	
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		      stmt = conexion.createStatement();
		      stmt.executeUpdate(sql);
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
		  }
	}   
    Vector lista = new Vector();
    lista = getListaMenus(gVO,params);
    return lista;
   }


public Vector eliminarMenu(GeneralValueObject gVO, String[] params){
  	AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    if  ( (gVO.getAtributo("codAplicacion") != null) && (gVO.getAtributo("codOrganizacion") != null) 
    		&& (gVO.getAtributo("codMenu") != null)) {
	    String apl = (String) gVO.getAtributo("codAplicacion");  	
	  	String org = (String) gVO.getAtributo("codOrganizacion");  	    	
	  	String cod = (String) gVO.getAtributo("codMenu");  	    		  	
		
		if ( !"".equals(apl) && !"".equals(org) && !"".equals(cod) ) {
		    try{      
              String[] parametros = (String []) params.clone();
              parametros[6] = m_ConfigTechnical.getString("CON.jndi");
		      abd = new AdaptadorSQLBD(parametros);
		      conexion = abd.getConnection();
		      abd.inicioTransaccion(conexion);		      
		      
		      // Hijos de a_mor.
		      sql = " DELETE FROM A_MOR WHERE " +
                      mor_org + "=" + org + " AND " + mor_apl+ "=" + apl + " AND " +
                      mor_mnu + "=" + cod;
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		      stmt = conexion.createStatement();
		      stmt.executeUpdate(sql);
		      stmt.close();
		      
		      // Actualizar.
		      sql = " DELETE FROM A_MNU WHERE " +
                    mnu_org + "=" + org + " AND " + mnu_apl+ "=" + apl + " AND " +
                    mnu_cod + "=" + cod;
		    		  	
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		      stmt = conexion.createStatement();
		      stmt.executeUpdate(sql);
		      stmt.close();
		      gVO.setAtributo("codMenu",null);
		      gVO.setAtributo("descMenu",null);
		    }catch (Exception e){
		      rollBackTransaction(abd,conexion,e);
		    }finally{
		      commitTransaction(abd,conexion);
		    }
		  }
	}   
	
	
    Vector lista = new Vector();
    lista = getListaMenus(gVO,params);
    return lista;
   }

public Vector altaPuntoMenu(GeneralValueObject gVO, String[] params){
  	AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    if  ( (gVO.getAtributo("codAplicacion") != null) && (gVO.getAtributo("codOrganizacion") != null) 
    		&& (gVO.getAtributo("codMenu") != null) && (gVO.getAtributo("codPadre") != null)
    		&& (gVO.getAtributo("codProceso") != null) ) {
    			
	    String apl = (String) gVO.getAtributo("codAplicacion");  	
	  	String org = (String) gVO.getAtributo("codOrganizacion");  	    	
	  	String mnu = (String) gVO.getAtributo("codMenu");  	
	  	String padre = (String) gVO.getAtributo("codPadre");  	    	
	  	String proceso = (String) gVO.getAtributo("codProceso");  	    	
	  	
	  	if (!"".equals(apl) && !"".equals(org) && !"".equals(mnu) && !"".equals(padre) && !"".equals(proceso) ) {
		    try{      
              String[] parametros = (String []) params.clone();
              parametros[6] = m_ConfigTechnical.getString("CON.jndi");
		      abd = new AdaptadorSQLBD(parametros);
		      conexion = abd.getConnection();
		      String codigo = "1";
		      // Buscar codigo menu.
		      sql = "SELECT "+ abd.funcionMatematica(AdaptadorSQLBD.FUNCIONMATEMATICA_MAX,new String[]{mor_ele}) +
                    "+1 AS codigo " +
                    "FROM A_MOR WHERE " + mor_org +"="+org +
                    " AND " + mor_apl +"="+ apl + " AND " + mor_mnu +"="+  mnu;
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		      stmt = conexion.createStatement();
		      rs = stmt.executeQuery(sql);
		      if (rs.next()){
		      	codigo = rs.getString("codigo");
		      	if (codigo == null) codigo="1";
		      } else
                  codigo ="1";
		      rs.close();
		      stmt.close();      
		      
		      // Insertar.
		      sql = "INSERT INTO A_MOR ("+ mor_org+","+mor_apl+","+mor_mnu +","+mor_ele
		      				+ "," + mor_pad + "," + mor_pro + "," + mor_vis  + ") VALUES ("
		      		+ org + "," + apl + "," + mnu + "," + codigo +"," + padre + "," + proceso +",1)";
		    		  	
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		      stmt = conexion.createStatement();
		      stmt.executeUpdate(sql);
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
		  }
	}   
    Vector lista = new Vector();
    lista = getListaMenus(gVO,params);
    return lista;
   }

public Vector modificarPuntoMenu(GeneralValueObject gVO, String[] params){
  	AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    if  ( (gVO.getAtributo("codAplicacion") != null) && (gVO.getAtributo("codOrganizacion") != null) 
    		&& (gVO.getAtributo("codMenu") != null) && (gVO.getAtributo("codElemento") != null)
    		&& (gVO.getAtributo("codProceso") != null) ) {
    			
	    String apl = (String) gVO.getAtributo("codAplicacion");  	
	  	String org = (String) gVO.getAtributo("codOrganizacion");  	    	
	  	String mnu = (String) gVO.getAtributo("codMenu");  	
	  	String elemento = (String) gVO.getAtributo("codElemento");  	    		  	
	  	String proceso = (String) gVO.getAtributo("codProceso");  	    	
	  	
	  	if (!"".equals(apl) && !"".equals(org) && !"".equals(mnu) && !"".equals(elemento) && !"".equals(proceso) ) {
		    try{      
              String[] parametros = (String []) params.clone();
              parametros[6] = m_ConfigTechnical.getString("CON.jndi");
		      abd = new AdaptadorSQLBD(parametros);
		      conexion = abd.getConnection();
		      // Update
		      sql = "UPDATE A_MOR SET " + mor_pro + "=" + proceso +
		      		" WHERE " + mor_org+"="+ org + " AND " + mor_apl + "=" + apl + " AND " + mor_mnu + "=" + mnu + " AND " +
                    mor_ele + "=" + elemento;
		    		  	
              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		      stmt = conexion.createStatement();
		      stmt.executeUpdate(sql);
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
		  }
	}   
    Vector lista = new Vector();
    lista = getListaMenus(gVO,params);
    return lista;
   }

public Vector eliminarPuntoMenu(GeneralValueObject gVO, String[] params){
  	AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    if  ( (gVO.getAtributo("codAplicacion") != null) && (gVO.getAtributo("codOrganizacion") != null) 
    		&& (gVO.getAtributo("codMenu") != null) && (gVO.getAtributo("codElemento") != null)) {
    			
	    String apl = (String) gVO.getAtributo("codAplicacion");  	
	  	String org = (String) gVO.getAtributo("codOrganizacion");  	    	
	  	String mnu = (String) gVO.getAtributo("codMenu");  	
	  	String elemento = (String) gVO.getAtributo("codElemento");  	    	
	  	
	  	if (!"".equals(apl) && !"".equals(org) && !"".equals(mnu) && !"".equals(elemento)) {
		    try{      
              String[] parametros = (String []) params.clone();
              parametros[6] = m_ConfigTechnical.getString("CON.jndi");
		      abd = new AdaptadorSQLBD(parametros);
		      conexion = abd.getConnection();
		      abd.inicioTransaccion(conexion);

		      // Implementacion del borrado en cascada.
		      Vector puntos = new Vector();
		      puntos.addElement(elemento);
		      int i=0;
		      while (i<puntos.size()) {
		      	
		      		// Buscamos sus hijos.		      
		      		sql = "SELECT " + mor_ele + " FROM A_MOR WHERE " +
                          mor_org + "="+ org + " AND " +  mor_apl +
                          "=" + apl + " AND " + mor_mnu + "=" +  mnu + " AND " +
                          mor_pad + "=" + puntos.elementAt(i);
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		      		stmt = conexion.createStatement();
		      		rs = stmt.executeQuery(sql);
		      		if (rs.next()){
		      			String codigo = rs.getString("mor_ele");
		      			puntos.addElement(codigo);
		      		}
		      		rs.close();
		      		stmt.close();      
		      		
		      		// DELETE
		      		sql = "DELETE FROM A_MOR WHERE " +
                          mor_org + "=" + org + " AND " + mor_apl + "="+ apl + " AND " +
                          mor_mnu +"="+ mnu + " AND " + mor_ele+"="+ puntos.elementAt(i);
		    		  	
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		      		stmt = conexion.createStatement();
		      		stmt.executeUpdate(sql);
		      		stmt.close();	      
		      		i++;
		      	}
		      	
		    }catch (Exception e){
		      rollBackTransaction(abd,conexion,e);
		    }finally{
		      commitTransaction(abd,conexion);
		    }
		  }
	}   
    Vector lista = new Vector();
    lista = getListaMenus(gVO,params);
    return lista;
   }
  
  public Vector obtenerMenuDiputacion(GeneralValueObject gVO,String[] params){
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    String codAplicacion = (String) gVO.getAtributo("codAplicacion");
    	      	
    try{		     
      abd = new AdaptadorSQLBD(params);
      conexion = abd.getConnection();
      sql = "SELECT * FROM " + GlobalNames.ESQUEMA_GENERICO + "A_MOR WHERE " + mor_org + "=0 AND " + mor_apl + "=" +
            codAplicacion;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);      		      
      		      
      while(rs.next()){
      	GeneralValueObject g = new GeneralValueObject();
      	String cAplic = rs.getString(mor_apl);
      	g.setAtributo("codAplicacion",cAplic);
      	String codMenu = rs.getString(mor_mnu);
      	g.setAtributo("codMenu",codMenu);
      	String codElemento = rs.getString(mor_ele);
      	g.setAtributo("codElemento",codElemento);
      	String codProceso = rs.getString(mor_pro);
      	g.setAtributo("codProceso",codProceso);
      	String visible = rs.getString(mor_vis);		      	
      	g.setAtributo("visible",visible);
      	String descMenu = rs.getString(mor_des);
      	g.setAtributo("descMenu",descMenu);
      	String elementoPadre = rs.getString(mor_pad);
      	g.setAtributo("elementoPadre",elementoPadre);
        resultado.addElement(g);
      }
      rs.close();
      stmt.close();
    }catch (Exception e){
      resultado = null;
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
  
  public Vector altaMenuCompleto(GeneralValueObject gVO,Vector listaMenu, String[] params){
  	AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    			
	    String apl = (String) gVO.getAtributo("codAplicacion");  	
	  	String org = (String) gVO.getAtributo("codOrganizacion");  	    	 	    	
	  	
  	  try{      
          String[] parametros = (String []) params.clone();
          parametros[6] = m_ConfigTechnical.getString("CON.jndi");
	      abd = new AdaptadorSQLBD(parametros);
	      conexion = abd.getConnection();
	      abd.inicioTransaccion(conexion);

		  sql = "DELETE FROM A_MOR WHERE " + mor_org + "=" + org + " AND " +
                mor_apl + "=" + apl;
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	      stmt = conexion.createStatement();
	      stmt.executeUpdate(sql);
	      stmt.close();

		  if(listaMenu.size() != 0) {
		    for(int i=0;i<listaMenu.size();i++) {
			    GeneralValueObject g = new GeneralValueObject();
				g = (GeneralValueObject) listaMenu.elementAt(i);
				sql = "INSERT INTO A_MOR ("+ mor_org+","+mor_apl+","+mor_mnu +","
                      + mor_ele + "," + mor_pad + "," + mor_pro + "," + mor_vis  + ") VALUES (" +
	      		   	  org + "," + apl + "," + g.getAtributo("codMenu") + "," +
	      		   	  g.getAtributo("codElemento") + "," + g.getAtributo("elementoPadre") +
	      			  "," + g.getAtributo("codProceso") + "," + g.getAtributo("visible") + ")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
			    stmt = conexion.createStatement();
			    stmt.executeUpdate(sql);
			    stmt.close();
			 }	
		  }
	    }catch (Exception e){
	      rollBackTransaction(abd,conexion,e);
	    }finally{
	      commitTransaction(abd,conexion);
	    }
    Vector lista = new Vector();
    lista = getListaMenus(gVO,params);
    return lista;
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

  private void commitTransaction(AdaptadorSQLBD bd,Connection con){
    try{
      bd.finTransaccion(con);
      bd.devolverConexion(con);
    }catch (Exception ex) {
      ex.printStackTrace();
      m_Log.error(ex.getMessage());
    }
  }   
  
}
