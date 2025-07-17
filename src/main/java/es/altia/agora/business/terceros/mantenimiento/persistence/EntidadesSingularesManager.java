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
 * <p>Descripción: EntidadesSingularesManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */

public class EntidadesSingularesManager  {
  private static EntidadesSingularesManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(EntidadesSingularesManager.class.getName());

  protected EntidadesSingularesManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static EntidadesSingularesManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(EntidadesSingularesManager.class) {
        if (instance == null) {
          instance = new EntidadesSingularesManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaEntidadesSingulares(GeneralValueObject gVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaEntidadesSingularesValue");
    EntidadesSingularesDAO entidadesSingularesDAO = EntidadesSingularesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = entidadesSingularesDAO.getListaEntidadesSingulares(gVO,params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaEntidadesSingularesValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector getListaEntidadesSingularesTodas(GeneralValueObject gVO,String[] params){
	  // queremos estar informados de cuando este metodo es ejecutado
	  log.debug("getListaEntidadesSingularesValue");
	  EntidadesSingularesDAO entidadesSingularesDAO = EntidadesSingularesDAO.getInstance();
	  Vector resultado = new Vector();
	  try{
		log.debug("Usando persistencia manual");
		resultado = entidadesSingularesDAO.getListaEntidadesSingularesTodas(gVO,params);
		// queremos estar informados de cuando este metodo finaliza
		log.debug("getListaEntidadesSingularesValue");
		return resultado;
	  }catch(Exception ce){
		log.error("JDBC Technical problem " + ce.getMessage());
		return resultado;
	  }
	}

  public Vector eliminarEntidadSingular(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarEntidadSingular");
    EntidadesSingularesDAO entidadesSingularesDAO = EntidadesSingularesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = entidadesSingularesDAO.eliminarEntidadSingular(gVO,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarEntidadSingular");
    return resultado;
  }

  public Vector modificarEntidadSingular(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarEntidadSingular");
    EntidadesSingularesDAO entidadesSingularesDAO = EntidadesSingularesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = entidadesSingularesDAO.modificarEntidadSingular(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarEntidadSingular");
    return resultado;
  }

  public Vector altaEntidadSingular(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaEntidadSingular");
    EntidadesSingularesDAO entidadesSingularesDAO = EntidadesSingularesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = entidadesSingularesDAO.altaEntidadSingular(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaEntidadSingular");
    return resultado;
  }

  public String modificarEsiTerritorio(String[] params,GeneralValueObject gVO){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarEsiTerritorio");
    EntidadesSingularesDAO entidadesSingularesDAO = EntidadesSingularesDAO.getInstance();
    String resultado = "SI";
    try{
      resultado = entidadesSingularesDAO.modificarEsiTerritorio(params,gVO);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarEsiTerritorio");
    return resultado;
  }

}