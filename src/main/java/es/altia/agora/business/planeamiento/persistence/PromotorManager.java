package es.altia.agora.business.planeamiento.persistence;

import java.util.Vector;
import java.util.Collection;
import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.agora.business.planeamiento.PromotorValueObject;
import es.altia.agora.business.planeamiento.persistence.manual.PromotorDAO;
import es.altia.util.conexion.BDException;

public class PromotorManager {

    // Mi propia instancia usada en el metodo getInstance.
    private static PromotorManager instance = null;

    // Para el fichero de configuracion technical.
    protected static Config m_ConfigTechnical;

    protected static Log m_Log =
            LogFactory.getLog(PromotorManager.class.getName());

    protected PromotorManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    }


    public static PromotorManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(PromotorManager.class) {
                if (instance == null)
                    instance = new PromotorManager();
            }
        }
        return instance;
    }

    public void create(PromotorValueObject promotorVO, Connection conexion) throws BDException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("PromotorManager.create");

        PromotorDAO.getInstance().create(promotorVO, conexion);
    }

    public void delete(PromotorValueObject promotorVO, Connection conexion) throws BDException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("PromotorManager.delete");

        PromotorDAO.getInstance().delete(promotorVO, conexion);
    }

    public void deleteByRegistro(PromotorValueObject promotorVO, Connection conexion) throws BDException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("PromotorManager.deleteByRegistro");

        PromotorDAO.getInstance().deleteByRegistro(promotorVO, conexion);
    }

    public Vector findAll(String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("PromotorManager.findAll");
        Vector promotores;
        try{
            promotores = PromotorDAO.getInstance().findAll(params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            promotores = new Vector();
        }
        return promotores;
    }

    public Vector findByRegistro(Character tipoRegistro, String codigoSubseccion, String anho,
                                 Integer numero, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("PromotorManager.findByRegistro");
        Vector promotores;
        try{
            promotores = PromotorDAO.getInstance().findByRegistro(tipoRegistro, codigoSubseccion,
                    anho, numero, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            promotores = new Vector();
        }
        return promotores;
    }

    public Vector findByPromotores(Collection codigosPromotor, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("PromotorManager.findByPromotores");
        Vector promotores;
        try{
            promotores = PromotorDAO.getInstance().findByPromotores(codigosPromotor, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            promotores = new Vector();
        }
        return promotores;
    }
}