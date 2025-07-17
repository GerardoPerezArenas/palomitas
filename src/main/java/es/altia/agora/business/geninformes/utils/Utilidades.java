package es.altia.agora.business.geninformes.utils;

//import javax.crypto.Cipher;
//import javax.crypto.SecretKey;
//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.PBEKeySpec;
//import javax.crypto.spec.PBEParameterSpec;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Utilidades {
    protected static Log m_Log = LogFactory.getLog(Utilidades.class.getName()); // Para información de logs

    public Utilidades() {
    }
//	public static long insertar(PreparedStatement ps, String secuencia)
//		throws Exception {
//
//		long resultado = 0;
//
//		try {
//			//es.altia.util.Debug.println("Voy a por la conexion para hacer la select de NextVal");
//			java.sql.Connection con = ps.getConnection();
//			//es.altia.util.Debug.println("Tengo la conexion para hacer la select de NextVal");
//			PreparedStatement psSec =
//				con.prepareStatement(
//					" select " + secuencia + ".nextval from dual");
//			//es.altia.util.Debug.println("Tengo la sentencia preparada para "+secuencia);
//			java.sql.ResultSet rs = psSec.executeQuery();
//			//es.altia.util.Debug.println("Ejecutada "+secuencia);
//			if (rs.next()) {
//				resultado = rs.getLong(1);
//				//es.altia.util.Debug.println("Obtenido "+resultado);
//			}
//
//			if(m_Log.isDebugEnabled()){
//                m_Log.debug("Utilidades:insertar:secuencia : " + secuencia);
//                m_Log.debug("Utilidades:insertar:resultado : " + resultado);
//            }
//            ps.setLong(1, resultado);
//			//es.altia.util.Debug.println("Voya insertar+++++++ ");
//			ps.execute();
//			//es.altia.util.Debug.println("Acabado !!!!! ");
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//            m_Log.error(e.getMessage());
//            if (e.getErrorCode() == 1)
//				throw e;
//			else if (e.getErrorCode() == 2291)
//				throw e;
//			else
//				throw e;
//
//		}
//		return resultado;
//
//	}

	/**
	 * Method ReplaceCadena
	 *
	 * @param    String cadenaVieja cadenaNueva
	 *
	 * @return   el String modificado
	 *
	 * @throws   Exception
	 *
	 */
	public static String ReplaceCadena(
		String Cadena,
		String CadenaAntigua,
		String CadenaNueva) {
		//es.altia.util.Debug.println("La cadena antes de reemplazar "+Cadena);
		String CadenaDevolver = "";
		String Cad = "";
		java.util.StringTokenizer cadtoken =
			new java.util.StringTokenizer(Cadena, CadenaAntigua);

		while (cadtoken.hasMoreTokens()) {

			Cad = cadtoken.nextToken();
			if (cadtoken.hasMoreElements()) {
				CadenaDevolver = CadenaDevolver + Cad + CadenaNueva;
			} else {
				CadenaDevolver = CadenaDevolver + Cad;
			}
		}
		return CadenaDevolver;
	}

	/**
	 * Method ConvertirCursorToArrayJSEscaped aplica js_escape a todos los strings antes de convertir
	 *
	 * @param    cursor              a  Cursor
	 *
	 * @return   a String
	 *
	 * @throws   Exception
	 *
	 */
	public static String ConvertirCursorToArrayJSEscaped(
		es.altia.util.conexion.Cursor cursor)
		throws Exception {
		String sentencia = "[";

		int cont = 1;
		if (cursor != null) {
			while (cursor.next()) {

				sentencia += "[";
				for (int j = 1; j <= cursor.numColumnas(); j++) {
					if (j == 1)
						sentencia
							+= es.altia.util.conexion.Cursor.class.isInstance(
								cursor.getObject(j))
							? ConvertirCursorToArrayJSEscaped(
								(
									es
										.altia
										.util
										.conexion
										.Cursor) cursor
										.getObject(
									j))
							: "'" + js_escape(cursor.getString(j)) + "'";
					else
						sentencia
							+= es.altia.util.conexion.Cursor.class.isInstance(
								cursor.getObject(j))
							? ","
								+ ConvertirCursorToArrayJSEscaped(
									(
										es
											.altia
											.util
											.conexion
											.Cursor) cursor
											.getObject(
										j))
							: ",'" + js_escape(cursor.getString(j)) + "'";
				}

				if (cont == cursor.numTuplas())
					sentencia += "]";
				else
					sentencia += "],";
				cont++;
			}
			cursor.Indice = -1;
		} else {
			//Devolvera []
		}
		return (sentencia + "]");

	}

	/**
	 * Method ConvertirCursorToArrayJSEscaped aplica js_escape a todos los strings antes de convertir
	 *
	 * @param    cursor              a  Cursor
	 * @param    fila                an int
	 *
	 * @return   a String
	 *
	 * @throws   Exception
	 *
	 */
	public static String ConvertirCursorToArrayJSEscaped(
		es.altia.util.conexion.Cursor cursor,
		int fila)
		throws Exception {
		String sentencia = "[";

		int cont = 0;
		while (cursor.next()) {

			if (cont == 0)
				sentencia
					+= es.altia.util.conexion.Cursor.class.isInstance(
						cursor.getObject(fila))
					? ConvertirCursorToArrayJSEscaped(
						(es.altia.util.conexion.Cursor) cursor.getObject(fila))
					: "'" + js_escape(cursor.getString(fila)) + "'";
			else
				sentencia
					+= es.altia.util.conexion.Cursor.class.isInstance(
						cursor.getObject(fila))
					? ","
						+ ConvertirCursorToArrayJSEscaped(
							(es.altia.util.conexion.Cursor) cursor.getObject(
								fila))
					: ",'" + js_escape(cursor.getString(fila)) + "'";
			cont++;
		}

		cursor.Indice = -1;

		return (sentencia + "]");
	}

	/**
	 * Method ConvertirCursorToArrayJSComboEscaped  aplica js_escape a todos los strings antes de convertir
	 *
	 * @param    cursor              a  Cursor
	 * @param    arrayValues         an int[]
	 * @param    filaText            an int
	 *
	 * @return   a String
	 *
	 * @throws   Exception
	 *
	 */
	public static String ConvertirCursorToArrayJSComboEscaped(
		es.altia.util.conexion.Cursor cursor,
		int arrayValues[],
		int filaText)
		throws Exception {
		String sentencia = "[";

		int cont = 0;
		while (cursor.next()) {

			String elemento = "";
			for (int i = 0; i < arrayValues.length; i++) {
				if (i == 0)
					elemento += cursor.getString(arrayValues[i]);
				else
					elemento += "-" + cursor.getString(arrayValues[i]);
			}

			if (cont == 0) {
				sentencia += "['" + js_escape(elemento) + "'";
				sentencia += ",'"
					+ js_escape(cursor.getString(filaText))
					+ "']";

			} else {
				sentencia += ",['" + js_escape(elemento) + "'";
				sentencia += ",'"
					+ js_escape(cursor.getString(filaText))
					+ "']";
			}

			cont++;
		}

		cursor.Indice = -1;

		return (sentencia + "]");
	}

	/**
	 * Method ConvertirCursorToArrayJSEscaped aplica js_escape a todos los strings antes de convertir
	 *
	 * @param    cursor              a  Cursor
	 * @param    fila                a  String
	 *
	 * @return   a String
	 *
	 * @throws   Exception
	 *
	 */
	public static String ConvertirCursorToArrayJSEscaped(
		es.altia.util.conexion.Cursor cursor,
		String fila)
		throws Exception {
		String sentencia = "[";

		int cont = 0;
		while (cursor.next()) {

			if (cont == 0)
				sentencia
					+= es.altia.util.conexion.Cursor.class.isInstance(
						cursor.getObject(fila))
					? ConvertirCursorToArrayJSEscaped(
						(es.altia.util.conexion.Cursor) cursor.getObject(fila))
					: "'" + js_escape(cursor.getString(fila)) + "'";
			else
				sentencia
					+= es.altia.util.conexion.Cursor.class.isInstance(
						cursor.getObject(fila))
					? ","
						+ ConvertirCursorToArrayJSEscaped(
							(es.altia.util.conexion.Cursor) cursor.getObject(
								fila))
					: ",'" + js_escape(cursor.getString(fila)) + "'";
			cont++;
		}

		cursor.Indice = -1;

		return (sentencia + "]");
	}

	/**
	 * Method ConvertirVectorToArrayJSEscaped aplica js_escape a todos los strings antes de convertir
	 *
	 * @param    vector              a  Vector
	 *
	 * @return   a String
	 *
	 * @throws   Exception
	 *
	 */
	public static String ConvertirVectorToArrayJSEscaped(
		java.util.Vector vector)
		throws Exception {
		String sentencia = "[";

		int i = 0;
		if (vector == null)
			return ("[]");
		while (i < vector.size()) {
			if (i == 0)
				sentencia += "'" + js_escape(vector.elementAt(i) + "") + "'";
			else
				sentencia += ",'" + js_escape(vector.elementAt(i) + "") + "'";
			i++;
		}

		return (sentencia + "]");
	}

	/**
	 * Method ConvertirCursorToArrayJS
	 *
	 * @param    cursor              a  Cursor
	 *
	 * @return   a String
	 *
	 * @throws   Exception
	 *
	 */
	public static String ConvertirCursorToArrayJS(
		es.altia.util.conexion.Cursor cursor)
		throws Exception {
		String sentencia = "[";

		int cont = 1;
		//		if (cursor != null)
		//			es.altia.util.Debug.println(
		//				"En ConvCurstoArrJS curs es:" + cursor.hash + ".");
		//		else
		//			es.altia.util.Debug.println("En ConvCurstoArrJS curs es null.");
		while (cursor.next()) {

			sentencia += "[";
			for (int j = 1; j <= cursor.numColumnas(); j++) {
				if (j == 1)
					sentencia
						+= es.altia.util.conexion.Cursor.class.isInstance(
							cursor.getObject(j))
						? ConvertirCursorToArrayJS(
							(es.altia.util.conexion.Cursor) cursor.getObject(
								j))
						: "'"
							+ ReplaceCadena(cursor.getString(j), "'", "\\'")
							+ "'";
				else
					sentencia
						+= es.altia.util.conexion.Cursor.class.isInstance(
							cursor.getObject(j))
						? ","
							+ ConvertirCursorToArrayJS(
								(
									es
										.altia
										.util
										.conexion
										.Cursor) cursor
										.getObject(
									j))
						: ",'"
							+ ReplaceCadena(cursor.getString(j), "'", "\\'")
							+ "'";
			}

			if (cont == cursor.numTuplas())
				sentencia += "]";
			else
				sentencia += "],";
			cont++;
		}
		//es.altia.util.Debug.println("sentencia:" + sentencia + ".");

		return (sentencia + "]");
	}

	/**
	 * Method ConvertirCursorToArrayJS
	 *
	 * @param    cursor              a  Cursor
	 * @param    fila                an int
	 *
	 * @return   a String
	 *
	 * @throws   Exception
	 *
	 */
	public static String ConvertirCursorToArrayJS(
		es.altia.util.conexion.Cursor cursor,
		int fila)
		throws Exception {
		String sentencia = "[";

		int cont = 0;
		while (cursor.next()) {

			if (cont == 0)
				sentencia
					+= es.altia.util.conexion.Cursor.class.isInstance(
						cursor.getObject(fila))
					? ConvertirCursorToArrayJS(
						(es.altia.util.conexion.Cursor) cursor.getObject(fila))
					: "'"
						+ ReplaceCadena(cursor.getString(fila), "'", "\\'")
						+ "'";
			else
				sentencia
					+= es.altia.util.conexion.Cursor.class.isInstance(
						cursor.getObject(fila))
					? ","
						+ ConvertirCursorToArrayJS(
							(es.altia.util.conexion.Cursor) cursor.getObject(
								fila))
					: ",'"
						+ ReplaceCadena(cursor.getString(fila), "'", "\\'")
						+ "'";
			cont++;
		}

		cursor.Indice = -1;

		return (sentencia + "]");
	}

	/**
	 * Method ConvertirCursorToArrayJSCombo
	 * @param    cursor              a  Cursor
	 * @param    array campos value
	 * @param    fila text
	 * @return   a String
	 * @throws   Exception
	 */
	public static String ConvertirCursorToArrayJSCombo(
		es.altia.util.conexion.Cursor cursor,
		int arrayValues[],
		int filaText)
		throws Exception {
		String sentencia = "[";

		int cont = 0;
		while (cursor.next()) {

			String elemento = "";
			for (int i = 0; i < arrayValues.length; i++) {
				if (i == 0)
					elemento += cursor.getString(arrayValues[i]);
				else
					elemento += "-" + cursor.getString(arrayValues[i]);
			}

			if (cont == 0) {
				sentencia += "['" + ReplaceCadena(elemento, "'", "\\'") + "'";
				sentencia += ",'"
					+ ReplaceCadena(cursor.getString(filaText), "'", "\\'")
					+ "']";

			} else {
				sentencia += ",['" + ReplaceCadena(elemento, "'", "\\'") + "'";
				sentencia += ",'"
					+ ReplaceCadena(cursor.getString(filaText), "'", "\\'")
					+ "']";
			}

			cont++;
		}

		cursor.Indice = -1;

		return (sentencia + "]");
	}

	/*********************************************************************************/
	public static String ConvertirCursorToArrayJS(
		es.altia.util.conexion.Cursor cursor,
		String fila)
		throws Exception {
		String sentencia = "[";

		int cont = 0;
		while (cursor.next()) {

			if (cont == 0)
				sentencia
					+= es.altia.util.conexion.Cursor.class.isInstance(
						cursor.getObject(fila))
					? ConvertirCursorToArrayJS(
						(es.altia.util.conexion.Cursor) cursor.getObject(fila))
					: "'"
						+ ReplaceCadena(cursor.getString(fila), "'", "\\'")
						+ "'";
			else
				sentencia
					+= es.altia.util.conexion.Cursor.class.isInstance(
						cursor.getObject(fila))
					? ","
						+ ConvertirCursorToArrayJS(
							(es.altia.util.conexion.Cursor) cursor.getObject(
								fila))
					: ",'"
						+ ReplaceCadena(cursor.getString(fila), "'", "\\'")
						+ "'";
			cont++;
		}

		cursor.Indice = -1;

		return (sentencia + "]");
	}

	/****************************************************************************/
	public static String ConvertirVectorToArrayJS(java.util.Vector vector)
		throws Exception {
		String sentencia = "[";

		int i = 0;
		if (vector == null)
			return ("[]");
		while (i < vector.size()) {
			if (i == 0)
				sentencia += "'"
					+ ReplaceCadena(vector.elementAt(i) + "", "'", "\'")
					+ "'";
			else
				sentencia += ",'"
					+ ReplaceCadena(vector.elementAt(i) + "", "'", "\'")
					+ "'";
			i++;
		}

		return (sentencia + "]");
	}

	/****************************************************************************/
	public static String FormateaStringBusqueda(String StringBusqueda)
		throws Exception {

		String StringFormateado = null;
		if (StringBusqueda != null) {
			StringFormateado = StringBusqueda.trim();
			StringFormateado = StringFormateado.replace('*', '%');
		}
		return StringFormateado;
	}

	/**
	 * Method ConvertirHashtableToArrayJS
	 *
	 * @param    tabla           a  Hashtable
	 *
	 * @return   a String contiene el código javascript necesario para evaluarlo y crear un array asociativo
	 * 				en javascript con el nombre que se le pasa. Los claves con valor null no lo pone.
	 *			Despues en javascript hay que hacer un eval(cadena) donde cadena es el String que
	 *			devuelve este método y quedará definido un array asociativo de nombre nombre.
	 *
	 *
	 * @throws   Exception
	 *
	 *
	 *		NOTA: Comprueba que los valores sean Integer, String o Long antes de meterlos en el array,
	 *			  evita meter otras clases de objetos, como Cursores o Collection's
	 *
	 */
	public static String ConvertirHashtableToArrayJS(
		java.util.Hashtable tabla,
		String nombre)
		throws Exception {
		String clave = null;
		String valor = null;
		String salida = "var " + nombre + "=new Array(); ";
		java.util.Enumeration claves = tabla.keys();

		while (claves.hasMoreElements()) {
			clave = (String) claves.nextElement();
			Object valorA = tabla.get(clave);
			if ((java.lang.String.class.isInstance(valorA))
				|| (java.lang.Long.class.isInstance(valorA))
				|| (java.lang.Integer.class.isInstance(valorA))
				|| (java.lang.Double.class.isInstance(valorA))
				|| (java.lang.Float.class.isInstance(valorA))) {
				valor = valorA.toString();
				salida += " " + nombre + "['" + clave + "']='" + valor + "'; ";
				//es.altia.util.Debug.println("En Utilidades.ConvertirHashtableToArrayJS entro en if y voy metiendo: clave:"+clave+".Valor:"+valor+".");
			}

		}

		return salida;
	}

	/**
	    * The global method escape, as per ECMA-262 15.1.2.4.
	
	    * Includes code for the 'mask' argument supported by the C escape
	    * method, which used to be part of the browser imbedding.  Blame
	    * for the strange constant names should be directed there.
	    */

	public static String js_escape(String entrada) {
		final int URL_XALPHAS = 1, URL_XPALPHAS = 2, URL_PATH = 4;

		String s = entrada;

		int mask = URL_XALPHAS | URL_XPALPHAS | URL_PATH;

		StringBuffer R = new StringBuffer();
		for (int k = 0; k < s.length(); k++) {
			int c = s.charAt(k), d;
			if (mask != 0
				&& ((c >= '0' && c <= '9')
					|| (c >= 'A' && c <= 'Z')
					|| (c >= 'a' && c <= 'z')
					|| c == '@'
					|| c == '*'
					|| c == '_'
					|| c == '-'
					|| c == '.'
					|| ((c == '/' || c == '+') && mask > 3)))
				R.append((char) c);
			else if (c < 256) {
				if (c == ' ' && mask == URL_XPALPHAS) {
					R.append('+');
				} else {
					R.append('%');
					R.append(hex_digit_to_char(c >>> 4));
					R.append(hex_digit_to_char(c & 0xF));
				}
			} else {
				R.append('%');
				R.append('u');
				R.append(hex_digit_to_char(c >>> 12));
				R.append(hex_digit_to_char((c & 0xF00) >>> 8));
				R.append(hex_digit_to_char((c & 0xF0) >>> 4));
				R.append(hex_digit_to_char(c & 0xF));
			}
		}
		return R.toString();
	}

	/**
	 * The global unescape method, as per ECMA-262 15.1.2.5.
	 */

	public static String js_unescape(String entrada) {
		String s = entrada;
		int firstEscapePos = s.indexOf('%');
		if (firstEscapePos >= 0) {
			int L = s.length();
			char[] buf = s.toCharArray();
			int destination = firstEscapePos;
			for (int k = firstEscapePos; k != L;) {
				char c = buf[k];
				++k;
				if (c == '%' && k != L) {
					int end, start;
					if (buf[k] == 'u') {
						start = k + 1;
						end = k + 5;
					} else {
						start = k;
						end = k + 2;
					}
					if (end <= L) {
						int x = 0;
						for (int i = start; i != end; ++i) {
							x = (x << 4) | xDigitToInt(buf[i]);
						}
						if (x >= 0) {
							c = (char) x;
							k = end;
						}
					}
				}
				buf[destination] = c;
				++destination;
			}
			s = new String(buf, 0, destination);
		}
		return s;
	}

	/**
	 * Method hex_digit_to_char
	 *
	 * @param    x                   an int
	 *
	 * @return   a char
	 *
	 */
	private static char hex_digit_to_char(int x) {
		return (char) (x <= 9 ? x + '0' : x + ('A' - 10));
	}
	/**
	 * Method xDigitToInt
	 *
	 * @param    c                   an int
	 *
	 * @return   an int
	 *
	 */
	private static int xDigitToInt(int c) {
		if ('0' <= c && c <= '9') {
			return c - '0';
		}
		if ('a' <= c && c <= 'f') {
			return c - ('a' - 10);
		}
		if ('A' <= c && c <= 'F') {
			return c - ('A' - 10);
		}
		return -1;
	}

	/****************************************************************************/
	/* Hace el merge de dos hashtables                                          */
	/****************************************************************************/

	public static es.altia.util.HashtableWithNull MergeHashTable(
		es.altia.util.HashtableWithNull hash1,
		es.altia.util.HashtableWithNull hash2)
		throws Exception {
		es.altia.util.HashtableWithNull Devuelve =
			new es.altia.util.HashtableWithNull();
		if (hash1 == null)
			Devuelve = hash2;
		else if (hash2 == null)
			Devuelve = hash1;
		else {
			Devuelve = (es.altia.util.HashtableWithNull) hash1.clone();
			java.util.Enumeration claves = hash2.keys();
			for (; claves.hasMoreElements();) {
				String campo = claves.nextElement().toString();
				Devuelve.put(campo, hash2.get(campo));
			}
		}
		return Devuelve;

	}

	/**
	 * Method recortaString. Devuelve un String formado por los <code>limite</code>
	 * primeros caracteres de <code>texto</code> si <code>texto.length>limite</code>
	 * sino devuelve <code>texto</code>
	 * @param texto String a recortar
	 * @param limite 
	 * @return String
	 */
	public static String recortaString(String texto, int limite) {
		String salida = null;
		if ((texto != null) && (limite > 0)) {
			salida =
				(texto.length() > limite)
					? texto.substring(0, limite - 1)
					: texto;
		}
		return salida;
	}

	/*********************************************************************
	*****
	*****
	**********************************************************************/

        
         /**
         * Comprueba si una determinada fecha contenida en un Calendar es menor estricta que la fecha del instante actual
         * @param fecha: Calendar
         * @return Un boolean
         */
        public static boolean isDateGreaterOrEqualThanActual(Calendar fecha){
            boolean exito = false;
                       
            Calendar actual = Calendar.getInstance();
            
            SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");          
            if(fecha.getTimeInMillis()>=actual.getTimeInMillis())
                exito = true;
            
            return exito;
        }
        
        
        /**
         * Comprueba si una cadena de caracteres contiene un int
         * @param cadena
         * @return
         */
        public static boolean isInteger(String cadena)
        {
            boolean exito = false;
            try{
                Integer.parseInt(cadena);
                exito = true;
            }catch(NumberFormatException e){                
            }
                    
            return exito;
        }
        
        /**
         * Comprueba si una cadena de caracteres contiene un long
         * @param cadena
         * @return
         */
        public static boolean isLong(String cadena)
        {
            boolean exito = false;
            try{
                Long.parseLong(cadena);
                exito = true;
            }catch(NumberFormatException e){                
            }
                    
            return exito;
        }
                
}
