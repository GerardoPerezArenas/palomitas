package es.altia.agora.business.planeamiento.persistence;

import es.altia.agora.business.planeamiento.AnotacionRegistroValueObject;
import es.altia.agora.business.planeamiento.persistence.manual.AnotacionRegistroDAO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.util.Vector;

public class AnotacionRegistroManager {

    // Mi propia instancia usada en el metodo getInstance.
    private static AnotacionRegistroManager instance = null;

    // Para el fichero de configuracion technical.
    protected static Config m_ConfigTechnical;

    protected static Log m_Log =
            LogFactory.getLog(AnotacionRegistroManager.class.getName());

    protected AnotacionRegistroManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    }


    public static AnotacionRegistroManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(AnotacionRegistroManager.class) {
                if (instance == null)
                    instance = new AnotacionRegistroManager();
            }
        }
        return instance;
    }

    public void create(AnotacionRegistroValueObject anotacionRegistroVO, Connection conexion, String[] params) throws BDException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("AnotacionRegistroManager.create");

        AnotacionRegistroDAO.getInstance().create(anotacionRegistroVO, conexion, params);
    }

    public void delete(AnotacionRegistroValueObject anotacionRegistroVO, Connection conexion) throws BDException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("AnotacionRegistroManager.delete");

        AnotacionRegistroDAO.getInstance().delete(anotacionRegistroVO, conexion);
    }

    public void deleteByRegistro(AnotacionRegistroValueObject anotacionRegistroVO, Connection conexion) throws BDException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("AnotacionRegistroManager.deleteByRegistro");

        AnotacionRegistroDAO.getInstance().deleteByRegistro(anotacionRegistroVO, conexion);
    }

    public void modify(AnotacionRegistroValueObject anotacionRegistroVO, Connection conexion) throws BDException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("AnotacionRegistroManager.modify");

        AnotacionRegistroDAO.getInstance().modify(anotacionRegistroVO, conexion);
    }

    public Vector findAll(String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("AnotacionRegistroManager.findAll");
        Vector anotaciones;
        try{
            anotaciones = AnotacionRegistroDAO.getInstance().findAll(params);
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
        m_Log.info("AnotacionRegistroManager.findByRegistro");
        Vector anotaciones;
        try{
            anotaciones = AnotacionRegistroDAO.getInstance().findByRegistro(tipoRegistro, codigoSubseccion,
                    anho, numero, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            anotaciones = new Vector();
        }
        return anotaciones;
    }
}