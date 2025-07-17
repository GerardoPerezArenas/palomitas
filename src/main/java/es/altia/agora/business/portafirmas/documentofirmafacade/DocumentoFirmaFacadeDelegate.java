/*______________________________BOF_________________________________*/
package es.altia.agora.business.portafirmas.documentofirmafacade;

import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoFirmaPK;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoFirmaVO;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.facades.StatelessBusinessFacadeDelegate;
import es.altia.util.persistance.daocommands.OrderCriteria;
import es.altia.util.persistance.exceptions.DuplicateInstanceException;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.exceptions.IntegrityViolationAttemptedException;
import es.altia.util.persistance.exceptions.StaleUpdateException;
import es.altia.util.persistance.searchcriterias.SearchCriteria;

import java.io.Serializable;
import java.util.List;


/**
  * @version      $\Revision$ $\Date$
  *
  * This is the business facade for handling DocumentoFirma
  *
  **/
public interface DocumentoFirmaFacadeDelegate extends Serializable, StatelessBusinessFacadeDelegate {

    public void firmarDocumentoPortafirmas(DocumentoFirmaVO vo)
        throws InstanceNotFoundException, InternalErrorException; 

    public void firmarDocumentoTramitacion(DocumentoFirmaVO vo)
        throws InstanceNotFoundException, InternalErrorException;

	public DocumentoFirmaVO createDocumentoFirma(DocumentoFirmaVO vo)
		throws DuplicateInstanceException, InternalErrorException;

	public DocumentoFirmaVO retrieveDocumentoFirma(DocumentoFirmaPK pk)
		throws InstanceNotFoundException, InternalErrorException;

	public void updateDocumentoFirma(DocumentoFirmaVO vo)
            throws InstanceNotFoundException, InternalErrorException, StaleUpdateException;

	public void removeDocumentoFirma(DocumentoFirmaPK pk)
            throws InstanceNotFoundException, InternalErrorException, IntegrityViolationAttemptedException;

	public List findDocumentoFirma(SearchCriteria selCriteria,
						OrderCriteria orderCriteria,
						int startIndex, int count)
		throws InternalErrorException;

    public List findDocumentoPortafirmas(SearchCriteria selCriteria,
                        OrderCriteria orderCriteria,
                        int startIndex, int count)
        throws InternalErrorException;

	public long countDocumentoFirma(SearchCriteria selCriteria)
		throws InternalErrorException;

	public OrderCriteria ocDocumentoFirmaByEstadoFirma(boolean asc)
            throws InternalErrorException;
    public SearchCriteria scDocumentoFirmaByDocumento(DocumentoFirmaPK documentPK)
            throws InternalErrorException;
    public OrderCriteria ocDocumentoFirmaByNumeroExpediente(boolean asc)
            throws InternalErrorException;
     public OrderCriteria ocDocumentoFirmaByFechaEnvio(boolean asc)
            throws InternalErrorException;
    public OrderCriteria ocDocumentoPortafirmasByNombreDocumento(boolean asc)
            throws InternalErrorException;
    public SearchCriteria scDocumentoPortafirmasByUsuarios(Integer[] idsUsuarios)
            throws InternalErrorException;
    public SearchCriteria scDocumentoFirmaByEstadoFirma(String estadoFirma)
            throws InternalErrorException;

    public String getDsKey();
    public void setDsKey(String dsKey);

}//interface
/*______________________________EOF_________________________________*/

