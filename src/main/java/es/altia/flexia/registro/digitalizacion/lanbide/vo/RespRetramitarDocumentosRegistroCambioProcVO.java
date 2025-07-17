/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.registro.digitalizacion.lanbide.vo;

import es.altia.agora.business.registro.RegistroValueObject;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author INGDGC
 */
public class RespRetramitarDocumentosRegistroCambioProcVO {
    
    private boolean errorEnlaPeticion;
    private String detalleErrorEnlaPeticion;
    RegistroValueObject datosEntradaregistro;
    List<String> datosDocumentosRetramitados;
    private String datosDocumentosRetramitadosAsString;

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

    public List<String> getDatosDocumentosRetramitados() {
        return datosDocumentosRetramitados;
    }

    public void setDatosDocumentosRetramitados(List<String> datosDocumentosRetramitados) {
        this.datosDocumentosRetramitados = datosDocumentosRetramitados;
    }

    public RegistroValueObject getDatosEntradaregistro() {
        return datosEntradaregistro;
    }

    public void setDatosEntradaregistro(RegistroValueObject datosEntradaregistro) {
        this.datosEntradaregistro = datosEntradaregistro;
    }

    public String getDatosDocumentosRetramitadosAsString() {
        if(datosDocumentosRetramitados!=null && datosDocumentosRetramitados.size()>0){
            return Arrays.toString(datosDocumentosRetramitados.toArray());
        }else return "";
    }
    
    
}
