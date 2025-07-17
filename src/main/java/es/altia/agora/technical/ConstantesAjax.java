package es.altia.agora.technical;

public interface ConstantesAjax {
    // Errores para llamadas ajax
    public static final int STATUS_AJAX_OK = 0;
    public static final int STATUS_AJAX_ERROR_INTERNO = 1;
    public static final int STATUS_AJAX_ERROR_YA_EXISTE_CSV = 100;
    public static final int STATUS_AJAX_ERROR_NO_EXISTE_FICHERO = 101;
    public static final int STATUS_AJAX_ERROR_FORMATO_FICHERO_NO_SOPORTADO = 102;
    public static final int STATUS_AJAX_ERROR_CONFIGURACION_RECARGAR_CACHE = 103;
    public static final int STATUS_AJAX_ERROR_EXPEDIENTE_NO_EXISTE = 104;
    public static final int STATUS_AJAX_ERROR_FORMATO_NUM_EXPEDIENTE_INCORRECTO = 105;
    
    // Descripcion de los errores para llamadas ajax
    public static final String DESC_STATUS_AJAX_OK = "OK";
    public static final String DESC_STATUS_AJAX_ERROR_INTERNO = "ERROR INTERNO DE LA APLICACION";
    public static final String DESC_STATUS_AJAX_ERROR_YA_EXISTE_CSV = "YA EXISTE CSV PARA ESTE DOCUMENTO";
    public static final String DESC_STATUS_AJAX_ERROR_NO_EXISTE_FICHERO = "EL FICHERO NO EXISTE";
    public static final String DESC_STATUS_AJAX_ERROR_FORMATO_FICHERO_NO_SOPORTADO = "FORMATO DE FICHERO NO SOPORTADO";
    
    // Etiquetas de los errores para llamadas ajax para la internacionalizacion
    public static final String ETIQUETA_STATUS_AJAX_OK = "ajaxOk";
    public static final String ETIQUETA_STATUS_AJAX_ERROR_INTERNO = "ajaxErrorInterno";
    public static final String ETIQUETA_STATUS_AJAX_ERROR_CONFIGURACION_RECARGAR_CACHE = "msjErrPropRecargarCacheAviso";
    public static final String ETIQUETA_STATUS_AJAX_ERROR_EXPEDIENTE_NO_EXISTE = "msjExpNoExiste";
    public static final String ETIQUETA_STATUS_AJAX_ERROR_FORMATO_NUM_EXPEDIENTE_INCORRECTO = "msjErrorFormatoNumExp";

}