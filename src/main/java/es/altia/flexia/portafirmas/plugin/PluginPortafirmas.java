package es.altia.flexia.portafirmas.plugin;


import es.altia.common.exception.TechnicalException;
import es.altia.flexia.portafirmas.plugin.vo.DocumentoFirmadoVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.ArrayListFirmasVO;

public interface PluginPortafirmas {
    
    public boolean verificarFirma (DocumentoFirmadoVO documento) throws TechnicalException;
    
    public ArrayListFirmasVO verificarFirmaInfo(DocumentoFirmadoVO documento) throws TechnicalException;
    
    public String firmaServidor(byte[] documento) throws TechnicalException;
    
}
