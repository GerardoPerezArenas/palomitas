package es.altia.util.struts.exceptions;

public class InvalidParameterException extends Exception {

	public InvalidParameterException(Class theClass, String paramName) {
		super(theClass.getName()+": Invalid parameter value for '"+paramName+"'");
	}//constructor

	public void printStackTrace() {
		printStackTrace(System.err);
	}//printStackTrace
}//class
