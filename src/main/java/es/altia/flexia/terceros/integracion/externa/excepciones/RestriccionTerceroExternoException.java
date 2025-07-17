package es.altia.flexia.terceros.integracion.externa.excepciones;

import java.util.ArrayList;

public class RestriccionTerceroExternoException extends Exception {
    // Etiquetas de error a mostrar
    private ArrayList<String> etiquetasError = null;
    
    public RestriccionTerceroExternoException(String message,ArrayList<String> etiquetasError){
        super(message);
        setEtiquetasError(etiquetasError);
    }


    public RestriccionTerceroExternoException(Throwable exception,ArrayList<String> etiquetasError){
        super(exception);
        setEtiquetasError(etiquetasError);
    }


    public RestriccionTerceroExternoException(String message,Throwable exception,ArrayList<String> etiquetasError){
        super(message,exception);
        setEtiquetasError(etiquetasError);
    }

  
    /**
     * @return the etiquetasError
     */
    public ArrayList<String> getEtiquetasError() {
        return etiquetasError;
    }

    /**
     * @param etiquetasError the etiquetasError to set
     */
    public void setEtiquetasError(ArrayList<String> etiquetasError) {
        this.etiquetasError = etiquetasError;
    }
         
}