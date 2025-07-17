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
 * <p>Descripción: PaisesManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class PaisesManager  {
  private static PaisesManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(PaisesManager.class.getName());   

  protected PaisesManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static PaisesManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(PaisesManager.class) {
        if (instance == null) {
          instance = new PaisesManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaPaises(String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaPaisesValue");
    PaisesDAO paisesDAO = PaisesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = paisesDAO.getListaPaises(params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaPaisesValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarPais(String cod, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarPais");
    PaisesDAO paisesDAO = PaisesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = paisesDAO.eliminarPais(cod,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarPais");
    return resultado;
  }

  public Vector modificarPais(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarPais");
    PaisesDAO paisesDAO = PaisesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = paisesDAO.modificarPais(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarPais");
    return resultado;
  }

  public Vector altaPais(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaPais");
    PaisesDAO paisesDAO = PaisesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = paisesDAO.altaPais(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaPais");
    return resultado;
  }
}