/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.historico.expediente.vo;

import java.util.Calendar;

/**
 *
 * @author santiagoc
 * 
 * Tabla E_EXP
 */
public class ExpedienteVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO.
    private String codProcedimiento = null; //Se corresponde con el campo EXP_PRO. 
    private int ejercicio; //Se corresponde con el campo EXP_EJE.
    private String numExpediente = null; //Se corresponde con el campo EXP_NUM.
    private Calendar fechaInicio = null; //Se corresponde con el campo EXP_FEI.
    private Calendar fechaFin = null; //Se corresponde con el campo EXP_FEF
    private int estado; //Se corresponde con el campo EXP_EST
    private int codOrganizacion; //Se corresponde con el campo EXP_MUN
    private Integer codUsuario = null; //Se corresponde con el campo EXP_USU
    private int codUorInicio; //Se corresponde con el campo EXP_UOR
    private Calendar fechaPendiente = null; //Se corresponde con el campo EXP_PEND
    private int codTramitePendiente; //Se corresponde con el campo EXP_TRA
    private Integer ocurrenciaTramitePendiente = null; //Se corresponde con el campo EXP_TOCU
    private String localizacion = null; //Se corresponde con el campo EXP_LOC
    private Integer codLocalizacion = null; //Se corresponde con el campo EXP_CLO
    private String observaciones = null; //Se corresponde con el campo EXP_OBS
    private String asunto = null; //Se corresponde con el campo EXP_ASU
    private String referencia = null; //Se corresponde con el campo EXP_REF
    private String importante = null; //Se corresponde con el campo EXP_IMP
    private String ubicacionDocumentacion = null; //Se corresponde con el campo EXP_UBICACION_DOC
    
    public ExpedienteVO()
    {
        
    }

    public long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(long idProceso) {
        this.idProceso = idProceso;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public int getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public Calendar getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Calendar getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Calendar fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public Integer getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(Integer codUsuario) {
        this.codUsuario = codUsuario;
    }

    public int getCodUorInicio() {
        return codUorInicio;
    }

    public void setCodUorInicio(int codUorInicio) {
        this.codUorInicio = codUorInicio;
    }

    public Calendar getFechaPendiente() {
        return fechaPendiente;
    }

    public void setFechaPendiente(Calendar fechaPendiente) {
        this.fechaPendiente = fechaPendiente;
    }

    public int getCodTramitePendiente() {
        return codTramitePendiente;
    }

    public void setCodTramitePendiente(int codTramitePendiente) {
        this.codTramitePendiente = codTramitePendiente;
    }

    public Integer getOcurrenciaTramitePendiente() {
        return ocurrenciaTramitePendiente;
    }

    public void setOcurrenciaTramitePendiente(Integer ocurrenciaTramitePendiente) {
        this.ocurrenciaTramitePendiente = ocurrenciaTramitePendiente;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public Integer getCodLocalizacion() {
        return codLocalizacion;
    }

    public void setCodLocalizacion(Integer codLocalizacion) {
        this.codLocalizacion = codLocalizacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getImportante() {
        return importante;
    }

    public void setImportante(String importante) {
        this.importante = importante;
    }

    /**
     * @return the ubicacionDocumentacion
     */
    public String getUbicacionDocumentacion() {
        return ubicacionDocumentacion;
    }

    /**
     * @param ubicacionDocumentacion the ubicacionDocumentacion to set
     */
    public void setUbicacionDocumentacion(String ubicacionDocumentacion) {
        this.ubicacionDocumentacion = ubicacionDocumentacion;
    }
}
