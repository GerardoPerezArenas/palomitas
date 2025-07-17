// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.AreasDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase AreasManager</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class AreasManager  {
  private static AreasManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(AreasManager.class.getName());

  protected AreasManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static AreasManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(AreasManager.class) {
        if (instance == null) {
          instance = new AreasManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaAreas(String[] params){
    AreasDAO areasDAO = AreasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = areasDAO.getListaAreas(params);
      // queremos estar informados de cuando este metodo finaliza
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  
  public Map<Integer, Integer> checkErrorAreaProcedimiento (String codigoArea, String descripcionArea,String[] params){
    AreasDAO areasDAO = AreasDAO.getInstance();
    Map<Integer, Integer> error = new HashMap<Integer, Integer>();
    try{
      log.debug("Usando persistencia manual");
      error = areasDAO.checkErrorAreaProcedimiento(codigoArea, descripcionArea, params);
      // queremos estar informados de cuando este metodo finaliza
      return error;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return error;
    }
  }

  public Vector eliminarArea(String codigo,String codCampoAntiguo,String codIdiomaAntiguo, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.info("eliminarArea");
    AreasDAO areaDAO = AreasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = areaDAO.eliminarArea(codigo,codCampoAntiguo,codIdiomaAntiguo,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector modificarArea(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.info("modificarArea");
    AreasDAO areaDAO = AreasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = areaDAO.modificarArea(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector altaArea(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.info("altaArea");
    AreasDAO areaDAO = AreasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = areaDAO.altaArea(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }
}