package es.altia.flexia.terceros.integracion.externa.excepciones;

public class EjecucionTerceroExternoException extends Exception{

    public EjecucionTerceroExternoException(String message){
        super(message);
    }

    public EjecucionTerceroExternoException(Throwable exception){
        super(exception);
    }

    public EjecucionTerceroExternoException(String message,Throwable exception){
        super(message,exception);
    }
}
