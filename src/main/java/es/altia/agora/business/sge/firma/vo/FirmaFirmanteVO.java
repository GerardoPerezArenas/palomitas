package es.altia.agora.business.sge.firma.vo;

import java.util.Arrays;
import java.util.Calendar;

public class FirmaFirmanteVO {
    
    private Integer id;
    private String login;
    private String nombre;
    private String documento;
    private Integer orden;
    private String estadoFirma;
    private Calendar fechaFirma;
    private byte[] firma;
    private String observaciones;

    public FirmaFirmanteVO() {
    }

    public FirmaFirmanteVO(Integer id, String login, String nombre, String documento, Integer orden, String estadoFirma, Calendar fechaFirma, byte[] firma) {
        this.id = id;
        this.login = login;
        this.nombre = nombre;
        this.documento = documento;
        this.orden = orden;
        this.estadoFirma = estadoFirma;
        this.fechaFirma = fechaFirma;
        this.firma = firma;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
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
    
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 29 * hash + (this.login != null ? this.login.hashCode() : 0);
        hash = 29 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        hash = 29 * hash + (this.documento != null ? this.documento.hashCode() : 0);
        hash = 29 * hash + (this.orden != null ? this.orden.hashCode() : 0);
        hash = 29 * hash + (this.estadoFirma != null ? this.estadoFirma.hashCode() : 0);
        hash = 29 * hash + (this.fechaFirma != null ? this.fechaFirma.hashCode() : 0);
        hash = 29 * hash + Arrays.hashCode(this.firma);
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
        final FirmaFirmanteVO other = (FirmaFirmanteVO) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.login == null) ? (other.login != null) : !this.login.equals(other.login)) {
            return false;
        }
        if ((this.nombre == null) ? (other.nombre != null) : !this.nombre.equals(other.nombre)) {
            return false;
        }
        if ((this.documento == null) ? (other.documento != null) : !this.documento.equals(other.documento)) {
            return false;
        }
        if (this.orden != other.orden && (this.orden == null || !this.orden.equals(other.orden))) {
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
        return true;
    }

    @Override
    public String toString() {
        return "FirmaFirmanteVO{" + "id=" + id + ", login=" + login + ", nombre=" + nombre + ", documento=" + documento + ", orden=" + orden + ", estadoFirma=" + estadoFirma + ", fechaFirma=" + fechaFirma + ", firma=" + firma + '}';
    }

    public FirmaFirmanteVO copy() {
        Calendar fecha = null;
        if (this.fechaFirma != null) {
            fecha = (Calendar) this.fechaFirma.clone();
        }
        
        FirmaFirmanteVO clone = new FirmaFirmanteVO(
                this.id,
                this.login,
                this.nombre,
                this.documento,
                this.orden,
                this.estadoFirma,
                fecha,
                this.firma);
    
        return clone;
    }

}
