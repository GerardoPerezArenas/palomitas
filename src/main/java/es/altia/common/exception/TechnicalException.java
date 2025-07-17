package es.altia.common.exception;    
 

/**
 * Base class of all technical exceptions. A technical exception is different
 * from a functional exception in that it is produced by a technical error such
 * as a database connection failure, an SQL exception, ...
 *
 * @version 1.0 
 */
public class  TechnicalException extends BaseException 
{
    /**
     * Create a <code>TechnicalException</code> and set the exception error
     * message.
     *
     * @param theMessage the message
     */
    public TechnicalException(String theMessage) {
        this(theMessage, null);
    }

    /**
     * Create a <code>TechnicalException</code> and set the exception object
     * that caused this exception.
     *
     * @param theMessage   the detail of the error message
     * @param theException the original exception
     */
    public TechnicalException(String theMessage, Throwable theException)
    {
        super(theMessage, theException);
    }

}
