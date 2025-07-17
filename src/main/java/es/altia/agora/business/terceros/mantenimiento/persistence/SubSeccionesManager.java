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
 * <p>Descripción: SubSeccionesManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class SubSeccionesManager  {
  private static SubSeccionesManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(SubSeccionesManager.class.getName());

  protected SubSeccionesManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static SubSeccionesManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(SubSeccionesManager.class) {
        if (instance == null) {
          instance = new SubSeccionesManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaSubSecciones(GeneralValueObject gVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaSubSeccionesValue");
    SubSeccionesDAO manzanasDAO = SubSeccionesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = manzanasDAO.getListaSubSecciones(gVO,params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaSubSeccionesValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarSubSeccion(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarSubSeccion");
    SubSeccionesDAO manzanasDAO = SubSeccionesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = manzanasDAO.eliminarSubSeccion(gVO,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarSubSeccion");
    return resultado;
  }

  public Vector modificarSubSeccion(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarSubSeccion");
    SubSeccionesDAO manzanasDAO = SubSeccionesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = manzanasDAO.modificarSubSeccion(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarSubSeccion");
    return resultado;
  }

  public Vector altaSubSeccion(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaSubSeccion");
    SubSeccionesDAO manzanasDAO = SubSeccionesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = manzanasDAO.altaSubSeccion(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaSubSeccion");
    return resultado;
  }
}