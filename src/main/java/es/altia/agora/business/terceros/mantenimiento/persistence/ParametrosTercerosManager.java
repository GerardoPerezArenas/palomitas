// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.*;
import es.altia.agora.business.terceros.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: ParametrosTercerosManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class ParametrosTercerosManager  {
  private static ParametrosTercerosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(ParametrosTercerosManager.class.getName());

  protected ParametrosTercerosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static ParametrosTercerosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(ParametrosTercerosManager.class) {
        if (instance == null) {
          instance = new ParametrosTercerosManager();
        }
      }
    }
    return instance;
  }

  public ParametrosTerceroValueObject getParametrosTerceros(int orgCod, String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getParametrosTercerosValue");
    ParametrosTercerosDAO parametrosTercerosDAO = ParametrosTercerosDAO.getInstance();
    ParametrosTerceroValueObject resultado = new ParametrosTerceroValueObject();
    try{
      log.debug("Usando persistencia manual");
      resultado = parametrosTercerosDAO.getParametrosTerceros(orgCod, params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getParametrosTercerosValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public void modificarParametrosTerceros(ParametrosTerceroValueObject ptVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarParametrosTerceros");
    ParametrosTercerosDAO parametrosTercerosDAO = ParametrosTercerosDAO.getInstance();
    try{
      parametrosTercerosDAO.modificarParametrosTerceros(ptVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarParametrosTerceros");
  }
}