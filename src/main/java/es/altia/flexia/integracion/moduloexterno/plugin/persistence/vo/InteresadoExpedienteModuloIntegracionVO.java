package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;

import java.util.ArrayList;


public class InteresadoExpedienteModuloIntegracionVO {
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String particula1;
    private String particula2;
    private int codigoTercero;
    private int numeroVersion;
    private String numeroTelefonoFax;
    private String nombreCompleto;
    private String email;
    private int tipoDocumento;
    private String documento;
    private String descripcionDocumento;
    private int codigoRol;
    private String descripcionRol;
    private boolean rolDefecto;
    private boolean admiteNotificacionElectronica;
    private int codDomicilioExpediente;
    private ArrayList<DomicilioInteresadoModuloIntegracionVO> domicilios;
    

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
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
     * @return the codigoTercero
     */
    public int getCodigoTercero() {
        return codigoTercero;
    }

    /**
     * @param codigoTercero the codigoTercero to set
     */
    public void setCodigoTercero(int codigoTercero) {
        this.codigoTercero = codigoTercero;
    }

    /**
     * @return the numeroVersion
     */
    public int getNumeroVersion() {
        return numeroVersion;
    }

    /**
     * @param numeroVersion the numeroVersion to set
     */
    public void setNumeroVersion(int numeroVersion) {
        this.numeroVersion = numeroVersion;
    }

    /**
     * @return the numeroTelefonoFax
     */
    public String getNumeroTelefonoFax() {
        return numeroTelefonoFax;
    }

    /**
     * @param numeroTelefonoFax the numeroTelefonoFax to set
     */
    public void setNumeroTelefonoFax(String numeroTelefonoFax) {
        this.numeroTelefonoFax = numeroTelefonoFax;
    }

    /**
     * @return the nombreCompleto
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * @param nombreCompleto the nombreCompleto to set
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
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
     * @return the tipoDocumento
     */
    public int getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @param tipoDocumento the tipoDocumento to set
     */
    public void setTipoDocumento(int tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @return the documento
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * @param documento the documento to set
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /**
     * @return the descripcionDocumento
     */
    public String getDescripcionDocumento() {
        return descripcionDocumento;
    }

    /**
     * @param descripcionDocumento the descripcionDocumento to set
     */
    public void setDescripcionDocumento(String descripcionDocumento) {
        this.descripcionDocumento = descripcionDocumento;
    }

    /**
     * @return the codigoRol
     */
    public int getCodigoRol() {
        return codigoRol;
    }

    /**
     * @param codigoRol the codigoRol to set
     */
    public void setCodigoRol(int codigoRol) {
        this.codigoRol = codigoRol;
    }

    /**
     * @return the descripcionRol
     */
    public String getDescripcionRol() {
        return descripcionRol;
    }

    /**
     * @param descripcionRol the descripcionRol to set
     */
    public void setDescripcionRol(String descripcionRol) {
        this.descripcionRol = descripcionRol;
    }

    /**
     * @return the rolDefecto
     */
    public boolean isRolDefecto() {
        return rolDefecto;
    }

    /**
     * @param rolDefecto the rolDefecto to set
     */
    public void setRolDefecto(boolean rolDefecto) {
        this.rolDefecto = rolDefecto;
    }

    /**
     * @return the particula1
     */
    public String getParticula1() {
        return particula1;
    }

    /**
     * @param particula1 the particula1 to set
     */
    public void setParticula1(String particula1) {
        this.particula1 = particula1;
    }

    /**
     * @return the particula2
     */
    public String getParticula2() {
        return particula2;
    }

    /**
     * @param particula2 the particula2 to set
     */
    public void setParticula2(String particula2) {
        this.particula2 = particula2;
    }

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("codTercero: " + this.getCodigoTercero() + ",version: " + this.getNumeroVersion() + ", nombre: " + this.getNombre() + ", apellido1: " + this.getApellido1() + ", apellido2: " + this.getApellido2() + ", ");        
        sb.append("nombre completo: " + this.getNombreCompleto() + ", tipoDocumento: " + this.getTipoDocumento() + ",documento:" + this.getDocumento() +  ", descripcionDocumento: " + this.getDescripcionDocumento() + ", ");
        sb.append("particula1: " + this.getParticula1() + ", particula2: " + this.getParticula1() + ", email: " + this.getEmail() + ", nº telefono/fax " + this.getNumeroTelefonoFax() + ", ");
        sb.append("codigoRol: " + this.getCodigoRol() + ",descrol: " + this.getDescripcionRol() + ", es rol por defecto?: " + this.isRolDefecto());
        sb.append("admiteNotificacionesElectronicas: " + this.isAdmiteNotificacionElectronica());
        return sb.toString();
    }

   
    /**
     * Permite indicar si el interesado admite notificaciones electrónicas
     * @param admiteNotificacionElectronica: true si las admite y false en caso contrario
     */
    public void setAdmiteNotificacionElectronica(boolean admiteNotificacionElectronica) {
        this.admiteNotificacionElectronica = admiteNotificacionElectronica;
    }

    /**
     * @return the admiteNotificacionElectronica
     */
    public boolean isAdmiteNotificacionElectronica() {
        return admiteNotificacionElectronica;
    }

    /**
     * @return the domicilios
     */
    public ArrayList<DomicilioInteresadoModuloIntegracionVO> getDomicilios() {
        return domicilios;
    }

    /**
     * @param domicilios the domicilios to set
     */
    public void setDomicilios(ArrayList<DomicilioInteresadoModuloIntegracionVO> domicilios) {
        this.domicilios = domicilios;
    }

    /**
     * @return the codDomicilioExpediente
     */
    public int getCodDomicilioExpediente() {
        return codDomicilioExpediente;
    }

    /**
     * @param codDomicilioExpediente the codDomicilioExpediente to set
     */
    public void setCodDomicilioExpediente(int codDomicilioExpediente) {
        this.codDomicilioExpediente = codDomicilioExpediente;
    }
   
}