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
 * Tabla NOTIFICACION
 */
public class NotificacionVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO.
    private int codNotificacion; //Se corresponde con el campo CODIGO_NOTIFICACION
    private String numExpediente = null; //Se corresponde con el campo NUM_EXPEDIENTE
    private String codProcedimiento = null; //Se corresponde con el campo COD_PROCEDIMIENTO
    private int ejercicio; //Se corresponde con el campo EJERCICIO
    private int codMunicipio; //Se corresponde con el campo COD_MUNICIPIO
    private int codTramite; //Se corresponde con el campo COD_TRAMITE
    private int ocurrenciaTramite; //Se corresponde con el campo OCU_TRAMITE
    private String actoNotificado = null; //Se corresponde con el campo ACTO_NOTIFICADO
    private Integer caducidadNotificacion = null; //Se corresponde con el campo CADUCIDAD_NOTIFICACION
    private byte[] firma = null; //Se corresponde con el campo FIRMA
    private String textoNotificacion = null; //Se corresponde con el campo TEXTO_NOTIFICACION
    private String firmada = null; //Se corresponde con el campo FIRMADA
    private String xmlNotificacion = null; //Se corresponde con el campo XML_NOTIFICACION
    private Calendar fechaEnvio = null; //Se corresponde con el campo FECHA_ENVIO
    private String numRegistroTelematico = null; //Se corresponde con el campo REGISTRO_RT
    private Calendar fechaAcuse = null; //Se corresponde con el campo FECHA_ACUSE
    private String resultado = null; //Se corresponde con el campo RESULTADO
    private ArrayList<AdjuntoNotificacionVO> adjuntos = null; //Lista de adjuntos asociados a la notificación obtenidos de la tabla ADJUNTO_NOTIFICACION.
    private ArrayList<AutorizadoNotificacionVO> autorizados = null; //Lista de interesados a los que se envía la notificación. Se recuperan de la tabla AUTORIZADO_NOTIFICACION
    private ArrayList<AdjuntoExtNotificacionVO> adjuntosExternos = null;//Lista de adjuntos externos asociados a la notificación obtenidos de la tabla ADJUNTO_EXT_NOTIFICACION.
    
    public NotificacionVO()
    {
        
    }

    public long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(long idProceso) {
        this.idProceso = idProceso;
    }

    public int getCodNotificacion() {
        return codNotificacion;
    }

    public void setCodNotificacion(int codNotificacion) {
        this.codNotificacion = codNotificacion;
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

    public int getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    public int getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(int codMunicipio) {
        this.codMunicipio = codMunicipio;
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

    public String getActoNotificado() {
        return actoNotificado;
    }

    public void setActoNotificado(String actoNotificado) {
        this.actoNotificado = actoNotificado;
    }

    public Integer getCaducidadNotificacion() {
        return caducidadNotificacion;
    }

    public void setCaducidadNotificacion(Integer caducidadNotificacion) {
        this.caducidadNotificacion = caducidadNotificacion;
    }

    public byte[] getFirma() {
        return firma;
    }

    public void setFirma(byte[] firma) {
        this.firma = firma;
    }

    public String getTextoNotificacion() {
        return textoNotificacion;
    }

    public void setTextoNotificacion(String textoNotificacion) {
        this.textoNotificacion = textoNotificacion;
    }

    public String getFirmada() {
        return firmada;
    }

    public void setFirmada(String firmada) {
        this.firmada = firmada;
    }

    public String getXmlNotificacion() {
        return xmlNotificacion;
    }

    public void setXmlNotificacion(String xmlNotificacion) {
        this.xmlNotificacion = xmlNotificacion;
    }

    public Calendar getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Calendar fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getNumRegistroTelematico() {
        return numRegistroTelematico;
    }

    public void setNumRegistroTelematico(String numRegistroTelematico) {
        this.numRegistroTelematico = numRegistroTelematico;
    }

    public Calendar getFechaAcuse() {
        return fechaAcuse;
    }

    public void setFechaAcuse(Calendar fechaAcuse) {
        this.fechaAcuse = fechaAcuse;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public ArrayList<AdjuntoNotificacionVO> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(ArrayList<AdjuntoNotificacionVO> adjuntos) {
        this.adjuntos = adjuntos;
    }

    public ArrayList<AutorizadoNotificacionVO> getAutorizados() {
        return autorizados;
    }

    public void setAutorizados(ArrayList<AutorizadoNotificacionVO> autorizados) {
        this.autorizados = autorizados;
    }

    public ArrayList<AdjuntoExtNotificacionVO> getAdjuntosExternos() {
        return adjuntosExternos;
    }

    public void setAdjuntosExternos(ArrayList<AdjuntoExtNotificacionVO> adjuntosExternos) {
        this.adjuntosExternos = adjuntosExternos;
    }
}
