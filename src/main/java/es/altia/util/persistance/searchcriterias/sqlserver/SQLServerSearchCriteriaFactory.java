/*______________________________BOF_________________________________*/
package es.altia.util.persistance.searchcriterias.sqlserver;

import es.altia.util.persistance.daocommands.sqlserver.SQLServerSQLFormatter;
import es.altia.util.persistance.searchcriterias.SearchCriteriaFactory;
import es.altia.util.persistance.searchcriterias.stdsql.*;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class SQLServerSearchCriteriaFactory implements SearchCriteriaFactory {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    private static SQLServerSearchCriteriaFactory SINGLETON = null;

    /*_______Operations_____________________________________________*/
    protected SQLServerSearchCriteriaFactory() {
    }

    public static SQLServerSearchCriteriaFactory getInstance() {
        if (SINGLETON == null) initSingleton();
        return SINGLETON;
    }//getInstance

    private synchronized static void initSingleton() {
        if (SINGLETON == null) SINGLETON = new SQLServerSearchCriteriaFactory();
    }//initSingleton


    public EqualSearchCriteria newEqualSearchCriteria(String field) {
        final EqualSearchCriteria result = new EqualSearchCriteria(field);
        result.setSQLFormatter(SQLServerSQLFormatter.getInstance());
        return result;
    }

    public BetweenSearchCriteria newBetweenSearchCriteria(String field) {
        final BetweenSearchCriteria result = new BetweenSearchCriteria(field);
        result.setSQLFormatter(SQLServerSQLFormatter.getInstance());
        return result;
    }

    public InSearchCriteria newInSearchCriteria(String field) {
        final InSearchCriteria result = new InSearchCriteria(field);
        result.setSQLFormatter(SQLServerSQLFormatter.getInstance());
        return result;
    }

    public GreaterSearchCriteria newGreaterSearchCriteria(String field) {
        final GreaterSearchCriteria result = new GreaterSearchCriteria(field);
        result.setSQLFormatter(SQLServerSQLFormatter.getInstance());
        return result;
    }

    public GreaterOrEqualSearchCriteria newGreaterOrEqualSearchCriteria(String field) {
        final GreaterOrEqualSearchCriteria result = new GreaterOrEqualSearchCriteria(field);
        result.setSQLFormatter(SQLServerSQLFormatter.getInstance());
        return result;
    }

    public LessThanSearchCriteria newLessSearchCriteria(String field) {
        final LessThanSearchCriteria result = new LessThanSearchCriteria(field);
        result.setSQLFormatter(SQLServerSQLFormatter.getInstance());
        return result;
    }

    public LessOrEqualSearchCriteria newLessOrEqualSearchCriteria(String field) {
        final LessOrEqualSearchCriteria result = new LessOrEqualSearchCriteria(field);
        result.setSQLFormatter(SQLServerSQLFormatter.getInstance());
        return result;
    }

}//class
/*______________________________EOF_________________________________*/