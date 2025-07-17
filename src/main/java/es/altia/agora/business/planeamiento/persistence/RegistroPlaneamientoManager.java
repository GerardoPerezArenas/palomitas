package es.altia.agora.business.planeamiento.persistence;

import es.altia.agora.business.planeamiento.*;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.exceptions.InternalErrorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

public class RegistroPlaneamientoManager {

    // Mi propia instancia usada en el metodo getInstance.
    private static RegistroPlaneamientoManager instance = null;

    // Para el fichero de configuracion technical.
    protected static Config m_ConfigTechnical;

    protected static Log m_Log =
            LogFactory.getLog(RegistroPlaneamientoManager.class.getName());

    protected RegistroPlaneamientoManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    }


    public static RegistroPlaneamientoManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(RegistroPlaneamientoManager.class) {
                if (instance == null)
                    instance = new RegistroPlaneamientoManager();
            }
        }
        return instance;
    }

    public Integer create(RegistroValueObject registroVO, Collection anotacionesVO, Collection promotoresVO,
                          String[] params) throws InternalErrorException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("RegistroPlaneamientoManager.create");

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Integer numero = null;
        try{
            //m_Log.info("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            conexion.setAutoCommit(false) ;

            //Generamos un nuevo numero de registro
            ContadorRegistroValueObject contadorVO = new ContadorRegistroValueObject(registroVO.getTipoRegistro(),
                    registroVO.getCodigoSubseccion(), null, registroVO.getAnho());
            numero = ContadorRegistroManager.getInstance().create(contadorVO, conexion, params);

            //Insertamos el nuevo registro
            registroVO.setNumero(numero);
            registroVO.setNumeroRegistro(registroVO.getAnho() + "/" + numero.toString());
            RegistroManager.getInstance().create(registroVO, conexion);

            //Insertamos las posibles anotaciones accesorias
            Iterator anotacionesVOIt = anotacionesVO.iterator();
            AnotacionRegistroValueObject anotacionVO = null;
            while (anotacionesVOIt.hasNext()) {
                anotacionVO = (AnotacionRegistroValueObject) anotacionesVOIt.next();
                anotacionVO.setNumero(numero);
                AnotacionRegistroManager.getInstance().create(anotacionVO, conexion, params);
            }

            //Insertamos los posibles promotores
            Iterator promotoresVOIt = promotoresVO.iterator();
            PromotorValueObject promotorVO = null;
            while (promotoresVOIt.hasNext()) {
                promotorVO = (PromotorValueObject) promotoresVOIt.next();
                promotorVO.setNumero(numero);
                PromotorManager.getInstance().create(promotorVO, conexion);
            }

            conexion.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            try {
                abd.rollBack(conexion);
                throw new InternalErrorException(ex);
            } catch (BDException e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
                throw new InternalErrorException(ex);
            }
        } finally {
            if (conexion != null)
                try{
                    conexion.close();
                } catch(SQLException ex) {
                    ex.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException: " + ex.getMessage()) ;
                    throw new InternalErrorException(ex);
                }
        }
        return numero;
    }

    public void modify(RegistroValueObject registroVO, Collection anotacionesVO, Collection anotacionesRectificacionVO,
                       Collection promotoresVO, String[] params) throws InternalErrorException  {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("RegistroPlaneamientoManager.modify");

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try{
            //m_Log.info("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            conexion.setAutoCommit(false) ;

            RegistroValueObject registro = RegistroManager.getInstance().findByPrimaryKey(registroVO, params);
            if (registro == null) {
                throw new BDException("El registro no existe");
            }
            //Insertamos el nuevo registro
            RegistroManager.getInstance().modify(registroVO, conexion);

            //Insertamos las posibles anotaciones accesorias
            Iterator anotacionesVOIt = anotacionesVO.iterator();
            AnotacionRegistroValueObject anotacionVO = null;
            while (anotacionesVOIt.hasNext()) {
                anotacionVO = (AnotacionRegistroValueObject) anotacionesVOIt.next();
                AnotacionRegistroManager.getInstance().deleteByRegistro(anotacionVO, conexion);
                AnotacionRegistroManager.getInstance().create(anotacionVO, conexion, params);
            }

            //Insertamos las posibles anotaciones de rectificacion
            Iterator anotacionesRectificacionVOIt = anotacionesRectificacionVO.iterator();
            AnotacionRectificacionRegistroValueObject anotacionRectificacionVO = null;
            while (anotacionesRectificacionVOIt.hasNext()) {
                anotacionRectificacionVO = (AnotacionRectificacionRegistroValueObject) anotacionesRectificacionVOIt.next();
                AnotacionRectificacionRegistroManager.getInstance().deleteByRegistro(anotacionRectificacionVO, conexion);
                AnotacionRectificacionRegistroManager.getInstance().create(anotacionRectificacionVO, conexion, params);
            }

            //Insertamos los posibles promotores
            Iterator promotoresVOIt = promotoresVO.iterator();
            PromotorValueObject promotorVO = null;
            if (promotoresVOIt.hasNext()) {
                promotorVO = (PromotorValueObject) promotoresVOIt.next();
                promotorVO.setNumero(registroVO.getNumero());
                PromotorManager.getInstance().deleteByRegistro(promotorVO, conexion);//Borramos todos los promotores existentes para ese registro
                PromotorManager.getInstance().create(promotorVO, conexion);
            }
            while (promotoresVOIt.hasNext()) {
                promotorVO = (PromotorValueObject) promotoresVOIt.next();
                promotorVO.setNumero(registroVO.getNumero());
                PromotorManager.getInstance().create(promotorVO, conexion);
            }

            conexion.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            try {
                abd.rollBack(conexion);
                throw new InternalErrorException(ex);
            } catch (BDException e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
                throw new InternalErrorException(ex);
            }
        } finally {
            if (conexion != null)
                try{
                    conexion.close();
                } catch(SQLException ex) {
                    ex.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException: " + ex.getMessage()) ;
                    throw new InternalErrorException(ex);
                }
        }
    }
}