/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.persistance.*;
import es.altia.util.persistance.exceptions.*;
import es.altia.util.persistance.searchcriterias.*;
import es.altia.util.exceptions.*;

import java.sql.Connection;
import java.util.List;


/**
 * @author 
 */
public interface SQLDAOSearchCommand extends SQLDAOCommand {

    public List search(Connection connection, SearchCriteria whereClause, OrderCriteria orderClause,
                                Integer startIndex, Integer count)
        throws InternalErrorException;

}//interface

/*______________________________EOF_________________________________*/