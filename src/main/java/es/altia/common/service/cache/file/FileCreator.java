package es.altia.common.service.cache.file;

import es.altia.common.service.cache.*;
import es.altia.common.service.config.*;

import java.io.IOException;
import java.io.FileInputStream;


/**
 * File creator
 *
 * @see es.altia.common.service.cache.CacheManager
 * @see es.altia.common.service.cache.CreatorInterface
 */
public class FileCreator implements CreatorInterface
{
    // --------------------------------------------------------------
    // Members
    // --------------------------------------------------------------
    private static Config messages = ConfigServiceHelper.getConfig("es.altia.common.service.cache.file.messages");


    // --------------------------------------------------------------
    // Methods
    // --------------------------------------------------------------

    /**
     * Read and bufferize the content of a file.
     *
     * @param  thePath          the path (relative or absolute) of the file (String)
     * @return                  the content of the file in a byte array
     * @throws CreateException  if a IOException is raise when bufferizing the file
     */
    public Object create(Object thePath)
            throws CreateException
    {
        String filePath = thePath.toString();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            byte[] value = new byte[fis.available()];
            fis.read(value);
            fis.close();
            return value;
        } catch (IOException ex) {
            throw new CreateException(messages.getMessage("Error.FileCreator.IOException", new Object[]{filePath}), ex);
        }
    }
}
