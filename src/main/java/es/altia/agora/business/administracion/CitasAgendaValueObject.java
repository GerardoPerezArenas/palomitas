package es.altia.agora.business.administracion;

import java.io.Serializable;

import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import es.altia.technical.ValueObject;

public class CitasAgendaValueObject implements Serializable, ValueObject {

    /** Construye una nueva cita por defecto. */
    public CitasAgendaValueObject(String tipo_doc, String nif, String nombre, String hora, String tipo, String estado){
       this.tipo_doc = tipo_doc;
       this.nif      = nif;
       this.nombre   = nombre;
       this.hora     = hora;
       this.tipo     = tipo;
       this.estado   = estado;
    }

  //Inic Raquel
    public CitasAgendaValueObject(String tipo_doc, String nif, String nombre, String hora, String tipo, String estado, String tfno){
       this.tipo_doc = tipo_doc;
       this.nif      = nif;
       this.nombre   = nombre;
       this.hora     = hora;
       this.tipo     = tipo;
       this.estado   = estado;
       this.tfno     = tfno;
    }
  //Fin Raquel


    //Metodos getter y setter de los objetos que componen "la cita"
    public String getTipo_doc() { return tipo_doc; }
    public void setTipo_doc(String tipo_doc) { this.tipo_doc = tipo_doc; }

    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

  //Inic Raquel
    public String getTfno() { return tfno; }
    public void setTfno(String tfno) { this.tfno = tfno; }
  //Fin Raquel

    public void validate(String idioma) throws ValidationException {
        String sufijo = "";
        if ("euskera".equals(idioma)) sufijo="_eu";
        isValid=false;
        Messages errors = new Messages();

        isValid=true;
  }

    private boolean isValid;
    public boolean IsValid() { return isValid; }

    private String tipo_doc;
    private String nif;
    private String nombre;
    private String hora;
    private String tipo;
    private String estado;
    private String tfno;

}



