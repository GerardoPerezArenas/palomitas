/*______________________________BOF_________________________________*/
package es.altia.agora.business.portafirmas.documentofirma.dao;

import es.altia.util.persistance.daocommands.OrderCriteria;
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.exceptions.StaleUpdateException;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoFirmaPK;

import java.sql.Connection;

/**
  * @version      $\Revision$ $\Date$
  *               
  **/
public interface CustomDocumentoFirmaDAO {
	public OrderCriteria orderByEstadoFirma(boolean asc);
    public OrderCriteria orderByNumeroExpediente(boolean asc);
    public OrderCriteria orderDocumentoPortafirmasByNombreDocumento(boolean asc);
    public OrderCriteria orderByFechaEnvio(boolean asc);
    public SearchCriteria searchByDocument(DocumentoFirmaPK documentoPK);
    public SearchCriteria searchDocumentoPortafirmasByUsuarios(Integer[] idsUsuarios);
    public SearchCriteria searchByEstadoFirma(String estadoFirma);
    public void updateEstadoDocumentoTramitacion(Connection connection, PersistentObject newVO)
        throws InstanceNotFoundException, StaleUpdateException, InternalErrorException;
}//class
/*______________________________EOF_________________________________*/

