package es.altia.flexia.interfaces.user.web.carga.parcial.fichaexpediente.vo;

import es.altia.agora.business.sge.AsientoFichaExpedienteVO;
import es.altia.agora.business.sge.DefinicionAgrupacionCamposAgrupadosValueObject;
import es.altia.agora.business.sge.DocumentoInicioExpedienteVO;
import es.altia.agora.business.sge.FicheroVO;
import es.altia.flexia.business.expediente.vo.EnlaceVO;
import java.util.ArrayList;

public class ResultadoTratamientoCargaParcialExpedienteVO {

    private int status;
    private String descStatus;   
    private ArrayList<EnlaceVO> enlaces = null;
    private ArrayList<AsientoFichaExpedienteVO> asientos = null;
    private ArrayList<String> errores = null;
    private ArrayList<DocumentoInicioExpedienteVO> documentos = null;
    private ArrayList<FicheroVO> documentosAportados = null;
    //private ArrayList<EstructuraCampo> estructuraDatosSuplementarios = null;
    //private ArrayList<CamposFormulario> valoresDatosSuplementarios   = null;   
    private ArrayList<DefinicionAgrupacionCamposAgrupadosValueObject> agrupaciones = null;
    private int posicionarCamposSuplementariosExpediente;

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the descStatus
     */
    public String getDescStatus() {
        return descStatus;
    }

    /**
     * @param descStatus the descStatus to set
     */
    public void setDescStatus(String descStatus) {
        this.descStatus = descStatus;
    }

    /**
     * @return the enlaces
     */
    public ArrayList<EnlaceVO> getEnlaces() {
        return enlaces;
    }

    /**
     * @param enlaces the enlaces to set
     */
    public void setEnlaces(ArrayList<EnlaceVO> enlaces) {
        this.enlaces = enlaces;
    }

    /**
     * @return the asientos
     */
    public ArrayList<AsientoFichaExpedienteVO> getAsientos() {
        return asientos;
    }

    /**
     * @param asientos the asientos to set
     */
    public void setAsientos(ArrayList<AsientoFichaExpedienteVO> asientos) {
        this.asientos = asientos;
    }

    /**
     * @return the errores
     */
    public ArrayList<String> getErrores() {
        return errores;
    }

    /**
     * @param errores the errores to set
     */
    public void setErrores(ArrayList<String> errores) {
        this.errores = errores;
    }

    /**
     * @return the documentos
     */
    public ArrayList<DocumentoInicioExpedienteVO> getDocumentos() {
        return documentos;
    }

    /**
     * @param documentos the documentos to set
     */
    public void setDocumentos(ArrayList<DocumentoInicioExpedienteVO> documentos) {
        this.documentos = documentos;
    }

    /**
     * @return the estructura
     *
    public ArrayList<EstructuraCampo> getEstructuraDatosSuplementarios() {
        return estructuraDatosSuplementarios;
    }

    /**
     * @param estructura the estructura to set
     *
    public void setEstructuraDatosSuplementarios(ArrayList<EstructuraCampo> estructura) {
        this.estructuraDatosSuplementarios = estructura;
    }

    /**
     * @return the valores
     *
    public ArrayList<CamposFormulario> getValoresDatosSuplementarios() {
        return valoresDatosSuplementarios;
    }

    /**
     * @param valores the valores to set
     *
    public void setValoresDatosSuplementarios(ArrayList<CamposFormulario> valores) {
        this.valoresDatosSuplementarios = valores;
    } */   

    /**
     * @return the agrupaciones
     */
    public ArrayList<DefinicionAgrupacionCamposAgrupadosValueObject> getAgrupaciones() {
        return agrupaciones;
    }

    /**
     * @param agrupaciones the agrupaciones to set
     */
    public void setAgrupaciones(ArrayList<DefinicionAgrupacionCamposAgrupadosValueObject> agrupaciones) {
        this.agrupaciones = agrupaciones;
    }

    /**
     * @return the posicionarCamposSuplementariosExpediente
     */
    public int isPosicionarCamposSuplementariosExpediente() {
        return posicionarCamposSuplementariosExpediente;
    }

    /**
     * @param posicionarCamposSuplementariosExpediente the posicionarCamposSuplementariosExpediente to set
     */
    public void setPosicionarCamposSuplementariosExpediente(int posicionarCamposSuplementariosExpediente) {
        this.posicionarCamposSuplementariosExpediente = posicionarCamposSuplementariosExpediente;
    }
    
    public ArrayList<FicheroVO> getDocumentosAportados() {
        return documentosAportados;
    }

    public void setDocumentosAportados(ArrayList<FicheroVO> documentosAportados) {
        this.documentosAportados = documentosAportados;
    }
    
}
