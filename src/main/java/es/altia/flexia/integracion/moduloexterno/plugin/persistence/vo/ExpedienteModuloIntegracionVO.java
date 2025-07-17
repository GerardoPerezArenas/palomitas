package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;

import java.util.ArrayList;
import java.util.Calendar;

public class ExpedienteModuloIntegracionVO {
    // Código del municipio
    private int codMunicipio;
    // Ejercicio
    private int ejercicio;
    // Número del expediente
    private String numExpediente;
    // Código del procedimiento
    private String codProcedimiento;
    // Asunto
    private String asunto;
    // Observaciones
    private String observaciones;
    // Fecha de inicio expediente
    private Calendar fechaInicio;
    // Fecha de fin expediente
    private Calendar fechaFin;
    // Código del usuario que inicia el expediente
    private int codUsuarioIniciaExpediente;
    // Código de la uor visible de la unidad de inicio del expediente
    private String codigoUorVisibleInicioExpediente;
    // Descripción de la unidad de inicio del expediente
    private String descripcionUnidadInicioExpediente;
    // Localización del expediente
    private String localizacion;
    // Código interno del último trámite cerrado
    private int codigoTramiteCerrado;
    // Ocurrencia del último trámite cerrado
    private int ocurrenciaTramiteCerrado;
    // Código visible del último trámite del expediente que está cerrado
    private int codigoTramiteVisibleCerrado;    
    // Descripción/Título del último trámite cerrado
    private String descripcionTramiteCerrado;
    // Indica si un expediente está marcado o no como importante
    private boolean importante;
    private ArrayList<InteresadoExpedienteModuloIntegracionVO> interesados;
    // Código que identifica al tipo de procedimiento
    private int tipoProcedimiento;
    // Descripción correspondiente al tipo de procedimiento
    private String descripcionTipoProcedimiento;
    //Numero del expediente
    private int numero;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
    
    /**
     * Devuelve el código del municipio/organización del expediente
     * @return the codMunicipio
     */
    public int getCodMunicipio() {
        return codMunicipio;
    }

    /**
     * Establece el código del municipio/organización del expediente
     * @param codMunicipio: int con el cod. del municipio
     */
    public void setCodMunicipio(int codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    /**
     * Devuelve el ejercicio del expediente
     * @return the ejercicio
     */
    public int getEjercicio() {
        return ejercicio;
    }

    /**
     * Establece el ejercicio del expediente
     * @param ejercicio: int con el ejercicio
     */
    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }


    /**
     * Devuelve el numero del expediente
     * @return the numExpediente
     */
    public String getNumExpediente() {
        return numExpediente;
    }

    /**
     * Establece el numero del expediente
     * @param numExpediente: String con el número del expediente
     */
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    /**
     * Retorna el código del procedimiento
     * @return Código del procedimiento o null en caso contrario
     */
    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    /**
     * Permite establece el código del procedimiento
     * @param codProcedimiento: String con el código del procedimiento
     */
    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    /**
     * Devuelve el asunto del expediente
     * @return String con el asunto
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Establece el asunto del expediente
     * @param asunto the asunto to set
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /**
     * Devuelve las observaciones del expediente
     * @return String con las observaciones o null en caso contrario
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Permite establecer las observaciones del expediente
     * @param observaciones: Observaciones
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * retorna la fecha de inicio del expediente
     * @return fechaInicio: Calendar
     */
    public Calendar getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Permite establecer la fecha de inicio del expediente
     * @param fechaInicio: Calendar
     */
    public void setFechaInicio(Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Retorna un Calendar con la fecha de fin del expediente o null sino el expediente no ha sido finalizado
     * @return Calendar
     */
    public Calendar getFechaFin() {
        return fechaFin;
    }

    /**
     * Permite establecer la fecha de fin del expediente
     * @param fechaFin: Objeto de tipo Calendar con la fecha de fin
     */
    public void setFechaFin(Calendar fechaFin) {
        this.fechaFin = fechaFin;
    }
  
    /**
     * Retorna la localización del expediente
     * @return the localizacion
     */
    public String getLocalizacion() {
        return localizacion;
    }

    /**
     * Establece la localización del expediente
     * @param localizacion: String con la descripción de la localización
     */
    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    /**
     * Indica si el expediente está marcado como importante
     * @return the importante
     */
    public boolean isImportante() {
        return importante;
    }

    /**
     * Establece si un expediente está marcado como importante
     * @param importante: boolean
     */
    public void setImportante(boolean importante) {
        this.importante = importante;
    }

    /**
     * Retorna true si el expediente está cerrado y false en caso contrario
     * @return boolean
     */
    public boolean isCerrado(){
        if(this.fechaFin!=null)
            return true;

        return false;
    }

    /**
     * @return the codUsuarioIniciaExpediente
     */
    public int getCodUsuarioIniciaExpediente() {
        return codUsuarioIniciaExpediente;
    }

    /**
     * @param codUsuarioIniciaExpediente the codUsuarioIniciaExpediente to set
     */
    public void setCodUsuarioIniciaExpediente(int codUsuarioIniciaExpediente) {
        this.codUsuarioIniciaExpediente = codUsuarioIniciaExpediente;
    }

    /**
     * Devuelve el código visible de la unidad de inicio del expediente
     * @return the codigoUorVisibleInicioExpediente
     */
    public String getCodigoUorVisibleInicioExpediente() {
        return codigoUorVisibleInicioExpediente;
    }

    /**
     * Establece el código visible de la unidad de inicio del expediente
     * @param codigoUorVisibleInicioExpediente: Código visible de la unidad
     */
    public void setCodigoUorVisibleInicioExpediente(String codigoUorVisibleInicioExpediente) {
        this.codigoUorVisibleInicioExpediente = codigoUorVisibleInicioExpediente;
    }

   /**
     * Devuelve la descripción la descripción de la unidad de inicio del expediente
     * @return String con la descripción
     */
    public String getDescripcionUnidadInicioExpediente() {
        return descripcionUnidadInicioExpediente;
    }

    /**
     * Establece la descripción de la unidad de inicio del expediente
     * @param descripcionUnidadInicioExpediente : String con la descripción
     */
    public void setDescripcionUnidadInicioExpediente(String descripcionUnidadInicioExpediente) {
        this.descripcionUnidadInicioExpediente = descripcionUnidadInicioExpediente;
    }

   
   /**
     * Devuelve el código interno del último trámite cerrado del expediente
     * @return Código interno del trámite
     */
    public int getCodigoTramiteCerrado() {
        return codigoTramiteCerrado;
    }

    /**
     * Establece el código interno del último trámite cerrado del expediente
     * @param codTramiteCerrado the codTramiteCerrado to set
     */
    public void setCodigoTramiteCerrado(int codTramiteCerrado) {
        this.codigoTramiteCerrado = codTramiteCerrado;
    }

    /**
     * @return the ocurrenciaTramiteCerrado
     */
    public int getOcurrenciaTramiteCerrado() {
        return ocurrenciaTramiteCerrado;
    }

    /**
     * @param ocurrenciaTramiteCerrado the ocurrenciaTramiteCerrado to set
     */
    public void setOcurrenciaTramiteCerrado(int ocurrenciaTramiteCerrado) {
        this.ocurrenciaTramiteCerrado = ocurrenciaTramiteCerrado;
    }

    /**
     * Devuelve la descripción del último trámite cerrado
     * @return String
     */
    public String getDescripcionTramiteCerrado() {
        return descripcionTramiteCerrado;
    }

    /**
     * Devuelve la descripción del último trámite cerrado
     * @param descripcionTramiteCerrado the descripcionTramiteCerrado to set
     */
    public void setDescripcionTramiteCerrado(String descripcionTramiteCerrado) {
        this.descripcionTramiteCerrado = descripcionTramiteCerrado;
    }

    /**
     * Devuelve el código visible del último trámite cerrado del expediente
     * @return the codigoTramiteVisibleCerrado
     */
    public int getCodigoTramiteVisibleCerrado() {
        return codigoTramiteVisibleCerrado;
    }

    /**
     * Establece el código visible del último trámite cerrado del expediente
     * @param codigoTramiteVisibleCerrado the codigoTramiteVisibleCerrado to set
     */
    public void setCodigoTramiteVisibleCerrado(int codigoTramiteVisibleCerrado) {
        this.codigoTramiteVisibleCerrado = codigoTramiteVisibleCerrado;
    }

    /**
     * Devuelve los interesados del expediente
     * @return ArrayList<InteresadoExpedienteModuloIntegracionVO> con la colección de interesados del expediente si existen.
     * Sino la colección estará vacía
     */
    public ArrayList<InteresadoExpedienteModuloIntegracionVO> getInteresados() {
        return interesados;
    }

    /**
     * Establece los interesados de un determinado expediente
     * @param interesados: ArrayList<InteresadoExpedienteModuloIntegracionVO>
     */
    public void setInteresados(ArrayList<InteresadoExpedienteModuloIntegracionVO> interesados) {
        this.interesados = interesados;
    }

    /**
     * @return the tipoProcedimiento
     */
    public int getTipoProcedimiento() {
        return tipoProcedimiento;
    }

    /**
     * @param tipoProcedimiento the tipoProcedimiento to set
     */
    public void setTipoProcedimiento(int tipoProcedimiento) {
        this.tipoProcedimiento = tipoProcedimiento;
    }

    /**
     * @return the descripcionTipoProcedimiento
     */
    public String getDescripcionTipoProcedimiento() {
        return descripcionTipoProcedimiento;
    }

    /**
     * @param descripcionTipoProcedimiento the descripcionTipoProcedimiento to set
     */
    public void setDescripcionTipoProcedimiento(String descripcionTipoProcedimiento) {
        this.descripcionTipoProcedimiento = descripcionTipoProcedimiento;
    }
        
}