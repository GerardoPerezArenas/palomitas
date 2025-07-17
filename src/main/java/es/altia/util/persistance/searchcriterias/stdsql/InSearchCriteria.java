/*______________________________BOF_________________________________*/
package es.altia.util.persistance.searchcriterias.stdsql;

import es.altia.util.persistance.daocommands.SQLFormatter;
import es.altia.util.persistance.searchcriterias.SearchCriteria;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class InSearchCriteria extends SearchCriteria {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    protected String pField = null;
    protected Object[] pTo = null;
    protected int pType = -1;

    /*_______Operations_____________________________________________*/
    public InSearchCriteria(String field) {
        pField = field;
    }//constructor
    public InSearchCriteria(String field, String[] to) {
        pField = field;
        setTo(to);
    }//constructor
    public InSearchCriteria(String field, Integer[] to) {
        pField = field;
        setTo(to);
    }//constructor
    public InSearchCriteria(String field, Long[] to) {
        pField = field;
        setTo(to);
    }//constructor
    public InSearchCriteria(String field, Calendar[] to, int type) {
        pField = field;
        setTo(to,type);
    }//constructor

    public void setTo(String[] to) {
        pTo = to;
        pType = SQLFormatter.TYPE_STRING;
    }//setTo
    public void setTo(Integer[] to) {
        pTo = to;
        pType = SQLFormatter.TYPE_INTEGER;
    }//setTo
    public void setTo(Long[] to) {
        pTo = to;
        pType = SQLFormatter.TYPE_LONG;
    }//setTo
    public void setTo(Calendar[] to, int type) {
        pTo = to;
        pType = type;
    }//setTo

	public String toSQLString() {
		return getSQLStringToExecuteDirectly();
	}//toSQLString

    public String getSQLStringToPrepare() {
        return getSQLStringToExecuteDirectly();
    }//getSQLStringToPrepare

    public int bind(PreparedStatement preparedStatement, int i)
        throws SQLException {
        int result = i;
        return result;
    }//bind

    public String getSQLStringToExecuteDirectly() {
        final SQLFormatter formatter = getSQLFormatter();
        final StringBuffer buff = new StringBuffer("( ");
        if ( (pTo!=null) && (pTo.length>0) )  {
            buff.append(pField).append(" IN (");
            int i = 0;
            int l = pTo.length;
            while ( i < (l-1) ) {
                buff.append(formatter.formatValue(pTo[i],pType)).append(", ");
                i++;
            }//while
            buff.append(formatter.formatValue(pTo[l-1],pType)).append(")");
        } else {
            buff.append("1=1");
        }//if
        buff.append(" )");
        return buff.toString();
    }//getSQLStringToExecuteDirectly

}//class
/*______________________________EOF_________________________________*/
