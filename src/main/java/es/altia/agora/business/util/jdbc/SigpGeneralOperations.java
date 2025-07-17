package es.altia.agora.business.util.jdbc;

import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SigpGeneralOperations {
    
    /**
     * It closes a <code>ResultSet</code> if not <code>null</code>.
     */
    public static void closeResultSet(ResultSet resultSet) 
        throws TechnicalException {
        
        if (resultSet != null) {        
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new TechnicalException(e.getMessage(), e);
            }        
        }
        
    }

    /**
     * It closes a <code>Statement</code> if not <code>null</code>.
     */
    public static void closeStatement(Statement statement) 
        throws TechnicalException {
        
        if (statement != null) {        
            try {
                statement.close();
            } catch (SQLException e) {
                throw new TechnicalException(e.getMessage(), e);
            }        
        }
        
    }

    public static void commit(AdaptadorSQLBD oad, Connection con) throws TechnicalException {
        if (oad != null && con != null) {
            try {
                oad.finTransaccion(con);
            } catch (BDException bde) {
                throw new TechnicalException(bde.getMessage(), bde);
            }
        }
    }

    public static void devolverConexion(AdaptadorSQLBD oad, Connection con) throws TechnicalException {
        
        if (oad != null) {
            try {
                oad.devolverConexion(con);
            } catch (BDException bde) {
                throw new TechnicalException(bde.getMessage(), bde);
            }
        }
    }

    public static void rollBack(AdaptadorSQLBD oad, Connection con)  throws TechnicalException {
        if (oad != null && con != null) {
            try {
                oad.rollBack(con);
            } catch (BDException bde) {
                throw new TechnicalException(bde.getMessage(), bde);
            }
        }
    }

}
