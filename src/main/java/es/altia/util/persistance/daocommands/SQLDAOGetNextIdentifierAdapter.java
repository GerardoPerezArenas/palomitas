/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.PrimaryKey;

import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 */
public interface SQLDAOGetNextIdentifierAdapter {
    public String niGetSQLTableName();
    public String[] niGetSQLPKAttributeNames();
    public String niGetSQLSequenceAttributeName();
    public long niGetStartValue();
    public int niGetBlockSize();
    public int niPkBind(PrimaryKey primaryKey, PreparedStatement preparedStatement, int i)
        throws SQLException;
}//interface

/*______________________________EOF_________________________________*/