/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.mysql;

import es.altia.util.persistance.daocommands.LimitCriteria;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * @author
 * @version $\Revision$ $\Date$
 */
public class MySqlLimitCriteria implements LimitCriteria {

    private static final String ID = "MySqlLimit";

    private int pStartIndex = -1;
    private int pCount = -1;

    public String getUniqueId() {
        return ID;
    }//getUniqueId

    public boolean isSupported() {
        return true;
    }//isSupported

    public void setStartIndex(int newValue) {
        pStartIndex = newValue;
    }//setStartIndex

    public void setCount(int newValue) {
        pCount = newValue;
    }//setCount

    public String toSQLString() {
        return getSQLStringToExecuteDirectly();
    }//toSQLString

    public String getSQLStringToPrepare() {
        return " LIMIT ?, ? ";
    }//getSQLStringToPrepare

    public String getSQLStringToExecuteDirectly() {
        if (pCount>=0) {
            final StringBuffer buff = new StringBuffer(" LIMIT ");
            if (pStartIndex>=0) {
                buff.append(pStartIndex).append(", ");
            }//if
            buff.append(pCount);
            buff.append(" ");
            return buff.toString();
        } else {
            return "";
        }//if
    }//getSQLStringToExecuteDirectly

    public int bind(PreparedStatement preparedStatement, int i) throws SQLException {
        int result = i;
        preparedStatement.setInt(result++,pStartIndex);
        preparedStatement.setInt(result++,pCount);
        return result;
    }//bind

}//class
/*______________________________EOF_________________________________*/