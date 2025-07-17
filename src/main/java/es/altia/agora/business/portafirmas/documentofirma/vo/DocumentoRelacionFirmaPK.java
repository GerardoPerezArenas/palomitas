package es.altia.agora.business.portafirmas.documentofirma.vo;

import es.altia.util.persistance.PrimaryKey;

public class DocumentoRelacionFirmaPK implements PrimaryKey {
	/*_______Attributes_____________________________________________*/
	private int pIdMunicipio;
	private String pIdProcedimiento;
	private int pIdEjercicio;
	private String pIdNumeroExpediente;
	private int pIdTramite;
	private int pIdOcurrenciaTramite;
	private int pIdNumeroDocumento;
	private int pUsuarioFirmante;

	/*_______Operations_____________________________________________*/
	public DocumentoRelacionFirmaPK(  int theIdMunicipio,  String theIdProcedimiento,  int theIdEjercicio,  String theIdNumeroExpediente,  int theIdTramite,  int theIdOcurrenciaTramite,  int theIdNumeroDocumento,  int theUsuarioFirmante ) {
		pIdMunicipio = theIdMunicipio;
		pIdProcedimiento = theIdProcedimiento;
		pIdEjercicio = theIdEjercicio;
		pIdNumeroExpediente = theIdNumeroExpediente;
		pIdTramite = theIdTramite;
		pIdOcurrenciaTramite = theIdOcurrenciaTramite;
		pIdNumeroDocumento = theIdNumeroDocumento;
		pUsuarioFirmante = theUsuarioFirmante;
	}//constructor

	public int getIdMunicipio() {
		return pIdMunicipio;
	}//getIdMunicipio
	public String getIdProcedimiento() {
		return pIdProcedimiento;
	}//getIdProcedimiento
	public int getIdEjercicio() {
		return pIdEjercicio;
	}//getIdEjercicio
	public String getIdNumeroExpediente() {
		return pIdNumeroExpediente;
	}//getIdNumeroExpediente
	public int getIdTramite() {
		return pIdTramite;
	}//getIdTramite
	public int getIdOcurrenciaTramite() {
		return pIdOcurrenciaTramite;
	}//getIdOcurrenciaTramite
	public int getIdNumeroDocumento() {
		return pIdNumeroDocumento;
	}//getIdNumeroDocumento
	public int getUsuarioFirmante() {
		return pUsuarioFirmante;
	}//getUsuarioFirmante

	/**
	  * equals method inherited from Object
	  * @param _other
	  * @return boolean
	  **/
	public boolean equals(Object _other) {
		if (_other == null) return false;
		if (_other == this) return true;
		if (!(_other instanceof DocumentoRelacionFirmaPK)) return false;

		final DocumentoRelacionFirmaPK _cast = (DocumentoRelacionFirmaPK) _other;
		boolean result = true;
		result = result && ( this.pIdMunicipio == _cast.pIdMunicipio );
		if ( (this.pIdProcedimiento != null) && (_cast.pIdProcedimiento != null))
			result = result && ( pIdProcedimiento.equals(_cast.pIdProcedimiento) );
		else
			result = result && ( this.pIdProcedimiento == _cast.pIdProcedimiento );
		result = result && ( this.pIdEjercicio == _cast.pIdEjercicio );
		if ( (this.pIdNumeroExpediente != null) && (_cast.pIdNumeroExpediente != null))
			result = result && ( pIdNumeroExpediente.equals(_cast.pIdNumeroExpediente) );
		else
			result = result && ( this.pIdNumeroExpediente == _cast.pIdNumeroExpediente );
		result = result && ( this.pIdTramite == _cast.pIdTramite );
		result = result && ( this.pIdOcurrenciaTramite == _cast.pIdOcurrenciaTramite );
		result = result && ( this.pIdNumeroDocumento == _cast.pIdNumeroDocumento );
		result = result && ( this.pUsuarioFirmante == _cast.pUsuarioFirmante );
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
		_hashCode = 29 * _hashCode + pIdEjercicio;
		if (pIdNumeroExpediente != null)
			_hashCode = 29 * _hashCode + pIdNumeroExpediente.hashCode();
		_hashCode = 29 * _hashCode + pIdTramite;
		_hashCode = 29 * _hashCode + pIdOcurrenciaTramite;
		_hashCode = 29 * _hashCode + pIdNumeroDocumento;
		_hashCode = 29 * _hashCode + pUsuarioFirmante;
		return (int) _hashCode;
	}//hashCode

	/**
	  * String representation for debugging
	  * @return String
	  **/
	public String toString() {
		final StringBuffer result = new StringBuffer("[DocumentoFirmaPK: ");
		result.append("| IdMunicipio=");
		result.append(pIdMunicipio);
		result.append("| IdProcedimiento=");
		result.append(pIdProcedimiento);
		result.append("| IdEjercicio=");
		result.append(pIdEjercicio);
		result.append("| IdNumeroExpediente=");
		result.append(pIdNumeroExpediente);
		result.append("| IdTramite=");
		result.append(pIdTramite);
		result.append("| IdOcurrenciaTramite=");
		result.append(pIdOcurrenciaTramite);
		result.append("| IdNumeroDocumento=");
		result.append(pIdNumeroDocumento);
		result.append("| UsuarioFirmante=");
		result.append(pUsuarioFirmante);
		result.append("|]");
		return result.toString();
	}//toString
}