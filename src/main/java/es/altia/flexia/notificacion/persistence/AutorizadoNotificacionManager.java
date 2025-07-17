/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.notificacion.persistence;

// PAQUETES	IMPORTADOS

import es.altia.common.exception.TechnicalException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import es.altia.flexia.notificacion.vo.*;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.util.ArrayList;


public class AutorizadoNotificacionManager {


     private static AutorizadoNotificacionManager instance =	null;
    protected static Config m_ConfigTechnical; //	Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log =
            LogFactory.getLog(AutorizadoNotificacionManager.class.getName());


    protected AutorizadoNotificacionManager() {
		// Queremos usar el	fichero de configuración technical
		m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
		// Queremos tener acceso a los mensajes de error localizados
		m_ConfigError	= ConfigServiceHelper.getConfig("error");
	}

    public static AutorizadoNotificacionManager getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        synchronized (AutorizadoNotificacionManager.class) {
            if (instance == null) {
                instance = new AutorizadoNotificacionManager();
            }

        }
        return instance;
    }


    //Elimina un determinado autorizado que está asociado a una notificación
    public boolean eliminarAutorizado(AutorizadoNotificacionVO autorizado, String[] params)
    {
        //queremos estar informados de cuando este metodo es ejecutado
	m_Log.debug("eliminarAutorizado");

	boolean res=false;

	try{
            res = AutorizadoNotificacionDAO.getInstance().eliminarAutorizado(autorizado, params);

	/*}catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());*/
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return res;
	}
    }
    
    //Da de alta un registro en la tabla AUTORIZADO_NOTIFICACION
    public boolean insertarAutorizado(AutorizadoNotificacionVO autorizado,Connection con)
    {
        //queremos estar informados de cuando este metodo es ejecutado
	m_Log.info("insertarAutorizado");
	boolean res=false;
	try{
            res = AutorizadoNotificacionDAO.getInstance().insertarAutorizado(autorizado, con);
        }catch (Exception e) {
            m_Log.error("Error al insertar autorizadp para notificacion - manager " + e.getMessage(), e);
            e.printStackTrace();
	}finally{
            return res;
	}
    }



    //Comprueba si hay interesados asociados a un determinado expediente que admiten notificaciones electrónicas
    public boolean existenInteresadosNotificacionElectronica(NotificacionVO notificacion, String[] params)
    {
        //queremos estar informados de cuando este metodo es ejecutado
	m_Log.debug("existenInteresadosNotificacionElectronica");

	boolean res=false;

	try{
            res = AutorizadoNotificacionDAO.getInstance().existenInteresadosNotificacionElectronica(notificacion,params);


	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return res;
	}
    }



    public ArrayList<AutorizadoNotificacionVO> getInteresadosExpediente(String numExpediente,int codigoNotificacion, String[] params) throws TechnicalException {
        ArrayList<AutorizadoNotificacionVO> autorizados = new ArrayList<AutorizadoNotificacionVO>();
        AdaptadorSQLBD adapt = null;
        Connection con = null;

        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            autorizados = AutorizadoNotificacionDAO.getInstance().getInteresadosExpediente(numExpediente, codigoNotificacion, params[0], con);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return autorizados;
    }


}
