/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.sge.persistence.manual;

import java.io.Serializable;

/**
 *
 * @author alberto.pulpeiro
 */
public class TecnicoReferenciaDTO implements Serializable{

    private String documentosIdentificacion;
    private String nombre;
    private String apellido1;
    private String apellido2;

    public TecnicoReferenciaDTO() {
    }

    public TecnicoReferenciaDTO(String DocumentosIdentificacion, String Nombre, String apellido1, String apellido2) {
        this.documentosIdentificacion = DocumentosIdentificacion;
        this.nombre = Nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
    }

    public String getDocumentosIdentificacion() {
        return documentosIdentificacion;
    }

    public void setDocumentosIdentificacion(String DocumentosIdentificacion) {
        this.documentosIdentificacion = DocumentosIdentificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String Nombre) {
        this.nombre = Nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

}
