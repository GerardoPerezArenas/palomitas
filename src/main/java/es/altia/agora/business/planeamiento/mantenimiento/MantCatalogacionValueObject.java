package es.altia.agora.business.planeamiento.mantenimiento;

import java.io.Serializable;

public class MantCatalogacionValueObject implements Serializable {

    private String codigo;
    private String descripcion;

    public MantCatalogacionValueObject() {
    }

    public MantCatalogacionValueObject(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
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
        return "codigo: " + codigo + " | descripcion: " + descripcion;
    }
}
