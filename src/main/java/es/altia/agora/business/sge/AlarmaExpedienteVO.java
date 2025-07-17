/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.business.sge;

import java.util.Calendar;

/**
 *
 * @author altia
 */
public class AlarmaExpedienteVO {
    private int codOrganizacion;
    private int ejercicio;
    private String numExpediente;
    private int codTramite;
    private int ocurrenciaTramite;
    private String codigoDato;
    private Calendar fechaVencimiento;

    public AlarmaExpedienteVO ( ) {
    }
    
    public AlarmaExpedienteVO (int codOrganizacion, int ejercicio, String numExpediente, int codTramite,
            int ocurrenciaTramite, String codigoDato, Calendar fechaVencimiento){
        this.codOrganizacion = codOrganizacion;
        this.ejercicio = ejercicio;
        this.numExpediente = numExpediente;
        this.codTramite = codTramite;
        this.ocurrenciaTramite = ocurrenciaTramite;
        this.codigoDato = codigoDato;
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
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

    public int getCodTramite() {
        return codTramite;
    }

    public void setCodTramite(int codTramite) {
        this.codTramite = codTramite;
    }

    public int getOcurrenciaTramite() {
        return ocurrenciaTramite;
    }

    public void setOcurrenciaTramite(int ocurrenciaTramite) {
        this.ocurrenciaTramite = ocurrenciaTramite;
    }

    public String getCodigoDato() {
        return codigoDato;
    }

    public void setCodigoDato(String codigoDato) {
        this.codigoDato = codigoDato;
    }

    public Calendar getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Calendar fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}
