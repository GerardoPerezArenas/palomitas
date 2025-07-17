package es.altia.flexia.expedientes.anulacion.exception;

/**
 *  Excepci�n lanzada si ocurre alg�n error durante la verificaci�n de la finalizaci�n convencional de un expediente
 * @author Administrador
 */
public class VerificacionFinNoConvencionalExpedienteException extends Exception {

    public VerificacionFinNoConvencionalExpedienteException(String message){
        super(message);
    }


    public VerificacionFinNoConvencionalExpedienteException(String message,Exception e){
        super(message,e);
    }
    
}
