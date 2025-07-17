package es.altia.agora.business.sge;

/**
 * Clase que representa una condición de entrada de un trámite
 * @author oscar.rodriguez
 */
public class CondicionEntradaTramiteVO {

    private String tipoCondicionEntrada = null;
    private String codigoCondicionEntrada=null;
    private String codigoTramiteOrigen=null;
    private String codigoProcedimientoOrigen;
    private String idTramiteCondTramite=null;
    private String codTramiteCondTramite=null;
    private String estadoCodTramiteCondTramite=null;
    private String expresionCondicion=null;
    private String codDocumento=null;

    /**
     * @return the tipoCondicionEntrada
     */
    public String getTipoCondicionEntrada() {
        return tipoCondicionEntrada;
    }

    /**
     * @param tipoCondicionEntrada the tipoCondicionEntrada to set
     */
    public void setTipoCondicionEntrada(String tipoCondicionEntrada) {
        this.tipoCondicionEntrada = tipoCondicionEntrada;
    }

    /**
     * @return the codigoCondicionEntrada
     */
    public String getCodigoCondicionEntrada() {
        return codigoCondicionEntrada;
    }

    /**
     * @param codigoCondicionEntrada the codigoCondicionEntrada to set
     */
    public void setCodigoCondicionEntrada(String codigoCondicionEntrada) {
        this.codigoCondicionEntrada = codigoCondicionEntrada;
    }

    /**
     * @return the codigoTramiteOrigen
     */
    public String getCodigoTramiteOrigen() {
        return codigoTramiteOrigen;
    }

    /**
     * @param codigoTramiteOrigen the codigoTramiteOrigen to set
     */
    public void setCodigoTramiteOrigen(String codigoTramiteOrigen) {
        this.codigoTramiteOrigen = codigoTramiteOrigen;
    }

    /**
     * @return the codigoProcedimientoOrigen
     */
    public String getCodigoProcedimientoOrigen() {
        return codigoProcedimientoOrigen;
    }

    /**
     * @param codigoProcedimientoOrigen the codigoProcedimientoOrigen to set
     */
    public void setCodigoProcedimientoOrigen(String codigoProcedimientoOrigen) {
        this.codigoProcedimientoOrigen = codigoProcedimientoOrigen;
    }

    /**
     * @return the idTramiteCondTramite
     */
    public String getIdTramiteCondTramite() {
        return idTramiteCondTramite;
    }

    /**
     * @param idTramiteCondTramite the idTramiteCondTramite to set
     */
    public void setIdTramiteCondTramite(String idTramiteCondTramite) {
        this.idTramiteCondTramite = idTramiteCondTramite;
    }

    /**
     * @return the codTramiteCondTramite
     */
    public String getCodTramiteCondTramite() {
        return codTramiteCondTramite;
    }

    /**
     * @param codTramiteCondTramite the codTramiteCondTramite to set
     */
    public void setCodTramiteCondTramite(String codTramiteCondTramite) {
        this.codTramiteCondTramite = codTramiteCondTramite;
    }

    /**
     * @return the estadoCodTramiteCondTramite
     */
    public String getEstadoCodTramiteCondTramite() {
        return estadoCodTramiteCondTramite;
    }

    /**
     * @param estadoCodTramiteCondTramite the estadoCodTramiteCondTramite to set
     */
    public void setEstadoCodTramiteCondTramite(String estadoCodTramiteCondTramite) {
        this.estadoCodTramiteCondTramite = estadoCodTramiteCondTramite;
    }

    /**
     * @return the expresionCondicion
     */
    public String getExpresionCondicion() {
        return expresionCondicion;
    }

    /**
     * @param expresionCondicion the expresionCondicion to set
     */
    public void setExpresionCondicion(String expresionCondicion) {
        this.expresionCondicion = expresionCondicion;
    }

    public String getCodDocumento() {
        return codDocumento;
    }

    public void setCodDocumento(String codDocumento) {
        this.codDocumento = codDocumento;
    }



}