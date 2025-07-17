package es.altia.agora.business.planeamiento.persistence;

import es.altia.agora.business.planeamiento.AnotacionRectificacionRegistroValueObject;
import es.altia.agora.business.planeamiento.persistence.manual.AnotacionRectificacionRegistroDAO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.util.Vector;

public class AnotacionRectificacionRegistroManager {

    // Mi propia instancia usada en el metodo getInstance.
    private static AnotacionRectificacionRegistroManager instance = null;

    // Para el fichero de configuracion technical.
    protected static Config m_ConfigTechnical;

    protected static Log m_Log =
            LogFactory.getLog(AnotacionRectificacionRegistroManager.class.getName());

    protected AnotacionRectificacionRegistroManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    }


    public static AnotacionRectificacionRegistroManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(AnotacionRectificacionRegistroManager.class) {
                if (instance == null)
                    instance = new AnotacionRectificacionRegistroManager();
            }
        }
        return instance;
    }

    public void create(AnotacionRectificacionRegistroValueObject AnotacionRectificacionRegistroVO, Connection conexion,String[] params) throws BDException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("AnotacionRectificacionRegistroManager.create");

        AnotacionRectificacionRegistroDAO.getInstance().create(AnotacionRectificacionRegistroVO, conexion, params);
    }

    public void delete(AnotacionRectificacionRegistroValueObject AnotacionRectificacionRegistroVO, Connection conexion) throws BDException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("AnotacionRectificacionRegistroManager.delete");

        AnotacionRectificacionRegistroDAO.getInstance().delete(AnotacionRectificacionRegistroVO, conexion);
    }

    public void deleteByRegistro(AnotacionRectificacionRegistroValueObject AnotacionRectificacionRegistroVO, Connection conexion) throws BDException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("AnotacionRectificacionRegistroManager.deleteByRegitro");

        AnotacionRectificacionRegistroDAO.getInstance().deleteByRegistro(AnotacionRectificacionRegistroVO, conexion);
    }

    public void modify(AnotacionRectificacionRegistroValueObject AnotacionRectificacionRegistroVO, Connection conexion) throws BDException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("AnotacionRectificacionRegistroManager.modify");

        AnotacionRectificacionRegistroDAO.getInstance().modify(AnotacionRectificacionRegistroVO, conexion);
    }

    public Vector findAll(String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("AnotacionRectificacionRegistroManager.findAll");
        Vector anotaciones;
        try{
            anotaciones = AnotacionRectificacionRegistroDAO.getInstance().findAll(params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            anotaciones = new Vector();
        }
        return anotaciones;
    }

    public Vector findByRegistro(Character tipoRegistro, String codigoSubseccion, String anho,
                                 Integer numero, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("AnotacionRectificacionRegistroManager.findByRegistro");
        Vector anotaciones;
        try{
            anotaciones = AnotacionRectificacionRegistroDAO.getInstance().findByRegistro(tipoRegistro, codigoSubseccion,
                    anho, numero, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            anotaciones = new Vector();
        }
        return anotaciones;
    }
}