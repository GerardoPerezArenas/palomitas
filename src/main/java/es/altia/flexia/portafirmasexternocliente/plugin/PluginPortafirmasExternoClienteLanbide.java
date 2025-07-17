package es.altia.flexia.portafirmasexternocliente.plugin;

import com.viafirma.tray.ws.dto.RequestAddresseeDTO;
import com.viafirma.tray.ws.dto.RequestStatusResponseDTO;
import com.viafirma.tray.ws.server.ServiceWS;
import es.altia.agora.business.documentos.DocumentosLanbideManager;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.flexia.portafirmasexternocliente.factoria.PluginPortafirmasExternoClienteFactoria;
import es.altia.flexia.portafirmasexternocliente.plugin.persistance.PluginPortafirmasExternoClienteManager;
import es.altia.flexia.portafirmasexternocliente.plugin.viafirma.ViaFirmaInboxWSClient;
import es.altia.flexia.portafirmasexternocliente.util.ConstantesPortafirmasExternoCliente;
import es.altia.flexia.portafirmasexternocliente.vo.DocumentoTramitacionVO;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import es.altia.flexia.portafirmasexternocliente.plugin.service.PluginPortafirmasExternoService;
import es.altia.util.commons.MimeTypes;
import org.apache.commons.io.FilenameUtils;

public class PluginPortafirmasExternoClienteLanbide implements PluginPortafirmasExternoCliente {

    //Logger
    private static final Logger LOG = Logger.getLogger(PluginPortafirmasExternoClienteLanbide.class);

    
    /**
     * Envia al portafirmas de viafirma el documento indicado mediante los parametros del metodo
     * 
     * @param codProcedimiento
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param codDocumento
     * @param portafirmas
     * @return String
     */
    @Override
    public String enviarDocumentoTramitacionPortafirmas(String codOrganizacion, String codProcedimiento, String numExpediente, String codTramite, 
            String ocurrenciaTramite, String codDocumentoEnviar, String codDocumentoOriginal, int idUsuario, byte[] contenido, String[] params, String portafirmas) {
        if(LOG.isDebugEnabled()) LOG.debug("enviarDocumentoTramitacionPortafirmas() : BEGIN");
        String resultado = new String();
        String OIDDocumento = null;
        
        try{
            if(LOG.isDebugEnabled()) LOG.debug("Recuperamos el documento de tramitacion");
            DocumentoTramitacionVO documento = PluginPortafirmasExternoClienteManager.getInstance().
                    getDocumentoTramitacion(codOrganizacion, codProcedimiento, numExpediente, codTramite, ocurrenciaTramite, codDocumentoOriginal, params, portafirmas);
            
            //Se obtiene el OID del documento de la tabla MELANBIDE_DOKUSI_RELDOC_TRAMIT 
            OIDDocumento = DocumentosLanbideManager.getInstance().obtenerOIDDocumento(Integer.parseInt(documento.getCodMunicipio()), Integer.parseInt(documento.getEjercicio()),
                    documento.getCodProcedimiento(), documento.getNumExpediente(), Integer.parseInt(documento.getCodTramite()), Integer.parseInt(documento.getCodOcurrencia()),
                    codDocumentoEnviar, params);
            
            documento.setIdDocumentoEnGestor(OIDDocumento);
            //En este punto el codDocumento corresponde al parametro codDocumentoOriginal (el doc) pero idDocumentoEnGestor corresponde a codDocumentoEnviar (el pdf)
            
            //El nombre del documento recuperado corresponde al doc, lo modificamos por pdf (solo para visualizarlo bien en logs, en bbdd de datos el update se hace despues de la llamada)
            String nombreDocumentoPDF = String.format("%s.%s", FilenameUtils.removeExtension(documento.getDescripcion()), MimeTypes.FILEEXTENSION_PDF);
            documento.setDescripcion(nombreDocumentoPDF);
            
            String resultadoEnvioPortafirmas = enviarDocumentoTramitacionAFirma(documento,idUsuario, params);
            if(resultadoEnvioPortafirmas != null && !"".equalsIgnoreCase(resultadoEnvioPortafirmas)){
                if(LOG.isDebugEnabled()) LOG.debug("Recuperamos el codigo devuelvo por el portafirmas externo = " + resultadoEnvioPortafirmas);
                resultado = resultadoEnvioPortafirmas;
            }//if(resultadoEnvioPortafirmas != null && !"".equalsIgnoreCase(resultadoEnvioPortafirmas))
        }catch(Exception ex){
            LOG.error("Se ha producido un error enviando el doumento de tramitacion al portafirmas " + ex.getMessage());
            ex.printStackTrace();
            resultado = PluginPortafirmasExternoCliente.OPERACION_ERROR;
        }//try-catch
        if(LOG.isDebugEnabled()) LOG.debug("enviarDocumentoTramitacionPortafirmas() : END");
        return resultado;
    }//enviarDocumentoPortafirmas
    
    /**
     * Metodo que se comunica con el servicio web del portafirmas externo para enviarle el documento
     * @param doc
     * @return 
     */
    private String enviarDocumentoTramitacionAFirma(DocumentoTramitacionVO doc, int idUsuario, String[] params) throws Exception{
        if(LOG.isDebugEnabled()) LOG.debug("enviarDocumentoTramitacionAFirma() : BEGIN");
        String respuesta = new String();
        
        //Primero se busca el servicio especifico de Lanbide que se piensa utilizar 
        try {
            if(LOG.isDebugEnabled()) LOG.debug("Buscamos el servicio especifico de Lanbide");
             PluginPortafirmasExternoService servicioLanbide = PluginPortafirmasExternoClienteFactoria.getPortafirmaExternoServImplClass();
             
            if(servicioLanbide != null){
                //Si todo ha ocurrido correctamente devuelve los datos necesatios concatenados con $
                // 1 OID Documento, 2 Extension Documento, 3 Buzon, 4 Buzon Firma
                respuesta = servicioLanbide.enviarDocumentoTramitacionAFirma(doc, idUsuario, params);
                if(LOG.isDebugEnabled()) LOG.debug("ha finalizado la llamada al servicio de envio al portafirmas");
            }
            
        } catch (Exception ex) {
            LOG.error("Se ha producido un error llamando al Servicio de Lan6PortaFirmasServicios " + ex.getMessage());
            ex.printStackTrace();
            throw new Exception (ex.getMessage());
       }
        
        if(LOG.isDebugEnabled()) LOG.debug("respuesta vale: " + respuesta);

        if(LOG.isDebugEnabled()) LOG.debug("enviarDocumentoTramitacionAFirma() : END");
        
        return respuesta;

    }//enviarDocumentoTramitacionAFirma

    @Override
    public String actualizarFirmasTramitacionExpediente(String codOrganizacion, String numExpediente, String[] params) {
        if(LOG.isDebugEnabled()) LOG.debug("actualizarFirmasTramitacionExpediente() : BEGIN");
        String resultado = "0";
        try{
           ArrayList<TramitacionExpedientesValueObject> listaTramites =  PluginPortafirmasExternoClienteManager.getInstance().
                   getTramitesPendientes(codOrganizacion, numExpediente, params);
           if(LOG.isDebugEnabled()) LOG.debug("Recorremos los tramites pendientes");
           for(TramitacionExpedientesValueObject tramitePendiente : listaTramites){
               actualizarFirmasDocumentosTramitacion(tramitePendiente, params);
           }//for(TramitacionExpedientesValueObject tramitePendiente : listaTramites)
        }catch(Exception ex){
            LOG.error("Se ha producido un error actualizando el estado de los documentos de tramitacion pendientes de firma del "
                    + "portafirmas externo " + ex.getMessage());
            resultado = "1";
        }//try-catch
        if(LOG.isDebugEnabled()) LOG.debug("actualizarFirmasTramitacionExpediente() : END");
        return resultado;
    }//actualizarFirmasTramitacionExpediente

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
    @Override
    public String actualizarFirmasDocumentosTramitacion(TramitacionExpedientesValueObject tEVO, String[] params) {
        if(LOG.isDebugEnabled()) LOG.debug("actualizarFirmasDocumentosTramitacion() : BEGIN");
        String resultado = "0";
        try{
            String codOrganizacion = tEVO.getCodMunicipio();
            String codProcedimiento = tEVO.getCodProcedimiento();
            String numExpediente = tEVO.getNumeroExpediente();
            String codTramite = tEVO.getCodTramite();
            String ocurrenciaTramite = tEVO.getOcurrenciaTramite();

            if(LOG.isDebugEnabled()) LOG.debug("Recuperamos los documentos pendientes de firma para el expediente, tramite y ocurrencia actual");
            ArrayList<DocumentoTramitacionVO> doc = PluginPortafirmasExternoClienteManager.getInstance().
                getDocumentosTramitacionFirmaPendiente(codOrganizacion, codProcedimiento, numExpediente, codTramite, ocurrenciaTramite, params);
            
            if(doc != null && doc.size() > 0){
                if(LOG.isDebugEnabled()) LOG.debug("Recorremos los documentos pendientes de firma recuperados");
                String resultadoActualizarFirmas = comprobarYActualizarFirmasDocumentosTramitacion(doc, codOrganizacion, params);
                resultado = resultadoActualizarFirmas;
            }//if(doc != null && doc.size() > 0)
            
        }catch(Exception ex){
            LOG.error("Se ha producido un error actualizando el estado de los documentos de tramitacion pendientes de firma del "
                    + "portafirmas externo " + ex.getMessage());
            resultado = "1";
        }//try-catch
        if(LOG.isDebugEnabled()) LOG.debug("actualizarFirmasDocumentosTramitacion() : END");
        return resultado;
    }//actualizarFirmasDocumentosTramitacion
    
    private String comprobarYActualizarFirmasDocumentosTramitacion(ArrayList<DocumentoTramitacionVO> documentos, String codOrganizacion,
            String[] params){
        if(LOG.isDebugEnabled()) LOG.debug("comprobarYActualizarFirmasDocumentosTramitacion() : BEGIN");
        String resultado = "0";
        try{
            //Recuperamos las propiedades con los codigos de firmado y rechazado que devuelve el portafirmas externo
            if(LOG.isDebugEnabled()) LOG.debug("Recuperamos el cliente del portafirmas externo");
            String codCliente = PluginPortafirmasExternoClienteFactoria.getCodClientePortafirmasExterno(codOrganizacion);
            if(LOG.isDebugEnabled()) LOG.debug("Cod cliente portafirmas externo = " + codCliente);
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
            
            if(LOG.isDebugEnabled()) LOG.debug("Cargamos la clase del cliente de webservice del portafirmas externo");
            ViaFirmaInboxWSClient cliente = ViaFirmaInboxWSClient.getInstance(codOrganizacion);
            ServiceWS servicio = cliente.getService();
            
            if(LOG.isDebugEnabled()) LOG.debug("Recorremos el array de documentos de tramitacion");
            for(DocumentoTramitacionVO doc : documentos){
                RequestStatusResponseDTO respuesta = servicio.getRequestStatus(doc.getIdSolicitudPortafirmasExterno());
                if(!respuesta.getError()){
                    if(LOG.isDebugEnabled()) LOG.debug("Recuperamos el estado de la peticion");
                    RequestAddresseeDTO[] arrayRespuestas =  respuesta.getResult();
                    for(RequestAddresseeDTO resp : arrayRespuestas){
                        String estado = resp.getStatus();
                        if(estado != null && !"".equalsIgnoreCase(estado)){
                            if(codRechazado.equalsIgnoreCase(estado) || codFirmado.equalsIgnoreCase(estado)){
                               if(LOG.isDebugEnabled()) LOG.debug("Recuperamos el usuario firmante por su documento");
                               String nif = resp.getUser().getNif();
                               Integer codUsuario = PluginPortafirmasExternoClienteManager.getInstance().getCodUsuarioPorNif(nif, params);
                               if(codUsuario != null){
                                    PluginPortafirmasExternoClienteManager.getInstance().
                                            actualizaEstadoDocumento(doc, mapeoEstadoFirma(estado, codOrganizacion), codUsuario, params);
                               }//if(codUsuario != null)
                               if(LOG.isDebugEnabled()) LOG.debug("CodUsuario = " + codUsuario);
                            }//if(codRechazado.equalsIgnoreCase(estado) || codFirmado.equalsIgnoreCase(estado))
                        }//if(estado != null && !"".equalsIgnoreCase(estado))
                    }//for(RequestAddresseeDTO resp : arrayRespuestas)
                }//if(respuesta.getError() == false)
            }//for(DocumentoTramitacionVO doc : documentos)
        }catch(Exception ex){
            LOG.error("Se ha producido un error actualizando el estado de los documentos de tramitacion pendientes de firma del portafirmas externo " + ex.getMessage());
            resultado = "1";
        }//try-catch
        if(LOG.isDebugEnabled()) LOG.debug("comprobarYActualizarFirmasDocumentosTramitacion() : END");
        return resultado;
    }//private String comprobarYActualizarFirmasDocumentosTramitacion(ArrayList<DocumentoTramitacionVO> DocumentoTramitacionVO documentos)
    
    /**
     * Para los estados propios del portafirmas externo devuelve el equivalente para el codigo de flexia
     * 
     * @param estado
     * @return 
     */
    private String mapeoEstadoFirma(String estado, String codOrganizacion){
        if(LOG.isDebugEnabled()) LOG.debug("mapeoEstadoFirma() : BEGIN");
        String codFlexia = new String();
        //Recuperamos las propiedades con los codigos de firmado y rechazado que devuelve el portafirmas externo
            if(LOG.isDebugEnabled()) LOG.debug("Recuperamos el cliente del portafirmas externo");
            String codCliente = PluginPortafirmasExternoClienteFactoria.getCodClientePortafirmasExterno(codOrganizacion);
            if(LOG.isDebugEnabled()) LOG.debug("Cod cliente portafirmas externo = " + codCliente);
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
                if(LOG.isDebugEnabled()) LOG.debug("Codigo firmado");
                codFlexia = "F";
            }else if(estado.equalsIgnoreCase(codRechazado)){
                if(LOG.isDebugEnabled()) LOG.debug("Codigo rechazado");
                codFlexia = "R";
            }//if(estado.equalsIgnoreCase(codFirmado))
        if(LOG.isDebugEnabled()) LOG.debug("mapeoEstadoFirma() : END");
        return codFlexia;
    }////mapeoEstadoFirma

    @Override
    public Boolean comprobarUsuarioPortafirmas(String codOrganizacion, String documento, String[] params) {
        //Se devuelve siempre true, porque no se comprueba
        return Boolean.TRUE;
    }

    @Override
    public String enviarDocumentoTramitacionPortafirmas(String codOrganizacion, String codProcedimiento, String numExpediente, String codTramite, String ocurrenciaTramite, String codDocumento, int idUsuario, byte[] contenido, String[] params, String portafirmas) {
        throw new UnsupportedOperationException("En esta implementacion no se usa este metodo, si no la sobrecarga que tiene un parametro mas");
    }
    
}
