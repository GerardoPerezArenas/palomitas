package es.altia.agora.webservice.registro.exceptions;

public class RegistroException extends Exception {

    private String mensaje;

    public RegistroException(Throwable cause, String mensaje) {
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
