package es.altia.flexia.terceros.integracion.externa.vo;

import java.io.Serializable;
import java.util.ArrayList;


public class SalidaSistemaTerceroExternoVO implements Serializable{
    private int codigo;
    private ArrayList<String> erroresRestriccionesAltaTercero = null;
    private ArrayList<String> erroresRestriccionesModificacionTercero = null;
    private ArrayList<String> erroresCamposObligatorios = null;
    private String origen = null;

   
    /**
     * @return the codigo
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

   
    /**
     * @return the origen
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * @param origen the origen to set
     */
    public void setOrigen(String origen) {
        this.origen = origen;
    }

    /**
     * @return the erroresRestriccionesAltaTercero
     */
    public ArrayList<String> getErroresRestriccionesAltaTercero() {
        return erroresRestriccionesAltaTercero;
    }

    /**
     * @param erroresRestriccionesAltaTercero the erroresRestriccionesAltaTercero to set
     */
    public void setErroresRestriccionesAltaTercero(ArrayList<String> erroresRestriccionesAltaTercero) {
        this.erroresRestriccionesAltaTercero = erroresRestriccionesAltaTercero;
    }

    /**
     * @return the erroresRestriccionesModificacionTercero
     */
    public ArrayList<String> getErroresRestriccionesModificacionTercero() {
        return erroresRestriccionesModificacionTercero;
    }

    /**
     * @param erroresRestriccionesModificacionTercero the erroresRestriccionesModificacionTercero to set
     */
    public void setErroresRestriccionesModificacionTercero(ArrayList<String> erroresRestriccionesModificacionTercero) {
        this.erroresRestriccionesModificacionTercero = erroresRestriccionesModificacionTercero;
    }

    /**
     * @return the erroresCamposObligatorios
     */
    public ArrayList<String> getErroresCamposObligatorios() {
        return erroresCamposObligatorios;
    }

    /**
     * @param erroresCamposObligatorios the erroresCamposObligatorios to set
     */
    public void setErroresCamposObligatorios(ArrayList<String> erroresCamposObligatorios) {
        this.erroresCamposObligatorios = erroresCamposObligatorios;
    }
   
}