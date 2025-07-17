/*______________________________BOF_________________________________*/
package es.altia.agora.business.portafirmas.delegacionfirma.vo;

import es.altia.util.persistance.PrimaryKey;

/**
  * @version      $\Revision$ $\Date$
  *
  * This is the PK for value DelegacionFirmaVO 
  *
  **/
public class DelegacionFirmaPK implements PrimaryKey {
	/*_______Attributes_____________________________________________*/
	private int pCodigoUsuario;

	/*_______Operations_____________________________________________*/
	public DelegacionFirmaPK(  int theCodigoUsuario  ) {
		pCodigoUsuario = theCodigoUsuario;
	}//constructor

	public int getCodigoUsuario() {
		return pCodigoUsuario;
	}//getCodigoUsuario

	/**
	  * equals method inherited from Object
	  * @param _other
	  * @return boolean
	  **/
	public boolean equals(Object _other) {
		if (_other == null) return false;
		if (_other == this) return true;
		if (!(_other instanceof DelegacionFirmaPK)) return false;
		
		final DelegacionFirmaPK _cast = (DelegacionFirmaPK) _other;
		boolean result = true;
		result = result && ( this.pCodigoUsuario == _cast.pCodigoUsuario );
		return result;
	}//equals

	/**
	  * hashCode method inherited from Object
	  * @return int
	  **/
	public int hashCode(){
		long _hashCode = 0;
		
		_hashCode = 29 * _hashCode + pCodigoUsuario;
		return (int) _hashCode;
	}//hashCode

	/**
	  * String representation for debugging
	  * @return String
	  **/
	public String toString() {
		final StringBuffer result = new StringBuffer("[DelegacionFirmaPK: ");
		result.append("| CodigoUsuario=");
		result.append(pCodigoUsuario);
		result.append("|]");
		return result.toString();
	}//toString

}//class
/*______________________________EOF_________________________________*/

