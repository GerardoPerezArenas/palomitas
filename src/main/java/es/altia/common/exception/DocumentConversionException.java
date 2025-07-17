package es.altia.common.exception;    
  

/**
 * Exception that ocurrs when a conversion between documents failed
 *
 * @version 1.0 
 */
public class  DocumentConversionException extends BaseException 
{
    /**
     * Create a <code>DocumentConversionException</code> and set the exception error
     * message.
     *
     * @param theMessage the message
     */
    public DocumentConversionException(String theMessage) {
        this(theMessage, null);
    }

    /**
     * Create a <code>DocumentConversionException</code> and set the exception object
     * that caused this exception.
     *
     * @param theMessage   the detail of the error message
     * @param theException the original exception
     */
    public DocumentConversionException(String theMessage, Throwable theException)
    {
        super(theMessage, theException);
    }

}
