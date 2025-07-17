package es.altia.flexia.registro.oficinasregistro.lanbide.web.form;

import es.altia.flexia.registro.oficinasregistro.lanbide.vo.AnotacionOficinaRegistroVO;
import es.altia.flexia.registro.oficinasregistro.lanbide.vo.OficinaRegistroLanbideVO;
import java.util.ArrayList;
import org.apache.struts.action.ActionForm;

/**
 * Formulario para trabajar con el módulo de control de envio de oficinas de registro
 * @author oscar.rodriguez
 */
public class AnotacionesOficinasRegistroForm extends ActionForm {

    
    private String codigoOficinaRegistroOrigen;
    private String codigoOficinaRegistroDestino;
    private String nombreOficinaRegistroDestino;
    private String usuario;
    private ArrayList<OficinaRegistroLanbideVO> oficinasOrigen;
    private ArrayList<OficinaRegistroLanbideVO> oficinasDestino;
    private String codigoUnidadRegistroUsuario;
    private String codigoOficinaUsuario;
    private String descripcionOficinaUsuario;
    private String codigoError;
    private String fechaDesde;
    private String fechaHasta;
    private String estado;
    private String pagina;
    private String numPaginas;
    private ArrayList<AnotacionOficinaRegistroVO> anotaciones =null;
    private String lineasPaginas;
    private String numRelacionAnotaciones;
    private String listaAnotaciones;
   
    
    /**
     * @return the oficinasOrigen
     */
    public ArrayList<OficinaRegistroLanbideVO> getOficinasOrigen() {
        return oficinasOrigen;
    }

    /**
     * @param oficinasOrigen the oficinasOrigen to set
     */
    public void setOficinasOrigen(ArrayList<OficinaRegistroLanbideVO> oficinasOrigen) {
        this.oficinasOrigen = oficinasOrigen;
    }

    /**
     * @return the oficinasDestino
     */
    public ArrayList<OficinaRegistroLanbideVO> getOficinasDestino() {
        return oficinasDestino;
    }

    /**
     * @param oficinasDestino the oficinasDestino to set
     */
    public void setOficinasDestino(ArrayList<OficinaRegistroLanbideVO> oficinasDestino) {
        this.oficinasDestino = oficinasDestino;
    }

    /**
     * @return the codigoOficinaRegistroOrigen
     */
    public String getCodigoOficinaRegistroOrigen() {
        return codigoOficinaRegistroOrigen;
    }

    /**
     * @param codigoOficinaRegistroOrigen the codigoOficinaRegistroOrigen to set
     */
    public void setCodigoOficinaRegistroOrigen(String codigoOficinaRegistroOrigen) {
        this.codigoOficinaRegistroOrigen = codigoOficinaRegistroOrigen;
    }

    /**
     * @return the codigoOficinaRegistroDestino
     */
    public String getCodigoOficinaRegistroDestino() {
        return codigoOficinaRegistroDestino;
    }

    /**
     * @param codigoOficinaRegistroDestino the codigoOficinaRegistroDestino to set
     */
    public void setCodigoOficinaRegistroDestino(String codigoOficinaRegistroDestino) {
        this.codigoOficinaRegistroDestino = codigoOficinaRegistroDestino;
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
     * @return the codigoOficinaUsuario
     */
    public String getCodigoOficinaUsuario() {
        return codigoOficinaUsuario;
    }

    /**
     * @param codigoOficinaUsuario the codigoOficinaUsuario to set
     */
    public void setCodigoOficinaUsuario(String codigoOficinaUsuario) {
        this.codigoOficinaUsuario = codigoOficinaUsuario;
    }

    /**
     * @return the descripcionOficinaUsuario
     */
    public String getDescripcionOficinaUsuario() {
        return descripcionOficinaUsuario;
    }

    /**
     * @param descripcionOficinaUsuario the descripcionOficinaUsuario to set
     */
    public void setDescripcionOficinaUsuario(String descripcionOficinaUsuario) {
        this.descripcionOficinaUsuario = descripcionOficinaUsuario;
    }

    /**
     * @return the codigoError
     */
    public String getCodigoError() {
        return codigoError;
    }

    /**
     * @param codigoError the codigoError to set
     */
    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }

    /**
     * @return the fechaDesde
     */
    public String getFechaDesde() {
        return fechaDesde;
    }

    /**
     * @param fechaDesde the fechaDesde to set
     */
    public void setFechaDesde(String fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    /**
     * @return the fechaHasta
     */
    public String getFechaHasta() {
        return fechaHasta;
    }

    /**
     * @param fechaHasta the fechaHasta to set
     */
    public void setFechaHasta(String fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    /**
     * @return the pagina
     */
    public String getPagina() {
        return pagina;
    }

    /**
     * @param pagina the pagina to set
     */
    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    /**
     * @return the numPaginas
     */
    public String getNumPaginas() {
        return numPaginas;
    }

    /**
     * @param numPaginas the numPaginas to set
     */
    public void setNumPaginas(String numPaginas) {
        this.numPaginas = numPaginas;
    }

    /**
     * @return the anotaciones
     */
    public ArrayList<AnotacionOficinaRegistroVO> getAnotaciones() {
        return anotaciones;
    }

    /**
     * @param anotaciones the anotaciones to set
     */
    public void setAnotaciones(ArrayList<AnotacionOficinaRegistroVO> anotaciones) {
        this.anotaciones = anotaciones;
    }

    /**
     * @return the lineasPaginas
     */
    public String getLineasPaginas() {
        return lineasPaginas;
    }

    /**
     * @param lineasPaginas the lineasPaginas to set
     */
    public void setLineasPaginas(String lineasPaginas) {
        this.lineasPaginas = lineasPaginas;
    }

    /**
     * @return the numRelacionAnotaciones
     */
    public String getNumRelacionAnotaciones() {
        return numRelacionAnotaciones;
    }

    /**
     * @param numRelacionAnotaciones the numRelacionAnotaciones to set
     */
    public void setNumRelacionAnotaciones(String numRelacionAnotaciones) {
        this.numRelacionAnotaciones = numRelacionAnotaciones;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param codEstado the codEstado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the listaAnotacionesAceptar
     */
    public String getListaAnotaciones() {
        return listaAnotaciones;
    }

    /**
     * @param listaAnotacionesAceptar the listaAnotacionesAceptar to set
     */
    public void setListaAnotaciones(String listaAnotaciones) {
        this.listaAnotaciones = listaAnotaciones;
    }

    public String getNombreOficinaRegistroDestino() {
        return nombreOficinaRegistroDestino;
    }

    public void setNombreOficinaRegistroDestino(String nombreOficinaRegistroDestino) {
        this.nombreOficinaRegistroDestino = nombreOficinaRegistroDestino;
    }

    /**
     * @return the codigoUnidadRegistroUsuario
     */
    public String getCodigoUnidadRegistroUsuario() {
        return codigoUnidadRegistroUsuario;
    }

    /**
     * @param codigoUnidadRegistroUsuario the codigoUnidadRegistroUsuario to set
     */
    public void setCodigoUnidadRegistroUsuario(String codigoUnidadRegistroUsuario) {
        this.codigoUnidadRegistroUsuario = codigoUnidadRegistroUsuario;
    }
 
}