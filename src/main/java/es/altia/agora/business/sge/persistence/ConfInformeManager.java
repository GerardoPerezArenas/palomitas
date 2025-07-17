package es.altia.agora.business.sge.persistence;

import java.util.Vector;

import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.exception.*;
import es.altia.agora.business.sge.ConfInformeValueObject;
import es.altia.agora.business.sge.persistence.manual.ConfInformeDAO;

public class ConfInformeManager {

	// Mi propia instancia usada en el metodo getInstance.
	private static ConfInformeManager instance = null;

	// Para el fichero de configuracion technical.
	protected static Config m_ConfigTechnical;
	// Para el fichero de mensajes de error localizados.
    protected static Config m_ConfigError;
    protected static Log m_Log =
            LogFactory.getLog(ConfInformeManager.class.getName());

    protected ConfInformeManager() {
        //Queremos usar el fichero de configuración technical
		m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
	}


	/**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de MantTemaManager
    */
	public static ConfInformeManager getInstance() {
		//Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
		// Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
			synchronized(ConfInformeManager.class) {
				if (instance == null)
					instance = new ConfInformeManager();
            }
        }
        return instance;
    }

	public Vector cargarCombos(ConfInformeValueObject ciVO, String[] params) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("cargarCombos");

		Vector opciones = new Vector();

		try{
			opciones = ConfInformeDAO.getInstance().cargarCombos(ciVO, params);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			return opciones;
		}
	}

       /**
     * Método encargado de recuperar el contenido de un determinado combo. Tiene en cuenta los procedimientos restringidos y los permisos que un
     * usuario pueda tener sobre ellos
     * @param ciVO ConfInformeValueObject que contiene la información necesaria para rellenar el combo
     * @param codUsuario: Código del usuario
     * @param codOrganizacion: Código de la organización
     * @param params: Parámetros de conexión a la BBDD
     * @return Vector
     * @throws es.altia.common.exception.TechnicalException
     */
    public Vector cargarCombos(ConfInformeValueObject ciVO, String codUsuario, String codOrganizacion, String[] params) throws TechnicalException {

            //queremos estar informados de cuando este metodo es ejecutado
            m_Log.debug("cargarCombos");

            Vector opciones = new Vector();

            try{
                opciones = ConfInformeDAO.getInstance().cargarCombos(ciVO, codUsuario,codOrganizacion, params);
                
            }catch (TechnicalException te) {
                m_Log.error("JDBC Technical problem " + te.getMessage());
            }catch (Exception e) {
                e.printStackTrace();
            }finally{
                return opciones;
            }
      }


}

