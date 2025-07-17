/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.business.administracion.mantenimiento.persistence;
// PAQUETES IMPORTADOS

import java.util.Vector;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.CamposDesplegablesExternoDAO;
import es.altia.agora.business.util.GeneralValueObject; 
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


/**
 *
 * @author paz.rodriguez
 */
public class CamposDesplegablesExternoManager {
    private static CamposDesplegablesExternoManager instance = null;
    protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log log =LogFactory.getLog(CamposDesplegablesExternoManager.class.getName());


protected CamposDesplegablesExternoManager() {
        // Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
}

public static CamposDesplegablesExternoManager getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread)
            // las invocaciones a este metodo
            synchronized (CamposDesplegablesExternoManager.class) {
                if (instance == null) {
                    instance = new CamposDesplegablesExternoManager();
                }
            }
        }
        return instance;
}

public Vector getListaCampoDesplegableExterno(String[] params) {
        CamposDesplegablesExternoDAO CamposDesplegableExternoDAO = CamposDesplegablesExternoDAO.getInstance();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            resultado = CamposDesplegableExternoDAO.getListaCampoDesplegablesExterno(params);
            return resultado;
        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
}

 public Vector altaCampoDesplegableExterno(GeneralValueObject gVO, String[] params) {
        CamposDesplegablesExternoDAO CampoDesplegableExternoDAO = CamposDesplegablesExternoDAO.getInstance();
        Vector resultado = new Vector();
        try {
            resultado = CampoDesplegableExternoDAO.altaCampoDesplegableExterno(gVO, params);
        } catch (Exception e) {
            log.error("Excepción capturada: " + e.toString());
        }
        return resultado;
}

public Vector insertaCampoDesplegableExterno(GeneralValueObject gVO, String[] params) {
        CamposDesplegablesExternoDAO CampoDesplegableExternoDAO = CamposDesplegablesExternoDAO.getInstance();
        Vector resultado=new Vector();
        try {
            resultado = CampoDesplegableExternoDAO.insertaCampoDesplegableExterno(gVO, params);
        } catch (Exception e) {
            log.error("Excepción capturada: " + e.toString());
        }
        return resultado;
}

public int modificaGuardaCampoDesplegableExterno(GeneralValueObject gVO, String[] params) {
        CamposDesplegablesExternoDAO CampoDesplegableExternoDAO = CamposDesplegablesExternoDAO.getInstance();
        int resultado=0;
        try {
            resultado = CampoDesplegableExternoDAO.modificaGuardaCampoDesplegableExterno(gVO, params);
        } catch (Exception e) {
            log.error("Excepción capturada: " + e.toString());
        }
        return resultado;
}

public Vector probarConexion(GeneralValueObject gVO, String[] params) {
        CamposDesplegablesExternoDAO CampoDesplegableExternoDAO = CamposDesplegablesExternoDAO.getInstance();
        Vector resultado = new Vector();
        try {
            resultado = CampoDesplegableExternoDAO.probarConexion(gVO, params);
        } catch (Exception e) {
             log.error("Excepción capturada: " + e.toString());
        }
        return resultado;
}

public Vector getListaValoresCampoDesplegableExterno(GeneralValueObject gVO, String[] params) {
        CamposDesplegablesExternoDAO CampoDesplegableExternoDAO = CamposDesplegablesExternoDAO.getInstance();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            resultado = CampoDesplegableExternoDAO.getListaValoresCampoDesplegablesExterno(gVO, params);
            return resultado;
        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
}

public Vector modificarCampoDesplegableExterno(GeneralValueObject gVO, String[] params) {
        CamposDesplegablesExternoDAO CampoDesplegableExternoDAO = CamposDesplegablesExternoDAO.getInstance();
        Vector resultado = new Vector();
        try {
            resultado = CampoDesplegableExternoDAO.modificarCampoDesplegableExterno(gVO, params);
        } catch (Exception e) {
            log.error("Excepcion capturada: " + e.toString());
        }
        return resultado; 
}
public Vector getListaCampoSupExterno(GeneralValueObject gVO, String[] params) {
        CamposDesplegablesExternoDAO CampoDesplegableExternoDAO = CamposDesplegablesExternoDAO.getInstance();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            resultado = CampoDesplegableExternoDAO.getListaCampoSupDesplegablesExterno(gVO, params);
            return resultado;
        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
}



    /**
     * Elimina un campo desplegable externo
     * @param codigoCampo
     * @param params
     * @return 0 -> Se puede eliminar el campo desplegable externo
     *         1 -> El campo está asignado como campo a nivel de procedimiento
     *         2 -> El campo está asignado como campo a nivel de trámite
     *        -1 -> No se ha podido obtener una conexión a la BBDD
     *        -2 -> Se ha producido un error técnico al eliminar el desplegable
     */
    public int comprobarEliminacionCampoDesplegableExterno(String codigoCampo,String[] params){
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        int salida = -1;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            // Para eliminar el campo desplegable, primero hay que comprobar que no esté asignado
            // como campo suplementario a nivel de expediente o de trámite. Si lo está no se permite
            // realizar el borrado.
            CamposDesplegablesExternoDAO dao = CamposDesplegablesExternoDAO.getInstance();
            
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

    
    /**
     * Elimina definitivamente de BBDD un campo desplegable externo
     * @param codCampo: Código del campo
     * @param con: Conexión a la BBDD
     * @return Un boolean
     */
    
     public boolean eliminarCampoDesplegableExterno(String codCampo,String[] params){
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        boolean exito = false;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            adapt.inicioTransaccion(con);
            
            exito = CamposDesplegablesExternoDAO.getInstance().eliminarCampoDesplegableExterno(codCampo, con);
            
            if(exito)
                adapt.finTransaccion(con);
            else
                adapt.rollBack(con);
            
        }catch(BDException e){
            exito = false;
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
            try{
                adapt.rollBack(con);
                
            }catch(BDException f){
                log.error("Error al  " + f.getMessage());
            }
        }catch(SQLException e){
            exito = false;
            try{
                adapt.rollBack(con);
                
            }catch(BDException f){
                log.error("Error al  " + f.getMessage());
            }
        }
        finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }        
        return exito;             
         
     }

}
