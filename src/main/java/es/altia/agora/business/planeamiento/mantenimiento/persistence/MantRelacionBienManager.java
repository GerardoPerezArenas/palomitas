package es.altia.agora.business.planeamiento.mantenimiento.persistence;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.agora.business.planeamiento.mantenimiento.MantRelacionBienValueObject;
import es.altia.agora.business.planeamiento.mantenimiento.persistence.manual.MantRelacionBienDAO;

public class MantRelacionBienManager {

    // Mi propia instancia usada en el metodo getInstance.
    private static MantRelacionBienManager instance = null;

    // Para el fichero de configuracion technical.
    protected static Config m_ConfigTechnical;
    // Para el fichero de mensajes de error localizados.
    protected static Config m_ConfigError;
    protected static Log m_Log =
            LogFactory.getLog(MantRelacionBienManager.class.getName());

    protected MantRelacionBienManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    }


    public static MantRelacionBienManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(MantRelacionBienManager.class) {
                if (instance == null)
                    instance = new MantRelacionBienManager();
            }
        }
        return instance;
    }

    public void create(MantRelacionBienValueObject mantRelacionBienVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantRelacionBienManager.create");

        try{
            MantRelacionBienDAO.getInstance().create(mantRelacionBienVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void delete(MantRelacionBienValueObject mantRelacionBienVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantRelacionBienManager.delete");

        try{
            MantRelacionBienDAO.getInstance().delete(mantRelacionBienVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void modify(MantRelacionBienValueObject mantRelacionBienVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantRelacionBienManager.modify");

        try{
            MantRelacionBienDAO.getInstance().modify(mantRelacionBienVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public Vector findAll(String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantRelacionBienManager.findAll");
        Vector subsecciones;
        try{
            subsecciones = MantRelacionBienDAO.getInstance().findAll(params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            subsecciones = new Vector();
        }
        return subsecciones;
    }
}