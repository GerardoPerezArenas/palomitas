package es.altia.util.jdbc.sqlbuilder;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.sir.persistence.GestionSirManager;
import java.util.ResourceBundle;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Gestor de BBDD.
 */
public enum GestorBaseDatos {
	ORACLE		("ORACLE"),
	SQL_SERVER	("SQLSERVER");
	
	private static final Logger LOGGER = Logger.getLogger(GestionSirManager.class);
	private final String valor;
	
	private GestorBaseDatos(String valor) {
		this.valor = valor;
	}
	
	/**
	 * @return Gestor que se conecta a la BBDD definido en el archivo techserver.properties.
	 */
	public static GestorBaseDatos getGestorBaseDatos() {
		ResourceBundle bundle = ResourceBundle.getBundle("techserver");
		String gestor = bundle.getString("CON.gestor");
		
		if (gestor.equalsIgnoreCase(ConstantesDatos.ORACLE)) {
			return ORACLE;
		}
		if (gestor.equalsIgnoreCase(ConstantesDatos.SQLSERVER)) {
			return SQL_SERVER;
		}
		// Por defecto
		LOGGER.info("No se ha detectado una configuracion de gestor de Base de Datos, utilizando ORACLE por defecto.");
		return ORACLE;
	}
	
	/**
	 * Recupera el valor interno del enumerado.
	 *
	 * @return Valor interno.
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * Recupera un valor del enumerado a partir de su valor interno.
	 *
	 * @param valor Valor para el cual se quiere recuperar el valor del enumerado correspondiente.
	 * @return GestorBaseDatos de valor correspondiente.
	 */
	public static GestorBaseDatos getEnum(String valor) {
		if (StringUtils.isNotEmpty(valor)) {
			for (GestorBaseDatos tipo : GestorBaseDatos.values()) {
				if (tipo.getValor().equalsIgnoreCase(valor)) {
					return tipo;
				}
			}
		}
		return null;
	}

}
