package es.altia.common.service.jndi;


import es.altia.common.service.ServiceManager;

/**
 * Helper class to easily get a <code>JNDIWrapper</code> object. Normally you
 * would call <code>((JNDIService)ServiceManager.getInstance().
 * getService("jndi")).getJNDI(String)</code>. Using this helper class
 * you simply need to call <code>JNDIServiceHelper.getJNDI(String)</code>.
 *
 * @version 1.0
 */
public class JNDIServiceHelper
{
    /**
     * @param theLogicalName    the logical's name of the directory.
     * @return                  the <code>JNDIWrapper</code> instance associated 
     *                          with the specified logical name
     */
    public static JNDIWrapper getJNDI(String theLogicalName)
    {
        return ((JNDIService)ServiceManager.getInstance().getService("jndi")).getJNDI(theLogicalName);
    }

}
