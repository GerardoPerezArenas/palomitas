/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.exceptions.InternalErrorException;

import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
import java.sql.Connection;


/**
 */
public interface SQLDAOBlobRetrieveCommand {
    
    public byte[] retrieve(Connection connection, PrimaryKey primaryKey)
        throws InstanceNotFoundException, InternalErrorException;

}//interface

/*______________________________EOF_________________________________*/