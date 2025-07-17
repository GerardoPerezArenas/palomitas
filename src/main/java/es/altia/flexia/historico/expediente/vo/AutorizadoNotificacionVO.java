/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.historico.expediente.vo;

/**
 *
 * @author santiagoc
 * 
 * Tabla AUTORIZADO_NOTIFICACION
 */
public class AutorizadoNotificacionVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO.
    private int codNotificacion;  //Se corresponde con el campo CODIGO_NOTIFICACION
    private int codMunicipio;  //Se corresponde con el campo COD_MUNICIPIO
    private int ejercicio;  //Se corresponde con el campo EJERCICIO
    private String numExpediente = null;  //Se corresponde con el campo NUM_EXPEDIENTE
    private int codTercero;  //Se corresponde con el campo COD_TERCERO
    private int versionTercero;  //Se corresponde con el campo VER_TERCERO
    
    public AutorizadoNotificacionVO()
    {
        
    }

    public long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(long idProceso) {
        this.idProceso = idProceso;
    }

    public int getCodNotificacion() {
        return codNotificacion;
    }

    public void setCodNotificacion(int codNotificacion) {
        this.codNotificacion = codNotificacion;
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

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public int getCodTercero() {
        return codTercero;
    }

    public void setCodTercero(int codTercero) {
        this.codTercero = codTercero;
    }

    public int getVersionTercero() {
        return versionTercero;
    }

    public void setVersionTercero(int versionTercero) {
        this.versionTercero = versionTercero;
    }
}
