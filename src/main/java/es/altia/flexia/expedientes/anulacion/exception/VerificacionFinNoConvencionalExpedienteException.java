package es.altia.flexia.expedientes.anulacion.exception;

/**
 *  Excepción lanzada si ocurre algún error durante la verificación de la finalización convencional de un expediente
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
