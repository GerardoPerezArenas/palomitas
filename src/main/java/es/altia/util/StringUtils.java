package es.altia.util;

/**
 *
 * @author Óscar Rodríguez
 */
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static int MAX_LENGTH = 9000;
    protected static Map stringCharMappings = new HashMap(4);

    private static char[][] getMappings(String encoding)
    {
        char[][] stringChars = (char[][]) stringCharMappings.get(encoding);

        if (stringChars == null)
        {
            stringChars = new char[MAX_LENGTH][];
            if ("UTF-8".equalsIgnoreCase(encoding) ||
                    "Big5".equalsIgnoreCase(encoding) ||
                    "Windows-1252".equalsIgnoreCase(encoding))
            {
                // FIXME: These characters are valid in utf-8
                addMapping(8216, "'", stringChars);
                addMapping(8217, "'", stringChars);
                addMapping(8220, "\"", stringChars);
                addMapping(8221, "\"", stringChars);
                addMapping(8230, "...", stringChars);
                addMapping(8211, "-", stringChars);
                addMapping(183, "- ", stringChars); // replace bullets
            } else if ("ISO-8859-1".equalsIgnoreCase(encoding))
            {
                addMapping(145, "'", stringChars);
                addMapping(146, "'", stringChars);
                addMapping(147, "\"", stringChars);
                addMapping(148, "\"", stringChars);
                addMapping(133, "...", stringChars);
                addMapping(150, "-", stringChars);
                addMapping(183, "- ", stringChars); // replace bullets
            }
            // unicode control characters should be chopped off
            for (int i = 0; i < 32; i++)
            {
                if (i == 9 || i == 10 || i == 13)
                {
                    continue; // 9, 10, 13 are line feed and carriage return chars
                } else
                {
                    addMapping(i, "", stringChars);
                }
            }

            stringCharMappings.put(encoding, stringChars);
        }

        return stringChars;
    }

    private static void addMapping(int charsNumericValue, String replaceStr, char[][] mappings)
    {
        mappings[charsNumericValue] = replaceStr.toCharArray();
    }

	/**
	 * Devuelve true si la cadena no es nula ni vacía.
	 *
	 * @param string
	 * @return
	 */
	public static boolean isNotNullOrEmpty(String string) {
		return string != null && string.length() > 0;
	}

	/**
	 * Devuelve true si la cadena no es nula ni vacía ni "null".
	 *
	 * @param string
	 * @return
	 */
	public static boolean isNotNullOrEmptyOrNullString(String string) {
		return isNotNullOrEmpty(string) && !string.equalsIgnoreCase("null");
	}
	
	/**
	 * Devuelve true si la cadena es nula o vacía.
	 *
	 * @param string
	 * @return
	 */
	public static boolean isNullOrEmpty(String string) {
		return !isNotNullOrEmpty(string);
	}
	
	/**
	 * Devuelve true si la cadena es nula o vacía o "null".
	 *
	 * @param string
	 * @return
	 */
	public static boolean isNullOrEmptyOrNullString(String string) {
		return !isNotNullOrEmptyOrNullString(string);
	}

	/**
	 * Concatena un número de Strings ignorando aquellas que sean null.
	 * 
	 * @param strings
	 * @return 
	 */
	public static String concatenarStrings(String... strings) {
		StringBuilder sb = new StringBuilder();
		
		for (String string : strings) {
			if (isNotNullOrEmptyOrNullString(string)) {
				sb.append(string);
			}
		}
		
		return sb.toString();
	}
	
    /**
    * replaces "smart quotes" and other problematic characters that appear in JIRA when data is cut and pasted
    * from a Microsoft word document. <p>
    * These include smart single and double quotes, ellipses, em-dashes and bullets
    * (these characters belong to the Windows Code Page 1252 encoding)
    *
    * @param s string to simplify
    * @param encoding eg. UTF-8, Big5, ISO-8859-1 etc.
    * @return
    */
    public static final String escapeCP1252(String s, String encoding)
    {
        if (s == null)
            return null;

        int len = s.length();
        if (len == 0)
            return s;

        // if extended empty string just return it
        String trimmed = s.trim();
        if (trimmed.length() == 0 || ("\"\"").equals(trimmed))
            return trimmed;

        // initialise the Mapping before encoding
        char[][] stringChars = getMappings(encoding);

        int i = 0;
        // First loop through String and check if escaping is needed at all
        // No buffers are copied at this time
        do
        {
            int index = s.charAt(i);
            if (index >= MAX_LENGTH)
                continue;
            if (stringChars[index] != null)
            {
                break;
            }
        }
        while (++i < len);

        // If the check went to the end with no escaping then i should be == len now
        // otherwise we must continue escaping for real
        if (i == len)
        {
            return s;
        }

        // We found a character to escape and broke out at position i
        // Now copy all characters before that to StringBuffer sb
        // Since a char[] will be used for copying we might as well get
        // a complete copy of it so that we can use array indexing instead of charAt
        StringBuffer sb = new StringBuffer(len + 40);
        char[] chars = new char[len];
        // Copy all chars from the String s to the chars buffer
        s.getChars(0, len, chars, 0);
        // Append the first i characters that we have checked to the resulting StringBuffer
        sb.append(chars, 0, i);
        int last = i;
        char[] subst = null;
        for (; i < len; i++)
        {
            char c = chars[i];
            int index = c;

            if (index < stringChars.length)
                subst = stringChars[index];
            else
                subst = null;

            // It is faster to append a char[] than a String which is why we use this
            if (subst != null)
            {
                if (i > last)
                    sb.append(chars, last, i - last);
                sb.append(subst);
                last = i + 1;
            }
        }
        if (i > last)
        {
            sb.append(chars, last, i - last);
        }
        return sb.toString();
    }
	
	/**
	 * Metodo que determina si un determinado string termina con una determinada cadena de caracteres
	 * @param str
	 * @param suffix
	 * @return 
	 */
	public static boolean endsWith(String str, String suffix) {
		// Metodo copiado del metodo del mismo nombre del paquete org.apache.commons.lang.StringUtils (commons-lang-2.4.jar)
		// La llamada al metodo de la librería falla en Weblogic
		if ((str == null) || (suffix == null)) {
			return (str == null) && (suffix == null);
		}
		if (suffix.length() > str.length()) {
		  return false;
		}
		int strOffset = str.length() - suffix.length();
		return str.regionMatches(strOffset, suffix, 0, suffix.length());
	 }
        
        /**
         * Metodo que busca por cadena caracteres Unicodes y lso sustituye por caracteres normales
         * @param cadenaUnicode
         * @return 
         */
        public static String convertirUnicodeAString (String cadenaUnicode) {
        
        if (cadenaUnicode == null || "".equals(cadenaUnicode.trim())) {
            return "";
        }
        
        String unicodeRegex = "\\\\u([0-9a-fA-F]{4})";
        Pattern unicodePattern = Pattern.compile(unicodeRegex);
        
        //Se busca si existen caracteres unicodes en el literal
        Matcher matcher = unicodePattern.matcher(cadenaUnicode);
        StringBuffer decodedMessage = new StringBuffer();
        while (matcher.find()) {
            //Se sustitute el valor unicode por el caracter normal
          matcher.appendReplacement(
              decodedMessage, String.valueOf((char) Integer.parseInt(matcher.group(1), 16)));
        }
        matcher.appendTail(decodedMessage);
        
        return decodedMessage.toString();
        
    }
}
