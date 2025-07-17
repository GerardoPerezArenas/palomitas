package es.altia.util.persistance.exceptions;

import es.altia.util.exceptions.ModelException;
import java.util.Locale;

public class IntegrityViolationAttemptedException extends ModelException {
    /*_______Constants______________________________________________*/
    public static String MESSAGE_I18N = "es.altia.util.persistance.exceptions.IntegrityViolationAttemptedException.MESSAGE";
    public static String DEFAULT_MESSAGE = "Integrity violation attempted trying to delete {1} with key {0} !";

    /*_______Operations_____________________________________________*/
    public IntegrityViolationAttemptedException(String extentName, String key) {
    	this(extentName,key,null);
    }//constructor
	
    public IntegrityViolationAttemptedException(String extentName, String key, Locale locale) {
        super(null,MESSAGE_I18N, null,locale);
        _parameters=new String[2];
        _parameters[0]=key;
        _parameters[1]=extentName;
    }//constructor
	
    public String getExtentName() {
        return (String) _parameters[1];
    }//getExtentName
	
    public String getKey() {
        return (String) _parameters[0];
    }//getKey
}//class
