package es.altia.flexia.registro.digitalizacion.lanbide.vo;

public class DigitalizacionDocumentosLanbideVO {
    private String urlServicio;
    private String tipo;
    private String aplicacion;
    private String auditUser;
    private String idioma;

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the aplicacion
     */
    public String getAplicacion() {
        return aplicacion;
    }

    /**
     * @param aplicacion the aplicacion to set
     */
    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    /**
     * @return the auditUser
     */
    public String getAuditUser() {
        return auditUser;
    }

    /**
     * @param auditUser the auditUser to set
     */
    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    /**
     * @return the urlServicio
     */
    public String getUrlServicio() {
        return urlServicio;
    }

    /**
     * @param urlServicio the urlServicio to set
     */
    public void setUrlServicio(String urlServicio) {
        this.urlServicio = urlServicio;
    }
}
