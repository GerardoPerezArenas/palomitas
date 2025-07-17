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
 * <p>Descripción: OrganismosExternosManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */

public class OrganismosExternosManager  {
  private static OrganismosExternosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(OrganismosExternosManager.class.getName());

  protected OrganismosExternosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static OrganismosExternosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(OrganismosExternosManager.class) {
        if (instance == null) {
          instance = new OrganismosExternosManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaOrganismosExternos(String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaOrganismosExternosValue");
    OrganismosExternosDAO organismosExternosDAO = OrganismosExternosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = organismosExternosDAO.getListaOrganismosExternos(params);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaOrganismosExternosValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarOrganismoExterno(String codigo, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarOrganismoExterno");
    OrganismosExternosDAO organismosExternosDAO = OrganismosExternosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = organismosExternosDAO.eliminarOrganismoExterno(codigo,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarOrganismoExterno");
    return resultado;
  }

  public Vector modificarOrganismoExterno(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarOrganismoExterno");
    OrganismosExternosDAO organismosExternosDAO = OrganismosExternosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = organismosExternosDAO.modificarOrganismoExterno(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarOrganismoExterno");
    return resultado;
  }

  public Vector altaOrganismoExterno(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaOrganismoExterno");
    OrganismosExternosDAO organismosExternosDAO = OrganismosExternosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = organismosExternosDAO.altaOrganismoExterno(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaOrganismoExterno");
    return resultado;
  }
}