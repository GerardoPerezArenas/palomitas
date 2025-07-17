/*______________________________BOF_________________________________*/
package es.altia.agora.business.sge.plantillafirmafacade.plainimpl;

import es.altia.agora.business.sge.plantillafirma.vo.PlantillaFirmaPK;
import es.altia.agora.business.sge.plantillafirma.vo.PlantillaFirmaVO;
import es.altia.agora.business.sge.plantillafirmafacade.PlantillaFirmaFacadeDelegate;
import es.altia.util.configuration.ConfigurationParametersManager;
import es.altia.util.configuration.MissingConfigurationParameterException;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.persistance.GlobalNames;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.exceptions.StaleUpdateException;
import es.altia.util.persistance.facades.defaultimpl.FacadeDelegateOperations;


/**
  * @version      $\Revision$ $\Date$
  *
  * Concrete request implementation for PlantillaFirmaFacadeDelegate:
  **/
public class PlainPlantillaFirmaFacadeDelegate implements PlantillaFirmaFacadeDelegate {
	/*_______Constants______________________________________________*/
	private static final String DEFAULT_DSKEY_PARAMETER = "CON.jndi";

	/*_______Attributes_____________________________________________*/
	private String dsKey = null;

	/*_______Operations_____________________________________________*/
	public PlainPlantillaFirmaFacadeDelegate() {
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

	public PlantillaFirmaVO retrievePlantillaFirma(PlantillaFirmaPK pk)
		throws InstanceNotFoundException, InternalErrorException {
		return (PlantillaFirmaVO) FacadeDelegateOperations.simpleRetrieve(dsKey,PlantillaFirmaVO.class,pk);
	}//retrievePlantillaFirma

	public void updatePlantillaFirma(PlantillaFirmaVO vo)
            throws InstanceNotFoundException, InternalErrorException, StaleUpdateException{
		FacadeDelegateOperations.simpleUpdate(dsKey,PlantillaFirmaVO.class,vo);
	}//updatePlantillaFirma

	public boolean supportsNonSerializableParameters() {
		return false;
	}//supportsNonSerializableParameters

}//class
/*______________________________EOF_________________________________*/

