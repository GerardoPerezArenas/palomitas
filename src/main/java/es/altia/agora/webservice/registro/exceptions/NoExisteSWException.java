package es.altia.agora.webservice.registro.exceptions;

public class NoExisteSWException extends Exception {

    private String mensaje;

    public NoExisteSWException(Throwable cause, String mensaje) {
        super(cause);
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
