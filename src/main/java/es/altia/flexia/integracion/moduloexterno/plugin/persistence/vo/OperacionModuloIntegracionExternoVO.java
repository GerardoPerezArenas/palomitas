package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;

/**
 * Representa a una operación de un módulo de integración externo
 */
public class OperacionModuloIntegracionExternoVO {
    private String codigoOperacion;
    private String tituloOperacion;
    private String nombreModulo;
    private String descripcionModulo;
    private String urlPantallaConfiguracion;
    private String altoPantallaConfiguracion;
    private String anchoPantallaConfiguracion;

    /**
     * @return the codigoOperacion
     */
    public String getCodigoOperacion() {
        return codigoOperacion;
    }

    /**
     * @param codigoOperacion the codigoOperacion to set
     */
    public void setCodigoOperacion(String codigoOperacion) {
        this.codigoOperacion = codigoOperacion;
    }

    /**
     * @return the tituloOperacion
     */
    public String getTituloOperacion() {
        return tituloOperacion;
    }

    /**
     * @param tituloOperacion the tituloOperacion to set
     */
    public void setTituloOperacion(String tituloOperacion) {
        this.tituloOperacion = tituloOperacion;
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
     * @return the descripcionModulo
     */
    public String getDescripcionModulo() {
        return descripcionModulo;
    }

    /**
     * @param descripcionModulo the descripcionModulo to set
     */
    public void setDescripcionModulo(String descripcionModulo) {
        this.descripcionModulo = descripcionModulo;
    }

    /**
     * @return the urlPantallaConfiguracion
     */
    public String getUrlPantallaConfiguracion() {
        return urlPantallaConfiguracion;
    }

    /**
     * @param urlPantallaConfiguracion the urlPantallaConfiguracion to set
     */
    public void setUrlPantallaConfiguracion(String urlPantallaConfiguracion) {
        this.urlPantallaConfiguracion = urlPantallaConfiguracion;
    }

    /**
     * @return the altoPantallaConfiguracion
     */
    public String getAltoPantallaConfiguracion() {
        return altoPantallaConfiguracion;
    }

    /**
     * @param altoPantallaConfiguracion the altoPantallaConfiguracion to set
     */
    public void setAltoPantallaConfiguracion(String altoPantallaConfiguracion) {
        this.altoPantallaConfiguracion = altoPantallaConfiguracion;
    }

    /**
     * @return the anchoPantallaConfiguracion
     */
    public String getAnchoPantallaConfiguracion() {
        return anchoPantallaConfiguracion;
    }

    /**
     * @param anchoPantallaConfiguracion the anchoPantallaConfiguracion to set
     */
    public void setAnchoPantallaConfiguracion(String anchoPantallaConfiguracion) {
        this.anchoPantallaConfiguracion = anchoPantallaConfiguracion;
    }
    
}