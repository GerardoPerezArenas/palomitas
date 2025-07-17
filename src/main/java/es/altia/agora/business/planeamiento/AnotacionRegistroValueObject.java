package es.altia.agora.business.planeamiento;

import java.io.Serializable;
import java.util.Calendar;

public class AnotacionRegistroValueObject implements Serializable {

    private Character tipoRegistro;
    private String codigoSubseccion;
    private Integer numero;
    private String anho;
    private String numeroAnotacion;
    private Calendar fechaAnotacion;
    private String observaciones;

    public AnotacionRegistroValueObject() {
    }

    public AnotacionRegistroValueObject(Character tipoRegistro, String codigoSubseccion,
                                        Integer numero, String anho, String numeroAnotacion, Calendar fechaAnotacion,
                                        String observaciones) {
        this.tipoRegistro = tipoRegistro;
        this.codigoSubseccion = codigoSubseccion;
        this.numero = numero;
        this.anho = anho;
        this.numeroAnotacion = numeroAnotacion;
        this.fechaAnotacion = fechaAnotacion;
        this.observaciones = observaciones;
    }

    public Character getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(Character tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getCodigoSubseccion() {
        return codigoSubseccion;
    }

    public void setCodigoSubseccion(String codigoSubseccion) {
        this.codigoSubseccion = codigoSubseccion;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getAnho() {
        return anho;
    }

    public void setAnho(String anho) {
        this.anho = anho;
    }

    public String getNumeroAnotacion() {
        return numeroAnotacion;
    }

    public void setNumeroAnotacion(String numeroAnotacion) {
        this.numeroAnotacion = numeroAnotacion;
    }

    public Calendar getFechaAnotacion() {
        return fechaAnotacion;
    }

    public void setFechaAnotacion(Calendar fechaAnotacion) {
        this.fechaAnotacion = fechaAnotacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String toString() {
        return "tipoRegistro: " + tipoRegistro + " | codigoSubseccion: " + codigoSubseccion + " | numero: " + numero +
                " | año: " + anho + " | numeroAnotacion: " + numeroAnotacion +
                " | fechaAnotacion: " + fechaAnotacion + " | observaciones: " + observaciones;
    }
}