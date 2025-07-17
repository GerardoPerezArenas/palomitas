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
 * Important note: PrimaryKey must be a LongPairPrimaryKey
 *
 */
public class IntPairPKAdapter implements SQLDAOPKCommandAdapter {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    private String[] pkAttributeNames= null;
    private String tableName = null;

    /*_______Operations_____________________________________________*/
    public IntPairPKAdapter(String tableName, String[] pkAttributeName) {
        this.tableName = tableName;
        this.pkAttributeNames = pkAttributeName;
    }//constructor

    public String pkGetSQLMainTableName() {
        return tableName;
    }//pkGetSQLMainTableName

    public String[] pkGetSQLPKAttributeNames() {
        return pkAttributeNames;
    }//pkGetSQLPKAttributeNames

    public int pkBind(PrimaryKey primaryKey, PreparedStatement preparedStatement, int i) throws SQLException {
        int result = i;
        LongPairPrimaryKey concretePK = (LongPairPrimaryKey) primaryKey;
        Integer pk1AsInteger = null;
        Integer pk2AsInteger = null;
        if (concretePK!=null) pk1AsInteger = new Integer((int) concretePK.getId());
        if (concretePK!=null) pk2AsInteger = new Integer((int) concretePK.getSecondId());
        result = JdbcOperations.bindIntegerToStatement(pk1AsInteger,preparedStatement,result);
        result = JdbcOperations.bindIntegerToStatement(pk2AsInteger,preparedStatement,result);
        return result;
    }//pkBind

}//class
/*______________________________EOF_________________________________*/
