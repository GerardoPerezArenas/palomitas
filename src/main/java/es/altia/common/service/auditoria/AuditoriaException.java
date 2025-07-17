package es.altia.common.service.auditoria;

public class AuditoriaException extends Exception {

    public AuditoriaException(String message) {
        super(message);
    }
    
    public AuditoriaException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
