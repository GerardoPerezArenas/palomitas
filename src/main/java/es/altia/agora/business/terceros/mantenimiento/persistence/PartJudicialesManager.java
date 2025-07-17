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

public class PartJudicialesManager  {
  private static PartJudicialesManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(PartJudicialesManager.class.getName());

  protected PartJudicialesManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static PartJudicialesManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(PartJudicialesManager.class) {
        if (instance == null) {
          instance = new PartJudicialesManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaPartJudiciales(GeneralValueObject gVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaPartJudiciales");
    PartJudicialesDAO partJudicialesDAO = PartJudicialesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = partJudicialesDAO.getListaPartJudiciales(gVO,params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaPartJudiciales");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarPartJudiciales(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarPartJudiciales");
    PartJudicialesDAO partJudicialesDAO = PartJudicialesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = partJudicialesDAO.eliminarPartJudiciales(gVO,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarPartJudiciales");
    return resultado;
  }

  public Vector modificarPartJudiciales(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarPartJudiciales");
    PartJudicialesDAO partJudicialesDAO = PartJudicialesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = partJudicialesDAO.modificarPartJudiciales(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarPartJudiciales");
    return resultado;
  }

  public Vector altaPartJudiciales(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaPartJudiciales");
    PartJudicialesDAO partJudicialesDAO = PartJudicialesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = partJudicialesDAO.altaPartJudiciales(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaPartJudiciales");
    return resultado;
  }
}