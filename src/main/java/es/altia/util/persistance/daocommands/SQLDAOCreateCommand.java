/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.exceptions.DuplicateInstanceException;

import java.sql.Connection;


/**
 */
public interface SQLDAOCreateCommand extends SQLDAOCommand {

    public PersistentObject create(Connection connection, PersistentObject theVO)
        throws DuplicateInstanceException, InternalErrorException;

    public PersistentObject[] create(Connection connection, PersistentObject[] theVO)
        throws DuplicateInstanceException, InternalErrorException;

}//interface
/*______________________________EOF_________________________________*/