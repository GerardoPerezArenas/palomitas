package es.altia.flexia.sir.model;

import es.altia.util.StringUtils;

/**
 * Obligatoriedad de la documentacion que acompaña un {@link Asiento}. Los valores posibles son:
 * <ul>
 * <li>"1" = Es obligatorio que el mensaje vaya acompañado de documentacion fisica.</li>
 * <li>"2" = El mensaje va a compañado de documentacion física complementaria no obligatoria.</li>
 * <li>"3" = No hay documentacion fisica.</li>
 * </ul>
 */
public enum TipoDocumentacionFisica {

	COMPLEMENTARIA("2"),
	NINGUNA("3"),
	REQUERIDA("1");

	private final String valor;

	private TipoDocumentacionFisica(String valor) {
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
	 * @return TipoDocumentacionFisica de valor correspondiente.
	 */
	public static TipoDocumentacionFisica getEnum(String valor) {
		if (StringUtils.isNotNullOrEmpty(valor)) {
			for (TipoDocumentacionFisica tipo : TipoDocumentacionFisica.values()) {
				if (tipo.getValor().equals(valor)) {
					return tipo;
				}
			}
		}
		return null;
	}

}
