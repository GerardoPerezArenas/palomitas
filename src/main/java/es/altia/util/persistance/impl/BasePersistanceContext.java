/*______________________________BOF_________________________________*/
package es.altia.util.persistance.impl;

import es.altia.util.persistance.PersistanceContext;

/**
  * @author
  * @version      $\Date$ $\Revision$
  *
  * Marker interface
  *
  **/
public class BasePersistanceContext implements PersistanceContext {

	/*_______Attributes_____________________________________________*/
	protected long pVersionNumber=0;
	protected boolean pIsDirty=true;
	protected boolean pIsNew=true;


	/*_______Operations_____________________________________________*/
	public BasePersistanceContext() {
	}//constructor
	
	public BasePersistanceContext(long aVersionNumber, boolean isDirty, boolean isNew) {
		this.pVersionNumber = aVersionNumber;
		this.pIsDirty = isDirty;
		this.pIsNew = isNew;
	}//constructor
	

	public long getVersionNumber() {
		return pVersionNumber;
	}//getVersionNumber
	
	public void setVersionNumber(long newValue) {
		pVersionNumber = newValue;
	}//setVersionNumber

	public boolean isDirty() {
		return pIsDirty;
	}//isDirty
	
	public void setDirty(boolean newValue) {
		pIsDirty = newValue;
	}//setDirty

	public boolean isNew() {
		return pIsNew;
	}//isNew
	
	public void setNew(boolean newValue) {
		pIsNew = newValue;
	}//setNew

	public String toString() {
		final StringBuffer result = new StringBuffer("[BasePersistanceContext:");		
		result.append("|VersionNumber="+pVersionNumber);
		result.append("|Dirty="+pIsDirty);
		result.append("|New="+pIsNew);
		result.append("|]");
		return result.toString();
	}//toString

}//class
/*______________________________EOF_________________________________*/
