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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Vector;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: ProcesosDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: ALTIA CONSULTORES</p>
 */

public class ProcesosDAO  {
  private static ProcesosDAO instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(ProcesosDAO.class.getName());
  
  protected static String pro_apl;
  protected static String pro_cod;
  protected static String pro_frm;
  protected static String pro_tpr;
  
  protected static String pid_apl;
  protected static String pid_pro;
  protected static String pid_idi;
  protected static String pid_des;
  
  protected static String idi_cod;
  protected static String idi_nom;
  
  protected static String rpg_apl;
  protected static String rpg_pro;

  protected static String rpu_apl;
  protected static String rpu_pro;
  
  protected static String mor_apl;
  protected static String mor_pro;
  
 
  protected ProcesosDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");

   pro_apl = m_ConfigTechnical.getString("SQL.A_PRO.aplicacion");
   pro_cod = m_ConfigTechnical.getString("SQL.A_PRO.codigo");
   pro_frm = m_ConfigTechnical.getString("SQL.A_PRO.formulario");
   pro_tpr = m_ConfigTechnical.getString("SQL.A_PRO.tipoProceso");
   
   pid_apl = m_ConfigTechnical.getString("SQL.A_PID.aplicacion");
   pid_pro = m_ConfigTechnical.getString("SQL.A_PID.proceso");
   pid_idi = m_ConfigTechnical.getString("SQL.A_PID.idioma");
   pid_des = m_ConfigTechnical.getString("SQL.A_PID.descripcion");
   
   idi_cod = m_ConfigTechnical.getString("SQL.A_IDI.codigo");
   idi_nom = m_ConfigTechnical.getString("SQL.A_IDI.descripcion");
   
   rpg_apl  = m_ConfigTechnical.getString("SQL.A_RPG.aplicacion");
   rpg_pro  = m_ConfigTechnical.getString("SQL.A_RPG.proceso");
   	
   rpu_apl  = m_ConfigTechnical.getString("SQL.A_RPU.aplicacion");
   rpu_pro  = m_ConfigTechnical.getString("SQL.A_RPU.proceso"); 
   
   mor_apl = m_ConfigTechnical.getString("SQL.A_MOR.aplicacion");
   mor_pro = m_ConfigTechnical.getString("SQL.A_MOR.proceso");
	
  }

  public static ProcesosDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (ProcesosDAO.class) {
        if (instance == null) {
          instance = new ProcesosDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarProceso(GeneralValueObject gVO, String[] params){
  	if ( gVO.getAtributo("codAplicacion")!=null && gVO.getAtributo("codProceso")!=null) {
  		String apl = (String) gVO.getAtributo("codAplicacion");
  		String cod = (String) gVO.getAtributo("codProceso");
	    AdaptadorSQLBD abd = null;
	    Connection conexion = null;
	    Statement stmt = null;
	    String sql = "";
	    try{
	      //m_Log.debug("A por el OAD");
          String[] parametros = (String []) params.clone();
          parametros[6] = m_ConfigTechnical.getString("CON.jndi");
	      abd = new AdaptadorSQLBD(parametros);
	      //m_Log.debug("A por la conexion");
	      conexion = abd.getConnection();
	      abd.inicioTransaccion(conexion);
	      stmt = conexion.createStatement();
	      sql = "DELETE FROM A_RPG WHERE " +
	        rpg_apl+"="+apl+" AND " + rpg_pro+"="+cod;
	      stmt.executeUpdate(sql);
	      stmt.close();	      
	      stmt = conexion.createStatement();
	      sql = "DELETE FROM A_RPU WHERE " +
	        rpu_apl+"="+apl+" AND " + rpu_pro+"="+cod;
	      stmt.executeUpdate(sql);
	      stmt.close();	      	      
	      stmt = conexion.createStatement();
	      sql = "DELETE FROM A_MOR WHERE " +
	        mor_apl+"="+apl+" AND " + mor_pro+"="+cod;
	      stmt.executeUpdate(sql);
	      stmt.close();	      	      	  
	      stmt = conexion.createStatement();
	      sql = "DELETE FROM A_PID WHERE " +
	        pid_apl+"="+apl+" AND " + pid_pro+"="+cod;
	      stmt.executeUpdate(sql);
	      stmt.close();
	      stmt = conexion.createStatement();
	      sql = "DELETE FROM A_PRO WHERE " +
	        pro_apl+"="+apl+" AND " + pro_cod+"="+cod;
	      stmt.executeUpdate(sql);
	      stmt.close();	      
	    }catch (Exception e){
	      rollBackTransaction(abd,conexion,e);
	    }finally{
	      commitTransaction(abd,conexion);
	    }
	 }
	 Vector lista = new Vector();
	 lista = getListaProcesos(gVO,params);
	 return lista;
  }

  public Vector getListaProcesos(GeneralValueObject gVO1,String[] params){
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    
    if (gVO1.getAtributo("codAplicacion") != null) {
    	String aplicacion = (String) gVO1.getAtributo("codAplicacion");	
    	if (!"".equals(aplicacion)) {		  
		    try{
		      //m_Log.debug("A por el OAD");
		      abd = new AdaptadorSQLBD(params);
		      //m_Log.debug("A por la conexion");
		      conexion = abd.getConnection();
		      // Creamos la select con los parametros adecuados.
		      sql = "SELECT " + pro_cod + "," + pro_frm + "," + pid_idi + "," + idi_nom + "," + pid_des + " FROM " +
                    GlobalNames.ESQUEMA_GENERICO + "A_PRO, " + GlobalNames.ESQUEMA_GENERICO + "A_PID, " +
                    GlobalNames.ESQUEMA_GENERICO + "A_IDI WHERE " + GlobalNames.ESQUEMA_GENERICO + "a_pro."
		      	    + pro_apl + "=" + GlobalNames.ESQUEMA_GENERICO + "a_pid." + pid_apl + " AND " +
                    GlobalNames.ESQUEMA_GENERICO + "a_pro." + pro_cod + "=" + GlobalNames.ESQUEMA_GENERICO + "a_pid." + pid_pro
		      	    + " AND " + GlobalNames.ESQUEMA_GENERICO + "a_pid." + pid_idi + "=" + GlobalNames.ESQUEMA_GENERICO +
                    "a_idi." + idi_cod + " AND " + pro_apl + "=" + gVO1.getAtributo("codAplicacion");
		      String[] orden = {pro_cod,"1"};
		      sql += abd.orderUnion(orden);
		      stmt = conexion.createStatement();
		      rs = stmt.executeQuery(sql);      
		      GeneralValueObject gVO= new GeneralValueObject();
		      GeneralValueObject iVO= new GeneralValueObject();
		      Vector listaDescripciones = new Vector();
		      
		      while(rs.next()){
		      	String codigo = rs.getString(pro_cod);
		      	if (codigo != null) 
		      		if (!codigo.equals(gVO.getAtributo("codigo"))) {
		      			gVO= new GeneralValueObject();
		      			gVO.setAtributo("codigo",codigo);
		      			gVO.setAtributo("formulario",rs.getString(pro_frm));
		      			listaDescripciones = new Vector();
		      			gVO.setAtributo("descripcionesIdioma", listaDescripciones);
		      			iVO= new GeneralValueObject();
		      			iVO.setAtributo("codigoIdioma",rs.getString(pid_idi));
		      			iVO.setAtributo("descripcionIdioma",rs.getString(idi_nom));
		      			iVO.setAtributo("descripcion",rs.getString(pid_des));
		      			listaDescripciones.addElement(iVO);
		      			resultado.add(gVO);
		      		} else {
		      			iVO= new GeneralValueObject();
		      			iVO.setAtributo("codigoIdioma",rs.getString(pid_idi));
		      			iVO.setAtributo("descripcionIdioma",rs.getString(idi_nom));
		      			iVO.setAtributo("descripcion",rs.getString(pid_des));
		      			listaDescripciones.addElement(iVO);
		      		}        
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
		}
	}	
    return resultado;
  }

  public Vector modificarProceso(GeneralValueObject gVO, String[] params){
        
  	if ( gVO.getAtributo("codAplicacion")!=null && gVO.getAtributo("codProceso")!=null) {
  		String apl = (String) gVO.getAtributo("codAplicacion");
  		String cod = (String) gVO.getAtributo("codProceso");
	
	    AdaptadorSQLBD abd = null;
	    Connection conexion = null;
            PreparedStatement stmt = null;
	    String sql = "";
	    try{
	      //m_Log.debug("A por el OAD");
          String[] parametros = (String []) params.clone();
          parametros[6] = m_ConfigTechnical.getString("CON.jndi");
	      abd = new AdaptadorSQLBD(parametros);
	      //m_Log.debug("A por la conexion");
	      conexion = abd.getConnection();
	      abd.inicioTransaccion(conexion);
                sql = "UPDATE A_PRO SET PRO_FRM = ? WHERE PRO_APL = ? AND PRO_COD = ?";
                stmt = conexion.prepareStatement(sql);
                
                if (gVO.getAtributo("formulario") != null) stmt.setString(1, (String)gVO.getAtributo("formulario"));
                else stmt.setNull(1, Types.VARCHAR);
                stmt.setInt(2, Integer.parseInt(apl));
                stmt.setInt(3, Integer.parseInt(cod));
                
                stmt.executeUpdate();
	      stmt.close();
                
                sql = "DELETE FROM A_PID WHERE PID_APL = ? AND PID_PRO = ?";
                stmt = conexion.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(apl));
                stmt.setInt(2, Integer.parseInt(cod));
                
                stmt.executeUpdate();
	      stmt.close();	      
                
		  // Insertar descripciones.
      	  Vector di = (Vector) gVO.getAtributo("idiomasDescripciones");
                if (di != null) {
      	  for (int i=0; i<di.size();i++) {
      		Vector datos = (Vector) di.elementAt(i);
                        sql = "INSERT INTO A_PID(PID_APL, PID_PRO, PID_IDI, PID_DES) VALUES (?, ?, ?, ?)";
                        stmt = conexion.prepareStatement(sql);
                        stmt.setInt(1, Integer.parseInt(apl));
                        stmt.setInt(2, Integer.parseInt(cod));
                        stmt.setInt(3, Integer.parseInt((String)datos.elementAt(0)));
                        stmt.setString(4, (String)datos.elementAt(1));
                        
                        stmt.executeUpdate();
		    stmt.close();      		
      	  } 
                }
	      //m_Log.debug("las filas afectadas en el update son : " + res);
	    }catch (Exception e){
	      rollBackTransaction(abd,conexion,e);
	    }finally{
	      commitTransaction(abd,conexion);
	    }
	}
    Vector lista = new Vector();
    lista = getListaProcesos(gVO,params);
    return lista;
  }

  public Vector altaProceso(GeneralValueObject gVO, String[] params){
  	String apl = (String) gVO.getAtributo("codAplicacion");
  	String codProc = "";
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = m_ConfigTechnical.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      // Buscar codigo proceso.

      sql = "SELECT "+abd.funcionMatematica(AdaptadorSQLBD.FUNCIONMATEMATICA_MAX,new String[]{pro_cod})+"+1 AS codigo " +
            "FROM A_PRO WHERE " + pro_apl +"="+ apl;
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      if (rs.next()){
      	codProc = rs.getString("codigo");
      	if (codProc == null) codProc="0";
      } else codProc ="0"; 
      rs.close();
      stmt.close();      
      
      // Insertar.
      sql = "INSERT INTO A_PRO("+ pro_apl+","+pro_cod +","+pro_frm+","+pro_tpr+") " +
            "VALUES (" + apl + "," + codProc + "," + (gVO.getAtributo("formulario")!=null?"'"+
            (String)gVO.getAtributo("formulario")+"'":" null ") +",1)";

      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
      
      // Insertar descripciones.
      Vector di = (Vector) gVO.getAtributo("idiomasDescripciones");
      if (di != null) 
      	for (int i=0; i<di.size();i++) {
      		Vector datos = (Vector) di.elementAt(i);
		    sql = "INSERT INTO "+ GlobalNames.ESQUEMA_GENERICO + "A_PID("+ pid_apl+","+pid_pro +","+pid_idi+","+pid_des+") " +
                  "VALUES (" + apl + "," + codProc +"," + datos.elementAt(0) + ",'" + datos.elementAt(1)+"')";
		    stmt = conexion.createStatement();
		    stmt.executeUpdate(sql);
		    stmt.close();      		
      	}           
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    Vector lista = new Vector();
    lista = getListaProcesos(gVO,params);
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