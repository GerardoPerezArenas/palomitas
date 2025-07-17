// NOMBRE DEL PAQUETE
package es.altia.agora.business.formularios.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import java.util.Vector;
import java.sql.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.*;
import es.altia.util.conexion.*;
import es.altia.agora.business.util.GeneralValueObject;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: IdiomasesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class TiposDAO  {
  private static TiposDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(TiposDAO.class.getName());

  protected TiposDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static TiposDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (TiposDAO.class) {
        if (instance == null) {
          instance = new TiposDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarTipo(String codigo, String[] params){
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
      stmt = conexion.createStatement();
      sql = "DELETE FROM F_TIPO_FORM WHERE " +
        campos.getString("SQL.F_TIPO_FORM.codigo")+"='"+codigo+"'";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    Vector lista = new Vector();
    lista = getListaTipos(params);
    return lista;
  }

  public Vector getListaTipos(String[] params){
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
        sql = "SELECT * FROM F_TIPO_FORM";
        String[] orden = {campos.getString("SQL.F_TIPO_FORM.codigo"),"1"};
        sql += abd.orderUnion(orden);
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = conexion.createStatement();
        rs = stmt.executeQuery(sql);
        while(rs.next()){
          GeneralValueObject gVO = new GeneralValueObject();
          gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.F_TIPO_FORM.codigo")));
          gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.F_TIPO_FORM.descripcion")));
          resultado.add(gVO);
        }
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

  public Vector modificarTipo(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    try{
      abd = new AdaptadorSQLBD(params);
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      sql = "UPDATE F_TIPO_FORM SET " +
        campos.getString("SQL.F_TIPO_FORM.codigo")+"='"+gVO.getAtributo("codigo")+"',"+
        campos.getString("SQL.F_TIPO_FORM.descripcion")+"='"+gVO.getAtributo("descripcion")+"'"+
        " WHERE " +
        campos.getString("SQL.F_TIPO_FORM.codigo")+"='"+gVO.getAtributo("codigoAntiguo")+"'";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      int res = stmt.executeUpdate(sql);
      //m_Log.debug("las filas afectadas en el update son : " + res);
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    Vector lista = new Vector();
    lista = getListaTipos(params);
    return lista;
  }

  public Vector altaTipo(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try{
      abd = new AdaptadorSQLBD(params);
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      sql = "INSERT INTO F_TIPO_FORM("+
       campos.getString("SQL.F_TIPO_FORM.codigo")+","+
       campos.getString("SQL.F_TIPO_FORM.descripcion")+
       ") VALUES ('" +
       gVO.getAtributo("codigo") +"','" +
       gVO.getAtributo("descripcion") + "')";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      //m_Log.debug("las filas afectadas en el insert son : " + res);
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    Vector lista = new Vector();
    lista = getListaTipos(params);
    return lista;
   }

  private void rollBackTransaction(AdaptadorSQLBD bd,Connection con,Exception e){
    try {
      bd.rollBack(con);
      bd.devolverConexion(con);
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