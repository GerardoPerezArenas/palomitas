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
 * Tabla E_CRD
 */
public class DocumentoTramitacionVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO.
    private int codMunicipio; //Se corresponde con el campo CRD_MUN
    private String codProcedimiento = null; //Se corresponde con el campo CRD_PRO
    private int ejercicio; //Se corresponde con el campo CRD_EJE
    private String numExpediente = null; //Se corresponde con el campo CRD_NUM
    private int codTramite; //Se corresponde con el campo CRD_TRA
    private int ocurrenciaTramite; //Se corresponde con el campo CRD_OCU
    private int numDocumento; //Se corresponde con el campo CRD_NUD
    private Calendar fechaAlta = null; //Se corresponde con el campo CRD_FAL
    private Calendar fechaModificacion = null; //Se corresponde con el campo CRD_FMO
    private int codUsuarioAlta; //Se corresponde con el campo CRD_USC
    private Integer codUsuarioModificacion = null; //Se corresponde con el campo CRD_USM
    private byte[] contenido = null; //Se corresponde con el campo CRD_FIL
    private String nombreDocumento = null; //Se corresponde con el campo CRD_DES
    private int codDocumento; //Se corresponde con el campo CRD_DOT
    private String estadoFirma = null; //Se corresponde con el campo CRD_FIR_EST
    private String fd = null; //Se corresponde con el campo CRD_EXP_FD
    private String docFd = null; //Se corresponde con el campo CRD_DOC_FD
    private Integer firFd = null; //Se corresponde con el campo CRD_FIR_FD
    private Calendar fechaInforme = null; //Se corresponde con el campo CRD_FINF
    private ArrayList<DocumentoTramitacionFirmaVO> firmas = null; //Lista de firmas asociadas al documento de tramitación
    private ArrayList<DocumentoTramitacionFirmantesVO> usuariosFirmantes = null; //Lista de usuarios firmantes asociados al documento de tramitación
    private ArrayList<DocumentoTramitacionFlujoVO> firmaFlujo = null; //Flujos de firmas del documento de tramitación

    public DocumentoTramitacionVO()
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

    public int getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(int numDocumento) {
        this.numDocumento = numDocumento;
    }

    public Calendar getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Calendar fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Calendar getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Calendar fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
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

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public int getCodDocumento() {
        return codDocumento;
    }

    public void setCodDocumento(int codDocumento) {
        this.codDocumento = codDocumento;
    }

    public String getEstadoFirma() {
        return estadoFirma;
    }

    public void setEstadoFirma(String estadoFirma) {
        this.estadoFirma = estadoFirma;
    }

    public String getFd() {
        return fd;
    }

    public void setFd(String fd) {
        this.fd = fd;
    }

    public String getDocFd() {
        return docFd;
    }

    public void setDocFd(String docFd) {
        this.docFd = docFd;
    }

    public Integer getFirFd() {
        return firFd;
    }

    public void setFirFd(Integer firFd) {
        this.firFd = firFd;
    }

    public Calendar getFechaInforme() {
        return fechaInforme;
    }

    public void setFechaInforme(Calendar fechaInforme) {
        this.fechaInforme = fechaInforme;
    }
    
    public ArrayList<DocumentoTramitacionFirmaVO> getFirmas() {
        return firmas;
    }

    public void setFirmas(ArrayList<DocumentoTramitacionFirmaVO> firmas) {
        this.firmas = firmas;
    }

	/**
	 * @return the usuariosFirmantes
	 */
	public ArrayList<DocumentoTramitacionFirmantesVO> getUsuariosFirmantes() {
		return usuariosFirmantes;
	}

	/**
	 * @param usuariosFirmantes the usuariosFirmantes to set
	 */
	public void setUsuariosFirmantes(ArrayList<DocumentoTramitacionFirmantesVO> usuariosFirmantes) {
		this.usuariosFirmantes = usuariosFirmantes;
	}

	/**
	 * @return the firmaFlujo
	 */
	public ArrayList<DocumentoTramitacionFlujoVO> getFirmaFlujo() {
		return firmaFlujo;
	}

	/**
	 * @param firmaFlujo the firmaFlujo to set
	 */
	public void setFirmaFlujo(ArrayList<DocumentoTramitacionFlujoVO> firmaFlujo) {
		this.firmaFlujo = firmaFlujo;
	}
    
}
