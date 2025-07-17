/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.sir.model;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Kevin
 */
public class Justificante {
	
	private int codigoJustidicante;							//Codigo del justificante.
	private boolean isFirmado;								//Indica si el Justificante de Registro se devuelve firmado o no
	private byte[] contenido;								//Fichero Justificante de Registro codificado en Base64
	private String cdTpDoc;									//Indica el tipo de documento. Siempre es '02' = Documento adjunto al formulario
	private String cdValidez;								//Indica la categoría de autenticidad del documento.Siempre es '04' Origina
	private String csv;										//Código CSV del fichero Justificante de Registro
	private String codIntercambio;							//Código de Intercambio.
	private String dsCertificado;							//Certificado del fichero Justificante de Registro (parte pública)
	private String dsFirma;									//Firma electrónica del fichero Justificante de Registro
	private String dsHash;									//Huella binaria del fichero Justificante de Registro que garantiza la integridad de los archivos enviados
	private String dsTpMime;								//Tipo MIME del fichero Justificante de Registro
	private String dsValCertificado;						//Validación del certificado
	private String hash;									//Justificante de Registro
	private String idFichero;								//Identificador del fichero Justificante de Registro (uso interno de la librería)
	private String nombre;									//Nombre del Fichero Justificante de Registro
	private String numeroRegistro;							//Número de Registro
	private String tsAnexo;									//TimeStamp: Sello de tiempo del fichero Justificante de Registro
	private XMLGregorianCalendar fechaHoraPresentacion;		//Fecha y Hora de Presentación
	private XMLGregorianCalendar fechaHoraRegistro;			//Fecha y Hora de Registro

	/**
	 * TRUE: el justificante se devuelve firmado
	 * FALSE: el justificante se devuelve sin firmar
	 * @return 
	 */
	public boolean isIsFirmado() {
		return isFirmado;
	}
	
	/**
	 * TRUE: el justificante se devuelve firmado
	 * FALSE: el justificante se devuelve sin firmar
	 * @return 
	 */
	public void setIsFirmado(boolean isFirmado) {
		this.isFirmado = isFirmado;
	}

	public byte[] getContenido() {
		return contenido;
	}

	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}

	public String getCdTpDoc() {
		return cdTpDoc;
	}

	public void setCdTpDoc(String cdTpDoc) {
		this.cdTpDoc = cdTpDoc;
	}

	public String getCdValidez() {
		return cdValidez;
	}

	public void setCdValidez(String cdValidez) {
		this.cdValidez = cdValidez;
	}

	public String getCsv() {
		return csv;
	}

	public void setCsv(String csv) {
		this.csv = csv;
	}

	public String getCodIntercambio() {
		return codIntercambio;
	}

	public void setCodIntercambio(String codIntercambio) {
		this.codIntercambio = codIntercambio;
	}

	public String getDsCertificado() {
		return dsCertificado;
	}

	public void setDsCertificado(String dsCertificado) {
		this.dsCertificado = dsCertificado;
	}

	public String getDsFirma() {
		return dsFirma;
	}

	public void setDsFirma(String dsFirma) {
		this.dsFirma = dsFirma;
	}

	public String getDsTpMime() {
		return dsTpMime;
	}

	public void setDsTpMime(String dsTpMime) {
		this.dsTpMime = dsTpMime;
	}

	public String getDsHash() {
		return dsHash;
	}

	public void setDsHash(String dsHash) {
		this.dsHash = dsHash;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getIdFichero() {
		return idFichero;
	}

	public void setIdFichero(String idFichero) {
		this.idFichero = idFichero;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNumeroRegistro() {
		return numeroRegistro;
	}

	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public String getTsAnexo() {
		return tsAnexo;
	}

	public void setTsAnexo(String tsAnexo) {
		this.tsAnexo = tsAnexo;
	}

	public XMLGregorianCalendar getFechaHoraPresentacion() {
		return fechaHoraPresentacion;
	}

	public void setFechaHoraPresentacion(XMLGregorianCalendar fechaHoraPresentacion) {
		this.fechaHoraPresentacion = fechaHoraPresentacion;
	}

	public XMLGregorianCalendar getFechaHoraRegistro() {
		return fechaHoraRegistro;
	}

	public void setFechaHoraRegistro(XMLGregorianCalendar fechaHoraRegistro) {
		this.fechaHoraRegistro = fechaHoraRegistro;
	}

	public String getDsValCertificado() {
		return dsValCertificado;
	}

	public void setDsValCertificado(String dsValCertificado) {
		this.dsValCertificado = dsValCertificado;
	}

	public int getCodigoJustidicante() {
		return codigoJustidicante;
	}

	public void setCodigoJustidicante(int codigoJustidicante) {
		this.codigoJustidicante = codigoJustidicante;
	}
	
	
	
}