
package es.altia.agora.business.registro.notificacion.dao;

import es.altia.agora.business.registro.EntradaRechazadaValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import org.joda.time.DateTime;
import org.joda.time.Days;


/**
 *
 * @author altia
 */
public class NotificacionEntradasRechazadasDAO {
    private static NotificacionEntradasRechazadasDAO instance = null;
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    protected static Log m_Log = LogFactory.getLog(NotificacionEntradasRechazadasDAO.class.getName());
    
    public static NotificacionEntradasRechazadasDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una
        if(instance == null){
            // Necesitamos sincronización para serializar las invocaciones de este metodo
            synchronized (NotificacionEntradasRechazadasDAO.class){
                if(instance == null){
                    instance = new NotificacionEntradasRechazadasDAO();
                }
            }
        }
        return instance;
    }
    
    public ArrayList<EntradaRechazadaValueObject> getListaNotificarRechazadasRegistrador(Connection con) throws TechnicalException{
        
        ArrayList<EntradaRechazadaValueObject> listadoEntradasNotificarRegistrador = new ArrayList();
        
        String sql = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            sql = "SELECT RES_NUM, RES_EJE, RES_USU, USU_EMAIL "
                    + "FROM R_RES, "+GlobalNames.ESQUEMA_GENERICO+"A_USU "
                    + "WHERE "
                    + "RES_EST = 2 AND "
                    + "RES_TIP = 'E' AND "
                    + "RES_USU = USU_COD";
            
            m_Log.debug(sql);
            
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                EntradaRechazadaValueObject  rechazadaVO = new EntradaRechazadaValueObject();
                rechazadaVO.setEjercicio(rs.getInt("RES_EJE"));
                rechazadaVO.setNumReg(rs.getLong("RES_NUM"));
                rechazadaVO.setCorreoUsuario(rs.getString("USU_EMAIL")); 
                
                listadoEntradasNotificarRegistrador.add(rechazadaVO);
            }
            
            rs.close();
            ps.close();
        
        
        return listadoEntradasNotificarRegistrador;
        } catch (Exception e){
            m_Log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(), e);
        }
    }   
    
    public ArrayList<EntradaRechazadaValueObject> getListaEntradasRechazadasNotificarOrg(Connection con) throws TechnicalException {
        m_Log.debug("--> getListaNotificarRechazadas()");
        ArrayList<EntradaRechazadaValueObject> listadoEntradasRechazadasOrganizacion = new ArrayList();
        ArrayList<EntradaRechazadaValueObject> listaFinalNotificar = new ArrayList(); 
        ArrayList<EntradaRechazadaValueObject> listaAuxNotificar = new ArrayList();
        listadoEntradasRechazadasOrganizacion=getListaEntradasRechazadasOrganizacion(con);
        
       
        // PRIMERO SE DESCARTAN LAS QUE HAN SIDO RECHAZADAS VARIAS VECES, NO QUEDAMOS CON EL ÚLTIMO RECHAZO
        for (EntradaRechazadaValueObject entRechazadaAuxVO: listadoEntradasRechazadasOrganizacion){
            if(!listaAuxNotificar.contains(entRechazadaAuxVO)){
                    listaAuxNotificar.add(entRechazadaAuxVO);
            }
        }
        // DESPUÉS DE LOS ÚLTIMOS RECHAZOS SE COMPRUEBA SI HAN PASADO MÁS DE 5 DIAS 
        for(EntradaRechazadaValueObject entradaRechazadaVO: listaAuxNotificar){
            if(calculoDiasRechazo(entradaRechazadaVO)){
                listaFinalNotificar.add(entradaRechazadaVO);
            }
        }
        m_Log.debug("size entradas notificadas : "+listadoEntradasRechazadasOrganizacion.size());
        m_Log.debug("size entradas notificadas Final " +listaFinalNotificar.size());
        return listaFinalNotificar;
    }
    
    
    public ArrayList<EntradaRechazadaValueObject> getListaEntradasRechazadasOrganizacion(Connection con) throws TechnicalException {
        m_Log.debug("---> getListaNotificarRechazadasOrganizacion()");
        ArrayList<EntradaRechazadaValueObject> listadoEntradasRechazadasOrganizacion = new ArrayList();
        String sql = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{

            
            sql = "SELECT RES_NUM, RES_EJE, R_HISTORICO_PREV.FECHA"
                  + " FROM R_RES, R_HISTORICO_PREV"
                  + " WHERE SUBSTR(R_HISTORICO_PREV.CODENTIDAD, 12,12) = R_RES.RES_NUM"
                  + " AND SUBSTR(R_HISTORICO_PREV.CODENTIDAD, 7,4) = R_RES.RES_EJE"
                  + " AND SUBSTR (R_HISTORICO_PREV.CODENTIDAD, 5,1) = R_RES.RES_TIP"
                  + " AND RES_EST = 2 "  // ESTADO RECHAZADO
                  + " AND RES_TIP = 'E'"
                  + " AND R_HISTORICO_PREV.TIPOMOV = 6"  // 6 TIPO MOVIMIENTO "RECHAZAR" EN R_HISTORICO_PREV
                  + " ORDER BY RES_EJE DESC, RES_NUM DESC,FECHA DESC";  
            m_Log.debug(sql);
            
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                EntradaRechazadaValueObject entradaRechazadaVO = new EntradaRechazadaValueObject();
                entradaRechazadaVO.setEjercicio(rs.getInt("RES_EJE"));
                entradaRechazadaVO.setNumReg(rs.getLong("RES_NUM"));
                
                java.sql.Date dateRechazo = rs.getDate("FECHA");
                Calendar calendarRechazo = Calendar.getInstance();
                calendarRechazo.setTimeInMillis(dateRechazo.getTime());
                entradaRechazadaVO.setFechaRechazo(calendarRechazo);

                listadoEntradasRechazadasOrganizacion.add(entradaRechazadaVO);
                        
                
            }
            return listadoEntradasRechazadasOrganizacion;
            
        } catch (Exception e){
            m_Log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(), e);
        }
   
    }
    
    public Boolean calculoDiasRechazo (EntradaRechazadaValueObject rechazadaVO){
        Boolean notificar = false;
        int dias = Integer.parseInt(m_ConfigTechnical.getString("dias_notificar_rechazadas_pendientes_fin_organizacion"));
        DateTime fechaActual = new DateTime(Calendar.getInstance());
        DateTime fechaRechazo = new DateTime(rechazadaVO.getFechaRechazo());
        int daysBetween = Days.daysBetween(fechaRechazo, fechaActual).getDays();
        if(daysBetween>dias){
            notificar = true;
        }
        return notificar;
    }
    
}
