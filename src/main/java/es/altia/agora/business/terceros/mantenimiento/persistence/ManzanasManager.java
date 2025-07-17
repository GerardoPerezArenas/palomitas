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
 * <p>Descripción: ManzanasManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class ManzanasManager  {
  private static ManzanasManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(ManzanasManager.class.getName());

  protected ManzanasManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static ManzanasManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(ManzanasManager.class) {
        if (instance == null) {
          instance = new ManzanasManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaManzanas(GeneralValueObject gVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaManzanasValue");
    ManzanasDAO manzanasDAO = ManzanasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = manzanasDAO.getListaManzanas(gVO,params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaManzanasValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarManzana(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarManzana");
    ManzanasDAO manzanasDAO = ManzanasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = manzanasDAO.eliminarManzana(gVO,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarManzana");
    return resultado;
  }

  public Vector modificarManzana(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarManzana");
    ManzanasDAO manzanasDAO = ManzanasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = manzanasDAO.modificarManzana(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarManzana");
    return resultado;
  }

  public Vector altaManzana(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaManzana");
    ManzanasDAO manzanasDAO = ManzanasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = manzanasDAO.altaManzana(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaManzana");
    return resultado;
  }
}