// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.ProcesosDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: ProcesosManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: ALTIA CONSULTORES</p>
 */

public class ProcesosManager  {
  private static ProcesosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(ProcesosManager.class.getName());

  protected ProcesosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static ProcesosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(ProcesosManager.class) {
        if (instance == null) {
          instance = new ProcesosManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaProcesos(GeneralValueObject gVO,String[] params){
    log.debug("getListaProcesosesValue");
    ProcesosDAO procesoDAO = ProcesosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = procesoDAO.getListaProcesos(gVO,params);
      return resultado;
    }catch(Exception ce){
      ce.printStackTrace();
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarProceso(GeneralValueObject gVO, String[] params) {
    log.debug("eliminarProceso");
    ProcesosDAO procesoDAO = ProcesosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = procesoDAO.eliminarProceso(gVO,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector modificarProceso(GeneralValueObject gVO, String[] params){
    log.debug("modificarProceso");
    ProcesosDAO procesoDAO = ProcesosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = procesoDAO.modificarProceso(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector altaProceso(GeneralValueObject gVO, String[] params) {
    log.debug("altaProceso");
    ProcesosDAO procesoDAO = ProcesosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = procesoDAO.altaProceso(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }
}