// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.MenusDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: MenusManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: ALTIA CONSULTORES</p>
 */

public class MenusManager  {
  private static MenusManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(MenusManager.class.getName());

  protected MenusManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static MenusManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(MenusManager.class) {
        if (instance == null) {
          instance = new MenusManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaMenus(GeneralValueObject gVO,String[] params){
    MenusDAO menuDAO = MenusDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = menuDAO.getListaMenus(gVO,params);
      return resultado;
    }catch(Exception ce){
      ce.printStackTrace();
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector altaMenu(GeneralValueObject gVO, String[] params) {
    MenusDAO menuDAO = MenusDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = menuDAO.altaMenu(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }
  
  public Vector modificarMenu(GeneralValueObject gVO, String[] params) {
    log.info("modificarMenu");
    MenusDAO menuDAO = MenusDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = menuDAO.modificarMenu(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }
  
  public Vector eliminarMenu(GeneralValueObject gVO, String[] params) {
    log.info("eliminarMenu");
    MenusDAO menuDAO = MenusDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = menuDAO.eliminarMenu(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }
  
  public Vector altaPuntoMenu(GeneralValueObject gVO, String[] params) {
    log.info("altaPuntoMenu");
    MenusDAO menuDAO = MenusDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = menuDAO.altaPuntoMenu(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }
  
  public Vector modificarPuntoMenu(GeneralValueObject gVO, String[] params) {
    log.info("modificarPuntoMenu");
    MenusDAO menuDAO = MenusDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = menuDAO.modificarPuntoMenu(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }
  
  public Vector eliminarPuntoMenu(GeneralValueObject gVO, String[] params) {
    log.info("eliminarPuntoMenu");
    MenusDAO menuDAO = MenusDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = menuDAO.eliminarPuntoMenu(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }  
  
  public Vector obtenerMenuDiputacion(GeneralValueObject gVO,String[] params) {

    log.info("obtenerMenuDiputacion");
    MenusDAO menuDAO = MenusDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = menuDAO.obtenerMenuDiputacion(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
      resultado = null;
    }
    return resultado;
  }
  
  public Vector altaMenuCompleto(GeneralValueObject gVO,Vector listaMenu, String[] params) {
    log.info("altaMenuCompleto");
    MenusDAO menuDAO = MenusDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = menuDAO.altaMenuCompleto(gVO,listaMenu,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }    
    return resultado;
  }
}