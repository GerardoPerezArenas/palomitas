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
 * <p>Descripción: TiposViviendaManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class TiposViviendaManager  {
  private static TiposViviendaManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(TiposViviendaManager.class.getName());

  protected TiposViviendaManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static TiposViviendaManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(TiposViviendaManager.class) {
        if (instance == null) {
          instance = new TiposViviendaManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaTiposVivienda(String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaTiposViviendaesValue");
    TiposViviendaDAO tiposViviendaDAO = TiposViviendaDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = tiposViviendaDAO.getListaTiposVivienda(params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaTiposViviendaesValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarTiposVivienda(String codigo, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarTiposVivienda");
    TiposViviendaDAO tiposViviendaDAO = TiposViviendaDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tiposViviendaDAO.eliminarTiposVivienda(codigo,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarTiposVivienda");
    return resultado;
  }

  public Vector modificarTiposVivienda(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarTiposVivienda");
    TiposViviendaDAO tiposViviendaDAO = TiposViviendaDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tiposViviendaDAO.modificarTiposVivienda(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarTiposVivienda");
    return resultado;
  }

  public Vector altaTiposVivienda(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaTiposVivienda");
    TiposViviendaDAO tiposViviendaDAO = TiposViviendaDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tiposViviendaDAO.altaTiposVivienda(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaTiposVivienda");
    return resultado;
  }
}