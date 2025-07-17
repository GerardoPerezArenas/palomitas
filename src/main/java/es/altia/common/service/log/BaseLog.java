package es.altia.common.service.log;

import org.apache.log4j.Category;
import org.apache.log4j.Appender;


/**
 * Wrapper around the Log4j <code>Category</code> class. A category is
 * usually the full name of the class in which to log (including the package
 * name).
 *
 * The order of priority is as follows : ERROR > WARNING > DEBUG > INFO
 *
 * @version 1.0
 */
public class BaseLog implements Log {

    /**
     * Corresponding Log4j <code>Category</code> instance
     */
    private Category category;

    /**
     * @param theCategoryName the category's name. Usually, it is the full
     *        name of the class being logged, including the package name
     */
    public BaseLog(String theCategoryName)
    {
	category = Category.getInstance(theCategoryName);
    }

    /**
     * Log a DEBUG level message.
     *
     * @param theMessage the message to log
     */
    public void debug(String theMessage)
    {
        category.debug(theMessage);
    }

    /**
     * Log a DEBUG level message along with an exception
     *
     * @param theMessage the message to log
     * @param theThrowable the exception to log
     */
    public void debug(String theMessage, Throwable theThrowable)
    {
        category.debug(theMessage, theThrowable);
    }

    /**
     * Log an ERROR level message.
     *
     * @param theMessage the message to log
     */
    public void error(String theMessage)
    {
        category.error(theMessage);
    }

    /**
     * Log an ERROR level message along with an exception
     *
     * @param theMessage the message to log
     * @param theThrowable the exception to log
     */
    public void error(String theMessage, Throwable theThrowable)
    {
        category.error(theMessage, theThrowable);
    }

    /**
     * Log an ERROR level exception only
     *
     * @param theMessage the message to log
     * @param theThrowable the exception to log
     */
    public void error(Throwable theThrowable)
    {
        category.error("", theThrowable);
    }

    /**
     * Log an INFO level message.
     *
     * @param theMessage the message to log
     */
    public void info(String theMessage)
    {
        category.info(theMessage);
    }

    /**
     * Log an INFO level message along with an exception
     *
     * @param theMessage the message to log
     * @param theThrowable the exception to log
     */
    public void info(String theMessage, Throwable theThrowable)
    {
        category.info(theMessage, theThrowable);
    }

    /**
     * Log a WARNING level message.
     *
     * @param theMessage the message to log
     */
    public void warn(String theMessage)
    {
        category.warn(theMessage);
    }

    /**
     * Log a WARNING level message along with an exception
     *
     * @param theMessage the message to log
     * @param theThrowable the exception to log
     */
    public void warn(String theMessage, Throwable theThrowable)
    {
        category.warn(theMessage, theThrowable);
    }

    /**
     * Used to log a message when entering a method.
     *
     * @param theMessage the message to log
     */
    public void entry(String theMessage)
    {
        category.debug('>' + theMessage);
    }

    /**
     * Used to log a message when exiting a method.
     *
     * @param theMessage the message to log
     */
    public void exit(String theMessage)
    {
        category.debug('<' + theMessage);
    }

    /**
     * @return true if the Log4j priority level is debugging
     */
    public boolean isDebugEnabled()
    {
        return category.isDebugEnabled();
    }

    /**
     * Special method that returns the Log4j root <code>Appender</code>. This
     * method is useful for unit testing, where you can define a test
     * <code>Appender</code> and use it to verify the result of a log.
     *
     * @param theAppenderName the name of the <code>Appender</code> to return.
     *        It is the name defined in the log properties file.
     * @return the root appender or null if not found
     */
    public Appender getRootAppender(String theAppenderName)
    {
        return category.getRoot().getAppender(theAppenderName);
    }

}
