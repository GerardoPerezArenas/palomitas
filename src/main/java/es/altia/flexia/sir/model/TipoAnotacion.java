package es.altia.flexia.sir.model;

import es.altia.util.StringUtils;

/**
 * Codigo que indica el motivo de la anotacion. Los valores posibles son:
 * <ul>
 * <li>"01" = pendiente (sin identificador de intercambio).</li>
 * <li>"02" = Envio.</li>
 * <li>"03" = Reenvio.</li>
 * <li>"04" = Rechazo.</li>
 * </ul>
 */
public enum TipoAnotacion {

	ENVIO("02"),
	PENDIENTE("01"),
	RECHAZO("04"),
	REENVIO("03");

	private final String valor;

	private TipoAnotacion(String valor) {
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
	 * @return TipoAnotacion de valor correspondiente.
	 */
	public static TipoAnotacion getEnum(String valor) {
		if (StringUtils.isNotNullOrEmpty(valor)) {
			for (TipoAnotacion tipo : TipoAnotacion.values()) {
				if (tipo.getValor().equals(valor)) {
					return tipo;
				}
			}
		}
		return null;
	}

}
