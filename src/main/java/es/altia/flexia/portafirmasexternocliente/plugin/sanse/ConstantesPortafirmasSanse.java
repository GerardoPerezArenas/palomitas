package es.altia.flexia.portafirmasexternocliente.plugin.sanse;

public class ConstantesPortafirmasSanse {
    
    // Constantes del cliente de SANSE
    public static final String ID_PETICION_NULA = "";
    public static final String PROPERTIES_PATTERN_WITHOUT_ORG = "%s/%s";
    public static final String PROPERTIES_PATTERN_WITH_ORG = "%s/%s/%s";
    public static final String PROPERTIES_BASE = "PluginPortafirmasExternoCliente/SANSE";
    public static final String PROPERTIES_QUERYSERVICE_IMPLCLASS = "query/implClass";
    public static final String PROPERTIES_QUERYSERVICE_USER = "query/user";
    public static final String PROPERTIES_QUERYSERVICE_PASS = "query/pass";
    public static final String PROPERTIES_QUERYSERVICE_URL = "query/url";
    public static final String PROPERTIES_MODIFYSERVICE_IMPLCLASS = "modify/implClass";
    public static final String PROPERTIES_MODIFYSERVICE_USER = "modify/user";
    public static final String PROPERTIES_MODIFYSERVICE_PASS = "modify/pass";
    public static final String PROPERTIES_MODIFYSERVICE_URL = "modify/url";
    public static final String PROPERTIES_TIPO_DOCUMENTO_ID = "tipoDocumentoId";
    public static final String PROPERTIES_TIPO_DOCUMENTO_DESCRIPCION = "tipoDocumentoDescripcion";
    public static final String PROPERTIES_ID_REMITENTE = "idRemitente";
    public static final String PROPERTIES_APLICACION = "aplicacion";
    public static final String PROPERTIES_SELLO_TIEMPO = "selloTiempo";
    public static final String PROPERTIES_FECHA_EXPIRACION = "fechaExpiracionDias";
    public static final String PROPERTIES_ASUNTO = "asunto";
    public static final String PROPERTIES_TEXTO = "texto";
    public static final String PROPERTIES_NIVEL_IMPORTANCIA_ID = "nivelImportanciaId";
    public static final String PROPERTIES_NIVEL_IMPORTANCIA_DESC = "nivelImportanciaDescripcion";
    
    // Estados de la peticion
    public static final String PROPERTIES_ESTADO_FIRMA_FIRMADO = "estadoFirmado";
    public static final String PROPERTIES_ESTADO_FIRMA_RECHAZADO = "estadoRechazado";
    public static final String PROPERTIES_ESTADO_FIRMA_CADUCADO = "estadoCaducado";
    public static final String PROPERTIES_ESTADO_FIRMA_PENDIENTE = "estadoPendiente";
    public static final String PROPERTIES_ESTADO_FIRMA_RETIRADO = "estadoRetirado";

    // Estados del firmante de la peticion 
    public static final String PROPERTIES_ESTADO_FIRMANTE_FIRMADO = "estadoFirmanteFirmado";
    public static final String PROPERTIES_ESTADO_FIRMANTE_VISTOBUENO = "estadoFirmanteVistoBueno";
    public static final String PROPERTIES_ESTADO_FIRMANTE_RECHAZADO = "estadoFirmanteRechazado";
    public static final String PROPERTIES_ESTADO_FIRMANTE_CADUCADO = "estadoFirmanteCaducado";
    public static final String PROPERTIES_ESTADO_FIRMANTE_NUEVO = "estadoFirmanteNuevo";
    public static final String PROPERTIES_ESTADO_FIRMANTE_LEIDO = "estadoFirmanteLeido";
    public static final String PROPERTIES_ESTADO_FIRMANTE_EN_ESPERA = "estadoFirmanteEnEspera";
    public static final String PROPERTIES_ESTADO_FIRMANTE_RETIRADO = "estadoFirmanteRetirado";
    
    // Constantes para el mapa de parametros para los metodos del cliente externo
    public static final String SANSE_PARAM_FIRMANTES = "listaFirmantes";
    public static final String SANSE_PARAM_DOCUMENTO_TRAMITE = "documentoTramite";
    public static final String SANSE_PARAM_TIPO_DOCUMENTO_ID = "tipoDocumentoId";
    public static final String SANSE_PARAM_TIPO_DOCUMENTO_DESCRIPCION = "tipoDocumentoDescripcion";
    public static final String SANSE_PARAM_ID_REMITENTE = "idRemitente";
    public static final String SANSE_PARAM_REFERENCIA = "referencia";
    public static final String SANSE_PARAM_APLICACION = "aplicacion";
    public static final String SANSE_PARAM_SELLO_TIEMPO = "selloTiempo";
    public static final String SANSE_PARAM_FECHA_INICIO = "fechaInicio";
    public static final String SANSE_PARAM_FECHA_EXPIRACION = "fechaExpiracion";
    public static final String SANSE_PARAM_ASUNTO = "asunto";
    public static final String SANSE_PARAM_TEXTO = "texto";
    public static final String SANSE_PARAM_NIVEL_IMPORTANCIA_ID = "nivelImportanciaId";
    public static final String SANSE_PARAM_NIVEL_IMPORTANCIA_DESCRIPCION = "nivelImportanciaDesc";
    public static final String SANSE_PARAM_TIPO_FIRMA = "tipoFirma";
    
    // Constantes
    public static final String SANSE_ID_REQUEST_SEPARADOR_ID_DOCUMENTO = "$";
    public static final int SANSE_POSICION_ID_REQUEST = 0;
    public static final int SANSE_POSICION_ID_DOCUMENTO = 1;
}
