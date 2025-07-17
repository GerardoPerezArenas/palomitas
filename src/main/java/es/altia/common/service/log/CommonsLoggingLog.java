/*______________________________BOF_________________________________*/
package es.altia.common.service.log;


/**
 * @version $\Date$ $\Revision$
 */
public class CommonsLoggingLog implements Log {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    private org.apache.commons.logging.Log _log;

    /*_______Operations_____________________________________________*/
    public CommonsLoggingLog(String theCategoryName) {
        _log = org.apache.commons.logging.LogFactory.getLog(theCategoryName);
    }

    public void debug(String theMessage) {
        _log.debug(theMessage);
    }
    public void debug(String theMessage, Throwable theThrowable) {
        _log.debug(theMessage,theThrowable);
    }
    public void error(String theMessage) {
        _log.error(theMessage);
    }
    public void error(String theMessage, Throwable theThrowable) {
        _log.error(theMessage,theThrowable);
    }
    public void error(Throwable theThrowable) {
        _log.error(theThrowable.getMessage(),theThrowable);
    }
    public void info(String theMessage) {
        _log.info(theMessage);
    }
    public void info(String theMessage, Throwable theThrowable) {
        _log.info(theMessage,theThrowable);
    }
    public void warn(String theMessage) {
        _log.warn(theMessage);
    }
    public void warn(String theMessage, Throwable theThrowable) {
        _log.warn(theMessage,theThrowable);
    }
    public void entry(String theMessage) {
        debug("> "+theMessage);
    }
    public void exit(String theMessage) {
        debug("< "+theMessage);
    }
    public boolean isDebugEnabled() {
        return true;
    }

}//class
/*______________________________EOF_________________________________*/
