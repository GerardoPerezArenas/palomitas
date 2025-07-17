package es.altia.agora.business.portafirmas.documentofirma.vo;

import java.util.Calendar;

/**
 *
 * @author Tiffany
 */
public class DocumentoProcedimientoFirmaVO {
    
    private Calendar fechaEnvioFirma;
    private Calendar fechaFirma;
    private String nombreDocumento;
    private String estadoFirma;    
    private int codigoDocumento;
    private int idPresentado;
    private String codigoUsuarioFirma;    
    private int idEjercicio;
    private String tipoMime;
    private String extension;
    private String observaciones;
    private byte[] contenido;
    private byte[] firma;
    private String[] params;
    private String codOrganizacion;
    private String idNumeroExpediente;
    private String idProcedimiento;
    private String nombreProcedimiento;
    private int orden;
    private int idNumFirma;
    private String finalizaRechazo;

    public DocumentoProcedimientoFirmaVO() {
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    public int getIdPresentado() {
        return idPresentado;
    }

    public void setIdPresentado(int idPresentado) {
        this.idPresentado = idPresentado;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public Calendar getFechaFirma() {
        return fechaFirma;
    }

    public void setFechaFirma(Calendar fechaFirma) {
        this.fechaFirma = fechaFirma;
    }

    public int getIdNumFirma() {
        return idNumFirma;
    }

    public void setIdNumFirma(int idNumFirma) {
        this.idNumFirma = idNumFirma;
    }

    public String getCodigoUsuarioFirma() {
        return codigoUsuarioFirma;
    }

    public void setCodigoUsuarioFirma(String codigoUsuarioFirma) {
        this.codigoUsuarioFirma = codigoUsuarioFirma;
    }

    public byte[] getFirma() {
        return firma;
    }

    public void setFirma(byte[] firma) {
        this.firma = firma;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getIdProcedimiento() {
        return idProcedimiento;
    }

    public void setIdProcedimiento(String idProcedimiento) {
        this.idProcedimiento = idProcedimiento;
    }

    public String getNombreProcedimiento() {
        return nombreProcedimiento;
    }

    public void setNombreProcedimiento(String nombreProcedimiento) {
        this.nombreProcedimiento = nombreProcedimiento;
    }

    public Calendar getFechaEnvioFirma() {
        return fechaEnvioFirma;
    }

    public void setFechaEnvioFirma(Calendar fechaEnvioFirma) {
        this.fechaEnvioFirma = fechaEnvioFirma;
    }

    public String getIdNumeroExpediente() {
        return idNumeroExpediente;
    }

    public void setIdNumeroExpediente(String idNumeroExpediente) {
        this.idNumeroExpediente = idNumeroExpediente;
    }

    public int getCodigoDocumento() {
        return codigoDocumento;
    }

    public void setCodigoDocumento(int codigoDocumento) {
        this.codigoDocumento = codigoDocumento;
    }

    public String getEstadoFirma() {
        return estadoFirma;
    }

    public void setEstadoFirma(String estadoFirma) {
        this.estadoFirma = estadoFirma;
    }

    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public int getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(int idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public String getFinalizaRechazo() {
        return finalizaRechazo;
    }

    public void setFinalizaRechazo(String finalizaRechazo) {
            this.finalizaRechazo = finalizaRechazo;
    }

    
    
        
}
