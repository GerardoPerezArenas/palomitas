package es.altia.flexia.integracion.moduloexterno.plugin.exception;

public class EjecucionModuloException extends Exception {
    private String mensaje;
    private Throwable traza;

    public EjecucionModuloException(Throwable exception, String causa) {
        this.mensaje = causa;
        this.traza = exception;
    }

    public EjecucionModuloException(String message){
        this.mensaje = message;
    }

    public String getMensaje(){
        return this.mensaje;
    }

    public void setMensaje(String mensaje){
        this.mensaje = mensaje;
    }

    public void setTraza(Throwable traza){
        this.traza = traza;
    }

    public Throwable getTraza(){
        return this.traza;
    }
}