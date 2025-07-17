package es.altia.flexia.portafirmasexternocliente.plugin;

import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.firma.vo.FirmaCircuitoVO;
import es.altia.agora.business.sge.firma.vo.FirmaDocumentoTramitacionVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.portafirmas.plugin.util.ConstantesPortafirmas;
import es.altia.flexia.portafirmasexternocliente.plugin.persistance.PluginPortafirmasExternoClienteManager;
import es.altia.flexia.portafirmasexternocliente.plugin.persistance.PluginPortafirmasExternoClienteSanseManager;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.ConstantesPortafirmasSanse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.SanseUtils;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.WSClientModifyService;
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
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo.ModifyServiceBaseParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo.SendRequestParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.WSClientQueryService;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadDocumentResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.DownloadSignResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryRequestResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request.QueryUsersResponse;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Request;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.DownloadDocumentParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.DownloadSignParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.QueryRequestParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.QueryUsersParams;
import es.altia.flexia.portafirmasexternocliente.vo.DocumentoTramitacionVO;
import es.altia.util.commons.MimeTypes;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.xml.rpc.ServiceException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PluginPortafirmasExternoClienteSanse implements PluginPortafirmasExternoCliente {

    // Logger
    private static final Log log = LogFactory.getLog(PluginPortafirmasExternoClienteSanse.class.getName());

    // Managers
    private static PluginPortafirmasExternoClienteSanseManager pluginSanseManager;
    private static PluginPortafirmasExternoClienteManager pluginCommonManager;
 
    // Clientes
    WSClientQueryService clienteQuery = null;
    WSClientModifyService clienteModify = null;
    
    public PluginPortafirmasExternoClienteSanse() {
        try {
            pluginSanseManager = PluginPortafirmasExternoClienteSanseManager.getInstance();
            pluginCommonManager = PluginPortafirmasExternoClienteManager.getInstance();
            
            clienteQuery =  (WSClientQueryService) Class.forName(SanseUtils.getPropiedadSanse(ConstantesPortafirmasSanse.PROPERTIES_QUERYSERVICE_IMPLCLASS)).newInstance();
            clienteModify = (WSClientModifyService) Class.forName(SanseUtils.getPropiedadSanse(ConstantesPortafirmasSanse.PROPERTIES_MODIFYSERVICE_IMPLCLASS)).newInstance();
        } catch (InstantiationException ex) {
            log.fatal(String.format("No se ha podido instanciar la clase PluginPortafirmasExternoClienteSanse: %s", ex.getMessage()));
        } catch (IllegalAccessException ex) {
            log.fatal(String.format("No se ha podido instanciar la clase PluginPortafirmasExternoClienteSanse: %s", ex.getMessage()));
        } catch (ClassNotFoundException ex) {
            log.fatal(String.format("No se ha podido instanciar la clase PluginPortafirmasExternoClienteSanse: %s", ex.getMessage()));
        }
    }
    
    /**
     * Envia al portafirmas de externo de sanse el documento indicado mediante los parametros del metodo
     * 
     * @param codProcedimiento
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param codDocumentoEnviar Codigo de documento pdf que se va a enviar al portafirmas
     * @param codDocumentoOriginal Código de documento doc original
     * @return String
     */
    public String enviarDocumentoTramitacionPortafirmas(String codOrganizacion, String codProcedimiento, String numExpediente, String codTramite, 
            String ocurrenciaTramite, String codDocumentoEnviar, String codDocumentoOriginal, int idUsuario, byte[] contenido, String[] params, String portafirmas) {
        
        // En esta implementacion codDocumentoOriginal es nulo y no se tiene en cuenta
        return this.enviarDocumentoTramitacionPortafirmas(codOrganizacion, codProcedimiento, numExpediente, codTramite, ocurrenciaTramite, codDocumentoEnviar, idUsuario, contenido, params, portafirmas);
    
    }

    /**
     * Envia al portafirmas de externo de sanse el documento indicado mediante los parametros del metodo
     * 
     * @param codProcedimiento
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param codDocumento
     * @return String
     */
    @Override
    public String enviarDocumentoTramitacionPortafirmas(
            String codOrganizacion, String codProcedimiento, String numExpediente,
            String codTramite, String ocurrenciaTramite, String codDocumento, int idUsuario, byte[] contenido,
            String[] params, String portafirmas) {

        String idPeticion = ConstantesPortafirmasSanse.ID_PETICION_NULA;

        log.debug("enviarDocumentoTramitacionPortafirmas() : BEGIN");

        try {
            GeneralValueObject datos = new GeneralValueObject();
            Integer codOrg = NumberUtils.createInteger(codOrganizacion);
            Integer codTram = NumberUtils.createInteger(codTramite);
            Integer ocuTram = NumberUtils.createInteger(ocurrenciaTramite);
            Integer codDoc = NumberUtils.createInteger(codDocumento);
            Integer ejercicio = SanseUtils.extraerEjercicioNumExpediente(numExpediente);

            ModifyServiceBaseParams paramBase = new ModifyServiceBaseParams();
            SanseUtils.rellenarBaseParams(codOrganizacion, paramBase);

            // Obtencion de los datos necesarios para enviar la peticion al
            // Portafirmas de SANSE
            pluginSanseManager.obtenerDatosInsertarDocumento(
                    codOrg, codProcedimiento, ejercicio,
                    numExpediente, codTram, ocuTram, codDoc,
                    params, datos);
                        
            pluginSanseManager.obtenerDatosCreacionPeticion(
                    codOrg, codProcedimiento, ejercicio,
                    numExpediente, codTram, ocuTram, codDoc,
                    params, datos);
            
            pluginSanseManager.obtenerDatosInsertarFirmantes(
                    codOrg, codProcedimiento, ejercicio,
                    numExpediente, codTram, ocuTram, codDoc,
                    params, datos);
            
            // *** 1. Crear la request e insertar firmantes
            idPeticion = createRequest(paramBase, datos);

            // *** 2. Insertar Documento
            String idDocumento = insertDocumento(idPeticion, paramBase, datos);
            
            // *** 3. Enviar la peticion
            idPeticion = sendRequest(idPeticion, paramBase, datos);

            // *** 4. Construir idPeticion: idRequest$idDocumento
            // Para poder almacenar los valores de id de peticion e id de documento,
            // se retorna la cadena unida por $
            idPeticion = String.format("%s%s%s",
                    idPeticion, ConstantesPortafirmasSanse.SANSE_ID_REQUEST_SEPARADOR_ID_DOCUMENTO, idDocumento);
        } catch (Exception ex) {
            log.error(String.format("Se ha producido un error al intentar enviar el documento al portafirmas externo: %s", ex.getMessage()));

            // Si hubo algun error, pero se ha creado la peticion en el portafirmas,
            // es necesario borrarla
            if (!StringUtils.equals(ConstantesPortafirmasSanse.ID_PETICION_NULA, idPeticion)) {
                log.info(String.format("Se intentara eliminar la peticion [%s] creada con errores...", idPeticion));
                
                if (!deleteRequest(codOrganizacion, idPeticion)) {
                    log.error(String.format("No se ha podido eliminar la peticion creada: %s", ex.getMessage()));
                }
                
                idPeticion = ConstantesPortafirmasSanse.ID_PETICION_NULA;
            }
        }

        log.debug("enviarDocumentoTramitacionPortafirmas() : END");
        return idPeticion;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String actualizarFirmasTramitacionExpediente(String codOrganizacion, String numExpediente, String[] params) {
        return actualizarFirmasTramitacionExpediente(codOrganizacion, numExpediente, null, params);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String actualizarFirmasTramitacionExpediente(
            String codOrganizacion, String numExpediente, String codUsuario, String[] params) {
        
        log.debug("actualizarFirmasTramitacionExpediente() : BEGIN");

        String resultado = "0";
        
        try {
           ArrayList<TramitacionExpedientesValueObject> listaTramites = new ArrayList<TramitacionExpedientesValueObject>();
           listaTramites =  pluginCommonManager.
                   getTramitesPendientes(codOrganizacion, numExpediente, params);
           
           log.debug("Recorremos los tramites pendientes para actualizar la firma");
           
           for (TramitacionExpedientesValueObject tramitePendiente : listaTramites) {
               tramitePendiente.setCodUsuario(codUsuario);
               actualizarFirmasDocumentosTramitacion(tramitePendiente, params);
           }
        } catch(Exception ex){
            log.error(String.format(
                    "Se ha producido un error actualizando el estado de los documentos de tramitacion pendientes de firma del portafirmas externo: %s", ex.getMessage()));
            resultado = "1";
        }
        
        log.debug("actualizarFirmasTramitacionExpediente() : END");
        
        return resultado;
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public String actualizarFirmasDocumentosTramitacion(TramitacionExpedientesValueObject tEVO, String[] params) {
        log.debug("actualizarFirmasDocumentosTramitacion() : BEGIN");
        
        String resultado = "0";
        
        try {
            Integer codOrganizacion = NumberUtils.createInteger(tEVO.getCodMunicipio());
            String codProcedimiento = tEVO.getCodProcedimiento();
            Integer ejercicio = NumberUtils.createInteger(tEVO.getEjercicio());
            String numExpediente = tEVO.getNumeroExpediente();
            Integer codTramite = NumberUtils.createInteger(tEVO.getCodTramite());
            Integer ocurrenciaTramite = NumberUtils.createInteger(tEVO.getOcurrenciaTramite());
            Integer codUsuario = NumberUtils.createInteger(tEVO.getCodUsuario());

            log.debug("Recuperamos los documentos pendientes de firma para el expediente, tramite y ocurrencia actual");
            
            ArrayList<FirmaDocumentoTramitacionVO> doc = pluginCommonManager.
                    getDocumentosTramitacionFirmaPendienteFlujoUsuario(
                            codOrganizacion, codProcedimiento, ejercicio, numExpediente,
                            codTramite, ocurrenciaTramite, params);

            if (doc != null && doc.size() > 0) {
                if (log.isDebugEnabled()) {
                    log.debug("Recorremos los documentos pendientes de firma recuperados");
                }
                
                String resultadoActualizarFirmas = comprobarYActualizarFirmasDocumentosTramitacion(
                        doc, codOrganizacion, codUsuario, params);
                resultado = resultadoActualizarFirmas;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error actualizando el estado de los documentos de tramitacion pendientes de firma del "
                    + "portafirmas externo " + ex.getMessage());
            resultado = "1";
        }
        
        log.debug("actualizarFirmasDocumentosTramitacion() : END");
        
        return resultado;
    }

    /**
     * Comprueba el estado de las firmas de los documentos de tramitacion en el
     * portafirmas de SANSE y actualiza el estado en Flexia
     * 
     * @param documentos
     * @param codOrganizacion
     * @param codUsuario
     * @param params
     * @return 
     */
    private String comprobarYActualizarFirmasDocumentosTramitacion(
            ArrayList<FirmaDocumentoTramitacionVO> documentos,
            Integer codOrganizacion, Integer codUsuario, String[] params) {

        log.debug("comprobarYActualizarFirmasDocumentosTramitacion() : BEGIN");
        
        String resultado = "0";
        
        try {
            FirmaDocumentoTramitacionVO docActualizar = null;
            String codigoOrganizacion = String.valueOf(codOrganizacion);

            log.debug("Recorremos el array de documentos de tramitacion");
            for (FirmaDocumentoTramitacionVO doc : documentos) {
                QueryRequestResponse respuesta = null;
                try {
                    respuesta = queryRequest(codigoOrganizacion, doc.getIdSolicitudPortafirmasExterno());
                } catch (Exception ex) {
                    log.error(String.format(
                            "Se ha producido un error al intentar consultar el estado en el servicio web externo: %s",
                            ex.getMessage()));
                    respuesta = null;
                }
                
                if (respuesta != null && respuesta.getRequest() != null) {
                    Request request = respuesta.getRequest();
                    
                    docActualizar = pluginSanseManager.obtenerDocumentoActualizarEstado(codigoOrganizacion, doc, request, params);
                 
                    if (docActualizar != null) {
                        String estadoPeticionFirma = docActualizar.getEstadoFirma();
                        // Si la peticion ha cambiado y es FIRMADA, es necesario descargar el documento y la firma
                        if (StringUtils.equals(estadoPeticionFirma, ConstantesPortafirmas.ESTADO_FIRMA_FIRMADA)) {
                            // Descargar documento firmado
                            DownloadSignResponse respuestaDocumento =
                                    downloadSign(codigoOrganizacion, doc.getIdDocumentoPortafirmasExterno());
                            byte[] contenidoDocumento = respuestaDocumento.getSignature().getContent();
                            docActualizar.setFichero(contenidoDocumento);
                        }
                        
                        // Actualizar los estados de las firmas en Flexia
                        pluginSanseManager.actualizarEstadosFirmasFlujosUsuarios(
                                codOrganizacion, codUsuario, docActualizar, params);
                    }
                }
            }
        } catch (Exception ex) {
            log.error(String.format(
                    "Se ha producido un error actualizando el estado de los documentos de tramitacion pendientes de firma del portafirmas externo: %s",
                    ex.getMessage()));
            resultado = "1";
        }

        log.debug("comprobarYActualizarFirmasDocumentosTramitacion() : END");
        
        return resultado;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Boolean comprobarUsuarioPortafirmas(String codOrganizacion, String documento, String[] params) {
        Boolean existeUsuario = Boolean.FALSE;

        log.debug("comprobarUsuarioPortafirmas() : BEGIN");

        try {
            QueryUsersParams paramCliente = new QueryUsersParams();
            SanseUtils.rellenarBaseParams(codOrganizacion, paramCliente);
            paramCliente.setIdentificadorUsuario(documento);

            QueryUsersResponse respuesta = clienteQuery.queryUsers(paramCliente);
            if (respuesta != null && respuesta.getUserList() != null) {
                if (respuesta.getUserList().length > 0) {
                    existeUsuario = Boolean.TRUE;
                }
            }
        } catch (Exception ex) {
            log.error(String.format("Se ha producido un error al comprobar el usuario en el portafirmas externo: %s", ex.getMessage()));
            existeUsuario = null;
        }

        log.debug("comprobarUsuarioPortafirmas() : END");

        return existeUsuario;
    }

    /**
     * Crea una peticion en el portafirmas de SANSE
     *
     * @param paramBase parametros base
     * @param datos datos
     * @return
     * @throws RemoteException
     * @throws ExceptionInfo
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws TechnicalException
     */
    private String createRequest(ModifyServiceBaseParams paramBase, GeneralValueObject datos)
            throws RemoteException, ExceptionInfo, MalformedURLException, ServiceException, TechnicalException {

        String requestId = null;

        CreateRequestParams paramCliente = new CreateRequestParams(paramBase);
        paramCliente.setIdRemitente((String) datos.getAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_ID_REMITENTE));
        paramCliente.setReferencia((String) datos.getAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_REFERENCIA));
        paramCliente.setAplicacion((String) datos.getAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_APLICACION));
        paramCliente.setSelloDeTiempo((Boolean) datos.getAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_SELLO_TIEMPO));
        paramCliente.setFechaInicio((Calendar) datos.getAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_FECHA_INICIO));
        paramCliente.setFechaExpiracion((Calendar) datos.getAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_FECHA_EXPIRACION));
        paramCliente.setAsunto((String) datos.getAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_ASUNTO));
        paramCliente.setTexto((String) datos.getAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_TEXTO));
        paramCliente.setNivelImportanciaId((String) datos.getAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_NIVEL_IMPORTANCIA_ID));
        paramCliente.setNivelImportanciaDescripcion((String) datos.getAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_NIVEL_IMPORTANCIA_DESCRIPCION));
        paramCliente.setTipoFirma((String) datos.getAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_TIPO_FIRMA));
        paramCliente.setListaFirmantes((List<FirmaCircuitoVO>) datos.getAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_FIRMANTES));
        
        CreateRequestResponse respuesta = clienteModify.createRequest(paramCliente);
        if (respuesta != null && respuesta.getRequestId() != null) {
            requestId = respuesta.getRequestId();
        } else {
            throw new TechnicalException("No se pudo crear la peticion");
        }

        return requestId;
    }

    /**
     * Inserta un documento en la peticion asociada al portafirmas de SANSE
     *
     * @param idPeticion el id de la peticion
     * @param paramBase parametros base
     * @param datos datos
     * @return
     * @throws RemoteException
     * @throws ExceptionInfo
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws TechnicalException
     */
    private String insertDocumento(String idPeticion, ModifyServiceBaseParams paramBase, GeneralValueObject datos)
            throws RemoteException, ExceptionInfo, MalformedURLException, ServiceException, TechnicalException {

        String documentId = null;

        String tipoDocumentoId = (String) datos.getAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_TIPO_DOCUMENTO_ID);
        String tipoDocumentoDesc = (String) datos.getAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_TIPO_DOCUMENTO_DESCRIPCION);

        DocumentoTramitacionVO documentoTramite = (DocumentoTramitacionVO) datos.getAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_DOCUMENTO_TRAMITE);
        byte[] ficheroBase64 = documentoTramite.getFichero();
        
        InsertDocumentParams paramCliente = new InsertDocumentParams(paramBase);
        paramCliente.setIdRequest(idPeticion);
        paramCliente.setNombreDocumento(documentoTramite.getDescripcion());
        paramCliente.setTipoMime(MimeTypes.PDF[0]); // Actualmente siempre se envía PDF
        paramCliente.setTipoDocumentoId(tipoDocumentoId);
        paramCliente.setTipoDocumentoDescripcion(tipoDocumentoDesc);
        paramCliente.setContenido(ficheroBase64);

        InsertDocumentResponse respuesta = clienteModify.insertDocument(paramCliente);
        if (respuesta != null && respuesta.getDocumentId() != null) {
            documentId = respuesta.getDocumentId();
        } else {
            throw new TechnicalException("No se pudo insertar correctamente el documento");
        }

        return documentId;
    }

    /**
     * Inserta los firmantes en la peticion asociada al portafirmas de SANSE
     *
     * @param idPeticion el id de la peticion
     * @param paramBase parametros base
     * @param datos datos
     * @return
     * @throws RemoteException
     * @throws ExceptionInfo
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws TechnicalException
     */
    private String insertSigners(String idPeticion, ModifyServiceBaseParams paramBase, GeneralValueObject datos)
            throws RemoteException, ExceptionInfo, MalformedURLException, ServiceException, TechnicalException {

        String requestId = null;

        List<FirmaCircuitoVO> listaFirmantes = (List<FirmaCircuitoVO>) datos.getAtributo(ConstantesPortafirmasSanse.SANSE_PARAM_FIRMANTES);

        InsertSignersParams paramCliente = new InsertSignersParams(paramBase);
        paramCliente.setIdRequest(idPeticion);
        
        FirmaCircuitoVO firmante = null;
        List<String> documentoFirmantes = new ArrayList<String>();
        for (int i = 0; i < listaFirmantes.size(); i++) {
            documentoFirmantes.clear();
            firmante = listaFirmantes.get(i);

            documentoFirmantes.add(firmante.getDocumentoUsuario());
            paramCliente.setLineaFirma(i);
            paramCliente.setListaIdFirmantes(documentoFirmantes);

            InsertSignersResponse respuesta = clienteModify.insertSigners(paramCliente);
            if (respuesta != null && StringUtils.equals(respuesta.getRequestId(), idPeticion)) {
                requestId = respuesta.getRequestId();
            } else {
                throw new TechnicalException("No se pudieron insertar correctamente los firmantes");
            }            
        }

        return requestId;
    }

    /**
     * Envia la peticion al portafirmas de SANSE
     *
     * @param idPeticion el id de la peticion
     * @param paramBase parametros base
     * @return
     * @throws RemoteException
     * @throws ExceptionInfo
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws TechnicalException
     */
    private String sendRequest(String idPeticion, ModifyServiceBaseParams paramBase, GeneralValueObject datos)
            throws RemoteException, ExceptionInfo, MalformedURLException, ServiceException, TechnicalException {

        String requestId = null;

        SendRequestParams paramCliente = new SendRequestParams(paramBase);
        paramCliente.setIdRequest(idPeticion);

        SendRequestResponse respuesta = clienteModify.sendRequest(paramCliente);
        if (respuesta != null && StringUtils.equals(respuesta.getRequestId(), idPeticion)) {
            requestId = respuesta.getRequestId();
        } else {
            throw new TechnicalException("No se ha enviado correctamente la peticion");
        }

        return requestId;
    }

    /**
     * Borra una peticion en el portafirmas de SANSE
     * 
     * @param codOrganizacion
     * @param idPeticion
     * @return
     */
    private Boolean deleteRequest(String codOrganizacion, String idPeticion) {
        Boolean borrado = false;

        log.debug("deleteRequest() : BEGIN");

        try {
            DeleteRequestParams paramCliente = new DeleteRequestParams();
            SanseUtils.rellenarBaseParams(codOrganizacion, paramCliente);
            paramCliente.setIdRequest(idPeticion);

            DeleteRequestResponse respuesta = clienteModify.deleteRequest(paramCliente);
            if (respuesta != null && StringUtils.equals(idPeticion, respuesta.getRequestId())) {
                borrado = true;
            }
        } catch (Exception ex) {
            log.error(String.format("Se ha producido un error al borrar la peticion del portafirmas externo: %s", ex.getMessage()));
            borrado = false;
        }

        log.debug("deleteRequest() : END");

        return borrado;
    }

    /**
     * Obtiene el estado de una peticion de firma
     * 
     * @param codOrganizacion codigo de la organizacino
     * @param idPeticion id de la peticion en SANSE
     * @return 
     * @throws RemoteException
     * @throws ExceptionInfo
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws TechnicalException 
     */
    private QueryRequestResponse queryRequest(String codOrganizacion, String idPeticion)
            throws RemoteException, ExceptionInfo, MalformedURLException, ServiceException, TechnicalException {
        QueryRequestResponse respuesta = null;

        log.debug("queryRequest() : BEGIN");

        try {
            QueryRequestParams paramCliente = new QueryRequestParams();
            SanseUtils.rellenarBaseParams(codOrganizacion, paramCliente);
            paramCliente.setIdRequest(idPeticion);

            respuesta = clienteQuery.queryRequest(paramCliente);
            if (respuesta == null || respuesta.getRequest() == null
                    || !StringUtils.equals(idPeticion, respuesta.getRequest().getIdentifier())) {

                respuesta = null;
                throw new TechnicalException("No se ha enviado correctamente la peticion");
            }
        } catch (Exception ex) {
            throw new TechnicalException("Se ha producido un error al obtener el estado de la firma del portafirmas externo", ex);
        }

        log.debug("queryRequest() : END");

        return respuesta;
    }
    
     /**
     * Obtiene la firma del documento
     * 
     * @param codOrganizacion codigo de la organizacion
     * @param idDocumento id del documento en SANSE
     * @return 
     * @throws RemoteException
     * @throws ExceptionInfo
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws TechnicalException 
     */
    private DownloadSignResponse downloadSign(String codOrganizacion, String idDocumento)
            throws RemoteException, ExceptionInfo, MalformedURLException, ServiceException, TechnicalException {
        DownloadSignResponse respuesta = null;

        log.debug("downloadSign() : BEGIN");

        try {
            DownloadSignParams paramCliente = new DownloadSignParams();
            SanseUtils.rellenarBaseParams(codOrganizacion, paramCliente);
            paramCliente.setIdDocumento(idDocumento);

            respuesta = clienteQuery.downloadSign(paramCliente);
            if (respuesta == null || respuesta.getSignature() == null) {
                throw new TechnicalException("No se ha recibido correctamente la respuesta");
            }
        } catch (Exception ex) {
            throw new TechnicalException("Se ha producido un error al descargar la firma del portafirmas externo", ex);
        }

        log.debug("downloadSign() : END");

        return respuesta;
    }
    
    /**
     * Obtiene el coontenido del documento
     * 
     * @param codOrganizacion codigo de la organizacion
     * @param idDocumento id del documento en SANSE
     * @return 
     * @throws RemoteException
     * @throws ExceptionInfo
     * @throws MalformedURLException
     * @throws ServiceException
     * @throws TechnicalException 
     */
    private DownloadDocumentResponse downloadDocument(String codOrganizacion, String idDocumento)
            throws RemoteException, ExceptionInfo, MalformedURLException, ServiceException, TechnicalException {
        DownloadDocumentResponse respuesta = null;

        log.debug("downloadDocument() : BEGIN");

        try {
            DownloadDocumentParams paramCliente = new DownloadDocumentParams();
            SanseUtils.rellenarBaseParams(codOrganizacion, paramCliente);
            paramCliente.setIdDocumento(idDocumento);

            respuesta = clienteQuery.downloadDocument(paramCliente);
            if (respuesta == null || respuesta.getDocumentBinary() == null) {
                throw new TechnicalException("No se ha recibido correctamente la respuesta");
            }
        } catch (Exception ex) {
            throw new TechnicalException("Se ha producido un error al descargar la firma del portafirmas externo", ex);
        }

        log.debug("downloadDocument() : END");

        return respuesta;
    }
    
}
