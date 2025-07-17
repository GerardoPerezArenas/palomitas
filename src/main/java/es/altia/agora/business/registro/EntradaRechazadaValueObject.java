
package es.altia.agora.business.registro;

import java.util.Calendar;

public class EntradaRechazadaValueObject {
    private long numReg;
    private int ejercicio;
    private String tipo;
    private int codOrganizacion;
    
    private Calendar fechaRechazo;
 
    private String correoUsuario;
    
    public void setCorreoUsuario(String correoUsuario){
        this.correoUsuario = correoUsuario;
    }
    
    public String getCorreoUsuario(){
        return correoUsuario;
    }

    public long getNumReg() {
        return numReg;
    }

    public void setNumReg(long numReg) {
        this.numReg = numReg;
    }

    public int getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public Calendar getFechaRechazo() {
        return fechaRechazo;
    }

    public void setFechaRechazo(Calendar fechaRechazo) {
        this.fechaRechazo = fechaRechazo;
    }

    
    @Override
    public boolean equals(Object other){
        if(!(other instanceof EntradaRechazadaValueObject)){
            return false;
        }
        EntradaRechazadaValueObject that = (EntradaRechazadaValueObject) other;
        return this.ejercicio==that.ejercicio && this.numReg==that.numReg; 
    }
  }
