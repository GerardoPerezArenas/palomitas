/*______________________________BOF_________________________________*/
package es.altia.agora.business.sge.plantillafirma.vo;

import es.altia.util.persistance.PrimaryKey;

/**
  * @version      $\Revision$ $\Date$
  *
  * This is the PK for value PlantillaFirmaVO 
  *
  **/
public class PlantillaFirmaPK implements PrimaryKey {
	/*_______Attributes_____________________________________________*/
	private int pIdMunicipio;
	private String pIdProcedimiento;
	private int pIdTramite;
	private int pIdPlantilla;

	/*_______Operations_____________________________________________*/
	public PlantillaFirmaPK(  int theIdMunicipio,  String theIdProcedimiento,  int theIdTramite,  int theIdPlantilla  ) {
		pIdMunicipio = theIdMunicipio;
		pIdProcedimiento = theIdProcedimiento;
		pIdTramite = theIdTramite;
		pIdPlantilla = theIdPlantilla;
	}//constructor

	public int getIdMunicipio() {
		return pIdMunicipio;
	}//getIdMunicipio
	public String getIdProcedimiento() {
		return pIdProcedimiento;
	}//getIdProcedimiento
	public int getIdTramite() {
		return pIdTramite;
	}//getIdTramite
	public int getIdPlantilla() {
		return pIdPlantilla;
	}//getIdPlantilla

	/**
	  * equals method inherited from Object
	  * @param _other
	  * @return boolean
	  **/
	public boolean equals(Object _other) {
		if (_other == null) return false;
		if (_other == this) return true;
		if (!(_other instanceof PlantillaFirmaPK)) return false;
		
		final PlantillaFirmaPK _cast = (PlantillaFirmaPK) _other;
		boolean result = true;
		result = result && ( this.pIdMunicipio == _cast.pIdMunicipio );
		if ( (this.pIdProcedimiento != null) && (_cast.pIdProcedimiento != null))
			result = result && ( pIdProcedimiento.equals(_cast.pIdProcedimiento) );
		else
			result = result && ( this.pIdProcedimiento == _cast.pIdProcedimiento );
		result = result && ( this.pIdTramite == _cast.pIdTramite );
		result = result && ( this.pIdPlantilla == _cast.pIdPlantilla );
		return result;
	}//equals

	/**
	  * hashCode method inherited from Object
	  * @return int
	  **/
	public int hashCode(){
		long _hashCode = 0;
		
		_hashCode = 29 * _hashCode + pIdMunicipio;
		if (pIdProcedimiento != null)
			_hashCode = 29 * _hashCode + pIdProcedimiento.hashCode();
		_hashCode = 29 * _hashCode + pIdTramite;
		_hashCode = 29 * _hashCode + pIdPlantilla;
		return (int) _hashCode;
	}//hashCode

	/**
	  * String representation for debugging
	  * @return String
	  **/
	public String toString() {
		final StringBuffer result = new StringBuffer("[PlantillaFirmaPK: ");
		result.append("| IdMunicipio=");
		result.append(pIdMunicipio);
		result.append("| IdProcedimiento=");
		result.append(pIdProcedimiento);
		result.append("| IdTramite=");
		result.append(pIdTramite);
		result.append("| IdPlantilla=");
		result.append(pIdPlantilla);
		result.append("|]");
		return result.toString();
	}//toString

}//class
/*______________________________EOF_________________________________*/

