package es.altia.agora.business.registro.persistence;

import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.exception.*;
import es.altia.agora.business.registro.BuzonWCValueObject;
import es.altia.agora.business.registro.persistence.manual.BuzonWCDAO;
import java.util.Vector;

public class BuzonWCManager{
  /**
    * Código que sigue el patrón de diseño <code>Singleton</code>
    * Los métodos de negocio gestionan que la persistencia sea manual o automática
    * Es protected, por lo que la única manera de instanciar esta clase es usando el factory method <code>getInstance</code>
    */


  protected BuzonWCManager() {
    // Fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Mensajes de error localizados
    // m_ConfigError = ConfigServiceHelper.getConfig("error");
  }


  public Vector getRegistrosWC(BuzonWCValueObject buzonVO, String[] params) {

    Vector v = null;

    m_Log.debug("getRegistrosWC");
    try {
      m_Log.debug("Usando persistencia manual");
      v = BuzonWCDAO.getInstance().getRegistrosWC(buzonVO,params);
      m_Log.debug("Lista de registros del web del ciudadano");
    } catch (TechnicalException tecE) {
      m_Log.error("JDBC Technical problem " + tecE.getMessage());
      v = new Vector();

    } finally {
      m_Log.debug("getRegistrosWC");
      return v;
    }
  }
  
  public Vector getRegistrosWCHistoricos(BuzonWCValueObject buzonVO, String[] params) {

    Vector v = null;

    m_Log.debug("getRegistrosWCHistoricos");
    try {
      m_Log.debug("Usando persistencia manual");
      v = BuzonWCDAO.getInstance().getRegistrosWCHistoricos(buzonVO,params);
      m_Log.debug("Lista de registros del web del ciudadano");
    } catch (TechnicalException tecE) {
      m_Log.error("JDBC Technical problem " + tecE.getMessage());
      v = new Vector();

    } finally {
      m_Log.debug("getRegistrosWCHistoricos");
      return v;
    }
  }
  
  public int aceptarRegistro(BuzonWCValueObject buzonVO, String[] params) {

    int resultado = 0;

    m_Log.debug("aceptarRegistro");
    try {
      m_Log.debug("Usando persistencia manual");
      resultado = BuzonWCDAO.getInstance().aceptarRegistro(buzonVO,params);
      m_Log.debug("Lista de registros del web del ciudadano");
    } catch (TechnicalException tecE) {
      m_Log.error("JDBC Technical problem " + tecE.getMessage());
      resultado = 0;

    } finally {
      m_Log.debug("aceptarRegistro");
      return resultado;
    }
  }
  
  public int rechazarRegistro(BuzonWCValueObject buzonVO, String[] params) {

    int resultado = 0;

    m_Log.debug("rechazarRegistro");
    try {
      m_Log.debug("Usando persistencia manual");
      resultado = BuzonWCDAO.getInstance().rechazarRegistro(buzonVO,params);
      m_Log.debug("Lista de registros del web del ciudadano");
    } catch (TechnicalException tecE) {
      m_Log.error("JDBC Technical problem " + tecE.getMessage());
      resultado = 0;

    } finally {
      m_Log.debug("rechazarRegistro");
      return resultado;
    }
  }
  
  public String getSolicitud(String numero,String ejercicio, String[] params) {

    String resultado = "";

    m_Log.debug("getSolicitud");
    try {
      m_Log.debug("Usando persistencia manual");
      resultado = BuzonWCDAO.getInstance().getSolicitud(numero,ejercicio,params);
      m_Log.debug("Lista de registros del web del ciudadano");
    } catch (TechnicalException tecE) {
      m_Log.error("JDBC Technical problem " + tecE.getMessage());
      resultado = null;

    } finally {
      m_Log.debug("getSolicitud");
      return resultado;
    }
  }

  
  /**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de SelectManager
  */
  public static BuzonWCManager getInstance() {
    if (instance == null) {
    // Sincronización para serializar (no multithread) las invocaciones a este metodo
    synchronized(BuzonWCManager.class)
    {
      if (instance == null) {
        instance = new BuzonWCManager();
      }
    }
    }
    return instance;
  }

  private static BuzonWCManager instance = null; // Mi propia instancia

  /* Declaracion de servicios */

  protected static Config m_ConfigTechnical; // Para el fichero de configuracion technical
  protected static Config m_ConfigError; // Para el fichero de mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(BuzonWCManager.class.getName());
}
