package es.altia.flexia.terceros.integracion.externa.excepciones;

public class InstantiationTerceroExternoException extends Exception {

    public InstantiationTerceroExternoException(String message){
        super(message);
    }

    public InstantiationTerceroExternoException(Throwable exception){
        super(exception);
    }

    public InstantiationTerceroExternoException(String message,Throwable exception){
        super(message,exception);
    }
    
}
