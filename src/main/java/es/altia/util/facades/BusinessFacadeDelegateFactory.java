/*______________________________BOF_________________________________*/
package es.altia.util.facades;

import es.altia.util.collections.CollectionsFactory;
import es.altia.util.commons.ReflectionOperations;
import es.altia.util.exceptions.InternalErrorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;
import java.util.Map;



/**
  * @version      $\Revision$ $\Date$
  *               
  **/
public final class BusinessFacadeDelegateFactory {

	/*_______Constants______________________________________________*/
	private final static String BASE_CLASS_NAME_PARAMETER = "BusinessFacadeDelegateFactory/facadeClassName/";
	private static Map CACHED_INSTANCES = Collections.synchronizedMap(CollectionsFactory.getInstance().newHashMap());
    private static final Log _log =
            LogFactory.getLog(BusinessFacadeDelegateFactory.class.getName());


    /*_______Operations_____________________________________________*/
	private BusinessFacadeDelegateFactory() {}


	public static final BusinessFacadeDelegate getFacadeDelegate(Class cls) throws InternalErrorException {
		try {
			final String clsName = cls.getName();
			BusinessFacadeDelegate result = (BusinessFacadeDelegate) CACHED_INSTANCES.get(clsName);
			if (result == null)
                result = loadFacade(clsName);
			return result;
		} catch (Exception e) {
			_log.fatal("BusinessFacadeDelegateFactory: Cannot create new FACADE instance!");
			throw new InternalErrorException(e);
		}//try-catch
	}//getDAO


    private static final synchronized BusinessFacadeDelegate loadFacade(String clsName) throws InternalErrorException  {
        final BusinessFacadeDelegate result = (BusinessFacadeDelegate) ReflectionOperations.loadClassAndGetInstanceFromParameter(BASE_CLASS_NAME_PARAMETER+clsName);
        if (result instanceof StatelessBusinessFacadeDelegate)
            CACHED_INSTANCES.put(clsName,result);
        return result;
    }//loadFacade


}//class
/*______________________________EOF_________________________________*/
