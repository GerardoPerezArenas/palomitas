package es.altia.flexia.portafirmasexternocliente.plugin.service;

import es.altia.flexia.portafirmasexternocliente.vo.DocumentoTramitacionVO;

public interface PluginPortafirmasExternoService {
    
    /**
     * metodo que invoca al servicio para enviar el documento al portafirmas
     * @param DocumentoTramitacionVO doc
     * @param String[] params
     * @return 
     */
    public String enviarDocumentoTramitacionAFirma(DocumentoTramitacionVO doc, int idUsuario, String[] params) throws Exception;
    
    
}//class