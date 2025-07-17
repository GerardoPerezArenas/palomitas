package es.altia.agora.business.registro.mantenimiento.persistence;

import java.util.Vector;

import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.exception.*;
import es.altia.agora.business.registro.mantenimiento.persistence.manual.MantTransporteDAO;
import es.altia.agora.business.registro.mantenimiento.MantTemasValueObject;

public class MantTransporteManager {

  // Mi propia instancia usada en el metodo getInstance.
  private static MantTransporteManager instance = null;

  // Para el fichero de configuracion technical.
  protected static Config m_ConfigTechnical;
  // Para el fichero de mensajes de error localizados.
    protected static Config m_ConfigError;
    protected static Log m_Log =
            LogFactory.getLog(MantTransporteManager.class.getName());

    protected MantTransporteManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }

  /**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de MantTemaManager
    */
  public static MantTransporteManager getInstance() {
    //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(MantTransporteManager.class) {
        if (instance == null)
          instance = new MantTransporteManager();
            }
        }
        return instance;
    }

  public Vector buscaTemas(String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("buscaTemas");

    Vector temas = new Vector();

    try{
      temas = MantTransporteDAO.getInstance().loadTemas(params);
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
      temasUtilizados = MantTransporteDAO.getInstance().loadTemasUtilizados(params);
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
      temasUtilizadosCerrados = MantTransporteDAO.getInstance().loadTemasUtilizadosCerrados(fechaCerradoE,fechaCerradoS,params);
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
      temas = MantTransporteDAO.getInstance().eliminarTema(temasVO,params);
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
      temas = MantTransporteDAO.getInstance().modificarTema(temasVO,params);
    }catch (Exception te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return temas;
    }
  }

  public void modificarTemaActivo(String idModificar,String[] params) {

	    //queremos estar informados de cuando este metodo es ejecutado
	    m_Log.debug("modificarTemaActivo");

	    
	    try{
	     MantTransporteDAO.getInstance().modificarTemaActivo(idModificar,params);
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
      temas = MantTransporteDAO.getInstance().altaTema(temasVO,params);
    }catch (Exception te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return temas;
    }
  }


}