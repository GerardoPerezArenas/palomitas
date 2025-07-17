package es.altia.agora.interfaces.user.web.gestionInformes;

import org.apache.struts.action.ActionForm;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.Vector;

public class GestionInformesForm extends ActionForm {
    Vector listaInformes = new Vector();
    Vector listaAmbitos = new Vector();
    Vector listaProcedimientos = new Vector();
    String codProcedimiento;
    JSONArray listaInformesCod = new JSONArray();
    JSONArray listaInformesOrig = new JSONArray();
    JSONArray listaInformesNombre = new JSONArray();
    JSONArray listaInformesProc = new JSONArray();
    JSONArray listaInformesPub = new JSONArray();
    String codPlantilla;
    String publicar;
    String hayPermiso;
    public String toJSONString() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("listaInformesCod", this.listaInformesCod);
        jsonObj.put("listaInformesOrig", this.listaInformesOrig);
        jsonObj.put("listaInformesNombre", this.listaInformesNombre);
        jsonObj.put("listaInformesProc", this.listaInformesProc);
        jsonObj.put("listaInformesPub", this.listaInformesPub);
        jsonObj.put("codPlantilla", this.codPlantilla);
        jsonObj.put("publicar", this.publicar);
        jsonObj.put("hayPermiso", this.hayPermiso);
        return jsonObj.toString();
    }

    public String getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(String codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    public String getPublicar() {
        return publicar;
    }

    public void setPublicar(String publicar) {
        this.publicar = publicar;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public Vector getListaInformes() {
        return listaInformes;
    }

    public void setListaInformes(Vector listaInformes) {
        this.listaInformes = listaInformes;
    }

    public Vector getListaProcedimientos() {
        return listaProcedimientos;
    }

    public void setListaProcedimientos(Vector listaProcedimientos) {
        this.listaProcedimientos=listaProcedimientos;
    }

    public JSONArray getListaInformesCod() {
        return listaInformesCod;
    }

    public void setListaInformesCod(JSONArray listaInformesCod) {
        this.listaInformesCod = listaInformesCod;
    }

    public JSONArray getListaInformesOrig() {
        return listaInformesOrig;
    }

    public void setListaInformesOrig(JSONArray listaInformesOrig) {
        this.listaInformesOrig = listaInformesOrig;
    }

    public JSONArray getListaInformesNombre() {
        return listaInformesNombre;
    }

    public void setListaInformesNombre(JSONArray listaInformesNombre) {
        this.listaInformesNombre = listaInformesNombre;
    }

    public JSONArray getListaInformesProc() {
        return listaInformesProc;
    }

    public void setListaInformesProc(JSONArray listaInformesProc) {
        this.listaInformesProc = listaInformesProc;
    }

    public JSONArray getListaInformesPub() {
        return listaInformesPub;
    }

    public void setListaInformesPub(JSONArray listaInformesPub) {
        this.listaInformesPub = listaInformesPub;
    }

    public Vector getListaAmbitos() {
        return listaAmbitos;
    }

    public void setListaAmbitos(Vector listaAmbitos) {
        this.listaAmbitos = listaAmbitos;
    }
    public String getHayPermiso() {
        return hayPermiso;
    }

    public void setHayPermiso(String hayPermiso) {
        this.hayPermiso = hayPermiso;
    }    
}