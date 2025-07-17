package es.altia.agora.interfaces.user.web.integracionsw;

import org.apache.struts.action.ActionForm;

public class AltaSWForm extends ActionForm {

    private String tituloNewSW;
    private String wsdlNewSW;
    private String codigoSW;

    public String getCodigoSW() {
        return codigoSW;
    }

    public void setCodigoSW(String codigoSW) {
        this.codigoSW = codigoSW;
    }

    public String getTituloNewSW() {
        return tituloNewSW;
    }

    public void setTituloNewSW(String tituloNewSW) {
        this.tituloNewSW = tituloNewSW;
    }

    public String getWsdlNewSW() {
        return wsdlNewSW;
    }

    public void setWsdlNewSW(String wsdlNewSW) {
        this.wsdlNewSW = wsdlNewSW;
    }

}
