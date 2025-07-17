package es.altia.common.service.cache;


/**
 * Creator interface used by the CacheManager
 *
 * @see CacheManager
 */
public interface CreatorInterface
{
    /**
     * Create a new object.
     *
     * @param  theParams        parameters that could help to create the new object
     * @throws CreateException  if it could not create the object
     */
    public Object create(Object theParams)
            throws CreateException;
}
