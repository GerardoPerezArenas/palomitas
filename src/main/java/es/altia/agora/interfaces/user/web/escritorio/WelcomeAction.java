package es.altia.agora.interfaces.user.web.escritorio;

import es.altia.agora.technical.ConstantesDatos;
import org.apache.struts.action.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.config.Config;
import java.util.Enumeration;

import java.util.Locale;
import javax.servlet.http.Cookie;
import org.apache.struts.Globals;

public final class WelcomeAction extends Action {
      
    protected static Log mlog = LogFactory.getLog(EntradaAction.class.getName());
    private static final String CERT_MAPPING_REDIRECT = "cert";
    private static final String DNIE_MAPPING_REDIRECT = "dnie";
    private static final String SESION_CADUCA = "sesionCaduca";
        
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        mlog.info("welcomeAction ------->");
        mlog.debug(request.getSession().getAttribute(Globals.ERROR_KEY));
        String authForward = "";
        try{
            HttpSession session = request.getSession();
            Config m_Config = ConfigServiceHelper.getConfig("authentication");
            mlog.info("welcomeAction información del session : -------> ");
            Enumeration<String> attributes = request.getSession().getAttributeNames();
            while (attributes.hasMoreElements()) {
            String attribute = (String) attributes.nextElement();
            mlog.info(attribute+" : "+request.getSession().getAttribute(attribute) + "\n");
            }
            mlog.info("welcomeAction fin información del session : -------> ");
            mlog.info("welcomeAction informacion cookies : -------> " + request.getCookies().length);

            for (Cookie cookie : request.getCookies()) {
                mlog.info("---------------------------");
                mlog.info("Domain :" + cookie.getDomain());
                mlog.info("Comment : " + cookie.getComment());
                mlog.info("MaxAge:" + cookie.getMaxAge());
                mlog.info("Name : " + cookie.getName());
                mlog.info("Path : " + cookie.getPath());
                mlog.info("Secura : " + cookie.getSecure());
                mlog.info("Value : " + cookie.getValue());
                mlog.info("Version : " + cookie.getVersion());
                mlog.info("---------------------------");
                mlog.info("Seteamos el max Age de la cookie JSESSIONID a 0 :"+ cookie.getName());
                Cookie newCookie =  new Cookie(cookie.getName(), "");
                newCookie.setMaxAge(0);
                response.addCookie(newCookie);

            }
            mlog.info("welcomeAction fin cookies : -------> ");
            // Fijamos el locale en función del idioma indicado inicialmente (aun no conocemos el del usuario)
            String sLocale = getServlet().getServletContext().getInitParameter("sLocaleInicial");
            String[] asLocale = sLocale.split("_");
            Locale oLocale = new Locale(asLocale[0], asLocale[1]);
            session.setAttribute("org.apache.struts.action.LOCALE", oLocale);           // para que tenga efecto en new ActionError ...
            session.setAttribute("javax.servlet.jsp.jstl.fmt.locale.session", oLocale); // para que tenga efecto en <fmt:message ...

            // Definimos el tipo de autenticacion a realizar.
            // Comprobación de que la request se obtiene a partir de una conexión segura.
            Object obj = request.getAttribute("javax.servlet.request.X509Certificate");
            if (request.isSecure() && obj != null) {
                // Autenticación por DNI Electrónico
                if(m_Config.getString(ConstantesDatos.AUTH_ACCESS_MODE).equals(ConstantesDatos.AUTH_ACCESS_MODE_DNIE))
                   authForward = DNIE_MAPPING_REDIRECT;
                else
                    // Autenticación por certificado
                    authForward = CERT_MAPPING_REDIRECT;

            } else {
                authForward = m_Config.getString("Auth/accessMode");                                
                if(request.getParameter("ticket")!=null){
                    request.setAttribute("ticket", request.getParameter("ticket"));
                }
            }
            
            // Comprobamos si ha llegado hasta aqui debido a que caduco la sesion
            String error = request.getParameter("error");
            if (SESION_CADUCA.equals(error)) {
                mlog.info("welcomeAction --> La sesión ha cadudado");
                if ("cas".equals(authForward)) {
                    eliminarCookies(request, response); 
                }
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Escritorio.login.CaducoSesion"));
                saveErrors(request, errors);
            }
        } catch(Exception e){
            
            mlog.error("Excepcion recuperando parametros de configuracion en WelcomeAction");
            e.printStackTrace();
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.validate.exception"));
            saveErrors(request, errors);
        }
        mlog.info("welcomeAction --> se autentica con:"+ authForward);
        return mapping.findForward(authForward);
    }
    
    private void eliminarCookies(HttpServletRequest request, HttpServletResponse response) {
        mlog.debug("eliminarCookies()");
        if (request.getCookies() != null)
          for (Cookie cookie : request.getCookies()) {
            Cookie newCookie = new Cookie(cookie.getName(), "");
            newCookie.setMaxAge(0);
            newCookie.setPath(cookie.getPath());
            if (cookie.getDomain() != null)
              newCookie.setDomain(cookie.getDomain()); 
            response.addCookie(newCookie);
        }  
    }
}
