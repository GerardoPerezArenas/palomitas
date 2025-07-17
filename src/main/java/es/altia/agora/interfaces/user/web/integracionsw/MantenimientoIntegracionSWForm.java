package es.altia.agora.interfaces.user.web.integracionsw;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.struts.action.ActionForm;

import java.util.Vector;

public class MantenimientoIntegracionSWForm extends ActionForm {
    Vector listaSW = new Vector();
    JSONArray listaSWCod = new JSONArray();
    JSONArray listaSWNombre = new JSONArray();
    JSONArray listaSWPub = new JSONArray();


    public String toJSONString() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("listaSWCod", this.listaSWCod);
        jsonObj.put("listaSWNombre", this.listaSWNombre);
        jsonObj.put("listaSWPub", this.listaSWPub);
        return jsonObj.toString();
    }


    public JSONArray getlistaSWCod() {
        return listaSWCod;
    }


    public void setlistaSWCod(JSONArray listaSWCod) {
        this.listaSWCod = listaSWCod;
    }


    public JSONArray getlistaSWNombre() {
        return listaSWNombre;
    }


    public void setlistaSWNombre(JSONArray listaSWNombre) {
        this.listaSWNombre = listaSWNombre;
    }


    public JSONArray getlistaSWPub() {
        return listaSWPub;
    }


    public void setlistaSWPub(JSONArray listaSWPub) {
        this.listaSWPub = listaSWPub;
    }


    public Vector getListaSW() {
        return listaSW;
    }


    public void setListaSW(Vector listaSW) {
        this.listaSW = listaSW;
    }


}
