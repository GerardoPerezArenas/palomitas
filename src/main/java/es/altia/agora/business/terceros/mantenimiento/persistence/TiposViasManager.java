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
 * <p>Descripción: TiposViasManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class TiposViasManager  {
  private static TiposViasManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(TiposViasManager.class.getName());

  protected TiposViasManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static TiposViasManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(TiposViasManager.class) {
        if (instance == null) {
          instance = new TiposViasManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaTiposVias(String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaTiposViasValue");
    TiposViasDAO tiposViasDAO = TiposViasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = tiposViasDAO.getListaTiposVias(params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaTiposViasValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarTipoVia(String codigo, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarTipoVia");
    TiposViasDAO tiposViasDAO = TiposViasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tiposViasDAO.eliminarTipoVia(codigo,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarTipoVia");
    return resultado;
  }

  public Vector modificarTipoVia(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarTipoVia");
    TiposViasDAO tiposViasDAO = TiposViasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tiposViasDAO.modificarTipoVia(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarTipoVia");
    return resultado;
  }

  public Vector altaTipoVia(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaTipoVia");
    TiposViasDAO tiposViasDAO = TiposViasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tiposViasDAO.altaTipoVia(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaTipoVia");
    return resultado;
  }
}