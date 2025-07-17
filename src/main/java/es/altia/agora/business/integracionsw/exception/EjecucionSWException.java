package es.altia.agora.business.integracionsw.exception;

import org.apache.axis.AxisFault;

public class EjecucionSWException extends Exception {

    private String mensaje;
    private Throwable traza;
    private boolean stopEjecucion;
    
    public EjecucionSWException(Throwable exception, String causa, boolean stopEjecucion) {
        this.mensaje = "ERROR EN LA EJECUCION DEL SERVICIO WEB: " + causa;
        this.traza = exception;
        this.stopEjecucion = stopEjecucion;
    }

    public EjecucionSWException(Throwable exception, String causa) {
        this.mensaje = "ERROR EN LA EJECUCION DEL SERVICIO WEB: " + causa;
        this.traza = exception;
        this.stopEjecucion = false;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Throwable getTraza() {
        return traza;
    }

    public void setTraza(Throwable traza) {
        this.traza = traza;
    }

    public boolean isStopEjecucion() {
        return stopEjecucion;
    }

    public void setStopEjecucion(boolean stopEjecucion) {
        this.stopEjecucion = stopEjecucion;
    }

    public String stackTraceToString() {
        String strStackTrace = this.mensaje + "\n";
        try {
            AxisFault af = (AxisFault)this.traza;
            strStackTrace += af.dumpToString() + "\n";
        } catch (ClassCastException cce) {
            //Do nothing.
        }
        StackTraceElement[] listaTrazas = this.traza.getStackTrace();
        for (int i=0; i < listaTrazas.length; i++) {
            strStackTrace += listaTrazas[i].toString() + "\n";
        }
        return strStackTrace;
    }
}
