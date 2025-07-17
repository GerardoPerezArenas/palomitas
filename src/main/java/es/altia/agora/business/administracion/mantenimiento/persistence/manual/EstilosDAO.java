// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.OrganizacionVO;
import java.util.Vector;
import java.sql.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.*;
import es.altia.util.conexion.*;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.util.cache.CacheDatosFactoria;
import java.util.Map;
import java.util.SortedMap;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: OrganizacionesesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class EstilosDAO  {
  private static EstilosDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(EstilosDAO.class.getName());

  protected EstilosDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static EstilosDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (EstilosDAO.class) {
        if (instance == null) {
          instance = new EstilosDAO();
        }
      }
    }
    return instance;
  }

  public Vector getListaOrganizaciones(String[] params){
    Vector resultado = new Vector();
    SortedMap <String,OrganizacionVO> organizaciones = (SortedMap <String,OrganizacionVO>) CacheDatosFactoria.getImplOrganizaciones().getDatos();
    if (organizaciones!=null) {
        for(Map.Entry<String,OrganizacionVO> entry : organizaciones.entrySet()) {
            OrganizacionVO organizacion = entry.getValue();
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codigo",String.valueOf(organizacion.getCodOrganizacion()));
            gVO.setAtributo("descripcion",organizacion.getDescripcionOrganizacion());
            gVO.setAtributo("icono",organizacion.getIconoOrganizacion());
            gVO.setAtributo("css",String.valueOf(organizacion.getCssOrganizacion()));
            resultado.add(gVO);
        }
    }
    return resultado;
  }

   public Vector getListaCss(String[] params){
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
      sql = "SELECT * FROM " + GlobalNames.ESQUEMA_GENERICO + "A_CSS";
     // String[] orden = {campos.getString("SQL.A_ORG.codigo"),"1"};
     // sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codigo",rs.getString("CSS_COD"));
        gVO.setAtributo("ruta",rs.getString("CSS_RUTA"));
        gVO.setAtributo("descripcion",rs.getString("CSS_DES"));
        gVO.setAtributo("general",rs.getString("CSS_GENERAL"));
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

   public Vector modificarCssOrganizacion(GeneralValueObject gVO, String[] params){
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
      sql = "UPDATE A_ORG SET ORG_CSS="+gVO.getAtributo("codCss")+" WHERE " +
        campos.getString("SQL.A_ORG.codigo")+"="+gVO.getAtributo("codOrg");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      int res = stmt.executeUpdate(sql);
      stmt.close();
      
      OrganizacionVO datoAntiguo = (OrganizacionVO)CacheDatosFactoria.getImplOrganizaciones().getDatoClaveUnica((String)gVO.getAtributo("codOrg"));
      OrganizacionVO datoNuevo = new OrganizacionVO(datoAntiguo);
      datoNuevo.setCssOrganizacion(Integer.parseInt((String)gVO.getAtributo("codCss")));
      CacheDatosFactoria.getImplOrganizaciones().actualizarDatoClaveUnica(datoNuevo,(String)gVO.getAtributo("codOrg"));
      //m_Log.debug("las filas afectadas en el update son : " + res);
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
    Vector lista = new Vector();
    lista = getListaOrganizaciones(params);
    return lista;
  }

   
  public Vector modificarCss(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
     String sql2 = "";
     m_Log.debug("************   EN CONCULTA"+gVO.getAtributo("activo"));
    
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      sql = "UPDATE A_CSS SET CSS_GENERAL="+gVO.getAtributo("activo")+" WHERE CSS_COD="+gVO.getAtributo("codiCss");
      sql2 = "UPDATE A_CSS SET CSS_GENERAL=0 WHERE CSS_COD="+gVO.getAtributo("codCss");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql+" // "+sql2);
      stmt = conexion.createStatement();
      int res = stmt.executeUpdate(sql);
      stmt.close();
       stmt = conexion.createStatement();
      int res2 = stmt.executeUpdate(sql2);
      stmt.close();
      //m_Log.debug("las filas afectadas en el update son : " + res);
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
    Vector lista = new Vector();
    lista = getListaCss(params);
    return lista;
  }
  public Vector eliminarCss(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
     m_Log.debug("************   EN CONCULTA"+gVO.getAtributo("activo"));
    
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      sql = "DELETE FROM A_CSS WHERE CSS_COD="+gVO.getAtributo("codiCss"); 
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      int res = stmt.executeUpdate(sql);
      stmt.close();
       stmt = conexion.createStatement();
      stmt.close();
      //m_Log.debug("las filas afectadas en el update son : " + res);
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
    Vector lista = new Vector();
    lista = getListaCss(params);
    return lista;
  }
  
   public Vector buscarCss(GeneralValueObject gVO, String[] params){
     Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
     m_Log.debug("************   EN CONCULTA"+gVO.getAtributo("codCss"));
    
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
       // Creamos la select con los parametros adecuados.
      sql = "SELECT CSS_RUTA FROM A_CSS WHERE CSS_COD="+gVO.getAtributo("codCss"); 
     
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        gVO.setAtributo("ruta",rs.getString("CSS_RUTA"));
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

      
      
      
      public Vector insertarCss(GeneralValueObject gVO, String[] params){
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
     String sql2 = "";
     int codigo=0;
     String desc=(String)gVO.getAtributo("descripcion");
     String ruta=(String )gVO.getAtributo("nombreFichero");
     
     m_Log.debug("************   EN CONCULTA"+gVO.getAtributo("descripcion"));
      m_Log.debug("************   EN CONCULTA nombreFichero "+gVO.getAtributo("nombreFichero"));
    
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      //sacamos el id con que guardar:
      sql = "SELECT MAX(CSS_COD) AS COD FROM A_CSS"; 
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
       while(rs.next()){
             codigo=rs.getInt("COD");
      }
      codigo=codigo+1;
      rs.close();
      stmt.close();
      sql="INSERT INTO A_CSS (CSS_COD,CSS_RUTA,CSS_DES,CSS_GENERAL) VALUES ("+codigo+",'"+ruta+"','"+desc+"',0)";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql+" // "+sql);
       stmt = conexion.createStatement();
      int res = stmt.executeUpdate(sql);
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
    Vector lista = new Vector();
    lista = getListaCss(params);
    return lista;
  }

}