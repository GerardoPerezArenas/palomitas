/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.business.usuariosforms.persistence;

import es.altia.agora.business.usuariosforms.UsuariosFormsPermisosVO;
import es.altia.agora.business.usuariosforms.persistence.manual.UsuariosFormsPermisosDAO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author manuel.bahamonde
 */
public class UsuariosFormsPermisosManager {

    private static UsuariosFormsPermisosManager instance = null;
    protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log log =
            LogFactory.getLog(UsuariosFormsPermisosManager.class.getName());

    protected UsuariosFormsPermisosManager() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }

    public static UsuariosFormsPermisosManager getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread)
            // las invocaciones a este metodo
            synchronized (UsuariosFormsPermisosManager.class) {
                if (instance == null) {
                    instance = new UsuariosFormsPermisosManager();
                }
            }
        }
        return instance;
    }


    public int insertarPermisoUsuario(UsuariosFormsPermisosVO usuariosFormsPermisosVO, String[] params) {
        log.debug("insertarPermisoUsuario");
        UsuariosFormsPermisosDAO usuPermisosDAO = UsuariosFormsPermisosDAO.getInstance();
        int resultado = 0;
        try {
            log.debug("Usando persistencia manual");
            resultado = usuPermisosDAO.insertarPermisoUsuario(usuariosFormsPermisosVO, params);

        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
        } finally {
            return resultado;
        }

    }


    public int eliminarPermisosUsuario(String loginUsuario, String[] params) {
        log.debug("eliminarPermisosUsuario");
        UsuariosFormsPermisosDAO usuPermisosDAO = UsuariosFormsPermisosDAO.getInstance();
        int resultado = 0;
        try {
            log.debug("Usando persistencia manual");
            resultado = usuPermisosDAO.eliminarPermisosUsuario(loginUsuario, params);
            
        } catch (Exception ce) {
            resultado = -1;
            log.error("JDBC Technical problem " + ce.getMessage());
        }finally{
            return resultado;
        }
    }

    public Vector obtenerListaPermisosUsuario(String loginUsuario, String[] params) {
        log.debug("obtenerListaPermisosUsuario");
        UsuariosFormsPermisosDAO usuPermisosDAO = UsuariosFormsPermisosDAO.getInstance();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            resultado = usuPermisosDAO.obtenerListaPermisosUsuario(loginUsuario, params);
   
        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
        }finally{
            return resultado;
        }
    }



    public Vector obtenerListaPermisosUsuarioEnviarFormulario(String codFormulario, String loginUsuario, String[] params) {
        log.debug("obtenerListaPermisosUsuarioEnviarFormulario");
        UsuariosFormsPermisosDAO usuPermisosDAO = UsuariosFormsPermisosDAO.getInstance();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            resultado = usuPermisosDAO.obtenerListaPermisosUsuarioEnvioFormulario(codFormulario, loginUsuario, params);

        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
        } finally {
            return resultado;
        }
    }
}
