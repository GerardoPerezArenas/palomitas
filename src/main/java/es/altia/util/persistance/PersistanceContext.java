/*______________________________BOF_________________________________*/
package es.altia.util.persistance;

import java.io.Serializable;

/**
  * @version      $\Date$ $\Revision$
  *
  * Marker interface
  *
  **/
public interface PersistanceContext extends Serializable {

	
	public long getVersionNumber();
	
	public void setVersionNumber(long newValue);

	public boolean isDirty();
	
	public void setDirty(boolean newValue);

	public boolean isNew();
	
	public void setNew(boolean newValue);
	
	/**
	  * String representation for debugging
	  * @return String
	  **/
	public String toString();

}//class
/*______________________________EOF_________________________________*/
