/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.expedientes.relacionados.historico.util;


public class ResultadoPluginExpRelHist {
    
    private String CodigoError;
    private String mensajeError;
    private Integer status;

    /**
     * @return the CodigoError
     */
    public String getCodigoError() {
        return CodigoError;
    }

    /**
     * @param CodigoError the CodigoError to set
     */
    public void setCodigoError(String CodigoError) {
        this.CodigoError = CodigoError;
    }

    /**
     * @return the mensajeError
     */
    public String getMensajeError() {
        return mensajeError;
    }

    /**
     * @param mensajeError the mensajeError to set
     */
    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    
}