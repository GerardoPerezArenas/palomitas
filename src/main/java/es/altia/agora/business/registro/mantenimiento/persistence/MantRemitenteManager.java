package es.altia.agora.business.registro.mantenimiento.persistence;

import java.util.Vector;

import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.exception.*;
import es.altia.agora.business.registro.mantenimiento.persistence.manual.MantRemitenteDAO;
import es.altia.agora.business.registro.mantenimiento.MantTemasValueObject;

public class MantRemitenteManager {

  // Mi propia instancia usada en el metodo getInstance.
  private static MantRemitenteManager instance = null;

  // Para el fichero de configuracion technical.
  protected static Config m_ConfigTechnical;
  // Para el fichero de mensajes de error localizados.
  protected static Config m_ConfigError;
 protected static Log m_Log =
            LogFactory.getLog(MantRemitenteManager.class.getName());


    protected MantRemitenteManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }


  /**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de MantTemaManager
    */
  public static MantRemitenteManager getInstance() {
    //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(MantRemitenteManager.class) {
        if (instance == null)
          instance = new MantRemitenteManager();
            }
        }
        return instance;
    }

  public Vector buscaTemas(String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("buscaTemas");

    Vector temas = new Vector();

    try{
      temas = MantRemitenteDAO.getInstance().loadTemas(params);
    }catch (Exception te) {
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
      temasUtilizados = MantRemitenteDAO.getInstance().loadTemasUtilizados(params);
    }catch (Exception te) {
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
      temasUtilizadosCerrados = MantRemitenteDAO.getInstance().loadTemasUtilizadosCerrados(fechaCerradoE,fechaCerradoS,params);
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
      temas = MantRemitenteDAO.getInstance().eliminarTema(temasVO,params);
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
      temas = MantRemitenteDAO.getInstance().modificarTema(temasVO,params);
    }catch (Exception te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return temas;
    }
  }
  public void modificarTemaActivo(String idModificar,String[] params) {

	    //queremos estar informados de cuando este metodo es ejecutado
	    m_Log.debug("modificarTema");
	    try{
	    	MantRemitenteDAO.getInstance().modificarTemaActivo(idModificar,params);
	    }catch (Exception te) {
	            m_Log.error("JDBC Technical problem " + te.getMessage());
	    }finally{
	    }
	  }

  public Vector altaTema(MantTemasValueObject temasVO,String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("altaTema");

    Vector temas = new Vector();

    try{
      temas = MantRemitenteDAO.getInstance().altaTema(temasVO,params);
    }catch (Exception te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return temas;
    }
  }

}