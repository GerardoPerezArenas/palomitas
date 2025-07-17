package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo;

public class QueryUsersParams extends QueryServiceBaseParams {

    private String identificadorUsuario;

    public QueryUsersParams() {
    }

    public QueryUsersParams(QueryServiceBaseParams param) {
        if (param != null) {
            super.setCodOrganizacion(param.getCodOrganizacion());
            super.setEndPoint(param.getEndPoint());
            super.setPassword(param.getPassword());
            super.setUsername(param.getUsername());
        }
    }
    
    public String getIdentificadorUsuario() {
        return identificadorUsuario;
    }

    public void setIdentificadorUsuario(String nifUsuario) {
        this.identificadorUsuario = nifUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.identificadorUsuario != null ? this.identificadorUsuario.hashCode() : 0);
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
        final QueryUsersParams other = (QueryUsersParams) obj;
        if ((this.identificadorUsuario == null) ? (other.identificadorUsuario != null) : !this.identificadorUsuario.equals(other.identificadorUsuario)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + " QueryUsersParams{" + "identificadorUsuario=" + identificadorUsuario + '}';
    }
    
}
