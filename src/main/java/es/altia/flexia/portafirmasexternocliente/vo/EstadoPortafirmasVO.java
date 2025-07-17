/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.portafirmasexternocliente.vo;

import java.util.Calendar;

/**
 *
 * @author jesus.cordoba-perez
 */
public class EstadoPortafirmasVO {
    
    private Long idEstadoPortafirmas;
    private String documentoExtension;
    private String taskOid;
    private String tipoPlatea;
    private String subTipoPlatea;
    private Integer estado;
    private String buzon;
    private String firmaInformante;
    private Calendar  fechaPeticion;
    private Calendar fechaActualizacion;

    public Long getIdEstadoPortafirmas() {
        return idEstadoPortafirmas;
    }

    public void setIdEstadoPortafirmas(Long idEstadoPortafirmas) {
        this.idEstadoPortafirmas = idEstadoPortafirmas;
    }

    public String getDocumentoExtension() {
        return documentoExtension;
    }

    public void setDocumentoExtension(String documentoExtension) {
        this.documentoExtension = documentoExtension;
    }

    public String getTaskOid() {
        return taskOid;
    }

    public void setTaskOid(String taskOid) {
        this.taskOid = taskOid;
    }

    public String getTipoPlatea() {
        return tipoPlatea;
    }

    public void setTipoPlatea(String tipoPlatea) {
        this.tipoPlatea = tipoPlatea;
    }

    public String getSubTipoPlatea() {
        return subTipoPlatea;
    }

    public void setSubTipoPlatea(String subTipoPlatea) {
        this.subTipoPlatea = subTipoPlatea;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getBuzon() {
        return buzon;
    }

    public void setBuzon(String buzon) {
        this.buzon = buzon;
    }

    public String getFirmaInformante() {
        return firmaInformante;
    }

    public void setFirmaInformante(String firmaInformante) {
        this.firmaInformante = firmaInformante;
    }

    public Calendar getFechaPeticion() {
        return fechaPeticion;
    }

    public void setFechaPeticion(Calendar fechaPeticion) {
        this.fechaPeticion = fechaPeticion;
    }

    public Calendar getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Calendar fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
}
