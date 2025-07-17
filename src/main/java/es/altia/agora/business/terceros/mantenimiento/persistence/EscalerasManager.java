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
 * <p>T�tulo: Proyecto @gora</p>
 * <p>Descripci�n: EscalerasManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class EscalerasManager  {
  private static EscalerasManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion t�cnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(EscalerasManager.class.getName());

  protected EscalerasManager() {
    // Queremos usar el fichero de configuraci�n technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static EscalerasManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizaci�n aqu� para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(EscalerasManager.class) {
        if (instance == null) {
          instance = new EscalerasManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaEscaleras(String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaEscalerasValue");
    EscalerasDAO escaleraDAO = EscalerasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = escaleraDAO.getListaEscaleras(params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaEscalerasValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarEscalera(String codigo, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarEscalera");
    EscalerasDAO escaleraDAO = EscalerasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = escaleraDAO.eliminarEscalera(codigo,params);
    }catch (Exception e){
      log.error("Excepci�n capturada: " + e.toString());
    }
    log.debug("eliminarEscalera");
    return resultado;
  }

  public Vector modificarEscalera(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarEscalera");
    EscalerasDAO escaleraDAO = EscalerasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = escaleraDAO.modificarEscalera(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarEscalera");
    return resultado;
  }

  public Vector altaEscalera(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaEscalera");
    EscalerasDAO escaleraDAO = EscalerasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = escaleraDAO.altaEscalera(gVO,params);
    }catch (Exception e) {
      log.error("Excepci�n capturada: " + e.toString());
    }
    log.debug("altaEscalera");
    return resultado;
  }
}