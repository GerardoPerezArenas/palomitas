package es.altia.agora.business.sge;
/**
  * Representa un documento de inicio de expediente
  * @author oscar
  */
public class DocumentoInicioExpedienteVO {
  
    private int codigo;
    private String nombreDocumento;
    private String condicion;
    private boolean entregado;
    private String fechaEntrega;
    private int codDocumentoAdjunto;
    private String tipoMimeDocumentoAdjunto;
    private String nombreDocumentoAdjunto;
    private String fechaAltaDocumentoAdjunto;
    private String estadoFirmaDocumentoAdjunto;
    private int numeroFirmas;
    private String numExpediente;
    private int codOrganizacion;
    private String ejercicio;
    private String codProcedimiento;
    private boolean expedienteHistorico;
    
    /**
     * @return the codigo
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
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
     * @return the condicion
     */
    public String getCondicion() {
        return condicion;
    }

    /**
     * @param condicion the condicion to set
     */
    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    /**
     * @return the entregado
     */
    public boolean isEntregado() {
        return entregado;
    }

    /**
     * @param entregado the entregado to set
     */
    public void setEntregado(boolean entregado) {
        this.entregado = entregado;
    }

    /**
     * @return the fechaEntrega
     */
    public String getFechaEntrega() {
        return fechaEntrega;
    }

    /**
     * @param fechaEntrega the fechaEntrega to set
     */
    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    /**
     * @return the codDocumentoAdjunto
     */
    public int getCodDocumentoAdjunto() {
        return codDocumentoAdjunto;
    }

    /**
     * @param codDocumentoAdjunto the codDocumentoAdjunto to set
     */
    public void setCodDocumentoAdjunto(int codDocumentoAdjunto) {
        this.codDocumentoAdjunto = codDocumentoAdjunto;
    }

    /**
     * @return the tipoMimeDocumentoAdjunto
     */
    public String getTipoMimeDocumentoAdjunto() {
        return tipoMimeDocumentoAdjunto;
    }

    /**
     * @param tipoMimeDocumentoAdjunto the tipoMimeDocumentoAdjunto to set
     */
    public void setTipoMimeDocumentoAdjunto(String tipoMimeDocumentoAdjunto) {
        this.tipoMimeDocumentoAdjunto = tipoMimeDocumentoAdjunto;
    }

    /**
     * @return the nombreDocumentoAdjunto
     */
    public String getNombreDocumentoAdjunto() {
        return nombreDocumentoAdjunto;
    }

    /**
     * @param nombreDocumentoAdjunto the nombreDocumentoAdjunto to set
     */
    public void setNombreDocumentoAdjunto(String nombreDocumentoAdjunto) {
        this.nombreDocumentoAdjunto = nombreDocumentoAdjunto;
    }

    /**
     * @return the fechaAltaDocumentoAdjunto
     */
    public String getFechaAltaDocumentoAdjunto() {
        return fechaAltaDocumentoAdjunto;
    }

    /**
     * @param fechaAltaDocumentoAdjunto the fechaAltaDocumentoAdjunto to set
     */
    public void setFechaAltaDocumentoAdjunto(String fechaAltaDocumentoAdjunto) {
        this.fechaAltaDocumentoAdjunto = fechaAltaDocumentoAdjunto;
    }

    /**
     * @return the estadoFirmaDocumentoAdjunto
     */
    public String getEstadoFirmaDocumentoAdjunto() {
        return estadoFirmaDocumentoAdjunto;
    }

    /**
     * @param estadoFirmaDocumentoAdjunto the estadoFirmaDocumentoAdjunto to set
     */
    public void setEstadoFirmaDocumentoAdjunto(String estadoFirmaDocumentoAdjunto) {
        this.estadoFirmaDocumentoAdjunto = estadoFirmaDocumentoAdjunto;
    }

    /**
     * @return the numeroFirmas
     */
    public int getNumeroFirmas() {
        return numeroFirmas;
    }

    /**
     * @param numeroFirmas the numeroFirmas to set
     */
    public void setNumeroFirmas(int numeroFirmas) {
        this.numeroFirmas = numeroFirmas;
    }

    /**
     * @return the numExpediente
     */
    public String getNumExpediente() {
        return numExpediente;
    }

    /**
     * @param numExpediente the numExpediente to set
     */
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    /**
     * @return the codOrganizacion
     */
    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    /**
     * @param codOrganizacion the codOrganizacion to set
     */
    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    /**
     * @return the ejercicio
     */
    public String getEjercicio() {
        return ejercicio;
    }

    /**
     * @param ejercicio the ejercicio to set
     */
    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    /**
     * @return the codProcedimiento
     */
    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    /**
     * @param codProcedimiento the codProcedimiento to set
     */
    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    /**
     * @return the expedienteHistorico
     */
    public boolean isExpedienteHistorico() {
        return expedienteHistorico;
    }

    /**
     * @param expedienteHistorico the expedienteHistorico to set
     */
    public void setExpedienteHistorico(boolean expedienteHistorico) {
        this.expedienteHistorico = expedienteHistorico;
    }     
    
}