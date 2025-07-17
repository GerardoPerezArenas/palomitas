/*______________________________BOF_________________________________*/
package es.altia.util.persistance.searchcriterias.stdsql;

import java.util.Calendar;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class GreaterOrEqualSearchCriteria extends EqualSearchCriteria {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/
    public GreaterOrEqualSearchCriteria(String field) {
        super(field);
    }//constructor
    public GreaterOrEqualSearchCriteria(String field, String to) {
        super(field,to);
    }//constructor
    public GreaterOrEqualSearchCriteria(String field, Integer to) {
        super(field,to);
    }//constructor
    public GreaterOrEqualSearchCriteria(String field, Long to) {
        super(field,to);
    }//constructor
    public GreaterOrEqualSearchCriteria(String field, Calendar to, int type) {
        super(field,to,type);
    }//constructor

    protected String getSQLOperator() {
        return " >= ";
    }//getSQLOperator
}//class
/*______________________________EOF_________________________________*/
