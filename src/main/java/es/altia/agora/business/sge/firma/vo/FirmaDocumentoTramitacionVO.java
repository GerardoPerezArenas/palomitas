package es.altia.agora.business.sge.firma.vo;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FirmaDocumentoTramitacionVO {

    FirmaDocumentoTramiteClave clave;
    FirmaFlujoVO flujo;
    List<FirmaFirmanteVO> firmantes;
    private String nombre;
    private byte[] fichero;
    private String estadoFirma;
    private Calendar fechaFirma;
    private byte[] firma;
    private String clientePortafirmasExterno;
    private String idSolicitudPortafirmasExterno;
    private String idDocumentoPortafirmasExterno;

    public FirmaDocumentoTramiteClave getClave() {
        return clave;
    }

    public void setClave(FirmaDocumentoTramiteClave clave) {
        this.clave = clave;
    }

    public FirmaFlujoVO getFlujo() {
        return flujo;
    }

    public void setFlujo(FirmaFlujoVO flujo) {
        this.flujo = flujo;
    }

    public List<FirmaFirmanteVO> getFirmantes() {
        return firmantes;
    }

    public void setFirmantes(List<FirmaFirmanteVO> firmantes) {
        this.firmantes = firmantes;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getFichero() {
        return fichero;
    }

    public void setFichero(byte[] fichero) {
        this.fichero = fichero;
    }

    public String getEstadoFirma() {
        return estadoFirma;
    }

    public void setEstadoFirma(String estadoFirma) {
        this.estadoFirma = estadoFirma;
    }

    public Calendar getFechaFirma() {
        return fechaFirma;
    }

    public void setFechaFirma(Calendar fechaFirma) {
        this.fechaFirma = fechaFirma;
    }

    public byte[] getFirma() {
        return firma;
    }

    public void setFirma(byte[] firma) {
        this.firma = firma;
    }

    public String getClientePortafirmasExterno() {
        return clientePortafirmasExterno;
    }

    public void setClientePortafirmasExterno(String clientePortafirmasExterno) {
        this.clientePortafirmasExterno = clientePortafirmasExterno;
    }

    public String getIdSolicitudPortafirmasExterno() {
        return idSolicitudPortafirmasExterno;
    }

    public void setIdSolicitudPortafirmasExterno(String idSolicitudPortafirmasExterno) {
        this.idSolicitudPortafirmasExterno = idSolicitudPortafirmasExterno;
    }

    public String getIdDocumentoPortafirmasExterno() {
        return idDocumentoPortafirmasExterno;
    }

    public void setIdDocumentoPortafirmasExterno(String idDocumentoPortafirmasExterno) {
        this.idDocumentoPortafirmasExterno = idDocumentoPortafirmasExterno;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.clave != null ? this.clave.hashCode() : 0);
        hash = 29 * hash + (this.flujo != null ? this.flujo.hashCode() : 0);
        hash = 29 * hash + (this.firmantes != null ? this.firmantes.hashCode() : 0);
        hash = 29 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        hash = 29 * hash + Arrays.hashCode(this.fichero);
        hash = 29 * hash + (this.estadoFirma != null ? this.estadoFirma.hashCode() : 0);
        hash = 29 * hash + (this.fechaFirma != null ? this.fechaFirma.hashCode() : 0);
        hash = 29 * hash + Arrays.hashCode(this.firma);
        hash = 29 * hash + (this.clientePortafirmasExterno != null ? this.clientePortafirmasExterno.hashCode() : 0);
        hash = 29 * hash + (this.idSolicitudPortafirmasExterno != null ? this.idSolicitudPortafirmasExterno.hashCode() : 0);
        hash = 29 * hash + (this.idDocumentoPortafirmasExterno != null ? this.idDocumentoPortafirmasExterno.hashCode() : 0);
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
        final FirmaDocumentoTramitacionVO other = (FirmaDocumentoTramitacionVO) obj;
        if (this.clave != other.clave && (this.clave == null || !this.clave.equals(other.clave))) {
            return false;
        }
        if (this.flujo != other.flujo && (this.flujo == null || !this.flujo.equals(other.flujo))) {
            return false;
        }
        if (this.firmantes != other.firmantes && (this.firmantes == null || !this.firmantes.equals(other.firmantes))) {
            return false;
        }
        if ((this.nombre == null) ? (other.nombre != null) : !this.nombre.equals(other.nombre)) {
            return false;
        }
        if (!Arrays.equals(this.fichero, other.fichero)) {
            return false;
        }
        if ((this.estadoFirma == null) ? (other.estadoFirma != null) : !this.estadoFirma.equals(other.estadoFirma)) {
            return false;
        }
        if (this.fechaFirma != other.fechaFirma && (this.fechaFirma == null || !this.fechaFirma.equals(other.fechaFirma))) {
            return false;
        }
        if (!Arrays.equals(this.firma, other.firma)) {
            return false;
        }
        if ((this.clientePortafirmasExterno == null) ? (other.clientePortafirmasExterno != null) : !this.clientePortafirmasExterno.equals(other.clientePortafirmasExterno)) {
            return false;
        }
        if ((this.idSolicitudPortafirmasExterno == null) ? (other.idSolicitudPortafirmasExterno != null) : !this.idSolicitudPortafirmasExterno.equals(other.idSolicitudPortafirmasExterno)) {
            return false;
        }
        if ((this.idDocumentoPortafirmasExterno == null) ? (other.idDocumentoPortafirmasExterno != null) : !this.idDocumentoPortafirmasExterno.equals(other.idDocumentoPortafirmasExterno)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FirmaDocumentoTramiteVO{" + "clave=" + clave + ", flujo=" + flujo + ", firmantes=" + firmantes + ", nombre=" + nombre + ", fichero=" + fichero + ", estadoFirma=" + estadoFirma + ", fechaFirma=" + fechaFirma + ", firma=" + firma + ", clientePortafirmasExterno=" + clientePortafirmasExterno + ", idSolicitudPortafirmasExterno=" + idSolicitudPortafirmasExterno + ", idDocumentoPortafirmasExterno=" + idDocumentoPortafirmasExterno + '}';
    }

}
