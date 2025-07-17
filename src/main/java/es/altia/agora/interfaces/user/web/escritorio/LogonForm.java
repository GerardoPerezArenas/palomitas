/**
 * LogonForm.java
 *
 * @author Created by Omnicore CodeGuide
 */

package es.altia.agora.interfaces.user.web.escritorio;

    
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;

public class LogonForm extends ActionForm
{
	private String login=null;
	private String password=null;
        UsuarioEscritorioValueObject usuarioEscritorioVO = new UsuarioEscritorioValueObject();

	public void setLogin(String login){
		usuarioEscritorioVO.setLogin(login);
	}	
	public String getLogin(){
		return usuarioEscritorioVO.getLogin();
	}
	
	public void setPassword(String password){
		usuarioEscritorioVO.setPassword(password);
	}	
	public String getPassword(){
		return usuarioEscritorioVO.getPassword();
	}
	
	
	 /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.password = null;
        this.login = null;
    }
	
	 /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();

		if ((login == null) || (login.length() < 1))
			errors.add("login",new ActionError("error.username.required"));
		if ((password == null) || (password.length() < 1))
            errors.add("password", new ActionError("error.password.required"));  
		
		return errors;        
    }

}

