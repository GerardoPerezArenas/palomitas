package es.altia.agora.business.sge.persistence;

import java.util.Vector;

import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.exception.*;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.persistence.manual.BuzonEntradaSGEDAO;

public class BuzonEntradaSGEManager {

	// Mi propia instancia usada en el metodo getInstance.
	private static BuzonEntradaSGEManager instance = null;

	// Para el fichero de configuracion technical.
	protected static Config m_ConfigTechnical;
	// Para el fichero de mensajes de error localizados.
    protected static Config m_ConfigError;
    protected static Log m_Log =
            LogFactory.getLog(BuzonEntradaSGEManager.class.getName());


    protected BuzonEntradaSGEManager() {
        //Queremos usar el fichero de configuración technical
		m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
	}


	/**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de MantTemaManager
    */
	public static BuzonEntradaSGEManager getInstance() {
		//Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
		// Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
			synchronized(BuzonEntradaSGEManager.class) {
				if (instance == null)
					instance = new BuzonEntradaSGEManager();
            }
        }
        return instance;
    }

	public Vector loadIniciados(UsuarioValueObject usuarioVO) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("buscaInciados");

		Vector iniciados = new Vector();

		try{
			iniciados = BuzonEntradaSGEDAO.getInstance().loadIniciados(usuarioVO);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			return iniciados;
		}
	}

	public int modifyEstado (Vector inicioVector, String[] params) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("buscaInciados");

		int resultado = -1;

		try{
			resultado = BuzonEntradaSGEDAO.getInstance().modifyEstado(inicioVector, params);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			return resultado;
		}
	}
}

