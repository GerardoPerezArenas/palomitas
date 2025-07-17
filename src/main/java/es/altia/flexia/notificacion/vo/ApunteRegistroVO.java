package es.altia.flexia.notificacion.vo;

import java.util.ArrayList;

public class ApunteRegistroVO {
    private String entidad;
    private String codOrganoOrigen;
    private String codUnidadOrigen;
    private String numExpediente;
    private String codProcedimiento;
    private String asunto;
    private String texto;    
    private String numeroRegistroRT =null;
    private String fechaRT =null;
    private String horaRT =null;
    private ArrayList<TerceroRegistroVO> terceros;



    /**
     * @return the entidad
     */
    public String getEntidad() {
        return entidad;
    }

    /**
     * @param entidad the entidad to set
     */
    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    /**
     * @return the codOrganoOrigen
     */
    public String getCodOrganoOrigen() {
        return codOrganoOrigen;
    }

    /**
     * @param codOrganoOrigen the codOrganoOrigen to set
     */
    public void setCodOrganoOrigen(String codOrganoOrigen) {
        this.codOrganoOrigen = codOrganoOrigen;
    }

    /**
     * @return the codUnidadOrigen
     */
    public String getCodUnidadOrigen() {
        return codUnidadOrigen;
    }

    /**
     * @param codUnidadOrigen the codUnidadOrigen to set
     */
    public void setCodUnidadOrigen(String codUnidadOrigen) {
        this.codUnidadOrigen = codUnidadOrigen;
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
     * @return the asunto
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * @param asunto the asunto to set
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /**
     * @return the texto
     */
    public String getTexto() {
        return texto;
    }

    /**
     * @param texto the texto to set
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * @return the numeroRegistroRT
     */
    public String getNumeroRegistroRT() {
        return numeroRegistroRT;
    }

    /**
     * @param numeroRegistroRT the numeroRegistroRT to set
     */
    public void setNumeroRegistroRT(String numeroRegistroRT) {
        this.numeroRegistroRT = numeroRegistroRT;
    }

    /**
     * @return the fechaHoraRT
     */
    public String getFechaRT() {
        return fechaRT;
    }

    /**
     * @param fechaHoraRT the fechaHoraRT to set
     */
    public void setFechaRT(String fechaHoraRT) {
        this.fechaRT = fechaHoraRT;
    }

    /**
     * @return the horaRT
     */
    public String getHoraRT() {
        return horaRT;
    }

    /**
     * @param horaRT the horaRT to set
     */
    public void setHoraRT(String horaRT) {
        this.horaRT = horaRT;
    }

    /**
     * @return the terceros
     */
    public ArrayList<TerceroRegistroVO> getTerceros() {
        return terceros;
    }

    /**
     * @param terceros the terceros to set
     */
    public void setTerceros(ArrayList<TerceroRegistroVO> terceros) {
        this.terceros = terceros;
    }

}