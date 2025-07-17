/*______________________________BOF_________________________________*/
package es.altia.util.persistance.impl;

import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.daocommands.SQLDAOPKCommandAdapter;
import es.altia.util.jdbc.JdbcOperations;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author
 * @version $\Date$ $\Revision$
 *
 * PKAdapter for a single int primary key
 *
 * Important note: PrimaryKey must be a SingleLongPrimaryKey
 *
 */
public class SingleIntPKAdapter implements SQLDAOPKCommandAdapter {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    private String[] pkAttributeNames= new String[1];
    private String tableName = null;

    /*_______Operations_____________________________________________*/
    public SingleIntPKAdapter(String tableName, String pkAttributeName) {
        this.tableName = tableName;
        this.pkAttributeNames[0] = pkAttributeName;
    }//constructor

    public String pkGetSQLMainTableName() {
        return tableName;
    }//pkGetSQLMainTableName

    public String[] pkGetSQLPKAttributeNames() {
        return pkAttributeNames;
    }//pkGetSQLPKAttributeNames

    public int pkBind(PrimaryKey primaryKey, PreparedStatement preparedStatement, int i) throws SQLException {
        int result = i;
        SingleLongPrimaryKey concretePK = (SingleLongPrimaryKey) primaryKey;
        Integer pkAsInteger = null;
        if (concretePK!=null) pkAsInteger = new Integer((int) concretePK.getId());
        result = JdbcOperations.bindIntegerToStatement(pkAsInteger,preparedStatement,result);
        return result;
    }//pkBind

}//class
/*______________________________EOF_________________________________*/
