/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.business.comunicaciones.vo;

import java.util.Calendar;

/** Clase que encapsula los datos relativos a los adjuntos de comunicaciones
 *
 * @author Ricardo
 */
public class AdjuntoComunicacionVO {
    
    private Long id;
    private Long idComunicacion;
    private String nombre;
    private String tipoMime;
    private String contenido;
    private Calendar fecha;
    private String firma;
    private String plataformaFirma;

    /** Getter del contenido del fichero adjunto
     * 
     * @return Contenido del fichero adjunto
     */
    public String getContenido() {
        return contenido;
    }

    /** Setter del contenido del fichero adjunto
     * 
     * @param contenido Contenido del fichero adjunto
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /** Getter de la fecha de la comunicación
     * 
     * @return Fecha
     */
    public Calendar getFecha() {
        return fecha;
    }

    /** Setter de la fecha
     *  
     * @param fecha Fecha
     */
    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    /** Getter de la firma del adjunto
     * 
     * @return Firma del adjunto
     */
    public String getFirma() {
        return firma;
    }

    /** Setter de la firma del adjunto
     * 
     * @param firma Firma del adjunto
     */
    public void setFirma(String firma) {
        this.firma = firma;
    }

    /** Getter del identificador único del adjunto
     * 
     * @return Identificador único del adjunto
     */
    public Long getId() {
        return id;
    }

    /** Setter del identificador único del adjunto
     * 
     * @param id Identificador único del adjunto
     */
    public void setId(Long id) {
        this.id = id;
    }

    /** Getter del identificador único de la comunicación asociada
     * 
     * @return Identificador único de la comunicación asociada
     */
    public Long getIdComunicacion() {
        return idComunicacion;
    }

    /** Setter del identificador de la comunicación
     * 
     * @param idComunicacion Identificador único de la comunicación asociada
     */
    public void setIdComunicacion(Long idComunicacion) {
        this.idComunicacion = idComunicacion;
    }

    /** Nombre del usuario
     * 
     * @return Nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /** Setter del nombre
     * 
     * @param nombre Nombre del usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** Getter de la plataforma de firma
     * 
     * @return Plataforma de la firma
     */
    public String getPlataformaFirma() {
        return plataformaFirma;
    }

    /** Setter de la plataforma de firma
     * 
     * @param plataformaFirma Plataforma de firma
     */
    public void setPlataformaFirma(String plataformaFirma) {
        this.plataformaFirma = plataformaFirma;
    }

    /** Getter del tipo de adjunto
     * 
     * @return Tipo mime del adjunto
     */
    public String getTipoMime() {
        return tipoMime;
    }

    /** Setter del tipo mime del adjunto
     * 
     * @param tipoMime Tipo mime del adjunto
     */
    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    @Override
    public String toString() {
        return "AdjuntoComunicacionVO{" + "id=" + id + ", idComunicacion=" + idComunicacion + ", nombre=" + nombre + ", tipoMime=" + tipoMime + ", contenido=" + contenido + ", fecha=" + fecha + ", firma=" + firma + ", plataformaFirma=" + plataformaFirma + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AdjuntoComunicacionVO other = (AdjuntoComunicacionVO) obj;
        if (this.idComunicacion != other.idComunicacion && (this.idComunicacion == null || !this.idComunicacion.equals(other.idComunicacion))) {
            return false;
        }
        if ((this.nombre == null) ? (other.nombre != null) : !this.nombre.equals(other.nombre)) {
            return false;
        }
        if ((this.tipoMime == null) ? (other.tipoMime != null) : !this.tipoMime.equals(other.tipoMime)) {
            return false;
        }
        if ((this.contenido == null) ? (other.contenido != null) : !this.contenido.equals(other.contenido)) {
            return false;
        }
        if (this.fecha != other.fecha && (this.fecha == null || !this.fecha.equals(other.fecha))) {
            return false;
        }
        if ((this.firma == null) ? (other.firma != null) : !this.firma.equals(other.firma)) {
            return false;
        }
        if ((this.plataformaFirma == null) ? (other.plataformaFirma != null) : !this.plataformaFirma.equals(other.plataformaFirma)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 29 * hash + (this.idComunicacion != null ? this.idComunicacion.hashCode() : 0);
        hash = 29 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        hash = 29 * hash + (this.tipoMime != null ? this.tipoMime.hashCode() : 0);
        hash = 29 * hash + (this.contenido != null ? this.contenido.hashCode() : 0);
        hash = 29 * hash + (this.fecha != null ? this.fecha.hashCode() : 0);
        hash = 29 * hash + (this.firma != null ? this.firma.hashCode() : 0);
        hash = 29 * hash + (this.plataformaFirma != null ? this.plataformaFirma.hashCode() : 0);
        return hash;
    }

    /** Constructor
     * 
     * @param id Identificador único del adjunto
     * @param idComunicacion Identificador único de la comunicación
     * @param nombre Nombre del usuario
     * @param tipoMime Tipo mime del adjunto
     * @param contenido Contenido del fichero
     * @param fecha Fecha de creación
     * @param firma Firma del adjunto
     * @param plataformaFirma Plataforma de firma utilizada
     */
    public AdjuntoComunicacionVO(Long id, Long idComunicacion, String nombre, String tipoMime, String contenido, Calendar fecha, String firma, String plataformaFirma) {
        this.id = id;
        this.idComunicacion = idComunicacion;
        this.nombre = nombre;
        this.tipoMime = tipoMime;
        this.contenido = contenido;
        this.fecha = fecha;
        this.firma = firma;
        this.plataformaFirma = plataformaFirma;
    }

    /**
     * 
     */
    public AdjuntoComunicacionVO() {
    }
    
    
}
