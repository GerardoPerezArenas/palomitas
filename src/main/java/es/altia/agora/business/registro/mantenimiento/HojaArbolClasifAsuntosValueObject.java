package es.altia.agora.business.registro.mantenimiento;

import es.altia.technical.ValidationException;
import es.altia.technical.ValueObject;
import java.io.Serializable;
import java.util.Vector;

public class HojaArbolClasifAsuntosValueObject implements Serializable, ValueObject {
    
    public static final long serialVersionUID=0;
    //Estos son los atributos propios de la clasificacion de asunto
    private Integer codigo ; //Codigo de la clasificacion de asunto
    private String descripcion ; //Descripcion de la clasificacion de asunto
    //Estos son los asuntos de una clasificacion
    private Vector<MantAsuntosValueObject>  asuntosHijos =new Vector<MantAsuntosValueObject>(); 

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

    public Vector<MantAsuntosValueObject> getAsuntosHijos() {
        return asuntosHijos;
    }

    public void setAsuntosHijos(Vector<MantAsuntosValueObject> asuntosHijos) {
        this.asuntosHijos = asuntosHijos;
    }
    
    
   
    public String toString() {
        String vo = "HojaArbolClasifAsuntosValueObject :" + 
               " codigo=" + codigo + 
               " descripcion=" + descripcion+
               " Hijos: ";
        
        for (MantAsuntosValueObject hijo : asuntosHijos) {
            vo += hijo.getDescripcion() + " ";
        }
      

        return vo;
    }

    public void validate(String idioma) throws ValidationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

 
}