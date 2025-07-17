package es.altia.agora.business.integracionsw.exception;

public class NoServicioWebDefinidoException extends Exception {

    public NoServicioWebDefinidoException() {
        super("NO HAY DEFINIDO NINGUN SERVICIO WEB PARA ESTE TRAMITE");
    }
}
