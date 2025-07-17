/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.sge.AlarmaExpedienteVO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.log4j.Logger;

/**
 *
 * @author santiagoc
 */
public class AlarmasExpedienteDAO {
    //Logger
    private static Logger log = Logger.getLogger(AlarmasExpedienteDAO.class);
    
    //Instancia
    private static AlarmasExpedienteDAO instance = null;
    private Config m_CommonProperties; // Para el fichero de contantes
    private Config m_ConfigTechnical; //	Para el fichero de configuracion técnico
    private Config m_ConfigError; // Para los mensajes de error localizados
    
    private AlarmasExpedienteDAO() {   
        m_CommonProperties = ConfigServiceHelper.getConfig("common");
        // Queremos usar el	fichero de configuración technical
        m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError	= ConfigServiceHelper.getConfig("error");
    }
    
    public static AlarmasExpedienteDAO getInstance() {
        if (instance == null) {
            synchronized(AlarmasExpedienteDAO.class) {
                instance = new AlarmasExpedienteDAO();
            }
        }
        return instance;
    }   
    
    /**
     * Grabar el contenido del objeto de la clase AlarmaExpedienteVO en la tabla E_ALE
     * 
     * @param alarma
     * @param con
     * @return
     * @throws Exception 
     */
    public void grabarAlarmaExpediente(AlarmaExpedienteVO alarma, Connection con) throws SQLException {
        
        try {
            borrarAlarmaExpediente(alarma,con);

            Calendar fechaPrimeraAlarma = getFechaPrimeraAlarmaExpediente(alarma.getCodOrganizacion(),
                    alarma.getEjercicio(),alarma.getNumExpediente(),con);
            
            if (alarma.getFechaVencimiento()!=null)
                insertarAlarmaExpediente(alarma,con);
            
            if (fechaPrimeraAlarma == null || (alarma.getFechaVencimiento()!=null && 
                    alarma.getFechaVencimiento().before(fechaPrimeraAlarma)))
                ExpedientesDAO.getInstance().actualizarFechaPrimeraAlarma(alarma.getCodOrganizacion(), 
                        alarma.getEjercicio(), alarma.getNumExpediente(), alarma.getFechaVencimiento(), con);
        } catch(SQLException ex) {
            ex.printStackTrace();
            log.error("Se ha producido un error en el metodo AlarmasExpedienteDAO.grabarAlarmaExpediente(): " + ex.getMessage() , ex);
            throw new SQLException(ex);
        } 
    }
    
    /**
     * Insertar el contenido del objeto de la clase AlarmaExpedienteVO en la tabla E_ALE
     * 
     * @param alarma
     * @param con
     * @return
     * @throws Exception 
     */
    public void insertarAlarmaExpediente(AlarmaExpedienteVO alarma, Connection con) throws SQLException {
        
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO E_ALE (ALE_MUN,ALE_EJE,ALE_NUM,ALE_TRA,ALE_OCU,ALE_COD,ALE_FEC) " +
                "VALUES (?,?,?,?,?,?,?)";
            
            log.debug(" (AlarmasExpedienteDAO.grabarAlarmaExpediente(): " + sql);
            ps = con.prepareStatement(sql);
            
            int i = 1;
            ps.setInt(i++,alarma.getCodOrganizacion());
            ps.setInt(i++,alarma.getEjercicio());
            ps.setString(i++,alarma.getNumExpediente());
            ps.setInt(i++,alarma.getCodTramite());
            ps.setInt(i++,alarma.getOcurrenciaTramite());
            ps.setString(i++,alarma.getCodigoDato());
            ps.setTimestamp(i++,DateOperations.toTimestamp(alarma.getFechaVencimiento()));
            
            ps.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
            log.error("Se ha producido un error en el metodo AlarmasExpedienteDAO.grabarAlarmaExpediente(): " + ex.getMessage() , ex);
            throw new SQLException(ex);
        } finally {
            if(ps != null)
                ps.close();
        }
    }
    
    /**
     * Borra el contenido del objeto de la clase AlarmaExpedienteVO en la tabla E_ALE
     * 
     * @param alarma
     * @param con
     * @return
     * @throws Exception 
     */
    private int borrarAlarmaExpediente(AlarmaExpedienteVO alarma, Connection con) throws SQLException {
        
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM E_ALE WHERE ALE_MUN = ? AND ALE_EJE = ? AND ALE_NUM = ? AND " + 
                    "ALE_TRA = ? AND ALE_OCU = ? AND ALE_COD = ?";
            
            log.debug(" (AlarmasExpedienteDAO.borrarAlarmaExpediente(): " + sql);
            ps = con.prepareStatement(sql);
            
            int i = 1;
            ps.setInt(i++,alarma.getCodOrganizacion());
            ps.setInt(i++,alarma.getEjercicio());
            ps.setString(i++,alarma.getNumExpediente());
            ps.setInt(i++,alarma.getCodTramite());
            ps.setInt(i++,alarma.getOcurrenciaTramite());
            ps.setString(i++,alarma.getCodigoDato());
            
            int numEliminados = ps.executeUpdate();
            
            return numEliminados;
        } catch(SQLException ex) {
            ex.printStackTrace();
            log.error("Se ha producido un error en el metodo AlarmasExpedienteDAO.borrarAlarmaExpediente(): " + ex.getMessage() , ex);
            throw new SQLException(ex);
        } finally {
            if(ps != null)
                ps.close();
        }
    }
    
    /**
     * Borra de la tabla E_ALE las alarmas de una ocurrencia de un trámite
     * 
     * @param alarma
     * @param con
     * @return
     * @throws Exception 
     */
    public int borrarAlarmasOcurrenciaTramite(int codMunicipio, int ejercicio, String numExpediente, 
            int tramite, int ocurrencia, Connection con) throws SQLException {
        
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM E_ALE WHERE ALE_MUN = ? AND ALE_EJE = ? AND ALE_NUM = ? AND " + 
                    "ALE_TRA = ? AND ALE_OCU = ?";
            
            log.debug(" (AlarmasExpedienteDAO.borrarAlarmasOcurrenciaTramite(): " + sql);
            ps = con.prepareStatement(sql);
            
            int i = 1;
            ps.setInt(i++,codMunicipio);
            ps.setInt(i++,ejercicio);
            ps.setString(i++,numExpediente);
            ps.setInt(i++,tramite);
            ps.setInt(i++,ocurrencia);
            
            int numEliminados = ps.executeUpdate();
            
            if (numEliminados > 0){
                Calendar fechaPrimeraAlarma = getFechaPrimeraAlarmaExpediente(codMunicipio,
                    ejercicio, numExpediente, con);

                ExpedientesDAO.getInstance().actualizarFechaPrimeraAlarma(codMunicipio,
                    ejercicio, numExpediente, fechaPrimeraAlarma, con);            
            }
                
            return numEliminados;
        } catch(SQLException ex) {
            ex.printStackTrace();
            log.error("Se ha producido un error en el metodo AlarmasExpedienteDAO.borrarAlarmasOcurrenciaTramite(): " + ex.getMessage() , ex);
            throw new SQLException(ex);
        } finally {
            if(ps != null)
                ps.close();
        }
    }
    
    public ArrayList<String> getNombresAlarmasVencidasExpediente(int codOrganizacion, int ejercicio, 
            String numExpediente, String codProcedimiento, AdaptadorSQLBD oad, Connection con) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<String> resultado = new ArrayList<String>();
        String sql = "";

        try {
            sql = "SELECT ALE_TRA, ALE_OCU, ALE_COD, PCA_DES AS DESCRICION, '--' AS NOM_TRAMITE " + 
                    "FROM E_ALE INNER JOIN E_PCA ON (PCA_MUN = ALE_MUN AND PCA_PRO = ? AND " + 
                    "PCA_COD = ALE_COD) WHERE ALE_MUN = ? AND ALE_EJE = ? AND ALE_NUM = ? " + 
                    "AND ALE_TRA = 0 AND ALE_OCU = 0 " +
                    "AND " + oad.convertir("ALE_FEC", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYYMMDD") +
                    " < " +
                    oad.convertir(oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYYMMDD") +  
                    " UNION " +
                    "SELECT ALE_TRA, ALE_OCU, ALE_COD, TCA_DES AS DESCRICION, TML_VALOR AS NOM_TRAMITE " + 
                    "FROM E_ALE INNER JOIN E_TCA ON (TCA_MUN = ALE_MUN AND TCA_PRO = ? AND " + 
                    "TCA_TRA = ALE_TRA AND TCA_COD = ALE_COD) INNER JOIN E_TML ON (TML_MUN = ALE_MUN AND " + 
                    "TML_PRO = TCA_PRO AND TML_TRA = ALE_TRA AND TML_CMP = 'NOM' AND TML_LENG = '1') " + 
                    "WHERE ALE_MUN = ? AND ALE_EJE = ? AND ALE_NUM = ? AND ALE_TRA <> 0 AND ALE_OCU <> 0 " +
                    "AND " + oad.convertir("ALE_FEC", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYYMMDD") +
                    " < " +
                    oad.convertir(oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYYMMDD") +  
                    " ORDER BY ALE_TRA, ALE_OCU, ALE_COD";
            log.debug(" (AlarmasExpedienteDAO.getAlarmasVencidasExpediente(): " + sql);
            
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setString(i++,codProcedimiento);
            ps.setInt(i++,codOrganizacion);
            ps.setInt(i++,ejercicio);
            ps.setString(i++,numExpediente);
            ps.setString(i++,codProcedimiento);
            ps.setInt(i++,codOrganizacion);
            ps.setInt(i++,ejercicio);
            ps.setString(i++,numExpediente);
            
            if (log.isDebugEnabled()) {
                log.debug(sql);
            }
            rs = ps.executeQuery();
            
            while (rs.next()) {
                String nomTramite = rs.getString("NOM_TRAMITE");
                
                if ("--".equals(nomTramite))
                    resultado.add(rs.getString("ALE_COD"));
                else
                    resultado.add(nomTramite + " - " + rs.getString("ALE_COD"));
            }
        } catch (SQLException e) {
            resultado = null;
            e.printStackTrace();
            log.error("Se ha producido un error en el metodo AlarmasExpedienteDAO.getAlarmasVencidasExpediente(): " + e.getMessage() , e);
            throw new SQLException(e);
        } finally {
            try {
                if (rs != null) 
                    rs.close();
                if (ps != null) 
                    ps.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return resultado;
    }

    public Calendar getFechaPrimeraAlarmaExpediente(int codOrganizacion,  int ejercicio, 
            String numExpediente, Connection con) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Calendar resultado = null;
        String sql = "";

        try {
            sql = "SELECT MIN(ALE_FEC) AS MIN_FEC FROM E_ALE WHERE ALE_MUN = ? AND ALE_EJE = ? AND ALE_NUM = ?";
            log.debug(" (AlarmasExpedienteDAO.getFechaPrimeraAlarmaExpediente(): " + sql);
                        
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,codOrganizacion);
            ps.setInt(i++,ejercicio);
            ps.setString(i++,numExpediente);
            
            if (log.isDebugEnabled()) {
                log.debug(sql);
            }
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("MIN_FEC");
                if (timestamp!=null)
                    resultado = DateOperations.toCalendar(timestamp);
            }
        } catch (SQLException e) {
            resultado = null;
            e.printStackTrace();
            log.error("Se ha producido un error en el metodo AlarmasExpedienteDAO.getFechaPrimeraAlarmaExpediente(): " + e.getMessage() , e);
            throw new SQLException(e);
        } finally {
            try {
                if (rs != null) 
                    rs.close();
                if (ps != null) 
                    ps.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return resultado;
    }

}
