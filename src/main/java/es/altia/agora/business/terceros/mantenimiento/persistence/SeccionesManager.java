// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.*;
import es.altia.agora.business.util.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Vector;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: SeccionesManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class SeccionesManager  {
  private static SeccionesManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(SeccionesManager.class.getName());

  protected SeccionesManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static SeccionesManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(SeccionesManager.class) {
        if (instance == null) {
          instance = new SeccionesManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaSecciones(GeneralValueObject gVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaSeccionesValue");
    SeccionesDAO seccionesDAO = SeccionesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = seccionesDAO.getListaSecciones(gVO,params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaSeccionesValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarSeccion(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarSeccion");
    SeccionesDAO seccionesDAO = SeccionesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = seccionesDAO.eliminarSeccion(gVO,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarSeccion");
    return resultado;
  }

  public Vector modificarSeccion(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarSeccion");
    SeccionesDAO seccionesDAO = SeccionesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = seccionesDAO.modificarSeccion(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarSeccion");
    return resultado;
  }

  public Vector altaSeccion(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaSeccion");
    SeccionesDAO seccionesDAO = SeccionesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = seccionesDAO.altaSeccion(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaSeccion");
    return resultado;
  }
}