// NOMBRE DEL PAQUETE
package es.altia.agora.business.administracion.mantenimiento.persistence;

// PAQUETES IMPORTADOS

import static es.altia.agora.business.administracion.mantenimiento.persistence.CamposDesplegablesExternoManager.log;
import java.util.Vector;
import java.sql.SQLException;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.CamposDesplegablesDAO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.CamposDesplegablesExternoDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class CamposDesplegablesManager {
    private static CamposDesplegablesManager instance = null;
    protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log log =
            LogFactory.getLog(CamposDesplegablesManager.class.getName());


    protected CamposDesplegablesManager() {
        // Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }

    public static CamposDesplegablesManager getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread)
            // las invocaciones a este metodo
            synchronized (CamposDesplegablesManager.class) {
                if (instance == null) {
                    instance = new CamposDesplegablesManager();
                }
            }
        }
        return instance;
    }

    public Vector getListaCampoDesplegable(String[] params) {
        CamposDesplegablesDAO CampoDesplegableDAO = CamposDesplegablesDAO.getInstance();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            resultado = CampoDesplegableDAO.getListaCampoDesplegables(params);
            return resultado;
        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }

    public Vector eliminarCampoDesplegable(String codigo, String[] params) {
        CamposDesplegablesDAO CampoDesplegableDAO = CamposDesplegablesDAO.getInstance();
        Vector resultado = new Vector();
        try {
            resultado = CampoDesplegableDAO.eliminarCampoDesplegable(codigo, params);
        } catch (Exception e) {
            log.error("Excepción capturada: " + e.toString());
        }
        return resultado;
    }

    public Vector modificarCampoDesplegable(GeneralValueObject gVO, String[] params) {
        CamposDesplegablesDAO CampoDesplegableDAO = CamposDesplegablesDAO.getInstance();
        Vector resultado = new Vector(); 
        try {
            resultado = CampoDesplegableDAO.modificarCampoDesplegable(gVO, params);
        } catch (Exception e) {
            log.error("Excepcion capturada: " + e.toString());
        }
        return resultado;
    }

    public Vector altaCampoDesplegable(GeneralValueObject gVO, String[] params) {
        CamposDesplegablesDAO CampoDesplegableDAO = CamposDesplegablesDAO.getInstance();
        Vector resultado = new Vector();
        try {
            resultado = CampoDesplegableDAO.altaCampoDesplegable(gVO, params);
        } catch (Exception e) {
            log.error("Excepción capturada: " + e.toString());
        }
        return resultado;
    }

    public Vector getListaValoresCampoDesplegable(GeneralValueObject gVO, String[] params) {
        CamposDesplegablesDAO CampoDesplegableDAO = CamposDesplegablesDAO.getInstance();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            resultado = CampoDesplegableDAO.getListaValoresCampoDesplegables(gVO, params);
            return resultado;
        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }

    public Vector altaValorCampoDesplegable(GeneralValueObject gVO, String[] params) {
        CamposDesplegablesDAO CampoDesplegableDAO = CamposDesplegablesDAO.getInstance();
        Vector resultado = new Vector();
        try {
            resultado = CampoDesplegableDAO.altaValorCampoDesplegable(gVO, params);
        } catch (Exception e) {
            log.error("Excepción capturada: " + e.toString());
        }
        return resultado;
    }

    public Vector modificarValorCampoDesplegable(GeneralValueObject gVO, String[] params) {
        CamposDesplegablesDAO CampoDesplegableDAO = CamposDesplegablesDAO.getInstance();
        Vector resultado = new Vector();
        try {
            resultado = CampoDesplegableDAO.modificarValorCampoDesplegable(gVO, params);
        } catch (Exception e) {
            log.error("Excepcion capturada: " + e.toString());
        }
        return resultado;
    }

    public Vector eliminarValorCampoDesplegable(GeneralValueObject gVO, String[] params) {
        CamposDesplegablesDAO CampoDesplegableDAO = CamposDesplegablesDAO.getInstance();
        Vector resultado = new Vector();
        try {
            resultado = CampoDesplegableDAO.eliminarValorCampoDesplegable(gVO, params);
        } catch (Exception e) {
            log.error("Excepción capturada: " + e.toString());
        }
        return resultado;
    }
    
    public Vector recuperarValorCampoDesplegable(GeneralValueObject gVO, String[] params){
        CamposDesplegablesDAO CampoDesplegableDAO = CamposDesplegablesDAO.getInstance();
        Vector resultado = new Vector();
        try{
            resultado = CampoDesplegableDAO.recuperarValorCampoDesplegable(gVO, params);
        }catch(Exception e){
            log.error("Excepción capturada: "+ e.toString());
        }
        return resultado;
    }
    /**
     * Metodo que llama al DAO correspondiente para almacenar en BBDD un conjunto de campos desplegables,
     * si no están almacenados ya.
     * @param   camposDesplegables  Vector con la descripción de los campos deplegables. Cada 4 posiciones representa
     * un elemento de un campo desplegable, siendo la posicion i el codigo del campo desplegable, i + 1 el nombre del
     * campo desplegable, i + 2 el codigo del valor del campo desplegable e i + 3 la descrición del elemento del campo
     * desplegable.
     * @param   params              Parametros de conexion a la BBDD.
     * @throws TechnicalException Si hay algún problema en el DAO.
     */
    public void anhadirConjuntoDesplegables(Vector camposDesplegables, String[] params)
            throws TechnicalException {

        log.debug("CamposDesplegablesManager --> Inicio anhadirConjuntoDesplegables");
        try {
            CamposDesplegablesDAO.getInstance().anhadirConjuntoDesplegables(camposDesplegables, params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new TechnicalException(e.getMessage(), e);
        }

        log.debug("CamposDesplegablesManager --> Fin anhadirConjuntoDesplegables");
    }
    
     
     
   /**
     * Comprueba si se puede eliminar un campo desplegable
     * @param codigoCampo
     * @param params
     * @return 0 -> Se puede eliminar el campo desplegable externo
     *         1 -> El campo está asignado como campo a nivel de procedimiento
     *         2 -> El campo está asignado como campo a nivel de trámite
     *        -1 -> No se ha podido obtener una conexión a la BBDD
     *        -2 -> Se ha producido un error técnico al eliminar el desplegable
     */
    public int comprobarEliminacionCampoDesplegable(String codigoCampo,String[] params){
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        int salida = -1;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            // Para eliminar el campo desplegable, primero hay que comprobar que no esté asignado
            // como campo suplementario a nivel de expediente o de trámite. Si lo está no se permite
            // realizar el borrado.
            CamposDesplegablesDAO dao = CamposDesplegablesDAO.getInstance();
            
            // Se comprueba si el campo es un campo suplementario del algun procedimiento
            if(dao.estaCampoAsignadoCampoSuplementarioProcedimiento(codigoCampo, con))
                salida= 1;
            else
            if(dao.estaCampoAsignadoCampoSuplementarioTramite(codigoCampo, con))
                salida = 2;       
            else
                salida = 0;
            
        }catch(BDException e){
            salida = -1;
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
        }catch(SQLException e){
            salida = -2;
        }
        finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }        
        return salida;        
    }
    
    
}