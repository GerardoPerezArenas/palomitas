package es.altia.flexia.registro.digitalizacion.exception;

public class DigitalizarDocumentosException extends Exception{
    public DigitalizarDocumentosException(String message) {
        super(message);
    }
    
    public DigitalizarDocumentosException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
