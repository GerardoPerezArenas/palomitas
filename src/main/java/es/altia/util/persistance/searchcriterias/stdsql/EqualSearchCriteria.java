/*______________________________BOF_________________________________*/
package es.altia.util.persistance.searchcriterias.stdsql;

import es.altia.util.persistance.searchcriterias.SearchCriteria;
import es.altia.util.persistance.daocommands.SQLFormatter;
import es.altia.util.jdbc.JdbcOperations;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class EqualSearchCriteria extends SearchCriteria {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    protected String pField = null;
    protected Object pTo = null;
    protected int pType = -1;

    /*_______Operations_____________________________________________*/
    public EqualSearchCriteria(String field) {
        pField = field;
    }//constructor
    public EqualSearchCriteria(String field, String to) {
        pField = field;
        setTo(to);
    }//constructor
    public EqualSearchCriteria(String field, Integer to) {
        pField = field;
        setTo(to);
    }//constructor
    public EqualSearchCriteria(String field, Long to) {
        pField = field;
        setTo(to);
    }//constructor
    public EqualSearchCriteria(String field, Calendar to, int type) {
        pField = field;
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

	public int bind(PreparedStatement preparedStatement, int i)
		throws SQLException {
		return JdbcOperations.bindObjectToStatement(pTo,pType,preparedStatement,i);
	}//bind

	public String toSQLString() {
		return getSQLStringToExecuteDirectly();
	}//toSQLString

    public String getSQLStringToPrepare() {
        return new StringBuffer("( ").append(pField).append(getSQLOperator()).append("?)").toString();
    }//getSQLStringToPrepare

    public String getSQLStringToExecuteDirectly() {
        final StringBuffer buff = new StringBuffer("( ").append(pField).append(getSQLOperator());
        buff.append(getSQLFormatter().formatValue(pTo,pType));
        buff.append(" )");
        return buff.toString();
    }//getSQLStringToExecuteDirectly

    protected String getSQLOperator() {
        return " = ";
    }
}//class
/*______________________________EOF_________________________________*/
