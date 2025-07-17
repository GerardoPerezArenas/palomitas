/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import es.altia.util.persistance.PrimaryKey;

import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;


/**
 */
public interface SQLDAOBlobCommandAdapter extends SQLDAOPKCommandAdapter {

    public String blGetSQLBlobAttributeName();

//    public void setBlob(byte[] blob)
//        throws SQLException;

//    public byte[] getBlob()
//        throws SQLException;
}//interface

/*______________________________EOF_________________________________*/