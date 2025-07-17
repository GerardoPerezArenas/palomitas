package es.altia.agora.business.registro.sir.exception;

/**
 * Excepción específica para errores en el servicio SIR.
 */
public class ServicioSIRException extends Exception {
    
    /**
     * Constructor para ServicioSIRException con mensaje.
     *
     * @param mensaje El mensaje de error
     */
    public ServicioSIRException(String mensaje) {
        super(mensaje);
    }
    
    /**
     * Constructor para ServicioSIRException con mensaje y causa.
     *
     * @param mensaje El mensaje de error
     * @param causa La excepción original que causó este error
     */
    public ServicioSIRException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
