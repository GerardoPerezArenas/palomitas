/*______________________________BOF_________________________________*/
package es.altia.util.persistance.searchcriterias;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author
 */
public class JoinCriteria extends SearchCriteria {
    private String pWhere = null;

    public JoinCriteria(String where) {
        this.pWhere = where;
    }//constructor

    public int bind(PreparedStatement preparedStatement, int i)
     throws SQLException {
        return i;
    }//bind

    public String toSQLString() {
        return pWhere;
    }//toSQLString

}//class
/*______________________________EOF_________________________________*/