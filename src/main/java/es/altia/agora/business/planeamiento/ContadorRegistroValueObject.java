package es.altia.agora.business.planeamiento;

import java.io.Serializable;

public class ContadorRegistroValueObject implements Serializable {

    private Character tipoRegistro;
    private String codigoSubseccion;
    private Integer numero;
    private String anho;

    public ContadorRegistroValueObject() {
    }

    public ContadorRegistroValueObject(Character tipoRegistro, String codigoSubseccion,
                                       Integer numero, String anho) {
        this.tipoRegistro = tipoRegistro;
        this.codigoSubseccion = codigoSubseccion;
        this.numero = numero;
        this.anho = anho;
    }

    public Character getTipoRegistro(){
        return tipoRegistro;
    }

    public void setTipoRegistro(Character tipoRegistro){
    	this.tipoRegistro = tipoRegistro;
    }

    public String getCodigoSubseccion(){
        return codigoSubseccion;
    }

    public void setCodigoSubseccion(String codigoSubseccion){
    	this.codigoSubseccion = codigoSubseccion;
    }

    public Integer getNumero(){
        return numero;
    }

    public void setNumero(Integer numero){
        this.numero = numero;
    }

    public String getAnho(){
        return anho;
    }

    public void setAnho(String anho){
        this.anho = anho;
    }

    public String toString() {
        return "tipoRegistro: " + tipoRegistro + " | codigoSubseccion: " + codigoSubseccion + " | numero: " + numero +
                " | año: " + anho;
    }
}
