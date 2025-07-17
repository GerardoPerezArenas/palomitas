/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands;

import java.util.Calendar;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public interface SQLFormatter {
    public static final int TYPE_STRING = 1;
    public static final int TYPE_INTEGER = 2;
    public static final int TYPE_LONG = 3;
    public static final int TYPE_CALENDAR = 4;
    public static final int TYPE_CALENDAR_DATE = 40;
    public static final int TYPE_CALENDAR_TIME = 41;
    public static final int TYPE_CALENDAR_DATETIME = 42;
    public static final int TYPE_CALENDAR_TIMESTAMP = 43;
    public static final int TYPE_TEXT = 5;

    /*_______Operations_____________________________________________*/
    public String formatValue(Object value, int type);

    public String formatValue(Number value);
    public String formatValue(String value);
    public String formatNull(int type);
    public String formatAsTimestamp(Calendar value);
    public String formatAsDatetime(Calendar value);
    public String formatAsDate(Calendar value);
    public String formatAsTime(Calendar value);

    public String filterSqlConstant(String sqlConstant);

    public String mathAbs(String value);
    public String mathRound(String value);
    public String mathTrunc(String value);
    public String strLength(String value);
    public String strLowercase(String value);
    public String strUppercase(String value);
    public String strConcatenate(String value, String other);
    public String strSubstring(String value, int startIndex, int count);
    public String dateCurrentDate();
    public String dateCurrentTime();
    public String dateCurrentTimestamp();

}//class
/*______________________________EOF_________________________________*/
