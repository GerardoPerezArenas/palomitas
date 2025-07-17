package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;


public class TareasPendientesInicioTramiteVO {
    private int idTarea;
    private int codMunicipio;
    private long codOperacion;
    private int codTramite;
    private int ocurrenciaTramite;
    private String numeroExpediente;
    private boolean errorPersonalizado;
    private String etiquetaErrorPersonalizado;
    private String nombreOperacion;
    private boolean operacionModulo;
    private String nombreModulo;
    private String mensajeError;


    /**
     * @return the idTarea
     */
    public int getIdTarea() {
        return idTarea;
    }

    /**
     * @param idTarea the idTarea to set
     */
    public void setIdTarea(int idTarea) {
        this.idTarea = idTarea;
    }

    /**
     * @return the codMunicipio
     */
    public int getCodMunicipio() {
        return codMunicipio;
    }

    /**
     * @param codMunicipio the codMunicipio to set
     */
    public void setCodMunicipio(int codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    /**
     * @return the codOperacion
     */
    public long getCodOperacion() {
        return codOperacion;
    }

    /**
     * @param codOperacion the codOperacion to set
     */
    public void setCodOperacion(long codOperacion) {
        this.codOperacion = codOperacion;
    }

    /**
     * @return the codTramite
     */
    public int getCodTramite() {
        return codTramite;
    }

    /**
     * @param codTramite the codTramite to set
     */
    public void setCodTramite(int codTramite) {
        this.codTramite = codTramite;
    }

    /**
     * @return the ocurrenciaTramite
     */
    public int getOcurrenciaTramite() {
        return ocurrenciaTramite;
    }

    /**
     * @param ocurrenciaTramite the ocurrenciaTramite to set
     */
    public void setOcurrenciaTramite(int ocurrenciaTramite) {
        this.ocurrenciaTramite = ocurrenciaTramite;
    }

    /**
     * @return the numeroExpediente
     */
    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    /**
     * @param numeroExpediente the numeroExpediente to set
     */
    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    /**
     * @return the errorPersonalizado
     */
    public boolean isErrorPersonalizado() {
        return errorPersonalizado;
    }

    /**
     * @param errorPersonalizado the errorPersonalizado to set
     */
    public void setErrorPersonalizado(boolean errorPersonalizado) {
        this.errorPersonalizado = errorPersonalizado;
    }

    /**
     * @return the etiquetaErrorPersonalizado
     */
    public String getEtiquetaErrorPersonalizado() {
        return etiquetaErrorPersonalizado;
    }

    /**
     * @param etiquetaErrorPersonalizado the etiquetaErrorPersonalizado to set
     */
    public void setEtiquetaErrorPersonalizado(String etiquetaErrorPersonalizado) {
        this.etiquetaErrorPersonalizado = etiquetaErrorPersonalizado;
    }

    /**
     * @return the nombreOperacion
     */
    public String getNombreOperacion() {
        return nombreOperacion;
    }

    /**
     * @param nombreOperacion the nombreOperacion to set
     */
    public void setNombreOperacion(String nombreOperacion) {
        this.nombreOperacion = nombreOperacion;
    }

    /**
     * @return the operacionModulo
     */
    public boolean isOperacionModulo() {
        return operacionModulo;
    }

    /**
     * @param operacionModulo the operacionModulo to set
     */
    public void setOperacionModulo(boolean operacionModulo) {
        this.operacionModulo = operacionModulo;
    }

    /**
     * @return the nombreModulo
     */
    public String getNombreModulo() {
        return nombreModulo;
    }

    /**
     * @param nombreModulo the nombreModulo to set
     */
    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }

    /**
     * @return the mensajeError
     */
    public String getMensajeError() {
        return mensajeError;
    }

    /**
     * @param mensajeError the mensajeError to set
     */
    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

}