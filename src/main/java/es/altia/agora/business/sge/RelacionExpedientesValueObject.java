package es.altia.agora.business.sge;

import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.Vector;

public class RelacionExpedientesValueObject implements Serializable, ValueObject {

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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

 

    public String getCodOcultoTramite() {
        return codOcultoTramite;
    }

    public void setCodOcultoTramite(String codOcultoTramite) {
        this.codOcultoTramite = codOcultoTramite;
    }

    public String getCodTramite() {
        return codTramite;
    }

    public void setCodTramite(String codTramite) {
        this.codTramite = codTramite;
    }

    public String getDescTramite() {
      return descTramite;
    }

    public void setDescTramite(String descTramite) {
      this.descTramite = descTramite;
    }

    public Vector getListaProcedimientos() {
        return listaProcedimientos;
    }

    public void setListaProcedimientos(Vector listaProcedimientos) {
        this.listaProcedimientos = listaProcedimientos;
    }

    public Vector getListaTramites() {
        return listaTramites;
    }

    public void setListaTramites(Vector listaTramites) {
        this.listaTramites = listaTramites;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFueraDePlazo() {
        return fueraDePlazo;
    }

    public void setFueraDePlazo(String fueraDePlazo) {
        this.fueraDePlazo = fueraDePlazo;
    }

    public String getPendiente() {
        return pendiente;
    }

    public void setPendiente(String pendiente) {
        this.pendiente = pendiente;
    }

    public String getNumLineasPaginaListado() {
        return numLineasPaginaListado;
    }

    public void setNumLineasPaginaListado(String numLineasPaginaListado) {
        this.numLineasPaginaListado = numLineasPaginaListado;
    }

    public String getNumLineasPaginaListadoE() {
        return numLineasPaginaListadoE;
    }

    public void setNumLineasPaginaListadoE(String numLineasPaginaListadoE) {
        this.numLineasPaginaListadoE = numLineasPaginaListadoE;
    }

    public String getPaginaListado() {
        return paginaListado;
    }

    public void setPaginaListado(String paginaListado) {
        this.paginaListado = paginaListado;
    }

    public String getPaginaListadoE() {
        return paginaListadoE;
    }

    public void setPaginaListadoE(String paginaListadoE) {
        this.paginaListadoE = paginaListadoE;
    }

    public String getAsuntoRel() {
        return asuntoRel;
    }

    public void setAsuntoRel(String asuntoRel) {
        this.asuntoRel = asuntoRel;
    }

    public String getCodDepartamento() {
        return codDepartamento;
    }

    public void setCodDepartamento(String codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    public String getCodDomicilio() {
        return codDomicilio;
    }

    public void setCodDomicilio(String codDomicilio) {
        this.codDomicilio = codDomicilio;
    }

    public String getCodTercero() {
        return codTercero;
    }

    public void setCodTercero(String codTercero) {
        this.codTercero = codTercero;
    }

    public String getCodUnidadRegistro() {
        return codUnidadRegistro;
    }

    public void setCodUnidadRegistro(String codUnidadRegistro) {
        this.codUnidadRegistro = codUnidadRegistro;
    }

    public String getEjercicioRegistro() {
        return ejercicioRegistro;
    }

    public void setEjercicioRegistro(String ejercicioRegistro) {
        this.ejercicioRegistro = ejercicioRegistro;
    }

    public String getEjerNum() {
        return ejerNum;
    }

    public void setEjerNum(String ejerNum) {
        this.ejerNum = ejerNum;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaAnotacion() {
        return fechaAnotacion;
    }

    public void setFechaAnotacion(String fechaAnotacion) {
        this.fechaAnotacion = fechaAnotacion;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) { 
        isValid = valid;
    }
    
    public void setListaUnidadesProcedimiento(Vector listaUnidadesProcedimiento){
        this.listaUnidadesProcedimiento = listaUnidadesProcedimiento;
    }
    
    public Vector getListaUnidadesProcedimiento(){
        return listaUnidadesProcedimiento;
    }
    
    public Vector getListaUnidadesTramitadoras() {
        return listaUnidadesTramitadoras;
    }

    public void setListaUnidadesTramitadoras(Vector listaUnidadesTramitadoras) {
        this.listaUnidadesTramitadoras = listaUnidadesTramitadoras;
    }

    public String getNumeroRelacionExpedientes() {
        return numeroRelacionExpedientes;
    }

    public void setNumeroRelacionExpedientes(String numeroRelacionExpedientes) {
        this.numeroRelacionExpedientes = numeroRelacionExpedientes;
    }

    public String getNumeroExpedienteAntiguo() {
        return numeroExpedienteAntiguo;
    }

    public void setNumeroExpedienteAntiguo(String numeroExpedienteAntiguo) {
        this.numeroExpedienteAntiguo = numeroExpedienteAntiguo;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getRespOpcion() {
        return respOpcion;
    }

    public void setRespOpcion(String respOpcion) {
        this.respOpcion = respOpcion;
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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


    private String codTramite;
    private String codOcultoTramite;
    private String descTramite;
    private Vector listaProcedimientos;
    private Vector listaTramites;
    private Vector<ElementoListaValueObject> listaUtrs;

    private String codDepartamento;
    private String codUnidadRegistro;
    private String tipoRegistro;
    private String ejerNum;
    private String fechaAnotacion;
    private String remitente;
    private String asunto;
    private String estado;
    private String codTercero;
    private String codDomicilio;
    private String version;
    private String ejercicioRegistro;

    private String codMunicipio;
    private String codProcedimiento;
    private String descProcedimiento;
    private String ejercicio;
    private String numero;
    private String numeroRelacion;
    private String fechaInicio;
    private String fueraDePlazo;
    private String pendiente;
    private String asuntoRel;

    private String numeroRelacionExpedientes;
    private String numeroExpedienteAntiguo;

    private String paginaListado;
    private String numLineasPaginaListado;
    private String paginaListadoE;
    private String numLineasPaginaListadoE;

    private Vector listaUnidadesTramitadoras;
    private Vector listaUnidadesProcedimiento;
    
    private String respOpcion;

    private String origen;// Atributo para indicar si su origen es SGE, SicalWin,....
    
    private String codUor;
    private String descUor;
    private String codOcultoUor;

    public String getCodUor() {
        return codUor;
    }

    public void setCodUor(String codUor) {
        this.codUor = codUor;
    }

    public String getDescUor() {
        return descUor;
    }

    public void setDescUor(String descUor) {
        this.descUor = descUor;
    }

    public String getCodOcultoUor() {
        return codOcultoUor;
    }

    public void setCodOcultoUor(String codOcultoUor) {
        this.codOcultoUor = codOcultoUor;
    }        

    public Vector<ElementoListaValueObject> getListaUtrs() {
        return listaUtrs;
    }

    public void setListaUtrs(Vector<ElementoListaValueObject> listaUtrs) {
        this.listaUtrs = listaUtrs;
    }        
        
    /* ******************************************************** */
}