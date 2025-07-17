/*______________________________BOF_________________________________*/
package es.altia.util.persistance.searchcriterias;

import es.altia.util.persistance.searchcriterias.stdsql.*;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public interface SearchCriteriaFactory {
    /*_______Operations_____________________________________________*/
    public EqualSearchCriteria newEqualSearchCriteria(String field);
    public BetweenSearchCriteria newBetweenSearchCriteria(String field);
    public InSearchCriteria newInSearchCriteria(String field);
    public GreaterSearchCriteria newGreaterSearchCriteria(String field);
    public GreaterOrEqualSearchCriteria newGreaterOrEqualSearchCriteria(String field);
    public LessThanSearchCriteria newLessSearchCriteria(String field);
    public LessOrEqualSearchCriteria newLessOrEqualSearchCriteria(String field);

}//class
/*______________________________EOF_________________________________*/
