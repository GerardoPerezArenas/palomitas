package es.altia.flexia.sir.model;

import es.altia.util.StringUtils;

/**
 * Representa los valores posibles que puede tomar un tipo de documento de identificación según la norma SICRES 3.0.
 */
public enum TipoDocumentoIdentificacion {

	CIF("C"),
	CODIGO_ORIGEN("O"),
	DOCUMENTO_EXTRANJEROS("E"),
	NIF("N"),
	PASAPORTE("P"),
	OTROS("X");

	private final String valor;

	TipoDocumentoIdentificacion(String valor) {
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
	 * @return TipoDocumentoIdentificacion de valor correspondiente.
	 */
	public static TipoDocumentoIdentificacion getEnum(String valor) {
		if (StringUtils.isNotNullOrEmpty(valor)) {
			for (TipoDocumentoIdentificacion tipo : TipoDocumentoIdentificacion.values()) {
				if (tipo.getValor().equals(valor)) {
					return tipo;
				}
			}
		}
		return null;
	}

}
