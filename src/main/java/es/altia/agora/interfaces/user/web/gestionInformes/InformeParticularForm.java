package es.altia.agora.interfaces.user.web.gestionInformes;

import org.apache.struts.action.ActionForm;

import java.util.Vector;

public class InformeParticularForm extends ActionForm {

    String tituloInforme;
    String rutaInforme;
    Vector<String> errores = new Vector<String>();
    String estadoInforme;
    String tipoInforme;
    String tipoFichero;
    

    public String getTipoFichero(){
    	return this.tipoFichero;
    }
    
    public void setTipoFichero(String tipoFichero){
    	this.tipoFichero=tipoFichero;
    }

    public String getRutaInforme() {
        return rutaInforme;
    }

    public void setRutaInforme(String rutaInforme) {
        this.rutaInforme = rutaInforme;
    }

    public Vector<String> getErrores() {
        return errores;
    }

    public void setErrores(Vector<String> errores) {
        this.errores = errores;
    }

    public String getTituloInforme() {
        return tituloInforme;
    }

    public void setTituloInforme(String tituloInforme) {
        this.tituloInforme = tituloInforme;
    }

    public String getEstadoInforme() {
        return estadoInforme;
    }

    public void setEstadoInforme(String estadoInforme) {
        this.estadoInforme = estadoInforme;
    }

    public String getTipoInforme() {
        return tipoInforme;
    }

    public void setTipoInforme(String tipoInforme) {
        this.tipoInforme = tipoInforme;
    }

}
