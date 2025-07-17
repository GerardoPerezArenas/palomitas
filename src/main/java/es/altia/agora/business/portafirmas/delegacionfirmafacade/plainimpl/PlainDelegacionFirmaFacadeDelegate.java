/*______________________________BOF_________________________________*/
package es.altia.agora.business.portafirmas.delegacionfirmafacade.plainimpl;

import es.altia.agora.business.portafirmas.delegacionfirma.dao.CustomDelegacionFirmaDAO;
import es.altia.agora.business.portafirmas.delegacionfirma.vo.DelegacionFirmaPK;
import es.altia.agora.business.portafirmas.delegacionfirma.vo.DelegacionFirmaVO;
import es.altia.agora.business.portafirmas.delegacionfirmafacade.DelegacionFirmaFacadeDelegate;
import es.altia.agora.business.portafirmas.usuariodelegado.vo.UsuarioDelegadoVO;
import es.altia.agora.business.portafirmas.usuariodelegado.vo.UsuarioDelegadoPK;
import es.altia.agora.business.portafirmas.usuariodelegado.dao.CustomUsuarioDelegadoDAO;
import es.altia.util.commons.DebugOperations;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Iterator;


/**
  * @version      $\Revision$ $\Date$
  *
  * Concrete request implementation for DelegacionFirmaFacadeDelegate:
  **/
public class PlainDelegacionFirmaFacadeDelegate implements DelegacionFirmaFacadeDelegate {
	/*_______Constants______________________________________________*/
	private static final String DEFAULT_DSKEY_PARAMETER = "CON.jndi";
    private static final Log _log =
            LogFactory.getLog(PlainDelegacionFirmaFacadeDelegate.class.getName());

	/*_______Attributes_____________________________________________*/
	private String dsKey = null;

	/*_______Operations_____________________________________________*/
	public PlainDelegacionFirmaFacadeDelegate() {
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

    public void resetDsKey(){
		try {
			dsKey = ConfigurationParametersManager.getParameter(DEFAULT_DSKEY_PARAMETER);
		} catch (MissingConfigurationParameterException e) {
			dsKey = GlobalNames.DEFAULT_DATASOURCE;
		}//try-catch        
    }

    /***********************************************************************/
	/**        Do not touch bellow here if not necessary                  **/
	/***********************************************************************/
	public DelegacionFirmaVO createDelegacionFirma(DelegacionFirmaVO vo)
		throws DuplicateInstanceException, InternalErrorException {
		return (DelegacionFirmaVO) FacadeDelegateOperations.simpleCreate(dsKey,DelegacionFirmaVO.class,vo);
	}//createDelegacionFirma


	public DelegacionFirmaVO retrieveDelegacionFirma(DelegacionFirmaPK pk)
		throws InstanceNotFoundException, InternalErrorException {
		return (DelegacionFirmaVO) FacadeDelegateOperations.simpleRetrieve(dsKey,DelegacionFirmaVO.class,pk);
	}//retrieveDelegacionFirma


	public List findDelegacionFirma(SearchCriteria selCriteria,
						OrderCriteria orderCriteria, 
						int startIndex, int count)
		throws InternalErrorException {
		return FacadeDelegateOperations.simpleFind(dsKey,DelegacionFirmaVO.class,selCriteria, orderCriteria, startIndex, count);
	}//findDelegacionFirma


	public void updateDelegacionFirma(DelegacionFirmaVO vo)
            throws InstanceNotFoundException, InternalErrorException, StaleUpdateException{
		FacadeDelegateOperations.simpleUpdate(dsKey,DelegacionFirmaVO.class,vo);
	}//updateDelegacionFirma

    public void createOrUpdateDelegacionFirma(DelegacionFirmaVO vo)
            throws InternalErrorException, StaleUpdateException{
        FacadeDelegateOperations.simpleCreateOrUpdate(dsKey,DelegacionFirmaVO.class,vo);
    }//updateDelegacionFirma


	public void removeDelegacionFirma(DelegacionFirmaPK pk)
            throws InstanceNotFoundException, InternalErrorException, IntegrityViolationAttemptedException {
		FacadeDelegateOperations.simpleDelete(dsKey,DelegacionFirmaVO.class,pk);
	}//removeDelegacionFirma


	public long countDelegacionFirma(SearchCriteria selCriteria)
		throws InternalErrorException {
		return FacadeDelegateOperations.simpleCount(dsKey,DelegacionFirmaVO.class, selCriteria);
	}//countDelegacionFirma

	public boolean supportsNonSerializableParameters() {
		return false;
	}//supportsNonSerializableParameters

    public List findUsuariosDelegables(SearchCriteria selCriteria,
                        OrderCriteria orderCriteria,
                        int startIndex, int count)
        throws InternalErrorException {
        return FacadeDelegateOperations.simpleFind(dsKey,UsuarioDelegadoVO.class,selCriteria, orderCriteria, startIndex, count);
    }//findDelegacionFirma

    public SearchCriteria searchDelegacionFirmaByFechaValidez(final Calendar fechaValidez)
        throws InternalErrorException {
        final CustomDelegacionFirmaDAO customDao = (CustomDelegacionFirmaDAO) FacadeDelegateOperations.getDAO(dsKey,DelegacionFirmaVO.class);
        final SearchCriteria result = customDao.searchByFechaValidez(fechaValidez);
        return result;
    }//searchDelegacionFirmaByFechaValidez

    public SearchCriteria searchDelegacionFirmaByUsuarios(final Integer[] idsUsuarios)
        throws InternalErrorException {
        final CustomDelegacionFirmaDAO customDao = (CustomDelegacionFirmaDAO) FacadeDelegateOperations.getDAO(dsKey,DelegacionFirmaVO.class);
        final SearchCriteria result = customDao.searchByUsuarios(idsUsuarios);
        return result;
    }//searchDelegacionFirmaByUsuarios

    public Integer[] findCierreUsuariosPortafirmas(int idUsuario) throws InternalErrorException {
        SearchCriteria sc = null;
        List result = null;
        Integer[] tmp = new Integer[0];
        Integer[] idsUsuarios = new Integer[1];
        idsUsuarios[0] = new Integer(idUsuario);
        final SearchCriteria scFechaValidez = searchDelegacionFirmaByFechaValidez(Calendar.getInstance());
        boolean seguir = true;
        while (seguir) {
            sc = searchDelegacionFirmaByUsuarios(idsUsuarios);
            sc = sc.and(scFechaValidez);
            result = findDelegacionFirma(sc,null,-1,-1);
            tmp = union(result,idsUsuarios);
            if (idsUsuarios.length == tmp.length) {
                seguir = false;
            } else {
                idsUsuarios = tmp;
            }//if
        };
        return idsUsuarios;
    }//findCierreUsuariosPortafirmas

    private static final Integer[] union(List integerLst, Integer[] integerArray) {
        Set resultSet = new HashSet();
        for (Iterator iterator = integerLst.iterator(); iterator.hasNext();) {
            DelegacionFirmaVO delegacionFirmaVO = (DelegacionFirmaVO) iterator.next();
            resultSet.add(new Integer(delegacionFirmaVO.getCodigoUsuario()));
        }//for
        for (int i = 0; i < integerArray.length; i++) resultSet.add(integerArray[i]);

        if (_log.isDebugEnabled()) {
            final StringBuffer buff = new StringBuffer("ResultSet = ");
            DebugOperations.appendDebugStringFor(resultSet,buff);
            _log.debug(buff);
        }//if

        return (Integer[]) resultSet.toArray(new Integer[0]);
    }//union


    public UsuarioDelegadoVO retrieveUsuarioDelegable(UsuarioDelegadoPK pk)
        throws InstanceNotFoundException, InternalErrorException {
        return (UsuarioDelegadoVO) FacadeDelegateOperations.simpleRetrieve(dsKey,UsuarioDelegadoVO.class,pk);
    }//retrieveUsuarioDelegado

    public SearchCriteria scUsuarioDelegableByFirmante(boolean esFirmante, boolean obligatorioNif, boolean obligBuzonFirma) throws InternalErrorException {
        CustomUsuarioDelegadoDAO customDao = (CustomUsuarioDelegadoDAO) FacadeDelegateOperations.getDAO(dsKey,UsuarioDelegadoVO.class);
        return customDao.searchBySoloFirmantes(esFirmante, obligatorioNif, obligBuzonFirma);
    }

    public OrderCriteria ocUsuarioDelegableByFirmante(boolean ascendente) throws InternalErrorException {
        CustomUsuarioDelegadoDAO customDao = (CustomUsuarioDelegadoDAO) FacadeDelegateOperations.getDAO(dsKey,UsuarioDelegadoVO.class);
        return customDao.orderByNombre(ascendente);
    }

}//class
/*______________________________EOF_________________________________*/

