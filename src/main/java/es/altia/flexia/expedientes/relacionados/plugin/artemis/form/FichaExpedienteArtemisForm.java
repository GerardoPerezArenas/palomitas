package es.altia.flexia.expedientes.relacionados.plugin.artemis.form;

import es.altia.flexia.expedientes.relacionados.plugin.artemis.vo.EmpresaAdjudicatariaVO;
import java.util.ArrayList;
import org.apache.struts.action.ActionForm;

/**
 * Formulario de la ficha de un expediente de Artemis
 * @author oscar.rodriguez
 */
public class FichaExpedienteArtemisForm extends ActionForm{
   private String numExpediente;
    private String nombreExpediente;
    private String objeto;
    private String departamentoResponsable;
    private String areaAdquisicionesResponsable;
    private String naturalezaContrato;
    private String alcanceMaximo;    
    private String numeroLotes;
    private String importeModeloOfertaSinIVA;
    private String importeModeloOfertaConIVA;
    private String plazoRecepcionOfertas;    
    private String estadoExpediente;
    private String duracionContrato;
    private ArrayList<EmpresaAdjudicatariaVO> empresasAdjudicatarias  =null;
    private String codigosCPV = null;
    private String fechaInicio;
    private String fechaFin;

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
     * @return the codigosCPV
     */
    public String getCodigosCPV() {
        return codigosCPV;
    }

    /**
     * @param codigosCPV the codigosCPV to set
     */
    public void setCodigosCPV(String codigosCPV) {
        this.codigosCPV = codigosCPV;
    }

    /**
     * @return the alcanceMaximo
     */
    public String getAlcanceMaximo() {
        return alcanceMaximo;
    }

    /**
     * @param alcanceMaximo the alcanceMaximo to set
     */
    public void setAlcanceMaximo(String alcanceMaximo) {
        this.alcanceMaximo = alcanceMaximo;
    }

    /**
     * @return the duracionContrato
     */
    public String getDuracionContrato() {
        return duracionContrato;
    }

    /**
     * @param duracionContrato the duracionContrato to set
     */
    public void setDuracionContrato(String duracionContrato) {
        this.duracionContrato = duracionContrato;
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
     * @return the fechaFin
     */
    public String getFechaFin() {
        return fechaFin;
    }

    /**
     * @param fechaFin the fechaFin to set
     */
    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

}