package es.altia.flexia.sir.model;

import es.altia.util.StringUtils;

/**
 * Canal preferente de contacto para un {@link Interesado}. Los valores posibles son:
 * <ul>
 * <li>"01" = Direccion postal.</li>
 * <li>"02" = Direccion electronica habilitada.</li>
 * <li>"03" = Comparecencia electronica.</li>
 * </ul>
 */
public enum CanalPreferente {

	DIRECCION_POSTAL("01"),
	DIRECCION_ELECTRONICA_HABILITADA("02"),
	COMPARECENCIA_ELECTRONICA("03");

	private final String valor;

	private CanalPreferente(String valor) {
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
	 * @return CanalPreferente de valor correspondiente.
	 */
	public static CanalPreferente getEnum(String valor) {
		if (StringUtils.isNotNullOrEmpty(valor)) {
			for (CanalPreferente tipo : CanalPreferente.values()) {
				if (tipo.getValor().equals(valor)) {
					return tipo;
				}
			}
		}
		return null;
	}

}
