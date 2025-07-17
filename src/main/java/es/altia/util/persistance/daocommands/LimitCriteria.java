/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * @version $\Revision$ $\Date$
 */
public interface LimitCriteria {
    /**
     * @deprecated Not used... and should not be used because it will be removed soon 
     * @return an unique id for the class
     */
    public String getUniqueId();

    public boolean isSupported();

    public void setStartIndex(int newValue);

    public void setCount(int newValue);

    public int bind(PreparedStatement preparedStatement, int i)
        throws SQLException;

    public String toSQLString();

    public String getSQLStringToPrepare();

    public String getSQLStringToExecuteDirectly();

    public String toString();

}//interface

/*______________________________EOF_________________________________*/