package es.altia.util.configuration;

import es.altia.util.exceptions.ExtendedException;
import java.util.Locale;

public class MissingConfigurationParameterException extends ExtendedException {

    private static String MESSAGE_I18N = "es.altia.util.configuration.MissingConfigurationParameterException.MESSAGE";
    private static String DEFAULT_MESSAGE = "Missing configuration parameter: {0} !";


    public MissingConfigurationParameterException(String parameterName) {
        super(null,DEFAULT_MESSAGE, null,false,null);
        _parameters=new String[1];
	_parameters[0]=parameterName;
    }//constructor
	
    public MissingConfigurationParameterException(String parameterName, Locale locale) {
        super(null,MESSAGE_I18N, null,true,locale);
        _parameters=new String[1];
	_parameters[0]=parameterName;
    }//constructor
	
    public String getParameterName() {
        return (String) _parameters[0];
    }//getParameterName
	
}//class
