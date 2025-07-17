package es.altia.technical;

import java.io.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Esta excepción mantendrá una lista de los errores de que puede poseer  
 * un StateBean. Para que los errores sean multilingües, se añadirán los
 * errores con la clave del mensaje.
 * 
 * Se podrán obtener las cadenas de los errores en un Locale determinado
 * o las claves de los mensajes, para ser usados en la capa cliente.
 *
 * Traducir y explicar mejor!!!
 *
 * @version 1.0
 */
public class ValidationException extends Exception implements Serializable
{
    protected static Log m_Log =
             LogFactory.getLog(ValidationException.class.getName());

    /**
     * Create a <code>FunctionalException</code> and set the exception error
     * message.
     *
     * @param theMessage the message
     */
    public ValidationException(Messages theMessages) {
        super(theMessages.toString());
    	messages = theMessages;
    }

    public ValidationException(Message message) {
		super(message.getMessage());
		m_Log.error("ValidationException(Message message): " + message.getMessage());
    	Messages messages = new Messages();
    	messages.add(message);
    	m_Log.error("messages.size(): " + messages.size());
    }

    public ValidationException(String theMessageKey) {
    	this(new Message(theMessageKey));
    	m_Log.error("ValidationException(String theMessageKey): " + theMessageKey);    	
    }

	public Messages getMessages() {
		return messages;
	}
	
	private Messages messages;

}
