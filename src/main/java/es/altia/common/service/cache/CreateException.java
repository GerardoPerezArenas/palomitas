package es.altia.common.service.cache;

import es.altia.common.exception.TechnicalException;


/**
 * @version 1.0
 */
public class CreateException extends TechnicalException
{
    public CreateException(String theMessage) {
        this(theMessage, null);
    }

    public CreateException(String theMessage, Throwable theException)
    {
        super(theMessage, theException);
    }

}
