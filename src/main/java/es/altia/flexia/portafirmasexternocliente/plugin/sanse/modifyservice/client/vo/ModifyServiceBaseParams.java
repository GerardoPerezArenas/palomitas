package es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo;

public class ModifyServiceBaseParams {

    private String endPoint;
    private String username;
    private String password;
    private Integer codOrganizacion;

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(Integer codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.endPoint != null ? this.endPoint.hashCode() : 0);
        hash = 59 * hash + (this.username != null ? this.username.hashCode() : 0);
        hash = 59 * hash + (this.password != null ? this.password.hashCode() : 0);
        hash = 59 * hash + (this.codOrganizacion != null ? this.codOrganizacion.hashCode() : 0);
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
        final ModifyServiceBaseParams other = (ModifyServiceBaseParams) obj;
        if ((this.endPoint == null) ? (other.endPoint != null) : !this.endPoint.equals(other.endPoint)) {
            return false;
        }
        if ((this.username == null) ? (other.username != null) : !this.username.equals(other.username)) {
            return false;
        }
        if ((this.password == null) ? (other.password != null) : !this.password.equals(other.password)) {
            return false;
        }
        if (this.codOrganizacion != other.codOrganizacion && (this.codOrganizacion == null || !this.codOrganizacion.equals(other.codOrganizacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ModifyServiceBaseParams{" + "endPoint=" + endPoint + ", username=" + username + ", password=" + password + ", codOrganizacion=" + codOrganizacion + '}';
    }

}
