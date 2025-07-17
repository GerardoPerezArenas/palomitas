// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: EscalerasesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class EscalerasDAO  {
  private static EscalerasDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
            LogFactory.getLog(EscalerasDAO.class.getName());

  protected EscalerasDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static EscalerasDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (EscalerasDAO.class) {
        if (instance == null) {
          instance = new EscalerasDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarEscalera(String codigo, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    AdaptadorSQLBD oad = null;
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    int cont = 0;
    int cont1 = 0;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      stmt = conexion.createStatement();
      
      sql = "SELECT * FROM T_DPO WHERE " + 
            campos.getString("SQL.T_DPO.escalera")+"='"+codigo+"'";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont++;
      }
      rs.close();
      
      sql = "SELECT * FROM T_DNN WHERE " + 
            campos.getString("SQL.T_DNN.escalera")+"='"+codigo+"'";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont1++;
      }
      rs.close();
      
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      oad = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      con = oad.getConnection();
      stmt = con.createStatement();

      if(cont == 0 && cont1 == 0) {
	      sql = "DELETE FROM T_ESC WHERE " + 
	        campos.getString("SQL.T_ESC.codigo")+"='"+codigo+"'";
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	      stmt.executeUpdate(sql);
      }
      stmt.close();
    }catch (Exception e){
        e.printStackTrace();
    }finally{
        try {
            oad.devolverConexion(con);
            abd.devolverConexion(conexion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    Vector lista = new Vector();
    if(cont == 0 && cont1 == 0) {
      lista = getListaEscaleras(params);
    } else {
    	GeneralValueObject gVO = new GeneralValueObject();
    	gVO.setAtributo("puedeEliminar","no");
    	lista.addElement(gVO);
    }
    return lista;
  }

  public Vector getListaEscaleras(String[] params){
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
//      abd.inicioTransaccion(conexion);
      // Creamos la select con los parametros adecuados.
      sql = "SELECT * FROM " + GlobalNames.ESQUEMA_GENERICO + "T_ESC ";
      String[] orden = {campos.getString("SQL.T_ESC.codigo"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.T_ESC.codigo")));
        gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.T_ESC.descripcion")));
        resultado.add(gVO);
      }
      rs.close();
      stmt.close();
    }catch (Exception e){
//      rollBackTransaction(abd,conexion,e);
        e.printStackTrace();
        if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
        try {
//      commitTransaction(abd,conexion);
            abd.devolverConexion(conexion);
        }catch (BDException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en EscalerasDAO.getListaEscaleras");
        }
    }
    return resultado;
  }

  public Vector modificarEscalera(GeneralValueObject gVO, String[] params){
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
      abd.inicioTransaccion(conexion);
      sql = "UPDATE T_ESC SET " + 
        campos.getString("SQL.T_ESC.codigo")+"='"+gVO.getAtributo("codigo")+"',"+ 
        campos.getString("SQL.T_ESC.descripcion")+"='"+gVO.getAtributo("descripcion")+"'"+
        " WHERE " + 
        campos.getString("SQL.T_ESC.codigo")+"='"+gVO.getAtributo("codigoAntiguo")+"'";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      int res = stmt.executeUpdate(sql);
      //m_Log.debug("las filas afectadas en el update son : " + res);
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    Vector lista = new Vector();
    lista = getListaEscaleras(params);
    return lista;
  }

  public Vector altaEscalera(GeneralValueObject gVO, String[] params){
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
      abd.inicioTransaccion(conexion);
      sql = "INSERT INTO T_ESC("+
       campos.getString("SQL.T_ESC.codigo")+","+ 
       campos.getString("SQL.T_ESC.descripcion")+ 
       ") VALUES ('" + 
       gVO.getAtributo("codigo") +"','" + 
       gVO.getAtributo("descripcion") + "')";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      //m_Log.debug("las filas afectadas en el insert son : " + res);
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    Vector lista = new Vector();
    lista = getListaEscaleras(params);
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