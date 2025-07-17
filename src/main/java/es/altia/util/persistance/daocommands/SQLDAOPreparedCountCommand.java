/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.persistance.*;
import es.altia.util.persistance.exceptions.*;
import es.altia.util.persistance.searchcriterias.*;
import es.altia.util.exceptions.*;

import java.sql.Connection;


/**
 */
public interface SQLDAOPreparedCountCommand extends SQLDAOCommand {

    public long count()
        throws InternalErrorException;

    public void free();
}//interface

/*______________________________EOF_________________________________*/