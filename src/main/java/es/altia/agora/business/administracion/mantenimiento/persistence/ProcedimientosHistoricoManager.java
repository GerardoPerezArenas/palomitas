/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.business.administracion.mantenimiento.persistence;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.historico.expediente.vo.ProcedimientoHistoricoVO;
import es.altia.flexia.historico.expedientes.dao.ExpedienteEnvioHistoricoDAO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


/**
 *
 * @author paz.rodriguez
 */
public class ProcedimientosHistoricoManager {
    private static ProcedimientosHistoricoManager instance = null;
    protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log log =LogFactory.getLog(ProcedimientosHistoricoManager.class.getName());


protected ProcedimientosHistoricoManager() {
        // Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
}

public static ProcedimientosHistoricoManager getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread)
            // las invocaciones a este metodo
            synchronized (ProcedimientosHistoricoManager.class) {
                if (instance == null) {
                    instance = new ProcedimientosHistoricoManager();
                }
            }
        }
        return instance;
}

public ArrayList<ProcedimientoHistoricoVO> getListaProcedimientos(String[] params, int codOrganizacion) {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        ArrayList<ProcedimientoHistoricoVO> resultado = new ArrayList<ProcedimientoHistoricoVO>();
                
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            ExpedienteEnvioHistoricoDAO expEnvHistDAO = ExpedienteEnvioHistoricoDAO.getInstance();
            log.debug("Usando persistencia manual");
            resultado = expEnvHistDAO.getProcedimientosOrganizacion(con,codOrganizacion);
        }catch(BDException e){
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
        }catch(SQLException e){
            log.error("Error sql:  " + e.getMessage());
        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
        } finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }finally{
                return resultado;
            }
        }        
}

public String grabarProcedimiento(String[] params, 
        ProcedimientoHistoricoVO procedimientoHistorico) {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        String resultado = "";

        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            ExpedienteEnvioHistoricoDAO expEnvHistDAO = ExpedienteEnvioHistoricoDAO.getInstance();
            log.debug("Usando persistencia manual");
            resultado = expEnvHistDAO.grabarProcedimientoHistorico(con,procedimientoHistorico);
        }catch(BDException e){
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
            resultado = "msgErrConexionBD";
        }catch(SQLException e){
            log.error("Error sql:  " + e.getMessage());
            resultado = "msgErrGrabarDatos";
        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
            resultado = "msjErrorInterno";
        } finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }finally{
                return resultado;
            }
        }        
}

public String modificarProcedimiento(String[] params, 
        ProcedimientoHistoricoVO procedimientoHistorico) {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        String resultado = "";
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            ExpedienteEnvioHistoricoDAO expEnvHistDAO = ExpedienteEnvioHistoricoDAO.getInstance();
            log.debug("Usando persistencia manual");
            expEnvHistDAO.modificarProcedimientoHistorico(con,procedimientoHistorico);
        }catch(BDException e){
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
            resultado = "msgErrConexionBD";
        }catch(SQLException e){
            log.error("Error sql:  " + e.getMessage());
            resultado = "msgErrGrabarDatos";
        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
            resultado = "msjErrorInterno";
        } finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }finally{
                return resultado;
            }
        }        
}

public String eliminarProcedimiento(String[] params, 
        ProcedimientoHistoricoVO procedimientoHistorico) {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        String resultado = "";
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            ExpedienteEnvioHistoricoDAO expEnvHistDAO = ExpedienteEnvioHistoricoDAO.getInstance();
            log.debug("Usando persistencia manual");
            expEnvHistDAO.eliminarProcedimientoHistorico(con,procedimientoHistorico);
        }catch(BDException e){
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
            resultado = "msgErrConexionBD";
        }catch(SQLException e){
            log.error("Error sql:  " + e.getMessage());
            resultado = "msgErrGrabarDatos";
        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
            resultado = "msjErrorInterno";
        } finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }finally{
                return resultado;
            }
        }        
    }
}
