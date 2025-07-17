/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.persistance.*;
import es.altia.util.persistance.exceptions.*;
import es.altia.util.persistance.searchcriterias.*;
import es.altia.util.exceptions.*;

import java.sql.Connection;


/**
 * @author
 */
public interface SQLDAOUpdateCommand extends SQLDAOCommand {

    public void update(Connection connection, PersistentObject theVO)
        throws InstanceNotFoundException, StaleUpdateException, InternalErrorException;

}//interface

/*______________________________EOF_________________________________*/