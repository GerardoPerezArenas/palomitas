package es.altia.common.service.log;


import es.altia.common.service.*;

/**
 * Helper class to easily get a <code>Log</code> object. Normally you
 * would call <code>((LogService)ServiceManager.getInstance().
 * getService("log")).getLog(String)</code>. Using this helper class
 * you simply need to call <code>LogFactory.getLog(String)</code>.
 *
 * @version 1.0
 */
public class LogServiceHelper
{
    /**
     * @param theCategoryName the category's name. Usually, it is the full
     *        name of the class being logged, including the package name
     * @return the <code>Log</code> instance associated with the specified
     *         category name
     */
    public static Log getLog(String theCategoryName)
    {
        return ((LogService)ServiceManager.getInstance().getService("log")).getLog(theCategoryName);
    }

}
