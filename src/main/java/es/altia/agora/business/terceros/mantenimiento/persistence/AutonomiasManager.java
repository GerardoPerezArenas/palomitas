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
 * <p>Descripción: AutonomiasManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class AutonomiasManager  {
  private static AutonomiasManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(AutonomiasManager.class.getName());


  protected AutonomiasManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static AutonomiasManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(AutonomiasManager.class) {
        if (instance == null) {
          instance = new AutonomiasManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaAutonomias(String codPais,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("getListaAutonomiasValue");
    AutonomiasDAO autonomiasDAO = AutonomiasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      m_Log.debug("Usando persistencia manual");
      resultado = autonomiasDAO.getListaAutonomias(codPais,params);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("getListaAutonomiasValue");
      return resultado;
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarAutonomia(String cod,String codAuton, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("eliminarAutonomia");
    AutonomiasDAO autonomiasDAO = AutonomiasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = autonomiasDAO.eliminarAutonomia(cod,codAuton,params);
    }catch (Exception e){
      m_Log.error("Excepción capturada: " + e.toString());
    }
    m_Log.debug("eliminarAutonomia");
    return resultado;
  }

  public Vector modificarAutonomia(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("modificarAutonomia");
    AutonomiasDAO autonomiasDAO = AutonomiasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = autonomiasDAO.modificarAutonomia(gVO, params);
    }catch(Exception e){
      m_Log.error("Excepcion capturada: " + e.toString());
    }
    m_Log.debug("modificarAutonomia");
    return resultado;
  }

  public Vector altaAutonomia(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("altaAutonomia");
    AutonomiasDAO autonomiasDAO = AutonomiasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = autonomiasDAO.altaAutonomia(gVO,params);
    }catch (Exception e) {
      m_Log.error("Excepción capturada: " + e.toString());
    }
    m_Log.debug("altaAutonomia");
    return resultado;
  }
}