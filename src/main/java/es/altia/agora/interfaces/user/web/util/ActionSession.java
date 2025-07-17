package es.altia.agora.interfaces.user.web.util;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

public abstract class ActionSession extends Action {

    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    private static final String CLSNAME = ActionSession.class.getName();
    private static final String URL_REDIRECT_NEW_SESSION = "caducaSession";
    private static final String TOKEN_MAPPING_REDIRECT = "token";
 	protected static Log m_Log =
            LogFactory.getLog(ActionSession.class.getName());

    public ActionSession() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        
        if (checkSession(request, response)) return performSession(mapping, form, request, response);
        
        // Se ha perdido la sesion, mirar si se carga algún parámetro.
        Config m_Config = ConfigServiceHelper.getConfig("authentication");
        Object obj = request.getAttribute("javax.servlet.request.X509Certificate");
        ActionErrors errors = new ActionErrors();
        if (request.isSecure() && obj != null) {
            request.setAttribute("showMessage", true);
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Escritorio.login.CaducoSesion2"));
        } else if (m_Config.getString("Auth/accessMode").equals(TOKEN_MAPPING_REDIRECT)) {
            request.setAttribute("showMessage", true);
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Escritorio.login.CaducoSesion2"));
        } else {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Escritorio.login.CaducoSesion"));
        }                
        saveErrors(request, errors);
        return mapping.findForward(URL_REDIRECT_NEW_SESSION);
    }

	private boolean checkSession(HttpServletRequest request,HttpServletResponse response){
        HttpSession session;
        try {
			session = request.getSession(true);
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(CLSNAME + ".checkSession(): " + session.getId());
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(CLSNAME + ".checkSession(): usuarioEscritorio:" + session.getAttribute("usuarioEscritorio"));
            }
			if((session.getAttribute("usuarioEscritorio") == null)){
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(CLSNAME + ".checkSession(): session is new");
	   		}
                return false;
        }
        } catch (Exception e) {
			m_Log.error("Error en checkSession");
            return false;
        }
        return true;
   }

   public abstract ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException;

}


