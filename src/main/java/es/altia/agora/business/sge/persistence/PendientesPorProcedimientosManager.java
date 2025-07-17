// NOMBRE DEL PAQUETE
package es.altia.agora.business.sge.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.sge.PendientesPorProcedimientosValueObject;
import es.altia.agora.business.sge.persistence.manual.PendientesPorProcedimientosDAO;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DefinicionTramitesManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class PendientesPorProcedimientosManager {

  private static PendientesPorProcedimientosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(PendientesPorProcedimientosManager.class.getName());


  protected PendientesPorProcedimientosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static PendientesPorProcedimientosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(PendientesPorProcedimientosManager.class) {
        if (instance == null) {
          instance = new PendientesPorProcedimientosManager();
        }
      }
    }
    return instance;
  }

  public Vector consultarP(PendientesPorProcedimientosValueObject pendProcVO,String[] params)
       throws AnotacionRegistroException{

      Vector res = null;

    m_Log.debug("consultar");

    try {

        m_Log.debug("Usando persistencia manual");
        res = PendientesPorProcedimientosDAO.getInstance().consultarP(pendProcVO,params);
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

  public Vector consultarF(PendientesPorProcedimientosValueObject pendProcVO,String[] params)
         throws AnotacionRegistroException{

        Vector res = null;

      m_Log.debug("consultar");

      try {

          m_Log.debug("Usando persistencia manual");
          res = PendientesPorProcedimientosDAO.getInstance().consultarF(pendProcVO,params);
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

  public Vector getListaProcedimientos(PendientesPorProcedimientosValueObject pendProcVO,String[] params)
         throws AnotacionRegistroException{

        Vector res = null;

      m_Log.debug("getListaProcedimientos");

      try {

          m_Log.debug("Usando persistencia manual");
          res = PendientesPorProcedimientosDAO.getInstance().getListaProcedimientos(pendProcVO,params);
          m_Log.debug("lista procedimientos obtenida");
          //We want to be informed when this method has finalized
          m_Log.debug("getListaProcedimientos");

      } catch (Exception ce) {
          res = null;
          m_Log.error("JDBC Technical problem " + ce.getMessage());
          throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
      }

      return res;
  }


}