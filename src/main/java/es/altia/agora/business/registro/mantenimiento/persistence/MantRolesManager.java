package es.altia.agora.business.registro.mantenimiento.persistence;

import es.altia.agora.business.registro.mantenimiento.MantRolesValueObject;
import java.util.Vector;

import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.exception.*;
import es.altia.agora.business.registro.mantenimiento.persistence.manual.MantRolesDAO;
import es.altia.agora.business.registro.mantenimiento.persistence.manual.MantTransporteDAO;
import es.altia.agora.business.registro.mantenimiento.MantTemasValueObject;

public class MantRolesManager {

  // Mi propia instancia usada en el metodo getInstance.
  private static MantRolesManager instance = null;

  // Para el fichero de configuracion technical.
  protected static Config m_ConfigTechnical;
  // Para el fichero de mensajes de error localizados.
    protected static Config m_ConfigError;
    protected static Log m_Log =
            LogFactory.getLog(MantRolesManager.class.getName());

    protected MantRolesManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }

  /**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de MantTemaManager
    */
  public static MantRolesManager getInstance() {
    //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(MantRolesManager.class) {
        if (instance == null)
          instance = new MantRolesManager();
            }
        }
        return instance;
    }

  public Vector buscaRoles(String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("buscaRoles");

    Vector roles = new Vector();

    try{
      roles = MantRolesDAO.getInstance().loadRoles(params);
    }catch (Exception te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return roles;
    }
  }

    public String getRolDefecto(String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("buscaRoles");

    String rol = "";

    try{
      rol = MantRolesDAO.getInstance().getRolDefecto(params);
    }catch (Exception te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return rol;
    }
  }

  public Vector eliminarRol(MantRolesValueObject rolesVO,String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("eliminarRol");

    Vector roles = new Vector();

    try{
      roles = MantRolesDAO.getInstance().eliminarRol(rolesVO,params);
    }catch (Exception te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return roles;
    }
  }

  public Vector modificarRol(MantRolesValueObject rolesVO,String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("modificarRol");

    Vector roles = new Vector();

    try{
      roles = MantRolesDAO.getInstance().modificarRol(rolesVO,params);
    }catch (Exception te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return roles;
    }
  }
  
  public void modificarRolDefecto(String idModificar,String[] params) {

	    //queremos estar informados de cuando este metodo es ejecutado
	    m_Log.debug("modificarRolDefecto");

	    
	    try{
	     MantRolesDAO.getInstance().modificarRolDefecto(idModificar,params);
	    }catch (Exception te) {
	            m_Log.error("JDBC Technical problem " + te.getMessage());
	    }finally{
	
	    }
	  }

  public Vector altaRol(MantRolesValueObject rolesVO,String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("altaRol");

    Vector roles = new Vector();

    try{
      roles = MantRolesDAO.getInstance().altaRol(rolesVO,params);
    }catch (Exception te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
    }finally{
      return roles;
    }
  }


}