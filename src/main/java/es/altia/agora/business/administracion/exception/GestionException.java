package es.altia.agora.business.administracion.exception;

import es.altia.common.exception.FunctionalException;


public class GestionException extends FunctionalException {
    /**
     * Create a <code>GestionException</code> and set the exception error
     * message.
     *
     * @param theMessage the message
     */
    public GestionException(String theMessage) {
        this(theMessage, null);
    }

    /**
     * Create a <code>GestionException</code> and set the exception object
     * that caused this exception.
     *
     * @param theMessage   the detail of the error message
     * @param theException the original exception
     */
    public GestionException(String theMessage, Throwable theException)
    {
        super(theMessage, theException);
    }
}
