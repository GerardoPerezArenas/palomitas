package es.altia.flexia.sir.model;

import es.altia.util.StringUtils;

/**
 * Categoria de autenticidad de un {@link Anexo}. Los valores posibles son:
 * <ul>
 * <li>"01" = Copia.</li>
 * <li>"02" = Copia comuplsada.</li>
 * <li>"03" = Copia original.</li>
 * <li>"03" = Original.</li>
 * </ul>
 */
public enum ValidezDocumento {

	COPIA("01"),
	COPIA_COMPULSADA("02"),
	COPIA_ORIGINAL("03"),
	ORIGINAL("04");

	private final String valor;

	private ValidezDocumento(String valor) {
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
	 * @return ValidezDocumento de valor correspondiente.
	 */
	public static ValidezDocumento getEnum(String valor) {
		if (StringUtils.isNotNullOrEmpty(valor)) {
			for (ValidezDocumento tipo : ValidezDocumento.values()) {
				if (tipo.getValor().equals(valor)) {
					return tipo;
				}
			}
		}
		return null;
	}

}
