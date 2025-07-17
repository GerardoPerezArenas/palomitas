package es.altia.flexia.sir.model;

/**
 * Encapsula los campos que sirven para cualquier tipo de respuesta que se le pueda dar al CIR.
 */
public class RespuestaAsiento {

	private String appVersion;
	private int codAsiento;
	private String codEntidadRegistralDestino;
	private String codEntidadRegistralProcesa;
	private String codIntercambio;
	private String codUnidadTramitadoraDestino;
	private String contactoUsuario;
	private String descripcionEntidadRegistralDestino;
	private String descripcionUnidadTramitadoraDestino;
	private String motivo;
	private String nombreUsuario;
	private TipoRespuesta tipoRespuesta;

	public RespuestaAsiento() {}
	
	/**
	 * Constructor de la respuesta a partir de un {@link Asiento}, un {@link TipoRespuesta}, el codigo de la entidad y
	 * un motivo que justifica la respuesta.
	 *
	 * @param asiento {@link Asiento} sobre el que trata la respuesta.
	 * @param tipoRespuesta Tipo de la respuesta.
	 * @param codEntidadRegistralProcesa Codigo de la entidad que confirma o rechaza el intercambio.
	 * @param motivo Justificacion de la respuesta.
	 */
	public RespuestaAsiento(Asiento asiento, TipoRespuesta tipoRespuesta, 
			String codEntidadRegistralProcesa, String motivo) {
		this.appVersion = asiento.getAppVersion();
		this.codAsiento = asiento.getCodAsiento();
		this.codEntidadRegistralDestino = asiento.getCodEntidadRegistralDestino();
		this.codEntidadRegistralProcesa = codEntidadRegistralProcesa;
//		this.codIntercambio = asiento.getCodIntercambio();
		this.codUnidadTramitadoraDestino = asiento.getCodUnidadTramitadoraDestino();
		this.contactoUsuario = asiento.getContactoUsuario();
		this.descripcionEntidadRegistralDestino = asiento.getDescripcionEntidadRegistralDestino();
		this.descripcionUnidadTramitadoraDestino = asiento.getDescripcionUnidadTramitadoraDestino();
		this.motivo = motivo;
		this.nombreUsuario = asiento.getNombreUsuario();
		
		this.tipoRespuesta = tipoRespuesta;
	}
	
	/**
	 * Constructor de la respuesta a partir de un {@link Asiento}, un {@link TipoRespuesta} y el codigo de la entidad.
	 * 
	 * @param asiento {@link Asiento} sobre el que trata la respuesta.
	 * @param tipoRespuesta Tipo de la respuesta.
	 * @param codEntidadRegistralProcesa Codigo de la entidad que confirma o rechaza el intercambio.
	 */
	public RespuestaAsiento(Asiento asiento, TipoRespuesta tipoRespuesta,
			String codEntidadRegistralProcesa) {
		this(asiento, tipoRespuesta, codEntidadRegistralProcesa, null);
	}
	
	public int getCodAsiento() {
		return codAsiento;
	}

	public void setCodAsiento(int codAsiento) {
		this.codAsiento = codAsiento;
	}
	
	public String getCodIntercambio() {
		return codIntercambio;
	}

	public void setCodIntercambio(String codIntercambio) {
		this.codIntercambio = codIntercambio;
	}

	public String getCodEntidadRegistralProcesa() {
		return codEntidadRegistralProcesa;
	}

	public void setCodEntidadRegistralProcesa(String codEntidadRegistralProcesa) {
		this.codEntidadRegistralProcesa = codEntidadRegistralProcesa;
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

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
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

	public TipoRespuesta getTipoRespuesta() {
		return tipoRespuesta;
	}

	public void setTipoRespuesta(TipoRespuesta tipoRespuesta) {
		this.tipoRespuesta = tipoRespuesta;
	}

	@Override
	public String toString() {
		return new StringBuilder("RespuestaAsiento{")
				.append("codAsiento=").append(codAsiento)
				.append(", codIntercambio=").append(codIntercambio)
				.append(", codEntidadRegistralProcesa=").append(codEntidadRegistralProcesa)
				.append(", codUnidadTramitadoraDestino=").append(codUnidadTramitadoraDestino)
				.append(", descripcionUnidadTramitadoraDestino=").append(descripcionUnidadTramitadoraDestino)
				.append(", codEntidadRegistralDestino=").append(codEntidadRegistralDestino)
				.append(", descripcionEntidadRegistralDestino=").append(descripcionEntidadRegistralDestino)
				.append(", motivo=").append(motivo)
				.append(", appVersion=").append(appVersion)
				.append(", nombreUsuario=").append(nombreUsuario)
				.append(", contactoUsuario=").append(contactoUsuario)
				.append(", tipoRespuesta=").append(tipoRespuesta)
				.append("}").toString();
	}

}
