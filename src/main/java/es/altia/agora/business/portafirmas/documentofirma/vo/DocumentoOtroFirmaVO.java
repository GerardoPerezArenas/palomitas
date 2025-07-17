package es.altia.agora.business.portafirmas.documentofirma.vo;

import es.altia.util.commons.DateOperations;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author oscar.rodriguez
 */
public class DocumentoOtroFirmaVO {
    private Calendar fechaEnvioFirma;
    private Calendar fechaFirma;
    private String nombreDocumento;
    private String estadoFirma;    
    private int codigoDocumento;
    private String codigoUsuarioAlta;
    private String nifUsuario;
    private String codigoUsuarioFirma;    
    private int ejercicio;
    private String tipoMime;
    private String extension;
    private String observaciones;
    private byte[] contenido;
    private byte[] firma;
    private String[] params;
    private String codOrganizacion;
    private String finalizaRechazo;
    /**
     * @return the fechaEnvioFirma
     */
    public Calendar getFechaEnvioFirma() {
        return fechaEnvioFirma;
    }

    /**
     * @param fechaEnvioFirma the fechaEnvioFirma to set
     */
    public void setFechaEnvioFirma(Calendar fechaEnvioFirma) {
        this.fechaEnvioFirma = fechaEnvioFirma;
    }

    /**
     * @return the fechaFirma
     */
    public Calendar getFechaFirma() {
        return fechaFirma;
    }

    /**
     * @param fechaFirma the fechaFirma to set
     */
    public void setFechaFirma(Calendar fechaFirma) {
        this.fechaFirma = fechaFirma;
    }

    /**
     * @return the nombreDocumento
     */
    public String getNombreDocumento() {
        return nombreDocumento;
    }

    /**
     * @param nombreDocumento the nombreDocumento to set
     */
    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    /**
     * @return the estadoFirma
     */
    public String getEstadoFirma() {
        return estadoFirma;
    }

    /**
     * @param estadoFirma the estadoFirma to set
     */
    public void setEstadoFirma(String estadoFirma) {
        this.estadoFirma = estadoFirma;
    }

    /**
     * @return the fechaEnvioFirmaAsDate
     */
    public Date getFechaEnvioFirmaAsDate() {
        return DateOperations.toDate(this.fechaEnvioFirma);
    }

      /**
     * @return the fechaFirmaAsDate
     */
    public Date getFechaFirmaAsDate() {
        return DateOperations.toDate(this.fechaFirma);
    }

  
    /**
     * @return the codigoDocumento
     */
    public int getCodigoDocumento() {
        return codigoDocumento;
    }

    /**
     * @param codigoDocumento the codigoDocumento to set
     */
    public void setCodigoDocumento(int codigoDocumento) {
        this.codigoDocumento = codigoDocumento;
    }

    /**
     * @return the codigoUsuarioAlta
     */
    public String getCodigoUsuarioAlta() {
        return codigoUsuarioAlta;
    }

    /**
     * @param codigoUsuarioAlta the codigoUsuarioAlta to set
     */
    public void setCodigoUsuarioAlta(String codigoUsuarioAlta) {
        this.codigoUsuarioAlta = codigoUsuarioAlta;
    }

    /**
     * @return the codigoUsuarioFirma
     */
    public String getCodigoUsuarioFirma() {
        return codigoUsuarioFirma;
    }

    /**
     * @param codigoUsuarioFirma the codigoUsuarioFirma to set
     */
    public void setCodigoUsuarioFirma(String codigoUsuarioFirma) {
        this.codigoUsuarioFirma = codigoUsuarioFirma;
    }


    /**
     * @return the ejercicio
     */
    public int getEjercicio() {
        return ejercicio;
    }

    /**
     * @param ejercicio the ejercicio to set
     */
    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    /**
     * @return the tipoMime
     */
    public String getTipoMime() {
        return tipoMime;
    }

    /**
     * @param tipoMime the tipoMime to set
     */
    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
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

    /**
     * @return the nifUsuario
     */
    public String getNifUsuario() {
        return nifUsuario;
    }

    /**
     * @param nifUsuario the nifUsuario to set
     */
    public void setNifUsuario(String nifUsuario) {
        this.nifUsuario = nifUsuario;
    }

    /**
     * @return the contenido
     */
    public byte[] getContenido() {
        return contenido;
    }

    /**
     * @param contenido the contenido to set
     */
    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    /**
     * @return the firma
     */
    public byte[]  getFirma() {
        return firma;
    }

    /**
     * @param firma the firma to set
     */
    public void setFirma(byte[] firma) {
        this.firma = firma;
    }

    /**
     * @return the observaciones
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * @param observaciones the observaciones to set
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * @return the params
     */
    public String[] getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(String[] params) {
        this.params = params;
    }

    /**
     * @return the codOrganizacion
     */
    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    /**
     * @param codOrganizacion the codOrganizacion to set
     */
    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public String getFinalizaRechazo() {
        return finalizaRechazo;
    }

    public void setFinalizaRechazo(String finalizaRechazo) {
        this.finalizaRechazo = finalizaRechazo;
    }
    
    

}