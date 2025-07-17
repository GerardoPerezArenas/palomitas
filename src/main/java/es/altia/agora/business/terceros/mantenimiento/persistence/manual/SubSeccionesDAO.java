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
 * <p>Descripción: SubSeccionesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class SubSeccionesDAO  {
  private static SubSeccionesDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(SubSeccionesDAO.class.getName());

  protected SubSeccionesDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static SubSeccionesDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (SubSeccionesDAO.class) {
        if (instance == null) {
          instance = new SubSeccionesDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarSubSeccion(GeneralValueObject gVO, String[] params){
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
      stmt = conexion.createStatement();
      sql = "DELETE FROM T_SSC WHERE " + 
        campos.getString("SQL.T_SSC.pais")+"="+gVO.getAtributo("codPais")+" AND "+
        campos.getString("SQL.T_SSC.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
        campos.getString("SQL.T_SSC.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
        campos.getString("SQL.T_SSC.distrito")+"="+gVO.getAtributo("codDistrito")+" AND "+
        campos.getString("SQL.T_SSC.seccion")+"="+gVO.getAtributo("codSeccion")+" AND "+
        campos.getString("SQL.T_SSC.letraSeccion")+"='"+gVO.getAtributo("letraSeccion")+"' AND "+
        campos.getString("SQL.T_SSC.codigo")+"="+gVO.getAtributo("codSubSeccion");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    resultado = getListaSubSecciones(gVO,params);
    return resultado;
  }

  public Vector getListaSubSecciones(GeneralValueObject parametros,String[] params){
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
      sql = "SELECT * FROM T_SSC WHERE ";
      // CONDICIONES  
      sql+= campos.getString("SQL.T_SSC.pais")+"="+parametros.getAtributo("codPais")+" AND "+
        campos.getString("SQL.T_SSC.provincia")+"="+parametros.getAtributo("codProvincia")+" AND "+
        campos.getString("SQL.T_SSC.municipio")+"="+parametros.getAtributo("codMunicipio")+" AND "+
        campos.getString("SQL.T_SSC.distrito")+"="+parametros.getAtributo("codDistrito")+" AND "+
        campos.getString("SQL.T_SSC.seccion")+"="+parametros.getAtributo("codSeccion")+" AND "+
        campos.getString("SQL.T_SSC.letraSeccion")+"='"+parametros.getAtributo("letraSeccion")+"'";
        
      String[] orden = {campos.getString("SQL.T_SSC.codigo"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        // DISTRITO
        gVO.setAtributo("codPais",rs.getString(campos.getString("SQL.T_SSC.pais")));
        gVO.setAtributo("codProvincia",rs.getString(campos.getString("SQL.T_SSC.provincia")));
        gVO.setAtributo("codMunicipio",rs.getString(campos.getString("SQL.T_SSC.municipio")));
        gVO.setAtributo("codDistrito",rs.getString(campos.getString("SQL.T_SSC.distrito")));
        gVO.setAtributo("codSeccion",rs.getString(campos.getString("SQL.T_SSC.seccion")));
        gVO.setAtributo("letraSeccion",rs.getString(campos.getString("SQL.T_SSC.letraSeccion")));
        gVO.setAtributo("codSubSeccion",rs.getString(campos.getString("SQL.T_SSC.codigo")));
        gVO.setAtributo("descSubSeccion",rs.getString(campos.getString("SQL.T_SSC.nOficial")));
        gVO.setAtributo("nombreLargo",rs.getString(campos.getString("SQL.T_SSC.nLargo")));
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
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en SubSeccionesDAO.getListaSubSecciones");
        }
    }
    return resultado;
  }

  public Vector modificarSubSeccion(GeneralValueObject gVO, String[] params){
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
      sql = "UPDATE T_SSC SET " + 
        campos.getString("SQL.T_SSC.codigo")+"="+gVO.getAtributo("codSubSeccion")+","+
        campos.getString("SQL.T_SSC.nOficial")+"="+gVO.getAtributo("descSubSeccion")+","+
        campos.getString("SQL.T_SSC.nLargo")+"="+gVO.getAtributo("nombreLargo")+","+ 
        " WHERE " + 
        campos.getString("SQL.T_SSC.pais")+"="+gVO.getAtributo("codPais")+" AND "+
        campos.getString("SQL.T_SSC.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
        campos.getString("SQL.T_SSC.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
        campos.getString("SQL.T_SSC.distrito")+"="+gVO.getAtributo("codDistrito")+" AND "+
        campos.getString("SQL.T_SSC.seccion")+"="+gVO.getAtributo("codSeccion")+" AND "+
        campos.getString("SQL.T_SSC.letraSeccion")+"='"+gVO.getAtributo("letraSeccion")+"' AND "+
        campos.getString("SQL.T_SSC.codigo")+"="+gVO.getAtributo("codSubSeccionAntiguo");
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
    resultado = getListaSubSecciones(gVO,params);
    return resultado;
  }

  public Vector altaSubSeccion(GeneralValueObject gVO, String[] params){
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
      sql = "INSERT INTO T_SSC("+
        campos.getString("SQL.T_SSC.pais")+","+
        campos.getString("SQL.T_SSC.provincia")+","+ 
        campos.getString("SQL.T_SSC.municipio")+","+
        campos.getString("SQL.T_SSC.distrito")+","+ 
        campos.getString("SQL.T_SSC.seccion")+","+ 
        campos.getString("SQL.T_SSC.letra")+","+ 
        campos.getString("SQL.T_SSC.codigo")+","+
        campos.getString("SQL.T_SSC.nOficial")+","+
        campos.getString("SQL.T_SSC.nLargo")+
        ") VALUES (" +
        gVO.getAtributo("codPais")+","+
        gVO.getAtributo("codProvincia")+","+
        gVO.getAtributo("codMunicipio")+","+
        gVO.getAtributo("codDistrito")+","+
        gVO.getAtributo("codSeccion")+",'"+
        gVO.getAtributo("letraSeccion")+"',"+
        gVO.getAtributo("codSubSeccion")+",'"+
        gVO.getAtributo("descSubSeccion")+"','"+
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
    resultado = getListaSubSecciones(gVO,params);
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