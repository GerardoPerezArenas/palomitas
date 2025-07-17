package es.altia.agora.interfaces.user.web.integracionsw;

import org.apache.struts.action.ActionForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class MantenimientoOperacionSWForm extends ActionForm {

    private String codigoSW;
    private String codDefOp;
    private String tituloSW;
    private String tituloOp;
    private String nombreOpSW;
    private Vector listaParamsIn;
    private Vector listaParamsOut;
    private Vector listaArrays;

    JSONArray listaDescParamIn = new JSONArray();
    JSONArray listaTituloParamIn = new JSONArray();
    JSONArray listaOblParamIn = new JSONArray();
    JSONArray listaTipoParamIn = new JSONArray();
    JSONArray listaValorParamIn = new JSONArray();
    JSONArray listaCodParamIn = new JSONArray();
    JSONArray listaDescParamOut = new JSONArray();
    JSONArray listaTituloParamOut = new JSONArray();
    JSONArray listaTipoParamOut = new JSONArray();
    JSONArray listaValorParamOut = new JSONArray();
    JSONArray listaArrayDef = new JSONArray();
    JSONArray listaArrayEntSal = new JSONArray();
    JSONArray listaArrayNumReps = new JSONArray();

    public String getCodDefOp() {
        return codDefOp;
    }

    public void setCodDefOp(String codDefOp) {
        this.codDefOp = codDefOp;
    }

    public String getTituloSW() {
        return tituloSW;
    }

    public void setTituloSW(String tituloSW) {
        this.tituloSW = tituloSW;
    }

    public String getTituloOp() {
        return tituloOp;
    }

    public void setTituloOp(String tituloOp) {
        this.tituloOp = tituloOp;
    }

    public String getNombreOpSW() {
        return nombreOpSW;
    }

    public void setNombreOpSW(String nombreOpSW) {
        this.nombreOpSW = nombreOpSW;
    }

    public String getCodigoSW() {
        return codigoSW;
    }

    public void setCodigoSW(String codigoSW) {
        this.codigoSW = codigoSW;
    }

    public Vector getListaParamsIn() {
        return listaParamsIn;
    }

    public void setListaParamsIn(Vector listaParamsIn) {
        this.listaParamsIn = listaParamsIn;
    }

    public Vector getListaParamsOut() {
        return listaParamsOut;
    }

    public void setListaParamsOut(Vector listaParamsOut) {
        this.listaParamsOut = listaParamsOut;
    }

    public JSONArray getListaDescParamIn() {
        return listaDescParamIn;
    }

    public void setListaDescParamIn(JSONArray listaDescParamIn) {
        this.listaDescParamIn = listaDescParamIn;
    }

    public JSONArray getListaValorParamIn() {
        return listaValorParamIn;
    }

    public void setListaValorParamIn(JSONArray listaValorParamIn) {
        this.listaValorParamIn = listaValorParamIn;
    }

    public JSONArray getListaTipoParamIn() {
        return listaTipoParamIn;
    }

    public void setListaTipoParamIn(JSONArray listaTipoParamIn) {
        this.listaTipoParamIn = listaTipoParamIn;
    }

    public JSONArray getListaOblParamIn() {
        return listaOblParamIn;
    }

    public void setListaOblParamIn(JSONArray listaOblParamIn) {
        this.listaOblParamIn = listaOblParamIn;
    }

    public JSONArray getListaTituloParamIn() {
        return listaTituloParamIn;
    }

    public void setListaTituloParamIn(JSONArray listaTituloParamIn) {
        this.listaTituloParamIn = listaTituloParamIn;
    }

    public JSONArray getListaCodParamIn() {
        return listaCodParamIn;
    }

    public void setListaCodParamIn(JSONArray listaCodParamIn) {
        this.listaCodParamIn = listaCodParamIn;
    }

    public JSONArray getListaDescParamOut() {
        return listaDescParamOut;
    }

    public void setListaDescParamOut(JSONArray listaDescParamOut) {
        this.listaDescParamOut = listaDescParamOut;
    }

    public JSONArray getListaTituloParamOut() {
        return listaTituloParamOut;
    }

    public void setListaTituloParamOut(JSONArray listaTituloParamOut) {
        this.listaTituloParamOut = listaTituloParamOut;
    }

    public Vector getListaArrays() {
        return listaArrays;
    }

    public void setListaArrays(Vector listaArrays) {
        this.listaArrays = listaArrays;
    }

    public JSONArray getListaArrayDef() {
        return listaArrayDef;
    }

    public void setListaArrayDef(JSONArray listaArrayDef) {
        this.listaArrayDef = listaArrayDef;
    }

    public JSONArray getListaArrayEntSal() {
        return listaArrayEntSal;
    }

    public void setListaArrayEntSal(JSONArray listaArrayEntSal) {
        this.listaArrayEntSal = listaArrayEntSal;
    }

    public JSONArray getListaArrayNumReps() {
        return listaArrayNumReps;
    }

    public void setListaArrayNumReps(JSONArray listaArrayNumReps) {
        this.listaArrayNumReps = listaArrayNumReps;
    }

    public JSONArray getListaTipoParamOut() {
        return listaTipoParamOut;
    }

    public void setListaTipoParamOut(JSONArray listaTipoParamOut) {
        this.listaTipoParamOut = listaTipoParamOut;
    }

    public JSONArray getListaValorParamOut() {
        return listaValorParamOut;
    }

    public void setListaValorParamOut(JSONArray listaValorParamOut) {
        this.listaValorParamOut = listaValorParamOut;
    }

    public String toJSONStringParamIn() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("listaDescParamIn", this.listaDescParamIn);
        jsonObj.put("listaTituloParamIn", this.listaTituloParamIn);
        jsonObj.put("listaOblParamIn", this.listaOblParamIn);
        jsonObj.put("listaTipoParamIn", this.listaTipoParamIn);
        jsonObj.put("listaValorParamIn", this.listaValorParamIn);
        jsonObj.put("listaCodParamIn", this.listaCodParamIn);

        return jsonObj.toString();
    }

    public String toJSONStringParamOut() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("listaDescParamOut", this.listaDescParamOut);
        jsonObj.put("listaTituloParamOut", this.listaTituloParamOut);
        jsonObj.put("listaTipoParamOut", this.listaTipoParamOut);
        jsonObj.put("listaValorParamOut", this.listaValorParamOut);

        return jsonObj.toString();
    }

    public String toJSONStringArray() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("listaArrayDef", this.listaArrayDef);
        jsonObj.put("listaArrayEntSal", this.listaArrayEntSal);
        jsonObj.put("listaArrayNumReps", this.listaArrayNumReps);

        return jsonObj.toString();
    }
}
