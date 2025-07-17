/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.business.administracion;

/**
 *
 * @author altia
 */
public class EntidadVO {
    private int codOrganizacion;
    private int codEntidad;
    private String tipoEntidad;
    private String directorio;
    private String nombre;

    public EntidadVO(){ };
    
    public EntidadVO(int codOrganizacion, int codEntidad, String tipoEntidad, String directorio, String nombre) {
        this.codOrganizacion = codOrganizacion;
        this.codEntidad = codEntidad;
        this.tipoEntidad = tipoEntidad;
        this.directorio = directorio;
        this.nombre = nombre;
    }
    
    public EntidadVO(EntidadVO ent) {
        this.codOrganizacion = ent.getCodOrganizacion();
        this.codEntidad = ent.getCodEntidad();
        this.tipoEntidad = ent.getTipoEntidad();
        this.directorio = ent.getDirectorio();
        this.nombre = ent.getNombre();
    }
    
    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public int getCodEntidad() {
        return codEntidad;
    }

    public void setCodEntidad(int codEntidad) {
        this.codEntidad = codEntidad;
    }

    public String getTipoEntidad() {
        return tipoEntidad;
    }

    public void setTipoEntidad(String tipoEntidad) {
        this.tipoEntidad = tipoEntidad;
    }

    public String getDirectorio() {
        return directorio;
    }

    public void setDirectorio(String directorio) {
        this.directorio = directorio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
}
