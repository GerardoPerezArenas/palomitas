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
import es.altia.agora.business.util.TransformacionAtributoSelect;
import es.altia.agora.business.util.GlobalNames;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase TramitesDAO</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class TramitesDAO  {
  private static TramitesDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(TramitesDAO.class.getName());

  protected TramitesDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static TramitesDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (TramitesDAO.class) {
        if (instance == null) {
          instance = new TramitesDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarTramite(String codigo,String codCampoAntiguo,String codIdiomaAntiguo, String[] params){
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
      stmt = conexion.createStatement();
      sql = "DELETE FROM A_CML WHERE " +
            campos.getString("SQL.A_CML.codTramite") + "=" + codigo + " AND " +
            campos.getString("SQL.A_CML.codCampoML") + "='" + codCampoAntiguo +
            "' AND " + campos.getString("SQL.A_CML.idioma") + "='" + campos.getString("idiomaDefecto") + "'";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
      stmt.close();
      String existe = "no";
      sql = "SELECT " + campos.getString("SQL.A_CML.codTramite") + " FROM " +GlobalNames.ESQUEMA_GENERICO + "A_CML WHERE " +
            campos.getString("SQL.A_CML.codTramite")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()) {
          existe = "si";
      }
      rs.close();
      stmt.close();
      if("no".equals(existe)) {
        sql = "DELETE FROM A_CLS WHERE " +
              campos.getString("SQL.A_CLS.codTramite")+"="+codigo;
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
    lista = getListaTramites(params);
    return lista;
  }

  public Vector getListaTramites(String[] params){
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
      sql = "SELECT " + campos.getString("SQL.A_CLS.codTramite") + "," +
             campos.getString("SQL.A_CML.valor") + "," +
             campos.getString("SQL.A_CML.codCampoML") + "," +
             campos.getString("SQL.A_CML.idioma") + "," +
             campos.getString("SQL.A_IDI.descripcion") +
             " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_CLS, " + GlobalNames.ESQUEMA_GENERICO + "A_CML, " +
             GlobalNames.ESQUEMA_GENERICO + "A_IDI WHERE " + GlobalNames.ESQUEMA_GENERICO + "a_cls." +
             campos.getString("SQL.A_CLS.codTramite") + "=" + GlobalNames.ESQUEMA_GENERICO +  "a_cml." +
             campos.getString("SQL.A_CML.codTramite") + " AND " + GlobalNames.ESQUEMA_GENERICO +  "a_cml." +
             campos.getString("SQL.A_CML.idioma") + "=" + GlobalNames.ESQUEMA_GENERICO +  "a_idi." +
             campos.getString("SQL.A_IDI.codigo") + " AND " + campos.getString("SQL.A_CML.idioma") + "='"+
             campos.getString("idiomaDefecto") + "'";
      String[] orden = {campos.getString("SQL.A_CLS.codTramite"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.A_CLS.codTramite")));
        gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.A_CML.valor")));
        gVO.setAtributo("codCampo",rs.getString(campos.getString("SQL.A_CML.codCampoML")));
        gVO.setAtributo("idioma",rs.getString(campos.getString("SQL.A_CML.idioma")));
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

  public Vector modificarTramite(GeneralValueObject gVO, String[] params){
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
      sql = "UPDATE A_CLS SET " +
        campos.getString("SQL.A_CLS.codTramite")+"="+gVO.getAtributo("codigo")+
        " WHERE " +
        campos.getString("SQL.A_CLS.codTramite")+"="+gVO.getAtributo("codigoAntiguo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
      sql = "UPDATE A_CML SET " + campos.getString("SQL.A_CML.codTramite")+"="+
            gVO.getAtributo("codigo")+","+ campos.getString("SQL.A_CML.codCampoML")+"='"+
            gVO.getAtributo("codCampo")+"',"+ campos.getString("SQL.A_CML.idioma")+"='"+
            campos.getString("idiomaDefecto")+"',"+ campos.getString("SQL.A_CML.valor")+"='"+
            gVO.getAtributo("descripcion")+ "' WHERE " +
            campos.getString("SQL.A_CML.codTramite")+"="+gVO.getAtributo("codigoAntiguo") +
            " AND " + campos.getString("SQL.A_CML.codCampoML") + "='" + gVO.getAtributo("codCampoAntiguo") +
            "' AND " + campos.getString("SQL.A_CML.idioma") + "='" + campos.getString("idiomaDefecto") + "'";
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
    lista = getListaTramites(params);
    return lista;
  }

  public Vector altaTramite(GeneralValueObject gVO, String[] params){
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
      stmt = conexion.createStatement();
      String existe = "no";
      sql  ="SELECT " + campos.getString("SQL.A_CLS.codTramite") + " FROM A_CLS WHERE " +
            campos.getString("SQL.A_CLS.codTramite") + "=" + gVO.getAtributo("codigo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()) {
        existe = "si";
      }
      rs.close();
      stmt.close();
      if("no".equals(existe)) {
        sql = "INSERT INTO A_CLS("+
              campos.getString("SQL.A_CLS.codTramite")+
              ") VALUES (" +
              gVO.getAtributo("codigo") +")";
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = conexion.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
      }
      sql = "INSERT INTO A_CML("+
            campos.getString("SQL.A_CML.codTramite")+","+
            campos.getString("SQL.A_CML.codCampoML")+","+
            campos.getString("SQL.A_CML.idioma")+","+
            campos.getString("SQL.A_CML.valor")+
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
    lista = getListaTramites(params);
    return lista;
   }


   public Vector getListaTramitesByCodigos(String[] params, String codigos){

       TransformacionAtributoSelect transformador;
       Vector resultado = new Vector();
       AdaptadorSQLBD abd = null;
       Connection conexion = null;
       Statement stmt = null;
       ResultSet rs = null;
       String sql = "";
       try{
         abd = new AdaptadorSQLBD(params);
         transformador = new TransformacionAtributoSelect(abd);
         conexion = abd.getConnection();
         // Creamos la select con los parametros adecuados.
         sql = "SELECT " + campos.getString("SQL.A_CLS.codTramite") + "," + campos.getString("SQL.A_CML.valor") +
                " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_CLS, " + GlobalNames.ESQUEMA_GENERICO + "A_CML WHERE "
                + GlobalNames.ESQUEMA_GENERICO + "a_cls." + campos.getString("SQL.A_CLS.codTramite") +
                "=" + GlobalNames.ESQUEMA_GENERICO +  "a_cml." + campos.getString("SQL.A_CML.codTramite") +
                " AND " + campos.getString("SQL.A_CML.codCampoML") + " LIKE 'NOM' " +
                " AND " + campos.getString("SQL.A_CML.idioma") + "='"+campos.getString("idiomaDefecto")+"'";

        if(codigos != null) {
            if (!"".equals(codigos)) {
                String condicion = transformador.construirCondicionWhereConOperadores(GlobalNames.ESQUEMA_GENERICO + "a_cls." +
                    campos.getString("SQL.A_CLS.codTramite"), codigos,true);
                if (!"".equals(condicion)) {
                     sql += " AND " + condicion;
                }
            }
        }

         String[] orden = {campos.getString("SQL.A_CLS.codTramite"),"1"};
         sql += abd.orderUnion(orden);
         if (m_Log.isDebugEnabled()) m_Log.debug(sql);
         stmt = conexion.createStatement();
         rs = stmt.executeQuery(sql);
         while(rs.next()){
           GeneralValueObject gVO = new GeneralValueObject();
           gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.A_CLS.codTramite")));
           gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.A_CML.valor")));
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