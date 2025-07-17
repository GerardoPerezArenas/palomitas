package es.altia.agora.business.sge.exception;

import es.altia.common.exception.FunctionalException;

public class TramitacionException extends FunctionalException {
	/**
	* Create a <code>TramitacionException</code> 
	* and set the exception error message.
	* @param theMessage the message
	*/

	public TramitacionException(String theMessage) {
		this(theMessage, null);
	}

	/**
	* Create a <code>TramitacionException</code> 
	* and set the exception object that caused this exception.
	* @param theMessage   the detail of the error message
	* @param theException the original exception
	*/

	public TramitacionException(String theMessage, Throwable theException) {
		super(theMessage, theException);
	}
}
