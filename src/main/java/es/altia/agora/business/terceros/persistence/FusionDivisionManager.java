// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.persistence;

// PAQUETES IMPORTADOS
import java.util.*;
import es.altia.agora.business.terceros.persistence.manual.*;
import es.altia.agora.business.util.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class FusionDivisionManager  {
  private static FusionDivisionManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(FusionDivisionManager.class.getName());

  protected FusionDivisionManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static FusionDivisionManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(FusionDivisionManager.class) {
        if (instance == null) {
          instance = new FusionDivisionManager();
        }
      }
    }
    return instance;
  }

  public Vector getListaProcesos(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("getListaProcesos");
    FusionDivisionDAO fusionDAO = FusionDivisionDAO.getInstance();
    Vector resultado = new Vector();
    try{
      m_Log.debug("Usando persistencia manual");
      resultado = fusionDAO.getListaProcesos(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("getListaProcesos");
      return resultado;
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public String retrocederProceso(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("retrocederProceso");
    FusionDivisionDAO fusionDAO = FusionDivisionDAO.getInstance();
    String correcto = "SI";
    try{
      m_Log.debug("Usando persistencia manual");
      correcto = fusionDAO.retrocederProceso(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("retrocederProceso");
      return correcto;
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
      return "NO";
    }
  }


  public String insertarProceso(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("insertarProceso");
    FusionDivisionDAO fusionDAO = FusionDivisionDAO.getInstance();
    String correcto = "SI";
    try{
      m_Log.debug("Usando persistencia manual");
      correcto = fusionDAO.insertarProceso(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("insertarProceso");
      return correcto;
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
      return "NO";
    }
  }

  public String eliminarProceso(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("eliminarProceso");
    FusionDivisionDAO fusionDAO = FusionDivisionDAO.getInstance();
    String correcto = "SI";
    try{
      m_Log.debug("Usando persistencia manual");
      correcto = fusionDAO.eliminarProceso(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("eliminarProceso");
      return correcto;
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
      return "NO";
    }
  }

  public String procesarProceso(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("procesarProceso");
    FusionDivisionDAO fusionDAO = FusionDivisionDAO.getInstance();
    String correcto = "SI";
    try{
      m_Log.debug("Usando persistencia manual");
      correcto = fusionDAO.procesarProceso(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("procesarProceso");
      return correcto;
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
      return "NO";
    }
  }

  public String finalizarProceso(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("finalizarProceso");
    FusionDivisionDAO fusionDAO = FusionDivisionDAO.getInstance();
    String correcto = "SI";
    try{
      m_Log.debug("Usando persistencia manual");
      correcto = fusionDAO.finalizarProceso(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("finalizarProceso");
      return correcto;
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
      return "NO";
    }
  }


  public GeneralValueObject verDomicilios(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("verDomicilios");
    FusionDivisionDAO fusionDAO = FusionDivisionDAO.getInstance();
    GeneralValueObject resultado = new GeneralValueObject();
    try{
      m_Log.debug("Usando persistencia manual");
      resultado = fusionDAO.verDomicilios(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("verDomicilios");
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
    return resultado;
  }

  public GeneralValueObject verDetalle(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("verDetalle");
    FusionDivisionDAO fusionDAO = FusionDivisionDAO.getInstance();
    GeneralValueObject resultado = new GeneralValueObject();
    try{
      m_Log.debug("Usando persistencia manual");
      resultado = fusionDAO.verDetalle(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("verDetalle");
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
    return resultado;
  }
}