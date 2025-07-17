/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.cache.CacheFactory;
import es.altia.util.cache.KeyObject;
import es.altia.util.cache.RetrievalMechanism;
import es.altia.util.commons.ReflectionOperations;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.persistance.impl.StringPrimaryKey;
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import es.altia.util.persistance.searchcriterias.SearchCriteriaFactory;
import es.altia.util.persistance.searchcriterias.stdsql.EqualSearchCriteria;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 */
public abstract class SQLDAOCommandFactory {

    /*_______Constants______________________________________________*/
    public static final String DB_STANDARDSQL = "standardsql";
    public static final String DB_INFORMIX = "informix";
    public static final String DB_POSTGRESQL = "postgresql";
    public static final String DB_MYSQL = "mysql";
    public static final String DB_ORACLE = "oracle";
    public static final String DB_SQLSERVER = "sqlserver";
    public static final String[] SUPPORTED_DBS = {DB_STANDARDSQL,DB_ORACLE, DB_SQLSERVER};

    /*_______Attributes_____________________________________________*/
    protected static final Log _log =
            LogFactory.getLog(SQLDAOCommandFactory.class.getName());

    private final static String DB_FACTORY_CLASS_NAME_PARAMETER = "SQLDAOCommandFactory/ClassName/";
    private final static RetrievalMechanism CACHE = CacheFactory.getInstance().newReadCache(
            new SQLDAOCommandFactoryImplRetrievalMechanism(), SUPPORTED_DBS.length);


    /*_______Operations_____________________________________________*/
    public static SQLDAOCommandFactory getInstance(String databaseServer)
            throws InternalErrorException {
        try {
            return (SQLDAOCommandFactory) CACHE.retrieve(new StringPrimaryKey(databaseServer));
        } catch (Exception e) {
            _log.fatal("SQLDAOCommandFactory: Cannot create new DAOCommandFactory instance!");
            throw new InternalErrorException(e);
        }//try-catch
    }//getInstance

    private static class SQLDAOCommandFactoryImplRetrievalMechanism implements RetrievalMechanism {
        /*_______Operations_____________________________________________*/
        public Object retrieve(KeyObject key) throws InternalErrorException {
            try {
                final StringPrimaryKey concreteKey = (StringPrimaryKey) key;
                return (SQLDAOCommandFactory) ReflectionOperations.loadClassAndGetInstanceFromParameter(DB_FACTORY_CLASS_NAME_PARAMETER+concreteKey.getPK().toLowerCase());
            } catch (Exception e) {
                throw new InternalErrorException(e);
            }//try-catch
        }//retrieve
    }//inner class


    protected SQLDAOCommandFactory() {}

    public abstract LimitCriteria newLimitCriteria();
    public abstract SQLDAOSequenceCommand newSequenceCmd();
    public abstract SQLDAOGetNextIdentifierCommand newGetNextIdentifierCmd(SQLDAOGetNextIdentifierAdapter adapter);
    public abstract SQLDAOBlobRetrieveCommand newBlobRetrieveCmd(SQLDAOBlobCommandAdapter adapter);
    public abstract SQLDAOBlobStoreCommand newBlobStoreCmd(SQLDAOBlobCommandAdapter adapter);

    public abstract SQLDAOCreateCommand newCreateCmd(SQLDAOCreateCommandAdapter adapter);
    public abstract SQLDAOExistsCommand newExistsCmd(SQLDAOPKCommandAdapter adapter);
    public abstract SQLDAORetrieveCommand newRetrieveCmd(SQLDAORetrieveCommandAdapter adapter);
    public abstract SQLDAOUpdateCommand newUpdateCmd(SQLDAOUpdateCommandAdapter adapter);
    public abstract SQLDAODeleteCommand newDeleteCmd(SQLDAOPKCommandAdapter adapter);
    public abstract SQLDAODeleteByCriteriaCommand newDeleteByCriteriaCmd(SQLDAOPKCommandAdapter adapter);

    public abstract SQLDAOCountCommand newCountCmd(SQLDAOPKCommandAdapter adapter);
    public abstract SQLDAOMaxCommand newMaxCmd(String attributeName, String tableName);

    public abstract SQLDAOSearchCommand newSearchCmd(SQLDAORetrieveCommandAdapter adapter);

    public abstract SQLDAOCreateIntArrayPropertyCommand newCreateIntArrayPropertyCmd(SQLDAOCreateIntArrayPropertyCommandAdapter adapter);
    public abstract SQLDAORetrieveIntArrayPropertyCommand newRetrieveIntArrayPropertyCmd(SQLDAORetrieveIntArrayPropertyCommandAdapter adapter);

    public abstract SQLFormatter newSQLFormatter();
    public abstract SearchCriteria newAndSC(SearchCriteria left, SearchCriteria right);
    public abstract EqualSearchCriteria newEqualSC(String field);
    public abstract OrderCriteria newOrderCriteria(String field,boolean asc);

    public abstract SearchCriteriaFactory getSearchCriteriaFactory();

}//class

/*______________________________EOF_________________________________*/