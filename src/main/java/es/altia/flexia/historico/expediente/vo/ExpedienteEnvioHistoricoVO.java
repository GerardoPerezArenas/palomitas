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
 * Tabla EXP_ENVIO_HISTORICO
 */
public class ExpedienteEnvioHistoricoVO
{     
    private long id; //Se corresponde con el campo ID.
    private int codOrganizacion; //Se corresponde con el campo COD_ORGANIZACION
    private String numExpediente = null; //Se corresponde con el campo NUM_EXPEDIENTE
    private Calendar fecEnvArchivo = null; //Se corresponde con el campo FECHA_ENVIO_ARCHIVO
    private int procesado; //Se corresponde con el campo PROCESADO
    private Calendar fechaProceso = null; //Se corresponde con el campo FECHA_PROCESADO
    private String errorProceso = null; //Se corresponde con el campo ERROR_PROCESO
    private int codUsuario; //Se corresponde con el campo COD_USUARIO
    private Calendar fechaRecuperacion = null; //Se corresponde con el campo FECHA_RECUPERACION
    private int codUsuarioRecuperacion; //Se corresponde con el campo COD_USUARIO_RECUP

    public Calendar getFechaRecuperacion() {
        return fechaRecuperacion;
    }

    public void setFechaRecuperacion(Calendar fechaRecuperacion) {
        this.fechaRecuperacion = fechaRecuperacion;
    }

    public int getCodUsuarioRecuperacion() {
        return codUsuarioRecuperacion;
    }

    public void setCodUsuarioRecuperacion(int codUsuarioRecuperacion) {
        this.codUsuarioRecuperacion = codUsuarioRecuperacion;
    }
    
    public ExpedienteEnvioHistoricoVO()
    {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public Calendar getFecEnvArchivo() {
        return fecEnvArchivo;
    }

    public void setFecEnvArchivo(Calendar fecEnvArchivo) {
        this.fecEnvArchivo = fecEnvArchivo;
    }

    public int getProcesado() {
        return procesado;
    }

    public void setProcesado(int procesado) {
        this.procesado = procesado;
    }

    public Calendar getFechaProceso() {
        return fechaProceso;
    }

    public void setFechaProceso(Calendar fechaProceso) {
        this.fechaProceso = fechaProceso;
    }

    public String getErrorProceso() {
        return errorProceso;
    }

    public void setErrorProceso(String errorProceso) {
        this.errorProceso = errorProceso;
    }

    public int getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(int codUsuario) {
        this.codUsuario = codUsuario;
    }
}
