/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.persistance.*;
import es.altia.util.persistance.exceptions.*;
import es.altia.util.persistance.searchcriterias.*;
import es.altia.util.exceptions.*;

import java.sql.Connection;


/**
 */
public interface SQLDAODeleteByCriteriaCommand extends SQLDAOCommand {

    public int delete(Connection connection, SearchCriteria criteria)
        throws InternalErrorException;

}//interface

/*______________________________EOF_________________________________*/