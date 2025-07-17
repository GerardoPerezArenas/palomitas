// NOMBRE DEL PAQUETE
package es.altia.agora.business.sge.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.sge.PendientesPorSolicitanteValueObject;
import es.altia.agora.business.sge.persistence.manual.PendientesPorSolicitanteDAO;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DefinicionTramitesManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class PendientesPorSolicitanteManager {

  private static PendientesPorSolicitanteManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(PendientesPorSolicitanteManager.class.getName());


  protected PendientesPorSolicitanteManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static PendientesPorSolicitanteManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(PendientesPorSolicitanteManager.class) {
        if (instance == null) {
          instance = new PendientesPorSolicitanteManager();
        }
      }
    }
    return instance;
  }

  public Vector consultarP(PendientesPorSolicitanteValueObject pendSolVO,String[] params)
       throws AnotacionRegistroException{

      Vector res = null;

    m_Log.debug("consultar");

    try {

        m_Log.debug("Usando persistencia manual");
        res = PendientesPorSolicitanteDAO.getInstance().consultarP(pendSolVO,params);
        m_Log.debug("consulta realizada");
        //We want to be informed when this method has finalized
        m_Log.debug("consultar");

    } catch (Exception ce) {
        res = null;
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
    }

    return res;
  }

  public Vector consultarF(PendientesPorSolicitanteValueObject pendSolVO,String[] params)
       throws AnotacionRegistroException{

      Vector res = null;

    m_Log.debug("consultar");

    try {

        m_Log.debug("Usando persistencia manual");
        res = PendientesPorSolicitanteDAO.getInstance().consultarF(pendSolVO,params);
        m_Log.debug("consulta realizada");
        //We want to be informed when this method has finalized
        m_Log.debug("consultar");

    } catch (Exception ce) {
        res = null;
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
    }

    return res;
  }



}