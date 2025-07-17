package es.altia.agora.business.registro.mantenimiento.persistence;

import java.util.Vector;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.exception.*;
import es.altia.agora.business.registro.mantenimiento.persistence.manual.MantTemaDAO;
import es.altia.agora.business.registro.mantenimiento.MantTemasValueObject;

public class MantTemaManager {

  // Mi propia instancia usada en el metodo getInstance.
  private static MantTemaManager instance = null;

  // Para el fichero de configuracion technical.
  protected static Config m_ConfigTechnical;
  // Para el fichero de mensajes de error localizados.
    protected static Config m_ConfigError;
    protected static Log m_Log =
            LogFactory.getLog(MantTemaManager.class.getName());


    protected MantTemaManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }


  /**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de MantTemaManager
    */
  public static MantTemaManager getInstance() {
    //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(MantTemaManager.class) {
        if (instance == null)
          instance = new MantTemaManager();
            }
        }
        return instance;
    }

  public Vector buscaTemas(String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("buscaTemas");

    Vector temas = new Vector();

    try{
      temas = MantTemaDAO.getInstance().loadTemas(params);
    }catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return temas;
    }
  }
  
  public Vector buscaTemasDescripcion(String[] params,String nombreTema) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("buscaTemasDescripcion");

    Vector temas = new Vector();

    try{
      temas = MantTemaDAO.getInstance().buscaTemasDescripcion(params,nombreTema);
    }catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return temas;
    }
  }

  public Vector buscaTemasUtilizados(String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("buscaTemasUtilizados");

    Vector temasUtilizados = new Vector();

    try{
      temasUtilizados = MantTemaDAO.getInstance().loadTemasUtilizados(params);
    }catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return temasUtilizados;
    }
  }

  public Vector buscaTemasUtilizadosCerrados(String fechaCerradoE,String fechaCerradoS,String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("buscaTemasUtilizadosCerrados");

    Vector temasUtilizadosCerrados = new Vector();

    try{
      temasUtilizadosCerrados = MantTemaDAO.getInstance().loadTemasUtilizadosCerrados(fechaCerradoE,fechaCerradoS,params);
    }catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return temasUtilizadosCerrados;
    }
  }

  public Vector eliminarTema(MantTemasValueObject temasVO,String[] params) {

     //queremos estar informados de cuando este metodo es ejecutado
     m_Log.debug("eliminarTema");

     Vector temas = new Vector();

     try{
       temas = MantTemaDAO.getInstance().eliminarTema(temasVO,params);
     }catch (Exception te) {
             m_Log.error("JDBC Technical problem " + te.getMessage());
     }finally{
       return temas;
     }
   }

   public Vector modificarTema(MantTemasValueObject temasVO,String[] params) {

     //queremos estar informados de cuando este metodo es ejecutado
     m_Log.debug("modificarTema");

     Vector temas = new Vector();

     try{
       temas = MantTemaDAO.getInstance().modificarTema(temasVO,params);
     }catch (Exception te) {
             m_Log.error("JDBC Technical problem " + te.getMessage());
     }finally{
       return temas;
     }
   }

   public Vector altaTema(MantTemasValueObject temasVO,String[] params) {

     //queremos estar informados de cuando este metodo es ejecutado
     m_Log.debug("altaTema");

     Vector temas = new Vector();

     try{
       temas = MantTemaDAO.getInstance().altaTema(temasVO,params);
     }catch (Exception te) {
             m_Log.error("JDBC Technical problem " + te.getMessage());
     }finally{
       return temas;
     }
   }


}