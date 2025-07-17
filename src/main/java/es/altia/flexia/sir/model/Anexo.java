package es.altia.flexia.sir.model;

import es.altia.util.jdbc.sqlbuilder.RowResult;
import java.sql.SQLException;
import java.util.Date;

/**
 * Documento anexo a un registro. Un anexo siempre forma parte de un asiento, por lo que su PK es debil y dependiente de
 * la PK del Asiento. Para servir de puente entre las clases de la libreria de integracion del SIR y las clases que ya
 * existen en Flexia respetara lo necesario de cada una de las partes. También están incluidas, como clave candidata, la
 * PK de DocumentoAnotacionRegistroVO. Hay multiples campos que no tienen correspondencia en la tabla ANEXO de la base
 * de datos debido a que no es la intencion del soporte de SIR la de guardar documentos que no sean aceptados y, en este
 * caso, seran guardados en la tabla R_RED, por lo que la tabla ANEXO no necesita tener dichos campos. Sin embargo, la
 * clase debe poder soportar el almacenamiento de dichos campos para poder recibir los datos del WebService y poder
 * volcarlos en Flexia. Corresponde a la tabla ANEXO o a la tabla R_RED.
 */
public class Anexo {

	private String certificado;							// ()
	private Integer codAnexo;							// COD_ANEXO () PK Débil
	private Integer codAsiento;							// COD_ASIENTO () FK, parte de la PK de anexo
	private byte[] contenido;							// TODO Por que no esta en DB? (R_RED.RED_DOC)
	private String csv;									// TODO Por que no esta en DB? ()
	private String firmaDocumento;						// TODO Por que no esta en DB? ()
	private String hash;								// TODO Por que no esta en DB? ()
	private String identificadorDocumentoFirmado;		// IDENTIFICADOR_DOC_FIRMADO ()
	private String identificadorFichero;				// IDENTIFICADOR_FICHERO (R_RED.RED_DOC_ID)
	private String nombreFichero;						// NOMBRE_FICHERO (R_RED.RED_NOM_DOC)
	private String observaciones;						// OBSERVACIONES (R_RED.RED_OBSERV)
	private Date timestamp;								// TIMESTAMP (R_RED.RED_FEC_DOC)
	private TipoDocumentoAnexo tipoDocumento;			// TIPO_DOCUMENTO ()
	private String tipoMIME;							// TIPO_MIME (R_RED.RED_TIP_DOC)
	private String validacionOCSPCertificado;			// TIPO_REGISTRO ()
	private ValidezDocumento validezDocumento;			// VALIDEZ_DOCUMENTO ()
	// Atributos Flexia
	private Integer codDocumento;							// COD_DEPARTAMENTO (RED_DOC_ID)

	public Anexo() {
	}

	/**
	 * Constructor a partir de una fila de la tabla Anexo.
	 *
	 * @param row
	 */
	public Anexo(RowResult row) throws SQLException {
		// TODO Revisar cuando se revise toda la DB
		this.certificado = row.getString("");
		this.codAnexo = row.getInteger("COD_ANEXO");
		this.codAsiento = row.getInteger("COD_ASIENTO");
		this.contenido = row.getBlobBytes("CONTENIDO");
//		this.csv = row.getString("");
//		this.firmaDocumento = row.getString("");
//		this.hash = row.getString("");
		this.identificadorDocumentoFirmado = row.getString("IDENTIFICADOR_DOC_FIRMADO");
		this.identificadorFichero = row.getString("IDENTIFICADOR_FICHERO");
		this.nombreFichero = row.getString("NOMBRE_FICHERO");
		this.observaciones = row.getString("OBSERVACIONES");
		this.timestamp = row.get("TIMESTAMP", Date.class);
		this.tipoDocumento = TipoDocumentoAnexo.getEnum(row.getString("TIPO_DOCUMENTO"));
		this.tipoMIME = row.getString("TIPO_MIME");
		this.validacionOCSPCertificado = row.getString("TIPO_REGISTRO");
		this.validezDocumento = ValidezDocumento.getEnum(row.getString("VALIDEZ_DOCUMENTO"));
		// Atributos Flexia
		this.codDocumento = row.getInteger("COD_DOCUMENTO");
	}

	/**
	 * @return Certificado del anexo (parte publica).
	 */
	public String getCertificado() {
		return certificado;
	}

	public void setCertificado(String certificado) {
		this.certificado = certificado;
	}

	/**
	 * @return Codigo unico del anexo autogenerado por la base de datos.
	 */
	public int getCodAnexo() {
		return codAnexo;
	}

	public void setCodAnexo(int codAnexo) {
		this.codAnexo = codAnexo;
	}

	/**
	 * @return Codigo del {@link Asiento} al que pertenece el anexo.
	 */
	public int getCodAsiento() {
		return codAsiento;
	}

	public void setCodAsiento(int codAsiento) {
		this.codAsiento = codAsiento;
	}

	/**
	 * @return El contenido del anexo en base64.
	 */
	public byte[] getContenido() {
		return contenido;
	}

	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}

	/**
	 * @return CSV del archivo.
	 */
	public String getCsv() {
		return csv;
	}

	public void setCsv(String csv) {
		this.csv = csv;
	}

	/**
	 * @return Firma electronica del fichero anexo. Solo se debe informar cuando la firma de un documento se base en la
	 * generación de un CSV.
	 */
	public String getFirmaDocumento() {
		return firmaDocumento;
	}

	public void setFirmaDocumento(String firmaDocumento) {
		this.firmaDocumento = firmaDocumento;
	}

	/**
	 * @return Huella binaria del fichero anexo que garantiza la integridad de los archivos enviados.
	 */
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * @return Identificador del fichero objeto de la firma en caso de que el anexo sea firma de otro documento. Este
	 * campo tomará el valor de si mismo para indicar que contiene firma embebida. Dado que es el identificador de otro
	 * anexo, tiene el mismo formato que {@link Anexo#getIdentificadorFichero()}.
	 */
	public String getIdentificadorDocumentoFirmado() {
		return identificadorDocumentoFirmado;
	}

	public void setIdentificadorDocumentoFirmado(String identificadorDocumentoFirmado) {
		this.identificadorDocumentoFirmado = identificadorDocumentoFirmado;
	}

	/**
	 * @return Identificador del fichero anexo.
	 */
	public String getIdentificadorFichero() {
		return identificadorFichero;
	}

	/**
	 * @param identificadorFichero De formato
	 * IdentificadorDelIntercambio_CódigoDeTipoDeArchivo_NúmeroSecuencial.Extension del fichero. donde:
	 * <ul>
	 * <li>IdentificadorDelIntercambio: Codigo unico del intercambio. Ver
	 * {@link Asiento#setCodIntercambio(java.lang.String)}.</li>
	 * <li>CódigoDeTipoDeArchivo: Puede ser 00 si es un mensaje de datos de intercambio en formato XML o 01 si el
	 * fichero es un anexo.</li>
	 * <li>NumeroSecuencial: Hasta 4 digitos.</li>
	 * <li>Extensión: extension del archivo.</li>
	 * </ul>.
	 */
	public void setIdentificadorFichero(String identificadorFichero) {
		this.identificadorFichero = identificadorFichero;
	}

	/**
	 * @return Nombre del fichero original.
	 */
	public String getNombreFichero() {
		return nombreFichero;
	}

	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}

	/**
	 * @return Observaciones del fichero adjunto.
	 */
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return Sello de tiempo del fichero anexo.
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timeStamp) {
		this.timestamp = timeStamp;
	}

	/**
	 * @return {@link TipoDocumentoAnexo} del anexo.
	 */
	public TipoDocumentoAnexo getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumentoAnexo tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return Tipo MIME del anexo.
	 */
	public String getTipoMIME() {
		return tipoMIME;
	}

	public void setTipoMIME(String tipoMIME) {
		this.tipoMIME = tipoMIME;
	}

	/**
	 * @return Validacion del certificado
	 */
	public String getValidacionOCSPCertificado() {
		return validacionOCSPCertificado;
	}

	public void setValidacionOCSPCertificado(String validacionOCSPCertificado) {
		this.validacionOCSPCertificado = validacionOCSPCertificado;
	}

	/**
	 * @return {@link ValidezDocumento} del anexo.
	 */
	public ValidezDocumento getValidezDocumento() {
		return validezDocumento;
	}

	public void setValidezDocumento(ValidezDocumento validezDocumento) {
		this.validezDocumento = validezDocumento;
	}

	/**
	 * @return
	 */
	public int getCodDocumento() {
		return codDocumento;
	}

	public void setCodDocumento(int codDocumento) {
		this.codDocumento = codDocumento;
	}

	@Override
	public String toString() {
		return new StringBuilder("AnexoBean{")
				.append("certificado=").append(certificado)
				.append(", codAnexo=").append(codAnexo)
				.append(", codAsiento=").append(codAsiento)
				.append(", csv=").append(csv)
				.append(", contenido=").append(contenido)
				.append(", firmaDocumento=").append(firmaDocumento)
				.append(", hash=").append(hash).append(", tipoMIME=").append(tipoMIME)
				.append(", identificadorDocumentoFirmado=").append(identificadorDocumentoFirmado)
				.append(", identificadorFichero=").append(identificadorFichero)
				.append(", observaciones=").append(observaciones)
				.append(", nombreFichero=").append(nombreFichero)
				.append(", timeStamp=").append(timestamp)
				.append(", tipoDocumento=").append(tipoDocumento)
				.append(", validacionOCSPCertificado=").append(validacionOCSPCertificado)
				.append(", validezDocumento=").append(validezDocumento)
				.append('}').toString();
	}

}
