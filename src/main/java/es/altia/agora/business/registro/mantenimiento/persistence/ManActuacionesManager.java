package es.altia.agora.business.registro.mantenimiento.persistence;

import java.util.Vector;

import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.exception.*;
import es.altia.agora.business.registro.mantenimiento.persistence.manual.ManActuacionesDAO;
import es.altia.agora.business.registro.mantenimiento.ManActuacionesValueObject;

public class ManActuacionesManager {

  // Mi propia instancia usada en el metodo getInstance.
  private static ManActuacionesManager instance = null;

  // Para el fichero de configuracion technical.
  protected static Config m_ConfigTechnical;
  // Para el fichero de mensajes de error localizados.
    protected static Config m_ConfigError;
    protected static Log m_Log =
            LogFactory.getLog(ManActuacionesManager.class.getName());

    protected ManActuacionesManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }


  /**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de ManActuacionesManager
    */
  public static ManActuacionesManager getInstance() {
    //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(ManActuacionesManager.class) {
        if (instance == null)
          instance = new ManActuacionesManager();
            }
        }
        return instance;
    }

  public Vector buscaActuaciones(String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("buscaActuaciones");

    Vector actuaciones = new Vector();

    try{
      actuaciones = ManActuacionesDAO.getInstance().loadActuaciones(params);
    }catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return actuaciones;
    }
  }
  
    public Vector buscaActuacionesByFecha(String[] params, String fecha) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("buscaActuaciones");

        Vector actuaciones = new Vector();

        try {
            actuaciones = ManActuacionesDAO.getInstance().loadActuacionesByFecha(params, fecha);
        } catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
        } finally {
            return actuaciones;
        }
    }

  public Vector buscaActuacionesUtilizados(String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("buscaActuacionesUtilizados");

    Vector actuacionesUtilizados = new Vector();

    try{
      actuacionesUtilizados = ManActuacionesDAO.getInstance().loadActuacionesUtilizados(params);
    }catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return actuacionesUtilizados;
    }
  }

  public Vector buscaTemasUtilizadosCerrados(String fechaCerradoE,String fechaCerradoS,String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("buscaTemasUtilizadosCerrados");

    Vector temasUtilizadosCerrados = new Vector();

    try{
      temasUtilizadosCerrados = ManActuacionesDAO.getInstance().loadTemasUtilizadosCerrados(fechaCerradoE,fechaCerradoS,params);
    }catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return temasUtilizadosCerrados;
    }
  }

  public Vector eliminarTema(ManActuacionesValueObject manActVO,String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("eliminarTema");

    Vector temas = new Vector();

    try{
      temas = ManActuacionesDAO.getInstance().eliminarTema(manActVO,params);
    }catch (Exception te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return temas;
    }
  }

  public Vector altaTema(ManActuacionesValueObject manActVO,String[] params) {

   //queremos estar informados de cuando este metodo es ejecutado
   m_Log.debug("altaTema");

   Vector temas = new Vector();

   try{
     temas = ManActuacionesDAO.getInstance().altaTema(manActVO,params);
   }catch (Exception te) {
           m_Log.error("JDBC Technical problem " + te.getMessage());
   }finally{
     return temas;
   }
 }

 public Vector modificarTema(ManActuacionesValueObject manActVO,String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("modificarTema");

    Vector temas = new Vector();

    try{
      temas = ManActuacionesDAO.getInstance().modificarTema(manActVO,params);
    }catch (Exception te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return temas;
    }
  }

}