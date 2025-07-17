package es.altia.flexia.sir.model;

import es.altia.util.StringUtils;


/**
 * Tipo de una {@link RespuestaAsiento}.
 */
public enum TipoRespuesta {

	RECHAZO("RECHAZO"),
	CONFIRMACION("CONFIRMACION");

	private final String valor;

	private TipoRespuesta(String valor) {
		this.valor = valor;
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
	 * @return TipoRespuesta de valor correspondiente.
	 */
	public static TipoRespuesta getEnum(String valor) {
		if (StringUtils.isNotNullOrEmpty(valor)) {
			if (valor.equals(RECHAZO.valor)) {
				return RECHAZO;
			}
			if (valor.equals(CONFIRMACION.valor)) {
				return CONFIRMACION;
			}
		}
		return null;
	}

}
