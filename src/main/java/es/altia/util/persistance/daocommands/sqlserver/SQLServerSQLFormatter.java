/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.sqlserver;

import es.altia.util.commons.DateOperations;
import es.altia.util.persistance.daocommands.SQLFormatter;
import es.altia.util.persistance.daocommands.stdsql.StdSQLFormatter;

import java.util.Calendar;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class SQLServerSQLFormatter extends StdSQLFormatter implements SQLFormatter {
    /*_______Constants______________________________________________*/
    private static final SQLFormatter SINGLETON = new SQLServerSQLFormatter();
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String SQLSERVER_DATE_FORMAT = "103";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
    public static final String SQLSERVER_DATETIME_FORMAT = "20";

    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/
    private SQLServerSQLFormatter() {}

    public static SQLFormatter getInstance() {
        return SINGLETON;
    }//getInstance

    public String formatAsDate(Calendar value) {
        return new StringBuffer("CONVERT(datetime,'").append(DateOperations.toString(value,DATE_FORMAT)).append("',").append(SQLSERVER_DATE_FORMAT).append(")").toString();
    }

    public String formatAsDatetime(Calendar value) {
        return new StringBuffer("CONVERT(datetime,'").append(DateOperations.toString(value,DATETIME_FORMAT)).append("',").append(SQLSERVER_DATETIME_FORMAT).append(")").toString();
    }

    public String dateCurrentDate() {
        return "CONVERT(varchar,GETDATE(),103)";
    }

    public String dateCurrentTime() {
        return "GETDATE()";
    }

    public String dateCurrentTimestamp() {
        return "SYSTIMESTAMP";
    }

}//class

/*______________________________EOF_________________________________*/
