// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.OrganizacionVO;
import java.util.Vector;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.OrganizacionesDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: OrganizacionesManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class OrganizacionesManager  {
  private static OrganizacionesManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(OrganizacionesManager.class.getName());

  protected OrganizacionesManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static OrganizacionesManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(OrganizacionesManager.class) {
        if (instance == null) {
          instance = new OrganizacionesManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaOrganizaciones(String[] params){
    log.debug("getListaOrganizacionesesValue");
    OrganizacionesDAO organizacionDAO = OrganizacionesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = organizacionDAO.getListaOrganizaciones(params);
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarOrganizacion(String codigo, String[] params) {
    log.debug("eliminarOrganizacion");
    OrganizacionesDAO organizacionDAO = OrganizacionesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = organizacionDAO.eliminarOrganizacion(codigo,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector modificarOrganizacion(GeneralValueObject gVO, String[] params){
    log.debug("modificarOrganizacion");
    OrganizacionesDAO organizacionDAO = OrganizacionesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = organizacionDAO.modificarOrganizacion(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    return resultado;
  }

  public Vector altaOrganizacion(GeneralValueObject gVO, String[] params) {
    log.debug("altaOrganizacion");
    OrganizacionesDAO organizacionDAO = OrganizacionesDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = organizacionDAO.altaOrganizacion(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    return resultado;
  }


  public String getDescripcionOrganizacion(String codOrganizacion, String[] params) {
    log.debug("getDescripcionOrganizacion init");
    String descripcion = "";
    try{

        descripcion =  OrganizacionesDAO.getInstance().getDescripcionOrganizacion(codOrganizacion, params);

    }catch (Exception e) {
        log.error("Excepción capturada: " + e.toString());
    }

    return descripcion;
  }


  /**
   * Recupera las organizaciones distintas a una dada. Util por ejemplo para recuperar información de organizaciones que no
   * no son de pruebas
   * @param codOrganizacion: Código de la organización
   * @param params: Parámetros de conexión a la base de datos
   * @return
   */
  public ArrayList<OrganizacionVO> getOrganizacionDistintasDe(int codOrganizacion,String[] params){
      ArrayList<OrganizacionVO> orgs = new ArrayList<OrganizacionVO>();
      Connection con = null;
      AdaptadorSQLBD adapt = null;
      try{
          adapt = new AdaptadorSQLBD(params);
          con = adapt.getConnection();
          orgs = OrganizacionesDAO.getInstance().getOrganizacionDistintasDe(codOrganizacion, con);
      }catch(Exception e){
          e.printStackTrace();
      }finally{
          try{
              adapt.devolverConexion(con);
          }catch(Exception e){
              e.printStackTrace();
          }
      }
      return orgs;
  }


}