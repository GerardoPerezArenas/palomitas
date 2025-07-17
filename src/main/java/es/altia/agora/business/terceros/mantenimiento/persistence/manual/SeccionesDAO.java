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
 * <p>Descripción: SeccionesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class SeccionesDAO  {
  private static SeccionesDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(SeccionesDAO.class.getName());

  protected SeccionesDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static SeccionesDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (SeccionesDAO.class) {
        if (instance == null) {
          instance = new SeccionesDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarSeccion(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    Vector resultado = new Vector();
    int cont =0;
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      stmt = conexion.createStatement();
      
      sql = "SELECT * FROM T_MZI WHERE " + 
            campos.getString("SQL.T_MZI.pais")+"="+gVO.getAtributo("codPais")+" AND "+
		        campos.getString("SQL.T_MZI.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
		        campos.getString("SQL.T_MZI.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
		        campos.getString("SQL.T_MZI.distrito")+"="+gVO.getAtributo("codDistrito")+" AND "+
		        campos.getString("SQL.T_MZI.seccion")+"="+gVO.getAtributo("codSeccion")+" AND "+
		        campos.getString("SQL.T_MZI.letraSeccion")+"='"+gVO.getAtributo("letraSeccion")+"'";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont++;
      }
      
      if(cont == 0) {
	      sql = "DELETE FROM T_SEC WHERE " + 
	        campos.getString("SQL.T_SEC.pais")+"="+gVO.getAtributo("codPais")+" AND "+
	        campos.getString("SQL.T_SEC.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
	        campos.getString("SQL.T_SEC.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
	        campos.getString("SQL.T_SEC.distrito")+"="+gVO.getAtributo("codDistrito")+" AND "+
	        campos.getString("SQL.T_SEC.codigo")+"="+gVO.getAtributo("codSeccion")+" AND "+
	        campos.getString("SQL.T_SEC.letra")+"='"+gVO.getAtributo("letraSeccion")+"'";
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
      resultado = getListaSecciones(gVO,params);
    } else {
    	gVO.setAtributo("puedeEliminar","no");
    	resultado.addElement(gVO);
    }
    return resultado;
  }

  public Vector getListaSecciones(GeneralValueObject parametros,String[] params){
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
      sql = "SELECT * FROM T_SEC WHERE ";
      // CONDICIONES  
      sql+= campos.getString("SQL.T_SEC.pais")+"="+parametros.getAtributo("codPais")+" AND "+
        campos.getString("SQL.T_SEC.provincia")+"="+parametros.getAtributo("codProvincia")+" AND "+
        campos.getString("SQL.T_SEC.municipio")+"="+parametros.getAtributo("codMunicipio")+" AND "+
        campos.getString("SQL.T_SEC.distrito")+"="+parametros.getAtributo("codDistrito");
        
      String[] orden = {campos.getString("SQL.T_SEC.codigo"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        // DISTRITO
        gVO.setAtributo("codPais",rs.getString(campos.getString("SQL.T_SEC.pais")));
        gVO.setAtributo("codProvincia",rs.getString(campos.getString("SQL.T_SEC.provincia")));
        gVO.setAtributo("codMunicipio",rs.getString(campos.getString("SQL.T_SEC.municipio")));
        gVO.setAtributo("codDistrito",rs.getString(campos.getString("SQL.T_SEC.distrito")));
        gVO.setAtributo("codSeccion",rs.getString(campos.getString("SQL.T_SEC.codigo")));
        gVO.setAtributo("letraSeccion",rs.getString(campos.getString("SQL.T_SEC.letra")));
        gVO.setAtributo("descSeccion",rs.getString(campos.getString("SQL.T_SEC.nOficial")));
        gVO.setAtributo("nombreLargo",rs.getString(campos.getString("SQL.T_SEC.nLargo")));
        gVO.setAtributo("idObjetoGrafico",rs.getString(campos.getString("SQL.T_SEC.grafico")));
        gVO.setAtributo("contadorHojas",rs.getString(campos.getString("SQL.T_SEC.contadorHojas")));
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
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en SeccionesDAO.getListaSecciones");
        }
    }
    return resultado;
  }

  public Vector modificarSeccion(GeneralValueObject gVO, String[] params){
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
      sql = "UPDATE T_SEC SET " + 
        campos.getString("SQL.T_SEC.pais")+"="+gVO.getAtributo("codPais")+","+
        campos.getString("SQL.T_SEC.provincia")+"="+gVO.getAtributo("codProvincia")+","+ 
        campos.getString("SQL.T_SEC.municipio")+"="+gVO.getAtributo("codMunicipio")+","+
        campos.getString("SQL.T_SEC.distrito")+"="+gVO.getAtributo("codDistrito")+","+ 
        campos.getString("SQL.T_SEC.codigo")+"="+gVO.getAtributo("codSeccion")+","+ 
        campos.getString("SQL.T_SEC.letra")+"='"+gVO.getAtributo("letraSeccion")+"',"+ 
        campos.getString("SQL.T_SEC.nOficial")+"='"+gVO.getAtributo("descSeccion")+"',"+
        campos.getString("SQL.T_SEC.nLargo")+"='"+gVO.getAtributo("nombreLargo")+"',"+ 
        campos.getString("SQL.T_SEC.grafico")+"='"+gVO.getAtributo("idObjetoGrafico")+"',"+
        campos.getString("SQL.T_SEC.contadorHojas")+"='"+gVO.getAtributo("contadorHojas")+"'"+
        " WHERE " + 
        campos.getString("SQL.T_SEC.pais")+"="+gVO.getAtributo("codPais")+" AND "+
        campos.getString("SQL.T_SEC.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
        campos.getString("SQL.T_SEC.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
        campos.getString("SQL.T_SEC.distrito")+"="+gVO.getAtributo("codDistrito")+" AND "+
        campos.getString("SQL.T_SEC.codigo")+"="+gVO.getAtributo("codSeccionAntiguo")+" AND "+
        campos.getString("SQL.T_SEC.letra")+"='"+gVO.getAtributo("letraSeccionAntigua")+"'";
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
    resultado = getListaSecciones(gVO,params);
    return resultado;
  }

  public Vector altaSeccion(GeneralValueObject gVO, String[] params){
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
      sql = "INSERT INTO T_SEC("+
        campos.getString("SQL.T_SEC.pais")+","+
        campos.getString("SQL.T_SEC.provincia")+","+ 
        campos.getString("SQL.T_SEC.municipio")+","+
        campos.getString("SQL.T_SEC.distrito")+","+ 
        campos.getString("SQL.T_SEC.codigo")+","+ 
        campos.getString("SQL.T_SEC.letra")+","+ 
        campos.getString("SQL.T_SEC.nOficial")+","+
        campos.getString("SQL.T_SEC.nLargo")+","+ 
        campos.getString("SQL.T_SEC.grafico")+","+
        campos.getString("SQL.T_SEC.contadorHojas")+
        ") VALUES (" +
        gVO.getAtributo("codPais")+","+
        gVO.getAtributo("codProvincia")+","+
        gVO.getAtributo("codMunicipio")+","+
        gVO.getAtributo("codDistrito")+","+
        gVO.getAtributo("codSeccion")+",'"+
        gVO.getAtributo("letraSeccion")+"','"+
        gVO.getAtributo("descSeccion")+"','"+
        gVO.getAtributo("nombreLargo")+"','"+
        gVO.getAtributo("idObjetoGrafico")+"',"+
        gVO.getAtributo("contadorHojas")+")";
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
    resultado = getListaSecciones(gVO,params);
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