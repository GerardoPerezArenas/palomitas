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
 * Tabla E_CRO
 */
public class CronoVO
{
    private long idProceso;									//Se corresponde con el campo ID_PROCESO
    private String codProcedimiento = null;					//Se corresponde con el campo CRO_PRO
    private int ejercicio;									//Se corresponde con el campo CRO_EJE
    private int codTramite;									//Se corresponde con el campo CRO_TRA
    private String numExpediente = null;					//Se corresponde con el campo CRO_NUM
    private Calendar fechaInicio = null;					//Se corresponde con el campo CRO_FEI
    private Calendar fechaFin = null;						//Se corresponde con el campo CRO_FEF
    private int codUsuario;									//Se corresponde con el campo CRO_USU
    private int ocurrenciaTramite;							//Se corresponde con el campo CRO_OCU
    private int codUorTramitadora;							//Se corresponde con el campo CRO_UTR
    private int codOrganizacion;							//Se corresponde con el campo CRO_MUN
    private Calendar fechaInicioPlazo = null;				//Se corresponde con el campo CRO_FIP
    private Calendar fechaLimite = null;					//Se corresponde con el campo CRO_FLI
    private Calendar fechaFinPlazo = null;					//Se corresponde con el campo CRO_FFP
    private Integer reserva = null;							//Se corresponde con el campo CRO_RES
    private String observaciones = null;					//Se corresponde con el campo CRO_OBS
    private Integer usuarioFinalizacion = null;				//Se corresponde con el campo CRO_USF
    private Integer avisoCercanaFinPlazo = null;			//Se corresponde con el campo CRO_AVISADOCFP
    private Integer avisoFinPlazo = null;					//Se corresponde con el campo CRO_AVISADOFDP
    
    public CronoVO()
    {
        
    }

    public long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(long idProceso) {
        this.idProceso = idProceso;
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

    public int getCodTramite() {
        return codTramite;
    }

    public void setCodTramite(int codTramite) {
        this.codTramite = codTramite;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public Calendar getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Calendar getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Calendar fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(int codUsuario) {
        this.codUsuario = codUsuario;
    }

    public int getOcurrenciaTramite() {
        return ocurrenciaTramite;
    }

    public void setOcurrenciaTramite(int ocurrenciaTramite) {
        this.ocurrenciaTramite = ocurrenciaTramite;
    }

    public int getCodUorTramitadora() {
        return codUorTramitadora;
    }

    public void setCodUorTramitadora(int codUorTramitadora) {
        this.codUorTramitadora = codUorTramitadora;
    }

    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public Calendar getFechaInicioPlazo() {
        return fechaInicioPlazo;
    }

    public void setFechaInicioPlazo(Calendar fechaInicioPlazo) {
        this.fechaInicioPlazo = fechaInicioPlazo;
    }

    public Calendar getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Calendar fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Calendar getFechaFinPlazo() {
        return fechaFinPlazo;
    }

    public void setFechaFinPlazo(Calendar fechaFinPlazo) {
        this.fechaFinPlazo = fechaFinPlazo;
    }

    public Integer getReserva() {
        return reserva;
    }

    public void setReserva(Integer reserva) {
        this.reserva = reserva;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getUsuarioFinalizacion() {
        return usuarioFinalizacion;
    }

    public void setUsuarioFinalizacion(Integer usuarioFinalizacion) {
        this.usuarioFinalizacion = usuarioFinalizacion;
    }

    public Integer getAvisoCercanaFinPlazo() {
        return avisoCercanaFinPlazo;
    }

    public void setAvisoCercanaFinPlazo(Integer avisoCercanaFinPlazo) {
        this.avisoCercanaFinPlazo = avisoCercanaFinPlazo;
    }

    public Integer getAvisoFinPlazo() {
        return avisoFinPlazo;
    }

    public void setAvisoFinPlazo(Integer avisoFinPlazo) {
        this.avisoFinPlazo = avisoFinPlazo;
    }
}
