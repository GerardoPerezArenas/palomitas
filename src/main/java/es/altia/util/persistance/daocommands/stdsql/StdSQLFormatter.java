/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.stdsql;

import es.altia.util.commons.DateOperations;
import es.altia.util.commons.StringOperations;
import es.altia.util.persistance.daocommands.SQLFormatter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Calendar;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class StdSQLFormatter implements SQLFormatter {
    /*_______Constants______________________________________________*/
    private static final SQLFormatter SINGLETON = new StdSQLFormatter();
    public static final String NULL = "NULL";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSS";

    private static final String SQL_CONSTANT_SECURE_CHARS = "abcdefghijklmnñopqrstuvwxyz0123456789ABCDEFGHIJKLMNÑOPQRSTUVWXYZáéíóúÁÉÍÓÚªº,.:";


    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/
    protected StdSQLFormatter() {}

    public static SQLFormatter getInstance() {
        return SINGLETON;
    }//getInstance

    public String formatValue(Object value, int type) {
        final String result;
        if (value == null)
            result = formatNull(type);
        else {
            switch (type) {
                case (TYPE_CALENDAR_DATE): {
                    final Calendar c = (Calendar) value;
                    result = formatAsDate(c);
                    break;
                }//case
                case (TYPE_CALENDAR_TIME): {
                    final Calendar c = (Calendar) value;
                    result = formatAsTime(c);
                    break;
                }//case
                case (TYPE_CALENDAR_DATETIME): {
                    final Calendar c = (Calendar) value;
                    result = formatAsDatetime(c);
                    break;
                }//case
                case (TYPE_CALENDAR_TIMESTAMP): {
                    final Calendar c = (Calendar) value;
                    result = formatAsTimestamp(c);
                    break;
                }//case
                case (TYPE_STRING): {
                    result = formatValue(value.toString());
                    break;
                }//case
                case (TYPE_INTEGER):
                case (TYPE_LONG):
                default: {
                    final Number n = (Number) value;
                    result = formatValue(n);
                }
            }//switch
        }//if
        return result;
    }//formatValue

    public String formatValue(Number value) {
        return value.toString();
    }

    public String formatValue(String value) {
        return (value==null)?NULL:StringOperations.wrap(filterSqlConstant(value),"'","'");
    }

    public String formatNull(int type) {
        return NULL;
    }//formatNull

    public String formatAsTimestamp(Calendar value) {
        return StringOperations.wrap(DateOperations.toString(value,TIMESTAMP_FORMAT),"'","'");
    }

    public String formatAsDatetime(Calendar value) {
        return StringOperations.wrap(DateOperations.toString(value,DATETIME_FORMAT),"'","'");
    }

    public String formatAsDate(Calendar value) {
        return StringOperations.wrap(DateOperations.toString(value,DATE_FORMAT),"'","'");
    }

    public String formatAsTime(Calendar value) {
        return StringOperations.wrap(DateOperations.toString(value,TIME_FORMAT),"'","'");
    }


    public String filterSqlConstant(String sqlConstant) {
        if (StringUtils.containsOnly(sqlConstant,SQL_CONSTANT_SECURE_CHARS)) {
            return sqlConstant;
        } else {
            String result=sqlConstant;

            /* Remove comments delimiters */
            if (sqlConstant.indexOf("/*")>=0)
                result = StringUtils.replace(result,"/*","");
            if (sqlConstant.indexOf("*/")>=0)
                result = StringUtils.replace(result,"*/","");
            if (sqlConstant.indexOf("--")>=0)
                result = StringUtils.replace(result,"--","");
            

            /* Commons-lang filtering ('->'') */
            return StringEscapeUtils.escapeSql(result);
        }//if
    }//filterSqlConstant

    public String mathAbs(String value) {
        return StringOperations.wrap(value,"ABS(",")");
    }

    public String mathRound(String value) {
        return StringOperations.wrap(value,"ROUND(",")");
    }

    public String mathTrunc(String value) {
        return StringOperations.wrap(value,"TRUNC(",")");
    }

    public String strLength(String value) {
        return StringOperations.wrap(value,"LENGTH(",")");
    }

    public String strLowercase(String value) {
        return StringOperations.wrap(value,"LOWER(",")");
    }

    public String strUppercase(String value) {
        return StringOperations.wrap(value,"UPPER(",")");
    }

    public String strConcatenate(String value, String other) {
        return new StringBuffer("CONCAT(").append(value).append(", ").append(other).append(")").toString();
    }

    public String strSubstring(String value, int startIndex, int count) {
        return new StringBuffer("SUBSTR(").append(value).
                append(", ").append(startIndex).
                append(", ").append(count).append(")").toString();
    }

    public String dateCurrentDate() {
        return "CURRENT_DATE()";
    }

    public String dateCurrentTime() {
        return "CURRENT_TIME()";
    }

    public String dateCurrentTimestamp() {
        return "CURRENT_TIMESTAMP()";
    }

}//class
/*______________________________EOF_________________________________*/
