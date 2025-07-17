package es.altia.agora.business.administracion;

import java.io.Serializable;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import es.altia.technical.ValueObject;

public class HorarioCentroValueObject implements Serializable, ValueObject {

    /** Construye un nuevo horario por defecto. */
    public HorarioCentroValueObject(String fecha_inicio, String fecha_fin, String hora_inicio_m,
                                    String hora_fin_m, String hora_inicio_t, String hora_fin_t){
       this.fecha_inicio  = fecha_inicio;
       this.fecha_fin     = fecha_fin;
       this.hora_inicio_m = hora_inicio_m;
       this.hora_fin_m    = hora_fin_m;
       this.hora_inicio_t = hora_inicio_t;
       this.hora_fin_t    = hora_fin_t;
    }

    //Metodos getter y setter de los objetos que componen "elHorarioCentro"
    public String getFecha_inicio() { return fecha_inicio; }
    public void setFecha_inicio(String fecha_inicio) { this.fecha_inicio = fecha_inicio; }

    public String getFecha_fin() { return fecha_fin; }
    public void setFecha_fin(String fecha_fin) { this.fecha_fin = fecha_fin; }

    public String getHora_inicio_m() { return hora_inicio_m; }
    public void setHora_inicio_m(String hora_inicio_m) { this.hora_inicio_m = hora_inicio_m; }

    public String getHora_fin_m() { return hora_fin_m; }
    public void setHora_fin_m(String hora_fin_m) { this.hora_fin_m = hora_fin_m; }

    public String getHora_inicio_t() { return hora_inicio_t; }
    public void setHora_inicio_t(String hora_inicio_t) { this.hora_inicio_t = hora_inicio_t; }

    public String getHora_fin_t() { return hora_fin_t; }
    public void setHora_fin_t(String hora_fin_t) { this.hora_fin_t = hora_fin_t; }


    public void validate(String idioma) throws ValidationException {
        String sufijo = "";
        if ("euskera".equals(idioma)) sufijo="_eu";
        isValid=false;
        Messages errors = new Messages();

        isValid=true;
  }

    private boolean isValid;
    public boolean IsValid() { return isValid; }

    private String fecha_inicio;
    private String fecha_fin;
    private String hora_inicio_m;
    private String hora_fin_m;
    private String hora_inicio_t;
    private String hora_fin_t;
}



