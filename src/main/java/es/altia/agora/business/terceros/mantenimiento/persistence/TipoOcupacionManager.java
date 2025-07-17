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
 * <p>Descripción: TipoOcupacionManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class TipoOcupacionManager  {
  private static TipoOcupacionManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(TipoOcupacionManager.class.getName());

  protected TipoOcupacionManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static TipoOcupacionManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(TipoOcupacionManager.class) {
        if (instance == null) {
          instance = new TipoOcupacionManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaTipoOcupaciones(String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaTipoOcupacionesValue");
    TipoOcupacionDAO tipoOcupacionDAO = TipoOcupacionDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = tipoOcupacionDAO.getListaTipoOcupaciones(params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaTipoOcupacionesValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarTipoOcupacion(String codigo, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarTipoOcupacion");
    TipoOcupacionDAO tipoOcupacionDAO = TipoOcupacionDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tipoOcupacionDAO.eliminarTipoOcupacion(codigo,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarTipoOcupacion");
    return resultado;
  }

  public Vector modificarTipoOcupacion(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarTipoOcupacion");
    TipoOcupacionDAO tipoOcupacionDAO = TipoOcupacionDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tipoOcupacionDAO.modificarTipoOcupacion(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarTipoOcupacion");
    return resultado;
  }

  public Vector altaTipoOcupacion(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaTipoOcupacion");
    TipoOcupacionDAO tipoOcupacionDAO = TipoOcupacionDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tipoOcupacionDAO.altaTipoOcupacion(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaTipoOcupacion");
    return resultado;
  }
}