
package es.altia.agora.interfaces.user.web.administracion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import java.util.Vector;
import es.altia.agora.business.administracion.GestionValueObject;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


/** Clase utilizada para capturar o mostrar el estado de una PersonaFisica */
public class GestionForm extends ActionForm {//implements FacturacionInterface {
    //Necesitamos el servicio de log
    protected static Log m_Log = LogFactory.getLog(GestionForm.class.getName());

    /*public DatosFacturacionInterface getDatosFacturacion(){
      return null;
    }*/

    //Reutilizamos
    GestionValueObject laGestion = new GestionValueObject();
    public GestionValueObject getGestion() { return laGestion; }
    public void setGestion(GestionValueObject laGestion) { this.laGestion = laGestion; }


    //Metodos getter y setter de los objetos que componen "laGestion"
    public String getCentro(){ return laGestion.getCentro(); }
    public void setCentro(String centro){ laGestion.setCentro(centro.trim()); }

    public String getUbicacion(){ return laGestion.getUbicacion(); }
    public void setUbicacion(String ubicacion){ laGestion.setUbicacion(ubicacion.trim()); }

    public String getDesccentro(){  return laGestion.getDesccentro(); }
    public void setDesccentro(String desccentro){ laGestion.setDesccentro(desccentro.trim()); }

    public String getDescubicacion(){ return laGestion.getDescubicacion(); }
    public void setDescubicacion(String descubicacion){ laGestion.setDescubicacion(descubicacion.trim()); }

    public Vector getLista_centros_ubicaciones(){ return laGestion.getLista_centros_ubicaciones();}
    public void setLista_centros_ubicaciones(Vector lista_centros_ubicaciones){ laGestion.setLista_centros_ubicaciones(lista_centros_ubicaciones); }

    public String getFecha(){ return laGestion.getFecha(); }
    public void setFecha(String fecha){ laGestion.setFecha(fecha.trim()); }

    public String getHora(){ return laGestion.getHora(); }
    public void setHora(String hora){ laGestion.setHora(hora.trim()); }

    public String getAno(){ return laGestion.getAno(); }
    public void setAno(String ano){ laGestion.setAno(ano.trim()); }

    public String getLaborable(){ return laGestion.getLaborable(); }
    public void setLaborable(String laborable){ laGestion.setLaborable(laborable.trim()); }

    public String getAccion(){ return laGestion.getAccion(); }
    public void setAccion(String accion){ laGestion.setAccion(accion.trim()); }

    public String getMensaje(){ return laGestion.getMensaje(); }
    public void setMensaje(String mensaje){ laGestion.setMensaje(mensaje.trim()); }

    public String getMaxFestivosCentro(){ return laGestion.getMaxFestivosCentro(); }
    public void setMaxFestivosCentro(String maxFestivosCentro){ laGestion.setMaxFestivosCentro(maxFestivosCentro.trim()); }

    public String getMaxVacacionesCentro(){ return laGestion.getMaxVacacionesCentro(); }
    public void setMaxVacacionesCentro(String maxVacacionesCentro){ laGestion.setMaxVacacionesCentro(maxVacacionesCentro.trim()); }

    public String getError(){ return laGestion.getError(); }
    public void setError(String error){ laGestion.setError(error.trim()); }


  //Raquel
    public String getDia_antes(){ return laGestion.getDia_antes(); }
    public void setDia_antes(String dia){laGestion.setDia_antes(dia); }

    public String getDias_cambiados () { return laGestion.getDias_cambiados(); }
    public void setDias_cambiados(String dias) {laGestion.setDias_cambiados(dias); }

    public String getHorarios_borrados0 () { return laGestion.getHorarios_borrados0(); }
    public void setHorarios_borrados0(String borrados) {laGestion.setHorarios_borrados0(borrados); }

    public String getHorarios_borrados1 () { return laGestion.getHorarios_borrados1(); }
    public void setHorarios_borrados1(String borrados) {laGestion.setHorarios_borrados1(borrados); }

    public Vector getEntrevistadores_dia_horas() { return laGestion.getEntrevistadores_dia_horas(); }
    public void setEntrevistadores_dia_horas(Vector entrevistadores_dia_horas) { laGestion.setEntrevistadores_dia_horas(entrevistadores_dia_horas); }
    //Fin Raquel

    //Metodo getter que devuelve la lista de entidades
    public Vector getLista_centro() { return laGestion.getLista_centro(); }

    //Metodo getter que devuelve la lista de horas validas
    public Vector getLista_horas_validas() { return laGestion.getLista_horas_validas(); }

    //Metodo getter que devuelve la lista de horas reducidas
    public Vector getLista_horas_reducidas() { return laGestion.getLista_horas_reducidas(); }

    //Metodo getter que devuelve la lista de citas
    public Vector getLista_citas() { return laGestion.getLista_citas(); }

    //Metodo getter que devuelve la lista de personas citadas
    public Vector getLista_personas_citadas() { return laGestion.getLista_personas_citadas(); }

    //Metodo getter que devuelve la lista de las valideces de las horas
    public Vector getLista_validez_horas() { return laGestion.getLista_validez_horas(); }

    //Metodo getter que devuelve la lista de los dias festivos generales
    public Vector getLista_festivos_generales() { return laGestion.getLista_festivos_generales(); }

    //Metodo getter que devuelve la lista de los dias festivos de centro
    public Vector getLista_festivos_centro() { return laGestion.getLista_festivos_centro(); }

    //Metodo getter que devuelve la lista de los dias estivales de centro
    public Vector getLista_vacaciones_centro() { return laGestion.getLista_vacaciones_centro(); }

    //Metodo getter que devuelve la lista de dias ocupados
    public Vector getLista_dias_ocupados() { return laGestion.getLista_dias_ocupados(); }

   //Metodo getter que devuelve la lista de horas ocupadas
    public Vector getLista_horas_ocupadas() { return laGestion.getLista_horas_ocupadas(); }

   //Metodo getter que devuelve el num max de personas que hay en un centro-ubicacion
   public Vector getNum_max_personas() {return laGestion.getNum_max_personas(); }

    //Metodo getter que devuelve el horario normal
    public Vector getHorario_normal() { return laGestion.getHorario_normal(); }

    //Metodo getter que devuelve la lista del horario especial
    public Vector getHorario_especial() { return laGestion.getHorario_especial(); }



    //Metodos getter y setter que gestionan la lista de los errores
    public Vector getListaMensajesError() { return laGestion.getListaMensajesError(); }
    public void setListaMensajesError(Vector listaMensajesError) { laGestion.setListaMensajesError(listaMensajesError); }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        return errors;
    }

    //Inic Raquel
    private Vector entrevistadores_dia_horas = new Vector();
    //Fin Raquel
}
