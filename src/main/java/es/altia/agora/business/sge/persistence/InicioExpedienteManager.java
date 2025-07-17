package es.altia.agora.business.sge.persistence;

import java.util.Vector;

import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.exception.*;
import es.altia.agora.business.sge.persistence.manual.InicioExpedienteDAO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.InicioExpedienteValueObject;

public class InicioExpedienteManager {

	// Mi propia instancia usada en el metodo getInstance.
	private static InicioExpedienteManager instance = null;

	// Para el fichero de configuracion technical.
	protected static Config m_ConfigTechnical;
	// Para el fichero de mensajes de error localizados.
    protected static Config m_ConfigError;
    protected static Log m_Log =
            LogFactory.getLog(InicioExpedienteManager.class.getName());           

    protected InicioExpedienteManager() {
        //Queremos usar el fichero de configuración technical
		m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");

	}


	/**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de MantTemaManager
    */
	public static InicioExpedienteManager getInstance() {
		//Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
		// Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
			synchronized(InicioExpedienteManager.class) {
				if (instance == null)
					instance = new InicioExpedienteManager();
            }
        }
        return instance;
    }

	public int insertExpediente(InicioExpedienteValueObject inicioVO, String[] params) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("insertExpediente");

		int res = -1;

		try{
			res = InicioExpedienteDAO.getInstance().insertExpediente(inicioVO, params);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			return res;
		}
	}
	
	public Vector loadProcedimientos(UsuarioValueObject usuarioVO) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("loadProcedimientos");

		Vector res = new Vector();

		try{
			res = InicioExpedienteDAO.getInstance().loadProcedimientos(usuarioVO);
		}catch (TechnicalException te) {		
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			return res;
		}
	}

}


