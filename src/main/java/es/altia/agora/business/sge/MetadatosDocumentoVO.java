package es.altia.agora.business.sge;

import java.io.Serializable;

public class MetadatosDocumentoVO implements Serializable {

    private Long id;
    private String csv;
    private String csvAplicacion;
    private String csvUri;

    public MetadatosDocumentoVO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public String getCsvAplicacion() {
        return csvAplicacion;
    }

    public void setCsvAplicacion(String csvAplicacion) {
        this.csvAplicacion = csvAplicacion;
    }

    public String getCsvUri() {
        return csvUri;
    }

    public void setCsvUri(String csvUri) {
        this.csvUri = csvUri;
    }

    @Override
    public String toString() {
        return String.format(
                "MetadatosDocumentoVO{id=%d, csv=%s, csvAplicacion=%s, csvUri=%s}",
                id,
                csv,
                csvAplicacion,
                csvUri);
    }

}
