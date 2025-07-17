package es.altia.agora.business.registro;

import java.io.Serializable;

/**
 * Contiene tan sólo los atributos que son clave de un asiento, es decir,
 * unidad orgánica, departamento, tipo, ejercicio y número.
 *
 */

public class SimpleRegistroValueObject implements Serializable {

    static final long serialVersionUID=0;
    
    private String uor;
    private String dep;
    private String tipo;
    private String ejercicio;
    private String numero;
    public String getUor() {
        return uor;
    }
    public void setUor(String uor) {
        this.uor = uor;
    }
    public String getDep() {
        return dep;
    }
    public void setDep(String dep) {
        this.dep = dep;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getEjercicio() {
        return ejercicio;
    }
    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public String toString() {
        String desc = "Uor: " + uor + " ,dep: " + dep + ", " + ejercicio + "/" + numero + "-" + tipo;
        return desc;
    }
    
}
