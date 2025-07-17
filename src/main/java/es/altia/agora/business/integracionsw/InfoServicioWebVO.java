package es.altia.agora.business.integracionsw;

import java.io.Serializable;
import java.util.Collection;

public class InfoServicioWebVO implements Serializable {

    private int codigoSW;
    private String tituloSW;
    private String urlAccesoSW;
    private String wsdlSW;
    private boolean estaPublicado;
    private Collection operacionesSW;


    public InfoServicioWebVO(int codigoSW, String tituloSW, String urlAccesoSW, String wsdlSW, boolean estaPublicado) {
        this.codigoSW = codigoSW;
        this.tituloSW = tituloSW;
        this.urlAccesoSW = urlAccesoSW;
        this.wsdlSW = wsdlSW;
        this.estaPublicado = estaPublicado;
    }


    public int getCodigoSW() {
        return codigoSW;
    }

    public void setCodigoSW(int codigoSW) {
        this.codigoSW = codigoSW;
    }

    public String getTituloSW() {
        return tituloSW;
    }

    public void setTituloSW(String tituloSW) {
        this.tituloSW = tituloSW;
    }

    public String getUrlAccesoSW() {
        return urlAccesoSW;
    }

    public void setUrlAccesoSW(String urlAccesoSW) {
        this.urlAccesoSW = urlAccesoSW;
    }

    public String getWsdlSW() {
        return wsdlSW;
    }

    public void setWsdlSW(String wsdlSW) {
        this.wsdlSW = wsdlSW;
    }

    public boolean isEstaPublicado() {
        return estaPublicado;
    }

    public void setEstaPublicado(boolean estaPublicado) {
        this.estaPublicado = estaPublicado;
    }

    public Collection getOperacionesSW() {
        return operacionesSW;
    }

    public void setOperacionesSW(Collection operacionesSW) {
        this.operacionesSW = operacionesSW;
    }

    public String toString() {
        return "InfoServicioWebVO--> codigoSW: " + this.codigoSW + " | tituloSW: " + this.getTituloSW() +
                " | urlAccesoSW: " + this.urlAccesoSW + " | wsdlSW: " + this.wsdlSW + " | estaPublicado: " +
                this.estaPublicado + "\nOperaciones -->" + this.operacionesSW;
    }
    
}
