package es.altia.flexia.sir.model;

import es.altia.util.StringUtils;

/**
 * Tipo de registro del {@link Asiento}. Los valores posibles son:
 * <ul>
 * <li>"0" = Registro de entrada.</li>
 * <li>"1" = Registro de salida.</li>
 * </ul>
 */
public enum TipoRegistro {

	ENTRADA("0"),
	SALIDA("1");

	private final String valor;

	private TipoRegistro(String valor) {
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
	 * @return TipoRegistro de valor correspondiente.
	 */
	public static TipoRegistro getEnum(String valor) {
		if (StringUtils.isNotNullOrEmpty(valor)) {
			for (TipoRegistro tipo : TipoRegistro.values()) {
				if (tipo.getValor().equals(valor)) {
					return tipo;
				}
			}
		}
		return null;
	}

}
