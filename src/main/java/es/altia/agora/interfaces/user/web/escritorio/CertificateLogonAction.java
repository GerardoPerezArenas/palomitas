package es.altia.agora.interfaces.user.web.escritorio;

import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.exception.UsuarioNoEncontradoException;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.interfaces.user.web.escritorio.exception.CertificateLogonException;
import es.altia.agora.interfaces.user.web.escritorio.util.CertificateUtils;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.jaas.exception.ConfigAuthenticationException;
import es.altia.jaas.exception.ProcessAuthenticationException;
import es.altia.jaas.facade.AuthenticationFacade;
import es.altia.jaas.facade.AuthenticationModuleFacade;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.cert.X509Certificate;

public final class CertificateLogonAction extends Action {
    
    private static final String SUCCESS_MAPPING = "exito";
    private static final String ERROR_MAPPING = "error";
    
    protected static Log m_Log = LogFactory.getLog(EntradaAction.class.getName());
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        
        try {
            return doExecute(mapping, form, request, response);
        } catch (CertificateLogonException cle) {
            cle.printStackTrace();
            saveErrors(request, cle.getErrors());
            return mapping.findForward(ERROR_MAPPING);
        }
    }
    
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws CertificateLogonException {
        
        ActionErrors errors = new ActionErrors();
        
        // Comprobación de que la request se obtiene a partir de una conexión segura.
        if (!request.isSecure()) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("ErrorMessages.https.required"));
            throw new CertificateLogonException(errors);
        }
        
        // Se va a recuperar el certificado del cliente.
        X509Certificate[] certChain = CertificateUtils.getCertificatesChain(request);
        if (!CertificateUtils.hasCerts(certChain) || certChain[0] == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("ErrorMessages.https.ClientCertificate.required"));
            throw new CertificateLogonException(errors);
        }
        
        String documentUserAuth = "";
        // Obtenemos el certificado principal. Con este certificado realizaremos la autenticación.
        try {
            
            AuthenticationFacade facade = new AuthenticationModuleFacade();
            //documentUserAuth = facade.authenticate(request); 
            documentUserAuth = facade.authenticate(certChain[0]); 
            
            /*NOTA: El cambio documentUserAuth = facade.authenticate(request); por 
            documentUserAuth = facade.authenticate(certChain[0]); se añadió cuando se controlo
            * si el login era insensitivo a mayusculas.
            * Este cambio no permitía la conexión por certificado en instalaciones (imelsa)
            * Desconocemos si es necesario, pero se ha hecho una carpeta perso para volver a poner
            * documentUserAuth = facade.authenticate(request);
            * Revisar revisiones de esta clase.
            */
           
            /////////////////Se comprueba si el login es insensible a maiusculas o no
            Config m_Config = ConfigServiceHelper.getConfig("authentication");
            String insensitivo=m_Config.getString("Auth/insentivoMaiusculas");
            m_Log.debug("CertificateLogonAction. Propiedad Login Insensitivo Maiusculas: "+insensitivo);
            
            //El login MAI y mai, por ejemplo, tiene que ser el mismo  
            Boolean hayDos=true;

            if("SI".equals(insensitivo)){

               //Tenemos que ir a BD a comprobar si existen dos logins que sean iguales en maiuscula y minuscula Y si sí lanzar error
                hayDos=UsuarioManager.getInstance().comprobarSiHayDos(documentUserAuth);
                 //Si aparece dos veces el usuario, lanzamos el error  
                if (hayDos){

                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.username.ExistTwo"));
                    saveErrors(request, errors);
                    return mapping.findForward(ERROR_MAPPING);

               }
            }
            /////////////////////////////////////////////////
            // Recuperar un usuario a partir de ese ID
            UsuarioEscritorioValueObject usuarioEscritorioVO = UsuarioManager.getInstance().buscaUsuario(documentUserAuth);
            if (usuarioEscritorioVO.getIdUsuario() != 0) {
                usuarioEscritorioVO = UsuarioManager.getInstance().buscaApp(usuarioEscritorioVO);
                usuarioEscritorioVO = UsuarioManager.getInstance().buscaCssGeneral(usuarioEscritorioVO);
               
                request.getSession().setAttribute("usuarioEscritorio", usuarioEscritorioVO);
                request.getSession().setAttribute("usuarioLoginNT", "yes");
                return mapping.findForward(SUCCESS_MAPPING);
            } else {
                return mapping.findForward(ERROR_MAPPING);
            }
            
        } catch (ConfigAuthenticationException cae) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(cae.getMessage()));            
            throw new CertificateLogonException(errors);
        } catch (ProcessAuthenticationException pae) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError
                    ("ErrorMessages.https.ClientCertificate.invalid", pae.getMessage()));
            throw new CertificateLogonException(errors);
        } catch (TechnicalException te) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("ErrorMessages.FailedLogin",documentUserAuth));
            throw new CertificateLogonException(errors);
        } catch (UsuarioNoEncontradoException unee) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.validate.exception"));            
            throw new CertificateLogonException(errors);
        }
    }
}
