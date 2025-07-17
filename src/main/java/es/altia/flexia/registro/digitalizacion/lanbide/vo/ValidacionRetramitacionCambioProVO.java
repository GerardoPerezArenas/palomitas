/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.registro.digitalizacion.lanbide.vo;

/**
 *
 * @author INGDGC
 */
public class ValidacionRetramitacionCambioProVO {
    
    private int codDepartamento;
    private int codUnidadOrganica;
    private String codTipoRegistro;
    private int ejercicioRegistro;
    private int numeroRegistro;
    private String codProcedimiento;
    private String codProcedimientoAnterior;
    private boolean tieneDocumentosEnDokusi;
    private boolean tieneDocumentosCatalogados;
    private boolean hayCambioProcedimiento;
    private boolean errorEnlaPeticion;
    private String detalleErrorEnlaPeticion;

    public int getCodDepartamento() {
        return codDepartamento;
    }

    public void setCodDepartamento(int codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    public int getCodUnidadOrganica() {
        return codUnidadOrganica;
    }

    public void setCodUnidadOrganica(int codUnidadOrganica) {
        this.codUnidadOrganica = codUnidadOrganica;
    }

    public String getCodTipoRegistro() {
        return codTipoRegistro;
    }

    public void setCodTipoRegistro(String codTipoRegistro) {
        this.codTipoRegistro = codTipoRegistro;
    }

    public int getEjercicioRegistro() {
        return ejercicioRegistro;
    }

    public void setEjercicioRegistro(int ejercicioRegistro) {
        this.ejercicioRegistro = ejercicioRegistro;
    }

    public int getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(int numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public String getCodProcedimientoAnterior() {
        return codProcedimientoAnterior;
    }

    public void setCodProcedimientoAnterior(String codProcedimientoAnterior) {
        this.codProcedimientoAnterior = codProcedimientoAnterior;
    }

    public boolean getTieneDocumentosEnDokusi() {
        return tieneDocumentosEnDokusi;
    }

    public void setTieneDocumentosEnDokusi(boolean tieneDocumentosEnDokusi) {
        this.tieneDocumentosEnDokusi = tieneDocumentosEnDokusi;
    }

    public boolean isErrorEnlaPeticion() {
        return errorEnlaPeticion;
    }

    public void setErrorEnlaPeticion(boolean errorEnlaPeticion) {
        this.errorEnlaPeticion = errorEnlaPeticion;
    }

    public String getDetalleErrorEnlaPeticion() {
        return detalleErrorEnlaPeticion;
    }

    public void setDetalleErrorEnlaPeticion(String detalleErrorEnlaPeticion) {
        this.detalleErrorEnlaPeticion = detalleErrorEnlaPeticion;
    }

    public boolean getHayCambioProcedimiento() {
        return hayCambioProcedimiento;
    }

    public void setHayCambioProcedimiento(boolean hayCambioProcedimiento) {
        this.hayCambioProcedimiento = hayCambioProcedimiento;
    }

    public boolean getTieneDocumentosCatalogados() {
        return tieneDocumentosCatalogados;
    }

    public void setTieneDocumentosCatalogados(boolean tieneDocumentosCatalogados) {
        this.tieneDocumentosCatalogados = tieneDocumentosCatalogados;
    }

    @Override
    public String toString() {
        return "ValidacionRetramitacionCambioProVO{" + "codDepartamento=" + codDepartamento + ", codUnidadOrganica=" + codUnidadOrganica + ", codTipoRegistro=" + codTipoRegistro + ", ejercicioRegistro=" + ejercicioRegistro + ", numeroRegistro=" + numeroRegistro + ", codProcedimiento=" + codProcedimiento + ", codProcedimientoAnterior=" + codProcedimientoAnterior + ", tieneDocumentosEnDokusi=" + tieneDocumentosEnDokusi + ", tieneDocumentosCatalogados=" + tieneDocumentosCatalogados + ", hayCambioProcedimiento=" + hayCambioProcedimiento + ", errorEnlaPeticion=" + errorEnlaPeticion + ", detalleErrorEnlaPeticion=" + detalleErrorEnlaPeticion + '}';
    }

    
    
    
}
