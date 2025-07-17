package es.altia.flexia.sir.vo;

import es.altia.flexia.sir.model.Domicilio;
import es.altia.flexia.sir.model.Interesado;

public class InteresadoValueObject {
	
	private String canalPreferenteComunicacion;
	private Integer codAsiento;
	private Integer codInteresado;
	private String correoElectronico;
	private Domicilio domicilio;
	private String direccionElectronicaHabilitada;	
	private String documentoIdentificacion;
	private String nombreRazonSocial;
	private String observaciones;
	private String primerApellido;
	private String segundoApellido;
	private String telefono;
	private String tipoDocumentoIdentificacion;
	
	public InteresadoValueObject (Interesado interesado) {
		this.canalPreferenteComunicacion = interesado.getCanalPreferenteComunicacion();
		this.codAsiento = interesado.getCodAsiento();
		this.codInteresado = interesado.getCodInteresado();
		this.correoElectronico = interesado.getCorreoElectronico();
		this.domicilio = interesado.getDomicilio();
		this.direccionElectronicaHabilitada = interesado.getDireccionElectronicaHabilitada();
		this.documentoIdentificacion = interesado.getDocumentoIdentificacion();
		this.nombreRazonSocial = interesado.getNombreRazonSocial();
		this.observaciones = interesado.getObservaciones();
		this.primerApellido = interesado.getPrimerApellido();
		this.segundoApellido = interesado.getSegundoApellido();
		this.telefono = interesado.getTelefono();
		this.tipoDocumentoIdentificacion = (interesado.getTipoDocumentoIdentificacion() != null) ? interesado.getTipoDocumentoIdentificacion().getValor() : null;
	}

	public String getCanalPreferenteComunicacion() {
		return canalPreferenteComunicacion;
	}

	public void setCanalPreferenteComunicacion(String canalPreferenteComunicacion) {
		this.canalPreferenteComunicacion = canalPreferenteComunicacion;
	}

	public Integer getCodAsiento() {
		return codAsiento;
	}

	public void setCodAsiento(Integer codAsiento) {
		this.codAsiento = codAsiento;
	}

	public Integer getCodInteresado() {
		return codInteresado;
	}

	public void setCodInteresado(Integer codInteresado) {
		this.codInteresado = codInteresado;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public Domicilio getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}

	public String getDireccionElectronicaHabilitada() {
		return direccionElectronicaHabilitada;
	}

	public void setDireccionElectronicaHabilitada(String direccionElectronicaHabilitada) {
		this.direccionElectronicaHabilitada = direccionElectronicaHabilitada;
	}

	public String getDocumentoIdentificacion() {
		return documentoIdentificacion;
	}

	public void setDocumentoIdentificacion(String documentoIdentificacion) {
		this.documentoIdentificacion = documentoIdentificacion;
	}

	public String getNombreRazonSocial() {
		return nombreRazonSocial;
	}

	public void setNombreRazonSocial(String nombreRazonSocial) {
		this.nombreRazonSocial = nombreRazonSocial;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTipoDocumentoIdentificacion() {
		return tipoDocumentoIdentificacion;
	}

	public void setTipoDocumentoIdentificacion(String tipoDocumentoIdentificacion) {
		this.tipoDocumentoIdentificacion = tipoDocumentoIdentificacion;
	}
	
	
}
