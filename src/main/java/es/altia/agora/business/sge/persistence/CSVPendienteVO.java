package es.altia.agora.business.sge.persistence;

import java.io.Serializable;

public class CSVPendienteVO implements Serializable {

    private Long idToken;
    private String csv;

    public CSVPendienteVO() {
    }

    public Long getIdToken() {
        return idToken;
    }

    public void setIdToken(Long idToken) {
        this.idToken = idToken;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    @Override
    public String toString() {
        return String.format(
                "CSVPendienteVO{idToken=%d, csv=%s}",
                idToken,
                csv);
    }

}
