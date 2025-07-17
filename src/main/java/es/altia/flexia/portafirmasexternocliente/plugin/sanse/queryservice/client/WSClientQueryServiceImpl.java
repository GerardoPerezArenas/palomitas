package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client;

import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.handler.DownloadDocumentResponseSingleFieldXOPHandler;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.handler.DownloadSignResponseSingleFieldXOPHandler;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.QueryUsersParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Authentication;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.QueryService_PortType;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.QueryService_ServiceLocator;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadDocument;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadDocumentResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadSign;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadSignResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryRequest;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryRequestResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryUsers;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryUsersResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Request;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Signature;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.User;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.DownloadDocumentParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.DownloadSignParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.QueryRequestParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.QueryServiceBaseParams;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.rpc.ServiceException;
import org.apache.axis.Handler;
import org.apache.axis.SimpleChain;
import org.apache.axis.SimpleTargetedChain;
import org.apache.axis.configuration.SimpleProvider;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.transport.http.HTTPSender;
import org.apache.axis.transport.http.HTTPTransport;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WSClientQueryServiceImpl implements WSClientQueryService {

    private static final Log log = LogFactory.getLog(WSClientQueryServiceImpl.class.getName());

    /**
     * @inheritDoc
     */
    @Override
    public DownloadSignResponse downloadSign(DownloadSignParams params)
            throws java.rmi.RemoteException, ExceptionInfo, MalformedURLException,
            ServiceException, IOException, InstantiationException, IllegalAccessException {

        DownloadSign query = new DownloadSign();

        if (log.isDebugEnabled()) {
            log.debug("WSClientQueryServiceImpl:downloadSign");
            log.debug("PARAMS:");
            log.debug(params.toString());
        }

        // Credenciales de acceso al metodo. Se envian como parametros de entrada
        query.setAuthentication(getAuthentication(params));

        // Parametros de entrada del servicio web
        query.setDocumentId(params.getIdDocumento());

        // Llamada al servicio web
        log.debug("Realizando llamada al servicio web...");
        DownloadSignResponse wsResponse = null;
        try {
            QueryService_PortType wsClient = getWSClient(params, null, DownloadSignResponseSingleFieldXOPHandler.class);
            wsResponse = wsClient.downloadSign(query);

            if (wsResponse != null && wsResponse.getSignature() != null) {
                wsResponse.getSignature().setContent(IOUtils.toByteArray(DownloadSignResponseSingleFieldXOPHandler.getDocumentStream()));
            }
        } finally {
            // Limpiamos el contenido de la variable ThreadLocal para evitar memory leaks
            // y compartir informacion entre hilos.
            DownloadSignResponseSingleFieldXOPHandler.cleanDocumentStream();
        }
        
        if (log.isDebugEnabled()) {
            log.debug("Respuesta del servicio web: ");
            log.debug(String.format("Respuesta es null -> %s", (wsResponse != null) ? "NO" : "SI"));
            if (wsResponse != null) {
                log.debug(String.format("Signature es null -> %s", (wsResponse.getSignature() != null) ? "NO" : "SI"));
                if (wsResponse.getSignature() != null) {
                    Signature signature = wsResponse.getSignature();
                    log.debug(String.format("Signature: identifier[%s], sign[%b], content is null[%s]",
                            signature.getIdentifier(), signature.getSign(), (signature.getContent() != null) ? "NO" : "SI"));
                }
            }
        }

        return wsResponse;
    }

    /**
     * @inheritDoc
     */
    @Override
    public QueryUsersResponse queryUsers(QueryUsersParams params)
            throws java.rmi.RemoteException, ExceptionInfo, MalformedURLException,
            ServiceException, InstantiationException, IllegalAccessException {

        QueryUsers query = new QueryUsers();

        if (log.isDebugEnabled()) {
            log.debug("WSClientQueryServiceImpl:queryUsers");
            log.debug("PARAMS:");
            log.debug(params.toString());
        }

        // Credenciales de acceso al metodo. Se envian como parametros de entrada
        query.setAuthentication(getAuthentication(params));

        // NIF del usuario a buscar
        query.setQuery(params.getIdentificadorUsuario());

        // Llamada al servicio web
        log.debug("Realizando llamada al servicio web...");
        QueryService_PortType wsClient = getWSClient(params, null, null);
        QueryUsersResponse wsResponse = wsClient.queryUsers(query);

        if (log.isDebugEnabled()) {
            log.debug("Respuesta del servicio web: ");
            log.debug(String.format("Respuesta es null -> %s", (wsResponse != null) ? "NO" : "SI"));

            if (wsResponse != null) {
                log.debug(String.format("UserList es null -> %s", (wsResponse.getUserList() != null) ? "NO" : "SI"));
                if (wsResponse.getUserList() != null) {
                    User[] users = wsResponse.getUserList();
                    log.debug(String.format("userList.length = %d: ", users.length));

                    for (int i = 0; i < users.length; i++) {
                        User user = users[i];
                        log.debug(String.format("User[%d]: name[%s], surname1[%s], surname2[%s]",
                                i, user.getName(), user.getSurname1(), user.getSurname2()));
                    }
                }
            }
        }

        return wsResponse;
    }

    /**
     * @inheritDoc
     */
    @Override
    public DownloadDocumentResponse downloadDocument(DownloadDocumentParams params)
            throws java.rmi.RemoteException, ExceptionInfo, MalformedURLException,
            ServiceException, IOException, InstantiationException, IllegalAccessException {

        DownloadDocument query = new DownloadDocument();

        if (log.isDebugEnabled()) {
            log.debug("WSClientQueryServiceImpl:downloadDocument");
            log.debug("PARAMS:");
            log.debug(params.toString());
        }

        // Credenciales de acceso al metodo. Se envian como parametros de entrada
        query.setAuthentication(getAuthentication(params));

        // Parametros de entrada del servicio web
        query.setDocumentId(params.getIdDocumento());

        // Llamada al servicio web
        log.debug("Realizando llamada al servicio web...");
        DownloadDocumentResponse wsResponse = null;
        try {
            QueryService_PortType wsClient = getWSClient(params, null, DownloadDocumentResponseSingleFieldXOPHandler.class);
            wsResponse = wsClient.downloadDocument(query);
            
            if (wsResponse != null) {
                wsResponse.setDocumentBinary(IOUtils.toByteArray(DownloadDocumentResponseSingleFieldXOPHandler.getDocumentStream()));
            }
        } finally {
            // Limpiamos el contenido de la variable ThreadLocal para evitar memory leaks
            // y compartir informacion entre hilos.
            DownloadSignResponseSingleFieldXOPHandler.cleanDocumentStream();
        }
        
        if (log.isDebugEnabled()) {
            log.debug("Respuesta del servicio web: ");
            log.debug(String.format("Respuesta es null -> %s", (wsResponse != null) ? "NO" : "SI"));
            if (wsResponse != null) {
                log.debug(String.format("DocumentBinary es null -> %s", (wsResponse.getDocumentBinary() != null) ? "NO" : "SI"));
                if (wsResponse.getDocumentBinary() != null) {
                    log.debug(String.format("DocumentBinary.length = %d: ", wsResponse.getDocumentBinary().length));
                }
            }
        }

        return wsResponse;
    }

    /**
     * @inheritDoc
     */
    @Override
    public QueryRequestResponse queryRequest(QueryRequestParams params)
            throws java.rmi.RemoteException, ExceptionInfo, MalformedURLException,
            ServiceException, InstantiationException, IllegalAccessException {

        QueryRequest query = new QueryRequest();

        if (log.isDebugEnabled()) {
            log.debug("WSClientQueryServiceImpl:queryRequest");
            log.debug("PARAMS:");
            log.debug(params.toString());
        }

        // Credenciales de acceso al metodo. Se envian como parametros de entrada
        query.setAuthentication(getAuthentication(params));

        // Parametros de entrada del servicio web
        query.setRequestId(params.getIdRequest());

        // Llamada al servicio web
        log.debug("Realizando llamada al servicio web...");
        QueryService_PortType wsClient = getWSClient(params, null, null);
        QueryRequestResponse wsResponse = wsClient.queryRequest(query);

        if (log.isDebugEnabled()) {
            log.debug("Respuesta del servicio web: ");
            log.debug(String.format("Respuesta es null -> %s", (wsResponse != null) ? "NO" : "SI"));
            if (wsResponse != null) {
                log.debug(String.format("Request es null -> %s", (wsResponse.getRequest() != null) ? "NO" : "SI"));
                if (wsResponse.getRequest() != null) {
                    Request request = wsResponse.getRequest();
                    log.debug("TODO MOSTRAR REQUEST");
                }
            }
        }

        return wsResponse;
    }

    /**
     * Se obtiene el objeto autenticacion para las llamadas a los metodos del
     * servicio web.
     *
     * @param params
     * @return
     */
    private Authentication getAuthentication(QueryServiceBaseParams params) {
        Authentication auth = new Authentication();

        if (params != null) {
            auth.setUserName(params.getUsername());
            auth.setPassword(params.getPassword());
        }

        return auth;
    }

    /**
     * Se obtiene el cliente para realizar las llamadas al servicio web.
     * Se le pueden especificar handlers tanto para la request como para la response.
     * 
     * @param params parametros de la peticion
     * @param requestClassHandler la clase del handler de la request
     * @param responseClassHandler la clase del handler de la respuesta
     * @return
     * @throws MalformedURLException
     * @throws ServiceException
     */
    private QueryService_PortType getWSClient(
            QueryServiceBaseParams params,
            Class<? extends BasicHandler> requestClassHandler,
            Class<? extends BasicHandler> responseClassHandler)
            throws MalformedURLException, ServiceException, InstantiationException, IllegalAccessException {
        URL url = new URL(params.getEndPoint());
        
        QueryService_ServiceLocator serviceLocator = null;
        if (requestClassHandler != null || responseClassHandler != null) {
            serviceLocator = new QueryService_ServiceLocator(
                    registrarHandler(requestClassHandler, responseClassHandler));
        } else {
            serviceLocator = new QueryService_ServiceLocator();
        }

        return serviceLocator.getQueryServicePort(url);
    }
    
    /**
     * Registramos un handler para poder extraer el contenido en formato MTOM/XOP
     * Esto afecta, de momento, solo a los métodos downloadSign y downloadDocument.
     * 
     * @param requestClassHandler la clase del handler de la request
     * @param responseClassHandler la clase del handler de la respuesta
     * @return 
     */
    private static SimpleProvider registrarHandler(
            Class<? extends BasicHandler> requestClassHandler,
            Class<? extends BasicHandler> responseClassHandler) throws InstantiationException, IllegalAccessException {
        
        SimpleProvider clientConfig = new SimpleProvider();
        
        // Anadimos el handler de la request
        SimpleChain reqHandler = null;
        if (requestClassHandler != null) {
            reqHandler = new SimpleChain();
            BasicHandler handler = requestClassHandler.newInstance();
            reqHandler.addHandler(handler);
        }
        
        // Anadimos el handler de la respuesta
        SimpleChain respHandler = null;
        if (responseClassHandler != null) {
            respHandler = new SimpleChain();
            BasicHandler handler = responseClassHandler.newInstance();
            respHandler.addHandler(handler);
        }
        
        Handler pivot = new HTTPSender();
        Handler transport = new SimpleTargetedChain(reqHandler, pivot, respHandler);
        
        clientConfig.deployTransport(HTTPTransport.DEFAULT_TRANSPORT_NAME, transport);
        return clientConfig;
    }
}
