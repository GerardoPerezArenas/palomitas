package es.altia.agora.business.planeamiento.mantenimiento.persistence;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.agora.business.planeamiento.mantenimiento.MantSubseccionValueObject;
import es.altia.agora.business.planeamiento.mantenimiento.persistence.manual.MantSubseccionDAO;

public class MantSubseccionManager {

    // Mi propia instancia usada en el metodo getInstance.
    private static MantSubseccionManager instance = null;

    // Para el fichero de configuracion technical.
    protected static Config m_ConfigTechnical;
    // Para el fichero de mensajes de error localizados.
    protected static Config m_ConfigError;
    protected static Log m_Log =
            LogFactory.getLog(MantSubseccionManager.class.getName());

    protected MantSubseccionManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    }


    public static MantSubseccionManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(MantSubseccionManager.class) {
                if (instance == null)
                    instance = new MantSubseccionManager();
            }
        }
        return instance;
    }

    public void create(MantSubseccionValueObject mantSubseccionVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantSubseccionManager.create");

        try{
            MantSubseccionDAO.getInstance().create(mantSubseccionVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void delete(MantSubseccionValueObject mantSubseccionVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantSubseccionManager.delete");

        try{
            MantSubseccionDAO.getInstance().delete(mantSubseccionVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void modify(MantSubseccionValueObject mantSubseccionVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantSubseccionManager.modify");

        try{
            MantSubseccionDAO.getInstance().modify(mantSubseccionVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public Vector findAll(String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantSubseccionManager.findAll");
        Vector subsecciones;
        try{
            subsecciones = MantSubseccionDAO.getInstance().findAll(params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            subsecciones = new Vector();
        }
        return subsecciones;
    }

    public Vector findByTipoRegistro(Character tipoRegistro, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantSubseccionManager.findByTipoRegistro");
        Vector subsecciones;
        try{
            subsecciones = MantSubseccionDAO.getInstance().findByTipoRegistro(tipoRegistro, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            subsecciones = new Vector();
        }
        return subsecciones;
    }
}