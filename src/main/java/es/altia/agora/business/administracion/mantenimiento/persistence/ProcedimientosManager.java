// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.ProcedimientosDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase ProcedimientosManager</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class ProcedimientosManager  {
  private static ProcedimientosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(ProcedimientosManager.class.getName());

  protected ProcedimientosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static ProcedimientosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(ProcedimientosManager.class) {
        if (instance == null) {
          instance = new ProcedimientosManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaProcedimientos(String[] params){
    log.debug("getListaProcedimientosValue");
    ProcedimientosDAO procedimientoDAO = ProcedimientosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = procedimientoDAO.getListaProcedimientos(params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarProcedimiento(String codigo,String codCampoAntiguo,String codIdiomaAntiguo, String[] params) {
    log.debug("eliminarProcedimiento");
    ProcedimientosDAO procedimientoDAO = ProcedimientosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = procedimientoDAO.eliminarProcedimiento(codigo,codCampoAntiguo,codIdiomaAntiguo,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector modificarProcedimiento(GeneralValueObject gVO, String[] params){
    log.debug("modificarProcedimiento");
    ProcedimientosDAO procedimientoDAO = ProcedimientosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = procedimientoDAO.modificarProcedimiento(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector altaProcedimiento(GeneralValueObject gVO, String[] params) {
    log.debug("altaProcedimiento");
    ProcedimientosDAO procedimientoDAO = ProcedimientosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = procedimientoDAO.altaProcedimiento(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }
}