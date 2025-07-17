package es.altia.agora.business.planeamiento.mantenimiento.persistence;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.agora.business.planeamiento.mantenimiento.MantCatalogacionValueObject;
import es.altia.agora.business.planeamiento.mantenimiento.persistence.manual.MantCatalogacionDAO;

public class MantCatalogacionManager {

    // Mi propia instancia usada en el metodo getInstance.
    private static MantCatalogacionManager instance = null;

    // Para el fichero de configuracion technical.
    protected static Config m_ConfigTechnical;
    // Para el fichero de mensajes de error localizados.
    protected static Config m_ConfigError;
    protected static Log m_Log =
            LogFactory.getLog(MantCatalogacionManager.class.getName());

    protected MantCatalogacionManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    }


    public static MantCatalogacionManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(MantCatalogacionManager.class) {
                if (instance == null)
                    instance = new MantCatalogacionManager();
            }
        }
        return instance;
    }

    public void create(MantCatalogacionValueObject mantCatalogacionVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantCatalogacionManager.create");

        try{
            MantCatalogacionDAO.getInstance().create(mantCatalogacionVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void delete(MantCatalogacionValueObject mantCatalogacionVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantCatalogacionManager.delete");

        try{
            MantCatalogacionDAO.getInstance().delete(mantCatalogacionVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void modify(MantCatalogacionValueObject mantCatalogacionVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantCatalogacionManager.modify");

        try{
            MantCatalogacionDAO.getInstance().modify(mantCatalogacionVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public Vector findAll(String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("MantCatalogacionManager.findAll");
        Vector subsecciones;
        try{
            subsecciones = MantCatalogacionDAO.getInstance().findAll(params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            subsecciones = new Vector();
        }
        return subsecciones;
    }
}