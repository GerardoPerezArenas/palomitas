/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.util.notificaciones;


import es.altia.technical.ValueObject;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;


public class TipoNotificacionValueObject  {

    private String codigo;
    private String descripcion;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    

}
