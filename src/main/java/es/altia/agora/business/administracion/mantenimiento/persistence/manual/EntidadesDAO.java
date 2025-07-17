// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.EntidadVO;
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
 * <p>Descripción: EntidadesesDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class EntidadesDAO  {
  private static EntidadesDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(EntidadesDAO.class.getName());

  protected EntidadesDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static EntidadesDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (EntidadesDAO.class) {
        if (instance == null) {
          instance = new EntidadesDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarEntidad(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    String existeAAE = "no";
    String existeAAU = "no";
    String existeRPG = "no";
    String existeRPU = "no";
    String existeUGO = "no";
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();

      sql = "SELECT * FROM A_AAE WHERE " +
      			campos.getString("SQL.A_AAE.organizacion")+"="+gVO.getAtributo("codOrganizacion")+" AND "+
        		campos.getString("SQL.A_AAE.entidad")+"="+gVO.getAtributo("codEntidad");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      if(rs.next()){
      	existeAAE = "si";
      }
      rs.close();
      stmt.close();
      sql = "SELECT * FROM A_AAU WHERE " +
      			campos.getString("SQL.A_AAU.organizacion")+"="+gVO.getAtributo("codOrganizacion")+" AND "+
        		campos.getString("SQL.A_AAU.entidad")+"="+gVO.getAtributo("codEntidad");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      if(rs.next()){
      	existeAAU = "si";
      }
      rs.close();
      stmt.close();
      sql = "SELECT * FROM A_RPG WHERE " +
      			campos.getString("SQL.A_RPG.organizacion")+"="+gVO.getAtributo("codOrganizacion")+" AND "+
        		campos.getString("SQL.A_RPG.entidad")+"="+gVO.getAtributo("codEntidad");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      if(rs.next()){
      	existeRPG = "si";
      }
      rs.close();
      stmt.close();
      sql = "SELECT * FROM A_RPU WHERE " +
      			campos.getString("SQL.A_RPU.organizacion")+"="+gVO.getAtributo("codOrganizacion")+" AND "+
        		campos.getString("SQL.A_RPU.entidad")+"="+gVO.getAtributo("codEntidad");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      if(rs.next()){
      	existeRPU = "si";
      }
      rs.close();
      stmt.close();
      sql = "SELECT * FROM A_UGO WHERE " +
      			campos.getString("SQL.A_UGO.organizacion")+"="+gVO.getAtributo("codOrganizacion")+" AND "+
        		campos.getString("SQL.A_UGO.entidad")+"="+gVO.getAtributo("codEntidad");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      if(rs.next()){
      	existeUGO = "si";
      }
      rs.close();
      stmt.close();
	  if(existeAAE.equals("no") && existeAAU.equals("no") &&
	    existeRPG.equals("no") && existeRPU.equals("no") && existeUGO.equals("no")) {
			    sql = "DELETE FROM A_ENT WHERE " +
			        campos.getString("SQL.A_ENT.organizacion")+"="+gVO.getAtributo("codOrganizacion")+" AND "+
			        campos.getString("SQL.A_ENT.codigo")+"="+gVO.getAtributo("codEntidad");
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt = conexion.createStatement();
			    stmt.executeUpdate(sql);
                stmt.close();

                CacheDatosFactoria.getImplEntidades().eliminarDatoClaveUnica((String)gVO.getAtributo("codOrganizacion"),
                        (String)gVO.getAtributo("codEntidad"));
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
    if(existeAAE.equals("no") && existeAAU.equals("no") && 
        existeRPG.equals("no") && existeRPU.equals("no") && existeUGO.equals("no")) {
    	lista = getListaEntidades(gVO,params);
    } else {
    	gVO.setAtributo("puedeEliminar","no");
    	lista.addElement(gVO);
    }
    return lista;
  }

  public Vector getListaEntidades(GeneralValueObject gVO1,String[] params){
    Vector resultado = new Vector();
    SortedMap <ArrayList<String>,EntidadVO> entidades = (SortedMap <ArrayList<String>,EntidadVO>) CacheDatosFactoria.getImplEntidades().getDatos();
    if (entidades!=null) {
        for(Map.Entry<ArrayList<String>,EntidadVO> entry : entidades.entrySet()) {
            EntidadVO entidad = entry.getValue();
            if (String.valueOf(entidad.getCodOrganizacion()).equals((String)gVO1.getAtributo("codOrganizacion"))) {
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codOrganizacion",String.valueOf(entidad.getCodOrganizacion()));
                gVO.setAtributo("codEntidad",String.valueOf(entidad.getCodEntidad()));
                gVO.setAtributo("descEntidad",entidad.getNombre());
                gVO.setAtributo("tipoEntidad",entidad.getTipoEntidad());
                gVO.setAtributo("directorio",entidad.getDirectorio());
                resultado.add(gVO);
            }
        }
    }
    return resultado;
  }

  public Vector modificarEntidad(GeneralValueObject gVO, String[] params){
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
      sql = "UPDATE A_ENT SET " +
        campos.getString("SQL.A_ENT.codigo")+"="+gVO.getAtributo("codEntidad")+","+ 
        campos.getString("SQL.A_ENT.nombre")+"='"+gVO.getAtributo("descEntidad")+"',"+
        campos.getString("SQL.A_ENT.tipo")+"='"+gVO.getAtributo("tipoEntidad")+"',"+
        campos.getString("SQL.A_ENT.dirTrabajo")+"='"+gVO.getAtributo("directorio")+"'"+
        " WHERE " +
        campos.getString("SQL.A_ENT.organizacion")+"="+gVO.getAtributo("codOrganizacion")+" AND "+
        campos.getString("SQL.A_ENT.codigo")+"="+gVO.getAtributo("codEntidadAntiguo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
      
      EntidadVO datoNuevo = new EntidadVO(Integer.valueOf((String)gVO.getAtributo("codOrganizacion")),
              Integer.valueOf((String)gVO.getAtributo("codEntidad")), (String)gVO.getAtributo("tipoEntidad"),
              (String)gVO.getAtributo("directorio"),(String)gVO.getAtributo("descEntidad"));
      CacheDatosFactoria.getImplEntidades().actualizarDatoClaveUnica(datoNuevo, 
              (String)gVO.getAtributo("codOrganizacion"), (String)gVO.getAtributo("codEntidadAntiguo"));
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
    lista = getListaEntidades(gVO,params);
    return lista;
  }

  public Vector altaEntidad(GeneralValueObject gVO, String[] params){
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
      sql = "INSERT INTO A_ENT("+
       campos.getString("SQL.A_ENT.organizacion")+","+ 
       campos.getString("SQL.A_ENT.codigo")+","+ 
       campos.getString("SQL.A_ENT.nombre")+","+ 
       campos.getString("SQL.A_ENT.tipo")+","+ 
       campos.getString("SQL.A_ENT.dirTrabajo")+
       ") VALUES (" + 
       gVO.getAtributo("codOrganizacion") +"," + 
       gVO.getAtributo("codEntidad") +",'" + 
       gVO.getAtributo("descEntidad") + "','"+
       gVO.getAtributo("tipoEntidad") + "','"+
       gVO.getAtributo("directorio") + "')";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
      EntidadVO dato = new EntidadVO(Integer.valueOf((String)gVO.getAtributo("codOrganizacion")),
              Integer.valueOf((String)gVO.getAtributo("codEntidad")),(String)gVO.getAtributo("tipoEntidad"),
              (String)gVO.getAtributo("directorio"),(String)gVO.getAtributo("descEntidad"));
        CacheDatosFactoria.getImplEntidades().insertarDato(dato);
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
    lista = getListaEntidades(gVO,params);
    return lista;
   }

  /**
   * Recupera la lista de entidades de una determinada organización
   * @param idOrganizacion: Id de la organización
   * @param params: Parámetros de conexión a la base de datos
   * @return Colección de entidades en forma de objetos GeneralValueTO
   */
  public Vector getListaEntidades(int idOrganizacion,String[] params){
    Vector resultado = new Vector();
    SortedMap <ArrayList<String>,EntidadVO> entidades = (SortedMap <ArrayList<String>,EntidadVO>) CacheDatosFactoria.getImplEntidades().getDatos();
    
    for(Map.Entry<ArrayList<String>,EntidadVO> entry : entidades.entrySet()) {
        EntidadVO entidad = entry.getValue();
        if (entidad.getCodOrganizacion() == idOrganizacion) {
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codOrganizacion",String.valueOf(entidad.getCodOrganizacion()));
            gVO.setAtributo("codEntidad",String.valueOf(entidad.getCodEntidad()));
            gVO.setAtributo("descEntidad",entidad.getNombre());
            gVO.setAtributo("tipoEntidad",entidad.getTipoEntidad());
            gVO.setAtributo("directorio",entidad.getDirectorio());
            resultado.add(gVO);
        }
    }
    return resultado;
  }
  
    public SortedMap cargaCacheEntidades(){ 
        
        Comparator comparador = new Comparator<ArrayList<String>>() {
            public int compare (ArrayList<String> listaA, ArrayList<String> listaB) {
                if (Integer.parseInt(listaA.get(0)) > Integer.parseInt(listaB.get(0)))
                    return 1;
                else if (Integer.parseInt(listaA.get(0)) < Integer.parseInt(listaB.get(0)))
                    return -1;
                else if (Integer.parseInt(listaA.get(1)) > Integer.parseInt(listaB.get(1)))
                    return 1;
                else if (Integer.parseInt(listaA.get(1)) < Integer.parseInt(listaB.get(1)))
                    return -1;
                else 
                    return 0;
            }
        };

        SortedMap listaEntidades = Collections.synchronizedSortedMap(new TreeMap<ArrayList<String>,EntidadVO>(comparador));
    
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
        String params[] ={ gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndiD };

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            
            String sql = "SELECT ENT_ORG, ENT_COD, ENT_TIP, ENT_DTR, ENT_NOM FROM A_ENT " + 
                    "ORDER BY ENT_ORG, ENT_COD";
            
            if(m_Log.isDebugEnabled()) m_Log.debug("CacheEntidades, sql: " + sql);
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                EntidadVO entidad = new EntidadVO();
                entidad.setCodOrganizacion(rs.getInt("ENT_ORG"));
                entidad.setCodEntidad(rs.getInt("ENT_COD"));
                entidad.setTipoEntidad(rs.getString("ENT_TIP"));
                entidad.setDirectorio(rs.getString("ENT_DTR"));
                entidad.setNombre(rs.getString("ENT_NOM"));
                ArrayList <String> clave = new ArrayList<String>(2); 
                clave.add(String.valueOf(entidad.getCodOrganizacion()));
                clave.add(String.valueOf(entidad.getCodEntidad()));
                listaEntidades.put(clave,entidad);
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
            return listaEntidades;
        }
    }
   

}