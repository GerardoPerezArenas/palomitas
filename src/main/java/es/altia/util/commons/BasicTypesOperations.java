/*______________________________BOF_________________________________*/
package es.altia.util.commons;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;


/**
 * @version $\Revision$ $\Date$
 */
public final class BasicTypesOperations {

    public static final int TRUE  = 1;
    public static final int FALSE = 0;

    private BasicTypesOperations(){}

    public static final String safeToString(Object obj, String defaultValue) {
        return (obj!=null) ? obj.toString() : defaultValue;
    }//toString

    public static final String toString(int[] items, String separator) {
        if (items!=null) {
            final StringBuffer buff = new StringBuffer();
            final int l = items.length;
            if (l > 0) {
                for (int i=0; i < (l-1) ; i++) {
                    buff.append(items[i]);
                    buff.append(separator);
                }//for
                buff.append(items[l-1]);
            }//if
            return buff.toString();
        } else {
            return null;
        }//if
    }//toString

    public static final String toString(long[] items, String separator) {
        if (items!=null) {
            final StringBuffer buff = new StringBuffer();
            final int l = items.length;
            if (l > 0) {
                for (int i=0; i < (l-1) ; i++) {
                    buff.append(items[i]);
                    buff.append(separator);
                }//for
                buff.append(items[l-1]);
            }//if
            return buff.toString();
        } else {
            return null;
        }//if
    }//toString

    public static final String toString(String[] items, String separator) {
        if (items!=null) {
            final StringBuffer buff = new StringBuffer();
            final int l = items.length;
            if (l > 0) {
                for (int i=0; i < (l-1) ; i++) {
                    buff.append(items[i]);
                    buff.append(separator);
                }//for
                buff.append(items[l-1]);
            }//if
            return buff.toString();
        } else {
            return null;
        }//if
    }//toString

    public static final String[] toStringArray(String items, String separator) {
        if (items!=null) {
            final StringTokenizer stringTokenizer = new StringTokenizer(items,separator,false);
            final int tokensCount = stringTokenizer.countTokens();
            final String[] result = new String[tokensCount];
            for (int i=0;( (stringTokenizer.hasMoreTokens()) && (i<tokensCount) );i++)
                result[i]=stringTokenizer.nextToken();
            return result;
        } else {
            return new String[0];
        }//if
    }//toStringArray

    public static final int toInt(String item) {
        return Integer.parseInt(item);
    }//toInt

    public static final int toInt(boolean item) {
        return ((item)?(TRUE):(FALSE));
    }//toInt

    public static final int[] toIntArray(String items, String separator) {
        if (items!=null) {
            final StringTokenizer stringTokenizer = new StringTokenizer(items,separator,false);
            final int tokensCount = stringTokenizer.countTokens();
            final int[] result = new int[tokensCount];
            for (int i=0;( (stringTokenizer.hasMoreTokens()) && (i<tokensCount) );i++)
                result[i]=toInt(stringTokenizer.nextToken());
            return result;
        } else {
            return new int[0];
        }//if
    }//toIntArray

    public static final int[] toIntArray(Integer[] items) {
        if (items!=null) {
            final int[] result = new int[items.length];
            Integer item;
            for (int i = 0; i < items.length; i++) {
                item = items[i];
                result[i]=item.intValue();
            }//for
            return result;
        } else {
            return new int[0];
        }//if
    }//toIntArray

    public static final int[] toIntArrayFromIntegerCollection(Collection c) {
        if (c!=null) {
            Integer i;
            final int[] result = new int[c.size()];
            int count=0;
            for (Iterator iterator = c.iterator(); iterator.hasNext();) {
                i =  (Integer) iterator.next();
                if (i!=null) result[count++]=i.intValue();
            }//for
            return result;
        } else {
            return new int[0];
        }//if
    }//toIntArrayFromIntegerCollection

    public static final Integer[] toIntegerArrayFromIntegerCollection(Collection c) {
        if (c==null) return null;
        return (Integer[]) c.toArray(new Integer[c.size()]);
    }//toIntegerArrayFromIntegerCollection

    public static final Integer toInteger(String item) {
        try {
            return Integer.valueOf(item);
        } catch (Exception e) {
            return null;
        }//try-catch
    }//toInteger

    public static final Integer toInteger(Long item) {
        Integer result = null;
        if (item!=null) {
            result = new Integer(item.intValue());
        }//if
        return result;
    }//toInteger

    public static final long toLong(String item) {
        return Long.parseLong(item);
    }//toLong

    public static final Long toLongObject(String item) {
        if (BasicValidations.isEmpty(item)) return null;
        try {
            return Long.valueOf(item);
        } catch (NumberFormatException e) {
            return null;
        }//try-catch
    }//toLongObject

    public static final long[] toLongArray(String items, String separator) {
        if (items!=null) {
            final StringTokenizer stringTokenizer = new StringTokenizer(items,separator,false);
            final int tokensCount = stringTokenizer.countTokens();
            final long[] result = new long[tokensCount];
            for (int i=0;( (stringTokenizer.hasMoreTokens()) && (i<tokensCount) );i++)
                result[i]=toLong(stringTokenizer.nextToken());
            return result;
        } else {
            return new long[0];
        }//if
    }//toLongArray

    public static final long[] toLongArray(Long[] items) {
        if (items!=null) {
            final long[] result = new long[items.length];
            for (int i = 0; i < items.length; i++) {
                result[i]=items[i].longValue();
            }//for
            return result;
        } else {
            return new long[0];
        }//if
    }//toLongArray

    public static final long[] toLongArrayFromLongCollection(Collection c) {
        if (c!=null) {
            final long[] result = new long[c.size()];
            int count=0;
            for (Iterator iterator = c.iterator(); iterator.hasNext();) {
                final Long i =  (Long) iterator.next();
                if (i!=null) result[count++]=i.longValue();
            }//for
            return result;
        } else {
            return new long[0];
        }//if
    }//toLongArrayFromLongCollection

    public static final boolean toBoolean(int item) {
        return (item != FALSE);
    }//toBoolean

    public static final Integer[] toIntegerArray(int[] items) {
        if (items!=null) {
            final Integer[] result = new Integer[items.length];
            for (int i = 0; i < items.length; i++) {
                result[i]=new Integer(items[i]);
            }//for
            return result;
        } else {
            return new Integer[0];
        }//if
    }//toIntegerArray

    public static final char toChar(String item) {
        return (item.charAt(0));
    }//toChar

    public static final char toChar(String item, char defaultValue) {
        return ((item==null)||(item.length()==0))?defaultValue:(item.charAt(0));
    }//toChar

    public static final boolean equals(Object obj1, Object obj2) {
        return ( (obj1==obj2) || ( (obj1!=null) && (obj2!=null) && (obj1.equals(obj2)) ) );
    }//equals

    public static final Double toDouble(String item, Locale l, int minDecimals, int maxDecimals) {
        if (BasicValidations.isEmpty(item))
            return null;
        else {
            final Locale theLocale = (l!=null)?l:Locale.getDefault();
            final NumberFormat numberFormatter = NumberFormat.getNumberInstance(theLocale);
            numberFormatter.setMinimumFractionDigits(minDecimals);
            numberFormatter.setMaximumFractionDigits(maxDecimals);
            try {
                final Number theNumberParsed = numberFormatter.parse(item);
                return new Double(theNumberParsed.doubleValue());
            } catch (ParseException e) {
                return null;
            }//try-catch
        }//if
    }//toDouble

    /**
     * Convert a string based locale into a Locale Object
     * <br>
     * <br>Strings are formatted:
     * <br>
     * <br>language_contry_variant
     *
     **/
    public static Locale toLocale(String localeString) {
        if (localeString == null) return null;
        if (localeString.toLowerCase().equals("default")) return Locale.getDefault();
        int languageIndex = localeString.indexOf('_');
        if (languageIndex == -1) return null;
        int countryIndex = localeString.indexOf('_', languageIndex + 1);
        String country = null;
        if (countryIndex == -1) {
            if (localeString.length() > languageIndex) {
                country = localeString.substring(languageIndex + 1, localeString.length());
            } else {
                return null;
            }//if
        }//if
        int variantIndex = -1;
        if (countryIndex != -1)
            countryIndex = localeString.indexOf('_', countryIndex + 1);
        String language = localeString.substring(0, languageIndex);
        String variant = null;
        if (variantIndex != -1)
            variant = localeString.substring(variantIndex + 1, localeString.length());
        if (variant != null)
            return new Locale(language, country, variant);
        else
            return new Locale(language, country);
    }//toLocale

}//class
/*______________________________EOF_________________________________*/