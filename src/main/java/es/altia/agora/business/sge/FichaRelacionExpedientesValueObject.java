package es.altia.agora.business.sge;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.Vector;

public class FichaRelacionExpedientesValueObject implements Serializable, ValueObject {

    /** Construye un nuevo RelacionExpedientes por defecto. */

    public String getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(String codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public String getDescProcedimiento() {
      return descProcedimiento;
    }

    public void setDescProcedimiento(String descProcedimiento) {
      this.descProcedimiento = descProcedimiento;
    }

    public String getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getNumeroRelacion() {
        return numeroRelacion;
    }

    public void setNumeroRelacion(String numeroRelacion) {
        this.numeroRelacion = numeroRelacion;
    }

    public String getNumeroRelacionMostrar() {
        return numeroRelacionMostrar;
    }

    public void setNumeroRelacionMostrar(String numeroRelacionMostrar) {
        this.numeroRelacionMostrar = numeroRelacionMostrar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getUsuarioInicio() {
        return usuarioInicio;
    }

    public void setUsuarioInicio(String usuarioInicio) {
        this.usuarioInicio = usuarioInicio;
    }

    public String getUORInicio() {
      return UORInicio;
    }

    public void setUORInicio(String UORInicio) {
      this.UORInicio = UORInicio;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public String getUsuarioFin() {
        return usuarioFin;
    }

    public void setUsuarioFin(String usuarioFin) {
        this.usuarioFin = usuarioFin;
    }

    public String getUORFin() {
      return UORFin;
    }

    public void setUORFin(String UORFin) {
      this.UORFin = UORFin;
    }

    public String getTramiteInicio() {
        return tramiteInicio;
    }

    public void setTramiteInicio(String tramiteInicio) {
        this.tramiteInicio = tramiteInicio;
    }

    public String getTramiteActual() {
        return tramiteActual;
    }

    public void setTramiteActual(String tramiteActual) {
        this.tramiteActual = tramiteActual;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Vector getListaExpedientes() {
        return listaExpedientes;
    }

    public void setListaExpedientes(Vector listaExpedientes) {
        this.listaExpedientes = listaExpedientes;
    }

    public Vector getListaEjeExp() {
        return listaEjeExp;
    }

    public void setListaEjeExp(Vector listaEjeExp) {
        this.listaEjeExp = listaEjeExp;
    }

    public Vector getListaMunExp() {
        return listaMunExp;
    }

    public void setListaMunExp(Vector listaMunExp) {
        this.listaMunExp = listaMunExp;
    }

    public Vector getListaNumExp() {
        return listaNumExp;
    }

    public void setListaNumExp(Vector listaNumExp) {
        this.listaNumExp = listaNumExp;
    }

    public Vector getListaProExp() {
        return listaProExp;
    }

    public void setListaProExp(Vector listaProExp) {
        this.listaProExp = listaProExp;
    }


    public String getCodBloqueo() {
        return codBloqueo;
    }

    public void setCodBloqueo(String codBloqueo) {
        this.codBloqueo = codBloqueo;
    }

    public String getNomBloqueo() {
        return nomBloqueo;
    }

    public void setNomBloqueo(String nomBloqueo) {
        this.nomBloqueo = nomBloqueo;
    }

    public String getCodUorInicial() {
        return codUorInicial;
    }

    public void setCodUorInicial(String codUorInicial) {
        this.codUorInicial = codUorInicial;
    }
    
    

    /**
     * Valida el estado de esta RegistroSaida
     * Puede ser invocado desde la capa cliente o desde la capa de negocio
     * @exception es.altia.technical.ValidationException si el estado no es válido
     */
    public void validate(String idioma) throws ValidationException {
        String sufijo = "";
        if ("euskera".equals(idioma)) sufijo="_eu";
    boolean correcto = true;
        Messages errors = new Messages();

        if (!errors.empty())
            throw new ValidationException(errors);
        isValid = true;
    }

    /** Devuelve un booleano que representa si el estado de este RegistroSaida es válido. */
    public boolean IsValid() { return isValid; }



    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;

    private String codMunicipio;
    private String codProcedimiento;
    private String descProcedimiento;
    private String ejercicio;
    private String numeroRelacion;
    private String numeroRelacionMostrar;
    private String estado;
    private String fechaInicio;
    private String usuarioInicio;
    private String UORInicio;
    private String fechaFin;
    private String usuarioFin;
    private String UORFin;
    private String tramiteInicio;
    private String tramiteActual;
    private String asunto;
    private String observaciones;
    private Vector listaExpedientes;
    private Vector listaMunExp;
    private Vector listaProExp;
    private Vector listaEjeExp;
    private Vector listaNumExp;

    private String codBloqueo; //Código del usuario del bloqueo
    private String nomBloqueo; //Nombre del usuario del bloqueo
    
    private String codUorInicial;
/* ******************************************************** */
}