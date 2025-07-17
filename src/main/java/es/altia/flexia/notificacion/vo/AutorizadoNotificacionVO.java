/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.notificacion.vo;

public class AutorizadoNotificacionVO {

    private String nif;
    private String tipoDocumento;
    private String nombre;
    private String email;
    private int codigoMunicipio;
    private int ejercicio;
    private String numeroExpediente;
    private int codigoTercero;
    private int numeroVersionTercero;
    private String seleccionado;
    private int codigoNotificacion;
    private String apellido1;
    private String apellido2;
    private String telefono;
    private String nombreCompleto;
    private int codDomicilio;
    private int rol;


    private String codPais;
    private String codProvincia;
    private String descProvincia;
    private String codMunicipio;
    private String descMunicipio;
    private String codVia;
    private String descVia;
    private String codPostal;
    private String direccion;




//nif: NIF del autorizado al que se le dirige la notificación.
//nombre: De tipo String. Nombre del autorizado.
//email: De tipo String. Representa el email del autorizado.
//codigoMunicipio: De tipo int. Representa el código del municipio.
//ejercicio: De tipo int. Representa el ejercicio.
//numeroExpediente: Número del expediente
//codigoTercero: Código del tercero o interesado que admite notificaciones electrónicas y al que se le envía la notificación.
//numeroVersionTercero: Número de versión del tercero
//seleccionado: De tipo String. Si el adjunto está asociado a una notificación, entonces contiene el valor ?SI?, en caso contrario, contiene el valor ?NO?.

    public int getCodigoNotificacion() {
        return codigoNotificacion;
    }

    public void setCodigoNotificacion(int codigoNotificacion) {
        this.codigoNotificacion = codigoNotificacion;
    }

    public int getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(int codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public int getCodigoTercero() {
        return codigoTercero;
    }

    public void setCodigoTercero(int codigoTercero) {
        this.codigoTercero = codigoTercero;
    }

    public int getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public int getNumeroVersionTercero() {
        return numeroVersionTercero;
    }

    public void setNumeroVersionTercero(int numeroVersionTercero) {
        this.numeroVersionTercero = numeroVersionTercero;
    }

    public String getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(String seleccionado) {
        this.seleccionado = seleccionado;
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

    public void setApellido2(String apellido2){
        this.apellido2 = apellido2;
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

    public int getCodDomicilio() {
        return codDomicilio;
    }

    public void setCodDomicilio(int codDomicilio) {
        this.codDomicilio = codDomicilio;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }
    
    
    
    

    /**
     * @return the codPais
     */
    public String getCodPais() {
        return codPais;
    }

    /**
     * @param codPais the codPais to set
     */
    public void setCodPais(String codPais) {
        this.codPais = codPais;
    }

    /**
     * @return the codProvincia
     */
    public String getCodProvincia() {
        return codProvincia;
    }

    /**
     * @param codProvincia the codProvincia to set
     */
    public void setCodProvincia(String codProvincia) {
        this.codProvincia = codProvincia;
    }

    /**
     * @return the descProvincia
     */
    public String getDescProvincia() {
        return descProvincia;
    }

    /**
     * @param descProvincia the descProvincia to set
     */
    public void setDescProvincia(String descProvincia) {
        this.descProvincia = descProvincia;
    }

    /**
     * @return the codMunicipio
     */
    public String getCodMunicipio() {
        return codMunicipio;
    }

    /**
     * @param codMunicipio the codMunicipio to set
     */
    public void setCodMunicipio(String codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    /**
     * @return the descMunicipio
     */
    public String getDescMunicipio() {
        return descMunicipio;
    }

    /**
     * @param descMunicipio the descMunicipio to set
     */
    public void setDescMunicipio(String descMunicipio) {
        this.descMunicipio = descMunicipio;
    }

    /**
     * @return the codVia
     */
    public String getCodVia() {
        return codVia;
    }

    /**
     * @param codVia the codVia to set
     */
    public void setCodVia(String codVia) {
        this.codVia = codVia;
    }

    /**
     * @return the descVia
     */
    public String getDescVia() {
        return descVia;
    }

    /**
     * @param descVia the descVia to set
     */
    public void setDescVia(String descVia) {
        this.descVia = descVia;
    }

    /**
     * @return the codPostal
     */
    public String getCodPostal() {
        return codPostal;
    }

    /**
     * @param codPostal the codPostal to set
     */
    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

}
