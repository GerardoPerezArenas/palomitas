package es.altia.agora.business.sge;

import java.io.Serializable;

public class FicheroVO implements Serializable{    
    
    private String nombre;
    private String codigo;
    private String codigoDocumentoPresentado;
    private String tipoContenido;
    // Atributos para los ficheros del registro
    private String dep;
    private String uor;
    private String ejercicio;
    private String numero;
    private String tipo;
    private String nombreAsiento = null;
    private String fechaAsiento = null;
    // Atributos para los ficheros de los trámites.
    private String municipio;
    private String expediente;
    private String tramite;
    private String ocurrencia;
    private String codigoFicheroTramite;
    private String nombreTramite = null;
    // Atributo para indicar en la jsp que se trata de un tramite nuevo
    private String nuevoTramite = "false";
    private String nuevoRegistro = "false";
    private String tipoDocumental;

    // Atributos para los ficheros documentos aportados anterior
    private String fecha;
    private String organo;
    
    
    public String getFechaAsiento() {
        return fechaAsiento;
    }

    public void setFechaAsiento(String fechaAsiento) {
        this.fechaAsiento = fechaAsiento;
    }
    
    
    public String getTipoDocumental() {
        return tipoDocumental;
    }

    public void setTipoDocumental(String tipoDocumental) {
        this.tipoDocumental = tipoDocumental;
    }
    
    
    public String getNuevoRegistro() {
        return nuevoRegistro;
    }

    public void setNuevoRegistro(String nuevoRegistro) {
        this.nuevoRegistro = nuevoRegistro;
    }
    
    
    
    public String getNuevoTramite() {
        return nuevoTramite;
    }
    public void setNuevoTramite(String nuevoTramite) {
        this.nuevoTramite = nuevoTramite;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getCodigoFicheroTramite() {
        return codigoFicheroTramite;
    }
    public void setCodigoFicheroTramite(String codigoFicheroTramite) {
        this.codigoFicheroTramite = codigoFicheroTramite;
    }
    public String getDep() {
        return dep;
    }
    public void setDep(String dep) {
        this.dep = dep;
    }
    public String getUor() {
        return uor;
    }
    public void setUor(String uor) {
        this.uor = uor;
    }
    public String getEjercicio() {
        return ejercicio;
    }
    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getMunicipio() {
        return municipio;
    }
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
    public String getExpediente() {
        return expediente;
    }
    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }
    public String getTramite() {
        return tramite;
    }
    public void setTramite(String tramite) {
        this.tramite = tramite;
    }
    public String getOcurrencia() {
        return ocurrencia;
    }
    public void setOcurrencia(String ocurrencia) {
        this.ocurrencia = ocurrencia;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getNombreTramite() {
        return nombreTramite;
    }
    public void setNombreTramite(String nombreTramite) {
        this.nombreTramite = nombreTramite;
    }
    public String getNombreAsiento() {
        return nombreAsiento;
    }
    public void setNombreAsiento(String nombreAsiento) {
        this.nombreAsiento = nombreAsiento;
    }
    public String getTipoContenido() {
        return tipoContenido;
    }
    public void setTipoContenido(String tipoContenido) {
        this.tipoContenido = tipoContenido;
    }

    /**
     * @return the codigoDocumentoPresentado
     */
    public String getCodigoDocumentoPresentado() {
        return codigoDocumentoPresentado;
    }

    /**
     * @param codigoDocumentoPresentado the codigoDocumentoPresentado to set
     */
    public void setCodigoDocumentoPresentado(String codigoDocumentoPresentado) {
        this.codigoDocumentoPresentado = codigoDocumentoPresentado;
    }
    
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getOrgano() {
        return organo;
    }

    public void setOrgano(String organo) {
        this.organo = organo;
    }
    
}
