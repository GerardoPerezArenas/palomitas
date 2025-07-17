/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.sqlserver;

import es.altia.util.persistance.daocommands.*;
import es.altia.util.persistance.daocommands.stdsql.*;
import es.altia.util.persistance.searchcriterias.AndCriteria;
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import es.altia.util.persistance.searchcriterias.SearchCriteriaFactory;
import es.altia.util.persistance.searchcriterias.sqlserver.SQLServerSearchCriteriaFactory;
import es.altia.util.persistance.searchcriterias.stdsql.EqualSearchCriteria;


/**
 * @author
 * @version $\Revision$ $\Date$
 */
public class SQLServerSQLCmdFactory extends SQLDAOCommandFactory {
    public SQLServerSQLCmdFactory(){
        super();
    }

    public LimitCriteria newLimitCriteria() {
        throw new RuntimeException("SQLServerSQLCmdFactory: Not supported!");
    }//newLimitCriteria

    public SQLDAOSequenceCommand newSequenceCmd() {
        return new SQLServerSQLDAOSequenceCmd();
    }//newSequenceCmd

    public SQLDAOGetNextIdentifierCommand newGetNextIdentifierCmd(SQLDAOGetNextIdentifierAdapter adapter) {
        return new StdSQLDAOGetNextIdentifierCmd(adapter);
    }

    public SQLDAOBlobRetrieveCommand newBlobRetrieveCmd(SQLDAOBlobCommandAdapter adapter) {
        return new SQLServerRawSQLDAOBlobRetrieveCmd(adapter);
//        return new OracleSQLDAOBlobRetrieveCmd(adapter);
    }

    public SQLDAOBlobStoreCommand newBlobStoreCmd(SQLDAOBlobCommandAdapter adapter) {
        return new SQLServerRawSQLDAOBlobStoreCmd(adapter);
//        return new OracleSQLDAOBlobStoreCmd(adapter);
    }

    public SQLDAOCreateCommand newCreateCmd(SQLDAOCreateCommandAdapter adapter) {
        return new StdSQLDAOCreateCmd(adapter);
    }

    public SQLDAOExistsCommand newExistsCmd(SQLDAOPKCommandAdapter adapter) {
        return new StdSQLDAOExistsCmd(adapter);
    }

    public SQLDAORetrieveCommand newRetrieveCmd(SQLDAORetrieveCommandAdapter adapter) {
        return new StdSQLDAORetrieveCmd(adapter);
    }

    public SQLDAOUpdateCommand newUpdateCmd(SQLDAOUpdateCommandAdapter adapter) {
        return new StdSQLDAOUpdateCmd(adapter);
    }

    public SQLDAODeleteCommand newDeleteCmd(SQLDAOPKCommandAdapter adapter) {
        return new StdSQLDAODeleteCmd(adapter);
    }

    public SQLDAODeleteByCriteriaCommand newDeleteByCriteriaCmd(SQLDAOPKCommandAdapter adapter) {
        return new StdSQLDAODeleteByCriteriaCmd(adapter);
    }

    public SQLDAOCountCommand newCountCmd(SQLDAOPKCommandAdapter adapter) {
        return new StdSQLDAOCountCmd(adapter);
    }

    public SQLDAOMaxCommand newMaxCmd(String attributeName, String tableName) {
        return new StdSQLDAOMaxCmd(attributeName, tableName);
    }

    public SQLDAOSearchCommand newSearchCmd(SQLDAORetrieveCommandAdapter adapter) {
        return new SQLServerSQLDAOSearchCmd(adapter);
    }

//    public SQLDAOSearchCommand newSearchCmd(SQLDAORetrieveCommandAdapter adapter) {
//        return new StdSQLDAOSearchCmd(this,adapter);
//    }

    public SQLDAOCreateIntArrayPropertyCommand newCreateIntArrayPropertyCmd(SQLDAOCreateIntArrayPropertyCommandAdapter adapter) {
        return new StdSQLDAOCreateIntArrayPropertyCmd(adapter);
    }

    public SQLDAORetrieveIntArrayPropertyCommand newRetrieveIntArrayPropertyCmd(SQLDAORetrieveIntArrayPropertyCommandAdapter adapter) {
        return new StdSQLDAORetrieveIntArrayPropertyCmd(adapter);
    }

    public SQLFormatter newSQLFormatter() {
        return SQLServerSQLFormatter.getInstance();
    }//newSQLFormatter

    public SearchCriteria newAndSC(SearchCriteria left, SearchCriteria right) {
        return new AndCriteria(left,right);
    }

    public EqualSearchCriteria newEqualSC(String field) {
        return new EqualSearchCriteria(field);
    }//newEqualSC

    public OrderCriteria newOrderCriteria(String field, boolean asc) {
        return new OrderCriteria(field,asc);
    }//newOrderCriteria

    public SearchCriteriaFactory getSearchCriteriaFactory() {
        return SQLServerSearchCriteriaFactory.getInstance();
    }//getSearchCriteriaFactory

}//class

/*______________________________EOF_________________________________*/