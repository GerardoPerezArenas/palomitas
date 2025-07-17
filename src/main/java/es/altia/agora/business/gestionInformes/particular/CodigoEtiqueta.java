package es.altia.agora.business.gestionInformes.particular;

import java.io.Serializable;

public class CodigoEtiqueta implements Serializable {

    private String codigo;
    private String etiqueta;

    public CodigoEtiqueta(String codigo, String etiqueta) {
        this.codigo = codigo;
        this.etiqueta = etiqueta;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }
}
