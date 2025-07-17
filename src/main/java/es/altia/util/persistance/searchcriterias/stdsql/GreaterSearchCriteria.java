/*______________________________BOF_________________________________*/
package es.altia.util.persistance.searchcriterias.stdsql;

import java.util.Calendar;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class GreaterSearchCriteria extends EqualSearchCriteria {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/
    public GreaterSearchCriteria(String field) {
        super(field);
    }//constructor
    public GreaterSearchCriteria(String field, String to) {
        super(field,to);
    }//constructor
    public GreaterSearchCriteria(String field, Integer to) {
        super(field,to);
    }//constructor
    public GreaterSearchCriteria(String field, Long to) {
        super(field,to);
    }//constructor
    public GreaterSearchCriteria(String field, Calendar to, int type) {
        super(field,to,type);
    }//constructor

    protected String getSQLOperator() {
        return " > ";
    }//getSQLOperator
}//class
/*______________________________EOF_________________________________*/
