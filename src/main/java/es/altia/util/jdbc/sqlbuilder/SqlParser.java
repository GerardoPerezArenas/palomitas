package es.altia.util.jdbc.sqlbuilder;

import java.math.BigDecimal;

/**
 * Clase que contiene metodos de parseo segun como estan ciertos valores en base de datos a clases utiles para Flexia.
 * Esta clase pretende adaptarse a las necesidades particulares de la base de datos de Flexia, no a convenciones
 * genericas, por lo que podra llevar convenciones propias a valores de otras clases que habitualmente no permitirian
 * que la clase fuera generica.
 */
public class SqlParser {

	/**
	 * Parsea, según el tipo de objeto, el parámetro recibido a entero.
	 *
	 * @param numero
	 * @return
	 */
	public static Integer parsearAInt(Object numero) {
		if (numero != null) {
			if (numero instanceof Integer) {
				return (Integer) numero;
			}
			if (numero instanceof BigDecimal) {
				return ((BigDecimal) numero).intValue();
			}
			if (numero instanceof Long) {
				return ((Long) numero).intValue();
			}
			if (numero instanceof Double) {
				return ((Double) numero).intValue();
			}
			throw new IllegalArgumentException(String.format(
					"El parseo de %s a Integer no esta contemplado o esta mal implementado",
					numero.getClass().getName()));
		}

		return null;
	}

	/**
	 * Parsea, segun el tipo de objeto, el parametro recibido a entero.
	 *
	 * @param bool
	 * @return
	 */
	public static Boolean parsearABoolean(Object bool) {
		if (bool != null) {
			if (bool instanceof Boolean) {
				return (Boolean) bool;
			}
			if (bool instanceof Integer) {
				return ((Integer) bool) > 0;
			}
			if (bool instanceof String) {
				String s = (String) bool;
				if (s.length() > 0 && (s.equalsIgnoreCase("s") || (s.equalsIgnoreCase("si")) || Boolean.valueOf(s))) {
					return true;
				}
				if (s.length() == 0 || (s.equalsIgnoreCase("n") || (s.equalsIgnoreCase("no") || !Boolean.valueOf(s)))) {
					return false;
				}
			}
		}

		return null;
	}
}
