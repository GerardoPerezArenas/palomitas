
package es.altia.flexia.portafirmas.documentos.externos.localizacion.expediente.servicio;

import es.altia.flexia.portafirmas.documentos.externos.localizacion.expediente.vo.DocumentoExternoPortafirmasVO;
import es.altia.flexia.portafirmas.documentos.externos.localizacion.expediente.vo.SalidaServicioLocalizacionExpedientePortafirmasVO;

/**
 *
 * @author oscar
 */
public interface ServicioLocalizacionExpedientePortafirmas {
    
    public SalidaServicioLocalizacionExpedientePortafirmasVO getExpediente(DocumentoExternoPortafirmasVO doc);
}
