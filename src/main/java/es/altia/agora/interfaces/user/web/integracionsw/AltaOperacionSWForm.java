package es.altia.agora.interfaces.user.web.integracionsw;

import org.apache.struts.action.ActionForm;

import java.util.Vector;

public class AltaOperacionSWForm extends ActionForm {

    private String codigoSW;
    private String nombreOpSW;
    private String tituloOpSW;
    private String codigoOpDef;
    private Vector operacionesCombo;

    public Vector getOperacionesCombo() {
        return operacionesCombo;
    }

    public void setOperacionesCombo(Vector operacionesCombo) {
        this.operacionesCombo = operacionesCombo;
    }

    public String getCodigoSW() {
        return codigoSW;
    }

    public void setCodigoSW(String codigoSW) {
        this.codigoSW = codigoSW;
    }

    public String getNombreOpSW() {
        return nombreOpSW;
    }

    public void setNombreOpSW(String nombreOpSW) {
        this.nombreOpSW = nombreOpSW;
    }

    public String getTituloOpSW() {
        return tituloOpSW;
    }

    public void setTituloOpSW(String tituloOpSW) {
        this.tituloOpSW = tituloOpSW;
    }

    public String getCodigoOpDef() {
        return codigoOpDef;
    }

    public void setCodigoOpDef(String codigoOpDef) {
        this.codigoOpDef = codigoOpDef;
    }
}
