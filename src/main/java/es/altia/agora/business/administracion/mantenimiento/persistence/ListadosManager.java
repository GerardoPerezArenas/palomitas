// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.mantenimiento.CamposListadosParametrizablesVO;
import java.util.Vector;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.ListadosDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Listados</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Chus Lopez Lopez
 * @version 1.0
 */

public class ListadosManager  {
  private static ListadosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(ListadosManager.class.getName());

  protected ListadosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static ListadosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(ListadosManager.class) {
        if (instance == null) {
          instance = new ListadosManager();
        }
      }
    }
    return instance;
  }

   public Vector getListadosParametrizables(String[] params){
    log.debug("getListaCssValue");
    ListadosDAO listadosDAO = ListadosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = listadosDAO.getListadosParametrizables(params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
   
  public Vector getListaCamposListados(CamposListadosParametrizablesVO gVO, String[] params){
    log.debug("modificarCssOrganizacion");
    ListadosDAO listadosDAO = ListadosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = listadosDAO.getListaCamposListados(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }
    public int grabarListaCamposListados(CamposListadosParametrizablesVO gVO, String[] params){
    log.debug("modificarCssOrganizacion");
    ListadosDAO listadosDAO = ListadosDAO.getInstance();
    int resultado = 0;
    try{
      resultado = listadosDAO.grabarListaCamposListados(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }
   
   
   

  
 
 
 

 
}