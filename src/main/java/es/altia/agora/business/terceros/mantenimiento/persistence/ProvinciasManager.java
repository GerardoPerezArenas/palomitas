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
 * <p>Descripción: ProvinciasManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class ProvinciasManager  {
  private static ProvinciasManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(ProvinciasManager.class.getName());

  protected ProvinciasManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static ProvinciasManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(ProvinciasManager.class) {
        if (instance == null) {
          instance = new ProvinciasManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaProvincias(GeneralValueObject gVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaProvinciasValue");
    ProvinciasDAO provinciasDAO = ProvinciasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = provinciasDAO.getListaProvincias(gVO,params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaProvinciasValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarProvincia(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarProvincia");
    ProvinciasDAO provinciasDAO = ProvinciasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = provinciasDAO.eliminarProvincia(gVO,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarProvincia");
    return resultado;
  }

  public Vector modificarProvincia(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarProvincia");
    ProvinciasDAO provinciasDAO = ProvinciasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = provinciasDAO.modificarProvincia(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarProvincia");
    return resultado;
  }

  public Vector altaProvincia(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaProvincia");
    ProvinciasDAO provinciasDAO = ProvinciasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = provinciasDAO.altaProvincia(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaProvincia");
    return resultado;
  }
}