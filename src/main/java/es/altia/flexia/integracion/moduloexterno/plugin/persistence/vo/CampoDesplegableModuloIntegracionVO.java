package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;

import java.util.ArrayList;

/**
 * Representa un campo desplegable
 */
public class CampoDesplegableModuloIntegracionVO {
    private String codigo;
    private String descripcion;
    private ArrayList<ValorCampoDesplegableModuloIntegracionVO> valores = null;
    

    /**
     * Devuelve el código que identifica a un campo suplementario
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Permite establecer el código de un campo desplegable
     * @return the codigo
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Devuelve la descripción de un campo desplegable
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Permite establecer la descripción de un campo desplegable
     * @param descripción
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve los valores de un campo desplegable
     * @return the valores
     */
    public ArrayList<ValorCampoDesplegableModuloIntegracionVO> getValores() {
        return valores;
    }

    /**
     * Establece los valores de un campo desplegable
     * @param valores the valores to set
     */
    public void setValores(ArrayList<ValorCampoDesplegableModuloIntegracionVO> valores) {
        this.valores = valores;
    }
   
}