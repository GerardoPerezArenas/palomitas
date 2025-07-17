package es.altia.util.exceptions;

import java.util.Locale;
import java.text.MessageFormat;

/**
 *
 * @author  administrador
 */
public abstract class ExtendedException extends java.lang.Exception {
    
    private static String NESTED_DEFAULT_HEADER="******************************************************\n"+
                                                "** Root cause:                                      **";
    private static String NESTED_DEFAULT_FOOTER="**                                                  **\n"+
                                                "******************************************************";
    private static String NO_MESSAGE="No message!";

    protected String _message = null;
    protected Locale _locale = null;
    protected Object[] _parameters = null;
    protected Exception _nestedException = null;
    protected boolean _useI18N = false;
    protected Exception _nested = null;

    /**
     * Constructs an instance of <code>ExtendedException</code> with the specified detail message.
     * @param message the detail message.
     */
    public ExtendedException(Exception nested, String message, Object[] parameters, 
                            boolean useI18N, Locale locale) {
        _nestedException=nested;
        _message = message;
        _parameters = parameters;
        _useI18N = useI18N;
        _locale = locale;
        _nested = nested;
    }//constructor
    
    public ExtendedException(Exception nested) {
        this(nested,null,null,false,null);
    }//constructor
    
    public ExtendedException(Exception nested, String message) {
        this(nested,message,null,false,null);
    }//constructor
    
    public ExtendedException(Exception nested, String message, Object[] parameters) {
        this(nested,message,parameters,false,null);
    }//constructor
    
    public void setLocaleSensibility(Locale locale) {
        _locale = locale;
        _useI18N = (locale!=null);
    }//setLocaleSensibility

    public Exception getNestedException() {
        return _nestedException;
    }
    public void setNestedException(Exception _nestedException) {
        this._nestedException = _nestedException;
    }

    public Object[] getMessageParameters() {
        return _parameters;
    }//getMessageParameters

    /**
      * LOCALIZATION NOT IMPLEMENTED YET
      *
      */    
    public String getLocalizedMessage() {
        final String retValue;

        if (_message!=null) {
            if (_useI18N) {
              retValue = (_parameters==null)?_message:(MessageFormat.format(_message,_parameters));
            } else {
              retValue = (_parameters==null)?_message:(MessageFormat.format(_message,_parameters));
            }//if
        } else {
           retValue = _nestedException.getLocalizedMessage();
        }//if
        return retValue;
    }//getLocalizedMessage
    
    public String getMessage() {
        final String retValue;
        
        if (_message!=null) {
            if (_useI18N)
                retValue = getLocalizedMessage();
	    else
                retValue = (_parameters==null)?_message:(MessageFormat.format(_message,_parameters));
        } else {
            if (_nestedException != null)
                retValue = (_useI18N)?_nestedException.getLocalizedMessage():_nestedException.getMessage();
            else
                retValue = NO_MESSAGE;
        }//if
        return retValue;
    }//getMessage
    
    public void printStackTrace() {
        this.printStackTrace(System.err);
    }//printStackTrace
    
    public void printStackTrace(java.io.PrintStream printStream) {
        super.printStackTrace(printStream);
        if (_nestedException != null) {
            printStream.println( NESTED_DEFAULT_HEADER );
            _nestedException.printStackTrace(printStream);
            printStream.println( NESTED_DEFAULT_FOOTER );
        }//if
    }//printStackTrace
    
    public void printStackTrace(java.io.PrintWriter printWriter) {
        super.printStackTrace(printWriter);
        if (_nestedException != null) {
            printWriter.println( NESTED_DEFAULT_HEADER );
            _nestedException.printStackTrace(printWriter);
            printWriter.println( NESTED_DEFAULT_FOOTER );
        }//if
    }//printStackTrace

    public Exception get_nested() {
        return _nested;
    }

}//class
