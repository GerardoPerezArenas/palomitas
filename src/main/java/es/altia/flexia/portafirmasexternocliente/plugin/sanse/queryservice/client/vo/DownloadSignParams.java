package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo;

public class DownloadSignParams extends QueryServiceBaseParams {
    
    private String idDocumento;

    public DownloadSignParams() {
    }

    public DownloadSignParams(QueryServiceBaseParams param) {
        if (param != null) {
            super.setCodOrganizacion(param.getCodOrganizacion());
            super.setEndPoint(param.getEndPoint());
            super.setPassword(param.getPassword());
            super.setUsername(param.getUsername());
        }
    }
    
    public String getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.idDocumento != null ? this.idDocumento.hashCode() : 0);
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
        final DownloadSignParams other = (DownloadSignParams) obj;
        if ((this.idDocumento == null) ? (other.idDocumento != null) : !this.idDocumento.equals(other.idDocumento)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + " DownloadSignParams{" + "idDocumento=" + idDocumento + '}';
    }

}
