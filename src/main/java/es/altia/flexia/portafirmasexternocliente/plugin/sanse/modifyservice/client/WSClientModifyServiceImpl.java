package es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client;

import es.altia.agora.business.sge.firma.vo.FirmaCircuitoVO;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.ModifyService_PortType;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.ModifyService_ServiceLocator;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.CreateRequest;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.CreateRequestResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteRequest;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteRequestResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertDocument;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertDocumentResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertSigners;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertSignersResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.SendRequest;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.SendRequestResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo.ModifyServiceBaseParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Authentication;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Document;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.DocumentType;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ImportanceLevel;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Request;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.SignLine;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.SignLineType;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.SignType;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.Signer;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.TimestampInfo;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.User;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.UserJob;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo.CreateRequestParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo.DeleteRequestParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo.InsertDocumentParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo.InsertSignersParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo.SendRequestParams;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.rpc.ServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WSClientModifyServiceImpl implements WSClientModifyService {

    private static final Log log = LogFactory.getLog(WSClientModifyServiceImpl.class.getName());

    /**
     * @inheritDoc
     */
    @Override
    public CreateRequestResponse createRequest(CreateRequestParams params)
            throws RemoteException, ExceptionInfo, MalformedURLException, ServiceException {

        CreateRequest query = new CreateRequest();

        if (log.isDebugEnabled()) {
            log.debug("WSClientModifyServiceImpl:createRequest");
            log.debug("PARAMS:");
            log.debug(params.toString());
        }

        // Credenciales de acceso al metodo. Se envian como parametros de entrada
        query.setAuthentication(getAuthentication(params));

        // Parametros de entrada del servicio web
        User user = new User();
        user.setIdentifier(params.getIdRemitente());
        User[] remitentes = new User[] { user };
        
        ImportanceLevel nivelImportancia = new ImportanceLevel();
        nivelImportancia.setLevelCode(params.getNivelImportanciaId());
        nivelImportancia.setDescription(params.getNivelImportanciaDescripcion());

        TimestampInfo timestamp = new TimestampInfo();
        timestamp.setAddTimestamp(params.isSelloDeTiempo());
        
        Request request = new Request();
        request.setReference(params.getReferencia());
        request.setFentry(params.getFechaInicio());
        request.setFstart(params.getFechaInicio());
        request.setFexpiration(params.getFechaExpiracion());
        request.setSubject(params.getAsunto());
        request.setText(params.getTexto());
        request.setRemitterList(remitentes);
        request.setImportanceLevel(nivelImportancia);
        request.setSignType(SignType.fromValue(params.getTipoFirma()));
        request.setApplication(params.getAplicacion());
        request.setTimestampInfo(timestamp);
        request.setSignLineList(getListaFirmantes(params));
        
        query.setRequest(request);

        // Llamada al servicio web
        log.debug("Realizando llamada al servicio web...");
        ModifyService_PortType wsClient = getWSClient(params);
        CreateRequestResponse wsResponse = wsClient.createRequest(query);

        if (log.isDebugEnabled()) {
            log.debug("Respuesta del servicio web: ");
            log.debug(String.format("Respuesta es null -> %s", (wsResponse != null) ? "NO" : "SI"));
            if (wsResponse != null) {
                log.debug(String.format("RequestId[%s]", wsResponse.getRequestId()));
            }
        }

        return wsResponse;
    }

    /**
     * @inheritDoc
     */
    @Override
    public DeleteRequestResponse deleteRequest(DeleteRequestParams params)
            throws RemoteException, ExceptionInfo, MalformedURLException, ServiceException {

        DeleteRequest query = new DeleteRequest();

        if (log.isDebugEnabled()) {
            log.debug("WSClientModifyServiceImpl:deleteRequest");
            log.debug("PARAMS:");
            log.debug(params.toString());
        }

        // Credenciales de acceso al metodo. Se envian como parametros de entrada
        query.setAuthentication(getAuthentication(params));

        // Parametros de entrada del servicio web
        query.setRequestId(params.getIdRequest());

        // Llamada al servicio web
        log.debug("Realizando llamada al servicio web...");
        ModifyService_PortType wsClient = getWSClient(params);
        DeleteRequestResponse wsResponse = wsClient.deleteRequest(query);

        if (log.isDebugEnabled()) {
            log.debug("Respuesta del servicio web: ");
            log.debug(String.format("Respuesta es null -> %s", (wsResponse != null) ? "NO" : "SI"));
            if (wsResponse != null) {
                log.debug(String.format("RequestId[%s]", wsResponse.getRequestId()));
            }
        }

        return wsResponse;
    }

    /**
     * @inheritDoc
     */
    @Override
    public InsertDocumentResponse insertDocument(InsertDocumentParams params)
            throws RemoteException, ExceptionInfo, MalformedURLException, ServiceException {

        InsertDocument query = new InsertDocument();

        if (log.isDebugEnabled()) {
            log.debug("WSClientModifyServiceImpl:insertDocument");
            log.debug("PARAMS:");
            log.debug(params.toString());
        }

        // Credenciales de acceso al metodo. Se envian como parametros de entrada
        query.setAuthentication(getAuthentication(params));

        // Parametros de entrada del servicio web
        DocumentType docType = new DocumentType();
        docType.setIdentifier(params.getTipoDocumentoId());
        docType.setDescription(params.getTipoDocumentoDescripcion());
        docType.setValid(Boolean.TRUE);

        Document documento = new Document();
        documento.setName(params.getNombreDocumento());
        documento.setMime(params.getTipoMime());
        documento.setContent(params.getContenido());
        documento.setSign(Boolean.TRUE);
        documento.setDocumentType(docType);

        query.setRequestId(params.getIdRequest());
        query.setDocument(documento);

        // Llamada al servicio web
        log.debug("Realizando llamada al servicio web...");
        ModifyService_PortType wsClient = getWSClient(params);
        InsertDocumentResponse wsResponse = wsClient.insertDocument(query);

        if (log.isDebugEnabled()) {
            log.debug("Respuesta del servicio web: ");
            log.debug(String.format("Respuesta es null -> %s", (wsResponse != null) ? "NO" : "SI"));
            if (wsResponse != null) {
                log.debug(String.format("DocumentId[%s]", wsResponse.getDocumentId()));
            }
        }

        return wsResponse;
    }

    /**
     * @inheritDoc
     */
    @Override
    public InsertSignersResponse insertSigners(InsertSignersParams params)
            throws RemoteException, ExceptionInfo, MalformedURLException, ServiceException {

        InsertSigners query = new InsertSigners();

        if (log.isDebugEnabled()) {
            log.debug("WSClientModifyServiceImpl:insertSigners");
            log.debug("PARAMS:");
            log.debug(params.toString());
        }

        // Credenciales de acceso al metodo. Se envian como parametros de entrada
        query.setAuthentication(getAuthentication(params));

        // Parametros de entrada del servicio web
        query.setRequestId(params.getIdRequest());
        query.setSignLine(params.getLineaFirma());
        query.setSignLineType(SignLineType.FIRMA);
        query.setSignerList(getListaFirmantes(params));

        // Llamada al servicio web
        log.debug("Realizando llamada al servicio web...");
        ModifyService_PortType wsClient = getWSClient(params);
        InsertSignersResponse wsResponse = wsClient.insertSigners(query);

        if (log.isDebugEnabled()) {
            log.debug("Respuesta del servicio web: ");
            log.debug(String.format("Respuesta es null -> %s", (wsResponse != null) ? "NO" : "SI"));
            if (wsResponse != null) {
                log.debug(String.format("RequestId[%s]", wsResponse.getRequestId()));
            }
        }

        return wsResponse;
    }

    /**
     * @inheritDoc
     */
    @Override
    public SendRequestResponse sendRequest(SendRequestParams params)
            throws RemoteException, ExceptionInfo, MalformedURLException, ServiceException {

        SendRequest query = new SendRequest();

        if (log.isDebugEnabled()) {
            log.debug("WSClientModifyServiceImpl:sendRequest");
            log.debug("PARAMS:");
            log.debug(params.toString());
        }

        // Credenciales de acceso al metodo. Se envian como parametros de entrada
        query.setAuthentication(getAuthentication(params));

        // Parametros de entrada del servicio web
        query.setRequestId(params.getIdRequest());

        // Llamada al servicio web
        log.debug("Realizando llamada al servicio web...");
        ModifyService_PortType wsClient = getWSClient(params);
        SendRequestResponse wsResponse = wsClient.sendRequest(query);

        if (log.isDebugEnabled()) {
            log.debug("Respuesta del servicio web: ");
            log.debug(String.format("Respuesta es null -> %s", (wsResponse != null) ? "NO" : "SI"));
            if (wsResponse != null) {
                log.debug(String.format("RequestId[%s]", wsResponse.getRequestId()));
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
    private Authentication getAuthentication(ModifyServiceBaseParams params) {
        Authentication auth = new Authentication();

        if (params != null) {
            auth.setUserName(params.getUsername());
            auth.setPassword(params.getPassword());
        }

        return auth;
    }

    /**
     * Se obtiene el cliente para realizar las llamadas al servicio web.
     *
     * @param params
     * @return
     * @throws MalformedURLException
     * @throws ServiceException
     */
    private ModifyService_PortType getWSClient(ModifyServiceBaseParams params) throws MalformedURLException, ServiceException {
        URL url = new URL(params.getEndPoint());
        ModifyService_ServiceLocator serviceLocator = new ModifyService_ServiceLocator();

        return serviceLocator.getModifyServicePort(url);
    }

    /**
     * Obtiene la lista de firmantes a partir de los parametros
     *
     * @param params
     * @return
     */
    private Signer[] getListaFirmantes(InsertSignersParams params) {
        List<Signer> listaFirmantes = new ArrayList<Signer>();

        List<String> listaIdFirmantes = params.getListaIdFirmantes();
        if (listaIdFirmantes != null && !listaIdFirmantes.isEmpty()) {
            Signer signer = null;
            User user = null;

            for (String idFirmante : listaIdFirmantes) {
                user = new User();
                user.setIdentifier(idFirmante);

                signer = new Signer();
                signer.setUserJob(user);

                listaFirmantes.add(signer);
            }
        }

        return listaFirmantes.toArray(new Signer[listaFirmantes.size()]);
    }
    
    /**
     * Obtiene la lista de firmantes a partir de los parametros
     *
     * @param params
     * @return
     */
    private SignLine[] getListaFirmantes(CreateRequestParams params) {
        SignLine[] resultado = null;
        List<SignLine> listaFirmaLineas = new ArrayList<SignLine>();
        List<FirmaCircuitoVO> listaFirmantesCircuito = params.getListaFirmantes();
        
        if (listaFirmantesCircuito != null && !listaFirmantesCircuito.isEmpty()) {
            SignLine lineaFirma = null;
            Signer firmante = null;
            
            for (int i = 0; i < listaFirmantesCircuito.size(); i++) {
                firmante = getSignerFromFirmaCircuitoVO(listaFirmantesCircuito.get(i));
                
                if (firmante != null) {
                    lineaFirma = new SignLine();
                    lineaFirma.setSignerList(new Signer[]{ firmante });
                    listaFirmaLineas.add(lineaFirma);
                }
            }
            
            /* El servicio web de SANSE tiene varios errores.
             1. Si la lista de SignLine es null, falla el servicio web con un 
             Unknown Error (posiblemente un Nullpointer).
             2. Si la lista de SignLine no es null, pero está vacía, no es capaz
             de encontrar la petición, por lo que no se debe enviar vacía.
        
             Por lo tanto, para que falle si no hay firmantes se la enviamos a
             null, de momento.
             */
            if (!listaFirmaLineas.isEmpty()) {
                resultado = listaFirmaLineas.toArray(new SignLine[listaFirmaLineas.size()]);
            }
        }
        
        return resultado;
    }

    /**
     * Convierte un FirmaCircuitoVO a un Signer
     * 
     * @param firmaCircuito
     * @return 
     */
    private Signer getSignerFromFirmaCircuitoVO(FirmaCircuitoVO firmaCircuito) {
        Signer signer = null;
        
        if (firmaCircuito != null) {
            UserJob user = new User();
            user.setIdentifier(firmaCircuito.getDocumentoUsuario());
            
            signer = new Signer();
            signer.setUserJob(user);
        }
        
        return signer;
    }
}
