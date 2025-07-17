/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.TechnicalException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author fran
 */
public class RolDAO {
    
    
    protected static Log m_Log = LogFactory.getLog(RolDAO.class.getName());

    private static RolDAO instance = null;        

    protected RolDAO() { 
        super();
    }  
    
    public static RolDAO getInstance() {
            // Sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(RolDAO.class) {
                if (instance == null)
                    instance = new RolDAO();
            }
        return instance;
    }  
    
    public String getDescRolPorCodigo (int municipio, String codProcedimiento, int codigoRol, Connection con) throws TechnicalException {
        
        String descRol = "";

        PreparedStatement ps = null;
        String sql;
        ResultSet rs = null;
        try {
            sql = "SELECT ROL_DES FROM E_ROL WHERE ROL_MUN = ? AND ROL_PRO = ? AND ROL_COD = ?";
            
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++, municipio);
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, codigoRol);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                descRol = rs.getString(1);
            }
            
        } catch (Exception e) {
            if (m_Log.isErrorEnabled()) {m_Log.equals(e.getMessage());}
            e.printStackTrace();
            throw new TechnicalException(e.getMessage(),e);
        }finally {
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(ps);
        }
        
        return descRol;
    }
    
}
