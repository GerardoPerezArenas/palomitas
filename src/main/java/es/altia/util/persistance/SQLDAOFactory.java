/*______________________________BOF_________________________________*/
package es.altia.util.persistance;

import es.altia.util.cache.CacheFactory;
import es.altia.util.cache.KeyObject;
import es.altia.util.cache.RetrievalMechanism;
import es.altia.util.collections.CollectionsFactory;
import es.altia.util.commons.ReflectionOperations;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.persistance.daocommands.SQLDAOCommandFactory;
import es.altia.util.persistance.impl.StringPrimaryKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
  * @version      $\Revision$ $\Date$
  *               
  * This is a factory for getting a reference to a concrete
  * DAO for given class. The concrete class name is retrieved
  * from the configuration.
  **/
public final class SQLDAOFactory {
	/*_______Constants______________________________________________*/
    private static final int DEFAULT_DAOS_CACHE_SIZE = 40;
	private static final String DAO_CLASS_NAME_PARAMETER = "SQLDAOFactory/daoClassName/";
    private static final Map FACTORIES_INSTANCES = Collections.synchronizedMap(
            CollectionsFactory.getInstance().newHashMap(SQLDAOCommandFactory.SUPPORTED_DBS.length));//Cache for SQLDAOFactory instances

    //About to be removed (see getDAO method)
	private static final Map daos = Collections.synchronizedMap(CollectionsFactory.getInstance().newHashMap());

    private static final Log _log =
            LogFactory.getLog(SQLDAOFactory.class.getName());

    /*_______Attributes_____________________________________________*/
    private String pDbKey;
    private RetrievalMechanism DAOS_CACHE;//Cache for SQLDAO instances for each instance of SQLDAOFactory

	/*_______Operations_____________________________________________*/
    private SQLDAOFactory(String dbKey) {
        pDbKey = dbKey;
        DAOS_CACHE = CacheFactory.getInstance().newReadCache(
            new SQLDAOImplRetrievalMechanism(), DEFAULT_DAOS_CACHE_SIZE);
    }//constructor

    public static final SQLDAOFactory getInstance(String dbKey)
            throws InternalErrorException {
        try {
            SQLDAOFactory result = (SQLDAOFactory) FACTORIES_INSTANCES.get(dbKey);
            if ( (result == null) && (Arrays.asList(SQLDAOCommandFactory.SUPPORTED_DBS).contains(dbKey.toLowerCase())) ) {
                result = new SQLDAOFactory(dbKey);
                FACTORIES_INSTANCES.put(dbKey,result);
            }//if
            if (result==null) throw new Exception("SQLDAOFactory: Not supported DB");
            return result;
        } catch (Exception e) {
            _log.fatal("SQLDAOFactory: Cannot create new DAOFactory instance!");
            throw new InternalErrorException(e);
        }//try-catch
    }//getInstance

    public SQLDAO getDAOInstance(Class cls) throws InternalErrorException {
        try {
            final StringPrimaryKey clsName = new StringPrimaryKey(cls.getName());
            final SQLDAO result = (SQLDAO) DAOS_CACHE.retrieve(clsName);
            result.initForDb(pDbKey);
            return result;
        } catch (Exception e) {
            _log.fatal("SQLDAOFactory: getDAOInstance() Cannot create new DAO instance!");
            throw new InternalErrorException(e);
        }//try-catch
    }//getDAOInstance

    private static class SQLDAOImplRetrievalMechanism implements RetrievalMechanism {
        /*_______Operations_____________________________________________*/
        public Object retrieve(KeyObject key) throws InternalErrorException {
            try {
                final StringPrimaryKey concreteKey = (StringPrimaryKey) key;
                return (SQLDAO) ReflectionOperations.loadClassAndGetInstanceFromParameter(DAO_CLASS_NAME_PARAMETER+concreteKey.getPK());
            } catch (Exception e) {
                throw new InternalErrorException(e);
            }//try-catch
        }//retrieve
    }//inner class


    /**
     * ¡ W A R N I N G !
     * About to be DEPRECATED.
     * Use instead SQLDAOFactory.getInstance(dbKey).getDAOInstance(cls)
     */
	public static final SQLDAO getDAO(Class cls) throws InternalErrorException {
		try {
			final String clsName = cls.getName();
			SQLDAO result = (SQLDAO) daos.get(clsName);
			if (result == null) {
                result = (SQLDAO) ReflectionOperations.loadClassAndGetInstanceFromParameter(DAO_CLASS_NAME_PARAMETER+clsName);                
                result.initForDb(null);
                daos.put(clsName,result);
			}//if
			return result;
		} catch (Exception e) {
			_log.fatal("SQLDAOFactory: getDAO() Cannot create new DAO instance!");
			throw new InternalErrorException(e);
		}//try-catch
	}//getDAO
		
}//class
/*______________________________EOF_________________________________*/
