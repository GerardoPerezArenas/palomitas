package es.altia.util.struts;

import org.apache.struts.action.ActionMapping;

public class DefaultActionMapping extends ActionMapping {

	private boolean authenticationRequired;

	public DefaultActionMapping() {
		authenticationRequired = false;
	}//constructor
	
	public boolean getAuthenticationRequired() {
		return authenticationRequired;
	}//getAuthenticationRequired
	
	public void setAuthenticationRequired(boolean authenticationRequired) {
		this.authenticationRequired = authenticationRequired;
	}//setAuthenticationRequired

}//class
