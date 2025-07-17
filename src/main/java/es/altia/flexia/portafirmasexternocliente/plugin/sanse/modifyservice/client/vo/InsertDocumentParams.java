package es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo;

import java.util.Arrays;

public class InsertDocumentParams extends ModifyServiceBaseParams {
    
    private String idRequest;
    private String nombreDocumento;
    private String tipoMime;
    private String tipoDocumentoId;
    private String tipoDocumentoDescripcion;
    private byte[] contenido;
    
    public InsertDocumentParams() {
    }

    public InsertDocumentParams(ModifyServiceBaseParams param) {
        if (param != null) {
            super.setCodOrganizacion(param.getCodOrganizacion());
            super.setEndPoint(param.getEndPoint());
            super.setPassword(param.getPassword());
            super.setUsername(param.getUsername());
        }
    }
    
    public String getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(String idRequest) {
        this.idRequest = idRequest;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public String getTipoDocumentoId() {
        return tipoDocumentoId;
    }

    public void setTipoDocumentoId(String tipoDocumentoId) {
        this.tipoDocumentoId = tipoDocumentoId;
    }

    public String getTipoDocumentoDescripcion() {
        return tipoDocumentoDescripcion;
    }

    public void setTipoDocumentoDescripcion(String tipoDocumentoDescripcion) {
        this.tipoDocumentoDescripcion = tipoDocumentoDescripcion;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.idRequest != null ? this.idRequest.hashCode() : 0);
        hash = 61 * hash + (this.nombreDocumento != null ? this.nombreDocumento.hashCode() : 0);
        hash = 61 * hash + (this.tipoMime != null ? this.tipoMime.hashCode() : 0);
        hash = 61 * hash + (this.tipoDocumentoId != null ? this.tipoDocumentoId.hashCode() : 0);
        hash = 61 * hash + (this.tipoDocumentoDescripcion != null ? this.tipoDocumentoDescripcion.hashCode() : 0);
        hash = 61 * hash + Arrays.hashCode(this.contenido);
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
        final InsertDocumentParams other = (InsertDocumentParams) obj;
        if ((this.idRequest == null) ? (other.idRequest != null) : !this.idRequest.equals(other.idRequest)) {
            return false;
        }
        if ((this.nombreDocumento == null) ? (other.nombreDocumento != null) : !this.nombreDocumento.equals(other.nombreDocumento)) {
            return false;
        }
        if ((this.tipoMime == null) ? (other.tipoMime != null) : !this.tipoMime.equals(other.tipoMime)) {
            return false;
        }
        if ((this.tipoDocumentoId == null) ? (other.tipoDocumentoId != null) : !this.tipoDocumentoId.equals(other.tipoDocumentoId)) {
            return false;
        }
        if ((this.tipoDocumentoDescripcion == null) ? (other.tipoDocumentoDescripcion != null) : !this.tipoDocumentoDescripcion.equals(other.tipoDocumentoDescripcion)) {
            return false;
        }
        if (!Arrays.equals(this.contenido, other.contenido)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + " InsertDocumentParams{" + "idRequest=" + idRequest + ", nombreDocumento=" + nombreDocumento + ", tipoMime=" + tipoMime + ", tipoDocumentoId=" + tipoDocumentoId + ", tipoDocumentoDescripcion=" + tipoDocumentoDescripcion + ", contenido=" + contenido + '}';
    }

}
