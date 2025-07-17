// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.InfoSistemaDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.LabelValueTO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.util.ArrayList;
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

public class InfoSistemaManager  {
  private static InfoSistemaManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(InfoSistemaManager.class.getName());

  protected InfoSistemaManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static InfoSistemaManager getInstance() { 
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(InfoSistemaManager.class) {
        if (instance == null) {
          instance = new InfoSistemaManager();
        }
      }
    }
    return instance;
  }

  
  
  
  /**
   * Devuelve una colección de objetos GeneralValueTO con el código y descripción de cada idioma recuperado
   * de la base de datos
   * @param params: Array de String con los parámetros de la conexión
   * @param idIdioma: Id del idioma seleccionado por el usuario y que no deseamos
   * @return Coleccion de GeneralValueTO
   */
   public Vector ejecutarConsulta(String[] params,String sql){
    InfoSistemaDAO infoSistemaDAO = InfoSistemaDAO.getInstance();
   
    Vector resultados=new Vector();
    
    try{     
      resultados=infoSistemaDAO.ejecutarConsulta(params,sql);               
      
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());      
    }    
    
    return resultados;
  }
   
     public Vector ConsultasAdministrador(String[] params,String sql){
    InfoSistemaDAO infoSistemaDAO = InfoSistemaDAO.getInstance();
   
    Vector resultadosExp=new Vector();
    
    try{     
      resultadosExp=infoSistemaDAO.ConsultasAdministrador(params,sql);               
      
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());      
    }    
    return resultadosExp;
  }
    
    }    
