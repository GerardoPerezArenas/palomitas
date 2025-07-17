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
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase AreasDAO</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class AreasDAO  {
  private static AreasDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(AreasDAO.class.getName());

  protected AreasDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static AreasDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (AreasDAO.class) {
        if (instance == null) {
          instance = new AreasDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarArea(String codigo,String codCampoAntiguo,String codIdiomaAntiguo,  String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    boolean existeDependencia = false;
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      stmt = conexion.createStatement();
      sql = "SELECT " + campos.getString("SQL.E_PRO.area") + " FROM E_PRO WHERE " +
             campos.getString("SQL.E_PRO.area") + "=" + codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      existeDependencia = rs.next();
      rs.close();
      stmt.close();
      if (!existeDependencia) {
          String[] parametros = (String []) params.clone();
          parametros[6] = campos.getString("CON.jndi");
          abd = new AdaptadorSQLBD(parametros);
          conexion = abd.getConnection();
          abd.inicioTransaccion(conexion);
          stmt = conexion.createStatement();
          sql = "DELETE FROM " + GlobalNames.ESQUEMA_GENERICO + "A_AML WHERE " +
                GlobalNames.ESQUEMA_GENERICO + "A_AML." + campos.getString("SQL.A_AML.codArea")+"="+codigo +
                " AND " + GlobalNames.ESQUEMA_GENERICO + "A_AML." + campos.getString("SQL.A_AML.codCampoML") + "='" + codCampoAntiguo +
                "' AND " + GlobalNames.ESQUEMA_GENERICO + "A_AML." + campos.getString("SQL.A_AML.idioma") + "='" + campos.getString("idiomaDefecto") + "'";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
      stmt.close();
      String existe = "no";
      sql = "SELECT " + campos.getString("SQL.A_AML.codArea") + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_AML WHERE " +
            GlobalNames.ESQUEMA_GENERICO + "A_AML." + campos.getString("SQL.A_AML.codArea")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()) {
      	existe = "si";
      }
      rs.close();
      stmt.close();
      if("no".equals(existe)) {
        sql = "DELETE FROM " +GlobalNames.ESQUEMA_GENERICO + "A_ARE WHERE " +
               GlobalNames.ESQUEMA_GENERICO + "A_ARE." + campos.getString("SQL.A_ARE.codArea")+"="+codigo;
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = conexion.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
       }      
      }
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    Vector lista = new Vector();
    if (!existeDependencia)
    lista = getListaAreas(params);
    return lista;
  }

  public Vector getListaAreas(String[] params){
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
      sql = "SELECT " + campos.getString("SQL.A_ARE.codArea") + "," + 
             campos.getString("SQL.A_AML.valor") + "," + 
             campos.getString("SQL.A_AML.codCampoML") + "," + 
             campos.getString("SQL.A_AML.idioma") + "," + 
             campos.getString("SQL.A_IDI.descripcion") +
             " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_ARE, " + GlobalNames.ESQUEMA_GENERICO + "A_AML, " +
             GlobalNames.ESQUEMA_GENERICO + "A_IDI WHERE " + GlobalNames.ESQUEMA_GENERICO + "a_are." +
             campos.getString("SQL.A_ARE.codArea") + "=" + GlobalNames.ESQUEMA_GENERICO + "a_aml." +
             campos.getString("SQL.A_AML.codArea") + " AND " + GlobalNames.ESQUEMA_GENERICO + "a_aml." +
             campos.getString("SQL.A_AML.idioma") + "=" + GlobalNames.ESQUEMA_GENERICO + "a_idi." +
             campos.getString("SQL.A_IDI.codigo") + " AND " + campos.getString("SQL.A_AML.idioma") + "='"
             + campos.getString("idiomaDefecto") +"'";
      String[] orden = {campos.getString("SQL.A_ARE.codArea"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()) {
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codigo",rs.getString(campos.getString("SQL.A_ARE.codArea")));
        gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.A_AML.valor")));
        gVO.setAtributo("codCampo",rs.getString(campos.getString("SQL.A_AML.codCampoML")));
        gVO.setAtributo("idioma",rs.getString(campos.getString("SQL.A_AML.idioma")));
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
  
    public Map<Integer, Integer> checkErrorAreaProcedimiento(String codigoArea, String descripcionArea, String[] params) {
        // codigo del error y nuevo codigo de area en caso de que sea error tipo 3
        Map<Integer,Integer> error = new HashMap<Integer, Integer>();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;

        Statement stmtDescripcion = null;
        ResultSet rsDescripcion = null;

        String sqlCodigo = "";
        String sqlDescripcion = "";

        String[] parametros = (String[]) params.clone();
        parametros[6] = campos.getString("CON.jndi");
        abd = new AdaptadorSQLBD(parametros);
        try {
            conexion = abd.getConnection();
            
            sqlCodigo = "SELECT AML_COD,AML_VALOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_AML WHERE AML_COD =" + codigoArea;
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sqlCodigo);
            if (rs.next()) {

                if (rs.getString("AML_VALOR").equalsIgnoreCase(descripcionArea)) {
                    error.put(1, null);
                } else {
                    error.put(2, null);
                }

            
            rs.close();
            stmt.close();
            } else {
            // si no tiene siguiente buscamos por la descripción del área 
            sqlDescripcion = "SELECT AML_COD,AML_VALOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_AML WHERE upper(AML_VALOR) = upper('" + descripcionArea + "')"; 
                stmtDescripcion = conexion.createStatement();
            rsDescripcion = stmt.executeQuery(sqlDescripcion);
            
            if (rsDescripcion.next()) {
            
                if (rsDescripcion.getString("AML_VALOR").equalsIgnoreCase(descripcionArea)) {
                    error.put(3, rsDescripcion.getInt("AML_COD"));
                } 
            } else {
                error.put(4, null);
            }
            
            rsDescripcion.close();
            stmtDescripcion.close();
        }       

        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error(e.getMessage());
            }
        } finally {
            try {
                abd.devolverConexion(conexion);
            } catch (BDException ex) {
                if (m_Log.isErrorEnabled()) {
                    m_Log.error(ex.getMessage());
                }
            }
        }

        return error;
    }

  public Vector modificarArea(GeneralValueObject gVO, String[] params){
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
      sql = "UPDATE A_ARE SET " +
        campos.getString("SQL.A_ARE.codArea")+"="+gVO.getAtributo("codigo")+
        " WHERE " +
        campos.getString("SQL.A_ARE.codArea")+"="+gVO.getAtributo("codigoAntiguo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
      sql = "UPDATE A_AML SET " + campos.getString("SQL.A_AML.codArea") + "=" +
            gVO.getAtributo("codigo")+","+ campos.getString("SQL.A_AML.codCampoML")+"='"+
            gVO.getAtributo("codCampo")+"',"+ campos.getString("SQL.A_AML.idioma")+"='"+
            campos.getString("idiomaDefecto")+"',"+ campos.getString("SQL.A_AML.valor")+"='"+
            gVO.getAtributo("descripcion")+ "' WHERE " +
            campos.getString("SQL.A_AML.codArea")+"="+gVO.getAtributo("codigoAntiguo") +
            " AND " + campos.getString("SQL.A_AML.codCampoML") + "='" + gVO.getAtributo("codCampoAntiguo") +
            "' AND " + campos.getString("SQL.A_AML.idioma") + "='" + campos.getString("idiomaDefecto") + "'";
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
    lista = getListaAreas(params);
    return lista;
  }

  public Vector altaArea(GeneralValueObject gVO, String[] params){
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
      sql  ="SELECT " + campos.getString("SQL.A_ARE.codArea") + " FROM A_ARE WHERE " +
            campos.getString("SQL.A_ARE.codArea") + "=" + gVO.getAtributo("codigo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()) {
        existe = "si";	
      }
      rs.close();
      stmt.close();
      if("no".equals(existe)) {
        sql = "INSERT INTO A_ARE("+
              campos.getString("SQL.A_ARE.codArea")+
              ") VALUES (" +
              gVO.getAtributo("codigo") +")";
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = conexion.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
      }
      sql = "INSERT INTO A_AML("+
            campos.getString("SQL.A_AML.codArea")+","+
            campos.getString("SQL.A_AML.codCampoML")+","+
            campos.getString("SQL.A_AML.idioma")+","+
            campos.getString("SQL.A_AML.valor")+
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
    lista = getListaAreas(params);
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