
package es.altia.agora.business.sge.persistence;
import es.altia.agora.business.sge.RolVO;
import es.altia.agora.business.sge.persistence.manual.RolesDAO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import org.apache.log4j.Logger;





/**
 * 
 */
public class RolManager {
    
    //Logger
    private static Logger log = Logger.getLogger(RolManager.class);
    
    //Instancia
    private static RolManager instance = null;
    
    /**
     * Devuelve una instancia de MeLanbide03Manager, si no existe la crea.
     * @return 
     */
    public static RolManager getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(RolManager.class){
                if(instance == null){
                    instance = new RolManager();
                }//if(instance == null)
            }//synchronized(MeLanbide03Manager.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    
    /**
     * Recupera, para un procedimiento,
     * los posibles Roles que pueden tener
     * los interesados de un expediente de este
     * tipo de procedimiento.
     * @param adaptador
     * @param codProcedimiento
     * @param codOrg codigo de organización (o código de municipio)
     * @return ArrayList<RolVO>
     * @throws Exception 
     */
    
 
    public ArrayList<RolVO> dameRolesDeProcedimiento (String[] params, String codProcedimiento,int codOrg) throws Exception{
        
        if(log.isDebugEnabled()) log.debug("RolManager.dameRolesDeProcedimiento() : BEGIN");
        ArrayList<RolVO> listaRoles = new ArrayList<RolVO>();
        Connection con = null;
        AdaptadorSQLBD adaptador = null;
        try{
            adaptador = new AdaptadorSQLBD(params);
            if (log.isDebugEnabled()) log.debug("A por la conexion....");
            con = adaptador.getConnection();
            RolesDAO rolesDAO = RolesDAO.getInstance();
            listaRoles = rolesDAO.dameRolesDeProcedimiento(con,codProcedimiento,codOrg);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando los roles del procedimiento ", e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando los roles del procedimiento ",ex);
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("RolManager.dameRolesDeProcedimiento() : END. Tamanho del resultado devuelto:"+ listaRoles.size());
        return listaRoles;
    }//getCentros
    
  
   
}
