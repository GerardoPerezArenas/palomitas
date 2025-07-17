package es.altia.agora.business.planeamiento.mantenimiento.persistence;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.agora.business.planeamiento.mantenimiento.MantTipoValueObject;
import es.altia.agora.business.planeamiento.mantenimiento.persistence.manual.MantTipoDAO;

public class MantTipoManager {

    // Mi propia instancia usada en el metodo getInstance.
    private static MantTipoManager instance = null;

    // Para el fichero de configuracion technical.
    protected static Config m_ConfigTechnical;
    // Para el fichero de mensajes de error localizados.
    protected static Config m_ConfigError;
    protected static Log m_Log =
            LogFactory.getLog(MantTipoManager.class.getName());

    protected MantTipoManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    }


    public static MantTipoManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(MantTipoManager.class) {
                if (instance == null)
                    instance = new MantTipoManager();
            }
        }
        return instance;
    }

    public void create(MantTipoValueObject mantTipoVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantTipoManager.create");

        try{
            MantTipoDAO.getInstance().create(mantTipoVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void delete(MantTipoValueObject mantTipoVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantTipoManager.delete");

        try{
            MantTipoDAO.getInstance().delete(mantTipoVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void modify(MantTipoValueObject mantTipoVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantTipoManager.modify");

        try{
            MantTipoDAO.getInstance().modify(mantTipoVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public Vector findAll(String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantTipoManager.findAll");
        Vector subsecciones;
        try{
            subsecciones = MantTipoDAO.getInstance().findAll(params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            subsecciones = new Vector();
        }
        return subsecciones;
    }

    public Vector findByTipoRegistro(Character tipoRegistro, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantTipoManager.findByTipoRegistro");
        Vector subsecciones;
        try{
            subsecciones = MantTipoDAO.getInstance().findByTipoRegistro(tipoRegistro, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            subsecciones = new Vector();
        }
        return subsecciones;
    }
}