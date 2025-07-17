// NOMBRE DEL PAQUETE
package es.altia.agora.business.registro.persistence;

// PAQUETES IMPORTADOS

import java.util.Vector;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.registro.DiligenciasValueObject;
import es.altia.agora.business.registro.persistence.manual.DiligenciasDAO;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DiligenciasManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class DiligenciasManager {

  private static DiligenciasManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
        LogFactory.getLog(DiligenciasManager.class.getName());

  protected DiligenciasManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static DiligenciasManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(DiligenciasManager.class) {
        if (instance == null) {
          instance = new DiligenciasManager();
        }
      }
    }
    return instance;
  }

  public Vector loadVector(DiligenciasValueObject dilig,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("loadDiligenciasValue");
    Vector lista = new Vector();
    DiligenciasDAO diligDAO = DiligenciasDAO.getInstance();
    try{
      m_Log.debug("Usando persistencia manual");
      lista = diligDAO.loadVector(dilig,params);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("loadDiligenciasValue");
      return lista;
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
      return lista;
    }
  }
  
  public DiligenciasValueObject load(DiligenciasValueObject dilig,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("loadDiligenciasValue");
    DiligenciasDAO diligDAO = DiligenciasDAO.getInstance();
    try{
      m_Log.debug("Usando persistencia manual");
      dilig = diligDAO.load(dilig,params);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("loadDiligenciasValue");
      return dilig;
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
      return dilig;
    }
  }

  public int insert(DiligenciasValueObject dilig,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("insertDiligenciasValue");
    DiligenciasDAO diligDAO = DiligenciasDAO.getInstance();
    int correcto=0;
    try{
      m_Log.debug("Usando persistencia manual");
      correcto = diligDAO.insert(dilig,params);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("insertDiligenciasValue");
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
    return correcto;
  }

  public int modify(DiligenciasValueObject dilig,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("modifyDiligenciasValue");
    DiligenciasDAO diligDAO = DiligenciasDAO.getInstance();
    int correcto=0;
    try{
      m_Log.debug("Usando persistencia manual");
      correcto = diligDAO.modify(dilig,params);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("modifyDiligenciasValue");
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
    return correcto;
  }

  public int delete(DiligenciasValueObject dilig,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("deleteDiligenciasValue");
    DiligenciasDAO diligDAO = DiligenciasDAO.getInstance();
    int correcto=0;
    try{
      m_Log.debug("Usando persistencia manual");
      correcto = diligDAO.delete(dilig,params);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("deleteDiligenciasValue");
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
    return correcto;
  }
}