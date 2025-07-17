// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.EntidadVO;
import es.altia.agora.business.administracion.OrganizacionVO;
import java.util.Vector;
import java.sql.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.*;
import es.altia.util.conexion.*;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.util.cache.CacheDatosFactoria;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: OrganizacionesesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class OrganizacionesDAO  {
  private static OrganizacionesDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(OrganizacionesDAO.class.getName());

  protected OrganizacionesDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static OrganizacionesDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (OrganizacionesDAO.class) {
        if (instance == null) {
          instance = new OrganizacionesDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarOrganizacion(String codigo, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    String existeEntidad = "no";
    String existeMNU = "no";
    String existeOUS = "no";
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      
      EntidadVO entidad = (EntidadVO) CacheDatosFactoria.getImplEntidades().getDatoClaveUnica(codigo);
      
      if (entidad!=null) 
          existeEntidad = "si";

      sql = "SELECT * FROM A_MNU WHERE " +
      	    campos.getString("SQL.A_MNU.organizacion")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      if(rs.next()){
      	existeMNU = "si";
      }
      rs.close();
      stmt.close();

      sql = "SELECT * FROM  A_OUS WHERE " +
      	    campos.getString("SQL.A_OUS.organizacion")+"="+codigo;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      if(rs.next()){
      	existeOUS = "si";
      }
      rs.close();
      stmt.close();

      if(existeEntidad.equals("no") && existeMNU.equals("no") && existeOUS.equals("no")) {
	      sql = "DELETE FROM A_ORG WHERE " +
	            campos.getString("SQL.A_ORG.codigo")+"="+codigo;
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          stmt = conexion.createStatement();
	      stmt.executeUpdate(sql);
          stmt.close();

          CacheDatosFactoria.getImplOrganizaciones().eliminarDatoClaveUnica(codigo);
      }
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
    if(existeEntidad.equals("no") && existeMNU.equals("no") && existeOUS.equals("no")) {
      lista = getListaOrganizaciones(params);
    } else {
    	GeneralValueObject gVO = new GeneralValueObject();
    	gVO.setAtributo("puedeEliminar","no");
    	lista.addElement(gVO);
    }
    return lista;
  }

  public Vector getListaOrganizaciones(String[] params){
    
    Vector resultado = new Vector();
    SortedMap <String,OrganizacionVO> organizaciones = (SortedMap <String,OrganizacionVO>) CacheDatosFactoria.getImplOrganizaciones().getDatos();
    if (organizaciones != null){
        for(Map.Entry<String,OrganizacionVO> entry : organizaciones.entrySet()) {
            OrganizacionVO organizacion = entry.getValue();
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codigo",String.valueOf(organizacion.getCodOrganizacion()));
            gVO.setAtributo("descripcion",organizacion.getDescripcionOrganizacion());
            gVO.setAtributo("icono",organizacion.getIconoOrganizacion());
            resultado.add(gVO);
        }
    }
    return resultado;
  }

  public Vector modificarOrganizacion(GeneralValueObject gVO, String[] params){
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
      sql = "UPDATE A_ORG SET " +
        campos.getString("SQL.A_ORG.codigo")+"="+gVO.getAtributo("codigo")+","+
        campos.getString("SQL.A_ORG.descripcion")+"='"+gVO.getAtributo("descripcion")+"',"+
        //campos.getString("SQL.A_ORG.descripcion")+"='"+gVO.getAtributo("codMunicipio")+"'"+
        campos.getString("SQL.A_ORG.icono")+"='"+gVO.getAtributo("icono")+"'"+
        " WHERE " +
        campos.getString("SQL.A_ORG.codigo")+"="+gVO.getAtributo("codigoAntiguo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      int res = stmt.executeUpdate(sql);
      stmt.close();
      if (res>0) {
        OrganizacionVO datoAntiguo = (OrganizacionVO)CacheDatosFactoria.getImplOrganizaciones().getDatoClaveUnica((String)gVO.getAtributo("codigoAntiguo"));
        OrganizacionVO datoNuevo = new OrganizacionVO(Integer.valueOf((String)gVO.getAtributo("codigo")),
                (String)gVO.getAtributo("descripcion"),(String)gVO.getAtributo("icono"),datoAntiguo.getCssOrganizacion());
      
        CacheDatosFactoria.getImplOrganizaciones().actualizarDatoClaveUnica(datoNuevo,(String)gVO.getAtributo("codigoAntiguo"));
      }
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

  public Vector altaOrganizacion(GeneralValueObject gVO, String[] params){
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
      sql = "INSERT INTO A_ORG("+
       campos.getString("SQL.A_ORG.codigo")+","+
       campos.getString("SQL.A_ORG.descripcion")+","+
       //campos.getString("SQL.A_ORG.descripcion")+","+
       campos.getString("SQL.A_ORG.icono")+
       ") VALUES (" +
       gVO.getAtributo("codigo") +",'" +
       gVO.getAtributo("descripcion") + "','"+
       //gVO.getAtributo("descripcion") + "','"+
       gVO.getAtributo("icono") + "')";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
      OrganizacionVO dato = new OrganizacionVO(Integer.valueOf((String)gVO.getAtributo("codigo")),
              (String)gVO.getAtributo("descripcion"),(String)gVO.getAtributo("icono"),1);
      CacheDatosFactoria.getImplOrganizaciones().insertarDato(dato);

      //m_Log.debug("las filas afectadas en el insert son : " + res);
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


  public String getDescripcionOrganizacion(String codOrganizacion, String[] params){
    
    String descripcion = null;
    OrganizacionVO org = (OrganizacionVO)CacheDatosFactoria.getImplOrganizaciones().getDatoClaveUnica(codOrganizacion);
    if (org!=null)
        descripcion = org.getDescripcionOrganizacion();
    
    return descripcion;    
   }



  /**
   * Recupera las organizaciones distintas a una dada. Util por ejemplo para recuperar información de organizaciones que no
   * no son de pruebas
   * @param codOrganizacion: Código de la organización
   * @param params: Parámetros de conexión a la base de datos
   * @return
   */
  public ArrayList<OrganizacionVO> getOrganizacionDistintasDe(int codOrganizacion,Connection con){
        ArrayList<OrganizacionVO> orgs = new ArrayList<OrganizacionVO>();
        SortedMap <String,OrganizacionVO> organizaciones = (SortedMap <String,OrganizacionVO>) CacheDatosFactoria.getImplOrganizaciones().getDatos();
        if (organizaciones!=null) {
            for(Map.Entry<String,OrganizacionVO> entry : organizaciones.entrySet()) {
                OrganizacionVO org = entry.getValue();
                if (org.getCodOrganizacion() != codOrganizacion)
                    orgs.add(org);
            }
        }
        return orgs;
  }

  public String getDescripcionOrganizacion(int codOrganizacion, Connection con){    
    
    String descripcion = null;
    
    OrganizacionVO org = (OrganizacionVO)CacheDatosFactoria.getImplOrganizaciones().getDatoClaveUnica(String.valueOf(codOrganizacion));

    if (org!=null)
        descripcion = org.getDescripcionOrganizacion();
        
    return descripcion;    
   }

    public SortedMap cargaCacheOrganizaciones(){ 
        
        Comparator comparador = new Comparator<String>() {
            public int compare (String stringA, String stringB) {
                if (Integer.parseInt(stringA) > Integer.parseInt(stringB))
                    return 1;
                else if (Integer.parseInt(stringA) < Integer.parseInt(stringB))
                    return -1;
                else 
                    return 0;
            }
        };

        SortedMap listaOrganizaciones = Collections.synchronizedSortedMap(new TreeMap<String,OrganizacionVO>(comparador));
    
        Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");

        String jndiD = m_ConfigTechnical.getString("CON.jndi");
        String gestorD = m_ConfigTechnical.getString("CON.gestor");
        String driverD = m_ConfigTechnical.getString("CON.driver");
        String urlD = m_ConfigTechnical.getString("CON.url");
        String usuarioD = m_ConfigTechnical.getString("CON.usuario");
        String passwordD = m_ConfigTechnical.getString("CON.password");
        String fichLogD = m_ConfigTechnical.getString("CON.fichlog");

        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String params[] = { gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            
            String sql = "SELECT ORG_COD, ORG_DES, ORG_ICO, ORG_CSS FROM A_ORG ORDER BY ORG_COD";
            
            if(m_Log.isDebugEnabled()) m_Log.debug("OrganizacionesDAO.cargaCacheOrganizaciones, sql: " + sql);
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                OrganizacionVO organizacion = new OrganizacionVO();
                organizacion.setCodOrganizacion(rs.getInt("ORG_COD"));
                organizacion.setDescripcionOrganizacion(rs.getString("ORG_DES"));
                organizacion.setIconoOrganizacion(rs.getString("ORG_ICO"));
                organizacion.setCssOrganizacion(rs.getInt("ORG_CSS"));
                listaOrganizaciones.put(String.valueOf(organizacion.getCodOrganizacion()),organizacion);
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        } catch (Exception e) {
            m_Log.error(e.getMessage());
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(oad, con);
            } catch (Exception e) {
            m_Log.error(e.getMessage());
            }
        }
        return listaOrganizaciones;
    }

}