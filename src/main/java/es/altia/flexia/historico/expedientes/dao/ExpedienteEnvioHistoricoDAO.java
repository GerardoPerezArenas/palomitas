/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.historico.expedientes.dao;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.historico.expediente.vo.ExpedienteEnvioHistoricoVO;
import es.altia.flexia.historico.expediente.vo.ProcedimientoHistoricoVO;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;

/**
 *
 * @author santiagoc
 */
public class ExpedienteEnvioHistoricoDAO
{
    //Logger
    private static Logger log = Logger.getLogger(ExpedienteEnvioHistoricoDAO.class);
    
    //Instancia
    private static ExpedienteEnvioHistoricoDAO instance = null;
    private Config m_CommonProperties; // Para el fichero de contantes
    private Config m_ConfigTechnical; //	Para el fichero de configuracion técnico
    private Config m_ConfigError; // Para los mensajes de error localizados
    
    private ExpedienteEnvioHistoricoDAO()
    {   
        m_CommonProperties = ConfigServiceHelper.getConfig("common");
        // Queremos usar el	fichero de configuración technical
        m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError	= ConfigServiceHelper.getConfig("error");
    }
    
    public static ExpedienteEnvioHistoricoDAO getInstance()
    {
        if(instance == null)
        {
            synchronized(ExpedienteEnvioHistoricoDAO.class)
            {
                instance = new ExpedienteEnvioHistoricoDAO();
            }
        }
        return instance;
    }       
    
    /**
     * Lee los procedimientos para los cuales se van a mover sus expedientes al histórico.
     * Se recupera además para cada procedimiento el número de meses.
     * Se recupera de la tabla PROCEDIMIENTO_HISTORICO.
     * 
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<ProcedimientoHistoricoVO> getProcedimientos(Connection con) throws Exception
    {
        ArrayList<ProcedimientoHistoricoVO> retList = new ArrayList<ProcedimientoHistoricoVO>();
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql_tabla_expEnvioHistorico = m_ConfigTechnical.getString("SQL.PROCEDIMIENTO_HISTORICO");
            String sql_codProcedimiento = m_ConfigTechnical.getString("SQL.PROCEDIMIENTO_HISTORICO.codProcedimiento");
            String sql_meses = m_ConfigTechnical.getString("SQL.PROCEDIMIENTO_HISTORICO.meses");
            String sql_codOrganizacion = m_ConfigTechnical.getString("SQL.PROCEDIMIENTO_HISTORICO.codOrganizacion");
            String query = "select * from "+sql_tabla_expEnvioHistorico;
            log.debug(query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            ProcedimientoHistoricoVO p = null;
            
            while(rs.next())
            {
                p = new ProcedimientoHistoricoVO();
                p.setCodOrganizacion(rs.getInt(sql_codOrganizacion));
                p.setCodProcedimiento(rs.getString(sql_codProcedimiento));
                p.setMeses(rs.getInt(sql_meses));
                retList.add(p);
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un erro en el metodo expedienteEnvioHistoricoDAO.getProcedimientos", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(rs != null)
                rs.close();
            if(st != null)
                st.close();
        }
    }
    
    
    public ArrayList<ProcedimientoHistoricoVO> getProcedimientosOrganizacion(Connection con,
            int codOrganizacion) throws Exception {
        ArrayList<ProcedimientoHistoricoVO> retList = new ArrayList<ProcedimientoHistoricoVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT COD_PROCEDIMIENTO,MESES,COD_ORGANIZACION FROM PROCEDIMIENTO_HISTORICO " +
                    "WHERE COD_ORGANIZACION = ?";
            log.debug(query);
            ps = con.prepareStatement(query);
            
            ps.setInt(1, codOrganizacion);
            
            rs = ps.executeQuery();
            
            while(rs.next()) {
                ProcedimientoHistoricoVO p = new ProcedimientoHistoricoVO();
                p.setCodOrganizacion(rs.getInt("COD_ORGANIZACION"));
                p.setCodProcedimiento(rs.getString("COD_PROCEDIMIENTO"));
                p.setMeses(rs.getInt("MESES"));
                retList.add(p);
            }
            return retList;
        }
        catch(Exception ex) {
            log.error("Se ha producido un erro en el metodo expedienteEnvioHistoricoDAO.getProcedimientos", ex);
            throw new Exception(ex);
        } finally {
            if(rs != null)
                rs.close();
            if(ps != null)
                ps.close();
        }
    }
    
    /**
     * Inserta una nueva indicación de plazo de pase a histórico para un procedimiento.
     * Se inserta en la tabla PROCEDIMIENTO_HISTORICO.
     * 
     * @param con
     * @param procedimientoHistorico
     * @return String
     * @throws Exception 
     */
    public String grabarProcedimientoHistorico(Connection con,
            ProcedimientoHistoricoVO procedimientoHistorico) throws SQLException, Exception {
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "";
        try {
            String query = "SELECT * FROM E_PRO WHERE PRO_MUN = ? AND PRO_COD = ?";
            log.debug(query);
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setInt(i++, procedimientoHistorico.getCodOrganizacion());
            ps.setString(i++, procedimientoHistorico.getCodProcedimiento());
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                query = "INSERT INTO PROCEDIMIENTO_HISTORICO (COD_PROCEDIMIENTO,MESES,COD_ORGANIZACION) " +
                        "VALUES (?,?,?)";
                log.debug(query);
                ps = con.prepareStatement(query);
                i = 1;
                ps.setString(i++, procedimientoHistorico.getCodProcedimiento());
                ps.setInt(i++, procedimientoHistorico.getMeses());
                ps.setInt(i++, procedimientoHistorico.getCodOrganizacion());

                ps.executeUpdate();
            } else {
                result = "procNoExiste";
            }
            return result;
        } catch(SQLException ex) {
            log.error("Se ha producido un error sql en el metodo ExpedienteEnvioHistoricoDAO.grabarProcedimientoHistorico", ex);
            if ("23000".equals(ex.getSQLState()))
                return "msjCodExiste";
            else
                throw new SQLException(ex);
        } catch(Exception ex) {
            log.error("Se ha producido un error en el metodo ExpedienteEnvioHistoricoDAO.grabarProcedimientoHistorico", ex);
            throw new Exception(ex);
        } finally {
            if(ps != null)
                ps.close();
        }
    }
    
    /**
     * Modifica el plazo de pase a histórico para un procedimiento.
     * Se actualiza la tabla PROCEDIMIENTO_HISTORICO.
     * 
     * @param con
     * @param procedimientoHistorico
     * @return
     * @throws Exception 
     */
    public void modificarProcedimientoHistorico(Connection con,
            ProcedimientoHistoricoVO procedimientoHistorico) throws SQLException, Exception {
        
        PreparedStatement ps = null;
       
        try {
            String query = "UPDATE PROCEDIMIENTO_HISTORICO SET MESES = ? " +
                    "WHERE COD_PROCEDIMIENTO = ? AND COD_ORGANIZACION = ?";
            log.debug(query);
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setInt(i++, procedimientoHistorico.getMeses());
            ps.setString(i++, procedimientoHistorico.getCodProcedimiento());
            ps.setInt(i++, procedimientoHistorico.getCodOrganizacion());
            
            ps.executeUpdate();
        } catch(SQLException ex) {
            log.error("Se ha producido un error sql en el metodo ExpedienteEnvioHistoricoDAO.modificarProcedimientoHistorico", ex);
            throw new SQLException(ex);
        } catch(Exception ex) {
            log.error("Se ha producido un error en el metodo ExpedienteEnvioHistoricoDAO.modificarProcedimientoHistorico", ex);
            throw new Exception(ex);
        } finally {
            if(ps != null)
                ps.close();
        }
    }
    
    /**
     * Elimina un plazo de pase a histórico para un procedimiento.
     * Se actualiza la tabla PROCEDIMIENTO_HISTORICO.
     * 
     * @param con
     * @param procedimientoHistorico
     * @return
     * @throws Exception 
     */
    public void eliminarProcedimientoHistorico(Connection con,
            ProcedimientoHistoricoVO procedimientoHistorico) throws SQLException, Exception {
        
        PreparedStatement ps = null;
       
        try {
            String query = "DELETE FROM PROCEDIMIENTO_HISTORICO " +
                    "WHERE COD_PROCEDIMIENTO = ? AND COD_ORGANIZACION = ?";
            log.debug(query);
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setString(i++, procedimientoHistorico.getCodProcedimiento());
            ps.setInt(i++, procedimientoHistorico.getCodOrganizacion());
            
            ps.executeUpdate();
        } catch(SQLException ex) {
            log.error("Se ha producido un error sql en el metodo ExpedienteEnvioHistoricoDAO.eliminarProcedimientoHistorico", ex);
            throw new SQLException(ex);
        } catch(Exception ex) {
            log.error("Se ha producido un error en el metodo ExpedienteEnvioHistoricoDAO.eliminarProcedimientoHistorico", ex);
            throw new Exception(ex);
        } finally {
            if(ps != null)
                ps.close();
        }
    }
    
    /**
     * Crea un nuevo registro en la tabla EXP_ENVIO_HISTORICO
     * 
     * @param expEnv
     * @param con
     * @return long
     * @throws Exception 
     */
    
    /**
     * Recupera los expedientes finalizados (9) y/o anulados (1) de un procedimiento y cuya fecha de fin sea anterior 
     * o igual a la fecha actual menos un número de meses proporcionada en el parámetro ?numMeses?.
     * 
     * @param codOrganizacion
     * @param codProcedimento
     * @param numMeses
     * @param con
     * @return
     * @throws Exception 
     */
    public  ArrayList<String> getExpedientesProcedimiento(int codOrganizacion, String codProcedimento, int numMeses, Connection con,String[] params) throws Exception
    {
        ArrayList<String> retList = new ArrayList<String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        AdaptadorSQLBD adapt = null;
        try
        {   
            adapt = new AdaptadorSQLBD(params);
            Date today = new Date();
            Calendar calToday = new GregorianCalendar();
            calToday.setTime(today);
            
            int month = calToday.get(GregorianCalendar.MONTH);
            calToday.set(GregorianCalendar.MONTH, month - numMeses);
            
            String query = "SELECT EXP_NUM FROM E_EXP " + 
                           "WHERE EXP_MUN=? AND EXP_PRO=? AND EXP_EST IN (1,9) AND "+adapt.diferenciaMeses(adapt.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null),"EXP_FEF")+">=? " + 
                           "ORDER BY EXP_NUM ASC";            
            log.debug(query);
            
            int i=1;
            ps = con.prepareStatement(query);
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,codProcedimento);
            ps.setInt(i++,numMeses);
            
           log.debug("codOrganizacion "+codOrganizacion+" ;codProcedimiento: "+codProcedimento+" ;meses: "+numMeses);
                        
            rs = ps.executeQuery();
            while(rs.next()) 
            { 
                retList.add(rs.getString("EXP_NUM"));
            }
            return retList;
        }
        catch(Exception ex){
            log.error("Se ha producido un erro en el metodo expedienteEnvioHistoricoDAO.getExpedientesProcedimiento: " +  ex.getMessage());
             
            ex.printStackTrace();
            throw new Exception(ex); 
        }
        finally
        {
            if(rs != null) rs.close();
            if(ps != null) ps.close();
        }
    }
    
    /**
     * Este método recupera los expedientes de la tabla EXP_ENVIO_HISTORICO que están pendientes de ser procesados,
     * es decir, que el campo PROCESADO <> 1
     * 
     * @param con
     * @return
     * @throws Exception 
     */
    /*public ArrayList<ExpedienteEnvioHistoricoVO> getExpedientesPendientesProcesar(Connection con) throws Exception
    {
        ArrayList<ExpedienteEnvioHistoricoVO> retList = new ArrayList<ExpedienteEnvioHistoricoVO>();
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql_tablaExpEnvioHistorico = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO");
            String sql_id = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.id");
            String sql_codOrganizacion = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.codOrganizacion");
            String sql_numExpediente = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.numExpediente");
            String sql_fechaEnvioArchivo = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.fechaEnvioArchivo");
            String sql_procesado = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.procesado");
            String sql_fechaProceso = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.fechaProceso");
            String sql_errorProceso = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.errorProceso");
            String sql_codUsuario = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.codUsuario");
            String query = "select * from "+sql_tablaExpEnvioHistorico+" where "+sql_procesado+" <> 1";
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            ExpedienteEnvioHistoricoVO e = null;
            
            while(rs.next())
            {
                e = new ExpedienteEnvioHistoricoVO();
                e.setId(rs.getLong(sql_id));
                e.setCodOrganizacion(rs.getInt(sql_codOrganizacion));
                e.setCodUsuario(rs.getInt(sql_codUsuario));
                e.setErrorProceso(rs.getString(sql_errorProceso));
                Date fec = null;
                Calendar cal = null;
                fec = rs.getDate(sql_fechaEnvioArchivo);
                if(fec != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(fec);
                    e.setFecEnvArchivo(cal);
                }
                fec = rs.getDate(sql_fechaProceso);
                if(fec != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(fec);
                    e.setFechaProceso(cal);
                }
                e.setNumExpediente(rs.getString(sql_numExpediente));
                e.setProcesado(rs.getInt(sql_procesado));
                retList.add(e);
            }
            
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un erro en el metodo expedienteEnvioHistoricoDAO.getExpedientesPendientesProcesar", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(rs != null)
                rs.close();
            if(st != null)
                st.close();
        }
    }*/
    
    /**
     * Crea un nuevo registro en la tabla EXP_ENVIO_HISTORICO
     * 
     * @param exp
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean crearEnvioExpedienteHistorico( ExpedienteEnvioHistoricoVO exp, Connection con,String[] params) throws Exception
    {
        Statement st = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try
        {
            String sql_tablaExpEnvioHistorico = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO");
            String sql_id = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.id");
            String sql_codOrganizacion = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.codOrganizacion");
            String sql_numExpediente = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.numExpediente");
            String sql_fechaEnvioArchivo = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.fechaEnvioArchivo");
            String sql_procesado = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.procesado");
            String sql_fechaProceso = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.fechaProceso");
            String sql_errorProceso = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.errorProceso");
            String sql_codUsuario = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.codUsuario");
            String sql_fechaRecuperacion = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.fechaRecuperacion");
             String sql="";
             if (params[0].equals("sqlserver") || params[0].equals("SQLSERVER")) {
                  sql= "insert into "+sql_tablaExpEnvioHistorico
                    +"( "+sql_codOrganizacion+", "+sql_numExpediente+", "+sql_fechaEnvioArchivo+", "+sql_procesado
                    +", "+sql_fechaProceso+", "+sql_fechaRecuperacion+", "+sql_errorProceso+", "+sql_codUsuario
                    +") values ("                    
                    +" "+exp.getCodOrganizacion()
                    +", "+(exp.getNumExpediente() != null ? "'"+exp.getNumExpediente()+"'" : "null")
                    +", "+(exp.getFecEnvArchivo() != null ? "CONVERT(datetime,'"+sdf.format(exp.getFecEnvArchivo().getTime())+"',103) " : "null")
                    +", 0"
                    +", "+(exp.getFechaProceso() != null ? "CONVERT(datetime,'"+sdf.format(exp.getFechaProceso().getTime())+"',103) " : "null")
                    +", "+(exp.getFechaRecuperacion() != null ? "CONVERT(datetime,'"+sdf.format(exp.getFechaRecuperacion().getTime())+"',103) " : "null")
                    +", "+(exp.getErrorProceso() != null ? "'"+exp.getErrorProceso()+"'" : "null")
                    +", "+exp.getCodUsuario()
                    +")"
                    ;  
                 
             }else
             {
                 
             
            sql = "insert into "+sql_tablaExpEnvioHistorico
                    +"("+sql_id+", "+sql_codOrganizacion+", "+sql_numExpediente+", "+sql_fechaEnvioArchivo+", "+sql_procesado
                    +", "+sql_fechaProceso+", "+sql_fechaRecuperacion+", "+sql_errorProceso+", "+sql_codUsuario
                    +") values ("
                    +""+exp.getId()
                    +", "+exp.getCodOrganizacion()
                    +", "+(exp.getNumExpediente() != null ? "'"+exp.getNumExpediente()+"'" : "null")
                    +", "+(exp.getFecEnvArchivo() != null ? "TO_TIMESTAMP('"+sdf.format(exp.getFecEnvArchivo().getTime())+"', 'dd/MM/yyyy hh24:mi:ss')" : "null")
                    +", 0"
                    +", "+(exp.getFechaProceso() != null ? "TO_TIMESTAMP('"+sdf.format(exp.getFechaProceso().getTime())+"', 'dd/MM/yyyy hh24:mi:ss')" : "null")
                    +", "+(exp.getFechaRecuperacion() != null ? "TO_TIMESTAMP('"+sdf.format(exp.getFechaRecuperacion().getTime())+"', 'dd/MM/yyyy hh24:mi:ss')" : "null")
                    +", "+(exp.getErrorProceso() != null ? "'"+exp.getErrorProceso()+"'" : "null")
                    +", "+exp.getCodUsuario()
                    +")"
                    ;  
            }
            
            log.debug(sql);
            st = con.createStatement();
            
            int res = st.executeUpdate(sql);
            return res > 0;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un erro en el metodo expedienteEnvioHistoricoDAO.marcarEnvioExpedienteHistorico", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(st != null)
                st.close();
        }
    }
    
    /**
     * Cambia el estado del registro de la tabla EXP_ENVIO_HISTORICO que está siendo procesado. En el parámetro exp,
     * se tiene la información del registro a actualizar, en el parámetro procesado el valor para el campo EXP_ENVIO_HISTORICO, que puede ser:
     *     1 ? Expediente procesado y pasado al histórico
     *     2 ? Se ha producido un error
     * El parámetro error, contiene la descripción del error producido en el caso de que el paso del expediente al histórico haya fallado.
     * 
     * @param exp
     * @param procesado
     * @param error
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean marcarEnvioExpedienteHistorico( ExpedienteEnvioHistoricoVO exp, int procesado,String error,Connection con, String [] params) throws Exception
    {
        Statement st = null;
        AdaptadorSQLBD adapt = null;
        try
        {
            String sql_tablaExpEnvioHistorico = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO");
            String sql_procesado = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.procesado");
            String sql_id = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.id");
            String sql_codOrganizacion = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.codOrganizacion");
            String sql_numExpediente = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.numExpediente");
            String sql_fechaProceso = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.fechaProceso");
            String sql_errorProceso = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.errorProceso");
            
            adapt = new AdaptadorSQLBD(params);
            
            String sql = "update "+sql_tablaExpEnvioHistorico+" set "+sql_procesado+" = "+procesado+", "+sql_errorProceso+" = '"+error+"', "+sql_fechaProceso+" = "+adapt.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" where "+sql_id+" = "+exp.getId()+" and "+sql_numExpediente+" = '"+exp.getNumExpediente()+"' and "+sql_codOrganizacion+" = "+exp.getCodOrganizacion();
            log.debug(sql);
            st = con.createStatement();
            
            int res = st.executeUpdate(sql);
            return res > 0;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un erro en el metodo expedienteEnvioHistoricoDAO.marcarEnvioExpedienteHistorico", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(st != null)
                st.close();
        }
    }
    
    public long obtenerIdProceso(Connection con) throws Exception
    {
        long id = 0;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql_tablaExpEnvioHistorico = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO");
            String sql_id = m_ConfigTechnical.getString("SQL.EXP_ENVIO_HISTORICO.id");
            String query = "select max("+sql_id+")+1 SIG_ID from "+sql_tablaExpEnvioHistorico;
            log.debug(query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            if(rs.next())
            {
                id = rs.getLong("SIG_ID");
            }
            
            return id;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un erro en el metodo expedienteEnvioHistoricoDAO.obtenerIdProceso", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(rs != null)
                rs.close();
            if(st != null)
                st.close();
        }
    }
}
