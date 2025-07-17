// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.EntidadesDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: EntidadesManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class EntidadesManager  {
  private static EntidadesManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(EntidadesManager.class.getName());

  protected EntidadesManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static EntidadesManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(EntidadesManager.class) {
        if (instance == null) {
          instance = new EntidadesManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaEntidades(GeneralValueObject gVO,String[] params){
    EntidadesDAO entidadDAO = EntidadesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = entidadDAO.getListaEntidades(gVO,params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarEntidad(GeneralValueObject gVO, String[] params) {
    EntidadesDAO entidadDAO = EntidadesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = entidadDAO.eliminarEntidad(gVO,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector modificarEntidad(GeneralValueObject gVO, String[] params){
    EntidadesDAO entidadDAO = EntidadesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = entidadDAO.modificarEntidad(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector altaEntidad(GeneralValueObject gVO, String[] params) {
    EntidadesDAO entidadDAO = EntidadesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = entidadDAO.altaEntidad(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }
 
  /**
   * Recupera la lista de entidades de una determinada organización
   * @param idOrganizacion: Id de la organización
   * @param params: Parámetros de conexión a la base de datos
   * @return Colección de GeneralValueTO con los datos de las entidades
   */
  public Vector getListaEntidades(int idOrganizacion,String[] params)
  {    
     EntidadesDAO entidadDAO = EntidadesDAO.getInstance();
     Vector resultado = new Vector();
     try{
        log.debug("Usando persistencia manual");
        resultado = entidadDAO.getListaEntidades(idOrganizacion,params);        
        return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  
  
  
}