package es.altia.flexia.interfaces.user.web.carga.parcial.fichaexpediente.vo;

/**
 *
 * @author oscar
 */
public class DatosExpedienteVO {
 
    private int codOrganizacion;
    private int ejercicio;
    private String numExpediente;
    private Integer codTramite;
    private Integer ocurrenciaTramite;
    private String desdeJsp;
    private String consultaCampos;
    private String codProcedimiento;
    private boolean expHistorico;

    public boolean isExpHistorico() {
        return expHistorico;
    }

    public void setExpHistorico(boolean expHistorico) {
        this.expHistorico = expHistorico;
    }

    

    /**
     * @return the codOrganizacion
     */
    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    /**
     * @param codOrganizacion the codOrganizacion to set
     */
    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    /**
     * @return the ejercicio
     */
    public int getEjercicio() {
        return ejercicio;
    }

    /**
     * @param ejercicio the ejercicio to set
     */
    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

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
     * @return the codTramite
     */
    public Integer getCodTramite() {
        return codTramite;
    }

    /**
     * @param codTramite the codTramite to set
     */
    public void setCodTramite(Integer codTramite) {
        this.codTramite = codTramite;
    }

    /**
     * @return the desdeJsp
     */
    public String getDesdeJsp() {
        return desdeJsp;
    }

    /**
     * @param desdeJsp the desdeJsp to set
     */
    public void setDesdeJsp(String desdeJsp) {
        this.desdeJsp = desdeJsp;
    }

    /**
     * @return the consultaCampos
     */
    public String getConsultaCampos() {
        return consultaCampos;
    }

    /**
     * @param consultaCampos the consultaCampos to set
     */
    public void setConsultaCampos(String consultaCampos) {
        this.consultaCampos = consultaCampos;
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
     * @return the ocurrenciaTramite
     */
    public Integer getOcurrenciaTramite() {
        return ocurrenciaTramite;
    }

    /**
     * @param ocurrenciaTramite the ocurrenciaTramite to set
     */
    public void setOcurrenciaTramite(Integer ocurrenciaTramite) {
        this.ocurrenciaTramite = ocurrenciaTramite;
    }
    
}