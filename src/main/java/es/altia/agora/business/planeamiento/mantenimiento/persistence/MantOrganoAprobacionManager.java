package es.altia.agora.business.planeamiento.mantenimiento.persistence;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.agora.business.planeamiento.mantenimiento.MantOrganoAprobacionValueObject;
import es.altia.agora.business.planeamiento.mantenimiento.persistence.manual.MantOrganoAprobacionDAO;

public class MantOrganoAprobacionManager {

    // Mi propia instancia usada en el metodo getInstance.
    private static MantOrganoAprobacionManager instance = null;

    // Para el fichero de configuracion technical.
    protected static Config m_ConfigTechnical;
    // Para el fichero de mensajes de error localizados.
    protected static Config m_ConfigError;
    protected static Log m_Log =
            LogFactory.getLog(MantOrganoAprobacionManager.class.getName());

    protected MantOrganoAprobacionManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    }


    public static MantOrganoAprobacionManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(MantOrganoAprobacionManager.class) {
                if (instance == null)
                    instance = new MantOrganoAprobacionManager();
            }
        }
        return instance;
    }

    public void create(MantOrganoAprobacionValueObject mantOrganoAprobacionVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantOrganoAprobacionManager.create");

        try{
            MantOrganoAprobacionDAO.getInstance().create(mantOrganoAprobacionVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void delete(MantOrganoAprobacionValueObject mantOrganoAprobacionVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantOrganoAprobacionManager.delete");

        try{
            MantOrganoAprobacionDAO.getInstance().delete(mantOrganoAprobacionVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void modify(MantOrganoAprobacionValueObject mantOrganoAprobacionVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantOrganoAprobacionManager.modify");

        try{
            MantOrganoAprobacionDAO.getInstance().modify(mantOrganoAprobacionVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public Vector findAll(String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantOrganoAprobacionManager.findAll");
        Vector subsecciones;
        try{
            subsecciones = MantOrganoAprobacionDAO.getInstance().findAll(params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            subsecciones = new Vector();
        }
        return subsecciones;
    }
}