package es.altia.agora.business.planeamiento.persistence;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.agora.business.planeamiento.TipoRegistroValueObject;
import es.altia.agora.business.planeamiento.persistence.manual.TipoRegistroDAO;

public class TipoRegistroManager {

    // Mi propia instancia usada en el metodo getInstance.
    private static TipoRegistroManager instance = null;

    // Para el fichero de configuracion technical.
    protected static Config m_ConfigTechnical;

    protected static Log m_Log =
            LogFactory.getLog(TipoRegistroManager.class.getName());

    protected TipoRegistroManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    }


    public static TipoRegistroManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(TipoRegistroManager.class) {
                if (instance == null)
                    instance = new TipoRegistroManager();
            }
        }
        return instance;
    }

    public void create(TipoRegistroValueObject tipoRegistroVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("TipoRegistroManager.create");

        try{
            TipoRegistroDAO.getInstance().create(tipoRegistroVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void delete(TipoRegistroValueObject tipoRegistroVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("TipoRegistroManager.delete");

        try{
            TipoRegistroDAO.getInstance().delete(tipoRegistroVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public void modify(TipoRegistroValueObject tipoRegistroVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("TipoRegistroManager.modify");

        try{
            TipoRegistroDAO.getInstance().modify(tipoRegistroVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
    }

    public Vector findAll(String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("TipoRegistroManager.findAll");
        Vector subsecciones;
        try{
            subsecciones = TipoRegistroDAO.getInstance().findAll(params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            subsecciones = new Vector();
        }
        return subsecciones;
    }
}