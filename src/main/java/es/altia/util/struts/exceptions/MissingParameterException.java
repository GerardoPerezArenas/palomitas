package es.altia.util.struts.exceptions;

public class MissingParameterException extends Exception {

	public MissingParameterException(Class theClass, String paramName) {
		super(theClass.getName()+": Missing expected parameter '"+paramName+"'");
	}//constructor

	public void printStackTrace() {
		printStackTrace(System.err);
	}//printStackTrace
}//class
