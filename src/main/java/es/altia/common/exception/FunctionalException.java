package es.altia.common.exception;
 

/**
 * Base class of all functional exceptions. A functional exception is different
 * from a technical exception in that it is produced by a functional error
 * such as a user not valid, a failed validation, ... Usually these exceptions
 * need to be caught to display a personalized message to the user, whereas
 * a technical exception just need to log the message somewhere and adopt a
 * default error recovery mechanism.
 *
 * @version 1.0
 */
public class FunctionalException extends BaseException  
{
    /**
     * Create a <code>FunctionalException</code> and set the exception error
     * message.
     *
     * @param theMessage the message
     */
    public  FunctionalException(String theMessage) {
        this(theMessage, null);
    }

    /**
     * Create a <code>FunctionalException</code> and set the exception object
     * that caused this exception.
     *
     * @param theMessage   the detail of the error message
     * @param theException the original exception
     */
    public FunctionalException(String theMessage, Throwable theException)
    {
        super(theMessage, theException);
    }

}
