// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.AplicacionesDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: AplicacionesManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class AplicacionesManager  {
  private static AplicacionesManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(AplicacionesManager.class.getName());

  protected AplicacionesManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static AplicacionesManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(AplicacionesManager.class) {
        if (instance == null) {
          instance = new AplicacionesManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaAplicaciones(String[] params){

    AplicacionesDAO aplicacionDAO = AplicacionesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = aplicacionDAO.getListaAplicaciones(params);
      // queremos estar informados de cuando este metodo finaliza
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector getListaAplicaciones(GeneralValueObject g,String[] params){
    AplicacionesDAO aplicacionDAO = AplicacionesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = aplicacionDAO.getListaAplicaciones(g,params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarAplicacion(String codigo, String[] params) {
    AplicacionesDAO aplicacionDAO = AplicacionesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = aplicacionDAO.eliminarAplicacion(codigo,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector modificarAplicacion(GeneralValueObject gVO, String[] params){
    AplicacionesDAO aplicacionDAO = AplicacionesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = aplicacionDAO.modificarAplicacion(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector altaAplicacion(GeneralValueObject gVO, String[] params) {
    AplicacionesDAO aplicacionDAO = AplicacionesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = aplicacionDAO.altaAplicacion(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }
}