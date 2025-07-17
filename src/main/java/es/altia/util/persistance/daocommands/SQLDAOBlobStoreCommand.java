/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.persistance.PrimaryKey;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.persistance.PrimaryKey;

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
public interface SQLDAOBlobStoreCommand {

    public void store(Connection connection, PrimaryKey pk, byte[] blob)
            throws InternalErrorException;

}//interface

/*______________________________EOF_________________________________*/