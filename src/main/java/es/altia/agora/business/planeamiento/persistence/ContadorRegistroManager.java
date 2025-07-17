package es.altia.agora.business.planeamiento.persistence;

import es.altia.agora.business.planeamiento.ContadorRegistroValueObject;
import es.altia.agora.business.planeamiento.persistence.manual.ContadorRegistroDAO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.util.Vector;

public class ContadorRegistroManager {

    // Mi propia instancia usada en el metodo getInstance.
    private static ContadorRegistroManager instance = null;

    // Para el fichero de configuracion technical.
    protected static Config m_ConfigTechnical;

    protected static Log m_Log =
            LogFactory.getLog(ContadorRegistroManager.class.getName());

    protected ContadorRegistroManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    }


    public static ContadorRegistroManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(ContadorRegistroManager.class) {
                if (instance == null)
                    instance = new ContadorRegistroManager();
            }
        }
        return instance;
    }

    public Integer create(ContadorRegistroValueObject contadorRegistroVO, Connection conexion, String[] params) throws BDException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("ContadorRegistroManager.create");

        Integer contador = new Integer(-1);
        contador = ContadorRegistroDAO.getInstance().create(contadorRegistroVO, conexion, params);
        return contador;
    }

    public void delete(ContadorRegistroValueObject contadorRegistroVO, Connection conexion) throws BDException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("ContadorRegistroManager.delete");

       ContadorRegistroDAO.getInstance().delete(contadorRegistroVO, conexion);
    }

    public void modify(ContadorRegistroValueObject contadorRegistroVO, Connection conexion) throws BDException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("ContadorRegistroManager.modify");

            ContadorRegistroDAO.getInstance().modify(contadorRegistroVO, conexion);
    }

    public Vector findAll(String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("ContadorRegistroManager.findAll");
        Vector subsecciones;
        try{
            subsecciones = ContadorRegistroDAO.getInstance().findAll(params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            subsecciones = new Vector();
        }
        return subsecciones;
    }
}