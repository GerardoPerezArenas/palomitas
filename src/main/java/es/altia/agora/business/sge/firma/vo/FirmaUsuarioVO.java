package es.altia.agora.business.sge.firma.vo;

public class FirmaUsuarioVO {
    
    private Integer idUsuario;
    private String login;
    private String nombre;
    private String documento;
    private String buzonFirma;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
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
    
    public String getBuzonFirma() {
        return buzonFirma;
    }

    public void setBuzonFirma(String buzonFirma) {
        this.buzonFirma = buzonFirma;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.idUsuario != null ? this.idUsuario.hashCode() : 0);
        hash = 37 * hash + (this.login != null ? this.login.hashCode() : 0);
        hash = 37 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        hash = 37 * hash + (this.documento != null ? this.documento.hashCode() : 0);
        hash = 37 * hash + (this.buzonFirma != null ? this.buzonFirma.hashCode() : 0);
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
        final FirmaUsuarioVO other = (FirmaUsuarioVO) obj;
        if (this.idUsuario != other.idUsuario && (this.idUsuario == null || !this.idUsuario.equals(other.idUsuario))) {
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
        if ((this.buzonFirma == null) ? (other.buzonFirma != null) : !this.buzonFirma.equals(other.buzonFirma)) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "FirmaUsuarioVO{" + "idUsuario=" + idUsuario + ", login=" + login + ", nombre=" + nombre + ", documento=" + documento + ", buzonFirma=" + buzonFirma +'}';
    }
    
}
