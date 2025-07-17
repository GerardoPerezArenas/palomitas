/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.business.sge.notificacion;

import java.util.Calendar;

/**
 *
 * @author mjesus.lopez
 *
 * Contiene toda la información necesaria para poder determinar
 * que trámites necesitan enviar una notificación para avisar
 * de que se encuentran cercanos al fin de plazo o que ya
 * han sobrepasado el plazo definido para su tramitación
 */
public class NotificacionPlazoTramiteCVO {
    private String numeroExpediente;
    private int codOrganizacion;
    private String codProcedimiento;
    private int codTramite;
    private int ocurrencia;
    private int utr;
    private Calendar fechaInicio;
    private Calendar fechaLimite;
    private boolean avisadoCercaFinPlazo;
    private boolean avisadoFueraDePlazo;
    private boolean notificarCercaFinPlazo;
    private boolean notificarFueraDePlazo;
    private int tipoNotCercaFinPlazo;
    private int tipoNotFueraDePlazo;
    private int porcentajeCercaFinPlazo;//porcentaje de tiempo de tramitacion a partir del cual se empieza a avisar 
    private String nombreTramite;
    private String emailUtr;
    private int ejercicio;
    private String emailUsuarioTramite;	
    private String emailUsuarioExpediente;	
    private String notificarUsuTraFinPlazo;	
    private String notificarUsuExpFinPlazo;	
    private String notificarUORFinPlazo;

    public boolean isAvisadoCercaFinPlazo() {
        return avisadoCercaFinPlazo;
    }

    public void setAvisadoCercaFinPlazo(boolean avisadoCercaFinPlazo) {
        this.avisadoCercaFinPlazo = avisadoCercaFinPlazo;
    }

    public boolean isAvisadoFueraDePlazo() {
        return avisadoFueraDePlazo;
    }

    public void setAvisadoFueraDePlazo(boolean avisadoFueraDePlazo) {
        this.avisadoFueraDePlazo = avisadoFueraDePlazo;
    }

    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public int getCodTramite() {
        return codTramite;
    }

    public void setCodTramite(int codTramite) {
        this.codTramite = codTramite;
    }

    public int getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getEmailUtr() {
        return emailUtr;
    }

    public void setEmailUtr(String emailUtr) {
        this.emailUtr = emailUtr;
    }

    public Calendar getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Calendar getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Calendar fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getNombreTramite() {
        return nombreTramite;
    }

    public void setNombreTramite(String nombreTramite) {
        this.nombreTramite = nombreTramite;
    }

    public boolean getNotificarCercaFinPlazo() {
        return notificarCercaFinPlazo;
    }

    public void setNotificarCercaFinPlazo(boolean notificarCercaFinPlazo) {
        this.notificarCercaFinPlazo = notificarCercaFinPlazo;
    }

    public boolean getNotificarFueraDePlazo() {
        return notificarFueraDePlazo;
    }

    public void setNotificarFueraDePlazo(boolean notificarFueraDePlazo) {
        this.notificarFueraDePlazo = notificarFueraDePlazo;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public int getOcurrencia() {
        return ocurrencia;
    }

    public void setOcurrencia(int ocurrencia) {
        this.ocurrencia = ocurrencia;
    }

    public int getPorcentajeCercaFinPlazo() {
        return porcentajeCercaFinPlazo;
    }

    public void setPorcentajeCercaFinPlazo(int porcentajeCercaFinPlazo) {
        this.porcentajeCercaFinPlazo = porcentajeCercaFinPlazo;
    }

    public int getTipoNotCercaFinPlazo() {
        return tipoNotCercaFinPlazo;
    }

    public void setTipoNotCercaFinPlazo(int tipoNotCercaFinPlazo) {
        this.tipoNotCercaFinPlazo = tipoNotCercaFinPlazo;
    }

    public int getTipoNotFueraDePlazo() {
        return tipoNotFueraDePlazo;
    }

    public void setTipoNotFueraDePlazo(int tipoNotFueraDePlazo) {
        this.tipoNotFueraDePlazo = tipoNotFueraDePlazo;
    }

    public int getUtr() {
        return utr;
    }

    public void setUtr(int utr) {
        this.utr = utr;
    }

    public String getEmailUsuarioTramite() {
        return emailUsuarioTramite;
    }

    public void setEmailUsuarioTramite(String emailUsuarioTramite) {
        this.emailUsuarioTramite = emailUsuarioTramite;
    }

    public String getEmailUsuarioExpediente() {
        return emailUsuarioExpediente;
    }

    public void setEmailUsuarioExpediente(String emailUsuarioExpediente) {
        this.emailUsuarioExpediente = emailUsuarioExpediente;
    }

    public String getNotificarUsuTraFinPlazo() {
        return notificarUsuTraFinPlazo;
    }

    public void setNotificarUsuTraFinPlazo(String notificarUsuTraFinPlazo) {
        this.notificarUsuTraFinPlazo = notificarUsuTraFinPlazo;
    }

    public String getNotificarUsuExpFinPlazo() {
        return notificarUsuExpFinPlazo;
    }

    public void setNotificarUsuExpFinPlazo(String notificarUsuExpFinPlazo) {
        this.notificarUsuExpFinPlazo = notificarUsuExpFinPlazo;
    }

    public String getNotificarUORFinPlazo() {
        return notificarUORFinPlazo;
    }

    public void setNotificarUORFinPlazo(String notificarUORFinPlazo) {
        this.notificarUORFinPlazo = notificarUORFinPlazo;
    }
    
    

  public String toString() {

        String str = null;

            str = "| numeroExpediente = " + numeroExpediente + " |\n" +
				"| codOrganizacion = " + codOrganizacion + " |\n" +
                "| codProcedimiento = " + codProcedimiento + " |\n" +
                "| codTramite = " + codTramite + " |\n" +
                "| ocurrencia = " + ocurrencia + " |\n" +
                "| utr = " + utr + " |\n" +
                "| fechaInicio = " + fechaInicio + " |\n" +
                "| fechaLimite = " + fechaLimite + " |\n" +
                "| avisadoCercaFinPlazo = " + avisadoCercaFinPlazo + " |\n" +
                "| avisadoFueraDePlazo = " + avisadoFueraDePlazo + " |\n" +
                "| notificarCercaFinPlazo = " + notificarCercaFinPlazo + " |\n" +
                "| notificarFueraDePlazo = " + notificarFueraDePlazo + " |\n" +
                "| tipoNotCercaFinPlazo = " + tipoNotCercaFinPlazo + " |\n" +
                "| tipoNotFueraDePlazo = " + tipoNotFueraDePlazo + " |\n" +
                "| porcentajeCercaFinPlazo = " + porcentajeCercaFinPlazo + "|\n"+
                "| nombreTramite = " + nombreTramite + "|\n"+
                "| emailUtr = " + emailUtr + "|\n"+
                "| ejercicio = " + ejercicio + "|\n";

        return str;
    }


}
