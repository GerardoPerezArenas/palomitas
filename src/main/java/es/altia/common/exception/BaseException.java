package es.altia.common.exception;   

import java.rmi.*;  
import java.io.*;

/**
 * Base class of all exceptions. It provides exception chaining capabilities
 * and inherits from <code>RemoteException</code> in order to be able to
 * function when sending exceptions from EJBs.
 *
 * Note: This class should only be inherited by classes from the same
 * package, hence the package level protection.
 *
 * @version 1.0
 */
abstract class BaseException extends RemoteException
{
    /**
     * The original exception which caused this exception.
     */
    protected Throwable originalException;

    /**
     * Create a <code>BaseException</code> and set the exception error
     * message.
     *
     * @param theMessage the message
     */
    public BaseException(String theMessage) {
        this(theMessage, null);
    }

    /**
     * Create a <code>BaseException</code> and set the exception object that
     * caused this exception.
     *
     * @param theMessage   the detail of the error message
     * @param theException the original exception
     */
    public BaseException(String theMessage, Throwable theException)
    {
        super(theMessage);
        originalException = theException;
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

	/**
	 * @return the string representing the stack trace
	 */
	public String printStackTraceAsString()
	{
		StringWriter sw = new StringWriter();
		printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}


    public Throwable getOriginalException(){
        return this.originalException;
    }
	
}
