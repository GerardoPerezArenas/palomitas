// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: CodPostalesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class CodPostalesDAO  {
  private static CodPostalesDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(CodPostalesDAO.class.getName());

  protected CodPostalesDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static CodPostalesDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (CodPostalesDAO.class) {
        if (instance == null) {
          instance = new CodPostalesDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarCodPostal(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    Vector resultado = new Vector();
    int cont = 0;
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      stmt = conexion.createStatement();
      sql = "SELECT * FROM T_DSU WHERE " +
            campos.getString("SQL.T_DSU.cpoPais")+"="+gVO.getAtributo("codPais")+" AND "+
                campos.getString("SQL.T_DSU.cpoProvincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                campos.getString("SQL.T_DSU.cpoMunicipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                campos.getString("SQL.T_DSU.codigoPostal")+"='"+gVO.getAtributo("codPostal")+"'";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
          cont++;
      }
      rs.close();

      if(cont == 0) {
          sql = "DELETE FROM T_CPO WHERE " +
            campos.getString("SQL.T_CPO.codPais")+"="+gVO.getAtributo("codPais")+" AND "+
            campos.getString("SQL.T_CPO.codProvincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
            campos.getString("SQL.T_CPO.codMunicipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
            campos.getString("SQL.T_CPO.codigo")+"='"+gVO.getAtributo("codPostal")+"'";
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
      resultado = getListaCodPostales(params,gVO);
    } else {
      gVO.setAtributo("puedeEliminar","no");
        resultado.addElement(gVO);
    }
    return resultado;
  }

  public Vector eliminarCodPostalDesdeMantenimiento(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    Vector resultado = new Vector();
    int cont = 0;
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      stmt = conexion.createStatement();
      sql = "SELECT * FROM T_DSU WHERE " +
            campos.getString("SQL.T_DSU.cpoPais")+"="+gVO.getAtributo("codPais")+" AND "+
                campos.getString("SQL.T_DSU.cpoProvincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                campos.getString("SQL.T_DSU.cpoMunicipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                campos.getString("SQL.T_DSU.codigoPostal")+"='"+gVO.getAtributo("codPostal")+"'";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
          cont++;
      }
      rs.close();

      if(cont == 0) {
          sql = "DELETE FROM T_CPO WHERE " +
            campos.getString("SQL.T_CPO.codPais")+"="+gVO.getAtributo("codPais")+" AND "+
            campos.getString("SQL.T_CPO.codProvincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
            campos.getString("SQL.T_CPO.codMunicipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
            campos.getString("SQL.T_CPO.codigo")+"='"+gVO.getAtributo("codPostal")+"'";
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
      resultado = getListaCodPostalesDesdeMantenimiento(params,gVO);
    } else {
      gVO.setAtributo("puedeEliminar","no");
        resultado.addElement(gVO);
    }
    return resultado;
  }
  public Vector getListaCodPostales(String[] params,GeneralValueObject gVO){
    String sql = "";
    Vector resultado = new Vector();
    AdaptadorSQLBD bd = null;
    Connection con=null;
    ResultSet rs=null;
    Statement state=null;
    try{
      bd = new AdaptadorSQLBD(params);
      con = bd.getConnection();
//      bd.inicioTransaccion(con);
      state = con.createStatement();
      sql = "SELECT * FROM T_CPO WHERE " +
        campos.getString("SQL.T_CPO.codPais")+"="+gVO.getAtributo("codPais") +
        " AND "+ campos.getString("SQL.T_CPO.codProvincia")+"="+gVO.getAtributo("codProvincia");
      sql+= " ORDER BY " + campos.getString("SQL.T_CPO.codProvincia");

      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = state.executeQuery(sql);
      while (rs.next()) {
        GeneralValueObject vo = new GeneralValueObject();
        vo.setAtributo("codPostal", rs.getString(campos.getString("SQL.T_CPO.codigo")));
        vo.setAtributo("defecto", rs.getString(campos.getString("SQL.T_CPO.defecto")));
        resultado.add(vo);
      }
      rs.close();
      state.close();
    }
    catch (Exception e) {
//      rollBackTransaction(bd, con, e);
        e.printStackTrace();
        if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }
    finally {
//      commitTransaction(bd, con);
        try {
            bd.devolverConexion(con);
        }catch (BDException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en CodPostalesDAO.getListaCodPostales");
        }
    }
    return resultado;
  }
  
  public Vector getListaCodPostalesDesdeMantenimiento(String[] params,GeneralValueObject gVO){
    String sql = "";
    Vector resultado = new Vector();
    AdaptadorSQLBD bd = null;
    Connection con=null;
    ResultSet rs=null;
    Statement state=null;
    try{
      bd = new AdaptadorSQLBD(params);
      con = bd.getConnection();
//      bd.inicioTransaccion(con);
      state = con.createStatement();
      sql = "SELECT * FROM T_CPO WHERE " +
        campos.getString("SQL.T_CPO.codPais")+"="+gVO.getAtributo("codPais");
      sql+= " ORDER BY " + campos.getString("SQL.T_CPO.codProvincia");

      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = state.executeQuery(sql);
      while (rs.next()) {
        GeneralValueObject vo = new GeneralValueObject();
        vo.setAtributo("codPostal", rs.getString(campos.getString("SQL.T_CPO.codigo")));
        vo.setAtributo("defecto", rs.getString(campos.getString("SQL.T_CPO.defecto")));
        resultado.add(vo);
      }
      rs.close();
      state.close();
    }
    catch (Exception e) {
//      rollBackTransaction(bd, con, e);
        e.printStackTrace();
        if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }
    finally {
//      commitTransaction(bd, con);
        try {
            bd.devolverConexion(con);
        }catch (BDException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en CodPostalesDAO.getListaCodPostales");
        }
    }
    return resultado;
  }
  
  public Vector modificarCodPostal(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    Vector resultado = new Vector();
    int cont = 0;
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);

      stmt = conexion.createStatement();
      sql = "SELECT * FROM T_DSU WHERE " +
            campos.getString("SQL.T_DSU.cpoPais")+"="+gVO.getAtributo("codPais")+" AND "+
                campos.getString("SQL.T_DSU.cpoProvincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                campos.getString("SQL.T_DSU.cpoMunicipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                campos.getString("SQL.T_DSU.codigoPostal")+"='"+gVO.getAtributo("codPostalAntiguo")+"'";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
          cont++;
      }
      rs.close();

      if(cont == 0) {
          sql = "UPDATE T_CPO SET " +
            campos.getString("SQL.T_CPO.codPais")+"="+gVO.getAtributo("codPais")+","+
            campos.getString("SQL.T_CPO.codProvincia")+"="+gVO.getAtributo("codProvincia")+","+
            campos.getString("SQL.T_CPO.codMunicipio")+"="+gVO.getAtributo("codMunicipio")+","+
            campos.getString("SQL.T_CPO.codigo")+"='"+gVO.getAtributo("codPostal")+"',"+
            campos.getString("SQL.T_CPO.defecto")+"="+gVO.getAtributo("defecto")+
            " WHERE " +
            campos.getString("SQL.T_CPO.codPais")+"="+gVO.getAtributo("codPais")+" AND "+
            campos.getString("SQL.T_CPO.codProvincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
            campos.getString("SQL.T_CPO.codMunicipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
            campos.getString("SQL.T_CPO.codigo")+"='"+gVO.getAtributo("codPostalAntiguo")+"'";
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          int res = stmt.executeUpdate(sql);
      }
      stmt.close();
      //m_Log.debug("las filas afectadas en el update son : " + res);
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    if(cont == 0) {
      resultado = getListaCodPostales(params,gVO);
    } else {
      gVO.setAtributo("puedeModificar","no");
        resultado.addElement(gVO);
    }
    return resultado;
  }
 public Vector modificarCodPostalDesdeMantenimiento(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    Vector resultado = new Vector();
    int cont = 0;
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);

      stmt = conexion.createStatement();
      sql = "SELECT * FROM T_DSU WHERE " +
            campos.getString("SQL.T_DSU.cpoPais")+"="+gVO.getAtributo("codPais")+" AND "+
                campos.getString("SQL.T_DSU.cpoProvincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                campos.getString("SQL.T_DSU.cpoMunicipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                campos.getString("SQL.T_DSU.codigoPostal")+"='"+gVO.getAtributo("codPostalAntiguo")+"'";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
          cont++;
      }
      rs.close();

      if(cont == 0) {
          sql = "UPDATE T_CPO SET " +
            campos.getString("SQL.T_CPO.codPais")+"="+gVO.getAtributo("codPais")+","+
            campos.getString("SQL.T_CPO.codProvincia")+"="+gVO.getAtributo("codProvincia")+","+
            campos.getString("SQL.T_CPO.codMunicipio")+"="+gVO.getAtributo("codMunicipio")+","+
            campos.getString("SQL.T_CPO.codigo")+"='"+gVO.getAtributo("codPostal")+"',"+
            campos.getString("SQL.T_CPO.defecto")+"="+gVO.getAtributo("defecto")+
            " WHERE " +
            campos.getString("SQL.T_CPO.codPais")+"="+gVO.getAtributo("codPais")+" AND "+
            campos.getString("SQL.T_CPO.codProvincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
            campos.getString("SQL.T_CPO.codMunicipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
            campos.getString("SQL.T_CPO.codigo")+"='"+gVO.getAtributo("codPostalAntiguo")+"'";
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          int res = stmt.executeUpdate(sql);
      }
      stmt.close();
      //m_Log.debug("las filas afectadas en el update son : " + res);
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    if(cont == 0) {
      resultado = getListaCodPostalesDesdeMantenimiento(params,gVO);
    } else {
      gVO.setAtributo("puedeModificar","no");
        resultado.addElement(gVO);
    }
    return resultado;
  }
  public Vector altaCodPostal(GeneralValueObject gVO, String[] params){
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
      sql = "INSERT INTO T_CPO("+
        campos.getString("SQL.T_CPO.codPais")+","+
        campos.getString("SQL.T_CPO.codProvincia")+","+
        campos.getString("SQL.T_CPO.codMunicipio")+","+
        campos.getString("SQL.T_CPO.codigo")+","+
        campos.getString("SQL.T_CPO.defecto")+
        ") VALUES (" +
        gVO.getAtributo("codPais")+","+
        gVO.getAtributo("codProvincia")+","+
        gVO.getAtributo("codMunicipio")+",'"+
        gVO.getAtributo("descPostal")+"',"+
        gVO.getAtributo("defecto")+")";
      
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
    resultado = getListaCodPostales(params,gVO);
    return resultado;
   }
public Vector altaCodPostalDesdeMantenimiento(GeneralValueObject gVO, String[] params){
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
      sql = "INSERT INTO T_CPO("+
        campos.getString("SQL.T_CPO.codPais")+","+
        campos.getString("SQL.T_CPO.codProvincia")+","+
        campos.getString("SQL.T_CPO.codMunicipio")+","+
        campos.getString("SQL.T_CPO.codigo")+","+
        campos.getString("SQL.T_CPO.defecto")+
        ") VALUES (" +
        gVO.getAtributo("codPais")+","+
        gVO.getAtributo("codProvincia")+","+
        gVO.getAtributo("codMunicipio")+",'"+
        gVO.getAtributo("codPostal")+"',"+
        gVO.getAtributo("defecto")+")";
      
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
    resultado = getListaCodPostalesDesdeMantenimiento(params,gVO);
    return resultado;
   }
  
public boolean existeCodPostal(String params[],String codigoPostal)
    throws BDException, SQLException{
        
       AdaptadorSQLBD dbAdapter = new AdaptadorSQLBD(params);
       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
       
       String sqlQuery = ("SELECT * FROM T_CPO WHERE "+
                           campos.getString("SQL.T_CPO.codigo")+"=?");
       
        try {
            con = dbAdapter.getConnection();
            ps = con.prepareStatement(sqlQuery);
            m_Log.debug(sqlQuery);
            m_Log.debug("PARAMETRO 1 --> CODIGO POSTAL: " + codigoPostal);
            int i=1;
            
            ps.setString(i++,codigoPostal);
            rs = ps.executeQuery();
            if (rs.next()) {
             return true;
            }
            else {
              
                return false;
                
            }

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            dbAdapter.devolverConexion(con);
        }
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


  /******************************************/


   /**
     * Da de alta un código postal
     * @param gVO: Objeto de la clase GeneralValueObject
     * @param conexion: Conexión a la BD
     * @return Se devuelve un Vector de objeto GeneralValueObject con la nueva lista de códigos postales
     * @throws es.altia.common.exception.TechnicalException si ocurre algún error durante la consulta a la BD
     */
    public boolean altaCodPostal(GeneralValueObject gVO,Connection conexion) throws TechnicalException{
        Statement stmt = null;        
        String sql = "";
        boolean exito = false;

        try{

          sql = "INSERT INTO T_CPO("+
                campos.getString("SQL.T_CPO.codPais")+","+
                campos.getString("SQL.T_CPO.codProvincia")+","+
                campos.getString("SQL.T_CPO.codMunicipio")+","+
                campos.getString("SQL.T_CPO.codigo")+","+
                campos.getString("SQL.T_CPO.defecto")+
                ") VALUES (" +
                gVO.getAtributo("codPais")+","+
                gVO.getAtributo("codProvincia")+","+
                gVO.getAtributo("codMunicipio")+",'"+
                gVO.getAtributo("descPostal")+"',"+
                gVO.getAtributo("defecto")+ ")";

          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          stmt = conexion.createStatement();
          int rowsUpdated = stmt.executeUpdate(sql);
          if(rowsUpdated==1) exito = true;

        }catch (Exception e){
            m_Log.error("Error al dar de alta un código postal: " + e.getMessage());
            throw new TechnicalException("Error al dar de alta un código postal: " + e.getMessage());
        }finally{
            try{
                if(stmt!=null) stmt.close();
            }catch(Exception e){
                m_Log.error("Error al dar de alta un código postal: " + e.getMessage());
            }
        }        
        return exito;
   }


   /**
     * Recupera la lista de códigos postales:
     * @param gVO: Objeto de la clase GeneralValueObject con la información del país y provincia del que se recuperan los códigos postales
     * @param con: Conexión a la BD ya inicializada
     * @return
     */
    public Vector getListaCodPostales(GeneralValueObject gVO,Connection con) throws TechnicalException{
        Vector resultado = new Vector();
        ResultSet rs=null;
        Statement state=null;

        try{

          state = con.createStatement();
          String sql = "SELECT * FROM T_CPO WHERE " +
                       campos.getString("SQL.T_CPO.codPais")+"="+gVO.getAtributo("codPais") +
                       " AND "+ campos.getString("SQL.T_CPO.codProvincia")+"="+gVO.getAtributo("codProvincia");

          sql+= " ORDER BY " + campos.getString("SQL.T_CPO.codProvincia");

          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          rs = state.executeQuery(sql);

          while (rs.next()) {              
            GeneralValueObject vo = new GeneralValueObject();
            vo.setAtributo("codPostal", rs.getString(campos.getString("SQL.T_CPO.codigo")));
            vo.setAtributo("defecto", rs.getString(campos.getString("SQL.T_CPO.defecto")));
            resultado.add(vo);
          }

        }catch (Exception e) {            
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new TechnicalException("Error al recuperar la lista de códigos postales en la operación getListaCodPostales: " + e.getMessage());
        }
        finally {
            try {
                if(state!=null) state.close();
                if(rs!=null) rs.close();
            }catch (Exception e) {                
                if (m_Log.isErrorEnabled()) m_Log.error("Error en claúsula finally de la operación getListaCodPostales: " + e.getMessage());
            }
        }
        
        return resultado;        
  }// getListaCodPostales


    /**
     * Comprueba si existe un código postal
     * @param codigoPostal: Código postal
     * @param con: Conexión a la BD
     * @return Un boolean 
     * @throws es.altia.common.exception.TechnicalException si ocurre algún error durante el acceso a la BD
     */
    public boolean existeCodPostal(String codigoPostal,Connection con) throws TechnicalException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exito = false;

        try {
            String sqlQuery = "SELECT * FROM T_CPO WHERE " + campos.getString("SQL.T_CPO.codigo")+"=?";
            ps = con.prepareStatement(sqlQuery);
            m_Log.debug(sqlQuery);
            m_Log.debug("PARAMETRO 1 --> CODIGO POSTAL: " + codigoPostal);
            int i=1;
            ps.setString(i++,codigoPostal);
            rs = ps.executeQuery();
            if (rs.next()) {
                exito = true;
            }            

        } catch(Exception e){
            m_Log.error("Error al comprobar la existencia del código postal: " + e.getMessage());
            throw new TechnicalException("Error al comprobar la existencia del código postal: " + e.getMessage());
        }finally {
            try{
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            }catch(Exception e){
                m_Log.error("Error en claúsula finally del método existeCodPostal: " + e.getMessage());
            }
        }
        return exito;
   }
    
        /**
     * Inserta un codigo postal, si no existe ya.
     * @param codPais
     * @param codProvincia
     * @param codMunicipio
     * @param codPostal
     * @param con
     * @throws java.sql.SQLException
     */
    public void altaCodPostal(int codPais, int codProvincia, int codMunicipio,String codPostal, Connection con) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";

        try {

          // Comprobamos si ya existe
          sql = "SELECT CPO_COD " +
                "FROM T_CPO " +
                "WHERE CPO_PAI = " + codPais + " AND CPO_PRV = " + codProvincia +
                " AND CPO_MUN = " + codMunicipio + " AND CPO_COD = ?";

          if (m_Log.isDebugEnabled()) m_Log.debug("CodPostalesDAO.altaCodPostal");
          if (m_Log.isDebugEnabled()) m_Log.debug(sql);

          ps = con.prepareStatement(sql);
          int i = 1;
          ps.setString(i++, codPostal);

          rs = ps.executeQuery();
          boolean existe = rs.next();
          rs.close();
          ps.close();

          // Se inserta si no existe
          if (!existe) {
              sql = "INSERT INTO T_CPO (CPO_PAI, CPO_PRV, CPO_MUN, CPO_COD, CPO_DEF) " +
                    "VALUES (" + codPais + "," + codProvincia + "," + codMunicipio + ",?,0)";

              if (m_Log.isDebugEnabled()) m_Log.debug(sql);

              ps = con.prepareStatement(sql);
              i = 1;
              ps.setString(i++, codPostal);

              ps.executeUpdate();
              ps.close();
          }

        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
    }

    
}