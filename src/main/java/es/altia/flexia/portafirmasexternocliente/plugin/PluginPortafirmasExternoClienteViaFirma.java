package es.altia.flexia.portafirmasexternocliente.plugin;

import com.viafirma.tray.ws.dto.*;
import com.viafirma.tray.ws.server.ServiceWS;
import es.altia.agora.business.editor.mantenimiento.persistence.manual.DocumentosAplicacionDAO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.portafirmasexternocliente.factoria.PluginPortafirmasExternoClienteFactoria;
import es.altia.flexia.portafirmasexternocliente.plugin.persistance.PluginPortafirmasExternoClienteManager;
import es.altia.flexia.portafirmasexternocliente.plugin.viafirma.ViaFirmaInboxWSClient;
import es.altia.flexia.portafirmasexternocliente.util.ConstantesPortafirmasExternoCliente;
import es.altia.flexia.portafirmasexternocliente.vo.DocumentoTramitacionVO;
import es.altia.flexia.portafirmasexternocliente.vo.InfoTramiteVO;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * @author david.caamano
 * @version 04/03/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 04/03/2013 * #53275 Edición inicial</li>
 * </ol> 
 */
public class PluginPortafirmasExternoClienteViaFirma implements PluginPortafirmasExternoCliente {

    //Logger
    private static Logger log = Logger.getLogger(PluginPortafirmasExternoClienteViaFirma.class);
    
    //Fichero de propiedades comunes
    private static ResourceBundle bundleCommon = ResourceBundle.getBundle("common");
    
    //Fichero de propiedades de texto
    private static ResourceBundle applicationPropertiesBundle = ResourceBundle.getBundle("ApplicationResources");
    
    //Constantes para la sustitucion de propiedades en el asunto y mensaje enviados en la solicitud
    private static final String NUM_EXPEDIENTE = "%EXPEDIENTE%";
    private static final String DOCUMENTO = "%DOCUMENTO%";
    private static final String TRAMITE = "%TRAMITE%";
    private static final String FECHA_INICIO = "%FECHAINICIO%";
    
    /**
     * Envia al portafirmas de viafirma el documento indicado mediante los parametros del metodo
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
     * Envia al portafirmas de viafirma el documento indicado mediante los parametros del metodo
     * 
     * @param codProcedimiento
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param codDocumento
     * @return String
     */
    public String enviarDocumentoTramitacionPortafirmas(String codOrganizacion, String codProcedimiento, String numExpediente, String codTramite, 
            String ocurrenciaTramite, String codDocumento, int idUsuario, byte[] contenido, String[] params, String portafirmas) {
        if(log.isDebugEnabled()) log.debug("enviarDocumentoTramitacionPortafirmas() : BEGIN");
        String resultado = new String();
        try{
            String resultadoEnvioPortafirmas = new String();
            if(log.isDebugEnabled()) log.debug("Recuperamos el documento de tramitacion");
            DocumentoTramitacionVO documento = PluginPortafirmasExternoClienteManager.getInstance().
                    getDocumentoTramitacion(codOrganizacion, codProcedimiento, numExpediente, codTramite, ocurrenciaTramite, codDocumento, params, portafirmas);
            
            resultadoEnvioPortafirmas = enviarDocumentoTramitacionAFirma(documento, params);
            if(resultadoEnvioPortafirmas != null && !"".equalsIgnoreCase(resultadoEnvioPortafirmas)){
                if(log.isDebugEnabled()) log.debug("Recuperamos el codigo devuelvo por el portafirmas externo = " + resultadoEnvioPortafirmas);
                resultado = resultadoEnvioPortafirmas;
            }//if(resultadoEnvioPortafirmas != null && !"".equalsIgnoreCase(resultadoEnvioPortafirmas))
        }catch(Exception ex){
            log.error("Se ha producido un error enviando el doumento de tramitacion al portafirmas " + ex.getMessage());
            resultado = PluginPortafirmasExternoCliente.OPERACION_ERROR;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("enviarDocumentoTramitacionPortafirmas() : END");
        return resultado;
    }//enviarDocumentoPortafirmas
    
    /**
     * Metodo que se comunica con el servicio web del portafirmas externo para enviarle el documento
     * @param doc
     * @return 
     */
    private String enviarDocumentoTramitacionAFirma(DocumentoTramitacionVO doc, String[] params){
        if(log.isDebugEnabled()) log.debug("enviarDocumentoTramitacionAFirma() : BEGIN");
        String respuesta = new String();
        try{
            ViaFirmaInboxWSClient cliente = ViaFirmaInboxWSClient.getInstance(doc.getCodMunicipio());
            ServiceWS servicio = cliente.getService();
            
            RequestDTO sendRequest = new RequestDTO();
            RequestUserDTO destinatario = new RequestUserDTO();
            DocumentDTO documento = new DocumentDTO();
            DocumentDTO anexo = new DocumentDTO();
            
            MetadataDTO metadato = new MetadataDTO();
                
            destinatario.setPersonId(doc.getNifUsuarioFirmante());
            
            sendRequest.setAccessType("ANONIMO");
            sendRequest.setBoolNotifyIfRead(false);
            sendRequest.setBoolNotifyIfReturned(false);
            sendRequest.setBoolNotifyIfSigned(false);
            
            if(log.isDebugEnabled()) log.debug("Recuperamos las propiedades para el subject y el message del fichero de resources");
            String subject = new String();
            try{
                if(log.isDebugEnabled()) log.debug("Recuperamos las propiedades para el subject");
                String sbj = applicationPropertiesBundle.getString("portafirmasExterno.asuntoSolicitud");
                    sbj = sbj.replaceAll(NUM_EXPEDIENTE, doc.getNumExpediente());
                    subject = sbj;
            }catch(Exception ex){
                log.error("Se ha producido un error recuperando la propiedad para el subject de la solicitud al portafirmas externo " 
                        + ex.getMessage());
            }//try-catch
            
            String message = new String();
            try{
                if(log.isDebugEnabled()) log.debug("Recuperamos las propiedades para el message");
                String msg = applicationPropertiesBundle.getString("portafirmasExterno.mensajeSolicitud");
                if(log.isDebugEnabled()) log.debug("Recuperamos la informacion del tramite");
                InfoTramiteVO infoTramite = PluginPortafirmasExternoClienteManager.getInstance().getInfoTramite(doc.getCodMunicipio(), 
                        doc.getNumExpediente(), doc.getCodTramite(), doc.getCodOcurrencia(), params);
                    msg = msg.replaceAll(DOCUMENTO, doc.getDescripcion());
                    msg = msg.replaceAll(TRAMITE, infoTramite.getDescripcionTramite());
                    msg = msg.replaceAll(FECHA_INICIO, infoTramite.getFechaInicioTramite());
                    msg = msg.replaceAll(NUM_EXPEDIENTE, doc.getNumExpediente());
                    message = msg;
            }catch(Exception ex){
                log.error("Se ha producido un error recuperando la propiedad para el message de la solicitud al portafirmas externo " 
                        + ex.getMessage());
            }//try-catch
            
            if(log.isDebugEnabled()) log.debug("Rellenamos el subject y el message de la solicitud");
            sendRequest.setSubject(subject);
            sendRequest.setMessage(message);
            
            String editorTexto = "";
            if(log.isDebugEnabled()) log.debug("Determinamos si el documento de tramitacion es de open office o microsoft office");
            try{
                    editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTexto(Integer.parseInt(doc.getCodMunicipio()),
                    doc.getNumExpediente(),Integer.parseInt(doc.getCodTramite()), Integer.parseInt(doc.getCodOcurrencia()), 
                    Integer.parseInt(doc.getCodDocumento()), false, params);
            }catch (NumberFormatException e)
            {
                log.error("No se puede obtener el editor de texto definido debido a una excepcion");
            }
            if ("OOFFICE".equals(editorTexto) || "ODT".equals(editorTexto)){
                if(log.isDebugEnabled()) log.debug("El documento es open office");
                documento.setFileMimeType(ConstantesDatos.TIPO_MIME_DOCUMENTO_OPENOFFICE);
                documento.setFileExtension(ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE);
                documento.setFileName(doc.getDescripcion() + "." + ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE);
            }else{
                if(log.isDebugEnabled()) log.debug("El documento es microsoft office");
                documento.setFileMimeType(ConstantesDatos.TIPO_MIME_DOC_TRAMITES);
                documento.setFileExtension(ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_WORD);
                documento.setFileName(doc.getDescripcion() + "." + ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_WORD);
            }//if ("OOFFICE".equals(editorTexto)){
            
            if(log.isDebugEnabled()) log.debug("Introducimos el contenido del fichero");
            documento.setData(doc.getFichero());
            if(log.isDebugEnabled()) log.debug("Generamos un hash en base 64 del contenido del fichero");
            //documento.setFileHash(Base64.encode(doc.getFichero(), doc.getFichero().length, true));
            
            anexo.setFileName(doc.getDescripcion());
            anexo.setData(doc.getFichero()); 
            
            RequestUserDTO[][][] destinatarios = {{{destinatario}}};
            DocumentDTO[] documentos = {documento};
            DocumentDTO[] anexos = {};
            MetadataDTO[] reqMetadatos = {metadato};
            
            if(log.isDebugEnabled()) log.debug("Realizamos la llamada para enviar el documento a firmar");
            SendRequestResponseDTO respuestaSendRequest = 
                    servicio.sendRequest(sendRequest, destinatarios, documentos, anexos, reqMetadatos);
            
            if(log.isDebugEnabled()) log.debug("Respuesta = " + respuestaSendRequest.getMessage());
            if(respuestaSendRequest.getError() == false){
                if(log.isDebugEnabled()) log.debug("El envio realizado al portafirmas externo ha sido correcto");
                respuesta = respuestaSendRequest.getResult().getPublicAccessId();
            }//if(respuestaSendRequest.getError() == false)
            
        }catch(Exception ex){
            log.error("Se ha producido un error llamando al webservice para firmar un documento de tramitacion  " + ex.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("enviarDocumentoTramitacionAFirma() : END");
        return respuesta;
    }//enviarDocumentoTramitacionAFirma
    
    /**
     * Actualiza los documentos de los tramites pendientes del expediente
     * 
     * @param codOrganizacion
     * @param numExpediente
     * @param params
     * @return String
     */
    public String actualizarFirmasTramitacionExpediente(String codOrganizacion, String numExpediente, String[] params){
        if(log.isDebugEnabled()) log.debug("actualizarFirmasTramitacionExpediente() : BEGIN");
        String resultado = new String("0");
        try{
           ArrayList<TramitacionExpedientesValueObject> listaTramites = new ArrayList<TramitacionExpedientesValueObject>();
           listaTramites =  PluginPortafirmasExternoClienteManager.getInstance().
                   getTramitesPendientes(codOrganizacion, numExpediente, params);
           if(log.isDebugEnabled()) log.debug("Recorremos los tramites pendientes");
           for(TramitacionExpedientesValueObject tramitePendiente : listaTramites){
               actualizarFirmasDocumentosTramitacion(tramitePendiente, params);
           }//for(TramitacionExpedientesValueObject tramitePendiente : listaTramites)
        }catch(Exception ex){
            log.error("Se ha producido un error actualizando el estado de los documentos de tramitacion pendientes de firma del "
                    + "portafirmas externo " + ex.getMessage());
            resultado = "1";
        }//try-catch
        if(log.isDebugEnabled()) log.debug("actualizarFirmasTramitacionExpediente() : END");
        return resultado;
    }//actualizarFirmasTramitacionExpediente

    /**
     * @inheritDoc
     */
    @Override
    public String actualizarFirmasTramitacionExpediente(String codOrganizacion, String numExpediente, String codUsuario, String[] params) {
        return actualizarFirmasTramitacionExpediente(codOrganizacion, numExpediente, params);
    }
    
    /**
     * Metodo que recupera todos los documentos de tramitacion enviados al portafirmas para la ocurrencia de un tramite de un expediente
     * de este cliente VIAFIRMA y comprueba si estan firmados y actualiza la informacion de la BBDD si es asi.
     * 
     * @param tEVO
     * @param params
     * @return 
     */
    public String actualizarFirmasDocumentosTramitacion(TramitacionExpedientesValueObject tEVO, String[] params) {
        if(log.isDebugEnabled()) log.debug("actualizarFirmasDocumentosTramitacion() : BEGIN");
        String resultado = new String("0");
        try{
            String codOrganizacion = tEVO.getCodMunicipio();
            String codProcedimiento = tEVO.getCodProcedimiento();
            String numExpediente = tEVO.getNumeroExpediente();
            String codTramite = tEVO.getCodTramite();
            String ocurrenciaTramite = tEVO.getOcurrenciaTramite();

            if(log.isDebugEnabled()) log.debug("Recuperamos los documentos pendientes de firma para el expediente, tramite y ocurrencia actual");
            ArrayList<DocumentoTramitacionVO> doc = PluginPortafirmasExternoClienteManager.getInstance().
                getDocumentosTramitacionFirmaPendiente(codOrganizacion, codProcedimiento, numExpediente, codTramite, ocurrenciaTramite, params);
            
            if(doc != null && doc.size() > 0){
                if(log.isDebugEnabled()) log.debug("Recorremos los documentos pendientes de firma recuperados");
                String resultadoActualizarFirmas = comprobarYActualizarFirmasDocumentosTramitacion(doc, codOrganizacion, params);
                resultado = resultadoActualizarFirmas;
            }//if(doc != null && doc.size() > 0)
            
        }catch(Exception ex){
            log.error("Se ha producido un error actualizando el estado de los documentos de tramitacion pendientes de firma del "
                    + "portafirmas externo " + ex.getMessage());
            resultado = "1";
        }//try-catch
        if(log.isDebugEnabled()) log.debug("actualizarFirmasDocumentosTramitacion() : END");
        return resultado;
    }//actualizarFirmasDocumentosTramitacion
    
    private String comprobarYActualizarFirmasDocumentosTramitacion(ArrayList<DocumentoTramitacionVO> documentos, String codOrganizacion,
            String[] params){
        if(log.isDebugEnabled()) log.debug("comprobarYActualizarFirmasDocumentosTramitacion() : BEGIN");
        String resultado = new String("0");
        try{
            //Recuperamos las propiedades con los codigos de firmado y rechazado que devuelve el portafirmas externo
            if(log.isDebugEnabled()) log.debug("Recuperamos el cliente del portafirmas externo");
            String codCliente = PluginPortafirmasExternoClienteFactoria.getCodClientePortafirmasExterno(codOrganizacion);
            if(log.isDebugEnabled()) log.debug("Cod cliente portafirmas externo = " + codCliente);
            ResourceBundle ficheroPropiedades = ResourceBundle.getBundle(ConstantesPortafirmasExternoCliente.FICHERO_PROPERTIES);
            String codFirmado = ficheroPropiedades.getString(ConstantesPortafirmasExternoCliente.PLUGIN_PORTAFIRMAS_EXTERNO_CLIENTE 
                    + ConstantesPortafirmasExternoCliente.BARRA
                    + codCliente
                    + ConstantesPortafirmasExternoCliente.BARRA
                    + ConstantesPortafirmasExternoCliente.CODIGO_FIRMADO);
            
            String codRechazado = ficheroPropiedades.getString(ConstantesPortafirmasExternoCliente.PLUGIN_PORTAFIRMAS_EXTERNO_CLIENTE 
                    + ConstantesPortafirmasExternoCliente.BARRA
                    + codCliente
                    + ConstantesPortafirmasExternoCliente.BARRA
                    + ConstantesPortafirmasExternoCliente.CODIGO_RECHAZADO);
            
            if(log.isDebugEnabled()) log.debug("Cargamos la clase del cliente de webservice del portafirmas externo");
            ViaFirmaInboxWSClient cliente = ViaFirmaInboxWSClient.getInstance(codOrganizacion);
            ServiceWS servicio = cliente.getService();
            
            if(log.isDebugEnabled()) log.debug("Recorremos el array de documentos de tramitacion");
            for(DocumentoTramitacionVO doc : documentos){
                RequestStatusResponseDTO respuesta = servicio.getRequestStatus(doc.getIdSolicitudPortafirmasExterno());
                if(!respuesta.getError()){
                    if(log.isDebugEnabled()) log.debug("Recuperamos el estado de la peticion");
                    RequestAddresseeDTO[] arrayRespuestas =  respuesta.getResult();
                    for(RequestAddresseeDTO resp : arrayRespuestas){
                        String estado = resp.getStatus();
                        if(estado != null && !"".equalsIgnoreCase(estado)){
                            if(codRechazado.equalsIgnoreCase(estado) || codFirmado.equalsIgnoreCase(estado)){
                               if(log.isDebugEnabled()) log.debug("Recuperamos el usuario firmante por su documento");
                               String nif = resp.getUser().getNif();
                               Integer codUsuario = PluginPortafirmasExternoClienteManager.getInstance().getCodUsuarioPorNif(nif, params);
                               if(codUsuario != null){
                                    PluginPortafirmasExternoClienteManager.getInstance().
                                            actualizaEstadoDocumento(doc, mapeoEstadoFirma(estado, codOrganizacion), codUsuario, params);
                               }//if(codUsuario != null)
                               if(log.isDebugEnabled()) log.debug("CodUsuario = " + codUsuario);
                            }//if(codRechazado.equalsIgnoreCase(estado) || codFirmado.equalsIgnoreCase(estado))
                        }//if(estado != null && !"".equalsIgnoreCase(estado))
                    }//for(RequestAddresseeDTO resp : arrayRespuestas)
                }//if(respuesta.getError() == false)
            }//for(DocumentoTramitacionVO doc : documentos)
        }catch(Exception ex){
            log.error("Se ha producido un error actualizando el estado de los documentos de tramitacion pendientes de firma del portafirmas externo " + ex.getMessage());
            resultado = "1";
        }//try-catch
        if(log.isDebugEnabled()) log.debug("comprobarYActualizarFirmasDocumentosTramitacion() : END");
        return resultado;
    }//private String comprobarYActualizarFirmasDocumentosTramitacion(ArrayList<DocumentoTramitacionVO> DocumentoTramitacionVO documentos)

    /**
     * Para los estados propios del portafirmas externo devuelve el equivalente para el codigo de flexia
     * 
     * @param estado
     * @return 
     */
    private String mapeoEstadoFirma(String estado, String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("mapeoEstadoFirma() : BEGIN");
        String codFlexia = new String();
        //Recuperamos las propiedades con los codigos de firmado y rechazado que devuelve el portafirmas externo
            if(log.isDebugEnabled()) log.debug("Recuperamos el cliente del portafirmas externo");
            String codCliente = PluginPortafirmasExternoClienteFactoria.getCodClientePortafirmasExterno(codOrganizacion);
            if(log.isDebugEnabled()) log.debug("Cod cliente portafirmas externo = " + codCliente);
            ResourceBundle ficheroPropiedades = ResourceBundle.getBundle(ConstantesPortafirmasExternoCliente.FICHERO_PROPERTIES);
            String codFirmado = ficheroPropiedades.getString(ConstantesPortafirmasExternoCliente.PLUGIN_PORTAFIRMAS_EXTERNO_CLIENTE 
                    + ConstantesPortafirmasExternoCliente.BARRA
                    + codCliente
                    + ConstantesPortafirmasExternoCliente.BARRA
                    + ConstantesPortafirmasExternoCliente.CODIGO_FIRMADO);
            
            String codRechazado = ficheroPropiedades.getString(ConstantesPortafirmasExternoCliente.PLUGIN_PORTAFIRMAS_EXTERNO_CLIENTE 
                    + ConstantesPortafirmasExternoCliente.BARRA
                    + codCliente
                    + ConstantesPortafirmasExternoCliente.BARRA
                    + ConstantesPortafirmasExternoCliente.CODIGO_RECHAZADO);
            
            if(estado.equalsIgnoreCase(codFirmado)){
                if(log.isDebugEnabled()) log.debug("Codigo firmado");
                codFlexia = "F";
            }else if(estado.equalsIgnoreCase(codRechazado)){
                if(log.isDebugEnabled()) log.debug("Codigo rechazado");
                codFlexia = "R";
            }//if(estado.equalsIgnoreCase(codFirmado))
        if(log.isDebugEnabled()) log.debug("mapeoEstadoFirma() : END");
        return codFlexia;
    }////mapeoEstadoFirma

    public Boolean comprobarUsuarioPortafirmas(String codOrganizacion, String documento, String[] params) {
        //Se devuelve siempre true, porque no se comprueba
        return Boolean.TRUE;
    }
    
}//class
