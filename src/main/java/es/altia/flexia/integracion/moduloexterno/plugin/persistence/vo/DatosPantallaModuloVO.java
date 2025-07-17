package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;

public class DatosPantallaModuloVO {
    private String nombrePantalla;
    private String url;
    private String operacionProceso;
    private String codProcedimiento;
    private int codTramite;
    private boolean pantallaProcedimiento;

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the operacionProceso
     */
    public String getOperacionProceso() {
        return operacionProceso;
    }

    /**
     * @param operacionProceso the operacionProceso to set
     */
    public void setOperacionProceso(String operacionProceso) {
        this.operacionProceso = operacionProceso;
    }

    /**
     * @return the codProcedimiento
     */
    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    /**
     * @param codProcedimiento the codProcedimiento to set
     */
    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    /**
     * @return the codTramite
     */
    public int getCodTramite() {
        return codTramite;
    }

    /**
     * @param codTramite the codTramite to set
     */
    public void setCodTramite(int codTramite) {
        this.codTramite = codTramite;
    }

    /**
     * @return the pantallaProcedimiento
     */
    public boolean isPantallaProcedimiento() {
        return pantallaProcedimiento;
    }

    /**
     * @param pantallaProcedimiento the pantallaProcedimiento to set
     */
    public void setPantallaProcedimiento(boolean pantallaProcedimiento) {
        this.pantallaProcedimiento = pantallaProcedimiento;
    }

    /**
     * @return the nombrePantalla
     */
    public String getNombrePantalla() {
        return nombrePantalla;
    }

    /**
     * @param nombrePantalla the nombrePantalla to set
     */
    public void setNombrePantalla(String nombrePantalla) {
        this.nombrePantalla = nombrePantalla;
    }
    
}