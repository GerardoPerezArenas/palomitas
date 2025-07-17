package es.altia.agora.technical;

import java.io.Serializable;
import java.util.Vector;

/**
 * <p>Titulo: Herramienta de Gestión de Contenidos</p>
 * <p>Descripcion: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: <font color="navy"><b>altia</b><font><font color="gray">consultores</font></p><img src="/logoaltia.gif"></p>
 * @author unascribed
 * @version 1.0
 */
public class EstructuraCampo implements Serializable {
    // Estructura de un campo ...

    private String codCampo = null;
    private String descCampo = null;
    private String estadoValorCampo=null;
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

    public EstructuraCampo() {
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

    public String getEstadoValorCampo() {
        return estadoValorCampo;
    }

    public void setEstadoValorCampo(String estadoValorCampo) {
        this.estadoValorCampo = estadoValorCampo;
    }

    public Vector getListaEstadoValorDesplegable() {
        return listaEstadoValorDesplegable;
    }

    public void setListaEstadoValorDesplegable(Vector listaEstadoValorDesplegable) {
        this.listaEstadoValorDesplegable = listaEstadoValorDesplegable;
    }
    
    
    
}
