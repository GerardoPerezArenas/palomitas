/*______________________________BOF_________________________________*/
package es.altia.agora.business.portafirmas.usuariodelegado.dao;

import es.altia.util.persistance.searchcriterias.SearchCriteria;
import es.altia.util.persistance.daocommands.OrderCriteria;


/**
  * @version      $\Revision$ $\Date$
  *               
  **/
public interface CustomUsuarioDelegadoDAO {

    public SearchCriteria searchBySoloFirmantes(boolean firmantes, boolean obligatorioNif, boolean obligBuzonFirma);

    public OrderCriteria orderByNombre(boolean ascendente);


}//class
/*______________________________EOF_________________________________*/

