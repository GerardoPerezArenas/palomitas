package es.altia.common.service;

import java.io.*;

/**
 * Initialization exception that can be thrown by the <code>ServiceManager</code>
 * class upon Service initializations.
 *
 * @version 1.0
 */
public class InitializationException extends RuntimeException
{
    /**
     * Original exception which caused this exception.
     */
    protected Throwable originalException;

    /**
     * Create a <code>InitializationException</code>, set the exception error
     * message and log the exception.
     *
     * @param theMessage  the message
     */
    public InitializationException(String theMessage) {
        this(theMessage, null);
    }

    /**
     * Create a <code>InitializationException</code>, set the exception object that
     * caused this exception and log the exception.
     *
     * @param theMessage   the detail of the error message
     * @param theException the original exception
     */
    public InitializationException(String theMessage, Throwable theException)
    {
        super(theMessage);
        originalException = theException;

        // Log the exception
        StringWriter sw = new StringWriter();
        printStackTrace(new PrintWriter(sw));
        BootLogger.log(sw.toString());
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
