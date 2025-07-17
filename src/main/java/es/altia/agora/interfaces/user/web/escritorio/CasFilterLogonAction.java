/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.interfaces.user.web.escritorio;

import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.jaas.facade.AuthenticationFacade;
import es.altia.jaas.facade.AuthenticationModuleFacade;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

/**
 *
 * @author roberto
 */
public class CasFilterLogonAction extends Action {

    protected static Log m_Log = LogFactory.getLog(EntradaAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) {

        UsuarioManager usuarioManager = UsuarioManager.getInstance();
        ActionErrors errors = new ActionErrors();
        String opcionRedirect = "error";
        int estadoBloqueo, estadoEliminado;

        HttpSession session = request.getSession();
        
        AuthenticationFacade facade = new AuthenticationModuleFacade();
        Assertion assertion = (Assertion) session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
        if(assertion!=null) {
            try {
                String userAuth = facade.authenticate(assertion);

                UsuarioEscritorioValueObject usuarioEscritorioVO = new UsuarioEscritorioValueObject();
                usuarioEscritorioVO.setLogin(userAuth);
                usuarioEscritorioVO = usuarioManager.buscaUsuario(usuarioEscritorioVO);
                
                estadoBloqueo = usuarioManager.getBloqueado(usuarioEscritorioVO);
                estadoEliminado = usuarioManager.getEliminado(usuarioEscritorioVO);

                if (estadoBloqueo == 0 && estadoEliminado == 0) {

                    if (usuarioEscritorioVO.getIdUsuario() != 0) {
                        usuarioEscritorioVO = usuarioManager.buscaApp(usuarioEscritorioVO);
                        usuarioEscritorioVO = usuarioManager.buscaCssGeneral(usuarioEscritorioVO);

                        // Se audita el acceso a la aplicacion
                        usuarioEscritorioVO.setFechaUltimoAcceso(usuarioManager.getFechaUltimoAcceso(usuarioEscritorioVO.getIdUsuario()));
                        usuarioManager.auditarAccesoAplicacion(usuarioEscritorioVO.getIdUsuario());

                        request.getSession().setAttribute("usuarioEscritorio", usuarioEscritorioVO);
                        request.getSession().setAttribute("usuarioLoginNT", "yes");
                        opcionRedirect = "exito";
                    } else {
                        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("error.username.notValid"));
                        opcionRedirect = "error";
                    }
                } else {
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("error.username.notValid"));
                    opcionRedirect = "error";
                }

            } catch (Exception ex) {
                if(m_Log.isErrorEnabled()) {
                    m_Log.error("Error en " + this.getClass().getName(), ex);
                }
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("ErrorMessages.cas.ticket.invalid"));
            } 
        } else {
             errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("ErrorMessages.cas.ticket.invalid"));
        }

        this.saveErrors(request, errors);
        return mapping.findForward(opcionRedirect);
    }
}

