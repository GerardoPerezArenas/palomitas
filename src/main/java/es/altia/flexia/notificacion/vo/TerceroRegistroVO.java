package es.altia.flexia.notificacion.vo;


public class TerceroRegistroVO {
    private String nombre;
    private String nifTercero;
    private String tipoDocumento;
    private DireccionRegistroVO direccion;
    private String apellido1;
    private String apellido2;
    private String email;
    private String telefono;

    /**
     * @return the nombreTercero
     */
    public String getNombreTercero() {
        return nombre;
    }

    /**
     * @param nombreTercero the nombreTercero to set
     */
    public void setNombre(String nombreTercero) {
        this.nombre = nombreTercero;
    }

    /**
     * @return the nifTercero
     */
    public String getNifTercero() {
        return nifTercero;
    }

    /**
     * @param nifTercero the nifTercero to set
     */
    public void setNifTercero(String nifTercero) {
        this.nifTercero = nifTercero;
    }

    /**
     * @return the tipoDocumento
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @param tipoDocumento the tipoDocumento to set
     */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @return the direccion
     */
    public DireccionRegistroVO getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(DireccionRegistroVO direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the apellido1
     */
    public String getApellido1() {
        return apellido1;
    }

    /**
     * @param apellido1 the apellido1 to set
     */
    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    /**
     * @return the apellido2
     */
    public String getApellido2() {
        return apellido2;
    }

    /**
     * @param apellido2 the apellido2 to set
     */
    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}