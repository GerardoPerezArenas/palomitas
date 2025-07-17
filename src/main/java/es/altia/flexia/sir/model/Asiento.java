package es.altia.flexia.sir.model;

import es.altia.flexia.sir.GestionSir;
import es.altia.util.jdbc.sqlbuilder.RowResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Clase que representa un asiento y que relaciona los datos del registro, interesados y domicilios y anexos. Para
 * Clase que representa un asiento y que relaciona los datos del registro, interesados y domicilios y anexos. Para
 * servir de puente entre las clases de la librería de integración del SIR y las clases que ya existen en Flexia
 * respetará lo necesario de cada una de las partes. Para poder obtener datos de las tablas de base de datos de Flexia,
 * se incluye la PK de R_RES como una clave candidata de esta clase. Corresponde a la tabla ASIENTO o a la tabla R_RES.
 */
 public class Asiento {

	private static final Logger LOGGER = Logger.getLogger(GestionSir.class);
	private static final String FORMATO_FECHA_ASIENTO = "yyyyMMDDHHmmss";

	// Los comentarios tienen la forma // CAMPO_TABLA_SIR (CAMPO_TABLA_FLEXIA, Formato Flexia)
	// Los comentarios adicionales con respecto al SIR estan en los getters y setters de los campos
	private List<Anexo> anexos;
	private String appVersion;								// APP_VERSION () 
	private Integer codAsiento;								// COD_ASIENTO () Código identificativo del asiento registral
	private String codAsunto;								// COD_ASUNTO (R_RES.ASUNTO)
	private TipoDocumentacionFisica codDocFisica;			// COD_DOC_FISICA () 
	private String codEntidadRegistralDestino;				// COD_ENT_REGISTRAL_DESTINO (R_RES.RES_UCD) 
	private String codEntidadRegistralInicio;				// COD_ENT_REGISTRAL_INICIO (R_RES.RES_UOR) 
	private String codEntidadRegistralOrigen;				// COD_ENT_REGISTRAL_ORIGEN (R_RES.RES_UCO) Código de la Entidad Registral de Origen.
	private TipoAnotacion codTipoAnotacion;					// COD_TIPO_ANOTACION
	private TipoRegistro codTipoRegistro;					// COD_TIPO_REGISTRO (R_RES.RES_TIP "E" o "S") 
	private TipoTransporte codTipoTransporte;				// COD_TIPO_TRANSPORTE (R_RES.RES_TTR) 
	private String codUnidadTramitadoraDestino;				// COD_UND_TRAMITADORA_DESTINO () 
	private String codUnidadTramitadoraOrigen;				// COD_UND_TRAMITADORA_ORIGEN () 
	private String contactoUsuario;							// CONTACTO_USUARIO () 
	private String descripcionAsunto;						// DESC_ASUNTO (R_RES.RES_ASU) 
	private String descripcionEntidadRegistralDestino;		// DESC_ENT_REGISTRAL_DESTINO () 
	private String descripcionEntidadRegistralInicio;		// DESC_ENT_REGISTRAL_INICIO () 
	private String descripcionEntidadRegistralOrigen;		// DESC_ENT_REGISTRAL_ORIGEN () Descripción de la Entidad Registral de Origen
	private String descripcionTipoAnotacion;				// DESC_TIPO_ANOTACION () 
	private String descripcionUnidadTramitadoraDestino;		// DESC_UND_TRAMITADORA_DESTINO () 
	private String descripcionUnidadTramitadoraOrigen;		// DESC_UND_TRAMITADORA_ORIGEN () 
	private String expone;									// EXPONE () 
	private Date fechaEntrada;								// FECHA_ENTRADA (R_RES.RES_FEC "DD/MM/YYYY HH24:MI:SS") 
	private List<Interesado> interesados;					// De la tabla INTERESADO.
	private String nombreUsuario;							// NOMBRE_USUARIO () 
	private String numExpediente;							// NUM_EXPEDIENTE () 
	private String numRegistroEntrada;						// NUM_REGISTRO_ENTRADA (R_RES.RES_NUM) 
	private String numTransporte;							// NUM_TRANSPORTE (R_RES.RES_NTR) 
	private String observaciones;							// OBSERVACIONES () 
	private String referenciaExterna;						// REFERENCIA_EXTERNA () 
	private String solicita;								// SOLICITA () 
	private String timestampEntrada;						// TIMESTAMP_ENTRADA () 

	// Atributos para que sea relacionable con la tabla de Flexia
	private Integer departamento;							// DEPARTAMENTO  (R_RES.RES_DEP)
	private Integer ejercicio;								// EJERCICIO (R_RES.RES_EJE)
	private Integer numRegistro;							// NUM_REGISTRO  (R_RES.RES_NUM)
	private String tipoRegistro;							// TIPO_REGISTRO (R_RES.RES_TIP) Los valores posibles son E/S, correspondiente a Entrada/Salida.
	private Integer UnidadRegistro;							// UNIDAD_REGISTRO (R_RES.RES_UOD) Los valores posibles son E/S, correspondiente a Entrada/Salida.
	
	public Asiento() {
		this.interesados = new ArrayList<Interesado>();
		this.anexos = new ArrayList<Anexo>();
	}

	/**
	 * Constructor a partir de una fila de la tabla Asiento.
	 *
	 * @param row
	 */
	public Asiento(RowResult row) {
		this.interesados = new ArrayList<Interesado>();
		this.anexos = new ArrayList<Anexo>();
		this.appVersion = row.getString("APP_VERSION");
		this.codAsiento = row.getInteger("COD_ASIENTO");

		this.codAsunto = row.getString("COD_ASUNTO");
		this.codDocFisica = TipoDocumentacionFisica.getEnum(row.getString("COD_DOC_FISICA"));
		this.codEntidadRegistralDestino = row.getString("COD_ENT_REGISTRAL_DESTINO");
		this.codEntidadRegistralInicio = row.getString("COD_ENT_REGISTRAL_INICIO");
		this.codEntidadRegistralOrigen = row.getString("COD_ENT_REGISTRAL_ORIGEN");
		this.codTipoAnotacion = TipoAnotacion.getEnum(row.getString("COD_TIPO_ANOTACION"));
		this.codTipoRegistro = TipoRegistro.getEnum(row.getString("COD_TIPO_REGISTRO"));
		this.codTipoTransporte = TipoTransporte.getEnum(row.getString("COD_TIPO_TRANSPORTE"));
		this.codUnidadTramitadoraDestino = row.getString("COD_UND_TRAMITADORA_DESTINO");
		this.codUnidadTramitadoraOrigen = row.getString("COD_UND_TRAMITADORA_ORIGEN");
		this.contactoUsuario = row.getString("CONTACTO_USUARIO");
		this.descripcionAsunto = row.getString("DESC_ASUNTO");
		this.descripcionEntidadRegistralDestino = row.getString("DESC_ENT_REGISTRAL_DESTINO");
		this.descripcionEntidadRegistralInicio = row.getString("DESC_ENT_REGISTRAL_INICIO");
		this.descripcionEntidadRegistralOrigen = row.getString("DES_ENT_REGISTRAL_ORIGEN");
		this.descripcionTipoAnotacion = row.getString("DESC_TIPO_ANOTACION");
		this.descripcionUnidadTramitadoraDestino = row.getString("DESC_UND_TRAMITADORA_DESTINO");
		this.descripcionUnidadTramitadoraOrigen = row.getString("DESC_UND_TRAMITADORA_ORIGEN");
		this.expone = row.getString("EXPONE");
		this.fechaEntrada = row.get("FECHA_ENTRADA", Date.class);
		this.nombreUsuario = row.getString("NOMBRE_USUARIO");
		this.numExpediente = row.getString("NUM_EXPEDIENTE");
		this.numRegistroEntrada = row.getString("NUM_REGISTRO_ENTRADA");
		this.numTransporte = row.getString("NUM_TRANSPORTE");
		this.observaciones = row.getString("OBSERVACONES");
		this.referenciaExterna = row.getString("REFERENCIA_EXTERNA");
		this.solicita = row.getString("SOLICITA");
		this.timestampEntrada = row.getString("TIMESTAMP_ENTRADA");
		// Atributos Flexia
		this.ejercicio = row.getInteger("EJERCICIO");
		this.departamento = row.getInteger("DEPARTAMENTO");
		this.numRegistro = row.getInteger("NUM_REGISTRO");
		this.tipoRegistro = row.getString("TIPO_REGISTRO");
		this.UnidadRegistro = row.getInteger("UNIDAD_REGISTRO");
	}

	/**
	 * @return {@link Anexo Anexos} del asiento.
	 */
	public List<Anexo> getAnexos() {
		return anexos;
	}

	public void setAnexos(List<Anexo> anexos) {
		this.anexos = anexos;
	}

	/**
	 * @return Identificador de la aplicacion de registro y version que ha generado el fichero de mensaje de datos de
	 * intercambio.
	 */
	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	/**
	 * @return Codigo del asiento. Deberia ser autogenerado por la BD.
	 */
	public int getCodAsiento() {
		return codAsiento;
	}

	public void setCodAsiento(int codAsiento) {
		this.codAsiento = codAsiento;
	}

	/**
	 * @return Codigo de asunto del asiento.
	 */
	public String getCodAsunto() {
		return codAsunto;
	}

	/**
	 * @param codAsunto Puede ser hasta 5 caracteres.
	 */
	public void setCodAsunto(String codAsunto) {
		this.codAsunto = codAsunto;
	}

	/**
	 * @return Codigo que indica si el fichero va acompañado de documentacion fisica.
	 */
	public TipoDocumentacionFisica getCodDocFisica() {
		return codDocFisica;
	}

	/**
	 * @param codDocFisica
	 */
	public void setCodDocFisica(TipoDocumentacionFisica codDocFisica) {
		this.codDocFisica = codDocFisica;
	}

	/**
	 * @return Codigo de la entidad registral de destino.
	 */
	public String getCodEntidadRegistralDestino() {
		return codEntidadRegistralDestino;
	}

	public void setCodEntidadRegistralDestino(String codEntidadRegistralDestino) {
		this.codEntidadRegistralDestino = codEntidadRegistralDestino;
	}

	/**
	 * @return Codigo de la entidad registral de inicio.
	 */
	public String getCodEntidadRegistralInicio() {
		return codEntidadRegistralInicio;
	}

	public void setCodEntidadRegistralInicio(String codEntidadRegistralInicio) {
		this.codEntidadRegistralInicio = codEntidadRegistralInicio;
	}

	public String getCodEntidadRegistralOrigen() {
		return codEntidadRegistralOrigen;
	}

	/**
	 * @return Codigo de la entidad registral de origen.
	 */
	public void setCodEntidadRegistralOrigen(String codEntidadRegistralOrigen) {
		this.codEntidadRegistralOrigen = codEntidadRegistralOrigen;
	}

	public String getDescripcionEntidadRegistralOrigen() {
		return descripcionEntidadRegistralOrigen;
	}

	public void setDescripcionEntidadRegistralOrigen(String descripcionEntidadRegistralOrigen) {
		this.descripcionEntidadRegistralOrigen = descripcionEntidadRegistralOrigen;
	}

	/**
	 * @return {@link TipoAnotacion} del asiento.
	 */
	public TipoAnotacion getCodTipoAnotacion() {
		return codTipoAnotacion;
	}

	public void setCodTipoAnotacion(TipoAnotacion codTipoAnotacion) {
		this.codTipoAnotacion = codTipoAnotacion;
	}

	/**
	 * @return {@link TipoRegistro} del asiento.
	 */
	public TipoRegistro getCodTipoRegistro() {
		return codTipoRegistro;
	}

	public void setCodTipoRegistro(TipoRegistro codTipoRegistro) {
		this.codTipoRegistro = codTipoRegistro;
	}

	/**
	 * @return {@link TipoTransporte} del asiento.
	 */
	public TipoTransporte getCodTipoTransporte() {
		return codTipoTransporte;
	}

	public void setCodTipoTransporte(TipoTransporte codTipoTransporte) {
		this.codTipoTransporte = codTipoTransporte;
	}

	/**
	 * @return Codigo de la unidad de tramitacion de destino.
	 */
	public String getCodUnidadTramitadoraDestino() {
		return codUnidadTramitadoraDestino;
	}

	public void setCodUnidadTramitadoraDestino(String codUnidadTramitadoraDestino) {
		this.codUnidadTramitadoraDestino = codUnidadTramitadoraDestino;
	}

	/**
	 * @return Codigo de la unidad de tramitacion de origen.
	 */
	public String getCodUnidadTramitadoraOrigen() {
		return codUnidadTramitadoraOrigen;
	}

	public void setCodUnidadTramitadoraOrigen(String codUnidadTramitadoraOrigen) {
		this.codUnidadTramitadoraOrigen = codUnidadTramitadoraOrigen;
	}

	/**
	 * @return Telefono o email del usuario de origen, es decir, del usuario que genero el asiento.
	 */
	public String getContactoUsuario() {
		return contactoUsuario;
	}

	/**
	 * @param contactoUsuario Telefono o email del usuario que genera el asiento.
	 */
	public void setContactoUsuario(String contactoUsuario) {
		this.contactoUsuario = contactoUsuario;
	}

	/**
	 * @return Descripción del asunto del asiento.
	 */
	public String getDescripcionAsunto() {
		return descripcionAsunto;
	}

	public void setDescripcionAsunto(String descripcionAsunto) {
		this.descripcionAsunto = descripcionAsunto;
	}

	/**
	 * @return Descripcion de la entidad registral de destino.
	 */
	public String getDescripcionEntidadRegistralDestino() {
		return descripcionEntidadRegistralDestino;
	}

	public void setDescripcionEntidadRegistralDestino(String descripcionEntidadRegistralDestino) {
		this.descripcionEntidadRegistralDestino = descripcionEntidadRegistralDestino;
	}

	/**
	 * @return Descripcion de la entidad registral de inicio.
	 */
	public String getDescripcionEntidadRegistralInicio() {
		return descripcionEntidadRegistralInicio;
	}

	public void setDescripcionEntidadRegistralInicio(String descripcionEntidadRegistralInicio) {
		this.descripcionEntidadRegistralInicio = descripcionEntidadRegistralInicio;
	}

	/**
	 * @return Descripcion de la unidad de tramitacion de origen.
	 */
	public String getDescripcionUnidadTramitadoraOrigen() {
		return descripcionUnidadTramitadoraOrigen;
	}

	public void setDescripcionUnidadTramitadoraOrigen(String descripcionUnidadTramitadoraOrigen) {
		this.descripcionUnidadTramitadoraOrigen = descripcionUnidadTramitadoraOrigen;
	}

	/**
	 * @return Motivo del rechazo o del reenvio.
	 */
	public String getDescripcionTipoAnotacion() {
		return descripcionTipoAnotacion;
	}

	public void setDescripcionTipoAnotacion(String descripcionTipoAnotacion) {
		this.descripcionTipoAnotacion = descripcionTipoAnotacion;
	}

	/**
	 * @return Descripcion de la unidad de tramitacion de destino.
	 */
	public String getDescripcionUnidadTramitadoraDestino() {
		return descripcionUnidadTramitadoraDestino;
	}

	public void setDescripcionUnidadTramitadoraDestino(String descripcionUnidadTramitadoraDestino) {
		this.descripcionUnidadTramitadoraDestino = descripcionUnidadTramitadoraDestino;
	}

	/**
	 * @return Exposicion de los hechos y antecedentes relacionados con el asiento.
	 */
	public String getExpone() {
		return expone;
	}

	public void setExpone(String expone) {
		this.expone = expone;
	}

	/**
	 * @return Fecha y hora de entrada en origen. El formato del SIR es yyyyMMDDHHmmss.
	 */
	public Date getFechaEntrada() {
		return fechaEntrada;
	}

	public void setFechaEntrada(Date fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	/**
	 * @return {@link Interesado Interesados} del asiento.
	 */
	public List<Interesado> getInteresados() {
		return interesados;
	}

	public void setInteresados(List<Interesado> interesados) {
		this.interesados = interesados;
	}

	/**
	 * @return Nombre del usuario que genera el asiento.
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * @return Numero del expediente objeto de la tramitacion administrativa.
	 */
	public String getNumExpediente() {
		return numExpediente;
	}

	public void setNumExpediente(String numExpediente) {
		this.numExpediente = numExpediente;
	}

	/**
	 * @return Numero de registro de entrada asignado en la entidad registral de origen.
	 */
	public String getNumRegistroEntrada() {
		return numRegistroEntrada;
	}

	public void setNumRegistroEntrada(String numRegistroEntrada) {
		this.numRegistroEntrada = numRegistroEntrada;
	}

	/**
	 * @return Numero de transporte de entrada.
	 */
	public String getNumTransporte() {
		return numTransporte;
	}

	public void setNumTransporte(String numTransporte) {
		this.numTransporte = numTransporte;
	}

	/**
	 * @return Observaciones del registro de datos de intercambio recogidos por el funcionario de registro.
	 */
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return Cualquier referencia que el destino precise conocer y sea conocida por el solicitante (matricula de
	 * vehículo, número de recibo cuyo importe se reclama, etc.).
	 */
	public String getReferenciaExterna() {
		return referenciaExterna;
	}

	public void setReferenciaExterna(String referenciaExterna) {
		this.referenciaExterna = referenciaExterna;
	}

	/**
	 * @return Descripcion de la solicitud.
	 */
	public String getSolicita() {
		return solicita;
	}

	public void setSolicita(String solicita) {
		this.solicita = solicita;
	}

	/**
	 * @return Timestamp del registro de entrada en origen.
	 */
	public String getTimestampEntrada() {
		return timestampEntrada;
	}

	public void setTimestampEntrada(String timestampEntrada) {
		this.timestampEntrada = timestampEntrada;
	}

	/* Atributos Flexia */
	/**
	 * @return Departamento del registro.
	 */
	public Integer getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Integer departamento) {
		this.departamento = departamento;
	}

	/**
	 * @return Año de generacion del registro.
	 */
	public Integer getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(Integer ejercicio) {
		this.ejercicio = ejercicio;
	}

	public Integer getNumRegistro() {
		return numRegistro;
	}

	public void setNumRegistro(Integer numRegistro) {
		this.numRegistro = numRegistro;
	}

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public Integer getUnidadRegistro() {
		return UnidadRegistro;
	}

	public void setUnidadRegistro(Integer UnidadRegistro) {
		this.UnidadRegistro = UnidadRegistro;
	}

	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("Asiento{")
				.append("anexos=").append(anexos)
				.append(", appVersion=").append(appVersion)
				.append(", codAsiento=").append(codAsiento)
				.append(", codAsunto=").append(codAsunto)
				.append(", codDocFisica=").append(codDocFisica)
				.append(", codEntidadRegistralDestino=").append(codEntidadRegistralDestino)
				.append(", codEntidadRegistralInicio=").append(codEntidadRegistralInicio)
				.append(", codEntidadRegistralOrigen=").append(codEntidadRegistralOrigen)
				.append(", codTipoAnotacion=").append(codTipoAnotacion)
				.append(", codTipoRegistro=").append(codTipoRegistro)
				.append(", codTipoTransporte=").append(codTipoTransporte)
				.append(", codUnidadTramitadoraOrigen=").append(codUnidadTramitadoraOrigen)
				.append(", codUnidadTramitadoraDestino=").append(codUnidadTramitadoraDestino)
				.append(", contactoUsuario=").append(contactoUsuario)
				.append(", descripcionEntidadRegistralOrigen=").append(descripcionEntidadRegistralOrigen)
				.append(", descripcionAsunto=").append(descripcionAsunto)
				.append(", descripcionEntidadRegistralDestino=").append(descripcionEntidadRegistralDestino)
				.append(", descripcionEntidadRegistralInicio=").append(descripcionEntidadRegistralInicio)
				.append(", descripcionUnidadTramitadoraDestino=").append(descripcionUnidadTramitadoraDestino)
				.append(", descripcionUnidadTramitadoraOrigen=").append(descripcionUnidadTramitadoraOrigen)
				.append(", expone=").append(expone)
				.append(", fechaEntrada=").append(fechaEntrada)
				.append(", interesados=").append(interesados)
				.append(", nombreUsuario=").append(nombreUsuario)
				.append(", numExpediente=").append(numExpediente)
				.append(", numRegistroEntrada=").append(numRegistroEntrada)
				.append(", numTransporte=").append(numTransporte)
				.append(", referenciaExterna=").append(referenciaExterna)
				.append(", solicita=").append(solicita)
				.append(", timestampEntrada=").append(timestampEntrada)
				.append(", tipoAnotacion=").append(descripcionTipoAnotacion)
				.append(", observaciones=").append(observaciones)
				.append(", ejercicio=").append(ejercicio)
				.append(", departamento=").append(departamento)
				.append(", numRegistro=").append(numRegistro)
				.append('}').toString();
	}

}
