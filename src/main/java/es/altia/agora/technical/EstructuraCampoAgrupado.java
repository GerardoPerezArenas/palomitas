package es.altia.agora.technical;

import es.altia.agora.business.sge.ValorCampoSuplementarioVO;
import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author oscar
 */
public class EstructuraCampoAgrupado implements Serializable {
    private String codCampo = null;
    private String descCampo = null;
    private String codPlantilla = null;
    private String URLPlantilla = null;
    private String codTipoDato = null;
    private String tamano = null;
    private String mascara = null;
    private String obligatorio = null;
    private String numeroOrden = null;
    private String rotulo = null;
    private String visible = null;
    private String activo = null;
    private String bloqueado = null;
    private String soloLectura = null;
    private String codTramite = null;
    private String descripcionTramite = null;
    private String valorConsulta = null;
    private String ocurrencia = null;
    private String desplegable = null;
    private Vector listaCodDesplegable = null;
    private Vector listaDescDesplegable = null;
    private Vector listaEstadoValorDesplegable=null;
    private String variableCambios = null;
    private String plazoFecha = null;
    private String checkPlazoFecha = null;
    private String fechaVencimiento = null;
    private String campoActivo = null;
    private String codAgrupacion = null;
    private Boolean posicionar = false;
    private String tamanoVista = null;
    private String posX = null;
    private String posY = null;    
    private ValorCampoSuplementarioVO valorCampo = null; 
    private String codMunicipio = null;
    private String oculto = null;
    private String validacion;
    private String operacion;

    

    public EstructuraCampoAgrupado() {
    }

    public String getCodCampo() {
        return codCampo;
    }

    public void setCodCampo(String codCampo) {
        this.codCampo = codCampo;
    }

    public String getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(String codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    public String getCodTipoDato() {
        return codTipoDato;
    }

    public void setCodTipoDato(String codTipoDato) {
        this.codTipoDato = codTipoDato;
    }

    public String getDescCampo() {
        return descCampo;
    }

    public void setDescCampo(String descCampo) {
        this.descCampo = descCampo;
    }

    public String getMascara() {
        return mascara;
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(String obligatorio) {
        this.obligatorio = obligatorio;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public String getURLPlantilla() {
        return URLPlantilla;
    }

    public void setURLPlantilla(String URLPlantilla) {
        this.URLPlantilla = URLPlantilla;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public String getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(String bloqueado) {
        this.bloqueado = bloqueado;
    }

    public String getPlazoFecha() {
        return plazoFecha;
    }

    public void setPlazoFecha(String plazoFecha) {
        this.plazoFecha = plazoFecha;
    }

    public String getCheckPlazoFecha() {
        return checkPlazoFecha;
    }

    public void setCheckPlazoFecha(String checkPlazoFecha) {
        this.checkPlazoFecha = checkPlazoFecha;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getSoloLectura() {
        return soloLectura;
    }

    public String getCampoActivo() {
        return campoActivo;
    }

    public void setCampoActivo(String campoActivo) {
        this.campoActivo = campoActivo;
    }

    public void setSoloLectura(String soloLectura) {
        this.soloLectura = soloLectura;
    }

    public String getCodTramite() {
        return codTramite;
    }

    public void setCodTramite(String codTramite) {
        this.codTramite = codTramite;
    }

    public String getDescripcionTramite() {
        return descripcionTramite;
    }

    public void setDescripcionTramite(String tramite) {
        this.descripcionTramite = tramite;
    }

    public String getValorConsulta() {
        return valorConsulta;
    }

    public void setValorConsulta(String valor) {
        this.valorConsulta = valor;
    }

    public String getOcurrencia() {
        return ocurrencia;
    }

    public void setOcurrencia(String ocurrencia) {
        this.ocurrencia = ocurrencia;
    }

    public String getDesplegable() {
        return desplegable;
    }

    public void setDesplegable(String desplegable) {
        this.desplegable = desplegable;
    }

    public Vector getListaCodDesplegable() {
        return listaCodDesplegable;
    }

    public void setListaCodDesplegable(Vector listaCodDesplegable) {
        this.listaCodDesplegable = listaCodDesplegable;
    }

    public Vector getListaDescDesplegable() {
        return listaDescDesplegable;
    }

    public void setListaDescDesplegable(Vector listaDescDesplegable) {
        this.listaDescDesplegable = listaDescDesplegable;
    }

    public String getVariableCambios() {
        return variableCambios;
    }

    public void setVariableCambios(String variableCambios) {
        this.variableCambios = variableCambios;
    }
    
     public String getCodAgrupacion() {
        return codAgrupacion;
    }
    public void setCodAgrupacion(String codAgrupacion) {
        this.codAgrupacion = codAgrupacion;
    }

    public Boolean getPosicionar() {
        return posicionar;
    }
    public void setPosicionar(Boolean posicionar) {
        this.posicionar = posicionar;
    }
    
    public String getTamanoVista() {
        return tamanoVista;
    }
    public void setTamanoVista(String tamanoVista) {
        this.tamanoVista = tamanoVista;
    }

    public String getPosX() {
        return posX;
    }
    public void setPosX(String posX) {
        this.posX = posX;
    }

    public String getPosY() {
        return posY;
    }
    public void setPosY(String posY) {
        this.posY = posY;
    }

    /**
     * @return the valorCampo
     */
    public ValorCampoSuplementarioVO getValorCampo() {
        return valorCampo;
    }

    /**
     * @param valorCampo the valorCampo to set
     */
    public void setValorCampo(ValorCampoSuplementarioVO valorCampo) {
        this.valorCampo = valorCampo;
    }

    /**
     * @return the codMunicipio
     */
    public String getCodMunicipio() {
        return codMunicipio;
    }

    /**
     * @param codMunicipio the codMunicipio to set
     */
    public void setCodMunicipio(String codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    /**
     * @return the oculto
     */
    public String getOculto() {
        return oculto;
    }

    /**
     * @param oculto the oculto to set
     */
    public void setOculto(String oculto) {
        this.oculto = oculto;
    }

    /**
     * @return the validacion
     */
    public String getValidacion() {
        return validacion;
    }

    /**
     * @param validacion the validacion to set
     */
    public void setValidacion(String validacion) {
        this.validacion = validacion;
    }

    /**
     * @return the operacion
     */
    public String getOperacion() {
        return operacion;
    }

    /**
     * @param operacion the operacion to set
     */
    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public Vector getListaEstadoValorDesplegable() {
        return listaEstadoValorDesplegable;
    }

    public void setListaEstadoValorDesplegable(Vector listaEstadoValorDesplegable) {
        this.listaEstadoValorDesplegable = listaEstadoValorDesplegable;
    }
    
    
}
