package es.altia.agora.business.sge;

import es.altia.agora.interfaces.user.web.util.FormateadorTercero;
import java.io.Serializable;

public class AsientoFichaExpedienteVO implements Serializable {
    
    private int codigoDepartamento;
    private int codigoUOR;
    private String tipoAsiento;
    private int ejercicioAsiento;
    private Long numeroAsiento;
    private String fechaAsiento;
    private String nombreInteresado;
    private String apellido1Interesado;
    private String apellido2Interesado;
    private boolean masInteresados;
    private String asuntoAsiento;
    private boolean observaciones;
    private String origenAsiento;
    private int codigoUOD;

    public AsientoFichaExpedienteVO() {
    }

    public AsientoFichaExpedienteVO(int codigoDepartamento, int codigoUOR, String tipoAsiento, int ejercicioAsiento, Long numeroAsiento, String fechaAsiento, String nombreInteresado, String apellido1Interesado, String apellido2Interesado, boolean masInteresados, String asuntoAsiento, boolean observaciones, String origenAsiento) {
        this.codigoDepartamento = codigoDepartamento;
        this.codigoUOR = codigoUOR;
        this.tipoAsiento = tipoAsiento;
        this.ejercicioAsiento = ejercicioAsiento;
        this.numeroAsiento = numeroAsiento;
        this.fechaAsiento = fechaAsiento;
        this.nombreInteresado = nombreInteresado;
        this.apellido1Interesado = apellido1Interesado;
        this.apellido2Interesado = apellido2Interesado;
        this.masInteresados = masInteresados;
        this.asuntoAsiento = asuntoAsiento;
        this.observaciones = observaciones;
        this.origenAsiento = origenAsiento;
    }

    
    public String getAsuntoAsiento() {
        return asuntoAsiento;
    }

    public String getNombreCompleto() {
        return FormateadorTercero.getDescTercero(this.nombreInteresado, this.apellido1Interesado, this.apellido2Interesado, this.masInteresados);
    }

    public void setAsuntoAsiento(String asuntoAsiento) {
        this.asuntoAsiento = asuntoAsiento;
    }
    
    public String getShortDescAsunto() {
        if (this.asuntoAsiento.length() > 40) return this.asuntoAsiento.substring(0,40) + "...";
        else return this.asuntoAsiento;
    }

    public int getCodigoDepartamento() {
        return codigoDepartamento;
    }

    public void setCodigoDepartamento(int codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    public int getCodigoUOR() {
        return codigoUOR;
    }

    public void setCodigoUOR(int codigoUOR) {
        this.codigoUOR = codigoUOR;
    }

    public int getEjercicioAsiento() {
        return ejercicioAsiento;
    }

    public void setEjercicioAsiento(int ejercicioAsiento) {
        this.ejercicioAsiento = ejercicioAsiento;
    }

    public String getFechaAsiento() {
        return fechaAsiento;
    }

    public void setFechaAsiento(String fechaAsiento) {
        this.fechaAsiento = fechaAsiento;
    }

    public Long getNumeroAsiento() {
        return numeroAsiento;
    }

    public void setNumeroAsiento(Long numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }
    

    public String getTipoAsiento() {
        return tipoAsiento;
    }

    public void setTipoAsiento(String tipoAsiento) {
        this.tipoAsiento = tipoAsiento;
    }
    
    public String getIdAsiento() {
        return this.ejercicioAsiento + "/" + this.numeroAsiento;
    }

    public String getOrigenAsiento() {
        return origenAsiento;
    }

    public void setOrigenAsiento(String origenAsiento) {
        this.origenAsiento = origenAsiento;
    }

    public String getApellido1Interesado() {
        return apellido1Interesado;
    }

    public void setApellido1Interesado(String apellido1Interesado) {
        this.apellido1Interesado = apellido1Interesado;
    }

    public String getApellido2Interesado() {
        return apellido2Interesado;
    }

    public void setApellido2Interesado(String apellido2Interesado) {
        this.apellido2Interesado = apellido2Interesado;
    }

    public String getNombreInteresado() {
        return nombreInteresado;
    }

    public void setNombreInteresado(String nombreInteresado) {
        this.nombreInteresado = nombreInteresado;
    }

    public boolean isMasInteresados() {
        return masInteresados;
    }

    public void setMasInteresados(boolean masInteresados) {
        this.masInteresados = masInteresados;
    }

    public boolean isObservaciones() {
        return observaciones;
    }

    public void setObservaciones(boolean observaciones) {
        this.observaciones = observaciones;
    }

    public int getCodigoUOD() {
        return codigoUOD;
    }

    public void setCodigoUOD(int codigoUOD) {
        this.codigoUOD = codigoUOD;
    }
    
    

}
