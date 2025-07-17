/*______________________________BOF_________________________________*/
package es.altia.util.commons;

import java.util.ArrayList;

/**
 * @version $\Date$ $\Revision$
 */
class Formatter {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    private int width;
    private int precision;
    private String pre;
    private String post;
    private boolean leadingZeroes;
    private boolean showPlus;
    private boolean alternate;
    private boolean showSpace;
    private boolean leftAlign;
    private char fmt;				// one of cdeEfgGiosxXos
    private boolean countSignInLen;

    /*_______Operations_____________________________________________*/

    /**
     * Formats a number in a printf format, like C
     *
     * @param s      the format string following printf format string
     *               The string has a prefix, a format code and a suffix. The prefix and suffix
     *               become part of the formatted output. The format code directs the
     *               formatting of the (single) parameter to be formatted. The code has the
     *               following structure
     *               <ul>
     *               <li> a % (required)
     *               <li> a modifier (optional)
     *               <dl>
     *               <dt> + <dd> forces display of + for positive numbers
     *               <dt> ~ <dd> do not count leading + or - in length
     *               <dt> 0 <dd> show leading zeroes
     *               <dt> - <dd> align left in the field
     *               <dt> space <dd> prepend a space in front of positive numbers
     *               <dt> # <dd> use "alternate" format. Add 0 or 0x for octal or hexadecimal numbers.
     *               Don't suppress trailing zeroes in general floating point format.
     *               </dl>
     *               <li> an integer denoting field width (optional)
     *               <li> a period followed by an integer denoting precision (optional)
     *               <li> a format descriptor (required)
     *               <dl>
     *               <dt>f <dd> floating point number in fixed format
     *               <dt>e, E <dd> floating point number in exponential notation (scientific format).
     *               The E format results in an uppercase E for the exponent (1.14130E+003), the e
     *               format in a lowercase e.
     *               <dt>g, G <dd> floating point number in general format (fixed format for small
     *               numbers, exponential format for large numbers). Trailing zeroes are suppressed.
     *               The G format results in an uppercase E for the exponent (if any), the g format
     *               in a lowercase e.
     *               <dt>d, i <dd> signed long integer and integer in decimal
     *               <dt>u <dd> unsigned integer in decimal
     *               <dt>x <dd> unsigned integer in hexadecimal
     *               <dt>o <dd> unsigned integer in octal
     *               <dt>s <dd> string
     *               <dt>c <dd> character
     *               </dl>
     *               </ul>
     */
    public Formatter(String s) {
        width = 0;
        precision = -1;
        pre = "";
        post = "";
        leadingZeroes = false;
        showPlus = false;
        alternate = false;
        showSpace = false;
        leftAlign = false;
        countSignInLen = true;
        fmt = ' ';


        final int length = s.length();
        int parseState = 0;

        // 0 = prefix, 1 = flags, 2 = width, 3 = precision,
        // 4 = format, 5 = end
        int i = 0;

        while (parseState == 0) {
            if (i >= length) {
                parseState = 5;
            } else if (s.charAt(i) == '%') {
                if (i < length - 1) {
                    if (s.charAt(i + 1) == '%') {
                        pre = pre + '%';
                        i++;
                    } else {
                        parseState = 1;
                    }
                } else {
                    throw new java.lang.IllegalArgumentException();
                }
            } else {
                pre = pre + s.charAt(i);
            }
            i++;
        }

        while (parseState == 1) {
            if (i >= length) {
                parseState = 5;
            } else if (s.charAt(i) == ' ') {
                showSpace = true;
            } else if (s.charAt(i) == '-') {
                leftAlign = true;
            } else if (s.charAt(i) == '+') {
                showPlus = true;
            } else if (s.charAt(i) == '0') {
                leadingZeroes = true;
            } else if (s.charAt(i) == '#') {
                alternate = true;
            } else if (s.charAt(i) == '~') {
                countSignInLen = false;
            } else {
                parseState = 2;
                i--;
            }
            i++;
        }

        while (parseState == 2) {
            if (i >= length) {
                parseState = 5;
            } else if ('0' <= s.charAt(i) && s.charAt(i) <= '9') {
                width = width * 10 + s.charAt(i) - '0';
                i++;
            } else if (s.charAt(i) == '.') {
                parseState = 3;
                precision = 0;
                i++;
            } else {
                parseState = 4;
            }
        }

        while (parseState == 3) {
            if (i >= length) {
                parseState = 5;
            } else if ('0' <= s.charAt(i) && s.charAt(i) <= '9') {
                precision = precision * 10 + s.charAt(i) - '0';
                i++;
            } else {
                parseState = 4;
            }
        }

        if (parseState == 4) {
            if (i >= length) {
                parseState = 5;
            } else {
                fmt = s.charAt(i);
            }

            i++;
        }
        if (i < length) {
            post = s.substring(i, length);
        }
    }


    private String expFormat(double d) {
        String f = "";
        int e = 0;
        double dd = d;
        double factor = 1;

        if (d != 0) {
            while (dd > 10) {
                e++;
                factor /= 10;
                dd = dd / 10;
            }
            while (dd < 1) {
                e--;
                factor *= 10;
                dd = dd * 10;
            }
        }
        if ((fmt == 'g' || fmt == 'G') && e >= -4 && e < precision) {
            return fixedFormat(d);
        }

        d = d * factor;
        f = f + fixedFormat(d);

        if (fmt == 'e' || fmt == 'g') {
            f = f + "e";
        } else {
            f = f + "E";
        }

        String p = "000";

        if (e >= 0) {
            f = f + "+";
            p = p + e;
        } else {
            f = f + "-";
            p = p + (-e);
        }

        return f + p.substring(p.length() - 3, p.length());
    }

    private String fixedFormat(double d) {
        final boolean removeTrailing = (fmt == 'G' || fmt == 'g') && !alternate;

        // remove trailing zeroes and decimal point
        if (d > 0x7FFFFFFFFFFFFFFFL) {
            return expFormat(d);
        }
        if (precision == 0) {
            return (long) (d /*+ 0.5*/) + (removeTrailing ? "" : ".");	// no rounding
        }

        long whole = (long) d;
        final double fr = d - whole; // fractional part

        if (fr >= 1 || fr < 0) {
            return expFormat(d);
        }

        double factor = 1;
        String leadingZeroes = "";

        for (int i = 1; i <= precision && factor <= 0x7FFFFFFFFFFFFFFFL; i++) {
            factor *= 10;
            leadingZeroes = leadingZeroes + "0";
        }

        long l = (long) (factor * fr /*+ 0.5*/);						// no rounding

        if (l >= factor) {
            l = 0;
            whole++;
        }

        String z = leadingZeroes + l;
        z = "." + z.substring(z.length() - precision, z.length());

        if (removeTrailing) {
            int t = z.length() - 1;

            while (t >= 0 && z.charAt(t) == '0') {
                t--;
            }
            if (t >= 0 && z.charAt(t) == '.') {
                t--;
            }

            z = z.substring(0, t + 1);
        }

        return whole + z;
    }


    private String pad(String r) {
        final StringBuffer buff = new StringBuffer(pre);
        final String p = repeat(' ', width - r.length());

        if (leftAlign) {
            buff.append(r);
            buff.append(p);
        } else {
            buff.append(p);
            buff.append(r);
        }//if
        buff.append(post);
        return buff.toString();
    }

    private static long parseLong(String s, int base) {
        int i = 0;
        int sign = 1;
        long r = 0;

        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        if (i < s.length() && s.charAt(i) == '-') {
            sign = -1;
            i++;
        } else if (i < s.length() && s.charAt(i) == '+') {
            i++;
        }
        while (i < s.length()) {
            final char ch = s.charAt(i);

            if ('0' <= ch && ch < '0' + base) {
                r = r * base + ch - '0';
            } else if ('A' <= ch && ch < 'A' + base - 10) {
                r = r * base + ch - 'A' + 10;
            } else if ('a' <= ch && ch < 'a' + base - 10) {
                r = r * base + ch - 'a' + 10;
            } else {
                return r * sign;
            }

            i++;
        }

        return r * sign;
    }

    private static String repeat(char c, int n) {
        if (n <= 0) {
            return ("");
        }

        final StringBuffer s = new StringBuffer(n);
        for (int i = 0; i < n; i++)
            s.append(c);
        return s.toString();
    }

    private String sign(int s, String r) {
        String p = "";

        if (s < 0) {
            p = "-";
        } else if (s > 0) {
            if (showPlus) {
                p = "+";
            } else if (showSpace) {
                p = " ";
            }
        } else {
            if (fmt == 'o' && alternate && r.length() > 0 && r.charAt(0) != '0') {
                p = "0";
            } else if (fmt == 'x' && alternate) {
                p = "0x";
            } else if (fmt == 'X' && alternate) {
                p = "0X";
            }
        }

        int w = 0;

        if (leadingZeroes) {
            w = width;
        } else if ((fmt == 'u' || fmt == 'd' || fmt == 'i' || fmt == 'x' || fmt == 'X' || fmt == 'o') && precision > 0) {
            w = precision;
        }

        if (countSignInLen) {
            return p + repeat('0', w - p.length() - r.length()) + r;
        } else {
            return p + repeat('0', w - r.length()) + r;
        }
    }

    // ---------------------------------------------------------------- form methods

    /**
     * Formats a character into a string (like sprintf in C)
     *
     * @param c      the value to format
     *
     * @return the formatted string
     */
    public String form(char c) {
        if (fmt != 'c') {
            throw new java.lang.IllegalArgumentException();
        }

        final String r = "" + c;

        return pad(r);
    }
    public String form(Character c) {
        return form(c.charValue());
    }


    /**
     * Formats a double into a string (like sprintf in C)
     *
     * @param x      the number to format
     *
     * @return the formatted string
     */
    public String form(double x) {
        final String r;

        if (precision < 0) {
            precision = 6;
        }

        int s = 1;

        if (x < 0) {
            x = -x;
            s = -1;
        }
        if (fmt == 'f') {
            r = fixedFormat(x);
        } else if (fmt == 'e' || fmt == 'E' || fmt == 'g' || fmt == 'G') {
            r = expFormat(x);
        } else {
            throw new java.lang.IllegalArgumentException();
        }

        return pad(sign(s, r));
    }

    public String form(Double x) {
        return form(x.doubleValue());
    }
    public String form(Float x) {
        return form(x.doubleValue());
    }

    /**
     * Formats a long integer into a string (like sprintf in C)
     *
     * @param x      the number to format
     *
     * @return the formatted string
     */
    public String form(long x) {
        final String r;
        int s = 0;

        if (fmt == 'd') {
            if (x < 0) {
                r = ("" + x).substring(1);
                s = -1;
            } else {
                r = "" + x;
                s = 1;
            }
        } else if (fmt == 'i') {
            final int xx = (int) x;
            if (xx < 0) {
                r = ("" + xx).substring(1);
                s = -1;
            } else {
                r = "" + xx;
                s = 1;
            }
        } else if (fmt == 'u') {
            final long xl = x & 0x00000000FFFFFFFFL;
            r = "" + xl;
            s = 1;
        } else if (fmt == 'o') {
            r = convert(x, 3, "01234567");
        } else if (fmt == 'x') {
            r = convert(x, 4, "0123456789abcdef");
        } else if (fmt == 'X') {
            r = convert(x, 4, "0123456789ABCDEF");
        } else {
            throw new java.lang.IllegalArgumentException();
        }

        return pad(sign(s, r));
    }

    public String form(Long x) {
        return form(x.longValue());
    }

    /**
     * Formats an integer into a string (like sprintf in C)
     *
     * @param x      the number to format
     *
     * @return the formatted string
     */
    public String form(int x) {
        final String r;
        int s = 0;

        if (fmt == 'd' || fmt == 'i') {
            if (x < 0) {
                r = ("" + x).substring(1);
                s = -1;
            } else {
                r = "" + x;
                s = 1;
            }
        } else {
            final long xl = x & 0x00000000FFFFFFFFL;
            if (fmt == 'u') {
                r = "" + xl;
                s = 1;
            } else if (fmt == 'o') {
                r = convert(xl, 3, "01234567");
            } else if (fmt == 'x') {
                r = convert(xl, 4, "0123456789abcdef");
            } else if (fmt == 'X') {
                r = convert(xl, 4, "0123456789ABCDEF");
            } else {
                throw new java.lang.IllegalArgumentException();
            }
        }

        return pad(sign(s, r));
    }

    public String form(Integer x) {
        return form(x.intValue());
    }

    /**
     * Formats a string into a larger string (like sprintf in C)
     *
     * @param s      the value to format
     *
     * @return the formatted string
     */
    public String form(String s) {
        if (fmt != 's') {
            throw new java.lang.IllegalArgumentException();
        }
        if (precision >= 0 && precision < s.length()) {
            s = s.substring(0, precision);
        }

        return pad(s);
    }


    // ---------------------------------------------------------------- misc conversion

    /**
     * Converts a string of digits to an double
     *
     * @param s      a string
     *
     * @return double converted from String
     */
    public static double atof(String s) {
        int i = 0;
        int sign = 1;
        double r = 0;		// integer part
        double p = 1;		// exponent of fractional part
        int state = 0;		// 0 = int part, 1 = frac part

        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        if (i < s.length() && s.charAt(i) == '-') {
            sign = -1;
            i++;
        } else if (i < s.length() && s.charAt(i) == '+') {
            i++;
        }
        while (i < s.length()) {
            final char ch = s.charAt(i);

            if ('0' <= ch && ch <= '9') {
                if (state == 0) {
                    r = r * 10 + ch - '0';
                } else if (state == 1) {
                    p = p / 10;
                    r = r + p * (ch - '0');
                }
            } else if (ch == '.') {
                if (state == 0) {
                    state = 1;
                } else {
                    return sign * r;
                }
            } else if (ch == 'e' || ch == 'E') {
                final long e = (int) parseLong(s.substring(i + 1), 10);
                return sign * r * Math.pow(10, e);
            } else {
                return sign * r;
            }
            i++;
        }
        return sign * r;
    }

    /**
     * Converts a string of digits (decimal, octal or hex) to an integer
     *
     * @param s      a string
     *
     * @return the numeric value of the prefix of s representing a
     *         base 10 integer
     */
    public static int atoi(String s) {
        return (int) atol(s);
    }

    /**
     * Converts a string of digits (decimal, octal or hex) to a long integer
     *
     * @param s      a string
     *
     * @return the numeric value of the prefix of s representing a
     *         base 10 integer
     */
    public static long atol(String s) {
        int i = 0;

        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        if (i < s.length() && s.charAt(i) == '0') {
            if (i + 1 < s.length() &&
                    (s.charAt(i + 1) == 'x' || s.charAt(i + 1) == 'X')) {
                return parseLong(s.substring(i + 2), 16);
            } else {
                return parseLong(s, 8);
            }
        } else {
            return parseLong(s, 10);
        }
    }

    /**
     * Converts number to string
     *
     * @param x      value to convert
     * @param n      conversion base
     * @param d      string with characters for conversion.
     *
     * @return converted number as string
     */
    public static String convert(long x, int n, String d) {
        if (x == 0) {
            return "0";
        }

        String r = "";
        int m = 1 << n;
        m--;
        while (x != 0) {
            r = d.charAt((int) (x & m)) + r;
            x = x >>> n;
        }
        return r;
    }

    // ---------------------------------------------------------------- array sprintf

    /**
     * Splits input string on '%' and returns an array of substrings that can be
     * used for multiple scanf. Each substring has a one-char prefix that can
     * take one of two values:
     * '+' wich indicates that substring can be simply added to the resulting string
     * ' ' wich indicates that substring has to be additionally processed.
     *
     * @param s      Input string
     *
     * @return splited input string
     */
    static String[] split(String s) {
        final ArrayList list = new ArrayList();

        int lasti = 0;
        int i = s.indexOf("%");
        if (i == -1) {
            return new String[] {s};
        }
        if (i > 0) {
            list.add("+" + s.substring(0, i));
            lasti = i;
            i++;
            i = s.indexOf("%", i);
        } else if (i == 0) {
            i = s.indexOf("%", i + 1);
        }
        while (i != -1) {
            String ss = s.substring(lasti, i);
            if (ss.equals("%")) {
                lasti = i; i++;
                i = s.indexOf("%", i);
                if (i != -1) {
                    ss = s.substring(lasti, i);
                } else {
                    ss = s.substring(lasti);
                }
                list.add("+" + ss);
            } else {
                list.add(" " + ss);
            }
            if (i == -1) {
                lasti = i;
                break;
            }
            lasti = i;
            i++;
            i = s.indexOf("%", i);
        }
        if (lasti != -1) {
            list.add(" " + s.substring(lasti));
        }

        final String[] ret = new String[list.size()];
        for (i = 0; i < list.size(); i++) {
            ret[i] = (String) list.get(i);
        }
        return ret;
    }



    // ---------------------------------------------------------------- primitives array sprintf

    public static String sprintf(String s, int[] params) {
        if ((s == null) || (params == null)) {
            return s;
        }
        final StringBuffer result = new StringBuffer("");
        final String[] ss = split(s);
        int p = 0;
        for (int i = 0; i < ss.length; i++) {
            final char c = ss[i].charAt(0);
            final String t = ss[i].substring(1);
            if (c == '+') {
                result.append(t);
            } else {
                final int param = params[p];
                result.append(new Formatter(t).form(param));
                p++;
            }//if
        }//for
        return result.toString();
    }//sprintf

}//class
/*______________________________EOF_________________________________*/

