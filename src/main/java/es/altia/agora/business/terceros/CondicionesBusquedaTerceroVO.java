package es.altia.agora.business.terceros;

import java.io.Serializable;

/** Esta clase representa los posibles parámetros de busqueda que pueden ser utilizados para buscar
 * un tercero mediante la nueva implementación de la busqueda de terceros.
 */
public class CondicionesBusquedaTerceroVO implements Serializable {

    private int tipoDocumento;
    private String documento;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String telefono;
    private String email;
    private int codigoPais;
    private int codigoProvincia;
    private int codigoMunicipio;
    private int codigoVia;
    private String nombreVia;
    private int numeroDesde;
    private String letraDesde;
    private int numeroHasta;
    private String letraHasta;
    private String bloque;
    private String portal;
    private String escalera;
    private String planta;
    private String puerta;
    private String codPostal;
    private String domicilio;
    private String lugar;
    private int codigoEsi;
    private int codigoEco;
    private int codOrganizacion;
    private int idUsuario;
    private int codEnt;

    // Constructor por defecto.
    public CondicionesBusquedaTerceroVO() {
        this.tipoDocumento = -1;
        this.documento = null;
        this.nombre = null;
        this.apellido1 = null;
        this.apellido2 = null;
        this.telefono = null;
        this.email = null;
        this.codigoPais = -1;
        this.codigoProvincia = -1;
        this.codigoMunicipio = -1;
        this.codigoVia = -1;
        this.nombreVia = null;
        this.numeroDesde = -1;
        this.letraDesde = null;
        this.numeroHasta = -1;
        this.letraHasta = null;
        this.bloque = null;
        this.portal = null;
        this.escalera = null;
        this.planta = null;
        this.puerta = null;
        this.codPostal = null;
        this.domicilio = null;
        this.lugar = null;
        this.codigoEsi = -1;
        this.codigoEco = -1;
        this.codOrganizacion = -1;
        this.idUsuario = -1;
        this.codEnt = -1;
    }


    public CondicionesBusquedaTerceroVO(int tipoDocumento, String documento, String nombre, String apellido1,
                                        String apellido2, int codigoPais, int codigoProvincia, int codigoMunicipio,
                                        int codigoVia, String nombreVia, int numeroDesde, String letraDesde,
                                        int numeroHasta, String letraHasta, int codigoEsi, int codigoEco,
                                        int codOrganizacion) {

        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.codigoPais = codigoPais;
        this.codigoProvincia = codigoProvincia;
        this.codigoMunicipio = codigoMunicipio;
        this.codigoVia = codigoVia;
        this.nombreVia = nombreVia;
        this.numeroDesde = numeroDesde;
        this.letraDesde = letraDesde;
        this.numeroHasta = numeroHasta;
        this.letraHasta = letraHasta;
        this.codigoEsi = codigoEsi;
        this.codigoEco = codigoEco;
        this.codOrganizacion = codOrganizacion;
    }

    // Metodos Getters y Setters.
    public int getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(int tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public int getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(int codigoPais) {
        this.codigoPais = codigoPais;
    }

    public int getCodigoProvincia() {
        return codigoProvincia;
    }

    public void setCodigoProvincia(int codigoProvincia) {
        this.codigoProvincia = codigoProvincia;
    }

    public int getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(int codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public int getCodigoVia() {
        return codigoVia;
    }

    public void setCodigoVia(int codigoVia) {
        this.codigoVia = codigoVia;
    }

    public String getNombreVia() {
        return nombreVia;
    }

    public void setNombreVia(String nombreVia) {
        this.nombreVia = nombreVia;
    }

    public int getNumeroDesde() {
        return numeroDesde;
    }

    public void setNumeroDesde(int numeroDesde) {
        this.numeroDesde = numeroDesde;
    }

    public String getLetraDesde() {
        return letraDesde;
    }

    public void setLetraDesde(String letraDesde) {
        this.letraDesde = letraDesde;
    }

    public int getNumeroHasta() {
        return numeroHasta;
    }

    public void setNumeroHasta(int numeroHasta) {
        this.numeroHasta = numeroHasta;
    }

    public String getLetraHasta() {
        return letraHasta;
    }

    public void setLetraHasta(String letraHasta) {
        this.letraHasta = letraHasta;
    }

    public int getCodigoEsi() {
        return codigoEsi;
    }

    public void setCodigoEsi(int codigoEsi) {
        this.codigoEsi = codigoEsi;
    }

    public int getCodigoEco() {
        return codigoEco;
    }

    public void setCodigoEco(int codigoEco) {
        this.codigoEco = codigoEco;
    }

    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public String getBloque() {
        return bloque;
    }

    public void setBloque(String bloque) {
        this.bloque = bloque;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getEscalera() {
        return escalera;
    }

    public void setEscalera(String escalera) {
        this.escalera = escalera;
    }

    public String getPlanta() {
        return planta;
    }

    public void setPlanta(String planta) {
        this.planta = planta;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getPortal() {
        return portal;
    }

    public void setPortal(String portal) {
        this.portal = portal;
    }

    public String getPuerta() {
        return puerta;
    }

    public void setPuerta(String puerta) {
        this.puerta = puerta;
    }

    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getCodEnt() {
        return codEnt;
    }

    public void setCodEnt(int codEnt) {
        this.codEnt = codEnt;
    }

      
    
    
}
