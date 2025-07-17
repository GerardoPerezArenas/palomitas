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
 * Tabla E_EXT
 */
public class InteresadoExpedienteVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO.
    private int codMunicipio; //Se corresponde con el campo EXT_MUN
    private int ejercicio; //Se corresponde con el campo EXT_EJE
    private String numExpediente = null; //Se corresponde con el campo EXT_NUM
    private int codTercero; //Se corresponde con el campo EXT_TER
    private int versionTercero; //Se corresponde con el campo EXT_NVR
    private int codDomicilio; //Se corresponde con el campo EXT_DOT
    private int codRol; //Se corresponde con el campo EXT_ROL
    private String codProcedimiento = null; //Se corresponde con el campo EXT_PRO
    private int mostrar; //Se corresponde con el campo MOSTRAR
    private String notificacionElectronica = null; //Se corresponde con el campo EXT_NOTIFICACION_ELECTRONICA.
    
    public InteresadoExpedienteVO()
    {
        
    }

    public long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(long idProceso) {
        this.idProceso = idProceso;
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

    public int getCodDomicilio() {
        return codDomicilio;
    }

    public void setCodDomicilio(int codDomicilio) {
        this.codDomicilio = codDomicilio;
    }

    public int getCodRol() {
        return codRol;
    }

    public void setCodRol(int codRol) {
        this.codRol = codRol;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public int getMostrar() {
        return mostrar;
    }

    public void setMostrar(int mostrar) {
        this.mostrar = mostrar;
    }

    public String getNotificacionElectronica() {
        return notificacionElectronica;
    }

    public void setNotificacionElectronica(String notificacionElectronica) {
        this.notificacionElectronica = notificacionElectronica;
    }
}
