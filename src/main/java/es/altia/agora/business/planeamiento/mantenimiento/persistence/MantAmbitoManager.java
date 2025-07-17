package es.altia.agora.business.planeamiento.mantenimiento.persistence;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.agora.business.planeamiento.mantenimiento.MantAmbitoValueObject;
import es.altia.agora.business.planeamiento.mantenimiento.persistence.manual.MantAmbitoDAO;

public class MantAmbitoManager {

    // Mi propia instancia usada en el metodo getInstance.
    private static MantAmbitoManager instance = null;

    // Para el fichero de configuracion technical.
    protected static Config m_ConfigTechnical;
    // Para el fichero de mensajes de error localizados.
    protected static Config m_ConfigError;
    protected static Log m_Log =
            LogFactory.getLog(MantAmbitoManager.class.getName());

    protected MantAmbitoManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    }


    public static MantAmbitoManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(MantAmbitoManager.class) {
                if (instance == null)
                    instance = new MantAmbitoManager();
            }
        }
        return instance;
    }

    public void create(MantAmbitoValueObject mantAmbitoVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantAmbitoManager.create");

        try{
            MantAmbitoDAO.getInstance().create(mantAmbitoVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void delete(MantAmbitoValueObject mantAmbitoVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantAmbitoManager.delete");

        try{
            MantAmbitoDAO.getInstance().delete(mantAmbitoVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void modify(MantAmbitoValueObject mantAmbitoVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantAmbitoManager.modify");

        try{
            MantAmbitoDAO.getInstance().modify(mantAmbitoVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public Vector findAll(String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantAmbitoManager.findAll");
        Vector subsecciones;
        try{
            subsecciones = MantAmbitoDAO.getInstance().findAll(params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            subsecciones = new Vector();
        }
        return subsecciones;
    }
}