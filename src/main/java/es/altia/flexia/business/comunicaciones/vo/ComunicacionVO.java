/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.business.comunicaciones.vo;

import java.util.Calendar;
import java.util.List;

/** Clase que encapsula los datos de las comunicaciones de Flexia
 *
 * @author Ricardo
 */
public class ComunicacionVO implements java.io.Serializable{
    
    
    private Long id;
    private String asunto;
    private String texto;
    private Integer tipoDocumento;
    private String documento;
    private Calendar fecha;
    private String fechaAsString;
    private String nombre;
    private String numeroRegistro;
    private String origenRegistro;
    private String firma;
    private String numeroExpediente;
    private String xmlComunicacion;
    private Integer ejercicio;
    private List<AdjuntoComunicacionVO> listaAdjuntos;
    private Boolean leida;

    /** Getter del XML generado de la comunicacion
     * 
     * @return XML de la comunicacion
     */
    public String getXmlComunicacion() {
        return xmlComunicacion;
    }

      /** Setter del XML de la comunicacion
     *  
     * @param asunto XML de la comunicacion
     */
    public void setXmlComunicacion(String xmlComunicacion) {
        this.xmlComunicacion = xmlComunicacion;
    }

    
    
    /** Getter de asunto
     * 
     * @return Asunto de la comunicacion
     */
    public String getAsunto() {
        return asunto;
    }

    /** Setter del asunto
     *  
     * @param asunto Asunto de la comunicacion
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /** Getter del documento
     * 
     * @return N�mero de documento del usuario de la comunicacion
     */
    public String getDocumento() {
        return documento;
    }

    /** Setter del numero de documento
     * 
     * @param documento N�mero de documento del usuario de la comunicacion
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /** Getter del ejercicio
     * 
     * @return Ejercicio de la comunicacion
     */
    public Integer getEjercicio() {
        return ejercicio;
    }

    /** Setter del ejercicio
     * 
     * @param ejercicio Ejercicio de la comunicacion
     */
    public void setEjercicio(Integer ejercicio) {
        this.ejercicio = ejercicio;
    }

    /** Getter de la fecha 
     * 
     * @return Fecha de la comunicacion
     */
    public Calendar getFecha() {
        return fecha;
    }

    /** Setter de la fecha
     *  
     * @param fecha Fecha de la comunicaci�n
     */
    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    /** Getter de la firma
     * 
     * @return Firma de la comunicaci�n
     */
    public String getFirma() {
        return firma;
    }

    /** Stter de la firma
     * 
     * @param firma Firma de la comunicaci�n
     */
    public void setFirma(String firma) {
        this.firma = firma;
    }

    /** Getter del id
     * 
     * @return Identificador �nico de la comunicacion
     */
    public Long getId() {
        return id;
    }

    /** Setter del identificador
     * 
     * @param id Identificador �nico de la comunicacion
     */
    public void setId(Long id) {
        this.id = id;
    }

    /** Getter de la lista de adjuntos
     * 
     * @return Lista de adjuntos de la comunicaci�n
     */
    public List<AdjuntoComunicacionVO> getListaAdjuntos() {
        return listaAdjuntos;
    }

    /**setter de la lista de adjuntos
     * 
     * @param listaAdjuntos lista de adjuntos de la comunicaci�n
     */
    public void setListaAdjuntos(List<AdjuntoComunicacionVO> listaAdjuntos) {
        this.listaAdjuntos = listaAdjuntos;
    }

    /** Getter del nombre
     * 
     * @return Nombre del usuario que crea la comunicaci�n
     */
    public String getNombre() {
        return nombre;
    }

    /**setter del nombre
     * 
     * @param nombre Nombre del usuario que crea la comunicaci�n
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**Getter del numero de expediente
     * 
     * @return N�mero de expediente asociado a la comunicaci�n
     */
    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    /**Setter del numero de expediente
     * 
     * @param numeroExpediente N�mero de expediente asociado a la comunicaci�n
     */
    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    /** Getter del n�mero de registro asociado a la comunicaci�n
     * 
     * @return N�mero de registro asociado a la comunicaci�n
     */
    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    /** Setter del numero de registro
     * 
     * @param numeroRegistro  N�mero de registro asociado a la comunicaci�n
     */
    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    /** Getter del origen del registro
     * 
     * @return Origen del registro
     */
    public String getOrigenRegistro() {
        return origenRegistro;
    }

    /** Setter del origen del registro
     * 
     * @param origenRegistro  Origen de registro asociado a la comunicaci�n
     */
    public void setOrigenRegistro(String origenRegistro) {
        this.origenRegistro = origenRegistro;
    }

    /** Getter del texto de la comunicaci�n
     * 
     * @return Texto de la comunicaci�n
     */
    public String getTexto() {
        return texto;
    }

    /** Setter del texto de la comunicaci�n
     * 
     * @param texto Texto de la comunicaci�n
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /** Getter del tipo de documento identificativo del usuario
     * 
     * @return Tipo de documento identificativo del usuario
     */
    public Integer getTipoDocumento() {
        return tipoDocumento;
    }

    /**Setter del tipo de documento identificativo del usuario
     * 
     * @param tipoDocumento Tipo de documento identificativo del usuario
     */
    public void setTipoDocumento(Integer tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * 
     */
    public ComunicacionVO() {
    }

     /**
      * 
      * @return
      */
     public Boolean getLeida() {
        return leida;
    }

    /** Getter del estado de lectura
     * 
     * @param leida Si la comunicaci�n ha sido leida (1) o no (0)
     */
    public void setLeida(Boolean leida) {
        this.leida = leida;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ComunicacionVO other = (ComunicacionVO) obj;
        if ((this.asunto == null) ? (other.asunto != null) : !this.asunto.equals(other.asunto)) {
            return false;
        }
        if ((this.texto == null) ? (other.texto != null) : !this.texto.equals(other.texto)) {
            return false;
        }
        if (this.tipoDocumento != other.tipoDocumento && (this.tipoDocumento == null || !this.tipoDocumento.equals(other.tipoDocumento))) {
            return false;
        }
        if ((this.documento == null) ? (other.documento != null) : !this.documento.equals(other.documento)) {
            return false;
        }
        if (this.fecha != other.fecha && (this.fecha == null || !this.fecha.equals(other.fecha))) {
            return false;
        }
        if ((this.nombre == null) ? (other.nombre != null) : !this.nombre.equals(other.nombre)) {
            return false;
        }
        if ((this.numeroRegistro == null) ? (other.numeroRegistro != null) : !this.numeroRegistro.equals(other.numeroRegistro)) {
            return false;
        }
        if ((this.origenRegistro == null) ? (other.origenRegistro != null) : !this.origenRegistro.equals(other.origenRegistro)) {
            return false;
        }
        if ((this.firma == null) ? (other.firma != null) : !this.firma.equals(other.firma)) {
            return false;
        }
        if ((this.numeroExpediente == null) ? (other.numeroExpediente != null) : !this.numeroExpediente.equals(other.numeroExpediente)) {
            return false;
        }
        if (this.ejercicio != other.ejercicio && (this.ejercicio == null || !this.ejercicio.equals(other.ejercicio))) {
            return false;
        }
        if (this.listaAdjuntos != other.listaAdjuntos && (this.listaAdjuntos == null || !this.listaAdjuntos.equals(other.listaAdjuntos))) {
            return false;
        }
        if (this.leida != other.leida && (this.leida == null || !this.leida.equals(other.leida))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.asunto != null ? this.asunto.hashCode() : 0);
        hash = 47 * hash + (this.texto != null ? this.texto.hashCode() : 0);
        hash = 47 * hash + (this.tipoDocumento != null ? this.tipoDocumento.hashCode() : 0);
        hash = 47 * hash + (this.documento != null ? this.documento.hashCode() : 0);
        hash = 47 * hash + (this.fecha != null ? this.fecha.hashCode() : 0);
        hash = 47 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        hash = 47 * hash + (this.numeroRegistro != null ? this.numeroRegistro.hashCode() : 0);
        hash = 47 * hash + (this.origenRegistro != null ? this.origenRegistro.hashCode() : 0);
        hash = 47 * hash + (this.firma != null ? this.firma.hashCode() : 0);
        hash = 47 * hash + (this.numeroExpediente != null ? this.numeroExpediente.hashCode() : 0);
        hash = 47 * hash + (this.ejercicio != null ? this.ejercicio.hashCode() : 0);
        hash = 47 * hash + (this.listaAdjuntos != null ? this.listaAdjuntos.hashCode() : 0);
        hash = 47 * hash + (this.leida != null ? this.leida.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ComunicacionVO{" + "id=" + id + ", asunto=" + asunto + ", texto=" + texto + ", tipoDocumento=" + tipoDocumento + ", documento=" + documento + ", fecha=" + fecha + ", nombre=" + nombre + ", numeroRegistro=" + numeroRegistro + ", origenRegistro=" + origenRegistro + ", firma=" + firma + ", numeroExpediente=" + numeroExpediente + ", ejercicio=" + ejercicio + ", listaAdjuntos=" + listaAdjuntos + ", leida=" + leida + '}';
    }

    /** Constructor
     * 
     * @param id Identificador �nico de la comunicaci�n
     * @param asunto asunto de la comunicaci�n
     * @param texto texto de la comunicaci�n
     * @param tipoDocumento tipo de documento identificativo del usuario
     * @param documento n�mero de documento identificativo del usuario
     * @param fecha Fecha de creaci�n
     * @param nombre Nombre del usuario
     * @param numeroRegistro n�mero de registro telematico
     * @param origenRegistro origen del registro telem�tico
     * @param firma Firma de la comunicaci�n
     * @param numeroExpediente N�mero de expediente relacionado con  la comunicaci�n
     * @param ejercicio ejercicio de la comunicaci�n
     * @param listaAdjuntos lista de adjuntos de la comunicaci�n
     * @param leida indicador de si ha sido leida previamente
     */
    public ComunicacionVO(Long id, String asunto, String texto, Integer tipoDocumento, String documento, Calendar fecha, String nombre, String numeroRegistro, String origenRegistro, String firma, String numeroExpediente, Integer ejercicio, List<AdjuntoComunicacionVO> listaAdjuntos, Boolean leida) {
        this.id = id;
        this.asunto = asunto;
        this.texto = texto;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.fecha = fecha;
        this.nombre = nombre;
        this.numeroRegistro = numeroRegistro;
        this.origenRegistro = origenRegistro;
        this.firma = firma;
        this.numeroExpediente = numeroExpediente;
        this.ejercicio = ejercicio;
        this.listaAdjuntos = listaAdjuntos;
        this.leida = leida;
    }

    /**
     * @return the fechaAsString
     */
    public String getFechaAsString() {
        return fechaAsString;
    }

    /**
     * @param fechaAsString the fechaAsString to set
     */
    public void setFechaAsString(String fechaAsString) {
        this.fechaAsString = fechaAsString;
    }
    
    
    
}
