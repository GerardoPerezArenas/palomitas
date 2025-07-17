package es.altia.agora.business.registro.persistence.manual;

import es.altia.agora.business.registro.BuzonWCValueObject;
import es.altia.common.exception.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.*;
import java.sql.*;
import java.util.Vector;


public class BuzonWCDAO {

  /* Declaracion de servicios */
  protected static Config m_ConfigTechnical; // Fichero de configuracion tecnico
  protected static Log m_Log =
          LogFactory.getLog(BuzonWCDAO.class.getName());

  // protected static Config m_ConfigError; // Mensajes de error localizados

  /* Instancia unica */
    private static BuzonWCDAO instance = null;

  protected static String bwc_fec;
  protected static String bwc_nom;
  protected static String bwc_xml;
  protected static String bwc_est;
  protected static String bwc_eje;
  protected static String bwc_num;

  
  /**
  * Construye un nuevo BuzonWCDAO.
  * Es protected, por lo que la unica manera de instanciar esta clase
  * es usando el factory method <code>getInstance</code>
  */
  protected BuzonWCDAO() {
    super();
    // Fichero de configuracion techserver
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");

    // Mensajes de error localizados
    // m_ConfigError = ConfigServiceHelper.getConfig("error");

    // Tabla R_BWC
    bwc_fec= m_ConfigTechnical.getString("SQL.R_BWC.fecha");
    bwc_nom= m_ConfigTechnical.getString("SQL.R_BWC.nombre");
    bwc_xml= m_ConfigTechnical.getString("SQL.R_BWC.xml");
    bwc_est= m_ConfigTechnical.getString("SQL.R_BWC.estado"); 
    bwc_eje= m_ConfigTechnical.getString("SQL.R_BWC.ejercicio");
    bwc_num= m_ConfigTechnical.getString("SQL.R_BWC.numero");   

  }

  public static BuzonWCDAO getInstance() {
    if (instance == null) {
      // Sincronizacion para serializar (no multithread) las invocaciones de este metodo.
      synchronized(BuzonWCDAO.class) {
        if (instance == null)
          instance = new BuzonWCDAO();
      }
    }
    return instance;
  }


  public Vector getRegistrosWC(BuzonWCValueObject buzonVO,String[] params)
  throws TechnicalException {

    m_Log.debug("getRegistrosWC");

    Vector lista = new Vector();

    AdaptadorSQLBD oad = null;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql="";

    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      
      sql = "SELECT " + oad.convertir(bwc_fec, AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") + " AS " +
              bwc_fec + "," + bwc_nom + "," + bwc_est + "," + bwc_eje + "," + bwc_num + " FROM R_BWC WHERE " +
            bwc_est + " IS NULL ORDER BY " + bwc_fec + " DESC";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();
      String entrar = "no";
      while(rs.next()) {
        entrar = "si";
        BuzonWCValueObject b = new BuzonWCValueObject();
        String ejercicio = rs.getString(bwc_eje);
        b.setEjercicio(ejercicio);
        String numero = rs.getString(bwc_num);
        b.setNumero(numero);
        String fecha = rs.getString(bwc_fec);
        b.setFecha(fecha);
        String nombre = rs.getString(bwc_nom);
        b.setNombre(nombre);
        String estado = rs.getString(bwc_est);
        b.setEstado(estado);
        lista.addElement(b);	
      } 
      if("no".equals(entrar)) {
      	lista = new Vector();
      }

      
      m_Log.debug("BuzonWCDAO: getRegistrosWC --> numero de registros: " + lista.size());
      rs.close();
      stmt.close();

      } catch (Exception e) {
        lista = null;
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());

      }	finally {
      try{
        oad.devolverConexion(con);
      }catch(Exception e) {
          e.printStackTrace();
          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
      }
      }
      m_Log.debug("getAsientosRegistroEntrada");
      return lista;
  }
  
  public Vector getRegistrosWCHistoricos(BuzonWCValueObject buzonVO,String[] params)
  throws TechnicalException {

    m_Log.debug("getRegistrosWC");

    Vector lista = new Vector();

    AdaptadorSQLBD oad = null;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql="";

    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      
      sql = "SELECT " + oad.convertir(bwc_fec, AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") + " AS " +
              bwc_fec + "," + bwc_nom + "," + bwc_est + "," + bwc_eje + "," + bwc_num + " FROM R_BWC WHERE " + 
            bwc_est + " IS NOT NULL ORDER BY " + bwc_fec + " DESC";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();
      String entrar = "no";
      while(rs.next()) {
        entrar = "si";
        BuzonWCValueObject b = new BuzonWCValueObject();
        String ejercicio = rs.getString(bwc_eje);
        b.setEjercicio(ejercicio);
        String numero = rs.getString(bwc_num);
        b.setNumero(numero);
        String fecha = rs.getString(bwc_fec);
        b.setFecha(fecha);
        String nombre = rs.getString(bwc_nom);
        b.setNombre(nombre);
        String estado = rs.getString(bwc_est);
        b.setEstado(estado);
        lista.addElement(b);	
      } 
      if("no".equals(entrar)) {
      	lista = new Vector();
      }

      
      m_Log.debug("BuzonWCDAO: getRegistrosWC --> numero de registros: " + lista.size());
      rs.close();
      stmt.close();

      } catch (Exception e) {
        lista = null;
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());

      }	finally {
      try{
        oad.devolverConexion(con);
      }catch(BDException e) {
          e.printStackTrace();
          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
      }
      }
      m_Log.debug("getAsientosRegistroEntrada");
      return lista;
  }
  
  public int aceptarRegistro(BuzonWCValueObject buzonVO,String[] params)
  throws TechnicalException {

    m_Log.debug("aceptarRegistro");

    AdaptadorSQLBD oad = null;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql="";
    int res = 0;

    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      
      sql = "UPDATE R_BWC SET " + bwc_est + "='A' WHERE " + bwc_eje + "=" + 
            buzonVO.getEjercicio() + " AND " + bwc_num + "=" + buzonVO.getNumero();
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = con.prepareStatement(sql);
      res = stmt.executeUpdate();
      
      stmt.close();

      } catch (Exception e) {
        res = 0;
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());

      }	finally {
      try{
        oad.devolverConexion(con);
      }catch(Exception e) {
          e.printStackTrace();
          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
      }
      }
      m_Log.debug("aceptarRegistro");
      return res;
  }
  
  public int rechazarRegistro(BuzonWCValueObject buzonVO,String[] params)
  throws TechnicalException {

    m_Log.debug("rechazarRegistro");

    AdaptadorSQLBD oad = null;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql="";
    int res = 0;

    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      
      sql = "UPDATE R_BWC SET " + bwc_est + "='R' WHERE " + bwc_eje + "=" + 
            buzonVO.getEjercicio() + " AND " + bwc_num + "=" + buzonVO.getNumero();
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = con.prepareStatement(sql);
      res = stmt.executeUpdate();
      
      stmt.close();

      } catch (Exception e) {
        res = 0;
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());

      }	finally {
      try{
        oad.devolverConexion(con);
      }catch(Exception e) {
          e.printStackTrace();
          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
      }
      }
      m_Log.debug("rechazarRegistro");
      return res;
  }
  
  public String getSolicitud(String numero,String ejercicio,String[] params)
  throws TechnicalException {

    m_Log.debug("getSolicitud");

    AdaptadorSQLBD oad = null;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql="";
    String resultado = "";

    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      
      sql = "SELECT " + bwc_xml + " FROM R_BWC WHERE " + bwc_eje + "=" + ejercicio + " AND " +
            bwc_num + "=" + numero;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();
      if(rs.next()) {
        java.io.Reader cr = rs.getCharacterStream(bwc_xml);
        java.io.CharArrayWriter ot = new java.io.CharArrayWriter();
        int c;
        while(( c = cr.read()) != -1) {
          ot.write(c);	
        }	
        ot.flush();
        resultado = new String(ot.toCharArray());
        ot.close();
        cr.close();
      } 

      
      m_Log.debug("BuzonWCDAO: getSolicitud --> XML: " + resultado);
      rs.close();
      stmt.close();

      } catch (Exception e) {
        resultado = null;
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());

      }	finally {
      try{
        oad.devolverConexion(con);
      }catch(Exception e) {
          e.printStackTrace();
          if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
      }
      }
      m_Log.debug("getSolicitud");
      return resultado;
  }

    private void rollBackTransaction(AdaptadorSQLBD bd,Connection con,Exception e){
    try {
    bd.rollBack(con);
    }catch(Exception e1) {
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
    }catch(Exception ex) {
    ex.printStackTrace();
    m_Log.error("SQLException: " + ex.getMessage()) ;
    }
  }

}
