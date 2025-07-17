package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.sge.RolVO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class RolesDAO {

  //Para el fichero de configuracion tecnico.
    protected static Config m_ConfigTechnical;
  //Para los mensajes de error localizados.
    protected static Config m_ConfigError;
  //Para informacion de logs.
    protected static Log log =
          LogFactory.getLog(RolesDAO.class.getName());

    private static RolesDAO instance = null;


  protected RolesDAO() {
    super();
    //Queremos usar el fichero de configuracion techserver
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");

  
  }

    public static RolesDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        synchronized (RolesDAO.class) {
            if (instance == null) {
                instance = new RolesDAO();
            }
        }
        return instance;
    }

 public ArrayList<RolVO> dameRolesDeProcedimiento(Connection con, String codProcedimiento, int codOrg) throws Exception {

        if (log.isDebugEnabled()) {
            log.debug("RolesDAO.dameRolesDeProcedimiento() : BEGIN ");
            log.debug("RolesDAO.dameRolesDeProcedimiento() : codProcedimiento:  "+ codProcedimiento);
            log.debug("RolesDAO.dameRolesDeProcedimiento() : codOrganizacion:  "+ codOrg);
        }

        ArrayList<RolVO> resultado = new ArrayList<RolVO>();
        Statement st = null;
        ResultSet rs = null;

        try {

            String sql = " SELECT * FROM E_ROL"
                    +    " WHERE ROL_PRO='"+codProcedimiento+"'"
                    +    " AND ROL_MUN = " + codOrg;

             if (log.isDebugEnabled()) {
                log.debug("dameRolesDeProcedimiento.sql = " + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()) {
                RolVO rolVO= new RolVO();
                rolVO.setOrganizacion(rs.getInt("ROL_MUN"));
                rolVO.setCodigoProcedimiento(rs.getString("ROL_PRO"));
                rolVO.setCodigo(rs.getInt("ROL_COD"));
                rolVO.setDescripcion(rs.getString("ROL_DES"));
                rolVO.setPde(rs.getString("ROL_PDE"));
                rolVO.setPcw(rs.getString("ROL_PCW"));
                resultado.add(rolVO);
            }//while(rs.next())
        } catch (Exception e) {
            log.error("Se ha producido un error consultado la tabla E_ROL", e);
            throw new Exception(e);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }//try-catch
        }//try-catch-finally


        if (log.isDebugEnabled()) {
            log.debug("dameRolesProcedimiento. Tamanho del resultado devuelto : " + resultado.size());
        }

        return resultado;


    }
    
    
    
}