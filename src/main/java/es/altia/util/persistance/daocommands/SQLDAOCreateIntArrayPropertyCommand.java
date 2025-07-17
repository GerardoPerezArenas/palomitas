/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.persistance.*;
import es.altia.util.persistance.exceptions.*;
import es.altia.util.persistance.searchcriterias.*;
import es.altia.util.exceptions.*;

import java.sql.Connection;


/**
 */
public interface SQLDAOCreateIntArrayPropertyCommand extends SQLDAOCommand {

    public void create(Connection connection, PrimaryKey thePK, int[] intArray)
        throws InternalErrorException;

}//interface

/*______________________________EOF_________________________________*/