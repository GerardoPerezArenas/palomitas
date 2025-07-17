package es.altia.agora.business.gestionInformes;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: susana.rodriguez
 * Date: 15-may-2007
 * Time: 12:40:55
 * To change this template use File | Settings | File Templates.
 */


public class ValoresCamposDesplegablesValueObject implements Serializable {

    public ValoresCamposDesplegablesValueObject() {
    }

    public ValoresCamposDesplegablesValueObject(String codigoValor, String descripcionValor )
   {
            this.codigoValor = codigoValor;
            this.descripcionValor = descripcionValor;
    }

    public String getCodigoValor() { return codigoValor; }

    public void setCodigoValor(String codigoValor) { this.codigoValor = codigoValor; }

    public String getDescripcionValor() { return descripcionValor; }

    public void setDescripcionValor(String descripcionValor) { this.descripcionValor = descripcionValor; }

    public String toString() {
    	return "codigo: " + this.codigoValor + " valor: " + this.descripcionValor;
    }

    private String codigoValor;
    private String descripcionValor;

}
