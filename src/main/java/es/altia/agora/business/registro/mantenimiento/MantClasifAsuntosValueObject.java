package es.altia.agora.business.registro.mantenimiento;

import es.altia.technical.ValueObject;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.technical.ValidationException;

import java.io.Serializable;
import java.util.Vector;

public class MantClasifAsuntosValueObject implements Serializable, ValueObject {
    
    public static final long serialVersionUID=0;
   
    
    private Integer codigo ;
    private String descripcion ;
    private Integer unidadRegistro;
    
    
    
    public MantClasifAsuntosValueObject() {
        super();
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
        
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getUnidadRegistro() {
        return unidadRegistro;
    }

    public void setUnidadRegistro(Integer unidadRegistro) {
        this.unidadRegistro = unidadRegistro;
    }

    
    public String toString() {
        String vo = "MantClasifAsuntosValueObject :" + 
               " codigo= " + codigo + 
               " descripcion= " + descripcion +
               " unidadRegistro= " + unidadRegistro ;
       
        return vo;
    }

    public void validate(String idioma) throws ValidationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

  
}