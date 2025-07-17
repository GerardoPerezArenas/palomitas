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
 * <p>Descripción: TramerosManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class TramerosManager  {
  private static TramerosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(TramerosManager.class.getName());

  protected TramerosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static TramerosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(TramerosManager.class) {
        if (instance == null) {
          instance = new TramerosManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaTrameros(GeneralValueObject gVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaTramerosValue");
    TramerosDAO tramerosDAO = TramerosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = tramerosDAO.getListaTrameros(gVO,params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaTramerosValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public GeneralValueObject buscarTramosProceso(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("buscarTramosProceso");
    TramerosDAO tramerosDAO = TramerosDAO.getInstance();
    GeneralValueObject resultado = new GeneralValueObject();
    try{
      log.debug("Usando persistencia manual");
      resultado = tramerosDAO.buscarTramosProceso(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("buscarTramosProceso");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }


  public Vector eliminarTramero(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarTramero");
    TramerosDAO tramerosDAO = TramerosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tramerosDAO.eliminarTramero(gVO,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarTramero");
    return resultado;
  }

  public Vector modificarTramero(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarTramero");
    TramerosDAO tramerosDAO = TramerosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tramerosDAO.modificarTramero(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarTramero");
    return resultado;
  }

  public Vector altaTramero(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaTramero");
    TramerosDAO tramerosDAO = TramerosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tramerosDAO.altaTramero(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaTramero");
    return resultado;
  }
}