/*______________________________BOF_________________________________*/
package es.altia.util.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class J2eeSessionManager {
    /*_______Constants______________________________________________*/
    public static final String SESSIONINFO_SESSION_ATTRIBUTE = "j2eeSessionInfo";

    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/
    public void fillSession(HttpServletRequest request, HttpServletResponse response,
                            J2eeSessionAttributes sessionInfo) {
        final HttpSession session = request.getSession(true);
        session.setAttribute(SESSIONINFO_SESSION_ATTRIBUTE,sessionInfo);
    }//updateSessionForAuthenticatedUser
    
    public void invalidateSession(HttpServletRequest request, HttpServletResponse response) {
        final HttpSession session = request.getSession(true);
        session.invalidate();
    }//invalidateSession

    public J2eeSessionAttributes readSession(HttpServletRequest request, HttpServletResponse response) {
        final HttpSession session = request.getSession(false);
        if (session!=null) {
            final Object tmp = session.getAttribute(SESSIONINFO_SESSION_ATTRIBUTE);
            if (tmp instanceof J2eeSessionAttributes)
                return (J2eeSessionAttributes) tmp;
            else
                return null;
        } else {
            return null;
        }//if
    }//readSession

    public void touchSession(HttpServletRequest request, HttpServletResponse response) {
        readSession(request,response);
    }//touchSession

    public boolean checkSession(HttpServletRequest request, HttpServletResponse response) {
        final J2eeSessionAttributes sessionInfo = readSession(request,response);
        return (sessionInfo!=null);
    }//checkSession

}//class
/*______________________________EOF_________________________________*/
