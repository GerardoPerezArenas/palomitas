
package es.altia.agora.interfaces.user.web.registro.notificacion.batch;

import es.altia.agora.business.registro.EntradaRechazadaValueObject;
import es.altia.agora.business.registro.notificacion.dao.NotificacionEntradasRechazadasDAO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.mail.MailHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 *
 * @author altia
 */
public class NotificacionEntradasRechazadasManager {
    
    private static NotificacionEntradasRechazadasManager instance = null;
    
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    protected static Config m_ConfigError;
    protected static Log m_Log = LogFactory.getLog(NotificacionEntradasRechazadasManager.class.getName());
    
    protected NotificacionEntradasRechazadasManager(){
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }
    
    public static NotificacionEntradasRechazadasManager getInstance(){
        if(instance == null) {
            synchronized(NotificacionEntradasRechazadasManager.class){
                if (instance == null) {
                    instance = new NotificacionEntradasRechazadasManager();
                }
            }
        }
        return instance;
    }
    
  /**
   * notificarRechazadasRegistrador 
   * @param params
   * @throws SQLException 
   */
    
    public void notificarRechazadasRegistrador(String[] params) throws SQLException{
        m_Log.debug("-->notificarRechazadasRegistrador()");
        NotificacionEntradasRechazadasDAO notificacionDAO = NotificacionEntradasRechazadasDAO.getInstance();
        ArrayList<EntradaRechazadaValueObject> listadoEntradasRechazadasNotif = new ArrayList();
        
        Connection con = null;
        AdaptadorSQLBD  oad = null;
        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);
            
            listadoEntradasRechazadasNotif = notificacionDAO.getListaNotificarRechazadasRegistrador(con);
            
            String asuntoMail = m_ConfigTechnical.getString("mail.entradaRechazada.subject");
            String contenidoMail = m_ConfigTechnical.getString("mail.entradaRechazada.content");
            m_Log.info("----- Se va a enviar el siguiente mail:");
            m_Log.info("----- ----- Asunto: " + asuntoMail);
            m_Log.info("----- ----- Contenido: " + contenidoMail);
            m_Log.info("----- ----- A los siguientes destinatarios: ");
            for(EntradaRechazadaValueObject rechazadaVO: listadoEntradasRechazadasNotif){
                sendMailRegistrador(rechazadaVO);
                
            }
            
            oad.finTransaccion(con);
        } catch (Exception e){
            try {
                oad.rollBack(con);
            }catch(Exception ex){
                if(m_Log.isDebugEnabled()) m_Log.debug("Fallo al hacer rollback");
                ex.printStackTrace();
            }
            if(m_Log.isDebugEnabled()) m_Log.debug("Fallo durante la operación");
            e.printStackTrace();
        } finally {
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
     * 
     * @param params
     * @throws SQLException 
     */
    public void notificarRechazadasOrganizacion(String[] params) throws SQLException{
        m_Log.debug("-->notificarRechazadasOrganizacion()");
         NotificacionEntradasRechazadasDAO notificacionDAO = NotificacionEntradasRechazadasDAO.getInstance();
        ArrayList<EntradaRechazadaValueObject> listadoEntradasRechazadasNotif = new ArrayList();
        
        Connection con = null;
        AdaptadorSQLBD  oad = null;
        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);
            
            listadoEntradasRechazadasNotif = notificacionDAO.getListaEntradasRechazadasNotificarOrg(con);
                     
            sendMailOrganizacion(listadoEntradasRechazadasNotif);
                
            oad.finTransaccion(con);
        } catch (Exception e){
            try {
                oad.rollBack(con);
            }catch(Exception ex){
                if(m_Log.isDebugEnabled()) m_Log.debug("Fallo al hacer rollback");
                ex.printStackTrace();
            }
            if(m_Log.isDebugEnabled()) m_Log.debug("Fallo durante la operación");
            e.printStackTrace();
        } finally {
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
     * 
     * @param rechazadaVO 
     */
    private void sendMailRegistrador (EntradaRechazadaValueObject rechazadaVO){
        m_Log.info("-----> sendMailRegistrador()::INI");
        String emailDestino = rechazadaVO.getCorreoUsuario();
        m_Log.info("----- ----- ----- Email: " + emailDestino);
        int ejercicio = rechazadaVO.getEjercicio();
        long numeroAnotacion = rechazadaVO.getNumReg();
        if(emailDestino!=null) {
            String asuntoMail = m_ConfigTechnical.getString("mail.entradaRechazada.subject");
            asuntoMail = asuntoMail.replaceAll("@numeroAnotacion@",Long.toString(numeroAnotacion));
            asuntoMail = asuntoMail.replaceAll("@ejercicio@",Integer.toString(ejercicio));
            
            String contenidoMail = m_ConfigTechnical.getString("mail.entradaRechazada.content");
            contenidoMail = contenidoMail.replaceAll("@numeroAnotacion@",Long.toString(numeroAnotacion));
            contenidoMail = contenidoMail.replaceAll("@ejercicio@", Integer.toString(ejercicio));
            
            /*MailHelper mailer = new MailHelper();
            try {
                mailer.sendMail(emailDestino, asuntoMail, contenidoMail);
            }catch (Exception e){
                //error durante el envio
                e.printStackTrace();
                m_Log.error("Error al enviar e-mail notificación entrada rechazada " +ejercicio+"/"+numeroAnotacion+": "+e.getMessage());
            }*/
        } else {
            m_Log.debug("No existe correo");
        }
        m_Log.info("-----> sendMailRegistrador()::FIN");
    }
   
    
    /**
     * 
     * @param listadoEntradasRechazadasNotificarOrg 
     */
    private void sendMailOrganizacion (ArrayList<EntradaRechazadaValueObject> listadoEntradasRechazadasNotificarOrg){
        m_Log.info("---> sendMailOrganizacion()::INI");
        
        String strEmailOrganizacion = m_ConfigTechnical.getString("mail.entradasRecehazadas.organizacion");
        String[] emailOrganizacion = strEmailOrganizacion.split(";");
        m_Log.info("----- Se va a enviar el siguiente mail:");
        if(emailOrganizacion.length>0){
            String asuntoMailOrganizacion = m_ConfigTechnical.getString("mail.entradasRechazadas.subject.organizacion");
            m_Log.info("----- ----- Asunto: " + asuntoMailOrganizacion);
            
            StringBuilder contenido= new StringBuilder();
            contenido.append(m_ConfigTechnical.getString("mail.entradasRechazadas.content1.organizacion")+"\n");
            for(EntradaRechazadaValueObject entradaRechazadaVO: listadoEntradasRechazadasNotificarOrg){
                String textoMail = m_ConfigTechnical.getString("mail.entradasRechazadas.content2.organizacion");
                textoMail = textoMail.replaceAll("@ejercicio@", Integer.toString(entradaRechazadaVO.getEjercicio()) );
                textoMail = textoMail.replaceAll("@numeroAnotacion@",Long.toString(entradaRechazadaVO.getNumReg()));
                SimpleDateFormat formatter =new SimpleDateFormat("dd/MM/yyyy"); 
                textoMail = textoMail.replaceAll("@fechaRechazo@", formatter.format(entradaRechazadaVO.getFechaRechazo().getTime()));
                contenido.append(textoMail+"\n");
            }
            m_Log.info("----- ----- Contenido: " + contenido.toString());
            m_Log.info("----- ----- A los siguientes destinatarios: " + strEmailOrganizacion);
            /*MailHelper mailer = new MailHelper();
            try{
                for(int i=0; i<emailOrganizacion.length; i++){
                    mailer.sendMail(emailOrganizacion[i], asuntoMailOrganizacion, contenido.toString());
                }
            } catch (Exception e) {
                //error durante el envio
                e.printStackTrace();
                m_Log.error("Error al enviar e-mail notificación entradas rechazadas a organización: "+e.getMessage());
            }*/
        } else {
            m_Log.debug("No existe correo");
        }
        m_Log.info("---> sendMailOrganizacion()::FIN");
    }
}
