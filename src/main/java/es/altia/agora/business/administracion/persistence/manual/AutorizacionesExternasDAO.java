// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.persistence.manual;

// PAQUETES IMPORTADOS
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import es.altia.agora.business.administracion.AutorizacionesExternasValueObject;
import es.altia.agora.business.administracion.ParametrosBDVO;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.cache.CacheDatosFactoria;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DepartamentosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class AutorizacionesExternasDAO  {
  private static AutorizacionesExternasDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(AutorizacionesExternasDAO.class.getName());
  
  protected static String ent_cod;
  protected static String ent_nom;
  protected static String ent_org;
  protected static String ent_dtr;
  protected static String ent_tip;
  
  protected static String org_cod;
  protected static String org_des;
  
  protected static String aae_org;
  protected static String aae_ent;
  protected static String aae_apl;
  protected static String aae_lic;
  
  protected static String aau_apl;
  protected static String aau_usu;
  
  protected static String apl_cod;
  protected static String apl_nom;
  
  protected static String uae_org;
  protected static String uae_ent;
  protected static String uae_apl;
  protected static String uae_usu;

  protected static String eea_bde;
  protected static String eea_ent;
  protected static String eea_apl;
  protected static String eea_eje;


  protected static String ugo_gru;
  protected static String ugo_ent;
  protected static String ugo_apl;
  protected static String ugo_usu;
  protected static String ugo_org;

  protected AutorizacionesExternasDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");

    ent_cod = campos.getString("SQL.A_ENT.codigo");
    ent_nom = campos.getString("SQL.A_ENT.nombre");
    ent_org = campos.getString("SQL.A_ENT.organizacion");
    ent_dtr = campos.getString("SQL.A_ENT.dirTrabajo");
    ent_tip = campos.getString("SQL.A_ENT.tipo"); 
    
    org_cod = campos.getString("SQL.A_ORG.codigo");
    org_des = campos.getString("SQL.A_ORG.descripcion");
    
    aae_org = campos.getString("SQL.A_AAE.organizacion");
    aae_ent = campos.getString("SQL.A_AAE.entidad");
    aae_apl = campos.getString("SQL.A_AAE.aplicacion");
    aae_lic = campos.getString("SQL.A_AAE.licencia");
    
    aau_apl = campos.getString("SQL.A_AAU.aplicacion");
    aau_usu = campos.getString("SQL.A_AAU.usuario");
    
    apl_cod = campos.getString("SQL.A_APL.codigo");
    apl_nom = campos.getString("SQL.A_APL.nombre");
    
    uae_org = campos.getString("SQL.A_UAE.organizacion");
    uae_ent = campos.getString("SQL.A_UAE.entidad");
    uae_apl = campos.getString("SQL.A_UAE.aplicacion");
    uae_usu = campos.getString("SQL.A_UAE.usuario");
        
    eea_bde = campos.getString("SQL.A_EEA.jndi");
    eea_ent = campos.getString("SQL.A_EEA.entidad");
    eea_apl = campos.getString("SQL.A_EEA.aplicacion");
    eea_eje = campos.getString("SQL.A_EEA.ejercicio");

    ugo_ent = campos.getString("SQL.A_UGO.entidad");
    ugo_apl = campos.getString("SQL.A_UGO.aplicacion");
    ugo_usu = campos.getString("SQL.A_UGO.usuario");
    ugo_org = campos.getString("SQL.A_UGO.organizacion");
  }

  public static AutorizacionesExternasDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (AutorizacionesExternasDAO.class) {
        if (instance == null) {
          instance = new AutorizacionesExternasDAO();
        }
      }
    }
    return instance;
  }

  public Vector getListaEntidadesAplicaciones(AutorizacionesExternasValueObject aeVO,String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = "";
    String from = "";
    String where = "";
    Vector listaEntidades = new Vector();
    Vector listaEntidadesAplicaciones = new Vector();
    Vector listaEntidadesFinal = new Vector();
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      from = ent_org + "," + org_des + "," + ent_cod + "," + ent_nom ;
      String[] join = new String[5];
      join[0] = GlobalNames.ESQUEMA_GENERICO + "A_ENT";
      join[1] = "INNER";
      join[2] = GlobalNames.ESQUEMA_GENERICO + "a_org";
      join[3] = GlobalNames.ESQUEMA_GENERICO + "a_ent." + ent_org + "=" + GlobalNames.ESQUEMA_GENERICO + "a_org." + org_cod;
      join[4] = "false";
      sql = abd.join(from,where,join);
      sql += " ORDER BY 1,3";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.prepareStatement(sql);
      rs = stmt.executeQuery();
      while(rs.next()) {
        AutorizacionesExternasValueObject a = new AutorizacionesExternasValueObject();
        String codOrganizacion = rs.getString(ent_org);
        a.setCodOrganizacion(codOrganizacion);
        String nombreOrganizacion = rs.getString(org_des);
        a.setNombreOrganizacion(nombreOrganizacion);
        String codEntidad = rs.getString(ent_cod);
        a.setCodEntidad(codEntidad);
        String nombreEntidad = rs.getString(ent_nom);
        a.setNombreEntidad(nombreEntidad);
        listaEntidades.addElement(a);
      }
      if (m_Log.isDebugEnabled()) m_Log.debug("el tamaño de la lista de entidades es : " + listaEntidades.size());
      rs.close();
      stmt.close();
      sql = "SELECT " + GlobalNames.ESQUEMA_GENERICO + "A_AAE." + aae_org + "," + aae_ent + "," + eea_bde +
            " FROM "+ GlobalNames.ESQUEMA_GENERICO + "A_AAE, " + GlobalNames.ESQUEMA_GENERICO + "A_EEA WHERE " + aae_apl +
            "=" + aeVO.getCodAplicacion() + " AND " + GlobalNames.ESQUEMA_GENERICO + "A_AAE." +
            aae_org + "=" + GlobalNames.ESQUEMA_GENERICO +  "A_EEA." + aae_org + " AND " + GlobalNames.ESQUEMA_GENERICO +
            "A_AAE." + aae_ent + "=" + GlobalNames.ESQUEMA_GENERICO +  "A_EEA." +eea_ent + " AND " + GlobalNames.ESQUEMA_GENERICO +
            "A_AAE." + aae_apl + "=" + GlobalNames.ESQUEMA_GENERICO +  "A_EEA." +eea_apl + " ORDER BY 1,2";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.prepareStatement(sql);
      rs = stmt.executeQuery();
      while(rs.next()) {
      	AutorizacionesExternasValueObject a = new AutorizacionesExternasValueObject();
      	String codOrganizacion = rs.getString(aae_org);
        a.setCodOrganizacion(codOrganizacion);
        String codEntidad = rs.getString(aae_ent);
        a.setCodEntidad(codEntidad);
        String baseDeDatos = rs.getString("eea_bde");
        a.setBaseDeDatos(baseDeDatos);
        listaEntidadesAplicaciones.addElement(a);
      }
      int m=0;
      for(int i=0;i<listaEntidadesAplicaciones.size();i++) {
        AutorizacionesExternasValueObject a = new AutorizacionesExternasValueObject();
        a = (AutorizacionesExternasValueObject) listaEntidadesAplicaciones.elementAt(i);
        String cO = (String) a.getCodOrganizacion();
        String cE = (String) a.getCodEntidad();
        for(int j=m;j<listaEntidades.size();j++) {
        	AutorizacionesExternasValueObject a2 = new AutorizacionesExternasValueObject();
          a2 = (AutorizacionesExternasValueObject) listaEntidades.elementAt(j);
          String cO2 = (String) a2.getCodOrganizacion();
          String cE2 = (String) a2.getCodEntidad();
          if(cO.equals(cO2) && cE.equals(cE2)) {
          	a2.setAutorizacion("si");
          	a2.setBaseDeDatos(a.getBaseDeDatos());
          	m=j+1;
          	if(i != (listaEntidadesAplicaciones.size()-1)) {
          	  j=listaEntidades.size();
            }
          } else {
          	a2.setAutorizacion("no");
          	a2.setBaseDeDatos("");
          }
          listaEntidadesFinal.addElement(a2);
        } 	
      }
      if(listaEntidadesAplicaciones.size() == 0) {
      	listaEntidadesFinal = listaEntidades;
      }

      rs.close();
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    return listaEntidadesFinal;
  } 
  
  public Vector getListaAplicacionesUsuarios(AutorizacionesExternasValueObject aeVO,String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = "";
    String from = "";
    String where = "";
    Vector listaAplicaciones = new Vector();
    Vector listaAplicacionesUsuarios = new Vector();
    Vector listaAplicacionesFinal = new Vector();
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      sql = "SELECT " + apl_cod + "," + apl_nom + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_APL ORDER BY 1";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.prepareStatement(sql);
      rs = stmt.executeQuery();
      while(rs.next()) {
        AutorizacionesExternasValueObject a = new AutorizacionesExternasValueObject();
        String codAplicacion = rs.getString(apl_cod);
        a.setCodAplicacion(codAplicacion);
        String nombreAplicacion = rs.getString(apl_nom);
        a.setNombreAplicacion(nombreAplicacion);
        a.setCodUsuario(aeVO.getCodUsuario());
        listaAplicaciones.addElement(a);
      }
      if (m_Log.isDebugEnabled()) m_Log.debug("el tamaño de la lista de aplicaciones es : " + listaAplicaciones.size());
      rs.close();
      stmt.close();
      sql = "SELECT " + aau_apl + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_AAU WHERE " +
            aau_usu + "=" + aeVO.getCodUsuario() + " ORDER BY 1";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.prepareStatement(sql);
      rs = stmt.executeQuery();
      while(rs.next()) {
      	AutorizacionesExternasValueObject a = new AutorizacionesExternasValueObject();
      	String codAplicacion = rs.getString(aau_apl);
        a.setCodAplicacion(codAplicacion);
        listaAplicacionesUsuarios.addElement(a);
      }
      int m=0;
      for(int i=0;i<listaAplicacionesUsuarios.size();i++) {
        AutorizacionesExternasValueObject a = new AutorizacionesExternasValueObject();
        a = (AutorizacionesExternasValueObject) listaAplicacionesUsuarios.elementAt(i);
        String cA = a.getCodAplicacion();
        for(int j=m;j<listaAplicaciones.size();j++) {
        	AutorizacionesExternasValueObject a2 = new AutorizacionesExternasValueObject();
          a2 = (AutorizacionesExternasValueObject) listaAplicaciones.elementAt(j);
          String cA2 = (String) a2.getCodAplicacion();
          if(cA.equals(cA2)) {
          	a2.setAutorizacion("si");
          	m=j+1;
          	if(i != (listaAplicacionesUsuarios.size()-1)) {
          	  j=listaAplicaciones.size();
            }
          } else {
          	a2.setAutorizacion("no");
          }
          listaAplicacionesFinal.addElement(a2);
        } 	
      }
      if(listaAplicacionesUsuarios.size() == 0) {
      	listaAplicacionesFinal = listaAplicaciones;
      }

      rs.close();
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    return listaAplicacionesFinal;
  }
  
  public Vector getListaEntidadesUsuarios(AutorizacionesExternasValueObject aeVO,String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = "";
    String from = "";
    String where = "";
    Vector listaEntidades = new Vector();
    Vector listaEntidadesUsuarios = new Vector();
    Vector listaEntidadesFinal = new Vector();
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      from = aae_org + "," + org_des + "," + aae_ent + "," + ent_nom;
      where = aae_apl + "=" + aeVO.getCodAplicacion();
      String[] join = new String[8];
      join[0] = GlobalNames.ESQUEMA_GENERICO + "A_AAE";
      join[1] = "INNER";
      join[2] = GlobalNames.ESQUEMA_GENERICO + "a_org";
      join[3] = GlobalNames.ESQUEMA_GENERICO + "a_aae." + aae_org + "=" + GlobalNames.ESQUEMA_GENERICO + "a_org." + org_cod;
      join[4] = "INNER";
      join[5] = GlobalNames.ESQUEMA_GENERICO + "a_ent";
      join[6] = GlobalNames.ESQUEMA_GENERICO + "a_aae." + aae_org + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ent." + ent_org + 
                " AND " +
                GlobalNames.ESQUEMA_GENERICO + "a_aae." + aae_ent + "=" + GlobalNames.ESQUEMA_GENERICO + "a_ent." + ent_cod;
      join[7] = "false";
      sql = abd.join(from,where,join);
      sql += " ORDER BY 1,3";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.prepareStatement(sql);
      rs = stmt.executeQuery();
      while(rs.next()) {
        AutorizacionesExternasValueObject a = new AutorizacionesExternasValueObject();
        String codOrganizacion = rs.getString(aae_org);
        a.setCodOrganizacion(codOrganizacion);
        String nombreOrganizacion = rs.getString(org_des);
        a.setNombreOrganizacion(nombreOrganizacion);
        String codEntidad = rs.getString(aae_ent);
        a.setCodEntidad(codEntidad);
        String nombreEntidad = rs.getString(ent_nom);
        a.setNombreEntidad(nombreEntidad);
        listaEntidades.addElement(a);
      }
      if (m_Log.isDebugEnabled()) m_Log.debug("el tamaño de la lista de entidades es : " + listaEntidades.size());
      rs.close();
      stmt.close();
      sql = "SELECT " + uae_org + "," + uae_ent + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UAE WHERE " +
            uae_usu + "=" + aeVO.getCodUsuario() + " AND " +
            uae_apl + "=" + aeVO.getCodAplicacion() + " ORDER BY 1,2";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.prepareStatement(sql);
      rs = stmt.executeQuery();
      while(rs.next()) {
      	AutorizacionesExternasValueObject a = new AutorizacionesExternasValueObject();
      	String codOrganizacion = rs.getString(uae_org);
        a.setCodOrganizacion(codOrganizacion);
        String codEntidad = rs.getString(uae_ent);
        a.setCodEntidad(codEntidad);
        listaEntidadesUsuarios.addElement(a);
      }
      int m=0;
      for(int i=0;i<listaEntidadesUsuarios.size();i++) {
        AutorizacionesExternasValueObject a = new AutorizacionesExternasValueObject();
        a = (AutorizacionesExternasValueObject) listaEntidadesUsuarios.elementAt(i);
        String cO = (String) a.getCodOrganizacion();
        String cE = (String) a.getCodEntidad();
        for(int j=m;j<listaEntidades.size();j++) {
        	AutorizacionesExternasValueObject a2 = new AutorizacionesExternasValueObject();
          a2 = (AutorizacionesExternasValueObject) listaEntidades.elementAt(j);
          String cO2 = (String) a2.getCodOrganizacion();
          String cE2 = (String) a2.getCodEntidad();
          if(cO.equals(cO2) && cE.equals(cE2)) {
          	a2.setAutorizacion("si");
          	m=j+1;
          	if(i != (listaEntidadesUsuarios.size()-1)) {
          	  j=listaEntidades.size();
            }
          } else {
          	a2.setAutorizacion("no");
          }
          listaEntidadesFinal.addElement(a2);
        } 	
      }
      if(listaEntidadesUsuarios.size() == 0) {
      	listaEntidadesFinal = listaEntidades;
      }

      rs.close();
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    return listaEntidadesFinal;
  }
  
  public int grabarListas(AutorizacionesExternasValueObject aeVO,String grabarPrimPest,String grabarSegPest,String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = "";
    int maxCod = 0;
    int resultado = 0;
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      if("si".equals(grabarPrimPest)) {
      	String codAplicacion = aeVO.getCodAplicacion();
      	Vector listaOrganizaciones = aeVO.getListaOrganizacionesUsuarios();
      	Vector listaEntidades = aeVO.getListaEntidadesUsuarios();
      	sql = "SELECT " + aae_org + "," + aae_ent + " FROM A_AAE WHERE " +
              aae_apl + "=" + codAplicacion;
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      	stmt = conexion.prepareStatement(sql);
      	rs = stmt.executeQuery();
      	Vector listaOrg = new Vector();
      	Vector listaEnt = new Vector();
      	while(rs.next()) {
      		String codOrganizacion = rs.getString(aae_org);
      		String codEntidad = rs.getString(aae_ent);
      		listaOrg.addElement(codOrganizacion);
      		listaEnt.addElement(codEntidad);
      	}
        rs.close();
        stmt.close();
        for(int i=0;i<listaOrg.size();i++) {
        	String entrar = "no";
        	for(int j=0;j<listaOrganizaciones.size();j++) {
        		String codOrg = (String) listaOrg.elementAt(i);
        		String codEnt = (String) listaEnt.elementAt(i);
        		String codOrganizacion = (String) listaOrganizaciones.elementAt(j);
        		String codEntidad = (String) listaEntidades.elementAt(j);
        		if(codOrg.equals(codOrganizacion) && codEnt.equals(codEntidad)) {
        			entrar = "si";
        		}
        	}
        	if("no".equals(entrar)) {
        		sql = "DELETE FROM A_EEA WHERE " +
                      eea_apl + "=" + codAplicacion;
	      		sql += " AND (" + aae_org + "=" + listaOrg.elementAt(i) + " AND " +
                       eea_ent + "=" + listaEnt.elementAt(i) + ")";
                  if(m_Log.isDebugEnabled()) m_Log.debug(sql);
			      stmt = conexion.prepareStatement(sql);
			      resultado = stmt.executeUpdate();
			      stmt.close();

                              CacheDatosFactoria.getImplParametrosBD().eliminarDatoClaveUnica((String)listaOrg.elementAt(i),
                                      (String)listaEnt.elementAt(i),codAplicacion);
                              
			      sql = "DELETE FROM A_UAE WHERE " +
                        uae_apl + "=" + codAplicacion;
		      	  sql += " AND (" + uae_org + "=" + listaOrg.elementAt(i) + " AND " +
                         uae_ent + "=" + listaEnt.elementAt(i) + ")";
                  if(m_Log.isDebugEnabled()) m_Log.debug(sql);
			      stmt = conexion.prepareStatement(sql);
			      resultado = stmt.executeUpdate();
			      stmt.close();
			      sql = "DELETE FROM A_AAE WHERE " +
                        aae_apl + "=" + codAplicacion;
		      	sql += " AND (" + aae_org + "=" + listaOrg.elementAt(i) + " AND " +
                       aae_ent + "=" + listaEnt.elementAt(i) + ")";
                  if(m_Log.isDebugEnabled()) m_Log.debug(sql);
			      stmt = conexion.prepareStatement(sql);
			      resultado = stmt.executeUpdate();
			      stmt.close();
                    sql = "DELETE FROM A_UGO WHERE "
                            + ugo_apl + "=" + codAplicacion;
                  sql += " AND (" + ugo_org + "=" + listaOrg.elementAt(i) + " AND "
                        + ugo_ent + "=" + listaEnt.elementAt(i) + ")";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                stmt = conexion.prepareStatement(sql);
                resultado = stmt.executeUpdate();
                stmt.close();
        	} else {
        	  resultado = 1;	
        	}
        }
        
      	for(int i=0;i<listaOrganizaciones.size();i++) {
		      sql = "SELECT " + aae_org + "," + aae_ent + " FROM A_AAE WHERE " +
                    aae_apl + "=" + codAplicacion + " AND " +
                    aae_org + "=" + listaOrganizaciones.elementAt(i) + " AND " + aae_ent +
                    "=" + listaEntidades.elementAt(i);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	      	stmt = conexion.prepareStatement(sql);
	      	rs = stmt.executeQuery();
	      	String entrar = "no";
	      	while(rs.next()) {
	      		entrar = "si";
	      	}
            rs.close();
            stmt.close();
            if("no".equals(entrar)) {
			    sql = "INSERT INTO A_AAE (" + aae_apl + "," + aae_org + "," +
                      aae_ent + ") VALUES (" + codAplicacion + "," + listaOrganizaciones.elementAt(i) + "," +
			  		  listaEntidades.elementAt(i) + ")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
			    stmt = conexion.prepareStatement(sql);
			    resultado = stmt.executeUpdate();
			    stmt.close();
		    } else {
		        resultado = 1;
		    }
        }
      }
      
      if("si".equals(grabarSegPest)) {
      	Vector listaUsuarios = aeVO.getListaUsuariosPrimera();
      	if(listaUsuarios != null) {
	      	if(listaUsuarios.size()!= 0) {
		      	sql = "DELETE FROM A_AAU WHERE " +
                      aau_usu + "=" + listaUsuarios.firstElement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		        stmt = conexion.prepareStatement(sql);
		        resultado = stmt.executeUpdate();
		        stmt.close();
		      	
		      	Vector listaAplicaciones = aeVO.getListaAplicacionesPrimera();
		      	if(listaAplicaciones != null) {
			      	if(listaAplicaciones.size() !=0) {
				      	for(int i=0;i<listaAplicaciones.size();i++) {
				      	  sql = "INSERT INTO A_AAU (" + aau_apl + "," + aau_usu + ") " +
                                "VALUES (" +
				      	        listaAplicaciones.elementAt(i) + "," + listaUsuarios.firstElement() + ")";
                              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
						      stmt = conexion.prepareStatement(sql);
						      resultado = stmt.executeUpdate();
						      stmt.close();     
				      	}
			        }
		        }
		      	Vector listaUsuariosSegunda = aeVO.getListaUsuariosSegunda();
		      	Vector listaAplicacionesSegunda = aeVO.getListaAplicacionesUsuariosSegunda();
		      	if(listaUsuariosSegunda != null && listaAplicacionesSegunda != null) {
			      	if(listaUsuariosSegunda.size()!=0 && listaAplicacionesSegunda.size() !=0) {
				      	sql = "DELETE FROM A_UAE WHERE " +
                              uae_usu + "=" + listaUsuariosSegunda.firstElement() + " AND " +
                              uae_apl + "=" + listaAplicacionesSegunda.firstElement();
                        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
				        stmt = conexion.prepareStatement(sql);
				        resultado = stmt.executeUpdate();
				        stmt.close();
			        }
		       }
		      	
		      	Vector listaOrganizaciones = aeVO.getListaOrganizacionesUsuariosSegunda();
		      	Vector listaEntidades = aeVO.getListaEntidadesUsuariosSegunda();
		      	if(listaOrganizaciones != null && listaEntidades !=null) {
			      	if(listaOrganizaciones.size() !=0 && listaEntidades.size()!=0) {
				      	for(int i=0;i<listaOrganizaciones.size();i++) {
				      		sql = "INSERT INTO A_UAE (" + uae_usu + "," + uae_org + "," +
                                  uae_ent + "," + uae_apl + ") VALUES ( " + listaUsuariosSegunda.firstElement() + "," +
				      		      listaOrganizaciones.elementAt(i) + "," + listaEntidades.elementAt(i) + "," + 
				      		      listaAplicacionesSegunda.elementAt(i) + ")";
                              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
						      stmt = conexion.prepareStatement(sql);
						      resultado = stmt.executeUpdate();
						      stmt.close();
				      	}
			        }
		        }
	       	}
        }
      }
      
      
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    return resultado;
  }
  
  public int grabarListasLocal(AutorizacionesExternasValueObject aeVO,String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = "";
    int maxCod = 0;
    int resultado = 0;
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);

      	Vector listaUsuarios = aeVO.getListaUsuariosPrimera();
      	if(listaUsuarios != null) {
	      	if(listaUsuarios.size()!= 0) {
		      	sql = "DELETE FROM A_AAU WHERE " +
                      aau_usu + "=" + listaUsuarios.firstElement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
		        stmt = conexion.prepareStatement(sql);
		        resultado = stmt.executeUpdate();
		        stmt.close();
		      	
		      	Vector listaAplicaciones = aeVO.getListaAplicacionesPrimera();
		      	if(listaAplicaciones != null) {
			      	if(listaAplicaciones.size() !=0) {
				      	for(int i=0;i<listaAplicaciones.size();i++) {
				      	  sql = "INSERT INTO A_AAU (" + aau_apl + "," + aau_usu + ") " +
                                "VALUES (" + listaAplicaciones.elementAt(i) + "," + listaUsuarios.firstElement() + ")";
                              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
						      stmt = conexion.prepareStatement(sql);
						      resultado = stmt.executeUpdate();
						      stmt.close();     
				      	}
			        }
		        }
		      	Vector listaUsuariosSegunda = aeVO.getListaUsuariosSegunda();
		      	Vector listaAplicacionesSegunda = aeVO.getListaAplicacionesUsuariosSegunda();
		      	if(listaUsuariosSegunda != null && listaAplicacionesSegunda != null) {
			      	if(listaUsuariosSegunda.size()!=0 && listaAplicacionesSegunda.size() !=0) {
				      	for(int i=0;i<listaAplicacionesSegunda.size();i++) {
					      	sql = "DELETE FROM A_UAE WHERE " +
                                  uae_usu + "=" + listaUsuariosSegunda.firstElement() + " AND " +
                                  uae_apl + "=" + listaAplicacionesSegunda.elementAt(i);
                            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
					        stmt = conexion.prepareStatement(sql);
					        resultado = stmt.executeUpdate();
                            stmt.close();
				        }
			        }
		       }
		      	
		      	Vector listaOrganizaciones = aeVO.getListaOrganizacionesUsuariosSegunda();
		      	Vector listaEntidades = aeVO.getListaEntidadesUsuariosSegunda();
		      	if(listaOrganizaciones != null && listaEntidades !=null) {
			      	if(listaOrganizaciones.size() !=0 && listaEntidades.size()!=0) {
				      	for(int i=0;i<listaAplicacionesSegunda.size();i++) {
				      		sql = "INSERT INTO A_UAE (" + uae_usu + "," + uae_org + "," +
                                  uae_ent + "," + uae_apl + ") VALUES ( " + listaUsuariosSegunda.firstElement() + "," +
				      		      listaOrganizaciones.firstElement() + "," + listaEntidades.firstElement() + "," + 
				      		      listaAplicacionesSegunda.elementAt(i) + ")";
                              if(m_Log.isDebugEnabled()) m_Log.debug(sql);
						      stmt = conexion.prepareStatement(sql);
						      resultado = stmt.executeUpdate();
						      stmt.close();
				      	}
			        }
		        }
	       	}
        }
      
      
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    return resultado;
  }
  
  public int grabarNombreBD(String codAplicacion,String codEntidad,String codOrganizacion,String baseDeDatos,String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = "";
    String entrar = "no";
    int resultado = 0;

    try{
        synchronized (AutorizacionesExternasDAO.class) {
            //m_Log.debug("A por el OAD");
            String[] parametros = (String []) params.clone();
            parametros[6] = campos.getString("CON.jndi");
            abd = new AdaptadorSQLBD(parametros);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            ParametrosBDVO parametrosBD = (ParametrosBDVO)CacheDatosFactoria.getImplParametrosBD().getDatoClaveUnica(codOrganizacion,
                codEntidad,codAplicacion);

            if (parametrosBD!=null) 
                entrar = "si";
            
            if("no".equals(entrar)) {
                        sql = "INSERT INTO A_EEA (aae_org,eea_apl,eea_ent,eea_eje,eea_est,eea_bde," +
                              "eea_vbd,eea_ubd,eea_pwd,eea_dbms,eea_arq,eea_log,eea_tra,eea_qto,eea_pwdbd," +
                              "eea_db,eea_odbc) VALUES (" + codOrganizacion + "," + codAplicacion + "," + 
                              codEntidad + ",0,'1','" + baseDeDatos + "','1', null, null, null, null" +
                              ",null,null,null,null, null, null)";
            } else {
                              sql = "UPDATE A_EEA SET eea_bde " + "='" + baseDeDatos + "' WHERE " +
                                    aae_org + "=" + codOrganizacion + " AND " +
                        eea_apl + "=" + codAplicacion + " AND " + eea_ent + "=" + codEntidad +
                        " AND " + eea_eje + "=0";
            }
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();

            if("no".equals(entrar)) {
              ParametrosBDVO dato = new ParametrosBDVO(Integer.valueOf(codOrganizacion),
                      Integer.valueOf(codEntidad),Integer.valueOf(codAplicacion),null,null,null,null,null,null,baseDeDatos);
              CacheDatosFactoria.getImplParametrosBD().insertarDato(dato);
            } else { 
              ParametrosBDVO datoNuevo = new ParametrosBDVO(0,0,0,null,null,null,null,null,null,baseDeDatos);
              CacheDatosFactoria.getImplParametrosBD().actualizarDatoClaveUnica(datoNuevo,codOrganizacion,
                      codEntidad,codAplicacion);
            }
        }
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
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

  private void commitTransaction(AdaptadorSQLBD bd,Connection con){
    try{
      bd.finTransaccion(con);
      bd.devolverConexion(con);
    }catch (Exception ex) {
      ex.printStackTrace();
      m_Log.error(ex.getMessage());
    }
  }

    public SortedMap cargaCacheParametrosBD(){ 
        
        Comparator comparador = new Comparator<ArrayList<String>>() {
            public int compare (ArrayList<String> listaA, ArrayList<String> listaB) {
                if (Integer.parseInt(listaA.get(0)) > Integer.parseInt(listaB.get(0)))
                    return 1;
                else if (Integer.parseInt(listaA.get(0)) < Integer.parseInt(listaB.get(0)))
                    return -1;
                else if (Integer.parseInt(listaA.get(1)) > Integer.parseInt(listaB.get(1)))
                    return 1;
                else if (Integer.parseInt(listaA.get(1)) < Integer.parseInt(listaB.get(1)))
                    return -1;
                else if (Integer.parseInt(listaA.get(2)) > Integer.parseInt(listaB.get(2)))
                    return 1;
                else if (Integer.parseInt(listaA.get(2)) < Integer.parseInt(listaB.get(2)))
                    return -1;
                else 
                    return 0;
            }
        };
        
        SortedMap listaParametrosBD = Collections.synchronizedSortedMap(new TreeMap<ArrayList<String>,ParametrosBDVO>(comparador));
        
        Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");

        String jndiD = m_ConfigTechnical.getString("CON.jndi");
        String gestorD = m_ConfigTechnical.getString("CON.gestor");
        String driverD = m_ConfigTechnical.getString("CON.driver");
        String urlD = m_ConfigTechnical.getString("CON.url");
        String usuarioD = m_ConfigTechnical.getString("CON.usuario");
        String passwordD = m_ConfigTechnical.getString("CON.password");
        String fichLogD = m_ConfigTechnical.getString("CON.fichlog");

        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String params[] = { gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            
            String sql = "SELECT AAE_ORG, EEA_ENT, EEA_APL, EEA_DBMS, EEA_DB, EEA_ODBC, EEA_UBD, " + 
                    "EEA_PWD, EEA_LOG, EEA_BDE FROM A_EEA ORDER BY AAE_ORG, EEA_ENT, EEA_APL";
            
            if(m_Log.isDebugEnabled()) m_Log.debug("CacheParametrosBD, sql: " + sql);
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ParametrosBDVO parametrosBD = new ParametrosBDVO();
                parametrosBD.setCodOrganizacion(rs.getInt("AAE_ORG"));
                parametrosBD.setCodEntidad(rs.getInt("EEA_ENT"));
                parametrosBD.setCodAplicacion(rs.getInt("EEA_APL"));
                parametrosBD.setGestor(rs.getString("EEA_DBMS"));
                parametrosBD.setDriver(rs.getString("EEA_DB"));
                parametrosBD.setUrl(rs.getString("EEA_ODBC"));
                parametrosBD.setUsuario(rs.getString("EEA_UBD"));
                parametrosBD.setPassword(rs.getString("EEA_PWD"));
                parametrosBD.setFichlog(rs.getString("EEA_LOG"));
                parametrosBD.setJndi(rs.getString("EEA_BDE"));
                ArrayList <String> clave = new ArrayList<String>(3); 
                clave.add(String.valueOf(parametrosBD.getCodOrganizacion()));
                clave.add(String.valueOf(parametrosBD.getCodEntidad()));
                clave.add(String.valueOf(parametrosBD.getCodAplicacion()));
                listaParametrosBD.put(clave,parametrosBD);
            }
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        } catch (Exception e) {
            m_Log.error(e.getMessage());
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(oad, con);
            } catch (Exception e) {
            m_Log.error(e.getMessage());
            }
        }
        return listaParametrosBD;
    }
   
  
}