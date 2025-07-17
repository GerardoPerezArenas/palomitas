/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.lanbide.impresion;

/**
 * Clase VO para los interesados del report
 * 
 * @author david.caamano
 * @version 15/10/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 15-10-2012 * </li>
 * </ol> 
 */
public class InteresadoGenerateReportVO {
    
    private String nombre;
    private String fechaNacimiento;
    private String fechaNacimientoAlt;
    private String codOficina;
    
    public String getNombre() {
        return nombre;
    }//getNombre
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }//setNombre

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }//getFechaNacimiento
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }//getFechaNacimiento
    
    public String getFechaNacimientoAlt() {
        return fechaNacimientoAlt;
    }//getFechaNacimientoAlt
    public void setFechaNacimientoAlt(String fechaNacimientoAlt) {
        this.fechaNacimientoAlt = fechaNacimientoAlt;
    }//setFechaNacimientoAlt
    
    public String getCodOficina() {
        return codOficina;
    }//getNombre
    public void setCodOficina(String codOficina) {
        this.codOficina = codOficina;
    }//setNombre

}//class