// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.EstilosDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: EstilosManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Chus Lopez Lopez
 * @version 1.0
 */

public class EstilosManager  {
  private static EstilosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(EstilosManager.class.getName());

  protected EstilosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static EstilosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(EstilosManager.class) {
        if (instance == null) {
          instance = new EstilosManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaOrganizaciones(String[] params){
    log.debug("getListaOrganizacionesesValue");
    EstilosDAO estilosDAO = EstilosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = estilosDAO.getListaOrganizaciones(params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
   public Vector getListaCss(String[] params){
    log.debug("getListaCssValue");
    EstilosDAO estilosDAO = EstilosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = estilosDAO.getListaCss(params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
   public Vector modificarCssOrganizacion(GeneralValueObject gVO, String[] params){
    log.debug("modificarCssOrganizacion");
    EstilosDAO estilosDAO = EstilosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = estilosDAO.modificarCssOrganizacion(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }
  public Vector modificarCss(GeneralValueObject gVO, String[] params){
    log.debug("modificarCssOrganizacion");
    EstilosDAO estilosDAO = EstilosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = estilosDAO.modificarCss(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }
    
  public Vector eliminarCss(GeneralValueObject gVO, String[] params){
    log.debug("modificarCssOrganizacion");
    EstilosDAO estilosDAO = EstilosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = estilosDAO.eliminarCss(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }
  
  public Vector buscarCss(GeneralValueObject gVO, String[] params){
    log.debug("modificarCssOrganizacion");
    EstilosDAO estilosDAO = EstilosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = estilosDAO.buscarCss(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }
  
  //guardar archivos css
  
   public Vector insertarCss(GeneralValueObject gVO, String[] params){
    log.debug("modificarCssOrganizacion");
    EstilosDAO estilosDAO = EstilosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = estilosDAO.insertarCss(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }
 
 
 

 
}