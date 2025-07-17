package es.altia.agora.business.registro.exception;

import es.altia.common.exception.FunctionalException;


public class AbrirCerrarRegistroException extends FunctionalException {
    /**
     * Create a <code>AbrirCerrarRegistroException</code> and set the exception error
     * message.
     *
     * @param theMessage the message
     */
    public AbrirCerrarRegistroException(String theMessage) {
        this(theMessage, null);
    }

    /**
     * Create a <code>AbrirCerrarRegistroException</code> and set the exception object
     * that caused this exception.
     *
     * @param theMessage   the detail of the error message
     * @param theException the original exception
     */
    public AbrirCerrarRegistroException(String theMessage, Throwable theException)
    {
        super(theMessage, theException);
    }
}
