/*______________________________BOF_________________________________*/
package es.altia.util.persistance;

import java.io.Serializable;

/**
  * @version      $\Date$ $\Revision$
  *
  * The interface must at least implement all persistent objects.
  *
  **/
public interface PersistentObject extends Serializable {

	/**
	  * Getter for primary key
	  * @return PrimaryKey
	  **/
	public PrimaryKey getPrimaryKey();

	/**
	  * String representation for debugging
	  * @return String
	  **/
	public String toString();

	/**
	  * @return PersistanceContext
	  **/
	public PersistanceContext getPersistanceContext();

	/**
	  * @param ctx The context
	  **/
	public void setPersistanceContext(PersistanceContext ctx);

}//class
/*______________________________EOF_________________________________*/
