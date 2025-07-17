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
 * <p>Descripción: EcosManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @version 1.0
 */

public class EcosManager  {
  private static EcosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(EcosManager.class.getName());

  protected EcosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static EcosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(EcosManager.class) {
        if (instance == null) {
          instance = new EcosManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaEcos(GeneralValueObject gVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaEcosValue");
    EcosDAO distritosDAO = EcosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = distritosDAO.getListaEcos(gVO,params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaEcosValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector getListaEcosTodas(GeneralValueObject gVO,String[] params){
	// queremos estar informados de cuando este metodo es ejecutado
	log.debug("getListaEcosValue");
	EcosDAO distritosDAO = EcosDAO.getInstance();
	Vector resultado = new Vector();
	try{
	  log.debug("Usando persistencia manual");
	  resultado = distritosDAO.getListaEcosTodas(gVO,params);
	  // queremos estar informados de cuando este metodo finaliza
	  log.debug("getListaEcosValue");
	  return resultado;
	}catch(Exception ce){
	  log.error("JDBC Technical problem " + ce.getMessage());
	  return resultado;
	}
  }

  public Vector eliminarEco(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarEco");
    EcosDAO distritosDAO = EcosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = distritosDAO.eliminarEco(gVO,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarEco");
    return resultado;
  }

  public Vector modificarEco(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarEco");
    EcosDAO distritosDAO = EcosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = distritosDAO.modificarEco(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarEco");
    return resultado;
  }

  public String modificarEcoTerritorio(String[] params,GeneralValueObject gVO){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarEcoTerritorio");
    EcosDAO ecosDAO = EcosDAO.getInstance();
    String resultado = "SI";
    try{
      resultado = ecosDAO.modificarEcoTerritorio(params,gVO);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarEcoTerritorio");
    return resultado;
  }


  public Vector altaEco(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaEco");
    EcosDAO distritosDAO = EcosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = distritosDAO.altaEco(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaEco");
    return resultado;
  }
}