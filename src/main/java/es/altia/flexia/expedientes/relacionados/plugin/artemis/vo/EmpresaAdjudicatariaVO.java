package es.altia.flexia.expedientes.relacionados.plugin.artemis.vo;

/**
 * Clase que representa una empresa adjudicataria de un expediente de artemis
 * @author oscar.rodriguez
 */
public class EmpresaAdjudicatariaVO {
    private String codigoEmpresa;
    private String nombreEmpresa;
    private String nif;

    /**
     * @return the codigoEmpresa
     */
    public String getCodigoEmpresa() {
        return codigoEmpresa;
    }

    /**
     * @param codigoEmpresa the codigoEmpresa to set
     */
    public void setCodigoEmpresa(String codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    /**
     * @return the nombreEmpresa
     */
    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    /**
     * @param nombreEmpresa the nombreEmpresa to set
     */
    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    /**
     * @return the nif
     */
    public String getNif() {
        return nif;
    }

    /**
     * @param nif the nif to set
     */
    public void setNif(String nif) {
        this.nif = nif;
    }
    
}