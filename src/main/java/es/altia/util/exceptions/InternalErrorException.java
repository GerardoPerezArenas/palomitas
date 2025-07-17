package es.altia.util.exceptions;

import java.util.Locale;

public class InternalErrorException extends ExtendedException {

    private static String MESSAGE_I18N = "es.altia.util.exceptions.InternalErrorException.MESSAGE";

    public InternalErrorException(Exception nested) {
        this(nested,null);
    }//constructor
    
    public InternalErrorException(Exception nested, Locale locale) {
        super(nested,MESSAGE_I18N,null,true,locale);
    }//constructor
    
    
}//class
