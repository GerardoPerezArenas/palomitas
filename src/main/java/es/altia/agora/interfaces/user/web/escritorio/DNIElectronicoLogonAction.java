package es.altia.agora.interfaces.user.web.escritorio;

import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.exception.UsuarioNoEncontradoException;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.interfaces.user.web.escritorio.exception.CertificateLogonException;
import es.altia.agora.interfaces.user.web.escritorio.util.CertificateUtils;
import es.altia.common.exception.TechnicalException;
import es.altia.jaas.exception.ConfigAuthenticationException;
import es.altia.jaas.exception.ProcessAuthenticationException;
import es.altia.jaas.facade.AuthenticationFacade;
import es.altia.jaas.facade.AuthenticationModuleFacade;
import java.security.cert.X509Certificate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action que es llamado cuando se realiza la validación a través del DNI electrónico
 * @author oscar.rodriguez
 */
public class DNIElectronicoLogonAction extends Action{

    private static final String SUCCESS_MAPPING = "exito";
    private static final String ERROR_MAPPING = "error";

    private Logger m_Log = Logger.getLogger(DNIElectronicoLogonAction.class);

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

        UsuarioManager usuarioManager = UsuarioManager.getInstance();
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
            X509Certificate cert = certChain[0];
            m_Log.debug(" *************************** NOMBRE DEL CERTIFICADO X509CERTIFICATE: " + cert.getSubjectX500Principal().getName());

            AuthenticationFacade facade = new AuthenticationModuleFacade();
            documentUserAuth = facade.authenticate(cert); 
        
            // Recuperar un usuario a partir de ese ID
            UsuarioEscritorioValueObject usuarioEscritorioVO = usuarioManager.buscaUsuarioDniE(documentUserAuth);
            if (usuarioEscritorioVO.getIdUsuario() != 0) {
                usuarioEscritorioVO = usuarioManager.buscaApp(usuarioEscritorioVO);
                usuarioEscritorioVO = usuarioManager.buscaCssGeneral(usuarioEscritorioVO);

                // Se audita el acceso a la aplicacion
                usuarioEscritorioVO.setFechaUltimoAcceso(usuarioManager.getFechaUltimoAcceso(usuarioEscritorioVO.getIdUsuario()));
                usuarioManager.auditarAccesoAplicacion(usuarioEscritorioVO.getIdUsuario());

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
            pae.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(pae.getMessage()));
            throw new CertificateLogonException(errors);
        } catch (TechnicalException te) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("ErrorMessages.FailedLogin",documentUserAuth));
            throw new CertificateLogonException(errors);
        } catch (UsuarioNoEncontradoException unee) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.usuarionovalido.exception"));
            throw new CertificateLogonException(errors);
        }catch(Exception e){
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.validate.exception"));
            throw new CertificateLogonException(errors);
        }
    }

}
