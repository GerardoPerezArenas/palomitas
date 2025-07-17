package es.altia.agora.business.select.exception;

import es.altia.common.exception.FunctionalException;


public class SelectDescripcionException extends FunctionalException {
    /**
     * Create a <code>SelectDescripcionException</code> and set the exception error
     * message.
     *
     * @param theMessage the message
     */
    public SelectDescripcionException(String theMessage) {
        this(theMessage, null);
    }

    /**
     * Create a <code>SelectDescripcionException</code> and set the exception object
     * that caused this exception.
     *
     * @param theMessage   the detail of the error message
     * @param theException the original exception
     */
    public SelectDescripcionException(String theMessage, Throwable theException)
    {
        super(theMessage, theException);
    }
}
