/*______________________________BOF_________________________________*/
package es.altia.util.persistance.searchcriterias.stdsql;

import es.altia.util.persistance.searchcriterias.SearchCriteriaFactory;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class StdSearchCriteriaFactory implements SearchCriteriaFactory {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    private static StdSearchCriteriaFactory SINGLETON = null;

    /*_______Operations_____________________________________________*/
    protected StdSearchCriteriaFactory() {
    }

    public static StdSearchCriteriaFactory getInstance() {
        if (SINGLETON == null) initSingleton();
        return SINGLETON;
    }//getInstance

    private synchronized static void initSingleton() {
        if (SINGLETON == null) SINGLETON = new StdSearchCriteriaFactory();
    }//initSingleton


    public EqualSearchCriteria newEqualSearchCriteria(String field) {
        return new EqualSearchCriteria(field);
    }

    public BetweenSearchCriteria newBetweenSearchCriteria(String field) {
        return new BetweenSearchCriteria(field);
    }

    public InSearchCriteria newInSearchCriteria(String field) {
        return new InSearchCriteria(field);
    }

    public GreaterSearchCriteria newGreaterSearchCriteria(String field) {
        return new GreaterSearchCriteria(field);
    }

    public GreaterOrEqualSearchCriteria newGreaterOrEqualSearchCriteria(String field) {
        return new GreaterOrEqualSearchCriteria(field);
    }

    public LessThanSearchCriteria newLessSearchCriteria(String field) {
        return new LessThanSearchCriteria(field);
    }

    public LessOrEqualSearchCriteria newLessOrEqualSearchCriteria(String field) {
        return new LessOrEqualSearchCriteria(field);
    }

}//class
/*______________________________EOF_________________________________*/