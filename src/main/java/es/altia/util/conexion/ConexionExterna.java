/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.util.conexion;

import static es.altia.util.conexion.AdaptadorSQLBD.m_Log;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author david.vidal
 */
public class ConexionExterna {
    
    public Connection getConnection(String[] parametros) throws BDException
    {
        Connection con = null;
        String nombredriver = parametros[0];        
        String url = parametros[1];
        String usuario = parametros[2];
        String password = parametros[3];
        
        try{
             Driver driver = (Driver)Class.forName(nombredriver).newInstance();
             Properties p = new Properties();
             p.put("user",usuario);
             p.put("password",password);
             con = driver.connect(url,p);
             if (m_Log.isDebugEnabled()) m_Log.debug("Conexión adquirida en OAD");
        }
        catch(SQLException sqle){
             if (m_Log.isDebugEnabled()) m_Log.error("*** ConexionExterna." + sqle.toString());
             throw new BDException(100,"Error, no se pudo obtener la conexión a " +
                                         " la base de datos en la función getConnection "
                                         + " sin DataSource ", sqle.toString());
        }
        catch(Exception e){
             if(m_Log.isErrorEnabled()) m_Log.error("*** ConexionExterna." + e.toString());
             throw new BDException(100,"Error, excepcion capturada en la funcion " +
                                         " getConnection ",e.toString());
        }
        return con;
    }
}
