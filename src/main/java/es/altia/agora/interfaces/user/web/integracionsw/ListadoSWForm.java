package es.altia.agora.interfaces.user.web.integracionsw;

import org.apache.struts.action.ActionForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class ListadoSWForm extends ActionForm {

    Vector listaSW = new Vector();
    JSONArray listaSWCod = new JSONArray();
    JSONArray listaSWNombre = new JSONArray();
    JSONArray listaSWPub = new JSONArray();
    JSONArray jsonError = new JSONArray();

    private String error;

    public String toJSONStringServicios() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("listaCodigosSW", this.listaSWCod);
        jsonObj.put("listaTitulosSW", this.listaSWNombre);
        jsonObj.put("listaServiciosPub", this.listaSWPub);
        jsonObj.put("jsonError", this.jsonError);
        return jsonObj.toString();
    }

    public JSONArray getListaSWCod() {
        return listaSWCod;
    }

    public void setListaSWCod(JSONArray listaSWCod) {
        this.listaSWCod = listaSWCod;
    }

    public Vector getListaSW() {
        return listaSW;
    }


    public void setListaSW(Vector listaSW) {
        this.listaSW = listaSW;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public JSONArray getListaSWNombre() {
        return listaSWNombre;
    }

    public void setListaSWNombre(JSONArray listaSWNombre) {
        this.listaSWNombre = listaSWNombre;
    }

    public JSONArray getListaSWPub() {
        return listaSWPub;
    }

    public void setListaSWPub(JSONArray listaSWPub) {
        this.listaSWPub = listaSWPub;
    }

    public JSONArray getJsonError() {
        return jsonError;
    }

    public void setJsonError(JSONArray jsonError) {
        this.jsonError = jsonError;
    }
}
