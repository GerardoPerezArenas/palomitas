package es.altia.common.service.log;


/**
 * Interface for logging implementation classes that are returned
 * by <code>LogService.getLog()</code> methods.
 *
 * @version 1.0
 */
public interface Log {

    /**
     * Log a DEBUG level message.
     *
     * @param theMessage the message to log
     */
    public void debug(String theMessage);

    /**
     * Log a DEBUG level message along with an exception
     *
     * @param theMessage the message to log
     * @param theThrowable the exception to log
     */
    public void debug(String theMessage, Throwable theThrowable);

    /**
     * Log an ERROR level message.
     *
     * @param theMessage the message to log
     */
    public void error(String theMessage);

    /**
     * Log an ERROR level message along with an exception
     *
     * @param theMessage the message to log
     * @param theThrowable the exception to log
     */
    public void error(String theMessage, Throwable theThrowable);

    /**
     * Log an ERROR level exception only
     *
     * @param theMessage the message to log
     * @param theThrowable the exception to log
     */
    public void error(Throwable theThrowable);

    /**
     * Log an INFO level message.
     *
     * @param theMessage the message to log
     */
    public void info(String theMessage);

    /**
     * Log an INFO level message along with an exception
     *
     * @param theMessage the message to log
     * @param theThrowable the exception to log
     */
    public void info(String theMessage, Throwable theThrowable);

    /**
     * Log a WARNING level message.
     *
     * @param theMessage the message to log
     */
    public void warn(String theMessage);

    /**
     * Log a WARNING level message along with an exception
     *
     * @param theMessage the message to log
     * @param theThrowable the exception to log
     */
    public void warn(String theMessage, Throwable theThrowable);

    /**
     * Used to log a message when entering a method.
     *
     * @param theMessage the message to log
     */
    public void entry(String theMessage);

    /**
     * Used to log a message when exiting a method.
     *
     * @param theMessage the message to log
     */
    public void exit(String theMessage);

    /**
     * @return true if the Log4j priority level is debugging
     */
    public boolean isDebugEnabled();

}
