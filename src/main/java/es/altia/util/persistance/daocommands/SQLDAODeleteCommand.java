/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.persistance.*;
import es.altia.util.persistance.exceptions.*;
import es.altia.util.exceptions.*;

import java.sql.Connection;


/**
 */
public interface SQLDAODeleteCommand extends SQLDAOCommand {

    public void delete(Connection connection, PrimaryKey primaryKey)
            throws InstanceNotFoundException, InternalErrorException, IntegrityViolationAttemptedException;

    public void delete(Connection connection, PrimaryKey[] primaryKeys)
        throws InstanceNotFoundException, InternalErrorException, IntegrityViolationAttemptedException;

}//interface

/*______________________________EOF_________________________________*/