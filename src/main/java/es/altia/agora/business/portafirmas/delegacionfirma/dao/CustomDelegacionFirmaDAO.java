/*______________________________BOF_________________________________*/
package es.altia.agora.business.portafirmas.delegacionfirma.dao;

import es.altia.util.persistance.searchcriterias.SearchCriteria;

import java.util.Calendar;

/**
  * @version      $\Revision$ $\Date$
  *               
  **/
public interface CustomDelegacionFirmaDAO {

    public SearchCriteria searchByFechaValidez(Calendar fechaValidez);
    public SearchCriteria searchByUsuarios(Integer[] idsUsuarios);

}//class
/*______________________________EOF_________________________________*/

