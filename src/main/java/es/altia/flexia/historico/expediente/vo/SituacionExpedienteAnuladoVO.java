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
 * Tabla E_EXPSIT
 */
public class SituacionExpedienteAnuladoVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO.
    private String numExpediente = null; //Se corresponde con el campo EXPSIT_NUM
    private String justificacion = null; //Se corresponde con el campo EXPSIT_JUST
    private String usuario = null; //Se corresponde con el campo EXPSIT_USUARIO
    private String autoriza = null; //Se corresponde con el campo EXPSIT_AUTORIZA
    private int codMunicipio ; //Se corresponde con el campo EXPSIT_MUN
    private int ejercicio ; //Se corresponde con el campo EXPSIT_EJE
    
    public SituacionExpedienteAnuladoVO()
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

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getAutoriza() {
        return autoriza;
    }

    public void setAutoriza(String autoriza) {
        this.autoriza = autoriza;
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
}
