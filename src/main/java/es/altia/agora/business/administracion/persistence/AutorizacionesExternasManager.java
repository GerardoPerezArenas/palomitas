// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;

import es.altia.agora.business.administracion.AutorizacionesExternasValueObject;
import es.altia.agora.business.administracion.persistence.manual.AutorizacionesExternasDAO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DepartamentosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class AutorizacionesExternasManager  {
  private static AutorizacionesExternasManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(AutorizacionesExternasManager.class.getName());

  protected AutorizacionesExternasManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static AutorizacionesExternasManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(AutorizacionesExternasManager.class) {
        if (instance == null) {
          instance = new AutorizacionesExternasManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaEntidadesAplicaciones(AutorizacionesExternasValueObject aeVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaEntidadesAplicaciones");
    AutorizacionesExternasDAO aeDAO = AutorizacionesExternasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = aeDAO.getListaEntidadesAplicaciones(aeVO,params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  public Vector getListaAplicacionesUsuarios(AutorizacionesExternasValueObject aeVO,String[] params){
    log.debug("getListaAplicacionesUsuarios");
    AutorizacionesExternasDAO aeDAO = AutorizacionesExternasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = aeDAO.getListaAplicacionesUsuarios(aeVO,params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  public Vector getListaEntidadesUsuarios(AutorizacionesExternasValueObject aeVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaEntidadesUsuarios");
    AutorizacionesExternasDAO aeDAO = AutorizacionesExternasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = aeDAO.getListaEntidadesUsuarios(aeVO,params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  public int grabarListas(AutorizacionesExternasValueObject aeVO,String gPP,String gSP,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaEntidadesUsuarios");
    AutorizacionesExternasDAO aeDAO = AutorizacionesExternasDAO.getInstance();
    int resultado = 0;
    try{
      log.debug("Usando persistencia manual");
      resultado = aeDAO.grabarListas(aeVO,gPP,gSP,params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  public int grabarListasLocal(AutorizacionesExternasValueObject aeVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaEntidadesUsuarios");
    AutorizacionesExternasDAO aeDAO = AutorizacionesExternasDAO.getInstance();
    int resultado = 0;
    try{
      log.debug("Usando persistencia manual");
      resultado = aeDAO.grabarListasLocal(aeVO,params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  public int grabarNombreBD(String codAplicacion,String codEntidad,String codOrganizacion,String baseDeDatos,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("grabarNombreBD");
    AutorizacionesExternasDAO aeDAO = AutorizacionesExternasDAO.getInstance();
    int resultado = 0;
    try{
      log.debug("Usando persistencia manual");
      resultado = aeDAO.grabarNombreBD(codAplicacion,codEntidad,codOrganizacion,baseDeDatos,params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
    
}