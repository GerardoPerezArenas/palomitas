package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.handler;

/**
 * Obtiene el fichero referenciado por un campo en formato MTOM/XOP para la
 * llamada a downloadSign.
 * El valor obtenido será almacenado en un ThreadLocal, que será accesible desde
 * la clase.
 *
 */
public class DownloadSignResponseSingleFieldXOPHandler extends GenericSingleFieldXOPHandler {

    static {
        HANDLER_OPTION_MTOM_NODE_PATH = "downloadSignResponse.signature.content";
        HANDLER_OPTION_OPERATION_NAME = "downloadSign";
        HANDLER_OPTION_SERVICE_NAME = "{urn:juntadeandalucia:cice:pfirma:query:v2.0}QueryService";
    }
}
