/* Natxo Technology */

/*
 * 
 *    Rev 1.0   Nov 06 2002 18:39:38   mbt
 * Initial revision.
 * 
 *    Rev 1.0   Nov 06 2002 18:12:16   mbt
 * Initial revision.
 * 
 *    Rev 1.0   May 01 2002 10:33:58   Administrador
 * Initial revision.
 * 
 *    Rev 1.0   Apr 08 2002 18:17:48   Administrador
 * Initial revision.
 * @history Revision 1.2  2001/03/07 16:50:23  irojof
 * @history Source code ¿well? comented
 * @history
 * ====================================================================
 *
 */

package es.altia.technical;

import java.io.Serializable;

import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An encapsulation of an individual message returned by the
 * <code>validate()</code> method of an <code>StateBean</code>, consisting
 * of a message key (to be used to look up message text in an appropriate
 * message resources database) plus a type.
 * @author Natxo Technology
 */

public class Message implements Serializable {

    protected static Log m_Log =
            LogFactory.getLog(Message.class.getName());

    String ficheroErrores;

    public Message(String messageKey) {
        this(GLOBAL_ERROR, messageKey, ERROR);
    }

    public Message( String messageKey, String sufijo) {
        this.property = GLOBAL_ERROR;
        this.messageKey = messageKey;
        this.type = ERROR;
		message = ConfigServiceHelper.getConfig("error"+ sufijo).getString(messageKey);

    }

    public Message(String property, String messageKey, String type) {
		this.property = property;
        this.messageKey = messageKey;
        this.type = type;
		message = ConfigServiceHelper.getConfig("error").getString(messageKey);
        m_Log = LogFactory.getLog(this.getClass().getName());
    }

    /**
     * The "property name" marker to use for global (StateBean) errors, as opposed to
     * those related to a specific property.
     */
    public static final String GLOBAL_ERROR = "GLOBAL_ERROR";

	public static final String ERROR = "Error";
	public static final String WARNING = "Warning";
	public static final String INFO = "Info";

    /**
     * Get the property which message bellongs.
     */
    public String getProperty() {
        return (this.property);
    }

    /**
     * Get the message key for this error message.
     */
    public String getMessageKey() {
        return (this.messageKey);
    }

    /**
     * Get the message type for this error message.
     */
    public String getType() {
        return (this.type);
    }

    /**
     * Get the message for this error message.
     */
    public String getMessage() {
        if (this.message == null) {
        	this.message = "Es nulo y no devería serlo";
        }
        if (m_Log.isDebugEnabled()) m_Log.debug("Message.getMessage():" + message);
        return (this.message);
    }

    /**
     * Get the property which message bellongs.
     */
    private String property = null;

    /**
     * The message key for this error message.
     */
    private String messageKey = null;

    /**
     * The message type for this error message.
     */
    private String type = ERROR;

    /**
     * The message for this error message.
     */
    private String message = null;

}
