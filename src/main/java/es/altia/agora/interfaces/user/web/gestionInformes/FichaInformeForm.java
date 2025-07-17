package es.altia.agora.interfaces.user.web.gestionInformes;

import org.apache.struts.action.ActionForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

import es.altia.agora.business.gestionInformes.FichaInformeValueObject;

public class FichaInformeForm extends ActionForm {
    String verificacion;
    String opcion;
    String codPlantilla;
    String nombre;
    String codMunicipio;
    String codProcedimiento;
    String descProcedimiento;
    String codAmbito;
    String descAmbito;
    String publicado;
    String papel;
    String orientacion;
    String margenSup;
    String margenInf;
    String margenDer;
    String margenIzq;
    JSONArray listaCriteriosCod = new JSONArray();
    JSONArray listaCriteriosNom = new JSONArray();
    JSONArray listaCriteriosCampo = new JSONArray();
    JSONArray listaCriteriosCond = new JSONArray();
    JSONArray listaCriteriosValor1 = new JSONArray();
    JSONArray listaCriteriosValor2 = new JSONArray();
    JSONArray listaCriteriosTitulo = new JSONArray();
    JSONArray listaCriteriosOrigen = new JSONArray();
    JSONArray listaCriteriosTabla = new JSONArray();
    JSONArray listaCriteriosTipo = new JSONArray();
    Vector listaCamposDesplegables;

    private FichaInformeValueObject fichaInformeVO = new FichaInformeValueObject();

    public String toJSONString() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("verificacion", this.verificacion);
        jsonObj.put("listaCriteriosCod", this.listaCriteriosCod);
        jsonObj.put("listaCriteriosNom", this.listaCriteriosNom);
        jsonObj.put("listaCriteriosCampo", this.listaCriteriosCampo);
        jsonObj.put("listaCriteriosCond", this.listaCriteriosCond);
        jsonObj.put("listaCriteriosValor1", this.listaCriteriosValor1);
        jsonObj.put("listaCriteriosValor2", this.listaCriteriosValor2);
        jsonObj.put("listaCriteriosTitulo", this.listaCriteriosTitulo);
        jsonObj.put("listaCriteriosOrigen", this.listaCriteriosOrigen);
        jsonObj.put("listaCriteriosTabla", this.listaCriteriosTabla);
        return jsonObj.toString();
    }

    public String getVerificacion() {
        return verificacion;
    }

    public void setVerificacion(String verificacion) {
        this.verificacion = verificacion;
    }

    public String getOpcion() {
        return fichaInformeVO.getOpcion();
    }

    public void setOpcion(String opcion) {
        fichaInformeVO.setOpcion(opcion);
    }

    public String getCodPlantilla() {
        return fichaInformeVO.getCodPlantilla();
    }

    public void setCodPlantilla(String codPlantilla) {
        fichaInformeVO.setCodPlantilla(codPlantilla);
    }

    public String getNombre() {
        return fichaInformeVO.getNombre();
    }

    public void setNombre(String nombre) {
        fichaInformeVO.setNombre(nombre);
    }

    public String getCodMunicipio() {
        return fichaInformeVO.getCodMunicipio();
    }

    public void setCodMunicipio(String codMunicipio) {
        fichaInformeVO.setCodMunicipio(codMunicipio);
    }

    public String getCodProcedimiento() {
        return fichaInformeVO.getCodProcedimiento();
    }

    public void setCodProcedimiento(String codProcedimiento) {
        fichaInformeVO.setCodProcedimiento(codProcedimiento);
    }

    public String getDescProcedimiento() {
        return fichaInformeVO.getDescProcedimiento();
    }

    public void setDescProcedimiento(String descProcedimiento) {
        fichaInformeVO.setDescProcedimiento(descProcedimiento);
    }

    public String getCodAmbito() {
        return fichaInformeVO.getCodAmbito();
    }

    public void setCodAmbito(String codAmbito) {
        fichaInformeVO.setCodAmbito(codAmbito);
    }

    public String getDescAmbito() {
        return fichaInformeVO.getDescAmbito();
    }

    public void setDescAmbito(String descAmbito) {
        fichaInformeVO.setDescAmbito(descAmbito);
    }

    public String getPublicado() {
        return fichaInformeVO.getPublicado();
    }

    public void setPublicado(String publicado) {
        fichaInformeVO.setPublicado(publicado);
    }

    public String getPapel() {
        return fichaInformeVO.getPapel();
    }

    public void setPapel(String papel) {
        fichaInformeVO.setPapel(papel);
    }

    public String getOrientacion() {
        return fichaInformeVO.getOrientacion();
    }

    public void setOrientacion(String orientacion) {
        fichaInformeVO.setOrientacion(orientacion);
    }

    public String getMargenSup() {
        return fichaInformeVO.getMargenSup();
    }

    public void setMargenSup(String margenSup) {
        fichaInformeVO.setMargenSup(margenSup);
    }

    public String getMargenInf() {
        return fichaInformeVO.getMargenInf();
    }

    public void setMargenInf(String margenInf) {
        fichaInformeVO.setMargenInf(margenInf);
    }

    public String getMargenDer() {
        return fichaInformeVO.getMargenDer();
    }

    public void setMargenDer(String margenDer) {
        fichaInformeVO.setMargenDer(margenDer);
    }

    public String getMargenIzq() {
        return fichaInformeVO.getMargenIzq();
    }

    public void setMargenIzq(String margenIzq) {
        fichaInformeVO.setMargenIzq(margenIzq);
    }

    public Vector getListaProcedimientos() {
        return fichaInformeVO.getListaProcedimientos();
    }

    public void setListaProcedimientos(Vector listaProcedimientos) {
        fichaInformeVO.setListaProcedimientos(listaProcedimientos);
    }

    public Vector getListaAmbitos() {
        return fichaInformeVO.getListaAmbitos();
    }

    public void setListaAmbitos(Vector listaAmbitos) {
        fichaInformeVO.setListaAmbitos(listaAmbitos);
    }

    public Vector getListaCamposDesplegables() {
        return listaCamposDesplegables;
    }

    public void setListaCamposDesplegables(Vector listaCamposDesplegables) {
        this.listaCamposDesplegables=listaCamposDesplegables;
    }

    public Vector getListaCampos() {
        return fichaInformeVO.getListaCampos();
    }

    public void setListaCampos(Vector listaCampos) {
        fichaInformeVO.setListaCampos(listaCampos);
    }

    public Vector getListaCamposDisponibles() {
        return fichaInformeVO.getListaCamposDisponibles();
    }

    public void setListaCamposDisponibles(Vector listaCamposDisponibles) {
        fichaInformeVO.setListaCamposDisponibles(listaCamposDisponibles);
    }

    public Vector getListaCriInf() {
        return fichaInformeVO.getListaCriInf();
    }

    public void setListaCriInf(Vector listaCriInf) {
        fichaInformeVO.setListaCriInf(listaCriInf);
    }

    public Vector getListaCriInfDisponibles() {
        return fichaInformeVO.getListaCriInfDisponibles();
    }

    public void setListaCriInfDisponibles(Vector listaCriInfDisponibles) {
        fichaInformeVO.setListaCriInfDisponibles(listaCriInfDisponibles);
    }

    public Vector getListaCabInf() {
        return fichaInformeVO.getListaCabInf();
    }

    public void setListaCabInf(Vector listaCabInf) {
        fichaInformeVO.setListaCabInf(listaCabInf);
    }

    public Vector getListaCabInfDisponibles() {
        return fichaInformeVO.getListaCabInfDisponibles();
    }

    public void setListaCabInfDisponibles(Vector listaCabInfDisponibles) {
        fichaInformeVO.setListaCabInfDisponibles(listaCabInfDisponibles);
    }

    public Vector getListaPieInf() {
        return fichaInformeVO.getListaPieInf();
    }

    public void setListaPieInf(Vector listaPieInf) {
        fichaInformeVO.setListaPieInf(listaPieInf);
    }

    public Vector getListaPieInfDisponibles() {
        return fichaInformeVO.getListaPieInfDisponibles();
    }

    public void setListaPieInfDisponibles(Vector listaPieInfDisponibles) {
        fichaInformeVO.setListaPieInfDisponibles(listaPieInfDisponibles);
    }

    public Vector getListaCabPag() {
        return fichaInformeVO.getListaCabPag();
    }

    public void setListaCabPag(Vector listaCabPag) {
        fichaInformeVO.setListaCabPag(listaCabPag);
    }

    public Vector getListaCabPagDisponibles() {
        return fichaInformeVO.getListaCabPagDisponibles();
    }

    public void setListaCabPagDisponibles(Vector listaCabPagDisponibles) {
        fichaInformeVO.setListaCabPagDisponibles(listaCabPagDisponibles);
    }

    public Vector getListaPiePag() {
        return fichaInformeVO.getListaPiePag();
    }

    public void setListaPiePag(Vector listaPiePag) {
        fichaInformeVO.setListaPiePag(listaPiePag);
    }

    public Vector getListaPiePagDisponibles() {
        return fichaInformeVO.getListaPiePagDisponibles();
    }

    public void setListaPiePagDisponibles(Vector listaPiePagDisponibles) {
        fichaInformeVO.setListaPiePagDisponibles(listaPiePagDisponibles);
    }

    public Vector getListaUOR() {
        return fichaInformeVO.getListaUOR();
    }

    public void setListaUOR(Vector listaUOR) {
        fichaInformeVO.setListaUOR(listaUOR);
    }

    public Vector getListaUORDisponibles() {
        return fichaInformeVO.getListaUORDisponibles();
    }

    public void setListaUORDisponibles(Vector listaUORDisponibles) {
        fichaInformeVO.setListaUORDisponibles(listaUORDisponibles);
    }

    public JSONArray getListaCriteriosCod() {
        return listaCriteriosCod;
    }

    public void setListaCriteriosCod(JSONArray listaCriteriosCod) {
        this.listaCriteriosCod = listaCriteriosCod;
    }

    public JSONArray getListaCriteriosNom() {
        return listaCriteriosNom;
    }

    public void setListaCriteriosNom(JSONArray listaCriteriosNom) {
        this.listaCriteriosNom = listaCriteriosNom;
    }

    public JSONArray getListaCriteriosCampo() {
        return listaCriteriosCampo;
    }

    public void setListaCriteriosCampo(JSONArray listaCriteriosCampo) {
        this.listaCriteriosCampo = listaCriteriosCampo;
    }

    public JSONArray getListaCriteriosTipo() {
        return listaCriteriosTipo;
    }

    public void setListaCriteriosTipo(JSONArray listaCriteriosTipo) {
        this.listaCriteriosTipo = listaCriteriosTipo;
    }

    public JSONArray getListaCriteriosCond() {
        return listaCriteriosCond;
    }

    public void setListaCriteriosCond(JSONArray listaCriteriosCond) {
        this.listaCriteriosCond = listaCriteriosCond;
    }

    public JSONArray getListaCriteriosValor1() {
        return listaCriteriosValor1;
    }

    public void setListaCriteriosValor1(JSONArray listaCriteriosValor1) {
        this.listaCriteriosValor1 = listaCriteriosValor1;
    }

    public JSONArray getListaCriteriosValor2() {
        return listaCriteriosValor2;
    }

    public void setListaCriteriosValor2(JSONArray listaCriteriosValor2) {
        this.listaCriteriosValor2 = listaCriteriosValor2;
    }

    public JSONArray getListaCriteriosTitulo() {
        return listaCriteriosTitulo;
    }

    public void setListaCriteriosTitulo(JSONArray listaCriteriosTitulo) {
        this.listaCriteriosTitulo = listaCriteriosTitulo;
    }

    public JSONArray getListaCriteriosOrigen() {
        return listaCriteriosOrigen;
    }

    public void setListaCriteriosOrigen(JSONArray listaCriteriosOrigen) {
        this.listaCriteriosOrigen = listaCriteriosOrigen;
    }

    public JSONArray getListaCriteriosTabla() {
        return listaCriteriosTabla;
    }

    public void setListaCriteriosTabla(JSONArray listaCriteriosTabla) {
        this.listaCriteriosTabla = listaCriteriosTabla;
    }

    public FichaInformeValueObject getFichaInformeVO () {
        return this.fichaInformeVO;
    }

    public void setFichaInformeVO(FichaInformeValueObject fichaInformeVO) {
        this.fichaInformeVO = fichaInformeVO;
    }

    public Vector getListaCamposCodigo() {
        return fichaInformeVO.getListaCamposCodigo();
    }

    public void setListaCamposCodigo(Vector listaCamposCodigo) {
        fichaInformeVO.setListaCamposCodigo(listaCamposCodigo);
    }

    public Vector getListaCamposNombre() {
        return fichaInformeVO.getListaCamposNombre();
    }

    public void setListaCamposNombre(Vector listaCamposNombre) {
        fichaInformeVO.setListaCamposNombre(listaCamposNombre);
    }
}