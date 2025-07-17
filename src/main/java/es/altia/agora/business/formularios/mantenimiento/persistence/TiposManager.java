// NOMBRE DEL PAQUETE
package es.altia.agora.business.formularios.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.formularios.mantenimiento.persistence.manual.TiposDAO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: IdiomasManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class TiposManager  {
  private static TiposManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(TiposManager.class.getName());

  protected TiposManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static TiposManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(TiposManager.class) {
        if (instance == null) {
          instance = new TiposManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaTipos(String[] params){
    TiposDAO tipoDAO = TiposDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = tipoDAO.getListaTipos(params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarTipo(String codigo, String[] params) {
    TiposDAO tipoDAO = TiposDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tipoDAO.eliminarTipo(codigo,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector modificarTipo(GeneralValueObject gVO, String[] params){
    TiposDAO tipoDAO = TiposDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tipoDAO.modificarTipo(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector altaTipo(GeneralValueObject gVO, String[] params) {
    TiposDAO tipoDAO = TiposDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = tipoDAO.altaTipo(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }
}