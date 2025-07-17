package es.altia.flexia.sir.model;

import es.altia.util.StringUtils;

/**
 * Tipo de documento de un {@link Anexo}. Los valores posibles son:
 * <ul>
 * <li>"01" = Formulario.</li>
 * <li>"02" = Documento adjunto al formulario.</li>
 * <li>"03" = Fichero tecnico interno.</li>
 * </ul>
 */
public enum TipoDocumentoAnexo {

	ADJUNTO_AL_FORMULARIO("02"),
	FICHERO_TECNICO_INTERNO("03"),
	FORMULARIO("01");

	private final String valor;

	private TipoDocumentoAnexo(String valor) {
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
	 * @return TipoDocumentoAnexo de valor correspondiente.
	 */
	public static TipoDocumentoAnexo getEnum(String valor) {
		if (StringUtils.isNotNullOrEmpty(valor)) {
			for (TipoDocumentoAnexo tipo : TipoDocumentoAnexo.values()) {
				if (tipo.getValor().equals(valor)) {
					return tipo;
				}
			}
		}
		return null;
	}

}
