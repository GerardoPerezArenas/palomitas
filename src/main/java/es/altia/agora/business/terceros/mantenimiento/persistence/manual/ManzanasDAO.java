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

public class ManzanasDAO  {
  private static ManzanasDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
            LogFactory.getLog(ManzanasDAO.class.getName());

  protected ManzanasDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static ManzanasDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (ManzanasDAO.class) {
        if (instance == null) {
          instance = new ManzanasDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarManzana(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    Vector resultado = new Vector();
    int cont = 0;
    int cont1 = 0;
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      stmt = conexion.createStatement();
      
      sql = "SELECT * FROM T_DSU WHERE " + 
            campos.getString("SQL.T_DSU.distritoPais")+"="+gVO.getAtributo("codPais")+" AND "+
		        campos.getString("SQL.T_DSU.distritoProvincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
		        campos.getString("SQL.T_DSU.distritoMunicipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
		        campos.getString("SQL.T_DSU.distrito")+"="+gVO.getAtributo("codDistrito")+" AND "+
		        campos.getString("SQL.T_DSU.seccion")+"="+gVO.getAtributo("codSeccion")+" AND "+
		        campos.getString("SQL.T_DSU.letra")+"='"+gVO.getAtributo("letraSeccion")+"' AND "+
		        campos.getString("SQL.T_DSU.manzanaINE")+"="+gVO.getAtributo("codManzana");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont++;
      }
      rs.close();
      
      sql = "SELECT * FROM T_TRM WHERE " + 
            campos.getString("SQL.T_TRM.paisManzana")+"="+gVO.getAtributo("codPais")+" AND "+
		        campos.getString("SQL.T_TRM.provinciaManzana")+"="+gVO.getAtributo("codProvincia")+" AND "+
		        campos.getString("SQL.T_TRM.municipioManzana")+"="+gVO.getAtributo("codMunicipio")+" AND "+
		        campos.getString("SQL.T_TRM.distritoManzana")+"="+gVO.getAtributo("codDistrito")+" AND "+
		        campos.getString("SQL.T_TRM.seccionManzana")+"="+gVO.getAtributo("codSeccion")+" AND "+
		        campos.getString("SQL.T_TRM.letraManzana")+"='"+gVO.getAtributo("letraSeccion")+"' AND "+
		        campos.getString("SQL.T_TRM.manzana")+"="+gVO.getAtributo("codManzana");   
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont1++;
      }
      rs.close();
      
      if(cont == 0 && cont1 == 0) {
	      sql = "DELETE FROM T_MZI WHERE " + 
	        campos.getString("SQL.T_MZI.pais")+"="+gVO.getAtributo("codPais")+" AND "+
	        campos.getString("SQL.T_MZI.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
	        campos.getString("SQL.T_MZI.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
	        campos.getString("SQL.T_MZI.distrito")+"="+gVO.getAtributo("codDistrito")+" AND "+
	        campos.getString("SQL.T_MZI.seccion")+"="+gVO.getAtributo("codSeccion")+" AND "+
	        campos.getString("SQL.T_MZI.letraSeccion")+"='"+gVO.getAtributo("letraSeccion")+"' AND "+
	        campos.getString("SQL.T_MZI.codigo")+"="+gVO.getAtributo("codManzana");
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	      stmt.executeUpdate(sql);
      } 
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    if(cont == 0 && cont1 == 0) {
      resultado = getListaManzanas(gVO,params);
    } else {
    	gVO.setAtributo("puedeEliminar","no");
    	resultado.addElement(gVO);
    }
    return resultado;
  }

  public Vector getListaManzanas(GeneralValueObject parametros,String[] params){
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
      sql = "SELECT * FROM T_MZI WHERE ";
      // CONDICIONES  
      sql+= campos.getString("SQL.T_MZI.pais")+"="+parametros.getAtributo("codPais")+" AND "+
        campos.getString("SQL.T_MZI.provincia")+"="+parametros.getAtributo("codProvincia")+" AND "+
        campos.getString("SQL.T_MZI.municipio")+"="+parametros.getAtributo("codMunicipio")+" AND "+
        campos.getString("SQL.T_MZI.distrito")+"="+parametros.getAtributo("codDistrito")+" AND "+
        campos.getString("SQL.T_MZI.seccion")+"="+parametros.getAtributo("codSeccion")+" AND "+ 
        campos.getString("SQL.T_MZI.letraSeccion")+"='"+parametros.getAtributo("letraSeccion")+"'";
      String[] orden = {campos.getString("SQL.T_MZI.codigo"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        // DISTRITO
        gVO.setAtributo("codPais",rs.getString(campos.getString("SQL.T_MZI.pais")));
        gVO.setAtributo("codProvincia",rs.getString(campos.getString("SQL.T_MZI.provincia")));
        gVO.setAtributo("codMunicipio",rs.getString(campos.getString("SQL.T_MZI.municipio")));
        gVO.setAtributo("codDistrito",rs.getString(campos.getString("SQL.T_MZI.distrito")));
        gVO.setAtributo("codSeccion",rs.getString(campos.getString("SQL.T_MZI.seccion")));
        gVO.setAtributo("letraSeccion",rs.getString(campos.getString("SQL.T_MZI.letraSeccion")));
        gVO.setAtributo("codManzana",rs.getString(campos.getString("SQL.T_MZI.codigo")));
        gVO.setAtributo("descManzana",rs.getString(campos.getString("SQL.T_MZI.nombre")));
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
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ManzanasDAO.getListaManzanas");
        }
    }
    return resultado;
  }

  public Vector modificarManzana(GeneralValueObject gVO, String[] params){
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
      sql = "UPDATE T_MZI SET " + 
        campos.getString("SQL.T_MZI.codigo")+"='"+gVO.getAtributo("codManzana")+"',"+
        campos.getString("SQL.T_MZI.nombre")+"='"+gVO.getAtributo("descManzana")+
        "' WHERE " + 
        campos.getString("SQL.T_MZI.pais")+"="+gVO.getAtributo("codPais")+" AND "+
        campos.getString("SQL.T_MZI.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
        campos.getString("SQL.T_MZI.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
        campos.getString("SQL.T_MZI.distrito")+"="+gVO.getAtributo("codDistrito")+" AND "+
        campos.getString("SQL.T_MZI.seccion")+"="+gVO.getAtributo("codSeccion")+" AND "+
        campos.getString("SQL.T_MZI.letraSeccion")+"='"+gVO.getAtributo("letraSeccion")+"' AND "+
        campos.getString("SQL.T_MZI.codigo")+"="+gVO.getAtributo("codManzana");
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
    resultado = getListaManzanas(gVO,params);
    return resultado;
  }

  public Vector altaManzana(GeneralValueObject gVO, String[] params){
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
      sql = "INSERT INTO T_MZI("+
        campos.getString("SQL.T_MZI.pais")+","+
        campos.getString("SQL.T_MZI.provincia")+","+ 
        campos.getString("SQL.T_MZI.municipio")+","+
        campos.getString("SQL.T_MZI.distrito")+","+ 
        campos.getString("SQL.T_MZI.seccion")+","+ 
        campos.getString("SQL.T_MZI.letraSeccion")+","+ 
        campos.getString("SQL.T_MZI.codigo")+","+
        campos.getString("SQL.T_MZI.nombre")+
        ") VALUES (" +
        gVO.getAtributo("codPais")+","+
        gVO.getAtributo("codProvincia")+","+
        gVO.getAtributo("codMunicipio")+","+
        gVO.getAtributo("codDistrito")+","+
        gVO.getAtributo("codSeccion")+",'"+
        gVO.getAtributo("letraSeccion")+"',"+
        gVO.getAtributo("codManzana")+",'"+
        gVO.getAtributo("descManzana")+"')";
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
    resultado = getListaManzanas(gVO,params);
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