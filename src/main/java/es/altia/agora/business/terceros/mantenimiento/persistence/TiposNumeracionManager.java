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
 * <p>Descripción: TiposNumeracionManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class TiposNumeracionManager  {
  private static TiposNumeracionManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(TiposNumeracionManager.class.getName());

  protected TiposNumeracionManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static TiposNumeracionManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(TiposNumeracionManager.class) {
        if (instance == null) {
          instance = new TiposNumeracionManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaTiposNumeraciones(String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaTiposNumeracionesValue");
    TiposNumeracionDAO tiposNumeracionDAO = TiposNumeracionDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = tiposNumeracionDAO.getListaTiposNumeraciones(params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaTiposNumeracionesValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarTipoNumeracion(String codigo, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarTipoNumeracion");
    TiposNumeracionDAO tiposNumeracionDAO = TiposNumeracionDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tiposNumeracionDAO.eliminarTipoNumeracion(codigo,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarTipoNumeracion");
    return resultado;
  }

  public Vector modificarTipoNumeracion(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarTipoNumeracion");
    TiposNumeracionDAO tiposNumeracionDAO = TiposNumeracionDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tiposNumeracionDAO.modificarTipoNumeracion(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarTipoNumeracion");
    return resultado;
  }

  public Vector altaTipoNumeracion(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaTipoNumeracion");
    TiposNumeracionDAO tiposNumeracionDAO = TiposNumeracionDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tiposNumeracionDAO.altaTipoNumeracion(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaTipoNumeracion");
    return resultado;
  }
}