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
 * Tabla E_CRD_FIR
 */
public class DocumentoTramitacionFirmaVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO.
    private int codMunicipio; //Se corresponde con el campo CRD_MUN
    private String codProcedimiento = null; //Se corresponde con el campo CRD_PRO
    private int ejercicio; //Se corresponde con el campo CRD_EJE
    private String numExpediente = null; //Se corresponde con el campo CRD_NUM
    private int codTramite; //Se corresponde con el campo CRD_TRA
    private int ocurrenciaTramite; //Se corresponde con el campo CRD_OCU
    private int numeroDocumento; //Se corresponde con el campo CRD_NUD
    private int codUsuario; //Se corresponde con el campo USU_COD
    private String estadoFirma = null; //Se corresponde con el campo FIR_EST
    private byte[] firma = null; //Se corresponde con el campo FIR
    private Calendar fxFirma = null; //Se corresponde con el campo FX_FIRMA
    private String observaciones = null; //Se corresponde con el campo OBSERV
    private Integer codUsuarioFirma = null; //Se corresponde con el campo USU_FIR
    private String pfExt = null; //Se corresponde con el campo CRD_COD_PF_EXT
    private String solPfExt = null; //Se corresponde con el campo CRD_ID_SOL_PF_EXT
    
    public DocumentoTramitacionFirmaVO()
    {
        
    }

    public long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(long idProceso) {
        this.idProceso = idProceso;
    }

    public int getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(int codMunicipio) {
        this.codMunicipio = codMunicipio;
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

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public int getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(int codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getEstadoFirma() {
        return estadoFirma;
    }

    public void setEstadoFirma(String estadoFirma) {
        this.estadoFirma = estadoFirma;
    }

    public byte[] getFirma() {
        return firma;
    }

    public void setFirma(byte[] firma) {
        this.firma = firma;
    }

    public Calendar getFxFirma() {
        return fxFirma;
    }

    public void setFxFirma(Calendar fxFirma) {
        this.fxFirma = fxFirma;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getCodUsuarioFirma() {
        return codUsuarioFirma;
    }

    public void setCodUsuarioFirma(Integer codUsuarioFirma) {
        this.codUsuarioFirma = codUsuarioFirma;
    }

    public String getPfExt() {
        return pfExt;
    }

    public void setPfExt(String pfExt) {
        this.pfExt = pfExt;
    }

    public String getSolPfExt() {
        return solPfExt;
    }

    public void setSolPfExt(String solPfExt) {
        this.solPfExt = solPfExt;
    }
}
