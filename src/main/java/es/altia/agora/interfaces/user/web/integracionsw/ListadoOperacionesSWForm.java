package es.altia.agora.interfaces.user.web.integracionsw;

import org.apache.struts.action.ActionForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class ListadoOperacionesSWForm extends ActionForm {

    private String codigoSW;
    private String tituloSW;
    private String wsdlSW;
    private Vector listaOperaciones;
    private String error;

    JSONArray listaOperacionesCod = new JSONArray();
    JSONArray listaOperacionesNombre = new JSONArray();
    JSONArray listaOperacionesPub = new JSONArray();
    JSONArray msjError = new JSONArray();

    public String toJSONStringParamIn() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("listaOperacionesCod", this.listaOperacionesCod);
        jsonObj.put("listaOperacionesNombre", this.listaOperacionesNombre);
        jsonObj.put("listaOperacionesPub", this.listaOperacionesPub);
        jsonObj.put("msjError", this.msjError);
        return jsonObj.toString();
    }

    public String getCodigoSW() {
        return codigoSW;
    }

    public void setCodigoSW(String codigoSW) {
        this.codigoSW = codigoSW;
    }

    public String getTituloSW() {
        return tituloSW;
    }

    public void setTituloSW(String tituloSW) {
        this.tituloSW = tituloSW;
    }

    public String getWsdlSW() {
        return wsdlSW;
    }

    public void setWsdlSW(String wsdlSW) {
        this.wsdlSW = wsdlSW;
    }

    public Vector getListaOperaciones() {
        return listaOperaciones;
    }

    public void setListaOperaciones(Vector listaOperaciones) {
        this.listaOperaciones = listaOperaciones;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public JSONArray getListaOperacionesCod() {
        return listaOperacionesCod;
    }

    public void setListaOperacionesCod(JSONArray listaOperacionesCod) {
        this.listaOperacionesCod = listaOperacionesCod;
    }

    public JSONArray getListaOperacionesNombre() {
        return listaOperacionesNombre;
    }

    public void setListaOperacionesNombre(JSONArray listaOperacionesNombre) {
        this.listaOperacionesNombre = listaOperacionesNombre;
    }

    public JSONArray getListaOperacionesPub() {
        return listaOperacionesPub;
    }

    public void setListaOperacionesPub(JSONArray listaOperacionesPub) {
        this.listaOperacionesPub = listaOperacionesPub;
    }

    public JSONArray getMsjError() {
        return msjError;
    }

    public void setMsjError(JSONArray msjError) {
        this.msjError = msjError;
    }
}
