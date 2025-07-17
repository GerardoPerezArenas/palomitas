package es.altia.flexia.sir.vo;

import es.altia.flexia.sir.model.Anexo;
import java.util.Date;

public class AnexoValueObject {
	private String certificado;
	private Integer codAnexo;
	private Integer codAsiento;
	private byte[] contenido;
	private String csv;
	private String firmaDocumento;
	private String hash;
	private String identificadorDocumentoFirmado;
	private String identificadorFichero;
	private String nombreFichero;
	private String observaciones;
	private Date timestamp;
	private String tipoDocumento;
	private String tipoMIME;
	private String validacionOCSPCertificado;
	private String validezDocumento;
	
	public AnexoValueObject(Anexo anexo) {
		this.certificado = anexo.getCertificado();
		this.codAnexo = anexo.getCodAnexo();
		this.codAsiento = anexo.getCodAsiento();
		this.contenido = anexo.getContenido();
		this.csv = anexo.getCsv();
		this.firmaDocumento = anexo.getFirmaDocumento();
		this.hash = anexo.getHash();
		this.identificadorDocumentoFirmado = anexo.getIdentificadorDocumentoFirmado();
		this.identificadorFichero = anexo.getIdentificadorFichero();
		this.nombreFichero = anexo.getNombreFichero();
		this.observaciones = anexo.getObservaciones();
		this.timestamp = anexo.getTimestamp();
		this.tipoDocumento = (anexo.getTipoDocumento() != null) ? anexo.getTipoDocumento().getValor() : null;
		this.tipoMIME = anexo.getTipoMIME();
		this.validacionOCSPCertificado = anexo.getValidacionOCSPCertificado();
		this.validezDocumento = (anexo.getValidezDocumento() != null) ? anexo.getValidezDocumento().getValor() : null;
	}

	public String getCertificado() {
		return certificado;
	}

	public void setCertificado(String certificado) {
		this.certificado = certificado;
	}

	public Integer getCodAnexo() {
		return codAnexo;
	}

	public void setCodAnexo(Integer codAnexo) {
		this.codAnexo = codAnexo;
	}

	public Integer getCodAsiento() {
		return codAsiento;
	}

	public void setCodAsiento(Integer codAsiento) {
		this.codAsiento = codAsiento;
	}

	public byte[] getContenido() {
		return contenido;
	}

	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}

	public String getCsv() {
		return csv;
	}

	public void setCsv(String csv) {
		this.csv = csv;
	}

	public String getFirmaDocumento() {
		return firmaDocumento;
	}

	public void setFirmaDocumento(String firmaDocumento) {
		this.firmaDocumento = firmaDocumento;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getIdentificadorDocumentoFirmado() {
		return identificadorDocumentoFirmado;
	}

	public void setIdentificadorDocumentoFirmado(String identificadorDocumentoFirmado) {
		this.identificadorDocumentoFirmado = identificadorDocumentoFirmado;
	}

	public String getIdentificadorFichero() {
		return identificadorFichero;
	}

	public void setIdentificadorFichero(String identificadorFichero) {
		this.identificadorFichero = identificadorFichero;
	}

	public String getNombreFichero() {
		return nombreFichero;
	}

	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getTipoMIME() {
		return tipoMIME;
	}

	public void setTipoMIME(String tipoMIME) {
		this.tipoMIME = tipoMIME;
	}

	public String getValidacionOCSPCertificado() {
		return validacionOCSPCertificado;
	}

	public void setValidacionOCSPCertificado(String validacionOCSPCertificado) {
		this.validacionOCSPCertificado = validacionOCSPCertificado;
	}

	public String getValidezDocumento() {
		return validezDocumento;
	}

	public void setValidezDocumento(String validezDocumento) {
		this.validezDocumento = validezDocumento;
	}
	
	
}
