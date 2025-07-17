/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.oracle;

import es.altia.util.commons.DateOperations;
import es.altia.util.persistance.daocommands.SQLFormatter;
import es.altia.util.persistance.daocommands.stdsql.StdSQLFormatter;

import java.util.Calendar;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class OracleSQLFormatter extends StdSQLFormatter implements SQLFormatter {
    /*_______Constants______________________________________________*/
    private static final SQLFormatter SINGLETON = new OracleSQLFormatter();
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String ORACLE_DATE_FORMAT = "yyyy-mm-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
    public static final String ORACLE_DATETIME_FORMAT = "yyyy-mm-dd hh24:mi:ss";

    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/
    private OracleSQLFormatter() {}

    public static SQLFormatter getInstance() {
        return SINGLETON;
    }//getInstance

    public String formatAsDate(Calendar value) {
        return new StringBuffer("to_date('").append(DateOperations.toString(value,DATE_FORMAT)).append("','").append(ORACLE_DATE_FORMAT).append("')").toString();
    }

    public String formatAsDatetime(Calendar value) {
        return new StringBuffer("to_date('").append(DateOperations.toString(value,DATETIME_FORMAT)).append("','").append(ORACLE_DATETIME_FORMAT).append("')").toString();
    }

    public String dateCurrentDate() {
        return "TRUNC(SYSDATE)";
    }

    public String dateCurrentTime() {
        return "SYSDATE";
    }

    public String dateCurrentTimestamp() {
        return "SYSTIMESTAMP";
    }

}//class

/*______________________________EOF_________________________________*/
