/*______________________________BOF_________________________________*/
package es.altia.agora.business.portafirmas.documentofirmafacade.plainimpl;

import es.altia.agora.business.portafirmas.documentofirma.dao.CustomDocumentoFirmaDAO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoFirmaPK;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoFirmaVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoPortafirmasVO;
import es.altia.agora.business.portafirmas.documentofirmafacade.DocumentoFirmaFacadeDelegate;
import es.altia.util.configuration.ConfigurationParametersManager;
import es.altia.util.configuration.MissingConfigurationParameterException;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.persistance.GlobalNames;
import es.altia.util.persistance.daocommands.OrderCriteria;
import es.altia.util.persistance.exceptions.DuplicateInstanceException;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.exceptions.IntegrityViolationAttemptedException;
import es.altia.util.persistance.exceptions.StaleUpdateException;
import es.altia.util.persistance.facades.defaultimpl.FacadeDelegateOperations;
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import es.altia.util.facades.BusinessRequestManager;

import java.util.List;


/**
  * @version      $\Revision$ $\Date$
  *
  * Concrete request implementation for DocumentoFirmaFacadeDelegate:
  **/
public class PlainDocumentoFirmaFacadeDelegate implements DocumentoFirmaFacadeDelegate {
	/*_______Constants______________________________________________*/
	private static final String DEFAULT_DSKEY_PARAMETER = "CON.jndi";

	/*_______Attributes_____________________________________________*/
	private String dsKey = null;

	/*_______Operations_____________________________________________*/
	public PlainDocumentoFirmaFacadeDelegate() {
		try {
			dsKey = ConfigurationParametersManager.getParameter(DEFAULT_DSKEY_PARAMETER);
		} catch (MissingConfigurationParameterException e) {
			dsKey = GlobalNames.DEFAULT_DATASOURCE;
		}//try-catch
	}//constructor

    public String getDsKey() {
        return dsKey;
    }
    public void setDsKey(String dsKey) {
        this.dsKey = dsKey;
    }

	/***********************************************************************/
	/**        Do not touch bellow here if not necessary                  **/
	/***********************************************************************/
	public DocumentoFirmaVO createDocumentoFirma(DocumentoFirmaVO vo)
		throws DuplicateInstanceException, InternalErrorException {
		return (DocumentoFirmaVO) FacadeDelegateOperations.simpleCreate(dsKey,DocumentoFirmaVO.class,vo);
	}//createDocumentoFirma

	public DocumentoFirmaVO retrieveDocumentoFirma(DocumentoFirmaPK pk)
		throws InstanceNotFoundException, InternalErrorException {
		return (DocumentoFirmaVO) FacadeDelegateOperations.simpleRetrieve(dsKey,DocumentoFirmaVO.class,pk);
	}//retrieveDocumentoFirma

	public List findDocumentoFirma(SearchCriteria selCriteria,
						OrderCriteria orderCriteria, 
						int startIndex, int count)
		throws InternalErrorException {
		return FacadeDelegateOperations.simpleFind(dsKey,DocumentoFirmaVO.class,selCriteria, orderCriteria, startIndex, count);
	}//findDocumentoFirma

    public List findDocumentoPortafirmas(SearchCriteria selCriteria,
                        OrderCriteria orderCriteria,
                        int startIndex, int count)
        throws InternalErrorException {
        return FacadeDelegateOperations.simpleFindCustom(dsKey,DocumentoFirmaVO.class,DocumentoPortafirmasVO.class,selCriteria, orderCriteria, startIndex, count);
    }//findDocumentoPortafirmas

	public void updateDocumentoFirma(DocumentoFirmaVO vo)
            throws InstanceNotFoundException, InternalErrorException, StaleUpdateException{
		FacadeDelegateOperations.simpleUpdate(dsKey,DocumentoFirmaVO.class,vo);
	}//updateDocumentoFirma

	public void removeDocumentoFirma(DocumentoFirmaPK pk)
            throws InstanceNotFoundException, InternalErrorException, IntegrityViolationAttemptedException {
		FacadeDelegateOperations.simpleDelete(dsKey,DocumentoFirmaVO.class,pk);
	}//removeDocumentoFirma

	public long countDocumentoFirma(SearchCriteria selCriteria)
		throws InternalErrorException {
		return FacadeDelegateOperations.simpleCount(dsKey,DocumentoFirmaVO.class, selCriteria);
	}//countDocumentoFirma

    public OrderCriteria ocDocumentoFirmaByEstadoFirma(boolean asc) throws InternalErrorException {
        CustomDocumentoFirmaDAO customDao = (CustomDocumentoFirmaDAO) FacadeDelegateOperations.getDAO(dsKey,DocumentoFirmaVO.class);
        OrderCriteria result = customDao.orderByEstadoFirma(asc);
        return result;
    }//ocDocumentoFirmaByEstadoFirma

    public SearchCriteria scDocumentoFirmaByDocumento(DocumentoFirmaPK documentPK) throws InternalErrorException {
        CustomDocumentoFirmaDAO customDao = (CustomDocumentoFirmaDAO) FacadeDelegateOperations.getDAO(dsKey,DocumentoFirmaVO.class);
        SearchCriteria result = customDao.searchByDocument(documentPK);
        return result;
    }//scDocumentoFirmaByDocumento

    public OrderCriteria ocDocumentoFirmaByNumeroExpediente(boolean asc) throws InternalErrorException {
        CustomDocumentoFirmaDAO customDao = (CustomDocumentoFirmaDAO) FacadeDelegateOperations.getDAO(dsKey,DocumentoFirmaVO.class);
        OrderCriteria result = customDao.orderByEstadoFirma(asc);
        return result;
    }//orderByNumeroExpediente

    public OrderCriteria ocDocumentoPortafirmasByNombreDocumento(boolean asc) throws InternalErrorException {
        CustomDocumentoFirmaDAO customDao = (CustomDocumentoFirmaDAO) FacadeDelegateOperations.getDAO(dsKey,DocumentoFirmaVO.class);
        OrderCriteria result = customDao.orderByEstadoFirma(asc);
        return result;
    }//orderDocumentoPortafirmasByNombreDocumento

     public OrderCriteria ocDocumentoFirmaByFechaEnvio(boolean asc) throws InternalErrorException {
        CustomDocumentoFirmaDAO customDao = (CustomDocumentoFirmaDAO) FacadeDelegateOperations.getDAO(dsKey,DocumentoFirmaVO.class);
        OrderCriteria result = customDao.orderByFechaEnvio(asc);
        return result;
    }//orderDocumentoPortafirmasByNombreDocumento

    public SearchCriteria scDocumentoPortafirmasByUsuarios(Integer[] idsUsuarios) throws InternalErrorException {
        CustomDocumentoFirmaDAO customDao = (CustomDocumentoFirmaDAO) FacadeDelegateOperations.getDAO(dsKey,DocumentoFirmaVO.class);
        SearchCriteria result = customDao.searchDocumentoPortafirmasByUsuarios(idsUsuarios);
        return result;
    }//scDocumentoPortafirmasByUsuarios

    public SearchCriteria scDocumentoFirmaByEstadoFirma(String estadoFirma) throws InternalErrorException {
        CustomDocumentoFirmaDAO customDao = (CustomDocumentoFirmaDAO) FacadeDelegateOperations.getDAO(dsKey,DocumentoFirmaVO.class);
        SearchCriteria result = customDao.searchByEstadoFirma(estadoFirma);
        return result;
    }//scDocumentoFirmaByEstadoFirma

    public boolean supportsNonSerializableParameters() {
		return false;
	}//supportsNonSerializableParameters


    public void firmarDocumentoPortafirmas(DocumentoFirmaVO vo)
            throws InstanceNotFoundException, InternalErrorException {
        try {
            final FirmaUpdateDocumentoRequest br;
            if (dsKey!=null)
                br = new FirmaUpdateDocumentoRequest(dsKey,vo);
            else
                br = new FirmaUpdateDocumentoRequest(vo);
            BusinessRequestManager.getInstance().handleRequest(br);
            br.getResult();
        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (StaleUpdateException e) {
            throw new InternalErrorException(e);
        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }//try-catch
    }//firmarDocumentoPortafirmas

    public void firmarDocumentoTramitacion(DocumentoFirmaVO vo)
            throws InternalErrorException {
        try {
            final FirmaDocumentoTramitacionRequest br;
            if (dsKey!=null)
                br = new FirmaDocumentoTramitacionRequest(dsKey,vo);
            else
                br = new FirmaDocumentoTramitacionRequest(vo);
            BusinessRequestManager.getInstance().handleRequest(br);
            br.getResult();
        } catch (InstanceNotFoundException e) {
            throw new InternalErrorException(e);
        } catch (StaleUpdateException e) {
            throw new InternalErrorException(e);
        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }//try-catch
    }//firmarDocumentoTramitacion


}//class
/*______________________________EOF_________________________________*/

