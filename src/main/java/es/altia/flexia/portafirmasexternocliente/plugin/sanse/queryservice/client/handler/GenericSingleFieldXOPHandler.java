package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.handler;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.attachments.AttachmentPart;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.handlers.BasicHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Obtiene el fichero referenciado por un campo en formato MTOM/XOP para
 * El valor obtenido será almacenado en un ThreadLocal, que será accesible desde
 * la clase.
 * La clase que extiende de esta clase tiene que darle valor a las siguientes variables:
 * HANDLER_OPTION_MTOM_NODE_PATH -> estructura del nodo desde el elemento de la respuesta (ejemplo: downloadDocumentResponse.documentBinary)
 * HANDLER_OPTION_OPERATION_NAME -> metodo invocado (ejemplo: downloadDocument)
 * HANDLER_OPTION_SERVICE_NAME -> Nombre del servicio (ejemplo: {urn:juntadeandalucia:cice:pfirma:query:v2.0}QueryService)
 */
public abstract class GenericSingleFieldXOPHandler extends BasicHandler {

    private final String CONCRETE_CLASS_NAME = this.getClass().getName();
    protected static String HANDLER_OPTION_MTOM_NODE_PATH = "";
    protected static String HANDLER_OPTION_OPERATION_NAME = "";
    protected static String HANDLER_OPTION_SERVICE_NAME = "";

    private static final Log log = LogFactory.getLog(GenericSingleFieldXOPHandler.class.getName());
    
    // Contenido del campo en formato MTOM/XOP
    private static final ThreadLocal<InputStream> documentStream = new ThreadLocal<InputStream>();

    /**
     * Retorna el stream que contiene el contenido del campo en formato MTOM/XOP
     * @return 
     */
    public static InputStream getDocumentStream() {
        return documentStream.get();
    }

    /**
     * Borra el valor del stream que contiene el contenido del campo en formato MTOM/XOP.
     * Debe llamarse antes de finalizar la petición para evitar que la información
     * pueda ser accesible desde otra petición.
     */
    public static void cleanDocumentStream() {
        documentStream.remove();
    }
    /**
     * Copia el contenido referenciado por el nodo XOP a un stream en el Threadlocal
     * 
     * @param msgContext
     * @throws org.apache.axis.AxisFault
     */
    @Override
    public void invoke(MessageContext msgContext) throws AxisFault {
        
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s::invoke", CONCRETE_CLASS_NAME));
        }
        
        if (isTargetedServiceAndOperation(msgContext)) {
                String[] pathToNode = HANDLER_OPTION_MTOM_NODE_PATH.split("\\.");
                int nodesLevel = pathToNode.length;

                try {
                    // Obtenemos el body del SOAP
                    Message msg = msgContext.getResponseMessage();
                    SOAPMessage soapMessage = msgContext.getResponseMessage();
                    SOAPBody soapBody = soapMessage.getSOAPBody();

                    // Busqueda del nodo con el contenido XOP en el body del SOAP
                    Node currentNode = null;
                    int currentNodeLevel = 0;
                    NodeList nodeList = soapBody.getChildNodes();
                    while (currentNodeLevel < nodesLevel && nodeList != null) {
                        currentNode = null;
                        for (int i = 0; i < nodeList.getLength(); i++) {
                            if (nodeList.item(i).getLocalName().equals(pathToNode[currentNodeLevel])) {
                                currentNode = nodeList.item(i);
                                break;
                            }
                        }
                        if (currentNode != null) {
                            nodeList = currentNode.getChildNodes();
                        } else {
                            nodeList = null;
                        }
                        currentNodeLevel++;
                    }

                    // Encontrado el nodo a tratar
                    if (currentNode != null) {
                        // Obtenemos la referencia al contenido al que apunta el nodo XOP
                        String attachmentRef = null;
                        if (currentNode.getChildNodes() != null
                                && currentNode.getChildNodes().getLength() > 0) {
                            NamedNodeMap nnm = currentNode.getChildNodes().item(0).getAttributes();
                            for (int j = 0; j < nnm.getLength(); j++) {
                                attachmentRef = nnm.item(j).getNodeValue();
                            }
                            
                            // Se eliminar el nodo XOP para evitar errores de 
                            // unmarshalling del servicio de Axis
                            currentNode.getParentNode().removeChild(currentNode);
                        } else {
                            log.debug(String.format("No se ha encontrado la referencia en el nodo: %s", HANDLER_OPTION_MTOM_NODE_PATH));
                        }

                        // Recupera el documento referenciado por el elemento XOP
                        if (attachmentRef != null) {
                            Iterator attachments = msg.getAttachments();
                            while (attachments.hasNext()) {
                                AttachmentPart attachmentPart = (AttachmentPart) attachments.next();
                                if (attachmentPart.getContentIdRef().equals(attachmentRef)) {
                                    documentStream.set(attachmentPart.getDataHandler().getInputStream());
                                }
                            }

                            // Operaciones SOAP para persistir los cambios
                            TransformerFactory tff = TransformerFactory.newInstance();
                            Transformer tf = tff.newTransformer();
                            Source sc = soapMessage.getSOAPPart().getContent();
                            StringWriter modifiedBody = new StringWriter();
                            StreamResult result = new StreamResult(modifiedBody);
                            tf.transform(sc, result);
                            Message modifiedMsg = new Message(modifiedBody.toString(), false);
                            msgContext.setResponseMessage(modifiedMsg);
                        } else {
                            if (log.isDebugEnabled()) {
                                log.debug(String.format("No se ha encontrado la referencia al fichero XOP en el nodo: %s", HANDLER_OPTION_MTOM_NODE_PATH));
                            }
                        }
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug(String.format("No se ha encontrado el nodo en el body de la respuesta SOAP: %s", HANDLER_OPTION_MTOM_NODE_PATH));
                        }
                    }
                } catch (Exception e) {
                    throw new AxisFault(String.format("Error en el handler %s", CONCRETE_CLASS_NAME), e);
                }
        }
    }

    /**
     * Comprueba que la llamada se corresponde al servicio y metodo deseado.
     */
    private boolean isTargetedServiceAndOperation(MessageContext msgContext) {
        boolean parametersFound = false;

        Service locator = (Service) msgContext.getProperty(Call.WSDL_SERVICE);
        if (locator != null && locator.getServiceName().toString().equals(HANDLER_OPTION_SERVICE_NAME)) {
            if (msgContext.getOperation().getName().equals(HANDLER_OPTION_OPERATION_NAME)) {
                parametersFound = true;
                
                if (log.isDebugEnabled()) {
                    log.debug(String.format("Encontrada operacion a procesar: %s ", HANDLER_OPTION_OPERATION_NAME));
                }
            }
        }

        return parametersFound;
    }
}
