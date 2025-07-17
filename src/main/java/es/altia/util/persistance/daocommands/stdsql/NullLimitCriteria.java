/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.stdsql;

import es.altia.util.persistance.daocommands.LimitCriteria;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * @author
 * @version $\Revision$ $\Date$
 */
public class NullLimitCriteria implements LimitCriteria {

    private static final String ID = "NullLimit";

    private int pStartIndex = -1;
    private int pCount = -1;

    public String getUniqueId() {
        return ID;
    }//getUniqueId

    public boolean isSupported() {
        return false;
    }//isSupported

    public void setStartIndex(int newValue) {
        pStartIndex = newValue;
    }//setStartIndex

    public void setCount(int newValue) {
        pCount = newValue;
    }//setCount

    public String toSQLString() {
        return null;
    }//toSQLString

    public String getSQLStringToPrepare() {
        return toSQLString();
    }//getSQLStringToPrepare

    public String getSQLStringToExecuteDirectly() {
        return toSQLString();
    }//getSQLStringToExecuteDirectly

    public int bind(PreparedStatement preparedStatement, int i) throws SQLException {
        throw new SQLException("NullLimitCriteria: Not supported");
    }//bind

}//class

/*______________________________EOF_________________________________*/