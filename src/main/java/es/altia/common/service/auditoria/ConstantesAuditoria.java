package es.altia.common.service.auditoria;

public class ConstantesAuditoria {
    // Codigos de errores
    public static final int COD_ERROR_OK = 0;
    public static final int COD_ERROR_GENERICO = -1;
    public static final int COD_ERROR_NO_PARAM_EVENTO = 1;
    public static final int COD_ERROR_NO_TIPO_EVENTO = 2;
    public static final int COD_ERROR_NO_USUARIO = 3;

    // Descripciones de los errores
    public static final String DESC_ERROR_GENERICO = "Se ha producido un error desconocido";
    public static final String DESC_ERROR_NO_PARAM_EVENTO = "No se ha especificado el evento a registrar";
    public static final String DESC_ERROR_NO_TIPO_EVENTO = "No se ha especificado el tipo de evento";
    public static final String DESC_ERROR_NO_USUARIO = "No se ha especificado el usuario";

     // Constantes valores defecto
    public static final Integer EVENTO_SIN_ID_USUARIO = -1;
    public static final String EVENTO_SIN_NOMBRE_USUARIO = "";
    
    // Constantes de fichero comunes
    public static final String SEPARADOR_COLUMNAS = ";";
    
    // Constantes de registro de E/S
    public static final String REGISTRO_VER_LISTADO = "LISTADO_VISUALIZAR";
    public static final String REGISTRO_LISTADO_INFORME = "LISTADO_INFORME";
    public static final String REGISTRO_LISTADO_EXPORT_CSV = "LISTADO_EXPORT_CSV";
    public static final String REGISTRO_LISTADO_ENVIO_ENTRADAS = "LISTADO_ENVIO_ENTRADAS";
    public static final String REGISTRO_VER_ANOTACION = "VISUALIZAR_ANOTACION";
    public static final String REGISTRO_INFORME_POR_OFICINA = "INFORME_POR_OFICINA";
    public static final String REGISTRO_LIBRO_INFORME = "INFORME_LIBRO_REGISTRO";
    public static final String REGISTRO_INFORME_POR_UNIDAD_TRAMITADORA = "INFORME_POR_UNIDAD_TRAMITADORA";
    public static final String REGISTRO_INFORME_POR_UNIDAD_TRAMITADORA_EXTERNA = "INFORME_POR_UNIDAD_TRAMITADORA_EXTERNA";
    public static final String REGISTRO_INFORME_ENVIO_ENTRADAS = "INFORME_ENVIO_ENTRADAS";
     public static final String REGISTRO_SEPARADOR = "#";
    
    // Constantes de expediente
    public static final String EXPEDIENTE_VER_ANOTACION = "VISUALIZAR_ANOTACION";
    public static final String EXPEDIENTE_VER_ANOTACION_DESDE_EXPEDIENTE = "VISUALIZAR_ANOTACION_DESDE_EXPEDIENTE";
    public static final String EXPEDIENTE_BUZON_ENTRADA_CONSULTA_LISTADO = "BUZON_ENTRADA_CONSULTA_LISTADO";
    public static final String EXPEDIENTE_BUZON_ENTRADA_CONSULTA_LISTADO_HISTORICO = "BUZON_ENTRADA_CONSULTA_LISTADO_HISTORICO";
    public static final String EXPEDIENTE_PENDIENTES_CONSULTA_LISTADO = "EXPEDIENTES_PENDIENTES_CONSULTA_LISTADO";
    public static final String EXPEDIENTE_VER = "VISUALIZAR_EXPEDIENTE";
    public static final String EXPEDIENTE_VER_CONSULTA_LISTADO_RELACIONADOS = "VISUALIZAR_EXPEDIENTE_LISTADO_EXPEDIENTES_RELACIONADOS";
    public static final String EXPEDIENTE_IMPRIMIR = "IMPRIMIR_EXPEDIENTE";
    public static final String EXPEDIENTE_CONSULTA_LISTADO = "CONSULTA_LISTADO";
    public static final String EXPEDIENTE_CONSULTA_LISTADO_POR_CAMPOS_SUPL = "CONSULTA_LISTADO_POR_CAMPOS_SUPL";
    public static final String EXPEDIENTE_CONSULTA_LISTADO_POR_LOCALIZACION = "CONSULTA_LISTADO_POR_LOCALIZACION";
    public static final String EXPEDIENTE_CONSULTA_LISTADO_INFORME = "LISTADO_INFORME";
    public static final String EXPEDIENTE_CONSULTA_LISTADO_INFORME_CSV = "LISTADO_INFORME_CSV";
        public static final String EXPEDIENTE_SEPARADOR = "#";
    public static final String EXPEDIENTE_BUZON_ENTRADA_LISTADO_EXPORT_CSV = "BUZON_ENTRADA_LISTADO_EXPORT_CSV";    
    // Constantes para la solicitud de informes de expedientes
    public static final String EXPEDIENTE_SOLICITUD_GENERAR_INFORME_DIRECTO = "SOLICITUD_GENERAR_INFORME_DIRECTO";
    public static final String EXPEDIENTE_SOLICITUD_GENERAR_INFORME_BUZON = "SOLICITUD_GENERAR_INFORME_BUZON";
    public static final String EXPEDIENTE_SOLICITUD_ELIMINAR_INFORME_BUZON = "SOLICITUD_ELIMINAR_INFORME_BUZON";
    public static final String EXPEDIENTE_SOLICITUD_VER_INFORME_BUZON = "SOLICITUD_VISUALIZAR_INFORME_BUZON";
    public static final String EXPEDIENTE_SOLICITUD_INFORME_CRITERIO_INICIO_LINEA = " - ";
    public static final String EXPEDIENTE_SOLICITUD_INFORME_CRITERIO_NEWLINE = "\\n";
    public static final String EXPEDIENTE_SOLICITUD_INFORME_CRITERIO_SUSTITUIR_NEWLINE = " #";
        // Constantes para los servicios web
    public static final String SERVICIO_WEB_WS_TRAMITACION = "WS_TRAMITACION";
    public static final String SERVICIO_WEB_WS_REGISTRO_ES = "WS_REGISTRO_ES";
    public static final String SERVICIO_WEB_WS_BUSQUEDA_EXPEDIENTE = "WS_BUSQUEDA_EXPEDIENTE";

}
