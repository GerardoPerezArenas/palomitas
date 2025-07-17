package es.altia.flexia.sir.vo;

import es.altia.flexia.sir.model.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que representa un asiento y que relaciona los datos del registro,
 * interesados y domicilios y anexos. Para servir de puente entre las clases de
 * la librería de integración del SIR y las clases que ya existen en Flexia
 * respetará lo necesario de cada una de las partes. Para poder obtener datos de
 * las tablas de base de datos de Flexia, se incluye la PK de R_RES como una
 * clave candidata de esta clase.
 */
public class AsientoValueObject implements Serializable {
	
	private Date fechaEntrada;
	private EstadoAsiento estado;
	private Integer codAsiento;
	private Integer departamento;
	private Integer ejercicio;
	private List<AnexoValueObject> anexos;
	private List<InteresadoValueObject> interesados;
	private String appVersion;
	private String codAsunto;
	private String codDocFisica;
	private String codEntidadRegistralDestino;
	private String codEntidadRegistralInicio;
	private String codEntidadRegistralOrigen;
	private String codIntercambio;
	private String codTipoAnotacion;
	private String codTipoRegistro;
	private String codTipoTransporte;
	private String codUnidadTramitadoraDestino;
	private String codUnidadTramitadoraOrigen;
	private String contactoUsuario;
	private String descripcionEntidadRegistralDestino;
	private String descripcionEntidadRegistralInicio;
	private String descripcionEntidadRegistralOrigen;
	private String descripcionUnidadTramitadoraDestino;
	private String descripcionUnidadTramitadoraOrigen;
	private String expone;
	private String nombreUsuario;
	private String numExpediente;
	private String numRegistroEntrada;
	private String numTransporte;
	private String observaciones;
	private String referenciaExterna;
	private String descripcionAsunto;
	private String solicita;
	private String timestampEntrada;
	private String descripcionTipoAnotacion;
	private String tipoRegistro;

	public AsientoValueObject(Intercambio intercambio) {
		Asiento asiento = intercambio.getAsiento();
		this.anexos = new ArrayList<AnexoValueObject>();
		for (Anexo anexo : asiento.getAnexos()) {
			this.anexos.add(new AnexoValueObject(anexo));
		}
		this.appVersion = asiento.getAppVersion();
		this.codAsiento = asiento.getCodAsiento();
		this.codAsunto = asiento.getCodAsunto();
		this.codDocFisica = (asiento.getCodDocFisica() != null) ? asiento.getCodDocFisica().getValor() : TipoDocumentacionFisica.NINGUNA.getValor();
		this.codEntidadRegistralDestino = asiento.getCodEntidadRegistralDestino();
		this.codEntidadRegistralInicio = asiento.getCodEntidadRegistralInicio();
		this.codEntidadRegistralOrigen = asiento.getCodEntidadRegistralOrigen();
		this.codIntercambio = intercambio.getCodIntercambio();
		this.codTipoAnotacion = (asiento.getCodTipoAnotacion() != null) ? asiento.getCodTipoAnotacion().getValor() : null;
		this.codTipoRegistro = (asiento.getCodTipoRegistro() != null) ? asiento.getCodTipoRegistro().getValor() : null;
		this.codTipoTransporte = (asiento.getCodTipoTransporte() != null) ? asiento.getCodTipoTransporte().getValor() : null;
		this.codUnidadTramitadoraDestino = asiento.getCodUnidadTramitadoraDestino();
		this.codUnidadTramitadoraOrigen = asiento.getCodUnidadTramitadoraOrigen();
		this.contactoUsuario = asiento.getContactoUsuario();
		this.departamento = asiento.getDepartamento();
		this.descripcionAsunto = asiento.getDescripcionAsunto();
		this.descripcionEntidadRegistralDestino = asiento.getDescripcionEntidadRegistralDestino();
		this.descripcionEntidadRegistralInicio = asiento.getDescripcionEntidadRegistralInicio();
		this.descripcionEntidadRegistralOrigen = asiento.getDescripcionEntidadRegistralOrigen();
		this.descripcionTipoAnotacion = asiento.getDescripcionTipoAnotacion();
		this.descripcionUnidadTramitadoraDestino = asiento.getDescripcionUnidadTramitadoraDestino();
		this.descripcionUnidadTramitadoraOrigen = asiento.getDescripcionUnidadTramitadoraOrigen();
		this.ejercicio = asiento.getEjercicio();
		this.estado = intercambio.getEstado();
		this.expone = asiento.getExpone();
		this.fechaEntrada = asiento.getFechaEntrada();
		this.interesados = new ArrayList<InteresadoValueObject>();
		for (Interesado interesado : asiento.getInteresados()) {
			this.interesados.add(new InteresadoValueObject(interesado));
		}
		this.nombreUsuario = asiento.getNombreUsuario();
		this.numExpediente = asiento.getNumExpediente();
		this.numRegistroEntrada = asiento.getNumRegistroEntrada();
		this.numTransporte = asiento.getNumTransporte();
		this.observaciones = asiento.getObservaciones();
		this.referenciaExterna = asiento.getReferenciaExterna();
		this.solicita = asiento.getSolicita();
		this.timestampEntrada = asiento.getTimestampEntrada();
	}

	public AsientoValueObject() {
		this.interesados = new ArrayList<InteresadoValueObject>();
		this.anexos = new ArrayList<AnexoValueObject>();
	}

	public Integer getCodAsiento() {
		return codAsiento;
	}

	public void setCodAsiento(Integer codAsiento) {
		this.codAsiento = codAsiento;
	}

	public String getCodEntidadRegistralOrigen() {
		return codEntidadRegistralOrigen;
	}

	public void setCodEntidadRegistralOrigen(String codEntidadRegistralOrigen) {
		this.codEntidadRegistralOrigen = codEntidadRegistralOrigen;
	}

	public String getDescripcionEntidadRegistralOrigen() {
		return descripcionEntidadRegistralOrigen;
	}

	public void setDescripcionEntidadRegistralOrigen(String descripcionEntidadRegistralOrigen) {
		this.descripcionEntidadRegistralOrigen = descripcionEntidadRegistralOrigen;
	}

	public String getNumRegistroEntrada() {
		return numRegistroEntrada;
	}

	public void setNumRegistroEntrada(String numRegistroEntrada) {
		this.numRegistroEntrada = numRegistroEntrada;
	}

	public Date getFechaEntrada() {
		return fechaEntrada;
	}

	public void setFechaEntrada(Date fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	public String getTimestampEntrada() {
		return timestampEntrada;
	}

	public void setTimestampEntrada(String timestampEntrada) {
		this.timestampEntrada = timestampEntrada;
	}

	public String getCodUnidadTramitadoraOrigen() {
		return codUnidadTramitadoraOrigen;
	}

	public void setCodUnidadTramitadoraOrigen(String codUnidadTramitadoraOrigen) {
		this.codUnidadTramitadoraOrigen = codUnidadTramitadoraOrigen;
	}

	public String getDescripcionUnidadTramitadoraOrigen() {
		return descripcionUnidadTramitadoraOrigen;
	}

	public void setDescripcionUnidadTramitadoraOrigen(String descripcionUnidadTramitadoraOrigen) {
		this.descripcionUnidadTramitadoraOrigen = descripcionUnidadTramitadoraOrigen;
	}

	public String getCodEntidadRegistralDestino() {
		return codEntidadRegistralDestino;
	}

	public void setCodEntidadRegistralDestino(String codEntidadRegistralDestino) {
		this.codEntidadRegistralDestino = codEntidadRegistralDestino;
	}

	public String getDescripcionEntidadRegistralDestino() {
		return descripcionEntidadRegistralDestino;
	}

	public void setDescripcionEntidadRegistralDestino(String descripcionEntidadRegistralDestino) {
		this.descripcionEntidadRegistralDestino = descripcionEntidadRegistralDestino;
	}

	public String getCodUnidadTramitadoraDestino() {
		return codUnidadTramitadoraDestino;
	}

	public void setCodUnidadTramitadoraDestino(String codUnidadTramitadoraDestino) {
		this.codUnidadTramitadoraDestino = codUnidadTramitadoraDestino;
	}

	public String getDescripcionUnidadTramitadoraDestino() {
		return descripcionUnidadTramitadoraDestino;
	}

	public void setDescripcionUnidadTramitadoraDestino(String descripcionUnidadTramitadoraDestino) {
		this.descripcionUnidadTramitadoraDestino = descripcionUnidadTramitadoraDestino;
	}

	public String getResumen() {
		return descripcionAsunto;
	}

	public void setResumen(String resumen) {
		this.descripcionAsunto = resumen;
	}

	public String getCodAsunto() {
		return codAsunto;
	}

	public void setCodAsunto(String codAsunto) {
		this.codAsunto = codAsunto;
	}

	public String getReferenciaExterna() {
		return referenciaExterna;
	}

	public void setReferenciaExterna(String referenciaExterna) {
		this.referenciaExterna = referenciaExterna;
	}

	public String getNumExpediente() {
		return numExpediente;
	}

	public void setNumExpediente(String numExpediente) {
		this.numExpediente = numExpediente;
	}

	public String getCodTipoTransporte() {
		return codTipoTransporte;
	}

	public void setCodTipoTransporte(String codTipoTransporte) {
		this.codTipoTransporte = codTipoTransporte;
	}

	public String getNumTransporte() {
		return numTransporte;
	}

	public void setNumTransporte(String numTransporte) {
		this.numTransporte = numTransporte;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getContactoUsuario() {
		return contactoUsuario;
	}

	public void setContactoUsuario(String contactoUsuario) {
		this.contactoUsuario = contactoUsuario;
	}

	public String getCodIntercambio() {
		return codIntercambio;
	}

	public void setCodIntercambio(String codIntercambio) {
		this.codIntercambio = codIntercambio;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getCodTipoAnotacion() {
		return codTipoAnotacion;
	}

	public void setCodTipoAnotacion(String codTipoAnotacion) {
		this.codTipoAnotacion = codTipoAnotacion;
	}

	public String getDescripcionTipoAnotacion() {
		return descripcionTipoAnotacion;
	}

	public void setDescripcionTipoAnotacion(String tipoAnotacion) {
		this.descripcionTipoAnotacion = tipoAnotacion;
	}

	public String getCodTipoRegistro() {
		return codTipoRegistro;
	}

	public void setCodTipoRegistro(String codTipoRegistro) {
		this.codTipoRegistro = codTipoRegistro;
	}

	public String getCodDocFisica() {
		return codDocFisica;
	}

	public void setCodDocFisica(String codDocFisica) {
		this.codDocFisica = codDocFisica;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getCodEntidadRegistralInicio() {
		return codEntidadRegistralInicio;
	}

	public void setCodEntidadRegistralInicio(String codEntidadRegistralInicio) {
		this.codEntidadRegistralInicio = codEntidadRegistralInicio;
	}

	public String getDescripcionEntidadRegistralInicio() {
		return descripcionEntidadRegistralInicio;
	}

	public void setDescripcionEntidadRegistralInicio(String descripcionEntidadRegistralInicio) {
		this.descripcionEntidadRegistralInicio = descripcionEntidadRegistralInicio;
	}

	public String getExpone() {
		return expone;
	}

	public void setExpone(String expone) {
		this.expone = expone;
	}

	public String getSolicita() {
		return solicita;
	}

	public void setSolicita(String solicita) {
		this.solicita = solicita;
	}

	public EstadoAsiento getEstado() {
		return estado;
	}

	public void setEstado(EstadoAsiento estado) {
		this.estado = estado;
	}

	public List<InteresadoValueObject> getInteresados() {
		return interesados;
	}

	public void setInteresados(List<InteresadoValueObject> interesados) {
		this.interesados = interesados;
	}

	public List<AnexoValueObject> getAnexos() {
		return anexos;
	}

	public void setAnexos(List<AnexoValueObject> anexos) {
		this.anexos = anexos;
	}

	public Integer getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(Integer ejercicio) {
		this.ejercicio = ejercicio;
	}

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public Integer getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Integer departamento) {
		this.departamento = departamento;
	}

	/**
	 * Compara las claves primarias de este objeto y el recibido por parámetro.
	 * Devuelve true si tienen la misma clave primaria.
	 *
	 * @param asiento
	 * @return
	 */
	public boolean equalsPK(AsientoValueObject asiento) {

		if (asiento == null) {
			return false;
		}

		return (this.getCodEntidadRegistralInicio().equals(asiento.getCodEntidadRegistralInicio()) // RES_UOR
				&& this.getTipoRegistro().equals(asiento.getTipoRegistro()) // RES_TIP
				&& this.getEjercicio().equals(asiento.getEjercicio()) // RES_EJE 
				&& this.getNumRegistroEntrada().equals(asiento.getNumRegistroEntrada()) // RES_NUM
				&& this.getDepartamento().equals(asiento.getDepartamento()) // RES_DEP
				);
	}

}
