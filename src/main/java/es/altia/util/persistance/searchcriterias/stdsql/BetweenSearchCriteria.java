/*______________________________BOF_________________________________*/
package es.altia.util.persistance.searchcriterias.stdsql;

import es.altia.util.persistance.searchcriterias.SearchCriteria;
import es.altia.util.persistance.daocommands.SQLFormatter;
import es.altia.util.persistance.daocommands.stdsql.StdSQLFormatter;
import es.altia.util.jdbc.JdbcOperations;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class BetweenSearchCriteria extends SearchCriteria {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    protected String pField = null;
    protected Object pFrom = null;
    protected Object pTo = null;
    protected int pType = -1;

    /*_______Operations_____________________________________________*/
    public BetweenSearchCriteria(String field) {
        pField = field;
    }//constructor
    public BetweenSearchCriteria(String field, String from, String to) {
        pField = field;
        setFrom(from);
        setTo(to);
    }//constructor
    public BetweenSearchCriteria(String field, Integer from, Integer to) {
        pField = field;
        setFrom(from);
        setTo(to);
    }//constructor
    public BetweenSearchCriteria(String field, Long from, Long to) {
        pField = field;
        setFrom(from);
        setTo(to);
    }//constructor
    public BetweenSearchCriteria(String field, Calendar from, Calendar to, int type) {
        pField = field;
        setFrom(from,type);
        setTo(to,type);
    }//constructor

    public void setTo(String to) {
        pTo = to;
        pType = SQLFormatter.TYPE_STRING;
    }//setTo
    public void setTo(Integer to) {
        pTo = to;
        pType = SQLFormatter.TYPE_INTEGER;
    }//setTo
    public void setTo(Long to) {
        pTo = to;
        pType = SQLFormatter.TYPE_LONG;
    }//setTo
    public void setTo(Calendar to, int type) {
        pTo = to;
        pType = type;
    }//setTo

    public void setFrom(String from) {
        pFrom = from;
        pType = SQLFormatter.TYPE_STRING;
    }//setFrom
    public void setFrom(Integer from) {
        pFrom = from;
        pType = SQLFormatter.TYPE_INTEGER;
    }//setFrom
    public void setFrom(Long from) {
        pFrom = from;
        pType = SQLFormatter.TYPE_LONG;
    }//setFrom
    public void setFrom(Calendar from, int type) {
        pFrom = from;
        pType = type;
    }//setFrom

    public String toSQLString() {
        return getSQLStringToExecuteDirectly();
    }//toSQLString

	public int bind(PreparedStatement preparedStatement, int i)
		throws SQLException {
        int result = i;
        if (pFrom!=null) result = JdbcOperations.bindObjectToStatement(pFrom,pType,preparedStatement,result);
		if (pTo!=null) result = JdbcOperations.bindObjectToStatement(pTo,pType,preparedStatement,result);
        return result;
	}//bind

    public String getSQLStringToPrepare() {
        StringBuffer buff = new StringBuffer("( ");
        if ( (pFrom!=null) && (pTo!=null) ) {
            buff.append(pField).append(" BETWEEN (?) AND (?)");
        } else if (pFrom!=null) {
            buff.append(pField).append(" > (?)");
        } else if (pTo!=null) {
            buff.append(pField).append(" < (?)");
        } else {
            buff.append("1=1");
        }//if
        buff.append(" )");
        return buff.toString();
    }//getSQLStringToPrepare

    public String getSQLStringToExecuteDirectly() {
        SQLFormatter formatter = getSQLFormatter();
        StringBuffer buff = new StringBuffer("( ");
        if ( (pFrom!=null) && (pTo!=null) ) {
            buff.append(pField).append(" BETWEEN ");
            buff.append("(").append(formatter.formatValue(pFrom,StdSQLFormatter.TYPE_CALENDAR_DATE)).append(")");
            buff.append(" AND ");
            buff.append("(").append(formatter.formatValue(pTo,StdSQLFormatter.TYPE_CALENDAR_DATE)).append(")");
        } else if (pFrom!=null) {
            buff.append(pField).append(" > ");
            buff.append(formatter.formatValue(pFrom,StdSQLFormatter.TYPE_CALENDAR_DATE));
        } else if (pTo!=null) {
            buff.append(pField).append(" < ");
            buff.append(formatter.formatValue(pTo,StdSQLFormatter.TYPE_CALENDAR_DATE));
        } else {
            buff.append("1=1");
        }//if
        buff.append(" )");
        return buff.toString();
    }//getSQLStringToExecuteDirectly

}//class
/*______________________________EOF_________________________________*/
