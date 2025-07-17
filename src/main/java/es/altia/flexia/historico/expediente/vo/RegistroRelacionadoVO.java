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
 * Tabla E_EXR
 */
public class RegistroRelacionadoVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO.
    private int codDepartamentoRegistro; //Se corresponde con el campo EXR_DEP
    private int codUnidadRegistro; //Se corresponde con el campo EXR_UOR
    private String tipoAnotacionRegistro = null; //Se corresponde con el campo EXR_TIP
    private String ejercicioAnotacionRegistro = null; //Se corresponde con el campo EXR_EJR
    private int numAnotacionRegistro; //Se corresponde con el campo EXR_NRE
    private int origenAnotacionRegistro; //Se corresponde con el campo EXR_ORI
    private int ejercicioExpediente; //Se corresponde con el campo EXR_EJE
    private String numExpediente = null; //Se corresponde con el campo EXR_NUM
    private String tipoOperacion = null; //Se corresponde con el campo EXR_TOP
    private int codMunicipioExpediente; //Se corresponde con el campo EXR_MUN
    private String codProcedimiento = null; //Se corresponde con el campo EXR_PRO
    private String origenExpediente = null; //Se corresponde con el campo EXR_ORIGEN
    
    public RegistroRelacionadoVO()
    {
        
    }

    public long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(long idProceso) {
        this.idProceso = idProceso;
    }

    public int getCodDepartamentoRegistro() {
        return codDepartamentoRegistro;
    }

    public void setCodDepartamentoRegistro(int codDepartamentoRegistro) {
        this.codDepartamentoRegistro = codDepartamentoRegistro;
    }

    public int getCodUnidadRegistro() {
        return codUnidadRegistro;
    }

    public void setCodUnidadRegistro(int codUnidadRegistro) {
        this.codUnidadRegistro = codUnidadRegistro;
    }

    public String getTipoAnotacionRegistro() {
        return tipoAnotacionRegistro;
    }

    public void setTipoAnotacionRegistro(String tipoAnotacionRegistro) {
        this.tipoAnotacionRegistro = tipoAnotacionRegistro;
    }

    public String getEjercicioAnotacionRegistro() {
        return ejercicioAnotacionRegistro;
    }

    public void setEjercicioAnotacionRegistro(String ejercicioAnotacionRegistro) {
        this.ejercicioAnotacionRegistro = ejercicioAnotacionRegistro;
    }

    public int getNumAnotacionRegistro() {
        return numAnotacionRegistro;
    }

    public void setNumAnotacionRegistro(int numAnotacionRegistro) {
        this.numAnotacionRegistro = numAnotacionRegistro;
    }

    public int getOrigenAnotacionRegistro() {
        return origenAnotacionRegistro;
    }

    public void setOrigenAnotacionRegistro(int origenAnotacionRegistro) {
        this.origenAnotacionRegistro = origenAnotacionRegistro;
    }

    public int getEjercicioExpediente() {
        return ejercicioExpediente;
    }

    public void setEjercicioExpediente(int ejercicioExpediente) {
        this.ejercicioExpediente = ejercicioExpediente;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public int getCodMunicipioExpediente() {
        return codMunicipioExpediente;
    }

    public void setCodMunicipioExpediente(int codMunicipioExpediente) {
        this.codMunicipioExpediente = codMunicipioExpediente;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public String getOrigenExpediente() {
        return origenExpediente;
    }

    public void setOrigenExpediente(String origenExpediente) {
        this.origenExpediente = origenExpediente;
    }
}
