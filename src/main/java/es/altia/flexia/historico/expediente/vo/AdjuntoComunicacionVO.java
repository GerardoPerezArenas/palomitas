/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.historico.expediente.vo;

import java.util.Calendar;

/**
 *
 * @author santiagoc
 * 
 * Tabla ADJUNTO_COMUNICACION
 */
public class AdjuntoComunicacionVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO.
    private int id; //Se corresponde con el campo ID
    private int idComunicacion; //Se corresponde con el campo ID_COMUNICACION
    private String nombre = null; //Se corresponde con el campo NOMBRE
    private String tipoMime = null; //Se corresponde con el campo TIPO_MIME
    private Calendar fecha = null; //Se corresponde con el campo FECHA
    private byte[] contenido = null; //Se corresponde con el campo CONTENIDO
    private String firma = null; //Se corresponde con el campo FIRMA
    private String plataformaFirma = null; //Se corresponde con el campo PLATAFORMA_FIRMA
    
    public AdjuntoComunicacionVO()
    {
        
    }

    public long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(long idProceso) {
        this.idProceso = idProceso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdComunicacion() {
        return idComunicacion;
    }

    public void setIdComunicacion(int idComunicacion) {
        this.idComunicacion = idComunicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getPlataformaFirma() {
        return plataformaFirma;
    }

    public void setPlataformaFirma(String plataformaFirma) {
        this.plataformaFirma = plataformaFirma;
    }
}
