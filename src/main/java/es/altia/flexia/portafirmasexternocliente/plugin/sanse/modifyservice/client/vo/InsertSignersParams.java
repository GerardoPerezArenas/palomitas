package es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo;

import java.util.List;

public class InsertSignersParams extends ModifyServiceBaseParams {

    private String idRequest;
    private Integer lineaFirma;
    private List<String> listaIdFirmantes;

    public InsertSignersParams() {
    }

    public InsertSignersParams(ModifyServiceBaseParams param) {
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

    public Integer getLineaFirma() {
        return lineaFirma;
    }

    public void setLineaFirma(Integer lineaFirma) {
        this.lineaFirma = lineaFirma;
    }

    public List<String> getListaIdFirmantes() {
        return listaIdFirmantes;
    }

    public void setListaIdFirmantes(List<String> listaIdFirmantes) {
        this.listaIdFirmantes = listaIdFirmantes;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (this.idRequest != null ? this.idRequest.hashCode() : 0);
        hash = 43 * hash + (this.lineaFirma != null ? this.lineaFirma.hashCode() : 0);
        hash = 43 * hash + (this.listaIdFirmantes != null ? this.listaIdFirmantes.hashCode() : 0);
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
        final InsertSignersParams other = (InsertSignersParams) obj;
        if ((this.idRequest == null) ? (other.idRequest != null) : !this.idRequest.equals(other.idRequest)) {
            return false;
        }
        if (this.lineaFirma != other.lineaFirma && (this.lineaFirma == null || !this.lineaFirma.equals(other.lineaFirma))) {
            return false;
        }
        if (this.listaIdFirmantes != other.listaIdFirmantes && (this.listaIdFirmantes == null || !this.listaIdFirmantes.equals(other.listaIdFirmantes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + " InsertSignersParams{" + "idRequest=" + idRequest + ", lineaFirma=" + lineaFirma + ", listaIdFirmantes=" + listaIdFirmantes + '}';
    }

}
