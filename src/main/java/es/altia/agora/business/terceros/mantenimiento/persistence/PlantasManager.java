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
 * <p>T�tulo: Proyecto @gora</p>
 * <p>Descripci�n: PlantasManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */

public class PlantasManager  {
  private static PlantasManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion t�cnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(PlantasManager.class.getName());

  protected PlantasManager() {
    // Queremos usar el fichero de configuraci�n technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static PlantasManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizaci�n aqu� para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(PlantasManager.class) {
        if (instance == null) {
          instance = new PlantasManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaPlantas(String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaPlantasValue");
    PlantasDAO plantasDAO = PlantasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = plantasDAO.getListaPlantas(params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaPlantasValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarPlanta(String codigo, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarPlanta");
    PlantasDAO plantasDAO = PlantasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = plantasDAO.eliminarPlanta(codigo,params);
    }catch (Exception e){
      log.error("Excepci�n capturada: " + e.toString());
    }
    log.debug("eliminarPlanta");
    return resultado;
  }

  public Vector modificarPlanta(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarPlanta");
    PlantasDAO plantasDAO = PlantasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = plantasDAO.modificarPlanta(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarPlanta");
    return resultado;
  }

  public Vector altaPlanta(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaPlanta");
    PlantasDAO plantasDAO = PlantasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = plantasDAO.altaPlanta(gVO,params);
    }catch (Exception e) {
      log.error("Excepci�n capturada: " + e.toString());
    }
    log.debug("altaPlanta");
    return resultado;
  }
}