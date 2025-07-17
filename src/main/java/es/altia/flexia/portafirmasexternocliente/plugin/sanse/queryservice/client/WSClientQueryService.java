package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client;

import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadDocumentResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadSignResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryRequestResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryUsersResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.DownloadDocumentParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.DownloadSignParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.QueryRequestParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.QueryUsersParams;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;

public interface WSClientQueryService {

    /**
     * Permite descargar la firma de un documento.
     *
     * @param params Parametros necesarios para invocar el metodo del servicio web
     * @return
     * @throws RemoteException
     * @throws ExceptionInfo
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    DownloadSignResponse downloadSign(DownloadSignParams params)
            throws RemoteException, ExceptionInfo, MalformedURLException,
            ServiceException, IOException, InstantiationException, IllegalAccessException;

    /**
     * Obtiene una lista de usuarios definidos en el Port@firmas.
     *
     * @param params Parametros necesarios para invocar el metodo del servicio web
     * @return
     * @throws RemoteException
     * @throws ExceptionInfo
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    QueryUsersResponse queryUsers(QueryUsersParams params)
            throws RemoteException, ExceptionInfo, MalformedURLException,
            ServiceException, InstantiationException, IllegalAccessException;

    /**
     * Permite descargar un documento
     *
     * @param params Parametros necesarios para invocar el metodo del servicio web
     * @return
     * @throws RemoteException
     * @throws ExceptionInfo
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    DownloadDocumentResponse downloadDocument(DownloadDocumentParams params)
            throws RemoteException, ExceptionInfo, MalformedURLException,
            ServiceException, IOException, InstantiationException, IllegalAccessException;

    /**
     * Obtiene la información de una petición enviada, incluyendo el estado (No
     * descarga el contenido de documentos, para ello es necesario llamar al
     * método downloadDocument)
     *
     * @param params Parametros necesarios para invocar el metodo del servicio web
     * @return
     * @throws RemoteException
     * @throws ExceptionInfo
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    QueryRequestResponse queryRequest(QueryRequestParams params)
            throws RemoteException, ExceptionInfo, MalformedURLException,
            ServiceException, InstantiationException, IllegalAccessException;
}
