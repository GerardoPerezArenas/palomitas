// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import java.util.Vector;
import java.sql.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.*;
import es.altia.util.conexion.*;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.LabelValueTO;
import java.util.ArrayList;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: IdiomasesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class IdiomasDAO  {
  private static IdiomasDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(IdiomasDAO.class.getName());

  protected IdiomasDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static IdiomasDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (IdiomasDAO.class) {
        if (instance == null) {
          instance = new IdiomasDAO();
        }
      }
    }
    return instance;
  }

  public Vector buscarUsuarios(String codigo, String[] params) {
	  Vector resultado = new Vector();
	  AdaptadorSQLBD abd = null;
	  Connection conexion = null;
	  Statement stmt = null;
	  ResultSet rs = null;
	  String sql = "";
	  
	  try {
	      abd = new AdaptadorSQLBD(params);
	      conexion = abd.getConnection();
	      // Creamos la select con los parametros adecuados.	      
	      //sql = "SELECT * FROM " + GlobalNames.ESQUEMA_GENERICO + "A_IDI A_IDI ";
	      sql = "SELECT " + campos.getString("SQL.A_USU.codigo") + ", " + campos.getString("SQL.A_USU.nombre") + 
	      		" FROM " + GlobalNames.ESQUEMA_GENERICO + " A_USU A_USU WHERE A_USU.USU_IDI = " + codigo;
	      String[] orden = {campos.getString("SQL.A_USU.codigo"), "1"};
	      sql += abd.orderUnion(orden);
	      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	      stmt = conexion.createStatement();
	      rs = stmt.executeQuery(sql);
	      while(rs.next()) {
	    	  GeneralValueObject gVO = new GeneralValueObject();
	    	  gVO.setAtributo("codigo", rs.getString(campos.getString("SQL.A_USU.codigo")));
	    	  gVO.setAtributo("nombre", rs.getString(campos.getString("SQL.A_USU.nombre")));
	    	  resultado.add(gVO);
	      }
	      rs.close();
	      stmt.close();
	  } catch (Exception e) {
		  try {
			  abd.devolverConexion(conexion);
	          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
	      } catch (Exception ex) {
	    	  ex.printStackTrace();
	          if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
	      }
	  } finally {
		  try {
			  abd.devolverConexion(conexion);
		  } catch (Exception e) {
			  e.printStackTrace();
	          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
	      }
	  }
	  
	  return resultado;
  }
  
  public Vector eliminarIdioma(String codigo, String[] params){
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
      stmt = conexion.createStatement();
      
      // Se inicia la transación
      abd.inicioTransaccion(conexion);
      
      // Se borra primero en las tablas en la que aparece 
      // como clave foránea      
      sql = "DELETE FROM A_AID WHERE " +
      campos.getString("SQL.A_AID.idioma")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
      
      sql = "DELETE FROM A_PID WHERE " +
      campos.getString("SQL.A_PID.idioma")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
      
      sql = "DELETE FROM A_TEX WHERE " +
      campos.getString("SQL.A_TEX.codIdi")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
      
      // Ahora se borra en la tabla de idiomas
      sql = "DELETE FROM A_IDI WHERE " +
        campos.getString("SQL.A_IDI.codigo")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
    
      stmt.close();
      // Se cierra la transación
      abd.finTransaccion(conexion);
      
    } catch (Exception e) {
    	try {
    		// Se deshace la transacción
    		abd.rollBack(conexion);
    	} catch (BDException ex) {
    		ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
    	}
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    } finally {
        try{
            abd.devolverConexion(conexion);
        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
    }
    Vector lista = new Vector();
    lista = getListaIdiomas(params);
    return lista;
  }
  
  	public Vector eliminarIdiomaConUsuarios(String codigoViejo, String codigoNuevo, String[] params) {
	    AdaptadorSQLBD abd = null;
	    Connection conexion = null;
	    Statement stmt = null;
	    String sql = "";
	    
	    try {
	      String[] parametros = (String []) params.clone();
	      parametros[6] = campos.getString("CON.jndi");
	      abd = new AdaptadorSQLBD(parametros);
	      conexion = abd.getConnection();
	      stmt = conexion.createStatement();
	      
	      // Se inicia la transación
	      abd.inicioTransaccion(conexion);
	      
	      // Se cambia el idioma a borrar por el seleccionado    
	      sql = "UPDATE A_USU SET " + campos.getString("SQL.A_USU.idioma") + " = " + codigoNuevo + 
	      " WHERE " + campos.getString("SQL.A_USU.idioma") + " = " + codigoViejo;
	      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	      stmt.executeUpdate(sql);
	    
	      // Se borra primero en las tablas en la que aparece como clave foránea
	      sql = "DELETE FROM A_AID WHERE " +
	      campos.getString("SQL.A_AID.idioma") + " = " + codigoViejo;
	      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	      stmt.executeUpdate(sql);
	      
	      sql = "DELETE FROM A_PID WHERE " +
	      campos.getString("SQL.A_PID.idioma") + " = " + codigoViejo;
	      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	      stmt.executeUpdate(sql);
	      
	      sql = "DELETE FROM A_TEX WHERE " +
	      campos.getString("SQL.A_TEX.codIdi") + " = " + codigoViejo;
	      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	      stmt.executeUpdate(sql);
	      
	      sql = "DELETE FROM A_USU WHERE " +
	        campos.getString("SQL.A_USU.idioma") + " = " + codigoViejo;      
	      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	      stmt.executeUpdate(sql);	      
	      
	      // Ahora se borra en la tabla de idiomas
	      sql = "DELETE FROM A_IDI WHERE " +
	      campos.getString("SQL.A_IDI.codigo") + " = " + codigoViejo;      
	      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	      stmt.executeUpdate(sql);
	      
      stmt.close();
	      // Se cierra la transación
	      abd.finTransaccion(conexion);
	      
    }catch (Exception e){
	    	try {
	    		// Se deshace la transacción
	    		abd.rollBack(conexion);
	    	} catch (BDException ex) {
	    		ex.printStackTrace();
	            if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
	    	}
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
    lista = getListaIdiomas(params);
    return lista;
  }

  /**
   * Recupera todos los idiomas disponibles en la base de datos exceptuando el que se le pasa como parámetro
   * @param params
   * @param idIdioma
   * @return
   */
  public ArrayList<LabelValueTO> getListaIdiomasLabel(String[] params,int idIdioma){
    ArrayList<LabelValueTO> resultado = new ArrayList<LabelValueTO>();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try{       
       
      abd = new AdaptadorSQLBD(params);
      conexion = abd.getConnection();
      // Creamos la select con los parametros adecuados.
      sql = "SELECT * FROM " + GlobalNames.ESQUEMA_GENERICO + "A_IDI A_IDI ";
      sql+= "WHERE " + campos.getString("SQL.A_IDI.codigo") + "!=" + idIdioma + " ";
      String[] orden = {campos.getString("SQL.A_IDI.codigo"),"1"};
      sql += abd.orderUnion(orden);
                  
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      m_Log.debug("?????????? getListaIdiomasLabel - sql: " + sql);
      m_Log.debug("?????????? getListaIdiomasLabel - idiomaActual: " + idIdioma);
      
      rs = stmt.executeQuery(sql);
                        
      while(rs.next()){
        LabelValueTO gVO = new LabelValueTO();
        gVO.setValue(rs.getString(campos.getString("SQL.A_IDI.codigo")));
        gVO.setLabel(rs.getString(campos.getString("SQL.A_IDI.descripcion"))); 
        resultado.add(gVO);
      }
      rs.close();
      stmt.close();
    }catch (Exception e){
        try{
             abd.devolverConexion(conexion);
             if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }catch (Exception ex){
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
        }
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
  
  
   public Vector getListaIdiomas(String[] params){
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try{
      abd = new AdaptadorSQLBD(params);
      conexion = abd.getConnection();
      // Creamos la select con los parametros adecuados.
      sql = "SELECT * FROM " + GlobalNames.ESQUEMA_GENERICO + "A_IDI A_IDI ";
      String[] orden = {campos.getString("SQL.A_IDI.codigo"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.A_IDI.codigo")));
        gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.A_IDI.descripcion")));
         gVO.setAtributo("clave",rs.getString("IDI_CLAVE"));
        resultado.add(gVO);
      }
      rs.close();
      stmt.close();
    }catch (Exception e){
        try{
             abd.devolverConexion(conexion);
             if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }catch (Exception ex){
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
        }
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
  

  public Vector modificarIdioma(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    try{
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      conexion = abd.getConnection();
      sql = "UPDATE A_IDI SET " +
        campos.getString("SQL.A_IDI.descripcion")+"='"+gVO.getAtributo("descripcion")+"',"+
        " IDI_CLAVE='"+gVO.getAtributo("clave")+"' WHERE " +
        campos.getString("SQL.A_IDI.codigo")+"="+gVO.getAtributo("codigo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
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
    lista = getListaIdiomas(params);
    return lista;
  }

  public Vector altaIdioma(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try{
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      conexion = abd.getConnection();
      sql = "INSERT INTO A_IDI("+
       campos.getString("SQL.A_IDI.codigo")+","+
       campos.getString("SQL.A_IDI.descripcion")+
       ",IDI_CLAVE) VALUES (" +
       gVO.getAtributo("codigo") +",'" +
       gVO.getAtributo("descripcion") +"','" +
       gVO.getAtributo("clave") + "')";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
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
    lista = getListaIdiomas(params);
    return lista;
   }

    private void rollBackTransaction(AdaptadorSQLBD bd,Connection con,Exception e){
      try {
          bd.rollBack(con);
          bd.devolverConexion(con);
      }
      catch (Exception e1) {
          try{
              bd.devolverConexion(con);
          } catch(Exception ex){
              ex.printStackTrace();
              m_Log.error(ex.getMessage());
          }
          e1.printStackTrace();
          m_Log.error(e1.getMessage());
      }
      finally {
        e.printStackTrace();
        m_Log.error(e.getMessage());
      }
    }

    private void commitTransaction(AdaptadorSQLBD bd,Connection con){
      try{
        bd.finTransaccion(con);
        bd.devolverConexion(con);
      }
      catch (Exception ex) {
        try{
            bd.devolverConexion(con);
        } catch(Exception e){
            e.printStackTrace();
            m_Log.error(e.getMessage());
        }
        ex.printStackTrace();
        m_Log.error(ex.getMessage());
      }
    }
    
    
    
    /**
   * Recupera la descripción del idioma de la BD
   * @param params: Parámetros de conexión
   * @param idIdioma: Identificador del idioma
   * @return: String
   */
   public String getDescripcionIdioma(String[] params,int idIdioma){
    String resultado = null;
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try{
      abd = new AdaptadorSQLBD(params);
      conexion = abd.getConnection();
      // Creamos la select con los parametros adecuados.
      sql  = "SELECT IDI_NOM FROM " + GlobalNames.ESQUEMA_GENERICO + "A_IDI A_IDI ";
      sql += "WHERE " + campos.getString("SQL.A_IDI.codigo") + "=" + idIdioma;
      
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){        
        resultado = rs.getString(campos.getString("SQL.A_IDI.descripcion"));        
      }
      
      rs.close();
      stmt.close();
    }catch (Exception e){
        try{
             abd.devolverConexion(conexion);
             if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }catch (Exception ex){
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
        }
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

    /**
   * Recupera la clave del idioma para su uso en construccion de informes con XSL
   * @param params: Parámetros de conexión
   * @param idIdioma: Identificador del idioma
   * @return: String
   */
   public String getClaveIdioma(String[] params,int idIdioma){
    String resultado = null;
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try{
      abd = new AdaptadorSQLBD(params);
      conexion = abd.getConnection();
      // Creamos la select con los parametros adecuados.
      sql  = "SELECT IDI_CLAVE FROM " + GlobalNames.ESQUEMA_GENERICO + "A_IDI A_IDI ";
      sql += "WHERE " + campos.getString("SQL.A_IDI.codigo") + "=" + idIdioma;

      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        resultado = rs.getString("IDI_CLAVE");
      }

      rs.close();
      stmt.close();
    }catch (Exception e){
        try{
             abd.devolverConexion(conexion);
             if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }catch (Exception ex){
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
        }
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


}