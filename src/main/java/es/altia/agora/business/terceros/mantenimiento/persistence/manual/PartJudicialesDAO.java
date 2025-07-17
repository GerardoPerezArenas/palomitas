// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.util.GeneralValueObject;
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
 * <p>Descripción: ManzanasDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class PartJudicialesDAO  {
  private static PartJudicialesDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(PartJudicialesDAO.class.getName());

  protected PartJudicialesDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static PartJudicialesDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (PartJudicialesDAO.class) {
        if (instance == null) {
          instance = new PartJudicialesDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarPartJudiciales(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    Vector resultado = new Vector();
    int cont = 0;
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      stmt = conexion.createStatement();
      
      sql = "SELECT * FROM T_MUN WHERE " + 
            campos.getString("SQL.T_MUN.idPais")+"="+gVO.getAtributo("codPais")+" AND "+
		        campos.getString("SQL.T_MUN.idProvincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
		        campos.getString("SQL.T_MUN.partidoJudicial")+"="+gVO.getAtributo("codPartJudicial");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont++;
      }
      rs.close();
      
      
      if(cont == 0) {
	      sql = "DELETE FROM T_PJU WHERE " + 
	        campos.getString("SQL.T_PJU.idPais")+"="+gVO.getAtributo("codPais")+" AND "+
	        campos.getString("SQL.T_PJU.idProvincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
	        campos.getString("SQL.T_PJU.idCodigo")+"="+gVO.getAtributo("codPartJudicial"); 
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	      stmt.executeUpdate(sql);
      } 
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    if(cont == 0) {
      resultado = getListaPartJudiciales(gVO,params);
    } else {
    	gVO.setAtributo("puedeEliminar","no");
    	resultado.addElement(gVO);
    }
    return resultado;
  }

  public Vector getListaPartJudiciales(GeneralValueObject parametros,String[] params){
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
      sql = "SELECT * FROM T_PJU WHERE ";
      // CONDICIONES  
      sql+= campos.getString("SQL.T_PJU.idPais")+"="+parametros.getAtributo("codPais")+" AND "+
        campos.getString("SQL.T_PJU.idProvincia")+"="+parametros.getAtributo("codProvincia");      
      String[] orden = {campos.getString("SQL.T_PJU.idCodigo"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        // DISTRITO
        gVO.setAtributo("codPais",rs.getString(campos.getString("SQL.T_PJU.idPais")));
        gVO.setAtributo("codProvincia",rs.getString(campos.getString("SQL.T_PJU.idProvincia")));
        gVO.setAtributo("codPartJudicial",rs.getString(campos.getString("SQL.T_PJU.idCodigo")));
        gVO.setAtributo("nombre",rs.getString(campos.getString("SQL.T_PJU.nombre")));
        gVO.setAtributo("nombreLargo",rs.getString(campos.getString("SQL.T_PJU.nombreLargo")));
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
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en PartJudicialesDAO.getListaPartJudiciales");
        }
    }
    return resultado;
  }

  public Vector modificarPartJudiciales(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    Vector resultado = new Vector();
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      sql = "UPDATE T_PJU SET " + 
        campos.getString("SQL.T_PJU.nombre")+"='"+gVO.getAtributo("nombre")+"',"+
        campos.getString("SQL.T_PJU.nombreLargo")+"='"+gVO.getAtributo("nombreLargo")+
        "' WHERE " + 
        campos.getString("SQL.T_PJU.idPais")+"="+gVO.getAtributo("codPais")+" AND "+
        campos.getString("SQL.T_PJU.idProvincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
        campos.getString("SQL.T_PJU.idCodigo")+"="+gVO.getAtributo("codPartJudicial");
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
    resultado = getListaPartJudiciales(gVO,params);
    return resultado;
  }

  public Vector altaPartJudiciales(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    Vector resultado = new Vector();
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      sql = "INSERT INTO T_PJU("+
        campos.getString("SQL.T_PJU.idPais")+","+
        campos.getString("SQL.T_PJU.idProvincia")+","+ 
        campos.getString("SQL.T_PJU.idCodigo")+","+
        campos.getString("SQL.T_PJU.nombre")+","+ 
        campos.getString("SQL.T_PJU.nombreLargo")+
        ") VALUES (" +
        gVO.getAtributo("codPais")+","+
        gVO.getAtributo("codProvincia")+","+
        gVO.getAtributo("codPartJudicial")+",'"+
        gVO.getAtributo("nombre")+"','"+
        gVO.getAtributo("nombreLargo")+"')";
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
    resultado = getListaPartJudiciales(gVO,params);
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
  
}