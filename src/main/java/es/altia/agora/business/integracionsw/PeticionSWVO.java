package es.altia.agora.business.integracionsw;

import java.io.Serializable;

public class PeticionSWVO implements Serializable {

    private int codMunicipio;
    private String codProcedimiento;
    private int codTramite;
    private boolean avanzar;
    private boolean retroceder;
    private boolean iniciar;
    private String numExpediente;
    private int ocurrencia;
    private int ejercicio;
    private String[] params;
    private boolean ocurrenciaCerrada;
    private String origenLlamada;
    //Login del usuario que realiza la peticion
    private String usuario;
    
    public PeticionSWVO(int codMunicipio, String codProcedimiento, int codTramite, boolean avanzar,boolean retroceder,boolean iniciar,
                        String numExpediente, int ocurrencia, int ejercicio, boolean ocurrenciaCerrada,String[] params) {
        this.codMunicipio = codMunicipio;
        this.codProcedimiento = codProcedimiento;
        this.codTramite = codTramite;
        this.avanzar = avanzar;
        this.retroceder = retroceder;
        this.iniciar = iniciar;
        this.numExpediente = numExpediente;
        this.ocurrencia = ocurrencia;
        this.ejercicio = ejercicio;
        this.ocurrenciaCerrada = ocurrenciaCerrada;
       
        this.params = params;
    }
    
    
    public int getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(int codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public int getCodTramite() {
        return codTramite;
    }

    public void setCodTramite(int codTramite) {
        this.codTramite = codTramite;
    }

    public boolean isAvanzar() {
        return avanzar;
    }

    public void setAvanzar(boolean avanzar) {
        this.avanzar = avanzar;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public int getOcurrencia() {
        return ocurrencia;
    }

    public void setOcurrencia(int ocurrencia) {
        this.ocurrencia = ocurrencia;
    }

    public int getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    /**
     * @return the retroceder
     */
    public boolean isRetroceder() {
        return retroceder;
    }

    /**
     * @param retroceder the retroceder to set
     */
    public void setRetroceder(boolean retroceder) {
        this.retroceder = retroceder;
    }

    /**
     * @return the iniciar
     */
    public boolean isIniciar() {
        return iniciar;
    }

    /**
     * @param iniciar the iniciar to set
     */
    public void setIniciar(boolean iniciar) {
        this.iniciar = iniciar;
    }

    /**
     * @return the ocurrenciaCerrada
     */
    public boolean isOcurrenciaCerrada() {
        return ocurrenciaCerrada;
    }

    /**
     * @param ocurrenciaCerrada the ocurrenciaCerrada to set
     */
    public void setOcurrenciaCerrada(boolean ocurrenciaCerrada) {
        this.ocurrenciaCerrada = ocurrenciaCerrada;
    }

    public String getOrigenLlamada() {
        return origenLlamada;
    }

    public void setOrigenLlamada(String origenLlamada) {
        this.origenLlamada = origenLlamada;
    }

    public String toString() {
        return String.format("PeticionSW: (codMunicipio=%d, codProcedimiento=%s, codTramite=%d, avanzar=%b, retroceder=%b, iniciar=%b, numExpediente=%s, "
                + "ocurrencia=%d, ejercicio=%d, ocurrenciaCerrada=%b, origenLlamada=%s)", 
                this.codMunicipio, this.codProcedimiento, this.codTramite, this.avanzar, this.retroceder, this.iniciar, this.numExpediente, this.ocurrencia, this.ejercicio,
                this.ocurrenciaCerrada, this.origenLlamada);
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    
}
