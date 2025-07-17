package es.altia.flexia.expedientes.anulacion.plugin;

import es.altia.flexia.expedientes.anulacion.exception.VerificacionFinNoConvencionalExpedienteException;

/**
 * Plugin por defecto de verificaci�n de finalizaci�n no convencional de un expediente para Flexia.
 * Siempre permite finalizar de forma no convencional/anular un expediente 
 */
public class VerificacionFinNoConvencionalExpedienteFlexia extends VerificacionFinNoConvencionalExpediente{
    
    public boolean verificarFinalizacionNoConvencional(int codOrganizacion,String codProcedimiento,String numExpediente,int codUsuario,String loginUsuario)  throws VerificacionFinNoConvencionalExpedienteException{
        return true;
    }
    
}
