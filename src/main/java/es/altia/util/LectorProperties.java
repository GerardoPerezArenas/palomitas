package es.altia.util;

import java.util.ResourceBundle;
import java.text.MessageFormat;

/**
 * Objeto que se relaciona con un archivo properties para leer, segun nombre e idioma escogidos para leer comodamente 
 * del mismo. Ademas tambien renderizara los mensajes en dichos properties con sustitucion de parametros marcados con 
 * "{}".
 */
public class LectorProperties {
	
	ResourceBundle rb;
	/**
	 * Se crea un lector que lee del fichero rutaFichero.properties.
	 * 
	 * @param rutaFichero Ruta relativa a partir de {$basedir}/src/java/resources. Es decir, si la ruta fuera 
	 * "fichero", el archivo a leer seria {$basedir}/src/java/resources/fichero_xx_XX.properties, siendo xx_XX el locale
	 * correspondiente al codIdioma.
	 * @param codIdioma No puede ser null. Se utilizara para recuperar el codigo del locale "xx_XX" a utilizar del 
	 * archivo {$basedir}/src/java/resources/i18n/codigosidioma.properties.
	 */
	public LectorProperties(String rutaFichero, int codIdioma) {
		// Se recupera el sufijo (es, gl, eu) del properties de los codigos de idioma
		String sufijoProperties = ResourceBundle.getBundle("resources/i18n/codigosidioma").getString(String.valueOf(codIdioma));
		// Se asocia el archivo al ResourceBundle de la instancia.
		rb = ResourceBundle.getBundle(String.format("resources/%s_%s", rutaFichero, sufijoProperties));
	}
	
	/**
	 * Recupera una cadena del archivo mensajes_xx_XX.properties especificado en el momento de creacion del objeto.
	 *
	 * @param clave Palabra que se buscara en el archivo mensajes_xx_XX.properties .
	 * @param args Parametros a mostrar en la cadena del que hay en el archivo properties, sustituyendo los "{}" por
	 * el valor del parametro.
	 * @return Mensaje recuperado del archivo properties.
	 */
	public String getMensaje(String clave, Object... args) {
		String mensaje = this.rb.getString(clave);
		return new MessageFormat(mensaje).format(args);
	}

	/**
	 * Metodo de conveniencia para llamadas sin argumentos.
	 *
	 * @param clave
	 * @return Mensaje recuperado del archivo properties.
	 */
	public String getMensaje(String clave) {
		return getMensaje(clave, (Object[]) null);
	}
}
