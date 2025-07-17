package es.altia.flexia.sir.model;

import es.altia.util.jdbc.sqlbuilder.RowResult;

/**
 * Involucrado en el asunto de uno o más {@link Asiento asientos}. Para servir de puente entre las clases de la librería
 * de integración del SIR y las clases que ya existen en Flexia respetará lo necesario de cada una de las partes.
 * Corresponde a la tabla INTERESADO o a la tabla T_TER.
 */
public class Interesado {

	private String canalPreferenteComunicacion;								// CANAL_PREFERENTE_COMUNICACION () 
	private Integer codAsiento;												// COD_ASIENTO
	private Integer codInteresado;											// COD_INTERESADO
	private String correoElectronico;										// CORREO_ELECTRONICO (T_TER.TER_DCE) 
	private Domicilio domicilio;											// Ver Domicilio COD_DOMICILIO
	private String direccionElectronicaHabilitada;							// DIRECCION_ELECTRONICA () 
	private String documentoIdentificacion;									// DOCUMENTO_IDENTIFICACION (T_TER.TER_DOC) 
	private String nombreRazonSocial;										// NOMBRE_RAZON_SOCIAL (T_TER.TER_NOM)
	private String observaciones;											// OBSERVACIONES () 
	private String primerApellido;											// PRIMER_APELLIDO (T_TER.TER_PA1) 
	private String segundoApellido;											// SEGUNDO_APELLIDO (T_TER.TER_PA2) 
	private String telefono;												// TELEFONO (T_TER.TER_TLF) 
	private TipoDocumentoIdentificacion tipoDocumentoIdentificacion;		// TIPO_DOCUMENTO_IDENTIFICACION (T_TER.TER_TID) 
	// Atributos Flexia
	private Integer codTercero;												// COD_TERCERO (T_TER.TER_COD)

	public Interesado() {
	}

	/**
	 * Constructor a partir de una fila de la tabla Interesado.
	 *
	 * @param row
	 */
	public Interesado(RowResult row) {
		this.canalPreferenteComunicacion = row.getString("CANAL_PREFERENTE_COMUNICACION");
		this.codAsiento = row.getInteger("COD_ASIENTO");
		this.codInteresado = row.getInteger("COD_INTERESADO");
		this.correoElectronico = row.getString("CORREO_ELECTRONICO");
		this.domicilio = new Domicilio(row);
		this.direccionElectronicaHabilitada = row.getString("DIRECCION_ELECTRONICA");
		this.documentoIdentificacion = row.getString("DOCUMENTO_IDENTIFICACION");
		this.nombreRazonSocial = row.getString("NOMBRE_RAZON_SOCIAL");
		this.observaciones = row.getString("OBSERVACIONES");
		this.primerApellido = row.getString("PRIMER_APELLIDO");
		this.segundoApellido = row.getString("SEGUNDO_APELLIDO");
		this.telefono = row.getString("TELEFONO");
		this.tipoDocumentoIdentificacion = TipoDocumentoIdentificacion.getEnum(
				row.getString("TIPO_DOCUMENTO_IDENTIFICACION"));
		// Atributos Flexia
		this.codTercero = row.getInteger("COD_TERCERO");
	}

	/**
	 * @return {@link CanalPreferente} del interesado.
	 */
	public String getCanalPreferenteComunicacion() {
		return canalPreferenteComunicacion;
	}

	public void setCanalPreferenteComunicacion(String canalPreferenteComunicacion) {
		this.canalPreferenteComunicacion = canalPreferenteComunicacion;
	}

	/**
	 * @return Codigo del {@link Asiento} al que pertenece el interesado.
	 */
	public int getCodAsiento() {
		return codAsiento;
	}

	public void setCodAsiento(int codAsiento) {
		this.codAsiento = codAsiento;
	}

	/**
	 * @return COdigo unico del interesado autogenerado por la base de datos.
	 */
	public int getCodInteresado() {
		return codInteresado;
	}

	public void setCodInteresado(int codInteresado) {
		this.codInteresado = codInteresado;
	}

	/**
	 * @return {@link Domicilio} del interesado.
	 */
	public Domicilio getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}

	/**
	 * @return Direccion de correo electronico del interesado.
	 */
	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	/**
	 * @return Direccion electronica en caso de no disponer de buzon de notificaciones telematicas seguras.
	 */
	public String getDireccionElectronicaHabilitada() {
		return direccionElectronicaHabilitada;
	}

	public void setDireccionElectronicaHabilitada(String direccionElectronicaHabilitada) {
		this.direccionElectronicaHabilitada = direccionElectronicaHabilitada;
	}

	/**
	 * @return Documento de identificacion del interesado.
	 */
	public String getDocumentoIdentificacion() {
		return documentoIdentificacion;
	}

	/**
	 * @param documentoIdentificacion Valor del documento de identificacion acorde a
	 * {@link Interesado#getTipoDocumentoIdentificacion()}.
	 */
	public void setDocumentoIdentificacion(String documentoIdentificacion) {
		this.documentoIdentificacion = documentoIdentificacion;
	}

	/**
	 * @return Nombre o razon social del interesado.
	 */
	public String getNombreRazonSocial() {
		return nombreRazonSocial;
	}

	public void setNombreRazonSocial(String nombreRazonSocial) {
		this.nombreRazonSocial = nombreRazonSocial;
	}

	/**
	 * @return Observaciones del interesado.
	 */
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return Primer apellido del interesado.
	 */
	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	/**
	 * @return Segundo apellido del interesado.
	 */
	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	/**
	 * @return Telefono del interesado.
	 */
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return {@link TipoDocumentoIdentificacion} del interesado.
	 */
	public TipoDocumentoIdentificacion getTipoDocumentoIdentificacion() {
		return tipoDocumentoIdentificacion;
	}

	public void setTipoDocumentoIdentificacion(TipoDocumentoIdentificacion tipoDocumentoIdentificacion) {
		this.tipoDocumentoIdentificacion = tipoDocumentoIdentificacion;
	}

	/**
	 * @return Codigo del {@link TercerosValueObject}.
	 */
	public Integer getCodTercero() {
		return codTercero;
	}

	public void setCodTercero(Integer codTercero) {
		this.codTercero = codTercero;
	}

	@Override
	public String toString() {
		return new StringBuilder("Bean{")
				.append("canalPreferenteComunicacion=").append(canalPreferenteComunicacion)
				.append(", codAsiento=").append(codAsiento)
				.append(", codInteresado=").append(codInteresado)
				.append(", correoElectronico=").append(correoElectronico)
				.append(", direccionElectronicaHabilitada=").append(direccionElectronicaHabilitada)
				.append(", documentoIdentificacion=").append(documentoIdentificacion)
				.append(", domicilio=").append(domicilio)
				.append(", nombreRazonSocial=").append(nombreRazonSocial)
				.append(", observaciones=").append(observaciones)
				.append(", primerApellido=").append(primerApellido)
				.append(", segundoApellido=").append(segundoApellido)
				.append(", telefono=").append(telefono)
				.append(", tipoDocumentoIdentificacion=").append(tipoDocumentoIdentificacion)
				.append(", codTercero=").append(codTercero)
				.append('}').toString();
	}

}
