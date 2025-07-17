package es.altia.agora.business.usuariosforms.persistence;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.agora.business.usuariosforms.UsuariosFormsValueObject;
import es.altia.agora.business.usuariosforms.UsuariosFormsPermisosVO;
import es.altia.agora.business.usuariosforms.persistence.UsuariosFormsPermisosManager;

import es.altia.agora.business.usuariosforms.persistence.manual.UsuariosFormsDAO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

public class UsuariosFormsManager  {
	
	private static UsuariosFormsManager instance = null;
	protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
	protected static Config m_ConfigError; // Para los mensajes de error localizados
	protected static Log log =
		LogFactory.getLog(UsuariosFormsManager.class.getName());

	protected UsuariosFormsManager() {
		//Queremos usar el fichero de configuración technical
	    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
	    // Queremos tener acceso a los mensajes de error localizados
	    m_ConfigError = ConfigServiceHelper.getConfig("error");
	}

	   public static UsuariosFormsManager getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread)
            // las invocaciones a este metodo
            synchronized (UsuariosFormsManager.class) {
                if (instance == null) {
                    instance = new UsuariosFormsManager();
                }
            }
        }
        return instance;
    }

    public UsuariosFormsValueObject getDatosUsuarios(UsuariosFormsValueObject u, String logUsuario, String codOrg, String[] params) {
        log.debug("getDatosUsuarios");
        UsuariosFormsDAO ufDAO = UsuariosFormsDAO.getInstance();
        UsuariosFormsValueObject ufVO = new UsuariosFormsValueObject();
        try {
            log.debug("Usando persistencia manual");
            ufVO = ufDAO.getDatosUsuario(u, logUsuario, codOrg, params);
            return ufVO;
        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
            return ufVO;
        }
    }

    public Vector getUsuariosForms(String codOrg, String[] params) {
        log.debug("getUsuariosForms");
        UsuariosFormsDAO ufDAO = UsuariosFormsDAO.getInstance();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            resultado = ufDAO.getUsuariosForms(codOrg, params);
            return resultado;
        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }

    public Vector getUnidadOrganicaUsuario(UsuariosFormsValueObject u, String[] params) {
        log.debug("getUnidadOrganicaUsuario");
        UsuariosFormsDAO ufDAO = UsuariosFormsDAO.getInstance();
        Vector lista = new Vector();
        try {
            log.debug("Usando persistencia manual");
            lista.addElement(ufDAO.getUnidadOrganicaUsuario(u, params));
            return lista;
        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
            return lista;
        }
    }

    public Vector getListaPerfiles(String[] params) {
        UsuariosFormsDAO ufDAO = new UsuariosFormsDAO();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            resultado = ufDAO.getListaPerfiles(params);
            return resultado;
        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }

    public Vector getListaCatProfesionales(String[] params) {
        UsuariosFormsDAO ufDAO = new UsuariosFormsDAO();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            resultado = ufDAO.getListaCatProfesionales(params);
            return resultado;
        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }

    public Vector getListaUors(String[] params) {
        UsuariosFormsDAO ufDAO = new UsuariosFormsDAO();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            resultado = ufDAO.getListaUors(params);
            return resultado;
        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }

    public int eliminarUsuario(String codUsuario, String[] params) {
        log.debug("eliminarUsuario");
        UsuariosFormsDAO ufDAO = UsuariosFormsDAO.getInstance();
        int resultado = 0;
        int resultadoPermisos = 0;

        try {

            UsuariosFormsPermisosManager usuPermisosDAO = UsuariosFormsPermisosManager.getInstance();
            resultadoPermisos = usuPermisosDAO.eliminarPermisosUsuario(codUsuario, params);
            log.debug(" resultado eliminarPermisosUsuario = " + resultadoPermisos);

            log.debug("Usando persistencia manual");
            resultado = ufDAO.eliminarUsuario(codUsuario, params);

            if (resultadoPermisos == -1) {
                resultado = resultadoPermisos;
            }

        } catch (Exception ce) {
            resultado = -1;
            log.error("JDBC Technical problem " + ce.getMessage());

        } finally {
            return resultado;
        }
    }

    public int modificarUsuario(UsuariosFormsValueObject u, String[] params) {
        log.debug("modificarUsuarioLocal");
        UsuariosFormsDAO ufDAO = UsuariosFormsDAO.getInstance();
        int resultado = 0;
        try {
            log.debug("Usando persistencia manual");
            resultado = ufDAO.modificarUsuario(u, params);

            // grabamos los permisos del usuario
            if (resultado != -1) {

                Vector listaPermisosUsuario = u.getListaUORs();
                UsuariosFormsPermisosManager usuPermisosDAO = UsuariosFormsPermisosManager.getInstance();

                // al modificar un usuario, primero borraremos todos los permisos que tiene y insertaremos los nuevos permisos
                int resultadoEliminarPermisos = usuPermisosDAO.eliminarPermisosUsuario(u.getLogin(), params);
                log.debug(" resultado eliminarPermisosUsuario = " + resultadoEliminarPermisos);

                for (int i = 0; i < listaPermisosUsuario.size(); i++) {
                    UsuariosFormsPermisosVO usuPermisosVO = (UsuariosFormsPermisosVO) listaPermisosUsuario.elementAt(i);
                    usuPermisosDAO.insertarPermisoUsuario(usuPermisosVO, params);
                }
            }

        } catch (Exception ce) {
            resultado = -1;
            log.error("JDBC Technical problem " + ce.getMessage());
         
        }finally{
            return resultado;
        }
    }

    public int insertarUsuario(UsuariosFormsValueObject u, String[] params) {
        log.debug("insertarUsuario");
        UsuariosFormsDAO ufDAO = UsuariosFormsDAO.getInstance();

        int resultado = 0;

        try {
            log.debug("Usando persistencia manual");
            resultado = ufDAO.insertarUsuario(u, params);

            // insertamos los permisos asociados al usuario
            if (resultado != -1) {
                UsuariosFormsPermisosManager usuPermisosDAO = UsuariosFormsPermisosManager.getInstance();
                Vector listaPermisosUsuario = u.getListaUORs();

                for (int i = 0; i < listaPermisosUsuario.size(); i++) {
                    UsuariosFormsPermisosVO usuPermisosVO = (UsuariosFormsPermisosVO) listaPermisosUsuario.elementAt(i);
                    usuPermisosDAO.insertarPermisoUsuario(usuPermisosVO, params);
                }
            }
           
        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
           
        }finally{
            return resultado;
        }
    }


}