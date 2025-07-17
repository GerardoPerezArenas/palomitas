/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.historico.expediente.vo;

/**
 *
 * @author santiagoc
 * 
 * Tabla ADJUNTO_NOTIFICACION
 */
public class AdjuntoNotificacionVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO.
    private int codNotificacion; //Se corresponde con el campo CODIGO_NOTIFICACION
    private int codMunicipio; //Se corresponde con el campo COD_MUNICIPIO
    private int ejercicio; //Se corresponde con el campo EJERCICIO
    private String numExpediente = null; //Se corresponde con el campo NUM_EXPEDIENTE
    private String codProcedimiento = null; //Se corresponde con el campo COD_PROCEDIMIENTO
    private int codTramite; //Se corresponde con el campo COD_TRAMITE
    private int ocurrenciaTramite; //Se corresponde con el campo OCU_TRAMITE
    private int numUnidadDocumento; //Se corresponde con el campo NUM_UNIDAD_DOC
    
    public AdjuntoNotificacionVO()
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

    public int getNumUnidadDocumento() {
        return numUnidadDocumento;
    }

    public void setNumUnidadDocumento(int numUnidadDocumento) {
        this.numUnidadDocumento = numUnidadDocumento;
    }
}
