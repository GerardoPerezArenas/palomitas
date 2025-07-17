package es.altia.flexia.portafirmas.documentos.externos.localizacion.expediente.servicio.exception;

public class LocalizacionExpedienteDocumentoExternoPortafirmasException extends Exception{
    
    public LocalizacionExpedienteDocumentoExternoPortafirmasException(String message){
        super(message);
    }
    
    public LocalizacionExpedienteDocumentoExternoPortafirmasException(Throwable e){
        super(e);
    }
    
    public LocalizacionExpedienteDocumentoExternoPortafirmasException(String msg,Throwable e){
        super(msg,e);
    }
    
}