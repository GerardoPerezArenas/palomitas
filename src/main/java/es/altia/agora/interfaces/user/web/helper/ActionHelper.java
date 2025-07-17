package es.altia.agora.interfaces.user.web.helper;

import java.util.Locale;

import org.apache.struts.util.MessageResources;

/**
 * Implementaci�n del helper para construir
 * la informaci�n necesaria para las implementaciones de los Action
 * usando los recursos de errores y mensajes de struts
 */

public final class ActionHelper  {

	private Locale theLocale;
	private MessageResources messages;


	public ActionHelper(Locale theLocale,MessageResources messages) {
		this.theLocale = theLocale;
		this.messages = messages;
	}





	 /**
	  * Errors
	  */
	  public static String ERROR_OUT_SESSION="error.logout";

	  /**
	   * Forwards
	   */
	   public static String FORWARD_RELOGIN="relogin";




}
