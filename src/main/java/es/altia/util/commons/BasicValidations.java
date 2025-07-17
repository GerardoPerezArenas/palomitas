/*______________________________BOF_________________________________*/
package es.altia.util.commons;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;


/**
 * @version $\Revision$ $\Date$
 */
public final class BasicValidations {
    /*_______Constants______________________________________________*/
    private static final char[] LONG_VALID_CHARS = {'0','1','2','3','4','5','6','7','8','9'};
    private static final char[] NUMBER_VALID_CHARS = {'0','1','2','3','4','5','6','7','8','9',',','.','+','-','E','e'};
            
    private BasicValidations(){}

    public static final boolean isDate(String item, String fmt) {
        if ( (!isEmpty(item)) && (fmt!=null) ) {
            final Calendar c = DateOperations.toCalendar(item, fmt);
            final String theSameDate = DateOperations.toString(c,fmt);
            return ( (c!=null) && (theSameDate!=null) && (theSameDate.equals(item)) );
        } else {
            return false;
        }//if
    }//isDate

    public static final boolean isDateBetween(String item, String fmt, Calendar lower, Calendar upper) {
        boolean result = isDate(item, fmt);
        if (result) {
            final Calendar c = DateOperations.toCalendar(item,fmt);
            if (lower!=null) result = (c.after(lower));
            if (upper!=null) result = result && (c.before(upper));
        }//if
        return result;
    }//isDateBetween

    public static final boolean isStringWithLengthBetween(String str, int minLength, int maxLength) {
        return (! isEmpty(str)) && (str.length()>=minLength) && (str.length()<=maxLength);
    }//isStringWithLengthBetween

    public static final boolean isCharIn(char c, char[] validValues) {
        Arrays.sort(validValues);
        return (Arrays.binarySearch(validValues,c) >= 0 );
    }//isCharIn

    public static final boolean isEmail(String item){
		return (!isEmpty(item)) && (GenericValidator.isEmail(item));
    }//isEmail

    public static final boolean isEmpty(String item) {
        return ( (item==null) || (item.trim().length() == 0) );
    }//isEmpty

    public static final boolean hasElements(Object[] a) {
        return ( (a!=null) && (a.length>0) );
    }//hasElements

    public static final boolean isChar(String item) {
        return ( (!isEmpty(item)) && (item.length()==1) );
    }//isChar

    public static final boolean isLong(String item) {
        if (isEmpty(item)) {
            return false;
        } else {
            boolean propertyValueIsCorrect = StringUtils.containsOnly(item, LONG_VALID_CHARS);
            if (propertyValueIsCorrect) {
                try {
                    Long.parseLong(item);
                } catch (Exception e) {
                    propertyValueIsCorrect = false;
                }//try-catch
            }//if

            return propertyValueIsCorrect;
        }//if
    }//isLong

    public static final boolean isNumber(String item) {
        if (isEmpty(item)) {
            return false;
        } else {
            boolean propertyValueIsCorrect = StringUtils.containsOnly(item, NUMBER_VALID_CHARS);
            if (propertyValueIsCorrect) {
                try {
                    Double.parseDouble(item);
                } catch (Exception e) {
                    propertyValueIsCorrect = false;
                }//try-catch
            }//if

            return propertyValueIsCorrect;
        }//if
    }//isNumber

    public static final boolean isNumberBetween(String number, long min, long max) {
        boolean result = isNumber(number);
        if (result) {
            try {
                final NumberFormat numberFormatter = NumberFormat.getNumberInstance();
                final Number propertyValueAsDouble = numberFormatter.parse(number);
                final long propertyValueAsLong = propertyValueAsDouble.longValue();
                result = (propertyValueAsLong >= min) && (propertyValueAsLong <= max);
            } catch (Exception e) {
                result = false;
            }//try-catch
        }//if
        return result;
    }//isNumberBetween

    /**
     * Checks if date is valid.
     *
     * @param year   year to check
     * @param month  month to check
     * @param day    day to check
     *
     * @return true if date is valid, otherwise false
     */
    public static boolean isValidDate(int year, int month, int day) {
        if ((month < 1) || (month > 12)) return false;
        int ml = DateOperations.getMonthLength(year, month);
        if ((day < 1) || (day > ml)) return false;
        return true;
    }//isValidDate

    /**
     * Checks if time is valid.
     *
     * @param hour   hour to check
     * @param minute minute to check
     * @param second second to check
     *
     * @return true if time is valid, otherwise false
     */
    public static final boolean isValidTime(int hour, int minute, double second) {
        if ((hour < 0) || (hour >= 24)) return false;
        if ((minute < 0) || (minute >= 60)) return false;
        if ((second < 0) || (second >= 60)) return false;
        return true;
    }//isValidTime

    /**
     */
    public static final boolean isDouble(String item, Locale l, int minDecimals, int maxDecimals) {
        if (BasicValidations.isEmpty(item))
            return false;
        else if (!StringUtils.containsOnly(item, NUMBER_VALID_CHARS))
            return false;
        else {
            final Locale theLocale = (l!=null)?l:Locale.getDefault();
            final DecimalFormatSymbols dfs = new DecimalFormatSymbols(theLocale);
            final char decimalSeparator = dfs.getDecimalSeparator();
            if ( StringOperations.countOcurrences(item, decimalSeparator) > 1)
                return false;
            else {
                final NumberFormat numberFormatter = NumberFormat.getNumberInstance(theLocale);
                numberFormatter.setMinimumFractionDigits(minDecimals);
                numberFormatter.setMaximumFractionDigits(maxDecimals);
                try {
                    final Number theNumberParsed = numberFormatter.parse(item);
                    return (theNumberParsed!=null);
                } catch (ParseException e) {
                    return false;
                }//try-catch
            }//if
        }//if
    }//isDouble


	public static boolean isCheckedException(Throwable ex) {
		return (ex instanceof Exception) && (!(ex instanceof RuntimeException));
	}


}//class
/*______________________________EOF_________________________________*/