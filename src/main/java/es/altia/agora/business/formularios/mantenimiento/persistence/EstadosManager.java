// NOMBRE DEL PAQUETE
package es.altia.agora.business.formularios.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.formularios.mantenimiento.persistence.manual.EstadosDAO;
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

public class EstadosManager  {
  private static EstadosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(EstadosManager.class.getName());

  protected EstadosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static EstadosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(EstadosManager.class) {
        if (instance == null) {
          instance = new EstadosManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaEstados(String[] params){
    EstadosDAO estadoDAO = EstadosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = estadoDAO.getListaEstados(params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarEstado(String codigo, String[] params) {
    EstadosDAO estadoDAO = EstadosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = estadoDAO.eliminarEstado(codigo,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector modificarEstado(GeneralValueObject gVO, String[] params){
    EstadosDAO estadoDAO = EstadosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = estadoDAO.modificarEstado(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector altaEstado(GeneralValueObject gVO, String[] params) {
    EstadosDAO estadoDAO = EstadosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = estadoDAO.altaEstado(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }
}