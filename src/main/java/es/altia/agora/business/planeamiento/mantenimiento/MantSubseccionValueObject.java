package es.altia.agora.business.planeamiento.mantenimiento;

import java.io.Serializable;

public class MantSubseccionValueObject implements Serializable {

    private Character tipoRegistro;
    private String codigo;
    private String descripcion;

    public MantSubseccionValueObject() {
    }

    public MantSubseccionValueObject(Character tipoRegistro, String codigo, String descripcion) {
        this.tipoRegistro = tipoRegistro;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public Character getTipoRegistro(){
        return tipoRegistro;
    }

    public void setTipoRegistro(Character tipoRegistro){
    	this.tipoRegistro = tipoRegistro;
    }

    public String getCodigo(){
        return codigo;
    }

    public void setCodigo(String codigo){
    	this.codigo = codigo;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public void setDescripcion(String descripcion){
    	this.descripcion = descripcion;
    }

    public String toString() {
        return "tipoRegistro: " + tipoRegistro + " | codigo: " +
        codigo + " | descripcion: " + descripcion;
    }
}
