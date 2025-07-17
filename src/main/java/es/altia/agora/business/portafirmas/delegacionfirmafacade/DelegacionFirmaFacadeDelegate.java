/*______________________________BOF_________________________________*/
package es.altia.agora.business.portafirmas.delegacionfirmafacade;

import es.altia.agora.business.portafirmas.delegacionfirma.vo.DelegacionFirmaPK;
import es.altia.agora.business.portafirmas.delegacionfirma.vo.DelegacionFirmaVO;
import es.altia.agora.business.portafirmas.usuariodelegado.vo.UsuarioDelegadoVO;
import es.altia.agora.business.portafirmas.usuariodelegado.vo.UsuarioDelegadoPK;
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
import java.util.Calendar;


/**
  * @version      $\Revision$ $\Date$
  *
  * This is the model facade for handling DelegacionFirma
  *
  **/
public interface DelegacionFirmaFacadeDelegate extends Serializable, StatelessBusinessFacadeDelegate {

    public String getDsKey();
    public void setDsKey(String dsKey);
    public void resetDsKey();

	public DelegacionFirmaVO createDelegacionFirma(DelegacionFirmaVO vo)
		throws DuplicateInstanceException, InternalErrorException;

	public DelegacionFirmaVO retrieveDelegacionFirma(DelegacionFirmaPK pk)
		throws InstanceNotFoundException, InternalErrorException;

	public void updateDelegacionFirma(DelegacionFirmaVO vo)
            throws InstanceNotFoundException, InternalErrorException, StaleUpdateException;

    public void createOrUpdateDelegacionFirma(DelegacionFirmaVO vo)
        throws InternalErrorException, StaleUpdateException;

	public void removeDelegacionFirma(DelegacionFirmaPK pk)
            throws InstanceNotFoundException, InternalErrorException, IntegrityViolationAttemptedException;

	public List findDelegacionFirma(SearchCriteria selCriteria,
						OrderCriteria orderCriteria,
						int startIndex, int count)
		throws InternalErrorException;

	public long countDelegacionFirma(SearchCriteria selCriteria)
		throws InternalErrorException;

    public List findUsuariosDelegables(SearchCriteria selCriteria,
                        OrderCriteria orderCriteria,
                        int startIndex, int count)
        throws InternalErrorException;

    public SearchCriteria searchDelegacionFirmaByFechaValidez(final Calendar fechaValidez)
        throws InternalErrorException;

    public SearchCriteria searchDelegacionFirmaByUsuarios(final Integer[] idsUsuarios)
        throws InternalErrorException;

    public Integer[] findCierreUsuariosPortafirmas(int idUsuario)
            throws InternalErrorException;

    public UsuarioDelegadoVO retrieveUsuarioDelegable(UsuarioDelegadoPK pk)
        throws InstanceNotFoundException, InternalErrorException;

    public SearchCriteria scUsuarioDelegableByFirmante(boolean esFirmante, boolean obligatorioNif, boolean obligBuzonFirma)
            throws InternalErrorException;

    public OrderCriteria ocUsuarioDelegableByFirmante(boolean ascendente) throws InternalErrorException;
}//interface
/*______________________________EOF_________________________________*/

