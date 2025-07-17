// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.exception.LoginDuplicadoException;
import es.altia.agora.business.administracion.mantenimiento.UsuariosGruposValueObject;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UsuariosGruposDAO;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.exception.UsuarioNoValidadoException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.util.Vector;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DepartamentosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class UsuariosGruposManager  {
  private static UsuariosGruposManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(UsuariosGruposManager.class.getName());

  protected UsuariosGruposManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static UsuariosGruposManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(UsuariosGruposManager.class) {
        if (instance == null) {
          instance = new UsuariosGruposManager();
        }
      }
    }
    return instance;
  }

  public Vector getUsuarios(String[] params){
    log.debug("getUsuarios");
    UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = ugDAO.getUsuarios(params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector getUsuariosLocal(String codOrg,String[] params){
    log.debug("getUsuarios");
    UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = ugDAO.getUsuariosLocal(codOrg,params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  public Vector getUsuariosFirmantesUnidadCargo(String codOrg,String uor, String cargo,String[] params){
    log.debug("getUsuarios");
    UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = ugDAO.getUsuariosFirmantesUnidadCargo(codOrg,uor,cargo,params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  public Vector getGrupos(String[] params){
    log.debug("getGrupos");
    UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = ugDAO.getGrupos(params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  public UsuariosGruposValueObject getDatosUsuarios(UsuariosGruposValueObject u,String codUsuario,String[] params, String portafirmas){
    log.debug("getGrupos");
    UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
    UsuariosGruposValueObject ugVO = new UsuariosGruposValueObject();
    try{
      log.debug("Usando persistencia manual");
      ugVO = ugDAO.getDatosUsuarios(u,codUsuario,params, portafirmas);
      return ugVO;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return ugVO;
    }
  }
  
  public Vector getListaUnidadesOrganicasUsuario(UsuariosGruposValueObject u,String[] params){
    log.debug("getListaUnidadesOrganicasUsuario");
    UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
    Vector lista = new Vector();
    try{
      log.debug("Usando persistencia manual");
      lista = ugDAO.getListaUnidadesOrganicasUsuario(u,params);
      return lista;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return lista;
    }
  }
  
  public UsuariosGruposValueObject getDatosUsuariosLocal(UsuariosGruposValueObject u,String codUsuario,String codOrg,String codEnt,String[] params, String portafirmas){
    log.debug("getGrupos");
    UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
    UsuariosGruposValueObject ugVO = new UsuariosGruposValueObject();
    try{
      log.debug("Usando persistencia manual");
      ugVO = ugDAO.getDatosUsuariosLocal(u,codUsuario,codOrg,codEnt,params, portafirmas);
      return ugVO;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return ugVO;
    }
  }
  
  public Vector getListaOrganizaciones(String codUsuario,String[] params){
    log.debug("getListaOrganizaciones");
    UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
    Vector lista = new Vector();
    try{
      log.debug("Usando persistencia manual");
      lista = ugDAO.getListaOrganizaciones(codUsuario,params);
      return lista;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return lista;
    }
  }
  
  public Vector getListaOrganizacionesLocal(String codOrg,String descOrg,String codUsuario,String[] params){
    log.debug("getListaOrganizaciones");
    UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
    Vector lista = new Vector();
    try{
      log.debug("Usando persistencia manual");
      lista = ugDAO.getListaOrganizacionesLocal(codOrg,descOrg,codUsuario,params);
      return lista;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return lista;
    }
  }
  
  public Vector getListaUsuariosGrupos(String codGrupo,String[] params){
    log.debug("getListaUsuariosGrupos");
    UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
    Vector lista = new Vector();
    try{
      log.debug("Usando persistencia manual");
      lista = ugDAO.getListaUsuariosGrupos(codGrupo,params);
      return lista;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return lista;
    }
  }

public boolean tienePermisoDirectiva(String codDirectiva, int codUsuario, String[] params){
    log.debug("tienePermisoDirectiva");
    UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
    boolean tienePermiso = false;
    try {
      tienePermiso = ugDAO.tienePermisoDirectiva(codDirectiva, codUsuario, params);
      return tienePermiso;
    } catch(Exception ce) {
      log.error("JDBC Technical problem " + ce.getMessage());
      return tienePermiso;
    }
}

    public int insertarUsuario(UsuariosGruposValueObject u, String[] params, String portafirmas) throws LoginDuplicadoException {

        UsuarioManager usuarioManager = UsuarioManager.getInstance();
        UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();

        UsuarioEscritorioValueObject userDesktop = new UsuarioEscritorioValueObject();
        userDesktop.setLogin(u.getLogin());
        userDesktop.setPassword(u.getContrasena());

        /*try {*/
            int resultado = ugDAO.insertarUsuario(u, params, portafirmas);
            
            /**
             * Debido a problemas con el alta de nuevos usuarios, se elimina este if del código.
             * Con él lo que se hace es, una vez dado de alta dicho usuario,se comprueba que existe
             * en el sistema externo. Por ejemplo, si me autentico con LDAP y doy de alta un nuevo usuario,
             * se comprueba que existe en dicho LDAP y si no no se da de alta.
             */
            /*if (resultado != -1) {
                usuarioManager.validarUsuarioAlta(userDesktop);
            }*/

            return resultado;

        /*} catch (AuthenticationException ae) {
            ugDAO.eliminarUsuario(u.getCodUsuario(), params);
            throw new UsuarioNoValidadoException(ae.getMessage());
        }*/

    }

    public int modificarUsuario(UsuariosGruposValueObject u, String[] params, String portafirmas) throws UsuarioNoValidadoException, TechnicalException {

        UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
        //UsuarioManager usuarioManager = UsuarioManager.getInstance();

        UsuarioEscritorioValueObject userDesktop = new UsuarioEscritorioValueObject();
        userDesktop.setLogin(u.getLogin());
        userDesktop.setPassword(u.getContrasena());

        //UsuariosGruposValueObject infoAntigua = ugDAO.getDatosUsuarios(new UsuariosGruposValueObject(), u.getCodUsuario(), params, portafirmas);

        //try {

          
	int resultado = ugDAO.modificarUsuario(u, params, portafirmas);


            /**
            * Debido a problemas con el alta de nuevos usuarios, se elimina este if del c?digo.
            * Con ?l lo que se hace es, una vez dado de alta dicho usuario,se comprueba que existe
            * en el sistema externo. Por ejemplo, si me autentico con LDAP y doy de alta un nuevo usuario,
            * se comprueba que existe en dicho LDAP y si no no se da de alta.
            */

            /*if (resultado != -1) {
                usuarioManager.validarUsuarioAlta(userDesktop);
            }*/
            return resultado;


        /*} catch (Exception ae) {
            ugDAO.modificarUsuario(infoAntigua, params);
            throw new UsuarioNoValidadoException(ae.getMessage());
        } */
    }
    
    // Modifica solo los datos que puede cambiar cualquier usuario (nombre, contraseña, nif...)
    public int modificarDatosBasicosUsuario(UsuariosGruposValueObject usuarioVO, String[] params) 
    	throws UsuarioNoValidadoException, TechnicalException {

        UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
        UsuarioManager usuarioManager = UsuarioManager.getInstance();

        UsuarioEscritorioValueObject userDesktop = new UsuarioEscritorioValueObject();
        userDesktop.setLogin(usuarioVO.getLogin());
        userDesktop.setPassword(usuarioVO.getContrasena());

        //UsuariosGruposValueObject infoAntigua = ugDAO.getDatosUsuarios(new UsuariosGruposValueObject(), usuarioVO.getCodUsuario(), params);

        //try {
            int resultado = ugDAO.modificarDatosBasicosUsuario(usuarioVO, params);
              /**
            * Debido a problemas con el alta de nuevos usuarios, se elimina este if del c?digo.
            * Con ?l lo que se hace es, una vez dado de alta dicho usuario,se comprueba que existe
            * en el sistema externo. Por ejemplo, si me autentico con LDAP y doy de alta un nuevo usuario,
            * se comprueba que existe en dicho LDAP y si no no se da de alta.
            */

            /*if (resultado != -1) {
                usuarioManager.validarUsuarioAlta(userDesktop);
            }*/
            return resultado;

        /*} catch (AuthenticationException ae) {
            //ugDAO.modificarUsuario(infoAntigua, params);
            throw new UsuarioNoValidadoException(ae.getMessage());
        }*/
    }

    public int modificarUsuarioLocal(UsuariosGruposValueObject u, String codOrg, String codEnt, String[] params, String portafirmas) throws UsuarioNoValidadoException, TechnicalException {

        UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
        UsuarioManager usuarioManager = UsuarioManager.getInstance();

        UsuarioEscritorioValueObject userDesktop = new UsuarioEscritorioValueObject();
        userDesktop.setLogin(u.getLogin());
        userDesktop.setPassword(u.getContrasena());

        UsuariosGruposValueObject infoAntigua = ugDAO.getDatosUsuariosLocal(new UsuariosGruposValueObject(), u.getCodUsuario(), codOrg, codEnt, params, portafirmas);

        //try {
            int resultado = ugDAO.modificarUsuarioLocal(u, codOrg, params, portafirmas);
              /**
            * Debido a problemas con el alta de nuevos usuarios, se elimina este if del c?digo.
            * Con ?l lo que se hace es, una vez dado de alta dicho usuario,se comprueba que existe
            * en el sistema externo. Por ejemplo, si me autentico con LDAP y doy de alta un nuevo usuario,
            * se comprueba que existe en dicho LDAP y si no no se da de alta.
            */

            /*if (resultado != -1) {
                usuarioManager.validarUsuarioAlta(userDesktop);
            }*/
            return resultado;

        /*} catch (AuthenticationException ae) {
            ugDAO.modificarUsuarioLocal(infoAntigua, codOrg, params);
            throw new UsuarioNoValidadoException(ae.getMessage());
        }*/
    }
  
  public int eliminarUsuario(String codUsuario,String[] params){
    log.debug("eliminarUsuario");
    UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
    int resultado = 0;
    try{
      log.debug("Usando persistencia manual");
      resultado = ugDAO.eliminarUsuario(codUsuario,params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  public int insertarGrupo(UsuariosGruposValueObject u,String[] params){
    log.debug("insertarGrupo");
    UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
    int resultado = 0;
    try{
      log.debug("Usando persistencia manual");
      resultado = ugDAO.insertarGrupo(u,params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  public int eliminarGrupo(String codGrupo,String[] params){
    log.debug("eliminarUsuario");
    UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
    int resultado = 0;
    try{
      log.debug("Usando persistencia manual");
      resultado = ugDAO.eliminarGrupo(codGrupo,params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  public int modificarGrupo(UsuariosGruposValueObject u,String[] params){
    log.debug("modificarUsuario");
    UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
    int resultado = 0;
    try{
      log.debug("Usando persistencia manual");
      resultado = ugDAO.modificarGrupo(u,params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  public String obtenerJNDI(String codEntidad, String codOrganizacion, String codAplicacion,
                               String[] params) {
    log.debug("obtenerJNDI");
    UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
    String jndi = null;
    try{
      log.debug("Usando persistencia manual");
      jndi = ugDAO.obtenerJNDI(codEntidad, codOrganizacion, codAplicacion, params);
      return jndi;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return jndi;
    }
  }
  
  

    public Vector getListaCargos(UsuariosGruposValueObject u,String[] params){
      log.debug("getListaCargos");
      UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
      Vector lista = null;
      try{
        log.debug("Usando persistencia manual");
        lista = ugDAO.getListaCargos(u,params);
        return lista;
      }catch(Exception ce){
        log.error("JDBC Technical problem " + ce.getMessage());
        return lista;
      }
    }


    public int cuentaDocumentosPendientesDeFirmarPorUsuario(int codigoUsuario, String[] params) {
        log.debug("cuentaDocumentosPendientesDeFirmarPorUsuario");
        UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
        return ugDAO.cuentaDocumentosPendientesDeFirmarPorUsuario(codigoUsuario,params);
    }

    public int cuentaPlantillasParaFirmarPorUsuario(int codigoUsuario, String[] params) {
        log.debug("cuentaPlantillasParaFirmarPorUsuario");
        UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
        return ugDAO.cuentaPlantillasParaFirmarPorUsuario(codigoUsuario,params);
    }

    public int cuentaDelegacionesHaciaUsuario(int codigoUsuario, String[] params) {
        log.debug("cuentaDelegacionesHaciaUsuario");
        UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
        return ugDAO.cuentaDelegacionesHaciaUsuario(codigoUsuario,params);
    }
    /* ****************************************************+*** */

    public String getMailByUsuario (int codigoUsuario, String[] params) {
        return UsuariosGruposDAO.getInstance().getMailByUsuario(codigoUsuario, params);
    }

    public int getUsuarioDelegado (int codigoUsuario, String[] params) {
        return UsuariosGruposDAO.getInstance().getUsuarioDelegado(codigoUsuario, params);
    }
    
   /**
     * Recupera el número total de usuarios de la BD sin contar el SW.FORMULARIOS. Permite realizar un
     * filtrado por login y nombre del usuario
     * @param login: Login del usuario
     * @param nombre: Nombre del usuario
     * @param params: Parámetros de conexión a la base de datos
     * @return int
     */
    public int countNumUsers(String login,String nombre,String[] params) {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        int salida = 0;
        try{
            
            ResourceBundle config = ResourceBundle.getBundle("techserver");
            String[] parametros = (String []) params.clone();
            parametros[6] = config.getString("CON.jndi");

            adapt = new AdaptadorSQLBD(parametros);
            con = adapt.getConnection();
            
            salida = UsuariosGruposDAO.getInstance().countNumUsers(login,nombre,con);
            
        }catch(BDException e){
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
        }finally{
            
            try{
                if(con!=null) con.close();
                
            }catch(SQLException e){                
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }            
            return salida;
        }
        
        
    }
    
    
    /*********/
    /**
     * Recupera los usuarios a mostrar en el administrador local
     * @param codOrg: Código de la organización
     * @param login: Login del usuario
     * @param nombre: Nombre del usuario
     * @param params: Parámetros de conexión a la BBDD
     * @return Vector con la lista de usuarios
     */
    public Vector getUsuariosLocalConFiltroBusqueda(String codOrg,String login,String nombre,String[] params){
        log.debug("getUsuarios");
        UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
        Vector resultado = new Vector();
        try{
          log.debug("Usando persistencia manual");
          resultado = ugDAO.getUsuariosLocalFiltroBusqueda(codOrg,login,nombre,params);
          return resultado;
        }catch(Exception ce){
          log.error("JDBC Technical problem " + ce.getMessage());
          return resultado;
        }
  }
    
    
  /**
   * Recupera todos los usuarios de Flexia que existen en la base de datos.
   * Se permite filtrar por login y nombre
   * @param login: Login del usuario
   * @param nombre: Nombre del usuario
   * @param params: Parámetros de conexión a la BBDD
   * @return 
   */
   public Vector getUsuariosFiltroBusqueda(String login,String nombre,String[] params){
        log.debug("getUsuarios");
        UsuariosGruposDAO ugDAO = UsuariosGruposDAO.getInstance();
        Vector resultado = new Vector();
        try{
          log.debug("Usando persistencia manual");
          resultado = ugDAO.getUsuariosFiltroBusqueda(login,nombre,params);
          return resultado;
        }catch(Exception ce){
          log.error("JDBC Technical problem " + ce.getMessage());
          return resultado;
        }
  }
		
}