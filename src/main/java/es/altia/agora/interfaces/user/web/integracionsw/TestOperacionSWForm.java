package es.altia.agora.interfaces.user.web.integracionsw;

import org.apache.struts.action.ActionForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class TestOperacionSWForm extends ActionForm {

    private String codigoSW;
    private String codOpDef;
    private String tituloSW;
    private String wsdlSW;
    private String tituloOpSW;
    private String nombreOpSW;
    private Vector listaParamsIn;
    private Vector listaParamsOut;
    private String estadoEjecucion;
    private Vector resultados;

    private JSONArray jsonResultados;

    private JSONArray jsonEstado;

    public String getCodOpDef() {
        return codOpDef;
    }

    public void setCodOpDef(String codOpDef) {
        this.codOpDef = codOpDef;
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

    public String getTituloOpSW() {
        return tituloOpSW;
    }

    public void setTituloOpSW(String tituloOpSW) {
        this.tituloOpSW = tituloOpSW;
    }

    public String getNombreOpSW() {
        return nombreOpSW;
    }

    public void setNombreOpSW(String nombreOpSW) {
        this.nombreOpSW = nombreOpSW;
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

    public String getEstadoEjecucion() {
        return estadoEjecucion;
    }

    public void setEstadoEjecucion(String estadoEjecucion) {
        this.estadoEjecucion = estadoEjecucion;
    }

    public Vector getResultados() {
        return resultados;
    }

    public void setResultados(Vector resultados) {
        this.resultados = resultados;
    }

    public JSONArray getJsonResultados() {
        return jsonResultados;
    }

    public void setJsonResultados(JSONArray jsonResultados) {
        this.jsonResultados = jsonResultados;
    }

    public JSONArray getJsonEstado() {
        return jsonEstado;
    }

    public void setJsonEstado(JSONArray jsonEstado) {
        this.jsonEstado = jsonEstado;
    }

    public String toJSONStringResultados() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("jsonResultados", this.jsonResultados);
        jsonObj.put("jsonEstado", this.jsonEstado);
        return jsonObj.toString();
    }
}
