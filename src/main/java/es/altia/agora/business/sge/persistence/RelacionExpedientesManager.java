package es.altia.agora.business.sge.persistence;

import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.exception.*;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.RelacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.manual.RelacionExpedientesDAO;
import es.altia.agora.business.sge.exception.TramitacionException;
import java.util.Vector;

public class RelacionExpedientesManager {
  /**
    * Código que sigue el patrón de diseño <code>Singleton</code>
    * Los métodos de negocio gestionan que la persistencia sea manual o automática
    * Es protected, por lo que la única manera de instanciar esta clase es usando el factory method <code>getInstance</code>
    */


  protected RelacionExpedientesManager() {
    // Fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Mensajes de error localizados
    // m_ConfigError = ConfigServiceHelper.getConfig("error");
    // Servicio de log

  }
  
  public Vector getExpedientesPendientes(UsuarioValueObject uVO, String codProcedimiento, String codTramite, String codUtr, String[] params)
  throws TramitacionException {

    Vector v = null;

    m_Log.debug("getExpedientesPendientes");

    try {
      m_Log.debug("Usando persistencia manual");
      v = RelacionExpedientesDAO.getInstance().getExpedientesPendientes(uVO,codProcedimiento,codTramite,codUtr, params);
      m_Log.debug("getExpedientesPendientes");

    } catch (TechnicalException tecE) {
      m_Log.error("JDBC Technical problem " + tecE.getMessage());
      v = new Vector();
      throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

    } catch (TramitacionException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
      v = new Vector();
      throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());

    } finally {
      m_Log.debug("getUnidadesTramitadorasUsuario");
      return v;
    }
  }

    public Vector getRelacionesPendientes(UsuarioValueObject uVO, String codProcedimiento, String codTramite, String[] params)
    throws TramitacionException {

      Vector v = null;

      m_Log.debug("getExpedientesPendientes");

      try {
        m_Log.debug("Usando persistencia manual");
        v = RelacionExpedientesDAO.getInstance().getRelacionesPendientes(uVO,codProcedimiento,codTramite,params);
        m_Log.debug("getExpedientesPendientes");

      } catch (TechnicalException tecE) {
        m_Log.error("JDBC Technical problem " + tecE.getMessage());
        v = new Vector();
        throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

      } catch (TramitacionException te) {
        m_Log.error("JDBC Technical problem " + te.getMessage());
        v = new Vector();
        throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());

      } finally {
        m_Log.debug("getUnidadesTramitadorasUsuario");
        return v;
      }
    }


  /**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de SelectManager
  */
  public static RelacionExpedientesManager getInstance() {
    if (instance == null) {
    // Sincronización para serializar (no multithread) las invocaciones a este metodo
    synchronized(RelacionExpedientesManager.class)
    {
      if (instance == null) {
        instance = new RelacionExpedientesManager();
      }
    }
    }
    return instance;
  }

  private static RelacionExpedientesManager instance = null; // Mi propia instancia

  /* Declaracion de servicios */

  protected static Config m_ConfigTechnical; // Para el fichero de configuracion technical
  protected static Config m_ConfigError; // Para el fichero de mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(RelacionExpedientesManager.class.getName());



  

    public Vector getRelacionExpedientesPendientes(UsuarioValueObject uVO, RelacionExpedientesValueObject reVO, String[] params)
    throws TramitacionException {

      Vector v = null;

      m_Log.debug("getRelacionExpedientesPendientes");

      try {
        m_Log.debug("Usando persistencia manual");
        v = RelacionExpedientesDAO.getInstance().getRelacionExpedientesPendientes(uVO,reVO,params);
        m_Log.debug("getRelacionExpedientesPendientes");
        m_Log.debug("getRelacionExpedientesPendientes");

      } catch (TechnicalException tecE) {
        m_Log.error("JDBC Technical problem " + tecE.getMessage());
        v = new Vector();
        throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

      } catch (TramitacionException te) {
        m_Log.error("JDBC Technical problem " + te.getMessage());
        v = new Vector();
        throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());

      } finally {
        m_Log.debug("getRelacionExpedientesPendientes");
        return v;
      }
    }
    
    
     public String getLimiteExpedientesMostrar(){	
        return RelacionExpedientesDAO.getInstance().getLimiteExpedientesMostrar();
    }
}
