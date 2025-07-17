package es.altia.agora.interfaces.user.web.escritorio;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import es.altia.agora.business.escritorio.UserPreferences;
import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.interfaces.user.web.util.ActionSession;

public final class UserPreferencesAction extends ActionSession {

	public ActionForward performSession(ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
		throws IOException, ServletException {
		
		String option = (String) request.getParameter("option");
		String value  = (String) request.getParameter("value");	

		HttpSession sesion = (HttpSession) request.getSession(false);
		UsuarioEscritorioValueObject usuarioEscritorioVO = (UsuarioEscritorioValueObject) sesion.getAttribute("usuarioEscritorio");
		UserPreferences userPreferences = (UserPreferences) usuarioEscritorioVO.getPreferences();  
		if ((sesion == null) || (option == null) || (value == null) || (userPreferences == null)) {
			return mapping.findForward("preferences");
		}

		if (option.equals("changeBgColor")) {
			userPreferences.setBackgroundColor(value);
		} else if (option.equals("changeFontLetter")) {
			userPreferences.setFont(value);
		} else if (option.equals("changeFontColor")) {
			userPreferences.setFontColor(value);
		} else if (option.equals("changeFontSize")) {
			userPreferences.setFontSize(value);
		} else if (option.equals("changeIdiomaEsc")) {
			usuarioEscritorioVO.setIdiomaEsc(Integer.parseInt(value));
			usuarioEscritorioVO = UsuarioManager.getInstance().buscaApp(usuarioEscritorioVO);
		} else if (option.equals("cambiarClave")) {
			String claveAntigua = (String) request.getParameter("claveAntigua");
			String claveNueva = (String) request.getParameter("claveNueva");
			int resultado = UsuarioManager.getInstance().comprobarClave(usuarioEscritorioVO,claveAntigua);
			if(resultado > 0) {
                            
                                
                                
                                if(!UsuarioManager.getInstance().existeContrasenha( String.valueOf(usuarioEscritorioVO.getIdUsuario()), claveNueva)){
                                
                                    int r = UsuarioManager.getInstance().modificarClave(usuarioEscritorioVO,claveNueva);
                                    if(r>0) {
                                            return mapping.findForward("claveModificada");
                                    }
                                }else
                                {                                    
                                    return mapping.findForward("claveRepetida");
                                }
                                
			} else {
				return mapping.findForward("noClave");
			}
		}	else if (option.equals("default")) {
			userPreferences.setDefault();

			return mapping.findForward("noapp");				

		}else{

			return mapping.findForward("preferences");

		}

		// Forward a la pagina principal
		return mapping.findForward("preferences");

	}

}



