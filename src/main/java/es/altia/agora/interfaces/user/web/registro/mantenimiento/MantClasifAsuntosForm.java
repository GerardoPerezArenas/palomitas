package es.altia.agora.interfaces.user.web.registro.mantenimiento;


import org.apache.struts.action.ActionForm;
import es.altia.agora.business.registro.mantenimiento.MantClasifAsuntosValueObject;
import java.util.ArrayList;

public class MantClasifAsuntosForm extends ActionForm {
    
    public static final long serialVersionUID=0;
    
    private ArrayList<MantClasifAsuntosValueObject> clasifAsuntos;
    
  
    private String opcion;
    //Tenemos esta opción, para cuando a la .jsp, vamos desde un error
    private String opcionError="";
    private Integer codigo;
    private Integer unidadRegistro;
    private String descripcion;

    public String getOpcionError() {
        return opcionError;
    }

    public void setOpcionError(String opcionError) {
        this.opcionError = opcionError;
    }
      
    
    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }        

    
    public ArrayList<MantClasifAsuntosValueObject> getClasifAsuntos() {
        return clasifAsuntos;
    }

    public void setClasifAsuntos(ArrayList<MantClasifAsuntosValueObject> clasifAsuntos) {
        this.clasifAsuntos = clasifAsuntos;
    }
              

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo=codigo;
    }

   
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion=descripcion;
    }
   
    public Integer getUnidadRegistro() {
        return this.unidadRegistro;
    }

    public void setUnidadRegistro(Integer unidadRegistro) {
        this.unidadRegistro=unidadRegistro;
    }
   
  
}
