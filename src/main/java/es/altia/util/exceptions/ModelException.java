package es.altia.util.exceptions;


import java.util.Locale;

/**
 * The root exception of all exceptions in the "Model".
 */
public abstract class ModelException extends ExtendedException {

    protected ModelException(Exception nested, String message, Object[] parameters) {
        super(nested,message,parameters,false,null);
    }//constructor    
    
    protected ModelException(Exception nested, String message, Object[] parameters, 
                            Locale locale) {
        super(nested,message,parameters,true,locale);
    }//constructor    
    
}//class
