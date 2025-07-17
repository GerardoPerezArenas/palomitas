package es.altia.flexia.expedientes.relacionados.plugin.artemis.vo;

import java.util.ArrayList;

/**
 *
 * @author oscar.rodriguez
 */
public class ExpedienteArtemisVO {
    private String numExpediente="";
    private String nombreExpediente="";
    private String objeto="";
    private String departamentoResponsable="";
    private String areaAdquisicionesResponsable="";
    private String naturalezaContrato="";
    private String alcanceMaximoConIVA="";
    private String alcanceMaximoSinIVA="";
    private String numeroLotes="";
    private String importeModeloOfertaSinIVA="";
    private String importeModeloOfertaConIVA="";
    private String plazoRecepcionOfertas="";
    private String estadoExpediente="";
    private String duracionContratoNum="";
    private String duracionContratoUnidades="";
    private ArrayList<EmpresaAdjudicatariaVO> empresasAdjudicatarias  = new ArrayList<EmpresaAdjudicatariaVO>();
    private String codigosCPV = "";
    private String fechaInicio = "";
    private String fechaAdjudicacionProvisional = "";
    private String fechaAdjudicacionDefinitiva   = "";
    
    /**
     * @return the numExpediente
     */
    public String getNumExpediente() {
        return numExpediente;
    }

    /**
     * @param numExpediente the numExpediente to set
     */
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    /**
     * @return the nombreExpediente
     */
    public String getNombreExpediente() {
        return nombreExpediente;
    }

    /**
     * @param nombreExpediente the nombreExpediente to set
     */
    public void setNombreExpediente(String nombreExpediente) {
        this.nombreExpediente = nombreExpediente;
    }

    /**
     * @return the objeto
     */
    public String getObjeto() {
        return objeto;
    }

    /**
     * @param objeto the objeto to set
     */
    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    /**
     * @return the departamentoResponsable
     */
    public String getDepartamentoResponsable() {
        return departamentoResponsable;
    }

    /**
     * @param departamentoResponsable the departamentoResponsable to set
     */
    public void setDepartamentoResponsable(String departamentoResponsable) {
        this.departamentoResponsable = departamentoResponsable;
    }

    /**
     * @return the areaAdquisicionesResponsable
     */
    public String getAreaAdquisicionesResponsable() {
        return areaAdquisicionesResponsable;
    }

    /**
     * @param areaAdquisicionesResponsable the areaAdquisicionesResponsable to set
     */
    public void setAreaAdquisicionesResponsable(String areaAdquisicionesResponsable) {
        this.areaAdquisicionesResponsable = areaAdquisicionesResponsable;
    }

    /**
     * @return the naturalezaContrato
     */
    public String getNaturalezaContrato() {
        return naturalezaContrato;
    }

    /**
     * @param naturalezaContrato the naturalezaContrato to set
     */
    public void setNaturalezaContrato(String naturalezaContrato) {
        this.naturalezaContrato = naturalezaContrato;
    }

  
    /**
     * @return the numeroLotes
     */
    public String getNumeroLotes() {
        return numeroLotes;
    }

    /**
     * @param numeroLotes the numeroLotes to set
     */
    public void setNumeroLotes(String numeroLotes) {
        this.numeroLotes = numeroLotes;
    }

    /**
     * @return the importeModeloOfertaSinIVA
     */
    public String getImporteModeloOfertaSinIVA() {
        return importeModeloOfertaSinIVA;
    }

    /**
     * @param importeModeloOfertaSinIVA the importeModeloOfertaSinIVA to set
     */
    public void setImporteModeloOfertaSinIVA(String importeModeloOfertaSinIVA) {
        this.importeModeloOfertaSinIVA = importeModeloOfertaSinIVA;
    }

    /**
     * @return the importeModeloOfertaConIVA
     */
    public String getImporteModeloOfertaConIVA() {
        return importeModeloOfertaConIVA;
    }

    /**
     * @param importeModeloOfertaConIVA the importeModeloOfertaConIVA to set
     */
    public void setImporteModeloOfertaConIVA(String importeModeloOfertaConIVA) {
        this.importeModeloOfertaConIVA = importeModeloOfertaConIVA;
    }
  
    /**
     * @return the plazoRecepcionOfertas
     */
    public String getPlazoRecepcionOfertas() {
        return plazoRecepcionOfertas;
    }

    /**
     * @param plazoRecepcionOfertas the plazoRecepcionOfertas to set
     */
    public void setPlazoRecepcionOfertas(String plazoRecepcionOfertas) {
        this.plazoRecepcionOfertas = plazoRecepcionOfertas;
    }

    
    /**
     * @return the estadoExpediente
     */
    public String getEstadoExpediente() {
        return estadoExpediente;
    }

    /**
     * @param estadoExpediente the estadoExpediente to set
     */
    public void setEstadoExpediente(String estadoExpediente) {
        this.estadoExpediente = estadoExpediente;
    }

    /**
     * @return the alcanceMaximoConIVA
     */
    public String getAlcanceMaximoConIVA() {
        return alcanceMaximoConIVA;
    }

    /**
     * @param alcanceMaximoConIVA the alcanceMaximoConIVA to set
     */
    public void setAlcanceMaximoConIVA(String alcanceMaximoConIVA) {
        this.alcanceMaximoConIVA = alcanceMaximoConIVA;
    }

    /**
     * @return the alcanceMaximoSinIVA
     */
    public String getAlcanceMaximoSinIVA() {
        return alcanceMaximoSinIVA;
    }

    /**
     * @param alcanceMaximoSinIVA the alcanceMaximoSinIVA to set
     */
    public void setAlcanceMaximoSinIVA(String alcanceMaximoSinIVA) {
        this.alcanceMaximoSinIVA = alcanceMaximoSinIVA;
    }

    /**
     * @return the duracionContratoNum
     */
    public String getDuracionContratoNum() {
        return duracionContratoNum;
    }

    /**
     * @param duracionContratoNum the duracionContratoNum to set
     */
    public void setDuracionContratoNum(String duracionContratoNum) {
        this.duracionContratoNum = duracionContratoNum;
    }

    /**
     * @return the duracionContratoUnidades
     */
    public String getDuracionContratoUnidades() {
        return duracionContratoUnidades;
    }

    /**
     * @param duracionContratoUnidades the duracionContratoUnidades to set
     */
    public void setDuracionContratoUnidades(String duracionContratoUnidades) {
        this.duracionContratoUnidades = duracionContratoUnidades;
    }

      /**
     * @param codigosCPV the codigosCPV to set
     */
    public void setCodigosCPV(String codigosCPV) {
        this.codigosCPV = codigosCPV;
    }


   public String getCodigosCPV() {
        return this.codigosCPV;
    }

    /**
     * @return the empresasAdjudicatarias
     */
    public ArrayList<EmpresaAdjudicatariaVO> getEmpresasAdjudicatarias() {
        return empresasAdjudicatarias;
    }

    /**
     * @param empresasAdjudicatarias the empresasAdjudicatarias to set
     */
    public void setEmpresasAdjudicatarias(ArrayList<EmpresaAdjudicatariaVO> empresasAdjudicatarias) {
        this.empresasAdjudicatarias = empresasAdjudicatarias;
    }

    /**
     * @return the fechaInicio
     */
    public String getFechaInicio() {
        return fechaInicio;
    }

    /**
     * @param fechaInicio the fechaInicio to set
     */
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * @return the fechaAdjudicacionProvisional
     */
    public String getFechaAdjudicacionProvisional() {
        return fechaAdjudicacionProvisional;
    }

    /**
     * @param fechaAdjudicacionProvisional the fechaAdjudicacionProvisional to set
     */
    public void setFechaAdjudicacionProvisional(String fechaAdjudicacionProvisional) {
        this.fechaAdjudicacionProvisional = fechaAdjudicacionProvisional;
    }

    /**
     * @return the fechaAdjudicacionDefinitiva
     */
    public String getFechaAdjudicacionDefinitiva() {
        return fechaAdjudicacionDefinitiva;
    }

    /**
     * @param fechaAdjudicacionDefinitiva the fechaAdjudicacionDefinitiva to set
     */
    public void setFechaAdjudicacionDefinitiva(String fechaAdjudicacionDefinitiva) {
        this.fechaAdjudicacionDefinitiva = fechaAdjudicacionDefinitiva;
    }
    
}