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

import java.sql.*;
import java.util.Vector;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: TiposViasDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class TiposViasDAO  {
  private static TiposViasDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(TiposViasDAO.class.getName());

  protected TiposViasDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static TiposViasDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (TiposViasDAO.class) {
        if (instance == null) {
          instance = new TiposViasDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarTipoVia(String codigo, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    int cont = 0;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      stmt = conexion.createStatement();
      
      sql = "SELECT * FROM T_VIA WHERE " + 
            campos.getString("SQL.T_VIA.tipo")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont++;
      }
      
      if(cont == 0) {
	      sql = "DELETE FROM T_TVI WHERE " + 
	        campos.getString("SQL.T_TVI.codigo")+"="+codigo;
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	      stmt.executeUpdate(sql);
      }
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    Vector lista = new Vector();
    if(cont == 0) {
      lista = getListaTiposVias(params);
    } else {
    	GeneralValueObject gVO = new GeneralValueObject();
    	gVO.setAtributo("puedeEliminar","no");
    	lista.addElement(gVO);
    }
    return lista;
  }

  public Vector getListaTiposVias(String[] params){
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
      sql = "SELECT * FROM T_TVI ";
      String[] orden = {campos.getString("SQL.T_TVI.tipoVia"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codTipoVia",rs.getString(campos.getString("SQL.T_TVI.codigo")));
        gVO.setAtributo("descTipoVia",rs.getString(campos.getString("SQL.T_TVI.tipoVia")));
        gVO.setAtributo("abreviatura",rs.getString(campos.getString("SQL.T_TVI.abreviatura")));
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
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en TiposViasDAO.getListaTiposVias");
        }
    }
    return resultado;
  }

  public Vector modificarTipoVia(GeneralValueObject gVO, String[] params){
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
      sql = "UPDATE T_TVI SET " + 
        campos.getString("SQL.T_TVI.codigo")+"="+gVO.getAtributo("codTipoVia")+","+ 
        campos.getString("SQL.T_TVI.abreviatura")+"='"+gVO.getAtributo("abreviatura")+"',"+
        campos.getString("SQL.T_TVI.tipoVia")+"='"+gVO.getAtributo("descTipoVia")+"'"+
        " WHERE " + 
        campos.getString("SQL.T_TVI.codigo")+"="+gVO.getAtributo("codTipoViaAntiguo");
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
    Vector lista = new Vector();
    lista = getListaTiposVias(params);
    return lista;
  }

  public Vector altaTipoVia(GeneralValueObject gVO, String[] params){
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
      abd.inicioTransaccion(conexion);
      sql = "INSERT INTO T_TVI("+
       campos.getString("SQL.T_TVI.codigo")+","+ 
       campos.getString("SQL.T_TVI.abreviatura")+","+
       campos.getString("SQL.T_TVI.tipoVia")+
       ") VALUES (" + 
       gVO.getAtributo("codTipoVia") +",'" + 
       gVO.getAtributo("abreviatura") +"','" + 
       gVO.getAtributo("descTipoVia") + "')";
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
    return getListaTiposVias(params);
   }

    public GeneralValueObject getTipoViaByAbreviatura(String abrvTipoVia, String[] params) throws BDException, SQLException {

        String sqlQuery = "SELECT TVI_COD, TVI_DES FROM T_TVI WHERE TVI_ABR = ?";

        GeneralValueObject tipoViaDefecto = new GeneralValueObject();
        tipoViaDefecto.setAtributo("codTipoVia", "0");
        tipoViaDefecto.setAtributo("abrvTipoVia", "SV");
        tipoViaDefecto.setAtributo("descTipoVia", "SIN TIPO VIA");

        AdaptadorSQLBD dbAdapter = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbAdapter.getConnection();
            ps = con.prepareStatement(sqlQuery);
            m_Log.debug(sqlQuery);
            m_Log.debug("PARAMETRO 1 --> ABREVIATURA DE VIA: " + abrvTipoVia);
            ps.setString(1, abrvTipoVia);

            rs = ps.executeQuery();
            if (rs.next()) {
                GeneralValueObject tipoViaRecuperado = new GeneralValueObject();
                tipoViaRecuperado.setAtributo("codTipoVia", rs.getString(1));
                tipoViaRecuperado.setAtributo("abrvTipoVia", abrvTipoVia);
                tipoViaRecuperado.setAtributo("descTipoVia", rs.getString(2));
                return tipoViaRecuperado;
            }
            else return tipoViaDefecto;

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            dbAdapter.devolverConexion(con);
        }
   }

   public GeneralValueObject getTipoViaByDescripcion(String descTipoVia, String[] params) throws BDException, SQLException {

        String sqlQuery = "SELECT TVI_COD, TVI_ABR FROM T_TVI WHERE TVI_DES = ?";

        GeneralValueObject tipoViaDefecto = new GeneralValueObject();
        tipoViaDefecto.setAtributo("codTipoVia", "0");
        tipoViaDefecto.setAtributo("abrvTipoVia", "SV");
        tipoViaDefecto.setAtributo("descTipoVia", "SIN TIPO VIA");

        AdaptadorSQLBD dbAdapter = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbAdapter.getConnection();
            ps = con.prepareStatement(sqlQuery);
            m_Log.debug(sqlQuery);
            m_Log.debug("PARAMETRO 1 --> DESCRIPCION DE VIA: " + descTipoVia);
            ps.setString(1, descTipoVia);

            rs = ps.executeQuery();
            if (rs.next()) {
                GeneralValueObject tipoViaRecuperado = new GeneralValueObject();
                tipoViaRecuperado.setAtributo("codTipoVia", rs.getString(1));
                tipoViaRecuperado.setAtributo("abrvTipoVia", rs.getString(2));
                tipoViaRecuperado.setAtributo("descTipoVia", descTipoVia);
                return tipoViaRecuperado;
            }
            else return tipoViaDefecto;

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
  
}