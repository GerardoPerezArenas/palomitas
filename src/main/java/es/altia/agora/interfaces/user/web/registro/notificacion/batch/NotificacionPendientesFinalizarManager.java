
package es.altia.agora.interfaces.user.web.registro.notificacion.batch;


import es.altia.agora.business.registro.notificacion.dao.NotificacionPendientesFinalizarDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.mail.MailHelper;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificacionPendientesFinalizarManager {
    private static NotificacionPendientesFinalizarManager instance = null;
    
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    protected static Log m_Log = LogFactory.getLog(NotificacionPendientesFinalizarManager.class.getName());
    
    protected NotificacionPendientesFinalizarManager (){
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        
    }
    
    public static NotificacionPendientesFinalizarManager getInstance(){
        if(instance == null){
            synchronized(NotificacionPendientesFinalizarManager.class){
                if (instance == null){
                    instance = new NotificacionPendientesFinalizarManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * notificarPendientesFinalizarRegistrador
     * @param params
     * throws SQLException
     */
     public void notificarPendientesFinalizarRegistrador(String[] params) throws SQLException{
         m_Log.debug("--> notificarPendientesFinalizarRegistrador: INICIO");
         
         NotificacionPendientesFinalizarDAO notificacionDAO = NotificacionPendientesFinalizarDAO.getInstance();
         ArrayList<GeneralValueObject> listadoPendientesFinalizar = new ArrayList();
         
         Connection con = null;
         AdaptadorSQLBD adapt = null;
         
         try{
             adapt = new AdaptadorSQLBD(params);
             con = adapt.getConnection();
             adapt.inicioTransaccion(con);
             
             listadoPendientesFinalizar = notificacionDAO.getListaNotificarPendientesFinalizarRegistrador(con);
             
             String asuntoMail = m_ConfigTechnical.getString("mail.pendienteFinDigitalizacion.subject");
             String contenidoMail = m_ConfigTechnical.getString("mail.pendienteFinDigitalizacion.content");
             m_Log.info("----- Se va a enviar el siguiente mail:");
             m_Log.info("----- ----- Asunto: " + asuntoMail);
             m_Log.info("----- ----- Contenido: " + contenidoMail);
             m_Log.info("----- ----- A los siguientes destinatarios: ");
             for(GeneralValueObject pendienteVO: listadoPendientesFinalizar){
                 sendMailRegistrador(pendienteVO);
             }
             adapt.finTransaccion(con);
         }catch(Exception e){
            if(m_Log.isDebugEnabled()) m_Log.debug("Fallo durante la operación");
            e.printStackTrace();
         }finally{
             try{
                if(con != null && !con.isClosed()) con.close();
             }catch (SQLException e){
                if(m_Log.isDebugEnabled()) m_Log.debug("Fallo al devolver la conexión");
                e.printStackTrace();
            }
         }
     }
     
     public void notificarPendientesFinalizarOrganizacion(String[] params) {
         m_Log.debug("-->notificarPendientesFinalizarOrganizacion(): INICIO");
         NotificacionPendientesFinalizarDAO notificacionDAO = NotificacionPendientesFinalizarDAO.getInstance();
         ArrayList<GeneralValueObject> listadoPendientesFinalizar = new ArrayList();
         
         Connection con = null;
         AdaptadorSQLBD adapt = null;
         try{
             adapt = new AdaptadorSQLBD(params);
             con = adapt.getConnection();
             adapt.inicioTransaccion(con);
             
             listadoPendientesFinalizar = notificacionDAO.getListaPendientesFinalizarOrganizacion(con);
             sendMailOrganizacion(listadoPendientesFinalizar);
             adapt.finTransaccion(con);
        } catch (BDException ex) {
           m_Log.debug(NotificacionPendientesFinalizarManager.class.getName()+" :"+ex.getMensaje());
        } catch(Exception e){
            m_Log.debug(e.getMessage());
        }finally {
            try{
            //devolvemos la conexión
                if(con != null && !con.isClosed()) con.close();
            } catch (SQLException e){
                if(m_Log.isDebugEnabled()) m_Log.debug("Fallo al devolver la conexión");
                e.printStackTrace();
            }
        }
     }
     
     /**
      * @param pendienteVO 
      */
     private void sendMailRegistrador(GeneralValueObject pendienteVO){
         m_Log.info("-----> sendMailRegistrador()::INI");
         
         String emailRegistrador = pendienteVO.getAtributo("correoUsuario").toString();
         m_Log.info("----- ----- ----- Email: " + emailRegistrador);
         String ejercicio = pendienteVO.getAtributo("ejercicio").toString();
         String numeroAnotacion = pendienteVO.getAtributo("numReg").toString();
         
         if(emailRegistrador!=null) {
             String asuntoMail = m_ConfigTechnical.getString("mail.pendienteFinDigitalizacion.subject");
             asuntoMail = asuntoMail.replaceAll("@numeroAnotacion@", numeroAnotacion);
             asuntoMail = asuntoMail.replaceAll("@ejercicio@", ejercicio);
             
             String contenidoMail = m_ConfigTechnical.getString("mail.pendienteFinDigitalizacion.content");
             contenidoMail = contenidoMail.replaceAll("@numeroAnotacion@", numeroAnotacion);
             contenidoMail = contenidoMail.replaceAll("@ejercicio@", ejercicio);
             
             /*MailHelper mailer = new MailHelper();
             try{
                 mailer.sendMail(emailRegistrador, asuntoMail, contenidoMail);
             }catch(Exception e){
                 e.printStackTrace();
                 m_Log.debug("Error al enviar e-mail notificacion entrada pendiente finalizar " +ejercicio+"/"+numeroAnotacion+": "+e.getMessage());
             }*/
         }else{
             m_Log.debug("No existe correo de usuario");
         }
         m_Log.info("-----> sendMailRegistrador()::FIN");
     }
     
     private void sendMailOrganizacion(ArrayList<GeneralValueObject> listadoPendientesOrg){
         m_Log.info("--> sendMailOrganizacion()::INI");
         
         String strEmailOrganizacion = m_ConfigTechnical.getString("mail.pendientesFinalizar.organizacion");
         String[] mailOrganizacion = strEmailOrganizacion.split(";");
         m_Log.info("----- Se va a enviar el siguiente mail:");
         if(mailOrganizacion.length>0){
             String asuntoMailOrganizacion = m_ConfigTechnical.getString("mail.pendientesFinalizarOrg.subject");
             m_Log.info("----- ----- Asunto: " + asuntoMailOrganizacion);
             StringBuilder contenido = new StringBuilder();
             contenido.append(m_ConfigTechnical.getString("mail.pendientesFinalizarOrg.content1")+"\n");
             for(GeneralValueObject pendienteVO: listadoPendientesOrg){
                 String textoMail = m_ConfigTechnical.getString("mail.pendientesFinalizarOrg.content2");
                 textoMail = textoMail.replaceAll("@ejercicio@", pendienteVO.getAtributo("ejercicio").toString());
                 textoMail = textoMail.replaceAll("@numeroAnotacion@", pendienteVO.getAtributo("numReg").toString());
                 SimpleDateFormat formatter = new SimpleDateFormat(DateOperations.LATIN_DATE_FORMAT);
                 Calendar fechaAnot = (Calendar) pendienteVO.getAtributo("fechaAnotacion");
                 textoMail = textoMail.replaceAll("@fechaAnotacion@", formatter.format(fechaAnot.getTime()));
                 contenido.append(textoMail+"\n");
             }
             m_Log.info("----- ----- Contenido: " + contenido.toString());
             m_Log.info("----- ----- A los siguientes destinatarios: " + strEmailOrganizacion);
             /*MailHelper mailer = new MailHelper();
             try{
                 for(int i=0; i<mailOrganizacion.length; i++){
                     mailer.sendMail(mailOrganizacion[i], asuntoMailOrganizacion, contenido.toString());
                 }
             } catch(Exception e){
                 //error durante el envio 
                 e.printStackTrace();
                 m_Log.error("Error al enviar e-mail notificación entradas pendientes finalizar digitalización a organización: "+e.getMessage());
             }*/
         } else {
             m_Log.debug("No existe correo de organización");
         }
         m_Log.info("--> sendMailOrganizacion()::FIN");
         
     }
}
