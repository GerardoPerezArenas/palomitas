/*______________________________BOF_________________________________*/
package es.altia.util.persistance.searchcriterias;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class OrCriteria extends SearchCriteria {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    private SearchCriteria left = null;
    private SearchCriteria right = null;
    private StringBuffer buff = new StringBuffer();

    /*_______Operations_____________________________________________*/
	public OrCriteria(SearchCriteria left, SearchCriteria right) {
		this.left = left;
		this.right = right;
	}//constructor

	public int bind(PreparedStatement preparedStatement, int i) 
	 throws SQLException {
		int result = i;

		result = left.bind(preparedStatement, result);
		result = right.bind(preparedStatement, result);
		return result;
	}//bind
	
	public String toSQLString() {
        final int buffLength = buff.length();
        if (buffLength>0) buff.delete(0, buffLength);
        buff.append("(");
        buff.append(left.toSQLString());
        buff.append(") OR (");
        buff.append(right.toSQLString());
        buff.append(")");
        return buff.toString();
	}//toSQLString

    public String getSQLStringToPrepare() {
        final int buffLength = buff.length();
        if (buffLength>0) buff.delete(0, buffLength);
        buff.append("(");
        buff.append(left.getSQLStringToPrepare());
        buff.append(") OR (");
        buff.append(right.getSQLStringToPrepare());
        buff.append(")");
        return buff.toString();
    }//getSQLStringToPrepare

    public String getSQLStringToExecuteDirectly() {
        final int buffLength = buff.length();
        if (buffLength>0) buff.delete(0, buffLength);
        buff.append("(");
        buff.append(left.getSQLStringToExecuteDirectly());
        buff.append(") OR (");
        buff.append(right.getSQLStringToExecuteDirectly());
        buff.append(")");
        return buff.toString();
    }//getSQLStringToExecuteDirectly

}//class
/*______________________________EOF_________________________________*/
