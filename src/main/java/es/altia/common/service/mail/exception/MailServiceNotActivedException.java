package es.altia.common.service.mail.exception;

import es.altia.common.exception.FunctionalException;


public class MailServiceNotActivedException extends FunctionalException {
        /**
     * Create a <code>DemandaException</code> and set the exception error
     * message.
     *
     * @param theMessage the message
     */
    public MailServiceNotActivedException(String theMessage) {
        this(theMessage, null);
    }

    /**
     * Create a <code>DemandaException</code> and set the exception object
     * that caused this exception.
     *
     * @param theMessage   the detail of the error message
     * @param theException the original exception
     */
    public MailServiceNotActivedException(String theMessage, Throwable theException)
    {
        super(theMessage, theException);
    }

}
