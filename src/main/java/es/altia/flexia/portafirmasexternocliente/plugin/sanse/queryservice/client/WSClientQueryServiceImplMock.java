package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client;

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
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryUsersResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Request;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.RequestStatus;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.SignFormat;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.SignLine;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Signature;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Signer;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.State;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.User;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.UserJob;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.DownloadDocumentParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.DownloadSignParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.QueryRequestParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.QueryServiceBaseParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.QueryUsersParams;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import javax.xml.rpc.ServiceException;
import org.apache.axis.encoding.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WSClientQueryServiceImplMock implements WSClientQueryService {

    private static final Log log = LogFactory.getLog(WSClientQueryServiceImpl.class.getName());

    /**
     * @inheritDoc
     */
    @Override
    public DownloadSignResponse downloadSign(DownloadSignParams params)
            throws java.rmi.RemoteException, ExceptionInfo, MalformedURLException, ServiceException {

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
        DownloadSignResponse wsResponse = new DownloadSignResponse();
        
        byte[] firma = null;
        try {
            String ficheroPrueba = "Fichero de prueba";
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Writer w = new OutputStreamWriter(baos);
            w.write(ficheroPrueba);
            w.flush();
            w.close();
            firma = Base64.encode(baos.toByteArray()).getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            log.error("Error de codificacion");
        } catch (IOException ex) {
            log.error("Error de I/O");
        }
        
        Signature sign = new Signature();
        sign.setIdentifier(params.getIdDocumento());
        sign.setSign(Boolean.TRUE);
        sign.setSignFormat(SignFormat.fromString("PDF"));
        sign.setContent(firma);
        wsResponse.setSignature(sign);
                        
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
            throws java.rmi.RemoteException, ExceptionInfo, MalformedURLException, ServiceException {

        if (log.isDebugEnabled()) {
            log.debug("WSClientQueryServiceImpl:queryUsers");
            log.debug("PARAMS:");
            log.debug(params.toString());
        }

        // Respuesta mock
        QueryUsersResponse wsResponse = new QueryUsersResponse();
        User[] userList = new User[0];
        if (params != null && !"00000000T".equals(params.getIdentificadorUsuario())) {
            User user = new User();
            userList = new User[1];
            user.setIdentifier("00000000T");
            user.setName("nameMock");
            user.setSurname1("suname1Mock");
            user.setSurname2("suname2Mock");
            userList[0] = user;
        }
        wsResponse.setUserList(userList);
        
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
            throws java.rmi.RemoteException, ExceptionInfo, MalformedURLException, ServiceException {

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
        DownloadDocumentResponse wsResponse = new DownloadDocumentResponse();

        byte[] fichero = null;
        try {
            String ficheroPrueba = "Fichero de prueba";
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Writer w = new OutputStreamWriter(baos);
            w.write(ficheroPrueba);
            w.flush();
            w.close();
            fichero = Base64.encode(baos.toByteArray()).getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            log.error("Error de codificacion");
        } catch (IOException ex) {
            log.error("Error de I/O");
        }

        wsResponse.setDocumentBinary(fichero);
        
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
            throws java.rmi.RemoteException, ExceptionInfo, MalformedURLException, ServiceException {

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
        QueryRequestResponse wsResponse = new QueryRequestResponse();
        Request peticion = new Request();
        peticion.setIdentifier(params.getIdRequest());
        wsResponse.setRequest(peticion);
        peticion.setRequestStatus(RequestStatus.fromString("EN PROCESO"));
        
        if (StringUtils.equals("cO80TXiM", params.getIdRequest())) {
            peticion.setRequestStatus(RequestStatus.fromString("ACEPTADO"));

            SignLine[] signLineList = new SignLine[3];
            peticion.setSignLineList(signLineList);

            // Firmante 1
            SignLine signline1 = new SignLine();
            Signer[] signerList1 = new Signer[1];
            signline1.setSignerList(signerList1);
            signLineList[0] = signline1;
            Signer signer1 = new Signer();
            UserJob user1 = new User();
            user1.setIdentifier("03126498Q");
            signer1.setUserJob(user1);
            signer1.setFstate(Calendar.getInstance());
            State state1 = new State();
            state1.setIdentifier("FIRMADO");
            signer1.setState(state1);
            signerList1[0] = signer1;
            
            // Firmante 2
            SignLine signline2 = new SignLine();
            Signer[] signerList2 = new Signer[1];
            signline2.setSignerList(signerList2);
            signLineList[1] = signline2;
            Signer signer2 = new Signer();
            UserJob user2 = new User();
            user2.setIdentifier("32701958Y");
            signer2.setUserJob(user2);
            signer2.setFstate(Calendar.getInstance());
            State state2 = new State();
            state2.setIdentifier("FIRMADO");
            signer2.setState(state2);
            signerList2[0] = signer2;
            
            // Firmante 3
            SignLine signline3 = new SignLine();
            Signer[] signerList3 = new Signer[1];
            signline3.setSignerList(signerList3);
            signLineList[2] = signline3;
            Signer signer3 = new Signer();
            UserJob user3 = new User();
            user3.setIdentifier("15958257Y");
            signer3.setUserJob(user3);
            signer3.setFstate(Calendar.getInstance());
            State state3 = new State();
            state3.setIdentifier("FIRMADO");
            signer3.setState(state3);
            signerList3[0] = signer3;
        }
        
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
     *
     * @param params
     * @return
     * @throws MalformedURLException
     * @throws ServiceException
     */
    private QueryService_PortType getWSClient(QueryServiceBaseParams params) throws MalformedURLException, ServiceException {
        URL url = new URL(params.getEndPoint());
        QueryService_ServiceLocator serviceLocator = new QueryService_ServiceLocator();

        return serviceLocator.getQueryServicePort(url);
    }
}
