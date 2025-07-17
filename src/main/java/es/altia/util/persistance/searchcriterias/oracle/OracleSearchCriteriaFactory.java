/*______________________________BOF_________________________________*/
package es.altia.util.persistance.searchcriterias.oracle;

import es.altia.util.persistance.searchcriterias.stdsql.EqualSearchCriteria;
import es.altia.util.persistance.searchcriterias.stdsql.BetweenSearchCriteria;
import es.altia.util.persistance.searchcriterias.stdsql.InSearchCriteria;
import es.altia.util.persistance.searchcriterias.stdsql.GreaterSearchCriteria;
import es.altia.util.persistance.searchcriterias.stdsql.GreaterOrEqualSearchCriteria;
import es.altia.util.persistance.searchcriterias.stdsql.LessThanSearchCriteria;
import es.altia.util.persistance.searchcriterias.stdsql.LessOrEqualSearchCriteria;
import es.altia.util.persistance.searchcriterias.SearchCriteriaFactory;
import es.altia.util.persistance.daocommands.oracle.OracleSQLFormatter;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class OracleSearchCriteriaFactory implements SearchCriteriaFactory {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    private static OracleSearchCriteriaFactory SINGLETON = null;

    /*_______Operations_____________________________________________*/
    protected OracleSearchCriteriaFactory() {
    }

    public static OracleSearchCriteriaFactory getInstance() {
        if (SINGLETON == null) initSingleton();
        return SINGLETON;
    }//getInstance

    private synchronized static void initSingleton() {
        if (SINGLETON == null) SINGLETON = new OracleSearchCriteriaFactory();
    }//initSingleton


    public EqualSearchCriteria newEqualSearchCriteria(String field) {
        final EqualSearchCriteria result = new EqualSearchCriteria(field);
        result.setSQLFormatter(OracleSQLFormatter.getInstance());
        return result;
    }

    public BetweenSearchCriteria newBetweenSearchCriteria(String field) {
        final BetweenSearchCriteria result = new BetweenSearchCriteria(field);
        result.setSQLFormatter(OracleSQLFormatter.getInstance());
        return result;
    }

    public InSearchCriteria newInSearchCriteria(String field) {
        final InSearchCriteria result = new InSearchCriteria(field);
        result.setSQLFormatter(OracleSQLFormatter.getInstance());
        return result;
    }

    public GreaterSearchCriteria newGreaterSearchCriteria(String field) {
        final GreaterSearchCriteria result = new GreaterSearchCriteria(field);
        result.setSQLFormatter(OracleSQLFormatter.getInstance());
        return result;
    }

    public GreaterOrEqualSearchCriteria newGreaterOrEqualSearchCriteria(String field) {
        final GreaterOrEqualSearchCriteria result = new GreaterOrEqualSearchCriteria(field);
        result.setSQLFormatter(OracleSQLFormatter.getInstance());
        return result;
    }

    public LessThanSearchCriteria newLessSearchCriteria(String field) {
        final LessThanSearchCriteria result = new LessThanSearchCriteria(field);
        result.setSQLFormatter(OracleSQLFormatter.getInstance());
        return result;
    }

    public LessOrEqualSearchCriteria newLessOrEqualSearchCriteria(String field) {
        final LessOrEqualSearchCriteria result = new LessOrEqualSearchCriteria(field);
        result.setSQLFormatter(OracleSQLFormatter.getInstance());
        return result;
    }

}//class
/*______________________________EOF_________________________________*/