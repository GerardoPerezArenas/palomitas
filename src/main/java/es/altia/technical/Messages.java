
package es.altia.technical;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.HashMap;


/**
 * <p>A class which encapsulates the error messages being reported by
 * the <code>validate()</code> method of an <code>StateBean</code>.
 * Validation errors are either global to the entire <code>StateBean</code>
 * bean they are associated with, or they are specific to a particular
 * bean property.</p>
 *
 * <p>Each individual error is described by a <code>Message</code>
 * object, which contains a message key (to be looked up in an appropriate
 * message resources database) and a type. You can get the message key or
 * the full message</p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - It is assumed that these objects
 * are created and manipulated only within the context of a single thread.
 * Therefore, no synchronization is required for access to internal
 * collections.</p>
 * @author Natxo Technology
 */

public class Messages implements Serializable {


    // ----------------------------------------------------- Manifest Constants

    /**
     * The accumulated set of <code>Message</code> objects (represented
     * as an ArrayList) for each property, keyed by property name.
     */
    protected HashMap errors = new HashMap();

    /**
     * @link aggregationByValue 
     */
    private Message lnkMessage;

    /**
     * Add an error message to the set of errors for the specified property.
     *
     * @param property Property name (or ActionErrors.GLOBAL_ERROR)
     * @param message The error message to be added
     */
    public void add(Message message) {
		String property = message.getProperty();
        ArrayList list = (ArrayList) errors.get(property);
        if (list == null) {
            list = new ArrayList();
            errors.put(property, list);
        }
        list.add(message);
    }

    /**
     * Add a list of error messages.
     *
     * @param messages The error messages to add
     */
    public void add(Messages messages) {
        Iterator iter = messages.get();
        while (iter.hasNext()) {
            Message message = (Message) iter.next();
            add(message);
		}
    }



    /**
     * Clear all error messages recorded by this object.
     */
    public void clear() {

        errors.clear();

    }


    /**
     * Return <code>true</code> if there are no error messages recorded
     * in this collection, or <code>false</code> otherwise.
     */
    public boolean empty() {

        return (errors.size() == 0);

    }


    /**
     * Return the set of all recorded error messages, without distinction
     * by which property the messages are associated with.  If there are
     * no error messages recorded, an empty enumeration is returned.
     */
    public Iterator get() {

        if (errors.size() == 0)
            return (Collections.EMPTY_LIST.iterator());
        ArrayList results = new ArrayList();
        Iterator props = errors.keySet().iterator();
        while (props.hasNext()) {
            String prop = (String) props.next();
            Iterator errors = ((ArrayList) this.errors.get(prop)).iterator();
            while (errors.hasNext())
                results.add(errors.next());
        }
        return (results.iterator());

    }


    /**
     * Return the set of error messages related to a specific property.
     * If there are no such errors, an empty enumeration is returned.
     *
     * @param property Property name (or ActionErrors.GLOBAL_ERROR)
     */
    public Iterator get(String property) {

        ArrayList list = (ArrayList) errors.get(property);
        if (list == null)
            return (Collections.EMPTY_LIST.iterator());
        else
            return (list.iterator());

    }


    /**
     * Return the set of property names for which at least one error has
     * been recorded.  If there are no errors, an empty Iterator is returned.
     * If you have recorded global errors, the String value of
     * <code>ActionErrors.GLOBAL_ERROR</code> will be one of the returned
     * property names.
     */
    public Iterator properties() {

        return (errors.keySet().iterator());

    }


    /**
     * Return the number of errors recorded for all properties (including
     * global errors).  <strong>NOTE</strong> - it is more efficient to call
     * <code>empty()</code> if all you care about is whether or not there are
     * any error messages at all.
     */
    public int size() {

        int total = 0;
        Iterator keys = errors.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            ArrayList list = (ArrayList) errors.get(key);
            total += list.size();
        }
        return (total);

    }


    /**
     * Return the number of errors associated with the specified property.
     *
     * @param property Property name (or ActionErrors.GLOBAL_ERROR)
     */
    public int size(String property) {

        ArrayList list = (ArrayList) errors.get(property);
        if (list == null)
            return (0);
        else
            return (list.size());

    }
	
	public String toString() 
	{
		return "Implementar";
	}

}
