package es.altia.util.security.exceptions;

import es.altia.util.exceptions.ModelException;

import java.util.Locale;

public class NotAuthenticatedException extends ModelException {

    public static String MESSAGE_I18N = "es.altia.util.security.exceptions.NotAuthenticatedException.MESSAGE";
    public static String DEFAULT_MESSAGE = "User not authenticated !";
	
	public NotAuthenticatedException() {
        super(null,MESSAGE_I18N,null,null);
	}//constructor
	
	public NotAuthenticatedException(Locale locale) {
        super(null,MESSAGE_I18N,null,locale);
	}//constructor
	
}//class
