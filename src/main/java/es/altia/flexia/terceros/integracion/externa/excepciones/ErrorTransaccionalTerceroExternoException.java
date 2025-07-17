package es.altia.flexia.terceros.integracion.externa.excepciones;


public class ErrorTransaccionalTerceroExternoException extends Exception {

    public ErrorTransaccionalTerceroExternoException(String message){
        super(message);
    }

    public ErrorTransaccionalTerceroExternoException(Throwable ex){
        super(ex);
    }

    public ErrorTransaccionalTerceroExternoException(String message,Throwable ex){
        super(message,ex);
    }


}
