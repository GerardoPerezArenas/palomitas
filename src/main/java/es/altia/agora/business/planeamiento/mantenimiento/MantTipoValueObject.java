package es.altia.agora.business.planeamiento.mantenimiento;

import java.io.Serializable;

public class MantTipoValueObject implements Serializable {

    private Character tipoRegistro;
    private String codigoSubseccion;
    private String codigo;
    private String descripcion;

    public MantTipoValueObject() {
    }

    public MantTipoValueObject(Character tipoRegistro, String codigoSubseccion, String codigo, String descripcion) {
        this.tipoRegistro = tipoRegistro;
        this.codigoSubseccion = codigoSubseccion;
        this.codigo = codigo;
        this.descripcion = descripcion;
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
        return "tipoRegistro: " + tipoRegistro + " | codigoSubseccion: " + codigoSubseccion + " | codigo: " + codigo +
                " | descripcion: " + descripcion;
    }
}
