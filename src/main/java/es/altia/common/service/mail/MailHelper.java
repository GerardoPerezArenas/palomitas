package es.altia.common.service.mail;

import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.service.mail.exception.*;
import es.altia.common.exception.CriticalException;
import es.altia.common.service.log.LogStream;

import java.io.IOException;
import java.io.PrintStream;

import java.util.*;

import javax.activation.DataHandler;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Clase de ayuda para crear y envíar un mail
 */
public class MailHelper {

    public MailHelper() {
        //Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }

    public void createAndSendMail(String to, String subject,
                                  String htmlContents) throws MailException{
        try {
            if (m_Log.isDebugEnabled())  m_Log.debug("Sending message" +
                                            "\nTo: " + to +
                                            "\nSubject: " + subject +
                                            "\nContents: " + htmlContents);

            InitialContext ic = new InitialContext();
            Session session = (Session)ic.lookup( m_ConfigTechnical.getString("Mail.mail_session"));

            session.setDebug(true);

            // Se construye el mensaje
            Message msg = new MimeMessage(session);
            msg.setFrom();
            msg.setRecipients(Message.RecipientType.TO,
                              InternetAddress.parse(to, false));
            msg.setSubject(subject);
            collect(subject, htmlContents, msg);
            msg.setHeader("X-Mailer", "JavaMailer");
            msg.setSentDate(new Date());

            // send the thing off
            Transport.send(msg);

//            LogServiceHelper("\nMail sent successfully.");
        } catch (Exception e) {
        	  m_Log.error(e);
	          throw new MailException(m_ConfigError.getString("Error.Mail.Envio"), e);
        }
    }

    private void collect(String subject, String htmlContents, Message msg)
        throws MessagingException, IOException {

        StringBuffer sb = new StringBuffer();
        sb.append("<HTML>\n");
        sb.append("<head>\n");
        sb.append("<TITLE>\n");
        sb.append(subject + "\n");
        sb.append("</TITLE>\n");
        sb.append("</HEAD>\n");

        sb.append("<BODY bgcolor=\"#ffffff\" link=\"#d12f36\" vlink=\"#d12f36\" alink=\"#000000\">\n");

        sb.append(htmlContents);

        sb.append("</BODY>\n");
        sb.append("</HTML>\n");

        msg.setDataHandler(new DataHandler(new ByteArrayDataSource(sb.toString(), "text/html")));
    }

    public void sendMail(String email, String subject, String textoMensaje) throws MailException,
    MailServiceNotActivedException {
        sendMail(email,subject,textoMensaje, null);
    }
    
    /**
     *  Envia un mail
     */
    public void sendMail(String email, String subject, String textoMensaje, String from) throws MailException,
            MailServiceNotActivedException {

            String  mailCC="";
            String  mailBCC="";
            Transport transporte=null;
        //Si el servicio de mail no esta activado, se lanza una excepcion
        try {
            String servicioActivo = m_ConfigTechnical.getString("mail.service.active");
            if (!servicioActivo.equalsIgnoreCase("si")) {
                throw new MailServiceNotActivedException("Servicio de mail no activado");
            }
        } catch (CriticalException e) {
            throw new MailException(m_ConfigError.getString("Error.Mail.Envio"), e);
        }
        try {
             mailCC=m_ConfigTechnical.getString("mail.cc");
             mailBCC=m_ConfigTechnical.getString("mail.bcc");
             
        } catch (CriticalException e) {
            mailCC="";
            mailBCC="";
        }
                
        
        /********************************************************************************************************/
        /******** Se comprueba si hay una sessión para el envio de mails definida, por ejemplo para Weblogic ****/
        /*******  Si está definida, no es necesario recuperar los datos de conexión al servidor de SMTP      ****/
        /********************************************************************************************************/
        Session session = null;
        String puerto = null;
        boolean SESION_MAIL_DEFINIDO = false;
        try{
           String MAIL_SESSION = m_ConfigTechnical.getString("Mail.mail_session");
           m_Log.debug(" ==============================> ESTÁ CONFIGURADA UNA SESIÓN DE MAIL EN EL SERVIDOR CUYO NOMBRE ES: " + MAIL_SESSION);
           if(MAIL_SESSION!=null && !"".equals(MAIL_SESSION)){
               InitialContext ic = new InitialContext();
               session = (Session)ic.lookup(MAIL_SESSION);
               //session.getProperties().setProperty("mail.transport.protocol", "smtp");
               session.setDebug(true);      
               SESION_MAIL_DEFINIDO = true;
           }
        }catch(NamingException e){
            e.printStackTrace();
            m_Log.error(" =================================>  ERROR AL RECUPERAR EL SERVICIO DE NOMENCLATURA PARA ESTABLECER SESIÓN CON EL SERVIDOR SMTP::  " + e.getMessage());
            SESION_MAIL_DEFINIDO = false;
        }
        

        try {  
            m_Log.debug(" ==============================> email al que irá dirigido el correo "+email); 
            InternetAddress[] direccion = new InternetAddress[1];
            direccion[0] = new InternetAddress(email);
                        
            if (from == null) from = m_ConfigTechnical.getString("mail.from");
            
            if(!SESION_MAIL_DEFINIDO){
                
                
                String USAR_AUTENTICACION = m_ConfigTechnical.getString("mail.smtp.auth");
                
                m_Log.debug(" ==============================> NO ESTÁ CONFIGURADA UNA SESIÓN DE MAIL EN EL SERVIDOR, POR TANTO SE RECUPERAN LOS DATOS DE techserver");
                // Si no está definido un servicio de nomenclatura para el envio de correos electrónicos, se recuperan los
                // datos de conexión al servidor de las propiedades de configuración del techserver correspondientes.                
                //Inicializamos los datos de la sesion
                Properties properties = new Properties();
                // Si no se ha indicado remitente se coge el del fichero de config                
                puerto = m_ConfigTechnical.getString("mail.smtp.port");
                properties.put("mail.transport.protocol", "smtp");
                //properties.put("mail.mime.host", m_ConfigTechnical.getString("mail.mime.host"));
                properties.put("mail.smtp.host", m_ConfigTechnical.getString("mail.smtp.host"));            
                if (!"".equals(puerto)&&puerto!=null) properties.put("mail.smtp.port", Integer.parseInt(m_ConfigTechnical.getString("mail.smtp.port")));            
                properties.put("mail.smtp.auth", m_ConfigTechnical.getString("mail.smtp.auth"));

                m_Log.debug("From:\t" + from);
                m_Log.debug("Host:\t" + m_ConfigTechnical.getString("mail.smtp.host"));
                if (!"".equals(puerto)&&puerto!=null) m_Log.debug("Port:\t" + Integer.parseInt(m_ConfigTechnical.getString("mail.smtp.port")));
                m_Log.debug("Auth:\t" + m_ConfigTechnical.getString("mail.smtp.auth"));
                m_Log.debug("User:\t" + m_ConfigTechnical.getString("mail.user"));
                m_Log.debug("Pass:\t" + m_ConfigTechnical.getString("mail.password"));

                
                
                if(USAR_AUTENTICACION!=null && USAR_AUTENTICACION.equalsIgnoreCase("true")){
                    Authenticator authenticator = new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(m_ConfigTechnical.getString("mail.user"), m_ConfigTechnical.getString("mail.password"));
                        }
                    };
                    session = Session.getDefaultInstance(properties, authenticator);
                }
                else
                    session = Session.getDefaultInstance(properties);
                
                //session = Session.getDefaultInstance(properties, authenticator);    
                session.setDebug(true);
            }else
                m_Log.debug(" ==============================> LA CONFIGURACIÓN DE ENVIO DE CORREO SE OBTIENE DE LA SESIÓN DEL CORREO DEL SERVIDOR");
            
            
            /** ORIGINAL
            Session session = Session.getDefaultInstance(properties, authenticator);
            session.setDebug(true);
            ***/
            //salida de depuracion de JavaMail EL BLOQUE ESTARA COMENTADO POR DEFECTO PORQUE VUELCA MUCHA TRAZA Y ENSUCIA EL LOG, CUANDO SE NECESITE SE DESCOMENTARA
            /* 
            if(session != null) {
                m_Log.debug("JAVAMAIL debug esta activado");
                session.setDebugOut(new PrintStream(new LogStream(null)));
            }
            */
            
            Message mensaje = new MimeMessage(session);
            mensaje.setSubject(subject);
            BodyPart textoMensajeBody = new MimeBodyPart();
            textoMensajeBody.setText(textoMensaje);
            Multipart contenedor = new MimeMultipart();
            contenedor.addBodyPart(textoMensajeBody);

            mensaje.setContent(contenedor);
            mensaje.setSentDate(new Date());
            mensaje.setFrom(new InternetAddress(from));            
            transporte = session.getTransport();

            String addressTo = (String) email;
            Address addr = new InternetAddress(addressTo);
            mensaje.addRecipient(Message.RecipientType.TO,addr);

            mailCC=getRecipient("mail.cc");
            mailBCC=getRecipient("mail.bcc");

            StringTokenizer stCC = new StringTokenizer(mailCC);
            StringTokenizer stBCC = new StringTokenizer(mailBCC);
           
            while (stCC.hasMoreTokens()){
                String addressStr = (String) stCC.nextToken();
                Address address = new InternetAddress(addressStr);
                mensaje.addRecipient(Message.RecipientType.CC,address);
            }//while (stCC.hasMoreTokens())
           
            while (stBCC.hasMoreTokens()){
                String addressStr = (String) stBCC.nextToken();
                Address address = new InternetAddress(addressStr);
                mensaje.addRecipient(Message.RecipientType.BCC,address);
            }//while (stBCC.hasMoreTokens())
          
            m_Log.debug("MailHelper.sendMail antes de hacer transporte.connect");
            transporte.connect();
            
            transporte.sendMessage(mensaje,mensaje.getAllRecipients());
        } catch (Exception e) {      
            e.printStackTrace();
            m_Log.error(e.getMessage());
            m_Log.error(e.toString());            
            throw new MailException(m_ConfigError.getString("Error.Mail.Envio"), e);
        }//try-catch
        finally{
            if (transporte!=null)
                try {
                    transporte.close();
                }catch (MessagingException ex )
                {
                    m_Log.error(ex.getMessage());
                }
        }
    }//sendMail

     private String getRecipient(String tipo) throws CriticalException{

         String  mail="";

        try {
             mail=m_ConfigTechnical.getString(tipo);
             return mail;


        } catch (CriticalException e) {
            mail="";
            return mail;
        }
     }

    protected static Config m_ConfigTechnical;
    protected static Config m_ConfigError;
    protected static Log m_Log = LogFactory.getLog(MailHelper.class.getName());
    
    /**
     *  Envia un mail con un archivo adjunto
     */
    public void sendMailWithAttachments(String email, String subject, String textoMensaje, String from, ArrayList<Documento> listaDocumentos,
            ArrayList<String> destinatariosCCO, ArrayList<String> destinatarios, ArrayList<String> destinatariosCC) throws MailException,
            MailServiceNotActivedException {
        
        if(m_Log.isDebugEnabled()) m_Log.debug("sendMailWithAttachments() : BEGIN");
        String  mailCC="";
        String  mailBCC="";
        
        //Si el servicio de mail no esta activado, se lanza una excepcion
        if(m_Log.isDebugEnabled()) m_Log.debug("Comprobamos si el servicio de mail esta activado");
        try {
            String servicioActivo = m_ConfigTechnical.getString("mail.service.active");
            if (!servicioActivo.equalsIgnoreCase("si")){
                throw new MailServiceNotActivedException("Servicio de mail no activado");
            }//if (!servicioActivo.equalsIgnoreCase("si"))
        } catch (CriticalException e) {
            throw new MailException(m_ConfigError.getString("Error.Mail.Envio"), e);
        }//try-catch
        
        //Comprobamos si se ha recibido un ArrayList con direcciones para copia oculta
        if(m_Log.isDebugEnabled()) m_Log.debug("Comprobamos si el mail tiene direcciones de copia oculta");
        if(destinatariosCCO != null && destinatariosCCO.size() > 0){
            for(String destinatario : destinatariosCCO){
                mailBCC = destinatario;
                mailBCC +=";";
            }//for(String destinatario : destinatariosCCO)
            mailBCC = mailBCC.substring(0, mailBCC.length() - 1);
        }else{
            try {    
                mailBCC=m_ConfigTechnical.getString("mail.bcc");
            } catch (CriticalException e) {
                mailBCC="";
            }//try-catch
        }//if(destinatariosCCO != null && destinatariosCCO.size() > 0)
        
        //Comprobamos si se ha recibido un ArrayList con direcciones para copia
        if(m_Log.isDebugEnabled()) m_Log.debug("Comprobamos si el mail tiene direcciones de copia");
        if(destinatariosCC != null && destinatariosCC.size() > 0){
            for(String destinatario : destinatariosCC){
                mailCC = destinatario;
                mailCC +=";";
            }//for(String destinatario : destinatariosCCO)
            mailCC = mailCC.substring(0, mailCC.length() - 1);
        }else{
            try {
                mailCC=m_ConfigTechnical.getString("mail.cc");
            } catch (CriticalException e) {
                mailCC="";
            }//try-catch
        }//if(destinatariosCC != null && destinatariosCC.size() > 0)

        try {
            if(m_Log.isDebugEnabled()) m_Log.debug("Creamos el array de direcciones de email");
            InternetAddress[] direccion = new InternetAddress[1];
            direccion[0] = new InternetAddress(email);
            
            //Inicializamos los datos de la sesion
            Properties properties = new Properties();
            
            // Si no se ha indicado remitente se coge el del fichero de config
            if(m_Log.isDebugEnabled()) m_Log.debug("Si no se indica remitente lo cogemos del properties");
            if (from == null) from = m_ConfigTechnical.getString("mail.from");
            
            String puerto = m_ConfigTechnical.getString("mail.smtp.port");
            properties.put("mail.transport.protocol", "smtp");
            properties.put("mail.smtp.host", m_ConfigTechnical.getString("mail.smtp.host"));            
            if (!"".equals(puerto)&&puerto!=null) properties.put("mail.smtp.port", Integer.parseInt(m_ConfigTechnical.getString("mail.smtp.port")));            
            properties.put("mail.smtp.auth", m_ConfigTechnical.getString("mail.smtp.auth"));
            
            m_Log.debug("From:\t" + from);
            m_Log.debug("Host:\t" + m_ConfigTechnical.getString("mail.smtp.host"));
            if (!"".equals(puerto)&&puerto!=null) m_Log.debug("Port:\t" + Integer.parseInt(m_ConfigTechnical.getString("mail.smtp.port")));
            m_Log.debug("Auth:\t" + m_ConfigTechnical.getString("mail.smtp.auth"));
            m_Log.debug("User:\t" + m_ConfigTechnical.getString("mail.user"));
            m_Log.debug("Pass:\t" + m_ConfigTechnical.getString("mail.password"));

            if(m_Log.isDebugEnabled()) m_Log.debug("Creamos el objeto Authenticator");
            Authenticator authenticator = new Authenticator(){
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(m_ConfigTechnical.getString("mail.user"), m_ConfigTechnical.getString("mail.password"));
                }//protected PasswordAuthentication getPasswordAuthentication() 
            };//Authenticator authenticator = new Authenticator()
            
            if(m_Log.isDebugEnabled()) m_Log.debug("Creamos la parte de texto del cuerpo del correo");
            BodyPart textoMensajeBody = new MimeBodyPart();
                textoMensajeBody.setText(textoMensaje);
                
            if(m_Log.isDebugEnabled()) m_Log.debug("Creamos la parte de los adjuntos del cuerpo del correo");
            ArrayList<BodyPart> listaBodyPartAdjuntos = new ArrayList<BodyPart>();
            for(Documento doc : listaDocumentos){
                if(doc.getFichero() != null){
                    BodyPart adjunto = new MimeBodyPart();
                    DataHandler dth = new DataHandler(doc.getFichero(), doc.getTipoMimeContenido());
                    adjunto.setDataHandler(dth);
                    adjunto.setFileName(doc.getNombreDocumento() + "." + doc.getExtension());
                    listaBodyPartAdjuntos.add(adjunto);
                }//if(doc.getFichero() != null)
            }//for(Documento doc : listaDocumentos)
                
            //Unimos el texto con el adjunto
            MimeMultipart multiParte = new MimeMultipart();
                multiParte.addBodyPart(textoMensajeBody);
                for(BodyPart adjunto : listaBodyPartAdjuntos){
                    multiParte.addBodyPart(adjunto);
                }//for(BodyPart adjunto : listaBodyPartAdjuntos)
                
            Session session = Session.getDefaultInstance(properties, authenticator);
            Message mensaje = new MimeMessage(session);
            
            mensaje.setSubject(subject);
            mensaje.setSentDate(new Date());
            mensaje.setFrom(new InternetAddress(from));            
            
            String addressTo = (String) email;
            Address addr = new InternetAddress(addressTo);
            mensaje.addRecipient(Message.RecipientType.TO,addr);

            StringTokenizer stCC = new StringTokenizer(mailCC);
            StringTokenizer stBCC = new StringTokenizer(mailBCC, ";");
            
            while (stCC.hasMoreTokens()){
                String addressStr = (String) stCC.nextToken();
                Address address = new InternetAddress(addressStr);
                mensaje.addRecipient(Message.RecipientType.CC,address);
            }//while (stCC.hasMoreTokens())
           
            while (stBCC.hasMoreTokens()){
                String addressStr = (String) stBCC.nextToken();
                Address address = new InternetAddress(addressStr);
                mensaje.addRecipient(Message.RecipientType.BCC,address);
            }//while (stBCC.hasMoreTokens())
            
            mensaje.setContent(multiParte);
            
            Transport transporte = session.getTransport();
            if ("".equals(puerto)||puerto==null) {
                transporte.connect(m_ConfigTechnical.getString("mail.smtp.host"), m_ConfigTechnical.getString("mail.user"), m_ConfigTechnical.getString("mail.password"));
            } else {
                transporte.connect(m_ConfigTechnical.getString("mail.smtp.host"), Integer.parseInt(puerto),m_ConfigTechnical.getString("mail.user"), m_ConfigTechnical.getString("mail.password"));
            }//if ("".equals(puerto)||puerto==null)      
            transporte.sendMessage(mensaje,mensaje.getAllRecipients());
            
        } catch (Exception e) {       
            m_Log.error(e.getMessage());
            m_Log.error(e.toString());            
            throw new MailException(m_ConfigError.getString("Error.Mail.Envio"), e);
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("sendMailWithAttachments() : END");
    }//sendMailWithAttachments
    
}//class

    