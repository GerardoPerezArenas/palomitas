package es.altia.common.exception;    
  

/**
 * Exception that ocurrs when a format is not supported
 *
 * @version 1.0 
 */
public class  FormatNotSupportedException extends BaseException 
{
    /**
     * Create a <code>FormatNotSupportedException</code> and set the exception error
     * message.
     *
     * @param theMessage the message
     */
    public FormatNotSupportedException(String theMessage) {
        this(theMessage, null);
    }

    /**
     * Create a <code>FormatNotSupportedException</code> and set the exception object
     * that caused this exception.
     *
     * @param theMessage   the detail of the error message
     * @param theException the original exception
     */
    public FormatNotSupportedException(String theMessage, Throwable theException)
    {
        super(theMessage, theException);
    }

}
