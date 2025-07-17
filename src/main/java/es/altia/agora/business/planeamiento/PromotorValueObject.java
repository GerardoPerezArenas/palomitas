package es.altia.agora.business.planeamiento;

import java.io.Serializable;
import java.util.Calendar;

public class PromotorValueObject implements Serializable {

    private Character tipoRegistro;
    private String codigoSubseccion;
    private Integer numero;
    private String anho;
    private Integer codigo;

    public PromotorValueObject() {
    }

    public PromotorValueObject(Character tipoRegistro, String codigoSubseccion, Integer numero,
                               String anho, Integer codigo) {
        this.tipoRegistro = tipoRegistro;
        this.codigoSubseccion = codigoSubseccion;
        this.numero = numero;
        this.anho = anho;
        this.codigo = codigo;
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

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public boolean equals(Object o) {
        PromotorValueObject promotorVO = (PromotorValueObject) o;
        return (tipoRegistro.equals(promotorVO.getTipoRegistro()) && codigoSubseccion.equals(
                promotorVO.getCodigoSubseccion()) &&
                numero.equals(promotorVO.getNumero()) && codigo.equals(promotorVO.getCodigo()));
    }
    public String toString() {
        return "tipoRegistro: " + tipoRegistro + " | codigoSubseccion: " + codigoSubseccion + " | numero: " + numero +
                " | año: " + anho + " | codigo: " + codigo;
    }
}