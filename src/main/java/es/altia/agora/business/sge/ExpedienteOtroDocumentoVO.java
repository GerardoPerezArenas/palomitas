/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.business.sge;

import java.io.Serializable;

/**
 *
 * @author manuel.bahamonde
 */
public class ExpedienteOtroDocumentoVO implements  Serializable{

    private String municipio;
    private String ejercicio;
    private String numeroExpediente;
    private String codigoDocumento;
    private String nombreDocumento;
    private String fechaAltaDocuemento;
    private byte[] contenidoDocumento = null;
    private String tipoDocumento;       // mimetype del documento agregado
    private String extension;
    private Long idMetadato;
    
    public ExpedienteOtroDocumentoVO() {
    }

    public ExpedienteOtroDocumentoVO(String municipio, String ejercicio, String numeroExpediente, String codigoDocumento, String nombreDocumento, String fechaAltaDocuemento, String tipoDocumento,String extension) {
        this.municipio = municipio;
        this.ejercicio = ejercicio;
        this.numeroExpediente = numeroExpediente;
        this.codigoDocumento = codigoDocumento;
        this.nombreDocumento = nombreDocumento;
        this.fechaAltaDocuemento = fechaAltaDocuemento;
        this.tipoDocumento = tipoDocumento;
        this.extension = extension;
    }

    public String getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    

    public String getCodigoDocumento() {
        return codigoDocumento;
    }

    public void setCodigoDocumento(String codigoDocumento) {
        this.codigoDocumento = codigoDocumento;
    }

    public byte[] getContenidoDocumento() {
        return contenidoDocumento;
    }

    public void setContenidoDocumento(byte[] contenidoDocumento) {
        this.contenidoDocumento = contenidoDocumento;
    }

    public String getFechaAltaDocuemento() {
        return fechaAltaDocuemento;
    }

    public void setFechaAltaDocuemento(String fechaAltaDocuemento) {
        this.fechaAltaDocuemento = fechaAltaDocuemento;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @param extension the extension to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Long getIdMetadato() {
        return idMetadato;
    }
    
    public void setIdMetadato(Long idMetadato) {
        this.idMetadato = idMetadato;
    }

}
