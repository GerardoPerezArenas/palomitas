package es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client;

import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.CreateRequestResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.DeleteRequestResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertDocumentResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.InsertSignersResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.modify.request.SendRequestResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.type.ExceptionInfo;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo.CreateRequestParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo.DeleteRequestParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo.InsertDocumentParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo.InsertSignersParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo.SendRequestParams;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;

public interface WSClientModifyService {

    /**
     * Crea una petici�n que a�n no se ha enviado a los firmantes.
     *
     * @param params Parametros necesarios para invocar el metodo del servicio
     * web
     * @return
     * @throws RemoteException
     * @throws ExceptionInfo
     * @throws MalformedURLException
     * @throws ServiceException
     */
    CreateRequestResponse createRequest(CreateRequestParams params)
            throws RemoteException, ExceptionInfo, MalformedURLException, ServiceException;

    /**
     * Borra una petici�n que a�n no se ha enviado a los firmantes.
     *
     * @param params Parametros necesarios para invocar el metodo del servicio
     * web
     * @return
     * @throws RemoteException
     * @throws ExceptionInfo
     * @throws MalformedURLException
     * @throws ServiceException
     */
    DeleteRequestResponse deleteRequest(DeleteRequestParams params)
            throws RemoteException, ExceptionInfo, MalformedURLException, ServiceException;

    /**
     * A�ade un documento a la request. El documento que se env�a desde Flexia
     * al Port@firmas se inserta en este m�todo.
     *
     * @param params Parametros necesarios para invocar el metodo del servicio
     * web
     * @return
     * @throws RemoteException
     * @throws ExceptionInfo
     * @throws MalformedURLException
     * @throws ServiceException
     */
    InsertDocumentResponse insertDocument(InsertDocumentParams params)
            throws RemoteException, ExceptionInfo, MalformedURLException, ServiceException;

    /**
     * A�ade lineas de firma (todas las lineas indican el flujo de firma por el
     * que ha de pasar la petici�n). Estas lineas son las definidas como flujo
     * de firma en flexia (o si e una �nica linea ser� el usuario firmante)
     *
     * @param params Parametros necesarios para invocar el metodo del servicio
     * web
     * @return
     * @throws RemoteException
     * @throws ExceptionInfo
     * @throws MalformedURLException
     * @throws ServiceException
     */
    InsertSignersResponse insertSigners(InsertSignersParams params)
            throws RemoteException, ExceptionInfo, MalformedURLException, ServiceException;

    /**
     * Env�a la petici�n. En este momento llegar� a los usuarios
     * correspondientes. Se puede definir el orden de firma en paralelo o en
     * cascada.
     *
     * @param params Parametros necesarios para invocar el metodo del servicio
     * web
     * @return
     * @throws RemoteException
     * @throws ExceptionInfo
     * @throws MalformedURLException
     * @throws ServiceException
     */
    SendRequestResponse sendRequest(SendRequestParams params)
            throws RemoteException, ExceptionInfo, MalformedURLException, ServiceException;

}
