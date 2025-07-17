package es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo;

public class SendRequestParams extends ModifyServiceBaseParams {
    
    private String idRequest;

    public SendRequestParams() {
    }

    public SendRequestParams(ModifyServiceBaseParams param) {
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.idRequest != null ? this.idRequest.hashCode() : 0);
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
        final SendRequestParams other = (SendRequestParams) obj;
        if ((this.idRequest == null) ? (other.idRequest != null) : !this.idRequest.equals(other.idRequest)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + " SendRequestParams{" + "idRequest=" + idRequest + '}';
    }

}
