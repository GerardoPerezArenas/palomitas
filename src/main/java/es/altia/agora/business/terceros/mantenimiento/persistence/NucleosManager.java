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
 * <p>Descripción: NucleosManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class NucleosManager  {
  private static NucleosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(NucleosManager.class.getName());

  protected NucleosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static NucleosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(NucleosManager.class) {
        if (instance == null) {
          instance = new NucleosManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaNucleos(GeneralValueObject gVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaNucleos");
    NucleosDAO nucleosDAO = NucleosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = nucleosDAO.getListaNucleos(gVO,params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaNucleos");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector getListaNucleosTodos(GeneralValueObject gVO,String[] params){
	// queremos estar informados de cuando este metodo es ejecutado
	log.debug("getListaNucleosValue");
	NucleosDAO nucleosDAO = NucleosDAO.getInstance();
	Vector resultado = new Vector();
	try{
	  log.debug("Usando persistencia manual");
	  resultado = nucleosDAO.getListaNucleosTodos(gVO,params);
	  // queremos estar informados de cuando este metodo finaliza
	  log.debug("getListaNucleosValue");
	  return resultado;
	}catch(Exception ce){
	  log.error("JDBC Technical problem " + ce.getMessage());
	  return resultado;
	}
  }

  public Vector eliminarNucleo(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarNucleo");
    NucleosDAO nucleosDAO = NucleosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = nucleosDAO.eliminarNucleo(gVO,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarNucleo");
    return resultado;
  }

  public Vector modificarNucleo(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarNucleo");
    NucleosDAO nucleosDAO = NucleosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = nucleosDAO.modificarNucleo(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarEco");
    return resultado;
  }

  public Vector altaNucleo(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaNucleo");
    NucleosDAO nucleosDAO = NucleosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = nucleosDAO.altaNucleo(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaNucleo");
    return resultado;
  }

  public String modificarNucTerritorio(String[] params,GeneralValueObject gVO){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarNucTerritorio");
    NucleosDAO nucleosDAO = NucleosDAO.getInstance();
    String resultado = "SI";
    try{
      resultado = nucleosDAO.modificarNucTerritorio(params,gVO);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarNucTerritorio");
    return resultado;
  }

}