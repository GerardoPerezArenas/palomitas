package es.altia.agora.business.administracion.mantenimiento.persistence.manual;

import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.common.service.config.Config;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Iterator;

/**
 * DAO para obtener y escribir los parametros propios de la organizacion guardados
 * en la tabla PARAMETROS.
 * @author juan.jato
 */
public class ParametrosDAO {
    
  //Para el fichero de configuracion tecnico.
    protected static Config m_ConfigTechnical;
  //Para informacion de logs.
    protected static Log m_Log =
          LogFactory.getLog(ParametrosDAO.class.getName());
  //La instancia de esta clase
    private static ParametrosDAO instance = null;
    
    protected ParametrosDAO() {
        super();
    }
    
    public static ParametrosDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(ParametrosDAO.class){
                if (instance == null)
                    instance = new ParametrosDAO();
                }
        }
        return instance;
    }
    
    /**
     * Obtiene todos los parametros propios de la organizacion.
     * @param params parametros de conexion a BD
     * @return hashmap con todos los parametros
     * @throws java.lang.Exception
     */
    public HashMap<String,String> obtenerParametros(String[] params)
            throws Exception {
        
        m_Log.debug("ParametrosDAO.obtenerParametros");
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        HashMap<String,String> parametros = new HashMap<String,String>();

        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            st = conexion.createStatement();

            // Obtenemos todos los parametros
            sql = "SELECT CODIGO, VALOR FROM PARAMETROS";
            m_Log.debug(sql);
            rs = st.executeQuery(sql);
            String codigo = null;
            String valor = null;
            while (rs.next()) {
                codigo = rs.getString("CODIGO");
                valor = rs.getString("VALOR");
                parametros.put(codigo, valor);
                m_Log.debug(codigo + "=" + valor);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
        
        return parametros;
    }
    
    /**
     * Obtiene el valor del parametro cuyo codigo se indica, lanza una excepcion
     * si no se encuentra el parametro buscado.
     * @param codigo codigo del parametro cuyo valor se quiere obtener
     * @param params parametros de conexion a BD
     * @return valor del parametro indicado
     * @throws java.lang.Exception
     */
    public String obtenerParametro(String codigo, String[] params)
            throws Exception {
        
        m_Log.debug("ParametrosDAO.obtenerParametro");
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        String parametro = null;

        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            st = conexion.createStatement();

            // Obtenemos el parametro pedido
            sql = "SELECT VALOR FROM PARAMETROS WHERE CODIGO = '" + codigo + "'";
            m_Log.debug(sql);
            rs = st.executeQuery(sql);
            if (rs.next()) {
                parametro = rs.getString("VALOR");
                m_Log.debug(codigo + "=" + parametro);
            } else {
                m_Log.debug("NO SE HA ENCONTRADO EL PARAMETRO " + codigo);
                throw new Exception("NO SE HA ENCONTRADO EL PARAMETRO " + codigo);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
        
        return parametro;
    }
    
    /** 
     * Actualiza los valores de los parametros que se incluyan en el HashMap
     * @param parametros codigos y valores de todos los parametros
     * @param params parametros de conexion a BD
     * @throws java.lang.Exception
     */
    public void grabarParametros(HashMap<String,String> parametros, String[] params)
            throws Exception {
        
        m_Log.debug("ParametrosDAO.grabarParametros");
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement ps = null;
        String sql = "";

        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();

            // Obtenemos el parametro pedido
            sql = "UPDATE PARAMETROS SET VALOR = ? WHERE CODIGO = ?";
            ps = conexion.prepareStatement(sql);
            
            Iterator iter = parametros.keySet().iterator();
	    while(iter.hasNext()) {
		String codigo = (String) iter.next();
		String valor = parametros.get(codigo);
                m_Log.debug("UPDATE PARAMETROS SET VALOR = " + valor + " WHERE CODIGO = " + codigo);
                ps.setString(1,valor);
                ps.setString(2,codigo);
                ps.executeUpdate();
	    }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
    }
}