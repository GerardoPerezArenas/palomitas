/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.exceptions.InternalErrorException;

import java.sql.Connection;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public interface SQLDAOSequenceCommand extends SQLDAOCommand {
    /*_______Operations_____________________________________________*/
    public long nextValue(Connection connection, String sequenceName)
        throws InternalErrorException;

}//class
/*______________________________EOF_________________________________*/
