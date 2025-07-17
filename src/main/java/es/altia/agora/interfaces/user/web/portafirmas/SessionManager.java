/*______________________________BOF_________________________________*/
package es.altia.agora.interfaces.user.web.portafirmas;

import es.altia.agora.business.escritorio.UsuarioValueObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @version $\Date$ $\Revision$
 */
public class SessionManager {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    private static final String USER_ATTRIBUTE = "usuario";
    private static final String SELECTED_INDEX_ATTRIBUTE = "selectedIndex";

    /*_______Operations_____________________________________________*/
    private SessionManager() {}

    public static boolean isUserAuthenticated(HttpServletRequest request) {
        boolean result = false;
        HttpSession session = request.getSession();
        if (session!=null) {
            UsuarioValueObject userProfile = (UsuarioValueObject) session.getAttribute(USER_ATTRIBUTE);
            result = ( (userProfile!=null) && (userProfile.getIdUsuario()>0) );
        }//if
        return result;
    }//isUserAuthenticated

    public static UsuarioValueObject getAuthenticatedUser(HttpServletRequest request) {
        UsuarioValueObject result = null;
        HttpSession session = request.getSession();
        if (session!=null) {
            result = (UsuarioValueObject) session.getAttribute(USER_ATTRIBUTE);
        }//if
        return result;
    }//getAuthenticatedUser

    public static String getDataSourceKey(HttpServletRequest request) {
        final UsuarioValueObject usuario = getAuthenticatedUser(request);
        if (usuario!=null) {
            final String[] params = usuario.getParamsCon();
            if ( (params!=null) && (params.length>0) ) {
                return params[params.length-1];
            }//if
        }//if
        return null;
    }//getDataSourceKey

    public static String getSelectedIndex(HttpServletRequest request) {
        return (String)request.getSession().getAttribute(SELECTED_INDEX_ATTRIBUTE);
    }

    public static void setSelectedIndex(HttpServletRequest request, String selectedIndex) {
        request.getSession().setAttribute(SELECTED_INDEX_ATTRIBUTE, selectedIndex);
    }

    public static void removeSelectedIndex(HttpServletRequest request) {
        request.getSession().removeAttribute(SELECTED_INDEX_ATTRIBUTE);
    }

}//class
/*______________________________EOF_________________________________*/
