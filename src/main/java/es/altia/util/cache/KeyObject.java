/*______________________________BOF_________________________________*/
package es.altia.util.cache;

import java.io.Serializable;

/**
 * @version $\Date$ $\Revision$
 */
public interface KeyObject extends Serializable {

	/**
	  * equals method inherited from Object
	  * @param _other
	  * @return boolean
	  **/
	public boolean equals(Object _other);


	/**
	  * hashCode method inherited from Object
	  * @return int
	  **/
	public int hashCode();


	/**
	  * String representation for debugging
	  * @return String
	  **/
	public String toString();

}//class
/*______________________________EOF_________________________________*/
