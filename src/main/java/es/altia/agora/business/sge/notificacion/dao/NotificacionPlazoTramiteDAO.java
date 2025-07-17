/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.business.sge.notificacion.dao;

import es.altia.agora.business.sge.notificacion.NotificacionPlazoTramiteCVO;
import es.altia.common.exception.TechnicalException;
import es.altia.agora.business.util.GlobalNames;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author mjesus.lopez
 */
public class NotificacionPlazoTramiteDAO {

    private static NotificacionPlazoTramiteDAO instance = null;
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");; //Para el fichero de configuracion tecnico
	protected static Config m_ConfigError; //Para los mensajes de error localizados
	protected static Log m_Log = LogFactory.getLog(NotificacionPlazoTramiteDAO.class.getName());

 public static NotificacionPlazoTramiteDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized (NotificacionPlazoTramiteDAO.class) {
                if (instance == null) {
                    instance = new NotificacionPlazoTramiteDAO();
                }
            }
        }
        return instance;
    }

 public NotificacionPlazoTramiteCVO[] getTramitesNotificacionPlazo(Connection con) throws TechnicalException{
     Vector tramitesNotPlazoTramite=new Vector();
     String sql=null;
     PreparedStatement ps = null;
     ResultSet rs = null;
        try {

            sql="SELECT CRO_MUN,CRO_PRO,CRO_TRA,CRO_EJE,CRO_NUM,CRO_OCU,CRO_FIP,CRO_FLI,CRO_UTR,CRO_AVISADOCFP,CRO_AVISADOFDP,TRA_NOTCERCAFP,TRA_NOTFUERADP," +
                    "TRA_TIPNOTCFP,TRA_TIPNOTFDP,TRA_FIN,TML_VALOR,UOR_EMAIL, TRA_NOTIF_USUTRA_FINPLAZO, TRA_NOTIF_USUEXP_FINPLAZO, TRA_NOTIF_UOR_FINPLAZO " +
                    "FROM E_CRO JOIN E_TRA ON (TRA_MUN = CRO_MUN AND TRA_PRO = CRO_PRO AND TRA_COD = CRO_TRA)" +
                    "JOIN E_TML ON (TRA_MUN = TML_MUN AND TRA_PRO = TML_PRO AND TRA_COD = TML_TRA AND TML_CMP = 'NOM' AND TML_LENG = '"+
                    m_ConfigTechnical.getString("idiomaDefecto")+"')" +
                    "JOIN A_UOR ON (UOR_COD = CRO_UTR)WHERE CRO_FEF IS NULL AND CRO_FLI IS NOT NULL AND (TRA_NOTFUERADP <> 0 OR TRA_NOTCERCAFP <> 0)";
    
             ps = con.prepareStatement(sql);
             rs = ps.executeQuery();
             while (rs.next()) {
                NotificacionPlazoTramiteCVO notPla=new NotificacionPlazoTramiteCVO();
                notPla.setCodOrganizacion(rs.getInt("CRO_MUN"));
                notPla.setCodProcedimiento(rs.getString("CRO_PRO"));
                notPla.setCodTramite(rs.getInt("CRO_TRA"));
                notPla.setEjercicio(rs.getInt("CRO_EJE"));
                notPla.setNumeroExpediente(rs.getString("CRO_NUM"));
                notPla.setOcurrencia(rs.getInt("CRO_OCU"));
                 
                try{ //Se han dado casos donde la fecha de inicio de plazo es null y falla todo el proceso
                java.sql.Date dateInicioPlazo = rs.getDate("CRO_FIP");
                Calendar calendarInicio = Calendar.getInstance();
                calendarInicio.setTimeInMillis(dateInicioPlazo.getTime());
                notPla.setFechaInicio(calendarInicio);

                java.sql.Date dateLimPlazo = rs.getDate("CRO_FLI");
                Calendar calendarLimite = Calendar.getInstance();
                calendarLimite.setTimeInMillis(dateLimPlazo.getTime());
                notPla.setFechaLimite(calendarLimite);

                } catch (Exception e)
                {
                    m_Log.error("Existe una fecha nula");
                    continue;
                }
                notPla.setUtr(rs.getInt("CRO_UTR"));
                notPla.setAvisadoCercaFinPlazo(rs.getBoolean("CRO_AVISADOCFP"));
                notPla.setAvisadoFueraDePlazo(rs.getBoolean("CRO_AVISADOFDP"));
                notPla.setNotificarCercaFinPlazo(rs.getBoolean("TRA_NOTCERCAFP"));
                notPla.setNotificarFueraDePlazo(rs.getBoolean("TRA_NOTFUERADP"));
                notPla.setTipoNotCercaFinPlazo(rs.getInt("TRA_TIPNOTCFP"));
                notPla.setTipoNotFueraDePlazo(rs.getInt("TRA_TIPNOTFDP"));
                notPla.setPorcentajeCercaFinPlazo(rs.getInt("TRA_FIN"));
                notPla.setNombreTramite(rs.getString("TML_VALOR"));
                notPla.setEmailUtr(rs.getString("UOR_EMAIL"));
              
                notPla.setNotificarUsuTraFinPlazo(rs.getString("TRA_NOTIF_USUTRA_FINPLAZO"));	
                notPla.setNotificarUsuExpFinPlazo(rs.getString("TRA_NOTIF_USUEXP_FINPLAZO"));	
                notPla.setNotificarUORFinPlazo(rs.getString("TRA_NOTIF_UOR_FINPLAZO"));	
                	
                	
                if(notPla.getNotificarUsuTraFinPlazo().equals("S")){	
                   String usuarioTraEmail=getCorreoUsuarioInicioTramite(notPla, con);	
                        if(!(usuarioTraEmail==null) && !(usuarioTraEmail.equals(""))){	
                            notPla.setEmailUsuarioTramite(usuarioTraEmail);	
                        }	
                 }	
                    	
                if(notPla.getNotificarUsuExpFinPlazo().equals("S")){	
                   String usuarioExpEmail=getCorreoUsuarioInicioExpediente(notPla, con);	
                        if(!(usuarioExpEmail==null) && !(usuarioExpEmail.equals(""))){	
                            notPla.setEmailUsuarioExpediente(usuarioExpEmail);	
                        }
                   	
                }

                tramitesNotPlazoTramite.add(notPla);

            }
             rs.close();
             ps.close();



       NotificacionPlazoTramiteCVO[] notPlaTra=new NotificacionPlazoTramiteCVO[tramitesNotPlazoTramite.size()];
        for(int i=0;i<tramitesNotPlazoTramite.size();i++){
            NotificacionPlazoTramiteCVO not=(NotificacionPlazoTramiteCVO)tramitesNotPlazoTramite.elementAt(i);
            notPlaTra[i]=not;
        }

        return notPlaTra;

        } catch (Exception e) {
            m_Log.error(e.getMessage());
                      m_Log.debug(sql);
            throw new TechnicalException(e.getMessage(), e);
        }
    }
 
 
  public String getCorreoUsuarioInicioTramite(NotificacionPlazoTramiteCVO notPlaVO, Connection con)throws TechnicalException{
      String usuarioTraEmail="";
      PreparedStatement ps = null;
      ResultSet rs = null;
      String sql="";
      try{
             sql = "SELECT USU_EMAIL FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU A_USU" 
                  + " INNER JOIN E_CRO ON (A_USU.USU_COD = E_CRO.CRO_USU ) WHERE E_CRO.CRO_NUM ='"+notPlaVO.getNumeroExpediente()+"' AND E_CRO.CRO_TRA ='"+notPlaVO.getCodTramite()+"'";
             ps = con.prepareStatement(sql);
             rs = ps.executeQuery();
             while (rs.next()) {
                usuarioTraEmail = rs.getString("USU_EMAIL");
             
                if(m_Log.isDebugEnabled()) m_Log.debug("USUARIO INICIO TRAMITE MAIL: "+usuarioTraEmail);
             }
            ps.close();
      } catch (Exception e) {
            m_Log.error(e.getMessage());
            m_Log.debug(sql);
            throw new TechnicalException(e.getMessage(), e);
        }
      return  usuarioTraEmail;
 }

 
  public String getCorreoUsuarioInicioExpediente(NotificacionPlazoTramiteCVO notPlaVO, Connection con)throws TechnicalException{
      String usuarioExpEmail="";
      PreparedStatement ps = null;
      ResultSet rs = null;
      String sql="";
      try{
             sql = "SELECT USU_EMAIL FROM " + GlobalNames.ESQUEMA_GENERICO +" a_usu a_usu"
                            + " INNER JOIN E_EXP ON (A_USU.USU_COD= E_EXP.EXP_USU )WHERE E_EXP.EXP_NUM ='"+ notPlaVO.getNumeroExpediente()+"'";
             ps = con.prepareStatement(sql);
             rs = ps.executeQuery();
             while (rs.next()) {
                usuarioExpEmail = rs.getString("USU_EMAIL");
 
                if(m_Log.isDebugEnabled()) m_Log.debug("USUARIO INICIO EXPEDIENTE MAIL: "+usuarioExpEmail);
             }
            ps.close();
      } catch (Exception e) {
            m_Log.error(e.getMessage());
            m_Log.debug(sql);
            throw new TechnicalException(e.getMessage(), e);
        }
      return  usuarioExpEmail;
 }

  public void setEstadoNotCercaFinPlazo(int codOrg,String codProc,int codTra,int ejercicio,String numExp,int ocurrencia,boolean estNotif, Connection con)
          throws TechnicalException{

         String sql=null;
         PreparedStatement ps = null;
         ResultSet rs = null;
        try {
            sql = "UPDATE E_CRO SET CRO_AVISADOCFP=? WHERE CRO_MUN=? AND CRO_PRO=? AND CRO_EJE=? AND CRO_NUM=? AND CRO_TRA=? AND CRO_OCU=?";
            
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setBoolean(i++, estNotif);
            ps.setInt(i++, codOrg);
            ps.setString(i++, codProc);
            ps.setInt(i++, ejercicio);
            ps.setString(i++, numExp);
            ps.setInt(i++, codTra);
            ps.setInt(i++,ocurrencia);
            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            m_Log.error(e.getMessage());
            if (m_Log.isDebugEnabled())m_Log.debug(sql);

            throw new TechnicalException(e.getMessage(), e);
        }

    }

   public void setEstadoNotFueraDePlazo(int codOrg,String codProc,int codTra,int ejercicio,String numExp,int ocurrencia,boolean estNotif, Connection con)
          throws TechnicalException{

    String sql=null;
         PreparedStatement ps = null;
         ResultSet rs = null;
        try {
            sql = "UPDATE E_CRO SET CRO_AVISADOFDP=? WHERE CRO_MUN=? AND CRO_PRO=? AND CRO_EJE=? AND CRO_NUM=? AND CRO_TRA=? AND CRO_OCU=?";
            

            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setBoolean(i++, estNotif);
            ps.setInt(i++, codOrg);
            ps.setString(i++, codProc);
            ps.setInt(i++, ejercicio);
            ps.setString(i++, numExp);
            ps.setInt(i++, codTra);
            ps.setInt(i++,ocurrencia);
            ps.executeUpdate();
            ps.close();


        } catch (Exception e) {
            m_Log.error(e.getMessage());
            if (m_Log.isDebugEnabled())m_Log.debug(sql);
            throw new TechnicalException(e.getMessage(), e);
        }

    }

}
