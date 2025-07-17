package es.altia.agora.business.sge;

import java.io.Serializable;

/**
 *  Objeto que contiene el valor de un determinado campo suplementario
 * @author oscar
 */
public class ValorCampoSuplementarioVO implements Serializable{
    private String codDatoSuplementario;
    private String valorDatoSuplementario;
    //codigo del valor seleccionado en el desplegable
    private String codValorDatoSuplementario;
    private int tipoDatoSuplementario;
    private String codPlazoActivo;
    private String valorPlazoActivo;
    private String codFechaVencimiento;
    private String valorFechaVencimiento;
    private boolean alarmaVencida;    
    private Integer longitudFichero;    
    private String tipoMimeFichero;
    private MetadatosDocumentoVO metadatosFichero;
    
     private int codOrganizacion;
    private String procedimiento;
    private int ejercicio;
    private String numExpediente;
    private int codTramite;
    private int ocurrenciaTramite;
    private String origenFichero;
    
       
                
    /**
     * @return the codDatoSuplementario
     */
    public String getCodDatoSuplementario() {
        return codDatoSuplementario;
    }

    /**
     * @param codDatoSuplementario the codDatoSuplementario to set
     */
    public void setCodDatoSuplementario(String codDatoSuplementario) {
        this.codDatoSuplementario = codDatoSuplementario;
    }

    /**
     * @return the valorDatoSuplementario
     */
    public String getValorDatoSuplementario() {
        return valorDatoSuplementario;
    }

    /**
     * @param valorDatoSuplementario the valorDatoSuplementario to set
     */
    public void setValorDatoSuplementario(String valorDatoSuplementario) {
        this.valorDatoSuplementario = valorDatoSuplementario;
    }

    /**
     * @return the tipoDatoSuplementario
     */
    public int getTipoDatoSuplementario() {
        return tipoDatoSuplementario;
    }

    /**
     * @param tipoDatoSuplementario the tipoDatoSuplementario to set
     */
    public void setTipoDatoSuplementario(int tipoDatoSuplementario) {
        this.tipoDatoSuplementario = tipoDatoSuplementario;
    }

    /**
     * @return the codPlazoActivo
     */
    public String getCodPlazoActivo() {
        return codPlazoActivo;
    }

    /**
     * @param codPlazoActivo the codPlazoActivo to set
     */
    public void setCodPlazoActivo(String codPlazoActivo) {
        this.codPlazoActivo = codPlazoActivo;
    }

    /**
     * @return the valorPlazoActivo
     */
    public String getValorPlazoActivo() {
        return valorPlazoActivo;
    }

    /**
     * @param valorPlazoActivo the valorPlazoActivo to set
     */
    public void setValorPlazoActivo(String valorPlazoActivo) {
        this.valorPlazoActivo = valorPlazoActivo;
    }

    /**
     * @return the codFechaVencimiento
     */
    public String getCodFechaVencimiento() {
        return codFechaVencimiento;
    }

    /**
     * @param codFechaVencimiento the codFechaVencimiento to set
     */
    public void setCodFechaVencimiento(String codFechaVencimiento) {
        this.codFechaVencimiento = codFechaVencimiento;
    }

    /**
     * @return the valorFechaVencimiento
     */
    public String getValorFechaVencimiento() {
        return valorFechaVencimiento;
    }

    /**
     * @param valorFechaVencimiento the valorFechaVencimiento to set
     */
    public void setValorFechaVencimiento(String valorFechaVencimiento) {
        this.valorFechaVencimiento = valorFechaVencimiento;
    }

    /**
     * @return the alarmaVencida
     */
    public boolean isAlarmaVencida() {
        return alarmaVencida;
    }

    /**
     * @param alarmaVencida the alarmaVencida to set
     */
    public void setAlarmaVencida(boolean alarmaVencida) {
        this.alarmaVencida = alarmaVencida;
    }

    /**
     * @return the longitudFichero
     */
    public Integer getLongitudFichero() {
        return longitudFichero;
    }

    /**
     * @param longitudFichero the longitudFichero to set
     */
    public void setLongitudFichero(Integer longitudFichero) {
        this.longitudFichero = longitudFichero;
    }

    /**
     * @return the tipoMimeFichero
     */
    public String getTipoMimeFichero() {
        return tipoMimeFichero;
    }

    /**
     * @param tipoMimeFichero the tipoMimeFichero to set
     */
    public void setTipoMimeFichero(String tipoMimeFichero) {
        this.tipoMimeFichero = tipoMimeFichero;
    }    
    
    
    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
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

    public String getOrigenFichero() {
        return origenFichero;
    }

    public void setOrigenFichero(String origenFichero) {
        this.origenFichero = origenFichero;
    }
    
    /**
     * @return the codValorDatoSuplementario
     */
    public String getCodValorDatoSuplementario(){
        return codValorDatoSuplementario;
    }
    
    /**
     * @param codValorDatoSuplementario the codValorDatoSuplementario to set
     */
    public void setCodValorDatoSuplementario(String codValorDatoSuplementario){
        this.codValorDatoSuplementario = codValorDatoSuplementario;
    }

    public MetadatosDocumentoVO getMetadatosFichero() {
        return metadatosFichero;
    }

    public void setMetadatosFichero(MetadatosDocumentoVO metadatosFichero) {
        this.metadatosFichero = metadatosFichero;
    }
}