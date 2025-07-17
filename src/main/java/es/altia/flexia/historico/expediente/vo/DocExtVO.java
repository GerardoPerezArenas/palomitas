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
 * Tabla E_DOC_EXT
 */
public class DocExtVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO
    private String numExpediente = null; //Se corresponde con el campo DOC_EXT_NUM
    private int codMunicipio; //Se corresponde con el campo DOC_EXT_MUN
    private int ejercicio; //Se corresponde con el campo DOC_EXT_EJE
    private int codDocumento; //Se corresponde con el campo DOC_EXT_COD
    private String nombreDocumento = null; //Se corresponde con el campo DOC_EXT_NOM
    private Calendar fechaDocumento = null; //Se corresponde con el campo DOC_EXT_FAL
    private byte[] contenido = null; //Se corresponde con el campo DOC_EXT_FIL
    private String tipoDocumento = null; //Se corresponde con el campo DOC_EXT_TIP
    private String extension = null; //Se corresponde con el campo DOC_EXT_EXT
    private String origen = null; //Se corresponde con el campo DOC_EXT_ORIGEN
    
    public DocExtVO()
    {
        
    }

    public long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(long idProceso) {
        this.idProceso = idProceso;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public int getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(int codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public int getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    public int getCodDocumento() {
        return codDocumento;
    }

    public void setCodDocumento(int codDocumento) {
        this.codDocumento = codDocumento;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public Calendar getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(Calendar fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }
}
