package es.altia.agora.business.select.exception;

import es.altia.common.exception.FunctionalException;


public class SelectException extends FunctionalException {
    /**
     * Create a <code>SelectException</code> and set the exception error
     * message.
     *
     * @param theMessage the message
     */
    public SelectException(String theMessage) {
        this(theMessage, null);
    }

    /**
     * Create a <code>SelectException</code> and set the exception object
     * that caused this exception.
     *
     * @param theMessage   the detail of the error message
     * @param theException the original exception
     */
    public SelectException(String theMessage, Throwable theException)
    {
        super(theMessage, theException);
    }
}
