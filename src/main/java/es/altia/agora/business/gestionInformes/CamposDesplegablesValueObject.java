package es.altia.agora.business.gestionInformes;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: susana.rodriguez
 * Date: 15-may-2007
 * Time: 12:40:55
 * To change this template use File | Settings | File Templates.
 */


public class CamposDesplegablesValueObject implements Serializable {

    public CamposDesplegablesValueObject() {
    }

    public CamposDesplegablesValueObject(String codigoCampo,String descripcionCampo, Vector listaValores )
   {
            this.codigoCampo = codigoCampo;
            this.descripcionCampo = descripcionCampo;
            this.listaValores = listaValores;
    }

    public String getCodigoCampo() { return codigoCampo; }

    public void setCodigoCampo(String codigoCampo) { this.codigoCampo = codigoCampo; }

    public String getDescripcionCampo() { return descripcionCampo; }

    public void setDescripcionCampo(String descripcionCampo) { this.descripcionCampo = descripcionCampo; }

    public Vector getListaValores() { return listaValores; }

    public void setListaValores(Vector listaValores) { this.listaValores = listaValores; }

    public String toString() {
    	return this.descripcionCampo + ", codigo " + this.codigoCampo + ", lista valores : " + this.listaValores;          	
    }

    private String codigoCampo;
    private String descripcionCampo;
    private Vector listaValores;

}
