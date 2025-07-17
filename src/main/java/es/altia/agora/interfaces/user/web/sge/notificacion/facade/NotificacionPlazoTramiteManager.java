/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.interfaces.user.web.sge.notificacion.facade;

import es.altia.agora.business.sge.notificacion.NotificacionPlazoTramiteCVO;
import es.altia.agora.business.sge.notificacion.dao.NotificacionPlazoTramiteDAO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.mail.MailHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 *
 * @author mjesus.lopez
 */
public class NotificacionPlazoTramiteManager {
  private static NotificacionPlazoTramiteManager instance = null;
      protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
  protected static Config m_ConfigError;
  protected static Log m_Log =
          LogFactory.getLog(NotificacionPlazoTramiteManager.class.getName());

  protected NotificacionPlazoTramiteManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static NotificacionPlazoTramiteManager getInstance() {
    if (instance == null) {
      synchronized(NotificacionPlazoTramiteManager.class) {
        if (instance == null) {
          instance = new NotificacionPlazoTramiteManager();
        }
      }
    }
    return instance;
  }


   public void notificarPlazoTramites(String[] params) throws SQLException{
      m_Log.debug("params "+params[0]+" -- "+params[6]);
      NotificacionPlazoTramiteDAO notificacionDAO = NotificacionPlazoTramiteDAO.getInstance();
      Connection con=null;
      AdaptadorSQLBD oad = null;
      String subject=null;
      String texto=null;
       try {
        oad = new AdaptadorSQLBD(params);
		con = oad.getConnection();
        oad.inicioTransaccion(con);


          NotificacionPlazoTramiteCVO[] infoNotiTramites=notificacionDAO.getTramitesNotificacionPlazo(con);
          m_Log.debug("**   infoNotiTramites.length "+infoNotiTramites.length);

       for(int i=0;i<infoNotiTramites.length;i++){

           //(int codOrg,String codProc,int codTra,int ejercicio,String numExp,int ocurrencia,boolean estNotif, Connection con)
          int codOrg=infoNotiTramites[i].getCodOrganizacion();
          String codProc=infoNotiTramites[i].getCodProcedimiento();
          int codTra=infoNotiTramites[i].getCodTramite();
          int ejercicio=infoNotiTramites[i].getEjercicio();
          String numExp=infoNotiTramites[i].getNumeroExpediente();
          int ocurrencia=infoNotiTramites[i].getOcurrencia();
          int estadoTramite;//var auxiliar que me indica si es un tra fuera de plazo o cercano a fin de plazo
          String nombreTramite=infoNotiTramites[i].getNombreTramite();
          String notUOR = infoNotiTramites[i].getNotificarUORFinPlazo();	
          String emailUOR=infoNotiTramites[i].getEmailUtr();	
          String notUsuTramite = infoNotiTramites[i].getNotificarUsuTraFinPlazo();	
          String emailUsuTramite = infoNotiTramites[i].getEmailUsuarioTramite();	
          String notUsuExpediente = infoNotiTramites[i].getNotificarUsuExpFinPlazo();	
          String emailUsuExpediente = infoNotiTramites[i].getEmailUsuarioExpediente();

          Calendar fechaLimite=infoNotiTramites[i].getFechaLimite();
          Calendar fechaInicio=infoNotiTramites[i].getFechaInicio();
          int porcentajeFin=infoNotiTramites[i].getPorcentajeCercaFinPlazo();

          String cercaPlazo=calculoCercaFinPlazo(porcentajeFin,fechaInicio,fechaLimite);

             m_Log.debug("**** El expediente " + numExp + " está cerca fin de plazo:  "+cercaPlazo);


           int diarioCercaFinPlazo = infoNotiTramites[i].getTipoNotCercaFinPlazo();
           int diarioFueraDePlazo = infoNotiTramites[i].getTipoNotFueraDePlazo();
           m_Log.debug("diarioCercaFinPlazo "+diarioCercaFinPlazo);
           m_Log.debug("is aviso "+!infoNotiTramites[i].isAvisadoCercaFinPlazo());
           m_Log.debug("EXPEDIENTE: " + infoNotiTramites[i].getNumeroExpediente());
           

           if("si".equals(cercaPlazo) && infoNotiTramites[i].getNotificarCercaFinPlazo() && (diarioCercaFinPlazo==2 || (diarioCercaFinPlazo==1 && !infoNotiTramites[i].isAvisadoCercaFinPlazo()))){
                  estadoTramite=1;
                  if(notUOR.equals("S") && emailUOR!=null){	
                    sendMail(nombreTramite,emailUOR,numExp,estadoTramite);	
            	
                    m_Log.debug("-envio mail envio mail cerca de fin de plazo para tramite " + nombreTramite + ", exp: " + numExp + ", estadoTramite: " + estadoTramite + ", email: " + emailUOR);	
                  }	
                  if(notUsuTramite.equals("S") && emailUsuTramite!=null){	
                    sendMail(nombreTramite,emailUsuTramite,numExp,estadoTramite);	
            	
                    m_Log.debug("-envio mail envio mail cerca de fin de plazo para tramite " + nombreTramite + ", exp: " + numExp + ", estadoTramite: " + estadoTramite + ", email: " + emailUsuTramite);	
                  }	
                  if(notUsuExpediente.equals("S") && emailUsuExpediente!=null){	
                    sendMail(nombreTramite,emailUsuExpediente,numExp,estadoTramite);	
            	
                    m_Log.debug("-envio mail envio mail cerca de fin de plazo para tramite " + nombreTramite + ", exp: " + numExp + ", estadoTramite: " + estadoTramite + ", email: " + emailUsuExpediente);	
                  }
                  notificacionDAO.setEstadoNotCercaFinPlazo(codOrg, codProc, codTra, ejercicio, numExp, ocurrencia, true, con);
           }//fin si
             
           if("no".equals(cercaPlazo) && infoNotiTramites[i].getNotificarFueraDePlazo() && (diarioFueraDePlazo==2 || (diarioFueraDePlazo==1 && !infoNotiTramites[i].isAvisadoFueraDePlazo()))){
                estadoTramite=2;
  if(notUOR.equals("S") && emailUOR!=null ){	
                    sendMail(nombreTramite,emailUOR,numExp,estadoTramite);	
            	
                    m_Log.debug("-envio mail envio mail fuera de plazo para tramite " + nombreTramite + ", exp: " + numExp + ", estadoTramite: " + estadoTramite + ", email: " + emailUOR);	
                  }	
                if(notUsuTramite.equals("S") && emailUsuTramite!=null){	
                    sendMail(nombreTramite,emailUsuTramite,numExp,estadoTramite);	
            	
                    m_Log.debug("-envio mail envio mail fuera de fin de plazo para tramite " + nombreTramite + ", exp: " + numExp + ", estadoTramite: " + estadoTramite + ", email: " + emailUsuTramite);	
                  }	
                  if(notUsuExpediente.equals("S") && emailUsuExpediente!=null){	
                    sendMail(nombreTramite,emailUsuExpediente,numExp,estadoTramite);	
            	
                    m_Log.debug("-envio mail envio mail fuera de fin de plazo para tramite " + nombreTramite + ", exp: " + numExp + ", estadoTramite: " + estadoTramite + ", email: " + emailUsuExpediente);	
                  }
               notificacionDAO.setEstadoNotFueraDePlazo(codOrg, codProc, codTra, ejercicio, numExp, ocurrencia, true, con);
           }//fin si
       }//fin for
             oad.finTransaccion(con);


        } catch (Exception e) {
            try {
                // Hacer rollback de la transaccion
                oad.rollBack(con);
            } catch (Exception ex) {
                if (m_Log.isDebugEnabled()) m_Log.debug("Fallo al hacer rollback");
                ex.printStackTrace();
            }
            if (m_Log.isDebugEnabled()) m_Log.debug("Fallo durante la operacion");
            e.printStackTrace();
        } finally {
            try {
                // Devolver la conexion
                if (con != null && !con.isClosed()) con.close();
            } catch (SQLException e) {
                if (m_Log.isDebugEnabled()) m_Log.debug("Fallo al devolver la conexion");
                e.printStackTrace();
            }
        }

   }


      public String calculoCercaFinPlazo(int plazoCercaFin, Calendar fechaInicioPlazo, Calendar fechaLimite) {
        String fuera = "";
        //calculo el dia de inicio de plazo
        int diaIni = 0;
        int horas;
        int diaP = 0;
        String[] dia = null;

        if (fechaInicioPlazo == null) return "sa";
      //m_Log.debug(" ********  fechaInicioPlazo : "+ fechaInicioPlazo.getTime().toString());
      // m_Log.debug(" ********  fechaLimite : "+ fechaLimite.getTime().toString());

         fechaLimite.add(Calendar.DATE, 1);

        // m_Log.debug(" ********  fechaLimite mas un dia : "+ fechaLimite.getTime().toString());
        //Realizo la operación
        long time = fechaLimite.getTimeInMillis() - fechaInicioPlazo.getTimeInMillis();
        //Muestro el resultado en días
       // m_Log.debug(" ********   dias de diferencia : " + time / (3600 * 24 * 1000));
        int d = (int) (time / (3600 * 24 * 1000));

       // m_Log.debug("- dia ENTRE INICIO Y FIN : " + d);

        //CALCULO DE CUANTAS HORAS DEBEN PASAR PARA QUE LA APLICACION EMPIECE A AVISAR:
        //la formula seria: (24 horas * dia de inicio de plazo )/ 100 *Porcentaje de aviso(plazoCercaFin)=
        //numero de horas que tienen que pasar para avisar

        //m_Log.debug(" ********   porcentaje para empezar avisa : " + plazoCercaFin);
        horas = ((24 * d * plazoCercaFin) / 100);
        //m_Log.debug(" ********   horas antes que debe avisar : " + horas);
        //calculo diap=cuantos dias antes tiene que empezar avisar
        //diaP = horas / 24;

        diaP =(int)Math.ceil(((double)horas/(double)24));
        //diaP = (int)Math.ceil((double)horas / (double)24));
        
        m_Log.debug(" ********   horas antes que debe avisar : " + diaP);
        //si diap es = tengo que avisar el mismo dia que finaliza
        //calculo la fecha para ello covierto a calendar el string
        //Obtengo la fecha actual.
        Calendar calendario = Calendar.getInstance();

        // Obtengo la fecha para comenzar a avisar.
        Calendar comenzarAviso= Calendar.getInstance();
        comenzarAviso.setTimeInMillis(fechaLimite.getTimeInMillis());
        comenzarAviso.add(Calendar.HOUR, -horas);
         m_Log.debug(" ********  comenzarAviso : "+ comenzarAviso.getTime().toString());

        // Obtengo la fecha de fin de plazo
        Calendar calFinPlazo = Calendar.getInstance();
        calFinPlazo.setTimeInMillis(fechaLimite.getTimeInMillis());

         // m_Log.debug(" ********  comenzarAviso : "+ comenzarAviso.getTime().toString());
        
        if (calendario.after(comenzarAviso) && calendario.before(calFinPlazo)) fuera = "si";
        else if(calendario.after(calFinPlazo)) fuera = "no";


        // VALORES DE RETORNO POSIBLES.
        // fuera = "si" --> hay que avisar que el trámite está cercano a la finalización del plazo.
        // fuera = "no" -->  hay que avisar que el trámite está fuera de fin del plazo.
        m_Log.debug(" ********   fuera : " + fuera);
        return fuera;
    }

    

 private void sendMail(String nombreTramite, String emailUtr,String numExpediente,int estadoTramite) {

    /*******************************************************************/
    // Se envia el correo en caso de que este activado la casilla de notificacion cerca o fin de plazo en definicion de tramite
 
    m_Log.debug(">>>>>>>destinatario del mail: " + emailUtr);
    if (emailUtr != null) {
        m_Log.debug("ENTRA POR RAMA IF");
        // Se recupera el asunto y mensaje del correo
        String asunto = m_ConfigTechnical.getString("mail.subject").replaceAll("@expediente@", numExpediente);

        String mensaje = "";
        if(estadoTramite==1){
               mensaje = m_ConfigTechnical.getString("mail.notificacionTraCercaFinPlazo").replaceAll("@nombre@",nombreTramite );

        }else if(estadoTramite==2){
             mensaje = m_ConfigTechnical.getString("mail.notificacionTraFueraPlazo").replaceAll("@nombre@",nombreTramite );

        }
        m_Log.debug("Mensaje de notificación: " + mensaje);
        m_Log.debug("Asunto del mail: " + asunto);

        MailHelper mailer = new MailHelper();
        try {
            mailer.sendMail(emailUtr, asunto, mensaje);
        } catch (Exception e) {
            // Si ocurre algún error durante el envío del correo
            e.printStackTrace();
            m_Log.error("Error al enviar e-mail con notificación de cercano o fin de plazo " + nombreTramite + ": " + e.getMessage());
        }
    } else {
        m_Log.debug("ENTRA POR RAMA ELSE");
    }
// una vez inicializado el expediente => Se envía el correo 
/*******************************************************************/
}




}
