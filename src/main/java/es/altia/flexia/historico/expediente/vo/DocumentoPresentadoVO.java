/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.historico.expediente.vo;

import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author santiagoc
 * 
 * Tabla E_DOCS_PRESENTADOS
 */
public class DocumentoPresentadoVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO
    private int codPresentado; //Se corresponde con el campo PRESENTADO_COD
    private int codMunicipio; //Se corresponde con el campo PRESENTADO_MUN
    private int ejercicio; //Se corresponde con el campo PRESENTADO_EJE
    private String numExpediente = null; //Se corresponde con el campo PRESENTADO_NUM
    private String codProcedimiento = null; //Se corresponde con el campo PRESENTADO_PRO
    private int codDocumentoPresentado; //Se corresponde con el campo PRESENTADO_COD_DOC
    private byte[] contenido = null; //Se corresponde con el campo PRESENTADO_CONTENIDO
    private String tipoMime = null; //Se corresponde con el campo PRESENTADO_TIPO
    private String extension = null; //Se corresponde con el campo PRESENTADO_EXTENSION
    private String origen = null; //Se corresponde con el campo PRESENTADO_ORIGEN
    private Calendar fechaAlta = null; //Se corresponde con el campo PRESENTADO_FECHA_ALTA
    private String nombreDocumento = null; //Se corresponde con el campo PRESENTADO_NOMBRE
    private int codUsuarioAlta; //Se corresponde con el campo PRESENTADO_COD_USU_ALTA
    private Integer codUsuarioModificacion = null; //Se corresponde con el campo PRESENTADO_COD_USU_MOD
    private Calendar fechaModificacion = null; //Se corresponde con el campo PRESENTADO_FECHA_MOD
    private ArrayList<DocsFirmasVO> firmas = null; //Contiene la lista de firmas del documento presentado. Puede existir más de una firma para un documento, puesto que este puede tener un circuito de firmas.
            
    public DocumentoPresentadoVO()
    {

    }

    public long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(long idProceso) {
        this.idProceso = idProceso;
    }

    public int getCodPresentado() {
        return codPresentado;
    }

    public void setCodPresentado(int codPresentado) {
        this.codPresentado = codPresentado;
    }

    public int getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(int codMunicipio) {
        this.codMunicipio = codMunicipio;
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

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public int getCodDocumentoPresentado() {
        return codDocumentoPresentado;
    }

    public void setCodDocumentoPresentado(int codDocumentoPresentado) {
        this.codDocumentoPresentado = codDocumentoPresentado;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public Calendar getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Calendar fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public int getCodUsuarioAlta() {
        return codUsuarioAlta;
    }

    public void setCodUsuarioAlta(int codUsuarioAlta) {
        this.codUsuarioAlta = codUsuarioAlta;
    }

    public Integer getCodUsuarioModificacion() {
        return codUsuarioModificacion;
    }

    public void setCodUsuarioModificacion(Integer codUsuarioModificacion) {
        this.codUsuarioModificacion = codUsuarioModificacion;
    }

    public Calendar getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Calendar fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public ArrayList<DocsFirmasVO> getFirmas() {
        return firmas;
    }

    public void setFirmas(ArrayList<DocsFirmasVO> firmas) {
        this.firmas = firmas;
    }
}
