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

public class ModificarContrasenhaCaducadaForm extends ActionForm
{
	private String login = null;
	private String password = null;
        private String codUsuario = null;
	private String nuevaPassword = null;
        private String renovacion = null;

    UsuarioEscritorioValueObject usuarioEscritorioVO = new UsuarioEscritorioValueObject();

  /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the codUsuario
     */
    public String getCodUsuario() {
        return codUsuario;
    }

    /**
     * @param codUsuario the codUsuario to set
     */
    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    /**
     * @return the nuevaPassword
     */
    public String getNuevaPassword() {
        return nuevaPassword;
    }

    /**
     * @param nuevaPassword the nuevaPassword to set
     */
    public void setNuevaPassword(String nuevaPassword) {
        this.nuevaPassword = nuevaPassword;
    }



	
	
	 /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.setPassword(null);
        this.setLogin(null);

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

		if ((getLogin() == null) || (getLogin().length() < 1))
			errors.add("login",new ActionError("error.username.required"));
		if ((getPassword() == null) || (getPassword().length() < 1))
            errors.add("password", new ActionError("error.password.required"));  
		
		return errors;        
    }

    /**
     * @return the renovacion
     */
    public String getRenovacion() {
        return renovacion;
    }

    /**
     * @param renovacion the renovacion to set
     */
    public void setRenovacion(String renovacion) {
        this.renovacion = renovacion;
    }

  
   
}

