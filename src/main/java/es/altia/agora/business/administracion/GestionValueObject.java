package es.altia.agora.business.administracion;

import java.io.Serializable;
import java.util.Vector;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import es.altia.technical.ValueObject;

public class GestionValueObject implements Serializable, ValueObject {

    /** Construye una nueva PersonaFisica por defecto. */
    public GestionValueObject() {
    }

    //Metodos getter y setter de los objetos que componen "laGestion"
    public String getCentro() { return centro; }
    public void setCentro(String centro) { this.centro = centro; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getDesccentro() { return desccentro; }
    public void setDesccentro(String desccentro) { this.desccentro = desccentro; }

    public String getDescubicacion() { return descubicacion; }
    public void setDescubicacion(String descubicacion) { this.descubicacion = descubicacion; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public java.util.Date getFecha_date() { return fecha_date; }
    public void setFecha_date(java.util.Date fecha) { this.fecha_date = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public int getDia() { return dia; }
    public void setDia(int dia) { this.dia = dia; }

    public String getAno() { return ano; }
    public void setAno(String ano) { this.ano = ano; }

    public String getLaborable() { return laborable; }
    public void setLaborable(String laborable) { this.laborable = laborable; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getMaxFestivosCentro() { return maxFestivosCentro; }
    public void setMaxFestivosCentro(String maxFestivosCentro) { this.maxFestivosCentro = maxFestivosCentro; }

    public String getMaxVacacionesCentro() { return maxVacacionesCentro;}
    public void setMaxVacacionesCentro(String maxVacacionesCentro) { this.maxVacacionesCentro = maxVacacionesCentro; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }


    //Metodos de la lista de entidades
    public Vector  getLista_centro() { return lista_centro; }
    public void setLista_centro(Vector lista_centro) { this.lista_centro = lista_centro; }

    //Metodos de la lista de codigos y descripciones de codigos+ubicaciones
    public Vector  getLista_centros_ubicaciones() { return lista_centros_ubicaciones; }
    public void setLista_centros_ubicaciones(Vector lista_centros_ubicaciones) { this.lista_centros_ubicaciones = lista_centros_ubicaciones; }

    //Metodos de la lista de horas normales
    public Vector getLista_horas_validas() { return lista_horas_validas; }
    public void setLista_horas_validas(Vector lista_horas_validas) { this.lista_horas_validas = lista_horas_validas; }

    //Metodos de la lista de horas especiales
    public Vector getLista_horas_reducidas() { return lista_horas_reducidas; }
    public void setLista_horas_reducidas(Vector lista_horas_reducidas) { this.lista_horas_reducidas = lista_horas_reducidas; }

    //Metodos de la lista de las valideces de las horas
    public Vector getLista_validez_horas() { return lista_validez_horas; }
    public void setLista_validez_horas(Vector lista_validez_horas) { this.lista_validez_horas = lista_validez_horas; }

    //Metodos de la lista de citas
    public Vector getLista_citas() { return lista_citas; }
    public void setLista_citas(Vector lista_citas) { this.lista_citas = lista_citas; }

    //Metodos de la lista de las personas citadas
    public Vector getLista_personas_citadas() { return lista_personas_citadas; }
    public void setLista_personas_citadas(Vector lista_personas_citadas) { this.lista_personas_citadas = lista_personas_citadas; }

    //Metodos de la lista de festivos generales
    public Vector getLista_festivos_generales() { return lista_festivos_generales; }
    public void setLista_festivos_generales(Vector lista_festivos_generales) { this.lista_festivos_generales = lista_festivos_generales; }

    //Metodos de la lista de festivos de centro
    public Vector getLista_festivos_centro() { return lista_festivos_centro; }
    public void setLista_festivos_centro(Vector lista_festivos_centro) { this.lista_festivos_centro = lista_festivos_centro; }

    //Metodos de la lista de festivos de centro
    public Vector getLista_vacaciones_centro() { return lista_vacaciones_centro; }
    public void setLista_vacaciones_centro(Vector lista_vacaciones_centro) { this.lista_vacaciones_centro = lista_vacaciones_centro; }

    //Metodos de la lista de dias ocupados de un centro
    public Vector getLista_dias_ocupados() { return lista_dias_ocupados; }
    public void setLista_dias_ocupados(Vector lista_dias_ocupados) { this.lista_dias_ocupados = lista_dias_ocupados; }

    //Metodos del horario normal
    public Vector getHorario_normal() { return horario_normal; }
    public void setHorario_normal(Vector horario_normal) { this.horario_normal = horario_normal; }

    //Metodos de la lista de horas ocupadas de un centro
   public Vector getLista_horas_ocupadas() { return lista_horas_ocupadas; }
   public void setLista_horas_ocupadas(Vector lista_horas_ocupadas) { this.lista_horas_ocupadas = lista_horas_ocupadas; }

   //Metodos de num max personas de centro-ubicacion
   public Vector getNum_max_personas() {return num_max_personas; }
   public void setNum_max_personas(Vector num_max_personas) {this.num_max_personas = num_max_personas; }

    //Metodos del horario especial
    public Vector getHorario_especial() { return horario_especial; }
    public void setHorario_especial(Vector horario_especial) { this.horario_especial = horario_especial; }

    //Metodos de la lista de errores
    public Vector getListaMensajesError() { return listaMensajesError; }
    public void setListaMensajesError(Vector listaMensajesError) { this.listaMensajesError = listaMensajesError; }

    //Inic Raquel
  //Metodos para la fecha anterior
    public String getDia_antes() { return dia_antes; }
    public void setDia_antes(String dia) { this.dia_antes = dia; }
  //Metodos para los dias cambiados
    public String getDias_cambiados() { return dias_cambiados; }
    public void setDias_cambiados(String cambiados) { this.dias_cambiados = cambiados; }
  //Metodos para los intervalos borrados
    public String getHorarios_borrados0() { return horarios_borrados0; }
    public void setHorarios_borrados0(String borrados) { this.horarios_borrados0 = borrados; }
    public String getHorarios_borrados1() { return horarios_borrados1; }
    public void setHorarios_borrados1(String borrados) { this.horarios_borrados1 = borrados; }
      //Metodos para saber los entrevistadores necesarios para un dia concreto
    public Vector getEntrevistadores_dia_horas() { return this.entrevistadores_dia_horas; }
    public void setEntrevistadores_dia_horas(Vector entrevistadores_dia_horas) { this.entrevistadores_dia_horas = entrevistadores_dia_horas; }
  //Fin Raquel

    /**
     * Valida el estado de esta PersonaFisica
     * Puede ser invocado desde la capa cliente o desde la capa de negocio
     * @exception ValidationException si el estado no es válido
     */
    public void validate(String idioma) throws ValidationException {
        String sufijo = "";
        if ("euskera".equals(idioma)) sufijo="_eu";
        isValid=false;
        Messages errors = new Messages();

        isValid=true;
  }

    /** Devuelve un booleano que representa si el estado de esta PersonaFisica es válido. */
    public boolean IsValid() { return isValid; }


    //Objetos que componen "laGestion"
    private String centro="";
    private String ubicacion="";
    private String desccentro="";
    private String descubicacion="";
    private String fecha="";
    private java.util.Date fecha_date;
    private String hora="";
    private int dia=0;
    private String ano="";
    private String laborable="";
    private String accion="";
    private String mensaje="";
    private String maxFestivosCentro="";
    private String maxVacacionesCentro="";
    //Inic Raquel
    private String dia_antes = "";
    private String dias_cambiados = "";
    private String horarios_borrados0 = "";
    private String horarios_borrados1 = "";
    private Vector entrevistadores_dia_horas = new Vector();
    //Fin Raquel

    private String error="";

    private Vector lista_centro;
    private Vector lista_centros_ubicaciones;
    private Vector lista_horas_validas;
    private Vector lista_horas_reducidas;
    private Vector lista_validez_horas;
    private Vector lista_citas;
    private Vector lista_personas_citadas;
    private Vector lista_festivos_generales;
    private Vector lista_festivos_centro;
    private Vector lista_vacaciones_centro;
    private Vector lista_dias_ocupados;
    private Vector lista_horas_ocupadas;
    private Vector horario_normal;
    private Vector horario_especial;
    private Vector num_max_personas;

    private Vector listaMensajesError=new Vector();

    /** Variable booleana que indica si el estado de la instancia de PersonaFisica es válido o no */
    private boolean isValid;
}



