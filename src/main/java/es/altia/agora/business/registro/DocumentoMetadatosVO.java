package es.altia.agora.business.registro;

import java.io.Serializable;
import java.util.Calendar;

public class DocumentoMetadatosVO implements Serializable {

    private Integer departamento;
    private Integer uor;
    private Integer ejercicio;
     private Long numero;
    private String tipoRegistro;
    private String nombreDoc;
    private String versionNTI;
    private Long idDocumento; // El campo en BD es un number(30,0). Se presupone que nunca se llegará a sobrepasar el tipo Long.
    private String organo;
    private Calendar fechaCaptura;
    private Integer origen;
    private Integer estadoElaboracion;
    private String nombreFormato;
    private Integer tipoDocumental;
    private Integer tipoFirma;
    private String metadatoId;
    private String metadatoValor;
    
    public DocumentoMetadatosVO() {
    }

    public Integer getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Integer departamento) {
        this.departamento = departamento;
    }

    public Integer getUor() {
        return uor;
    }

    public void setUor(Integer uor) {
        this.uor = uor;
    }

    public Integer getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Integer ejercicio) {
        this.ejercicio = ejercicio;
    }

    public Long  getNumero() {
        return numero;
    }

    public void setNumero(Long  numero) {
        this.numero = numero;
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getNombreDoc() {
        return nombreDoc;
    }

    public void setNombreDoc(String nombreDoc) {
        this.nombreDoc = nombreDoc;
    }

    public String getVersionNTI() {
        return versionNTI;
    }

    public void setVersionNTI(String versionNTI) {
        this.versionNTI = versionNTI;
    }

    public Long getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Long idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getOrgano() {
        return organo;
    }

    public void setOrgano(String organo) {
        this.organo = organo;
    }

    public Calendar getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Calendar fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public Integer getOrigen() {
        return origen;
    }

    public void setOrigen(Integer origen) {
        this.origen = origen;
    }

    public Integer getEstadoElaboracion() {
        return estadoElaboracion;
    }

    public void setEstadoElaboracion(Integer estadoElaboracion) {
        this.estadoElaboracion = estadoElaboracion;
    }

    public String getNombreFormato() {
        return nombreFormato;
    }

    public void setNombreFormato(String nombreFormato) {
        this.nombreFormato = nombreFormato;
    }

    public Integer getTipoDocumental() {
        return tipoDocumental;
    }

    public void setTipoDocumental(Integer tipoDocumental) {
        this.tipoDocumental = tipoDocumental;
    }

    public Integer getTipoFirma() {
        return tipoFirma;
    }

    public void setTipoFirma(Integer tipoFirma) {
        this.tipoFirma = tipoFirma;
    }

    public String getMetadatoId() {
        return metadatoId;
    }

    public void setMetadatoId(String metadatoId) {
        this.metadatoId = metadatoId;
    }

    public String getMetadatoValor() {
        return metadatoValor;
    }

    public void setMetadatoValor(String metadatoValor) {
        this.metadatoValor = metadatoValor;
    }
    

}
