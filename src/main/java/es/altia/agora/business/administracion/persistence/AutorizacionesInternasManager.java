// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;

import es.altia.agora.business.administracion.persistence.manual.AutorizacionesInternasDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: AutorizacionesInternasManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: ALTIA CONSULTORES</p>
 */

public class AutorizacionesInternasManager  {
  private static AutorizacionesInternasManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(AutorizacionesInternasManager.class.getName());
          
  protected AutorizacionesInternasManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static AutorizacionesInternasManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(AutorizacionesInternasManager.class) {
        if (instance == null) {
          instance = new AutorizacionesInternasManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaGrupos(GeneralValueObject gVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaGrupos");
    AutorizacionesInternasDAO menuDAO = AutorizacionesInternasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = menuDAO.getListaGrupos(gVO,params);
      return resultado;
    }catch(Exception ce){
      ce.printStackTrace();
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  public Vector getListaUsuarios(GeneralValueObject gVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaUsuarios");
    AutorizacionesInternasDAO menuDAO = AutorizacionesInternasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = menuDAO.getListaUsuarios(gVO,params);
      return resultado;
    }catch(Exception ce){
      ce.printStackTrace();
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }    
  
   public void actualizarPermisosGrupo(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("actualizarPermisosGrupo");
    AutorizacionesInternasDAO aDAO = AutorizacionesInternasDAO.getInstance();
    try{
     aDAO.actualizarPermisosGrupo(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
  }

   public void actualizarPermisosUsuario(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("actualizarPermisosUsuario");
    AutorizacionesInternasDAO aDAO = AutorizacionesInternasDAO.getInstance();
    try{
     aDAO.actualizarPermisosUsuario(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
  }
}