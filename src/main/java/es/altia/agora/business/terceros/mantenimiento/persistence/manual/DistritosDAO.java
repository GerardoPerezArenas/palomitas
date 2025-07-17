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
 * <p>Descripción: DistritosDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class DistritosDAO  {
  private static DistritosDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(DistritosDAO.class.getName());

  protected DistritosDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static DistritosDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (DistritosDAO.class) {
        if (instance == null) {
          instance = new DistritosDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarDistrito(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    int cont = 0;
    Vector resultado = new Vector();
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      stmt = conexion.createStatement();
      
      sql = "SELECT * FROM T_SEC WHERE " + 
            campos.getString("SQL.T_SEC.pais")+"="+gVO.getAtributo("codPais")+" AND "+
		        campos.getString("SQL.T_SEC.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
		        campos.getString("SQL.T_SEC.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
		        campos.getString("SQL.T_SEC.distrito")+"="+gVO.getAtributo("codDistrito");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){ 
        cont++;
      }
      rs.close();
      if(m_Log.isDebugEnabled()) m_Log.debug("el cont es : " + cont);
      
      if(cont == 0) {
	      sql = "DELETE FROM T_DIS WHERE " + 
	        campos.getString("SQL.T_DIS.pais")+"="+gVO.getAtributo("codPais")+" AND "+
	        campos.getString("SQL.T_DIS.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
	        campos.getString("SQL.T_DIS.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
	        campos.getString("SQL.T_DIS.codigo")+"="+gVO.getAtributo("codDistrito");
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
      resultado = getListaDistritos(gVO,params);
    } else {
    	gVO.setAtributo("puedeEliminar","no");
    	resultado.addElement(gVO);
    }
    return resultado;
  }

  public Vector getListaDistritos(GeneralValueObject parametros,String[] params){
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
      sql = "SELECT T_DIS.* FROM T_DIS WHERE ";
      // CONDICIONES  
      sql+= campos.getString("SQL.T_DIS.pais")+"="+parametros.getAtributo("codPais")+" AND "+
        campos.getString("SQL.T_DIS.provincia")+"="+parametros.getAtributo("codProvincia")+" AND "+
        campos.getString("SQL.T_DIS.municipio")+"="+parametros.getAtributo("codMunicipio");
        
      String[] orden = {campos.getString("SQL.T_DIS.codigo"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        // DISTRITO
        gVO.setAtributo("codPais",rs.getString(campos.getString("SQL.T_DIS.pais")));
        gVO.setAtributo("codProvincia",rs.getString(campos.getString("SQL.T_DIS.provincia")));
        gVO.setAtributo("codMunicipio",rs.getString(campos.getString("SQL.T_DIS.municipio")));
        gVO.setAtributo("codDistrito",rs.getString(campos.getString("SQL.T_DIS.codigo")));
        gVO.setAtributo("descDistrito",rs.getString(campos.getString("SQL.T_DIS.nOficial")));
        gVO.setAtributo("nombreLargo",rs.getString(campos.getString("SQL.T_DIS.nLargo")));
        gVO.setAtributo("idObjetoGrafico",rs.getString(campos.getString("SQL.T_DIS.grafico")));
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
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en DistritosDAO.getListaDistritos");
        }
    }
    return resultado;
  }

  public Vector modificarDistrito(GeneralValueObject gVO, String[] params){
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
      sql = "UPDATE T_DIS SET " + 
        campos.getString("SQL.T_DIS.pais")+"="+gVO.getAtributo("codPais")+","+
        campos.getString("SQL.T_DIS.provincia")+"="+gVO.getAtributo("codProvincia")+","+ 
        campos.getString("SQL.T_DIS.municipio")+"="+gVO.getAtributo("codMunicipio")+","+
        campos.getString("SQL.T_DIS.codigo")+"="+gVO.getAtributo("codDistrito")+","+ 
        campos.getString("SQL.T_DIS.nOficial")+"='"+gVO.getAtributo("descDistrito")+"',"+
        campos.getString("SQL.T_DIS.nLargo")+"='"+gVO.getAtributo("nombreLargo")+"',"+ 
        campos.getString("SQL.T_DIS.grafico")+"='"+gVO.getAtributo("idObjetoGrafico")+"'"+
        " WHERE " + 
        campos.getString("SQL.T_DIS.pais")+"="+gVO.getAtributo("codPais")+" AND "+
        campos.getString("SQL.T_DIS.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
        campos.getString("SQL.T_DIS.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
        campos.getString("SQL.T_DIS.codigo")+"="+gVO.getAtributo("codDistritoAntiguo");
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
    resultado = getListaDistritos(gVO,params);
    return resultado;
  }

  public Vector altaDistrito(GeneralValueObject gVO, String[] params){
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

      sql = "INSERT INTO T_DIS("+
        campos.getString("SQL.T_DIS.pais")+","+
        campos.getString("SQL.T_DIS.provincia")+","+ 
        campos.getString("SQL.T_DIS.municipio")+","+
        campos.getString("SQL.T_DIS.codigo")+","+ 
        campos.getString("SQL.T_DIS.nOficial")+","+
        campos.getString("SQL.T_DIS.nLargo")+","+ 
        campos.getString("SQL.T_DIS.grafico")+
        ") VALUES (" +
        gVO.getAtributo("codPais")+","+
        gVO.getAtributo("codProvincia")+","+
        gVO.getAtributo("codMunicipio")+","+
        gVO.getAtributo("codDistrito")+",'"+
        gVO.getAtributo("descDistrito")+"','"+
        gVO.getAtributo("nombreLargo")+"','"+
        gVO.getAtributo("idObjetoGrafico")+"')";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
      //m_Log.debug("las filas afectadas en el insert son : " + res);
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    resultado = getListaDistritos(gVO,params);
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