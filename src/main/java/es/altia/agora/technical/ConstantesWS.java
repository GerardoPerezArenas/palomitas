package es.altia.agora.technical;

public interface ConstantesWS {

    // ** Constantes genericas para los servicios web **
    // Codigos de error
    public static final int STATUS_WS_OK = 0;
    public static final int STATUS_WS_ERROR_INTERNO = -10;
    public static final int STATUS_WS_NO_PERMISO = -1;
    
    // Descripcion de los errores para llamadas ajax
    public static final String DESC_STATUS_WS_OK
            = "La operación se ha realizado con éxito";
    public static final String DESC_STATUS_WS_ERROR_INTERNO
            = "Error interno de la aplicación";
    public static final String DESC_STATUS_WS_NO_PERMISO
            = "La aplicación no tiene permisos para acceder al servicio web";
   
    
    // ** Constantes genericas para el servicio web de WSDocumentosFlexia **
    // Codigos de error
    public static final int STATUS_WS_ERROR_TOKEN_CSV_INCORRECTO = 53;
    
    // Descripcion de los errores para llamadas ajax
    public static final String DESC_STATUS_WS_ERROR_TOKEN_CSV_INCORRECTO
            = "El token recibido no es valido";
    
}
