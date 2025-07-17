/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.persistance.*;
import es.altia.util.persistance.exceptions.*;
import es.altia.util.persistance.searchcriterias.*;
import es.altia.util.exceptions.*;

import java.sql.Connection;


/**
 */
public interface SQLDAORetrieveCommand extends SQLDAOCommand {

    public static final String COUNTER = "COUNTER";
    public static final String PERSISTENT_OBJECT = "PERSISTENT_OBJECT";

    public PersistentObject retrieve(Connection connection, PrimaryKey primaryKey)
        throws InstanceNotFoundException, InternalErrorException;

}//interface

/*______________________________EOF_________________________________*/