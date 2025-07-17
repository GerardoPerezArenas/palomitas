/*______________________________BOF_________________________________*/
package es.altia.util.persistance.searchcriterias.stdsql;

import java.sql.Connection;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Locale;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.util.exceptions.*;
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import es.altia.util.persistance.*;
import es.altia.util.persistance.exceptions.*;
import es.altia.util.jdbc.*;


/**
 * @author
 */
public class StringFieldAlmostEqualSearchCriteria extends SearchCriteria {
        private static final int ID = 22;
        private String pField = null;
        private String pTo = null;

        public StringFieldAlmostEqualSearchCriteria(String field) {
            this.pField = field;
        }//constructor

        public StringFieldAlmostEqualSearchCriteria(String field, String to) {
            this.pField = field;
            setTo(to);
        }//constructor

        public String getTo() {
            return pTo;
        }//getTo

        public void setTo(String to) {
            this.pTo = to.trim().toLowerCase();
        }//setTo

        public int bind(PreparedStatement preparedStatement, int i)
         throws SQLException {
            int result = i;

            preparedStatement.setInt(result++, pTo.length());
            preparedStatement.setString(result++, pTo.substring(0,(pTo.length()/2))+"%");
            return result;
        }//bind

        public String toSQLString() {
            return " ( ABS( LENGTH("+pField+") - ? ) <= 1 ) AND " +
                    "( "+pField+" LIKE ? ) ";
        }//toSQLString

        public int getId() {
            return ID;
        }//getId
        public String toString() {
            return toSQLString();
        }//toString
}//class
/*______________________________EOF_________________________________*/