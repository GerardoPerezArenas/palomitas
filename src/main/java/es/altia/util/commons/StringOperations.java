/*______________________________BOF_________________________________*/
package es.altia.util.commons;

import es.altia.util.collections.CollectionsFactory;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * @version $\Date$ $\Revision$
 */
public class StringOperations {
    private static final int[] EQUIV_A = new int[] {'A', 'Á','Â','À','Ä','Ã','Å'};
    private static final int[] EQUIV_E = new int[] {'E', 'É','Ê','È','Ë'};
    private static final int[] EQUIV_I = new int[] {'I', 'Í','Î','Ì','Ï'};
    private static final int[] EQUIV_O = new int[] {'O', 'Ó','Ô','Ò','Õ','Ö'};
    private static final int[] EQUIV_U = new int[] {'U', 'Ù','Ú','Û','Ü'};
    private static final int[] EQUIV_S = new int[] {'S', 'Š'};
    private static final int[] EQUIV_Y = new int[] {'Y', 'Ÿ','Ý'};
    private static final int[] EQUIV_Z = new int[] {'Z', 'Ž'};
    private static final int[] EQUIV_NH = new int[] {'N', 'Ñ'};

    private static final int[] EQUIV_a = new int[] {'a', 'à','á','â','ã','ä','å'};
    private static final int[] EQUIV_e = new int[] {'e', 'è','é','ê','ë'};
    private static final int[] EQUIV_i = new int[] {'i', 'ì','í','î','ï'};
    private static final int[] EQUIV_o = new int[] {'o', 'ò','ó','ô','õ','ö'};
    private static final int[] EQUIV_u = new int[] {'u', 'ù','ú','û','ü'};
    private static final int[] EQUIV_s = new int[] {'s', 'š'};
    private static final int[] EQUIV_y = new int[] {'y', 'ÿ','ý'};
    private static final int[] EQUIV_z = new int[] {'z', 'ž'};
    private static final int[] EQUIV_nh = new int[] {'n', 'ñ'};

    private static final int[][] DELATIN_EQUIVS = new int[][]{EQUIV_A, EQUIV_E, EQUIV_I, EQUIV_O, EQUIV_U, EQUIV_S, EQUIV_Y, EQUIV_Z, EQUIV_NH,
                                                                EQUIV_a, EQUIV_e, EQUIV_i, EQUIV_o, EQUIV_u, EQUIV_s, EQUIV_y, EQUIV_z, EQUIV_nh};


    /*_______Operations_____________________________________________*/
    private StringOperations() {}

    public static final int countOcurrences(String src, char c) {
        int result = 0;
        final int total = src.length();
        for (int i = 0; i < total; i++)
            if (src.charAt(i)==c) result++;
        return result;
    }//countOcurrences

    public static final String removeSubstringAtBeginning(String wholeStr, String subStrToRemove) {
        String result = wholeStr;
        if ( (result!=null) && (subStrToRemove!=null) && (result.startsWith(subStrToRemove)) )
                result = result.substring(subStrToRemove.length());
        return result;
    }//removeSubstringAtBeginning

    public static final String removeSubstringAtEnd(String wholeStr, String subStrToRemove) {
        String result = wholeStr;
        if ( (result!=null) && (subStrToRemove!=null) && (result.endsWith(subStrToRemove)) )
            result = result.substring(0,result.lastIndexOf(subStrToRemove));
        return result;
    }//removeSubstringAtEnd

    public static final String removeSubstringStartingWith(String wholeStr, String subStrToRemove) {
        String result = wholeStr;
        if ( (result!=null) && (subStrToRemove!=null) && (result.indexOf(subStrToRemove)>=0) )
            result = result.substring(0,result.indexOf(subStrToRemove));
        return result;
    }//removeSubstringStartingWith

    public static final String removeSubstringsBetween(String wholeStr, String before, String after) {
        StringBuffer result = new StringBuffer();
        String remainingStr = wholeStr;
        int idxBegin;
        boolean stop = false;
        do {
            idxBegin = StringUtils.indexOf(remainingStr,before);
            if (idxBegin>=0) {
                int idxEnd = StringUtils.indexOf(remainingStr,after,idxBegin);
                if (idxEnd>=0) {
                    result.append(remainingStr.substring(0,idxBegin));
                    int newIdx = idxEnd + after.length();
                    if ( (newIdx>=0) && (newIdx<remainingStr.length()) )
                        remainingStr = remainingStr.substring(newIdx);
                    else
                        remainingStr = "";
                } else {
                    stop = true;
                }//if
            } else {
                stop = true;
            }//if
        } while (!stop);
        result.append(remainingStr);
        return result.toString();
    }//removeSubstrings

    public static final String prettyTrim(String str, int maxLength) {
        if ( (str==null) || ( (str.length()<=maxLength)) )
            return str;
        else {
            final String result;
            if (maxLength <= 3 ) {
                result = "...";
            } else {
                result = str.substring(0,maxLength-4);
                return result+"...";
            }//if
            return result;
        }//if
    }//prettyTrim

    public static final int indexOfIgnoreCase(String src, String subS) {
        return indexOfIgnoreCase(src, subS, 0);
    }

    public static final int indexOfIgnoreCase(String src, String subS, int startIndex) {
        final String sub = subS.toLowerCase();
        final int sublen = sub.length();
        final int total = src.length() - sublen + 1;
        for (int i = startIndex; i < total; i++) {
            int j = 0;
            while (j < sublen) {
                final char source = Character.toLowerCase(src.charAt(i + j));
                if (sub.charAt(j) != source) {
                    break;
                }
                j++;
            }
            if (j == sublen) {
                return i;
            }
        }
        return -1;
    }

    public static final String substringAfterIgnoreCase(String src, String subS) {
        if (src==null) return null;
        if ( (src.equals("")) || (subS==null) ) return "";
        final int idx = indexOfIgnoreCase(src,subS);
        final int cutIdx = idx + subS.length();
        if ( (idx>=0) && (cutIdx < src.length()-1) )
            return src.substring(cutIdx);
        else
            return "";
    }//substringAfterIgnoreCase

    public static final String removeAllCharsBut(String src, String acceptedChars) {
        final StringBuffer result = new StringBuffer();
        if (!StringUtils.containsOnly(src,acceptedChars)) {
            final int total = src.length();
            for (int i = 0; i < total; i++) {
                final String sub = src.substring(i,i+1);
                if (acceptedChars.indexOf(sub)>=0) result.append(sub);
            }//for
        }//if
        return result.toString();
    }//removeAllCharsBut

    public static final int lastIndexOfIgnoreCase(String s, String subS) {
        return lastIndexOfIgnoreCase(s, subS, 0);
    }

    public static final int lastIndexOfIgnoreCase(String src, String subS, int startIndex) {
        final String sub = subS.toLowerCase();
        final int sublen = sub.length();
        final int total = src.length() - sublen;
        for (int i = total; i >= startIndex; i--) {
            int j = 0;
            while (j < sublen) {
                final char source = Character.toLowerCase(src.charAt(i + j));
                if (sub.charAt(j) != source) {
                    break;
                }
                j++;
            }
            if (j == sublen) {
                return i;
            }
        }
        return -1;
    }

    public static final boolean startsWithIgnoreCase(String src, String subS) {
        return startsWithIgnoreCase(src, subS, 0);
    }

    public static final boolean startsWithIgnoreCase(String src, String subS, int startIndex) {
        final String sub = subS.toLowerCase();
        final int sublen = sub.length();
        if (startIndex + sublen > src.length()) {
            return false;
        }
        int j = 0;
        int i = startIndex;
        while (j < sublen) {
            final char source = Character.toLowerCase(src.charAt(i));
            if (sub.charAt(j) != source) {
                return false;
            }
            j++; i++;
        }
        return true;
    }

    public static final boolean endsWithIgnoreCase(String src, String subS) {
        final String sub = subS.toLowerCase();
        final int sublen = sub.length();
        int j = 0;
        int i = src.length() - sublen;
        if (i < 0) {
            return false;
        }
        while (j < sublen) {
            final char source = Character.toLowerCase(src.charAt(i));
            if (sub.charAt(j) != source) {
                return false;
            }
            j++; i++;
        }
        return true;
    }

    public static final String substringBetweenIgnoringCase(String src, String before, String after) {
        final String emptyStr = "";
        if (BasicValidations.isEmpty(src)) return null;
        if (BasicValidations.isEmpty(before)) return emptyStr;
        if (BasicValidations.isEmpty(after)) return emptyStr;
        final int idxBefore = indexOfIgnoreCase(src,before);
        if (idxBefore<0) return emptyStr;
        final int idxRemaining = idxBefore+before.length();
        if (idxRemaining>=src.length()) return src;
        final String remaining = src.substring(idxRemaining);
        if (BasicValidations.isEmpty(remaining)) return emptyStr;
        final int idxAfter = indexOfIgnoreCase(remaining,after);
        if (idxAfter<0)
            return remaining;
        else
            return remaining.substring(0,idxAfter);
    }//substringBetweenIgnoringCase

    public static final String removeLeadingCharsUntilNumber(String str) {
        if (!BasicValidations.isEmpty(str)) {
            int firstNumericCharPos = -1;
            boolean stop = false;
            final int strLength = str.length();
            for (int i = 0; (i < strLength) && (!stop); i++ ) {
                final char c = str.charAt(i);
                if (Character.isDigit(c)) {
                    firstNumericCharPos = i;
                    stop = true;
                }//if
            }//for
            if (firstNumericCharPos>=0)
                return str.substring(firstNumericCharPos);
            else
                return "";
        } else {
            return "";
        }//if
    }//removeLeadingCharsUntilNumber

    public static final int[] indexesOfIgnoreCase(String src, String subS) {
        final List lst = CollectionsFactory.getInstance().newArrayList();
        boolean stop = false;
        int lastIdx = 0;
        do {
            final int idx = indexOfIgnoreCase(src,subS,lastIdx);
            if (idx>=0) {
                lst.add(new Integer(idx));
                if (idx>=src.length()-1)
                    stop = true;
                else
                    lastIdx = idx+1;
            } else {
                stop = true;
            }//if
        } while(!stop);
        return BasicTypesOperations.toIntArrayFromIntegerCollection(lst);
    }//indexesOfIgnoreCase

    public static final String removeNewLines(String s) {
        if (s!=null) {
            s = StringUtils.replaceChars(s,'\n',' ').trim();
            s = StringUtils.replaceChars(s,'\r',' ').trim();
            s = StringUtils.replaceChars(s,'\f',' ').trim();
        }//if
        return s;
    }//removeNewLines

    public static final int safeLength(String str) {
        if (str!=null)
            return str.length();
        else
            return 0;
    }//safeLength

    public static final int minLength(String[] strs) {
        if ( (strs!=null) && (strs.length>0) ) {
            int result = Integer.MAX_VALUE;
            for (int i = 0; i < strs.length; i++) {
                String str = strs[i];
                final int strLen = safeLength(str);
                if (strLen<result) result = strLen;
            }//for
            return result;
        } else
            return 0;
    }//minLength

    public static final void appendRepeatedChar(char c, int times, StringBuffer buff) {
        for (int i = 0; i < times; i++) buff.append(c);
    }//appendRepeatedChar

    public static final String deLatin(String str) {
        StringBuffer buff = new StringBuffer();
        int l = str.length();
        for (int i=0;i<l;i++) {
            buff.append(deLatin(str.charAt(i)));
        }//for
        return buff.toString();
    }//deLatin

    public static final char deLatin(char c) {
        int code = c;
        if ( (code>=32) && (code<=126) ) return c;
        for (int i=0; (i<DELATIN_EQUIVS.length); i++) {
            if (ArrayUtils.contains(DELATIN_EQUIVS[i],c)) {
                return (char) DELATIN_EQUIVS[i][0];
            }//if
        }//for
        return c;
    }//deLatin

    public static final String wrap(String str, String left, String right) {
        return new StringBuffer(left).append(str).append(right).toString();
    }//wrap


    // ---------------------------------------------------------------- sprintf
    public static final String sprintf(String fmt, char x) {
        return new Formatter(fmt).form(x);
    }
    public static final String sprintf(String fmt, Character x) {
        return new Formatter(fmt).form(x);
    }
    public static final String sprintf(String fmt, double x) {
        return new Formatter(fmt).form(x);
    }
    public static final String sprintf(String fmt, Double x) {
        return new Formatter(fmt).form(x);
    }
    public static final String sprintf(String fmt, Float x) {
        return new Formatter(fmt).form(x);
    }
    public static final String sprintf(String fmt, long x) {
        return new Formatter(fmt).form(x);
    }
    public static final String sprintf(String fmt, Long x) {
        return new Formatter(fmt).form(x);
    }
    public static final String sprintf(String fmt, int x) {
        return new Formatter(fmt).form(x);
    }
    public static final String sprintf(String fmt, Integer x) {
        return new Formatter(fmt).form(x);
    }
    public static final String sprintf(String fmt, String x) {
        return new Formatter(fmt).form(x);
    }
    public static final String sprintf(String s, Object[] params) {
        if ((s == null) || (params == null)) {
            return s;
        }
        final StringBuffer result = new StringBuffer("");
        final String[] ss = Formatter.split(s);
        int p = 0;
        for (int i = 0; i < ss.length; i++) {
            final char c = ss[i].charAt(0);
            final String t = ss[i].substring(1);
            if (c == '+') {
                result.append(t);
            } else {
                final Object param = params[p];
                if (param instanceof Integer) {
                    result.append(new Formatter(t).form((Integer) param));
                } else if (param instanceof Long) {
                    result.append(new Formatter(t).form((Long) param));
                } else if (param instanceof Character) {
                    result.append(new Formatter(t).form((Character) param));
                } else if (param instanceof Double) {
                    result.append(new Formatter(t).form((Double) param));
                } else if (param instanceof Float) {
                    result.append(new Formatter(t).form((Float) param));
                } else {
                    result.append(new Formatter(t).form(param.toString()));
                }
                p++;
            }
        }
        return result.toString();
    }
    public static final String sprintf(String s, String[] params) {
        return sprintf(s, (Object[]) params);
    }
    public static final String sprintf(String s, Integer[] params) {
        return sprintf(s, (Object[]) params);
    }
    public static final String sprintf(String s, Long[] params) {
        return sprintf(s, (Object[]) params);
    }
    public static final String sprintf(String s, Double[] params) {
        return sprintf(s, (Object[]) params);
    }
    public static final String sprintf(String s, Float[] params) {
        return sprintf(s, (Object[]) params);
    }
    public static final String sprintf(String s, Character[] params) {
        return sprintf(s, (Object[]) params);
    }



    // ------------------------------------------------------------------ char[] ops
    public static final char[] trimToSize(final char[] value, final int newSize) {
        if (newSize < value.length) {
            char[] result = new char[newSize];
            System.arraycopy(value, 0, result, 0, newSize);
            return result;
        } else {
            return value;
        }//if
    }//trimToSize

    public static final int indexOf(char[] x, char b) {
        return indexOf(x, b, 0);
    }//indexOf

    public static final int indexOf(char[] x, char b, int startOffset) {
        int n = x.length;
        for (int i = startOffset; i < n; i++) {
            if (x[i] == b) return i;
        }//for
        return -1;
    }//indexOf

    public static final int lastIndexOf(char toBeFound, char[] array) {
        if (array==null) return -1;
        for (int i = array.length; --i >= 0;)
            if (toBeFound == array[i]) return i;
        return -1;
    }//lastIndexOf

    public static final int countOcurrences(char[] src, char c) {
        int result = 0;
        final int total = src.length;
        for (int i = 0; i < total; i++)
            if (src[i]==c) result++;
        return result;
    }//countOcurrences

    public static final char[] newCharArray(int size, char[] init) {
        char[] array = new char[size];
        if (init != null) {
            System.arraycopy(init, 0, array, 0, Math.min(size, init.length));
        }//if
        return array;
    }//newCharArray

    public static final String toString(char[] chars) {
        return (chars == null) ? null : new String(chars);
    }//toString

    public static char[] subarray(char[] src, int start, int length) {
        if (src == null) return null;
        int srcLength = src.length;
        if (start < 0 || start >= srcLength) return null;
        if (length < 0 || start + length > srcLength) return null;
        if (srcLength == length && start == 0) return src;

        char[] result = new char[length];
        if (length > 0) System.arraycopy(src, start, result, 0, length);
        return result;
    }//subarray

    public static final char[] concat(char[] x, char[] y) {
        if (x == null) return y;
        if (y == null) return x;
        char[] z = new char[x.length + y.length];
        System.arraycopy(x, 0, z, 0, x.length);
        System.arraycopy(y, 0, z, x.length, y.length);
        return z;
    }//concat

    public static final char[] concat(char[] x, char[] y, int start, int length) {
        if (y == null) return x;
        if (x == null) return subarray(y,start,length);
        int srcLength = y.length;
        if (start < 0 || start >= srcLength) return null;
        if (length < 0 || start + length > srcLength) return null;
        char[] z = new char[x.length + length];
        System.arraycopy(x, 0, z, 0, x.length);
        System.arraycopy(y, start, z, x.length, length);
        return z;
    }//concat

    public static final char[] append(char[] array, char suffix) {
		if (array == null) return new char[] { suffix };
		int length = array.length;
		System.arraycopy(array, 0, array = new char[length + 1], 0, length);
		array[length] = suffix;
		return array;
	}//append

    public static final boolean contains(char character, char[] array) {
        if (array==null) return false;
		for (int i = array.length; --i >= 0;)
			if (array[i] == character) return true;
		return false;
	}//contains

    public static final boolean endsWith(char[] array, char[] toBeFound) {
        if ( (array==null) || (toBeFound==null) ) return false;
        int i = toBeFound.length;
        int j = array.length - i;

        if (j < 0) return false;
        while (--i >= 0)
            if (toBeFound[i] != array[i + j])
                return false;
        return true;
    }//endsWith

    public static final boolean equals(char[] first, char[] second) {
        if (first == second) return true;
        if (first == null || second == null) return false;
        if (first.length != second.length) return false;
        for (int i = 0, length = first.length; i < length; i++)
            if (first[i] != second[i]) return false;
        return true;
    }//equals

    public static final boolean equalsIgnoreCase(char[] first, char[] second) {
        if (first == second) return true;
        if (first == null || second == null) return false;
        if (first.length != second.length) return false;

        for (int i = first.length; --i >= 0;)
            if (Character.toLowerCase(first[i]) != Character.toLowerCase(second[i]))
                return false;
        return true;
    }//equalsIgnoreCase

    public static final int hashCode(char[] array) {
        if (array==null) return 0;
		int hash = 0;
		int offset = 0;
		int length = array.length;
		if (length < 16) {
			for (int i = length; i > 0; i--)
				hash = (hash * 37) + array[offset++];
		} else {
			int skip = length / 8;
			for (int i = length; i > 0; i -= skip, offset += skip)
				hash = (hash * 39) + array[offset];
		}
		return hash & 0x7FFFFFFF;
	}//hashcode

	public static final boolean startsWith(char[] array, char[] toBeFound) {
        if ((array==null) || (toBeFound==null) ) return false;
		int i = toBeFound.length;
		if (i > array.length) return false;
		while (--i >= 0)
			if (toBeFound[i] != array[i]) return false;
		return true;
	}//startsWith

    public static final boolean startsWithIgnoreCase(char[] array, char[] toBeFound) {
        if ((array==null) || (toBeFound==null) ) return false;
        int i = toBeFound.length;
        if (i > array.length) return false;
        while (--i >= 0)
            if (Character.toLowerCase(toBeFound[i]) != Character.toLowerCase(array[i])) return false;
        return true;
    }//startsWithIgnoreCase

    public static final void replace(char[] array, char toBeReplaced, char replacementChar) {
		if ( (array!=null) && (toBeReplaced != replacementChar) )
			for (int i = 0, max = array.length; i < max; i++)
				if (array[i] == toBeReplaced) array[i] = replacementChar;
	}//replace

    /* sin depurar */
    public static final char[] delete(char[] array, int from, int to) {
        if ( (array==null) || (from<0) || (to<0) ||
                (from>to) || (from>array.length) || (to>array.length) )
            return null;
        char[] result = new char[array.length-(to-from)];
        System.arraycopy(array, 0, result, 0, from);
        System.arraycopy(array, to, result, from, array.length);
        return result;
    }//delete

    public static final char[][] splitOn(int divider, char[] array) {
		int length = (array == null) ? 0 : array.length;
		if (length == 0) return new char[0][];
        if ( (divider<0) || (divider>=length) ) {
            char[][] result = new char[1][];
            result[0] = array;
            return result;
        }//if

		char[][] split = new char[2][];
		int last = 0, currentWord = 0;
		for (int i = 0; i < length; i++) {
			if (i == divider) {
				split[currentWord] = new char[i - last];
				System.arraycopy(array, last, split[currentWord++], 0, i - last);
				last = i + 1;
			}//if
		}//for
		split[currentWord] = new char[length - last];
		System.arraycopy(array, last, split[currentWord], 0, length - last);
		return split;
	}//splitOn

    /* sin depurar */
    public static final char[] insert(char[] array, int index, char[] toBeInserted, int offset, int len) {
        if (array==null) return null;
        if ( (toBeInserted==null) || (toBeInserted.length==0) ) return array;
        if ( (index<0) || (index>array.length) ||
             (offset<0) || (offset>=toBeInserted.length) ||
             (len<0) || (offset+len>=toBeInserted.length) ) throw new IndexOutOfBoundsException();
        if (len==0) return array;
        char[] result = new char[array.length+len];
        System.arraycopy(array, 0, result, 0, index);
        System.arraycopy(toBeInserted, index, result, offset, len);
        System.arraycopy(array,index+len,result,index,(array.length-index));        
        return result;
        
    }//insert


  public static String unescape(String s) {
    StringBuffer sbuf = new StringBuffer () ;
    int l  = s.length() ;
    int ch = -1 ;
    int b, sumb = 0;
    for (int i = 0, more = -1 ; i < l ; i++) {
      /* Get next byte b from URL segment s */
      switch (ch = s.charAt(i)) {
	case '%':
	  ch = s.charAt (++i) ;
	  int hb = (Character.isDigit ((char) ch)
		    ? ch - '0'
		    : 10+Character.toLowerCase((char) ch) - 'a') & 0xF ;
	  ch = s.charAt (++i) ;
	  int lb = (Character.isDigit ((char) ch)
		    ? ch - '0'
		    : 10+Character.toLowerCase ((char) ch)-'a') & 0xF ;
	  b = (hb << 4) | lb ;
	  break ;
	case '+':
	  b = ' ' ;
	  break ;
	default:
	  b = ch ;
      }
      /* Decode byte b as UTF-8, sumb collects incomplete chars */
      if ((b & 0xc0) == 0x80) {			// 10xxxxxx (continuation byte)
	sumb = (sumb << 6) | (b & 0x3f) ;	// Add 6 bits to sumb
	if (--more == 0) sbuf.append((char) sumb) ; // Add char to sbuf
      } else if ((b & 0x80) == 0x00) {		// 0xxxxxxx (yields 7 bits)
	sbuf.append((char) b) ;			// Store in sbuf
      } else if ((b & 0xe0) == 0xc0) {		// 110xxxxx (yields 5 bits)
	sumb = b & 0x1f;
	more = 1;				// Expect 1 more byte
      } else if ((b & 0xf0) == 0xe0) {		// 1110xxxx (yields 4 bits)
	sumb = b & 0x0f;
	more = 2;				// Expect 2 more bytes
      } else if ((b & 0xf8) == 0xf0) {		// 11110xxx (yields 3 bits)
	sumb = b & 0x07;
	more = 3;				// Expect 3 more bytes
      } else if ((b & 0xfc) == 0xf8) {		// 111110xx (yields 2 bits)
	sumb = b & 0x03;
	more = 4;				// Expect 4 more bytes
      } else /*if ((b & 0xfe) == 0xfc)*/ {	// 1111110x (yields 1 bit)
	sumb = b & 0x01;
	more = 5;				// Expect 5 more bytes
      }
      /* We don't test if the UTF-8 encoding is well-formed */
    }
    return sbuf.toString() ;
  }

  
  
  public static boolean stringNoNuloNoVacio(String dato){
      boolean exito = false;
      if(dato!=null && !"".equals(dato)){
          exito = true;
      }
      return exito;
  }

  
 

}//class

