package es.altia.common.exception;   
 
import java.io.*; 

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Base class of all exceptions. It provides exception chaining capabilities.
 *
 * @version 1.0
 */
public class CriticalException extends RuntimeException
{
    /**
     * Original exception which caused this exception.
     */
    protected Throwable originalException;

    /**
     * Create a <code>CriticalException</code>, set the exception error
     * message and log the exception.
     *
     * @param theCategory the category in which to log the exception
     * @param theMessage  the message
     */
    public CriticalException(String theCategory, String theMessage) {
        this(theCategory, theMessage, null);
    }

    /**
     * Create a <code>CriticalException</code>, set the exception object that
     * caused this exception and log the exception.
     *
     * @param theCategory  the category in which to log the exception
     * @param theMessage   the detail of the error message
     * @param theException the original exception
     */
    public CriticalException(String theCategory, String theMessage, Throwable theException)
    {
        super(theMessage);
        originalException = theException;

        // Log the exception
        Log log = LogFactory.getLog(theCategory);
        log.error(theMessage, this);

    }

    /**
     * Print the full stack trace, including the original exception.
     */
    public void printStackTrace()
    {
        printStackTrace(System.err);
    }

    /**
     * Print the full stack trace, including the original exception.
     *
     * @param thePs the byte stream in which to print the stack trace
     */
    public void printStackTrace(PrintStream thePs)
    {
	super.printStackTrace(thePs);
        if (originalException != null) {
            originalException.printStackTrace(thePs);
	}
    }

    /**
     * Print the full stack trace, including the original exception.
     *
     * @param thePw the character stream in which to print the stack trace
     */
    public void printStackTrace(PrintWriter thePw)
    {
        super.printStackTrace(thePw);
        if (originalException != null) {
            originalException.printStackTrace(thePw);
        }
    }

}
