
package es.altia.agora.business.registro.notificacion.dao;

//import es.altia.agora.business.registro.EntradaRechazadaValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class NotificacionPendientesFinalizarDAO {
    private static NotificacionPendientesFinalizarDAO instance = null;
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    protected static Log m_Log = LogFactory.getLog(NotificacionPendientesFinalizarDAO.class.getName());
    
    public static NotificacionPendientesFinalizarDAO getInstance(){
        if(instance == null){
            synchronized (NotificacionPendientesFinalizarDAO.class){
                if(instance == null){
                    instance = new NotificacionPendientesFinalizarDAO();
                }
            }
        }
        return instance;
    }
    
    public ArrayList<GeneralValueObject> getListaNotificarPendientesFinalizarRegistrador(Connection con) throws TechnicalException{
        ArrayList<GeneralValueObject> listadoEntradasPendientesFinRegistrador = new ArrayList(); 
        
        String sql;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            sql = "SELECT RES_NUM, RES_EJE, RES_USU, USU_EMAIL "
                    + " FROM R_RES, " + GlobalNames.ESQUEMA_GENERICO +"A_USU "
                    + " WHERE "
                    + " RES_TIP = 'E'"
                    + " AND FIN_DIGITALIZACION = 0"
                    + " AND RES_USU = USU_COD";
            
            m_Log.debug(sql);
            
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                GeneralValueObject pendienteVO = new GeneralValueObject();
                pendienteVO.setAtributo("ejercicio", rs.getInt("RES_EJE"));
                pendienteVO.setAtributo("numReg", rs.getLong("RES_NUM"));
                pendienteVO.setAtributo("correoUsuario", rs.getString("USU_EMAIL"));
                
                listadoEntradasPendientesFinRegistrador.add(pendienteVO);
                
            }
            
        }catch(SQLException sqlEx){
            m_Log.error(sqlEx.getMessage());
        }catch(Exception e){
            m_Log.error(e.getMessage());
        }finally{
            try {
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(NotificacionPendientesFinalizarDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
             return listadoEntradasPendientesFinRegistrador;
        }
    }
    
    public ArrayList<GeneralValueObject> getListaPendientesFinalizarOrganizacion(Connection con) throws TechnicalException{
        m_Log.debug("---> getListaNotificarPendientesFinalizarOrganizacion()");
        ArrayList<GeneralValueObject> listadoPendientesFinalizarOrganizacion = new ArrayList();
        ArrayList<GeneralValueObject> listadoPendientesFinalizarOrganizacionAux = new ArrayList();
        String sql = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            sql = "SELECT RES_NUM, RES_EJE, RES_FEC, USU_NOM"
                    + " FROM R_RES, "+GlobalNames.ESQUEMA_GENERICO+"A_USU "
                    + " WHERE FIN_DIGITALIZACION = 0"
                    + " AND RES_TIP='E'"
                    + " AND RES_USU = USU_COD"
                    + " ORDER BY RES_EJE DESC, RES_NUM DESC";
            
            m_Log.debug(sql);
            
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                GeneralValueObject pendienteVO = new GeneralValueObject();
                pendienteVO.setAtributo("ejercicio", rs.getInt("RES_EJE"));
                pendienteVO.setAtributo("numReg", rs.getLong("RES_NUM"));
                
                java.sql.Date dateAnotacion = rs.getDate("RES_FEC");
                Calendar calendarAnotacion = Calendar.getInstance();
                calendarAnotacion.setTimeInMillis(dateAnotacion.getTime());
                
                pendienteVO.setAtributo("fechaAnotacion",calendarAnotacion);
                
                listadoPendientesFinalizarOrganizacionAux.add(pendienteVO);
            }   
            
            //se comprueba que hayan pasado mas de x días 
             int dias = Integer.parseInt(m_ConfigTechnical.getString("dias_notificar_rechazadas_pendientes_fin_organizacion"));
             if(listadoPendientesFinalizarOrganizacionAux.size()>0){
                for(GeneralValueObject pendienteVO: listadoPendientesFinalizarOrganizacionAux){
                    if(calculoDiasRechazo(pendienteVO, dias)){
                        listadoPendientesFinalizarOrganizacion.add(pendienteVO);
                    }
                }
             }
         }catch (Exception e){
            m_Log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(), e);
        }finally{
             try {
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(NotificacionPendientesFinalizarDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return listadoPendientesFinalizarOrganizacion;
        }
    }
    
    private Boolean calculoDiasRechazo (GeneralValueObject gVO, int diasTranscurridos){
        Boolean notificar = false;
        DateTime fechaActual = new DateTime(Calendar.getInstance());
        DateTime fechaAnotacion = new DateTime(gVO.getAtributo("fechaAnotacion"));
        int daysBetween = Days.daysBetween(fechaAnotacion, fechaActual).getDays();
        if(daysBetween>diasTranscurridos){
            notificar = true;
        }
        return notificar;
    }
}
