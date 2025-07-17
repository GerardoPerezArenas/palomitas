/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.sge;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author adrian.freixeiro
 */
public class OperacionExpedienteVO implements Serializable{
    
    
    private int idOperacion;
    private int codMunicipio;
    private int ejercicio;
    private String numExpediente;
    private int tipoOperacion;    
    private Calendar fechaOperacion;
    private int codUsuario;
    private String descripcionOperacion;
    private String tipoOperacionTxt;
    private String fechaOperacionTxt;
    
    public OperacionExpedienteVO() {
    }

    public OperacionExpedienteVO(int idOperacion,int codMunicipio,int ejercicio,String numExpediente,
            int tipoOperacion,Calendar fechaOperacion,int codUsuario,String descripcionOperacion){

        this.idOperacion = idOperacion;
        this.codMunicipio = codMunicipio;
        this.ejercicio = ejercicio;
        this.numExpediente = numExpediente;
        this.tipoOperacion = tipoOperacion;
        this.fechaOperacion = fechaOperacion;
        this.codUsuario = codUsuario;
        this.descripcionOperacion = descripcionOperacion;
    }

    /**
     * @return the idOperacion
     */
    public int getIdOperacion() {
        return idOperacion;
    }

    /**
     * @return the codMunicipio
     */
    public int getCodMunicipio() {
        return codMunicipio;
    }

    /**
     * @return the ejercicio
     */
    public int getEjercicio() {
        return ejercicio;
    }

    /**
     * @return the numExpediente
     */
    public String getNumExpediente() {
        return numExpediente;
    }

    /**
     * @return the tipoOperacion
     */
    public int getTipoOperacion() {
        return tipoOperacion;
    }

    /**
     * @return the fechaOperacion
     */
    public Calendar getFechaOperacion() {
        return fechaOperacion;
    }

    /**
     * @return the codUsuario
     */
    public int getCodUsuario() {
        return codUsuario;
    }

    /**
     * @return the descripcionOperacion
     */
    public String getDescripcionOperacion() {
        return descripcionOperacion;
    }

    /**
     * @param idOperacion the idOperacion to set
     */
    public void setIdOperacion(int idOperacion) {
        this.idOperacion = idOperacion;
    }

    /**
     * @param codMunicipio the codMunicipio to set
     */
    public void setCodMunicipio(int codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    /**
     * @param ejercicio the ejercicio to set
     */
    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    /**
     * @param numExpediente the numExpediente to set
     */
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    /**
     * @param tipoOperacion the tipoOperacion to set
     */
    public void setTipoOperacion(int tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    /**
     * @param fechaOperacion the fechaOperacion to set
     */
    public void setFechaOperacion(Calendar fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    /**
     * @param codUsuario the codUsuario to set
     */
    public void setCodUsuario(int codUsuario) {
        this.codUsuario = codUsuario;
    }

    /**
     * @param descripcionOperacion the descripcionOperacion to set
     */
    public void setDescripcionOperacion(String descripcionOperacion) {
        this.descripcionOperacion = descripcionOperacion;
    }
    
    /**
     * @return the fechaOperacionTxy
     */
    public String getFechaOperacionTxt() {
        return fechaOperacionTxt;
    }
    
     /**
     * @param fechaOperacion the fechaOperacionTxt to set
     */
    public void setFechaOperacionTxt(String fechaOperacionTxt) {
        this.fechaOperacionTxt = fechaOperacionTxt;
    }
    
    /**
     * @return the tipoOperacionTxt
     */
    public String getTipoOperacionTxt() {
        return tipoOperacionTxt;
    }
    
     /**
     * @param tipoOperacion the tipoOperacionTxt to set
     */
    public void setTipoOperacionTxt(String tipoOperacionTxt) {
        this.tipoOperacionTxt = tipoOperacionTxt;
    }
}
