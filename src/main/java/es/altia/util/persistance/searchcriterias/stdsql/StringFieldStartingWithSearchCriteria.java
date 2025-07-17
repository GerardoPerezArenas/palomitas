/*______________________________BOF_________________________________*/
package es.altia.util.persistance.searchcriterias.stdsql;

import es.altia.util.persistance.daocommands.stdsql.StdSQLFormatter;
import es.altia.util.persistance.searchcriterias.SearchCriteria;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * @author
 * @version $\Revision$ $\Date$
 */
public class StringFieldStartingWithSearchCriteria extends SearchCriteria {
    /*_______Constants______________________________________________*/
    protected static final String EMPTY_STR = "";

    /*_______Atributes______________________________________________*/
    protected String pField = null;
    protected String pTo = null;
    private String preparedClause = null;

    /*_______Operations_____________________________________________*/
    public StringFieldStartingWithSearchCriteria(String field, String to) {
        this.pField = field;
        setTo(to);
        preparedClause = "( "+pField+" LIKE ? ) ";
    }//constructor

    public String getTo() {
        return pTo;
    }//getTo

    public void setTo(String to) {
        this.pTo = to.trim();
    }//setTo

    public int bind(PreparedStatement preparedStatement, int i)
     throws SQLException {
        int result = i;

        preparedStatement.setString(result++, pTo+"%");
        return result;
    }//bind

    public String toSQLString() {
        return getSQLStringToExecuteDirectly();
    }//toSQLString

    public String getSQLStringToPrepare() {
        return preparedClause;
    }//getSQLStringToPrepare

    public String getSQLStringToExecuteDirectly() {
        final StringBuffer buff = new StringBuffer("( ");
        buff.append(pField);
        buff.append(" LIKE '");
        buff.append(StdSQLFormatter.getInstance().filterSqlConstant(pTo).trim());
        buff.append("%' )");
        return buff.toString();
    }//getSQLStringToExecuteDirectly

}//class
/*______________________________EOF_________________________________*/