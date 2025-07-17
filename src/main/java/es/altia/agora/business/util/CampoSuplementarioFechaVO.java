package es.altia.agora.business.util;

import java.util.Calendar;


public class CampoSuplementarioFechaVO {
    private int codMunicipio;
    private int ejercicio;
    private String numExpediente;
    private String codigoCampo;
    private Calendar valorfecha;
    private Calendar fechaVencimiento;
    private int plazoActivado;

    /**
     * @return the codMunicipio
     */
    public int getCodMunicipio() {
        return codMunicipio;
    }

    /**
     * @param codMunicipio the codMunicipio to set
     */
    public void setCodMunicipio(int codMunicipio) {
        this.codMunicipio = codMunicipio;
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
     * @return the codigoCampo
     */
    public String getCodigoCampo() {
        return codigoCampo;
    }

    /**
     * @param codigoCampo the codigoCampo to set
     */
    public void setCodigoCampo(String codigoCampo) {
        this.codigoCampo = codigoCampo;
    }

    /**
     * @return the valorfecha
     */
    public Calendar getValorfecha() {
        return valorfecha;
    }

    /**
     * @param valorfecha the valorfecha to set
     */
    public void setValorfecha(Calendar valorfecha) {
        this.valorfecha = valorfecha;
    }

    /**
     * @return the fechaVencimiento
     */
    public Calendar getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * @param fechaVencimiento the fechaVencimiento to set
     */
    public void setFechaVencimiento(Calendar fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * @return the plazoActivado
     */
    public int getPlazoActivado() {
        return plazoActivado;
    }

    /**
     * @param plazoActivado the plazoActivado to set
     */
    public void setPlazoActivado(int plazoActivado) {
        this.plazoActivado = plazoActivado;
    }
    
    
}
