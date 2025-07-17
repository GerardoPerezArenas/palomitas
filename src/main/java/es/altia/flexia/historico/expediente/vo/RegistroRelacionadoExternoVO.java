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
 * Tabla E_EXREXT
 */
public class RegistroRelacionadoExternoVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO.
    private int codUnidadRegistro; //Se corresponde con el campo EXREXT_UOR
    private String tipoAnotacionRegistro = null; //Se corresponde con el campo EXREXT_TIP
    private String ejercicioAnotacionRegistro = null; //Se corresponde con el campo EXREXT_EJR
    private long numAnotacionRegistro; //Se corresponde con el campo EXREXT_NRE
    private int codMunicipioRegistro; //Se corresponde con el campo EXREXT_MUN
    private String numExpediente = null; //Se corresponde con el campo EXREXT_NUM
    private String origenAnotacionRegistro = null; //Se corresponde con el campo EXREXT_ORI
    private String tipoOperacion = null; //Se corresponde con el campo EXREXT_TOP
    private String serie = null; //Se corresponde con el campo EXREXT_SER
    private String codProcedimiento = null; //Se corresponde con el campo EXREXT_PRO
    private Calendar fechaAlta = null; //Se corresponde con el campo EXREXT_FECALTA    
    
    public RegistroRelacionadoExternoVO()
    {
        
    }

    public long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(long idProceso) {
        this.idProceso = idProceso;
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

    public long getNumAnotacionRegistro() {
        return numAnotacionRegistro;
    }

    public void setNumAnotacionRegistro(long numAnotacionRegistro) {
        this.numAnotacionRegistro = numAnotacionRegistro;
    }

    public int getCodMunicipioRegistro() {
        return codMunicipioRegistro;
    }

    public void setCodMunicipioRegistro(int codMunicipioRegistro) {
        this.codMunicipioRegistro = codMunicipioRegistro;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public String getOrigenAnotacionRegistro() {
        return origenAnotacionRegistro;
    }

    public void setOrigenAnotacionRegistro(String origenAnotacionRegistro) {
        this.origenAnotacionRegistro = origenAnotacionRegistro;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public Calendar getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Calendar fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
}
