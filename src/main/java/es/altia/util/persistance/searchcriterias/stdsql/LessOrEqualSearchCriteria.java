/*______________________________BOF_________________________________*/
package es.altia.util.persistance.searchcriterias.stdsql;

import java.util.Calendar;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class LessOrEqualSearchCriteria extends EqualSearchCriteria {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/
    public LessOrEqualSearchCriteria(String field) {
        super(field);
    }//constructor
    public LessOrEqualSearchCriteria(String field, String to) {
        super(field,to);
    }//constructor
    public LessOrEqualSearchCriteria(String field, Integer to) {
        super(field,to);
    }//constructor
    public LessOrEqualSearchCriteria(String field, Long to) {
        super(field,to);
    }//constructor
    public LessOrEqualSearchCriteria(String field, Calendar to, int type) {
        super(field,to,type);
    }//constructor

    protected String getSQLOperator() {
        return " <= ";
    }//getSQLOperator
}//class
/*______________________________EOF_________________________________*/
