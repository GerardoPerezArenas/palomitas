/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.persistance.*;
import es.altia.util.persistance.exceptions.*;
import es.altia.util.persistance.searchcriterias.*;
import es.altia.util.exceptions.*;

import java.sql.Connection;


/**
 */
public interface SQLDAORetrieveIntArrayPropertyCommand extends SQLDAOCommand {

    public int[] retrieveIntArray(Connection connection, PrimaryKey primaryKey)
        throws InternalErrorException;

}//interface

/*______________________________EOF_________________________________*/