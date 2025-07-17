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

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase ProcedimientosDAO</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class ProcedimientosDAO  {
  private static ProcedimientosDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(ProcedimientosDAO.class.getName());

  protected ProcedimientosDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static ProcedimientosDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (ProcedimientosDAO.class) {
        if (instance == null) {
          instance = new ProcedimientosDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarProcedimiento(String codigo,String codCampoAntiguo,String codIdiomaAntiguo, String[] params){
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
	  
	  // Se borra de la conexion local la fila de A_TPML
      sql = "DELETE FROM A_TPML WHERE " +
            campos.getString("SQL.A_TPML.codTipoProcedimiento")+"="+codigo +
            " AND " + campos.getString("SQL.A_TPML.codCampoML") + "='" + codCampoAntiguo +
            "' AND " + campos.getString("SQL.A_TPML.idioma") + "='" + campos.getString("idiomaDefecto") + "'";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
      String existe = "no";
	  
	  // Se comprueba si queda alguna entrada en ESQUEMA_GENERICO.A_TPML relacionada con el procedimiento
      sql = "SELECT " + campos.getString("SQL.A_TPML.codTipoProcedimiento") + " FROM " + GlobalNames.ESQUEMA_GENERICO +
            "A_TPML WHERE " + campos.getString("SQL.A_TPML.codTipoProcedimiento")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()) {
      	existe = "si";
      }
      rs.close();
      stmt.close();
      if("no".equals(existe)) {
		// Se eliminan las entradas de A_TPR local relacionadas con el procedimiento si no hay ningun area relacionada 
		// con el en la conexion generica
        sql = "DELETE FROM A_TPR WHERE " +
               campos.getString("SQL.A_TPR.codProcedimiento")+"="+codigo;
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
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
    lista = getListaProcedimientos(params);
    return lista;
  }

  public Vector getListaProcedimientos(String[] params){
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
      // Creamos la select con los parametros adecuados.
      sql = "SELECT " + campos.getString("SQL.A_TPR.codProcedimiento") + "," + 
             campos.getString("SQL.A_TPML.valor") + "," + 
             campos.getString("SQL.A_TPML.codCampoML") + "," + 
             campos.getString("SQL.A_TPML.idioma") + "," + 
             campos.getString("SQL.A_IDI.descripcion") +
             " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_TPR, " + GlobalNames.ESQUEMA_GENERICO + "A_TPML, "
             + GlobalNames.ESQUEMA_GENERICO + "A_IDI WHERE " + GlobalNames.ESQUEMA_GENERICO + "a_tpr." +
             campos.getString("SQL.A_TPR.codProcedimiento") + "=" + GlobalNames.ESQUEMA_GENERICO + "a_tpml." +
             campos.getString("SQL.A_TPML.codTipoProcedimiento") + " AND " + GlobalNames.ESQUEMA_GENERICO + "a_tpml." +
             campos.getString("SQL.A_TPML.idioma") + "=" + GlobalNames.ESQUEMA_GENERICO + "a_idi." +
             campos.getString("SQL.A_IDI.codigo") + " AND " + campos.getString("SQL.A_TPML.idioma") + "='"+
             campos.getString("idiomaDefecto")+"'";
      String[] orden = {campos.getString("SQL.A_TPR.codProcedimiento"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.A_TPR.codProcedimiento")));
        gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.A_TPML.valor")));
        gVO.setAtributo("codCampo",rs.getString(campos.getString("SQL.A_TPML.codCampoML")));
        gVO.setAtributo("idioma",rs.getString(campos.getString("SQL.A_TPML.idioma")));
        gVO.setAtributo("descIdioma",rs.getString(campos.getString("SQL.A_IDI.descripcion")));
        resultado.add(gVO);
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
    return resultado;
  }

  public Vector modificarProcedimiento(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug.out.println("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      sql = "UPDATE A_TPR SET " +
            campos.getString("SQL.A_TPR.codProcedimiento")+"="+gVO.getAtributo("codigo")+
            " WHERE " +
            campos.getString("SQL.A_TPR.codProcedimiento")+"="+gVO.getAtributo("codigoAntiguo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
      sql = "UPDATE A_TPML SET " + campos.getString("SQL.A_TPML.codTipoProcedimiento")+"="+
            gVO.getAtributo("codigo")+","+ campos.getString("SQL.A_TPML.codCampoML")+"='"+
            gVO.getAtributo("codCampo")+"',"+ campos.getString("SQL.A_TPML.idioma")+"='"+
            campos.getString("idiomaDefecto")+"',"+ campos.getString("SQL.A_TPML.valor")+"='"+
            gVO.getAtributo("descripcion")+ "' WHERE " +
            campos.getString("SQL.A_TPML.codTipoProcedimiento")+"="+gVO.getAtributo("codigoAntiguo") +
            " AND " + campos.getString("SQL.A_TPML.codCampoML") + "='" + gVO.getAtributo("codCampoAntiguo") +
            "' AND " + campos.getString("SQL.A_TPML.idioma") + "='" + campos.getString("idiomaDefecto") + "'";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    Vector lista = new Vector();
    lista = getListaProcedimientos(params);
    return lista;
  }

  public Vector altaProcedimiento(GeneralValueObject gVO, String[] params){
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

      String existe = "no";
      sql  ="SELECT " + campos.getString("SQL.A_TPR.codProcedimiento") + " FROM " + GlobalNames.ESQUEMA_GENERICO +
            "A_TPR WHERE " + campos.getString("SQL.A_TPR.codProcedimiento") + "=" + gVO.getAtributo("codigo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()) {
        existe = "si";	
      }
      rs.close();
      stmt.close();
      if("no".equals(existe)) {
        sql = "INSERT INTO A_TPR("+
              campos.getString("SQL.A_TPR.codProcedimiento")+
              ") VALUES (" +
              gVO.getAtributo("codigo") +")";
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = conexion.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
      }
      sql = "INSERT INTO A_TPML("+
            campos.getString("SQL.A_TPML.codTipoProcedimiento")+","+
            campos.getString("SQL.A_TPML.codCampoML")+","+
            campos.getString("SQL.A_TPML.idioma")+","+
            campos.getString("SQL.A_TPML.valor")+
            ") VALUES (" +
            gVO.getAtributo("codigo") +",'" +
            gVO.getAtributo("codCampo") + "','" +
            campos.getString("idiomaDefecto") + "','" +
            gVO.getAtributo("descripcion") + "')";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    Vector lista = new Vector();
    lista = getListaProcedimientos(params);
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