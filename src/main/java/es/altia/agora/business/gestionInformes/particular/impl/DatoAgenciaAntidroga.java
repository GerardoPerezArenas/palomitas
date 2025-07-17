package es.altia.agora.business.gestionInformes.particular.impl;

import java.util.Calendar;

public class DatoAgenciaAntidroga {

    private String nombreUsuario;
    private String codigoRAD;
    private Calendar fechaCita;
    private int tipoIntervencion;
    private boolean acudeCita;
    private boolean citaPrevia;

    public DatoAgenciaAntidroga() {}

    public DatoAgenciaAntidroga(boolean citaPrevia, String nombreUsuario, String codigoRAD, Calendar fechaCita,
                                int tipoIntervencion, boolean acudeCita) {
        this.citaPrevia = citaPrevia;
        this.nombreUsuario = nombreUsuario;
        this.codigoRAD = codigoRAD;
        this.fechaCita = fechaCita;
        this.tipoIntervencion = tipoIntervencion;
        this.acudeCita = acudeCita;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCodigoRAD() {
        return codigoRAD;
    }

    public void setCodigoRAD(String codigoRAD) {
        this.codigoRAD = codigoRAD;
    }

    public Calendar getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Calendar fechaCita) {
        this.fechaCita = fechaCita;
    }

    public int getTipoIntervencion() {
        return tipoIntervencion;
    }

    public void setTipoIntervencion(int tipoIntervencion) {
        this.tipoIntervencion = tipoIntervencion;
    }

    public boolean isAcudeCita() {
        return acudeCita;
    }

    public void setAcudeCita(boolean acudeCita) {
        this.acudeCita = acudeCita;
    }

    public boolean isCitaPrevia() {
        return citaPrevia;
    }

    public void setCitaPrevia(boolean citaPrevia) {
        this.citaPrevia = citaPrevia;
    }

    public String toString() {
        return this.nombreUsuario + " | " + this.codigoRAD + " | " + (this.fechaCita == null ? null: this.fechaCita.getTime()) + " | " +
                this.tipoIntervencion + " | " + this.acudeCita + " | " + this.citaPrevia;
    }
}
