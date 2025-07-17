

package es.altia.agora.business.select.exception;

import es.altia.common.exception.FunctionalException;


public class InvalidSelectException extends FunctionalException {
    /**
     * Create a <code>InvalidUserException</code> and set the exception error
     * message.
     *
     * @param theMessage the message
     */
    public InvalidSelectException(String theMessage) {
        this(theMessage, null);
    }

    /**
     * Create a <code>InvalidSelectException</code> and set the exception object
     * that caused this exception.
     *
     * @param theMessage   the detail of the error message
     * @param theException the original exception
     */
    public InvalidSelectException(String theMessage, Throwable theException)
    {
        super(theMessage, theException);
    }
}
