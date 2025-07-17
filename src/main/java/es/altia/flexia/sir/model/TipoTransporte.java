package es.altia.flexia.sir.model;

import es.altia.util.StringUtils;

/**
 * Tipo de transporte de entrada del {@link Asiento}. Los valores posibles son:
 * <ul>
 * <li>"01" = Servicio de mensajeros.</li>
 * <li>"02" = Correo postal.</li>
 * <li>"03" = Correo postal certificado.</li>
 * <li>"04" = Burofax.</li>
 * <li>"05" = En mano.</li>
 * <li>"06" = Fax.</li>
 * <li>"07" = Otros.</li>
 * </ul>
 */
public enum TipoTransporte {

	BUROFAX("04"),
	CORREO_POSTAL("02"),
	CORREO_POSTAL_CERTIFICADO("03"),
	EN_MANO("05"),
	FAX("06"),
	OTROS("07"),
	SERVICIO_DE_MENSAJEROS("01");

	private final String valor;

	private TipoTransporte(String valor) {
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
	 * @return TipoTransporte de valor correspondiente.
	 */
	public static TipoTransporte getEnum(String valor) {
		if (StringUtils.isNotNullOrEmpty(valor)) {
			for (TipoTransporte tipo : TipoTransporte.values()) {
				if (tipo.getValor().equals(valor)) {
					return tipo;
				}
			}
		}
		return null;
	}

}
