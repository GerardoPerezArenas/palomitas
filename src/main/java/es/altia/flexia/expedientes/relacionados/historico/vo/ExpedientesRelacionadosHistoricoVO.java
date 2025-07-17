/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.expedientes.relacionados.historico.vo;

import java.util.Calendar;


/**
 * @author david.vidal
 * @version 30/05/2013 1.0
 * Historial de cambios: 
 */
public class ExpedientesRelacionadosHistoricoVO {
    
    private String num_entrada;
    private String ejercicio;
    private Calendar  fecha_desasociada;
    private Calendar fecha_presentacion;
    private String usuario;
    private String expediente;
    private int codigoIdiomaUsuario;
    private String estado;

    /**
     * @return the num_entrada
     */
    public String getNum_entrada() {
        return num_entrada;
    }

    /**
     * @param num_entrada the num_entrada to set
     */
    public void setNum_entrada(String num_entrada) {
        this.num_entrada = num_entrada;
    }

    /**
     * @return the ejercicio
     */
    public String getEjercicio() {
        return ejercicio;
    }

    /**
     * @param ejercicio the ejercicio to set
     */
    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    /**
     * @return the fecha_desasociada
     */
    public Calendar getFecha_desasociada() {
        return fecha_desasociada;
    }

    /**
     * @param fecha_desasociada the fecha_desasociada to set
     */
    public void setFecha_desasociada(Calendar fecha_desasociada) {
        this.fecha_desasociada = fecha_desasociada;
    }

    /**
     * @return the fecha_presentacion
     */
    public Calendar getFecha_presentacion() {
        return fecha_presentacion;
    }

    /**
     * @param fecha_presentacion the fecha_presentacion to set
     */
    public void setFecha_presentacion(Calendar fecha_presentacion) {
        this.fecha_presentacion = fecha_presentacion;
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

    /**
     * @return the expediente
     */
    public String getExpediente() {
        return expediente;
    }

    /**
     * @param expediente the expediente to set
     */
    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    /**
     * @return the codigoIdiomaUsuario
     */
    public int getCodigoIdiomaUsuario() {
        return codigoIdiomaUsuario;
    }

    /**
     * @param codigoIdiomaUsuario the codigoIdiomaUsuario to set
     */
    public void setCodigoIdiomaUsuario(int codigoIdiomaUsuario) {
        this.codigoIdiomaUsuario = codigoIdiomaUsuario;
    }

    public String getEstado() {
        return estado;
    }//getEstado
    public void setEstado(String estado) {
        this.estado = estado;
    }//setEstado

}//class
