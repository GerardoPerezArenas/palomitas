package es.altia.agora.business.registro.mantenimiento.persistence.manual;

import es.altia.agora.business.registro.mantenimiento.ManActuacionesValueObject;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

public class ManActuacionesDAO {

  //Para el fichero de configuracion tecnico.
    protected static Config m_ConfigTechnical;
  //Para los mensajes de error localizados.
    protected static Config m_ConfigError;
  //Para informacion de logs.
    protected static Log m_Log  =
          LogFactory.getLog(ManActuacionesDAO.class.getName());


    private static ManActuacionesDAO instance = null;

  protected static String	act_ide;
  protected static String act_cod;
  protected static String act_des;
  protected static String act_fde;
  protected static String act_fha;

  protected static String res_act;
  protected static String res_fec;

  protected ManActuacionesDAO() {
    super();
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");

    act_ide = m_ConfigTechnical.getString("SQL.R_ACT.identificador");
    act_cod = m_ConfigTechnical.getString("SQL.R_ACT.codigo");
    act_des = m_ConfigTechnical.getString("SQL.R_ACT.descripcion");
    act_fde = m_ConfigTechnical.getString("SQL.R_ACT.fechaDesde");
    act_fha = m_ConfigTechnical.getString("SQL.R_ACT.fechaHasta");

    res_act = m_ConfigTechnical.getString("SQL.R_RES.actuacion");
    res_fec =  m_ConfigTechnical.getString("SQL.R_RES.fechaAnotacion");

  }

  public static ManActuacionesDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(ManActuacionesDAO.class){
                if (instance == null)
                    instance = new ManActuacionesDAO();
                }
        }
        return instance;
  }

  public Vector loadActuaciones(String[] params) throws TechnicalException {

    Vector actuaciones = new Vector();
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    AdaptadorSQLBD oad = null;

    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      st = con.createStatement();

      // Creamos la select con los parametros adecuados.
      String SQL_BUSCA_ACTUACIONES = "SELECT " + act_ide + ", " + act_cod + ", " + act_des + ", " +
              oad.convertir(act_fde, AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fechaD, " + 
              oad.convertir(act_fha, AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
              " AS fechaH FROM R_ACT ORDER BY " + act_des;
      if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_ACTUACIONES);
      rs = st.executeQuery(SQL_BUSCA_ACTUACIONES);
      while(rs.next()){
        ManActuacionesValueObject manActuacionesVO = new ManActuacionesValueObject(rs.getInt(act_ide), rs.getString(act_cod), rs.getString(act_des), rs.getString("fechaD"), rs.getString("fechaH"), "");
        actuaciones.addElement(manActuacionesVO);
      }

      rs.close();
      st.close();
    }catch (Exception e){
      m_Log.error(e.getMessage());
    }finally{
      //Aquí se pueden lanzar TechnicalException que no se capturan.
        try{
            oad.devolverConexion(con);
        } catch(Exception ex) {
            ex.printStackTrace();
             if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage());
        }
    }
    return actuaciones;
  }

    public Vector loadActuacionesByFecha(String[] params, String fecha) throws TechnicalException {
        
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        
        String SQL_BUSCA_ACTUACIONES = "SELECT " + act_ide + ", " + act_cod + ", " + act_des + ", " +
                oad.convertir(act_fde, AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fechaD, " +
                oad.convertir(act_fha, AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                " AS fechaH FROM R_ACT " +
                "WHERE (" + oad.convertir("?", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") +
                " >= " + oad.convertir(oad.convertir("ACT_FDE", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"),
                AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ") AND ( (ACT_FHA IS NULL) OR ((" +
                oad.convertir("?", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ") <= " +
                oad.convertir(oad.convertir("ACT_FHA", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"),
                AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ")) ORDER BY " + act_des;

        try {
            
            con = oad.getConnection();
            st = con.prepareStatement(SQL_BUSCA_ACTUACIONES);

            st.setString(1, fecha);
            st.setString(2, fecha);
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(SQL_BUSCA_ACTUACIONES);
            }
            rs = st.executeQuery();
            
            Vector actuaciones = new Vector();
            while (rs.next()) {
                ManActuacionesValueObject manActuacionesVO = new ManActuacionesValueObject(rs.getInt(act_ide), rs.getString(act_cod), rs.getString(act_des), rs.getString("fechaD"), rs.getString("fechaH"), "");
                actuaciones.addElement(manActuacionesVO);
            }

            return actuaciones;
        } catch (Exception e) {
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
        }        
    }

  public Vector loadActuacionesUtilizados(String[] params) throws TechnicalException {

    Vector actuacionesUtilizados = new Vector();
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    AdaptadorSQLBD oad = null;

    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      st = con.createStatement();

      // Creamos la select con los parametros adecuados.
      String SQL_BUSCA_ACTUACIONES_UTIL = "SELECT DISTINCT " + res_act + " FROM R_RES WHERE " + res_act + " IS NOT NULL";
      if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_ACTUACIONES_UTIL);
      rs = st.executeQuery(SQL_BUSCA_ACTUACIONES_UTIL);
      while(rs.next()){
        actuacionesUtilizados.addElement(rs.getString("res_act"));
      }

      if(actuacionesUtilizados.firstElement() == null ) {
                       actuacionesUtilizados.removeElementAt(0);
                       actuacionesUtilizados.addElement("");
                     }
      if(actuacionesUtilizados.lastElement() == null) {
                       actuacionesUtilizados.removeElementAt(1);
                     }
      rs.close();
      st.close();
    }catch (Exception e){
      m_Log.error(e.getMessage());
    }finally{
      //Aquí se pueden lanzar TechnicalException que no se capturan.
        try{
            oad.devolverConexion(con);
         } catch(Exception ex) {
            ex.printStackTrace();
             if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage());
         }
    }
    return actuacionesUtilizados;
  }

  public Vector loadTemasUtilizadosCerrados(String fechaCerradoE,String fechaCerradoS,String[] params) throws TechnicalException {

       Vector temasUtilizadosCerrados = new Vector();
       Connection con = null;
       Statement st = null;
       ResultSet rs = null;
       AdaptadorSQLBD oad = null;

       try{
         oad = new AdaptadorSQLBD(params);
         con = oad.getConnection();
         st = con.createStatement();

         // Creamos la select con los parametros adecuados.
         String SQL_BUSCA_TEMAS_UTIL;
         if(!fechaCerradoE.equals("")) {
           SQL_BUSCA_TEMAS_UTIL = "SELECT DISTINCT " + res_act + " FROM R_RES WHERE (" + res_fec + " <= " +
                   oad.convertir("'" + fechaCerradoE + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") +
                   " AND " + res_act + " IS NOT NULL)";
           if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_TEMAS_UTIL);
           rs = st.executeQuery(SQL_BUSCA_TEMAS_UTIL);
           while(rs.next()){
             temasUtilizadosCerrados.addElement(rs.getString(res_act));
           }
           rs.close();             
         }

         if(!fechaCerradoS.equals("")) {
           SQL_BUSCA_TEMAS_UTIL = "SELECT DISTINCT " + res_act + " FROM R_RES WHERE (" + res_fec + " <= " +
                   oad.convertir("'" + fechaCerradoS + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") +
                   " AND " + res_act + " IS NOT NULL)";
           if(m_Log.isDebugEnabled()) m_Log.debug(SQL_BUSCA_TEMAS_UTIL);
           rs = st.executeQuery(SQL_BUSCA_TEMAS_UTIL);
           while(rs.next()){
             temasUtilizadosCerrados.addElement(rs.getString(res_act));
           }
           rs.close();
         }

         st.close();
       }catch (Exception e){
         m_Log.error(e.getMessage());
       }finally{
         //Aquí se pueden lanzar TechnicalException que no se capturan.
           try{
               oad.devolverConexion(con);
            } catch(Exception ex) {
               ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage());
            }
       }
      return temasUtilizadosCerrados;
    }


  public Vector eliminarTema(ManActuacionesValueObject manActVO, String[] params) throws TechnicalException,BDException {

    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";

    try{

      m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      m_Log.debug("A por la conexion");
      conexion = abd.getConnection();

      int identificador = manActVO.getIde();
      m_Log.debug("la descripcion en el DAO es : " + identificador);

      sql = "DELETE FROM R_ACT WHERE " + act_ide + " =" + identificador ;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);

      PreparedStatement ps = conexion.prepareStatement(sql);
      int res = ps.executeUpdate();
      m_Log.debug("las filas afectadas en el delete son : " + res);

    } catch (Exception e) {
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    } finally {
        try{
            abd.devolverConexion(conexion);
         } catch(Exception ex) {
            ex.printStackTrace();
             if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage());
         }
      }

      Vector lista = new Vector();
      lista = loadActuaciones(params);
      return lista;

    }

  public Vector modificarTema(ManActuacionesValueObject manActVO, String[] params) throws TechnicalException,BDException {

AdaptadorSQLBD abd = null;
Connection conexion = null;
Statement stmt = null;
String sql = "";

try{

  m_Log.debug("A por el OAD");
  abd = new AdaptadorSQLBD(params);
  m_Log.debug("A por la conexion");
  conexion = abd.getConnection();

  int identificador = manActVO.getIde();
  String codigo = manActVO.getCodigo();
  String descripcion = manActVO.getDescripcion();
  String fechaDesde = manActVO.getFechaDesde();
  String fechaHasta = manActVO.getFechaHasta();
  m_Log.debug("el identificador en el DAO es : " + identificador);

  if(fechaHasta == null) fechaHasta = "";
  sql = "UPDATE R_ACT SET " + act_cod + " = '" + codigo + "', " + act_des + " = '" + descripcion +
       "', " + act_fde + "= " +
          abd.convertir("'" + fechaDesde + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "," +
       act_fha + " = " + abd.convertir("'" + fechaHasta + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") +
       " WHERE " + act_ide + " = " + identificador;
  if(m_Log.isDebugEnabled()) m_Log.debug(sql);

  PreparedStatement ps = conexion.prepareStatement(sql);
  int res = ps.executeUpdate();
  m_Log.debug("las filas afectadas en el update son : " + res);

} catch (Exception e) {
    e.printStackTrace();
    if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
} finally {
    try{
        abd.devolverConexion(conexion);
     } catch(Exception ex) {
        ex.printStackTrace();
         if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage());
     }
  }

  Vector lista = new Vector();
  lista = loadActuaciones(params);
  return lista;

  }

  public Vector altaTema(ManActuacionesValueObject manActVO, String[] params) throws TechnicalException,BDException {

AdaptadorSQLBD abd = null;
Connection conexion = null;
Statement stmt = null;
ResultSet rs = null;
String sql = "";

try{

  m_Log.debug("A por el OAD");
  abd = new AdaptadorSQLBD(params);
  m_Log.debug("A por la conexion");
  conexion = abd.getConnection();
  stmt = conexion.createStatement();

  int ideMax = 0;
  sql = "SELECT " + abd.funcionMatematica(AdaptadorSQL.FUNCIONMATEMATICA_MAX, new String[]{act_ide}) + " FROM R_ACT";
  rs = stmt.executeQuery(sql);
  while(rs.next()){
    ideMax = rs.getInt(1);
  }

  m_Log.debug("el identificador más grande es : " + ideMax);

  String codigo = manActVO.getCodigo();
  String descripcion = manActVO.getDescripcion();
  String fechaDesde = manActVO.getFechaDesde();
  String fechaHasta = manActVO.getFechaHasta();
  if(fechaHasta == null) {
    sql = "INSERT INTO R_ACT VALUES( " + (ideMax + 1) + ", '" + codigo + "', '" + descripcion + "'," +
        abd.convertir("'" + fechaDesde + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ", NULL)";
    m_Log.debug("entra en el if de fecha hasta igual a nulo en el DAO " + fechaHasta);
  } else {

  sql = "INSERT INTO R_ACT VALUES( " + (ideMax + 1) + ", '" + codigo + "', '" + descripcion + "'," +
        abd.convertir("'" + fechaDesde + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "," +
          abd.convertir("'" + fechaHasta + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ")";
  }
  if(m_Log.isDebugEnabled()) m_Log.debug(sql);

  PreparedStatement ps = conexion.prepareStatement(sql);
  int res = ps.executeUpdate();
  m_Log.debug("las filas afectadas en el insert son : " + res);


} catch (Exception e) {
    e.printStackTrace();
    if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
  } finally {
    try{
        abd.devolverConexion(conexion);
     } catch(Exception ex) {
        ex.printStackTrace();
         if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage());
     }
  }

  Vector lista = new Vector();
  lista = loadActuaciones(params);
  return lista;

  }

}