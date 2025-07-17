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
 * Tabla E_DOCS_FIRMAS
 */
public class DocsFirmasVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO
    private int idDocFirma; //Se corresponde con el campo ID_DOC_FIRMA
    private String docFirmaEstado = null; //Se corresponde con el campo DOC_FIRMA_ESTADO
    private int docFirmaOrden; //Se corresponde con el campo DOC_FIRMA_ORDEN
    private Integer docFirmaUor = null; //Se corresponde con el campo DOC_FIRMA_UOR
    private Integer docFirmaCargo = null; //Se corresponde con el campo DOC_FIRMA_CARGO
    private Integer docFirmaUsuario = null; //Se corresponde con el campo DOC_FIRMA_USUARIO
    private Calendar docFirmaFecha = null; //Se corresponde con el campo DOC_FIRMA_FECHA
    private int idDocPresentado; //Se corresponde con el campo ID_DOC_PRESENTADO
    private Calendar docFechaEnvio = null; //Se corresponde con el campo DOC_FECHA_ENVIO
    private byte[] firma = null;//Se corresponde con el campo FIRMA
    private String observaciones = null; //Se corresponde con el campo DOC_FIRMA_OBSERVACIONES
    
    public DocsFirmasVO()
    {
        
    }

    public long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(long idProceso) {
        this.idProceso = idProceso;
    }

    public int getIdDocFirma() {
        return idDocFirma;
    }

    public void setIdDocFirma(int idDocFirma) {
        this.idDocFirma = idDocFirma;
    }

    public String getDocFirmaEstado() {
        return docFirmaEstado;
    }

    public void setDocFirmaEstado(String docFirmaEstado) {
        this.docFirmaEstado = docFirmaEstado;
    }

    public int getDocFirmaOrden() {
        return docFirmaOrden;
    }

    public void setDocFirmaOrden(int docFirmaOrden) {
        this.docFirmaOrden = docFirmaOrden;
    }

    public Integer getDocFirmaUor() {
        return docFirmaUor;
    }

    public void setDocFirmaUor(Integer docFirmaUor) {
        this.docFirmaUor = docFirmaUor;
    }

    public Integer getDocFirmaCargo() {
        return docFirmaCargo;
    }

    public void setDocFirmaCargo(Integer docFirmaCargo) {
        this.docFirmaCargo = docFirmaCargo;
    }

    public Integer getDocFirmaUsuario() {
        return docFirmaUsuario;
    }

    public void setDocFirmaUsuario(Integer docFirmaUsuario) {
        this.docFirmaUsuario = docFirmaUsuario;
    }

    public Calendar getDocFirmaFecha() {
        return docFirmaFecha;
    }

    public void setDocFirmaFecha(Calendar docFirmaFecha) {
        this.docFirmaFecha = docFirmaFecha;
    }

    public int getIdDocPresentado() {
        return idDocPresentado;
    }

    public void setIdDocPresentado(int idDocPresentado) {
        this.idDocPresentado = idDocPresentado;
    }

    public Calendar getDocFechaEnvio() {
        return docFechaEnvio;
    }

    public void setDocFechaEnvio(Calendar docFechaEnvio) {
        this.docFechaEnvio = docFechaEnvio;
    }

    public byte[] getFirma() {
        return firma;
    }

    public void setFirma(byte[] firma) {
        this.firma = firma;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
