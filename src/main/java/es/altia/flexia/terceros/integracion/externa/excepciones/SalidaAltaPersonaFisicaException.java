package es.altia.flexia.terceros.integracion.externa.excepciones;

import java.util.ArrayList;

/**
 * Excepción que se lanzará si ha ocurrido algún error durante el alta de un tercero
 * en un sistema de persona física 
 */
public class SalidaAltaPersonaFisicaException extends Exception {

    private ArrayList<String> listadoErrores;

    public SalidaAltaPersonaFisicaException(String message){
        super(message);
    }

    public SalidaAltaPersonaFisicaException(String message,Throwable e){
        super(message,e);
    }

    public SalidaAltaPersonaFisicaException(String message,ArrayList<String> errores){
        super(message);
        setListadoErrores(errores);
    }

    public SalidaAltaPersonaFisicaException(String message,Throwable e,ArrayList<String> errores){
        super(message,e);
        setListadoErrores(errores);
    }

    /**
     * @return the listadoErrores
     */
    public ArrayList<String> getListadoErrores() {
        return listadoErrores;
    }

    /**
     * @param listadoErrores the listadoErrores to set
     */
    public void setListadoErrores(ArrayList<String> listadoErrores) {
        this.listadoErrores = listadoErrores;
    }

}