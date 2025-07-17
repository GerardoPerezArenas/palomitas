package es.altia.agora.business.planeamiento.mantenimiento.persistence;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.agora.business.planeamiento.mantenimiento.MantProcedimientoValueObject;
import es.altia.agora.business.planeamiento.mantenimiento.persistence.manual.MantProcedimientoDAO;

public class MantProcedimientoManager {

    // Mi propia instancia usada en el metodo getInstance.
    private static MantProcedimientoManager instance = null;

    // Para el fichero de configuracion technical.
    protected static Config m_ConfigTechnical;
    // Para el fichero de mensajes de error localizados.
    protected static Config m_ConfigError;
    protected static Log m_Log =
            LogFactory.getLog(MantProcedimientoManager.class.getName());

    protected MantProcedimientoManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    }


    public static MantProcedimientoManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(MantProcedimientoManager.class) {
                if (instance == null)
                    instance = new MantProcedimientoManager();
            }
        }
        return instance;
    }

    public void create(MantProcedimientoValueObject mantProcedimientoVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantProcedimientoManager.create");

        try{
            MantProcedimientoDAO.getInstance().create(mantProcedimientoVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void delete(MantProcedimientoValueObject mantProcedimientoVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantProcedimientoManager.delete");

        try{
            MantProcedimientoDAO.getInstance().delete(mantProcedimientoVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void modify(MantProcedimientoValueObject mantProcedimientoVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantProcedimientoManager.modify");

        try{
            MantProcedimientoDAO.getInstance().modify(mantProcedimientoVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public Vector findAll(String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantProcedimientoManager.findAll");
        Vector subsecciones;
        try{
            subsecciones = MantProcedimientoDAO.getInstance().findAll(params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            subsecciones = new Vector();
        }
        return subsecciones;
    }
}