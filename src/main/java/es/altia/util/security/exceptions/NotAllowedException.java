package es.altia.util.security.exceptions;

import java.util.Locale;
import es.altia.util.exceptions.ModelException;

public class NotAllowedException extends ModelException {

    public static String MESSAGE_I18N = "es.altia.util.security.exceptions.NotAllowedException.MESSAGE";
    public static String DEFAULT_MESSAGE = "Permission denied !";
	
	public NotAllowedException() {
        super(null,MESSAGE_I18N,null,null);
	}//constructor
	
	public NotAllowedException(Locale locale) {
        super(null,MESSAGE_I18N,null,locale);
	}//constructor
	
}//class
