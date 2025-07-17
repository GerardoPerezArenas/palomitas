/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.historico.expediente.vo;

/**
 *
 * @author Mila NP
 * 
 * Tabla E_CRD_FIR_FLUJO
 */
public class DocumentoTramitacionFlujoVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO.
    private int codMunicipio; //Se corresponde con el campo COD_MUNICIPIO
    private String codProcedimiento = null; //Se corresponde con el campo COD_PROCEDIMIENTO
    private int ejercicio; //Se corresponde con el campo EJERCICIO
    private String numExpediente = null; //Se corresponde con el campo NUM_EXPEDIENTE
    private int codTramite; //Se corresponde con el campo COD_TRAMITE
    private int ocurrenciaTramite; //Se corresponde con el campo COD_OCURRENCIA
    private int numeroDocumento; //Se corresponde con el campo COD_DOCUMENTO
    private long idTipoFirma; //Se corresponde con el campo ID_TIPO_FIRMA
    
    public DocumentoTramitacionFlujoVO()
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

	/**
	 * @return the idTipoFirma
	 */
	public long getIdTipoFirma() {
		return idTipoFirma;
	}

	/**
	 * @param idTipoFirma the idTipoFirma to set
	 */
	public void setIdTipoFirma(long idTipoFirma) {
		this.idTipoFirma = idTipoFirma;
	}
}
