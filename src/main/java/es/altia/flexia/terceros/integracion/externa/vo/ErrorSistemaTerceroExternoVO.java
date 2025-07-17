package es.altia.flexia.terceros.integracion.externa.vo;

import java.util.ArrayList;


public class ErrorSistemaTerceroExternoVO {

    // Código de error devuelto por un sistema de alta de terceros externo. si existe este valor se concatena con cada una
    // de las etiquetas almacenas en etiquetasError
    private ArrayList<String> codigosError;
    private String nombreServicio;
    private String etiquetaError;
    private ArrayList<String> etiquetasError;    
    // Atributo que contiene una lista de campos obligatorios separados por una coma, que hacen referencia los campos obligatorios necesarios
    // para dar de alta el tercero en el sistema externo al que se hace referencia en el atributo "nombreServicio"
    private String listaCamposObligatorios = null;


    /******/
    private int tipoError = -1;
    private ArrayList<String> listaErrores = null;
    /******/

    /**
     * @return the etiquetaError
     */
    public String getEtiquetaError() {
        return etiquetaError;
    }

    /**
     * @param etiquetaError the etiquetaError to set
     */
    public void setEtiquetaError(String etiquetaError) {
        this.etiquetaError = etiquetaError;
    }

    /**
     * @return the nombreServicio
     */
    public String getNombreServicio() {
        return nombreServicio;
    }

    /**
     * @param nombreServicio the nombreServicio to set
     */
    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    /**
     * @return the etiquetasError
     */
    public ArrayList<String> getEtiquetasError() {
        return etiquetasError;
    }

    /**
     * @param etiquetasError the etiquetasError to set
     */
    public void setEtiquetasError(ArrayList<String> etiquetasError) {
        this.etiquetasError = etiquetasError;
    }

    /**
     * @return the listaCamposObligatorios
     */
    public String getListaCamposObligatorios() {
        return listaCamposObligatorios;
    }

    /**
     * @param listaCamposObligatorios the listaCamposObligatorios to set
     */
    public void setListaCamposObligatorios(String listaCamposObligatorios) {
        this.listaCamposObligatorios = listaCamposObligatorios;
    }

    
    /**
     * @return the codigosError
     */
    public ArrayList<String> getCodigosError() {
        return codigosError;
    }
    
    /**
     * @param codigosError the codigosError to set
     */
    public void setCodigosError(ArrayList<String> codigosError) {
        this.codigosError = codigosError;
    }

    /**
     * @return the listaErrores
     */
    public ArrayList<String> getListaErrores() {
        return listaErrores;
    }

    /**
     * @param listaErrores the listaErrores to set
     */
    public void setListaErrores(ArrayList<String> listaErrores) {
        this.listaErrores = listaErrores;
    }

    /**
     * @return the tipoError
     */
    public int getTipoError() {
        return tipoError;
    }

    /**
     * @param tipoError the tipoError to set
     */
    public void setTipoError(int tipoError) {
        this.tipoError = tipoError;
    }
}