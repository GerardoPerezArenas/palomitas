package es.altia.flexia.portafirmas.plugin.util;

public class ConstantesPortafirmas {
    
    public static final String FICHERO_PROPERTIES = "Portafirmas";
    public static final String GESTOR = "FLEXIA";
    public static final String AFIRMA = "AFIRMA";
    public static final String BARRA = "/";
    public static final String CLASE_IMPL_PORTAFIRMAS = "/implClass";
    public static final String PLUGIN_PORTAFIRMAS = "PluginPortafirmas";
    public static final String CLASE_PLUGIN_DEFECTO = "es.altia.flexia.portafirmas.plugin.PluginPortafirmasFlexia";
    
    //Parámetros de conexión con @firma
    public static final String  JAVA_SSL = PLUGIN_PORTAFIRMAS + BARRA + AFIRMA + "/javaSSL";
    public static final String  PROTOCOL_HANDLER = PLUGIN_PORTAFIRMAS + BARRA + AFIRMA + "/java_protocol_handler";
    public static final String  TRUSTORE = PLUGIN_PORTAFIRMAS + BARRA + AFIRMA + "/truststore";
    public static final String  PASSWORD_TRUSTORE = PLUGIN_PORTAFIRMAS + BARRA + AFIRMA + "/truststore_password";
    public static final String  TYPE_TRUSTORE = PLUGIN_PORTAFIRMAS + BARRA + AFIRMA + "/truststore_type";
    public static final String  JAVA_DEBUG = PLUGIN_PORTAFIRMAS + BARRA + AFIRMA + "/javax_net_debug";
    public static final String  APLICACION = PLUGIN_PORTAFIRMAS + BARRA + AFIRMA + "/aplicacion";
    public static final String  IP_AFIRMA = PLUGIN_PORTAFIRMAS + BARRA + AFIRMA + "/IP_afirma";
    public static final String  OPERACION_VALIDA_FIRMA = PLUGIN_PORTAFIRMAS + BARRA + AFIRMA + "/ValidarFirma";
    public static final String  ALGORITMO_HASH = PLUGIN_PORTAFIRMAS + BARRA + AFIRMA + "/algoritmoHash";
    public static final String  FORMATO_FIRMA = PLUGIN_PORTAFIRMAS + BARRA + AFIRMA + "/formatoFirma";
    public static final String  IP_VALIDAR_FIRMA = PLUGIN_PORTAFIRMAS + BARRA + AFIRMA + "/ValidarFirma";
            
    public static final String FIRMA_NO_REQUIERE = null;
    public static final String FIRMA_TRAMITADOR = "T";
    public static final String FIRMA_OTRO_USUARIO = "O";
    public static final String FIRMA_FLUJO = "L";
    public static final String FIRMA_UN_USUARIO = "U";
            
    // Estados
    public static final String ESTADO_FIRMA_PENDIENTE = "O";
    public static final String ESTADO_FIRMA_FIRMADA = "F";
    public static final String ESTADO_FIRMA_RECHAZADA = "R";
    // Para decidir en la actualizacion de la firma si es necesario actualizarlo en BBDD
    public static final String ESTADO_FIRMA_SIN_CAMBIOS = "";
            
    // FLUJO
    public static final Integer ID_TIPO_FLUJO_CASCADA = 1;
    public static final Integer ID_TIPO_FLUJO_PARALELO = 2;
    
}
