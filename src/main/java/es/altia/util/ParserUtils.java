package es.altia.util;

/**
 * 
 */
public class ParserUtils {

	/**
	 * Parsea un String a un int, devolviendo el valor predeterminado en caso de que la cadena sea null o vacia.
	 *
	 * @param valor
	 * @param predeterminado
	 * @return
	 */
	public static int parsear(String valor, int predeterminado) {
		return (StringUtils.isNotNullOrEmpty(valor)) ? Integer.parseInt(valor) : predeterminado;
	}

	/**
	 * Parsea un String a un char, devolviendo el valor predeterminado en caso de que la cadena sea null o vacia.
	 *
	 * @param valor
	 * @param predeterminado
	 * @return
	 */
	public static char parsear(String valor, char predeterminado) {
		return (StringUtils.isNotNullOrEmpty(valor) && valor.length() == 1) ? valor.charAt(0) : predeterminado;
	}

	/**
	 * Devuelve el valor de la clase wrapper si wrapper no es null y defecto en caso de que si lo sea.
	 * 
	 * @param wrapper Objeto de la clase Integer.
	 * @param defecto Valor que toma por defecto si wrapper es null.
	 * @return 
	 */
	public static int wrapperAPrimitivo(Integer wrapper, int defecto) {
		return (wrapper != null) ? wrapper : defecto;
	}
	
}
