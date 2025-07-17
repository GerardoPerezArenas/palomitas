// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.TramitesDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase TramitesManager</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class TramitesManager  {
  private static TramitesManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(TramitesManager.class.getName());

  protected TramitesManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static TramitesManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(TramitesManager.class) {
        if (instance == null) {
          instance = new TramitesManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaTramites(String[] params){
    log.debug("getListaTramitesValue");
    TramitesDAO tramiteDAO = TramitesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = tramiteDAO.getListaTramites(params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarTramite(String codigo,String codCampoAntiguo,String codIdiomaAntiguo, String[] params) {
    log.debug("eliminarTramite");
    TramitesDAO tramiteDAO = TramitesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tramiteDAO.eliminarTramite(codigo,codCampoAntiguo,codIdiomaAntiguo,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector modificarTramite(GeneralValueObject gVO, String[] params){
    log.debug("modificarTramite");
    TramitesDAO tramiteDAO = TramitesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tramiteDAO.modificarTramite(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector altaTramite(GeneralValueObject gVO, String[] params) {
    log.debug("altaTramite");
    TramitesDAO tramiteDAO = TramitesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tramiteDAO.altaTramite(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }
  
  public Vector getListaTramitesByCodigos(String[] params, String codigos){
	  log.debug("getListaTramitesByCodigos");
	  TramitesDAO tramiteDAO = TramitesDAO.getInstance();
	  Vector resultado = new Vector();
	  try{
		log.debug("Usando persistencia manual");
		resultado = tramiteDAO.getListaTramitesByCodigos(params, codigos);
		return resultado;
	  }catch(Exception ce){
		log.error("JDBC Technical problem " + ce.getMessage());
		return resultado;
	  }
	}

}