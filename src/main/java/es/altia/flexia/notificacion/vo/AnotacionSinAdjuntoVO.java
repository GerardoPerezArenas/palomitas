/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.notificacion.vo;

/**
 *
 * @author david.vidal
 */
public class AnotacionSinAdjuntoVO {
    private String numero_anotacion;
    private String ejercicio_anotacion;
    private String unidad_registro;
    private String fecha_proceso;
    private String fecha_proceso_alt;
    private String cod_departamento;
    private String cod_asunto;
    private String estado_envio;        
    private String usuario;

    /**
     * @return the numero_anotacion
     */
    public String getNumero_anotacion() {
        return numero_anotacion;
    }

    /**
     * @param numero_anotacion the numero_anotacion to set
     */
    public void setNumero_anotacion(String numero_anotacion) {
        this.numero_anotacion = numero_anotacion;
    }

    /**
     * @return the ejercicio_anotacion
     */
    public String getEjercicio_anotacion() {
        return ejercicio_anotacion;
    }

    /**
     * @param ejercicio_anotacion the ejercicio_anotacion to set
     */
    public void setEjercicio_anotacion(String ejercicio_anotacion) {
        this.ejercicio_anotacion = ejercicio_anotacion;
    }

    /**
     * @return the unidad_registro
     */
    public String getUnidad_registro() {
        return unidad_registro;
    }

    /**
     * @param unidad_registro the unidad_registro to set
     */
    public void setUnidad_registro(String unidad_registro) {
        this.unidad_registro = unidad_registro;
    }

    /**
     * @return the fecha_proceso
     */
    public String getFecha_proceso() {
        return fecha_proceso;
    }

    /**
     * @param fecha_proceso the fecha_proceso to set
     */
    public void setFecha_proceso(String fecha_proceso) {
        this.fecha_proceso = fecha_proceso;
    }

    public String getFecha_proceso_alt() {
        return fecha_proceso_alt;
    }//getFecha_proceso_alt
    public void setFecha_proceso_alt(String fecha_proceso_alt) {
        this.fecha_proceso_alt = fecha_proceso_alt;
    }//setFecha_proceso_alt
    
    /**
     * @return the cod_departamento
     */
    public String getCod_departamento() {
        return cod_departamento;
    }

    /**
     * @param cod_departamento the cod_departamento to set
     */
    public void setCod_departamento(String cod_departamento) {
        this.cod_departamento = cod_departamento;
    }

    /**
     * @return the cod_asunto
     */
    public String getCod_asunto() {
        return cod_asunto;
    }

    /**
     * @param cod_asunto the cod_asunto to set
     */
    public void setCod_asunto(String cod_asunto) {
        this.cod_asunto = cod_asunto;
    }

    /**
     * @return the estado_envio
     */
    public String getEstado_envio() {
        return estado_envio;
    }

    /**
     * @param estado_envio the estado_envio to set
     */
    public void setEstado_envio(String estado_envio) {
        this.estado_envio = estado_envio;
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
