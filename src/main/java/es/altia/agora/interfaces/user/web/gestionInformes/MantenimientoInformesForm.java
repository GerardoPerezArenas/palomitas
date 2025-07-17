package es.altia.agora.interfaces.user.web.gestionInformes;

import org.apache.struts.action.ActionForm;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONStringer;

import java.util.Vector;

import es.altia.agora.business.gestionInformes.AmbitoListaValueObject;
import es.altia.agora.business.gestionInformes.SolicitudInformeValueObject;
import es.altia.agora.business.util.ElementoListaValueObject;

public class MantenimientoInformesForm extends ActionForm {
    Vector listaAmbitos = new Vector();
    Vector listaModoAmbitos = new Vector();
    Vector listaCampos = new Vector();
    String origen;
    String eliminado;
    String codigoAmbito;
    SolicitudInformeValueObject solicitudInforme = new SolicitudInformeValueObject();
    ElementoListaValueObject elvo = new ElementoListaValueObject();
    AmbitoListaValueObject elvo1 = new AmbitoListaValueObject();
    
    JSONArray eli= new JSONArray();
    JSONArray listaInformesAmbito = new JSONArray();
    JSONArray codigo= new JSONArray();
    JSONArray modo= new JSONArray();
    JSONArray tab= new JSONArray();
    JSONArray descripcion = new JSONArray();
    JSONArray nome = new JSONArray();
    JSONArray campo = new JSONArray();
    JSONArray tipo = new JSONArray();
    JSONArray lonxitude = new JSONArray();
    JSONArray ori = new JSONArray();
    JSONArray descori = new JSONArray();
    JSONArray nomeas = new JSONArray();
    JSONArray criterio = new JSONArray();
    JSONArray cri = new JSONArray();

    public String toJSONStringAmbito() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("codigo", this.getCodigo());
        jsonObj.put("descripcion", this.getDescripcion());
        jsonObj.put("tab", this.getTab());
        jsonObj.put("modo", this.getModo());
        return jsonObj.toString();
    }
    public String toJSONStringModoAmbito() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("codigo", this.getCodigo());
        jsonObj.put("descripcion", this.getDescripcion());
        return jsonObj.toString();
    }
    public String toJSONStringCampos() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("codigo", this.getCodigo());
        jsonObj.put("nome", this.getNome());
        jsonObj.put("campo", this.getCampo());
        jsonObj.put("tipo", this.getTipo());
        jsonObj.put("lonxitude", this.getLonxitude());
        jsonObj.put("ori", this.getOri());
        jsonObj.put("descori", this.getDescOri());
        jsonObj.put("nomeas", this.getNomeas());
        jsonObj.put("criterio", this.getCriterio());
        jsonObj.put("cri", this.getCri());
        jsonObj.put("eli", this.getEli());
        
        return jsonObj.toString();
    }
    public JSONArray getCri() {
		return cri;
    }
    public void setCri(JSONArray cri) {
        this.cri = cri;
    }
    public JSONArray getNome() {
		return nome;
    }
    public void setNome(JSONArray nome) {
        this.nome = nome;
    }
    public JSONArray getCampo() {
		return campo;
    }
    public void setCampo(JSONArray campo) {
        this.campo = campo;
    }
    public JSONArray getTipo() {
		return tipo;
    }
    public void setTipo(JSONArray tipo) {
        this.tipo = tipo;
    }
    public JSONArray getLonxitude() {
		return lonxitude;
    }
    public void setLonxitude(JSONArray lonxitude) {
        this.lonxitude = lonxitude;
    }
    public JSONArray getOri() {
		return ori;
    }
    public void setOri(JSONArray ori) {
        this.ori = ori;
    }
    public JSONArray getDescOri() {
		return descori;
    }
    public void setDescOri(JSONArray descori) {
        this.descori = descori;
    }
    public JSONArray getNomeas() {
		return nomeas;
    }
    public void setNomeas(JSONArray nomeas) {
        this.nomeas = nomeas;
    }
    public JSONArray getCriterio() {
		return criterio;
    }
    public void setCriterio(JSONArray criterio) {
        this.criterio = criterio;
    }
    public JSONArray getCodigo() {
		return codigo;
    }
    public void setCodigo(JSONArray codigo) {
        this.codigo = codigo;
    }
    public JSONArray getDescripcion() {
		return descripcion;
    }
    public void setDescripcion(JSONArray descripcion) {
        this.descripcion = descripcion;
    }
   
    public JSONArray getTab() {
		return tab;
    }
    public void setTab(JSONArray tab) {
        this.tab = tab;
    }
    public JSONArray getModo() {
		return modo;
    }
    public void setModo(JSONArray modo) {
        this.modo = modo;
    }
    public JSONArray getEli() {
        return eli;
    }

    public void setEli(JSONArray eli) {
        this.eli = eli;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }
    public String getEliminado() {
        return eliminado;
    }

    public void setEliminado(String eliminado) {
        this.eliminado = eliminado;
    }


    public String getcodigoAmbito() {
        return codigoAmbito;
    }

    public void setcodigoAmbito(String codigoAmbito) {
        this.codigoAmbito = codigoAmbito;
    }

    public Vector getListaAmbitos() {
        return listaAmbitos;
    }

    public void setListaAmbitos(Vector listaAmbitos) {
        this.listaAmbitos=listaAmbitos;
    }
    
    public Vector getListaModoAmbitos() {
        return listaModoAmbitos;
    }

    public void setListaModoAmbitos(Vector listaModoAmbitos) {
        this.listaModoAmbitos=listaModoAmbitos;
    }
    
    public Vector getListaCampos() {
        return listaCampos;
    }

    public void setListaCampos(Vector listaCampos) {
        this.listaCampos=listaCampos;
    }

    
    public JSONArray getListaInformesAmbito() {
        return listaInformesAmbito;
    }

    public void setListaInformesAmbito(JSONArray listaInformesAmbito) {
        this.listaInformesAmbito = listaInformesAmbito;
    }

    
    
    
    
}
