package es.altia.agora.business.sge;

public class CampoListadoPendientesProcedimientoVO {
    private String codigo;
    private String codProcedimiento;
    private String codMunicipio;
    private String nombreCampo;
    private String tamanho;
    private String activo;
    private String orden;

    /** Para campos suplementarios **/    
    private String descripcionCampoSuplementario;
    private boolean campoSuplementario;

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
     * @return the codMunicipio
     */
    public String getCodMunicipio() {
        return codMunicipio;
    }

    /**
     * @param codMunicipio the codMunicipio to set
     */
    public void setCodMunicipio(String codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    /**
     * @return the nombreCampo
     */
    public String getNombreCampo() {
        return nombreCampo;
    }

    /**
     * @param nombreCampo the nombreCampo to set
     */
    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    /**
     * @return the tamanho
     */
    public String getTamanho() {
        return tamanho;
    }

    /**
     * @param tamanho the tamanho to set
     */
    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    /**
     * @return the activo
     */
    public String getActivo() {
        return activo;
    }

    /**
     * @param activo the activo to set
     */
    public void setActivo(String activo) {
        this.activo = activo;
    }

    /**
     * @return the orden
     */
    public String getOrden() {
        return orden;
    }

    /**
     * @param orden the orden to set
     */
    public void setOrden(String orden) {
        this.orden = orden;
    }

    /**
     * @return the campoSuplementario
     */
    public boolean isCampoSuplementario() {
        return campoSuplementario;
    }

    /**
     * @param campoSuplementario the campoSuplementario to set
     */
    public void setCampoSuplementario(boolean campoSuplementario) {
        this.campoSuplementario = campoSuplementario;
    }

   /**
     * @return the descripcionCampoSuplementario
     */
    public String getDescripcionCampoSuplementario() {
        return descripcionCampoSuplementario;
    }

    /**
     * @param descripcionCampoSuplementario the descripcionCampoSuplementario to set
     */
    public void setDescripcionCampoSuplementario(String descripcionCampoSuplementario) {
        this.descripcionCampoSuplementario = descripcionCampoSuplementario;
    }
    
}