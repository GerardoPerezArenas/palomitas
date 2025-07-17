package es.altia.agora.webservice.registro.exceptions;

public class InstanciacionRegistroException extends RegistroException {

    public InstanciacionRegistroException(String message, Throwable rootException) {
        super(rootException, message);
    }

}
