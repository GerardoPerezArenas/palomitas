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
 * Tabla ADJUNTO_EXT_NOTIFICACION
 */
public class AdjuntoExtNotificacionVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO.
    private int id;  //Se corresponde con el campo ID
    private int codMunicipio;  //Se corresponde con el campo COD_MUNICIPIO
    private String numExpediente = null;  //Se corresponde con el campo NUM_EXPEDIENTE
    private int codTramite; //Se corresponde con el campo COD_TRAMITE
    private int ocurrenciaTramite; //Se corresponde con el campo OCU_TRAMITE
    private String firma = null; //Se corresponde con el campo FIRMA
    private Calendar fecha = null;  //Se corresponde con el campo FECHA
    private byte[] contenido = null; //Se corresponde con el campo CONTENIDO
    private Integer idNotificacion = null; //Se corresponde con el campo ID_NOTIFICACION
    private String plataformaFirma = null; //Se corresponde con el campo PLATAFORMA_FIRMA
    private Integer codUsuarioFirma = null; //Se corresponde con el campo COD_USUARIO_FIRMA
    private String nombre = null; //Se corresponde con el campo NOMBRE
    private String tipoMime = null; //Se corresponde con el campo TIPO_MIME
    private String estadoFirma = null; //Se corresponde con el campo ESTADO_FIRMA
    private Calendar fechaFirma = null; //Se corresponde con el campo FECHA_FIRMA
    private Integer codUsuarioRechazo = null; //Se corresponde con el campo COD_USUARIO_RECHAZO
    private Calendar fechaRechazo = null; //Se corresponde con el campo FECHA_RECHAZO
    private String observacionesRechazo = null; //Se corresponde con el campo OBSERVACIONES_RECHAZO
    private Integer tipoCertificadoFirma = null;  //Se corresponde con el campo TIPO_CERTIFICADO_FIRMA
    
    public AdjuntoExtNotificacionVO()
    {
        
    }

    public long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(long idProceso) {
        this.idProceso = idProceso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(int codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public int getCodTramite() {
        return codTramite;
    }

    public void setCodTramite(int codTramite) {
        this.codTramite = codTramite;
    }

    public int getOcurrenciaTramite() {
        return ocurrenciaTramite;
    }

    public void setOcurrenciaTramite(int ocurrenciaTramite) {
        this.ocurrenciaTramite = ocurrenciaTramite;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    public Integer getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Integer idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getPlataformaFirma() {
        return plataformaFirma;
    }

    public void setPlataformaFirma(String plataformaFirma) {
        this.plataformaFirma = plataformaFirma;
    }

    public Integer getCodUsuarioFirma() {
        return codUsuarioFirma;
    }

    public void setCodUsuarioFirma(Integer codUsuarioFirma) {
        this.codUsuarioFirma = codUsuarioFirma;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public String getEstadoFirma() {
        return estadoFirma;
    }

    public void setEstadoFirma(String estadoFirma) {
        this.estadoFirma = estadoFirma;
    }

    public Calendar getFechaFirma() {
        return fechaFirma;
    }

    public void setFechaFirma(Calendar fechaFirma) {
        this.fechaFirma = fechaFirma;
    }

    public Integer getCodUsuarioRechazo() {
        return codUsuarioRechazo;
    }

    public void setCodUsuarioRechazo(Integer codUsuarioRechazo) {
        this.codUsuarioRechazo = codUsuarioRechazo;
    }

    public Calendar getFechaRechazo() {
        return fechaRechazo;
    }

    public void setFechaRechazo(Calendar fechaRechazo) {
        this.fechaRechazo = fechaRechazo;
    }

    public String getObservacionesRechazo() {
        return observacionesRechazo;
    }

    public void setObservacionesRechazo(String observacionesRechazo) {
        this.observacionesRechazo = observacionesRechazo;
    }

    public Integer getTipoCertificadoFirma() {
        return tipoCertificadoFirma;
    }

    public void setTipoCertificadoFirma(Integer tipoCertificadoFirma) {
        this.tipoCertificadoFirma = tipoCertificadoFirma;
    }
}
