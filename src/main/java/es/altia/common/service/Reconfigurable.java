package es.altia.common.service;

/**
 * All classes that are dynamically reconfigurable (meaning they can refresh
 * their data at runtime) should implement this method.
 *
 * @version 1.0
 */
public interface Reconfigurable
{
    /**
     * Reconfigure the data.
     */
    public void reconfigure();

}
