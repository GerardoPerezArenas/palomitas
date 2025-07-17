package es.altia.agora.interfaces.user.web.gestionInformes;

import org.apache.struts.action.ActionForm;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.Vector;

import es.altia.agora.business.gestionInformes.SolicitudInformeValueObject;

public class SolicitudesInformesForm extends ActionForm {
    Vector listaInformes = new Vector();
    Vector listaSolicitudes = new Vector();
    Vector listaAmbitos = new Vector();
    Vector listaProcedimientos = new Vector();
    Vector listaCamposDesplegables = new Vector();
    String codProcedimiento;
    String codSolicitud;
    String origen;
    SolicitudInformeValueObject solicitudInforme = new SolicitudInformeValueObject();
    JSONArray listaInformesCod = new JSONArray();
    JSONArray listaInformesNombre = new JSONArray();
    JSONArray listaInformesAmbito = new JSONArray();
    JSONArray listaInformesProc = new JSONArray();
    JSONArray listaInformesEstado = new JSONArray();
    JSONArray listaInformesFecha = new JSONArray();

    public String toJSONString() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("codInforme", solicitudInforme.getCodSolicitud());
        jsonObj.put("titulo", solicitudInforme.getDescripcion());
        jsonObj.put("estado", solicitudInforme.getEstado());
        jsonObj.put("tiempo", solicitudInforme.getTiempo());
        jsonObj.put("fechaGeneracion", solicitudInforme.getFechaGeneracion());
        jsonObj.put("fechaSolicitud", solicitudInforme.getFechaSolicitud());
        jsonObj.put("formato", solicitudInforme.getFormato());
        jsonObj.put("origen", solicitudInforme.getOrigen());
        jsonObj.put("procedimiento", solicitudInforme.getProcedimiento());
        jsonObj.put("usuario", solicitudInforme.getUsuario());
        jsonObj.put("listaCriterios", solicitudInforme.getCadenaCriterios());
        return jsonObj.toString();
    }

    public String toJSONStringSolicitudes() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("listaInformesCod", this.listaInformesCod);
        jsonObj.put("listaInformesNombre", this.listaInformesNombre);
        jsonObj.put("listaInformesAmbito", this.listaInformesAmbito);
        jsonObj.put("listaInformesProc", this.listaInformesProc);
        jsonObj.put("listaInformesEstado", this.listaInformesEstado);
        jsonObj.put("listaInformesFecha", this.listaInformesFecha);
        return jsonObj.toString();
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }
    public String getCodSolicitud() {
        return codSolicitud;
    }

    public void setCodSolicitud(String codSolicitud) {
    	this.codSolicitud = codSolicitud;
    }


    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public SolicitudInformeValueObject getSolicitudInforme() {
        return solicitudInforme;
    }

    public void setSolicitudInforme(SolicitudInformeValueObject solicitudInforme) {
        this.solicitudInforme = solicitudInforme;
    }

    public Vector getListaInformes() {
        return listaInformes;
    }

    public void setListaInformes(Vector listaInformes) {
        this.listaInformes = listaInformes;
    }

    public Vector getListaSolicitudes() {
        return listaSolicitudes;
    }

    public void setListaSolicitudes(Vector listaSolicitudes) {
        this.listaSolicitudes = listaSolicitudes;
    }

    public Vector getListaProcedimientos() {
        return listaProcedimientos;
    }

    public void setListaProcedimientos(Vector listaProcedimientos) {
        this.listaProcedimientos=listaProcedimientos;
    }

    public Vector getListaAmbitos() {
        return listaAmbitos;
    }

    public void setListaAmbitos(Vector listaAmbitos) {
        this.listaAmbitos=listaAmbitos;
    }

    public Vector getListaCamposDesplegables() {
        return listaCamposDesplegables;
    }

    public void setListaCamposDesplegables(Vector listaCamposDesplegables) {
        this.listaCamposDesplegables=listaCamposDesplegables;
    }

    public JSONArray getListaInformesCod() {
        return listaInformesCod;
    }

    public void setListaInformesCod(JSONArray listaInformesCod) {
        this.listaInformesCod = listaInformesCod;
    }

    public JSONArray getListaInformesNombre() {
        return listaInformesNombre;
    }

    public void setListaInformesNombre(JSONArray listaInformesNombre) {
        this.listaInformesNombre = listaInformesNombre;
    }

    public JSONArray getListaInformesAmbito() {
        return listaInformesAmbito;
    }

    public void setListaInformesAmbito(JSONArray listaInformesAmbito) {
        this.listaInformesAmbito = listaInformesAmbito;
    }

    public JSONArray getListaInformesProc() {
        return listaInformesProc;
    }

    public void setListaInformesProc(JSONArray listaInformesProc) {
        this.listaInformesProc = listaInformesProc;
    }

    public JSONArray getListaInformesEstado() {
        return listaInformesEstado;
    }

    public void setListaInformesEstado(JSONArray listaInformesEstado) {
        this.listaInformesEstado = listaInformesEstado;
    }

    public JSONArray getListaInformesFecha() {
        return listaInformesFecha;
    }

    public void setListaInformesFecha(JSONArray listaInformesFecha) {
        this.listaInformesFecha = listaInformesFecha;
    }
}
