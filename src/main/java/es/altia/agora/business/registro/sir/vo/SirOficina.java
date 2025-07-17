/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.registro.sir.vo;

/**
 * BD: SIR_OFICINA
 * @author INGDGC
 * 
 */
public class SirOficina {
    
    private String codigo; //varchar2(100 byte)  primary key
    private String nombre_ES; //varchar2(1500 byte)  not null
    private String nombre_EU; //varchar2(1500 byte) 
    
    public SirOficina(String codigo, String nombre_ES, String nombre_EU) {
        this.codigo = codigo;
        this.nombre_ES = nombre_ES;
        this.nombre_EU = nombre_EU;
    }

    public SirOficina(){};

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre_ES() {
        return nombre_ES;
    }

    public void setNombre_ES(String nombre_ES) {
        this.nombre_ES = nombre_ES;
    }

    public String getNombre_EU() {
        return nombre_EU;
    }

    public void setNombre_EU(String nombre_EU) {
        this.nombre_EU = nombre_EU;
    }

    @Override
    public String toString() {
        return "SirOficina{" + "codigo=" + codigo + ", nombre_ES=" + nombre_ES + ", nombre_EU=" + nombre_EU + '}';
    }

}
