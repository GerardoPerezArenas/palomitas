/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.historico.expediente.vo;

import java.util.Calendar;

/**
 *
 * @author Mila NP
 * 
 * Tabla E_CRD_FIR_FIRMANTES
 */
public class DocumentoTramitacionFirmantesVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO.
    private int codMunicipio; //Se corresponde con el campo COD_MUNICIPIO
    private String codProcedimiento = null; //Se corresponde con el campo COD_PROCEDIMIENTO
    private int ejercicio; //Se corresponde con el campo EJERCICIO
    private String numExpediente = null; //Se corresponde con el campo NUM_EXPEDIENTE
    private int codTramite; //Se corresponde con el campo COD_TRAMITE
    private int ocurrenciaTramite; //Se corresponde con el campo COD_OCURRENCIA
    private int numeroDocumento; //Se corresponde con el campo COD_DOCUMENTO
    private int idUsuario; //Se corresponde con el campo ID_USUARIO
    private int orden; //Se corresponde con el campo ORDEN
    private String estadoFirma = null; //Se corresponde con el campo ESTADO_FIRMA
    private Calendar fechaFirma = null; //Se corresponde con el campo FECHA_FIRMA
    private byte[] firma = null; //Se corresponde con el campo FIRMA
    
    public DocumentoTramitacionFirmantesVO()
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

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

	/**
	 * @return the orden
	 */
	public int getOrden() {
		return orden;
	}

	/**
	 * @param orden the orden to set
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}

	/**
	 * @return the fechaFirma
	 */
	public Calendar getFechaFirma() {
		return fechaFirma;
	}

	/**
	 * @param fechaFirma the fechaFirma to set
	 */
	public void setFechaFirma(Calendar fechaFirma) {
		this.fechaFirma = fechaFirma;
	}
}
