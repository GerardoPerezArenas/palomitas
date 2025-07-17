package es.altia.flexia.expedientes.anulacion.exception;

/**
 * Excepcion que se lanza sino se puede obtener la instancia de la clase de un plugin
 * de finalzación no convencional de expediente
 */
public class VerificacionFinNoConvencionalInstanceException extends Exception{
    
   public VerificacionFinNoConvencionalInstanceException(String message){
       super(message);
   }
   
   public VerificacionFinNoConvencionalInstanceException(String message,Exception e){
       super(message,e);
   }   
}