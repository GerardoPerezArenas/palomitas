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
 * <p>Descripción: DomiciliosManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class DomiciliosManager  {
  private static DomiciliosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(DomiciliosManager.class.getName());

  protected DomiciliosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static DomiciliosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(DomiciliosManager.class) {
        if (instance == null) {
          instance = new DomiciliosManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaDomicilios(GeneralValueObject gVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaDomiciliosValue");
    DomiciliosDAO domiciliosDAO = DomiciliosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = domiciliosDAO.getListaDomicilios(gVO,params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaDomiciliosValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  public GeneralValueObject eliminarDomicilio(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarDomicilio");
    DomiciliosDAO domiciliosDAO = DomiciliosDAO.getInstance();
    GeneralValueObject resultado = new GeneralValueObject();
    try{
      resultado = domiciliosDAO.eliminarDomicilio(gVO,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarDomicilio");
    return resultado;
  }

  public GeneralValueObject modificarDomicilio(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarDomicilio");
    DomiciliosDAO domiciliosDAO = DomiciliosDAO.getInstance();
    GeneralValueObject resultado = new GeneralValueObject();
    try{
      resultado = domiciliosDAO.modificarDomicilio(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarDomicilio");
    return resultado;
  }

  public GeneralValueObject altaDomicilio(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaDomicilio");
    DomiciliosDAO domiciliosDAO = DomiciliosDAO.getInstance();
    GeneralValueObject resultado = new GeneralValueObject();
    try{
      resultado = domiciliosDAO.altaDomicilio(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaDomicilio");
    return resultado;
  }
}