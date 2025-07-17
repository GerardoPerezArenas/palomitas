package es.altia.agora.business.planeamiento.mantenimiento.persistence;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.agora.business.planeamiento.mantenimiento.MantGradoProteccionValueObject;
import es.altia.agora.business.planeamiento.mantenimiento.persistence.manual.MantGradoProteccionDAO;

public class MantGradoProteccionManager {

    // Mi propia instancia usada en el metodo getInstance.
    private static MantGradoProteccionManager instance = null;

    // Para el fichero de configuracion technical.
    protected static Config m_ConfigTechnical;
    // Para el fichero de mensajes de error localizados.
    protected static Config m_ConfigError;
    protected static Log m_Log =
            LogFactory.getLog(MantGradoProteccionManager.class.getName());

    protected MantGradoProteccionManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    }


    public static MantGradoProteccionManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(MantGradoProteccionManager.class) {
                if (instance == null)
                    instance = new MantGradoProteccionManager();
            }
        }
        return instance;
    }

    public void create(MantGradoProteccionValueObject mantGradoProteccionVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantGradoProteccionManager.create");

        try{
            MantGradoProteccionDAO.getInstance().create(mantGradoProteccionVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void delete(MantGradoProteccionValueObject mantGradoProteccionVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantGradoProteccionManager.delete");

        try{
            MantGradoProteccionDAO.getInstance().delete(mantGradoProteccionVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void modify(MantGradoProteccionValueObject mantGradoProteccionVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantGradoProteccionManager.modify");

        try{
            MantGradoProteccionDAO.getInstance().modify(mantGradoProteccionVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public Vector findAll(String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantGradoProteccionManager.findAll");
        Vector subsecciones;
        try{
            subsecciones = MantGradoProteccionDAO.getInstance().findAll(params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            subsecciones = new Vector();
        }
        return subsecciones;
    }
}