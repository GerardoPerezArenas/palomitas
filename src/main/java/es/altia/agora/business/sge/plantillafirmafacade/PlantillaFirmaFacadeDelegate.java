/*______________________________BOF_________________________________*/
package es.altia.agora.business.sge.plantillafirmafacade;

import es.altia.agora.business.sge.plantillafirma.vo.PlantillaFirmaPK;
import es.altia.agora.business.sge.plantillafirma.vo.PlantillaFirmaVO;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.facades.StatelessBusinessFacadeDelegate;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.exceptions.StaleUpdateException;

import java.io.Serializable;


/**
  * @version      $\Revision$ $\Date$
  *
  * This is the model facade for handling PlantillaFirma
  *
  **/
public interface PlantillaFirmaFacadeDelegate extends Serializable, StatelessBusinessFacadeDelegate {

    public String getDsKey();
    public void setDsKey(String dsKey);


	public PlantillaFirmaVO retrievePlantillaFirma(PlantillaFirmaPK pk)
		throws InstanceNotFoundException, InternalErrorException;

	public void updatePlantillaFirma(PlantillaFirmaVO vo)
            throws InstanceNotFoundException, InternalErrorException, StaleUpdateException;

}//interface
/*______________________________EOF_________________________________*/

