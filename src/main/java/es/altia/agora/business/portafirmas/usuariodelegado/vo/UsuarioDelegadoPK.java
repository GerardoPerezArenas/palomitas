/*______________________________BOF_________________________________*/
package es.altia.agora.business.portafirmas.usuariodelegado.vo;

import es.altia.util.persistance.PrimaryKey;

/**
  * @version      $\Revision$ $\Date$
  *
  * This is the PK for value UsuarioDelegadoVO 
  *
  **/
public class UsuarioDelegadoPK implements PrimaryKey {
	/*_______Attributes_____________________________________________*/
	private int pId;

	/*_______Operations_____________________________________________*/
	public UsuarioDelegadoPK(  int theId  ) {
		pId = theId;
	}//constructor

	public int getId() {
		return pId;
	}//getId

	/**
	  * equals method inherited from Object
	  * @param _other
	  * @return boolean
	  **/
	public boolean equals(Object _other) {
		if (_other == null) return false;
		if (_other == this) return true;
		if (!(_other instanceof UsuarioDelegadoPK)) return false;
		
		final UsuarioDelegadoPK _cast = (UsuarioDelegadoPK) _other;
		boolean result = true;
		result = result && ( this.pId == _cast.pId );
		return result;
	}//equals

	/**
	  * hashCode method inherited from Object
	  * @return int
	  **/
	public int hashCode(){
		long _hashCode = 0;
		
		_hashCode = 29 * _hashCode + pId;
		return (int) _hashCode;
	}//hashCode

	/**
	  * String representation for debugging
	  * @return String
	  **/
	public String toString() {
		final StringBuffer result = new StringBuffer("[UsuarioDelegadoPK: ");
		result.append("| Id=");
		result.append(pId);
		result.append("|]");
		return result.toString();
	}//toString

}//class
/*______________________________EOF_________________________________*/

