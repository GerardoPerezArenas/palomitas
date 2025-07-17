/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.interfaces.user.web.escritorio;

import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;

/**
 *
 * @author roberto
 */
public class CasLogonAction extends Action {

    protected static Log m_Log = LogFactory.getLog(EntradaAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) {

        UsuarioManager usuarioManager = UsuarioManager.getInstance();
        ActionErrors errors = new ActionErrors();
        String opcionRedirect = "error";
        //ActionMessages messages = new ActionMessages();

        // Se obtiene de la request el ticket devuelto por CAS
        String ticket = request.getParameter("ticket");
        m_Log.info(String.format("El ticket recibido es: %s", ticket));
        // Se obtiene de la session la Assertion devuelta por CAS
        Assertion assertion = (AssertionImpl) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
        m_Log.info("El assertion recibido es: " + assertion);
       
        //Se comprueba si tenemos que verificar si hay dos logins iguales (maiuscula y minuscula)
        Config m_Config = ConfigServiceHelper.getConfig("authentication");
        //AuthenticationFacade facade = new AuthenticationModuleFacade();
        String insensitivo=m_Config.getString("Auth/insentivoMaiusculas");
        m_Log.debug("LogonAction. Propiedad Login Insensitivo Maiusculas: "+insensitivo);
       
        //Se comprueba si hay una URL a la que redirigirnos si pulsamos el boton salir
        //En este caso se verá el botón salir, en caso contrario no.
        String urlSalir= m_Config.getString("Auth/urlDesconexion");
        if((urlSalir!=null) && !("".equals(urlSalir))){
           request.setAttribute("botonSalir","SI");
           request.setAttribute("paginaParaSalir", urlSalir);
         
        }
        
        if (ticket != null || assertion != null) {
            try {
                // Se obtiene el login del usuario autenticado en CAS de la request
                String userAuth = request.getRemoteUser();
                m_Log.info(String.format("El remoteUser que viene en la request es: %s", userAuth));
              if("SI".equals(insensitivo)){
                //Tenemos que ir a BD a comprobar si existen dos logins que sean iguales en maiuscula y minuscula Y si sí lanzar error
           
                boolean hayDos=usuarioManager.comprobarSiHayDos(userAuth);
               //Si aparece dos veces el usuario, lanzamos el error  
                if (hayDos){

                     errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.username.ExistTwo"));
                     saveErrors(request, errors);
                     opcionRedirect = "error";
                     return mapping.findForward(opcionRedirect);

                 }else {
                    // Si sólo aparece una vez el usuario, tenemos que 
                    //pasar a la aplicación el login, en la forma correcta
                    //es decir, si el login está en la BD en mayúsculas en mayúsculas
                    //y si está en minúsculas en minúsculas.
                    userAuth=usuarioManager.dameElCorrecto(userAuth);
                    }
                    m_Log.info(String.format("El usuario autenticado después de pedir el correcto es: %s", userAuth));
               } 

                    UsuarioEscritorioValueObject usuarioEscritorioVO = new UsuarioEscritorioValueObject();
                    usuarioEscritorioVO.setLogin(userAuth);
                    usuarioEscritorioVO = usuarioManager.buscaUsuario(usuarioEscritorioVO, true);

                    // estado del usuario
                    //int estadoBloqueo = usuarioManager.getBloqueado(usuarioEscritorioVO);
                    //int estadoEliminado = usuarioManager.getEliminado(usuarioEscritorioVO);

                    if (usuarioEscritorioVO.getIdUsuario() != 0) {
                            usuarioEscritorioVO = usuarioManager.buscaApp(usuarioEscritorioVO);
                            usuarioEscritorioVO = usuarioManager.buscaCssGeneral(usuarioEscritorioVO);
                            
                            //Informamos en una propiedad del objeto UsuarioEscritorioValueObject el valor de días restantes para caducidad de contraseña, atributo que viene en el Assertion de CAS
                            m_Log.info(String.format("Otros atributos del assertion es: %s", assertion.getPrincipal().getAttributes() ));
                            usuarioEscritorioVO.setDaysLeftCASPass(Integer.parseInt(((String) assertion.getPrincipal().getAttributes().get("daysLeft"))));
                            // Se audita el acceso a la aplicacion
                            usuarioEscritorioVO.setFechaUltimoAcceso(usuarioManager.getFechaUltimoAcceso(usuarioEscritorioVO.getIdUsuario()));
                            usuarioManager.auditarAccesoAplicacion(usuarioEscritorioVO.getIdUsuario());
                            
                            request.getSession().setAttribute("usuarioEscritorio", usuarioEscritorioVO);
                            request.getSession().setAttribute("usuarioLoginNT", "yes");
                            opcionRedirect = "exito";
                      } else {
                            //ErrorMessages.FailedLogin
                            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("error.username.notValid"));
                            opcionRedirect = "error";
                      }
             

                /*} catch (ConfigAuthenticationException ex) {
                if(m_Log.isErrorEnabled()) {
                    m_Log.error("Error en " + this.getClass().getName(), ex);
                }
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("ErrorMessages.cas.ticket.invalid"));
            } catch (ProcessAuthenticationException ex) {
                if(m_Log.isErrorEnabled()) {
                    m_Log.error("Error en " + this.getClass().getName(), ex);
                }
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("ErrorMessages.cas.ticket.invalid"));
                 */
            } catch (Exception ex) {
                if(m_Log.isErrorEnabled()) {
                    m_Log.error("Error en " + this.getClass().getName(), ex);
                }
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("Error de mai"));
            }
            
        } else {
            m_Log.info("El ticket o el assertion recibidos son nulos");
            opcionRedirect = "cerrarSesion";
             errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("ErrorMessages.cas.ticket.invalid"));
        }
        m_Log.info("Opción de redirección antes de dejar el action: " + opcionRedirect);
        //this.saveMessages(request, messages);
        this.saveErrors(request, errors);
        return mapping.findForward(opcionRedirect);
    }
}

