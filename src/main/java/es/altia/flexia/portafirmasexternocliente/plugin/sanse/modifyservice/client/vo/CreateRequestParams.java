package es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo;

import es.altia.agora.business.sge.firma.vo.FirmaCircuitoVO;
import java.util.Calendar;
import java.util.List;

public class CreateRequestParams extends ModifyServiceBaseParams {

    private String referencia;
    private String aplicacion;
    private Calendar fechaInicio;
    private Calendar fechaExpiracion;
    private String asunto;
    private String texto;
    private String IdRemitente;
    private String tipoFirma;
    private Boolean selloDeTiempo;
    private String nivelImportanciaId;
    private String nivelImportanciaDescripcion;
    private List<FirmaCircuitoVO> listaFirmantes;

    public CreateRequestParams() {
    }

    public CreateRequestParams(ModifyServiceBaseParams param) {
        if (param != null) {
            super.setCodOrganizacion(param.getCodOrganizacion());
            super.setEndPoint(param.getEndPoint());
            super.setPassword(param.getPassword());
            super.setUsername(param.getUsername());
        }
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    public Calendar getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Calendar getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Calendar fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getIdRemitente() {
        return IdRemitente;
    }

    public void setIdRemitente(String IdRemitente) {
        this.IdRemitente = IdRemitente;
    }

    public String getTipoFirma() {
        return tipoFirma;
    }

    public void setTipoFirma(String tipoFirma) {
        this.tipoFirma = tipoFirma;
    }

    public Boolean isSelloDeTiempo() {
        return selloDeTiempo;
    }

    public void setSelloDeTiempo(Boolean selloDeTiempo) {
        this.selloDeTiempo = selloDeTiempo;
    }

    public String getNivelImportanciaId() {
        return nivelImportanciaId;
    }

    public void setNivelImportanciaId(String nivelImportanciaId) {
        this.nivelImportanciaId = nivelImportanciaId;
    }

    public String getNivelImportanciaDescripcion() {
        return nivelImportanciaDescripcion;
    }

    public void setNivelImportanciaDescripcion(String nivelImportanciaDescripcion) {
        this.nivelImportanciaDescripcion = nivelImportanciaDescripcion;
    }

    public List<FirmaCircuitoVO> getListaFirmantes() {
        return listaFirmantes;
    }

    public void setListaFirmantes(List<FirmaCircuitoVO> listaFirmantes) {
        this.listaFirmantes = listaFirmantes;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.referencia != null ? this.referencia.hashCode() : 0);
        hash = 97 * hash + (this.aplicacion != null ? this.aplicacion.hashCode() : 0);
        hash = 97 * hash + (this.fechaInicio != null ? this.fechaInicio.hashCode() : 0);
        hash = 97 * hash + (this.fechaExpiracion != null ? this.fechaExpiracion.hashCode() : 0);
        hash = 97 * hash + (this.asunto != null ? this.asunto.hashCode() : 0);
        hash = 97 * hash + (this.texto != null ? this.texto.hashCode() : 0);
        hash = 97 * hash + (this.IdRemitente != null ? this.IdRemitente.hashCode() : 0);
        hash = 97 * hash + (this.tipoFirma != null ? this.tipoFirma.hashCode() : 0);
        hash = 97 * hash + (this.selloDeTiempo != null ? this.selloDeTiempo.hashCode() : 0);
        hash = 97 * hash + (this.nivelImportanciaId != null ? this.nivelImportanciaId.hashCode() : 0);
        hash = 97 * hash + (this.nivelImportanciaDescripcion != null ? this.nivelImportanciaDescripcion.hashCode() : 0);
        hash = 97 * hash + (this.listaFirmantes != null ? this.listaFirmantes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CreateRequestParams other = (CreateRequestParams) obj;
        if ((this.referencia == null) ? (other.referencia != null) : !this.referencia.equals(other.referencia)) {
            return false;
        }
        if ((this.aplicacion == null) ? (other.aplicacion != null) : !this.aplicacion.equals(other.aplicacion)) {
            return false;
        }
        if (this.fechaInicio != other.fechaInicio && (this.fechaInicio == null || !this.fechaInicio.equals(other.fechaInicio))) {
            return false;
        }
        if (this.fechaExpiracion != other.fechaExpiracion && (this.fechaExpiracion == null || !this.fechaExpiracion.equals(other.fechaExpiracion))) {
            return false;
        }
        if ((this.asunto == null) ? (other.asunto != null) : !this.asunto.equals(other.asunto)) {
            return false;
        }
        if ((this.texto == null) ? (other.texto != null) : !this.texto.equals(other.texto)) {
            return false;
        }
        if ((this.IdRemitente == null) ? (other.IdRemitente != null) : !this.IdRemitente.equals(other.IdRemitente)) {
            return false;
        }
        if ((this.tipoFirma == null) ? (other.tipoFirma != null) : !this.tipoFirma.equals(other.tipoFirma)) {
            return false;
        }
        if (this.selloDeTiempo != other.selloDeTiempo && (this.selloDeTiempo == null || !this.selloDeTiempo.equals(other.selloDeTiempo))) {
            return false;
        }
        if ((this.nivelImportanciaId == null) ? (other.nivelImportanciaId != null) : !this.nivelImportanciaId.equals(other.nivelImportanciaId)) {
            return false;
        }
        if ((this.nivelImportanciaDescripcion == null) ? (other.nivelImportanciaDescripcion != null) : !this.nivelImportanciaDescripcion.equals(other.nivelImportanciaDescripcion)) {
            return false;
        }
        if (this.listaFirmantes != other.listaFirmantes && (this.listaFirmantes == null || !this.listaFirmantes.equals(other.listaFirmantes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CreateRequestParams{" + "referencia=" + referencia + ", aplicacion=" + aplicacion + ", fechaInicio=" + fechaInicio + ", fechaExpiracion=" + fechaExpiracion + ", asunto=" + asunto + ", texto=" + texto + ", IdRemitente=" + IdRemitente + ", tipoFirma=" + tipoFirma + ", selloDeTiempo=" + selloDeTiempo + ", nivelImportanciaId=" + nivelImportanciaId + ", nivelImportanciaDescripcion=" + nivelImportanciaDescripcion + ", listaFirmantes=" + listaFirmantes + '}';
    }

}
