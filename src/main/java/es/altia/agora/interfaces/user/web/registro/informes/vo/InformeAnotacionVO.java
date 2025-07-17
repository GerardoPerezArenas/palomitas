package es.altia.agora.interfaces.user.web.registro.informes.vo;

import es.altia.technical.ValidationException;
import es.altia.technical.ValueObject;
import java.io.Serializable;

public class InformeAnotacionVO implements Serializable, ValueObject {
    
    private String estado;
    private String expRelacionado;
    private String numAnotacion;
    private String fechaPresentacion;
    private String remitente;
    private String asunto;
    private String unidadDestino;

    public InformeAnotacionVO() {
        
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getExpRelacionado() {
        return expRelacionado;
    }

    public void setExpRelacionado(String expRelacionado) {
        this.expRelacionado = expRelacionado;
    }

    public String getNumAnotacion() {
        return numAnotacion;
    }

    public void setNumAnotacion(String numAnotacion) {
        this.numAnotacion = numAnotacion;
    }

    public String getFechaPresentacion() {
        return fechaPresentacion;
    }

    public void setFechaPresentacion(String fechaPresentacion) {
        this.fechaPresentacion = fechaPresentacion;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getUnidadDestino() {
        return unidadDestino;
    }

    public void setUnidadDestino(String unidadDestino) {
        this.unidadDestino = unidadDestino;
    }

    @Override
    public String toString() {
        return "InformeAnotacionVO{" + "estado=" + estado + ", expRelacionado=" + expRelacionado + ", numAnotacion=" + numAnotacion + ", fechaPresentacion=" + fechaPresentacion + ", remitente=" + remitente + ", asunto=" + asunto + ", unidadDestino=" + unidadDestino + '}';
    }

    public void validate(String idioma) throws ValidationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
