/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.sge.notificacion.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.technical.EstructuraNotificacion;
import es.altia.common.service.mail.MailHelper;
import es.altia.common.service.mail.exception.MailException;
import es.altia.common.service.mail.exception.MailServiceNotActivedException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author oscar
 */
public class MailsTramitacionDAO {
    
    private static MailsTramitacionDAO instance = null;
    private Logger log = Logger.getLogger(MailsTramitacionDAO.class);
    
    private MailsTramitacionDAO(){        
    }
    
    
    public static MailsTramitacionDAO getInstance(){
        if(instance==null) instance = new MailsTramitacionDAO();        
        return instance;
    }
    
    
     public void envioInicioTramite(EstructuraNotificacion estructura,String numExpediente,String codProcedimiento,int codTramite, Connection con) {
        
        log.debug(" ************************ MailSender.envioInicioTramite ===========================>");
        
        try {
            
            MailHelper mails = new MailHelper();

            String asuntoCorreo = this.getTextoCorreo(10);
            String contenido = "";
            
            log.debug(" ******* asuntoCorreo: " + asuntoCorreo);
            asuntoCorreo = asuntoCorreo.replaceAll("@expediente@",numExpediente);            
            
            Vector emailInteresados = estructura.getListaEMailsInteresados();
            Vector emailUsuariosUOR = estructura.getListaEMailsUsusUOR();
            Vector emailsUOR = estructura.getListaEMailsUOR();

            String emailUsuInicioTramite = estructura.getListaEmailsUsuInicioTramite();
            String emailUsuInicioExpediente = estructura.getListaEmailsUsuInicioTramite();

            if (emailUsuInicioTramite != null && !emailUsuInicioTramite.equals("")) {
                String texto = this.getTextoCorreo(12);
                contenido = this.contenidoCorreo(con, texto, numExpediente, codProcedimiento, codTramite);

                log.debug(emailUsuInicioTramite);
                log.debug(asuntoCorreo);
                log.debug(contenido);
                log.debug("******** ENVIANDO CORREOS USUARIO INICIO TRAMITE");

                mails.sendMail(emailUsuInicioTramite, asuntoCorreo, contenido);
            }

            if (emailUsuInicioExpediente != null && !emailUsuInicioExpediente.equals("")) {
                String texto = this.getTextoCorreo(14);
                contenido = this.contenidoCorreo(con, texto, numExpediente, codProcedimiento, codTramite);

                log.debug(emailUsuInicioTramite);
                log.debug(asuntoCorreo);
                log.debug(contenido);
                log.debug("******** ENVIANDO CORREOS USUARIO INICIO EXPEDIENTE");

                mails.sendMail(emailUsuInicioTramite, asuntoCorreo, contenido);
            }
            
            for (int i = 0; i < emailUsuariosUOR.size(); i++) {
                
                //contenido = this.getTextoCorreo(4);    
                String texto = this.getTextoCorreo(4);                    
                contenido = this.contenidoCorreo(con,texto,numExpediente,codProcedimiento,codTramite);
                
                log.debug (emailUsuariosUOR.elementAt(i));
                log.debug (asuntoCorreo);
                log.debug (contenido);
                log.debug ("******* ENVIANDO CORREOS USUARIOS DE LA UNIADD TRAMITADORA");
                mails.sendMail((String)emailUsuariosUOR.elementAt(i), asuntoCorreo, contenido);                
            }

            for (int i = 0; i < emailInteresados.size(); i++) {
                
                String aux = this.getTextoCorreo(5);                
                contenido = this.contenidoCorreo(con,aux,numExpediente,codProcedimiento,codTramite);
                
                log.debug("INTERESADO destino:" + emailInteresados.elementAt(i));
                log.debug ("INTERESADO ASUNTO: " + asuntoCorreo);
                log.debug ("INTERESADO contenido: " + contenido);
                log.debug ("******* ENVIANDO CORREO A LOS INTERESADOS correo: " + emailInteresados.elementAt(i) + ",asunto: " + asuntoCorreo + ",contenido: " + contenido);
                mails.sendMail((String)emailInteresados.elementAt(i), asuntoCorreo, contenido);
                
            }

            if(emailsUOR!=null && emailsUOR.size()>0){
                String correoUOR = (String)emailsUOR.get(0);
                if (correoUOR != null && !correoUOR.equalsIgnoreCase("")) {

                    String aux = this.getTextoCorreo(6);
                    contenido = this.contenidoCorreo(con,aux,numExpediente,codProcedimiento,codTramite);
                    log.debug (correoUOR);
                    log.debug (asuntoCorreo);
                    log.debug (contenido);
                    log.debug ("*******");
                    mails.sendMail(correoUOR, asuntoCorreo, contenido);
                }
            }

        } catch (MailException ex) {ex.printStackTrace();
            log.error("MailException al enviar correos al iniciar tramite: " + ex.getMessage());
        } catch (MailServiceNotActivedException ex) {ex.printStackTrace();
            log.error("MailServiceNotActivedException al enviar correos al iniciar tramite: " + ex.getMessage());
        } 



    }
    
    
    public String getTextoCorreo(int tipoCorreo){
        String texto = "";        

        ResourceBundle bundle = ResourceBundle.getBundle("techserver");
        
        switch (tipoCorreo) {
            case 1:
                texto = bundle.getString("mail.contentTramiteInicioUsusUOR");
                break;
            case 2:
                texto = bundle.getString("mail.contentTramiteInicioInteresados");
                break;
            case 3:
                texto = bundle.getString("mail.contentTramiteInicioUOR");
                break;
            case 4:
                texto = bundle.getString("mail.contentInicioTramiteUsusUOR");
                break;
            case 5:
                texto = bundle.getString("mail.contentInicioTramiteInteresados");
                break;
            case 6:
                texto = bundle.getString("mail.contentInicioTramiteUOR");
                break;
            case 7:
                texto = bundle.getString("mail.contentFinalizacionTramiteUsusUOR");
                break;
            case 8:
                texto = bundle.getString("mail.contentFinalizacionTramiteInteresados");
                break;
            case 9:
                texto = bundle.getString("mail.contentFinalizacionTramiteUOR");
                break;
            case 10:
                texto = bundle.getString("mail.subject");
                break;
            case 11:
                texto = bundle.getString("mail.contentFinalizacionTramiteUsuarioInicio");
                break;
            case 12:
                texto = bundle.getString("mail.contentInicioTramiteUsuarioInicio");
                break;
            case 13:
                texto = bundle.getString("mail.contentFinalizacionTramiteUsuarioInicioExped");
                break;
            case 14: 
                texto = bundle.getString("mail.contentInicioTramiteUsuarioInicioExped");
                break;
        }        
        return texto;     
    }

    
    
    public String contenidoCorreo(Connection con, String contenido,String numExpediente,String codProcedimiento,int codTramite) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        String generico = GlobalNames.ESQUEMA_GENERICO;

        String sql = "select uor_nom as uor, usu_nom as usuario, pml_cod||'-'|| pml_valor as procedimiento, "
                + "cro_eje as ejercicio,exp_num as expediente,tra_cou || '-' || tml_valor as tramite,"
                + " exp_asu as asunto from e_exp inner join e_cro on (cro_num=exp_num and exp_mun=cro_mun "
                + "and cro_pro=exp_pro) inner join e_tra on (tra_cod=cro_tra and tra_pro=cro_pro) inner "
                + "join e_pml on (pml_cod=exp_pro) inner join e_tml on (tml_pro=cro_pro and tml_tra=cro_tra) "
                + "inner join " + generico + "a_usu on (usu_cod=cro_usu) inner join a_uor on (cro_utr=uor_cod) "
                + "where cro_num=? and cro_pro=? "
                + " and cro_tra=?";

        try {

            int i = 1;
            ps = con.prepareStatement(sql);
            log.debug(sql);
            ps.setString(i++, numExpediente);
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, codTramite);
            rs = ps.executeQuery();

            if (rs.next()) {
                contenido = contenido.replaceAll("@procedimiento@", rs.getString("procedimiento"));
                contenido = contenido.replaceAll("@ejercicio@", rs.getString("ejercicio"));
                contenido = contenido.replaceAll("@expediente@", rs.getString("expediente"));
                contenido = contenido.replaceAll("@tramite@", rs.getString("tramite"));
                contenido = contenido.replaceAll("@asunto@", rs.getString("asunto"));
                contenido = contenido.replaceAll("@usuario@", rs.getString("usuario"));
                contenido = contenido.replaceAll("@uor@", rs.getString("uor"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.debug("Error en consulta : "  + ex.getMessage());
        } finally {
            try {
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                log.error("Error al cerrar recursos asociados a conexión a la BBDD: "  + ex.getMessage());
            }
            return contenido;
        }
    }
}
