package es.altia.agora.business.planeamiento.persistence;

import java.util.Vector;
import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.agora.business.planeamiento.RegistroValueObject;
import es.altia.agora.business.planeamiento.persistence.manual.RegistroDAO;
import es.altia.util.conexion.BDException;

public class RegistroManager {

    // Mi propia instancia usada en el metodo getInstance.
    private static RegistroManager instance = null;

    // Para el fichero de configuracion technical.
    protected static Config m_ConfigTechnical;

    protected static Log m_Log =
            LogFactory.getLog(RegistroManager.class.getName());

    protected RegistroManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    }


    public static RegistroManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(RegistroManager.class) {
                if (instance == null)
                    instance = new RegistroManager();
            }
        }
        return instance;
    }

    public void create(RegistroValueObject registroVO, Connection conexion)  throws BDException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("RegistroManager.create");

        RegistroDAO.getInstance().create(registroVO, conexion);
    }

    public void delete(RegistroValueObject registroVO, Connection conexion) throws BDException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("RegistroManager.delete");

        RegistroDAO.getInstance().delete(registroVO, conexion);
    }

    public void modify(RegistroValueObject registroVO, Connection conexion) throws BDException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("RegistroManager.modify");

        RegistroDAO.getInstance().modify(registroVO, conexion);
    }

    public Vector findAll(String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("RegistroManager.findAll");
        Vector registros;
        try{
            registros = RegistroDAO.getInstance().findAll(params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            registros = new Vector();
        }
        return registros;
    }

    public Vector findByProcedimiento(String codigoProcedimiento, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("RegistroManager.findByProcedimiento");
        Vector registros;
        try{
            registros = RegistroDAO.getInstance().findByProcedimiento(codigoProcedimiento, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            registros = new Vector();
        }
        return registros;
    }

    public Vector findByAmbito(String codigoAmbito, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("RegistroManager.findByAmbito");
        Vector registros;
        try{
            registros = RegistroDAO.getInstance().findByAmbito(codigoAmbito, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            registros = new Vector();
        }
        return registros;
    }

    public Vector findByOrganoAprobacion(String codigoOrganoAprobacion, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("RegistroManager.findByOrganoAprobacion");
        Vector registros;
        try{
            registros = RegistroDAO.getInstance().findByOrganoAprobacion(codigoOrganoAprobacion, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            registros = new Vector();
        }
        return registros;
    }

    public Vector findByCatalogacion(String codigoCatalogacion, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("RegistroManager.findByCatalogacion");
        Vector registros;
        try{
            registros = RegistroDAO.getInstance().findByCatalogacion(codigoCatalogacion, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            registros = new Vector();
        }
        return registros;
    }

    public Vector findByGradoProteccion(String codigoGradoProteccion, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("RegistroManager.findByGradoProteccion");
        Vector registros;
        try{
            registros = RegistroDAO.getInstance().findByGradoProteccion(codigoGradoProteccion, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            registros = new Vector();
        }
        return registros;
    }

    public Vector findByRelacionBien(String codigoRelacionBien, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("RegistroManager.findByRelacionBien");
        Vector registros;
        try{
            registros = RegistroDAO.getInstance().findByRelacionBien(codigoRelacionBien, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            registros = new Vector();
        }
        return registros;
    }

    public Vector findBySubseccion(Character tipoRegistro, String codigoSubseccion, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("RegistroManager.findBySubseccion");
        Vector registros;
        try{
            registros = RegistroDAO.getInstance().findBySubseccion(tipoRegistro, codigoSubseccion, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            registros = new Vector();
        }
        return registros;
    }

    public Vector findByTipo(Character tipoRegistro, String codigoSubseccion, String codigoTipo, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("RegistroManager.findByTipo");
        Vector registros;
        try{
            registros = RegistroDAO.getInstance().findByTipo(tipoRegistro, codigoSubseccion, codigoTipo, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            registros = new Vector();
        }
        return registros;
    }

    public RegistroValueObject findByPrimaryKey(RegistroValueObject registroVO, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("RegistroManager.findByPrimaryKey");
        RegistroValueObject registro = null;
        try{
            registro = RegistroDAO.getInstance().findByPrimaryKey(registroVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
        }
        return registro;
    }

    public Vector findByAll(RegistroValueObject registroVO, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("RegistroManager.findByAll");
        Vector registros;
        try{
            registros = RegistroDAO.getInstance().findByAll(registroVO, params);
        }catch (Exception te) {
            te.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            registros = new Vector();
        }
        return registros;
    }
}